package codeoptimizer

import dotty.tools.dotc.core.Contexts.*
import dotty.tools.dotc.plugins.*

class FilterMapOptimizerPlugin extends StandardPlugin:
  val name: String        = "filterMapOptimizerPlugin"
  val description: String =
    "Optimizes scala collection operations"

  override def init(options: List[String]): List[PluginPhase] =
    List(new FilterMapOptimizer)
