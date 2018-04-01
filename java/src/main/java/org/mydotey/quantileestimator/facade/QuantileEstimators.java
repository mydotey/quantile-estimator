package org.mydotey.quantileestimator.facade;

import java.util.Comparator;

import org.mydotey.quantileestimator.DefaultQuantileEstimatorConfig;
import org.mydotey.quantileestimator.QuantileEstimator;
import org.mydotey.quantileestimator.QuantileEstimatorConfig;
import org.mydotey.quantileestimator.ValueCaculator;
import org.mydotey.quantileestimator.basic.BasicQuantileEstimator;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class QuantileEstimators {

    protected QuantileEstimators() {

    }

    public static <T> QuantileEstimatorConfig<T> newConfig(ValueCaculator<T> caculator, Comparator<T> comparator) {
        return new DefaultQuantileEstimatorConfig<>(caculator, comparator);
    }

    public static <T> QuantileEstimator<T> newBasic(QuantileEstimatorConfig<T> config) {
        return new BasicQuantileEstimator<>(config);
    }

}
