using System;
using System.Collections.Generic;

/**
 * @author koqizhao
 *
 * Mar 30, 2018
 */
namespace MyDotey.Quantile
{
    public interface IQuantileEstimator<T>
    {
        /**
        * @param value   null value will be ignored
        */
        void Add(T value);

        /**
        * @return quantile and value map
        *      if no value added into the estimator, null is returned
        *      otherwise, each quantile has value
        * @throws IllegalArgumentException
        *      if {@code quantiles} is {@code null}
        *      or if quantile scode is invalid (must be [0, 1])
        */
        Dictionary<Double, T> Get(List<Double> quantiles);
    }
}