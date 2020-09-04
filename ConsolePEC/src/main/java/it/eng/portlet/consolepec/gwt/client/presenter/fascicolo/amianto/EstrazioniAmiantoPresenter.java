package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.amianto;

import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.EstrazioneAmiantoAbilitazione;
import it.eng.portlet.consolepec.gwt.client.command.ConsolePecCommandBinder;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.amianto.estrazioni.EstrazioneCommand;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants.VociRootSiteMap;
import it.eng.portlet.consolepec.gwt.shared.model.amianto.EstrazioneAmiantoDTO;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.Command;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class EstrazioniAmiantoPresenter extends Presenter<EstrazioniAmiantoPresenter.MyView, EstrazioniAmiantoPresenter.MyProxy> implements ConsolePecCommandBinder {

	private final DispatchAsync dispatcher;
	private final SitemapMenu sitemapMenu;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	public interface MyView extends View {
		public boolean controllaCampiObbligatori();

		void setEstrazioneCommand(Command command);

		void pulisciCampi();

		EstrazioneAmiantoDTO getEstrazioneAmiantoDTO();

		void sendDownload(SafeUri uri);
		
		
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.estrazioniamianto)
	public interface MyProxy extends ProxyPlace<EstrazioniAmiantoPresenter> {
	}

	@Inject
	public EstrazioniAmiantoPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher, final SitemapMenu sitemap, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(eventBus, view, proxy);
		// Usate quando implemento il command
		this.dispatcher = dispatcher;
		//Usato per configurare il menu di navigazione
		this.sitemapMenu = sitemap;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setEstrazioneCommand(new EstrazioneCommand(this));
	}

	@Override
	protected void onHide() {
		super.onHide();
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getEventBus().fireEvent(event);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		revealInParent();
		sitemapMenu.setActiveVoce(VociRootSiteMap.ESTRAZIONI_AMIANTO.getId());
		// caricaPratica();
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getEventBus().fireEvent(event);
		
		getView().pulisciCampi();

	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		
		if (!profilazioneUtenteHandler.isAbilitato(EstrazioneAmiantoAbilitazione.class)) {
			throw new IllegalArgumentException("Utente non abilitato");
		}
	}

	
	@Override
	public EventBus _getEventBus() {
		return getEventBus();
	}

	@Override
	public DispatchAsync getDispatchAsync() {
		return dispatcher;
	}

	@Override
	public PlaceManager getPlaceManager() {
		return null;
	}

	@Override
	public PecInPraticheDB getPecInPraticheDB() {
		return null;
	}
}
