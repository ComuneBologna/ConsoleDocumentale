package it.eng.portlet.consolepec.gwt.shared.action.configurazioni;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

import it.eng.cobo.consolepec.commons.drive.Dizionario;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Giacomo F.M.
 * @since 2019-06-11
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CaricaDizionariResult implements Result {

	private static final long serialVersionUID = -916504808868035026L;

	@Getter
	List<Dizionario> dizionari = new ArrayList<Dizionario>();

	@Getter
	boolean error;
	@Getter
	String errorMessage;

	public CaricaDizionariResult(String errorMessage) {
		this.error = true;
		this.errorMessage = errorMessage;
	}

	public CaricaDizionariResult(List<Dizionario> dizionari) {
		this.error = false;
		this.dizionari = dizionari;
	}
}
