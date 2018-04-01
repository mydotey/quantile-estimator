package org.mydotey.quantileestimator.simple;

import java.util.Comparator;
import java.util.Objects;

import org.mydotey.quantileestimator.QuantileEstimatorConfig;
import org.mydotey.quantileestimator.ValueCaculator;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class SimpleQuantileEstimatorConfig<T> implements QuantileEstimatorConfig<T> {

    private ValueCaculator<T> _caculator;
    private Comparator<T> _comparator;

    public SimpleQuantileEstimatorConfig(ValueCaculator<T> caculator, Comparator<T> comparator) {
        Objects.requireNonNull(caculator, "caculator is null");
        Objects.requireNonNull(comparator, "comparator is null");

        _caculator = caculator;
        _comparator = comparator;
    }

    @Override
    public Comparator<T> getComparator() {
        return _comparator;
    }

    @Override
    public ValueCaculator<T> getValueCaculator() {
        return _caculator;
    }

}
