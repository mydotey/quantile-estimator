using System;
using System.Collections.Generic;

using MyDotey.Quantile.Value;
using MyDotey.Quantile.Validation;
using MyDotey.Quantile.Concurrent;
using MyDotey.Quantile.TimeWindow;

using MyDotey.Quantile.Classic;
using MyDotey.Quantile.Ckms;
using MyDotey.Quantile.Gk;
using MyDotey.Quantile.Kll;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
namespace MyDotey.Quantile.Facade
{
    public class QuantileEstimators
    {
        protected QuantileEstimators()
        {

        }

        public static IQuantileEstimator<T> NewClassicEstimator<T>(IComparer<T> comparer, ICalculator<T> calculator)
        {
            IQuantileEstimator<T> quantileEstimator = new ClassicQuantileEstimator<T>(
                    new ClassicQuantileEstimatorConfig<T>(comparer, calculator));
            return new ValidationDecorator<T>(quantileEstimator);
        }

        /**
         * @param k    first compactor size
         * c default to 2.0 / 3.0
         */
        public static IQuantileEstimator<T> NewKllEstimator<T>(IComparer<T> comparer, int k)
        {
            IQuantileEstimator<T> quantileEstimator = new KllQuantileEstimator<T>(
                    new KllQuantileEstimatorConfig<T>(comparer, k));
            return new ValidationDecorator<T>(quantileEstimator);
        }

        /**
         * @param k    first compactor size
         * @param c    compaction rate
         */
        public static IQuantileEstimator<T> NewKllEstimator<T>(IComparer<T> comparer, int k, double c)
        {
            IQuantileEstimator<T> quantileEstimator = new KllQuantileEstimator<T>(
                    new KllQuantileEstimatorConfig<T>(comparer, k, c));
            return new ValidationDecorator<T>(quantileEstimator);
        }

        public static CkmsQuantileEstimatorConfig<T>.Builder NewCkmsEstimatorConfigBuilder<T>()
        {
            return CkmsQuantileEstimatorConfig<T>.NewBuilder();
        }

        public static IQuantileEstimator<T> NewCkmsEstimator<T>(CkmsQuantileEstimatorConfig<T> config)
        {
            IQuantileEstimator<T> quantileEstimator = new CkmsQuantileEstimator<T>(config);
            return new ValidationDecorator<T>(quantileEstimator);
        }

        public static IQuantileEstimator<T> NewGkEstimator<T>(IComparer<T> comparer, double error,
                int compactThreshold)
        {
            IQuantileEstimator<T> quantileEstimator = new GkQuantileEstimator<T>(
                    new GkQuantileEstimatorConfig<T>(comparer, error, compactThreshold));
            return new ValidationDecorator<T>(quantileEstimator);
        }

        public static IQuantileEstimator<T> NewTimeWindowEstimator<T>(
                Func<IQuantileEstimator<T>> quantileEstimatorSupplier, long timeWindowMillis,
                long rotateDurationMillis)
        {
            return new TimeWindowQuantileEstimator<T>(new TimeWindowQuantileEstimatorConfig<T>(quantileEstimatorSupplier,
                    timeWindowMillis, rotateDurationMillis));
        }

        public static IQuantileEstimator<T> NewConcurrentEstimator<T>(IQuantileEstimator<T> quantileEstimator)
        {
            return new ConcurrencyDecorator<T>(quantileEstimator);
        }
    }
}