package it.eng.portlet.consolepec.spring.genericdata.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;

import it.eng.cobo.consolepec.commons.datigenerici.IndirizzoEmail;
import it.eng.cobo.consolepec.commons.profilazione.Utente;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import it.eng.portlet.consolepec.spring.genericdata.IndirizzoEmailCachePrimoLivello;
import it.eng.portlet.consolepec.spring.genericdata.IndirizzoEmailCacheSecondoLivello;

/**
 *
 * Questa classe realizza il sistema di caching di secondo livello. Riceve tutti gli indirizzi email (cache 1 livello) ed estrae quelli che corrispondono alla chiave passata come parametro (dal
 * client) e in base al ruolo dell'utente.
 *
 * @author biagiot
 *
 */

public class IndirizzoEmailCacheSecondoLivelloImpl implements IndirizzoEmailCacheSecondoLivello {

	private final int RESULT_MAX_LENGTH = 10;

	@Autowired
	IndirizzoEmailCachePrimoLivello indirizzoEmailCachePrimoLivello;

	@Autowired
	CacheManager cacheManager;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Override
	@Cacheable(value = "indirizziEmailByKeyAndUser", key = "#chiave.concat('-').concat(#utente.username)")
	public List<String> getIndirizziEmail(String chiave, Utente utente) {
		List<String> result = new ArrayList<String>();

		List<IndirizzoEmail> indirizziEmail = indirizzoEmailCachePrimoLivello.getAllIndirizziEmail(userSessionUtil.getUtenteSpagic());

		if (!indirizziEmail.isEmpty()) {
			List<String> ruoli = utente.getRuoli();

			Iterator<IndirizzoEmail> iterator = indirizziEmail.iterator();
			int counter = 0;

			while (iterator.hasNext() && counter < RESULT_MAX_LENGTH) {
				IndirizzoEmail current = iterator.next();
				List<String> ruoliCheck = new ArrayList<String>(ruoli);

				if (current.getIndirizzoEmail().contains(chiave) && (current.isAbilitatoPerTutti() || ruoliCheck.removeAll(current.getRuoli())) && !result.contains(current.getIndirizzoEmail())) {

					result.add(current.getIndirizzoEmail());
					counter++;
				}
			}
		}

		return result;
	}

	@Override
	public void checkIndirizzoEmail(String indirizzoEmail, Utente utente) {

		List<String> emailFound = getIndirizziEmail(indirizzoEmail, utente);

		if (emailFound.isEmpty()) {
			Cache cache = cacheManager.getCache("indirizziEmailByKeyAndUser");
			cache.clear();

			indirizzoEmailCachePrimoLivello.insertIndirizzoEmail(indirizzoEmail);
		}
	}
}
