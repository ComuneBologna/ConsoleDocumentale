package it.eng.portlet.consolepec.gwt.client.presenter.newsitemap;

import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;

import com.google.inject.Inject;

public class SiteMapHandler {

	private SiteMapInitializer siteMapInitializer;
	private SiteMapConfiguraSiteMap siteMapConfiguraSiteMap;

	@Inject
	public SiteMapHandler(SiteMapInitializer siteMapInitializer, SiteMapConfiguraSiteMap siteMapConfiguraSiteMap) {
		super();
		this.siteMapInitializer = siteMapInitializer;
		this.siteMapConfiguraSiteMap = siteMapConfiguraSiteMap;
	}

	public void configuraSiteMap(final MainPresenter mainPresenter) {
		siteMapInitializer.init();
		siteMapConfiguraSiteMap.configureSiteMap(mainPresenter);
	}

}
