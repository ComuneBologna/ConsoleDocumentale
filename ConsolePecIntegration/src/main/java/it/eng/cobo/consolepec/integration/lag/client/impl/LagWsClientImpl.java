package it.eng.cobo.consolepec.integration.lag.client.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.cobo.consolepec.commons.exception.NotFoundException;
import it.eng.cobo.consolepec.integration.SOAPLoggingHandler;
import it.eng.cobo.consolepec.integration.lag.LagProperties;
import it.eng.cobo.consolepec.integration.lag.bean.AnagrafeDto;
import it.eng.cobo.consolepec.integration.lag.bean.PersonaFisicaDto;
import it.eng.cobo.consolepec.integration.lag.client.LagWsClient;
import it.eng.cobo.consolepec.integration.lag.generated.LagService;
import it.eng.cobo.consolepec.integration.lag.generated.LagServicePortType;
import it.eng.cobo.consolepec.integration.lag.generated.ServizioResult;
import it.eng.cobo.consolepec.integration.sit.bean.Indirizzo;
import it.eng.cobo.consolepec.integration.sit.bean.SitResponse;
import it.eng.cobo.consolepec.integration.sit.client.SitWsClient;
import it.eng.cobo.consolepec.integration.sit.exception.SitWsClientException;
import it.eng.cobo.consolepec.integration.sit.generated.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LagWsClientImpl implements LagWsClient {

	@SuppressWarnings("serial")
	public final static Map<String, String> CODICI_ERRORE = new HashMap<String, String>() {
		{
			put("01", "Errore ente in XWE-ENTE");
			put("02", "Cognome e nome formalmente invalido");
			put("03", "Matricola invalida");
			put("04", "Cognome nome invalido");
			put("06", "Sesso invalido");
			put("08", "Errore data di nascita");
			put("09", "Comune nascita invalido");
			put("10", "Estratte più di 100 persone");
			put("13", "Comune di residenza invalido");
			put("14", "Indirizzo residenza vuoto");
			put("20", "Numero di famiglia invalido");
			put("21", "Servizio non trovato");
			put("22", "Operatore non trovato in tabella");
			put("23", "Operatore non autorizzato alla funzione");
			put("24", "Errore sul servizio dell’operatore");
			put("80", "Funzione non supportata in XWE-OP");
			put("88", "Persona probabilmente già inserita (stesso codice fonetico e stessa data di nascita) ");
			put("90", "Persona non trovata");
			put("91", "Errore di sistema");
			put("92", "Errore di sistema");
			put("93", "Errore di sistema");
			put("94", "Errore di sistema");
			put("95", "Errore di sistema");
			put("96", "Errore di sistema");
			put("97", "Errore di sistema");
			put("98", "Errore di sistema");
			put("99", "Errore di sistema");

			// calcolo codice fiscale
			put("X0", "Matricola inesistente");
			put("X1", "La matricola non corrisponde a un NON RESIDENTE");
			put("X2", "Data di nascita mancante");
			put("X3", "Comune di nascita mancante");
			put("X4", "La persona ha già un codice fiscale assegnato");
			put("X5", "Il codice fiscale calcolato è già presente sul DB (matricola doppia o omocodice)");
			put("XV", "Dati formalmente errati");
			put("XZ", "Dati formalmente errati");

			// modifica codice fiscale
			put("Y0", "Matricola inesistente");
			put("Y1", "La matricola non corrisponde a un NON RESIDENTE");
			put("Y2", "Codice fiscale non valido");

			// modifica residenza
			put("Z0", "Matricola inesistente");
			put("Z1", "La matricola non corrisponde a un NON RESIDENTE");
			put("Z2", "Parametri mancanti");
			put("Z3", "Codice comune mancante o errato");
			put("Z4", "Parametri indirizzo domicilio mancanti o errati");
			put("Z5", "Non è possibile modificare l'indirizzo di residenza o di domicilio due volte nello stesso giorno.");
			put("Z6", "Non è stata trovata alcuna via con il nome specificato.");
			put("Z7", "Sono stati trovati più indirizzi corrispondente al nome indicato. Indicare un nome più dettagliato inserendo via, piazza, ecc.");
		}
	};

	private final static int POTCX1SM = 189; // servizio per ricerche
	private final static String F1_RICERCA_CF = "1";

	private int maxResult;
	private int timeout;
	private boolean residenti;
	private boolean residentiTemporanei;
	private boolean anagrafeAllargata;
	private boolean deceduti;

	private String url;
	private String ente;
	private String servizio;
	private String username;
	private String password;
	private String operatore;

	private SitWsClient sitWsClient;

	public LagWsClientImpl(SitWsClient sitWsClient, LagProperties properties) {
		this.sitWsClient = sitWsClient;

		this.url = properties.getProperty("url");
		this.ente = properties.getProperty("ente");
		this.servizio = properties.getProperty("servizio");
		this.username = properties.getProperty("username");
		this.password = properties.getProperty("password");
		this.operatore = properties.getProperty("operatore");

		this.timeout = Integer.valueOf(properties.getProperty("timeout")).intValue();
		this.maxResult = Integer.valueOf(properties.getProperty("maxResult")).intValue();
		this.residenti = Boolean.valueOf(properties.getProperty("anagrafe.residenti"));
		this.residentiTemporanei = Boolean.valueOf(properties.getProperty("anagrafe.residentiTemporanei"));
		this.anagrafeAllargata = Boolean.valueOf(properties.getProperty("anagrafe.anagrafeAllargata"));
		this.deceduti = Boolean.valueOf(properties.getProperty("anagrafe.deceduti"));
	}

	@Override
	public PersonaFisicaDto dettaglioPersonaFisicaByCodiceFiscale(String codiceFiscale) throws InvalidArgumentException, ApplicationException, NotFoundException {
		log.info("Inizio ricerca persona fisica sul lag tramite codice fiscale: {}", codiceFiscale);
		if (codiceFiscale == null || "".equals(codiceFiscale)) {
			throw new InvalidArgumentException("Valorizzare il codice fiscale", true);
		}

		LagService lagService = new LagService();
		LagServicePortType lagServiceHttpSoap11Endpoint = lagService.getLagServiceHttpSoap11Endpoint();

		BindingProvider bp = (BindingProvider) lagServiceHttpSoap11Endpoint;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
		bp.getRequestContext().put("com.sun.xml.internal.ws.connect.timeout", timeout);
		bp.getRequestContext().put("com.sun.xml.internal.ws.request.timeout", timeout);
		bp.getRequestContext().put("com.sun.xml.ws.request.timeout", timeout);
		bp.getRequestContext().put("com.sun.xml.ws.connect.timeout", timeout);
		bp.getRequestContext().put("javax.xml.ws.client.connectionTimeout", timeout);
		bp.getRequestContext().put("javax.xml.ws.client.receiveTimeout", timeout);

		@SuppressWarnings("rawtypes") List<Handler> handlerChain = bp.getBinding().getHandlerChain();
		handlerChain.add(new SOAPLoggingHandler());
		bp.getBinding().setHandlerChain(handlerChain);

		AnagrafeDto anagrafeDto = new AnagrafeDto();
		anagrafeDto.setField(AnagrafeDto.XWC_OP, F1_RICERCA_CF);
		anagrafeDto.setField(AnagrafeDto.XWC_COD_FISCALE, codiceFiscale.toUpperCase());

		anagrafeDto.setField(AnagrafeDto.XWC_ENTE, ente);
		anagrafeDto.setField(AnagrafeDto.XWC_SERVIZIO, servizio);
		anagrafeDto.setField(AnagrafeDto.XWC_OPERATORE, operatore);

		return dettaglioPersonaFisicaInternal(lagServiceHttpSoap11Endpoint.invocaServizio2(POTCX1SM, anagrafeDto.toString(), username, password), anagrafeDto);
	}

	private PersonaFisicaDto dettaglioPersonaFisicaInternal(ServizioResult servizioResult, AnagrafeDto anagrafeDto) throws ApplicationException, NotFoundException {
		if (servizioResult == null || servizioResult.getDatiRisultato() == null) {
			throw new ApplicationException("Errore durante l'invocazione del servizio LAG", false);
		}

		// Ciclo su tutte le risposte, per eliminare quelle non pertinenti (es. Anagrafe Allargata, deceduti, ecc...
		List<PersonaFisicaDto> elencoPersoneFisiche = new ArrayList<>();
		Iterator<String> iterator = servizioResult.getDatiRisultato().iterator();
		while (iterator.hasNext()) {
			String recordAnagrafica = iterator.next();
			anagrafeDto.setRecord(recordAnagrafica);

			if (CODICI_ERRORE.containsKey(anagrafeDto.getField(AnagrafeDto.XWC_RC))) {
				log.error(CODICI_ERRORE.get(anagrafeDto.getField(AnagrafeDto.XWC_RC)));

				if (anagrafeDto.getField(AnagrafeDto.XWC_RC).equals("90")) {
					throw new NotFoundException(CODICI_ERRORE.get(anagrafeDto.getField(AnagrafeDto.XWC_RC)), true);
				}

				throw new ApplicationException(CODICI_ERRORE.get(anagrafeDto.getField(AnagrafeDto.XWC_RC)), true);
			}

			String codProvenienza = anagrafeDto.getField(AnagrafeDto.XWC_COD_PROVENIENZA);
			String dataDecesso = anagrafeDto.getField(AnagrafeDto.XWC_DATA_MORTE);

			if (AnagrafeDto.COD_PROVENIENZA_AR.equals(codProvenienza) && !residenti) {
				continue;
			}
			if (AnagrafeDto.COD_PROVENIENZA_AT.equals(codProvenienza) && !residentiTemporanei) {
				continue;
			}
			if (AnagrafeDto.COD_PROVENIENZA_AA.equals(codProvenienza) && !anagrafeAllargata) {
				continue;
			}
			// Se la data decesso è valorizzata (!= null) e non si vogliono i deceduti
			if ((dataDecesso != null && !"".equals(dataDecesso)) && !deceduti) {
				continue;
			}

			// Imposto i dati base da LAG
			PersonaFisicaDto personaFisicaDto;
			try {
				personaFisicaDto = PersonaFisicaDto.Factory.newInstance(anagrafeDto);
			} catch (Exception e) {
				throw new ApplicationException(e);
			}

			decodificaResidenza(personaFisicaDto);
			elencoPersoneFisiche.add(personaFisicaDto);
		}

		if (elencoPersoneFisiche.size() > maxResult) {
			log.error("Troppe corrispondenze trovate");
			throw new ApplicationException("Troppe corrispondenze trovate", false);
		}
		if (elencoPersoneFisiche.size() < 1) {
			log.error("Nessuna corrispondenza trovata");
			throw new NotFoundException("Nessuna corrispondenza trovata", true);
		}
		return elencoPersoneFisiche.get(0);
	}

	private void decodificaResidenza(PersonaFisicaDto personaFisicaDto) throws ApplicationException {
		String numCivKeyResidenza = personaFisicaDto.getNumCivKeyResidenza();
		if (numCivKeyResidenza == null || numCivKeyResidenza.isEmpty()) {
			throw new ApplicationException("Nessuna residenza trovata", true);
		}

		String codiceVia = numCivKeyResidenza.substring(0, 5);
		String numeroCivico = numCivKeyResidenza.substring(5, 9);
		String esponente = caricaEsponente(numCivKeyResidenza.substring(9).trim());

		SitResponse sitResponse;
		try {
			sitResponse = sitWsClient.decodificaCodiceViaIndirizzoCompleto(codiceVia, numeroCivico, esponente);
		} catch (SitWsClientException e) {
			throw new ApplicationException("Errore durante l'invocazione del SIT", false);
		}
		Response response = sitResponse.getSitResponse();
		if (Boolean.parseBoolean(response.getHeader().getIsError().getValueError())) {
			throw new ApplicationException("Errore nella decodifica della via", false);
		}

		Indirizzo residenza = sitResponse.getIndirizzo();
		personaFisicaDto.setVia(residenza.getVia());
		personaFisicaDto.setCivico(Long.parseLong(residenza.getCivico()));
		personaFisicaDto.setEsponente(residenza.getEsponente());
		personaFisicaDto.setCap(residenza.getCap());

		// personaFisicaDto.setDescComuneResidenza("BOLOGNA");
		personaFisicaDto.setDescStatoResidenza("ITALIA");
	}

	private static String caricaEsponente(String possibileEsponente) {
		if (possibileEsponente == null || possibileEsponente.isEmpty()) {
			return possibileEsponente;
		}
		if (possibileEsponente.startsWith("0")) {
			possibileEsponente = possibileEsponente.replaceFirst("0", "");
		}
		try {
			return Integer.toString(Integer.parseInt(possibileEsponente));
		} catch (Exception e) {
			log.debug("L'esponente [" + possibileEsponente + "] non e' numerico", e);
		}
		return possibileEsponente;
	}

}
