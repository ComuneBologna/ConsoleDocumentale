/**
 * 
 */
package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask.Condivisione;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.CondividiFascicoloTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

import java.util.Date;
import java.util.List;
import java.util.TreeSet;

/**
 * @author roger
 * 
 */
public class CondividiFascicoloTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements CondividiFascicoloTaskApi {

	public CondividiFascicoloTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void condividi(AnagraficaRuolo gruppo, List<String> operazioni) throws PraticaException {
		T dati = task.getDati();

		TreeSet<Condivisione> condivisioni = dati.getCondivisioni();
		TreeSet<Condivisione> condivisioniReplace = new TreeSet<DatiGestioneFascicoloTask.Condivisione>();

		// se il gruppo già condivide il fascicolo aggiorno la condivisione
		for (Condivisione c : condivisioni)
			if (!c.getNomeGruppo().equals(gruppo.getRuolo())) {
				condivisioniReplace.add(c);
			}

		// creo la lista di operazioni
		TreeSet<DatiFascicolo.Operazione> elencoOperazioni = new TreeSet<DatiFascicolo.Operazione>();
		for (String nomeOperazione : operazioni) {
			for (Operazione op : getDatiFascicolo().getOperazioni()) {
				if (op.getNomeOperazione().equalsIgnoreCase(nomeOperazione) && op.isAbilitata())
					elencoOperazioni.add(new Operazione(nomeOperazione, true));
			}
		}

		// creo una nuova condivisione
		Condivisione condivisione = new Condivisione(gruppo.getRuolo(), new Date(), elencoOperazioni);
		condivisioniReplace.add(condivisione);

		boolean nuovaCondivisione = isNuovaCondivisione(condivisioni, condivisione);
		boolean modificaCondivisione = isModificaCondivisione(condivisioni, condivisione);

		// aggiorno la lista delle condivisioni del fascicolo
		dati.getCondivisioni().clear();
		dati.getCondivisioni().addAll(condivisioniReplace);

		if (nuovaCondivisione)
			generaEvento(EventiIterFascicolo.CONDIVIDI_FASCICOLO, task.getCurrentUser(), gruppo.getEtichetta());
		else if (modificaCondivisione)
			generaEvento(EventiIterFascicolo.MODIFICA_CONDIVIDI_FASCICOLO, task.getCurrentUser(), gruppo.getEtichetta());
	}

	// ritorna true se sto modificando una condivisione esistente
	private boolean isModificaCondivisione(TreeSet<Condivisione> condivisioni, Condivisione condivisione) {

		for (Condivisione c : condivisioni) {
			if (c.getNomeGruppo().equals(condivisione.getNomeGruppo())) {
				return !c.getOperazioni().equals(condivisione.getOperazioni());
			}
		}
		return false;
	}

	private boolean isNuovaCondivisione(TreeSet<Condivisione> condivisioni, Condivisione condivisione) {
		for (Condivisione c : condivisioni) {
			if (c.getNomeGruppo().equals(condivisione.getNomeGruppo())) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return getDatiFascicolo().getStato().equals(Stato.IN_GESTIONE) || getDatiFascicolo().getStato().equals(Stato.IN_VISIONE) || getDatiFascicolo().getStato().equals(Stato.DA_INOLTRARE_ESTERNO);
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.CONDIVIDI_FASCICOLO;
	}

}
