package org.mydotey.quantileestimator.facade;

import java.util.Comparator;

import org.mydotey.quantileestimator.QuantileEstimator;
import org.mydotey.quantileestimator.classic.ClassicQuantileEstimatorConfig;
import org.mydotey.quantileestimator.classic.ClassicQuantileEstimator;
import org.mydotey.quantileestimator.decorator.ConcurrencyDecorator;
import org.mydotey.quantileestimator.decorator.ValidationDecorator;
import org.mydotey.quantileestimator.kll.KllQuantileEstimatorConfig;
import org.mydotey.quantileestimator.value.Calculator;
import org.mydotey.quantileestimator.kll.KllQuantileEstimator;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class QuantileEstimators {

    protected QuantileEstimators() {

    }

    public static <T> QuantileEstimator<T> newClassicEstimator(Comparator<T> comparator, Calculator<T> calculator) {
        QuantileEstimator<T> quantileEstimator = new ClassicQuantileEstimator<>(
                new ClassicQuantileEstimatorConfig<>(comparator, calculator));
        return decorate(quantileEstimator);
    }

    /**
     * @param k    first compactor height is k * c
     * c default to 2.0 / 3.0
     */
    public static <T> QuantileEstimator<T> newKllEstimator(Comparator<T> comparator, int k) {
        QuantileEstimator<T> quantileEstimator = new KllQuantileEstimator<>(
                new KllQuantileEstimatorConfig<>(comparator, k));
        return decorate(quantileEstimator);
    }

    /**
     * @param k    first compactor height is k * c
     * @param c    compaction rate
     */
    public static <T> QuantileEstimator<T> newKllEstimator(Comparator<T> comparator, int k, double c) {
        QuantileEstimator<T> quantileEstimator = new KllQuantileEstimator<>(
                new KllQuantileEstimatorConfig<>(comparator, k, c));
        return decorate(quantileEstimator);
    }

    protected static <T> QuantileEstimator<T> decorate(QuantileEstimator<T> quantileEstimator) {
        ConcurrencyDecorator<T> concurrencyDecorator = new ConcurrencyDecorator<>(quantileEstimator);
        ValidationDecorator<T> validationDecorator = new ValidationDecorator<>(concurrencyDecorator);
        return validationDecorator;
    }

}
