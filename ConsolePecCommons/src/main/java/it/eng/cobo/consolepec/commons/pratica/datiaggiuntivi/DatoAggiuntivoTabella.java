package it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.RigaDatoAggiuntivo;

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
public class DatoAggiuntivoTabella extends DatoAggiuntivo {

	private static final long serialVersionUID = -2498563419741297403L;
	
	@Getter
	List<DatoAggiuntivo> intestazioni = new ArrayList<>();
	@Getter
	List<RigaDatoAggiuntivo> righe = new ArrayList<>();

	@Getter
	@Setter
	boolean editabile;
	
	public DatoAggiuntivoTabella(String nome, String descrizione, TipoDato tipo, Integer posizione, boolean visibile, List<DatoAggiuntivo> intestazioni, List<RigaDatoAggiuntivo> datiAggiuntivi, boolean editabile) {
		super(nome, descrizione, tipo, posizione, visibile);
		this.righe = datiAggiuntivi;
		this.intestazioni = intestazioni;
		this.editabile = editabile;
	}
	
	@Override
	public void accept(DatoAggiuntivoVisitor v) {
		v.visit(this);
	}
	
	@Override
	public DatoAggiuntivoTabella clona() {
		return new DatoAggiuntivoTabella(nome, descrizione, tipo, posizione, visibile, intestazioni, righe, editabile);
	}
}
