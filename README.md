# Quantile Estimator

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Commmon Library collecting multiple quantile algorithms for streaming data, concurrent & easy to use.

Implemented in java/dotnet separately. Code is clean. Easy to read.

## Usage

* java
  https://github.com/mydotey/quantile-estimator/tree/master/java

* dotnet
  https://github.com/mydotey/quantile-estimator/tree/master/dotnet

## Features

* Thread Safe
  * add value: lock free

  * batch get quantiles: synchronized

* Time Window
  * better accuracy by time window rotate

## Algorithms

* Classic Algorithm
  * accurate

  * no data compaction

* CKMS Algorithm
  * data compaction & space efficient

  * predefined quantile error

  * most used approximate algorithm

* GK Algorithm
  * data compaction & space efficient

* KLL Algorithm
  * data compaction & space efficient

  * new algorithm

## Papers

* [Cormode, Korn, Muthukrishnan, and Srivastava. "Effective Computation of Biased Quantiles over Data Streams" in ICDE 2005](http://www.cs.rutgers.edu/~muthu/bquant.pdf)

* [Greenwald and Khanna. "Space-efficient online computation of quantile summaries" in SIGMOD 2001](http://infolab.stanford.edu/~datar/courses/cs361a/papers/quantiles.pdf)

* [Zohar Karnin, Kevin Lang and Edo Liberty. "Optimal Quantile Approximation in Streams" in FOCS 2016](http://arxiv.org/abs/1603.05346)

## Others' Projects

* https://github.com/umbrant/QuantileEstimation

* https://github.com/edoliberty/streaming-quantiles

* https://github.com/tdunning/t-digest

## Developers

* Qiang Zhao <koqizhao@outlook.com>
