package it.eng.portlet.consolepec.gwt.shared.dto;

public class Row extends Element {

	private static final long serialVersionUID = 3293060006738481902L;
	private String value;

	@SuppressWarnings("unused")
	private Row() {
	}

	public Row(String name, String value) {
		super.name = name;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void accept(VistorElement v) {
		v.visit(this);
	}

}
