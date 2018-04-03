using System;
using System.Collections.Generic;
using System.Threading;

using MyDotey.Quantile.Value;
using MyDotey.Quantile.Facade;
using Xunit;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
namespace MyDotey.Quantile.Tests
{
    public class TimeWindowQuantileEstimatorTest : QuantileEstimatorTest
    {
        protected override void AddQuantileEsimators()
        {
            Func<IQuantileEstimator<long>> longSupplier = () => QuantileEstimators
                    .NewClassicEstimator(LongComparer.DEFAULT, LongCalculator.DEFAULT);
            _estimators[typeof(long)] = QuantileEstimators.NewTimeWindowEstimator(longSupplier,
                    5 * 1000, 1 * 1000);

            Func<IQuantileEstimator<int>> intSupplier = () => QuantileEstimators
                    .NewClassicEstimator(IntComparer.DEFAULT, IntCalculator.DEFAULT);
            _estimators[typeof(int)] = QuantileEstimators.NewTimeWindowEstimator(intSupplier,
                    5 * 1000, 1 * 1000);
        }

        protected override void Test(IQuantileEstimator<int> quantileEstimator, List<Double> quantiles, int count, int upperBound,
                double errorRate, Func<int, int, List<int>> dataProvider)
        {
            int maxError = (int)(upperBound * errorRate);
            List<int> items = dataProvider(count, upperBound);

            int timeWindowSec = 10;
            for (int i = 0; i < timeWindowSec; i++)
            {
                items.ForEach(item => quantileEstimator.Add(item));
                Thread.Sleep(1 * 1000);
            }

            Console.WriteLine("data: " + items);
            Console.WriteLine();

            items.Sort();

            Console.WriteLine("sorted: " + items);
            Console.WriteLine();

            Dictionary<Double, int> quantileResults = new Dictionary<Double, int>();
            foreach (Double quantile in quantiles)
            {
                int pos = (int)(count * quantile) - 1;
                if (pos < 0)
                    pos = 0;

                int item = items[pos];
                quantileEstimator.Add(item);
                quantileResults[quantile] = item;
            }

            Dictionary<Double, int> results = quantileEstimator.Get(quantiles);
            for (int i = 0; i < quantiles.Count; i++)
            {
                Double quantile = quantiles[i];
                int expected = quantileResults[quantile];
                int actual = results[quantile];
                int actualError = Math.Abs(actual - expected);
                Console.WriteLine("quantile " + quantile + ", expected: " + expected + ", actual: " + actual + ", error: "
                        + actualError);
                Console.WriteLine();

                Assert.True(actualError <= maxError);
            }
        }
    }
}