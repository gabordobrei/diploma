package net.gabordobrei.diploma.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.gabordobrei.diploma.logger.Logger;
import net.gabordobrei.diploma.openflights.model.Airline;
import net.gabordobrei.diploma.openflights.model.Airport;
import net.gabordobrei.diploma.openflights.model.Route;

import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class OpenFlightsLoader {

	final static Logger log = new Logger(OpenFlightsLoader.class);

	static final String airportsPath = null;
	static final String airlinesPath = null;
	static final String routesPath = null;

	static List<Airline> loadAirline() throws FileNotFoundException, IOException {

		List<Airline> airlines = Lists.newLinkedList();
		File airlinesSource = new File(airlinesPath);

		if (!airlinesSource.exists()) {
			throw new FileNotFoundException();
		}

		if (!airlinesSource.canRead()) {
			throw new IOException("Nem olvasható fájl!");
		}

		Stopwatch stopwatch = Stopwatch.createStarted();

		log.i("Read Airlines...");
		List<String> tempAirlines = Files.readLines(airlinesSource, Charsets.UTF_8);
		for (String string : tempAirlines) {
			airlines.add(new Airline(Splitter.on(';').split(CharMatcher.is('\"').removeFrom(string))));
		}

		stopwatch.elapsed(TimeUnit.MICROSECONDS);
		log.i(stopwatch.toString());

		return airlines;
	}

	static List<Airport> loadAirport() throws FileNotFoundException, IOException {
		
		List<Airport> airports = Lists.newLinkedList();
		File airportsSource = new File(airportsPath);

		if (!airportsSource.exists()) {
			throw new FileNotFoundException();
		}

		if (!airportsSource.canRead()) {
			throw new IOException("Nem olvasható fájl!");
		}

		Stopwatch stopwatch = Stopwatch.createStarted();

		log.i("Read Airports...");
		List<String> tempAirports = Files.readLines(airportsSource, Charsets.UTF_8);
		for (String string : tempAirports) {
			airports.add(new Airport(Splitter.on(';').split(CharMatcher.is('\"').removeFrom(string))));
		}
		stopwatch.elapsed(TimeUnit.MICROSECONDS);
		log.i(stopwatch.toString());

		return airports;
	}

	static List<Route> loadRoute() throws FileNotFoundException, IOException {
		List<Route> routes = Lists.newLinkedList();
		File routesSource = new File(routesPath);

		if (!routesSource.exists()) {
			throw new FileNotFoundException();
		}

		if (!routesSource.canRead()) {
			throw new IOException("Nem olvasható fájl!");
		}

		Stopwatch stopwatch = Stopwatch.createStarted();

		log.i("Read Routes...");
		List<Airline> airlines = OpenFlightsLoader.loadAirline();
		List<Airport> airports = OpenFlightsLoader.loadAirport();

		List<String> tempRoutes = Files.readLines(routesSource, Charsets.UTF_8);
		for (String string : tempRoutes) {

			Airline airline = null;
			Airport sourceAirport = null, destinationAirport = null;

			Iterable<String> line = Splitter.on(';').split(CharMatcher.is('\"').removeFrom(string));
			int airlineID = Integer.parseInt(Iterables.get(line, 1).equals("\\N") ? "0" : Iterables.get(line, 1));
			int sourceAirportID = Integer.parseInt(Iterables.get(line, 3).equals("\\N") ? "0" : Iterables.get(line, 3));
			int destinationAirportID = Integer.parseInt(Iterables.get(line, 5).equals("\\N") ? "0" : Iterables.get(line, 5));

			for (Airline l : airlines) {
				if (l.getAirlineID() == airlineID) {
					airline = l;
				}
			}

			for (Airport p : airports) {
				if (p.getAirportID() == sourceAirportID) {
					sourceAirport = p;
				}

				if (p.getAirportID() == destinationAirportID) {
					destinationAirport = p;
				}
			}

			routes.add(new Route(airline, sourceAirport, destinationAirport, Iterables.get(line, 6), Integer.parseInt(Iterables.get(line, 7)),
					Iterables.get(line, 7)));
		}

		stopwatch.elapsed(TimeUnit.MICROSECONDS);
		log.i(stopwatch.toString());

		return routes;
	}
}
