package it.eng.consolepec.xmlplugin.versioning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import it.eng.consolepec.xmlplugin.exception.PraticaException;

class JsonVersione1 extends JsonVersionUpdater {

	final Logger logger = LoggerFactory.getLogger(JsonVersione1.class);

	@Override
	public String getInputVersion() {
		return "0.9";
	}

	@Override
	public String getOutputVersion() {
		return "1.0";
	}

	@Override
	protected JsonElement updateInternal(JsonElement in) {
		// logger.info("Check update da {} a {}", getInputVersion(), getOutputVersion());
		if (getInputVersion().equals(detectVersion(in))) {
			try {

				JsonObject inJsonObject = in.getAsJsonObject();

				// controllo di versione superato, posso aggiornare..

				// aggiornamento del numero di versione
				inJsonObject.addProperty("versione", getOutputVersion());

			} catch (Throwable t) {
				throw new PraticaException(t, "Errore nel check della versione");
			}
		}
		return in;
	}

	@Override
	protected String detectVersion(JsonElement in) {
		return in.getAsJsonObject().get("versione").getAsString();
	}

}
