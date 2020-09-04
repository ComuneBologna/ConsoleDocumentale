package it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@ToString
public class DatoAggiuntivoValoreMultiplo extends DatoAggiuntivoValore{

	private static final long serialVersionUID = 2825636519402122136L;
	
	@Getter
	private List<String> valoriPredefiniti = new ArrayList<String>();
	
	@Getter
	List<String> valori = new ArrayList<String>();

	public DatoAggiuntivoValoreMultiplo(String nome, String descrizione, TipoDato tipo, Integer posizione, boolean visibile, boolean obbligatorio, boolean editabile,  List<String> valoriPredefiniti, List<String> valori) {
		super(nome, descrizione, tipo, posizione, visibile, obbligatorio, editabile);
		this.valoriPredefiniti = valoriPredefiniti;
		this.valori = valori;
	}
	
	@Override
	public void accept(DatoAggiuntivoVisitor v) {
		v.visit(this);
	}
	
	@Override
	public DatoAggiuntivoValoreMultiplo clona() {
		return new DatoAggiuntivoValoreMultiplo(nome, descrizione, tipo, posizione, visibile, isObbligatorio(), isEditabile(), valoriPredefiniti, valori);
	}

}
