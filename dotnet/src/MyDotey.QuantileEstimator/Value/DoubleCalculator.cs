using System;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
namespace MyDotey.Quantile.Value
{
    public class DoubleCalculator : ICalculator<Double>
    {
        public static readonly DoubleCalculator DEFAULT = new DoubleCalculator();

        public virtual Double Add(Double t, Double t2)
        {
            return t + t2;
        }

        public virtual Double Subtract(Double t, Double t2)
        {
            return t - t2;
        }

        public virtual Double Multiply(Double t, double t2)
        {
            return t * t2;
        }

        public virtual Double Divide(Double t, double t2)
        {
            return t / t2;
        }
    }
}