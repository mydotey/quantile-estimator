package org.mydotey.quantileestimator.kll;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    protected abstract <T> QuantileEstimator<T> newQuantileEstimator(Class<T> valueClazz);

    @Test
    public void test1() {
        QuantileEstimator<Long> quantileEstimator = newQuantileEstimator(Long.class);
        Long[] data = new Long[] { 199L, 76L, 581L, 960L, 757L, 699L, 944L, 225L, 766L, 887L, 900L, 913L, 738L, 330L,
                829L, 323L, 384L, 449L, 636L, 484L };
        List<Long> items = Lists.newArrayList(data);

        for (Long item : items) {
            quantileEstimator.add(item);
        }

        System.out.println("data: " + items);
        Collections.sort(items);
        System.out.println("sorted: " + items);
        System.out.println();

        HashMap<Double, Long> quantileResults = new HashMap<>();
        quantileResults.put(0.95, 944L);

        for (Map.Entry<Double, Long> entry : quantileResults.entrySet()) {
            double quantile = entry.getKey();
            Long expected = entry.getValue();
            Long actual = quantileEstimator.get(quantile);
            System.out.println("quantile " + quantile + ", expected: " + expected + ", actual: " + actual);
            System.out.println();

            Assert.assertEquals(expected, actual);
        }
    }

}
