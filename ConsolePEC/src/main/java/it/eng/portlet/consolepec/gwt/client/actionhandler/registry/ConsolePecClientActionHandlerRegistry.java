package it.eng.portlet.consolepec.gwt.client.actionhandler.registry;

import it.eng.portlet.consolepec.gwt.client.actionhandler.GetDatiAssegnaEsternoClientActionHandler;
import it.eng.portlet.consolepec.gwt.client.actionhandler.GruppiVisibilitaClientActionHandler;
import it.eng.portlet.consolepec.gwt.client.actionhandler.MatriceVisibilitaPraticaActionHandler;
import it.eng.portlet.consolepec.gwt.client.actionhandler.TipologiaProcedimentoClientActionHandler;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.actionhandler.DefaultClientActionHandlerRegistry;

public class ConsolePecClientActionHandlerRegistry extends DefaultClientActionHandlerRegistry {

	@Inject
	public ConsolePecClientActionHandlerRegistry(GruppiVisibilitaClientActionHandler gruppiVisibilitaClientActionHandler, 
			TipologiaProcedimentoClientActionHandler tipologiaProcedimentoClientActionHandler, 
			GetDatiAssegnaEsternoClientActionHandler getDatiAssegnaEsternoActionHandler,
			MatriceVisibilitaPraticaActionHandler matriceVisibilitaPraticaActionHandler) {
		register(gruppiVisibilitaClientActionHandler);
		register(tipologiaProcedimentoClientActionHandler);
		register(getDatiAssegnaEsternoActionHandler);
		register(matriceVisibilitaPraticaActionHandler);
		
	}

}
