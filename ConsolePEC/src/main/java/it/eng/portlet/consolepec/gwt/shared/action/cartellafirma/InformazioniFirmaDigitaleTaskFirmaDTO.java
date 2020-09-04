package it.eng.portlet.consolepec.gwt.shared.action.cartellafirma;

import it.eng.portlet.consolepec.gwt.shared.action.firma.CredenzialiFirma;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.TipologiaFirma;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author biagiot
 *
 */

@Getter
@Setter
@AllArgsConstructor
public class InformazioniFirmaDigitaleTaskFirmaDTO implements IsSerializable {
	TipologiaFirma tipoFirma;
	CredenzialiFirma credenzialiFirma;

	protected InformazioniFirmaDigitaleTaskFirmaDTO() {
		// Ser
	}
}
