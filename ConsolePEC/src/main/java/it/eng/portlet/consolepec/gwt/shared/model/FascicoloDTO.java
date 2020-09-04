package it.eng.portlet.consolepec.gwt.shared.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo.StepIter;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.AllegatoComposizione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.EmailComposizione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ProtocollazioneComposizione;
import it.eng.portlet.consolepec.gwt.shared.action.pec.TipoRiferimentoPEC;
import it.eng.portlet.consolepec.gwt.shared.dto.CollegamentoDto;
import it.eng.portlet.consolepec.gwt.shared.dto.CondivisioneDto;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO specifico di un Fascicolo, client side
 *
 * @author pluttero
 *
 */
public class FascicoloDTO extends PraticaDTO {
	public enum StatoDTO {
		IN_GESTIONE("In gestione"), //
		ARCHIVIATO("Archiviato"), //
		IN_AFFISSIONE("In affissione"), //
		IN_VISIONE("In visione"), //
		ANNULLATO("Annullato"), //
		DA_INOLTRARE_ESTERNO("Assegnato via mail");

		private String label;

		StatoDTO(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		public static String[] toStrings() {
			List<String> stati = new ArrayList<String>();
			for (StatoDTO s : values())
				if (!s.equals(StatoDTO.DA_INOLTRARE_ESTERNO))
					stati.add(s.name());
			return stati.toArray(new String[stati.size()]);
		}

		public static StatoDTO fromLabel(String label) {
			for (StatoDTO dto : values()) {
				if (dto.getLabel().equals(label))
					return dto;
			}
			return null;
		}
	}

	@Getter
	private List<ProtocollazioneComposizione> composizioneProtocollazioni = new LinkedList<>();
	@Getter
	private List<EmailComposizione> composizioneEmail = new LinkedList<>();
	@Getter
	private List<AllegatoComposizione> composizioneAllegati = new LinkedList<>();

	/* dati applicativi */
	private String numeroFascicolo, titoloOriginale;
	private List<ElementoElenco> elenco = new LinkedList<ElementoElenco>();
	private StatoDTO stato;
	private List<DatoAggiuntivo> valoriDatiAggiuntivi = new ArrayList<DatoAggiuntivo>();
	private List<String> operazioni = new ArrayList<String>();
	private List<CollegamentoDto> collegamenti = new ArrayList<CollegamentoDto>();
	private List<CondivisioneDto> condivisioni = new ArrayList<CondivisioneDto>();
	private List<ProcedimentoDto> procedimenti = new ArrayList<ProcedimentoDto>();
	private StepIter stepIter;

	@Getter
	private List<String> idPraticheProcedi = new ArrayList<String>();

	@Getter
	@Setter
	private boolean modificaTagAbilitata;

	@Getter
	@Setter
	private boolean spostaAllegati;

	@Getter
	@Setter
	private boolean accessoVersioniPrecedentiAllegati;

	@Getter
	@Setter
	private boolean spostaProtocollazioni;

	@Getter
	@Setter
	private boolean inviaDaCsvAbilitato;

	@Getter
	@Setter
	private boolean creaBozzaAbilitato;

	/*
	 * dati relativi all'abilitazione dei pulsanti che dipendono dall'attivazione del task
	 */
	private boolean riassegna, protocolla, concludi, rispondi, caricaAllegato, firmaAllegato, cancellaAllegato, isRiportaInGestioneAbilitato, isEliminaFascicoloAbilitato, affissioneAbilitato,
			modificaVisibilitaFascicoloAbilitato, modificaVisibilitaAllegatoAbilitato, pubblicazioneAbilitata, rimozionePubblicazioneAbilitata, collegamentoAbilitato, rimozioneCollegamentoAbilitato,
			condivisioneAbilitata, rimozioneCondivisioneAbilitata, avviaProcedimento, propostaFirmaEnabled, chiudiProcedimento, sganciaPecIn, cambiaTipologiaFascicolo, riportaInLettura,
			assegnaUtenteEsterno, modificaAbilitazioniAssegnaUtenteEsterno, ritornaDaInoltrareEsterno, chiudiAbilitato, nuovaEmailDaTemplate, cambiaStepIter, modificaOperatore, modificaDatiAggiuntivi,
			ritiroPropostaFirmaEnabled, nuovoPdfDaTemplate, aggiornaPG, estraiEML;

	private boolean modificaFascicolo, collegaPraticaProcedi, emissionePermesso;

	private boolean isCollegamentoVisitabile = true;

