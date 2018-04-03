using System;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
namespace MyDotey.Quantile.Value
{
    public class LongCalculator : ICalculator<long>
    {
        public static readonly LongCalculator DEFAULT = new LongCalculator();

        public virtual long Add(long t, long t2)
        {
            return t + t2;
        }

        public virtual long Subtract(long t, long t2)
        {
            return t - t2;
        }

        public virtual long Multiply(long t, double t2)
        {
            return (long)(t * t2);
        }

        public virtual long Divide(long t, double t2)
        {
            return (long)(t / t2);
        }
    }
}