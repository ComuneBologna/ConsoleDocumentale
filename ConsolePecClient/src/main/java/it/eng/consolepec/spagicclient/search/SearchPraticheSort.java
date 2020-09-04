package it.eng.consolepec.spagicclient.search;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class SearchPraticheSort implements Iterable<Entry<String, Integer>>{
	public static final int ASC = 1;
	public static final int DESC = -1;
	
	private HashMap<String, Integer> sorts = new HashMap<String, Integer>();

	public SearchPraticheSort add(String field, Integer sortType) {
		sorts.put(field, sortType);
		return this;
	}
	
	@Override
	public String toString() {
		return sorts.toString();
	}
	@Override
	public Iterator<Entry<String, Integer>> iterator() {
		return sorts.entrySet().iterator();
	}

}
