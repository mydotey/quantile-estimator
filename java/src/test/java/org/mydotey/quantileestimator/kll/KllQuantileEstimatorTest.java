package org.mydotey.quantileestimator.kll;

import org.mydotey.quantileestimator.QuantileEstimator;
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
    public void test5() {
        int count = 100;
        int upperBound = 1000;
        double errorRate = 0.05;
        QuantileEstimator<Integer> quantileEstimator = getQuantileEstimator(Integer.class);
        test5(quantileEstimator, count, upperBound, errorRate);
    }

}
