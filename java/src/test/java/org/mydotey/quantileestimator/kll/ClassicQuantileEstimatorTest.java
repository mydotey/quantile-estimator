package org.mydotey.quantileestimator.kll;

import org.mydotey.quantileestimator.facade.QuantileEstimators;
import org.mydotey.quantileestimator.value.LongCaculator;
import org.mydotey.quantileestimator.value.LongComparator;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class ClassicQuantileEstimatorTest extends QuantileEstimatorTest {

    public ClassicQuantileEstimatorTest() {
        _estimators.put(Long.class,
                QuantileEstimators.newClassicEstimator(LongComparator.DEFAULT, LongCaculator.DEFAULT));
    }

}
