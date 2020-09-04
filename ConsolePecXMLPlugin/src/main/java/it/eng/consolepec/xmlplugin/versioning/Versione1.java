package it.eng.consolepec.xmlplugin.versioning;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import it.eng.consolepec.xmlplugin.exception.PraticaException;

/**
 * Classe che aggiorna l'xml dalla versione 0.9 alla 1.0
 * 
 * @author pluttero
 * 
 */
class Versione1 extends XMLVersionUpdater {
	final Logger logger = LoggerFactory.getLogger(Versione1.class);

	@Override
	public String getInputVersion() {
		return "0.9";
	}

	@Override
	public String getOutputVersion() {
		return "1.0";
	}

	@Override
	protected Document updateInternal(Document in) {
		// logger.debug("Check update da {} a {}", getInputVersion(), getOutputVersion());
		if (getInputVersion().equals(detectVersion(in))) {
			try {

				// controllo di versione superato, posso aggiornare..

				// operazione dummy, in attesa di logica!
				in.getAttributes().getNamedItem("versione").setNodeValue(getOutputVersion());

			} catch (Throwable t) {
				throw new PraticaException(t, "Errore nel check della versione");
			}
		}
		return in;
	}

	@Override
	protected String detectVersion(Document in) {
		if (in.getChildNodes().getLength() > 0 && in.getChildNodes().item(0).getAttributes() != null && in.getChildNodes().item(0).getAttributes().getNamedItem("versione") != null)
			return in.getChildNodes().item(0).getAttributes().getNamedItem("versione").getNodeValue();
		else
			return null;
	}

	@Override
	protected InputStream getInputVersionXSD() {
		return this.getClass().getClassLoader().getResourceAsStream("consolePecXML_V1_0.xsd");
	}

}
