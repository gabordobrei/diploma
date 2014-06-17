package net.gabordobrei.diploma.openflights.model;

public class Route extends ModelObject {
	private final Airline airline;
	private final Airport sourceAirport;
	private final Airport destinationAirport;

	private final String codeshare;
	private final int stops;
	private final String equipment;

	public Route(Airline airline, Airport sourceAirport, Airport destinationAirport, String codeshare, int stops,
			String equipment) {
		super();
		this.airline = airline;
		this.sourceAirport = sourceAirport;
		this.destinationAirport = destinationAirport;
		this.codeshare = codeshare;
		this.stops = stops;
		this.equipment = equipment;
	}

	public int getAirlineID() {
		return airline.getAirlineID();
	}

	public String getAirlineName() {
		return airline.getName();
	}

	public int getSourceAirportID() {
		return sourceAirport.getAirportID();
	}

	public String getSourceAirportName() {
		return sourceAirport.getName();
	}

	public int getDestinationAirportID() {
		return destinationAirport.getAirportID();
	}

	public String getDestinationAirportName() {
		return destinationAirport.getName();
	}

	public String getCodeshare() {
		return codeshare;
	}

	public int getStops() {
		return stops;
	}

	public String getEquipment() {
		return equipment;
	}

}
