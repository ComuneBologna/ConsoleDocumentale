package it.eng.portlet.consolepec.spring.bean.converters.requestfascicolo;

import it.eng.consolepec.spagicclient.bean.request.fascicolo.FascicoloRequest;
import it.eng.consolepec.spagicclient.bean.request.inviomassivo.ComunicazioneRequest;
import it.eng.consolepec.spagicclient.bean.request.template.CreaTemplateEmailRequest;
import it.eng.consolepec.spagicclient.bean.request.template.CreaTemplatePdfRequest;
import it.eng.portlet.consolepec.gwt.shared.action.ProtocollaAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TemplatePdfDTO;

public interface RequestFascicoloConverter {

	public CreaTemplateEmailRequest convertTemplateRequest(TemplateDTO templateDTO);

	public CreaTemplatePdfRequest convertTemplateRequest(TemplatePdfDTO templateDTO);

	public ComunicazioneRequest convertComunicazioneRequest(ComunicazioneDTO comunicazioneDTO);

	public FascicoloRequest convertToFascicoloRequest(CreaFascicoloDTO creaFascicoloAction);
	
	public FascicoloRequest convertToFascicoloRequest(CreaFascicoloAction creaFascicoloAction);

	public FascicoloRequest convertToFascicoloRequest(ProtocollaAction protocollaAction, String pathPraticaDaCollegare);
}
