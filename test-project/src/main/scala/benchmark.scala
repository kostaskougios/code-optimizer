import game.utils.Timings

/**
   * Without plugin:
┌──────────────────┬───────┬───────┬───────────┬───────┬───────────────┐
│ Name             │ Last  │ Min   │ Total     │ Calls │ Per Call (ns) │
├──────────────────┼───────┼───────┼───────────┼───────┼───────────────┤
│ filter-map-large │ 3.169 │ 2.847 │ 8.663.708 │ 2.699 │ 3209969       │
│ filter-map-small │ 3.364 │ 2.725 │ 9.127.688 │ 2.699 │ 3381877       │
└──────────────────┴───────┴───────┴───────────┴───────┴───────────────┘
  */

@main
def benchmark(): Unit =
  var i = 0
  while true do
    Timings.profile("filter-map-large"):
      lists.ListsBenchmark.runFilterMap()
    Timings.profile("filter-map-small"):
      lists.ListsBenchmark.runFilterMapSmallList()

    i += 1
    if i % 100 == 99 then println(Timings.profilingTable)
