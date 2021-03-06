package org.mydotey.quantile.kll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.mydotey.quantile.QuantileEstimator;

/**
 * @author koqizhao
 *
 * Mar 30, 2018
 * 
 * see also: https://github.com/edoliberty/streaming-quantiles
 * 
 */
public class KllQuantileEstimator<T> implements QuantileEstimator<T> {

    private KllQuantileEstimatorConfig<T> _config;

    private List<Compactor> _compactors;
    private int _h;
    private int _size;
    private int _maxSize;

    public KllQuantileEstimator(KllQuantileEstimatorConfig<T> config) {
        Objects.requireNonNull(config, "config is null");
        _config = config;

        _compactors = new ArrayList<>();

        grow();
    }

    @Override
    public void add(T value) {
        _compactors.get(0).add(value);
        _size++;
        if (_size >= _maxSize)
            compress();
    }

    @Override
    public Map<Double, T> get(List<Double> quantiles) {
        List<ItemAndQuantile> itemsAndQuantiles = cdf();
        if (itemsAndQuantiles.isEmpty())
            return null;

        HashMap<Double, T> results = new HashMap<>();

        int i = 0, j = 0;
        while (i < quantiles.size() && j < itemsAndQuantiles.size()) {
            Double quantile = quantiles.get(i);
            ItemAndQuantile itemAndQuantile = itemsAndQuantiles.get(j);
            if (itemAndQuantile.quantile < quantile) {
                j++;
                continue;
            }

            results.put(quantile, itemAndQuantile.item);
            i++;
        }

        if (i < quantiles.size()) {
            T result = itemsAndQuantiles.get(j - 1).item;
            for (; i < quantiles.size(); i++) {
                Double quantile = quantiles.get(i);
                results.put(quantile, result);
            }
        }

        return results;
    }

    protected void grow() {
        _compactors.add(new Compactor());
        _h = _compactors.size();

        int maxSize = 0;
        for (int i = 0; i < _h; i++) {
            maxSize += capacity(i);
        }
        _maxSize = maxSize;
    }

    protected int capacity(int height) {
        int depth = _h - 1 - height;
        return (int) Math.ceil(Math.pow(_config.getC(), depth) * _config.getK()) + 1;
    }

    protected void compress() {
        for (int i = 0; i < _compactors.size(); i++) {
            if (_compactors.get(i).size() >= capacity(i)) {
                if (i + 1 >= _h)
                    grow();

                _compactors.get(i + 1).addAll(_compactors.get(i).compact());
                int size = 0;
                for (int j = 0; j < _compactors.size(); j++)
                    size += _compactors.get(j).size();
                _size = size;

                break;
            }
        }
    }

    protected List<ItemAndQuantile> cdf() {
        List<ItemAndWeight> itemsAndWeights = new ArrayList<>();
        for (int i = 0; i < _compactors.size(); i++) {
            for (int j = 0; j < _compactors.get(i).size(); j++) {
                itemsAndWeights.add(new ItemAndWeight(_compactors.get(i).get(j), (int) Math.pow(2, i)));
            }
        }

        int totWeight = 0;
        for (ItemAndWeight itemAndWeight : itemsAndWeights)
            totWeight += itemAndWeight.weight;

        Collections.sort(itemsAndWeights);

        List<ItemAndQuantile> cdf = new ArrayList<>();
        int cumWeight = 0;
        for (ItemAndWeight itemAndWeight : itemsAndWeights) {
            cumWeight += itemAndWeight.weight;
            cdf.add(new ItemAndQuantile(itemAndWeight.item, (double) cumWeight / totWeight));
        }

        return cdf;
    }

    protected void merge(KllQuantileEstimator<T> other) {
        while (_h < other._h)
            grow();

        for (int i = 0; i < other._h; i++)
            _compactors.get(i).addAll(other._compactors.get(i));

        int size = 0;
        for (int i = 0; i < _compactors.size(); i++)
            size += _compactors.get(i).size();
        _size = size;

        while (_size >= _maxSize)
            compress();
    }

    protected int rank(T value) {
        int r = 0;
        for (int i = 0; i < _compactors.size(); i++) {
            for (int j = 0; j < _compactors.get(i).size(); j++) {
                if (_config.getComparator().compare(_compactors.get(i).get(j), value) <= 0)
                    r += Math.pow(2, i);
            }
        }

        return r;
    }

    protected List<ItemAndWeight> ranks() {
        List<ItemAndWeight> itemsAndWeights = new ArrayList<>();
        for (int i = 0; i < _compactors.size(); i++) {
            for (int j = 0; j < _compactors.get(i).size(); j++) {
                itemsAndWeights.add(new ItemAndWeight(_compactors.get(i).get(j), (int) Math.pow(2, i)));
            }
        }

        Collections.sort(itemsAndWeights);

        List<ItemAndWeight> ranksList = new ArrayList<>();
        int cumWeight = 0;
        for (ItemAndWeight itemAndWeight : itemsAndWeights) {
            cumWeight += itemAndWeight.weight;
            ranksList.add(new ItemAndWeight(itemAndWeight.item, cumWeight));
        }

        return ranksList;
    }

    protected class Compactor extends ArrayList<T> {

        private static final long serialVersionUID = 1L;

        public List<T> compact() {
            sort(KllQuantileEstimator.this._config.getComparator());

            List<T> result = new ArrayList<>();
            if (Math.random() < 0.5) {
                while (size() >= 2) {
                    int index = size() - 1;
                    remove(index);
                    result.add(remove(index - 1));
                }
            } else {
                while (size() >= 2) {
                    int index = size() - 1;
                    result.add(remove(index));
                    remove(index - 1);
                }
            }

            return result;
        }
    }

    protected class ItemAndWeight implements Comparable<ItemAndWeight> {
        public T item;
        public int weight;

        public ItemAndWeight(T item, int weight) {
            this.item = item;
            this.weight = weight;
        }

        @Override
        public int compareTo(ItemAndWeight o) {
            if (o == null)
                return 1;

            int r = KllQuantileEstimator.this._config.getComparator().compare(item, o.item);
            if (r != 0)
                return r;

            return weight > o.weight ? 1 : (weight == o.weight ? 0 : -1);
        }
    }

    protected class ItemAndQuantile {

        public T item;
        public double quantile;

        public ItemAndQuantile(T item, double quantile) {
            this.item = item;
            this.quantile = quantile;
        }

        @Override
        public String toString() {
            return String.format("{ item: %s, quantile: %s }", item, quantile);
        }
    }

}
