package it.eng.portlet.consolepec.spring.bean.search.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.dto.TaskFirmaDocumentale;
import it.eng.cobo.consolepec.commons.dto.TaskFirmaDocumentale.DestinatarioDocumentale;
import it.eng.cobo.consolepec.commons.dto.TaskFirmaDocumentale.DestinatarioGruppoDocumentale;
import it.eng.cobo.consolepec.commons.dto.TaskFirmaDocumentale.DestinatarioUtenteDocumentale;
import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.consolepec.spagicclient.search.SearchObjectResult;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioGruppoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioUtenteDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DocumentoFirmaVistoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.ProponenteDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.StatoDestinatarioTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.StatoTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.TipoPropostaTaskFirmaDTO;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.IGestionePresaInCarico;
import it.eng.portlet.consolepec.spring.bean.search.MongoDbSearchResponseParser;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;

public class MongoDbSearchResponseParserImpl implements MongoDbSearchResponseParser {

	private static final Logger logger = LoggerFactory.getLogger(MongoDbSearchResponseParserImpl.class);

	private MetadatoToPraticaChain chain = null;

	@Autowired
	IGestionePresaInCarico gestionePresaInCarico;

	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	XMLPluginToDTOConverter utilPratica;

	public void init() {
		chain = new MetadatoToPratica();
		chain.setHandler(gestioneConfigurazioni).setNext(new MetadatoToFascicolo()).setNext(new MetadatoToPec()).setNext(new MetadatoToPecIn()).setNext(new MetadatoToPecOut()).setNext(
				new MetadatoToPecDaEprotocollo()).setNext(new MetadatoToPraticaModulistica()).setNext(new MetadatoToTemplate()).setNext(new MetadatoToComuncazione()).setNext(
						new MetadatoToTemplatePdf());
	}

	private PraticaDTO metadatiResponseToPraticaDTO(SearchObjectResult metadatiResponse, String folderPath) {
		return chain.creaPratica(metadatiResponse, folderPath);
	}

	@Override
	public List<PraticaDTO> metadatiResponseToPraticaDTO(List<SearchObjectResult> searchMetadata) {
		List<PraticaDTO> pratiche = new ArrayList<PraticaDTO>();
		logger.debug("Il servizio recupera {} risultati", searchMetadata.size());

		for (SearchObjectResult f : searchMetadata) {
			String alfrescoPath = (String) f.getValue("alfrescoPath");
			alfrescoPath = Base64Utils.URLencodeAlfrescoPath(alfrescoPath);
			PraticaDTO pratica = metadatiResponseToPraticaDTO(f, alfrescoPath);
			gestionePresaInCarico.caricaPresaInCarico(pratica);
			pratiche.add(pratica);
		}
		return pratiche;
	}

