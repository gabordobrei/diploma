package net.gabordobrei.diploma.openflights.model;

public class Airline extends ModelObject {

	private final int airlineID;
	private final String name;
	private final String alias;
	private final String IATA;
	private final String ICAO;
	private final String callsign;
	private final String country;
	private final boolean active;
		
	public Airline(int airlineID, String name, String alias, String iATA, String iCAO, String callsign, String country,
			boolean active) {
		super();
		this.airlineID = airlineID;
		this.name = name;
		this.alias = alias;
		IATA = iATA;
		ICAO = iCAO;
		this.callsign = callsign;
		this.country = country;
		this.active = active;
	}

	public Airline(Iterable<String> split) {
		this.airlineID = Integer.parseInt(split.iterator().next());
		this.name = split.iterator().next();
		this.alias = split.iterator().next();
		IATA = split.iterator().next();
		ICAO = split.iterator().next();
		this.callsign = split.iterator().next();
		this.country = split.iterator().next();
		this.active = Boolean.parseBoolean(split.iterator().next());
	}


	public int getAirlineID() {
		return airlineID;
	}

	public String getName() {
		return name;
	}

	public String getAlias() {
		return alias;
	}

	public String getIATA() {
		return IATA;
	}

	public String getICAO() {
		return ICAO;
	}

	public String getCallsign() {
		return callsign;
	}

	public String getCountry() {
		return country;
	}

	public boolean isActive() {
		return active;
	}
	
}
