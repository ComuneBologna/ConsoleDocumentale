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
public class ModificaEmailOutAbilitazione extends EmailOutAbilitazione implements ModificaPraticaAbilitazione {
	private static final long serialVersionUID = -3491297915914914952L;
	
	List<Operazione> operazioni = new ArrayList<>();
	
	public ModificaEmailOutAbilitazione(String tipo, String indirizzo) {
		super(tipo, indirizzo);
	}
}
