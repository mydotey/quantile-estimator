using System;
using System.Collections.Generic;

using MyDotey.Quantile.Value;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
namespace MyDotey.Quantile.Classic
{
    public class ClassicQuantileEstimatorConfig<T>
    {
        public virtual IComparer<T> Comparer { get; }
        public virtual ICalculator<T> Calculator { get; }

        public ClassicQuantileEstimatorConfig(IComparer<T> comparer, ICalculator<T> calculator)
        {
            if (comparer == null)
                throw new ArgumentNullException("comparer is null");

            if (calculator == null)
                throw new ArgumentNullException("calculator is null");

            Comparer = comparer;
            Calculator = calculator;
        }
    }
}