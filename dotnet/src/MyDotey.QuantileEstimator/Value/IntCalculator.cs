using System;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
namespace MyDotey.Quantile.Value
{
    public class IntCalculator : ICalculator<int>
    {
        public static readonly IntCalculator DEFAULT = new IntCalculator();

        public virtual int Add(int t, int t2)
        {
            return t + t2;
        }

        public virtual int Subtract(int t, int t2)
        {
            return t - t2;
        }

        public virtual int Multiply(int t, double t2)
        {
            return (int)(t * t2);
        }

        public virtual int Divide(int t, double t2)
        {
            return (int)(t / t2);
        }
    }
}