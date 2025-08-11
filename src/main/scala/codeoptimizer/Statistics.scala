package codeoptimizer

import scala.collection.concurrent.TrieMap

object Statistics:
  private val m = TrieMap.empty[String, Int]

  def clear(): Unit = m.clear()

  def inc(name: String): Unit = inc(name, 1)

  def inc(name: String, by: Int): Unit = m.updateWith(name):
    case Some(count) => Some(count + by)
    case None        => Some(by)

  def inc(names: IterableOnce[String]): Unit = for n <- names.iterator do inc(n)

  private val Headers = Seq("Name", "Count")
  // this is lazy so that it can be started only when the game is running

  def toStatisticsString: String =
    val rows =
      for (name, count) <- m.toList.sortBy(_._2).reverse
      yield Seq(name, StringUtils.format(count.toLong))
    layoutz.table(Headers, rows).render
