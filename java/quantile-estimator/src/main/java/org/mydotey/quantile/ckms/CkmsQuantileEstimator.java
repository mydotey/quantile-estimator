package org.mydotey.quantile.ckms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.mydotey.quantile.QuantileEstimator;
import org.mydotey.quantile.ckms.CKMSQuantiles.Quantile;

/**
 * @author koqizhao
 *
 * Apr 2, 2018
 */
public class CkmsQuantileEstimator<T> implements QuantileEstimator<T> {

    private CkmsQuantileEstimatorConfig<T> _config;
    private CKMSQuantiles<T> _ckmsQuantiles;

    public CkmsQuantileEstimator(CkmsQuantileEstimatorConfig<T> config) {
        Objects.requireNonNull(config, "config is null");

        _config = config;

        List<Quantile> quantiles = new ArrayList<>();
        _config.getQuantileConfigs().forEach(qc -> {
            Quantile quantile = new Quantile(qc.getQuantile(), qc.getError());
            quantiles.add(quantile);
        });
        _ckmsQuantiles = new CKMSQuantiles<>(quantiles, _config.getComparator());
    }

    @Override
    public void add(T value) {
        _ckmsQuantiles.insert(value);
    }

    @Override
    public Map<Double, T> get(List<Double> quantiles) {
        HashMap<Double, T> results = null;
        for (Double quantile : quantiles) {
            T result = _ckmsQuantiles.get(quantile);
            if (_ckmsQuantiles.isEmpty())
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
