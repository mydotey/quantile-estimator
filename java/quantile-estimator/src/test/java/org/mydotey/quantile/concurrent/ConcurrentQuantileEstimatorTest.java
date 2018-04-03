package org.mydotey.quantile.concurrent;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

import org.junit.Assert;
import org.mydotey.quantile.QuantileEstimator;
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
public class ConcurrentQuantileEstimatorTest extends QuantileEstimatorTest {

    @Override
    protected void addQuantileEsimators() {
        CkmsQuantileEstimatorConfig.Builder<Long> builder = QuantileEstimators.newCkmsEstimatorConfigBuilder();
        builder.setComparator(LongComparator.DEFAULT).addQuantileConfig(0.01, 0.001).addQuantileConfig(0.25, 0.01)
                .addQuantileConfig(0.5, 0.01).addQuantileConfig(0.75, 0.01).addQuantileConfig(0.99, 0.001);
        _estimators.put(Long.class, QuantileEstimators
                .newConcurrentEstimator(QuantileEstimators.newCkmsEstimator(builder.build())));

        CkmsQuantileEstimatorConfig.Builder<Integer> builder2 = QuantileEstimators.newCkmsEstimatorConfigBuilder();
        builder2.setComparator(IntComparator.DEFAULT).addQuantileConfig(0.01, 0.001).addQuantileConfig(0.25, 0.01)
                .addQuantileConfig(0.5, 0.01).addQuantileConfig(0.75, 0.01).addQuantileConfig(0.99, 0.001);
        _estimators.put(Integer.class, QuantileEstimators
                .newConcurrentEstimator(QuantileEstimators.newCkmsEstimator(builder2.build())));
    }

    @Override
    protected void test(QuantileEstimator<Integer> quantileEstimator, List<Double> quantiles, int count, int upperBound,
            double errorRate, BiFunction<Integer, Integer, List<Integer>> dataProvider) {
        int maxError = (int) (upperBound * errorRate);
        List<Integer> items = dataProvider.apply(count, upperBound);

        int concurrency = 20;
        ExecutorService executor = Executors.newFixedThreadPool(concurrency);
        Object lock = new Object();
        CountDownLatch latch = new CountDownLatch(concurrency);
        for (int i = 0; i < concurrency; i++) {
            executor.submit(() -> {
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                items.forEach(item -> quantileEstimator.add(item));
                quantileEstimator.get(quantiles);
                latch.countDown();
            });
        }

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        synchronized (lock) {
            lock.notifyAll();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("data: " + items);
        System.out.println();

        Collections.sort(items);
        System.out.println("sorted: " + items);
        System.out.println();

        HashMap<Double, Integer> quantileResults = new HashMap<>();
        for (Double quantile : quantiles) {
            int pos = (int) (count * quantile) - 1;
            if (pos < 0)
                pos = 0;

            int item = items.get(pos);
            quantileEstimator.add(item);
            quantileResults.put(quantile, item);
        }

        Map<Double, Integer> results = quantileEstimator.get(quantiles);
        for (int i = 0; i < quantiles.size(); i++) {
            Double quantile = quantiles.get(i);
            Integer expected = quantileResults.get(quantile);
            Integer actual = results.get(quantile);
            Integer actualError = Math.abs(actual - expected);
            System.out.println("quantile " + quantile + ", expected: " + expected + ", actual: " + actual + ", error: "
                    + actualError);
            System.out.println();

            Assert.assertTrue("error > " + maxError, actualError <= maxError);
        }
    }

}
