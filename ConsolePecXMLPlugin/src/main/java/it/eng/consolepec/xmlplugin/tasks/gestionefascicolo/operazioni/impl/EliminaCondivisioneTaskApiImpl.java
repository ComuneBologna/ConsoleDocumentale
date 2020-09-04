package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import java.text.MessageFormat;
import java.util.TreeSet;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Collegamento;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask.Condivisione;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.EliminaCondivisioneTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

public class EliminaCondivisioneTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements EliminaCondivisioneTaskApi {

	public EliminaCondivisioneTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void eliminaCondivisione(AnagraficaRuolo gruppo) throws PraticaException {
		TreeSet<Collegamento> collegameti = getDatiFascicolo().getCollegamenti();
		for (Collegamento collegamento : collegameti)
			if (collegamento.getNomeGruppo().equals(gruppo.getRuolo()))
				throw new PraticaException(MessageFormat.format("La condivisione non può essere eliminata per il gruppo {0}", gruppo.getEtichetta()));

		if (gruppo.getRuolo().equals(task.getDati().getAssegnatario().getNome())) {
			throw new PraticaException(MessageFormat.format("La condivisione non può essere eliminata per il gruppo assegnatario {0}", gruppo.getEtichetta()));

		}

		T dati = task.getDati();
		TreeSet<Condivisione> condivisioni = dati.getCondivisioni();
		TreeSet<Condivisione> newCondivisioni = new TreeSet<Condivisione>();

		// creo una nuova lista senza la condivisione da eliminare
		for (Condivisione condivisione : condivisioni)
			if (!condivisione.getNomeGruppo().equalsIgnoreCase(gruppo.getRuolo()))
				newCondivisioni.add(condivisione);

		dati.getCondivisioni().clear();
		dati.getCondivisioni().addAll(newCondivisioni);

		generaEvento(EventiIterFascicolo.ELIMINA_CONDIVISIONE_FASCICOLO, task.getCurrentUser(), gruppo.getEtichetta());

	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return getDatiFascicolo().getStato().equals(Stato.IN_GESTIONE) || getDatiFascicolo().getStato().equals(Stato.IN_VISIONE) || getDatiFascicolo().getStato().equals(Stato.DA_INOLTRARE_ESTERNO);
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.ELIMINA_CONDIVISIONE_FASCICOLO;
	}

}
