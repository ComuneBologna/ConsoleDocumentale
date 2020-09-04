package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import java.util.List;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.ParametriExtra;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Interoperabile;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmail;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.RispostaInteroperabileTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class RispostaInteroperabileTaskApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements RispostaInteroperabileTaskApi {

	private enum TipoNotifica {
		AGGIORNAMENTO, ANNULLAMENTO, CONFERMA, ECCEZIONE;
	}

	public RispostaInteroperabileTaskApiImpl(XMLGestionePECTask task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return task.getEnclosingPratica().isEmailInteroperabile(); // deve essere almeno una email interoperabile
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.RISPOSTA_INTEROPERABILE;
	}

	@Override
	public void inviaEccezione(List<String> indirizziNotifica, String pathAllegato, String nomeAllegato) {
		inviaNotifica(indirizziNotifica, pathAllegato, nomeAllegato, TipoNotifica.ECCEZIONE);
	}

	@Override
	public void inviaConferma(List<String> indirizziNotifica, String pathAllegato, String nomeAllegato) {
		inviaNotifica(indirizziNotifica, pathAllegato, nomeAllegato, TipoNotifica.CONFERMA);
	}

	@Override
	public void inviaAggiornamento(List<String> indirizziNotifica, String pathAllegato, String nomeAllegato) {
		inviaNotifica(indirizziNotifica, pathAllegato, nomeAllegato, TipoNotifica.AGGIORNAMENTO);
	}

	@Override
	public void inviaAnnullamento(List<String> indirizziNotifica, String pathAllegato, String nomeAllegato) {
		inviaNotifica(indirizziNotifica, pathAllegato, nomeAllegato, TipoNotifica.ANNULLAMENTO);
	}

	private void inviaNotifica(List<String> indirizziNotifica, String pathAllegato, String nomeAllegato, TipoNotifica tipo) {

		PraticaEmail emailCorrente = task.getEnclosingPratica();
		Interoperabile i = emailCorrente.getDati().getInteroperabile();

		String identificatore = emailCorrente.getDati().getOggetto();
		if (i != null && i.getNumeroRegistrazione() != null && i.getDataRegistrazione() != null)
			identificatore += "(" + i.getNumeroRegistrazione() + "/" + i.getDataRegistrazione() + ")";

		ParametriExtra parametriExtra = new ParametriExtra();
		parametriExtra.getIndirizziEmail().addAll(indirizziNotifica);
		parametriExtra.setSalvaEMLNotificaEmail(true);
		parametriExtra.getAllegatiDaCaricare().add(
				task.getEnclosingPratica().getDati().new Allegato(XmlPluginUtil.getNomeAllegato(pathAllegato), nomeAllegato, XmlPluginUtil.getFolderRelativo(pathAllegato)));
		String mittente = null;
		if (i.getRisposta() != null && i.getRisposta().getEmail() != null)
			mittente = i.getRisposta().getEmail();
		else
			mittente = (emailCorrente.getDati().getDestinatariInoltro().size() > 0) ? emailCorrente.getDati().getDestinatariInoltro().get(0)
					: emailCorrente.getDati().getDestinatarioPrincipale().getDestinatario();
		parametriExtra.setMittenteEmail(mittente);

		generaEvento(parametriExtra, EventiIterPEC.RISPOSTA_INTEROPERABILE, identificatore, tipo.name());

	}

}
