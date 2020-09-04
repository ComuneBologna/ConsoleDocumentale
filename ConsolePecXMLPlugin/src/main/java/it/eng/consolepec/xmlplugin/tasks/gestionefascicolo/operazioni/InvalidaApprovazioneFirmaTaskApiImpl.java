package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.XMLApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.operazioni.AbstractApprovazioneFirmaTaskApi;

/**
 *
 * @author biagiot
 *
 */
public class InvalidaApprovazioneFirmaTaskApiImpl extends AbstractApprovazioneFirmaTaskApi implements InvalidaApprovazioneFirmaTaskApi {

	public InvalidaApprovazioneFirmaTaskApiImpl(XMLApprovazioneFirmaTask task) {
		super(task);
	}

	@Override
	public void invalida() {
		task.getDati().setAttivo(false);
		task.getDati().setValido(false);
	}

	@Override
	protected ITipoApiTask getTipoApiTask() {
		return TipoApiTask.INVALIDA_TASK_FIRMA;
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		String nomeAllegato = task.getDati().getRiferimentoAllegato().getNome();

		for (Allegato allegato : task.getEnclosingPratica().getDati().getAllegati()) {
			if (allegato.getNome().equalsIgnoreCase(nomeAllegato))
				return false;
		}

		return !Boolean.FALSE.equals(task.getDati().getValido());
	}
}