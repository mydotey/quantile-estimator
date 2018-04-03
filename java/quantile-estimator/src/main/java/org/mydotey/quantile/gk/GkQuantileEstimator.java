package org.mydotey.quantile.gk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.mydotey.quantile.QuantileEstimator;

/**
 * @author koqizhao
 *
 * Apr 2, 2018
 */
public class GkQuantileEstimator<T> implements QuantileEstimator<T> {

    private GkQuantileEstimatorConfig<T> _config;
    private QuantileEstimationGK<T> _quantileEstimationGK;

    public GkQuantileEstimator(GkQuantileEstimatorConfig<T> config) {
        Objects.requireNonNull(config, "config is null");

        _config = config;
        _quantileEstimationGK = new QuantileEstimationGK<>(_config.getComparator(), _config.getError(),
                _config.getCompactThreshold());
    }

    @Override
    public void add(T value) {
        _quantileEstimationGK.insert(value);
    }

    @Override
    public Map<Double, T> get(List<Double> quantiles) {
        HashMap<Double, T> results = null;
        for (Double quantile : quantiles) {
            T result = _quantileEstimationGK.query(quantile);
            if (_quantileEstimationGK.isEmpty())
                return null;

            if (results == null)
                results = new HashMap<>();

            results.put(quantile, result);
        }

        if (results == null)
            results = new HashMap<>();

        return results;
    }

}
