package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.AssegnazioneEsterna;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.ParametriExtra;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.AssegnaUtenteEsternoTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.util.TaskDiFirmaUtil;

public class AssegnaUtenteEsternoTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements AssegnaUtenteEsternoTaskApi {

	public AssegnaUtenteEsternoTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return getDatiFascicolo().getStato().equals(Stato.IN_GESTIONE) && !TaskDiFirmaUtil.hasApprovazioneFirmaTask(task.getEnclosingPratica());
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.ASSEGNA_UTENTE_ESTERNO;
	}

	@Override
	public void assegnaEsterno(Set<String> destinatari, String testoEmail, Set<String> operazioni) {

		TreeSet<DatiFascicolo.Operazione> elencoOperazioni = new TreeSet<DatiFascicolo.Operazione>();
		for (String nomeOperazione : operazioni) {
			for (Operazione op : getDatiFascicolo().getOperazioni()) {
				if (op.getNomeOperazione().equalsIgnoreCase(nomeOperazione) && op.isAbilitata())
					elencoOperazioni.add(new Operazione(nomeOperazione, true));
			}
		}

		AssegnazioneEsterna assegnazioneEsterna = new AssegnazioneEsterna(elencoOperazioni, new TreeSet<String>(destinatari), testoEmail, new Date());

		getDatiFascicolo().setAssegnazioneEsterna(assegnazioneEsterna);

		((XMLFascicolo) task.getEnclosingPratica()).getDatiPraticaTaskAdapter().setStato(Stato.DA_INOLTRARE_ESTERNO);

		ParametriExtra parametriExtra = new ParametriExtra();
		parametriExtra.getIndirizziEmail().addAll(destinatari);

		generaEvento(parametriExtra, EventiIterFascicolo.ASSEGNA_UTENTE_ESTERNO, task.getCurrentUser(), GenericsUtil.convertCollectionToString(new ArrayList<String>(destinatari)));
	}

}
