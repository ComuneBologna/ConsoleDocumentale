package it.eng.portlet.consolepec.gwt.client.angular;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants.VociRootSiteMap;

/**
 * @author Giacomo F.M.
 * @since 2019-05-14
 */
public class AngularPresenter extends Presenter<AngularPresenter.MyView, AngularPresenter.MyProxy> {

	private SitemapMenu sitemapMenu;

	@ProxyCodeSplit
	@NameToken(NameTokens.angular)
	public interface MyProxy extends ProxyPlace<AngularPresenter> {/**/}

	public interface MyView extends View {/**/}

	@Inject
	public AngularPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final SitemapMenu sitemapMenu) {
		super(eventBus, view, proxy);
		this.sitemapMenu = sitemapMenu;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		sitemapMenu.setActiveVoce(VociRootSiteMap.ANGULAR.getId());
	}

}
