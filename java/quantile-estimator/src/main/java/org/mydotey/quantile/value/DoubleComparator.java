package org.mydotey.quantile.value;

import java.util.Comparator;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class DoubleComparator implements Comparator<Double> {

    public static final DoubleComparator DEFAULT = new DoubleComparator();

    @Override
    public int compare(Double o1, Double o2) {
        return o1 > o2 ? 1 : (o1 == o2 ? 0 : -1);
    }

}
