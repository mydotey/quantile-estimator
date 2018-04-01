package org.mydotey.quantileestimator.kll;

import java.util.concurrent.ConcurrentHashMap;

import org.mydotey.quantileestimator.facade.QuantileEstimators;
import org.mydotey.quantileestimator.value.LongCaculator;
import org.mydotey.quantileestimator.value.LongComparator;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class SimpleQuantileEstimatorTest extends QuantileEstimatorTest {

    public SimpleQuantileEstimatorTest() {
        _estimators = new ConcurrentHashMap<>();
        _estimators.put(Long.class, QuantileEstimators
                .newSimpleEstimator(QuantileEstimators.newSimpleConfig(LongCaculator.DEFAULT, LongComparator.DEFAULT)));
    }

}
