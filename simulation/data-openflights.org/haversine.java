public double haversine(Position from, Position to) {

	double R = 6371 * 1000;

	double dLongitude = Math.toRadians(from.getLongitude()
			- to.getLongitude());
	double dLatitude = Math
			.toRadians(from.getLatitude() - to.getLatitude());

	double sindLat = Math.sin(dLatitude / 2.0);
	double sindLong = Math.sin(dLongitude / 2.0);
	double a = sindLat * sindLat
			+ Math.cos(Math.toRadians(from.getLatitude()))
			* Math.cos(Math.toRadians(to.getLatitude())) * sindLong
			* sindLong;
	double b = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

	double distance = R * b;

	return distance;

}
