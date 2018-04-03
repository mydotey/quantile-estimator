package org.mydotey.quantile.classic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.mydotey.quantile.QuantileEstimator;
import org.mydotey.quantile.value.Calculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class ClassicQuantileEstimator<T> implements QuantileEstimator<T> {

    private static Logger _logger = LoggerFactory.getLogger(ClassicQuantileEstimator.class);

    private ClassicQuantileEstimatorConfig<T> _config;

    private volatile List<T> _values;

    public ClassicQuantileEstimator(ClassicQuantileEstimatorConfig<T> config) {
        Objects.requireNonNull(config, "config is null");
        _config = config;

        _values = new LinkedList<>();
    }

    @Override
    public void add(T value) {
        _values.add(value);
    }

    @Override
    public Map<Double, T> get(List<Double> quantiles) {
        if (_values.isEmpty())
            return null;

        _values.sort(_config.getComparator());

        HashMap<Double, T> results = new HashMap<>();
        double n = _values.size() - 1;
        Calculator<T> caculator = _config.getCalculator();
        for (int i = 0; i < quantiles.size(); i++) {
            Double quantile = quantiles.get(i);
            double pos = quantile * n;
            int lowerPos = (int) pos;
            int upperPos = lowerPos + 1;
            T lower = _values.get(lowerPos);
            if (upperPos <= n) {
                T upper = _values.get(upperPos);
                T result = caculator.add(lower, caculator.multiply(caculator.subtract(upper, lower), pos - lowerPos));
                results.put(quantile, result);
            } else
                results.put(quantile, lower);
        }

        return results;
    }

    protected void defensiveReset() {
        if (_values.size() == Integer.MAX_VALUE) {
            _logger.warn("count reached int.max, reset and prevent overflow");

            _values.clear();
        }
    }

}
