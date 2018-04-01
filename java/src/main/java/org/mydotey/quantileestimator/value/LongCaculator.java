package org.mydotey.quantileestimator.value;

import org.mydotey.quantileestimator.classic.ValueCaculator;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class LongCaculator implements ValueCaculator<Long> {

    public static final LongCaculator DEFAULT = new LongCaculator();

    @Override
    public Long add(Long t, Long t2) {
        return t + t2;
    }

    @Override
    public Long subtract(Long t, Long t2) {
        return t - t2;
    }

    @Override
    public Long multiply(Long t, double t2) {
        return (long) (t * t2);
    }

    @Override
    public Long divide(Long t, double t2) {
        return (long) (t / t2);
    }

}