	public boolean isEstraiEML() {
		return estraiEML;
	}

	public void setEstraiEML(boolean estraiEML) {
		this.estraiEML = estraiEML;
	}

	public boolean isNuovoPdfDaTemplate() {
		return nuovoPdfDaTemplate;
	}

	public void setNuovoPdfDaTemplate(boolean nuovoPdfDaTemplate) {
		this.nuovoPdfDaTemplate = nuovoPdfDaTemplate;
	}

	public boolean isChiudiAbilitato() {
		return chiudiAbilitato;
	}

	public void setChiudiAbilitato(boolean chiudiAbilitato) {
		this.chiudiAbilitato = chiudiAbilitato;
	}

	public boolean isSganciaPecIn() {
		return sganciaPecIn;
	}

	public void setSganciaPecIn(boolean sganciaPecIn) {
		this.sganciaPecIn = sganciaPecIn;
	}

	public boolean isCancellaAllegato() {
		return cancellaAllegato;
	}

	public List<ElementoElenco> getElenco() {
		return elenco;
	}

	public void setCancellaAllegato(boolean cancellaAllegato) {
		this.cancellaAllegato = cancellaAllegato;
	}

	protected FascicoloDTO() {

	}

	public FascicoloDTO(String alfrescoPath) {
		super(alfrescoPath);
	}

	public boolean isRiassegna() {
		return riassegna;
	}

	public void setRiassegna(boolean riassegna) {
		this.riassegna = riassegna;
	}

	public boolean isProtocolla() {
		return protocolla;
	}

	public void setProtocolla(boolean protocolla) {
		this.protocolla = protocolla;
	}

	public boolean isConcludi() {
		return concludi;
	}

	public void setConcludi(boolean concludi) {
		this.concludi = concludi;
	}

	public boolean isRispondi() {
		return rispondi;
	}

	public void setRispondi(boolean rispondi) {
		this.rispondi = rispondi;
	}

	public boolean isCaricaAllegato() {
		return caricaAllegato;
	}

	public void setCaricaAllegato(boolean caricaAllegato) {
		this.caricaAllegato = caricaAllegato;
	}

	public boolean isFirmaAllegato() {
		return firmaAllegato;
	}

	public void setFirmaAllegato(boolean firmaAllegato) {
		this.firmaAllegato = firmaAllegato;
	}

	public StatoDTO getStato() {
		return stato;
	}

	public void setStato(StatoDTO stato) {
		this.stato = stato;
	}

	public boolean isRiportaInGestioneAbilitato() {
		return isRiportaInGestioneAbilitato;
	}

	public void setRiportaInGestioneAbilitato(boolean isRiportaInGestioneAbilitato) {
		this.isRiportaInGestioneAbilitato = isRiportaInGestioneAbilitato;
	}

	public boolean isEliminaFascicoloAbilitato() {
		return isEliminaFascicoloAbilitato;
	}

	public void setEliminaFascicoloAbilitato(boolean isEliminaFascicoloAbilitato) {
		this.isEliminaFascicoloAbilitato = isEliminaFascicoloAbilitato;
	}

	public boolean isAffissioneAbilitato() {
		return affissioneAbilitato;
	}

	public void setAffissioneAbilitato(boolean affissioneAbilitato) {
		this.affissioneAbilitato = affissioneAbilitato;
	}

	public String getNumeroFascicolo() {
		return numeroFascicolo;
	}

	public void setNumeroFascicolo(String numeroFascicolo) {
		this.numeroFascicolo = numeroFascicolo;
	}

	public boolean isModificaVisibilitaFascicoloAbilitato() {
		return modificaVisibilitaFascicoloAbilitato;
	}

	public void setModificaVisibilitaFascicoloAbilitato(boolean modificaVisibilitaFascicoloAbilitato) {
		this.modificaVisibilitaFascicoloAbilitato = modificaVisibilitaFascicoloAbilitato;
	}

	public boolean isModificaVisibilitaAllegatoAbilitato() {
		return modificaVisibilitaAllegatoAbilitato;
	}

	public void setModificaVisibilitaAllegatoAbilitato(boolean modificaVisibilitaAllegatoAbilitato) {
		this.modificaVisibilitaAllegatoAbilitato = modificaVisibilitaAllegatoAbilitato;
	}

	public boolean isPubblicazioneAbilitata() {
		return pubblicazioneAbilitata;
	}

	public void setPubblicazioneAbilitata(boolean pubblicazioneAbilitata) {
		this.pubblicazioneAbilitata = pubblicazioneAbilitata;
	}

