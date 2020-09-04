package it.eng.portlet.consolepec.gwt.shared.rest;

import com.gwtplatform.dispatch.shared.Result;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Giacomo F.M.
 * @since 2019-07-04
 */
@NoArgsConstructor
public class RestLoginResult implements Result {

	private static final long serialVersionUID = 8422692529640252504L;

	@Getter
	private String msgError = "";
	@Getter
	private boolean error = false;

	public RestLoginResult(final String msgError) {
		super();
		this.msgError = msgError;
	}

}
