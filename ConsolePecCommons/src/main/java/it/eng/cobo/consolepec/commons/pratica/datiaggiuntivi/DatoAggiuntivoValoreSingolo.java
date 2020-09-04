package it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@ToString
public class DatoAggiuntivoValoreSingolo extends DatoAggiuntivoValore {

	private static final long serialVersionUID = -5951881752360691395L;

	@Getter
	private List<String> valoriPredefiniti = new ArrayList<String>();
	
	@Getter
	@Setter
	private String valore;

	public DatoAggiuntivoValoreSingolo(String nome, String descrizione, TipoDato tipo, Integer posizione, boolean visibile, boolean obbligatorio, boolean editabile, List<String> valoriPredefiniti, String valore) {
		super(nome, descrizione, tipo, posizione, visibile, obbligatorio, editabile);
		this.valoriPredefiniti = valoriPredefiniti;
		this.valore = valore;
	}

	@Override
	public void accept(DatoAggiuntivoVisitor v) {
		v.visit(this);
	}
	
	@Override
	public DatoAggiuntivoValoreSingolo clona() {
		return new DatoAggiuntivoValoreSingolo(nome, descrizione, tipo, posizione, visibile, isObbligatorio(), isEditabile(), valoriPredefiniti, valore);
	}

}