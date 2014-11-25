package hu.dobrei.diploma.model;

public class Route {
	private final int length;
	private int flightCount = 0;
	private final Airport sourceAirport;
	private final Airport destinationAirport;

	public Route(Airport sourceAirport, Airport destinationAirport) {
		this.sourceAirport = sourceAirport;
		this.destinationAirport = destinationAirport;

		length = Haversine(sourceAirport, destinationAirport);
	}

	private int Haversine(Airport sourceAirport, Airport destinationAirport) {
		double R = 6371 * 1000; // m

		double dLongitude = Math.toRadians(sourceAirport.getLongitude() - destinationAirport.getLongitude());
		double dLatitude = Math.toRadians(sourceAirport.getLatitude() - destinationAirport.getLatitude());

		double sindLat = Math.sin(dLatitude / 2.0);
		double sindLong = Math.sin(dLongitude / 2.0);
		double a = sindLat * sindLat + Math.cos(Math.toRadians(sourceAirport.getLatitude()))
				* Math.cos(Math.toRadians(destinationAirport.getLatitude())) * sindLong * sindLong;
		double b = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		int distance = (int) Math.round(R * b);

		return distance;
	}

	public int getLength() {
		return length;
	}

	public void increaseFlightCount() {
		this.flightCount++;
	}

	public int getFlightCount() {
		return this.flightCount;
	}

	public Airport getSourceAirport() {
		return sourceAirport;
	}

	public Airport getDestinationAirport() {
		return destinationAirport;
	}

}
