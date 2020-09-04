package it.eng.cobo.consolepec.commons.configurazioni.abilitazione;

import it.eng.cobo.consolepec.commons.configurazioni.Operazione;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModificaIngressoAbilitazione extends IngressoAbilitazione implements ModificaPraticaAbilitazione{
	private static final long serialVersionUID = -5216772172828099788L;
	
	List<Operazione> operazioni = new ArrayList<>();
	
	public ModificaIngressoAbilitazione(String tipoIngresso, String indirizzo) {
		super(tipoIngresso, indirizzo);
	}
}
