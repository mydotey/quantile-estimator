package org.mydotey.quantileestimator.classic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.mydotey.quantileestimator.QuantileEstimator;
import org.mydotey.quantileestimator.value.Calculator;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class ClassicQuantileEstimator<T> implements QuantileEstimator<T> {

    private ClassicQuantileEstimatorConfig<T> _config;

    private volatile LinkedList<T> _sortedSamples;
    private ConcurrentLinkedQueue<T> _cache;

    private Object _lock;

    public ClassicQuantileEstimator(ClassicQuantileEstimatorConfig<T> config) {
        Objects.requireNonNull(config, "config is null");
        _config = config;

        _sortedSamples = new LinkedList<>();
        _cache = new ConcurrentLinkedQueue<>();

        _lock = new Object();
    }

    @Override
    public void add(T value) {
        _cache.offer(value);
    }

    @Override
    public T get(double quantile) {
        List<T> sortedSamples = getSortedSamples();

        double n = sortedSamples.size();
        double pos = quantile * (n - 1);
        int lowerPos = (int) pos;
        T lower = sortedSamples.get(lowerPos);
        T upper = sortedSamples.get(lowerPos + 1);
        Calculator<T> caculator = _config.getValueCalculator();
        return caculator.add(lower, caculator.multiply(caculator.subtract(upper, lower), pos - lowerPos));
    }

    protected List<T> getSortedSamples() {
        if (_cache.size() > 0) {
            synchronized (_lock) {
                if (_cache.size() > 0) {
                    List<T> allCached = new ArrayList<>();
                    for (T cached = _cache.poll(); cached != null; cached = _cache.poll())
                        allCached.add(cached);

                    _sortedSamples.addAll(allCached);
                    _sortedSamples.sort(_config.getComparator());
                }
            }
        }

        return _sortedSamples;
    }

}
