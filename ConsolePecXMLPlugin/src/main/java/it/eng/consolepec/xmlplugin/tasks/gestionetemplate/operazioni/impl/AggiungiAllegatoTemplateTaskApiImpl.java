package it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.impl;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.AggiungiAllegato;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.DatiGestioneAbstractTemplateTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.XMLGestioneAbstractTemplateTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.AggiungiAllegatoTemplateTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.TipoApiTaskTemplate;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class AggiungiAllegatoTemplateTaskApiImpl<T extends DatiGestioneAbstractTemplateTask> extends AbstractTemplateTaskApiImpl<T> implements AggiungiAllegatoTemplateTaskApi {

	public AggiungiAllegatoTemplateTaskApiImpl(XMLGestioneAbstractTemplateTask<T> task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected TipoApiTaskTemplate getTipoApiTask() {
		return TipoApiTaskTemplate.AGGIUNGI_ALLEGATO;
	}

	@Override
	public void aggiungiAllegato(Allegato allegato, AggiungiAllegato handler) throws ApplicationException, InvalidArgumentException {

		String nomeAllegato = allegato.getNome();
		Allegato aggiungiAllegato = handler.aggiungiAllegato(new StringBuilder().append(getDatiTemplate().getFolderPath()).append("/").append(task.getEnclosingPratica().getSubFolderPath()).toString(),
				nomeAllegato);
		getDatiTemplate().getAllegati().removeAll(XmlPluginUtil.allegatoInPratica(aggiungiAllegato, task.getEnclosingPratica()));
		getDatiTemplate().getAllegati().add(aggiungiAllegato);

		generaEvento(EventiIterTemplate.CARICA_ALLEGATO, task.getCurrentUser(), allegato.getLabel());
	}

}
