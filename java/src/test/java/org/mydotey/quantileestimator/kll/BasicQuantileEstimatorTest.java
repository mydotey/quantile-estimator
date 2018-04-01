package org.mydotey.quantileestimator.kll;

import java.util.concurrent.ConcurrentHashMap;

import org.mydotey.quantileestimator.QuantileEstimator;
import org.mydotey.quantileestimator.facade.QuantileEstimators;
import org.mydotey.quantileestimator.value.LongCaculator;
import org.mydotey.quantileestimator.value.LongComparator;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class BasicQuantileEstimatorTest extends QuantileEstimatorTest {

    @SuppressWarnings("rawtypes")
    private static ConcurrentHashMap<Class<?>, QuantileEstimator> _estimators;

    static {
        _estimators = new ConcurrentHashMap<>();
        _estimators.put(Long.class, QuantileEstimators
                .newBasic(QuantileEstimators.newConfig(LongCaculator.DEFAULT, LongComparator.DEFAULT)));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> QuantileEstimator<T> newQuantileEstimator(Class<T> valueClazz) {
        return _estimators.get(valueClazz);
    }

}
