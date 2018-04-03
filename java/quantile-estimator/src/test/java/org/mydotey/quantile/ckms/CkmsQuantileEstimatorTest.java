package org.mydotey.quantile.ckms;

import org.mydotey.quantile.QuantileEstimatorTest;
import org.mydotey.quantile.ckms.CkmsQuantileEstimatorConfig;
import org.mydotey.quantile.facade.QuantileEstimators;
import org.mydotey.quantile.value.IntComparator;
import org.mydotey.quantile.value.LongComparator;

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
        _estimators.put(Long.class, QuantileEstimators.newCkmsEstimator(builder.build()));

        CkmsQuantileEstimatorConfig.Builder<Integer> builder2 = QuantileEstimators.newCkmsEstimatorConfigBuilder();
        builder2.setComparator(IntComparator.DEFAULT).addQuantileConfig(0.01, 0.001).addQuantileConfig(0.25, 0.01)
                .addQuantileConfig(0.5, 0.01).addQuantileConfig(0.75, 0.01).addQuantileConfig(0.99, 0.001);
        _estimators.put(Integer.class, QuantileEstimators.newCkmsEstimator(builder2.build()));
    }

}
