package it.eng.consolepec.xmlplugin.pratica.email;

import java.util.Date;
import java.util.List;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.pratica.operazionipecconcluse.AnnullaElettoraleApiTask;
import it.eng.consolepec.xmlplugin.pratica.operazionipecconcluse.impl.elettorale.AnnullaElettoraleApiTaskImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;

public class XMLPraticaEmailIn extends XMLPraticaEmail implements PraticaEmailIn {

	private AnnullaElettoraleApiTask annullaElettoraleApiTask = null;

	public XMLPraticaEmailIn() {
		annullaElettoraleApiTask = new AnnullaElettoraleApiTaskImpl((XMLGestionePECTask) getTaskInGestioneDisabilitato());
	}

	@Override
	protected Date getDate() {
		return getDati().getDataRicezione();
	}

	@Override
	protected String getProvenienza() {
		return getDati().getMittente();
	}

	@Override
	public boolean isProtocollaAbilitato() {
		return getDati().getProtocollazionePec() == null && !getDati().getStato().equals(Stato.ELIMINATA);
	}

	@Override
	public void annullaElettorale(List<Fascicolo> fascicoliCollegati, List<Fascicolo> daArchiviare) {
		if (!annullaElettoraleApiTask.isOperazioneAbilitata())
			throw new PraticaException("Operazione annulla elettorale non consentita.");
		annullaElettoraleApiTask.annullaElettorale(fascicoliCollegati, daArchiviare);

	}

	@Override
	public boolean isOperazioneAbilitata() {
		return annullaElettoraleApiTask.isOperazioneAbilitata();
	}

	@Override
	public TipologiaPratica getTipo() {
		return TipologiaPratica.EMAIL_IN;
	}
}
