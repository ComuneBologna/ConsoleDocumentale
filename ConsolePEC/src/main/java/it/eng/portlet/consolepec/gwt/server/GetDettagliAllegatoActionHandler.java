package it.eng.portlet.consolepec.gwt.server;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.bologna.comune.alfresco.versioning.AllVersions;
import it.bologna.comune.alfresco.versioning.Version;
import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.cobo.consolepec.util.objects.Ref;
import it.eng.consolepec.spagicclient.SpagicClientVersioning;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.PraticaObserver.VersionLoad;
import it.eng.consolepec.xmlplugin.factory.Versione;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.GetDettagliAllegato;
import it.eng.portlet.consolepec.gwt.shared.action.GetDettagliAllegatoResult;
import it.eng.portlet.consolepec.gwt.shared.model.DettagliAllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class GetDettagliAllegatoActionHandler implements ActionHandler<GetDettagliAllegato, GetDettagliAllegatoResult> {

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	SpagicClientVersioning spagicClientVersioning;

	@Autowired
	VerificaFirmaInvoker verificaFirmaUtil;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	XMLPluginToDTOConverter xmlPluginToDTOConverter;

	Logger logger = LoggerFactory.getLogger(GetDettagliAllegatoActionHandler.class);

	public GetDettagliAllegatoActionHandler() {}

	@Override
	public GetDettagliAllegatoResult execute(GetDettagliAllegato action, ExecutionContext context) throws ActionException {
		GetDettagliAllegatoResult result = null;
		try {
			logger.debug("Richiesti dettagli dell'allegato: {} appartenente alla pratica {}", action.getAllegato().getNome(), Base64Utils.URLdecodeAlfrescoPath(action.getClientID()));
			Pratica<?> pratica = praticaSessionUtil.loadPraticaFromEncodedPath(action.getClientID());

			final Ref<Boolean> abilitazioneEstrazioniPrecedenti = Ref.of(Boolean.TRUE);
			PraticaDTO praticaDTO = xmlPluginToDTOConverter.praticaToDTO(pratica);
			if (praticaDTO instanceof FascicoloDTO) {
				abilitazioneEstrazioniPrecedenti.set(((FascicoloDTO) praticaDTO).isAccessoVersioniPrecedentiAllegati());
			}

			final Ref<String> versAllegato = Ref.of(null);
			Allegato all = null;
			/* cerco l'allegato del plugin */
			for (Allegato allegato : pratica.getDati().getAllegati()) {
				if (allegato.getNome().equalsIgnoreCase(action.getAllegato().getNome())) {
					all = allegato;
					versAllegato.set(all.getCurrentVersion());
					break;
				}
			}
			logger.debug("Trovato allegato: {}. Procedo alla ricerca delle versioni", all);
			// TODO servizio spagic direttamente
			pratica.loadVersioni(all, new VersionLoad() {

				@Override
				public List<Versione> onLoadVersioni(String path, String nomeFile) {
					List<Versione> versioni = new ArrayList<Versione>();

					AllVersions versions = spagicClientVersioning.getAllVersions(path, nomeFile, userSessionUtil.getUtenteSpagic());
					for (Version version : versions.getVersions()) {

						if (!abilitazioneEstrazioniPrecedenti.get()) {
							if (version.getVersion().equals(versAllegato.get())) {
								versioni.add(
										new Versione(version.getVersion(), version.getPecUser(), version.getNodeUuid(), version.getHash(), version.getVersionDate().toGregorianCalendar().getTime()));
							}

						} else {
							versioni.add(new Versione(version.getVersion(), version.getPecUser(), version.getNodeUuid(), version.getHash(), version.getVersionDate().toGregorianCalendar().getTime()));
						}
					}

					return versioni;
				}
			});
			logger.debug("Trovate {} versioni", all.getVersioni().size());
			// TODO versioni float da xmlplugin
			TreeMap<Float, Versione> sortedVersioni = new TreeMap<Float, Versione>();

			for (Versione versione : all.getVersioni()) {
				logger.debug("Trovata versione: {}", versione);
				sortedVersioni.put(Float.parseFloat(versione.getLabel()), versione);
			}
			DettagliAllegatoDTO currentDTO = null;
			DettagliAllegatoDTO headDTO = null;

			for (Float v : sortedVersioni.descendingKeySet()) {
				logger.debug("Analizzo versione:  {}", v);
				Versione versione = sortedVersioni.get(v);
				if (currentDTO == null) {
					currentDTO = new DettagliAllegatoDTO(action.getAllegato().getNome(), action.getAllegato().getFolderOriginPath(), action.getAllegato().getFolderOriginName(), action.getClientID(),
							v.toString(), versione.getVersionid(), all.getFirmatoHash());
					currentDTO.setTipologiaAllegato(action.getAllegato().getTipologiaAllegato());
					currentDTO.getDatiAggiuntivi().addAll(all.getDatiAggiuntivi());
					currentDTO.setHash(versione.getHash());
					headDTO = currentDTO;
				} else {
					DettagliAllegatoDTO prevDTO = new DettagliAllegatoDTO(action.getAllegato().getNome(), action.getAllegato().getFolderOriginPath(), action.getAllegato().getFolderOriginName(),
							action.getClientID(), v.toString(), versione.getVersionid(), all.getFirmatoHash());
					prevDTO.setTipologiaAllegato(action.getAllegato().getTipologiaAllegato());
					currentDTO.setPreviousVersion(prevDTO);
					prevDTO.setHash(versione.getHash());
					currentDTO = prevDTO;
				}
				currentDTO.setDataCreazioneLabel(DateUtils.DATEFORMAT_DATEH.format(versione.getDataVersione()));
				logger.debug("Verifico la firma");
				/* verifico la firma */
				verificaFirmaUtil.verificaFirmaAllegato(currentDTO);
				logger.debug("Trovate {} firmatari su allegato: {} uuid:{}", currentDTO.getInformazioniFirma().length, currentDTO.getUUID());
				result = new GetDettagliAllegatoResult(headDTO, null, false);
			}

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result = new GetDettagliAllegatoResult(null, e.getErrorMessage(), true);

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new GetDettagliAllegatoResult(null, ConsolePecConstants.ERROR_MESSAGE, true);
		}

		return result;
	}

	@Override
	public void undo(GetDettagliAllegato action, GetDettagliAllegatoResult result, ExecutionContext context) throws ActionException {
		//
	}

	@Override
	public Class<GetDettagliAllegato> getActionType() {
		return GetDettagliAllegato.class;
	}
}
