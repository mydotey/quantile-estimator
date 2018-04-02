package org.mydotey.quantileestimator.ckms;

import org.mydotey.quantileestimator.QuantileEstimatorTest;
import org.mydotey.quantileestimator.facade.QuantileEstimators;
import org.mydotey.quantileestimator.value.IntComparator;
import org.mydotey.quantileestimator.value.LongComparator;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class CkmsQuantileEstimatorTest extends QuantileEstimatorTest {

    @Override
    protected void addQuantileEsimators() {
        CkmsQuantileEstimatorConfig.Builder<Long> builder = QuantileEstimators.newCkmsEstimatorConfigBuilder();
        builder.setComparator(LongComparator.DEFAULT).addQuantileConfig(0.01, 0.001).addQuantileConfig(0.25, 0.01)
                .addQuantileConfig(0.5, 0.01).addQuantileConfig(0.75, 0.01).addQuantileConfig(0.99, 0.001);
        _estimators.put(Long.class, QuantileEstimators.newCKmsEstimator(builder.build()));

        CkmsQuantileEstimatorConfig.Builder<Integer> builder2 = QuantileEstimators.newCkmsEstimatorConfigBuilder();
        builder2.setComparator(IntComparator.DEFAULT).addQuantileConfig(0.01, 0.001).addQuantileConfig(0.25, 0.01)
                .addQuantileConfig(0.5, 0.01).addQuantileConfig(0.75, 0.01).addQuantileConfig(0.99, 0.001);
        _estimators.put(Integer.class, QuantileEstimators.newCKmsEstimator(builder2.build()));
    }

}
