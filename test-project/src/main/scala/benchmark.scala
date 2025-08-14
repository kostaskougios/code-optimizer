import game.utils.Timings
@main
def benchmark(): Unit =
  var i = 0
  while true do
    Timings.profile("filter-map"):
      lists.ListsBenchmark.runFilterMap()

    i += 1
    if i % 100 == 99 then println(Timings.profilingTable)
