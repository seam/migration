package org.open18.model.enums;

public enum Weather {
	CLOUDY("Cloudy"),
	FAIR("Fair"),
	FLURRIES("Flurries"),
	FREEZING_RAIN("Freezing Rain"),
	MOSTLY_CLOUDY("Mostly Cloudy"),
	MOSTLY_SUNNY("Mostly Sunny"),
	PARTLY_CLOUDY("Partly Cloudy"),
	RAIN("Rain"),
	SCATTERED_SHOWERS("Scattered Showers"),
	SCATTERED_TSTORMS("Scattered T-storms"),
	SHOWERS("Showers"),
	SNOW("Snow"),
	SUNNY("Sunny"),
	TSTORMS("T-storms");
	private final String label;

	Weather(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return this.label;
	}
}
