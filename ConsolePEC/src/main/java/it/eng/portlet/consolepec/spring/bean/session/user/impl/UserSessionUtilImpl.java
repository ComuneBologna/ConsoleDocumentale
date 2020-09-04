package it.eng.portlet.consolepec.spring.bean.session.user.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.RequestProvider;
import com.liferay.portal.model.Role;

import it.eng.cobo.consolepec.util.utente.UtenteUtils;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.portlet.consolepec.spring.bean.session.user.ConsolePecUser;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import lombok.Getter;

public class UserSessionUtilImpl implements UserSessionUtil {

	private static final Logger logger = LoggerFactory.getLogger(UserSessionUtilImpl.class);

	@Autowired
	RequestProvider requestProvider;

	private String userSessionKey;

	@Getter
	private List<String> ruoli = new ArrayList<String>();

	public UserSessionUtilImpl() {}

	@Override
	public HttpSession getHttpSession() {
		HttpSession httpSession = requestProvider.getServletRequest().getSession();
		logger.trace("jsessionid springside: " + requestProvider.getServletRequest().getSession().getId());
		return httpSession;
	}

	@Override
	public DatiPratica.Utente getUtentePratica() {
		Utente utenteSpagic = getUtenteSpagic();
		return new DatiPratica.Utente(utenteSpagic.getUsername(), utenteSpagic.getNome(), utenteSpagic.getCognome(), utenteSpagic.getMatricola(), utenteSpagic.getCodicefiscale(), null);
	}

	@Override
	public Utente getUtenteSpagic() {
		ConsolePecUser consolePecUser = getUtenteConsolePEC();
		logger.trace("lettura dell'utente {}", consolePecUser);

		List<String> ruoli = new ArrayList<String>();

		if (ruoli.isEmpty()) {
			TreeSet<Role> userRoles = new TreeSet<Role>();
			try {
				userRoles.addAll(consolePecUser.getUser().getRoles());

			} catch (Exception e) {
				logger.error("Errore nel recupero dei ruoli", e);
				return null;
			}

			for (Role userRole : userRoles) {
				ruoli.add(userRole.getName());
			}

		} else {
			ruoli.addAll(this.ruoli);
		}

		ruoli.add(UtenteUtils.calcolaRuoloPersonale(consolePecUser.getUser().getFirstName(), consolePecUser.getUser().getLastName(), (String) consolePecUser.getCustomAttribute("coboCF"),
				(String) consolePecUser.getCustomAttribute("matricola")));

		return new Utente(consolePecUser.getUser().getFirstName(), consolePecUser.getUser().getLastName(), consolePecUser.getUser().getScreenName(),
				(String) consolePecUser.getCustomAttribute("matricola"), (String) consolePecUser.getCustomAttribute("coboCF"), ruoli, null, ruoli.contains("ExternalUserGroup"));
	}

	@Override
	public void setCurrentUserSessionKey(String userSessionKey) {
		this.userSessionKey = userSessionKey;
	}

	@Override
	public ConsolePecUser getUtenteConsolePEC() {
		ConsolePecUser user = (ConsolePecUser) getHttpSession().getAttribute(userSessionKey);
		return user;
	}

}
