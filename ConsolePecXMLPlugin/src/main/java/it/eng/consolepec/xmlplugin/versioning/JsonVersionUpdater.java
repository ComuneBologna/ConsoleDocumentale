package it.eng.consolepec.xmlplugin.versioning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;

public abstract class JsonVersionUpdater implements VersionUpdater {

	final Logger logger = LoggerFactory.getLogger(XMLVersionUpdater.class);

	private JsonVersionUpdater next;

	public abstract String getInputVersion();

	public abstract String getOutputVersion();

	protected abstract JsonElement updateInternal(JsonElement in);

	protected abstract String detectVersion(JsonElement in);

	public JsonElement updateVersion(JsonElement in) {
		JsonElement out = updateInternal(in);
		if (next != null)
			out = next.updateVersion(out);
		return out;
	}

	void setNext(JsonVersionUpdater next) {
		this.next = next;
	}

}
