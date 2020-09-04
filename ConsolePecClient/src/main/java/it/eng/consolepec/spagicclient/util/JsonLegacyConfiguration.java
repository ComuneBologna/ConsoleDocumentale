package it.eng.consolepec.spagicclient.util;

import java.util.Map;

import com.google.gson.GsonBuilder;

import it.eng.cobo.consolepec.commons.spagic.Message.Attachment;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.cobo.consolepec.util.json.ConsolePecJsonConfiguration.SpagicAttachmentSerializer;
import it.eng.cobo.consolepec.util.json.JsonConfiguration;
import it.eng.consolepec.client.RestClientInvoker;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;

/**
 * Configurazione custom JSON per {@link RestClientInvoker} e {@link AbstractSpagicClientRemoteProxy}, i client per le invocazioni API Legacy
 *
 * @author biagio.tozzi
 *
 */
public class JsonLegacyConfiguration extends JsonConfiguration {

	@Override
	public void configure(GsonBuilder builder) {
		builder.setDateFormat(ConsoleConstants.FORMATO_ISO8601);
		builder.registerTypeAdapter(Map.class, new MapAttachmentDeserializer());
		builder.registerTypeAdapter(Attachment.class, new SpagicAttachmentSerializer());
	}

}
