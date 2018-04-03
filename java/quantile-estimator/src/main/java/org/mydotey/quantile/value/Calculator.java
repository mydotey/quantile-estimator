package org.mydotey.quantile.value;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
public interface Calculator<T> {

    T add(T t, T t2);

    T subtract(T t, T t2);

    T multiply(T t, double t2);

    T divide(T t, double t2);

}
