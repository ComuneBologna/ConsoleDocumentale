package it.eng.portlet.consolepec.spring.bean.converters.requestfascicolo.impl;

import org.springframework.beans.factory.annotation.Autowired;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.spagicclient.bean.request.fascicolo.FascicoloRequest;
import it.eng.consolepec.spagicclient.bean.request.inviomassivo.ComunicazioneRequest;
import it.eng.consolepec.spagicclient.bean.request.template.CampoMetadato;
import it.eng.consolepec.spagicclient.bean.request.template.CampoTemplate;
import it.eng.consolepec.spagicclient.bean.request.template.CampoTemplate.TipoCampoTemplate;
import it.eng.consolepec.spagicclient.bean.request.template.CreaTemplateEmailRequest;
import it.eng.consolepec.spagicclient.bean.request.template.CreaTemplatePdfRequest;
import it.eng.consolepec.spagicclient.remoteproxy.util.DatiAggiuntiviUtil;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.action.ProtocollaAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.CampoTemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TemplatePdfDTO;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.converters.requestfascicolo.RequestFascicoloConverter;

public class RequestFascicoloConverterImpl implements RequestFascicoloConverter {

	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;

	@Override
	public FascicoloRequest convertToFascicoloRequest(ProtocollaAction protocollaAction, String pathPraticaDaCollegare) {
		FascicoloRequest fascicoloRequest = new FascicoloRequest();
		fascicoloRequest.setTipo(protocollaAction.getTipoPratica());
		fascicoloRequest.setTitolo(protocollaAction.getDatiPerProtocollazione().getTitolo());
		fascicoloRequest.setNote(protocollaAction.getDatiPerProtocollazione().getNote());
		fascicoloRequest.setAssegnatario(protocollaAction.getDatiPerProtocollazione().getAssegnatario());
		fascicoloRequest.setPathPraticaDaCollegare(pathPraticaDaCollegare);
		return fascicoloRequest;
	}

	@Override
	public FascicoloRequest convertToFascicoloRequest(CreaFascicoloAction creaFascicoloAction) {
		final FascicoloRequest fascicoloRequest = new FascicoloRequest();

		fascicoloRequest.setTipo(creaFascicoloAction.getTipologiaFascicolo().getNomeTipologia());
		fascicoloRequest.setTitolo(creaFascicoloAction.getTitolo());
		fascicoloRequest.setNote(creaFascicoloAction.getNote());
		fascicoloRequest.setAssegnatario(creaFascicoloAction.getAssegnatario());

		for (DatoAggiuntivo dag : creaFascicoloAction.getDatiAggiuntivi()) {
			fascicoloRequest.getDatiAggiuntivi().add(DatiAggiuntiviUtil.datoCommonToDatoSpagic(dag));
		}

		if (creaFascicoloAction.getClientID() != null) {
			fascicoloRequest.setPathPraticaDaCollegare(Base64Utils.URLdecodeAlfrescoPath(creaFascicoloAction.getClientID()));
		}
		return fascicoloRequest;
	}

	@Override
	public CreaTemplateEmailRequest convertTemplateRequest(TemplateDTO dto) {
		CreaTemplateEmailRequest request = new CreaTemplateEmailRequest();

		request.setNome(dto.getNome());
		request.setDescrizione(dto.getDescrizione());

		request.setOggettoMail(dto.getOggettoMail());

		request.setCorpoMail(dto.getCorpoMail());
		request.setMittente(dto.getMittente());
		request.setDestinatari(dto.getDestinatari());
		request.setDestinatariCC(dto.getDestinatariCC());

		for (CampoTemplateDTO c : dto.getCampi()) {

			CampoMetadato cm = null;
			if (c.getCampoMetadato() != null) {
				cm = new CampoMetadato(c.getCampoMetadato().getIdMetadato(), c.getCampoMetadato().getEtichettaMetadato());
			}

			request.getCampi().add(
					new CampoTemplate(c.getNome(), TipoCampoTemplate.valueOf(c.getTipo().name()), c.getFormato(), c.getRegexValidazione(), c.getLunghezzaMassima(), c.getValoriLista(), cm));
		}

		for (TipologiaPratica fa : dto.getFascicoliAbilitati()) {
			request.getFascicoliAbilitati().add(fa.getNomeTipologia());
		}

		if (gestioneConfigurazioni.getAnagraficaRuoloByEtichetta(dto.getAssegnatario()) != null) {
			request.setAssegnatario(gestioneConfigurazioni.getAnagraficaRuoloByEtichetta(dto.getAssegnatario()).getRuolo());
		} else {
			throw new IllegalArgumentException("Assegnatario non valido");
		}

		return request;
	}

	@Override
	public CreaTemplatePdfRequest convertTemplateRequest(TemplatePdfDTO dto) {

		CreaTemplatePdfRequest request = new CreaTemplatePdfRequest();

		request.setNome(dto.getNome());
		request.setDescrizione(dto.getDescrizione());
		request.setTitoloFile(dto.getTitoloFile());

		for (CampoTemplateDTO c : dto.getCampi()) {

			CampoMetadato cm = null;
			if (c.getCampoMetadato() != null) {
				cm = new CampoMetadato(c.getCampoMetadato().getIdMetadato(), c.getCampoMetadato().getEtichettaMetadato());
			}

			request.getCampi().add(
					new CampoTemplate(c.getNome(), TipoCampoTemplate.valueOf(c.getTipo().name()), c.getFormato(), c.getRegexValidazione(), c.getLunghezzaMassima(), c.getValoriLista(), cm));
		}

		for (TipologiaPratica fa : dto.getFascicoliAbilitati()) {
			request.getFascicoliAbilitati().add(fa.getNomeTipologia());
		}

		if (gestioneConfigurazioni.getAnagraficaRuoloByEtichetta(dto.getAssegnatario()) != null) {
			request.setAssegnatario(gestioneConfigurazioni.getAnagraficaRuoloByEtichetta(dto.getAssegnatario()).getRuolo());
		} else {
			throw new IllegalArgumentException("Assegnatario non valido");
		}

		return request;
	}

	@Override
	public ComunicazioneRequest convertComunicazioneRequest(ComunicazioneDTO dto) {
		ComunicazioneRequest request = new ComunicazioneRequest();

		request.setCodice(dto.getCodice());
		request.setDescrizione(dto.getDescrizione());
		request.setIdTemplate(dto.getIdDocumentaleTemplate());

		request.setAssegnatario(dto.getAssegnatario());
		return request;
	}

	@Override
	public FascicoloRequest convertToFascicoloRequest(CreaFascicoloDTO creaFascicoloAction) {
		final FascicoloRequest fascicoloRequest = new FascicoloRequest();

		fascicoloRequest.setTipo(creaFascicoloAction.getTipologiaFascicolo().getNomeTipologia());
		fascicoloRequest.setTitolo(creaFascicoloAction.getTitolo());
		fascicoloRequest.setNote(creaFascicoloAction.getNote());
		fascicoloRequest.setAssegnatario(creaFascicoloAction.getAssegnatario());

		for (DatoAggiuntivo dag : creaFascicoloAction.getDatiAggiuntivi()) {
			fascicoloRequest.getDatiAggiuntivi().add(DatiAggiuntiviUtil.datoCommonToDatoSpagic(dag));
		}

		if (creaFascicoloAction.getClientID() != null) {
			fascicoloRequest.setPathPraticaDaCollegare(Base64Utils.URLdecodeAlfrescoPath(creaFascicoloAction.getClientID()));
		}
		return fascicoloRequest;
	}

}
