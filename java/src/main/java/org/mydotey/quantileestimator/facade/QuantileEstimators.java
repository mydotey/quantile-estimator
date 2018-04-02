package org.mydotey.quantileestimator.facade;

import java.util.Comparator;

import org.mydotey.quantileestimator.QuantileEstimator;
import org.mydotey.quantileestimator.ckms.CkmsQuantileEstimator;
import org.mydotey.quantileestimator.ckms.CkmsQuantileEstimatorConfig;
import org.mydotey.quantileestimator.classic.ClassicQuantileEstimatorConfig;
import org.mydotey.quantileestimator.classic.ClassicQuantileEstimator;
import org.mydotey.quantileestimator.decorator.ConcurrencyDecorator;
import org.mydotey.quantileestimator.decorator.ValidationDecorator;
import org.mydotey.quantileestimator.gk.GkQuantileEstimator;
import org.mydotey.quantileestimator.gk.GkQuantileEstimatorConfig;
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
     * @param k    first compactor size
     * c default to 2.0 / 3.0
     */
    public static <T> QuantileEstimator<T> newKllEstimator(Comparator<T> comparator, int k) {
        QuantileEstimator<T> quantileEstimator = new KllQuantileEstimator<>(
                new KllQuantileEstimatorConfig<>(comparator, k));
        return decorate(quantileEstimator);
    }

    /**
     * @param k    first compactor size
     * @param c    compaction rate
     */
    public static <T> QuantileEstimator<T> newKllEstimator(Comparator<T> comparator, int k, double c) {
        QuantileEstimator<T> quantileEstimator = new KllQuantileEstimator<>(
                new KllQuantileEstimatorConfig<>(comparator, k, c));
        return decorate(quantileEstimator);
    }

    public static <T> CkmsQuantileEstimatorConfig.Builder<T> newCkmsEstimatorConfigBuilder() {
        return CkmsQuantileEstimatorConfig.<T> newBuiler();
    }

    public static <T> QuantileEstimator<T> newCKmsEstimator(CkmsQuantileEstimatorConfig<T> config) {
        QuantileEstimator<T> quantileEstimator = new CkmsQuantileEstimator<>(config);
        return decorate(quantileEstimator);
    }

    public static <T> QuantileEstimator<T> newGkEstimator(Comparator<T> comparator, double error,
            int compactThreshold) {
        QuantileEstimator<T> quantileEstimator = new GkQuantileEstimator<>(
                new GkQuantileEstimatorConfig<>(comparator, error, compactThreshold));
        return decorate(quantileEstimator);
    }

    protected static <T> QuantileEstimator<T> decorate(QuantileEstimator<T> quantileEstimator) {
        ConcurrencyDecorator<T> concurrencyDecorator = new ConcurrencyDecorator<>(quantileEstimator);
        ValidationDecorator<T> validationDecorator = new ValidationDecorator<>(concurrencyDecorator);
        return validationDecorator;
    }

}
