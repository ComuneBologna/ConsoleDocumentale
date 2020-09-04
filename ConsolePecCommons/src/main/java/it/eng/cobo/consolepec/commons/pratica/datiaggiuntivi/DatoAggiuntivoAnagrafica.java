package it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@ToString
public class DatoAggiuntivoAnagrafica extends DatoAggiuntivoValore {
	
	private static final long serialVersionUID = 3433720538171140419L;

	@Getter
	@Setter
	private String valore;
	
	@Getter
	@Setter
	private Double idAnagrafica;

	public DatoAggiuntivoAnagrafica(String nome, String descrizione, TipoDato tipo, Integer posizione, boolean visibile, boolean obbligatorio, boolean editabile, String valore, Double idAnagrafica) {
		super(nome, descrizione, tipo, posizione, visibile, obbligatorio, editabile);
		this.valore = valore;
		this.idAnagrafica = idAnagrafica;
	}
	
	@Override
	public void accept(DatoAggiuntivoVisitor v) {
		v.visit(this);
	}
	
	@Override
	public DatoAggiuntivoAnagrafica clona() {
		return new DatoAggiuntivoAnagrafica(nome, descrizione, tipo, posizione, visibile, isObbligatorio(), isEditabile(), valore, idAnagrafica);
	}
	
}
