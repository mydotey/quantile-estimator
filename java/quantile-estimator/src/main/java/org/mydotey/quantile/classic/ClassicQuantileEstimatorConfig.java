package org.mydotey.quantile.classic;

import java.util.Comparator;
import java.util.Objects;

import org.mydotey.quantile.value.Calculator;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class ClassicQuantileEstimatorConfig<T> {

    private Comparator<T> _comparator;
    private Calculator<T> _calculator;

    public ClassicQuantileEstimatorConfig(Comparator<T> comparator, Calculator<T> calculator) {
        Objects.requireNonNull(comparator, "comparator is null");
        Objects.requireNonNull(calculator, "calculator is null");

        _comparator = comparator;
        _calculator = calculator;
    }

    public Comparator<T> getComparator() {
        return _comparator;
    }

    public Calculator<T> getCalculator() {
        return _calculator;
    }

}
