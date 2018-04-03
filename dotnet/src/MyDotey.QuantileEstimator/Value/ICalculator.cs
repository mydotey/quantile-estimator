using System;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
namespace MyDotey.Quantile.Value
{
    public interface ICalculator<T>
    {
        T Add(T t, T t2);

        T Subtract(T t, T t2);

        T Multiply(T t, double t2);

        T Divide(T t, double t2);
    }
}