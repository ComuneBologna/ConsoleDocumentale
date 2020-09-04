package it.eng.cobo.consolepec.commons.drive;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.eng.cobo.consolepec.commons.drive.metadato.Metadato;
import it.eng.cobo.consolepec.commons.drive.permessi.PermessoDrive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Giacomo F.M.
 * @since 2019-06-17
 */
@Data
public abstract class DriveElement implements Serializable {

	public static final String PATH_SEPARATOR = ",";

	private static final long serialVersionUID = 5428280370326276084L;

	private boolean cartella;

	private String id;
	private String path;
	private String pathAlfresco;
	private String nome;
	@EqualsAndHashCode.Exclude
	private String descrizione;

	private String utenteCreazione;
	private Date dataCreazione;

	@EqualsAndHashCode.Exclude
	private String dizionario;

	@Setter(AccessLevel.NONE)
	@EqualsAndHashCode.Exclude
	private Set<Metadato> metadati = new HashSet<>();

	@Setter(AccessLevel.NONE)
	@EqualsAndHashCode.Exclude
	private List<PermessoDrive> permessi = new ArrayList<>();

	@Setter(AccessLevel.NONE)
	@EqualsAndHashCode.Exclude
	private List<AzioneDrive> azioni = new ArrayList<>();

	@Data
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor
	public static class AzioneDrive implements Serializable {

		private static final long serialVersionUID = 1L;

		private String utente;
		private String descrizione;
		private Date data;
	}

}
