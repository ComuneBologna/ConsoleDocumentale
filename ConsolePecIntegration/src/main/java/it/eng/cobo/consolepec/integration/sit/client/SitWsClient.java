package it.eng.cobo.consolepec.integration.sit.client;

import it.eng.cobo.consolepec.integration.sit.bean.SitResponse;
import it.eng.cobo.consolepec.integration.sit.exception.SitWsClientException;

/**
 * @author GiacomoFM
 * @since 20/nov/2018
 */
public interface SitWsClient {

	SitResponse validaDescrizioneVia(String via) throws SitWsClientException;

	SitResponse validaIndirizzoCompleto(String via, String numeroCivico) throws SitWsClientException;

	SitResponse validaIndirizzoCompleto(String via, String numeroCivico, String esponente) throws SitWsClientException;

	SitResponse decodificaCodiceViaIndirizzoCompleto(String codiceVia, String numeroCivico, String esponente) throws SitWsClientException;

	SitResponse decodificaCodiceVia(String codiceVia) throws SitWsClientException;

}
