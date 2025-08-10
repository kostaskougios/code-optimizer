package codeoptimizer

import dotty.tools.dotc.core.Contexts.*
import dotty.tools.dotc.plugins.*

class OptimizerPlugin extends StandardPlugin:
  override val name: String        = "OptimizerPlugin"
  override val description: String = "Optimizes scala collection operations"

  override def init(options: List[String]): List[PluginPhase] = List(
    new SeqFilterForallOptimizer
  )
