package it.eng.consolepec.spagicclient.remoteproxy.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.xml.bind.JAXBException;

import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.bologna.comune.spagic.taskfirma.AllegatoResult;
import it.bologna.comune.spagic.taskfirma.DestinatarioGruppoRichiestaRequest;
import it.bologna.comune.spagic.taskfirma.DestinatarioRichiestaRequest;
import it.bologna.comune.spagic.taskfirma.DestinatarioUtenteRichiestaRequest;
import it.bologna.comune.spagic.taskfirma.FileNameMap;
import it.bologna.comune.spagic.taskfirma.InformazioniFirma;
import it.bologna.comune.spagic.taskfirma.InformazioniNotifica;
import it.bologna.comune.spagic.taskfirma.InformazioniRiassegnazione;
import it.bologna.comune.spagic.taskfirma.InformazioniRichiesta;
import it.bologna.comune.spagic.taskfirma.PraticaAllegati;
import it.bologna.comune.spagic.taskfirma.PraticaIdTask;
import it.bologna.comune.spagic.taskfirma.RichiestaResponse;
import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.consolepec.spagicclient.SpagicClientTaskFirma;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.model.taskfirma.DestinatarioGruppoClient;
import it.eng.consolepec.spagicclient.model.taskfirma.DestinatarioTaskFirmaClient;
import it.eng.consolepec.spagicclient.model.taskfirma.DestinatarioUtenteTaskFirmaClient;
import it.eng.consolepec.spagicclient.model.taskfirma.OperazioneTaskFirmaClient;
import it.eng.consolepec.spagicclient.model.taskfirma.RichiestaTaskFirmaResult;
import it.eng.consolepec.spagicclient.model.taskfirma.TipoRichiestaTaskFirmaClient;
import it.eng.consolepec.spagicclient.model.taskfirma.TipoRispostaTaskFirmaClient;
import it.eng.consolepec.spagicclient.remoteproxy.abs.AbstractSpagicClientRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.abs.NewSpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientRemoteProxyUtil;
import it.eng.consolepec.spagicclient.remoteproxy.abs.SpagicClientSerializationUtil;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientErrorCode;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 *
 * @author biagiot
 *
 */
public class SpagicClientTaskFirmaRemoteProxy extends AbstractSpagicClientRemoteProxy<Request, Response> implements SpagicClientTaskFirma {

