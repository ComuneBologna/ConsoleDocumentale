package it.eng.portlet.consolepec.gwt.client.operazioni.template;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.portlet.consolepec.gwt.client.operazioni.BackToPratica;

import java.util.Map;


/**
 *
 * @author biagiot
 *
 */
public interface TemplateSceltaWizardApiClient extends BackToPratica {
	
	public void goToCreaDaTemplate(String fascicoloPath, TipologiaPratica tipo);
	
	public void backToSceltaTemplate();

	public void goToCompilaCampiTemplate(String templatePath);

	public void creaBozzaDaTemplate(Map<String, String> valori);

	public void creaPdfDaTemplate(Map<String, String> valori, String filename);
}
