package it.eng.cobo.consolepec.commons.configurazioni.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.eng.cobo.consolepec.commons.configurazioni.AbilitazioniRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.profilazione.Settore;
import lombok.Data;

@Data
public class AnagraficaRuoloResponse implements Serializable {

	private static final long serialVersionUID = 7574032182905999878L;

	private AnagraficaRuolo anagraficaRuolo;
	private AbilitazioniRuolo abilitazioniRuolo;
	private Settore settoreRuolo;

	private List<String> errors = new ArrayList<>();
	private boolean error = false;
}
