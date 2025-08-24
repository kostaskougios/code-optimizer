package codeoptimizer

import dotty.tools.dotc.core.Contexts.*

class MapFilterOptimizer(using Context) extends OptimizerCall1TypeCall2Iterable:
  override def method1     = "map"
  override def method2     = "filter"
  override def replacement = "mapFilter"
