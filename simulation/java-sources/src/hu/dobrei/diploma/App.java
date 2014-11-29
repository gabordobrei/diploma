package hu.dobrei.diploma;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class App {

	public static void main(String[] args) {

		Stopwatch stopwatch = Stopwatch.createStarted();

		Simulation.simulate();

		System.out.println("Full time: " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms.");

		/*-
		Routing<Integer> r = new Routing<>(network, new LeastHopAlgebra());
		Airport ss = network.getAirport(3456);
		Airport tt = network.getAirport(3948);
		r.computeAllPreferredPathsFrom(ss);
		Set<List<Airport>> paths = r.getAllPreferredPathsTo(tt);
		System.err.println(paths.iterator().next());
		System.err.println("--------");
		int ii = 0;
		for (List<Airport> path : paths) {
			System.err.println((ii++) + " " + path);
		}
		// 5209 - United Airlines (2415)
		// 4296 - Ryanair (2178)
		// 2009 - Delta Air Lines (1520)
		// 24 - American Airlines (1513)
		// 5265 - US Airways (1381)
		// 751 - Air China (1221)
		// 1767 - China Southern Airlines (1126)
		// 1758 - China Eastern Airlines (1093)
		// 4547 - Southwest Airlines (1018)
		// 3320 - Lufthansa (931)
		List<Airline> list = Lists.newArrayList(network.getAirlines().values());
		Collections.sort(list);
		for (int i = 0; i < 10; i++) {
			System.err.println(list.get(i).getId() + " - " + list.get(i).getName() + " (" + list.get(i).flightCount
					+ ")");
		}
		// */

	}
}