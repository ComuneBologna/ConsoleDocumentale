package it.eng.cobo.consolepec.commons.drive;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * @author Giacomo F.M.
 * @since 2019-05-28
 */
@Data
public class Dizionario implements Serializable {

	private static final long serialVersionUID = -183296340416124295L;

	private String nome;
	private String label;
	private boolean perCartella;

	@Setter(AccessLevel.NONE)
	private List<DatoAggiuntivo> datiAggiuntivi = new ArrayList<>();

}
