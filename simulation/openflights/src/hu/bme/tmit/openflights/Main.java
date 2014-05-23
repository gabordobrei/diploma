package hu.bme.tmit.openflights;

import hu.bme.tmit.openflights.logger.Logger;
import hu.bme.tmit.openflights.model.Airline;
import hu.bme.tmit.openflights.model.Airport;
import hu.bme.tmit.openflights.model.Route;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Iterables;
import com.google.common.io.Files;

public class Main {

	final static Logger log = new Logger(Main.class);

	public static void main(String args[]) throws Exception {

		List<Airport> airports = new LinkedList<Airport>();
		List<Airline> airlines = new LinkedList<Airline>();
		List<Route> routes = new LinkedList<Route>();

		if (args.length < 3) {
			printHelp();
			exit();
		}

		String airportsPath = null, airlinesPath = null, routesPath = null;

		for (int i = 0; i < args.length; i++) {
			if ("-p".matches(args[i])) {
				airportsPath = args[i + 1];
			}
			if ("-l".matches(args[i])) {
				airlinesPath = args[i + 1];
			}
			if ("-r".matches(args[i])) {
				routesPath = args[i + 1];
			}
		}

		Logger.setLevel(Logger.DEBUG);

		File airportsSource = new File(airportsPath);
		File airlinesSource = new File(airlinesPath);
		File routesSource = new File(routesPath);

		if (!airportsSource.exists() || !airlinesSource.exists() || !routesSource.exists()) {
			printHelp();
			exit();
		}

		if (!airportsSource.canRead() || !airlinesSource.canRead() || !routesSource.canRead()) {
			log.e("Cannot read file!");
			exit();
		}

		// log.setPrintStream(new PrintStream(new FileOutputStream("log.txt",
		// false)));

		Stopwatch stopwatch = Stopwatch.createStarted();

		log.i("Read Airlines...");
		List<String> tempAirlines = Files.readLines(airlinesSource, Charsets.UTF_8);
		for (String string : tempAirlines) {
			airlines.add(new Airline(Splitter.on(';').split(CharMatcher.is('\"').removeFrom(string))));
		}

		stopwatch.elapsed(TimeUnit.MICROSECONDS);
		log.i(stopwatch.toString());
		log.i("-------------------");

		stopwatch.stop();
		stopwatch.reset();
		stopwatch.start();

		log.i("Read Airports...");
		List<String> tempAirports = Files.readLines(airportsSource, Charsets.UTF_8);
		for (String string : tempAirports) {
			airports.add(new Airport(Splitter.on(';').split(CharMatcher.is('\"').removeFrom(string))));
		}
		stopwatch.elapsed(TimeUnit.MICROSECONDS);
		log.i(stopwatch.toString());
		log.i("-------------------");

		stopwatch.stop();
		stopwatch.reset();
		stopwatch.start();

		log.i("Read Routes...");
		List<String> tempRoutes = Files.readLines(routesSource, Charsets.UTF_8);
		for (String string : tempRoutes) {

			Airline airline = null;
			Airport sourceAirport = null, destinationAirport = null;

			Iterable<String> line = Splitter.on(';').split(CharMatcher.is('\"').removeFrom(string));
			int airlineID = Integer.parseInt( Iterables.get(line, 1).equals("\\N") ? "0" :  Iterables.get(line, 1));
			int sourceAirportID = Integer.parseInt( Iterables.get(line, 3).equals("\\N") ? "0" :  Iterables.get(line, 3));
			int destinationAirportID = Integer.parseInt( Iterables.get(line, 5).equals("\\N") ? "0" :  Iterables.get(line, 5));

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

			routes.add(new Route(airline, sourceAirport, destinationAirport, Iterables.get(line, 6), Integer
					.parseInt(Iterables.get(line, 7)), Iterables.get(line, 7)));
		}

		stopwatch.elapsed(TimeUnit.MICROSECONDS);
		log.i(stopwatch.toString());
		log.i("-------------------");

		log.i("#airports: " + airports.size());
		log.i("#airlines: " + airlines.size());
		log.i("#routes: " + routes.size());

	}

	private static void exit() {
		log.e("Exiting...");
		System.exit(-1);
	}

	private static void printHelp() {
		System.out.println("Usage:\topenflights -p <airports_file_name> -l <airlines_file_name> -r <routes_file_name>");
	}
}
