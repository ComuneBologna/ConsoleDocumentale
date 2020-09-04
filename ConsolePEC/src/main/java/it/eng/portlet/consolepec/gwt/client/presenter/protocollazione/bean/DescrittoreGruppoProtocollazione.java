package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.bean;

import it.eng.portlet.consolepec.gwt.client.widget.protocollazione.ElementoGruppoIndirizziProtocollazioneWidget;
import it.eng.portlet.consolepec.gwt.client.widget.protocollazione.ElementoGruppoNominativiProtocollazioneWidget;
import it.eng.portlet.consolepec.gwt.client.widget.protocollazione.ElementoGruppoProtocollazioneWidget;

public class DescrittoreGruppoProtocollazione implements Comparable<DescrittoreGruppoProtocollazione> {

	private String nomeGruppo, descrizione;
	private int maxOccurs;
	private ElementoGruppoProtocollazioneWidget<?> elementoGruppoProtocollazioneWidget;

	public DescrittoreGruppoProtocollazione(String nomeGruppo, String descrizione, int maxOccurs) {
		super();
		this.nomeGruppo = nomeGruppo;
		this.descrizione = descrizione;
		this.maxOccurs = maxOccurs;
		initMultiPanel();
	}

	private void initMultiPanel() {
		if (nomeGruppo.equals("reci2")) {
			elementoGruppoProtocollazioneWidget = new ElementoGruppoNominativiProtocollazioneWidget();
			((ElementoGruppoNominativiProtocollazioneWidget) elementoGruppoProtocollazioneWidget).setEliminaCommand(((ElementoGruppoNominativiProtocollazioneWidget) elementoGruppoProtocollazioneWidget).new EliminaNominativoCommmand());
		} else if (nomeGruppo.equals("reci5")) {
			elementoGruppoProtocollazioneWidget = new ElementoGruppoIndirizziProtocollazioneWidget();
			((ElementoGruppoIndirizziProtocollazioneWidget) elementoGruppoProtocollazioneWidget).setEliminaCommand(((ElementoGruppoIndirizziProtocollazioneWidget) elementoGruppoProtocollazioneWidget).new EliminaIndirizzoCommmand());
		}
	}

	public String getNomeGruppo() {
		return nomeGruppo;
	}

	public void setNomeGruppo(String nomeGruppo) {
		this.nomeGruppo = nomeGruppo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public int getMaxOccurs() {
		return maxOccurs;
	}

	public void setMaxOccurs(int maxOccurs) {
		this.maxOccurs = maxOccurs;
	}

	@Override
	public int compareTo(DescrittoreGruppoProtocollazione o) {
		return nomeGruppo.compareTo(o.nomeGruppo);
	}

	public  ElementoGruppoProtocollazioneWidget<?> getMultiPanel() {
		return elementoGruppoProtocollazioneWidget;

	}

}
