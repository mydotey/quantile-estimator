package org.mydotey.quantileestimator.decorator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.mydotey.quantileestimator.QuantileEstimator;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class ValidationDecorator<T> implements QuantileEstimator<T> {

    private QuantileEstimator<T> _quantileEstimator;

    public ValidationDecorator(QuantileEstimator<T> quantileEstimator) {
        Objects.requireNonNull(quantileEstimator, "quantileEstimator is null");
        _quantileEstimator = quantileEstimator;
    }

    @Override
    public void add(T value) {
        if (value == null)
            return;

        _quantileEstimator.add(value);
    }

    @Override
    public Map<Double, T> get(List<Double> quantiles) {
        Objects.requireNonNull(quantiles, "quantiles is null");

        if (quantiles.isEmpty())
            return new HashMap<>();

        quantiles.forEach(quantile -> {
            if (quantile == null || quantile < 0 || quantile > 1)
                throw new IllegalArgumentException("quantile is not valid: " + quantile);
        });

        return _quantileEstimator.get(quantiles);
    }

}
