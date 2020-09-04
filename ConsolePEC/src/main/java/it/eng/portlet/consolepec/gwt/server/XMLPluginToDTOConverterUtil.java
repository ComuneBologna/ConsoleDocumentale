package it.eng.portlet.consolepec.gwt.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gwt.thirdparty.guava.common.base.Strings;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoTabella;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreMultiplo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreSingolo;
import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.cobo.consolepec.util.utente.UtenteUtils;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.GruppoVisibilita;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Collegamento;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Procedimento;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaFascicoloTask;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.dto.CollegamentoDto;
import it.eng.portlet.consolepec.gwt.shared.dto.TipologiaProcedimentoDto;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.GruppoVisibilita.TipoVisibilita;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ProcedimentoDto;
import it.eng.portlet.consolepec.gwt.shared.model.TipoEmail;
import it.eng.portlet.consolepec.gwt.shared.procedimenti.StatoProcedimento;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import it.eng.portlet.consolepec.spring.bean.tipologiaprocedimenti.RecuperoTipologiaProcedimenti;

public class XMLPluginToDTOConverterUtil {

	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;
	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	RecuperoTipologiaProcedimenti recuperoTipologiaProcedimenti;

	public ProcedimentoDto convertoProcedimentoToDto(Procedimento procedimento) {
		List<TipologiaProcedimentoDto> elencoTipologieProcedimenti = recuperoTipologiaProcedimenti.getElencoTipologieProcedimenti();
		HashMap<Integer, String> mappaTipologie = new HashMap<Integer, String>();
		for (TipologiaProcedimentoDto tipologia : elencoTipologieProcedimenti)
			if (!mappaTipologie.containsKey(tipologia.getCodiceProcedimento())) {
				mappaTipologie.put(tipologia.getCodiceProcedimento(), tipologia.getDescrizione());
			}
		ProcedimentoDto procedimentoDto = new ProcedimentoDto();
		procedimentoDto.setCodQuartiere(procedimento.getCodQuartiere());
		procedimentoDto.setCodTipologiaProcedimento(procedimento.getCodTipologiaProcedimento());
		procedimentoDto.setCodUnitaOrgCompetenza(procedimento.getCodUnitaOrgCompetenza());
		procedimentoDto.setCodUnitaOrgResponsabile(procedimento.getCodUnitaOrgResponsabile());
		procedimentoDto.setCodUtente(procedimento.getCodUtente());
		procedimentoDto.setDataInizioDecorrenzaProcedimento(procedimento.getDataInizioDecorrenzaProcedimento());
		procedimentoDto.setDurata(procedimento.getDurata());
		procedimentoDto.setFlagInterruzione(procedimento.getFlagInterruzione());
		procedimentoDto.setModAvvioProcedimento(procedimento.getModAvvioProcedimento());
		procedimentoDto.setAnnoPG(procedimento.getAnnoPG());
		procedimentoDto.setNumeroPG(procedimento.getNumeroPG());
		String descrizione = null;
		for (Integer codice : mappaTipologie.keySet())
			if (codice.intValue() == procedimento.getCodTipologiaProcedimento().intValue()) {
				descrizione = mappaTipologie.get(codice);
			}
		procedimentoDto.setDescrTipologiaProcedimento(descrizione);
		procedimentoDto.setResponsabile(procedimento.getResponsabile());
		procedimentoDto.setProvenienza(procedimento.getProvenienza());
		procedimentoDto.setModalitaChiusura(procedimento.getModalitaChiusura());
		procedimentoDto.setTermine(procedimento.getTermine());
		procedimentoDto.setDataChiusura(procedimento.getDataChiusura());
		procedimentoDto.setNumeroPGChiusura(procedimento.getNumeroPGChiusura());
		procedimentoDto.setAnnoPGChiusura(procedimento.getAnnoPGChiusura());
		procedimentoDto.setStato(procedimento.getDataChiusura() == null ? StatoProcedimento.AVVIATO : StatoProcedimento.CHIUSO);

		return procedimentoDto;
	}

