package it.eng.portlet.consolepec.spring.genericdata.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.datigenerici.IndirizzoEmail;
import it.eng.consolepec.client.IndirizziEmailRubricaClient;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import it.eng.portlet.consolepec.spring.genericdata.IndirizzoEmailCachePrimoLivello;

/**
 *
 * Questa classe realizza il sistema di caching di primo livello che permette di rendere più efficiente il recupero di tutti gli indirizzi email dal db. Email che costituiranno l'autocomplete
 * nell'invio della PEC. Inoltre gestisce il salvataggio di nuove mail non ancora censite in db.
 *
 * @author biagiot
 *
 */
public class IndirizzoEmailCachePrimoLivelloImpl implements IndirizzoEmailCachePrimoLivello {

	@Autowired
	IndirizziEmailRubricaClient indirizziEmailRubricaClient;

	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;

	@Autowired
	CacheManager cacheManager;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Override
	@Cacheable(value = "allIndirizziEmail", key = "#root.methodName")
	public List<IndirizzoEmail> getAllIndirizziEmail(Utente utente) {
		return indirizziEmailRubricaClient.getIndirizziEmail(utente);
	}

	@Override
	public void insertIndirizzoEmail(String indirizzoEmail) {

		List<String> ruoli = Lists.transform(gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli(), new Function<AnagraficaRuolo, String>() {

			@Override
			public String apply(AnagraficaRuolo input) {
				return input.getRuolo();
			}

		});

		IndirizzoEmail email = new IndirizzoEmail();
		email.setIndirizzoEmail(indirizzoEmail.toLowerCase());
		email.getRuoli().addAll(ruoli);
		email.setFonte(gestioneProfilazioneUtente.getDatiUtente().getUsername());
		email.setAbilitatoPerTutti(false);

		if (indirizziEmailRubricaClient.inserisci(email, userSessionUtil.getUtenteSpagic())) {
			Cache cache = cacheManager.getCache("allIndirizziEmail");
			ValueWrapper valueWrapper = cache.get("getAllIndirizziEmail");

			@SuppressWarnings("unchecked") List<IndirizzoEmail> cached = new ArrayList<IndirizzoEmail>((List<IndirizzoEmail>) valueWrapper.get());
			cached.add(email);
			cache.put("getAllIndirizziEmail", cached);

		}
	}
}
