package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.StampaResult;

public class StampaRicevuteConsegnaResult extends StampaResult {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7575444256591657580L;

	public StampaRicevuteConsegnaResult(String fileDir, String fileName) {
		super(fileDir, fileName);
	}

	public StampaRicevuteConsegnaResult(String errorMsg, Boolean error) {
		super(errorMsg, error);
	}
	
	public StampaRicevuteConsegnaResult() {
		super();
	}

	
}
