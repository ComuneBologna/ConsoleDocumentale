package it.eng.portlet.consolepec.gwt.shared.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.IsSerializable;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.ProponenteDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.StatoTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.TipoPropostaTaskFirmaDTO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AllegatoDTO implements IsSerializable, Comparable<AllegatoDTO> {

	public enum Stato {
		FIRMATO("Firmato con firma valida"), NONFIRMATO("Non firmato"), FIRMANONVALIDA("Firmato con firma NON valida");

		private String label;

		Stato(String l) {
			this.label = l;
		}

		public String getLabel() {
			return label;
		}
	}

	public enum TipologiaFirma {
		NONFIRMATO(" - "), CADES("CAdES - p7m"), PADES("PAdES - pdf"), XADES("XAdES - xml");

		private String label;

		TipologiaFirma(String l) {
			this.label = l;
		}

		public String getLabel() {
			return label;
		}

		public static TipologiaFirma fromLabel(String label) {
			for (TipologiaFirma t : values()) {
				if (t.getLabel().equalsIgnoreCase(label)) {
					return t;
				}
			}
			return null;
		}

		public static TipologiaFirma getTipologiaFirma(String tipologia) {
			for (TipologiaFirma t : values()) {
				if (t.name().equalsIgnoreCase(tipologia)) {
					return t;
				}
			}
			return null;
		}

	}

	private Stato stato = Stato.FIRMATO;
	private TipologiaFirma tipologiaFirma = TipologiaFirma.NONFIRMATO;
	private String nome, versioneCorrente, praticaID;
	@Getter
	@Setter
	private String folderOriginPath, folderOriginName;
	private boolean pubblicato;
	private Date dataInizioPubblicazione, dataFinePubblicazione;
	private TreeSet<GruppoVisibilita> visibilita = new TreeSet<GruppoVisibilita>();
	private boolean firmatoHash;
	private boolean protocollato;
	private Date dataCaricamento;
	private List<String> tipologiaAllegato = new ArrayList<>();
	private Boolean lock;
	private Integer lockedBy;
	private boolean scaricaReportComunicazioneAbilitato, invioComunicazioneAbilitato;
	private String oggetto;
	private TreeSet<StoricoVersioniDTO> storicoVersioni = new TreeSet<StoricoVersioniDTO>();
	private boolean isInTaskFirma;
	private List<DatoAggiuntivo> datiAggiuntivi = new ArrayList<>();

	public AllegatoDTO() {
		// sincronizzazione
	}

	/**
	 * Campi sempre presenti ed obbligatori.
	 *
	 * @param nome
	 * @param clientID
	 * @param versione
	 */
	public AllegatoDTO(String nome, String folderOriginPath, String folderOriginName, String clientID, String versione) {
		this.nome = nome;
		this.folderOriginPath = folderOriginPath;
		this.folderOriginName = folderOriginName;
		this.praticaID = clientID;
		this.versioneCorrente = versione;
	}

	public Stato getStato() {
		return stato;
	}

	public ImageResource getIconaStato(ConsolePECIcons images) {
		switch (getStato()) {
		case FIRMATO:
			return images.firmato();
		case FIRMANONVALIDA:
			return images.firmanonvalida();
		case NONFIRMATO:
			return images.nonfirmato();
		}
		return null;
	}

	public TipologiaFirma getTipologiaFirma() {
		return tipologiaFirma;
	}

	public void setTipologiaFirma(TipologiaFirma tipologiaFirma) {
		this.tipologiaFirma = tipologiaFirma;
	}

	public void setStato(Stato s) {
		this.stato = s;
	}

	public String getNome() {
		return nome;
	}

	public String getVersioneCorrente() {
		return versioneCorrente;
	}

	public void setVersioneCorrente(String versioneCorrente) {
		this.versioneCorrente = versioneCorrente;
	}

	public String getLabel() {
		if (this.folderOriginName != null && !this.folderOriginName.isEmpty() && this.folderOriginPath != null && !this.folderOriginPath.isEmpty()) {
			if (getVersioneCorrente() != null) {
				return this.folderOriginName + " V. " + getVersioneCorrente();
			}
			return this.folderOriginName;
		}

		if (getVersioneCorrente() != null) {
			return getNome() + " V. " + getVersioneCorrente();
		}

		return getNome();
	}

	public String getClientID() {
		return praticaID;
	}

	public void setClientID(String clientID) {
		this.praticaID = clientID;
	}

	public boolean isPubblicato() {
		return pubblicato;
	}

	public void setPubblicato(boolean pubblicato) {
		this.pubblicato = pubblicato;
	}

	public Date getDataInizioPubblicazione() {
		return dataInizioPubblicazione;
	}

	public void setDataInizioPubblicazione(Date dataInizioPubblicazione) {
		this.dataInizioPubblicazione = dataInizioPubblicazione;
	}

	public Date getDataFinePubblicazione() {
		return dataFinePubblicazione;
	}

	public void setDataFinePubblicazione(Date dataFinePubblicazione) {
		this.dataFinePubblicazione = dataFinePubblicazione;
	}

	public TreeSet<GruppoVisibilita> getVisibilita() {
		return visibilita;
	}

	public boolean isFirmatoHash() {
		return firmatoHash;
	}

	public void setFirmatoHash(boolean firmatoHash) {
		this.firmatoHash = firmatoHash;
	}

	public boolean isFirmato() {
		return !TipologiaFirma.NONFIRMATO.equals(tipologiaFirma);
	}

	public List<String> getTipologiaAllegato() {
		return tipologiaAllegato;
	}

	public void setTipologiaAllegato(List<String> tipologiaAllegato) {
		this.tipologiaAllegato = tipologiaAllegato;
	}

	public Date getDataCaricamento() {
		return dataCaricamento;
	}

	public void setDataCaricamento(Date dataCaricamento) {
		this.dataCaricamento = dataCaricamento;
	}

	@Override
	/**
	 * L'equality deve essere garantita da pratica-nome-versione. Gli altri parametri devono corrispondere per definizione
	 */
	public boolean equals(Object obj) {
		if (obj instanceof AllegatoDTO) {
			AllegatoDTO allg = (AllegatoDTO) obj;

			boolean nomeAllegato = allg.getNome().equals(getNome());
			boolean versione = (allg.getVersioneCorrente() == null && versioneCorrente == null)
					|| ((allg.getVersioneCorrente() != null && versioneCorrente != null) && allg.getVersioneCorrente().equals(versioneCorrente));
			boolean clientID = allg.getClientID().equals(praticaID);

			return nomeAllegato && versione && clientID;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (getClientID() + getNome() + getVersioneCorrente()).hashCode();
	}

	@Override
	public String toString() {
		return nome;
	}

	public boolean isProtocollato() {
		return protocollato;
	}

	public void setProtocollato(boolean protocollato) {
		this.protocollato = protocollato;
	}

	public boolean isScaricaReportComunicazioneAbilitato() {
		return scaricaReportComunicazioneAbilitato;
	}

	public boolean isInvioComunicazioneAbilitato() {
		return invioComunicazioneAbilitato;
	}

	public void setScaricaReportComunicazioneAbilitato(boolean scaricaReportComunicazioneAbilitato) {
		this.scaricaReportComunicazioneAbilitato = scaricaReportComunicazioneAbilitato;
	}

	public void setInvioComunicazioneAbilitato(boolean invioComunicazioneAbilitato) {
		this.invioComunicazioneAbilitato = invioComunicazioneAbilitato;
	}

	@Override
	public int compareTo(AllegatoDTO o) {
		return o.dataCaricamento.compareTo(dataCaricamento);
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public Boolean isLock() {
		return lock;
	}

	public void setLock(Boolean lock) {
		this.lock = lock;
	}

	public Integer getLockedBy() {
		return lockedBy;
	}

	public void setLockedBy(Integer lockedBy) {
		this.lockedBy = lockedBy;
	}

	public Set<StoricoVersioniDTO> getStoricoVersioni() {
		return storicoVersioni;
	}

	public List<DatoAggiuntivo> getDatiAggiuntivi() {
		return datiAggiuntivi;
	}

	/*
	 * Storico Versioni
	 */

	public boolean isInTaskFirma() {
		return isInTaskFirma;
	}

	public void setInTaskFirma(boolean isInTaskFirma) {
		this.isInTaskFirma = isInTaskFirma;
	}

	public static class StoricoVersioniDTO implements IsSerializable, Comparable<StoricoVersioniDTO> {

		private String versione;
		private String utente;
		private InformazioniTaskFirmaDTO informazioniTaskFirma;
		private InformazioniCopiaDTO informazioniCopia;

		protected StoricoVersioniDTO() {

		}

		public StoricoVersioniDTO(String versione) {
			this.versione = versione;
		}

		public String getVersione() {
			return versione;
		}

		public void setVersione(String versione) {
			this.versione = versione;
		}

		public InformazioniTaskFirmaDTO getInformazioniTaskFirma() {
			return informazioniTaskFirma;
		}

		public void setInformazioniTaskFirma(InformazioniTaskFirmaDTO informazioniTaskFirma) {
			this.informazioniTaskFirma = informazioniTaskFirma;
		}

		@Override
		public int compareTo(StoricoVersioniDTO o) {
			return versione.compareTo(o.getVersione());
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((versione == null) ? 0 : versione.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			StoricoVersioniDTO other = (StoricoVersioniDTO) obj;
			if (versione == null) {
				if (other.versione != null) {
					return false;
				}
			} else if (!versione.equals(other.versione)) {
				return false;
			}
			return true;
		}

		public String getUtente() {
			return utente;
		}

		public void setUtente(String utente) {
			this.utente = utente;
		}

		public InformazioniCopiaDTO getInformazioniCopia() {
			return informazioniCopia;
		}

		public void setInformazioniCopia(InformazioniCopiaDTO informazioniCopia) {
			this.informazioniCopia = informazioniCopia;
		}

		public static class InformazioniTaskFirmaDTO implements IsSerializable {
			private String oggettoProposta;
			private ProponenteDTO proponente;
			private StatoTaskFirmaDTO operazioneEffettuata;
			private StatoTaskFirmaDTO statoRichiesta;
			private TipoPropostaTaskFirmaDTO tipoRichiesta;
			private List<DestinatarioDTO> destinatari = new ArrayList<DestinatarioDTO>();
			private String mittenteOriginale;
			private String motivazione;
			private String dataScadenza;

			protected InformazioniTaskFirmaDTO() {}

			public InformazioniTaskFirmaDTO(String oggettoProposta, ProponenteDTO proponente, TipoPropostaTaskFirmaDTO tipoRichiesta, List<DestinatarioDTO> destinatari,
					StatoTaskFirmaDTO operazioneEffettuata, String mittenteOriginale, String dataScadenza, StatoTaskFirmaDTO statoRichiesta, String motivazione) {
				this.oggettoProposta = oggettoProposta;
				this.proponente = proponente;
				this.tipoRichiesta = tipoRichiesta;
				this.operazioneEffettuata = operazioneEffettuata;
				this.statoRichiesta = statoRichiesta;
				this.destinatari = destinatari;
				this.mittenteOriginale = mittenteOriginale;
				this.dataScadenza = dataScadenza;
				this.motivazione = motivazione;
			}

			public ProponenteDTO getProponente() {
				return proponente;
			}

			public void setProponente(ProponenteDTO proponente) {
				this.proponente = proponente;
			}

			public TipoPropostaTaskFirmaDTO getTipoRichiesta() {
				return tipoRichiesta;
			}

			public void setTipoRichiesta(TipoPropostaTaskFirmaDTO tipoRichiesta) {
				this.tipoRichiesta = tipoRichiesta;
			}

			public StatoTaskFirmaDTO getOperazioneEffettuata() {
				return operazioneEffettuata;
			}

			public void setOperazioneEffettuata(StatoTaskFirmaDTO operazioneEffettuata) {
				this.operazioneEffettuata = operazioneEffettuata;
			}

			public String getOggettoProposta() {
				return oggettoProposta;
			}

			public void setOggettoProposta(String oggettoProposta) {
				this.oggettoProposta = oggettoProposta;
			}

			public List<DestinatarioDTO> getDestinatari() {
				return destinatari;
			}

			public String getMittenteOriginale() {
				return mittenteOriginale;
			}

			public void setMittenteOriginale(String mittenteOriginale) {
				this.mittenteOriginale = mittenteOriginale;
			}

			public String getDataScadenza() {
				return dataScadenza;
			}

			public void setDataScadenza(String dataScadenza) {
				this.dataScadenza = dataScadenza;
			}

			public StatoTaskFirmaDTO getStatoRichiesta() {
				return statoRichiesta;
			}

			public void setStatoRichiesta(StatoTaskFirmaDTO statoRichiesta) {
				this.statoRichiesta = statoRichiesta;
			}

			public String getMotivazione() {
				return motivazione;
			}

			public void setMotivazione(String motivazione) {
				this.motivazione = motivazione;
			}

		}

		@Data
		@NoArgsConstructor(access = AccessLevel.PRIVATE)
		public static class InformazioniCopiaDTO implements IsSerializable {

			public InformazioniCopiaDTO(String idDocumentaleSorgente) {
				this.idDocumentaleSorgente = idDocumentaleSorgente;
			}

			private String idDocumentaleSorgente;
			private Map<String, InformazioniTaskFirmaDTO> informazioniTaskFirma = new TreeMap<String, InformazioniTaskFirmaDTO>();
		}
	}
}
