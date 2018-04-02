package org.mydotey.quantileestimator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Assert;
import org.junit.Test;
import org.mydotey.quantileestimator.QuantileEstimator;

import com.google.common.collect.Lists;

/**
 * @author koqizhao
 *
 * Mar 30, 2018
 */
public abstract class QuantileEstimatorTest {

    @SuppressWarnings("rawtypes")
    protected ConcurrentHashMap<Class<?>, QuantileEstimator> _estimators = new ConcurrentHashMap<>();

    public QuantileEstimatorTest() {
        addQuantileEsimators();
    }

    protected abstract void addQuantileEsimators();

    @SuppressWarnings("unchecked")
    protected <T> QuantileEstimator<T> getQuantileEstimator(Class<T> valueClazz) {
        return _estimators.get(valueClazz);
    }

    @Test
    public void test1() {
        QuantileEstimator<Long> quantileEstimator = getQuantileEstimator(Long.class);
        Map<Double, Long> results = quantileEstimator.get(Lists.newArrayList(0.95));
        Assert.assertNull("results is not null", results);
    }

    @Test
    public void test2() {
        QuantileEstimator<Long> quantileEstimator = getQuantileEstimator(Long.class);
        Long[] data = new Long[] { 199L, 76L, 581L, 960L, 757L, 699L, 944L, 225L, 766L, 887L, 900L, 913L, 738L, 330L,
                829L, 323L, 384L, 449L, 636L, 484L };
        List<Long> items = Lists.newArrayList(data);

        for (Long item : items) {
            quantileEstimator.add(item);
        }

        Map<Double, Long> results = quantileEstimator.get(new ArrayList<>());
        Assert.assertTrue("results is not empty", results.isEmpty());
    }

    @Test
    public void test3() {
        QuantileEstimator<Long> quantileEstimator = getQuantileEstimator(Long.class);
        quantileEstimator.add(1L);

        HashMap<Double, Long> quantileResults = new HashMap<>();
        quantileResults.put(0.01, 1L);
        quantileResults.put(0.50, 1L);
        quantileResults.put(0.95, 1L);
        quantileResults.put(1.0, 1L);

        List<Double> quantiles = Lists.newArrayList(quantileResults.keySet());
        Map<Double, Long> results = quantileEstimator.get(quantiles);
        for (int i = 0; i < quantiles.size(); i++) {
            Double quantile = quantiles.get(i);
            Long expected = quantileResults.get(quantile);
            Long actual = results.get(quantile);
            System.out.println("quantile " + quantile + ", expected: " + expected + ", actual: " + actual);
            System.out.println();

            Assert.assertEquals(expected, actual);
        }
    }

    @Test
    public void test4() {
        QuantileEstimator<Long> quantileEstimator = getQuantileEstimator(Long.class);
        Long[] data = new Long[] { 199L, 76L, 581L, 960L, 757L, 699L, 944L, 225L, 766L, 887L, 900L, 913L, 738L, 330L,
                829L, 323L, 384L, 449L, 636L, 484L };
        List<Long> items = Lists.newArrayList(data);

        for (Long item : items) {
            quantileEstimator.add(item);
        }

        System.out.println("data: " + items);
        System.out.println();

        Collections.sort(items);
        System.out.println("sorted: " + items);
        System.out.println();

        HashMap<Double, Long> quantileResults = new HashMap<>();
        quantileResults.put(0.95, 944L);

        List<Double> quantiles = Lists.newArrayList(quantileResults.keySet());
        Map<Double, Long> results = quantileEstimator.get(quantiles);
        for (int i = 0; i < quantiles.size(); i++) {
            Double quantile = quantiles.get(i);
            Long expected = quantileResults.get(quantile);
            Long actual = results.get(quantile);
            System.out.println("quantile " + quantile + ", expected: " + expected + ", actual: " + actual);
            System.out.println();

            Assert.assertEquals(expected, actual);
        }
    }

    @Test
    public void test5() {
        int count = 100;
        int upperBound = 1000;
        double errorRate = 0.05;
        QuantileEstimator<Integer> quantileEstimator = getQuantileEstimator(Integer.class);
        test5(quantileEstimator, count, upperBound, errorRate);
    }

    @Test
    public void test5_2() {
        int count = 1000;
        int upperBound = 10000;
        double errorRate = 0.01;
        QuantileEstimator<Integer> quantileEstimator = getQuantileEstimator(Integer.class);
        test5(quantileEstimator, count, upperBound, errorRate);
    }

    @Test
    public void test5_3() {
        int count = 10000;
        int upperBound = 100000;
        double errorRate = 0.001;
        QuantileEstimator<Integer> quantileEstimator = getQuantileEstimator(Integer.class);
        test5(quantileEstimator, count, upperBound, errorRate);
    }

    protected void test5(QuantileEstimator<Integer> quantileEstimator, int count, int upperBound, double errorRate) {
        int maxError = (int) (upperBound * errorRate);
        List<Integer> items = Lists.newArrayList();

        Random random = new Random();
        for (int i = 0; i < count; i++) {
            Integer item = random.nextInt(upperBound);
            items.add(item);
            quantileEstimator.add(item);
        }

        System.out.println("data: " + items);
        System.out.println();

        Collections.sort(items);
        System.out.println("sorted: " + items);
        System.out.println();

        List<Double> quantiles = Lists.newArrayList(0.0, 0.001, 0.25, 0.50, 0.75, 0.90, 0.999, 1.0);
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
