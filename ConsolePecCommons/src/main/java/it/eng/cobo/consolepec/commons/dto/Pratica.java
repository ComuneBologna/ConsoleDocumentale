package it.eng.cobo.consolepec.commons.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public abstract class Pratica implements Serializable {

	private static final long serialVersionUID = 389066558715588020L;

	private Tipo tipo;
	private String idDocumentale;
	private Integer versione;
	private Date dataCreazione;
	private String alfrescoFolderPath;
	private String alfrescoFileName;
	private String stato;
	private String note;
	private Ruolo ruoloAssegnatario;
	private String utenteInCarico;

	@Setter(AccessLevel.NONE)
	private List<Allegato> allegati = new ArrayList<>();

	protected Pratica(final Tipo tipo) {
		this.tipo = tipo;
	}

	public static enum Tipo {
		FASCICOLO, EMAIL
	}

}
