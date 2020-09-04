package it.eng.cobo.consolepec.commons.pratica.fascicolo.collegamenti;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author GiacomoFM
 * @since 08/nov/2018
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permessi implements Serializable {

	private static final long serialVersionUID = 8323913407886212049L;

	private boolean sorgenteAccessibileInLettura = true, remotoAccessibileInLettura = true;

	private List<String> operazioniConsentite = new ArrayList<>();

}
