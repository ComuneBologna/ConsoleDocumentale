package it.eng.consolepec.spagicclient;

import it.eng.consolepec.spagicclient.bean.request.template.CreaTemplateEmailRequest;
import it.eng.consolepec.spagicclient.bean.request.template.CreaTemplatePdfRequest;
import it.eng.consolepec.spagicclient.bean.request.template.CreaTemplatePerCopia;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

public interface SpagicClientCreateTemplate {

	/**
	 * 
	 * @param request
	 * @param utente
	 * @return
	 * @throws SpagicClientException
	 */
	public LockedPratica createTemplate(CreaTemplateEmailRequest request, Utente utente) throws SpagicClientException;

	/**
	 * 
	 * @param request
	 * @param utente
	 * @return
	 * @throws SpagicClientException
	 */
	public LockedPratica createTemplate(CreaTemplatePdfRequest request, Utente utente) throws SpagicClientException;

	/**
	 * 
	 * @param request
	 * @param utente
	 * @return
	 * @throws SpagicClientException
	 */
	public LockedPratica createTemplatePerCopia(CreaTemplatePerCopia request, Utente utente) throws SpagicClientException;

}
