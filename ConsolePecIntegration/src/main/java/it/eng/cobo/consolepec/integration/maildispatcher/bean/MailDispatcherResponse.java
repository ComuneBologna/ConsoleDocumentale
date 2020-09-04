package it.eng.cobo.consolepec.integration.maildispatcher.bean;

import lombok.Data;

/**
 * 
 * @author biagiot
 *
 */
@Data
public class MailDispatcherResponse {
	
	private String dpi;
	private String input_email;
	private String output_class;
	private String processing_time_in_seconds;
	private String request_time;
	private String swt;
	private String tesseract_model;
}
