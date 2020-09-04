package it.eng.portlet.consolepec.gwt.shared.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.user.client.rpc.IsSerializable;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.pratica.OperativitaRidotta;
import lombok.Getter;
import lombok.Setter;

public class PraticaDTO implements IsSerializable, Comparable<PraticaDTO> {

	public enum TipoPresaInCarico {
		NESSUNO("Non in carico a nessuno"), UTENTE_CORRENTE("In carico a me"), ALTRO_UTENTE("In carico ad altri");

		TipoPresaInCarico(String desc) {
			this.desc = desc;
		}

		private String desc;

		public String getDesc() {
			return desc;
		}

	};

	/* dati generici di una pratica in worklist */
	private String annoPG, numeroPG, annoPGCapofila, numeroPGCapofila, titolo, provenienza, dataOraCreazione, utenteCreazione, usernameCreazione, assegnatario, numeroRepertorio, clientID,
			inCaricoALabel, visibileA, inCaricoAUserName;
	private TipoPresaInCarico tipoPresaInCarico;
	private List<AllegatoDTO> allegati = new ArrayList<AllegatoDTO>();
	private boolean isUtenteAssegnatario = false;
	private String note;

	/* dato generici tecnici */
	private boolean letto, gestionePresaInCarico;
	private String statoLabel;
	private TreeSet<GruppoVisibilita> visibilita = new TreeSet<GruppoVisibilita>();
	private List<EventoIterDTO> eventiIterDTO = new LinkedList<EventoIterDTO>();
	private AssegnazioneEsternaDTO assegnazioneEsterna;
	private List<String> operazioniAssegnaUtenteEsterno = new ArrayList<String>();
	private String operatore;

	@Getter
	@Setter
	private OperativitaRidotta operativitaRidotta;

	@Getter
	@Setter
	private TipologiaPratica tipologiaPratica;

	@Getter
	@Setter
	private boolean salvaNote;

	/* Task */
	private Set<TaskDTO<? extends DatiTaskDTO>> tasks = new HashSet<TaskDTO<? extends DatiTaskDTO>>();

	protected PraticaDTO() {
		// serialization
	}

	public PraticaDTO(String clientID) {
		this.clientID = clientID;
	}

	public String getUtenteCreazione() {
		return utenteCreazione;
	}

	public void setUtenteCreazione(String utenteCreazione) {
		this.utenteCreazione = utenteCreazione;
	}

	public String getUsernameCreazione() {
		return usernameCreazione;
	}

	public void setUsernameCreazione(String usernameCreazione) {
		this.usernameCreazione = usernameCreazione;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getDataOraCreazione() {
		return dataOraCreazione;
	}

	public void setDataOraCreazione(String dataCreazione) {
		this.dataOraCreazione = dataCreazione;
	}

	public String getAssegnatario() {
		return assegnatario;
	}

	public void setAssegnatario(String assegnatario) {
		this.assegnatario = assegnatario;
	}

	public boolean isLetto() {
		return letto;
	}

	public void setLetto(boolean letto) {
		this.letto = letto;
	}

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

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public String getNumeroRepertorio() {
		return numeroRepertorio;
	}

	public void setNumeroRepertorio(String numeroRepertorio) {
		this.numeroRepertorio = numeroRepertorio;
	}

	public List<AllegatoDTO> getAllegati() {
		return allegati;
	}

	public void setAllegati(List<AllegatoDTO> allegati) {
		this.allegati = allegati;
	}

	public String getClientID() {
		return clientID;
	}

	public String getStatoLabel() {
		return statoLabel;
	}

	public void setStatoLabel(String statoLabel) {
		this.statoLabel = statoLabel;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	/* ridefinizione metodi object */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PraticaDTO)
			return ((PraticaDTO) obj).getClientID().equals(clientID);
		else
			return false;
	}

	@Override
	public int hashCode() {
		return clientID.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append(getClientID()).append("}");
		return sb.toString();
	}

	public String getInCaricoALabel() {
		return inCaricoALabel;
	}

	public void setInCaricoALabel(String inCaricoALabel) {
		this.inCaricoALabel = inCaricoALabel;
	}

	public TipoPresaInCarico getTipoPresaInCarico() {
		return tipoPresaInCarico;
	}

	public void setTipoPresaInCarico(TipoPresaInCarico tipoPresaInCarico) {
		this.tipoPresaInCarico = tipoPresaInCarico;
	}

	public boolean getGestionePresaInCarico() {
		return gestionePresaInCarico;
	}

	public void setGestionePresaInCarico(boolean gestionePresaInCarico) {
		this.gestionePresaInCarico = gestionePresaInCarico;
	}

	public String getVisibileA() {
		return visibileA;
	}

	public void setVisibileA(String visibileA) {
		this.visibileA = visibileA;
	}

	public String getInCaricoAUserName() {
		return inCaricoAUserName;
	}

	public void setInCaricoAUserName(String inCaricoAUserName) {
		this.inCaricoAUserName = inCaricoAUserName;
	}

	public TreeSet<GruppoVisibilita> getVisibilita() {
		return visibilita;
	}

	public List<EventoIterDTO> getEventiIterDTO() {
		return eventiIterDTO;
	}

	/**
	 * true se l'utente corrente appartiene al gruppo assegnatario
	 *
	 * @return
	 */
	public boolean isUtenteAssegnatario() {
		return isUtenteAssegnatario;
	}

	public void setUtenteAssegnatario(boolean isUtenteAssegnatario) {
		this.isUtenteAssegnatario = isUtenteAssegnatario;
	}

	public AssegnazioneEsternaDTO getAssegnazioneEsterna() {
		return assegnazioneEsterna;
	}

	public void setAssegnazioneEsterna(AssegnazioneEsternaDTO assegnazioneEsterna) {
		this.assegnazioneEsterna = assegnazioneEsterna;
	}

	public List<String> getOperazioniAssegnaUtenteEsterno() {
		return operazioniAssegnaUtenteEsterno;
	}

	@Override
	public int compareTo(PraticaDTO o) {
		return clientID.compareTo(o.clientID);
	}

	public String getOperatore() {
		return operatore;
	}

	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}

	public String getAnnoPGCapofila() {
		return annoPGCapofila;
	}

	public String getNumeroPGCapofila() {
		return numeroPGCapofila;
	}

	public void setAnnoPGCapofila(String annoPGCapofila) {
		this.annoPGCapofila = annoPGCapofila;
	}

	public void setNumeroPGCapofila(String numeroPGCapofila) {
		this.numeroPGCapofila = numeroPGCapofila;
	}

	public Set<TaskDTO<? extends DatiTaskDTO>> getTasks() {
		return tasks;
	}
}
