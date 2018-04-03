using System;
using System.Collections.Generic;
using System.Threading;

/**
 * @author koqizhao
 *
 * Apr 3, 2018
 */
namespace MyDotey.Quantile.TimeWindow
{
    public class TimeWindowQuantileEstimator<T> : IQuantileEstimator<T>
    {
        protected internal static long CurrentTimeMillis { get { return DateTime.Now.Ticks / TimeSpan.TicksPerMillisecond; } }

        private TimeWindowQuantileEstimatorConfig<T> _config;

        private List<IQuantileEstimator<T>> _buffer;
        private volatile int _currentBucket;
        private long _lastRotateTime;
        private object _lock;

        public TimeWindowQuantileEstimator(TimeWindowQuantileEstimatorConfig<T> config)
        {
            if (config == null)
                throw new ArgumentNullException("config is null");

            _config = config;

            _buffer = new List<IQuantileEstimator<T>>();
            int bucketCount = (int)(_config.WindowMillis / _config.RotateDurationMillis);
            for (int i = 0; i < bucketCount; i++)
                _buffer.Add(_config.QuantileEstimatorSupplier());

            _lock = new object();
        }

        public virtual void Add(T value)
        {
            TryRotate();

            for (int i = 0; i < _buffer.Count; i++)
                _buffer[i].Add(value);
        }

        public virtual Dictionary<Double, T> Get(List<Double> quantiles)
        {
            TryRotate();

            return GetQuantileEstimator().Get(quantiles);
        }

        protected virtual void TryRotate()
        {
            if (_lastRotateTime + _config.RotateDurationMillis > CurrentTimeMillis)
                return;

            if (!Monitor.TryEnter(_lock))
                return;

            try
            {
                int oldestBucket = (_currentBucket + _buffer.Count - 1) % _buffer.Count;
                _buffer[oldestBucket] = _config.QuantileEstimatorSupplier();

                _currentBucket = (_currentBucket + 1) % _buffer.Count;
                Interlocked.CompareExchange(ref _lastRotateTime, CurrentTimeMillis, _lastRotateTime);
            }
            finally
            {
                Monitor.Exit(_lock);
            }
        }

        protected virtual IQuantileEstimator<T> GetQuantileEstimator()
        {
            return _buffer[_currentBucket];
        }
    }
}