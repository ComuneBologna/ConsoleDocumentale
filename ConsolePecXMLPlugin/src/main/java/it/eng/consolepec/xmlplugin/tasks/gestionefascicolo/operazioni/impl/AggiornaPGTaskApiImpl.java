package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Protocollazione;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.AggiornaPGTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.util.IdentificativoProtocollazione;

import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class AggiornaPGTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements AggiornaPGTaskApi {
	
	private static final Integer CHANGE = 0;
	private static final Integer NOT_CHANGED = 1;

	public AggiornaPGTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return !getDatiFascicolo().getProtocollazioniCapofila().isEmpty();
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.AGGIORNA_PG;
	}
	
	@Override
	public boolean aggiornamentoValido(List<IdentificativoProtocollazione> protocollazioni) {
		/*
		 *  questo controllo bloccante è stato dettato da FRenzi 06/07/2017: vedi segnalazione jira
		 *  successivamente mi è stato detto di togliere questo controllo in quanto non necessario per la fase 1 dell'aggiorna pg.  
		 *  non cancello il codice perchè potrebbe servire in futuro(Fase 2) come da indicazione di FRenzi
		 */
		
		List<String> chiaviProtocollazione = Lists.transform(protocollazioni, new Function<IdentificativoProtocollazione, String>() {
			
			@Override
			public String apply(IdentificativoProtocollazione p) {
				return chiave(p.getAnnoPG(), p.getNumPG());

			}
		});
		
		
		for(ProtocollazioneCapofila protocollazioneCapofila: getDatiFascicolo().getProtocollazioniCapofila()){
			String chiaveCapofila = chiave(protocollazioneCapofila.getAnnoPG(), protocollazioneCapofila.getNumeroPG());
			
			for(Protocollazione protocollazioneCollegata: protocollazioneCapofila.getProtocollazioniCollegate()){
				String chiaveCollegata = chiave(protocollazioneCollegata.getAnnoPG(), protocollazioneCollegata.getNumeroPG());
				
				if(chiaviProtocollazione.contains(chiaveCollegata) && !chiaviProtocollazione.contains(chiaveCapofila)) {
					// per aggiornare una protocollazione non capofila devo aggiornare anche la sua capofila
					return false;
				}
			}
		}
		return true;
	}


	@Override
	public void aggiornaPG(AggiornaPGTaskApiDati datiDiAggiornamento) {
		
		String chiaveDaAggiornare =  chiave(datiDiAggiornamento.getIdentificativo().getAnnoPG(), datiDiAggiornamento.getIdentificativo().getNumPG());
		
		
		// trovo la protocollazione da aggiornare
		ProtocollazioneCapofila protocollazioneCapofilaDaAggiornare = null; 
		Protocollazione protocollazioneCollegataDaAggiornare = null;
		
		for(ProtocollazioneCapofila protocollazioneCapofila: getDatiFascicolo().getProtocollazioniCapofila()){
			String chiaveCapofila = chiave(protocollazioneCapofila.getAnnoPG(), protocollazioneCapofila.getNumeroPG());
			
			if(chiaveDaAggiornare.equals(chiaveCapofila)){
				protocollazioneCapofilaDaAggiornare = protocollazioneCapofila;
				break;
			}
			
			for(Protocollazione protocollazioneCollegata: protocollazioneCapofila.getProtocollazioniCollegate()){
				String chiaveCollegata = chiave(protocollazioneCollegata.getAnnoPG(), protocollazioneCollegata.getNumeroPG());
				
				if(chiaveDaAggiornare.equals(chiaveCollegata)){
					protocollazioneCollegataDaAggiornare = protocollazioneCollegata;
					break;
				}	
			}
		}
		
		if(protocollazioneCapofilaDaAggiornare != null){
			LinkedList<Object> tmp = new LinkedList<>();
			// Oggetto
			if (!GenericsUtil.isSame(protocollazioneCapofilaDaAggiornare.getOggetto(), datiDiAggiornamento.getOggetto())) {
				aggiungiCampiEvento(tmp, CHANGE, protocollazioneCapofilaDaAggiornare.getOggetto(), datiDiAggiornamento.getOggetto());
				protocollazioneCapofilaDaAggiornare.setOggetto(datiDiAggiornamento.getOggetto());
			} else {
				aggiungiCampiEvento(tmp, NOT_CHANGED, NOT_CHANGED, NOT_CHANGED);
			}
			// Provenienza
			if (!GenericsUtil.isSame(protocollazioneCapofilaDaAggiornare.getProvenienza(), datiDiAggiornamento.getProvenienza())) {
				aggiungiCampiEvento(tmp, CHANGE, protocollazioneCapofilaDaAggiornare.getProvenienza(), datiDiAggiornamento.getProvenienza());
				protocollazioneCapofilaDaAggiornare.setProvenienza(datiDiAggiornamento.getProvenienza());
			} else {
				aggiungiCampiEvento(tmp, NOT_CHANGED, NOT_CHANGED, NOT_CHANGED);
			}
			// Titolo
			if (!GenericsUtil.isSame(protocollazioneCapofilaDaAggiornare.getTitolo(), datiDiAggiornamento.getTitolo())) {
				aggiungiCampiEvento(tmp, CHANGE, protocollazioneCapofilaDaAggiornare.getTitolo(), datiDiAggiornamento.getTitolo());
				protocollazioneCapofilaDaAggiornare.setTitolo(datiDiAggiornamento.getTitolo());
			} else {
				aggiungiCampiEvento(tmp, NOT_CHANGED, NOT_CHANGED, NOT_CHANGED);
			}
			// Rubrica
			if (!GenericsUtil.isSame(protocollazioneCapofilaDaAggiornare.getRubrica(), datiDiAggiornamento.getRubrica())) {
				aggiungiCampiEvento(tmp, CHANGE, protocollazioneCapofilaDaAggiornare.getRubrica(), datiDiAggiornamento.getRubrica());
				protocollazioneCapofilaDaAggiornare.setRubrica(datiDiAggiornamento.getRubrica());
			} else {
				aggiungiCampiEvento(tmp, NOT_CHANGED, NOT_CHANGED, NOT_CHANGED);
			}
			// Sezione
			if (!GenericsUtil.isSame(protocollazioneCapofilaDaAggiornare.getSezione(), datiDiAggiornamento.getSezione())) {
				aggiungiCampiEvento(tmp, CHANGE, protocollazioneCapofilaDaAggiornare.getSezione(), datiDiAggiornamento.getSezione());
				protocollazioneCapofilaDaAggiornare.setSezione(datiDiAggiornamento.getSezione());
			} else {
				aggiungiCampiEvento(tmp, NOT_CHANGED, NOT_CHANGED, NOT_CHANGED);
			}
			// Data Protocollazione
			if (!GenericsUtil.isSame(protocollazioneCapofilaDaAggiornare.getDataprotocollazione(), datiDiAggiornamento.getDataProtocollazione())) {
				aggiungiCampiEvento(tmp, CHANGE, protocollazioneCapofilaDaAggiornare.getDataprotocollazione(), datiDiAggiornamento.getDataProtocollazione());
				protocollazioneCapofilaDaAggiornare.setDataprotocollazione(datiDiAggiornamento.getDataProtocollazione());
			} else {
				aggiungiCampiEvento(tmp, NOT_CHANGED, NOT_CHANGED, NOT_CHANGED);
			}
			
			if (controllo(tmp)) {
				tmp.addFirst(protocollazioneCapofilaDaAggiornare.getAnnoPG().toString());
				tmp.addFirst(protocollazioneCapofilaDaAggiornare.getNumeroPG());
				tmp.addFirst(task.getCurrentUser());
				generaEvento(EventiIterFascicolo.AGGIORNA_PG_OGGETTO, tmp.toArray());
			} else {
				generaEvento(EventiIterFascicolo.AGGIORNA_PG_SENZA_MODIFICHE, task.getCurrentUser(),
						protocollazioneCapofilaDaAggiornare.getNumeroPG(), protocollazioneCapofilaDaAggiornare.getAnnoPG().toString());
			}
		}
		
		if(protocollazioneCollegataDaAggiornare != null){
			LinkedList<Object> tmp = new LinkedList<>();
			// Oggetto
			if (!GenericsUtil.isSame(protocollazioneCollegataDaAggiornare.getOggetto(), datiDiAggiornamento.getOggetto())) {
				aggiungiCampiEvento(tmp, CHANGE, protocollazioneCollegataDaAggiornare.getOggetto(), datiDiAggiornamento.getOggetto());
				protocollazioneCollegataDaAggiornare.setOggetto(datiDiAggiornamento.getOggetto());
			} else {
				aggiungiCampiEvento(tmp, NOT_CHANGED, NOT_CHANGED, NOT_CHANGED);
			}
			// Provenienza
			if (!GenericsUtil.isSame(protocollazioneCollegataDaAggiornare.getProvenienza(), datiDiAggiornamento.getProvenienza())) {
				aggiungiCampiEvento(tmp, CHANGE, protocollazioneCollegataDaAggiornare.getProvenienza(), datiDiAggiornamento.getProvenienza());
				protocollazioneCollegataDaAggiornare.setProvenienza(datiDiAggiornamento.getProvenienza());
			} else {
				aggiungiCampiEvento(tmp, NOT_CHANGED, NOT_CHANGED, NOT_CHANGED);
			}
			// Titolo
			if (!GenericsUtil.isSame(protocollazioneCollegataDaAggiornare.getTitolo(), datiDiAggiornamento.getTitolo())) {
				aggiungiCampiEvento(tmp, CHANGE, protocollazioneCollegataDaAggiornare.getTitolo(), datiDiAggiornamento.getTitolo());
				protocollazioneCollegataDaAggiornare.setTitolo(datiDiAggiornamento.getTitolo());
			} else {
				aggiungiCampiEvento(tmp, NOT_CHANGED, NOT_CHANGED, NOT_CHANGED);
			}
			// Rubrica
			if (!GenericsUtil.isSame(protocollazioneCollegataDaAggiornare.getRubrica(), datiDiAggiornamento.getRubrica())) {
				aggiungiCampiEvento(tmp, CHANGE, protocollazioneCollegataDaAggiornare.getRubrica(), datiDiAggiornamento.getRubrica());
				protocollazioneCollegataDaAggiornare.setRubrica(datiDiAggiornamento.getRubrica());
			} else {
				aggiungiCampiEvento(tmp, NOT_CHANGED, NOT_CHANGED, NOT_CHANGED);
			}
			// Sezione
			if (!GenericsUtil.isSame(protocollazioneCollegataDaAggiornare.getSezione(), datiDiAggiornamento.getSezione())) {
				aggiungiCampiEvento(tmp, CHANGE, protocollazioneCollegataDaAggiornare.getSezione(), datiDiAggiornamento.getSezione());
				protocollazioneCollegataDaAggiornare.setSezione(datiDiAggiornamento.getSezione());
			} else {
				aggiungiCampiEvento(tmp, NOT_CHANGED, NOT_CHANGED, NOT_CHANGED);
			}
			// Data Protocollazione
			if (!GenericsUtil.isSame(protocollazioneCollegataDaAggiornare.getDataprotocollazione(), datiDiAggiornamento.getDataProtocollazione())) {
				aggiungiCampiEvento(tmp, CHANGE, protocollazioneCollegataDaAggiornare.getDataprotocollazione(), datiDiAggiornamento.getDataProtocollazione());
				protocollazioneCollegataDaAggiornare.setDataprotocollazione(datiDiAggiornamento.getDataProtocollazione());
			} else {
				aggiungiCampiEvento(tmp, NOT_CHANGED, NOT_CHANGED, NOT_CHANGED);
			}
			
			if (controllo(tmp)) {
				tmp.addFirst(protocollazioneCollegataDaAggiornare.getAnnoPG().toString());
				tmp.addFirst(protocollazioneCollegataDaAggiornare.getNumeroPG());
				tmp.addFirst(task.getCurrentUser());
				generaEvento(EventiIterFascicolo.AGGIORNA_PG_OGGETTO, tmp.toArray());
			} else {
				generaEvento(EventiIterFascicolo.AGGIORNA_PG_SENZA_MODIFICHE, task.getCurrentUser(),
						protocollazioneCollegataDaAggiornare.getNumeroPG(), protocollazioneCollegataDaAggiornare.getAnnoPG().toString());
			}
		}
		
	}

	private static String chiave(Integer annopg, String numpg) {
		return annopg + "_" + numpg;
	}
	
	private static void aggiungiCampiEvento(List<Object> list, Object controllo, Object valoreDa, Object valoreA) {
		list.add(controllo);
		list.add(valoreDa);
		list.add(valoreA);
	}
	
	private static boolean controllo(List<Object> list) {
		for (Object o : list) {
			if (!(o instanceof Number)) {
				return true;
			}
		}
		return false;
	}

}
