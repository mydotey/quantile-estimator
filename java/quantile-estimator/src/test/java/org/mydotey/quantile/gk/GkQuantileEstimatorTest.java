package org.mydotey.quantile.gk;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import org.junit.Test;
import org.mydotey.quantile.QuantileEstimatorTest;
import org.mydotey.quantile.facade.QuantileEstimators;
import org.mydotey.quantile.gk.QuantileEstimationGK;
import org.mydotey.quantile.value.IntComparator;
import org.mydotey.quantile.value.LongComparator;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class GkQuantileEstimatorTest extends QuantileEstimatorTest {

    @Override
    protected void addQuantileEsimators() {
        _estimators.put(Long.class, QuantileEstimators.newGkEstimator(LongComparator.DEFAULT, 0.001, 100));
        _estimators.put(Integer.class, QuantileEstimators.newGkEstimator(IntComparator.DEFAULT, 0.001, 100));
    }

    @Test
    public void test5_3() {
        int count = 10000;
        int upperBound = 1000;
        double errorRate = 0.01;
        test5(count, upperBound, errorRate);
    }

    @Test
    public void gkTest() {
        final int window_size = 10000;
        final double epsilon = 0.001;

        System.out.println("Generating random longs...");
        long[] shuffle = new long[window_size];
        for (int i = 0; i < shuffle.length; i++) {
            shuffle[i] = i;
        }
        Random rand = new Random(0xDEADBEEF);
        Collections.shuffle(Arrays.asList(shuffle), rand);

        System.out.println("Inserting into estimator...");
        QuantileEstimationGK<Long> estimator = new QuantileEstimationGK<>(LongComparator.DEFAULT, epsilon, 1000);
        for (long l : shuffle) {
            estimator.insert(l);
        }

        double[] quantiles = { 0.5, 0.9, 0.95, 0.99, 1.0 };
        for (double q : quantiles) {
            long estimate = estimator.query(q);
            long actual = (long) ((q) * (window_size - 1));
            System.out.println(String.format("Estimated %.2f quantile as %d (actually %d)", q, estimate, actual));
        }
        System.out.println("# of samples: " + estimator.samples.size());
    }

}
