package it.eng.portlet.consolepec.gwt.shared.action;

public class GetConfigurazioneCampiProtocollazione extends LiferayPortletUnsecureActionImpl<GetConfigurazioneCampiProtocollazioneResult> {

	private String id;

	@SuppressWarnings("unused")
	private GetConfigurazioneCampiProtocollazione() {
		// For serialization only
	}

	public GetConfigurazioneCampiProtocollazione(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
