package codeoptimizer

import dotty.tools.dotc.core.Contexts.*

class FilterMapOptimizer(using Context) extends Optimizer2With2ndTypeArgsListSeqIterable:
  override def method1     = "filter"
  override def method2     = "map"
  override def replacement = "filterMap"
