package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.common.base.Strings;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.GruppoVisibilita;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.ParametriExtra;
import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.RiassegnaTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;

public class RiassegnaTaskPECApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements RiassegnaTaskPECApi {

	public RiassegnaTaskPECApiImpl(XMLGestionePECTask task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		String stato = task.getEnclosingPratica().getDati().getStato().name();
		if (stato.equals(Stato.ARCHIVIATA.name()))
			return false;
		else if (stato.equals(Stato.NOTIFICATA.name()))
			return false;
		else if (stato.equals(Stato.ELIMINATA.name()))
			return false;
		else if (stato.equals(Stato.RESPINTA.name()))
			return false;
		else if (stato.equals(Stato.RICONSEGNATA.name()))
			return false;
		return true;
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.RIASSEGNA;
	}

	@Override
	public void riassegna(AnagraficaRuolo nuovoAssegnatario, List<GruppoVisibilita> gruppiVisibilita, String operatore, List<String> indirizziNotifica) {

		if (nuovoAssegnatario == null)
			throw new PraticaException("Specificare un assegnatario valido");

		riassegnaPEC(nuovoAssegnatario, gruppiVisibilita, operatore, indirizziNotifica);
	}

	private void riassegnaPEC(AnagraficaRuolo nuovoAssegnatario, List<GruppoVisibilita> gruppiVisibilita, String operatore, List<String> indirizziNotifica) {

		Assegnatario corrente = task.getDati().getAssegnatario();
		corrente.setDataFine(new Date());
		Date dataFine = corrente.getDataFine(); // TODO: LOGICA TEMPORANEA. da decidere come impostare la data fine!

		/*
		 * Gestione vecchio assegnatario
		 */
		task.getDati().getAssegnatariPassati().add(corrente);
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
		 * Operatore
		 */
		String nomeOperatore = Strings.isNullOrEmpty(operatore) ? null : operatore;
		task.getEnclosingPratica().getDati().getOperatore().setNome(nomeOperatore);

		/*
		 * Azioni
		 */
		generaAzione(corrente, nuovoAssegnatario, indirizziNotifica);
	}

	private void aggiornaVisibilita(List<GruppoVisibilita> gruppiVisibilita) {

		for (GruppoVisibilita gv : gruppiVisibilita) {
			if (!task.getEnclosingPratica().getDati().getGruppiVisibilita().contains(gv)) {
				task.getEnclosingPratica().getDati().getGruppiVisibilita().add(gv);
			}
		}
	}

	private void generaAzione(Assegnatario vecchioAssegnatario, AnagraficaRuolo nuovoAssegnatario, List<String> indirizziNotifica) {
		ParametriExtra parametriExtraPeNotifica = new ParametriExtra();
		parametriExtraPeNotifica.getIndirizziEmail().addAll(indirizziNotifica);

		if (indirizziNotifica == null || indirizziNotifica.size() == 0)
			generaEvento(parametriExtraPeNotifica, EventiIterPEC.RIASSEGNA, task.getCurrentUser(), vecchioAssegnatario.getEtichetta(), nuovoAssegnatario.getEtichetta());
		else
			generaEvento(parametriExtraPeNotifica, EventiIterPEC.RIASSEGNA_CON_NOTIFICA, task.getCurrentUser(), vecchioAssegnatario.getEtichetta(), nuovoAssegnatario.getEtichetta(),
					GenericsUtil.convertCollectionToString(new ArrayList<String>(indirizziNotifica)));
	}
}
