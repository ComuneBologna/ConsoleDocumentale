package it.eng.portlet.consolepec.gwt.shared.action;


public class RiversamentoCartaceoResult extends StampaResult {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6307160458828929444L;

	public RiversamentoCartaceoResult(String fileDir, String fileName) {
		super(fileDir, fileName);
	}

	public RiversamentoCartaceoResult(String errorMsg, Boolean error) {
		super(errorMsg, error);
	}
	
	public RiversamentoCartaceoResult() {
		super();
	}
}
