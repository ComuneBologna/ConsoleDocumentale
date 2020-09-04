package it.eng.portlet.consolepec.gwt.servermock;

import it.eng.portlet.consolepec.gwt.shared.action.ProtocollaAction;
import it.eng.portlet.consolepec.gwt.shared.action.ProtocollaResult;
import it.eng.portlet.consolepec.gwt.shared.dto.Group;
import it.eng.portlet.consolepec.gwt.shared.dto.Row;
import it.eng.portlet.consolepec.gwt.shared.dto.VistorElement;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;

import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class ProtocollaMockActionHandler implements ActionHandler<ProtocollaAction, ProtocollaResult> {

	private Logger logger = LoggerFactory.getLogger(ProtocollaMockActionHandler.class);

	public ProtocollaMockActionHandler() {
	}

	@Override
	public ProtocollaResult execute(ProtocollaAction action, ExecutionContext context) throws ActionException {
		ProtocollaResult res = new ProtocollaResult();
		res.setError(false);
		String result = this.getProtocollazioneXmlRequest( action.getValueMap() );
		try {
			
	//LOGICA DI MOCKUP
			PecInDTO dd = PecInDB.getInstance().getDettaglio(action.getIdFascicolo());
			dd.setNumeroPG("999");
			dd.setAnnoPG("2013");
	//LOGICA DI MOCKUP
			res.setPecInDTO(dd);
		} catch (Exception e) {
			logger.error("Errore in fase di protocollazione", e);
			setError(res);
		}
		return res;
	}

	private void setError(ProtocollaResult res) {
		res.setError(true);
		res.setMessageError("Servizio temporaneamente non disponibile");
	}

	@Override
	public void undo(ProtocollaAction action, ProtocollaResult result, ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<ProtocollaAction> getActionType() {
		return ProtocollaAction.class;
	}
	
	private String getProtocollazioneXmlRequest(Map<String, Map<String, it.eng.portlet.consolepec.gwt.shared.dto.Element>> map) {

		Document document = DocumentHelper.createDocument();

		// root element
		Element request = document.addElement("request");

		request.addNamespace("", "http://model.webservice.protocollazionecompleta.cobo.eng.it");

		for (String elemento : map.keySet()) {

			// elemento di livello 1
			Map<String, it.eng.portlet.consolepec.gwt.shared.dto.Element> internalMap = map.get(elemento);

			if (internalMap.size() == 0)
				continue;

			final Element domElementLev1 = request.addElement(elemento);

			for (String elementoInterno : internalMap.keySet()) {
				it.eng.portlet.consolepec.gwt.shared.dto.Element element = internalMap.get(elementoInterno);

				element.accept(new VistorElement() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;
					Element current = domElementLev1;

					@Override
					public void visit(Row r) {
						current.addElement(r.getName()).setText(r.getValue());

					}

					@Override
					public void visit(Group g) {
						if (g.getElements().size() > 0) {
							current = domElementLev1.addElement(g.getName());
							for (it.eng.portlet.consolepec.gwt.shared.dto.Element el : g.getElements()) {
								el.accept(this);
							}
							current = domElementLev1;
						}

					}

					@Override
					public void visit(it.eng.portlet.consolepec.gwt.shared.dto.Element e) {
						// TODO Auto-generated method stub

					}
				});
			}

		}

		String xml = document.asXML().replaceAll("xmlns=\"\"", "");
		logger.debug(xml);
		return document.asXML().replaceAll("xmlns=\"\"", "");
	}
}
