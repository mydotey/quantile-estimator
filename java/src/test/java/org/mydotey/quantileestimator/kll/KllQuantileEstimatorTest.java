package org.mydotey.quantileestimator.kll;

import org.mydotey.quantileestimator.facade.QuantileEstimators;
import org.mydotey.quantileestimator.value.LongComparator;

/**
 * @author koqizhao
 *
 * Mar 30, 2018
 */
public class KllQuantileEstimatorTest extends QuantileEstimatorTest {

    public KllQuantileEstimatorTest() {
        _estimators.put(Long.class, QuantileEstimators.newKllEstimator(LongComparator.DEFAULT, 100));
    }

}
