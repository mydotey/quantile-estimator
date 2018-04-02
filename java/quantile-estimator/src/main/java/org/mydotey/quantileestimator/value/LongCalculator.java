package org.mydotey.quantileestimator.value;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class LongCalculator implements Calculator<Long> {

    public static final LongCalculator DEFAULT = new LongCalculator();

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
