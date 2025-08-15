import game.utils.Timings

/**
 *
 * What can be useful optimization:
 * - from scala3 compiler:
 * ┌───────────────────────────────────┬───────┐
 * │ Name                              │ Count │
 * ├───────────────────────────────────┼───────┤
 * │ List.withFilter.foreach           │ 85    │
 * │ List.withFilter.map               │ 40    │
 * │ List.filter.map                   │ 13    │
 * │ View.withFilter.foreach           │ 11    │
 * │ List.find.getOrElse               │ 10    │
 * │ Map.withFilter.foreach            │ 10    │
 * 
 * Without plugin:
 [info] ┌──────────────────────────┬───────┬───────┬───────────┬───────┬───────────────┐
 [info] │ Name                     │ Last  │ Min   │ Total     │ Calls │ Per Call (ns) │
 [info] ├──────────────────────────┼───────┼───────┼───────────┼───────┼───────────────┤
 [info] │ filter-map-empty         │ 2.682 │ 2.645 │ 1.078.153 │ 399   │ 2702139       │
 [info] │ filter-map-large         │ 4.152 │ 3.950 │ 1.674.041 │ 399   │ 4195591       │
 [info] │ filter-map-one           │ 6.289 │ 6.233 │ 2.526.858 │ 399   │ 6332978       │
 [info] │ filter-map-small         │ 4.672 │ 4.596 │ 1.864.509 │ 399   │ 4672954       │
 [info] │ filter-map-three         │ 9.263 │ 9.216 │ 3.739.389 │ 399   │ 9371902       │
 [info] │ filter-map-two           │ 7.831 │ 6.992 │ 2.860.944 │ 399   │ 7170287       │
 [info] │ withFilter-foreach-large │ 1.793 │ 1.681 │ 699.051   │ 399   │ 1752008       │
 [info] │ withFilter-foreach-small │ 1.408 │ 1.346 │ 565.122   │ 399   │ 1416347       │
 [info] └──────────────────────────┴───────┴───────┴───────────┴───────┴───────────────┘

With plugin:

 [info] ┌──────────────────┬───────┬───────┬───────────┬───────┬───────────────┐
 [info] │ Name             │ Last  │ Min   │ Total     │ Calls │ Per Call (ns) │
 [info] ├──────────────────┼───────┼───────┼───────────┼───────┼───────────────┤
 [info] │ filter-map-empty │ 0     │ 0     │ 285       │ 3.199 │ 89            │
 [info] │ filter-map-large │ 2.422 │ 2.174 │ 7.565.996 │ 3.199 │ 2365112       │
 [info] │ filter-map-one   │ 1.936 │ 1.903 │ 6.596.459 │ 3.199 │ 2062038       │
 [info] │ filter-map-small │ 1.675 │ 1.393 │ 4.827.590 │ 3.199 │ 1509093       │
 [info] │ filter-map-three │ 3.208 │ 2.786 │ 9.856.157 │ 3.199 │ 3081012       │
 [info] │ filter-map-two   │ 2.397 │ 2.004 │ 6.884.150 │ 3.199 │ 2151969       │
 [info] └──────────────────┴───────┴───────┴───────────┴───────┴───────────────┘
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

    Timings.profile("withFilter-foreach-large"):
      lists.ListsBenchmark.runWithFilterForeachLarge()
    Timings.profile("withFilter-foreach-small"):
      lists.ListsBenchmark.runWithFilterForeachSmall()

    i += 1
    if i % 100 == 99 then println(Timings.profilingTable)
