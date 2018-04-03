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
    private boolean _quantileEstimatorReusable;

    private long _windowMillis;
    private long _rotateDurationMillis;

    protected TimeWindowQuantileEstimatorConfig(Supplier<QuantileEstimator<T>> quantileEstimatorSupplier,
            long windowMillis, long rotateDurationMillis, boolean quantileEstimatorReusable) {
        _quantileEstimatorSupplier = quantileEstimatorSupplier;
        _quantileEstimatorReusable = quantileEstimatorReusable;

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

    public boolean isQuantileEstimatorReusable() {
        return _quantileEstimatorReusable;
    }

    public static <T> Builder<T> newBuiler() {
        return new Builder<>();
    }

    public static class Builder<T> {

        private Supplier<QuantileEstimator<T>> _quantileEstimatorSupplier;
        private boolean _quantileEstimatorReusable;

        private long _windowMillis;
        private long _rotateDurationMillis;

        protected Builder() {

        }

        public Builder<T> setQuantileEstimatorSupplier(Supplier<QuantileEstimator<T>> quantileEstimatorSupplier) {
            _quantileEstimatorSupplier = quantileEstimatorSupplier;
            return this;
        }

        public Builder<T> setQuantileEstimatorReusable(boolean quantileEstimatorReusable) {
            _quantileEstimatorReusable = quantileEstimatorReusable;
            return this;
        }

        public Builder<T> setWindowMillis(long windowMillis) {
            _windowMillis = windowMillis;
            return this;
        }

        public Builder<T> setRotateDurationMillis(long rotateDurationMillis) {
            _rotateDurationMillis = rotateDurationMillis;
            return this;
        }

        public TimeWindowQuantileEstimatorConfig<T> build() {
            Objects.requireNonNull(_quantileEstimatorSupplier, "quantileEstimatorSupplier is null");

            if (_windowMillis <= 0)
                throw new IllegalArgumentException("windowMillis is invalid: " + _windowMillis);

            if (_rotateDurationMillis <= 0)
                throw new IllegalArgumentException("rotateDurationMillis is invalid: " + _rotateDurationMillis);

            long reminder = _windowMillis % _rotateDurationMillis;
            if (reminder != 0)
                throw new IllegalArgumentException("windowMillis % rotateDurationMillis is not 0: " + reminder);

            return new TimeWindowQuantileEstimatorConfig<>(_quantileEstimatorSupplier, _windowMillis,
                    _rotateDurationMillis, _quantileEstimatorReusable);
        }

    }

}
