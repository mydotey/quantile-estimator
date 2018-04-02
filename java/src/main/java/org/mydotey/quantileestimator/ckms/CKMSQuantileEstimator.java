package org.mydotey.quantileestimator.ckms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.mydotey.quantileestimator.QuantileEstimator;
import org.mydotey.quantileestimator.ckms.CKMSQuantiles.Quantile;

/**
 * @author koqizhao
 *
 * Apr 2, 2018
 */
public class CKMSQuantileEstimator<T> implements QuantileEstimator<T> {

    private CKMSQuantileEstimatorConfig<T> _config;
    private CKMSQuantiles<T> _ckmsQuantiles;

    public CKMSQuantileEstimator(CKMSQuantileEstimatorConfig<T> config) {
        Objects.requireNonNull(config, "config is null");

        _config = config;

        List<Quantile> quantiles = new ArrayList<>();
        _config.getQuantileConfigs().forEach(qc -> {
            Quantile quantile = new Quantile(qc.getQuantile(), qc.getError());
            quantiles.add(quantile);
        });
        _ckmsQuantiles = new CKMSQuantiles<>(quantiles, _config.getComparator(), 500);
    }

    @Override
    public void add(T value) {
        _ckmsQuantiles.insert(value);
    }

    @Override
    public Map<Double, T> get(List<Double> quantiles) {
        if (_ckmsQuantiles.isEmpty())
            return null;

        HashMap<Double, T> results = new HashMap<>();
        for (Double quantile : quantiles) {
            T result = _ckmsQuantiles.get(quantile);
            results.put(quantile, result);
        }

        return results;
    }

}
