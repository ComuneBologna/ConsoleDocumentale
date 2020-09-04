package it.eng.consolepec.xmlplugin.versioning;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.consolepec.xmlplugin.exception.PraticaException;

class JsonVersione1_1 extends JsonVersionUpdater {

	final Logger logger = LoggerFactory.getLogger(JsonVersione1_1.class);

	@Override
	public String getInputVersion() {
		return "1.0";
	}

	@Override
	public String getOutputVersion() {
		return "1.1";
	}

	@Override
	protected JsonElement updateInternal(JsonElement in) {
		// logger.info("Check update da {} a {}", getInputVersion(), getOutputVersion());
		
		if (getInputVersion().equals(detectVersion(in))) {
			try {

				JsonObject inJsonObject = in.getAsJsonObject();

				// aggiorno tutte le pratiche di tipo FASCICOLO a FASCICOLO_PERSONALE
				String tipoPraticaString = inJsonObject.get("tipo").getAsString();
				if (tipoPraticaString.equals(TipologiaPratica.FASCICOLO.getNomeTipologia()))
					inJsonObject.addProperty("tipo", TipologiaPratica.FASCICOLO_PERSONALE.getNomeTipologia());

				if (isFascicolo(tipoPraticaString))
					updateFascicoli(inJsonObject);
				else
					updateAltrePratiche(inJsonObject);

				// aggiornamento del numero di versione
				inJsonObject.addProperty("versione", getOutputVersion());

			} catch (Throwable t) {
				throw new PraticaException(t, "Errore nel check della versione");
			}
		}
		return in;
	}

	private void updateAltrePratiche(JsonObject inJsonObject) {
		JsonObject fascicoloCollegatoJsonObject = inJsonObject.getAsJsonObject("fascicoloCollegato");
		if (fascicoloCollegatoJsonObject != null) {
			String alfrescoPath = fascicoloCollegatoJsonObject.get("alfrescoPath").getAsString();
			String tipoPratica = fascicoloCollegatoJsonObject.get("tipo").getAsString();
			if (alfrescoPath == null || tipoPratica == null)
				throw new PraticaException("Il nodo fascicoloCollegato non è valido");

			JsonObject praticaCollegataJson = new JsonObject();
			praticaCollegataJson.addProperty("tipo", tipoPratica);
			praticaCollegataJson.addProperty("alfrescoPath", alfrescoPath);

			inJsonObject.add("praticaCollegata", praticaCollegataJson);
			inJsonObject.remove("fascicoloCollegato");

		}
	}

	private void updateFascicoli(JsonObject inJsonObject) {
		JsonObject praticheCollegateJson = inJsonObject.getAsJsonObject("fascicolo").getAsJsonObject("praticheCollegate");
		inJsonObject.getAsJsonObject("fascicolo").remove("praticheCollegate");
		if (praticheCollegateJson != null) {
			
			for (Entry<String, JsonElement> element : praticheCollegateJson.entrySet()) {
				inJsonObject.getAsJsonObject("fascicolo").add(element.getKey(), element.getValue());
			}
			
		}
	}

	@Override
	protected String detectVersion(JsonElement in) {
		return in.getAsJsonObject().get("versione").getAsString();
	}

	private boolean isFascicolo(String tipoPratica) {
		return PraticaUtil.isFascicolo(tipoPratica);
	}
}
