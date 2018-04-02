package org.mydotey.quantileestimator.kll;

import java.util.Comparator;
import java.util.Objects;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class KllQuantileEstimatorConfig<T> {

    private Comparator<T> _comparator;
    private int _k;
    private double _c;

    /**
     * c default to 2.0 / 3.0
     * @param k    first compactor height is k * c
     */
    public KllQuantileEstimatorConfig(Comparator<T> comparator, int k) {
        this(comparator, k, 2.0 / 3.0);
    }

    /**
     * @param k    first compactor height is k * c
     * @param c    compaction rate
     */
    public KllQuantileEstimatorConfig(Comparator<T> comparator, int k, double c) {
        Objects.requireNonNull(comparator, "comparator is null");

        if (k <= 0)
            throw new IllegalArgumentException("k must be a positive integer.");

        if (c <= 0.5 || c > 1.0)
            throw new IllegalArgumentException("c must be larger than 0.5 and at most 1.0.");

        _comparator = comparator;
        _k = k;
        _c = c;
    }

    public Comparator<T> getComparator() {
        return _comparator;
    }

    public double getK() {
        return _k;
    }

    public double getC() {
        return _c;
    }

}
