package it.eng.portlet.consolepec.gwt.shared.action.configurazioni;

import it.eng.cobo.consolepec.commons.profilazione.Settore;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.gwtplatform.dispatch.shared.Result;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CaricaSettoriResult implements Result {
	
	private static final long serialVersionUID = 1L;

	@Getter
	List<Settore> settori = new ArrayList<Settore>();
	
	@Getter
	String errorMessage;
	
	@Getter
	boolean error;
	
	public CaricaSettoriResult(String errorMessage) {
		this.errorMessage = errorMessage;
		this.error = true;
	}
	
	public CaricaSettoriResult(List<Settore> settori) {
		this.settori = settori;
		this.error = false;
	}
}
