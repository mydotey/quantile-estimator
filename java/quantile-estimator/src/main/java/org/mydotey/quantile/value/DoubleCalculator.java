package org.mydotey.quantile.value;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class DoubleCalculator implements Calculator<Double> {

    public static final DoubleCalculator DEFAULT = new DoubleCalculator();

    @Override
    public Double add(Double t, Double t2) {
        return t + t2;
    }

    @Override
    public Double subtract(Double t, Double t2) {
        return t - t2;
    }

    @Override
    public Double multiply(Double t, double t2) {
        return t * t2;
    }

    @Override
    public Double divide(Double t, double t2) {
        return t / t2;
    }

}
