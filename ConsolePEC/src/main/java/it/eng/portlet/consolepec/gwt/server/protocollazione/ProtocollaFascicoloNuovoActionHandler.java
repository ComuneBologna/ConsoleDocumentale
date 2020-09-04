package it.eng.portlet.consolepec.gwt.server.protocollazione;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.bologna.comune.alfresco.generic.service.Response;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo.DatoAggiuntivoVisitorAdapter;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreSingolo;
import it.eng.consolepec.spagicclient.SpagicClientProtocollazioneCompleta;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.CreaFascicoloDto;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.ProtocollaFascicoloNuovoAction;
import it.eng.portlet.consolepec.gwt.shared.action.ProtocollaFascicoloNuovoActionResult;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.dto.Group;
import it.eng.portlet.consolepec.gwt.shared.dto.Row;
import it.eng.portlet.consolepec.gwt.shared.dto.VistorElement;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class ProtocollaFascicoloNuovoActionHandler implements ActionHandler<ProtocollaFascicoloNuovoAction, ProtocollaFascicoloNuovoActionResult> {

	@Autowired
	XMLPluginToDTOConverter praticaUtil;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;
	@Autowired
	SpagicClientProtocollazioneCompleta spagicClientProtocollazioneCompleta;

	private final Logger logger = LoggerFactory.getLogger(ProtocollaFascicoloNuovoActionHandler.class);

	public ProtocollaFascicoloNuovoActionHandler() {}

	@Override
	public ProtocollaFascicoloNuovoActionResult execute(ProtocollaFascicoloNuovoAction action, ExecutionContext context) throws ActionException {

		/* recupero i parametri dalla request */
		String pathPratica = Base64Utils.URLdecodeAlfrescoPath(action.getCreaFascicoloDTO().getClientID());
		String annoCapofila = action.getDatiPerProtocollazione().getDatiPg().getAnnoPg();
		String numeroCapofila = action.getDatiPerProtocollazione().getDatiPg().getNumeroPg();
		boolean capofilaFromBa01 = action.getDatiPerProtocollazione().getDatiPg().isCapofilaFromBa01();

		/* protocollazione */
		return protocollaFascicolo(action, pathPratica, annoCapofila, numeroCapofila, capofilaFromBa01);
	}

	@Override
	public void undo(ProtocollaFascicoloNuovoAction action, ProtocollaFascicoloNuovoActionResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<ProtocollaFascicoloNuovoAction> getActionType() {
		return ProtocollaFascicoloNuovoAction.class;
	}

	private ProtocollaFascicoloNuovoActionResult protocollaFascicolo(ProtocollaFascicoloNuovoAction action, String pathPratica, String annoCapofila, String numeroCapofila, boolean capofilaFromBa01) {

		ProtocollaFascicoloNuovoActionResult res = new ProtocollaFascicoloNuovoActionResult();

		try {

			String request = getProtocollazioneXmlRequest(action.getValueMap());

			CreaFascicoloDto creaFascicoloDto = creaFascicoloDto(action.getCreaFascicoloDTO());

			logger.debug("invocazione della protocollazione con xml: {}", request);
			Response response = null;

			if (capofilaFromBa01) {
				/* crea un nuovo fascicolo aggiunge la mail in entrata e protocolla con capofila recuperato da BA01 */
				response = spagicClientProtocollazioneCompleta.protocollaFascicoloNuovoFromBA01(creaFascicoloDto, request, pathPratica, numeroCapofila, annoCapofila,
						userSessionUtil.getUtenteSpagic());
			} else {
				if (annoCapofila != null && !annoCapofila.trim().equals("")) {
					/* crea un nuovo fascicolo aggiunge la mail in entrata e protocolla con capofila */
					response = spagicClientProtocollazioneCompleta.protocollaFascicoloNuovo(creaFascicoloDto, request, pathPratica, numeroCapofila, annoCapofila, userSessionUtil.getUtenteSpagic());
				} else {
					/* crea un nuovo fascicolo aggiunge la mail in entrata e protocolla senza capofila */
					response = spagicClientProtocollazioneCompleta.protocollaFascicoloNuovo(creaFascicoloDto, request, pathPratica, userSessionUtil.getUtenteSpagic());
				}
			}

			it.bologna.comune.spagic.protocollazionecompleta.Response protocollazioneResponse = SpagicClientSerializationUtil.getResponseProtocollazioneCompletaToObject(response.getResponseparam());

			String numeroProtocollo = protocollazioneResponse.getReco1().getO1NumeroProtocollo();
			String annoProtocollo = protocollazioneResponse.getReco1().getO1AnnoProtocollo();
			String numeroProtocolloCapofila = protocollazioneResponse.getReco1().getO1NumeroCapofila();
			String annoProtocolloCapofila = protocollazioneResponse.getReco1().getO1AnnoCapofila();

			if (protocollazioneResponse.getReco1().getO1CodiceMessaggio().equals("9999")) {
				res.setError(Boolean.TRUE);
				res.setMessageError(ConsolePecConstants.ERROR_MESSAGE);
			} else if (!protocollazioneResponse.getReco1().getO1CodiceMessaggio().equals("0000")) {
				res.setWarning(Boolean.TRUE);
				res.setWarningMessage(protocollazioneResponse.getReco1().getO1DescrizioneMesssaggio());
			} else {
				logger.debug("Protocollazione terminata correttamente Numero Pg/Anno Pg: {}/{}", numeroProtocollo, annoProtocollo);
				LockedPratica lockedPratica = new LockedPratica(response.getLockedPratica().getHashFascicolo(), response.getLockedPratica().getXmlfascicolo());
				Fascicolo newFascicolo = (Fascicolo) praticaSessionUtil.loadPraticaInSessione(lockedPratica);
				/*
				 * Rimozione pratiche collegate, in quanto modificate dalla protocollazione
				 */
				praticaSessionUtil.removePraticaFromEncodedPath(Base64Utils.URLencodeAlfrescoPath(pathPratica));

				FascicoloDTO dto = praticaUtil.fascicoloToDettaglio(newFascicolo);
				res.setError(false);
				res.setMessageError(null);
				res.setFascicoloDTO(dto);
				res.setNumeroPg(numeroProtocollo);
				res.setAnnoPg(annoProtocollo);
				res.setNumeroPgCapofila(numeroProtocolloCapofila);
				res.setAnnoPgCapofila(annoProtocolloCapofila);
			}

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			res.setError(true);
			res.setMessageError(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			res.setError(true);
			res.setMessageError(ConsolePecConstants.ERROR_MESSAGE);
		}

		return res;
	}

	private CreaFascicoloDto creaFascicoloDto(CreaFascicoloDTO creaFascicoloDTO) {
		CreaFascicoloDto dto = new CreaFascicoloDto();
		dto.setAssegnatario(gestioneConfigurazioni.getAnagraficaRuoloByEtichetta(creaFascicoloDTO.getAssegnatario()).getRuolo());
		dto.setIdEmailIn(creaFascicoloDTO.getClientID());
		dto.setNote(creaFascicoloDTO.getNote());
		dto.setTitolo(creaFascicoloDTO.getTitolo());
		dto.setUtente(creaFascicoloDTO.getUtente());
		dto.setTipoPratica(creaFascicoloDTO.getTipologiaFascicolo().getNomeTipologia());

		List<DatoAggiuntivo> datiAggiuntivi = creaFascicoloDTO.getDatiAggiuntivi();

		for (final DatoAggiuntivo valore : datiAggiuntivi) {
			final it.bologna.comune.alfresco.creazione.fascicolo.Request.DatoAggiuntivo datoAggiuntivo = new it.bologna.comune.alfresco.creazione.fascicolo.Request.DatoAggiuntivo();
			datoAggiuntivo.setDescrizione(valore.getDescrizione());
			datoAggiuntivo.setNome(valore.getNome());
			datoAggiuntivo.setPosizione(new BigInteger("" + valore.getPosizione()));
			datoAggiuntivo.setTipo(valore.getTipo().name());
			datoAggiuntivo.setVisibile(valore.isVisibile());

			// retrocompatibilità: funziona solo per dati aggiuntivi singoli --> riporto lo stesso comportamento
			valore.accept(new DatoAggiuntivoVisitorAdapter() {
				@Override
				public void visit(DatoAggiuntivoValoreSingolo ds) {
					datoAggiuntivo.setEditabile(ds.isEditabile());
					datoAggiuntivo.setObbligatorio(ds.isObbligatorio());
					datoAggiuntivo.setValore(ds.getValore());

				}
			});

			dto.getDatiAggiuntivi().add(datoAggiuntivo);
		}

		return dto;
	}

	private String getProtocollazioneXmlRequest(Map<String, Map<String, it.eng.portlet.consolepec.gwt.shared.dto.Element>> map) {

		Document document = DocumentHelper.createDocument();

		// root element
		Element request = document.addElement("request");

		request.addNamespace("", "http://comune.bologna.it/spagic/protocollazionecompleta");

		for (String elemento : map.keySet()) {

			// elemento di livello 1
			Map<String, it.eng.portlet.consolepec.gwt.shared.dto.Element> internalMap = map.get(elemento);

			if (internalMap.size() == 0) {
				continue;
			}

			String nomeNodo = elemento.split("#")[0];

			final Element domElementLev1 = request.addElement(nomeNodo);

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
						current.addElement(r.getName()).setText(r.getValue() == null ? "" : r.getValue());

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
