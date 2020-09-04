package it.eng.cobo.consolepec.integration.lag;

import java.util.HashMap;
import java.util.Map;

public class LagProperties {

	private Map<String, String> properties = new HashMap<>();

	public String getProperty(String string) {
		return properties.get(string);
	}

	public Map<String, String> getProperties() {
		return properties;
	}

}
