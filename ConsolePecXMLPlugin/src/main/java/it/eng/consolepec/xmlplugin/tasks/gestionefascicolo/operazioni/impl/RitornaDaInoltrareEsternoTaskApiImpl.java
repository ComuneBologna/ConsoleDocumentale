package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import java.util.List;

import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.RitornaDaInoltrareEsternoTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class RitornaDaInoltrareEsternoTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements RitornaDaInoltrareEsternoTaskApi {

	public static final String MESSAGGIO_NOTA = "L''utente {0} ha restituito il fascicolo";

	public RitornaDaInoltrareEsternoTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return getDatiFascicolo().getStato().equals(Stato.DA_INOLTRARE_ESTERNO);
	}

	@Override
	protected boolean controllaAbilitazioneGenerica() {
		return true;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.RITORNA_DA_INOLTRARE_ESTERNO;
	}

	@Override
	public void ritornaDaInoltrareEsterno(List<Pratica<?>> praticheCollegate) {
		((XMLFascicolo) task.getEnclosingPratica()).getDatiPraticaTaskAdapter().setStato(Stato.IN_GESTIONE);
		getDatiFascicolo().setAssegnazioneEsterna(null);

		for (Pratica<?> pc : praticheCollegate) {
			pc.getDati().setAssegnazioneEsterna(null);
		}

		if (task.isUtenteEsterno()) {
			getDatiFascicolo().setLetto(false);
			// aggiungo il messaggio alle note
			StringBuilder bl = new StringBuilder();
			bl.append(XmlPluginUtil.format(MESSAGGIO_NOTA, task.getCurrentUser()) + "\n\n");
			if (getDatiFascicolo().getNote() != null) {
				bl.append(getDatiFascicolo().getNote());
			}
			getDatiFascicolo().setNote(bl.toString());

		}

		generaEvento(EventiIterFascicolo.RITORNA_DA_INOLTRARE_ESTERNO, task.getCurrentUser());
	}

}
