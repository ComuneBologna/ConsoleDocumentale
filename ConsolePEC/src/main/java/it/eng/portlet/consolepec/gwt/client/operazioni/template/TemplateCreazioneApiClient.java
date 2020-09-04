package it.eng.portlet.consolepec.gwt.client.operazioni.template;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.portlet.consolepec.gwt.client.widget.template.AbstractCorpoTemplateWidget;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO;

import java.util.Map;

/**
 *
 * @author biagiot
 *
 */
public interface TemplateCreazioneApiClient {

	public static final String OPERATION_ELIMINA_FASCICOLO = "Elimina";
	public static final String OPERATION_AGGIUNGI_FASCICOLO = "Aggiungi";

	<T extends BaseTemplateDTO> void creaModello(T modello, CallbackTemplate<T> callback);
	<T extends BaseTemplateDTO> void salvaModello(T template, String path, CallbackTemplate<T> callback);
	<T extends BaseTemplateDTO> void creaModelloPerCopia(String idDocumentale, CallbackTemplate<T> callback);
	void loadEtichetteMetadatiMap(TipologiaPratica tipoPratica, CallbackMap callbackMap);
	void gestisciModificheFascicoli(AbstractCorpoTemplateWidget<?> corpoTemplateWidget, String text, String operation);

	public interface CallbackMap {
		void onComplete(Map<String, String> map);
		void onError(String errorMessage);
	}

	public interface CallbackTemplate<T extends BaseTemplateDTO> {
		void onComplete(T template);
		void onError(String errorMessage);
	}
}
