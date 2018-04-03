using System;

using MyDotey.Quantile.Value;
using MyDotey.Quantile.Facade;
using MyDotey.Quantile.Ckms;

/**
 * @author koqizhao
 *
 * Apr 1, 2018
 */
namespace MyDotey.Quantile.Tests
{
    public class CkmsQuantileEstimatorTest : QuantileEstimatorTest
    {
        protected override void AddQuantileEsimators()
        {
            CkmsQuantileEstimatorConfig<long>.Builder builder = QuantileEstimators.NewCkmsEstimatorConfigBuilder<long>();
            builder.SetComparer(LongComparer.DEFAULT).AddQuantileConfig(0.01, 0.001).AddQuantileConfig(0.25, 0.01)
                    .AddQuantileConfig(0.5, 0.01).AddQuantileConfig(0.75, 0.01).AddQuantileConfig(0.99, 0.001);
            _estimators[typeof(long)] = QuantileEstimators.NewCkmsEstimator(builder.Build());

            CkmsQuantileEstimatorConfig<int>.Builder builder2 = QuantileEstimators.NewCkmsEstimatorConfigBuilder<int>();
            builder2.SetComparer(IntComparer.DEFAULT).AddQuantileConfig(0.01, 0.001).AddQuantileConfig(0.25, 0.01)
                        .AddQuantileConfig(0.5, 0.01).AddQuantileConfig(0.75, 0.01).AddQuantileConfig(0.99, 0.001);
            _estimators[typeof(int)] = QuantileEstimators.NewCkmsEstimator(builder2.Build());
        }
    }
}