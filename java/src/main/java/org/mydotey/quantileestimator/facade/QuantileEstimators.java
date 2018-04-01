package org.mydotey.quantileestimator.facade;

import java.util.Comparator;

import org.mydotey.quantileestimator.QuantileEstimator;
import org.mydotey.quantileestimator.classic.ClassicQuantileEstimatorConfig;
import org.mydotey.quantileestimator.classic.ClassicQuantileEstimator;
import org.mydotey.quantileestimator.classic.ValueCaculator;
import org.mydotey.quantileestimator.kll.KllQuantileEstimatorConfig;
import org.mydotey.quantileestimator.validator.ValidatorDecorator;
import org.mydotey.quantileestimator.kll.KllQuantileEstimator;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class QuantileEstimators {

    protected QuantileEstimators() {

    }

    public static <T> QuantileEstimator<T> newClassicEstimator(Comparator<T> comparator, ValueCaculator<T> caculator) {
        QuantileEstimator<T> quantileEstimator = new ClassicQuantileEstimator<>(
                new ClassicQuantileEstimatorConfig<>(comparator, caculator));
        return new ValidatorDecorator<>(quantileEstimator);
    }

    public static <T> QuantileEstimator<T> newKllEstimator(Comparator<T> comparator, int k) {
        QuantileEstimator<T> quantileEstimator = new KllQuantileEstimator<>(
                new KllQuantileEstimatorConfig<>(comparator, k));
        return new ValidatorDecorator<>(quantileEstimator);
    }

}
