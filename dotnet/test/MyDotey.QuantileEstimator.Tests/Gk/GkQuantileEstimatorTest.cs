using System;
using System.Collections.Generic;

using MyDotey.Quantile.Value;
using MyDotey.Quantile.Facade;
using MyDotey.Quantile.Gk;

using Xunit;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
namespace MyDotey.Quantile.Tests
{
    public class GkQuantileEstimatorTest : QuantileEstimatorTest
    {
        protected override void AddQuantileEsimators()
        {
            _estimators[typeof(long)] = QuantileEstimators.NewGkEstimator(LongComparer.DEFAULT, 0.001, 100);
            _estimators[typeof(int)] = QuantileEstimators.NewGkEstimator(IntComparer.DEFAULT, 0.001, 100);
        }

        [Fact]
        public override void Test5_3()
        {
            int count = 10000;
            int upperBound = 1000;
            double errorRate = 0.01;
            Test5Internal(count, upperBound, errorRate);
        }

        [Fact]
        public virtual void GkTest()
        {
            int window_size = 10000;
            double epsilon = 0.001;

            Console.WriteLine("Generating random longs...");
            List<long> shuffle = new List<long>();
            for (int i = 0; i < window_size; i++)
            {
                shuffle.Add(i);
            }
            Shuffle(shuffle);

            Console.WriteLine("Inserting into estimator...");
            QuantileEstimationGK<long> estimator = new QuantileEstimationGK<long>(LongComparer.DEFAULT, epsilon, 1000);
            foreach (long l in shuffle)
            {
                estimator.Insert(l);
            }

            double[] quantiles = { 0.5, 0.9, 0.95, 0.99, 1.0 };
            foreach (double q in quantiles)
            {
                long estimate = estimator.Query(q);
                long actual = (long)((q) * (window_size - 1));
                Console.WriteLine(String.Format("Estimated {0} quantile as {1} (actually {2})", q, estimate, actual));
            }
            Console.WriteLine("# of samples: " + estimator.samples.Count);
        }

        protected virtual void Shuffle<T>(List<T> source)
        {
            List<T> sortedList = new List<T>();
            Random rand = new Random(0xEADBEEF);
            while (source.Count > 0)
            {
                int position = rand.Next(source.Count);
                sortedList.Add(source[position]);
                source.RemoveAt(position);
            }

            source.AddRange(sortedList);
        }
    }
}