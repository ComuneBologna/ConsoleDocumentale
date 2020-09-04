package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Protocollazione;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.RimuoviPubblicazioneAllegatoTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

import java.util.ArrayList;
import java.util.List;

public class RimuoviPubblicazioneAllegatoTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements RimuoviPubblicazioneAllegatoTaskApi {

	public RimuoviPubblicazioneAllegatoTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void rimuoviPubblicazioneAllegato(Allegato allegato) {
		boolean pubblicato = true;
		List<Allegato> allegatiProtocollati = new ArrayList<Allegato>();
		for(ProtocollazioneCapofila protocollazioneCapofila : getDatiFascicolo().getProtocollazioniCapofila()){
			for(Allegato allgProtCapofila : protocollazioneCapofila.getAllegatiProtocollati())
				allegatiProtocollati.add(allgProtCapofila);
			for(Protocollazione protocollazioneCollegata : protocollazioneCapofila.getProtocollazioniCollegate())
				for(Allegato allgProtCollegato : protocollazioneCollegata.getAllegatiProtocollati())
						allegatiProtocollati.add(allgProtCollegato);
		}
		
		for (Allegato a : getDatiFascicolo().getAllegati()) {
			if (a.equals(allegato)) {
				a.setDataInizioPubblicazione(null);
				a.setDataFinePubblicazione(null);
				log.debug("rimossa pubblicazione allegato: {}", a.getNome());
				for(Allegato allegatoProtocollato : allegatiProtocollati){
					if(a.getNome().equals(allegatoProtocollato.getNome())){
						allegatoProtocollato.setDataInizioPubblicazione(null);
						allegatoProtocollato.setDataFinePubblicazione(null);
						log.debug("rimossa pubblicazione allegato protocollato corrispondente: {}", a.getNome());
					}
				}
				pubblicato = false;
				break;
			}
		}
		if (pubblicato)
			throw new PraticaException("l'allegato " + allegato.getNome() + " non esiste nella pratica: " + getDatiFascicolo().getTitolo());
	}

	@Override
	protected boolean controllaAbilitazioneInterna() { // almeno un allegato pubblicato
		for (Allegato a : getDatiFascicolo().getAllegati())
			if (a.getDataInizioPubblicazione() != null && a.getDataFinePubblicazione() != null)
				return true;
		return false;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.RIMUOVI_PUBBLICAZIONE;
	}

}
