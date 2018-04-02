package org.mydotey.quantileestimator.classic;

import org.mydotey.quantileestimator.QuantileEstimatorTest;
import org.mydotey.quantileestimator.facade.QuantileEstimators;
import org.mydotey.quantileestimator.value.IntCalculator;
import org.mydotey.quantileestimator.value.IntComparator;
import org.mydotey.quantileestimator.value.LongCalculator;
import org.mydotey.quantileestimator.value.LongComparator;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class ClassicQuantileEstimatorTest extends QuantileEstimatorTest {

    @Override
    protected void addQuantileEsimators() {
        _estimators.put(Long.class,
                QuantileEstimators.newClassicEstimator(LongComparator.DEFAULT, LongCalculator.DEFAULT));
        _estimators.put(Integer.class,
                QuantileEstimators.newClassicEstimator(IntComparator.DEFAULT, IntCalculator.DEFAULT));
    }

}
