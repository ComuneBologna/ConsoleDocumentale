package it.eng.consolepec.spagicclient;

import it.eng.consolepec.spagicclient.bean.request.template.CreaTemplateEmailRequest;
import it.eng.consolepec.spagicclient.bean.request.template.CreaTemplatePdfRequest;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

/**
 *
 * @author biagiot
 *
 */
public interface SpagicClientSaveTemplate {

	public LockedPratica saveTemplateMail(String pathTemplate, CreaTemplateEmailRequest request, Utente utente) throws SpagicClientException;

	public LockedPratica saveTemplatePDF(String pathTemplate, CreaTemplatePdfRequest request, Utente utente) throws SpagicClientException;

}
