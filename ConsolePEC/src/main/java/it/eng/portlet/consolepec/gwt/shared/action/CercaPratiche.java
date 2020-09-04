package it.eng.portlet.consolepec.gwt.shared.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.portlet.consolepec.gwt.shared.model.ColonnaWorklist;
import it.eng.portlet.consolepec.gwt.shared.model.Destinatario.StatoDestinatario;
import it.eng.portlet.consolepec.gwt.shared.model.TipoEmail;
import it.eng.portlet.consolepec.gwt.shared.model.TipoProtocollazione;
import lombok.Getter;
import lombok.Setter;

public class CercaPratiche extends LiferayPortletUnsecureActionImpl<CercaPraticheResult> {

	/* tipo di query */
	private boolean count = false;

	/* generici di pratica */
	@Pattern(regexp = "(\\s*[0-9]{4,4}\\s*)?", message = "L'anno P.G. deve essere composto da 4 cifre")
	private String anno;
	@Pattern(regexp = "\\s*([0-9])*\\s*", message = "Il numero PG deve essere composto solo da cifre")
	private String numero;
	private List<String> assegnatoA = new ArrayList<String>();
	private String idDocumentale, provenienza, titolo;

	private String dataCreazioneFrom;
	private Integer dataCreazioneFromHour;
	private Integer dataCreazioneFromMinute;
	private String dataCreazioneTo;
	private Integer dataCreazioneToHour;
	private Integer dataCreazioneToMinute;

	private String[] stato;
	private Boolean daLeggere = false, soloWorklist = false, superutente = false, ignoraGruppi = false;
	private Boolean hasRicevutaAccettazione = false;
	private Boolean hasRicevutaConsegna = false;
	private Boolean escludiProprieAssegnazioni = false;

	/* specifici email */
	private String mittente, cc, idEmail;
	private String destinatario;
	private TipoEmail tipoEmail;

	/* specifici fascicolo */
	private String utenteCreazione;
	private String stepIter;
	private String operatore;

	/* campi per gestione sorting */
	private ColonnaWorklist campoOrdinamento;
	private boolean ordinamentoAsc;
	/* campi per gestione paginazione */
	private int inizio;
	private int fine;

	/* Campi per la gestione della protocollazione */
	private String oggettoProtocollazione;
	private String riferimentoProvenienzaProtocollazione;
	private String provenienzaProtocollazione;
	private String CFprovenienzaProtocollazione;
	private String tipologiadelDocumentoProtocollazione;
	private String titoloDocumentoProtocollazione;
	private String rubricaProtocollazione;
	private String sezioneProtocollazione;
	private String dataProtocollazione;

	@Pattern(regexp = "\\s*([0-9])*\\s*", message = "Il numero fascicolo deve essere composto solo da cifre")
	private String numeroFascicolo;
	@Pattern(regexp = "(\\s*[0-9]{4,4}\\s*)?", message = "L'anno registro deve essere composto da 4 cifre")
	private String annoRegistro;
	@Pattern(regexp = "\\s*([0-9])*\\s*", message = "Il numero registro deve essere composto solo da cifre")
	private String numeroRegistro;

	private String annoFascicolo;
	private TipoProtocollazione tipoProtocollazione;
	private String ricercaPerCollegamento;
	private List<String> gruppiDiCondivisione = new ArrayList<String>();
	private Map<DatoAggiuntivo, Object> datiAggiuntivi;
	private String nomeModulo;
	private String tipoFascicoloAbilitatoSceltaTemplate;
	private List<String> valoriModulo = new ArrayList<String>();
	private String nomeTemplate;
	private String destinatarioAssegnaUtenteEsterno;
	private Map<String, Object> parametriFissiWorklist;
	private String codiceComunicazione;
	private String idTemplateComunicazione;
	private StatoDestinatario statoDestinatario;
	private boolean filtroAbilitazioni;
	private String tipoPraticaProcedi;
	private String indirizzoVia;
	private String indirizzoCivico;
	private String ambito;
	private String cognomeNome;

	/*
	 * Filtri generici pratiche
	 */

	@Getter
	@Setter
	private String idDocumentaleDaEscludere;

	@Getter
	@Setter
	private List<TipologiaPratica> tipologiePratiche = new ArrayList<>();

	@Getter
	@Setter
	private String dataRicezioneFrom, dataRicezioneTo;

	@Getter
	@Setter
	private Integer dataRicezioneFromHour, dataRicezioneFromMinute, dataRicezioneToHour, dataRicezioneToMinute;

