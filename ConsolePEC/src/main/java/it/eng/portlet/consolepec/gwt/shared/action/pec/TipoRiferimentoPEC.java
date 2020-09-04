package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.model.PecDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecDaEprotocolloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum TipoRiferimentoPEC implements IsSerializable{
	IN, EPROTO, OUT;
	
	public static TipoRiferimentoPEC getTipo(PecDTO dto){
		if(dto instanceof PecInDTO)
			return IN;
		else if (dto instanceof PecDaEprotocolloDTO)
			return EPROTO;
		else
			return OUT;
	}
}
