package it.eng.cobo.consolepec.commons.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

/**
 * @since 2020-01-29
 */
@Data
public class TaskFirmaDocumentale implements Serializable {

	private static final long serialVersionUID = 4142508176117401673L;

	private Integer idTaskFirma;
	private String praticaPath;
	private String tipoRichiesta;
	private String statoRichiesta;
	private String mittenteOriginale;
	private String gruppoProponente;
	private Date dataProposta;
	private Date dataScadenza;
	private RiferimentoAllegatoDocumentale allegato;
	@Setter(AccessLevel.PRIVATE)
	private List<DestinatarioDocumentale> destinatari = new ArrayList<>();

	private boolean operazioniDestinatarioAbilitate;
	private boolean operazioniProponenteAbilitate;

	@Data
	public static abstract class DestinatarioDocumentale implements Serializable {
		private static final long serialVersionUID = -1893545002236496825L;
		private String stato;
	}

	@Data
	@ToString(callSuper = true)
	@EqualsAndHashCode(callSuper = true)
	public static class DestinatarioUtenteDocumentale extends DestinatarioDocumentale {
		private static final long serialVersionUID = -3769857040893570344L;
		private String userid;
		private String nome;
		private String cognome;
		private String matricola;
		private String settore;
	}

	@Data
	@ToString(callSuper = true)
	@EqualsAndHashCode(callSuper = true)
	public static class DestinatarioGruppoDocumentale extends DestinatarioDocumentale {
		private static final long serialVersionUID = -1334120374495294521L;
		private String nomeGruppo;
	}

	@Data
	public static class RiferimentoAllegatoDocumentale implements Serializable {
		private static final long serialVersionUID = 3037643690391368024L;
		private String nome;
		private String currentVersion;
	}
}
