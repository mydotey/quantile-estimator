using System;
using System.Collections.Generic;

using NLog;

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
 * Implementation of the Greenwald and Khanna algorithm for streaming
 * calculation of error-approximate quantiles.
 * 
 * See: Greenwald and Khanna,
 * "Space-efficient online computation of quantile summaries" in SIGMOD 2001
 * 
 */
namespace MyDotey.Quantile.Gk
{
    class QuantileEstimationGK<T>
    {
        private static Logger logger = LogManager.GetCurrentClassLogger();

        private IComparer<T> comparer;

        // Acceptable % error in percentile estimate
        private double error;

        // Threshold to trigger a compaction
        private int compactThreshold;

        // Total number of items in stream
        internal int count = 0;

        protected internal List<Item> samples = new List<Item>();

        public QuantileEstimationGK(IComparer<T> comparer, double error, int compactThreshold)
        {
            this.comparer = comparer;
            this.error = error;
            this.compactThreshold = compactThreshold;
        }

        public void Insert(T v)
        {
            DefensiveReset();

            int idx = 0;

            foreach (Item i in samples)
            {
                if (comparer.Compare(i.value, v) > 0)
                {
                    break;
                }
                idx++;
            }

            int delta;
            if (idx == 0 || idx == samples.Count)
            {
                delta = 0;
            }
            else
            {
                delta = (int)Math.Floor(2 * error * count);
            }

            Item newItem = new Item(v, 1, delta);
            samples.Insert(idx, newItem);

            if (samples.Count > compactThreshold)
                Compress();

            count++;
        }

        protected void Compress()
        {
            for (int i = 0; i < samples.Count - 1; i++)
            {
                Item item = samples[i];
                Item item1 = samples[i + 1];

                // Merge the items together if we don't need it to maintain the
                // error bound
                if (item.g + item1.g + item1.delta <= Math.Floor(2 * error * count))
                {
                    item1.g += item.g;
                    samples.RemoveAt(i);
                }
            }
        }

        public T Query(double quantile)
        {
            if (samples.Count == 0)
                return default(T);

            int rankMin = 0;
            int desired = (int)(quantile * count);

            for (int i = 1; i < samples.Count; i++)
            {
                Item prev = samples[i - 1];
                Item cur = samples[i];

                rankMin += prev.g;

                if (rankMin + cur.g + cur.delta > desired + (2 * error * count))
                {
                    return prev.value;
                }
            }

            // edge case of wanting max value
            return samples[samples.Count - 1].value;
        }

        protected internal bool IsEmpty { get { return samples.Count == 0; } }

        protected void DefensiveReset()
        {
            if (count == int.MaxValue)
            {
                logger.Warn("count reached int.max, reset and prevent overflow");

                count = 0;
                samples.Clear();
            }
        }

        protected internal class Item
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
    }
}