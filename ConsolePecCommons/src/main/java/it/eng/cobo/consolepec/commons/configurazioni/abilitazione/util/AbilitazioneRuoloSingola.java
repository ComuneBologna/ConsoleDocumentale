package it.eng.cobo.consolepec.commons.configurazioni.abilitazione.util;

import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.Abilitazione;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class AbilitazioneRuoloSingola implements Serializable{
	private static final long serialVersionUID = -7048169812850709042L;
	
	private String ruolo;
	private Abilitazione abilitazione;
}
