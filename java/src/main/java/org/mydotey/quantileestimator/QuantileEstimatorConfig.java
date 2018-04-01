package org.mydotey.quantileestimator;

import java.util.Comparator;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public interface QuantileEstimatorConfig<T> {

    Comparator<T> getComparator();

    ValueCaculator<T> getValueCaculator();

}
