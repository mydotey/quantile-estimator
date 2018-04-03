# Quantile Estimator

## maven dependency

```xml
<dependency>
    <groupId>org.mydotey.quantile</groupId>
    <artifactId>quantile-estimator</artifactId>
    <version>1.0.1</version>
</dependency>
```

## Usage

### Add and Get

```java
Comparator<Double> comparator = DoubleComparator.DEFAULT;
Calculator<Double> calculator = DoubleCalculator.DEFAULT;
QuantileEstimator<Double> quantileEstimator = QuantileEstimators.newClassicEstimator(
    comparator, calculator);

quantileEstimator.add(100.0);
quantileEstimator.add(200.0);
quantileEstimator.add(50.0);
quantileEstimator.add(10.0);

List<Double> quantiles = Arrays.asList(0.25, 0.50, 0.75, 0.9);
Map<Double, Double> quantilesAndValues = quantileEstimator.get(quantiles);
System.out.println(quantilesAndValues);
```

### Concurrent

```java
QuantileEstimator<Double> quantileEstimator = QuantileEstimators.newClassicEstimator(
    DoubleComparator.DEFAULT, DoubleCalculator.DEFAULT);
quantileEstimator = QuantileEstimators.newConcurrentEstimator(quantileEstimator);
```

### Time Window

```java
Supplier<QuantileEstimator<Double>> quantileEstimatorSupplier = () -> 
    QuantileEstimators.newClassicEstimator(DoubleComparator.DEFAULT, DoubleCalculator.DEFAULT);
long timeWindowMillis = TimeUnit.SECONDS.toMillis(60);
long rotateDurationMillis = TimeUnit.SECONDS.toMillis(10);
QuantileEstimator<Double> quantileEstimator = QuantileEstimators.newTimeWindowEstimator(
    quantileEstimatorSupplier, timeWindowMillis, rotateDurationMillis);
```

## Algorithms

### Classic

```java
Comparator<Double> comparator = DoubleComparator.DEFAULT;
Calculator<Double> calculator = DoubleCalculator.DEFAULT;
QuantileEstimator<Double> quantileEstimator = QuantileEstimators.newClassicEstimator(
    comparator, calculator);
```

### CKMS

```java
CkmsQuantileEstimatorConfig.Builder<Double> builder = QuantileEstimators.newCkmsEstimatorConfigBuilder();
Comparator<Double> comparator = DoubleComparator.DEFAULT;
double quantile = 0.01; // [0, 1]
double error = 0.001; // [0, 1]
builder.setComparator(comparator).addQuantileConfig(quantile, error)
    .addQuantileConfig(0.25, 0.01).addQuantileConfig(0.5, 0.01).addQuantileConfig(0.75, 0.01)
    .addQuantileConfig(0.99, 0.001);
QuantileEstimator<Double> quantileEstimator = QuantileEstimators.newCkmsEstimator(builder.build());
```

### GK

```java
Comparator<Double> comparator = DoubleComparator.DEFAULT;
double error = 0.01; // [0, 1]
int compactThreshold = 200; // most sample count
QuantileEstimator<Double> quantileEstimator = QuantileEstimators.newGkEstimator(
    comparator, 0.01, 200);
```

### KLL

```java
Comparator<Double> comparator = DoubleComparator.DEFAULT;
int k = 200; // first compactor size
double c = 2.0 / 3.0; // compact rate
QuantileEstimator<Double> quantileEstimator = QuantileEstimators.newKllEstimator(
    DoubleComparator.DEFAULT, 200, 2.0 / 3.0);
```
