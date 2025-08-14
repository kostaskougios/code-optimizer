package game.utils

import layoutz.*

import scala.collection.concurrent.TrieMap
import java.text.DecimalFormatSymbols
import java.text.DecimalFormat
import java.util.Locale

object Timings:

  private def toMs(ns: Long) = ns / 1000000

  /** Returns the time in NANOSECONDS and the result
    */
  def time[R](f: => R): (Long, R) =
    val start = System.nanoTime()
    val r     = f
    val dt    = System.nanoTime() - start
    (dt, r)

  def timeInMs[R](f: => R): (Long, R) =
    val start = System.currentTimeMillis()
    val r     = f
    val dt    = System.currentTimeMillis() - start
    (dt, r)

  private case class Stats(name: String, recorded: List[Long]):
    def lastTime: Long      = recorded.head
    def totalTime: Long     = recorded.sum
    def totalCalls: Long    = recorded.length
    def minTime: Long       = recorded.min
    def maxTime: Long       = recorded.max
    def add(t: Long): Stats =
      val newRecorded = t :: recorded.take(99)
      copy(recorded = newRecorded)

  private val stats      = TrieMap.empty[String, Stats]
  def resetStats(): Unit = stats.clear()

  def trackTimingStats[R](name: String)(f: => R): R =
    val (t, r) = time(f)
    stats.updateWith(name): statsOpt =>
      val s = statsOpt.getOrElse(Stats(name, Nil))
      Some(s.add(t))
    r

  private val IgnoreFirstNCalls = 100
  private case class Profiling(lastTime: Long, totalTime: Long, totalCalls: Long, minTime: Long = Long.MaxValue):
    def add(t: Long): Profiling = Profiling(t, if totalCalls < IgnoreFirstNCalls then 0 else totalTime + t, totalCalls + 1, if t < minTime then t else minTime)

  private val profiling                    = TrieMap.empty[String, Profiling]
  def profile[R](name: String)(f: => R): R =
    val (t, r) = time(f)
    profiling.updateWith(name): profOpt =>
      val s = profOpt.getOrElse(Profiling(0, 0, 0))
      Some(s.add(t))
    r

  private val Headers          = Seq("Name", "Last", "Avg", "Total", "Calls", "MinTime", "MaxTime")
  private val ProfilingHeaders = Seq("Name", "Last", "Min", "Total", "Calls", "Per Call (ns)")
  // this is lazy so that it can be started only when the game is running
  def timingsTable             =
    table(
      Headers,
      for (name, stats) <- stats.toList.sortBy(_._1)
      yield Seq(
        name,
        format(stats.lastTime / 1000),
        format((stats.totalTime / stats.totalCalls) / 1000),
        format(stats.totalTime / 1000),
        stats.totalCalls.toString,
        format(stats.minTime / 1000),
        format(stats.maxTime / 1000)
      )
    ).render

  def profilingTable =
    table(
      ProfilingHeaders,
      profiling.toSeq
        .sortBy(_._1)
        .map: (name, p) =>
          Seq(
            name,
            format(p.lastTime / 1000),
            format(p.minTime / 1000),
            format(p.totalTime / 1000),
            format(p.totalCalls - IgnoreFirstNCalls),
            (p.totalTime / math.max(1, p.totalCalls - IgnoreFirstNCalls)).toString
          )
    ).render

  def clear(): Unit = stats.clear()

  private val symbols = new DecimalFormatSymbols(Locale.UK)
  symbols.setGroupingSeparator('.') // use dot as thousands separator

  private val formatter = new DecimalFormat("#,###", symbols)

  def format(i: Long): String = formatter.format(i)
