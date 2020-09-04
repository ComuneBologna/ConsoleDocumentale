package it.eng.consolepec.spagicclient.search;

import it.eng.consolepec.spagicclient.search.SearchPraticheFilter.SearchFilter;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SearchPraticheFilter implements Iterable<SearchFilter>{
	private Map<String, SearchFilter> filter = new LinkedHashMap<String, SearchFilter>();
	
	public void addFilter(String name, String value){
		String trimmedValue = value != null ? value.trim() : value;
		filter.put(name, new SearchFilter(SearchFilter.STRING_TYPE, name, trimmedValue));
	}
	public void addFilter(String name, boolean b) {
		addFilter(name, Boolean.toString(b));
	}
	public void addFilter(String name, List<String> value){
		filter.put(name, new SearchFilter(SearchFilter.LIST_TYPE, name, value));
	}
	public void addFilter(String name, Map<String, ?> map){
		filter.put(name, new SearchFilter(SearchFilter.MAP_TYPE, name, map));
	}
	
	@Override
	public Iterator<SearchFilter> iterator() {
		return filter.values().iterator();
	}
	
	@Override
	public String toString() {
		return filter.values().toString();
	}
	
	public static class SearchFilter {
		public static final int STRING_TYPE = 0;
		public static final int LIST_TYPE = 1;
		public static final int MAP_TYPE = 2;
		
		private int type;
		private String name;
		private Object value;
		public SearchFilter(int type, String name, Object value) {
			super();
			this.type = type;
			this.name = name;
			this.value = value;
		}
		public int getType() {
			return type;
		}
		public String getName() {
			return name;
		}
		public Object getValue() {
			return value;
		}
		
		@Override
		public String toString() {
			return "(" + name + "," +value+ ")";
		}
		
	}
	
}