	@Override
	public List<DocumentoFirmaVistoDTO> metadatiResponseToDocumentoFirmaVistoDTO(List<TaskFirmaDocumentale> searchMetadata) {

		List<DocumentoFirmaVistoDTO> result = new ArrayList<DocumentoFirmaVistoDTO>();
		for (TaskFirmaDocumentale search : searchMetadata) {

			Pratica<?> loadPratica = praticaSessionUtil.loadPratica(search.getPraticaPath());
			FascicoloDTO f = (FascicoloDTO) utilPratica.praticaToDTO(loadPratica);

			final DocumentoFirmaVistoDTO d = new DocumentoFirmaVistoDTO();
			d.setClientIdFascicolo(f.getClientID());
			for (AllegatoDTO allegato : f.getAllegati()) {
				if (allegato.getNome().equalsIgnoreCase(search.getAllegato().getNome())) {
					d.setAllegato(allegato);
				}
			}

			if (d.getAllegato() != null && d.getClientIdFascicolo() != null) {
				d.setTipologiaPratica(f.getTipologiaPratica());
				d.setTitoloFascicolo(f.getTitolo());
				d.setNote(f.getNote());
				d.setTipoRichiesta(TipoPropostaTaskFirmaDTO.valueOf(search.getTipoRichiesta()));
				d.setStatoRichiesta(StatoTaskFirmaDTO.valueOf(search.getStatoRichiesta()));
				d.setDataProposta(DateUtils.DATEFORMAT_DATEH.format(search.getDataProposta()));
				ProponenteDTO gruppoProponente = new ProponenteDTO();
				gruppoProponente.setNomeGruppo(gestioneConfigurazioni.getAnagraficaRuolo(search.getGruppoProponente()).getEtichetta());
				d.setGruppoProponente(gruppoProponente);
				d.setMittenteOriginale(search.getMittenteOriginale());
				if (search.getDataScadenza() != null) {
					d.setDataScadenza(DateUtils.DATEFORMAT_DATEH.format(search.getDataScadenza()));
				}
				TreeSet<DestinatarioDTO> destinatari = new TreeSet<DestinatarioDTO>();
				for (DestinatarioDocumentale dest : search.getDestinatari()) {
					destinatari.add(convertDestinatarioToDTO(dest, d));
				}
				d.setDestinatariFirma(destinatari);
				d.setIdTaskFirma(search.getIdTaskFirma());
				d.setOperazioniDestinatarioAbilitate(search.isOperazioniDestinatarioAbilitate());
				d.setOperazioniProponenteAbilitate(search.isOperazioniProponenteAbilitate());
				d.setLetto(true);
				d.setRiassegnabilePerProponente(loadRiassegnabilita(gestioneConfigurazioni.getAnagraficaRuolo(search.getGruppoProponente())));

				result.add(d);
			}
		}
		return result;
	}

	private boolean loadRiassegnabilita(AnagraficaRuolo anagraficaRuolo) {
		if (anagraficaRuolo == null)
			return true;
		return gestioneConfigurazioni.isCartellaFirmaRiassegnabile(anagraficaRuolo);
	}

	private DestinatarioDTO convertDestinatarioToDTO(DestinatarioDocumentale destinatario, DocumentoFirmaVistoDTO doc) {
		if (destinatario instanceof DestinatarioUtenteDocumentale) {
			if (doc.getTaskPerRuoli() == null) {
				doc.setTaskPerRuoli(false);
			}
			DestinatarioUtenteDocumentale dest = (DestinatarioUtenteDocumentale) destinatario;
			DestinatarioUtenteDTO result = new DestinatarioUtenteDTO();
			result.setUserId(dest.getUserid());
			result.setNome(dest.getNome());
			result.setCognome(dest.getCognome());
			result.setNomeCompleto(dest.getNome() + " " + dest.getCognome());
			result.setSettore(dest.getSettore());
			result.setMatricola(dest.getMatricola());
			StatoDestinatarioTaskFirmaDTO statoRichiesta = StatoDestinatarioTaskFirmaDTO.valueOf(dest.getStato());
			result.setStatoRichiesta(statoRichiesta);
			return result;
		} else if (destinatario instanceof DestinatarioGruppoDocumentale) {
			if (doc.getTaskPerRuoli() == null) {
				doc.setTaskPerRuoli(true);
			}
			DestinatarioGruppoDocumentale dest = (DestinatarioGruppoDocumentale) destinatario;
			DestinatarioGruppoDTO result = new DestinatarioGruppoDTO();
			result.setNomeGruppoConsole(dest.getNomeGruppo());
			result.setNomeGruppoDisplay(gestioneConfigurazioni.getAnagraficaRuolo(dest.getNomeGruppo()).getEtichetta());
			StatoDestinatarioTaskFirmaDTO statoRichiesta = StatoDestinatarioTaskFirmaDTO.valueOf(dest.getStato());
			result.setStatoRichiesta(statoRichiesta);
			return result;
		} else {
			throw new IllegalArgumentException("Tipo destinatario non valido");
		}
	}

}
