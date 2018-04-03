using System;
using System.Collections.Generic;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
namespace MyDotey.Quantile.Validation
{
    public class ValidationDecorator<T> : IQuantileEstimator<T>
    {
        private IQuantileEstimator<T> _quantileEstimator;

        public ValidationDecorator(IQuantileEstimator<T> quantileEstimator)
        {
            if (quantileEstimator == null)
                throw new ArgumentNullException("quantileEstimator is null");

            _quantileEstimator = quantileEstimator;
        }

        public virtual void Add(T value)
        {
            if (value == null)
                return;

            _quantileEstimator.Add(value);
        }

        public virtual Dictionary<Double, T> Get(List<Double> quantiles)
        {
            if (quantiles == null)
                throw new ArgumentNullException("quantiles is null");

            if (quantiles.Count == 0)
                return new Dictionary<Double, T>();

            quantiles.ForEach(quantile =>
            {
                if (quantile < 0 || quantile > 1)
                    throw new ArgumentException("quantile is not valid: " + quantile);
            });

            return _quantileEstimator.Get(quantiles);
        }
    }
}