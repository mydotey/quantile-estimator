package org.mydotey.quantileestimator.gk;

import java.util.Comparator;
import java.util.Objects;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class GkQuantileEstimatorConfig<T> {

    private Comparator<T> _comparator;
    private double _error;
    private int _compactThreshold;

    public GkQuantileEstimatorConfig(Comparator<T> comparator, double error, int compactThreshold) {
        Objects.requireNonNull(comparator, "comparator is null");

        String format = "%s %f invalid: expected number between 0.0 and 1.0.";
        if (error < 0.0 || error > 1.0)
            throw new IllegalArgumentException(String.format(format, "error", error));

        if (compactThreshold <= 0)
            throw new IllegalArgumentException("compactThreshold is less than 1");

        _comparator = comparator;
        _error = error;
        _compactThreshold = compactThreshold;
    }

    public Comparator<T> getComparator() {
        return _comparator;
    }

    public double getError() {
        return _error;
    }

    public int getCompactThreshold() {
        return _compactThreshold;
    }

}
