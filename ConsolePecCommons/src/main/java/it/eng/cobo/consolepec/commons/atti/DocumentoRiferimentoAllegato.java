package it.eng.cobo.consolepec.commons.atti;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author GiacomoFM
 * @since 08/mar/2018
 */
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoRiferimentoAllegato {

	@Getter private String nome;
	@Getter private String versioneCorrente;
	@Getter private String oggetto;
	@Getter private String hash;
}
