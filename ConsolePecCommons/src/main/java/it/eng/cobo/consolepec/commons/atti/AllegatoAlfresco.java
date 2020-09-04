package it.eng.cobo.consolepec.commons.atti;

import java.io.InputStream;
import java.io.Serializable;

import it.eng.cobo.consolepec.commons.firmadigitale.FirmaDigitale;
import it.eng.cobo.consolepec.commons.services.Attachment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author GiacomoFM
 * @since 12/mar/2018
 */
@NoArgsConstructor
@AllArgsConstructor
public class AllegatoAlfresco implements Serializable {

	private static final long serialVersionUID = 7715437996796038755L;

	@Getter
	private String uuid;
	@Getter
	private Float versione;
	@Getter
	@Setter
	private String nomeFile;
	@Getter
	@Attachment
	@Setter
	private InputStream inputStream;
	@Getter
	private String hash;
	@Getter
	@Setter
	private FirmaDigitale firmaDigitale;

}
