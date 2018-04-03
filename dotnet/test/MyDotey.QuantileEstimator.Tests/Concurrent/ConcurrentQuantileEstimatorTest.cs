using System;
using System.Collections.Generic;
using System.Collections.Concurrent;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;

using MyDotey.Quantile.Value;
using MyDotey.Quantile.Facade;
using MyDotey.Quantile.Concurrent;
using MyDotey.Quantile.Ckms;

using Xunit;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
namespace MyDotey.Quantile.Tests
{
    public class ConcurrentQuantileEstimatorTest : QuantileEstimatorTest
    {
        protected override void AddQuantileEsimators()
        {
            CkmsQuantileEstimatorConfig<long>.Builder builder = QuantileEstimators.NewCkmsEstimatorConfigBuilder<long>();
            builder.SetComparer(LongComparer.DEFAULT).AddQuantileConfig(0.01, 0.001).AddQuantileConfig(0.25, 0.01)
                    .AddQuantileConfig(0.5, 0.01).AddQuantileConfig(0.75, 0.01).AddQuantileConfig(0.99, 0.001);
            _estimators[typeof(long)] = QuantileEstimators
                    .NewConcurrentEstimator<long>(QuantileEstimators.NewCkmsEstimator(builder.Build()));

            CkmsQuantileEstimatorConfig<int>.Builder builder2 = QuantileEstimators.NewCkmsEstimatorConfigBuilder<int>();
            builder2.SetComparer(IntComparer.DEFAULT).AddQuantileConfig(0.01, 0.001).AddQuantileConfig(0.25, 0.01)
                    .AddQuantileConfig(0.5, 0.01).AddQuantileConfig(0.75, 0.01).AddQuantileConfig(0.99, 0.001);
            _estimators[typeof(int)] = QuantileEstimators
                    .NewConcurrentEstimator(QuantileEstimators.NewCkmsEstimator(builder2.Build()));
        }

        protected override void Test(IQuantileEstimator<int> quantileEstimator, List<Double> quantiles, int count, int upperBound,
                double errorRate, Func<int, int, List<int>> dataProvider)
        {
            int maxError = (int)(upperBound * errorRate);
            List<int> items = dataProvider(count, upperBound);

            int concurrency = 20;
            object @lock = new object();
            CountdownEvent latch = new CountdownEvent(concurrency);
            for (int i = 0; i < concurrency; i++)
            {
                Task.Run(() =>
                {
                    lock (@lock)
                    {
                        Monitor.Wait(@lock);
                    }

                    items.ForEach(item => quantileEstimator.Add(item));
                    quantileEstimator.Get(quantiles);
                    latch.Signal();
                });
            }

            Thread.Sleep(1 * 1000);

            lock (@lock)
            {
                Monitor.PulseAll(@lock);
            }

            latch.Wait();

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