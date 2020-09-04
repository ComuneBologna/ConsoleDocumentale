package it.eng.portlet.consolepec.spring.bean.session.user;

import java.util.List;

import javax.servlet.http.HttpSession;

import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;

public interface UserSessionUtil {

	public HttpSession getHttpSession();

	public Utente getUtenteSpagic();

	public ConsolePecUser getUtenteConsolePEC();

	public DatiPratica.Utente getUtentePratica();

	public void setCurrentUserSessionKey(String userSessionKey);

	public List<String> getRuoli();

}
