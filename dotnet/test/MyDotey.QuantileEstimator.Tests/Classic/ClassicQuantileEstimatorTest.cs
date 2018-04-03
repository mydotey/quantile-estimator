using System;

using MyDotey.Quantile.Value;
using MyDotey.Quantile.Facade;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
namespace MyDotey.Quantile.Tests
{
    public class ClassicQuantileEstimatorTest : QuantileEstimatorTest
    {
        protected override void AddQuantileEsimators()
        {
            _estimators[typeof(long)] =
                    QuantileEstimators.NewClassicEstimator<long>(LongComparer.DEFAULT, LongCalculator.DEFAULT);
            _estimators[typeof(int)] = QuantileEstimators.NewClassicEstimator<int>(IntComparer.DEFAULT, IntCalculator.DEFAULT);
        }
    }
}