package it.eng.portlet.consolepec.gwt.server.protocollazione.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.bologna.comune.alfresco.protocollazione.Request;
import it.bologna.comune.alfresco.protocollazione.Response;
import it.bologna.comune.alfresco.protocollazione.Tipopratica;
import it.eng.consolepec.spagicclient.SpagicClientGetTipoPraticaProtocollazione;
import it.eng.portlet.consolepec.gwt.server.protocollazione.ElenchiCategorieProtocollazione;

public class ElenchiCatagorieProtocollazioneImpl implements ElenchiCategorieProtocollazione {

	private static final Logger logger = LoggerFactory.getLogger(ElenchiCatagorieProtocollazioneImpl.class);

	@Autowired
	SpagicClientGetTipoPraticaProtocollazione spagicClientGetTipoPraticaProtocollazione;

	@Override
	public Map<String, String> getCategorieProtocollazione(Categoria categoria) {
		it.bologna.comune.alfresco.protocollazione.Request request = new Request();
		Map<String, String> mapCategorie = null;
		switch (categoria) {
		case ENTRATA: {
			request.setTipo("E");
			break;
		}
		case USCITA: {
			request.setTipo("U");
			break;
		}
		case INTERNA: {
			request.setTipo("I");
			break;
		}

		}
		try {
			Response response = spagicClientGetTipoPraticaProtocollazione.getTipoPratica(request);
			mapCategorie = createMapCategorie(response);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
		return mapCategorie;

	}

	private static Map<String, String> createMapCategorie(Response response) {

		LinkedHashMap<String, String> mapCategorie = new LinkedHashMap<String, String>();

		for (Tipopratica tipopratica : response.getTipopratica()) {
			mapCategorie.put(tipopratica.getId(), tipopratica.getDescrizione());
		}
		return mapCategorie;

	}

}
