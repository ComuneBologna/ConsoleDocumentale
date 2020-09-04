package it.eng.portlet.consolepec.gwt.server.rest;

import java.util.Date;

import lombok.Data;

/**
 * @author Giacomo F.M.
 * @since 2019-06-20
 */
@Data
public class ErrorResponse {

	private Date timestamp;
	private Integer status;
	private String error;
	private String message;
	private String path;

}
