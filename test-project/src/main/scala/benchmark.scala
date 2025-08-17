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
 [info] │ filter-forall-large      │ 8.296 │ 7.896 │ 3.231.684 │ 399   │ 8099459       │
 [info] │ filter-map-empty         │ 2.785 │ 2.612 │ 1.086.139 │ 399   │ 2722155       │
 [info] │ filter-map-large         │ 4.634 │ 3.918 │ 1.701.031 │ 399   │ 4263237       │
 [info] │ filter-map-one           │ 6.323 │ 5.994 │ 2.463.765 │ 399   │ 6174849       │
 [info] │ filter-map-small         │ 4.745 │ 4.523 │ 1.856.692 │ 399   │ 4653365       │
 [info] │ filter-map-three         │ 9.468 │ 9.117 │ 3.710.649 │ 399   │ 9299873       │
 [info] │ filter-map-two           │ 7.749 │ 6.765 │ 2.769.977 │ 399   │ 6942298       │
 [info] │ withFilter-foreach-large │ 1.803 │ 1.619 │ 702.874   │ 399   │ 1761590       │
 [info] │ withFilter-foreach-small │ 1.456 │ 1.312 │ 561.189   │ 399   │ 1406491       │
 [info] └──────────────────────────┴───────┴───────┴───────────┴───────┴───────────────┘

With plugin:

 [info] ┌──────────────────────────┬───────┬───────┬───────────┬───────┬───────────────┐
 [info] │ Name                     │ Last  │ Min   │ Total     │ Calls │ Per Call (ns) │
 [info] ├──────────────────────────┼───────┼───────┼───────────┼───────┼───────────────┤
 [info] │ filter-forall-large      │ 3.383 │ 3.104 │ 2.751.193 │ 799   │ 3443296       │
 [info] │ filter-map-empty         │ 0     │ 0     │ 138       │ 799   │ 173           │
 [info] │ filter-map-large         │ 2.389 │ 2.270 │ 2.022.390 │ 799   │ 2531151       │
 [info] │ filter-map-one           │ 2.021 │ 1.986 │ 1.664.556 │ 799   │ 2083299       │
 [info] │ filter-map-small         │ 1.561 │ 1.426 │ 1.260.584 │ 799   │ 1577702       │
 [info] │ filter-map-three         │ 2.870 │ 2.854 │ 2.428.401 │ 799   │ 3039301       │
 [info] │ filter-map-two           │ 2.082 │ 2.065 │ 1.715.078 │ 799   │ 2146531       │
 [info] │ withFilter-foreach-large │ 1.724 │ 1.600 │ 1.389.991 │ 799   │ 1739663       │
 [info] │ withFilter-foreach-small │ 1.276 │ 1.210 │ 1.033.756 │ 799   │ 1293812       │
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

    i += 1
    if i % 100 == 99 then println(Timings.profilingTable)
