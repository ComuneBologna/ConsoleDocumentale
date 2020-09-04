package it.eng.consolepec.spagicclient.bean.request.modulistica;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode
public class ValoreModulo implements NodoModulistica {
	
	
	@Getter @Setter String nome;
	@Getter @Setter  String etichetta;
	@Getter @Setter  String valore;
	@Getter @Setter  String descrizione;
	@Getter @Setter  TipoValoreModulo tipo;
	@Getter @Setter  TabellaModulo tabella;
	@Getter @Setter  boolean visibile;
	@Getter final TipoNodoModulistica tipoNodo = TipoNodoModulistica.VALORE_MODULO;
	
	public ValoreModulo(String nome, String etichetta, String valore, String descrizione, boolean visibilita) {
		super();
		this.nome = nome;
		this.valore = valore;
		this.etichetta = etichetta;
		this.descrizione = descrizione;
		this.visibile = visibilita;
		tipo = TipoValoreModulo.Valore;
	}
	
	
	public ValoreModulo(String nome, String etichetta, TabellaModulo tabella, boolean visibilita) {
		super();
		this.nome = nome;
		this.etichetta = etichetta;
		this.tabella = tabella;
		this.visibile = visibilita;
		tipo = TipoValoreModulo.Tabella;
	}

	
	
	
	public static enum TipoValoreModulo {
		Valore, Tabella;
	}
	
	@AllArgsConstructor
	@NoArgsConstructor
	@EqualsAndHashCode
	public static class TabellaModulo {
		
		@Getter List<Riga> righe = new ArrayList<Riga>(); 
		
		public TabellaModulo(Riga...righe) {
			super();
			this.righe = java.util.Arrays.asList(righe);
		}
		
		
		@AllArgsConstructor
		@NoArgsConstructor
		@EqualsAndHashCode
		public static class Riga {
			
			@Getter List<ValoreModulo> colonne = new ArrayList<ValoreModulo>();

			
			public Riga(ValoreModulo...colonne) {
				super();
				this.colonne = java.util.Arrays.asList(colonne);
			}
			
			
		}
	}

	
}

