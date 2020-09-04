package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Protocollazione;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.PubblicaAllegatoTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

import java.util.Date;
import java.util.TreeSet;

public class PubblicaAllegatoTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements PubblicaAllegatoTaskApi {

	public PubblicaAllegatoTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void pubblicaAllegato(Allegato allegato, Date inizioPubblicazione, Date finePubblicazione) {
		Date inizio = DateUtils.getMidnightDate(inizioPubblicazione);
		Date fine = DateUtils.getMidnightDate(finePubblicazione);
		
		TreeSet<Allegato> allegatiProtocollati = new TreeSet<Allegato>();
		//allegatiProtocollati.addAll(getDatiFascicolo().getProtocollazioniCapofila().get(0).getProtocollazioniCollegate().get(0).getAllegatiProtocollati());
		//allegatiProtocollati.addAll(getDatiFascicolo().getProtocollazioniCapofila().get(0).getAllegatiProtocollati());
		for(ProtocollazioneCapofila protocollazioneCapofila : getDatiFascicolo().getProtocollazioniCapofila()){
			for(Allegato allgProtCapofila : protocollazioneCapofila.getAllegatiProtocollati())
				allegatiProtocollati.add(allgProtCapofila);
			for(Protocollazione protocollazioneCollegata : protocollazioneCapofila.getProtocollazioniCollegate())
				for(Allegato allgProtCollegato : protocollazioneCollegata.getAllegatiProtocollati())
						allegatiProtocollati.add(allgProtCollegato);
		}
		
		boolean pubblicato = false;
		if ((inizio != null && fine != null) && (fine.equals(inizio) || fine.after(inizio))) {
			for (Allegato a : getDatiFascicolo().getAllegati()) {
				if (a.equals(allegato)) {
					a.setDataInizioPubblicazione(inizioPubblicazione);
					a.setDataFinePubblicazione(finePubblicazione);
					log.debug("impostata pubblicazione allegato: {} {} {}", a.getNome(), a.getDataInizioPubblicazione(), a.getDataFinePubblicazione());
					for(Allegato allegatoProtocollato : allegatiProtocollati){
						if(a.getNome().equals(allegatoProtocollato.getNome())){
							allegatoProtocollato.setDataInizioPubblicazione(inizioPubblicazione);
							allegatoProtocollato.setDataFinePubblicazione(finePubblicazione);
							log.debug("impostata pubblicazione allegato protocollato corrispondente: {} {} {}", a.getNome(), a.getDataInizioPubblicazione(), a.getDataFinePubblicazione());
						}
					}
					pubblicato = true;
					break;
				}
			}
			
			
			
			
			
		} else {
			throw new PraticaException("Date incongruenti: inizio=" + inizioPubblicazione + " fine=" + finePubblicazione);
		}
		if (!pubblicato)
			throw new PraticaException("l'allegato " + allegato.getNome() + " non esiste nella pratica: " + getDatiFascicolo().getTitolo());
	}

	@Override
	protected boolean controllaAbilitazioneInterna() { // almeno un allegato da pubblicare
		return getDatiFascicolo().getAllegati().size() > 0;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.PUBBLICA;
	}

}
