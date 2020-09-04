package it.eng.consolepec.spagicclient.search;

import java.util.HashMap;

public class SearchObjectSort {

	public enum SortType {
		ASC, DESC;
	}

	private HashMap<String, Integer> sorts = new HashMap<String, Integer>();

	private SearchObjectSort() {

	}

	public static SearchObjectSort start() {
		return new SearchObjectSort();
	}

	public SearchObjectSort addSort(String field, SortType sortType) {
		sorts.put(field, sortType == SortType.ASC ? 1 : -1);
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String comma = "";
		for (String field : sorts.keySet()) {
			sb.append(comma);
			comma = ",";
			sb.append("{ ").append(field).append(" : ").append(sorts.get(field)).append(" }");
		}
		return sb.toString();
	}

}
