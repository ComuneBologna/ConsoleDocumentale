package it.eng.consolepec.xmlplugin.pratica.email;

import java.util.Date;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECOutTask;

public class XMLPraticaEmailOut extends XMLPraticaEmail implements PraticaEmailOut {

	public XMLPraticaEmailOut() {
		// richiesto da reflection
	}

	@Override
	protected Date getDate() {
		return getDati().getDataInvio() == null ? getDati().getDataCreazione() : getDati().getDataInvio();
	}

	@Override
	protected String getProvenienza() {
		return getDati().getMittente();
	}

	@Override
	public boolean isInviata() {
		return getDati().getStato().getIdStato() > Stato.BOZZA.getIdStato();
	}

	@Override
	public boolean isProtocollaAbilitato() {
		boolean taskAttivo = false;

		for (Task<?> t : this.getTasks()) {
			if (t.isAttivo() && t instanceof XMLGestionePECOutTask) {
				taskAttivo = true;
			}
		}

		return getDati().getProtocollazionePec() == null && taskAttivo;
	}

	@Override
	public boolean isReinoltro() {
		return getDati().getMessageIDReinoltro() != null;
	}

	@Override
	public boolean isReinoltrabile() {
		return getDati().getMessageID() != null && !isReinoltro();
	}

	@Override
	public boolean isEmailInteroperabileInviabile() {
		if (isEmailInteroperabile()) {
			switch (getDati().getInteroperabile().getTipologiaSegnatura()) {
			case ALLEGATI:
				return getDati().getInteroperabile().getNomeAllegatoPrincipale() != null;
			case EMAIL:
				return getDati().getProtocollazionePec() != null;
			default:
				return false;
			}

		}
		return false;
	}

	@Override
	public TipologiaPratica getTipo() {
		return TipologiaPratica.EMAIL_OUT;
	}
}
