package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.FirmaAllegatiTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

import java.util.List;

public class FirmaAllegatiApiTaskImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements FirmaAllegatiTaskApi {

	public FirmaAllegatiApiTaskImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void firmaAllegati(List<Allegato> allegati) throws PraticaException {
		List<Allegato> list = XmlPluginUtil.allegatoInPratica(allegati, task.getEnclosingPratica());
		if (list.size() != allegati.size()) {
			throw new PraticaException("L'allegato non appartiene alla pratica associata a questo task");
		}
		getDatiFascicolo().getAllegati().removeAll(list);
		getDatiFascicolo().getAllegati().addAll(allegati);

		for (Allegato a : list)
			generaEvento(EventiIterFascicolo.FIRMA_ALLEGATO, task.getCurrentUser(), a.getLabel());
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return getDatiFascicolo().getAllegati().size() != 0;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.FIRMA;
	}

}
