package hu.dobrei.diploma;

import hu.dobrei.diploma.model.Airport;
import hu.dobrei.diploma.model.Flight;
import hu.dobrei.diploma.model.OpenFlightsNetwork;
import hu.dobrei.diploma.model.Route;
import hu.dobrei.diploma.model.Route.RelaxedRoute;
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

public class Simulation {
	private static Stopwatch stopwatch;
	private static OpenFlightsNetwork network = new OpenFlightsNetwork();

	public static void simulate() {
		startTimer();

		try {
			parseNetwork(network);
		} catch (IOException e) {
			e.printStackTrace();
		}

		final Map<String, List<Flight>> topTenAirline = initializeSimulation(network);

		final Routing<Integer> shortestRouting = new Routing<Integer>(network, new ShortestAlgebra());
		final Routing<Integer> leastHopRouting = new Routing<Integer>(network, new LeastHopAlgebra());
		final List<Routing<Integer>> routings = Lists.newLinkedList();
		routings.add(shortestRouting);
		routings.add(leastHopRouting);

		printCurrentTime("Preparation time: ");

		String fileName;
		String algebraName;
		List<List<Route>> simulation;
		String airlineName;
		List<Flight> flightsOfAirline;
		for (Entry<String, List<Flight>> airlineEntry : topTenAirline.entrySet()) {

			airlineName = airlineEntry.getKey();
			flightsOfAirline = airlineEntry.getValue();

			for (Routing<Integer> routing : routings) {
				restartTimer();

				simulation = Lists.newLinkedList();
				for (Flight flight : flightsOfAirline) {
					simulation.add(getSimulationResult(routing, flight));
				}

				algebraName = getSimpleName(routing.getAlgebra());
				fileName = getFileName(airlineName, algebraName);
				saveSimulationToFile(simulation, fileName);
				printCurrentTime(fileName);
			}
		}
	}

	private static String getSimpleName(Object obj) {
		return obj.getClass().getSimpleName();
	}

	private static void printCurrentTime(String fileName) {
		System.out.println(fileName + ": " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms.");
	}

	private static void restartTimer() {
		stopwatch.reset();
		stopwatch.start();
	}

	private static void startTimer() {
		stopwatch = Stopwatch.createStarted();
	}

	private static Map<String, List<Flight>> initializeSimulation(OpenFlightsNetwork network) {
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

		return topTenAirline;
	}

	private static void parseNetwork(OpenFlightsNetwork network) throws IOException {
		network.readAirlines(new File("airlines.dat"));
		network.readAirports(new File("airports.dat"));
		network.readNetwork(new File("routes.dat"));
	}

	private static String getFileName(String airlineName, String algebraName) {
		return "sim/" + airlineName + " - " + algebraName + ".json";
	}

	private static List<Route> getSimulationResult(Routing<Integer> routing, Flight flight) {
		Airport sourceAirport = flight.getSourceAirport();
		Airport destinationAirport = flight.getDestinationAirport();
		routing.computeAllPreferredPathsFrom(sourceAirport);
		List<Airport> airportList = routing.getFirstPreferredPathsTo(destinationAirport);
		return convertAiportListToFlightList(airportList);
	}

	private static List<Route> convertAiportListToFlightList(List<Airport> airportList) {
		List<Route> routeList = Lists.newLinkedList();

		Route r;
		Airport s = airportList.get(0);
		Airport d;
		for (int i = 1; i < airportList.size(); i++) {
			d = airportList.get(i);
			r = network.getRoute(s, d);
			s = d;
			routeList.add(r);
		}

		return routeList;
	}

	private static void saveSimulationToFile(List<List<Route>> routes, String fileName) {
		/*-
		List<List<String>> toSave = Lists.newLinkedList();
		for (List<Route> list : routes) {
			List<String> line = Lists.newLinkedList();
			for (Route route : list) {
				//line.add(getPrintableRoute(route));toString
				line.add(route.toString());
			}
			toSave.add(line);
		}
		//*/
		
		List<List<RelaxedRoute>> toSave = Lists.newLinkedList();
		for (List<Route> list : routes) {
			List<RelaxedRoute> line = Lists.newLinkedList();
			for (Route route : list) {
				line.add(route.getRelaxation());
			}
			toSave.add(line);
		}
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try {
			Files.write(gson.toJson(toSave), new File(fileName), Charsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getPrintableRoute(Route route) {
		StringBuilder sb = new StringBuilder("");
		Airport s = route.getSourceAirport();
		Airport d = route.getDestinationAirport();
		
		sb.append(s.getId());
		sb.append(" -- ");
		sb.append(d.getId());
		sb.append(": ");
		sb.append(route.getLength());
		return sb.toString();
	}
}
