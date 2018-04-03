using System;
using System.Collections.Generic;

/**
 * @author koqizhao
 *
 * Mar 30, 2018
 * 
 * see also: https://github.com/edoliberty/streaming-quantiles
 * 
 */
namespace MyDotey.Quantile.Kll
{
    public class KllQuantileEstimator<T> : IQuantileEstimator<T>
    {
        private KllQuantileEstimatorConfig<T> _config;

        private List<Compactor> _compactors;
        private int _h;
        private int _size;
        private int _maxSize;

        public KllQuantileEstimator(KllQuantileEstimatorConfig<T> config)
        {
            if (config == null)
                throw new ArgumentNullException("config is null");

            _config = config;

            _compactors = new List<Compactor>();

            Grow();
        }

        public virtual void Add(T value)
        {
            _compactors[0].Add(value);
            _size++;
            if (_size >= _maxSize)
                Compress();
        }

        public virtual Dictionary<Double, T> Get(List<Double> quantiles)
        {
            List<ItemAndQuantile> itemsAndQuantiles = Cdf();
            if (itemsAndQuantiles.Count == 0)
                return null;

            Dictionary<Double, T> results = new Dictionary<Double, T>();

            int i = 0, j = 0;
            while (i < quantiles.Count && j < itemsAndQuantiles.Count)
            {
                Double quantile = quantiles[i];
                ItemAndQuantile itemAndQuantile = itemsAndQuantiles[j];
                if (itemAndQuantile.quantile < quantile)
                {
                    j++;
                    continue;
                }

                results[quantile] = itemAndQuantile.item;
                i++;
            }

            if (i < quantiles.Count)
            {
                T result = itemsAndQuantiles[j - 1].item;
                for (; i < quantiles.Count; i++)
                {
                    Double quantile = quantiles[i];
                    results[quantile] = result;
                }
            }

            return results;
        }

        protected virtual void Grow()
        {
            _compactors.Add(new Compactor(this));
            _h = _compactors.Count;

            int maxSize = 0;
            for (int i = 0; i < _h; i++)
            {
                maxSize += Capacity(i);
            }
            _maxSize = maxSize;
        }

        protected virtual int Capacity(int height)
        {
            int depth = _h - 1 - height;
            return (int)Math.Ceiling(Math.Pow(_config.C, depth) * _config.K) + 1;
        }

        protected virtual void Compress()
        {
            for (int i = 0; i < _compactors.Count; i++)
            {
                if (_compactors[i].Count >= Capacity(i))
                {
                    if (i + 1 >= _h)
                        Grow();

                    _compactors[i + 1].AddRange(_compactors[i].compact());
                    int size = 0;
                    for (int j = 0; j < _compactors.Count; j++)
                        size += _compactors[j].Count;
                    _size = size;

                    break;
                }
            }
        }

        protected virtual List<ItemAndQuantile> Cdf()
        {
            List<ItemAndWeight> itemsAndWeights = new List<ItemAndWeight>();
            for (int i = 0; i < _compactors.Count; i++)
            {
                for (int j = 0; j < _compactors[i].Count; j++)
                {
                    itemsAndWeights.Add(new ItemAndWeight(_compactors[i][j], (int)Math.Pow(2, i)));
                }
            }

            int totWeight = 0;
            foreach (ItemAndWeight itemAndWeight in itemsAndWeights)
                totWeight += itemAndWeight.weight;

            itemsAndWeights.Sort();

            List<ItemAndQuantile> Cdf = new List<ItemAndQuantile>();
            int cumWeight = 0;
            foreach (ItemAndWeight itemAndWeight in itemsAndWeights)
            {
                cumWeight += itemAndWeight.weight;
                Cdf.Add(new ItemAndQuantile(itemAndWeight.item, (double)cumWeight / totWeight));
            }

            return Cdf;
        }

        protected virtual void Merge(KllQuantileEstimator<T> other)
        {
            while (_h < other._h)
                Grow();

            for (int i = 0; i < other._h; i++)
                _compactors[i].AddRange(other._compactors[i]);

            int size = 0;
            for (int i = 0; i < _compactors.Count; i++)
                size += _compactors[i].Count;
            _size = size;

            while (_size >= _maxSize)
                Compress();
        }

        protected virtual int Rank(T value)
        {
            int r = 0;
            for (int i = 0; i < _compactors.Count; i++)
            {
                for (int j = 0; j < _compactors[i].Count; j++)
                {
                    if (_config.Comparer.Compare(_compactors[i][j], value) <= 0)
                        r += (int)Math.Pow(2, i);
                }
            }

            return r;
        }

        protected virtual List<ItemAndWeight> Ranks()
        {
            List<ItemAndWeight> itemsAndWeights = new List<ItemAndWeight>();
            for (int i = 0; i < _compactors.Count; i++)
            {
                for (int j = 0; j < _compactors[i].Count; j++)
                {
                    itemsAndWeights.Add(new ItemAndWeight(_compactors[i][j], (int)Math.Pow(2, i)));
                }
            }

            itemsAndWeights.Sort();

            List<ItemAndWeight> ranksList = new List<ItemAndWeight>();
            int cumWeight = 0;
            foreach (ItemAndWeight itemAndWeight in itemsAndWeights)
            {
                cumWeight += itemAndWeight.weight;
                ranksList.Add(new ItemAndWeight(itemAndWeight.item, cumWeight));
            }

            return ranksList;
        }

        protected class Compactor : List<T>
        {
            private static readonly Random Random = new Random();

            private KllQuantileEstimator<T> _kllQuantileEstimator;

            public Compactor(KllQuantileEstimator<T> kllQuantileEstimator)
            {
                _kllQuantileEstimator = kllQuantileEstimator;
            }

            public virtual List<T> compact()
            {
                Sort(_kllQuantileEstimator._config.Comparer);

                List<T> result = new List<T>();
                if (Random.NextDouble() < 0.5)
                {
                    while (Count >= 2)
                    {
                        int index = Count - 1;
                        RemoveAt(index);
                        result.Add(this[index - 1]);
                        RemoveAt(index - 1);
                    }
                }
                else
                {
                    while (Count >= 2)
                    {
                        int index = Count - 1;
                        result.Add(this[index]);
                        RemoveAt(index);
                        RemoveAt(index - 1);
                    }
                }

                return result;
            }
        }

        protected class ItemAndWeight : IComparable<ItemAndWeight>
        {
            public T item;
            public int weight;

            private KllQuantileEstimator<T> _kllQuantileEstimator;

            public ItemAndWeight(KllQuantileEstimator<T> kllQuantileEstimator)
            {
                _kllQuantileEstimator = kllQuantileEstimator;
            }

            public ItemAndWeight(T item, int weight)
            {
                this.item = item;
                this.weight = weight;
            }

            public virtual int CompareTo(ItemAndWeight o)
            {
                int r = _kllQuantileEstimator._config.Comparer.Compare(item, o.item);
                if (r != 0)
                    return r;

                return weight > o.weight ? 1 : (weight == o.weight ? 0 : -1);
            }
        }

        protected class ItemAndQuantile
        {
            public T item;
            public double quantile;

            public ItemAndQuantile(T item, double quantile)
            {
                this.item = item;
                this.quantile = quantile;
            }

            public override String ToString()
            {
                return String.Format("{{ item: {0}, quantile: {1} }}", item, quantile);
            }
        }
    }
}