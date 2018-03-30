package org.mydotey.quantileestimator;

/**
 * @author koqizhao
 *
 * Mar 30, 2018
 */
public interface QuantileEstimator<T extends Number> {

	void add(T value);

	T get(double quantile);

}
