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
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class ModificaPraticaModulisticaAbilitazione extends PraticaModulisticaAbilitazione implements ModificaPraticaAbilitazione{
	private static final long serialVersionUID = -365116361255459342L;
	
	List<Operazione> operazioni = new ArrayList<>();
	
	public ModificaPraticaModulisticaAbilitazione(String tipo) {
		super(tipo);
	}
}
