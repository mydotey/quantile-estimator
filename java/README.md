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
```java
QuantileEstimator<Double> quantileEstimator = QuantileEstimators.newClassicEstimator(DoubleComparator.DEFAULT,
        DoubleCalculator.DEFAULT);
quantileEstimator = QuantileEstimators.newConcurrentEstimator(quantileEstimator);

quantileEstimator.add(100.0);
quantileEstimator.add(200.0);
quantileEstimator.add(50.0);
quantileEstimator.add(10.0);

List<Double> quantiles = Arrays.asList(0.25, 0.50, 0.75, 0.9);
Map<Double, Double> quantilesAndValues = quantileEstimator.get(quantiles);
System.out.println(quantilesAndValues);
```

## Classic
```java
QuantileEstimator<Double> quantileEstimator = QuantileEstimators.newClassicEstimator(DoubleComparator.DEFAULT,
        DoubleCalculator.DEFAULT);
```

## CKMS
```java
CkmsQuantileEstimatorConfig.Builder<Double> builder = QuantileEstimators.newCkmsEstimatorConfigBuilder();
builder.setComparator(DoubleComparator.DEFAULT).addQuantileConfig(0.01, 0.001).addQuantileConfig(0.25, 0.01)
        .addQuantileConfig(0.5, 0.01).addQuantileConfig(0.75, 0.01).addQuantileConfig(0.99, 0.001);
QuantileEstimator<Double> quantileEstimator = QuantileEstimators.newCkmsEstimator(builder.build());
```

## GK
```java
QuantileEstimator<Double> quantileEstimator = QuantileEstimators.newGkEstimator(DoubleComparator.DEFAULT, 0.01, 200);
```

## KLL
```java
QuantileEstimator<Double> quantileEstimator = QuantileEstimators.newKllEstimator(DoubleComparator.DEFAULT, 200, 2.0 / 3.0);
```
