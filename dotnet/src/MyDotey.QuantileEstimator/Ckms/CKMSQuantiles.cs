using System;
using System.Collections.Generic;

// Copied from https://raw.githubusercontent.com/Netflix/ocelli/master/ocelli-core/src/main/java/netflix/ocelli/stats/CKMSQuantiles.java
// Revision d0357b8bf5c17a173ce94d6b26823775b3f999f6 from Jan 21, 2015.
//
// This is the original code except for the following modifications:
//
//  - Changed the type of the observed values from int to double.
//  - Removed the Quantiles interface and corresponding @Override annotations.
//  - Changed the package name.
//  - Make Get() return NaN when no sample was observed.
//  - Make class package private

/*
 Copyright 2012 Andrew Wang (andrew@umbrant.com)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

/*
   Editor: koqizhao
   Changes:
       1. remove synchronization 
       2. make it generic
       3. C# implementation
*/

/**
 * Implementation of the Cormode, Korn, Muthukrishnan, and Srivastava algorithm
 * for streaming calculation of tarGeted high-percentile epsilon-approximate
 * quantiles.
 * 
 * This is a generalization of the earlier work by Greenwald and Khanna (GK),
 * which essentially allows different error bounds on the tarGeted quantiles,
 * which allows for far more efficient calculation of high-percentiles.
 * 
 * 
 * See: Cormode, Korn, Muthukrishnan, and Srivastava
 * "Effective Computation of Biased Quantiles over Data Streams" in ICDE 2005
 * 
 * Greenwald and Khanna,
 * "Space-efficient online computation of quantile summaries" in SIGMOD 2001
 * 
 */
namespace MyDotey.Quantile.Ckms
{
    class CKMSQuantiles<T>
    {
        /**
         * Total number of items in stream.
         */
        private int count = 0;

        /**
         * Current list of sampled items, maintained in sorted order with error
         * bounds.
         */
        protected LinkedList<Item> samples;

        /**
         * Buffers incoming items to be Inserted in batch.
         */
        private int bufferSize;
        private List<T> buffer;

        /**
         * Array of Quantiles that we care about, along with desired error.
         */
        private List<Quantile> quantiles;

        private IComparer<T> comparer;

        public CKMSQuantiles(List<Quantile> quantiles, IComparer<T> comparer)
            : this(quantiles, comparer, 500)
        {
        }

        public CKMSQuantiles(List<Quantile> quantiles, IComparer<T> comparer, int bufferSize)
        {
            this.quantiles = quantiles;
            this.samples = new LinkedList<Item>();
            this.comparer = comparer;
            this.bufferSize = bufferSize;
            this.buffer = new List<T>(bufferSize);
        }

        /**
         * Add a new value from the stream.
         * 
         * @param value
         */
        public virtual void Insert(T value)
        {
            buffer.Add(value);

            if (buffer.Count == bufferSize)
            {
                InsertBatch();
                Compress();
            }
        }

        /**
         * Get the estimated value at the specified quantile.
         * 
         * @param q
         *            Queried quantile, e.g. 0.50 or 0.99.
         * @return Estimated value at that quantile.
         */
        public virtual T Get(double q)
        {
            // clear the buffer
            InsertBatch();
            Compress();

            if (samples.Count == 0)
                return default(T);

            int rankMin = 0;
            int desired = (int)(q * count);

            IEnumerator<Item> it = samples.GetEnumerator();
            it.MoveNext();
            Item prev, cur;
            cur = it.Current;
            while (it.MoveNext())
            {
                prev = cur;
                cur = it.Current;

                rankMin += prev.g;

                if (rankMin + cur.g + cur.delta > desired + (allowableError(desired) / 2))
                {
                    return prev.value;
                }
            }

            // edge case of wanting max value
            return samples.Last.Value.value;
        }

        protected internal Boolean IsEmpty { get { return samples.Count == 0; } }

