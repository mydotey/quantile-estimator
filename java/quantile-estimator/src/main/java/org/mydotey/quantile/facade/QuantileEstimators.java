package org.mydotey.quantile.facade;

import java.util.Comparator;
import java.util.function.Supplier;

import org.mydotey.quantile.QuantileEstimator;
import org.mydotey.quantile.ckms.CkmsQuantileEstimator;
import org.mydotey.quantile.ckms.CkmsQuantileEstimatorConfig;
import org.mydotey.quantile.classic.ClassicQuantileEstimator;
import org.mydotey.quantile.classic.ClassicQuantileEstimatorConfig;
import org.mydotey.quantile.concurrent.ConcurrencyDecorator;
import org.mydotey.quantile.gk.GkQuantileEstimator;
import org.mydotey.quantile.gk.GkQuantileEstimatorConfig;
import org.mydotey.quantile.kll.KllQuantileEstimator;
import org.mydotey.quantile.kll.KllQuantileEstimatorConfig;
import org.mydotey.quantile.timewindow.TimeWindowQuantileEstimator;
import org.mydotey.quantile.timewindow.TimeWindowQuantileEstimatorConfig;
import org.mydotey.quantile.validation.ValidationDecorator;
import org.mydotey.quantile.value.Calculator;

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
        return new ValidationDecorator<>(quantileEstimator);
    }

    /**
     * @param k    first compactor size
     * c default to 2.0 / 3.0
     */
    public static <T> QuantileEstimator<T> newKllEstimator(Comparator<T> comparator, int k) {
        QuantileEstimator<T> quantileEstimator = new KllQuantileEstimator<>(
                new KllQuantileEstimatorConfig<>(comparator, k));
        return new ValidationDecorator<>(quantileEstimator);
    }

    /**
     * @param k    first compactor size
     * @param c    compaction rate
     */
    public static <T> QuantileEstimator<T> newKllEstimator(Comparator<T> comparator, int k, double c) {
        QuantileEstimator<T> quantileEstimator = new KllQuantileEstimator<>(
                new KllQuantileEstimatorConfig<>(comparator, k, c));
        return new ValidationDecorator<>(quantileEstimator);
    }

    public static <T> CkmsQuantileEstimatorConfig.Builder<T> newCkmsEstimatorConfigBuilder() {
        return CkmsQuantileEstimatorConfig.<T> newBuilder();
    }

    public static <T> QuantileEstimator<T> newCkmsEstimator(CkmsQuantileEstimatorConfig<T> config) {
        QuantileEstimator<T> quantileEstimator = new CkmsQuantileEstimator<>(config);
        return new ValidationDecorator<>(quantileEstimator);
    }

    public static <T> QuantileEstimator<T> newGkEstimator(Comparator<T> comparator, double error,
            int compactThreshold) {
        QuantileEstimator<T> quantileEstimator = new GkQuantileEstimator<>(
                new GkQuantileEstimatorConfig<>(comparator, error, compactThreshold));
        return new ValidationDecorator<>(quantileEstimator);
    }

    public static <T> QuantileEstimator<T> newTimeWindowEstimator(
            Supplier<QuantileEstimator<T>> quantileEstimatorSupplier, long timeWindowMillis,
            long rotateDurationMillis) {
        return new TimeWindowQuantileEstimator<>(new TimeWindowQuantileEstimatorConfig<>(quantileEstimatorSupplier,
                timeWindowMillis, rotateDurationMillis));
    }

    public static <T> QuantileEstimator<T> newConcurrentEstimator(QuantileEstimator<T> quantileEstimator) {
        return new ConcurrencyDecorator<>(quantileEstimator);
    }

}
