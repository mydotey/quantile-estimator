using System;
using System.Collections.Generic;
using System.Collections.Concurrent;
using System.Threading;

/**
 * @author koqizhao
 *
 * Apr 2, 2018
 */
namespace MyDotey.Quantile.Concurrent
{
    public class ConcurrencyDecorator<T> : IQuantileEstimator<T>
    {
        private IQuantileEstimator<T> _quantileEstimator;

        private ConcurrentQueue<T> _cache;
        private object _lock;

        public ConcurrencyDecorator(IQuantileEstimator<T> quantileEstimator)
        {
            if (quantileEstimator == null)
                throw new ArgumentNullException("quantileEstimator is null");

            _quantileEstimator = quantileEstimator;
            _cache = new ConcurrentQueue<T>();
            _lock = new object();
        }

        public virtual void Add(T value)
        {
            _cache.Enqueue(value);

            if (Monitor.TryEnter(_lock))
            {
                try
                {
                    BatchAdd();
                }
                finally
                {
                    Monitor.Exit(_lock);
                }
            }
        }

        public virtual Dictionary<Double, T> Get(List<Double> quantiles)
        {
            lock (_lock)
            {
                BatchAdd();
                return _quantileEstimator.Get(quantiles);
            }
        }

        protected virtual void BatchAdd()
        {
            for (bool success = _cache.TryDequeue(out T cached); success; success = _cache.TryDequeue(out cached))
                _quantileEstimator.Add(cached);
        }
    }
}