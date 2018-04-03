using System;
using System.Collections.Generic;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
namespace MyDotey.Quantile.Gk
{
    public class GkQuantileEstimatorConfig<T>
    {
        public virtual IComparer<T> Comparer { get; }
        public virtual double Error { get; }
        public virtual int CompactThreshold { get; }

        public GkQuantileEstimatorConfig(IComparer<T> comparer, double error, int compactThreshold)
        {
            if (comparer == null)
                throw new ArgumentNullException("comparer is null");

            String format = "{0} {1} invalid: expected number between 0.0 and 1.0.";
            if (error < 0.0 || error > 1.0)
                throw new ArgumentException(String.Format(format, "error", error));

            if (compactThreshold <= 0)
                throw new ArgumentException("compactThreshold is less than 1");

            Comparer = comparer;
            Error = error;
            CompactThreshold = compactThreshold;
        }
    }
}