	public boolean isRimozionePubblicazioneAbilitata() {
		return rimozionePubblicazioneAbilitata;
	}

	public void setRimozionePubblicazioneAbilitata(boolean rimozionePubblicazioneAbilitata) {
		this.rimozionePubblicazioneAbilitata = rimozionePubblicazioneAbilitata;
	}

	public List<DatoAggiuntivo> getValoriDatiAggiuntivi() {
		return valoriDatiAggiuntivi;
	}

	public void setValoriDatiAggiuntivi(List<DatoAggiuntivo> valoriDatiAggiuntivi) {
		this.valoriDatiAggiuntivi = valoriDatiAggiuntivi;
	}

	public boolean isCollegamentoAbilitato() {
		return collegamentoAbilitato;
	}

	public void setCollegamentoAbilitato(boolean collegamentoAbilitato) {
		this.collegamentoAbilitato = collegamentoAbilitato;
	}

	public boolean isRimozioneCollegamentoAbilitato() {
		return rimozioneCollegamentoAbilitato;
	}

	public void setRimozioneCollegamentoAbilitato(boolean rimozioneCollegamentoAbilitato) {
		this.rimozioneCollegamentoAbilitato = rimozioneCollegamentoAbilitato;
	}

	public boolean isCondivisioneAbilitata() {
		return condivisioneAbilitata;
	}

	public void setCondivisioneAbilitata(boolean condivisioneAbilitata) {
		this.condivisioneAbilitata = condivisioneAbilitata;
	}

	public boolean isRimozioneCondivisioneAbilitata() {
		return rimozioneCondivisioneAbilitata;
	}

	public void setRimozioneCondivisioneAbilitata(boolean rimozioneCondivisioneAbilitata) {
		this.rimozioneCondivisioneAbilitata = rimozioneCondivisioneAbilitata;
	}

	public boolean isAvviaProcedimento() {
		return avviaProcedimento;
	}

	public void setAvviaProcedimento(boolean avviaProcedimento) {
		this.avviaProcedimento = avviaProcedimento;
	}

	public boolean isPropostaFirmaAbilitato() {
		return propostaFirmaEnabled;
	}

	public void setPropostaFirmaAbilitato(boolean inviaInApprovazioneConFirmaAbilitato) {
		this.propostaFirmaEnabled = inviaInApprovazioneConFirmaAbilitato;
	}

	public boolean isChiudiProcedimento() {
		return chiudiProcedimento;
	}

	public void setChiudiProcedimento(boolean chiudiProcedimento) {
		this.chiudiProcedimento = chiudiProcedimento;
	}

	public boolean isRiportaInLettura() {
		return riportaInLettura;
	}

	public void setRiportaInLettura(boolean riportaInLettura) {
		this.riportaInLettura = riportaInLettura;
	}

	public List<String> getOperazioni() {
		return operazioni;
	}

	public List<CollegamentoDto> getCollegamenti() {
		return collegamenti;
	}

	public List<CondivisioneDto> getCondivisioni() {
		return condivisioni;
	}

	public List<ProcedimentoDto> getProcedimenti() {
		return procedimenti;
	}

	public boolean isCambiaTipologiaFascicolo() {
		return cambiaTipologiaFascicolo;
	}

	public void setCambiaTipologiaFascicolo(boolean cambiaTipologiaFascicolo) {
		this.cambiaTipologiaFascicolo = cambiaTipologiaFascicolo;
	}

	public boolean isAssegnaUtenteEsterno() {
		return assegnaUtenteEsterno;
	}

	public void setAssegnaUtenteEsterno(boolean assegnaUtenteEsterno) {
		this.assegnaUtenteEsterno = assegnaUtenteEsterno;
	}

	public boolean isModificaAbilitazioniAssegnaUtenteEsterno() {
		return modificaAbilitazioniAssegnaUtenteEsterno;
	}

	public void setModificaAbilitazioniAssegnaUtenteEsterno(boolean modificaAbilitazioniAssegnaUtenteEsterno) {
		this.modificaAbilitazioniAssegnaUtenteEsterno = modificaAbilitazioniAssegnaUtenteEsterno;
	}

	public boolean isRitornaDaInoltrareEsterno() {
		return ritornaDaInoltrareEsterno;
	}

	public void setRitornaDaInoltrareEsterno(boolean ritornaDaInoltrareEsterno) {
		this.ritornaDaInoltrareEsterno = ritornaDaInoltrareEsterno;
	}

