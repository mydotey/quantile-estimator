using System;
using System.Collections.Generic;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
namespace MyDotey.Quantile.Ckms
{
    public class CkmsQuantileEstimatorConfig<T>
    {
        public virtual IComparer<T> Comparer { get; }
        public virtual List<QuantileConfig> QuantileConfigs { get; }

        protected CkmsQuantileEstimatorConfig(IComparer<T> comparer, List<QuantileConfig> quantileConfigs)
        {
            Comparer = comparer;
            QuantileConfigs = new List<QuantileConfig>(quantileConfigs);
        }

        public class QuantileConfig
        {
            public virtual double Quantile { get; }
            public virtual double Error { get; }

            protected internal QuantileConfig(double quantile, double error)
            {
                Quantile = quantile;
                Error = error;
            }
        }

        public static Builder NewBuilder()
        {
            return new Builder();
        }

        public class Builder
        {
            private IComparer<T> _comparer;
            private List<QuantileConfig> _quantileConfigs;

            protected internal Builder()
            {
                _quantileConfigs = new List<QuantileConfig>();
            }

            public virtual Builder SetComparer(IComparer<T> comparer)
            {
                _comparer = comparer;
                return this;
            }

            public virtual Builder AddQuantileConfig(double quantile, double error)
            {
                String format = "{0} {1} invalid: expected number between 0.0 and 1.0.";
                if (quantile < 0.0 || quantile > 1.0)
                    throw new ArgumentException(String.Format(format, "quantile", quantile));

                if (error < 0.0 || error > 1.0)
                    throw new ArgumentException(String.Format(format, "error", error));

                _quantileConfigs.Add(new QuantileConfig(quantile, error));
                return this;
            }

            public virtual CkmsQuantileEstimatorConfig<T> Build()
            {
                if (_comparer == null)
                    throw new ArgumentNullException("comparer is not set");

                if (_quantileConfigs.Count == 0)
                    throw new ArgumentException("quantileConfig is not added");

                return new CkmsQuantileEstimatorConfig<T>(_comparer, _quantileConfigs);
            }
        }
    }
}