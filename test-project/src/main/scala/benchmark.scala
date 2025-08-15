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

 [info] ┌──────────────────────────┬───────┬───────┬───────────┬───────┬───────────────┐
 [info] │ Name                     │ Last  │ Min   │ Total     │ Calls │ Per Call (ns) │
 [info] ├──────────────────────────┼───────┼───────┼───────────┼───────┼───────────────┤
 [info] │ filter-map-empty         │ 0     │ 0     │ 140       │ 899   │ 155           │
 [info] │ filter-map-large         │ 2.394 │ 2.283 │ 2.185.920 │ 899   │ 2431502       │
 [info] │ filter-map-one           │ 1.986 │ 1.943 │ 1.804.000 │ 899   │ 2006674       │
 [info] │ filter-map-small         │ 1.438 │ 1.419 │ 1.338.027 │ 899   │ 1488350       │
 [info] │ filter-map-three         │ 2.878 │ 2.870 │ 2.684.693 │ 899   │ 2986311       │
 [info] │ filter-map-two           │ 2.038 │ 2.029 │ 1.867.644 │ 899   │ 2077469       │
 [info] │ withFilter-foreach-large │ 1.661 │ 1.556 │ 1.497.965 │ 899   │ 1666257       │
 [info] │ withFilter-foreach-small │ 1.258 │ 1.246 │ 1.150.659 │ 899   │ 1279933       │
 [info] └──────────────────────────┴───────┴───────┴───────────┴───────┴───────────────┘
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
