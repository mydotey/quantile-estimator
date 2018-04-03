using System;
using System.Collections.Generic;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
namespace MyDotey.Quantile.Kll
{
    public class KllQuantileEstimatorConfig<T>
    {
        public virtual IComparer<T> Comparer { get; }
        public virtual int K { get; }
        public virtual double C { get; }

        /**
         * @param k    first compactor size
         * c default to 2.0 / 3.0
         */
        public KllQuantileEstimatorConfig(IComparer<T> comparer, int k)
            : this(comparer, k, 2.0 / 3.0)
        {
        }

        /**
         * @param k    first compactor size
         * @param c    compaction rate
         */
        public KllQuantileEstimatorConfig(IComparer<T> comparer, int k, double c)
        {
            if (comparer == null)
                throw new ArgumentNullException("comparer is null");

            if (k <= 0)
                throw new ArgumentException("k must be a positive integer.");

            if (c <= 0.5 || c > 1.0)
                throw new ArgumentException("c must be larger than 0.5 and at most 1.0.");

            Comparer = comparer;
            K = k;
            C = c;
        }
    }
}