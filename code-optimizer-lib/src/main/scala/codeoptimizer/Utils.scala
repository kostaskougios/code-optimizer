package codeoptimizer

object Utils:
  def notYetOptimized(x: AnyRef, method: String): Unit =
    System.err.println(s"[code-optimizer-lib] Warning: $method for ${x.getClass} not yet optimized")
