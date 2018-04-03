package org.mydotey.quantile.classic;

import org.mydotey.quantile.QuantileEstimatorTest;
import org.mydotey.quantile.facade.QuantileEstimators;
import org.mydotey.quantile.value.IntCalculator;
import org.mydotey.quantile.value.IntComparator;
import org.mydotey.quantile.value.LongCalculator;
import org.mydotey.quantile.value.LongComparator;

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
