using System;
using System.Collections.Generic;

/**
 * @author koqizhao
 *
 * Apr 2, 2018
 */
namespace MyDotey.Quantile.Gk
{
    public class GkQuantileEstimator<T> : IQuantileEstimator<T>
    {
        private GkQuantileEstimatorConfig<T> _config;
        private QuantileEstimationGK<T> _quantileEstimationGK;

        public GkQuantileEstimator(GkQuantileEstimatorConfig<T> config)
        {
            if (config == null)
                throw new ArgumentNullException("config is null");

            _config = config;
            _quantileEstimationGK = new QuantileEstimationGK<T>(_config.Comparer, _config.Error,
                    _config.CompactThreshold);
        }

        public virtual void Add(T value)
        {
            _quantileEstimationGK.Insert(value);
        }

        public virtual Dictionary<Double, T> Get(List<Double> quantiles)
        {
            Dictionary<Double, T> results = null;
            foreach (Double quantile in quantiles)
            {
                T result = _quantileEstimationGK.Query(quantile);
                if (_quantileEstimationGK.IsEmpty)
                    return null;

                if (results == null)
                    results = new Dictionary<Double, T>();

                results[quantile] = result;
            }

            if (results == null)
                results = new Dictionary<Double, T>();

            return results;
        }
    }
}