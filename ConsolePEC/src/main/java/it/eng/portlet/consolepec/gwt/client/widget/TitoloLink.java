package it.eng.portlet.consolepec.gwt.client.widget;

public class TitoloLink {

	private Integer counter, total, limit;
	private String label;

	public TitoloLink(String label) {
		this.label = label;
	}

	public TitoloLink(String label, int counter, int total, int limit) {
		this.counter = counter;
		this.label = label;
		this.total = total;
		this.limit = limit;
	}

	public int getCounter() {
		return counter;
	}

	public String getLabel() {
		return label;
	}

	public String normalizzaTitolo(int len) {
		if (label == null || label.isEmpty())
			return "Nessun Oggetto";
		String newTitolo = null;
		if (counter == null || counter < 0) {
			// Controllo sulla dimensione della parola
			newTitolo = (label.length() > len) ? label.substring(0, len) + "..." : label;
		} else {
			// Controllo sulla dimensione della parola
			String counterSuffix = "(" + getCounter(counter) + "/" + getCounter(total) + ")";
			if (label.length() > len)
				newTitolo = label.substring(0, ((len + 1) - 3)) + "..." + counterSuffix; // orrore
			else
				newTitolo = label + " " + counterSuffix;
		}
		return newTitolo;
	}

	private String getCounter(int counter) {
		if (counter == limit)
			return "...";
		else
			return "" + counter;
	}

}
