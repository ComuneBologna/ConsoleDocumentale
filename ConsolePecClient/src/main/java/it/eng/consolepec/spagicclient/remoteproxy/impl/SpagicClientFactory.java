package it.eng.consolepec.spagicclient.remoteproxy.impl;

import it.eng.consolepec.client.ArchiviaPraticaClient;
import it.eng.consolepec.client.CambiaVisibilitaAllegatoClient;
import it.eng.consolepec.client.CambiaVisibilitaFascicoloClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.CollegamentoPraticheClient;
import it.eng.consolepec.client.DownloadAllegatoClient;
import it.eng.consolepec.client.GestioneAllegatoClient;
import it.eng.consolepec.client.GestioneEmailOutClient;
import it.eng.consolepec.client.GestioneProcedimentiClient;
import it.eng.consolepec.client.ModificaNoteClient;
import it.eng.consolepec.client.OperativitaRidottaClient;
import it.eng.consolepec.client.PresaInCaricoClient;
import it.eng.consolepec.client.RestClientInvoker;
import it.eng.consolepec.client.RiassegnaPraticaClient;
import it.eng.consolepec.client.RicercaPraticheClient;
import it.eng.consolepec.client.StepIterClient;
import it.eng.consolepec.client.TaskFirmaClient;
import it.eng.consolepec.client.impl.ArchiviaPraticaClientImpl;
import it.eng.consolepec.client.impl.CambiaVisibilitaAllegatoClientImpl;
import it.eng.consolepec.client.impl.CambiaVisibilitaFascicoloClientImpl;
import it.eng.consolepec.client.impl.CollegamentoPraticheClientImpl;
import it.eng.consolepec.client.impl.DownloadAllegatoClientImpl;
import it.eng.consolepec.client.impl.GestioneAllegatoClientImpl;
import it.eng.consolepec.client.impl.GestioneEmailOutClientImpl;
import it.eng.consolepec.client.impl.GestioneProcedimentiClientImpl;
import it.eng.consolepec.client.impl.ModificaNoteClientImpl;
import it.eng.consolepec.client.impl.OperativitaRidottaClientImpl;
import it.eng.consolepec.client.impl.PresaInCaricoClientImpl;
import it.eng.consolepec.client.impl.RiassegnaPraticaClientImpl;
import it.eng.consolepec.client.impl.RicercaPraticheClientImpl;
import it.eng.consolepec.client.impl.StepIterClientImpl;
import it.eng.consolepec.client.impl.TaskFirmaClientImpl;
import it.eng.consolepec.spagicclient.*;
import lombok.AllArgsConstructor;

/**
 * La factory si occupa di istanziare i vari componenti dello Spagic Client.
 *
 * @author Roberto Santi
 *
 */
@AllArgsConstructor
public class SpagicClientFactory {

	private String serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl;

