package it.eng.cobo.consolepec.commons.datigenerici;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@Data
public class PreferenzeUtente implements Serializable {

	private static final long serialVersionUID = 5680130994350201457L;

	@NonNull
	private String utente;
	private String firmaEmail;
	private String fascicoloDefault;
	private PreferenzeCartellaAttivita preferenzeCartellaAttivita;

	@NoArgsConstructor
	@Data
	public static class PreferenzeCartellaAttivita implements Serializable {

		private static final long serialVersionUID = 537677501978966638L;

		private TipoRicerca tipoRicerca;
		private StatoFinaleRicerca statoFinaleRicerca;
		private List<String> indirizziNotifica = new ArrayList<>();
		private boolean riassegnaDefault = false;

		public static enum TipoRicerca {
			DESTINATARIO, PROPONENTE;
		}

		public static enum StatoFinaleRicerca {
			IN_APPROVAZIONE, CONCLUSO, EVASO;
		}
	}

	/**
	 *
	 * Da portare in PreferenzeUtente appena questi dati vengono spostati da Liferay a MongoDB
	 *
	 */
	@Data
	@NoArgsConstructor
	public static class PreferenzeRiassegnazione implements Serializable {
		private static final long serialVersionUID = 2573479467753407487L;

		private String settore;
		private String ruolo;
		private List<String> indirizziNotifica = new ArrayList<String>();
		private boolean ricordaScelta;
	}

	/**
	 *
	 * Da portare in PreferenzeUtente appena questi dati vengono spostati da Liferay a MongoDB
	 *
	 */
	@NoArgsConstructor
	@Data
	public static class PreferenzeFirmaDigitale implements Serializable {
		private static final long serialVersionUID = 6549311596686870898L;

		private String username;
		private String password;
	}
}
