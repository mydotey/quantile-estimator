using System;
using System.Collections.Generic;
using System.Collections.Concurrent;

/**
 * @author koqizhao
 *
 * Apr 2, 2018
 */
namespace MyDotey.Quantile.Ckms
{
    public class CkmsQuantileEstimator<T> : IQuantileEstimator<T>
    {
        private CkmsQuantileEstimatorConfig<T> _config;
        private CKMSQuantiles<T> _ckmsQuantiles;

        public CkmsQuantileEstimator(CkmsQuantileEstimatorConfig<T> config)
        {
            if (config == null)
                throw new ArgumentNullException("config is null");

            _config = config;

            List<CKMSQuantiles<T>.Quantile> quantiles = new List<CKMSQuantiles<T>.Quantile>();
            _config.QuantileConfigs.ForEach(qc =>
            {
                CKMSQuantiles<T>.Quantile quantile = new CKMSQuantiles<T>.Quantile(qc.Quantile, qc.Error);
                quantiles.Add(quantile);
            });
            _ckmsQuantiles = new CKMSQuantiles<T>(quantiles, _config.Comparer);
        }

        public virtual void Add(T value)
        {
            _ckmsQuantiles.Insert(value);
        }

        public virtual Dictionary<Double, T> Get(List<Double> quantiles)
        {
            Dictionary<Double, T> results = null;
            foreach (Double quantile in quantiles)
            {
                T result = _ckmsQuantiles.Get(quantile);
                if (_ckmsQuantiles.IsEmpty)
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