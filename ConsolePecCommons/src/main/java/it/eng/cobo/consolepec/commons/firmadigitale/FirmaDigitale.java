package it.eng.cobo.consolepec.commons.firmadigitale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author GiacomoFM
 * @since 14/feb/2018
 */
@NoArgsConstructor
public class FirmaDigitale implements Serializable {

	private static final long serialVersionUID = -1241686809132521943L;

	@Getter @Setter private String codice;
	@Getter @Setter private String descrizione;
	@Getter private List<Firmatario> firmatari = new ArrayList<>();

}
