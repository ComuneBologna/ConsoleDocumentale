package it.eng.portlet.consolepec.spring.bean.cartellafirma;

import java.util.List;
import java.util.Map;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.InformazioniNotificaTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.TipologiaFirma;
import it.eng.portlet.consolepec.gwt.shared.model.FileDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.TipoRispostaParereDTO;
import it.eng.portlet.consolepec.spring.firma.impl.CredenzialiFirma;

/**
 *
 * @author biagiot
 *
 */
public interface CartellaFirmaWizardUtil {

	public List<LockedPratica> firmaDocumenti(Map<String, List<String>> mapPathAllegati, CredenzialiFirma cf, TipologiaFirma tipologiaFirma, boolean riassegna,
			InformazioniNotificaTaskFirmaDTO infoNotifica, String ruolo, List<FileDTO> fileDaAllegare, String motivazione) throws SpagicClientException;

	public List<LockedPratica> vistoDocumenti(Map<String, List<String>> mapPathAllegati, boolean riassegna, InformazioniNotificaTaskFirmaDTO infoNotifica, String ruolo, List<FileDTO> fileDaAllegare,
			String motivazione) throws SpagicClientException;

	public List<LockedPratica> diniegoDocumenti(Map<String, List<String>> mapPathAllegati, boolean riassegna, InformazioniNotificaTaskFirmaDTO infoNotifica, String ruolo, List<FileDTO> fileDaAllegare,
			String motivazione) throws SpagicClientException;

	public List<LockedPratica> rispondiDocumenti(TipoRispostaParereDTO tipoRisposta, Map<String, List<String>> mapPathAllegati, boolean riassegna, InformazioniNotificaTaskFirmaDTO infoNotifica,
			String ruolo, List<FileDTO> fileDaAllegare, String motivazione) throws SpagicClientException;

	public List<LockedPratica> ritiroDocumenti(Map<String, List<String>> mapPathAllegati, InformazioniNotificaTaskFirmaDTO infoNotifica, String motivazione) throws SpagicClientException;

	public List<LockedPratica> evadiDocumenti(Map<String, List<Integer>> praticaIdTaskFirmaMap) throws SpagicClientException;
}
