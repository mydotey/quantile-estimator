using System;

using MyDotey.Quantile.Value;
using MyDotey.Quantile.Facade;

using Xunit;

/**
 * @author koqizhao
 *
 * Mar 30, 2018
 */
namespace MyDotey.Quantile.Tests
{
    public class KllQuantileEstimatorTest : QuantileEstimatorTest
    {
        protected override void AddQuantileEsimators()
        {
            _estimators[typeof(long)] = QuantileEstimators.NewKllEstimator(LongComparer.DEFAULT, 100);
            _estimators[typeof(int)] = QuantileEstimators.NewKllEstimator(IntComparer.DEFAULT, 100);
        }

        [Fact]
        public override void Test5()
        {
            int count = 100;
            int upperBound = 1000;
            double errorRate = 0.02;
            Test5Internal(count, upperBound, errorRate);
        }
 
        [Fact]
        public override void Test5_2()
        {
            int count = 1000;
            int upperBound = 1000;
            double errorRate = 0.02;
            Test5Internal(count, upperBound, errorRate);
        }

        [Fact]
        public override void Test5_3()
        {
            int count = 10000;
            int upperBound = 1000;
            double errorRate = 0.02;
            Test5Internal(count, upperBound, errorRate);
        }
    }
}