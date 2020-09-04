package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.EliminaFascicoloTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.util.TaskDiFirmaUtil;

public class EliminaFascicoloTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements EliminaFascicoloTaskApi {

	public EliminaFascicoloTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		boolean hasProtocollazioni = getDatiFascicolo().getProtocollazioniCapofila() != null && !getDatiFascicolo().getProtocollazioniCapofila().isEmpty();
		boolean hasPraticheCollegate = task.getEnclosingPratica().hasPraticheCollegate();
		boolean hasFascicoliCollegati = getDatiFascicolo().getCollegamenti() != null && !getDatiFascicolo().getCollegamenti().isEmpty();
		boolean hasApprovazioneFirmaTask = TaskDiFirmaUtil.hasApprovazioneFirmaTask(task.getEnclosingPratica());
		return !hasProtocollazioni && !hasPraticheCollegate && !hasFascicoliCollegati && !hasApprovazioneFirmaTask;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.ELIMINA_FASCICOLO;
	}

}
