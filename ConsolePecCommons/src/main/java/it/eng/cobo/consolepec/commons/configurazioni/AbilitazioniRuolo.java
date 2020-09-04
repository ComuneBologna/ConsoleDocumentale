package it.eng.cobo.consolepec.commons.configurazioni;

import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.Abilitazione;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false, of="ruolo")
@NoArgsConstructor
public class AbilitazioniRuolo implements Serializable, Configurabile {
	
	private static final long serialVersionUID = 8542352873314506016L;
	
	private List<Abilitazione> abilitazioni = new ArrayList<Abilitazione>();
	private String ruolo;
	
	
	private List<Azione> azioni = new ArrayList<Azione>();
	private String usernameCreazione;
	private Date dataCreazione;
}
