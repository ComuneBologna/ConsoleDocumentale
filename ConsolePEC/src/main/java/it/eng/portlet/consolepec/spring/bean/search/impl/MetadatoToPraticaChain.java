package it.eng.portlet.consolepec.spring.bean.search.impl;

import it.eng.consolepec.spagicclient.search.SearchObjectResult;
import it.eng.consolepec.xmlplugin.factory.MetadatiPratica;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;

public abstract class MetadatoToPraticaChain {
	
	private MetadatoToPraticaChain next;
	protected GestioneConfigurazioni gestioneConfigurazioni;

	public MetadatoToPraticaChain setHandler(GestioneConfigurazioni gestioneConfigurazioni) {
		this.gestioneConfigurazioni = gestioneConfigurazioni;
		return this;
	}


	/* api pubbliche */
	/**
	 * Il metodo crea la tipologia giusta di pratica, e la popola con i metadati passati
	 * 
	 * @param metadati
	 * @param alfrescoPath
	 * @return
	 */
	public PraticaDTO creaPratica(SearchObjectResult result, String alfrescoPath) {
		PraticaDTO res = null;
		res = creaPraticaChain(result, alfrescoPath);
		if (res != null)
			popolaMetadatiChain(result, res);
		return res;
	}

	/* api interne */

	MetadatoToPraticaChain setNext(MetadatoToPraticaChain next) {
		this.next = next;
		this.next.setHandler(gestioneConfigurazioni);
		return next;
	}
	
	protected abstract PraticaDTO creaPraticaInternal(SearchObjectResult result, String alfrescoPath);

	protected abstract void popolaMetadatiInternal(SearchObjectResult result, PraticaDTO pratica);

	protected String getTipologia(SearchObjectResult metadati) {
		return (String) metadati.getValue(MetadatiPratica.pTipoPratica.getNomeQualified());
	}
	
	protected PraticaDTO creaPraticaChain(SearchObjectResult result, String alfrescoPath) {
		PraticaDTO dto = creaPraticaInternal(result, alfrescoPath);
		while (dto == null && next != null)
			dto = next.creaPraticaChain(result, alfrescoPath);
		return dto;
	}

	protected void popolaMetadatiChain(SearchObjectResult result, PraticaDTO dto) {
		popolaMetadatiInternal(result, dto);
		if (next != null)
			next.popolaMetadatiChain(result, dto);
	}
}
