package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PubblicazioneAllegati extends LiferayPortletUnsecureActionImpl<PubblicazioneAllegatiResult> {

	private String praticaPath;
	private String nomeAllegato;
	private Date dataInizio;
	private Date dataFine;
	private List<String> destinatariEmail = new ArrayList<String>();
	private String testoEmail;

	@SuppressWarnings("unused")
	private PubblicazioneAllegati() {
		// For serialization only
	}

	public PubblicazioneAllegati(String praticaPath, String nomeAllegato, Date dataInizio, Date dataFine, List<String> destinatariEmail, String testoEmail) {
		this.praticaPath = praticaPath;
		this.nomeAllegato = nomeAllegato;
		this.dataInizio = dataInizio;
		this.dataFine = dataFine;
		this.destinatariEmail.clear();
		if (destinatariEmail != null && destinatariEmail.size() > 0)
			this.destinatariEmail.addAll(destinatariEmail);
		this.testoEmail = testoEmail;
	}

	public String getPraticaPath() {
		return praticaPath;
	}

	public String getNomeAllegato() {
		return nomeAllegato;
	}

	public Date getDataInizio() {
		return dataInizio;
	}

	public Date getDataFine() {
		return dataFine;
	}

	public List<String> getDestinatariEmail() {
		return destinatariEmail;
	}

	public String getTestoEmail() {
		return testoEmail;
	}

}
