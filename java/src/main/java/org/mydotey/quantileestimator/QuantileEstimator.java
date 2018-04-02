package org.mydotey.quantileestimator;

import java.util.Map;
import java.util.List;

/**
 * @author koqizhao
 *
 * Mar 30, 2018
 */
public interface QuantileEstimator<T> {

    void add(T value);

    Map<Double, T> get(List<Double> quantiles);

}