	public void popolaVisibilita(PraticaDTO dto, TreeSet<GruppoVisibilita> gruppi) {
		StringBuilder sb = new StringBuilder();

		for (GruppoVisibilita gruppo : gruppi) {
			AnagraficaRuolo ar = gestioneConfigurazioni.getAnagraficaRuolo(gruppo.getNomeGruppo());

			if (ar != null) {
				sb.append(ar.getEtichetta()).append(", ");
				dto.getVisibilita().add(new it.eng.portlet.consolepec.gwt.shared.model.GruppoVisibilita(gruppo.getNomeGruppo(), ar.getEtichetta(), TipoVisibilita.GRUPPO));

			} else {
				sb.append(gruppo.getNomeGruppo()).append(", ");
				dto.getVisibilita().add(new it.eng.portlet.consolepec.gwt.shared.model.GruppoVisibilita(gruppo.getNomeGruppo(), UtenteUtils.calcolaEtichettaRuoloPersonale(gruppo.getNomeGruppo()),
						TipoVisibilita.UTENTE));
			}

		}

		if (sb.length() >= 2)
			sb.delete(sb.length() - 2, sb.length());

		dto.setVisibileA(sb.toString());
	}

	public TreeSet<it.eng.portlet.consolepec.gwt.shared.model.GruppoVisibilita> popolaVisibilitaAllegato(AllegatoDTO allegatoDto, TreeSet<GruppoVisibilita> gruppi) {
		for (GruppoVisibilita gruppo : gruppi) {
			AnagraficaRuolo ar = gestioneConfigurazioni.getAnagraficaRuolo(gruppo.getNomeGruppo(), false);

			if (ar != null) {
				allegatoDto.getVisibilita().add(new it.eng.portlet.consolepec.gwt.shared.model.GruppoVisibilita(gruppo.getNomeGruppo(), ar.getEtichetta(), TipoVisibilita.GRUPPO));

			} else {
				allegatoDto.getVisibilita().add(new it.eng.portlet.consolepec.gwt.shared.model.GruppoVisibilita(gruppo.getNomeGruppo(),
						UtenteUtils.calcolaEtichettaRuoloPersonale(gruppo.getNomeGruppo()), TipoVisibilita.UTENTE));
			}
		}

		return allegatoDto.getVisibilita();
	}

	public void popolaCollegamenti(FascicoloDTO dto, TreeSet<Collegamento> collegamenti) {
		for (Collegamento collegamento : collegamenti) {
			CollegamentoDto collegamentoDTO = new CollegamentoDto();
			collegamentoDTO.setClientId(Base64Utils.URLencodeAlfrescoPath(collegamento.getPath()));
			collegamentoDTO.setDisplayNameGruppo(gestioneConfigurazioni.getAnagraficaRuolo(collegamento.getNomeGruppo()).getEtichetta());
			for (Operazione operazioneConsentita : collegamento.getOperazioniConsentite())
				collegamentoDTO.getOperazioni().add(operazioneConsentita.getNomeOperazione());
			collegamentoDTO.setAccessibileInLettura(collegamento.getAccessibileInLettura());
			dto.getCollegamenti().add(collegamentoDTO);
		}
	}

	public boolean pubblicazioneOK(Date inizio, Date fine) {
		Date oggi = DateUtils.getMidnightToday();
		return (inizio != null && fine != null) && (fine.equals(oggi) || fine.after(oggi));
	}

	public Map<String, List<String>> getValoriPredefiniti(AnagraficaFascicolo anagraficaPratica) {
		final Map<String, List<String>> map = new HashMap<String, List<String>>();

		for (it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo d : anagraficaPratica.getDatiAggiuntivi()) {
			popolaValoriPredefiniti(d, map);
		}

		return map;
	}

	private void popolaValoriPredefiniti(it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo d, final Map<String, List<String>> map) {

		d.accept(new it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo.DatoAggiuntivoVisitorAdapter() {

			@Override
			public void visit(DatoAggiuntivoTabella datoAggiuntivoTabella) {

				for (DatoAggiuntivo d : datoAggiuntivoTabella.getIntestazioni()) {
					popolaValoriPredefiniti(d, map);
				}
			}

			@Override
			public void visit(DatoAggiuntivoValoreMultiplo d) {
				map.put(d.getNome(), d.getValoriPredefiniti());
			}

			@Override
			public void visit(DatoAggiuntivoValoreSingolo d) {
				map.put(d.getNome(), d.getValoriPredefiniti());
			}
		});
	}

	public List<String> getAllegatiDaFirmare(AllegatoDTO[] allegati) {
		ArrayList<String> alls = new ArrayList<String>();
		for (AllegatoDTO element : allegati) {
			alls.add(element.getNome());
		}
		return alls;
	}

