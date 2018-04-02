package org.mydotey.quantileestimator;

import java.util.Map;
import java.util.List;

/**
 * @author koqizhao
 *
 * Mar 30, 2018
 */
public interface QuantileEstimator<T> {

    /**
     * @param value   null value will be ignored
     */
    void add(T value);

    /**
     * @return quantile and value map
     *      if no value added into the estimator, null is returned
     *      otherwise, each quantile has value
     * @throws IllegalArgumentException
     *      if {@code quantiles} is {@code null}
     *      or if quantile scode is valid (must be [0, 1])
     */
    Map<Double, T> get(List<Double> quantiles);

}
