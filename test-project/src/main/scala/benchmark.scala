import game.utils.Timings

/**
   * Without plugin:
[info] ┌──────────────────┬───────┬───────┬───────────┬───────┬───────────────┐
[info] │ Name             │ Last  │ Min   │ Total     │ Calls │ Per Call (ns) │
[info] ├──────────────────┼───────┼───────┼───────────┼───────┼───────────────┤
[info] │ filter-map-empty │ 2.673 │ 2.444 │ 2.134.504 │ 799   │ 2671470       │
[info] │ filter-map-large │ 4.226 │ 3.876 │ 3.549.473 │ 799   │ 4442394       │
[info] │ filter-map-one   │ 6.030 │ 5.778 │ 4.838.293 │ 799   │ 6055435       │
[info] │ filter-map-small │ 4.697 │ 4.469 │ 3.740.020 │ 799   │ 4680876       │
[info] │ filter-map-three │ 9.426 │ 9.113 │ 7.492.843 │ 799   │ 9377776       │
[info] │ filter-map-two   │ 6.960 │ 6.690 │ 5.604.679 │ 799   │ 7014617       │
[info] └──────────────────┴───────┴───────┴───────────┴───────┴───────────────┘  

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

    i += 1
    if i % 100 == 99 then println(Timings.profilingTable)