	public it.eng.portlet.consolepec.gwt.shared.model.Destinatario getDestinatario(Destinatario destinatario) {
		it.eng.portlet.consolepec.gwt.shared.model.Destinatario d = new it.eng.portlet.consolepec.gwt.shared.model.Destinatario();
		d.setDestinatario(cleanIndirizzoMail(destinatario.getDestinatario()));
		d.setTipoEmail(destinatario.getTipo().equals(it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.TipoEmail.certificato) ? TipoEmail.PEC : TipoEmail.NORMALE);
		if (!destinatario.isAccettazione()) {
			d.setConsegnato(false);
			return d;
		}
		if (!destinatario.isConsegna() && d.getTipoEmail().equals(TipoEmail.PEC)) {
			d.setConsegnato(false);
		} else {
			d.setConsegnato(true);
		}
		d.setErrore(destinatario.getErrore());
		if (destinatario.getStatoDestinatario() != null)
			d.setStatoDestinatario(it.eng.portlet.consolepec.gwt.shared.model.Destinatario.StatoDestinatario.fromName(destinatario.getStatoDestinatario().name()));
		return d;
	}

	private static String cleanIndirizzoMail(String destinatario) {
		return destinatario.replaceAll(">", "").replaceAll("<", "");
	}

	public Destinatario getDestinatario(it.eng.portlet.consolepec.gwt.shared.model.Destinatario destinatario) {
		it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.TipoEmail tipo = destinatario.getTipoEmail().equals(TipoEmail.PEC)
				? it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.TipoEmail.certificato : it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.TipoEmail.esterno;

		boolean accettazione = false;
		boolean consegna = false;
		if (destinatario.getTipoEmail().equals(TipoEmail.NORMALE)) {
			accettazione = destinatario.isConsegnato();
		} else {
			accettazione = destinatario.isConsegnato();
			consegna = destinatario.isConsegnato();
		}

		Destinatario d = new Destinatario(destinatario.getDestinatario(), destinatario.getErrore(), tipo, accettazione, consegna);
		return d;

	}

	public String convertToSafeHtml(String body) {

		if (!Strings.isNullOrEmpty(body) && isHtmlToEscape(body))
			return Jsoup.clean(body, Whitelist.relaxed().addAttributes(":all", "style").addAttributes(":all", "dir"));

		return body;
	}

	private static boolean isHtmlToEscape(String content) {
		Document doc = Jsoup.parse(content);
		Elements elements = doc.select("base");
		return !elements.isEmpty();
	}

	public <T> T safeCast(Object o, Class<T> c) {
		if (o == null) {
			return null;
		}

		if (c.isInstance(o)) {
			return c.cast(o);
		}

		if (Integer.class.equals(c)) {
			if (String.class.isInstance(o)) {
				return c.cast(Integer.valueOf(String.class.cast(o)));
			}

		}

		if (String.class.equals(c)) {
			if (Integer.class.isInstance(o)) {
				return c.cast(Integer.toString(Integer.class.cast(o)));
			}

		}

		throw new IllegalArgumentException("Cannot cast to " + c + ": " + o.toString());
	}

	public boolean isAssegnatario(Task<?> task) {
		AnagraficaRuolo anagraficaRuoloAssegnatario = gestioneConfigurazioni.getAnagraficaRuolo(task.getDati().getAssegnatario().getNome());
		return gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli().contains(anagraficaRuoloAssegnatario);

	}

	public boolean checkAbilitazioneRiportaInGestione(RiattivaFascicoloTask riattivaFascicoloTask, List<Operazione> operazioniSuperutente, TipologiaPratica tipologiaPratica, String indirizzoEmail) {

		if (riattivaFascicoloTask == null) {
			return false;
		}

		String assegnatario = riattivaFascicoloTask.getDati().getAssegnatario().getNome();

		AnagraficaRuolo ruoloAssegnatario = gestioneConfigurazioni.getAnagraficaRuolo(assegnatario);

		if (gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli().contains(ruoloAssegnatario)) {
			return true;
		}

		if (gestioneProfilazioneUtente.isOperazioneSuperutenteAbilitata(assegnatario, TipoApiTask.RIPORTA_IN_GESTIONE, operazioniSuperutente, tipologiaPratica, indirizzoEmail)) {
			return true;
		}

		return false;
	}

	public boolean isCollegamentoVisitabile(String assegnatario) {
		AnagraficaRuolo ruolo = gestioneConfigurazioni.getAnagraficaRuoloByEtichetta(assegnatario);
		return gestioneProfilazioneUtente.isRuoloUtenteAbilitato(ruolo);
	}

}