	public boolean isNuovaEmailDaTemplate() {
		return nuovaEmailDaTemplate;
	}

	public void setNuovaEmailDaTemplate(boolean nuovaEmailDaTemplate) {
		this.nuovaEmailDaTemplate = nuovaEmailDaTemplate;
	}

	public StepIter getStepIter() {
		return stepIter;
	}

	public void setStepIter(StepIter stepIter) {
		this.stepIter = stepIter;
	}

	public boolean isCambiaStepIter() {
		return cambiaStepIter;
	}

	public void setCambiaStepIter(boolean cambiaStepIter) {
		this.cambiaStepIter = cambiaStepIter;
	}

	public boolean isModificaOperatore() {
		return modificaOperatore;
	}

	public void setModificaOperatore(boolean modificaOperatore) {
		this.modificaOperatore = modificaOperatore;
	}

	public boolean isModificaDatiAggiuntivi() {
		return modificaDatiAggiuntivi;
	}

	public void setModificaDatiAggiuntivi(boolean modificaDatiAggiuntivi) {
		this.modificaDatiAggiuntivi = modificaDatiAggiuntivi;
	}

	public boolean isRitiroPropostaFirmaEnabled() {
		return ritiroPropostaFirmaEnabled;
	}

	public void setRitiroPropostaFirmaEnabled(boolean propostaFirmaEnabled) {
		this.ritiroPropostaFirmaEnabled = propostaFirmaEnabled;
	}

	public boolean isAggiornaPG() {
		return aggiornaPG;
	}

	public void setAggiornaPG(boolean aggiornaPG) {
		this.aggiornaPG = aggiornaPG;
	}

	public boolean isModificaFascicolo() {
		return modificaFascicolo;
	}

	public void setModificaFascicolo(boolean modificaFascicolo) {
		this.modificaFascicolo = modificaFascicolo;
	}

	public String getTitoloOriginale() {
		return titoloOriginale;
	}

	public void setTitoloOriginale(String titoloOriginale) {
		this.titoloOriginale = titoloOriginale;
	}

	public boolean isCollegaPraticaProcedi() {
		return collegaPraticaProcedi;
	}

	public void setCollegaPraticaProcedi(boolean collegaPraticaProcedi) {
		this.collegaPraticaProcedi = collegaPraticaProcedi;
	}

	public boolean isEmissionePermesso() {
		return emissionePermesso;
	}

	public void setEmissionePermesso(boolean emissionePermesso) {
		this.emissionePermesso = emissionePermesso;
	}

	public boolean isCollegamentoVisitabile() {
		return isCollegamentoVisitabile;
	}

	public void setCollegamentoVisitabile(boolean isCollegamentoVisitabile) {
		this.isCollegamentoVisitabile = isCollegamentoVisitabile;
	}

	/* beans di descrizione dell'elenco documenti inseriti nel fascicolo */
	public static interface ElementoElencoVisitor extends IsSerializable {
		public void visit(ElementoGruppo gruppo);

		public void visit(ElementoGruppoProtocollatoCapofila capofila);

		public void visit(ElementoGruppoProtocollato subProt);

		public void visit(ElementoGrupppoNonProtocollato nonProt);

		public void visit(ElementoAllegato allegato);

		public void visit(ElementoPECRiferimento pec);

		public void visit(ElementoComunicazioneRiferimento elementoComunicazioneRiferimento);

		public void visit(ElementoPraticaModulisticaRiferimento elementoPraticaModulisticaRiferimento);

	}

	public static abstract class ElementoElencoVisitorAdapter implements ElementoElencoVisitor {
		@Override
		public void visit(ElementoAllegato allegato) {/**/}

		@Override
		public void visit(ElementoPECRiferimento pec) {/**/}

		@Override
		public void visit(ElementoGruppo gruppo) {/**/}

		@Override
		public void visit(ElementoGruppoProtocollatoCapofila capofila) {/**/}

		@Override
		public void visit(ElementoGruppoProtocollato subProt) {/**/}

		@Override
		public void visit(ElementoGrupppoNonProtocollato nonProt) {/**/}

		@Override
		public void visit(ElementoPraticaModulisticaRiferimento elementoPraticaModulisticaRiferimento) {/**/}

		@Override
		public void visit(ElementoComunicazioneRiferimento elementoComunicazioneRiferimento) {/**/}
	}

	/**
	 * Le implementazioni devono definire opportunamente hashcode ed equals
	 *
	 * @author pluttero
	 *
	 */
	public interface ElementoElenco extends IsSerializable {
		public void accept(ElementoElencoVisitor visitor);
	}

