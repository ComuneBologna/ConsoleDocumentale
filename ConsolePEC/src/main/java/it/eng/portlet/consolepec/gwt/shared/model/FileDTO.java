package it.eng.portlet.consolepec.gwt.shared.model;

import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.Stato;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.TipologiaFirma;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author biagiot
 *
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(exclude={"path", "informazioniFirmaDigitale"})
public class FileDTO implements IsSerializable, Comparable<FileDTO> {

	private String nome;
	private String path;
	private InformazioniFirmaDigitaleDTO informazioniFirmaDigitale;

	protected FileDTO() {
		// Ser
	}

	@Getter
	@Setter
	@EqualsAndHashCode
	@ToString
	@AllArgsConstructor
	public static class InformazioniFirmaDigitaleDTO implements IsSerializable {
		private boolean firmato;
		private boolean firmatoHash;
		private TipologiaFirma tipologiaFirma;
		private Stato statoFirma;

		protected InformazioniFirmaDigitaleDTO() {
			// Ser
		}
	}

	@Override
	public int compareTo(FileDTO o) {
		return nome.compareTo(o.getNome());
	}
}
