package org.mydotey.quantile.timewindow;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.junit.Assert;
import org.mydotey.quantile.QuantileEstimator;
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
public class TimeWindowQuantileEstimatorTest extends QuantileEstimatorTest {

    @Override
    protected void addQuantileEsimators() {
        Supplier<QuantileEstimator<Long>> longSupplier = () -> QuantileEstimators
                .newClassicEstimator(LongComparator.DEFAULT, LongCalculator.DEFAULT);
        _estimators.put(Long.class, QuantileEstimators.newTimeWindowEstimator(longSupplier,
                TimeUnit.SECONDS.toMillis(5), TimeUnit.SECONDS.toMillis(1)));

        Supplier<QuantileEstimator<Integer>> intSupplier = () -> QuantileEstimators
                .newClassicEstimator(IntComparator.DEFAULT, IntCalculator.DEFAULT);
        _estimators.put(Integer.class, QuantileEstimators.newTimeWindowEstimator(intSupplier,
                TimeUnit.SECONDS.toMillis(5), TimeUnit.SECONDS.toMillis(1)));
    }

    @Override
    protected void test(QuantileEstimator<Integer> quantileEstimator, List<Double> quantiles, int count, int upperBound,
            double errorRate, BiFunction<Integer, Integer, List<Integer>> dataProvider) {
        int maxError = (int) (upperBound * errorRate);
        List<Integer> items = dataProvider.apply(count, upperBound);

        int timeWindowSec = 10;
        for (int i = 0; i < timeWindowSec; i++) {
            items.forEach(item -> quantileEstimator.add(item));
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            } catch (Exception e) {
                e.printStackTrace();
            }
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
