package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import it.eng.cobo.consolepec.commons.configurazioni.Operazione;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModificaComunicazioneAbilitazione extends ComunicazioneAbilitazione implements ModificaPraticaAbilitazione {
	private static final long serialVersionUID = -2557429803734170382L;
	
	List<Operazione> operazioni = new ArrayList<>();
	
	public ModificaComunicazioneAbilitazione(String tipo) {
		super(tipo);
	}
}
