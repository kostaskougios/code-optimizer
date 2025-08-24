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
 [info] ┌──────────────────────────┬────────┬────────┬───────────┬───────┬───────────────┐
 [info] │ Name                     │ Last   │ Min    │ Total     │ Calls │ Per Call (ns) │
 [info] ├──────────────────────────┼────────┼────────┼───────────┼───────┼───────────────┤
 [info] │ filter-forall-large      │ 8.077  │ 7.854  │ 3.199.433 │ 399   │ 8018631       │
 [info] │ filter-forall-small      │ 3.490  │ 3.385  │ 1.402.557 │ 399   │ 3515180       │
 [info] │ filter-map-empty         │ 3.574  │ 3.360  │ 1.433.436 │ 399   │ 3592573       │
 [info] │ filter-map-large         │ 3.956  │ 3.819  │ 1.610.280 │ 399   │ 4035790       │
 [info] │ filter-map-one           │ 8.321  │ 8.220  │ 3.329.837 │ 399   │ 8345457       │
 [info] │ filter-map-small         │ 6.105  │ 6.063  │ 2.463.691 │ 399   │ 6174665       │
 [info] │ filter-map-three         │ 12.384 │ 12.209 │ 4.931.908 │ 399   │ 12360672      │
 [info] │ filter-map-two           │ 8.731  │ 8.538  │ 3.456.047 │ 399   │ 8661772       │
 [info] │ map-find-large           │ 10.755 │ 10.527 │ 4.320.198 │ 399   │ 10827565      │
 [info] │ map-find-small           │ 5.091  │ 4.994  │ 2.033.483 │ 399   │ 5096449       │
 [info] │ withFilter-foreach-large │ 1.740  │ 1.566  │ 692.876   │ 399   │ 1736533       │
 [info] │ withFilter-foreach-small │ 1.478  │ 1.312  │ 581.308   │ 399   │ 1456912       │
 [info] └──────────────────────────┴────────┴────────┴───────────┴───────┴───────────────┘

With plugin:

 [info] ┌──────────────────────────┬───────┬───────┬────────────┬───────┬───────────────┐
 [info] │ Name                     │ Last  │ Min   │ Total      │ Calls │ Per Call (ns) │
 [info] ├──────────────────────────┼───────┼───────┼────────────┼───────┼───────────────┤
 [info] │ filter-forall-large      │ 3.126 │ 2.903 │ 15.288.392 │ 4.999 │ 3058290       │
 [info] │ filter-forall-small      │ 590   │ 532   │ 2.927.514  │ 4.999 │ 585620        │
 [info] │ filter-map-empty         │ 0     │ 0     │ 316        │ 4.999 │ 63            │
 [info] │ filter-map-large         │ 2.421 │ 2.107 │ 12.226.005 │ 4.999 │ 2445690       │
 [info] │ filter-map-one           │ 2.033 │ 1.825 │ 10.182.280 │ 4.999 │ 2036863       │
 [info] │ filter-map-small         │ 1.601 │ 1.330 │ 7.682.128  │ 4.999 │ 1536733       │
 [info] │ filter-map-three         │ 3.147 │ 2.715 │ 15.094.484 │ 4.999 │ 3019500       │
 [info] │ filter-map-two           │ 2.100 │ 1.907 │ 10.555.205 │ 4.999 │ 2111463       │
 [info] │ map-find-large           │ 895   │ 798   │ 4.479.633  │ 4.999 │ 896105        │
 [info] │ map-find-small           │ 1.581 │ 1.398 │ 7.835.796  │ 4.999 │ 1567472       │
 [info] │ withFilter-foreach-large │ 1.646 │ 1.472 │ 8.086.619  │ 4.999 │ 1617647       │
 [info] │ withFilter-foreach-small │ 1.346 │ 1.133 │ 6.282.658  │ 4.999 │ 1256783       │
 [info] └──────────────────────────┴───────┴───────┴────────────┴───────┴───────────────┘
*/

@main
def benchmark(): Unit =
  var i = 0
  while true do
//    Timings.profile("list-filter-map-large"):
//      benchmarks.ListsBenchmark.runFilterMapLarge()
//    Timings.profile("list-filter-map-small"):
//      benchmarks.ListsBenchmark.runFilterMapSmall()
//    Timings.profile("list-filter-map-empty"):
//      benchmarks.ListsBenchmark.runFilterMapEmptyList()
//    Timings.profile("list-filter-map-one"):
//      benchmarks.ListsBenchmark.runFilterMapListOfOne()
//    Timings.profile("list-filter-map-two"):
//      benchmarks.ListsBenchmark.runFilterMapListOfTwo()
//    Timings.profile("list-filter-map-three"):
//      benchmarks.ListsBenchmark.runFilterMapListOfThree()
//
//    Timings.profile("list-withFilter-foreach-large"):
//      benchmarks.ListsBenchmark.runWithFilterForeachLarge()
//    Timings.profile("list-withFilter-foreach-small"):
//      benchmarks.ListsBenchmark.runWithFilterForeachSmall()
//
//    Timings.profile("list-filter-forall-large"):
//      benchmarks.ListsBenchmark.runFilterForallLarge()
//    Timings.profile("list-filter-forall-small"):
//      benchmarks.ListsBenchmark.runFilterForallSmall()
//
//    Timings.profile("list-map-find-large"):
//      benchmarks.ListsBenchmark.runMapFindLarge()
//    Timings.profile("list-map-find-small"):
//      benchmarks.ListsBenchmark.runMapFindSmall()

//    Timings.profile("iterable-filter-map-small"):
//      benchmarks.IterableBenchmarks.runFilterMapSmall()
//    Timings.profile("iterable-filter-forall-small"):
//      benchmarks.IterableBenchmarks.runFilterForAllSmall()

//    Timings.profile("iterable-withFilter-foreach-small"):
//      benchmarks.IterableBenchmarks.runWithFilterForeachSmall()
//    Timings.profile("list-map-filter-large"):
//      benchmarks.ListsBenchmark.runMapFilterLarge()
//    Timings.profile("list-map-filter-small"):
//      benchmarks.ListsBenchmark.runMapFilterSmall()
    Timings.profile("iterable-map-filter-small"):
      benchmarks.IterableBenchmarks.runMapFilterSmall()

    i += 1
    if i % 100 == 99 then println(Timings.profilingTable)
