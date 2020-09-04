package it.eng.portlet.consolepec.gwt.client.gin;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.gwtplatform.mvp.client.RootPresenter;
import com.gwtplatform.mvp.client.googleanalytics.GoogleAnalytics;
import com.gwtplatform.mvp.client.googleanalytics.GoogleAnalyticsImpl;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.shared.proxy.ParameterTokenFormatter;
import com.gwtplatform.mvp.shared.proxy.TokenFormatter;

public class HireDefaultModule extends AbstractGinModule {

	private final Class<? extends PlaceManager> placeManagerClass;

	public HireDefaultModule(Class<? extends PlaceManager> placeManagerClass) {
		this.placeManagerClass = placeManagerClass;
	}

	@Override
	protected void configure() {
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
		bind(TokenFormatter.class).to(ParameterTokenFormatter.class).in(Singleton.class);
		bind(RootPresenter.class).to(HireRootPresenter.class).asEagerSingleton();
		bind(GoogleAnalytics.class).to(GoogleAnalyticsImpl.class).in(Singleton.class);
		bind(PlaceManager.class).to(placeManagerClass).in(Singleton.class);
	}

}
