package it.eng.consolepec.xmlplugin.versioning;

import java.io.InputStream;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.consolepec.xmlplugin.exception.PraticaException;

/**
 * Classe che aggiorna l'xml dalla versione 1.0 alla 1.1
 * 
 * @author fmattiazzo
 * 
 */
public class Versione1_1 extends Versione1 {

	@Override
	public String getInputVersion() {
		return "1.0";
	}

	@Override
	public String getOutputVersion() {
		return "1.1";
	}

	@Override
	protected Document updateInternal(Document in) {

		// logger.debug("Check update da {} a {}", getInputVersion(), getOutputVersion());
		if (getInputVersion().equals(detectVersion(in))) {
			try {

				Element sync = in.createElement("_sync");
				sync.setNodeValue("false");
				in.getFirstChild().appendChild(sync);

				String tipoPratica = getTipoPratica(in);

				if (isFascicolo(tipoPratica))
					updateFascicoli(in);
				else
					updateAltrePratiche(in);

				in.getChildNodes().item(0).getAttributes().getNamedItem("versione").setNodeValue(getOutputVersion());

			} catch (Throwable t) {
				throw new PraticaException(t, "Errore nel check della versione");
			}
		}
		return in;

	}

	@Override
	protected InputStream getInputVersionXSD() {
		return this.getClass().getClassLoader().getResourceAsStream("consolePecXML_V1_1.xsd");
	}

	private void updateAltrePratiche(Document in) throws Exception {

		Node fascicoloCollegato = getNodeX(in, "/pratica/fascicoloCollegato");
		if (fascicoloCollegato == null) {
			logger.debug("Nessun fascicolo collegato");
		} else {
			logger.debug("Trasformazione del fasciolo collegato in elemento della lista di pratiche collegate");
			String alfrescoPath = getValueX(in, "/pratica/fascicoloCollegato/alfrescoPath");
			String tipoPratica = getValueX(in, "/pratica/fascicoloCollegato/tipo");
			if (alfrescoPath == null || tipoPratica == null)
				throw new PraticaException("Il nodo fascicoloCollegato non è valido");

			Element alfrescoPathNode = in.createElement("alfrescoPath");
			alfrescoPathNode.setTextContent(alfrescoPath);

			Element tipoNode = in.createElement("tipo");
			tipoNode.setTextContent(tipoPratica);

			Element praticaCollegataNode = in.createElement("praticaCollegata");
			praticaCollegataNode.appendChild(alfrescoPathNode);
			praticaCollegataNode.appendChild(tipoNode);

			in.getChildNodes().item(0).replaceChild(praticaCollegataNode, fascicoloCollegato);

		}

	}

	private void updateFascicoli(Document in) throws Exception {

		String tipoPratica = getTipoPratica(in);

		Node fascicolo = getNodeX(in, "/pratica/Fascicolo");
		Node praticheCollegate = getNodeX(in, "/pratica/Fascicolo/praticheCollegate");
		if (praticheCollegate != null) {
			logger.debug("Spostamento nodo pratiche collegate");
			fascicolo.removeChild(praticheCollegate);

			if (praticheCollegate.getChildNodes().getLength() != 0)
				for (int i = 0; i < praticheCollegate.getChildNodes().getLength(); i++) {
					Node node = praticheCollegate.getChildNodes().item(i);
					in.getChildNodes().item(0).appendChild(node.cloneNode(true));
				}
		} else {
			logger.debug("Nessuna pratica collegato");
		}

		if (tipoPratica.equalsIgnoreCase(TipologiaPratica.FASCICOLO.getNomeTipologia())) {
			logger.debug("Aggiornamento vecchi fascicoli personali");
			Node tipo = getNodeX(in, "/pratica/tipo");
			tipo.setTextContent(TipologiaPratica.FASCICOLO_PERSONALE.getNomeTipologia());
		}
	}

	// METODI DI UTILITY PER GESTIRE L'XML

	private NodeList getNodesX(Document in, String queryXPath) throws XPathExpressionException {
		XPathFactory xpathfactory = XPathFactory.newInstance();
		XPath xpath = xpathfactory.newXPath();
		XPathExpression expr = xpath.compile(queryXPath);
		Object result = expr.evaluate(in, XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;
		return nodes;
	}

	private Node getNodeX(Document in, String queryXPath) throws XPathExpressionException {
		NodeList nodesX = getNodesX(in, queryXPath);
		if (nodesX.getLength() != 1) // se trova più di un nodo ritorna null anzichè sparare un errore
			return null;
		return nodesX.item(0);
	}

	private String getValueX(Document in, String queryXPath) throws XPathExpressionException {
		Node nodeX = getNodeX(in, queryXPath);
		return (nodeX == null) ? null : nodeX.getTextContent();
	}

	private String getTipoPratica(Document in) throws Exception {
		NodeList nodes = getNodesX(in, "/pratica/tipo");
		if (nodes == null || nodes.getLength() != 1 && nodes.item(0).getFirstChild() == null || nodes.item(0).getFirstChild().getNodeValue() == null)
			throw new Exception("Impossibile determinare il tipo di pratica");
		return nodes.item(0).getFirstChild().getNodeValue();
	}

	private boolean isFascicolo(String tipoPratica) {
		return PraticaUtil.isFascicolo(tipoPratica);
	}
}
