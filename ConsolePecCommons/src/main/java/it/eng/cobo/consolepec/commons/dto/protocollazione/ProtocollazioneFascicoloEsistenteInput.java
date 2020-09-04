package it.eng.cobo.consolepec.commons.dto.protocollazione;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProtocollazioneFascicoloEsistenteInput extends ProtocollazioneInput {

	private List<String> allegati = new ArrayList<>();
	private List<String> pratiche = new ArrayList<>();

	@Override
	public void accept(ProtocollazioneInputVisitor v) {
		v.visit(this);
	}
}
