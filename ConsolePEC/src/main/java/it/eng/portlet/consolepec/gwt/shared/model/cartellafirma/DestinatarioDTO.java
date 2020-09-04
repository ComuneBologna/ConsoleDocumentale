package it.eng.portlet.consolepec.gwt.shared.model.cartellafirma;

import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
public abstract class DestinatarioDTO implements IsSerializable{

	protected StatoDestinatarioTaskFirmaDTO statoRichiesta;
}
