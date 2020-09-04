package it.eng.cobo.consolepec.commons.dto.protocollazione;

import it.eng.cobo.consolepec.commons.dto.CreateFascicoloInput;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProtocollazioneNuovoFascicoloInput extends ProtocollazioneInput {

	private CreateFascicoloInput creazioneFascicolo;
	private String praticaDaProtocollare;

	@Override
	public void accept(ProtocollazioneInputVisitor v) {
		v.visit(this);
	}
}
