package org.mydotey.quantileestimator.facade;

import java.util.Comparator;

import org.mydotey.quantileestimator.QuantileEstimator;
import org.mydotey.quantileestimator.QuantileEstimatorConfig;
import org.mydotey.quantileestimator.ValueCaculator;
import org.mydotey.quantileestimator.kll.DefaultKllQuantileEstimatorConfig;
import org.mydotey.quantileestimator.kll.KllQuantileEstimator;
import org.mydotey.quantileestimator.kll.KllQuantileEstimatorConfig;
import org.mydotey.quantileestimator.simple.SimpleQuantileEstimator;
import org.mydotey.quantileestimator.simple.SimpleQuantileEstimatorConfig;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class QuantileEstimators {

    protected QuantileEstimators() {

    }

    public static <T> QuantileEstimatorConfig<T> newSimpleConfig(ValueCaculator<T> caculator,
            Comparator<T> comparator) {
        return new SimpleQuantileEstimatorConfig<>(caculator, comparator);
    }

    public static <T> QuantileEstimator<T> newSimpleEstimator(QuantileEstimatorConfig<T> config) {
        return new SimpleQuantileEstimator<>(config);
    }

    public static <T> KllQuantileEstimatorConfig<T> newKllConfig(ValueCaculator<T> caculator, Comparator<T> comparator,
            int k) {
        return new DefaultKllQuantileEstimatorConfig<>(caculator, comparator, k);
    }

    public static <T> QuantileEstimator<T> newKllEstimator(KllQuantileEstimatorConfig<T> config) {
        return new KllQuantileEstimator<>(config);
    }

}
