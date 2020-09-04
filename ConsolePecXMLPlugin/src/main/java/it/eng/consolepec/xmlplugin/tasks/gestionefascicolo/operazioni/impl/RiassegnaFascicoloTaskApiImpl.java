package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.base.Strings;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.GruppoVisibilita;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.ParametriExtra;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata;
import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.RiassegnaFascicoloTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.GestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;
import it.eng.consolepec.xmlplugin.util.TaskDiFirmaUtil;

public class RiassegnaFascicoloTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements RiassegnaFascicoloTaskApi {

	public RiassegnaFascicoloTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void riassegna(AnagraficaRuolo nuovoAssegnatario, List<GruppoVisibilita> gruppiVisibilita, List<Pratica<?>> praticheCollegate, String operatore, List<String> indirizziNotifica) {

		if (nuovoAssegnatario == null)
			throw new PraticaException("Specificare un assegnatario valido");

		riassegnaPraticheCollegate(nuovoAssegnatario, gruppiVisibilita, praticheCollegate, operatore);
		riassegnaFascicolo(nuovoAssegnatario, gruppiVisibilita, operatore, indirizziNotifica);
	}

	private void aggiornaCondivisioni(AnagraficaRuolo nuovoAssegnatario) {

		// recupero la lista di operazioni che il fascicolo può condividere
		TreeSet<DatiPratica.Operazione> operazioni = ((DatiFascicolo) task.getEnclosingPratica().getDati()).getOperazioni();

		ArrayList<String> operazioniString = new ArrayList<String>();
		for (DatiPratica.Operazione operazione : operazioni)
			operazioniString.add(operazione.getNomeOperazione());

		if (task.controllaAbilitazione(TipoApiTask.CONDIVIDI_FASCICOLO))
			task.condividi(nuovoAssegnatario, operazioniString);
	}

	private void aggiornaVisibilita(List<GruppoVisibilita> gruppiVisibilita) {
		if (task.controllaAbilitazione(TipoApiTask.CAMBIA_VISIBILITA_FASCICOLO))
			task.aggiungiVisibilita(gruppiVisibilita);
	}

	private void riassegnaFascicolo(AnagraficaRuolo nuovoAssegnatario, List<GruppoVisibilita> gruppiVisibilita, String operatore, List<String> indirizziNotifica) {

		Assegnatario corrente = task.getDati().getAssegnatario();
		corrente.setDataFine(new Date());
		Date dataFine = corrente.getDataFine(); // TODO: LOGICA TEMPORANEA. da decidere come impostare la data fine!

		/*
		 * Gestione vecchio assegnatario
		 */
		task.getDati().getAssegnatariPassati().add(corrente);
		((XMLFascicolo) task.getEnclosingPratica()).getDatiPraticaTaskAdapter().setProvenienza(corrente.getNome());
		task.getEnclosingPratica().getDati().setLetto(false);

		/*
		 * Nuovo Assegnatario
		 */
		task.getDati().setAssegnatario(new Assegnatario(nuovoAssegnatario.getRuolo(), nuovoAssegnatario.getEtichetta(), new Date(), dataFine));

		/*
		 * Aggiorno visibilita
		 */
		aggiornaVisibilita(gruppiVisibilita);

		/*
		 * Aggiorno condivisioni
		 */
		aggiornaCondivisioni(nuovoAssegnatario);

		/*
		 * Operatore
		 */
		String nomeOperatore = Strings.isNullOrEmpty(operatore) ? null : operatore;
		task.getEnclosingPratica().getDati().getOperatore().setNome(nomeOperatore);

		/*
		 * Azioni
		 */
		generaAzione(corrente, nuovoAssegnatario, operatore, indirizziNotifica);
	}

	private void generaAzione(Assegnatario vecchioAssegnatario, AnagraficaRuolo nuovoAssegnatario, String operatore, List<String> indirizziNotifica) {
		ParametriExtra parametriExtraPeNotifica = new ParametriExtra();
		parametriExtraPeNotifica.getIndirizziEmail().addAll(indirizziNotifica);
		if (indirizziNotifica == null || indirizziNotifica.size() == 0)
			generaEvento(parametriExtraPeNotifica, EventiIterFascicolo.RIASSEGNA, task.getCurrentUser(), vecchioAssegnatario.getEtichetta(), nuovoAssegnatario.getEtichetta(),
					(operatore == null || operatore.isEmpty()) ? 0 : 1, operatore);
		else
			generaEvento(parametriExtraPeNotifica, EventiIterFascicolo.RIASSEGNA_CON_NOTIFICA, task.getCurrentUser(), vecchioAssegnatario.getEtichetta(), nuovoAssegnatario.getEtichetta(),
					GenericsUtil.convertCollectionToString(new ArrayList<String>(indirizziNotifica)), (operatore == null || operatore.isEmpty()) ? 0 : 1, operatore);
	}

	private void riassegnaPraticheCollegate(AnagraficaRuolo nuovoAssegnatario, List<GruppoVisibilita> gruppiVisibilita, List<Pratica<?>> praticheCollegate, String operatore) {
		List<PraticaCollegata> collegate = task.getEnclosingPratica().getAllPraticheCollegate();

		/*
		 * Check pratiche collegate
		 */
		for (Pratica<?> pratica : praticheCollegate) {

			boolean isPraticaInterna = false;
			for (PraticaCollegata p : collegate) {
				if (p.getAlfrescoPath().equalsIgnoreCase(pratica.getAlfrescoPath())) {
					isPraticaInterna = true;
					break;
				}
			}

			if (!isPraticaInterna)
				throw new PraticaException("La pratica " + pratica.getAlfrescoPath() + " non e' presente nel fascicolo.");

			/*
			 * Riassegno le pec attive
			 */
			Set<Task<?>> tasks = pratica.getTasks();
			GestionePECTask gestionePECTask = null;
			for (it.eng.consolepec.xmlplugin.factory.Task<?> t : tasks) {
				if (t.isAttivo() && t instanceof GestionePECTask) {
					gestionePECTask = (GestionePECTask) t;
					gestionePECTask.setCurrentUser(task.getCurrentUser());
					break;
				}
			}

			if (gestionePECTask != null && gestionePECTask.controllaAbilitazione(TipoApiTaskPEC.RIASSEGNA)) {
				gestionePECTask.riassegna(nuovoAssegnatario, gruppiVisibilita, operatore, new ArrayList<String>());
			}
		}
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return !TaskDiFirmaUtil.hasApprovazioneFirmaTask(task.getEnclosingPratica());
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.RIASSEGNA;
	}
}
