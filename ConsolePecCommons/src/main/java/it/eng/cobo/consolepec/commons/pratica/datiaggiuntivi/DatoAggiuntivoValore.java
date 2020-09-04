package it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@ToString
public abstract class DatoAggiuntivoValore extends DatoAggiuntivo {

	private static final long serialVersionUID = -9146765307968729566L;
	
	@Getter
	@Setter
	private boolean obbligatorio;

	@Getter
	@Setter
	private boolean editabile;

	public DatoAggiuntivoValore(String nome, String descrizione, TipoDato tipo, Integer posizione, boolean visibile, boolean obbligatorio, boolean editabile) {
		super(nome, descrizione, tipo, posizione, visibile);
		this.obbligatorio = obbligatorio;
		this.editabile = editabile;
	}
	
}
