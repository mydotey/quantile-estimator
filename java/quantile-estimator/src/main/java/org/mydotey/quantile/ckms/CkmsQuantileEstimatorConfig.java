package org.mydotey.quantile.ckms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class CkmsQuantileEstimatorConfig<T> {

    private Comparator<T> _comparator;
    private List<QuantileConfig> _quantileConfigs;

    protected CkmsQuantileEstimatorConfig(Comparator<T> comparator, List<QuantileConfig> quantileConfigs) {
        _comparator = comparator;
        _quantileConfigs = Collections.unmodifiableList(quantileConfigs);
    }

    public Comparator<T> getComparator() {
        return _comparator;
    }

    public List<QuantileConfig> getQuantileConfigs() {
        return _quantileConfigs;
    }

    public static class QuantileConfig {

        private double quantile;
        private double error;

        protected QuantileConfig(double quantile, double error) {
            this.quantile = quantile;
            this.error = error;
        }

        public double getQuantile() {
            return quantile;
        }

        public double getError() {
            return error;
        }

    }

    public static <T> Builder<T> newBuiler() {
        return new Builder<>();
    }

    public static class Builder<T> {

        private Comparator<T> _comparator;
        private List<QuantileConfig> _quantileConfigs;

        protected Builder() {
            _quantileConfigs = new ArrayList<>();
        }

        public Builder<T> setComparator(Comparator<T> comparator) {
            _comparator = comparator;
            return this;
        }

        public Builder<T> addQuantileConfig(double quantile, double error) {
            String format = "%s %f invalid: expected number between 0.0 and 1.0.";
            if (quantile < 0.0 || quantile > 1.0)
                throw new IllegalArgumentException(String.format(format, "quantile", quantile));

            if (error < 0.0 || error > 1.0)
                throw new IllegalArgumentException(String.format(format, "error", error));

            _quantileConfigs.add(new QuantileConfig(quantile, error));
            return this;
        }

        public CkmsQuantileEstimatorConfig<T> build() {
            Objects.requireNonNull(_comparator, "comparator is not set");

            if (_quantileConfigs.isEmpty())
                throw new IllegalArgumentException("quantileConfig is not added");

            return new CkmsQuantileEstimatorConfig<>(_comparator, new ArrayList<>(_quantileConfigs));
        }

    }

}
