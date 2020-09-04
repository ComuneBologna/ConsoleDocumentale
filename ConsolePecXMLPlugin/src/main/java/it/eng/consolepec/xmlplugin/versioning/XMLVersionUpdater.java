package it.eng.consolepec.xmlplugin.versioning;

import it.eng.consolepec.xmlplugin.exception.PraticaException;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

/**
 * Chain di updater. Versioni concrete aggiornano l'xml di uno step
 * 
 * @author pluttero
 * 
 */
public abstract class XMLVersionUpdater implements VersionUpdater{
	final Logger logger = LoggerFactory.getLogger(XMLVersionUpdater.class);

	private XMLVersionUpdater next;

	public abstract String getInputVersion();

	public abstract String getOutputVersion();

	protected abstract Document updateInternal(Document in);

	protected abstract String detectVersion(Document in);

	protected abstract InputStream getInputVersionXSD();

	public Document updateVersion(Document in) {
		Document out = updateInternal(in);
		if (next != null)
			out = next.updateVersion(out);
		return out;
	}

	public ValidationResult validateDocument(Document in) {
		if (getOutputVersion().equals(detectVersion(in))) {
			ValidationResult vr = new ValidationResult();
			try {

				InputStream xsd = getInputVersionXSD();

				// modifica per consentire il passaggio della validazione in VM ibm
				if (xsd == null) {
					ValidationResult validationResult = new ValidationResult();
					validationResult.setValid(true);
					return validationResult;
				}

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(in);
				StringWriter out = new StringWriter();
				StreamResult result = new StreamResult(out);
				transformer.transform(source, result);
				SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

				Schema schema = factory.newSchema(new StreamSource(xsd));
				javax.xml.validation.Validator validator = schema.newValidator();
				validator.validate(new StreamSource(new StringReader(out.toString())));
				vr.setValid(true);
			} catch (Throwable t) {
				vr.setErrorMsg(t.getLocalizedMessage());
				vr.setValid(false);
			}
			return vr;
		} else if (next != null)
			return next.validateDocument(in);
		else
			throw new PraticaException("Non è stato trovato alcun componente in grado di validare il documento passato");
	}

	void setNext(XMLVersionUpdater next) {
		this.next = next;
	}

	public class ValidationResult {
		@Getter
		@Setter(value = AccessLevel.PACKAGE)
		boolean isValid;
		@Getter
		@Setter(value = AccessLevel.PACKAGE)
		String errorMsg;
	}
}
