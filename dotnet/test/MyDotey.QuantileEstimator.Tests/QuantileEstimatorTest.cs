using System;
using System.Collections.Generic;
using System.Collections.Concurrent;
using System.Linq;

using Xunit;
using NLog;
using NLog.Config;
using NLog.Targets;

/**
 * @author koqizhao
 *
 * Mar 30, 2018
 */
namespace MyDotey.Quantile.Tests
{
    public abstract class QuantileEstimatorTest
    {
        protected ConcurrentDictionary<Type, object> _estimators = new ConcurrentDictionary<Type, object>();

        public QuantileEstimatorTest()
        {
            var config = new LoggingConfiguration();
            var logconsole = new ConsoleTarget() { Name = "logconsole" };
            config.LoggingRules.Add(new NLog.Config.LoggingRule("*", LogLevel.Trace, logconsole));
            NLog.LogManager.Configuration = config;

            AddQuantileEsimators();
        }

        protected abstract void AddQuantileEsimators();

        protected virtual IQuantileEstimator<T> GetQuantileEstimator<T>()
        {
            return (IQuantileEstimator<T>)_estimators[typeof(T)];
        }

        [Fact]
        public virtual void Test1()
        {
            IQuantileEstimator<long> quantileEstimator = GetQuantileEstimator<long>();
            Dictionary<Double, long> results = quantileEstimator.Get(new List<Double>() { 0.95 });
            Assert.Null(results);
        }

        [Fact]
        public virtual void Test2()
        {
            IQuantileEstimator<long> quantileEstimator = GetQuantileEstimator<long>();
            long[] data = new long[] { 199L, 76L, 581L, 960L, 757L, 699L, 944L, 225L, 766L, 887L, 900L, 913L, 738L, 330L,
                829L, 323L, 384L, 449L, 636L, 484L };
            List<long> items = new List<long>(data);

            foreach (long item in items)
            {
                quantileEstimator.Add(item);
            }

            Dictionary<Double, long> results = quantileEstimator.Get(new List<Double>());
            Assert.True(results.Count == 0);
        }

        [Fact]
        public virtual void Test3()
        {
            IQuantileEstimator<long> quantileEstimator = GetQuantileEstimator<long>();
            quantileEstimator.Add(1L);

            Dictionary<Double, long> quantileResults = new Dictionary<Double, long>()
            {
                { 0.01, 1L },
                { 0.50, 1L },
                { 0.95, 1L },
                { 1.0, 1L }
            };

            List<Double> quantiles = quantileResults.Keys.ToList();
            Dictionary<Double, long> results = quantileEstimator.Get(quantiles);
            for (int i = 0; i < quantiles.Count; i++)
            {
                Double quantile = quantiles[i];
                long expected = quantileResults[quantile];
                long actual = results[quantile];
                Console.WriteLine("quantile " + quantile + ", expected: " + expected + ", actual: " + actual);
                Console.WriteLine();

                Assert.Equal(expected, actual);
            }
        }

        [Fact]
        public virtual void Test4()
        {
            IQuantileEstimator<long> quantileEstimator = GetQuantileEstimator<long>();
            long[] data = new long[] { 199L, 76L, 581L, 960L, 757L, 699L, 944L, 225L, 766L, 887L, 900L, 913L, 738L, 330L,
                829L, 323L, 384L, 449L, 636L, 484L };
            List<long> items = new List<long>(data);

            foreach (long item in items)
            {
                quantileEstimator.Add(item);
            }

            Console.WriteLine("data: " + items);
            Console.WriteLine();

            items.Sort();

            Console.WriteLine("sorted: " + items);
            Console.WriteLine();

            Dictionary<Double, long> quantileResults = new Dictionary<Double, long>()
            {
                { 0.95, 944L }
            };

            List<Double> quantiles = quantileResults.Keys.ToList();
            Dictionary<Double, long> results = quantileEstimator.Get(quantiles);
            for (int i = 0; i < quantiles.Count; i++)
            {
                Double quantile = quantiles[i];
                long expected = quantileResults[quantile];
                long actual = results[quantile];
                Console.WriteLine("quantile " + quantile + ", expected: " + expected + ", actual: " + actual);
                Console.WriteLine();

                Assert.Equal(expected, actual);
            }
        }

        [Fact]
        public virtual void Test5()
        {
            int count = 100;
            int upperBound = 1000;
            double errorRate = 0.10;
            Test5Internal(count, upperBound, errorRate);
        }

        [Fact]
        public virtual void Test5_2()
        {
            int count = 1000;
            int upperBound = 1000;
            double errorRate = 0.01;
            Test5Internal(count, upperBound, errorRate);
        }

        [Fact]
        public virtual void Test5_3()
        {
            int count = 10000;
            int upperBound = 1000;
            double errorRate = 0.005;
            Test5Internal(count, upperBound, errorRate);
        }

        protected virtual void Test5Internal(int count, int upperBound, double errorRate)
        {
            IQuantileEstimator<int> quantileEstimator = GetQuantileEstimator<int>();
            List<Double> quantiles = new List<Double>() { 0.0, 0.001, 0.25, 0.50, 0.75, 0.90, 0.999, 1.0 };

            Func<int, int, List<int>> balanceddataProvider = (c, b) =>
            {
                Dictionary<int, int> itemsAndCounts = new Dictionary<int, int>();
                int distinctItemCount = c < b ? c : b;
                int singleItemCount = c / b;
                if (singleItemCount == 0)
                    singleItemCount = 1;

                List<int> data = new List<int>();
                Random random = new Random();
                while (data.Count < c)
                {
                    int item = random.Next(b);
                    bool success = itemsAndCounts.TryGetValue(item, out int itemCount);
                    if (success && itemCount < singleItemCount)
                    {
                        itemsAndCounts[item] = itemCount++;
                        data.Add(item);
                    }
                    else if (itemsAndCounts.Keys.Count < distinctItemCount)
                    {
                        itemsAndCounts[item] = 1;
                        data.Add(item);
                    }
                }

                return data;
            };

            Test(quantileEstimator, quantiles, count, upperBound, errorRate, balanceddataProvider);
        }

        protected virtual void Test(IQuantileEstimator<int> quantileEstimator, List<Double> quantiles, int count, int upperBound,
                double errorRate, Func<int, int, List<int>> dataProvider)
        {
            int maxError = (int)(upperBound * errorRate);
            List<int> items = dataProvider(count, upperBound);
            items.ForEach(item => quantileEstimator.Add(item));

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