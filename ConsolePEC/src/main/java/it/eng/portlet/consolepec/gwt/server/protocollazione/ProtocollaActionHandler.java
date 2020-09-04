package it.eng.portlet.consolepec.gwt.server.protocollazione;

import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.bologna.comune.alfresco.generic.service.Response;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaEmailOut;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.consolepec.spagicclient.SpagicClientCreateFascicolo;
import it.eng.consolepec.spagicclient.SpagicClientProtocollazioneCompleta;
import it.eng.consolepec.spagicclient.bean.request.fascicolo.FascicoloRequest;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmail;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.GestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.ProtocollaAction;
import it.eng.portlet.consolepec.gwt.shared.action.ProtocollaResult;
import it.eng.portlet.consolepec.gwt.shared.dto.Group;
import it.eng.portlet.consolepec.gwt.shared.dto.Row;
import it.eng.portlet.consolepec.gwt.shared.dto.VistorElement;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.converters.requestfascicolo.RequestFascicoloConverter;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.GestioneTaskPratiche;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class ProtocollaActionHandler implements ActionHandler<ProtocollaAction, ProtocollaResult> {

	@Autowired
	XMLPluginToDTOConverter praticaUtil;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	SpagicClientProtocollazioneCompleta spagicClientProtocollazioneCompleta;
	@Autowired
	SpagicClientCreateFascicolo spagicClientCreateFascicolo;
	@Autowired
	GestioneTaskPratiche gestioneTask;
	@Autowired
	RequestFascicoloConverter requestFascicoloConverter;
	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;
	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;

	private final Logger logger = LoggerFactory.getLogger(ProtocollaActionHandler.class);

	public ProtocollaActionHandler() {

	}

	@Override
	public ProtocollaResult execute(ProtocollaAction action, ExecutionContext context) throws ActionException {

		ProtocollaResult res = new ProtocollaResult();
		res.setError(Boolean.FALSE);
		res.setWarning(Boolean.FALSE);

		try {
			Pratica<?> pratica = praticaSessionUtil.loadPraticaFromEncodedPath(action.getIdFascicolo());

			// parte protocollazione fascicolo
			logger.debug("Recupero della pratica con id: {}", action.getIdFascicolo());
			/* protocollo dall'interno del dettaglio fascicolo */
			if (!action.getDatiPerProtocollazione().isFascicoloNuovo()) {

				String fascicoloPath = Base64Utils.URLdecodeAlfrescoPath(action.getIdFascicolo());

				String[] pathPratiche = decodifica(action.getDatiPerProtocollazione().getPratiche());
				String[] allegati = action.getDatiPerProtocollazione().getAllegati() != null ? action.getDatiPerProtocollazione().getAllegati().toArray(new String[0]) : null;
				String annoCapofila = action.getDatiPerProtocollazione().getDatiPg().getAnnoPg();
				String numeroCapofila = action.getDatiPerProtocollazione().getDatiPg().getNumeroPg();
				boolean capofilaFromBa01 = action.getDatiPerProtocollazione().getDatiPg().isCapofilaFromBa01();
				protocollaFascicolo(action, pathPratiche, allegati, fascicoloPath, annoCapofila, numeroCapofila, res, capofilaFromBa01);

				for (String pathPraticaCollegata : pathPratiche) {
					praticaSessionUtil.removePraticaFromEncodedPath(Base64Utils.URLencodeAlfrescoPath(pathPraticaCollegata));
				}
				praticaSessionUtil.removePraticaFromEncodedPath(Base64Utils.URLencodeAlfrescoPath(fascicoloPath));

			}
			/* protocollo dalla mail in creando contestualmente il fascicolo */
			else {
				// GestionePECTask gestionePECTaskCorrente = (GestionePECTask) praticaUtil.estraiTaskCorrente(pratica);
				GestionePECTask gestionePECTaskCorrente = gestioneTask.estraiTaskCorrente(pratica, GestionePECTask.class, gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());

				boolean protocollabile = true;
				if (PraticaUtil.isIngresso(pratica.getDati().getTipo().getNomeTipologia())) {
					PraticaEmail p = (PraticaEmail) pratica;
					String destinatario = null;
					if (TipologiaPratica.EMAIL_IN.getNomeTipologia().equals(p.getDati().getTipo().getNomeTipologia())) {
						if (p.getDati().getDestinatariInoltro() != null && !p.getDati().getDestinatariInoltro().isEmpty()) {
							for (String dest : p.getDati().getDestinatariInoltro()) {
								if (gestioneConfigurazioni.getAnagraficaIngresso(TipologiaPratica.EMAIL_IN.getNomeTipologia(), dest) != null) {
									destinatario = dest;
								}
							}

						} else if (p.getDati().getDestinatarioPrincipale() != null && p.getDati().getDestinatarioPrincipale().getDestinatario() != null) {
							if (gestioneConfigurazioni.getAnagraficaIngresso(TipologiaPratica.EMAIL_IN.getNomeTipologia(), p.getDati().getDestinatarioPrincipale().getDestinatario()) != null) {
								destinatario = p.getDati().getDestinatarioPrincipale().getDestinatario();
							}

						} else if (p.getDati().getDestinatari() != null && !p.getDati().getDestinatari().isEmpty()) {
							for (Destinatario dest : p.getDati().getDestinatari()) {
								if (gestioneConfigurazioni.getAnagraficaIngresso(TipologiaPratica.EMAIL_IN.getNomeTipologia(), dest.getDestinatario()) != null) {
									destinatario = dest.getDestinatario();
								}
							}

						}
					} else if (TipologiaPratica.EMAIL_EPROTOCOLLO.getNomeTipologia().equals(p.getDati().getTipo().getNomeTipologia())) {
						for (String dest : p.getDati().getDestinatariInoltro()) {
							if (gestioneConfigurazioni.getAnagraficaIngresso(TipologiaPratica.EMAIL_EPROTOCOLLO.getNomeTipologia(), dest) != null) {
								destinatario = dest;
							}
						}
					}

					AnagraficaIngresso ai = gestioneConfigurazioni.getAnagraficaIngresso(destinatario, p.getDati().getTipo().getNomeTipologia());
					protocollabile = ai != null ? ai.isProtocollabile() : true;

				} else if (PraticaUtil.isEmailOut(pratica.getDati().getTipo().getNomeTipologia())) {
					PraticaEmailOut p = (PraticaEmailOut) pratica;
					AnagraficaEmailOut aeo = gestioneConfigurazioni.getAnagraficaMailInUscita(p.getDati().getTipo().getNomeTipologia(), p.getDati().getMittente());
					protocollabile = aeo != null ? aeo.isProtocollabile() : true;

				}

				if (pratica.isProtocollaAbilitato() && protocollabile && gestionePECTaskCorrente.controllaAbilitazione(TipoApiTaskPEC.CREA_FASCICOLO)) {

					String request = getProtocollazioneXmlRequest(action.getValueMap());
					logger.debug("invocazione della protocollazione con xml: {}", request);

					// TODO riservato e assegnatario
					/* Creo il fascicolo */
					FascicoloRequest fascicoloRequest = requestFascicoloConverter.convertToFascicoloRequest(action, pratica.getAlfrescoPath());
					LockedPratica newFascicoloLocked = spagicClientCreateFascicolo.createFascicolo(fascicoloRequest, userSessionUtil.getUtenteSpagic());
					Fascicolo newFascicolo = (Fascicolo) praticaSessionUtil.loadPraticaInSessione(newFascicoloLocked);

					/* protocollo */
					String[] pathPratiche = new String[] { Base64Utils.URLdecodeAlfrescoPath(action.getIdFascicolo()) };
					protocollaFascicolo(action, pathPratiche, null, newFascicolo.getAlfrescoPath(), null, null, res, false);

					for (String pathPraticaCollegata : pathPratiche) {
						praticaSessionUtil.removePraticaFromEncodedPath(Base64Utils.URLencodeAlfrescoPath(pathPraticaCollegata));
					}
				} else {
					logger.error("Protocollazione, con creazione contestuale del fascicolo non abilitata sulla mail {} per utente {} ", pratica.getDati().getIdDocumentale(),
							gestioneProfilazioneUtente.getDatiUtente().getUsername());
					logger.warn("Protocollazione non abilitata.");
					res.setWarning(Boolean.TRUE);
					res.setWarningMessage("Protocollazione non abilitata.");
				}
			}

		} catch (Exception e) {
			logger.error("Errore in fase di protocollazione", e);
			res.setError(Boolean.TRUE);
			res.setMessageError(e.getMessage());
		}
		return res;
	}

	private static String[] decodifica(Set<String> ids) {
		String[] decIds = new String[ids.size()];
		int i = 0;
		for (String id : ids) {
			decIds[i] = Base64Utils.URLdecodeAlfrescoPath(id);
			i++;
		}
		return decIds;
	}

	private void protocollaFascicolo(ProtocollaAction action, String[] pathPratiche, String[] allegati, String fascicoloPath, String annoCapofila, String numeroCapofila, ProtocollaResult res,
			boolean capofilaFromBa01) {

		try {
			String request = getProtocollazioneXmlRequest(action.getValueMap());

			logger.debug("invocazione della protocollazione con xml: {}", request);
			Response response = null;
			if (!Strings.isNullOrEmpty(annoCapofila)) {
				if (capofilaFromBa01) {
					response = spagicClientProtocollazioneCompleta.protocollaFromBA01(pathPratiche, allegati, fascicoloPath, request, numeroCapofila, annoCapofila, userSessionUtil.getUtenteSpagic());
				} else {
					response = spagicClientProtocollazioneCompleta.protocolla(pathPratiche, allegati, fascicoloPath, request, numeroCapofila, annoCapofila, userSessionUtil.getUtenteSpagic());
				}
			} else {
				response = spagicClientProtocollazioneCompleta.protocolla(pathPratiche, allegati, fascicoloPath, request, userSessionUtil.getUtenteSpagic());
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
				for (String pathPraticaCollegata : pathPratiche) {
					praticaSessionUtil.removePraticaFromEncodedPath(Base64Utils.URLencodeAlfrescoPath(pathPraticaCollegata));
				}
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

			final Element domElementLev1 = request.addElement(elemento.split("#")[0]);

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

	@Override
	public void undo(ProtocollaAction action, ProtocollaResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<ProtocollaAction> getActionType() {
		return ProtocollaAction.class;
	}
}