	/**
	 * gruppo di elementi non protocollato
	 *
	 * @author pluttero
	 */
	public static abstract class ElementoGruppo implements ElementoElenco {
		protected LinkedHashSet<ElementoElenco> elementi = new LinkedHashSet<FascicoloDTO.ElementoElenco>();

		public Set<ElementoElenco> getElementi() {
			return elementi;
		}

		public ElementoGruppo() {}

		public void addAllegato(ElementoAllegato allegato) {
			elementi.add(allegato);
		}

		public void addRiferimentoPEC(ElementoPECRiferimento pec) {
			elementi.add(pec);
		}

		public void addRiferimentoPraticaModulistica(ElementoPraticaModulisticaRiferimento pm) {
			elementi.add(pm);
		}

		public void addRiferimentoComunicazione(ElementoComunicazioneRiferimento c) {
			elementi.add(c);
		}

		@Override
		public void accept(ElementoElencoVisitor visitor) {
			visitor.visit(this);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ElementoGruppo) {
				return ((ElementoGruppo) obj).getElementi().equals(getElementi());
			}
			return false;
		}

		@Override
		public int hashCode() {
			return getElementi().hashCode();
		}
	}

	public static class ElementoAllegato extends AllegatoDTO implements ElementoElenco {
		public ElementoAllegato() {}

		public ElementoAllegato(String nome, String folderOriginPath, String folderOriginName, String clientID, String versione) {
			super(nome, folderOriginPath, folderOriginName, clientID, versione);
		}

		@Override
		public void accept(ElementoElencoVisitor visitor) {
			visitor.visit(this);
		}
	}

	public static class ElementoPECRiferimento implements ElementoElenco {

		public String riferimento;
		public TipoRiferimentoPEC tipo;
		public String fromEpro;
		private Date dataPec;

		public TipoRiferimentoPEC getTipo() {
			return tipo;
		}

		public void setTipo(TipoRiferimentoPEC tipo) {
			this.tipo = tipo;
		}

		public String getRiferimento() {
			return riferimento;
		}

		public void setRiferimento(String riferimento) {
			this.riferimento = riferimento;
		}

		public Date getDataPec() {
			return dataPec;
		}

		@SuppressWarnings("unused")
		private ElementoPECRiferimento() {}

		public ElementoPECRiferimento(String path, TipoRiferimentoPEC tipo, Date dataPec) {
			this.riferimento = path;
			this.tipo = tipo;
			this.dataPec = dataPec;
		}

		@Override
		public void accept(ElementoElencoVisitor visitor) {
			visitor.visit(this);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ElementoPECRiferimento)
				return ((ElementoPECRiferimento) obj).getRiferimento().equals(getRiferimento());
			return false;
		}

