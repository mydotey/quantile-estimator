package org.mydotey.quantile.timewindow;

import java.util.Objects;
import java.util.function.Supplier;

import org.mydotey.quantile.QuantileEstimator;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class TimeWindowQuantileEstimatorConfig<T> {

    private Supplier<QuantileEstimator<T>> _quantileEstimatorSupplier;

    private long _windowMillis;
    private long _rotateDurationMillis;

    public TimeWindowQuantileEstimatorConfig(Supplier<QuantileEstimator<T>> quantileEstimatorSupplier,
            long windowMillis, long rotateDurationMillis) {
        Objects.requireNonNull(quantileEstimatorSupplier, "quantileEstimatorSupplier is null");

        if (windowMillis <= 0)
            throw new IllegalArgumentException("windowMillis is invalid: " + windowMillis);

        if (rotateDurationMillis <= 0)
            throw new IllegalArgumentException("rotateDurationMillis is invalid: " + rotateDurationMillis);

        long reminder = windowMillis % rotateDurationMillis;
        if (reminder != 0)
            throw new IllegalArgumentException("windowMillis % rotateDurationMillis is not 0: " + reminder);

        _quantileEstimatorSupplier = quantileEstimatorSupplier;
        _windowMillis = windowMillis;
        _rotateDurationMillis = rotateDurationMillis;
    }

    public Supplier<QuantileEstimator<T>> getQuantileEstimatorSupplier() {
        return _quantileEstimatorSupplier;
    }

    public long getWindowMillis() {
        return _windowMillis;
    }

    public long getRotateDurationMillis() {
        return _rotateDurationMillis;
    }

}
