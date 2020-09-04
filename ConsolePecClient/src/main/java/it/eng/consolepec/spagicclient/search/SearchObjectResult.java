package it.eng.consolepec.spagicclient.search;

import java.util.HashMap;
import java.util.Set;

public class SearchObjectResult {

	private HashMap<String, Object> metadati = new HashMap<String, Object>();

	public SearchObjectResult() {
		// TODO Auto-generated constructor stub
	}

	public void addMetadato(String name, Object value) {
		metadati.put(name, value);
	}

	public Object getValue(String name) {
		return metadati.get(name);
	}
	
	public Set<String> getKeys(){
		return metadati.keySet();
	}
	
	@Override
	public String toString() {
		return metadati.toString();
	}

}