	protected SpagicClientTaskFirmaRemoteProxy(String serviceProxyUrl, String alfrescoUsername, String alfrescoPassword, String serviceUsername, String servicePassword, String restServiceUrl) {
		super(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	@Override
	public List<LockedPratica> firmaDocumenti(Map<String, List<String>> praticaAllegatiMap, String userFirma, String passFirma, String otpFirma, String tipoFirma, boolean riassegnazione,
			String assegnatario, String note, List<String> indirizziNotifica, String operatore, Utente utente, String ruolo, List<File> fileDaAllegare,
			String motivazione) throws SpagicClientException {
		InformazioniFirma infoFirma = new InformazioniFirma();
		infoFirma.setUserFirma(userFirma);
		infoFirma.setPassFirma(passFirma);
		infoFirma.setOtpFirma(otpFirma);
		infoFirma.setTipoFirma(tipoFirma);

		it.bologna.comune.spagic.taskfirma.Request request = creaRequest(OperazioneTaskFirmaClient.FIRMA, ruolo, praticaAllegatiMap, riassegnazione, null, infoFirma,
				getDatiNotifica(riassegnazione, assegnatario, operatore, indirizziNotifica, note), null, null, motivazione);

		return invocaTaskFirmaService(request, utente, fileDaAllegare);
	}

	@Override
	public List<LockedPratica> vistoDocumenti(Map<String, List<String>> praticaAllegatiMap, boolean riassegnazione, String assegnatario, String note, List<String> indirizziNotifica, String operatore,
			Utente utente, String ruolo, List<File> fileDaAllegare, String motivazione) throws SpagicClientException {

		it.bologna.comune.spagic.taskfirma.Request request = creaRequest(OperazioneTaskFirmaClient.VISTO, ruolo, praticaAllegatiMap, riassegnazione, null, null,
				getDatiNotifica(riassegnazione, assegnatario, operatore, indirizziNotifica, note), null, null, motivazione);
		return invocaTaskFirmaService(request, utente, fileDaAllegare);
	}

	@Override
	public List<LockedPratica> diniegoDocumenti(Map<String, List<String>> praticaAllegatiMap, boolean riassegnazione, String assegnatario, String note, List<String> indirizziNotifica,
			String operatore, Utente utente, String ruolo, List<File> fileDaAllegare, String motivazione) throws SpagicClientException {

		it.bologna.comune.spagic.taskfirma.Request request = creaRequest(OperazioneTaskFirmaClient.DINIEGO, ruolo, praticaAllegatiMap, riassegnazione, null, null,
				getDatiNotifica(riassegnazione, assegnatario, operatore, indirizziNotifica, note), null, null, motivazione);
		return invocaTaskFirmaService(request, utente, fileDaAllegare);
	}

	@Override
	public List<LockedPratica> ritiroDocumenti(Map<String, List<String>> praticaAllegatiMap, String note, List<String> indirizziNotifica, Utente utente,
			String motivazione) throws SpagicClientException {
		it.bologna.comune.spagic.taskfirma.Request request = creaRequest(OperazioneTaskFirmaClient.RITIRO, null, praticaAllegatiMap, false, null, null,
				getDatiNotifica(false, null, null, indirizziNotifica, note), null, null, motivazione);

		return invocaTaskFirmaService(request, utente, null);
	}

	@Override
	public List<LockedPratica> rispondiDocumenti(TipoRispostaTaskFirmaClient tipoRisposta, Map<String, List<String>> praticaAllegatiMap, boolean riassegnazione, String assegnatario, String note,
			List<String> indirizziNotifica, String operatore, Utente utente, String ruolo, List<File> fileDaAllegare, String motivazione) throws SpagicClientException {

		it.bologna.comune.spagic.taskfirma.Request request = creaRequest(OperazioneTaskFirmaClient.RISPONDI_PARERE, ruolo, praticaAllegatiMap, riassegnazione, null, null,
				getDatiNotifica(riassegnazione, assegnatario, operatore, indirizziNotifica, note), tipoRisposta, null, motivazione);
		return invocaTaskFirmaService(request, utente, fileDaAllegare);
	}

	@Override
	public List<LockedPratica> evadiDocumenti(Map<String, List<Integer>> praticaIdTaskFirmaMap, Utente utente) throws SpagicClientException {
		it.bologna.comune.spagic.taskfirma.Request request = creaRequest(OperazioneTaskFirmaClient.EVADI, null, null, false, null, null, null, null, praticaIdTaskFirmaMap, null);
		return invocaTaskFirmaService(request, utente, null);
	}

	@Override
	public RichiestaTaskFirmaResult richiediFirmaVistoDocumenti(String praticaPath, List<String> nomiAllegati, Utente utente, TipoRichiestaTaskFirmaClient tipoRichiesta, String oggetto,
			String gruppoProponente, List<DestinatarioTaskFirmaClient> destinatari, String note, List<String> indirizziNotifica, String mittenteOriginale, Date dataScadenza, Integer oraScadenza,
			Integer minutoScadenza, String motivazione) throws SpagicClientException {

		InformazioniRichiesta infoRichiesta = new InformazioniRichiesta();
		infoRichiesta.setTipoRichiesta(tipoRichiesta.name());
		infoRichiesta.setOggettoDocumento(oggetto);
		infoRichiesta.setGruppoProponente(gruppoProponente);
		List<DestinatarioRichiestaRequest> destinatariRequest = infoRichiesta.getDestinatariRichiesta();
		for (DestinatarioTaskFirmaClient destinatario : destinatari) {
			destinatariRequest.add(convertDestinatarioToRequest(destinatario));
		}

		infoRichiesta.setMittenteOriginale(mittenteOriginale);

		if (dataScadenza != null) {
			infoRichiesta.setDataScadenza(DateUtils.dateToXMLGrCal(dataScadenza));

			if (oraScadenza != null && minutoScadenza != null) {
				infoRichiesta.setOraScadenza(oraScadenza);
				infoRichiesta.setMinutoScadenza(minutoScadenza);
			}
		}

		InformazioniNotifica infoNotifica = new InformazioniNotifica();
		infoNotifica.setNote(note);
		infoNotifica.getIndirizziNotifica().addAll(indirizziNotifica);

		Map<String, List<String>> praticaAllegatiMap = new HashMap<String, List<String>>();
		praticaAllegatiMap.put(praticaPath, nomiAllegati);

		it.bologna.comune.spagic.taskfirma.Request request = creaRequest(OperazioneTaskFirmaClient.RICHIESTA, null, praticaAllegatiMap, false, infoRichiesta, null,
				getDatiNotifica(false, null, null, indirizziNotifica, note), null, null, motivazione);
		return invocaRichiestaTaskFirmaService(request, utente);
	}

	private static DestinatarioRichiestaRequest convertDestinatarioToRequest(DestinatarioTaskFirmaClient destinatario) {
		if (destinatario instanceof DestinatarioUtenteTaskFirmaClient) {
			DestinatarioUtenteTaskFirmaClient destinatarioUtente = (DestinatarioUtenteTaskFirmaClient) destinatario;
			DestinatarioUtenteRichiestaRequest dest = new DestinatarioUtenteRichiestaRequest();
			dest.setUserName(destinatarioUtente.getUsername());
			dest.setNome(destinatarioUtente.getNome());
			dest.setCognome(destinatarioUtente.getCognome());
			dest.setMatricola(destinatarioUtente.getMatricola());
			dest.setSettore(destinatarioUtente.getSettore());
			return dest;

		} else if (destinatario instanceof DestinatarioGruppoClient) {
			DestinatarioGruppoClient destinatarioGruppo = (DestinatarioGruppoClient) destinatario;
			DestinatarioGruppoRichiestaRequest dest = new DestinatarioGruppoRichiestaRequest();
			dest.setNomeGruppo(destinatarioGruppo.getNomeGruppo());
			return dest;
		} else throw new SpagicClientException(SpagicClientErrorCode.EINVALIDARGUMENT, "Tipo destinatario non valido");
	}

	private static it.bologna.comune.spagic.taskfirma.Request creaRequest(OperazioneTaskFirmaClient operazioneTaskFirma, String ruolo, Map<String, List<String>> praticaAllegatiMap,
			boolean riassegnazione, InformazioniRichiesta infoRichiesta, InformazioniFirma infoFirma, InformazioniNotifica infoNotifica, TipoRispostaTaskFirmaClient tipoRisposta,
			Map<String, List<Integer>> praticaIdTaskFirmaMap, String motivazione) {

		it.bologna.comune.spagic.taskfirma.Request taskFirmaRequest = new it.bologna.comune.spagic.taskfirma.Request();
		taskFirmaRequest.setOperazioneTaskFirma(operazioneTaskFirma.name());

		if (praticaAllegatiMap != null) {
			List<PraticaAllegati> mapAllegatiPath = taskFirmaRequest.getPraticaAllegatiMap();
			for (Entry<String, List<String>> entry : praticaAllegatiMap.entrySet()) {
				PraticaAllegati tempMap = new PraticaAllegati();
				tempMap.setPathPratica(entry.getKey());
				tempMap.getFiles().addAll(entry.getValue());
				mapAllegatiPath.add(tempMap);
			}
		}

		switch (operazioneTaskFirma) {
		case FIRMA:

			if (infoFirma == null) throw new SpagicClientException(SpagicClientErrorCode.EINVALIDARGUMENT, "Mancano le informazioni per la firma digitale");

			taskFirmaRequest.setInformazioniFirma(infoFirma);
			break;

		case RICHIESTA:

			if (infoRichiesta == null) throw new SpagicClientException(SpagicClientErrorCode.EINVALIDARGUMENT, "Mancano le informazioni per la richiesta di approvazione");

			taskFirmaRequest.setInformazioniRichiesta(infoRichiesta);
			break;

		case RISPONDI_PARERE:

			if (tipoRisposta == null) throw new SpagicClientException(SpagicClientErrorCode.EINVALIDARGUMENT, "Manca il tipo di risposta");

			taskFirmaRequest.setTipoRisposta(tipoRisposta.name());
			break;

		case EVADI:
			if (praticaIdTaskFirmaMap == null) throw new SpagicClientException(SpagicClientErrorCode.EINVALIDARGUMENT, "Mancano gli id dei task");

			List<PraticaIdTask> listRequest = taskFirmaRequest.getPraticaIdTaskMap();
			for (Entry<String, List<Integer>> entry : praticaIdTaskFirmaMap.entrySet()) {
				PraticaIdTask tempMap = new PraticaIdTask();
				tempMap.setPathPratica(entry.getKey());
				tempMap.getIdTask().addAll(entry.getValue());
				listRequest.add(tempMap);
			}

		default:
			break;
		}

		taskFirmaRequest.setRiassegnazione(riassegnazione);
		taskFirmaRequest.setInformazioniNotifica(infoNotifica);
		taskFirmaRequest.setRuolo(ruolo);
		taskFirmaRequest.setMotivazione(motivazione);

		return taskFirmaRequest;
	}

	private RichiestaTaskFirmaResult invocaRichiestaTaskFirmaService(it.bologna.comune.spagic.taskfirma.Request taskFirmaRequest, Utente utente) {
		Request genericRequest = new Request();
		genericRequest.setAlfrescopassword(getAlfrescoPassword());
		genericRequest.setAlfrescousername(getAlfrescoUsername());
		genericRequest.setPathapplicationxml("");
		genericRequest.setServicename(ServiceNamesUtil.TASK_FIRMA_SERVICE);
		genericRequest.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		Response response = null;
		genericRequest.setRequestparam(NewSpagicClientSerializationUtil.serialize(taskFirmaRequest));
		response = invokeSpagicService(genericRequest);

		if (response.getError() != null) throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());

		String responseParam = response.getResponseparam();
		it.bologna.comune.spagic.taskfirma.Response taskFirmaResponse = NewSpagicClientSerializationUtil.deserialize(responseParam, it.bologna.comune.spagic.taskfirma.Response.class);

		RichiestaTaskFirmaResult result = new RichiestaTaskFirmaResult();

		RichiestaResponse richiestaResult = taskFirmaResponse.getRichiestaResponse();
		result.setLockedPratica(new LockedPratica(richiestaResult.getLockedPratica().getHashPratica(), richiestaResult.getLockedPratica().getXmlpratica()));
		for (AllegatoResult ar : richiestaResult.getAllegatiResult()) {
			result.getAllegatiIdTaskFirmaMap().put(ar.getNomeAllegato(), ar.getIdTask());
		}

		return result;
	}

	private List<LockedPratica> invocaTaskFirmaService(it.bologna.comune.spagic.taskfirma.Request taskFirmaRequest, Utente utente, List<File> files) {
		Request genericRequest = new Request();
		genericRequest.setAlfrescopassword(getAlfrescoPassword());
		genericRequest.setAlfrescousername(getAlfrescoUsername());
		genericRequest.setPathapplicationxml("");
		genericRequest.setServicename(ServiceNamesUtil.TASK_FIRMA_SERVICE);
		genericRequest.setUtente(SpagicClientRemoteProxyUtil.convert(utente));

		Response response = null;

		if (files != null && !files.isEmpty()) {

			Map<String, DataSource> streams = new HashMap<>();
			int i = 0;

			for (File file : files) {
				if (file != null) {
					for (PraticaAllegati pa : taskFirmaRequest.getPraticaAllegatiMap()) {
						FileDataSource fileDataSource = new FileDataSource(file);
						streams.put("f" + i, fileDataSource);

						FileNameMap fileNameMap = new FileNameMap();
						fileNameMap.setFileName(file.getName());
						fileNameMap.setIdFile("f" + i);
						fileNameMap.setPathPratica(pa.getPathPratica());

						taskFirmaRequest.getFileNameMap().add(fileNameMap);
					}
				}

				i++;
			}

			genericRequest.setRequestparam(NewSpagicClientSerializationUtil.serialize(taskFirmaRequest));
			response = invokeSpagicServiceWhitAttachementRequest(genericRequest, "application/octet-stream", streams);

		} else {
			genericRequest.setRequestparam(NewSpagicClientSerializationUtil.serialize(taskFirmaRequest));
			response = invokeSpagicService(genericRequest);
		}

		if (response.getError() != null) throw SpagicClientRemoteProxyUtil.processErrorResponse(response.getError());

		String responseParam = response.getResponseparam();
		it.bologna.comune.spagic.taskfirma.Response taskFirmaResponse = NewSpagicClientSerializationUtil.deserialize(responseParam, it.bologna.comune.spagic.taskfirma.Response.class);
		List<it.bologna.comune.base.LockedPratica> lockedPraticheResponse = taskFirmaResponse.getLockedPratiche();
		List<LockedPratica> result = new ArrayList<LockedPratica>();

		for (it.bologna.comune.base.LockedPratica lockedPratica : lockedPraticheResponse) {
			result.add(new LockedPratica(lockedPratica.getHashPratica(), lockedPratica.getXmlpratica()));
		}

		return result;
	}

	private static InformazioniNotifica getDatiNotifica(boolean assegnazione, String assegnatario, String operatore, List<String> indirizziNotifica, String note) {

		if (assegnazione) {
			InformazioniRiassegnazione infoRiassegnazione = new InformazioniRiassegnazione();
			infoRiassegnazione.setNote(note);
			infoRiassegnazione.getIndirizziNotifica().addAll(indirizziNotifica);
			infoRiassegnazione.setAssegnatario(assegnatario);
			infoRiassegnazione.setOperatore(operatore);
			return infoRiassegnazione;

		}

		InformazioniNotifica infoNotifica = new InformazioniNotifica();
		infoNotifica.setNote(note);
		infoNotifica.getIndirizziNotifica().addAll(indirizziNotifica);
		return infoNotifica;

	}

	@Override
	protected String getJaxbRequestToXml(Request request) throws JAXBException {
		return SpagicClientSerializationUtil.getRequestToString(request);
	}

	@Override
	protected Response getXmlResponseToJaxb(String response) throws JAXBException {
		return SpagicClientSerializationUtil.getResponseXmlToObject(response);
	}

	@Override
	protected String getSpagicServiceId() {
		return ServiceNamesUtil.CONSOLE_PEC_DISPATCHER;
	}
}
