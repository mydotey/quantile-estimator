using System;
using System.Collections.Generic;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
namespace MyDotey.Quantile.TimeWindow
{
    public class TimeWindowQuantileEstimatorConfig<T>
    {
        public virtual Func<IQuantileEstimator<T>> QuantileEstimatorSupplier { get; }

        public virtual long WindowMillis { get; }
        public virtual long RotateDurationMillis { get; }

        public TimeWindowQuantileEstimatorConfig(Func<IQuantileEstimator<T>> quantileEstimatorSupplier,
                long windowMillis, long rotateDurationMillis)
        {
            if (quantileEstimatorSupplier == null)
                throw new ArgumentNullException("quantileEstimatorSupplier is null");

            if (windowMillis <= 0)
                throw new ArgumentException("windowMillis is invalid: " + windowMillis);

            if (rotateDurationMillis <= 0)
                throw new ArgumentException("rotateDurationMillis is invalid: " + rotateDurationMillis);

            long reminder = windowMillis % rotateDurationMillis;
            if (reminder != 0)
                throw new ArgumentException("windowMillis % rotateDurationMillis is not 0: " + reminder);

            QuantileEstimatorSupplier = quantileEstimatorSupplier;
            WindowMillis = windowMillis;
            RotateDurationMillis = rotateDurationMillis;
        }
    }
}