package it.eng.portlet.consolepec.gwt.client.operazioni.cartellafirma;

import it.eng.portlet.consolepec.gwt.client.widget.cartellafirma.FormRicercaCartellaFirma;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistCartellaFirmaStrategy.RicercaCallback;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.CercaDocumentoFirmaVistoAction;

import java.util.List;

/**
*
* Espone i metodi per la ricerca di documenti che fanno parte di un task di firma;
*
* @author biagiot
*
*/
public interface CartellaFirmaRicercaApiClient {

	void cercaDocumentiFirmaVisto(CercaDocumentoFirmaVistoAction filter, RicercaCallback callback);
	List<String> creaFiltroDiRicerca(FormRicercaCartellaFirma formRicercaCartellaFirma, CercaDocumentoFirmaVistoAction action);
}
