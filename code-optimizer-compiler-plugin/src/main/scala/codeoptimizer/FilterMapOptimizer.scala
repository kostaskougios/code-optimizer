package codeoptimizer

import dotty.tools.dotc.core.Contexts.*

class FilterMapOptimizer(using Context) extends OptimizerCall1Call2TypeIterable:
  override def method1     = "filter"
  override def method2     = "map"
  override def replacement = "filterMap"
