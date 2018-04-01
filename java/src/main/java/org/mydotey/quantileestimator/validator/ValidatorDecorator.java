package org.mydotey.quantileestimator.validator;

import java.util.Objects;

import org.mydotey.quantileestimator.QuantileEstimator;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class ValidatorDecorator<T> implements QuantileEstimator<T> {

    private QuantileEstimator<T> _quantileEstimator;

    public ValidatorDecorator(QuantileEstimator<T> quantileEstimator) {
        Objects.requireNonNull(quantileEstimator, "quantileEstimator is null");
        _quantileEstimator = quantileEstimator;
    }

    @Override
    public void add(T value) {
        Objects.requireNonNull(value, "value is null");
        _quantileEstimator.add(value);
    }

    @Override
    public T get(double quantile) {
        if (quantile < 0 || quantile > 1)
            throw new IllegalArgumentException("quantile is not valid: " + quantile);

        return _quantileEstimator.get(quantile);
    }

}
