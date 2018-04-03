/*
   Copyright 2012 Andrew Wang (andrew@umbrant.com)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

/*
   Editor: koqizhao
   Changes:
       1. remove synchronization 
       2. make it generic
*/

package org.mydotey.quantile.gk;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the Greenwald and Khanna algorithm for streaming
 * calculation of error-approximate quantiles.
 * 
 * See: Greenwald and Khanna,
 * "Space-efficient online computation of quantile summaries" in SIGMOD 2001
 * 
 */
class QuantileEstimationGK<T> {

    private static Logger logger = LoggerFactory.getLogger(QuantileEstimationGK.class);

    private Comparator<T> comparator;

    // Acceptable % error in percentile estimate
    private double error;

    // Threshold to trigger a compaction
    private final int compactThreshold;

    // Total number of items in stream
    int count = 0;

    protected List<Item> samples = new LinkedList<Item>();

    public QuantileEstimationGK(Comparator<T> comparator, double error, int compactThreshold) {
        this.comparator = comparator;
        this.error = error;
        this.compactThreshold = compactThreshold;
    }

    public void insert(T v) {
        defensiveReset();

        int idx = 0;

        for (Item i : samples) {
            if (comparator.compare(i.value, v) > 0) {
                break;
            }
            idx++;
        }

        int delta;
        if (idx == 0 || idx == samples.size()) {
            delta = 0;
        } else {
            delta = (int) Math.floor(2 * error * count);
        }

        Item newItem = new Item(v, 1, delta);
        samples.add(idx, newItem);

        if (samples.size() > compactThreshold)
            compress();

        count++;
    }

    protected void compress() {
        for (int i = 0; i < samples.size() - 1; i++) {
            Item item = samples.get(i);
            Item item1 = samples.get(i + 1);

            // Merge the items together if we don't need it to maintain the
            // error bound
            if (item.g + item1.g + item1.delta <= Math.floor(2 * error * count)) {
                item1.g += item.g;
                samples.remove(i);
            }
        }
    }

    public T query(double quantile) {
        if (samples.isEmpty())
            return null;

        int rankMin = 0;
        int desired = (int) (quantile * count);

        for (int i = 1; i < samples.size(); i++) {
            Item prev = samples.get(i - 1);
            Item cur = samples.get(i);

            rankMin += prev.g;

            if (rankMin + cur.g + cur.delta > desired + (2 * error * count)) {
                return prev.value;
            }
        }

        // edge case of wanting max value
        return samples.get(samples.size() - 1).value;
    }

    protected boolean isEmpty() {
        return samples.isEmpty();
    }

    protected void defensiveReset() {
        if (count == Integer.MAX_VALUE) {
            logger.warn("count reached int.max, reset and prevent overflow");

            count = 0;
            samples.clear();
        }
    }

    private class Item {
        public final T value;
        public int g;
        public final int delta;

        public Item(T value, int lower_delta, int delta) {
            this.value = value;
            this.g = lower_delta;
            this.delta = delta;
        }

        @Override
        public String toString() {
            return String.format("%d, %d, %d", value, g, delta);
        }
    }

}
