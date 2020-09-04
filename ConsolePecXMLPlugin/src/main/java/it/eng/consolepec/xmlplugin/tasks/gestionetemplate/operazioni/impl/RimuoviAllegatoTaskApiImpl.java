package it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.DatiGestioneAbstractTemplateTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.XMLGestioneAbstractTemplateTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.RimuoviAllegatoTemplateTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.TipoApiTaskTemplate;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class RimuoviAllegatoTaskApiImpl<T extends DatiGestioneAbstractTemplateTask> extends AbstractTemplateTaskApiImpl<T> implements RimuoviAllegatoTemplateTaskApi {

	public RimuoviAllegatoTaskApiImpl(XMLGestioneAbstractTemplateTask<T> task) {
		super(task);
	}

	@Override
	public void rimuoviAllegato(Allegato allegato) {
		if (!XmlPluginUtil.checkPresenzaAllegato(allegato, task.getEnclosingPratica()))
			throw new PraticaException("L'allegato " + allegato.getNome() + " non è presente nel template " + task.getEnclosingPratica().getDati().getIdDocumentale());

		boolean removed = task.getEnclosingPratica().getDati().getAllegati().remove(allegato);

		if (!removed)
			throw new PraticaException("Errore durante la rimozione dell'allegato " + allegato.getNome());

		generaEvento(EventiIterTemplate.ELIMINA_ALLEGATO, task.getCurrentUser(), allegato.getNome());
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return getDatiTemplate().getAllegati().size() != 0;
	}

	@Override
	protected TipoApiTaskTemplate getTipoApiTask() {
		return TipoApiTaskTemplate.RIMUOVI_ALLEGATO;
	}

}
