package it.eng.portlet.consolepec.gwt.shared.action.configurazioni;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

import it.eng.cobo.consolepec.commons.drive.Nomenclatura;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Giacomo F.M.
 * @since 2019-07-08
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CaricaNomenclatureResult implements Result {

	private static final long serialVersionUID = 5875847470725990854L;

	@Getter
	private List<Nomenclatura> nomenclature = new ArrayList<>();

	@Getter
	private boolean error;
	@Getter
	private String errorMessage = "";

	public CaricaNomenclatureResult(final String errorMessage) {
		this.error = true;
		this.errorMessage = errorMessage;
	}

	public CaricaNomenclatureResult(final List<Nomenclatura> nomenclature) {
		this.error = false;
		this.nomenclature.addAll(nomenclature);
	}

}
