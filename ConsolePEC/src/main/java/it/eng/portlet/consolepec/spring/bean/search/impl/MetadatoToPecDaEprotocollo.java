package it.eng.portlet.consolepec.spring.bean.search.impl;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.consolepec.spagicclient.search.SearchObjectResult;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

public class MetadatoToPecDaEprotocollo extends MetadatoToPecIn {

	@Override
	protected PraticaDTO creaPraticaInternal(SearchObjectResult metadati, String alfrescoPath) {

		if (TipologiaPratica.EMAIL_EPROTOCOLLO.getNomeTipologia().equals(getTipologia(metadati)))
			return new PecInDTO(alfrescoPath);
		
		return null;
	}
	
	public MetadatoToPecDaEprotocollo(){
		super();
	}

}
