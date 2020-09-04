package it.eng.consolepec.client.impl;

import java.util.Date;
import java.util.List;

import it.eng.cobo.consolepec.commons.configurazioni.AbilitazioniRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaComunicazione;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaEmailOut;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaModello;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaPraticaModulistica;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.Abilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.properties.ProprietaGenerali;
import it.eng.cobo.consolepec.commons.profilazione.Settore;
import it.eng.consolepec.client.AbstractConsolePecClient;
import it.eng.consolepec.client.ClientInvoker;
import it.eng.consolepec.client.ConfigurazioniClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.servicename.ServiceNamesUtil;

/**
 *
 * @author biagiot
 *
 */
public class ConfigurazioniClientImpl extends AbstractConsolePecClient implements ConfigurazioniClient {

	public ConfigurazioniClientImpl(ClientInvoker clientInvoker) {
		super(clientInvoker);
	}

	@Override
	public List<AnagraficaRuolo> getAnagraficheRuoli(Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CONFIGURAZIONI_ANGRAFICHE_RUOLI, utente);
	}

	@Override
	public List<Settore> getSettori(Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CONFIGURAZIONI_SETTORI, utente);
	}

	@Override
	public ProprietaGenerali getProprietaGenerali(Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CONFIGURAZIONI_PROPRIETA_GENERALI, utente);
	}

	@Override
	public List<AnagraficaFascicolo> getAnagraficheFascicoli(Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CONFIGURAZIONI_ANAGRAFICHE_FASCICOLI, utente);
	}

	@Override
	public List<AnagraficaIngresso> getAnagraficheIngressi(Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CONFIGURAZIONI_ANAGRAFICHE_INGRESSI, utente);
	}

	@Override
	public List<AnagraficaModello> getAnagraficheModelli(Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CONFIGURAZIONI_ANAGRAFICHE_MODELLI, utente);
	}

	@Override
	public List<AnagraficaEmailOut> getAnagraficheMailInUscita(Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CONFIGURAZIONI_ANAGRAFICHE_MAIL_IN_USCITA, utente);
	}

	@Override
	public List<AnagraficaComunicazione> getAnagraficheComunicazioni(Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CONFIGURAZIONI_ANAGRAFICHE_COMUNICAZIONI, utente);
	}

	@Override
	public List<AnagraficaPraticaModulistica> getAnagrafichePraticaModulistica(Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CONFIGURAZIONI_ANAGRAFICHE_PRATICHE_MODULISTICA, utente);
	}

	@Override
	public List<AnagraficaRuolo> getRuoliPersonali(Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CONFIGURAZIONI_ANAGRAFICHE_RUOLI_PERSONALI, utente);
	}

	@Override
	public List<AbilitazioniRuolo> getAbilitazioniRuoli(Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CONFIGURAZIONI_ABILITAZIONI_RUOLI, utente);
	}

	@Override
	public List<String> getEmailAssegnaEsterno(Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CONFIGURAZIONI_LISTA_EMAIL_ASSEGNA_ESTERNO, utente);
	}

	@Override
	public List<Abilitazione> getAbilitazioniRuoloCartellaDiFirma(AnagraficaRuolo ruolo, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CONFIGURAZIONI_ABILITAZIONI_RUOLO_CARTELLA_FIRMA, utente, ruolo);
	}

	@Override
	public List<String> getRuoliVisibilita(String tipologiaPratica, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CONFIGURAZIONI_ABILITAZIONI_VISIBILITA_TIPO_PRATICA, utente, tipologiaPratica);
	}

	@Override
	public List<AnagraficaRuolo> getRuoliSuperutentiMatriceVisibilita(AnagraficaRuolo ar, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CONFIGURAZIONI_ABILITAZIONI_MATRICE_VISIBILITA_RUOLO, utente, ar);
	}

	@Override
	public List<AnagraficaRuolo> getRuoliSuperutentiMatriceVisibilita(String tipologiaPratica, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CONFIGURAZIONI_ABILITAZIONI_MATRICE_VISIBILITA_TIPO_PRATICA, utente, tipologiaPratica);
	}

	@Override
	public List<AnagraficaRuolo> getRuoliSuperutentiModifica(AnagraficaRuolo ar, Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CONFIGURAZIONI_ABILITAZIONI_MODIFICA_RUOLO, utente, ar);
	}

	@Override
	public Date getLastEditDate(Utente utente) throws SpagicClientException {
		return clientInvoker.invoke(ServiceNamesUtil.CONFIGURAZIONI_GET_LAST_EDIT, utente);
	}
}
