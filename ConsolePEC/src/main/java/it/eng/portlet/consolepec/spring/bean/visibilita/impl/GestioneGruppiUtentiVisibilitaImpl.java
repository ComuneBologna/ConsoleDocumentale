package it.eng.portlet.consolepec.spring.bean.visibilita.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.ConsoleDocumentaleException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.cobo.consolepec.commons.ldap.LdapQueryFilter;
import it.eng.cobo.consolepec.commons.ldap.LdapUser;
import it.eng.cobo.consolepec.util.json.JsonFactory;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.cobo.consolepec.util.utente.UtenteUtils;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverterUtil;
import it.eng.portlet.consolepec.gwt.server.rest.RestClientInvoker;
import it.eng.portlet.consolepec.gwt.server.rest.RestResponse;
import it.eng.portlet.consolepec.gwt.shared.model.GruppoVisibilita;
import it.eng.portlet.consolepec.gwt.shared.model.GruppoVisibilita.TipoVisibilita;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.visibilita.GestioneGruppiUtentiVisibilita;

public class GestioneGruppiUtentiVisibilitaImpl implements GestioneGruppiUtentiVisibilita {

	private static final Logger logger = LoggerFactory.getLogger(GestioneGruppiUtentiVisibilitaImpl.class);

	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;

	@Autowired
	XMLPluginToDTOConverterUtil xmlPluginToDTOConverterUtil;

	@Autowired
	RestClientInvoker restClientInvoker;

	@Override
	@Cacheable(value = "gruppiVisibilita", key = "#tipoPratica.toString()")
	public TreeSet<GruppoVisibilita> getUtentiGruppiVisibilita(final TipologiaPratica tipoPratica) throws ConsoleDocumentaleException {
		logger.info("Recupero degli utenti e dei gruppi per la pratica: {}", tipoPratica.getNomeTipologia());

		/*
		 * Elenco ruoli abilitati per il tipo pratica
		 */
		if (!PraticaUtil.isFascicolo(tipoPratica)) {
			throw new InvalidArgumentException("Tipo pratica non gestito", false);
		}

		List<AnagraficaRuolo> ruoliVisibilita = gestioneConfigurazioni.getRuoliVisibilita(tipoPratica);

		List<String> elencoGruppiPerTipoPratica = Lists.transform(ruoliVisibilita, new Function<AnagraficaRuolo, String>() {

			@Override
			public String apply(AnagraficaRuolo input) {
				return input.getRuolo();
			}
		});

		logger.debug("Gruppi abilitati: {}", elencoGruppiPerTipoPratica);

		/*
		 * Elenco utenti appartenenti ai ruoli
		 */

		List<LdapUser> utentiLdap = new ArrayList<LdapUser>();

		if (elencoGruppiPerTipoPratica != null && !elencoGruppiPerTipoPratica.isEmpty()) {
			LdapQueryFilter ricerca = new LdapQueryFilter();
			ricerca.getGruppi().addAll(elencoGruppiPerTipoPratica);
			HttpEntity entity = new StringEntity(JsonFactory.defaultFactory().serialize(ricerca), ContentType.APPLICATION_JSON);

			RestResponse output = null;

			try {
				output = restClientInvoker.customPost("/service/ldap/gruppi", null, entity);

			} catch (ConsoleDocumentaleException e) {
				logger.error("Errore durante l'invocazione del servizio: " + "/service/ldap/gruppi");
				throw e;
			}

			if (!output.isOk()) {
				throw new ApplicationException("Errore nella chiamata al servizio: " + "/service/ldap/gruppi - " + output.getJson(), false);
			}

			String json = output.getJson();
			utentiLdap = JsonFactory.defaultFactory().deserializeList(json, LdapUser.class);
		}

		/*
		 * Merge
		 */
		return mergeGruppiVisibilita(getUtentiVisibilita(utentiLdap), getRuoliVisibilita(ruoliVisibilita));
	}

	private static TreeSet<GruppoVisibilita> getUtentiVisibilita(List<LdapUser> utenti) {
		TreeSet<GruppoVisibilita> gruppiVisibilita = new TreeSet<GruppoVisibilita>();
		for (LdapUser utente : utenti) {
			AnagraficaRuolo rp = UtenteUtils.getAnagraficaRuoloPersonale(utente.getName(), utente.getSurname(), utente.getCodiceFiscale(), utente.getMatricola());
			gruppiVisibilita.add(new GruppoVisibilita(rp.getRuolo(), rp.getEtichetta(), TipoVisibilita.UTENTE));
		}

		return gruppiVisibilita;
	}

	private static TreeSet<GruppoVisibilita> getRuoliVisibilita(List<AnagraficaRuolo> anagraficaRuoli) {
		TreeSet<GruppoVisibilita> gruppiVisibilita = new TreeSet<GruppoVisibilita>();
		for (AnagraficaRuolo ar : anagraficaRuoli) {
			gruppiVisibilita.add(new GruppoVisibilita(ar.getRuolo(), ar.getEtichetta(), TipoVisibilita.GRUPPO));
		}

		return gruppiVisibilita;
	}

	private static TreeSet<GruppoVisibilita> mergeGruppiVisibilita(TreeSet<GruppoVisibilita> utenti, TreeSet<GruppoVisibilita> gruppi) {
		gruppi.addAll(utenti);
		return gruppi;
	}
}
