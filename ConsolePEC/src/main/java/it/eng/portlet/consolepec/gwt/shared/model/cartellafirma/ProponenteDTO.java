package it.eng.portlet.consolepec.gwt.shared.model.cartellafirma;

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
public class ProponenteDTO implements IsSerializable{

	private String nomeGruppo;
}
