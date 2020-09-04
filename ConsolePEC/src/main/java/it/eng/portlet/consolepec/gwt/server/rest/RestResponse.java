package it.eng.portlet.consolepec.gwt.server.rest;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Giacomo F.M.
 * @since 2019-06-03
 */
@Data
@AllArgsConstructor
public class RestResponse {

	private boolean ok;
	private String json;

}
