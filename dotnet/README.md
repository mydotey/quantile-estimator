# MyDotey ObjectPool dotnet

## NuGet package

```sh
dotnet Add package MyDotey.QuantileEstimator -v 1.0.2
```

## Usage

### Add and Get

```cs
IComparer<Double> comparer = DoubleComparer.DEFAULT;
ICalculator<Double> calculator = DoubleCalculator.DEFAULT;
IQuantileEstimator<Double> quantileEstimator = QuantileEstimators.NewClassicEstimator<Double>(
    comparer, calculator);

quantileEstimator.Add(100.0);
quantileEstimator.Add(200.0);
quantileEstimator.Add(50.0);
quantileEstimator.Add(10.0);

List<Double> quantiles = new List<Double>() { 0.25, 0.50, 0.75, 0.9 };
Dictionary<Double, Double> quantilesAndValues = quantileEstimator.Get(quantiles);
Console.WriteLine(quantilesAndValues);
```

### Concurrent

```cs
IQuantileEstimator<Double> quantileEstimator = QuantileEstimators.NewClassicEstimator<Double>(
    DoubleComparer.DEFAULT, DoubleCalculator.DEFAULT);
quantileEstimator = QuantileEstimators.NewConcurrentEstimator<Double>(quantileEstimator);
```

### Time Window

```cs
Func<IQuantileEstimator<Double>> quantileEstimatorSupplier = () => 
    QuantileEstimators.NewClassicEstimator<Double>(DoubleComparer.DEFAULT, DoubleCalculator.DEFAULT);
long timeWindowMillis = 60 * 1000;
long rotateDurationMillis = 10 * 1000;
IQuantileEstimator<Double> quantileEstimator = QuantileEstimators.NewTimeWindowEstimator<Double>(
    quantileEstimatorSupplier, timeWindowMillis, rotateDurationMillis);
```

## Algorithms

### Classic

```cs
IComparer<Double> comparer = DoubleComparer.DEFAULT;
ICalculator<Double> calculator = DoubleCalculator.DEFAULT;
IQuantileEstimator<Double> quantileEstimator = QuantileEstimators.NewClassicEstimator<Double>(
    comparer, calculator);
```

### CKMS

```cs
CkmsQuantileEstimatorConfig<Double>.Builder builder = QuantileEstimators.NewCkmsEstimatorConfigBuilder<Double>();
IComparer<Double> comparer = DoubleComparer.DEFAULT;
double quantile = 0.01; // [0, 1]
double error = 0.001; // [0, 1]
builder.SetComparer(comparer).AddQuantileConfig(quantile, error)
    .AddQuantileConfig(0.25, 0.01).AddQuantileConfig(0.5, 0.01).AddQuantileConfig(0.75, 0.01)
    .AddQuantileConfig(0.99, 0.001);
IQuantileEstimator<Double> quantileEstimator = QuantileEstimators.NewCkmsEstimator<Double>(builder.Build());
```

### GK

```cs
IComparer<Double> comparer = DoubleComparer.DEFAULT;
double error = 0.01; // [0, 1]
int compactThreshold = 200; // most sample count
IQuantileEstimator<Double> quantileEstimator = QuantileEstimators.NewGkEstimator<Double>(
    comparer, 0.01, 200);
```

### KLL

```cs
IComparer<Double> comparer = DoubleComparer.DEFAULT;
int k = 200; // first compactor size
double c = 2.0 / 3.0; // compact rate, [0.5, 1)
IQuantileEstimator<Double> quantileEstimator = QuantileEstimators.NewKllEstimator<Double>(
    DoubleComparer.DEFAULT, 200, 2.0 / 3.0);
```
