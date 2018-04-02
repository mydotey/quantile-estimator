package org.mydotey.quantileestimator.kll;

import org.mydotey.quantileestimator.QuantileEstimatorTest;
import org.mydotey.quantileestimator.facade.QuantileEstimators;
import org.mydotey.quantileestimator.value.IntComparator;
import org.mydotey.quantileestimator.value.LongComparator;

/**
 * @author koqizhao
 *
 * Mar 30, 2018
 */
public class KllQuantileEstimatorTest extends QuantileEstimatorTest {

    @Override
    protected void addQuantileEsimators() {
        _estimators.put(Long.class, QuantileEstimators.newKllEstimator(LongComparator.DEFAULT, 100));
        _estimators.put(Integer.class, QuantileEstimators.newKllEstimator(IntComparator.DEFAULT, 100));
    }

    @Override
    public void test5_2() {
        int count = 1000;
        int upperBound = 1000;
        double errorRate = 0.01;
        test5(count, upperBound, errorRate);
    }

    @Override
    public void test5_3() {
        int count = 10000;
        int upperBound = 1000;
        double errorRate = 0.05;
        test5(count, upperBound, errorRate);
    }

}
