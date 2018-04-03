using System;
using System.Collections.Generic;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
namespace MyDotey.Quantile.Value
{
    public class DoubleComparer : IComparer<Double>
    {
        public static readonly DoubleComparer DEFAULT = new DoubleComparer();

        public virtual int Compare(Double o1, Double o2)
        {
            return o1 > o2 ? 1 : (o1 == o2 ? 0 : -1);
        }
    }
}