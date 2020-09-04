package it.eng.portlet.consolepec.gwt.shared.action.cartellafirma;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * @author biagiot
 *
 */
@Data
public class InformazioniNotificaTaskFirmaDTO implements IsSerializable{
	
	private String note;
	private List<String> indirizziNotifica = new ArrayList<String>();
	private boolean ricordaScelta;
}
