package it.eng.consolepec.spagicclient;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import it.eng.consolepec.spagicclient.model.taskfirma.DestinatarioTaskFirmaClient;
import it.eng.consolepec.spagicclient.model.taskfirma.RichiestaTaskFirmaResult;
import it.eng.consolepec.spagicclient.model.taskfirma.TipoRichiestaTaskFirmaClient;
import it.eng.consolepec.spagicclient.model.taskfirma.TipoRispostaTaskFirmaClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

/**
 *
 * @author biagiot
 *
 */
public interface SpagicClientTaskFirma {

	public List<LockedPratica> firmaDocumenti(Map<String, List<String>> praticaAllegatiMap, String userFirma, String passFirma, String otpFirma, String tipoFirma, boolean riassegnazione,
			String assegnatario, String note, List<String> indirizziNotifica, String operatore, Utente utente, String ruolo, List<File> fileDaAllegare,
			String motivazione) throws SpagicClientException;

	public List<LockedPratica> vistoDocumenti(Map<String, List<String>> praticaAllegatiMap, boolean riassegnazione, String assegnatario, String note, List<String> indirizziNotifica, String operatore,
			Utente utente, String ruolo, List<File> fileDaAllegare, String motivazione) throws SpagicClientException;

	public List<LockedPratica> diniegoDocumenti(Map<String, List<String>> praticaAllegatiMap, boolean riassegnazione, String assegnatario, String note, List<String> indirizziNotifica,
			String operatore, Utente utente, String ruolo, List<File> fileDaAllegare, String motivazione) throws SpagicClientException;

	public List<LockedPratica> rispondiDocumenti(TipoRispostaTaskFirmaClient tipoRisposta, Map<String, List<String>> praticaAllegatiMap, boolean riassegnazione, String assegnatario, String note,
			List<String> indirizziNotifica, String operatore, Utente utente, String ruolo, List<File> fileDaAllegare, String motivazione) throws SpagicClientException;

	public List<LockedPratica> ritiroDocumenti(Map<String, List<String>> praticaAllegatiMap, String note, List<String> indirizziNotifica, Utente utente,
			String motivazione) throws SpagicClientException;

	public RichiestaTaskFirmaResult richiediFirmaVistoDocumenti(String praticaPath, List<String> nomiAllegati, Utente utente, TipoRichiestaTaskFirmaClient tipoRichiesta, String oggetto,
			String gruppoProponente, List<DestinatarioTaskFirmaClient> destinatari, String note, List<String> indirizziNotifica, String mittenteOriginale, Date dataScadenza, Integer oraScadenza,
			Integer minutoScadenza, String motivazione) throws SpagicClientException;

	public List<LockedPratica> evadiDocumenti(Map<String, List<Integer>> praticaIdTaskFirmaMap, Utente utente) throws SpagicClientException;
}
