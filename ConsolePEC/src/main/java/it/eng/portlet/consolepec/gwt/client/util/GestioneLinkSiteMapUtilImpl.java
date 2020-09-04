package it.eng.portlet.consolepec.gwt.client.util;

import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.widget.TitoloLink;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class GestioneLinkSiteMapUtilImpl implements GestioneLinkSiteMapUtil {

	private PlaceManager placeManager;
	private SitemapMenu sitemapMenu;

	@Inject
	public GestioneLinkSiteMapUtilImpl(PlaceManager placeManager, SitemapMenu sitemapMenu) {
		this.placeManager = placeManager;
		this.sitemapMenu = sitemapMenu;
	}

	@Override
	public void aggiungiLinkInLavorazione(PraticaDTO dto) {
		PlaceRequest.Builder builder = new PlaceRequest.Builder();
		builder.nameToken(dto.getTipologiaPratica().getDettaglioNameToken());
		builder.with(NameTokensParams.idPratica, dto.getClientID());
		PlaceRequest r = builder.build();
		String historyToken = placeManager.buildHistoryToken(r);
		if (!sitemapMenu.containsLink(dto.getClientID())) {
			sitemapMenu.addBreadCrumbsChildVoce(new TitoloLink(dto.getTitolo()), historyToken, ConsolePecConstants.VociRootSiteMap.APERTE_ORA.name(), false, dto.getClientID());
			sitemapMenu.setActiveVoce(dto.getClientID());
		} else
			sitemapMenu.setActiveVoce(dto.getClientID());
	}
}
