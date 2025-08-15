import game.utils.Timings

/**
   * Without plugin:
┌──────────────────┬───────┬───────┬───────────┬───────┬───────────────┐
│ Name             │ Last  │ Min   │ Total     │ Calls │ Per Call (ns) │
├──────────────────┼───────┼───────┼───────────┼───────┼───────────────┤
│ filter-map-empty │ 3.150 │ 2.971 │ 4.996.974 │ 1.599 │ 3125062       │
│ filter-map-large │ 3.298 │ 2.892 │ 5.299.489 │ 1.599 │ 3314252       │
│ filter-map-small │ 3.341 │ 2.612 │ 5.343.379 │ 1.599 │ 3341700       │
└──────────────────┴───────┴───────┴───────────┴───────┴───────────────┘
  */

@main
def benchmark(): Unit =
  var i = 0
  while true do
    Timings.profile("filter-map-large"):
      lists.ListsBenchmark.runFilterMapLarge()
    Timings.profile("filter-map-small"):
      lists.ListsBenchmark.runFilterMapSmallList()
    Timings.profile("filter-map-empty"):
      lists.ListsBenchmark.runFilterMapEmptyList()
    Timings.profile("filter-map-one"):
      lists.ListsBenchmark.runFilterMapListOfOne()
    Timings.profile("filter-map-two"):
      lists.ListsBenchmark.runFilterMapListOfTwo()
    Timings.profile("filter-map-three"):
      lists.ListsBenchmark.runFilterMapListOfThree()

    i += 1
    if i % 100 == 99 then println(Timings.profilingTable)
