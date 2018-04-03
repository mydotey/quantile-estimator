package org.mydotey.quantile.value;

import java.util.Comparator;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public class LongComparator implements Comparator<Long> {

    public static final LongComparator DEFAULT = new LongComparator();

    @Override
    public int compare(Long o1, Long o2) {
        return o1 > o2 ? 1 : (o1 == o2 ? 0 : -1);
    }

}
