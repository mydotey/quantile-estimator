package org.mydotey.quantileestimator.classic;

import java.util.Comparator;
import java.util.Objects;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class ClassicQuantileEstimatorConfig<T> {

    private Comparator<T> _comparator;
    private ValueCaculator<T> _caculator;

    public ClassicQuantileEstimatorConfig(Comparator<T> comparator, ValueCaculator<T> caculator) {
        Objects.requireNonNull(comparator, "comparator is null");
        Objects.requireNonNull(caculator, "caculator is null");

        _comparator = comparator;
        _caculator = caculator;
    }

    public Comparator<T> getComparator() {
        return _comparator;
    }

    public ValueCaculator<T> getValueCaculator() {
        return _caculator;
    }

}
