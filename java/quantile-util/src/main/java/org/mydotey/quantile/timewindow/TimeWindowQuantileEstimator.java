package org.mydotey.quantile.timewindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import org.mydotey.quantile.QuantileEstimator;

/**
 * @author koqizhao
 *
 * Apr 3, 2018
 */
public class TimeWindowQuantileEstimator<T> implements QuantileEstimator<T> {

    private TimeWindowQuantileEstimatorConfig<T> _config;

    private List<QuantileEstimator<T>> _buffer;
    private AtomicInteger _currentBucket;
    private AtomicLong _lastRotateTime;
    private ReentrantLock _lock;

    public TimeWindowQuantileEstimator(TimeWindowQuantileEstimatorConfig<T> config) {
        Objects.requireNonNull(config, "config is null");
        _config = config;

        _buffer = new ArrayList<>();
        int bucketCount = (int) (_config.getWindowMillis() / _config.getRotateDurationMillis()) + 1;
        for (int i = 0; i < bucketCount; i++)
            _buffer.add(_config.getQuantileEstimatorSupplier().get());

        _currentBucket = new AtomicInteger();
        _lastRotateTime = new AtomicLong();
        _lock = new ReentrantLock();
    }

    @Override
    public void add(T value) {
        tryRotate();

        for (int i = 0; i < _buffer.size(); i++)
            _buffer.get(i).add(value);
    }

    @Override
    public Map<Double, T> get(List<Double> quantiles) {
        tryRotate();

        return getQuantileEstimator().get(quantiles);
    }

    protected void tryRotate() {
        if (_lastRotateTime.get() + _config.getRotateDurationMillis() > System.currentTimeMillis())
            return;

        if (!_lock.tryLock())
            return;

        try {
            int oldestBucket = (_currentBucket.get() + _buffer.size() - 1) % _buffer.size();
            _buffer.set(oldestBucket, _config.getQuantileEstimatorSupplier().get());

            _currentBucket.set((_currentBucket.get() + 1) % _buffer.size());
            _lastRotateTime.set(System.currentTimeMillis());
        } finally {
            _lock.unlock();
        }
    }

    protected QuantileEstimator<T> getQuantileEstimator() {
        return _buffer.get(_currentBucket.get());
    }

}
