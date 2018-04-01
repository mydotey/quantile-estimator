package org.mydotey.quantileestimator.kll;

import org.mydotey.quantileestimator.QuantileEstimatorConfig;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public interface KllQuantileEstimatorConfig<T> extends QuantileEstimatorConfig<T> {

    double getK();

    double getC();

}
