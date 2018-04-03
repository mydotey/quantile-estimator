using System;
using System.Collections.Generic;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
namespace MyDotey.Quantile.Value
{
    public class LongComparer : IComparer<long>
    {
        public static readonly LongComparer DEFAULT = new LongComparer();

        public virtual int Compare(long o1, long o2)
        {
            return o1 > o2 ? 1 : (o1 == o2 ? 0 : -1);
        }
    }
}