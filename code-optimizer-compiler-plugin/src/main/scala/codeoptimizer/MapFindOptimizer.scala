package codeoptimizer
import dotty.tools.dotc.core.Contexts.*

class MapFindOptimizer(using Context) extends OptimizerCall1TypeCall2Iterable:
  override def method1     = "map"
  override def method2     = "find"
  override def replacement = "mapFind"
