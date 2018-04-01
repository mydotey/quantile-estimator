package org.mydotey.quantileestimator.kll;

import java.util.Comparator;

import org.mydotey.quantileestimator.ValueCaculator;
import org.mydotey.quantileestimator.simple.SimpleQuantileEstimatorConfig;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class DefaultKllQuantileEstimatorConfig<T> extends SimpleQuantileEstimatorConfig<T>
        implements KllQuantileEstimatorConfig<T> {

    private int _k;
    private double _c;

    public DefaultKllQuantileEstimatorConfig(ValueCaculator<T> caculator, Comparator<T> comparator, int k) {
        this(caculator, comparator, k, 2.0 / 3.0);
    }

    public DefaultKllQuantileEstimatorConfig(ValueCaculator<T> caculator, Comparator<T> comparator, int k, double c) {
        super(caculator, comparator);

        if (k <= 0)
            throw new IllegalArgumentException("k must be a positive integer.");

        if (c <= 0.5 || c > 1.0)
            throw new IllegalArgumentException("c must larger than 0.5 and at most 1.0.");

        _k = k;
        _c = c;
    }

    @Override
    public double getK() {
        return _k;
    }

    @Override
    public double getC() {
        return _c;
    }

}
