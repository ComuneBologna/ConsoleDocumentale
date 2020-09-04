package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.ApprovazioneFirmaTask;

import java.util.List;

public interface RitiroApprovazioneFirmaTaskApi extends ITaskApi {

	public void ritira(Allegato a, ApprovazioneFirmaTask approvazioneFirmaTask, String note, List<String> destinatariNotifica, List<String> ruoli, String fullNameUtente);
}