        /**
         * Specifies the allowable error for this rank, depending on which quantiles
         * are being tarGeted.
         * 
         * This is the f(r_i, n) function from the CKMS paper. It's basically how
         * wide the range of this rank can be.
         * 
         * @param rank
         *            the index in the list of samples
         */
        private double allowableError(int rank)
        {
            // NOTE: according to CKMS, this should be count, not size, but this
            // leads
            // to error larger than the error bounds. Leaving it like this is
            // essentially a HACK, and blows up memory, but does "work".
            // int size = count;
            int size = samples.Count;
            double minError = size + 1;

            foreach (Quantile q in quantiles)
            {
                double error;
                if (rank <= q.quantile * size)
                {
                    error = q.u * (size - rank);
                }
                else
                {
                    error = q.v * rank;
                }
                if (error < minError)
                {
                    minError = error;
                }
            }

            return minError;
        }

        private bool InsertBatch()
        {
            if (buffer.Count == 0)
            {
                return false;
            }

            buffer.Sort(comparer);

            // Base case: no samples
            int start = 0;
            if (samples.Count == 0)
            {
                Item newItem = new Item(buffer[0], 1, 0);
                samples.AddLast(newItem);
                start++;
                count++;
            }

            LinkedListNode<Item> node = samples.First;
            Item item = node.Value;

            for (int i = start; i < buffer.Count; i++)
            {
                T v = buffer[i];
                while (node.Next != null && comparer.Compare(item.value, v) < 0)
                {
                    node = node.Next;
                    item = node.Value;
                }

                // If we found that bigger item, back up so we Insert ourselves
                // before it
                if (comparer.Compare(item.value, v) > 0)
                {
                    node = node.Previous;
                }

                // We use different indexes for the edge comparisons, because of the
                // above
                // if statement that adjusts the iterator
                int delta;
                if (node == null || node == samples.Last)
                {
                    delta = 0;
                }
                else
                {
                    int nextIndex = 1;
                    LinkedListNode<Item> temp = samples.First;
                    while (temp != node)
                    {
                        nextIndex++;
                        temp = temp.Next;
                    }

                    delta = ((int)Math.Floor(allowableError(nextIndex))) - 1;
                }

                item = new Item(v, 1, delta);
                if (node == null)
                {
                    samples.AddFirst(item);
                    node = samples.First;
                }
                else
                {
                    samples.AddAfter(node, item);
                    node = node.Next;
                }

                count++;
            }

            buffer.Clear();
            return true;
        }

        /**
         * Try to remove extraneous items from the set of sampled items. This checks
         * if an item is unnecessary based on the desired error bounds, and merges
         * it with the adjacent item if it is.
         */
        private void Compress()
        {
            if (samples.Count < 2)
            {
                return;
            }

            LinkedListNode<Item> node = samples.First;

            Item prev = null;
            Item next = node.Value;

            while (node.Next != null)
            {
                prev = next;
                node = node.Next;
                next = node.Value;

                int previousIndex = 0;
                LinkedListNode<Item> temp = samples.First;
                while (temp.Next != node)
                {
                    previousIndex++;
                    temp = temp.Next;
                }
                if (prev.g + next.g + next.delta <= allowableError(previousIndex))
                {
                    next.g += prev.g;
                    // Remove prev. it.remove() kills the last thing returned.
                    samples.Remove(node.Previous);
                }
            }
        }

        public class Item
        {
            public T value;
            public int g;
            public int delta;

            public Item(T value, int lower_delta, int delta)
            {
                this.value = value;
                this.g = lower_delta;
                this.delta = delta;
            }

            public override String ToString()
            {
                return String.Format("{0}, {1}, {2}", value, g, delta);
            }
        }

        public class Quantile
        {
            public double quantile;
            public double error;
            public double u;
            public double v;

            public Quantile(double quantile, double error)
            {
                this.quantile = quantile;
                this.error = error;
                u = 2.0 * error / (1.0 - quantile);
                v = 2.0 * error / quantile;
            }

            public override String ToString()
            {
                return String.Format("Q{{q={0}, eps={1}}})", quantile, error);
            }
        }
    }
}