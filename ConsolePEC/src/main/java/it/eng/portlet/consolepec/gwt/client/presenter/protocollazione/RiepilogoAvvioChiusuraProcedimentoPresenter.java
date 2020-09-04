package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione;

import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.RiepilogoAvvioChiusuraProcedimentoEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.RiepilogoAvvioChiusuraProcedimentoEvent.RiepilogoAvvioChiusuraProcedimentoHandler;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.GestioneProcedimentoResult;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class RiepilogoAvvioChiusuraProcedimentoPresenter extends Presenter<RiepilogoAvvioChiusuraProcedimentoPresenter.MyView, RiepilogoAvvioChiusuraProcedimentoPresenter.MyProxy> implements RiepilogoAvvioChiusuraProcedimentoHandler {

	private String clientID;
	private SitemapMenu sitemapMenu;
	
	public interface MyView extends View {

		public void initRiepilogo(GestioneProcedimentoResult result);

		public void setChiudiCommand(final Command chiudiCommand);
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<RiepilogoAvvioChiusuraProcedimentoPresenter> {
	}

	@Inject
	public RiepilogoAvvioChiusuraProcedimentoPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final SitemapMenu sitemapMenu) {
		super(eventBus, view, proxy);
		this.sitemapMenu = sitemapMenu;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
		Window.scrollTo (0 ,0);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setChiudiCommand(new Command() {

			@Override
			public void execute() {
				sitemapMenu.removeSiteMapOpenVoice(clientID);
				Place place = new Place();
				place.setToken(NameTokens.dettagliofascicolo);
				place.addParam(NameTokensParams.idPratica, clientID);
				getEventBus().fireEvent(new GoToPlaceEvent(place));

			}
		});
	}

	@Override
	@ProxyEvent
	public void onRiepilogoAvvioChiusuraProcedimento(RiepilogoAvvioChiusuraProcedimentoEvent event) {
		getView().initRiepilogo(event.getGestioneProcedimentoResult());
		clientID = event.getIdFascicolo();
		revealInParent();
	}
}
