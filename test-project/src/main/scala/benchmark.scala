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
 [info] ┌──────────────────────────┬────────┬───────┬───────────┬───────┬───────────────┐
 [info] │ Name                     │ Last   │ Min   │ Total     │ Calls │ Per Call (ns) │
 [info] ├──────────────────────────┼────────┼───────┼───────────┼───────┼───────────────┤
 [info] │ filter-forall-large      │ 8.796  │ 8.338 │ 2.534.480 │ 299   │ 8476523       │
 [info] │ filter-forall-small      │ 4.064  │ 3.837 │ 1.188.949 │ 299   │ 3976419       │
 [info] │ filter-map-empty         │ 2.671  │ 2.638 │ 805.849   │ 299   │ 2695148       │
 [info] │ filter-map-large         │ 4.108  │ 4.057 │ 1.318.183 │ 299   │ 4408640       │
 [info] │ filter-map-one           │ 6.039  │ 6.002 │ 1.826.647 │ 299   │ 6109190       │
 [info] │ filter-map-small         │ 4.727  │ 4.680 │ 1.427.419 │ 299   │ 4773978       │
 [info] │ filter-map-three         │ 10.011 │ 9.354 │ 2.848.163 │ 299   │ 9525629       │
 [info] │ filter-map-two           │ 7.699  │ 6.934 │ 2.114.193 │ 299   │ 7070880       │
 [info] │ withFilter-foreach-large │ 1.818  │ 1.641 │ 522.902   │ 299   │ 1748838       │
 [info] │ withFilter-foreach-small │ 1.469  │ 1.333 │ 424.467   │ 299   │ 1419624       │
 [info] └──────────────────────────┴────────┴───────┴───────────┴───────┴───────────────┘

With plugin:

 [info] ┌──────────────────────────┬───────┬───────┬───────────┬───────┬───────────────┐
 [info] │ Name                     │ Last  │ Min   │ Total     │ Calls │ Per Call (ns) │
 [info] ├──────────────────────────┼───────┼───────┼───────────┼───────┼───────────────┤
 [info] │ filter-forall-large      │ 3.367 │ 3.023 │ 1.966.913 │ 599   │ 3283661       │
 [info] │ filter-forall-small      │ 610   │ 558   │ 360.882   │ 599   │ 602474        │
 [info] │ filter-map-empty         │ 0     │ 0     │ 89        │ 599   │ 149           │
 [info] │ filter-map-large         │ 2.422 │ 2.281 │ 1.505.961 │ 599   │ 2514126       │
 [info] │ filter-map-one           │ 2.024 │ 1.997 │ 1.237.197 │ 599   │ 2065439       │
 [info] │ filter-map-small         │ 1.437 │ 1.414 │ 912.904   │ 599   │ 1524047       │
 [info] │ filter-map-three         │ 3.129 │ 3.121 │ 2.013.434 │ 599   │ 3361326       │
 [info] │ filter-map-two           │ 2.087 │ 2.066 │ 1.275.853 │ 599   │ 2129972       │
 [info] │ withFilter-foreach-large │ 1.679 │ 1.561 │ 999.716   │ 599   │ 1668975       │
 [info] │ withFilter-foreach-small │ 1.274 │ 1.208 │ 763.855   │ 599   │ 1275218       │
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

    Timings.profile("filter-forall-large"):
      lists.ListsBenchmark.runFilterForallLarge()
    Timings.profile("filter-forall-small"):
      lists.ListsBenchmark.runFilterForallSmall()

    Timings.profile("map-find-large"):
      lists.ListsBenchmark.runMapFindLarge()
    Timings.profile("map-find-small"):
      lists.ListsBenchmark.runMapFindSmall()

    i += 1
    if i % 100 == 99 then println(Timings.profilingTable)
