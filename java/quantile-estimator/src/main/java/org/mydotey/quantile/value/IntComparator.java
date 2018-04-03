package org.mydotey.quantile.value;

import java.util.Comparator;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class IntComparator implements Comparator<Integer> {

    public static final IntComparator DEFAULT = new IntComparator();

    @Override
    public int compare(Integer o1, Integer o2) {
        return o1 > o2 ? 1 : (o1 == o2 ? 0 : -1);
    }

}
