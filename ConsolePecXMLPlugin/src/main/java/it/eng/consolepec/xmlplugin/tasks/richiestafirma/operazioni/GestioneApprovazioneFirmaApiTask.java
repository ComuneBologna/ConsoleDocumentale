package it.eng.consolepec.xmlplugin.tasks.richiestafirma.operazioni;

import java.util.List;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.TipoRispostaApprovazioneFirmaTask;

public interface GestioneApprovazioneFirmaApiTask {

	public abstract void firma(Allegato a, String userID, String ruolo, String note, List<String> destinatariNotifica, String motivazione);

	public abstract void approva(Allegato a, String userID, String ruolo, String note, List<String> destinatariNotifica, String motivazione);

	public abstract void diniega(Allegato a, String userID, String ruolo, String note, List<String> destinatariNotifica, String motivazione);

	public abstract void rispondi(TipoRispostaApprovazioneFirmaTask tipoRisposta, Allegato a, String userID, String ruolo, String note, List<String> destinatariNotifica, String motivazione);
}
