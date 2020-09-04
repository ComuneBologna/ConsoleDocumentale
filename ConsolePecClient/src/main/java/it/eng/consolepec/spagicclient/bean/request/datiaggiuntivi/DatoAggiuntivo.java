package it.eng.consolepec.spagicclient.bean.request.datiaggiuntivi;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.RigaDatoAggiuntivo;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode
@NoArgsConstructor
public class DatoAggiuntivo {
	@Getter
	@Setter
	private String nome;
	@Getter
	@Setter
	private String valore;
	@Getter
	@Setter
	private Double idAnagrafica;
	@Getter
	private List<String> valori = new ArrayList<String>();
	@Getter
	@Setter
	private String descrizione;
	@Getter
	@Setter
	private TipoDato tipo;
	@Getter
	@Setter
	private Boolean obbligatorio;
	@Getter
	@Setter
	private Boolean editabile;
	@Getter
	@Setter
	private Boolean visibile;
	@Getter
	@Setter
	private Integer posizione;
	@Getter
	@Setter
	private TabellaDatoAggiuntivo tabella;
	
	public DatoAggiuntivo(String nome, String valore, String descrizione, TipoDato valueOf, boolean obbligatorio, boolean editabile, boolean visibile, int posizione) {
		this.nome = nome;
		this.valore = valore;
		this.descrizione = descrizione;
		this.tipo = valueOf;
		this.obbligatorio = obbligatorio;
		this.editabile = editabile;
		this.visibile = visibile;
		this.posizione = posizione;
	}

	public DatoAggiuntivo(String nome, List <String> valori, String descrizione, TipoDato valueOf, boolean obbligatorio, boolean editabile, boolean visibile, int posizione) {
		this.nome = nome;
		this.valori.addAll(valori);
		this.descrizione = descrizione;
		this.tipo = valueOf;
		this.obbligatorio = obbligatorio;
		this.editabile = editabile;
		this.visibile = visibile;
		this.posizione = posizione;
	}
	
	public DatoAggiuntivo(String nome, String valore, Double idAnagrafica, String descrizione, TipoDato valueOf, boolean obbligatorio, boolean editabile, boolean visibile, int posizione) {
		this.nome = nome;
		this.descrizione = descrizione;
		this.valore = valore;
		this.idAnagrafica = idAnagrafica;
		this.tipo = valueOf;
		this.obbligatorio = obbligatorio;
		this.editabile = editabile;
		this.visibile = visibile;
		this.posizione = posizione;
	}
	
	public DatoAggiuntivo(String nome, String descrizione, TipoDato valueOf, boolean obbligatorio, boolean editabile, boolean visibile, int posizione, TabellaDatoAggiuntivo tabella) {
		this.nome = nome;
		this.descrizione = descrizione;
		this.tipo = valueOf;
		this.obbligatorio = obbligatorio;
		this.editabile = editabile;
		this.visibile = visibile;
		this.posizione = posizione;
		this.tabella = tabella;
	}

	public static enum TipoDato {
		Testo, Numerico, Data, Lista, Suggest, IndirizzoVia, IndirizzoCivico, IndirizzoEsponente, MultiploTesto, Anagrafica, Tabella;
	}
	
	@NoArgsConstructor
	public static class TabellaDatoAggiuntivo {
		
		@Getter
		private List<DatoAggiuntivo> intestazioni = new ArrayList<DatoAggiuntivo>();
		@Getter
		@Setter
		private boolean editabile;
		
		@Getter
		private List<RigaDatoAggiuntivo> righe = new ArrayList<RigaDatoAggiuntivo>();
		
		public TabellaDatoAggiuntivo(List<DatoAggiuntivo> intestazioni, List<RigaDatoAggiuntivo> righe) {
			this.intestazioni = intestazioni;
			this.righe = righe;
		}
	}
}
