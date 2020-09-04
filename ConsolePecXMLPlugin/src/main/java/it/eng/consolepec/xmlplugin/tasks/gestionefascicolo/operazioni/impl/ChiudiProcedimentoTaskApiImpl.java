package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import java.util.List;
import java.util.TreeSet;

import it.eng.cobo.consolepec.commons.procedimento.ChiusuraProcedimentoInput;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Procedimento;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ChiudiProcedimentoTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

public class ChiudiProcedimentoTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements ChiudiProcedimentoTaskApi {

	public ChiudiProcedimentoTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void chiudiProcedimento(ChiusuraProcedimentoInput chiusuraProcedimentoInput, Integer durata) {
		boolean isCapofila = false;
		List<ProtocollazioneCapofila> capofila = getDatiFascicolo().getProtocollazioniCapofila();

		for (ProtocollazioneCapofila pc : capofila) {
			if (pc.getAnnoPG().equals(chiusuraProcedimentoInput.getAnnoProtocollazione()) && pc.getNumeroPG().equals(chiusuraProcedimentoInput.getNumProtocollazione())) {
				isCapofila = true;
				break;
			}
		}

		if (!isCapofila)
			throw new IllegalArgumentException("il PG non è capofila");

		boolean hasProcedimenti = false;
		TreeSet<Procedimento> procedimenti = getDatiFascicolo().getProcedimenti();
		for (Procedimento pc : procedimenti) {
			if (pc.getAnnoPG().equals(chiusuraProcedimentoInput.getAnnoProtocollazione()) && pc.getNumeroPG().equals(chiusuraProcedimentoInput.getNumProtocollazione())
					&& pc.getCodTipologiaProcedimento().equals(chiusuraProcedimentoInput.getCodTipologiaProcedimento())) {
				hasProcedimenti = true;
				pc.setDurata(durata);
				pc.setModalitaChiusura(chiusuraProcedimentoInput.getModalitaChiusura().name());
				pc.setDataChiusura(chiusuraProcedimentoInput.getDataChiusura());
				pc.setAnnoPGChiusura(chiusuraProcedimentoInput.getAnnoProtocolloDocChiusura());
				pc.setNumeroPGChiusura(chiusuraProcedimentoInput.getNumProtocolloDocChiusura());
			}
		}
		if (!hasProcedimenti)
			throw new IllegalArgumentException("Il capofila non ha nessun procedimento associato con codice " + chiusuraProcedimentoInput.getCodTipologiaProcedimento());

		generaEvento(EventiIterFascicolo.CHIUDI_PROCEDIMENTO, task.getCurrentUser(), chiusuraProcedimentoInput.getCodTipologiaProcedimento(), chiusuraProcedimentoInput.getNumProtocollazione(),
				chiusuraProcedimentoInput.getAnnoProtocollazione());

	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return (getDatiFascicolo().getStato().equals(Stato.IN_GESTIONE) || getDatiFascicolo().getStato().equals(Stato.IN_VISIONE) || getDatiFascicolo().getStato().equals(Stato.IN_AFFISSIONE)
				|| getDatiFascicolo().getStato().equals(Stato.DA_INOLTRARE_ESTERNO)) && getDatiFascicolo().getProtocollazioniCapofila().size() > 0;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.CHIUDI_PROCEDIMENTO;
	}

}
