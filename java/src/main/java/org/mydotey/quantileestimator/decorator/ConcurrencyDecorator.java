package org.mydotey.quantileestimator.decorator;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

import org.mydotey.quantileestimator.QuantileEstimator;

/**
 * @author koqizhao
 *
 * Apr 2, 2018
 */
public class ConcurrencyDecorator<T> implements QuantileEstimator<T> {

    private QuantileEstimator<T> _quantileEstimator;

    private ConcurrentLinkedQueue<T> _cache;
    private ReentrantLock _lock;

    public ConcurrencyDecorator(QuantileEstimator<T> quantileEstimator) {
        Objects.requireNonNull(quantileEstimator, "quantileEstimator is null");

        _quantileEstimator = quantileEstimator;
        _cache = new ConcurrentLinkedQueue<>();
        _lock = new ReentrantLock();
    }

    @Override
    public void add(T value) {
        _cache.offer(value);

        if (_lock.tryLock()) {
            try {
                batchAdd();
            } finally {
                _lock.unlock();
            }
        }
    }

    @Override
    public Map<Double, T> get(List<Double> quantiles) {
        try {
            _lock.lock();

            batchAdd();
            return _quantileEstimator.get(quantiles);
        } finally {
            _lock.unlock();
        }
    }

    protected void batchAdd() {
        for (T cached = _cache.poll(); cached != null; cached = _cache.poll())
            _quantileEstimator.add(cached);
    }

}
