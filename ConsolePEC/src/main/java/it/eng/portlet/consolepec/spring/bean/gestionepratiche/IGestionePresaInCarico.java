package it.eng.portlet.consolepec.spring.bean.gestionepratiche;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Utente;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

public interface IGestionePresaInCarico {

	public Pratica<?> rilascia(String id) throws SpagicClientException;

	public Pratica<?> prendiInCarico(String id) throws SpagicClientException;

	public boolean hasInCaricoUtenteCorrente(String id) throws SpagicClientException;

	public void caricaPresaInCarico(Pratica<?> pratica, PraticaDTO praticaDTO) throws SpagicClientException;

	public void caricaPresaInCarico(PraticaDTO praticaDTO) throws SpagicClientException;

	public String getInCaricoALabel(Utente utente) throws SpagicClientException;
}