	/**
	 *
	 * Metodo per la creazione del componente responsabile della creazione di un fascicolo
	 *
	 */
	public SpagicClientCreateFascicolo getSpagicClientCreateFascicolo() {
		return new SpagicClientCreateFascicoloRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	/**
	 * Metodo per la creazione del componente responsabile del recupero dei dettagli necessari per la protocollazione
	 */
	public SpagicClientDettaglioPraticaProtocollozione getSpagicClientDettaglioPraticaProtocollozione() {
		return new SpagicClientDettaglioPraticaProtocollozioneRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	/**
	 *
	 * Metodo per la creazione del componente responsabile del download di un documento da alfresco
	 *
	 */
	public SpagicClientDownloadDocument getSpagicClientDownloadDocument() {
		return new SpagicClientDownloadDocumentRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	/**
	 *
	 * Metodo per la creazione del componente responsabile della gestione degli allegati di una pratica
	 *
	 */
	public SpagicClientGestioneAllegatoPratica getSpagicClientGestioneAllegatoPratica() {
		return new SpagicClientGestioneAllegatoPraticaRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	/**
	 *
	 * Metodo per la creazione del componente responsabile del recupero dei dati necessari alla popolazione delle combobox utilizzate nella protocollazione
	 *
	 */
	public SpagicClientGetComboBoxesProtocollazione getSpagicClientGetComboBoxesProtocollazione() {
		return new SpagicClientGetComboBoxesProtocollazioneRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	/**
	 *
	 * Metodo per la creazione del componente responsabile del recupero dei tipi pratica per protocollazione
	 *
	 */
	public SpagicClientGetTipoPraticaProtocollazione getSpagicClientGetTipoPraticaProtocollazione() {
		return new SpagicClientGetTipoPraticaProtocollazioneRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	/**
	 *
	 * Metodo per la creazione del componente responsabile della protocollazione completa di pratiche e fascicoli
	 *
	 */
	public SpagicClientProtocollazioneCompleta getSpagicClientProtocollazioneCompleta() {
		return new SpagicClientProtocollazioneCompletaRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	/**
	 *
	 * Metodo per la creazione del componente responsabile della riassegnazione di un fascicolo
	 *
	 */
	public SpagicClientGestioneFascicolo getSpagicClientRiassegnaFascicolo() {
		return new SpagicClientGestioneFascicoloRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	/**
	 *
	 * Metodo per la creazione del componente responsabile della verifica della validità della firma digitale di un certo file
	 *
	 */
	public SpagicClientVerifySingnatureDocument getSpagicClientVerifySingnatureDocument() {
		return new SpagicClientVerifySingnatureDocumentRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	/**
	 *
	 * Metodo per la creazione del componente responsabile del recupero di tutte le versioni di un determinato contenuto
	 *
	 */
	public SpagicClientVersioning getSpagicClientVersioning() {
		return new SpagicClientVersioningRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	/**
	 * Gestione degli upload/download dei metadati
	 */
	@Deprecated
	public SpagicClientHandleLockedMetadata getSpagicClientHandleLockedMetadata() {
		return new SpagicClientHandleLockedMetadataRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	/**
	 *
	 */
	public SpagicClientDownlodAllegatoOriginale getSpagicClientDownlodAllegatoOriginale() {
		return new SpagicClientDownlodAllegatoOriginaleRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	/**
	 *
	 */
	public SpagicClientPubblicazioneAllegato getSpagicClientPubblicazioneAllegato() {
		return new SpagicClientPubblicazioneAllegatoRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	/**
	 *
	 */
	public SpagicClientRiattivaPratica getSpagicClientRiattivaPratica() {
		return new SpagicClientRiattivaPraticaRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	/**
	 *
	 */
	public SpagicClientRicercaCapofila getSpagicClientRicercaCapofila() {
		return new SpagicClientRicercaCapofilaRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	/**
	 *
	 */
	public SpagicClientInterrogaPG getSpagicClientInterrogaPG() {
		return new SpagicClientInterrogaPGRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	/**
	 *
	 */
	public SpagicClientVersamentoParerPG getSpagicClientVersamentoParerPG() {
		return new SpagicClientVersamentoParerPGRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	/**
	 * componente responsabile delle operazioni sull'iperfascicolo
	 *
	 * @return
	 */
	public SpagicClientIperFascicoloHandler getSpagicClientIperFascicoloHandler() {
		return new SpagicClientIperFascicoloHandlerRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	/**
	 * componente per la gestione dei dati aggiuntivi ad un fascicolo
	 *
	 * @return
	 */
	public SpagicClientDatiAggiuntivi getSpagicClientDatiAggiuntivi() {
		return new SpagicClientDatiAggiuntiviRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	/**
	 * componente per la creazione della pratica modulistica
	 *
	 * @return
	 */
	public SpagicClientCreatePraticaModulistica getSpagicClientCreatePraticaModulistica() {
		return new SpagicClientCreatePraticaModulisticaRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	/**
	 * componente per la gestione delle email elettorali
	 *
	 * @return
	 */
	public SpagicClientGestioneElettorale getSpagicClientGestioneElettorale() {
		return new SpagicClientGestioneElettoraleRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	/**
	 * componente per la gestione del template
	 *
	 * @return
	 */
	public SpagicClientGestioneTemplate getSpagicClientGestioneTemplate() {
		return new SpagicClientGestioneTemplateRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	public SpagicClientGestioneComunicazioniMassive getSpagicClientGestioneComunicazioniMassive() {
		return new SpagicClientGestioneComunicazioniMassiveRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	public SpagicClientGestioneProcedimenti getSpagicClientGestioneProcedimenti() {
		return new SpagicClientGestioneProcedimentiRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	public SpagicClientGestioneFascicolo getSpagicClientGestioneFascicolo() {
		return new SpagicClientGestioneFascicoloRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	public SpagicClientModificaFascicolo getSpagicClientModificaFascicolo() {
		return new SpagicClientModificaFascicoloRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	public SpagicClientTaskFirma getSpagicClientTaskFirma() {
		return new SpagicClientTaskFirmaRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	/*
	 * Ricerca
	 */
	/**
	 * Ricerca pratiche per utilizzo interno. Deprecato per utilizzo esterno;<br>
	 * Per ricerche esterne {@link RicercaPraticheClient}
	 *
	 * @Deprecated replacedBy getRicercaPraticheClient()
	 */
	@Deprecated
	public SpagicClientNewSearchPratiche getSpagicClientNewSearchPratiche() {
		return new SpagicClientNewSearchPraticheRemoteProxy(serviceProxyUrl, alfrescoUsername, alfrescoPassword, serviceUsername, servicePassword, restServiceUrl);
	}

	/*
	 * Nuovi client
	 */

	private ClientInvoker getClientInvoker() {
		return new RestClientInvoker(restServiceUrl);
	}

	public TaskFirmaClient getTaskFirmaClient() {
		return new TaskFirmaClientImpl(getClientInvoker());
	}

	public DownloadAllegatoClient getDownloadAllegatoClient() {
		return new DownloadAllegatoClientImpl(getClientInvoker());
	}

	public GestioneAllegatoClient getGestioneAllegatoClient() {
		return new GestioneAllegatoClientImpl(getClientInvoker());
	}

	public ArchiviaPraticaClient getArchiviaPraticaClient() {
		return new ArchiviaPraticaClientImpl(getClientInvoker());
	}

	public RiassegnaPraticaClient getRiassegnaPraticaClient() {
		return new RiassegnaPraticaClientImpl(getClientInvoker());
	}

	public CambiaVisibilitaAllegatoClient getCambiaVisibilitaAllegatoClient() {
		return new CambiaVisibilitaAllegatoClientImpl(getClientInvoker());
	}

	public CambiaVisibilitaFascicoloClient getCambiaVisibilitaFascicoloClient() {
		return new CambiaVisibilitaFascicoloClientImpl(getClientInvoker());
	}

	public StepIterClient getStepIterClient() {
		return new StepIterClientImpl(getClientInvoker());
	}

	public OperativitaRidottaClient getOperativitaRidottaClient() {
		return new OperativitaRidottaClientImpl(getClientInvoker());
	}

	public RicercaPraticheClient getRicercaPraticheClient() {
		return new RicercaPraticheClientImpl(getClientInvoker());
	}

	public GestioneEmailOutClient getGestioneEmailOutClient() {
		return new GestioneEmailOutClientImpl(getClientInvoker());
	}

	public CollegamentoPraticheClient getCollegamentoPraticheClient() {
		return new CollegamentoPraticheClientImpl(getClientInvoker());
	}

	public ModificaNoteClient getModificaNoteClient() {
		return new ModificaNoteClientImpl(getClientInvoker());
	}

	public GestioneProcedimentiClient getGestioneProcedimentiClient() {
		return new GestioneProcedimentiClientImpl(getClientInvoker());
	}

	public PresaInCaricoClient getPresaInCaricoClient() {
		return new PresaInCaricoClientImpl(getClientInvoker());
	}
}
