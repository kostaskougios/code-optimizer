package codeoptimizer

import dotty.tools.dotc.core.Contexts.*

class WithFilterForeachOptimizer(using Context) extends OptimizerCall1Call2TypeIterable:
  override def method1     = "withFilter"
  override def method2     = "foreach"
  override def replacement = "withFilterForeach"
