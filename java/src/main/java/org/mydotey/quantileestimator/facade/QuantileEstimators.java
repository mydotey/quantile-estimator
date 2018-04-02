package org.mydotey.quantileestimator.facade;

import java.util.Comparator;

import org.mydotey.quantileestimator.QuantileEstimator;
import org.mydotey.quantileestimator.classic.ClassicQuantileEstimatorConfig;
import org.mydotey.quantileestimator.classic.ClassicQuantileEstimator;
import org.mydotey.quantileestimator.classic.ValueCaculator;
import org.mydotey.quantileestimator.decorator.ValidationDecorator;
import org.mydotey.quantileestimator.kll.KllQuantileEstimatorConfig;
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
        return new ValidationDecorator<>(quantileEstimator);
    }

    /**
     * c default to 2.0 / 3.0
     * @param k    first compactor height is k * c
     */
    public static <T> QuantileEstimator<T> newKllEstimator(Comparator<T> comparator, int k) {
        QuantileEstimator<T> quantileEstimator = new KllQuantileEstimator<>(
                new KllQuantileEstimatorConfig<>(comparator, k));
        return new ValidationDecorator<>(quantileEstimator);
    }

    /**
     * @param k    first compactor height is k * c
     * @param c    compaction rate
     */
    public static <T> QuantileEstimator<T> newKllEstimator(Comparator<T> comparator, int k, double c) {
        QuantileEstimator<T> quantileEstimator = new KllQuantileEstimator<>(
                new KllQuantileEstimatorConfig<>(comparator, k, c));
        return new ValidationDecorator<>(quantileEstimator);
    }

}
