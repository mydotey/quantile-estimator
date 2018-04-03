using System;
using System.Collections.Generic;

using NLog;
using MyDotey.Quantile.Value;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
namespace MyDotey.Quantile.Classic
{
    public class ClassicQuantileEstimator<T> : IQuantileEstimator<T>
    {
        private static Logger _logger = LogManager.GetCurrentClassLogger();

        private ClassicQuantileEstimatorConfig<T> _config;

        private volatile List<T> _values;

        public ClassicQuantileEstimator(ClassicQuantileEstimatorConfig<T> config)
        {
            if (config == null)
                throw new ArgumentNullException("config is null");

            _config = config;

            _values = new List<T>();
        }

        public virtual void Add(T value)
        {
            _values.Add(value);
        }

        public virtual Dictionary<Double, T> Get(List<Double> quantiles)
        {
            if (_values.Count == 0)
                return null;

            _values.Sort(_config.Comparer);

            Dictionary<Double, T> results = new Dictionary<Double, T>();
            double n = _values.Count - 1;
            ICalculator<T> caculator = _config.Calculator;
            for (int i = 0; i < quantiles.Count; i++)
            {
                Double quantile = quantiles[i];
                double pos = quantile * n;
                int lowerPos = (int)pos;
                int upperPos = lowerPos + 1;
                T lower = _values[lowerPos];
                if (upperPos <= n)
                {
                    T upper = _values[upperPos];
                    T result = caculator.Add(lower, caculator.Multiply(caculator.Subtract(upper, lower), pos - lowerPos));
                    results[quantile] = result;
                }
                else
                    results[quantile] = lower;
            }

            return results;
        }

        protected virtual void DefensiveReset()
        {
            if (_values.Count == int.MaxValue)
            {
                _logger.Warn("count reached int.max, reset and prevent overflow");

                _values.Clear();
            }
        }
    }
}