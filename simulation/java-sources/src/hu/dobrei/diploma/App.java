package hu.dobrei.diploma;

import hu.dobrei.diploma.model.Airport;
import hu.dobrei.diploma.model.Flight;
import hu.dobrei.diploma.model.OpenFlightsNetwork;
import hu.dobrei.diploma.model.algebra.AbstractAlgebra;
import hu.dobrei.diploma.model.algebra.LeastHopAlgebra;
import hu.dobrei.diploma.model.algebra.ShortestAlgebra;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Charsets;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class App {
	public static void main(String[] args) {
		Stopwatch stopwatch = Stopwatch.createStarted();
		Stopwatch stopwatchFull = Stopwatch.createStarted();
		OpenFlightsNetwork network = new OpenFlightsNetwork();
		try {
			network.readAirlines(new File("airlines.dat"));
			network.readAirports(new File("airports.dat"));
			network.readNetwork(new File("routes.dat"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Routing<Integer> r = new Routing<>(network, new LeastHopAlgebra());
		// Airport ss = network.getAirport(3456);
		// Airport tt = network.getAirport(3948);
		// r.computeAllPreferredPathsFrom(ss);
		// Set<List<Airport>> paths = r.getAllPreferredPathsTo(tt);
		// System.err.println(paths.iterator().next());
		// System.err.println("--------");
		// int ii = 0;
		// for (List<Airport> path : paths) {
		// System.err.println((ii++) + " " + path);
		// }

		Map<String, List<Flight>> topTenAirline = Maps.newHashMap();
		topTenAirline.put("UnitedAirlines", network.getFlightsbyAirline(5209));
		topTenAirline.put("Ryanair", network.getFlightsbyAirline(4296));
		topTenAirline.put("DeltaAirLines", network.getFlightsbyAirline(2009));
		topTenAirline.put("AmericanAirlines", network.getFlightsbyAirline(24));
		topTenAirline.put("USAirways", network.getFlightsbyAirline(5265));
		topTenAirline.put("AirChina", network.getFlightsbyAirline(751));
		topTenAirline.put("ChinaSouthernAirlines", network.getFlightsbyAirline(1767));
		topTenAirline.put("ChinaEasternAirlines", network.getFlightsbyAirline(1758));
		topTenAirline.put("SouthwestAirlines", network.getFlightsbyAirline(4547));
		topTenAirline.put("Lufthansa", network.getFlightsbyAirline(3320));

		
		AbstractAlgebra<Integer> shortestAlgebra = new ShortestAlgebra();
		AbstractAlgebra<Integer> leastHopAlgebra = new LeastHopAlgebra();
		Map<AbstractAlgebra<Integer>, Routing<Integer>> routings = Maps.newHashMap();
		routings.put(shortestAlgebra, new Routing<>(network, shortestAlgebra));
		routings.put(leastHopAlgebra, new Routing<>(network, leastHopAlgebra));
		System.err.println("Preparation time: " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms.");

		for (Entry<String, List<Flight>> entry : topTenAirline.entrySet()) {
			List<Airport> airports = Lists.newLinkedList();
			stopwatch.reset();
			stopwatch.start();

			String company = entry.getKey();

			for (Flight flight : entry.getValue()) {
				Airport s = flight.getSourceAirport();
				Airport t = flight.getDestinationAirport();
				if (!airports.contains(s)) {
					airports.add(s);
				}
				if (!airports.contains(t)) {
					airports.add(t);
				}
			}

			List<List<Airport>> routes = Lists.newLinkedList();
			List<Airport> a;
			for (Entry<AbstractAlgebra<Integer>, Routing<Integer>> e : routings.entrySet()) {
				Routing<Integer> routing = e.getValue();
				AbstractAlgebra<Integer> algebra = e.getKey();

				for (Airport sourceAirport : airports) {
					routing.computeAllPreferredPathsFrom(sourceAirport);
					for (Airport destinationAirport : airports) {
						a = routing.getFirstPreferredPathsTo(destinationAirport);
						routes.add(a);
					}
				}

				saveAsJson(routes, company, algebra);

				System.err.println(company + " - " + algebra.getClass().getSimpleName() + ": "
						+ stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms.");
			}
		}

		// saveAsJson(UnitedAirlinesRoutes,
		// "UnitedAirlinesRoutes - LeastHopAlgebra.json");

		/*-
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

		System.err.println("Full time: " + stopwatchFull.elapsed(TimeUnit.MILLISECONDS) + " ms.");
	}

	private static void saveAsJson(List<List<Airport>> routes, String routesName, @SuppressWarnings("rawtypes") AbstractAlgebra algebra) {
		String string = "sim/" + routesName + " - " + algebra.getClass().getSimpleName() + ".json";
		List<List<String>> toSave = Lists.newLinkedList();

		for (List<Airport> list : routes) {
			List<String> line = Lists.newLinkedList();
			for (Airport airport : list) {
				line.add(String.valueOf(airport.getId()));
			}
			toSave.add(line);
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try {
			Files.write(gson.toJson(toSave), new File(string), Charsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}