package it.eng.consolepec.xmlplugin.pratica.operazionipecconcluse.impl.elettorale;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.pratica.operazionipecconcluse.AnnullaElettoraleApiTask;
import it.eng.consolepec.xmlplugin.pratica.operazionipecconcluse.impl.AbstractTaskApiPraticaDisabilitata;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaFascicoloTask;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

import java.util.List;

public class AnnullaElettoraleApiTaskImpl extends AbstractTaskApiPraticaDisabilitata<DatiGestionePECTask> implements AnnullaElettoraleApiTask {

	public AnnullaElettoraleApiTaskImpl(XMLGestionePECTask task) {
		super(task);
	}

	@Override
	public void annullaElettorale(List<Fascicolo> fascicoliCollegati, List<Fascicolo> daArchiviare) {
		PraticaEmailIn emailIn = (PraticaEmailIn) task.getEnclosingPratica();
		for(Fascicolo f : fascicoliCollegati)
			sgancia(f, emailIn, isIn(f, daArchiviare));
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return task.getEnclosingPratica().isRiattivabileElettorale();
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.ANNULLA_ELETTORALE;
	}

	private boolean isIn(Fascicolo fascicolo, List<Fascicolo> daArchiviare) {
		for(Fascicolo f : daArchiviare)
			if(f.equals(fascicolo))
				return true;
		return false;
	}

	private void sgancia(Fascicolo fascicolo, PraticaEmailIn emailIn, boolean archivia) throws PraticaException{
		if (!fascicolo.isAttiva()) {
			RiattivaFascicoloTask taskRiattiva = XmlPluginUtil.getRiattivaTaskCorrente(fascicolo, RiattivaFascicoloTask.class, task.getCurrentUser());
			taskRiattiva.riattivaTaskAssociato();
		}
		XMLTaskFascicolo<?> gestioneTask = XmlPluginUtil.getGestioneTaskCorrente(fascicolo, XMLTaskFascicolo.class, task.getCurrentUser());
		gestioneTask.sganciaPecIn(emailIn);
		if(archivia)
			gestioneTask.concludiFascicolo();
	}
}
