package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.CambiaTipologiaAllegatoTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.util.TaskDiFirmaUtil;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class CambiaTipologiaAllegatoTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements CambiaTipologiaAllegatoTaskApi {

	public CambiaTipologiaAllegatoTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true; // TODO
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.CAMBIA_TIPOLOGIA_ALLEGATO;
	}

	@Override
	public void modificaTipologiaAllegato(String nomeAllegato, String tipologiaAllegato) throws PraticaException {
		Pratica<?> pratica = task.getEnclosingPratica();

		for (Allegato allegato : pratica.getDati().getAllegati()) {

			if (nomeAllegato.equals(allegato.getNome())) {

				if (XmlPluginUtil.isAllegatoProtocollato(allegato, pratica) || Boolean.TRUE.equals(allegato.getLock())
						|| TaskDiFirmaUtil.getApprovazioneFirmaTaskAttivoByAllegato(pratica, allegato) != null) {
					throw new PraticaException("L'allegato è bloccato: potrebbe essere in un task di firma/visto/parere o protocollato");
				}

				if (tipologiaAllegato == null) {
					allegato.getTipologiaDocumento().clear();
				}

				if (tipologiaAllegato != null && (allegato.getTipologiaDocumento().isEmpty() || !allegato.getTipologiaDocumento().contains(tipologiaAllegato))) {
					allegato.getTipologiaDocumento().add(tipologiaAllegato);
					generaEvento(EventiIterFascicolo.AGGIUNTA_TIPOLOGIA_ALLEGATO, task.getCurrentUser(), tipologiaAllegato, allegato.getNome());
				}

			}
		}
	}

	@Override
	public void cambiaTipologiaAllegato(String nomeAllegato, String tipologiaAllegato) throws PraticaException {
		Pratica<?> pratica = task.getEnclosingPratica();
		for (Allegato allegato : pratica.getDati().getAllegati()) {
			if (nomeAllegato.equals(allegato.getNome())) {

				if (XmlPluginUtil.isAllegatoProtocollato(allegato, pratica) || Boolean.TRUE.equals(allegato.getLock())
						|| TaskDiFirmaUtil.getApprovazioneFirmaTaskAttivoByAllegato(pratica, allegato) != null) {
					throw new PraticaException("L'allegato è bloccato: potrebbe essere in un task di firma/visto/parere o protocollato");
				}

				allegato.getTipologiaDocumento().clear();

				if (tipologiaAllegato != null) {
					allegato.getTipologiaDocumento().add(tipologiaAllegato);
					generaEvento(EventiIterFascicolo.AGGIUNTA_TIPOLOGIA_ALLEGATO, task.getCurrentUser(), tipologiaAllegato, allegato.getNome());
				}

			}
		}
	}
}