	@Getter
	@Setter
	private boolean filtroGruppiVisibilita;

	public String getDataCreazioneFrom() {
		return safeTrim(dataCreazioneFrom);
	}

	public void setDataCreazioneFrom(String dataCreazioneFrom) {
		this.dataCreazioneFrom = dataCreazioneFrom;
	}

	public List<String> getAssegnatoA() {
		return assegnatoA;
	}

	public void setAssegnatoA(List<String> assegnatoA) {
		this.assegnatoA = assegnatoA;
	}

	public String getIdDocumentale() {
		return safeTrim(idDocumentale);
	}

	public void setIdDocumentale(String idDocumentale) {
		this.idDocumentale = idDocumentale;
	}

	public String getProvenienza() {
		return safeTrim(provenienza);
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public String[] getStato() {
		return stato;
	}

	public void setStato(String[] stato) {
		this.stato = stato;
	}

	public String getTitolo() {
		return safeTrim(titolo);
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getMittente() {
		return safeTrim(mittente);
	}

	public void setMittente(String mittente) {
		this.mittente = mittente;
	}

	public String getDestinatario() {
		return safeTrim(destinatario);
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getCc() {
		return safeTrim(cc);
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getIdEmail() {
		return safeTrim(idEmail);
	}

	public void setIdEmail(String idEmail) {
		this.idEmail = idEmail;
	}

	public String getUtenteCreazione() {
		return safeTrim(utenteCreazione);
	}

	public void setUtenteCreazione(String utenteCreazione) {
		this.utenteCreazione = utenteCreazione;
	}

	public String getStepIter() {
		return stepIter;
	}

	public void setStepIter(String stepIter) {
		this.stepIter = stepIter;
	}

	public ColonnaWorklist getCampoOrdinamento() {
		return campoOrdinamento;
	}

	public void setCampoOrdinamento(ColonnaWorklist campoOrdinamento) {
		this.campoOrdinamento = campoOrdinamento;
	}

	public int getInizio() {
		return inizio;
	}

	public void setInizio(int inizio) {
		this.inizio = inizio;
	}

	public int getFine() {
		return fine;
	}

	public void setFine(int fine) {
		this.fine = fine;
	}

	public boolean isOrdinamentoAsc() {
		return ordinamentoAsc;
	}

	public void setOrdinamentoAsc(boolean ordinamentoAsc) {
		this.ordinamentoAsc = ordinamentoAsc;
	}

	public TipoEmail getTipoEmail() {
		return tipoEmail;
	}

	public void setTipoEmail(TipoEmail tipoEmail) {
		this.tipoEmail = tipoEmail;
	}

	public String getDataCreazioneTo() {
		return safeTrim(dataCreazioneTo);
	}

	public void setDataCreazioneTo(String dataCreazioneTo) {
		this.dataCreazioneTo = dataCreazioneTo;
	}

	public Boolean getDaLeggere() {
		return daLeggere;
	}

	public void setDaLeggere(Boolean daLeggere) {
		this.daLeggere = daLeggere;
	}

	/*
	 * Filtri Protocollazione
	 */

	public void setNumeroFascicolo(String numeroFascicolo) {
		this.numeroFascicolo = numeroFascicolo;
	}

	public String getNumeroFascicolo() {
		return safeTrim(numeroFascicolo);
	}

	public String getAnnoRegistro() {
		return safeTrim(annoRegistro);
	}

	public void setAnnoRegistro(String annoRegistro) {
		this.annoRegistro = annoRegistro;
	}

	public String getNumeroRegistro() {
		return safeTrim(numeroRegistro);
	}

	public void setNumeroRegistro(String numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	public String getAnno() {
		return safeTrim(anno);
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getNumero() {
		return safeTrim(numero);
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getOggettoProtocollazione() {
		return safeTrim(oggettoProtocollazione);
	}

	public void setOggettoProtocollazione(String oggettoProtocollazione) {
		this.oggettoProtocollazione = oggettoProtocollazione;
	}

	public String getRiferimentoProvenienzaProtocollazione() {
		return safeTrim(riferimentoProvenienzaProtocollazione);
	}

	public void setRiferimentoProvenienzaProtocollazione(String riferimentoProvenienzaProtocollazione) {
		this.riferimentoProvenienzaProtocollazione = riferimentoProvenienzaProtocollazione;
	}

	public String getProvenienzaProtocollazione() {
		return safeTrim(provenienzaProtocollazione);
	}

	public void setProvenienzaProtocollazione(String provenienzaProtocollazione) {
		this.provenienzaProtocollazione = provenienzaProtocollazione;
	}

	public String getCFprovenienzaProtocollazione() {
		return safeTrim(CFprovenienzaProtocollazione);
	}

	public void setCFprovenienzaProtocollazione(String cFprovenienzaProtocollazione) {
		CFprovenienzaProtocollazione = cFprovenienzaProtocollazione;
	}

	public String getTipologiadelDocumentoProtocollazione() {
		return safeTrim(tipologiadelDocumentoProtocollazione);
	}

	public void setTipologiadelDocumentoProtocollazione(String tipologiadelDocumentoProtocollazione) {
		this.tipologiadelDocumentoProtocollazione = tipologiadelDocumentoProtocollazione;
	}

	public String getTitoloDocumentoProtocollazione() {
		return safeTrim(titoloDocumentoProtocollazione);
	}

	public void setTitoloDocumentoProtocollazione(String titoloDocumentoProtocollazione) {
		this.titoloDocumentoProtocollazione = titoloDocumentoProtocollazione;
	}

	public String getRubricaProtocollazione() {
		return safeTrim(rubricaProtocollazione);
	}

	public void setRubricaProtocollazione(String rubricaProtocollazione) {
		this.rubricaProtocollazione = rubricaProtocollazione;
	}

	public String getSezioneProtocollazione() {
		return safeTrim(sezioneProtocollazione);
	}

	public void setSezioneProtocollazione(String sezioneProtocollazione) {
		this.sezioneProtocollazione = sezioneProtocollazione;
	}

	public String getDataProtocollazione() {
		return safeTrim(dataProtocollazione);
	}

	public void setDataProtocollazione(String string) {
		this.dataProtocollazione = string;
	}

	public Boolean getSoloWorklist() {
		return soloWorklist;
	}

	public void setSoloWorklist(Boolean soloWorklist) {
		this.soloWorklist = soloWorklist;
	}

	public Boolean getHasRicevutaAccettazione() {
		return hasRicevutaAccettazione;
	}

	public void setHasRicevutaAccettazione(Boolean hasRicevutaAccettazione) {
		this.hasRicevutaAccettazione = hasRicevutaAccettazione;
	}

	public Boolean getHasRicevutaConsegna() {
		return hasRicevutaConsegna;
	}

	public void setHasRicevutaConsegna(Boolean hasRicevutaConsegna) {
		this.hasRicevutaConsegna = hasRicevutaConsegna;
	}

	public boolean isCount() {
		return count;
	}

	public void setCount(boolean count) {
		this.count = count;
	}

	public TipoProtocollazione getTipoProtocollazione() {
		return tipoProtocollazione;
	}

	public void setTipoProtocollazione(TipoProtocollazione tipoProtocollazione) {
		this.tipoProtocollazione = tipoProtocollazione;
	}

	private static String safeTrim(String in) {
		return (in == null) ? null : in.trim();
	}

	public Map<DatoAggiuntivo, Object> getDatiAggiuntivi() {
		return datiAggiuntivi;
	}

	public void setDatiAggiuntivi(Map<DatoAggiuntivo, Object> datiAggiuntivi) {
		this.datiAggiuntivi = datiAggiuntivi;
	}

	public String getRicercaPerCollegamento() {
		return ricercaPerCollegamento;
	}

	public void setRicercaPerCollegamento(String ricercaPerCollegamento) {
		this.ricercaPerCollegamento = ricercaPerCollegamento;
	}

	public List<String> getGruppiDiCondivisione() {
		return gruppiDiCondivisione;
	}

	public void setGruppiDiCondivisione(List<String> gruppiDiCondivisione) {
		this.gruppiDiCondivisione = gruppiDiCondivisione;
	}

	public String getNomeModulo() {
		return nomeModulo;
	}

	public void setNomeModulo(String nomeModulo) {
		this.nomeModulo = nomeModulo;
	}

	public List<String> getValoriModulo() {
		return valoriModulo;
	}

	public String getAnnoFascicolo() {
		return annoFascicolo;
	}

	public void setAnnoFascicolo(String annoFascicolo) {
		this.annoFascicolo = annoFascicolo;
	}

	public String getNomeTemplate() {
		return safeTrim(nomeTemplate);
	}

	public void setNomeTemplate(String nomeTemplate) {
		this.nomeTemplate = nomeTemplate;
	}

	public String getDestinatarioAssegnaUtenteEsterno() {
		return safeTrim(destinatarioAssegnaUtenteEsterno);
	}

	public void setDestinatarioAssegnaUtenteEsterno(String destinatarioAssegnaUtenteEsterno) {
		this.destinatarioAssegnaUtenteEsterno = destinatarioAssegnaUtenteEsterno;
	}

	public Boolean getSuperutente() {
		return superutente;
	}

	public void setSuperutente(Boolean superutente) {
		this.superutente = superutente;
	}

	public void setParametriFissi(Map<String, Object> parametriFissiWorklist) {
		this.parametriFissiWorklist = parametriFissiWorklist;
	}

	public Map<String, Object> getParametriFissiWorklist() {
		return parametriFissiWorklist;
	}

	public String getTipoFascicoloAbilitatoSceltaTemplate() {
		return safeTrim(tipoFascicoloAbilitatoSceltaTemplate);
	}

	public void setTipoFascicoloAbilitatoSceltaTemplate(String tipoFascicoloAbilitatoSceltaTemplate) {
		this.tipoFascicoloAbilitatoSceltaTemplate = tipoFascicoloAbilitatoSceltaTemplate;
	}

	public String getCodiceComunicazione() {
		return safeTrim(codiceComunicazione);
	}

	public String getIdTemplateComunicazione() {
		return safeTrim(idTemplateComunicazione);
	}

	public void setCodiceComunicazione(String codiceComunicazione) {
		this.codiceComunicazione = codiceComunicazione;
	}

	public void setIdTemplateComunicazione(String idTemplateComunicazione) {
		this.idTemplateComunicazione = idTemplateComunicazione;
	}

	public StatoDestinatario getStatoDestinatario() {
		return statoDestinatario;
	}

	public void setStatoDestinatario(StatoDestinatario statoDestinatario) {
		this.statoDestinatario = statoDestinatario;
	}

	public Boolean getIgnoraGruppi() {
		return ignoraGruppi;
	}

	public void setIgnoraGruppi(Boolean ignoraGruppi) {
		this.ignoraGruppi = ignoraGruppi;
	}

	public String getOperatore() {
		return operatore;
	}

	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}

	public boolean isFiltroAbilitazioni() {
		return filtroAbilitazioni;
	}

	public void setFiltroAbilitazioni(boolean filtroAbilitazioni) {
		this.filtroAbilitazioni = filtroAbilitazioni;
	}

	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}

	public void setTipoPraticaProcedi(String tipoPraticaProcedi) {
		this.tipoPraticaProcedi = tipoPraticaProcedi;
	}

	public void setIndirizzoVia(String indirizzoVia) {
		this.indirizzoVia = indirizzoVia;
	}

	public void setIndirizzoCivico(String indirizzoCivico) {
		this.indirizzoCivico = indirizzoCivico;
	}

	public String getTipoPraticaProcedi() {
		return safeTrim(tipoPraticaProcedi);
	}

	public String getIndirizzoVia() {
		return safeTrim(indirizzoVia);
	}

	public String getIndirizzoCivico() {
		return safeTrim(indirizzoCivico);
	}

	public String getAmbito() {
		return safeTrim(ambito);
	}

	public String getCognomeNome() {
		return cognomeNome;
	}

	public void setCognomeNome(String cognomeNome) {
		this.cognomeNome = cognomeNome;
	}

	public Integer getDataCreazioneFromHour() {
		return dataCreazioneFromHour;
	}

	public void setDataCreazioneFromHour(Integer dataCreazioneFromHour) {
		this.dataCreazioneFromHour = dataCreazioneFromHour;
	}

	public Integer getDataCreazioneFromMinute() {
		return dataCreazioneFromMinute;
	}

	public void setDataCreazioneFromMinute(Integer dataCreazioneFromMinute) {
		this.dataCreazioneFromMinute = dataCreazioneFromMinute;
	}

	public Integer getDataCreazioneToHour() {
		return dataCreazioneToHour;
	}

	public void setDataCreazioneToHour(Integer dataCreazioneToHour) {
		this.dataCreazioneToHour = dataCreazioneToHour;
	}

	public Integer getDataCreazioneToMinute() {
		return dataCreazioneToMinute;
	}

	public void setDataCreazioneToMinute(Integer dataCreazioneToMinute) {
		this.dataCreazioneToMinute = dataCreazioneToMinute;
	}

	public Boolean getEscludiProprieAssegnazioni() {
		return escludiProprieAssegnazioni;
	}

	public void setEscludiProprieAssegnazioni(Boolean escludiProprieAssegnazioni) {
		this.escludiProprieAssegnazioni = escludiProprieAssegnazioni;
	}

}