		@Override
		public int hashCode() {
			return riferimento.hashCode();
		}
	}

	public static class ElementoPraticaModulisticaRiferimento implements ElementoElenco {

		public String riferimento;
		public Date dataCaricamento;

		@SuppressWarnings("unused")
		private ElementoPraticaModulisticaRiferimento() {}

		public ElementoPraticaModulisticaRiferimento(String riferimento, Date dataCaricamento) {
			super();
			this.riferimento = riferimento;
			this.dataCaricamento = dataCaricamento;
		}

		public String getRiferimento() {
			return riferimento;
		}

		public void setRiferimento(String riferimento) {
			this.riferimento = riferimento;
		}

		public Date getDataCaricamento() {
			return dataCaricamento;
		}

		@Override
		public void accept(ElementoElencoVisitor visitor) {
			visitor.visit(this);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ElementoPECRiferimento)
				return ((ElementoPECRiferimento) obj).getRiferimento().equals(getRiferimento());
			return false;
		}

		@Override
		public int hashCode() {
			return riferimento.hashCode();
		}
	}

	public static class ElementoComunicazioneRiferimento implements ElementoElenco {

		public String riferimento;
		public Date dataCaricamento;

		@SuppressWarnings("unused")
		private ElementoComunicazioneRiferimento() {}

		public ElementoComunicazioneRiferimento(String riferimento, Date dataCaricamento) {
			super();
			this.riferimento = riferimento;
			this.dataCaricamento = dataCaricamento;
		}

		public String getRiferimento() {
			return riferimento;
		}

		public void setRiferimento(String riferimento) {
			this.riferimento = riferimento;
		}

		public Date getDataCaricamento() {
			return dataCaricamento;
		}

		@Override
		public void accept(ElementoElencoVisitor visitor) {
			visitor.visit(this);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ElementoPECRiferimento)
				return ((ElementoPECRiferimento) obj).getRiferimento().equals(getRiferimento());
			return false;
		}

		@Override
		public int hashCode() {
			return riferimento.hashCode();
		}
	}

	public static class ElementoGrupppoNonProtocollato extends ElementoGruppo {
		public ElementoGrupppoNonProtocollato() {

		}

		@Override
		public void accept(ElementoElencoVisitor visitor) {
			visitor.visit(this);
		}
	}

	public static class ElementoGruppoProtocollato extends ElementoGruppo implements Comparable<ElementoGruppoProtocollato> {

		public ElementoGruppoProtocollato() {}

		private String numeroPG, annoPG;
		private String numeroPGCapofila, annoPGCapofila;
		private String idTitolo;
		private String idRubrica;
		private String idSezione;
		private String tipologiaDocumento;
		private String oggetto;
		private Date dataProtocollazione;

		public String getAnnoPG() {
			return annoPG;
		}

		public void setAnnoPG(String annoPG) {
			this.annoPG = annoPG;
		}

		public String getNumeroPG() {
			return numeroPG;
		}

		public void setNumeroPG(String numeroPG) {
			this.numeroPG = numeroPG;
		}

		public void setIdTitolo(String idTitolo) {
			this.idTitolo = idTitolo;
		}

		public void setIdRubrica(String idRubrica) {
			this.idRubrica = idRubrica;
		}

		public void setIdSezione(String idSezione) {
			this.idSezione = idSezione;
		}

		public String getIdTitolo() {
			return idTitolo;
		}

		public String getIdRubrica() {
			return idRubrica;
		}

		public String getIdSezione() {
			return idSezione;
		}

		public String getOggetto() {
			return oggetto;
		}

		public void setOggetto(String oggetto) {
			this.oggetto = oggetto;
		}

		public String getTipologiaDocumento() {
			return tipologiaDocumento;
		}

		public void setTipologiaDocumento(String tipologiaDocumento) {
			this.tipologiaDocumento = tipologiaDocumento;
		}

		public Date getDataProtocollazione() {
			return dataProtocollazione;
		}

		public void setDataProtocollazione(Date dataProtocollazione) {
			this.dataProtocollazione = dataProtocollazione;
		}

		@Override
		public void accept(ElementoElencoVisitor visitor) {
			visitor.visit(this);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ElementoGruppoProtocollato) {
				ElementoGruppoProtocollato other = (ElementoGruppoProtocollato) obj;
				return (numeroPG + annoPG).equals(other.getNumeroPG() + other.getAnnoPG()) && getElementi().equals(other.getElementi());
			}
			return false;
		}

		@Override
		public int hashCode() {
			return (numeroPG + annoPG).hashCode() + getElementi().hashCode();
		}

		public String getNumeroPGCapofila() {
			return numeroPGCapofila;
		}

		public void setNumeroPGCapofila(String numeroPGCapofila) {
			this.numeroPGCapofila = numeroPGCapofila;
		}

		public String getAnnoPGCapofila() {
			return annoPGCapofila;
		}

		public void setAnnoPGCapofila(String annoPGCapofila) {
			this.annoPGCapofila = annoPGCapofila;
		}

		@Override
		public int compareTo(ElementoGruppoProtocollato o) {
			return o.getDataProtocollazione().compareTo(getDataProtocollazione());
		}

	}

	/**
	 * gruppo capofila di elementi protocollati
	 *
	 * @author pluttero
	 *
	 */

	public static class ElementoGruppoProtocollatoCapofila extends ElementoGruppoProtocollato {

		// private String idTitolo;
		// private String idRubrica;
		// private String idSezione;
		// private String tipologiaDocumento;
		// private String oggetto;
		// private Date dataProtocollazione;

		public ElementoGruppoProtocollatoCapofila() {}

		public void addElementoGruppoProtocollato(ElementoGruppoProtocollato subProt) {
			elementi.add(subProt);
		}

		@Override
		public void accept(ElementoElencoVisitor visitor) {
			visitor.visit(this);
		}

		@Override
		public int compareTo(ElementoGruppoProtocollato o) {
			if (o instanceof ElementoGruppoProtocollatoCapofila)
				return o.getDataProtocollazione().compareTo(getDataProtocollazione());
			throw new IllegalArgumentException();
		}

	}

}
