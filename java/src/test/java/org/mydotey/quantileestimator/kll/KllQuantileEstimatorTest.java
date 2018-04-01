package org.mydotey.quantileestimator.kll;

import org.mydotey.quantileestimator.facade.QuantileEstimators;
import org.mydotey.quantileestimator.value.LongCaculator;
import org.mydotey.quantileestimator.value.LongComparator;

/**
 * @author koqizhao
 *
 * Mar 30, 2018
 */
public class KllQuantileEstimatorTest extends QuantileEstimatorTest {

    public KllQuantileEstimatorTest() {
        KllQuantileEstimatorConfig<Long> config = QuantileEstimators.newKllConfig(LongCaculator.DEFAULT,
                LongComparator.DEFAULT, 100);
        _estimators.put(Long.class, QuantileEstimators.newKllEstimator(config));
    }

}
