package it.eng.portlet.consolepec.gwt.shared.action.inviomassivo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO;

public class NuovoInvioComunicazioneAction extends LiferayPortletUnsecureActionImpl<NuovoInvioComunicazioneActionResult> {

	private ComunicazioneDTO comunicazione;
	private AllegatoDTO allegato;
	private int numeroTest;
	private String destinatarioTest;
	private boolean test;
	
	protected NuovoInvioComunicazioneAction() {
		// For serialization only
	}

	public NuovoInvioComunicazioneAction(ComunicazioneDTO comunicazione, AllegatoDTO allegato, int numeroTest, String destinatarioTest) {
		super();
		this.comunicazione = comunicazione;
		this.allegato = allegato;
		this.numeroTest = numeroTest;
		this.destinatarioTest = destinatarioTest;
		this.test = true;
	}

	public NuovoInvioComunicazioneAction(ComunicazioneDTO comunicazione, AllegatoDTO allegato) {
		super();
		this.comunicazione = comunicazione;
		this.allegato = allegato;
	}

	public ComunicazioneDTO getComunicazione() {
		return comunicazione;
	}

	public AllegatoDTO getAllegato() {
		return allegato;
	}

	public int getNumeroTest() {
		return numeroTest;
	}

	public String getDestinatarioTest() {
		return destinatarioTest;
	}

	public boolean isTest() {
		return test;
	}

	
		
		
}
