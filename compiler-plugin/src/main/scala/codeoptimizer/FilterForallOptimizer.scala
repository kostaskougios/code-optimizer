package codeoptimizer

import dotty.tools.dotc.core.Contexts.*

class FilterForallOptimizer(using Context) extends OptimizerCall1Call2Iterable:
  override def method1     = "filter"
  override def method2     = "forall"
  override def replacement = "filterForall"
