package org.mydotey.quantileestimator.value;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class IntCalculator implements Calculator<Integer> {

    public static final IntCalculator DEFAULT = new IntCalculator();

    @Override
    public Integer add(Integer t, Integer t2) {
        return t + t2;
    }

    @Override
    public Integer subtract(Integer t, Integer t2) {
        return t - t2;
    }

    @Override
    public Integer multiply(Integer t, double t2) {
        return (int) (t * t2);
    }

    @Override
    public Integer divide(Integer t, double t2) {
        return (int) (t / t2);
    }

}
