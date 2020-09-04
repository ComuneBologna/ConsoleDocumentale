package it.eng.portlet.consolepec.gwt.client.presenter.pec;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.BackFromPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.event.ChiudiCaricaAllegatiDaPraticaEvent;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class CaricaAllegatiDaPraticaPresenter extends Presenter<CaricaAllegatiDaPraticaPresenter.MyView, CaricaAllegatiDaPraticaPresenter.MyProxy>  {

	public interface MyView extends View {

		public void popolaAllegati(FascicoloDTO fascicolo);

		public void setCommandUploadAllegatoPraticheCollegate(Command command);

		public void setAnnullaCommand(Command command);
		
		public Map<String, List<AllegatoDTO>> getAllegatiSelezionati();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.caricaallegati)
	public interface MyProxy extends ProxyPlace<CaricaAllegatiDaPraticaPresenter> {
	}

	private final PecInPraticheDB pecInDb;
	private String idFascicolo;
	private String clientIdBozza;
	
	private final SitemapMenu siteMapMenu;

	@Inject
	public CaricaAllegatiDaPraticaPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final PecInPraticheDB pecInDb, final SitemapMenu siteMapMenu) {
		super(eventBus, view, proxy);
		this.pecInDb = pecInDb;
		this.siteMapMenu = siteMapMenu;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);

	}

	@Override
	protected void onReveal() {
		pecInDb.getFascicoloByPath(idFascicolo, siteMapMenu.containsLink(idFascicolo), new PraticaFascicoloLoaded() {

			@Override
			public void onPraticaLoaded(FascicoloDTO fascicolo) {
				getView().popolaAllegati(fascicolo);
			}

			@Override
			public void onPraticaError(String error) {
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getEventBus().fireEvent(event);
			}
		});

	}

	@Override
	public void onHide() {
		super.onHide();
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getEventBus().fireEvent(event);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setAnnullaCommand(new Command() {

			@Override
			public void execute() {
				getEventBus().fireEvent(new BackFromPlaceEvent());
			}
		});
		getView().setCommandUploadAllegatoPraticheCollegate(new Command() {

			@Override
			public void execute() {
				ChiudiCaricaAllegatiDaPraticaEvent event = new ChiudiCaricaAllegatiDaPraticaEvent(idFascicolo, clientIdBozza);
				for(String key : getView().getAllegatiSelezionati().keySet())
					event.getAllegati().put(key, getView().getAllegatiSelezionati().get(key));
				getEventBus().fireEvent(event);
				
			}
		});
	}

	@Override
	public void prepareFromRequest(com.gwtplatform.mvp.shared.proxy.PlaceRequest request) {
		super.prepareFromRequest(request);
		idFascicolo = request.getParameter(NameTokensParams.idPratica, null);
		clientIdBozza = request.getParameter(NameTokensParams.idEmail, null);
	}
		



}
