package lists

object ListsBenchmark:
  private val l = (1 to 1000).toList

  /**
   * Without plugin:
┌────────────┬───────┬───────┬────────────┬────────┬───────────────┐
│ Name       │ Last  │ Min   │ Total      │ Calls  │ Per Call (ns) │
├────────────┼───────┼───────┼────────────┼────────┼───────────────┤
│ filter-map │ 3.238 │ 2.879 │ 39.499.193 │ 12.299 │ 3211577       │
└────────────┴───────┴───────┴────────────┴────────┴───────────────┘
   */
  def runFilterMap(): List[Int] =
    var i = 0
    var r = List.empty[Int]
    while i < 1000 do
      l.filter(_ > 500).map(_ * 2)
      i += 1
    r
