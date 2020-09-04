package it.eng.cobo.consolepec.integration.maildispatcher.client;

import java.io.File;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.integration.maildispatcher.bean.MailDispatcherResponse;

/**
 *
 * @author biagiot
 *
 */
public interface MailDispatcherClient {

	MailDispatcherResponse dispatch(File... files) throws ApplicationException;

}
