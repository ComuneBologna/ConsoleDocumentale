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
public class ModificaModelloAbilitazione extends ModelloAbilitazione implements ModificaPraticaAbilitazione {
	private static final long serialVersionUID = -5927063080713333060L;
	
	List<Operazione> operazioni = new ArrayList<>();
	
	public ModificaModelloAbilitazione(String tipo) {
		super(tipo);
	}
}
