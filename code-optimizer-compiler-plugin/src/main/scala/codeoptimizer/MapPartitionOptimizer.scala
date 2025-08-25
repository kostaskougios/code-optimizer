package codeoptimizer

import dotty.tools.dotc.core.Contexts.*

class MapPartitionOptimizer(using Context) extends OptimizerCall1TypeCall2Iterable:
  override def method1     = "map"
  override def method2     = "partition"
  override def replacement = "mapPartition"
