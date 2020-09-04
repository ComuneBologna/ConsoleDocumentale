package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import java.util.List;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Protocollazione;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.AggiungiReinoltroAProtocollazione;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

public class AggiungiReinoltroAProtocollazioneImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements AggiungiReinoltroAProtocollazione {

	public AggiungiReinoltroAProtocollazioneImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void aggiungiReinoltroAProtocollazione(PraticaEmailOut praticaEmailOut, String numeroProtocollo, int anno) throws PraticaException {
		if (!checkProtocollazione(numeroProtocollo, anno))
			throw new PraticaException("La protocollazione " + numeroProtocollo + "/" + anno + " non esiste nel fascicolo.");
		List<ProtocollazioneCapofila> protocollazioniCapofila = getDatiFascicolo().getProtocollazioniCapofila();
		for (ProtocollazioneCapofila p : protocollazioniCapofila) {
			if (p.getAnnoPG() == anno && p.getNumeroPG().equals(numeroProtocollo)) {
				p.getPraticheCollegateProtocollate().add(
						getDatiFascicolo().new PraticaCollegata(praticaEmailOut.getAlfrescoPath(), TipologiaPratica.EMAIL_OUT.getNomeTipologia(), praticaEmailOut.getDati().getDataCreazione()));
			} else {
				for (Protocollazione prot : p.getProtocollazioniCollegate())
					if (prot.getAnnoPG() == anno && prot.getNumeroPG().equals(numeroProtocollo))
						prot.getPraticheCollegateProtocollate().add(getDatiFascicolo().new PraticaCollegata(praticaEmailOut.getAlfrescoPath(), TipologiaPratica.EMAIL_OUT.getNomeTipologia(),
								praticaEmailOut.getDati().getDataCreazione()));
			}
		}

	}

	private boolean checkProtocollazione(String numeroProtocollo, int anno) {
		List<ProtocollazioneCapofila> protocollazioniCapofila = getDatiFascicolo().getProtocollazioniCapofila();
		for (ProtocollazioneCapofila p : protocollazioniCapofila) {
			if (p.getAnnoPG() == anno && p.getNumeroPG().equals(numeroProtocollo)) {
				return true;
			}

			for (Protocollazione prot : p.getProtocollazioniCollegate())
				if (prot.getAnnoPG() == anno && prot.getNumeroPG().equals(numeroProtocollo))
					return true;

		}

		return false;
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.AGGIUNGI_REINOLTRO_PROTOCOLLAZIONE;
	}

}
