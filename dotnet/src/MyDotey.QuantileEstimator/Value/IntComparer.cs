using System;
using System.Collections.Generic;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
namespace MyDotey.Quantile.Value
{
    public class IntComparer : IComparer<int>
    {
        public static readonly IntComparer DEFAULT = new IntComparer();

        public virtual int Compare(int o1, int o2)
        {
            return o1 > o2 ? 1 : (o1 == o2 ? 0 : -1);
        }
    }
}