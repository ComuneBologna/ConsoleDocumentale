package it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event.BackToDettaglioComunicazioneEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event.MostraInviaCsvTestComunicazioneEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event.MostraInviaCsvTestComunicazioneEvent.MostraInviaCsvTestComunicazioneHandler;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.inviomassivo.NuovoInvioComunicazioneAction;
import it.eng.portlet.consolepec.gwt.shared.action.inviomassivo.NuovoInvioComunicazioneActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class InviaCsvTestComunicazionePresenter extends Presenter<InviaCsvTestComunicazionePresenter.MyView, InviaCsvTestComunicazionePresenter.MyProxy> implements MostraInviaCsvTestComunicazioneHandler {
	
	private final EventBus eventBus;
	
	private final DispatchAsync dispatcher;
	private final PecInPraticheDB praticheDB;
	
	private final SitemapMenu sitemapMenu;
	
	
	private ComunicazioneDTO comunicazione;
	private AllegatoDTO allegato;
	
	public interface MyView extends View {

		public void clear();

		public void setAnnullaCommand(Command annullaCommand);

		public void setAvantiCommand(Command avantCommand);
		
		public String getDestinatario();
		
		public Integer getNumeroInvii();
		
		public boolean controllaCampi();
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<InviaCsvTestComunicazionePresenter> {
	}

	@Inject
	public InviaCsvTestComunicazionePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher, PecInPraticheDB db, SitemapMenu sitemapMenu) {

		super(eventBus, view, proxy);
		this.eventBus = eventBus;
		this.dispatcher = dispatcher;
		this.praticheDB = db;
		this.sitemapMenu = sitemapMenu;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);

	}

	@Override
	protected void onBind() {
		super.onBind();
		
		getView().setAnnullaCommand(new AnnullaCommand());
		getView().setAvantiCommand(new AvantiCommand());
		
	}

	@Override
	protected void onHide() {
		super.onHide();
		dropMessage();
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);
		
		getView().clear();
		revealInParent();
		
	}

	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		
		
		
	}



	

	
	
	private void dropMessage() {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);
	}


		
	
	private class AnnullaCommand implements Command{

		@Override
		public void execute() {
			eventBus.fireEvent(new BackToDettaglioComunicazioneEvent());
		}
		
	}
	
	private class AvantiCommand implements Command{

		
		@Override
		public void execute() {
			
			if(getView().controllaCampi()){
				
				ShowAppLoadingEvent.fire(InviaCsvTestComunicazionePresenter.this, true);
				NuovoInvioComunicazioneAction action = new NuovoInvioComunicazioneAction(comunicazione, allegato, getView().getNumeroInvii(), getView().getDestinatario());
				dispatcher.execute(action, new AsyncCallback<NuovoInvioComunicazioneActionResult>() {

					@Override
					public void onFailure(Throwable caught) {
						ShowAppLoadingEvent.fire(InviaCsvTestComunicazionePresenter.this, false);
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						eventBus.fireEvent(event);
					}

					
					@Override
					public void onSuccess(NuovoInvioComunicazioneActionResult result) {
						ShowAppLoadingEvent.fire(InviaCsvTestComunicazionePresenter.this, false);
						if (!result.getError()) {
							praticheDB.update(result.getComunicazione().getClientID(), result.getComunicazione(), sitemapMenu.containsLink(result.getComunicazione().getClientID()));
							eventBus.fireEvent(new BackToDettaglioComunicazioneEvent());
						} else {
							ShowMessageEvent event = new ShowMessageEvent();
							event.setErrorMessage(result.getMessError());
							eventBus.fireEvent(event);
						}
					}

				});
				
			}
			
			
		
		}
		
	}

	@Override
	@ProxyEvent
	public void onMostraInviaCsvTestComunicazione(MostraInviaCsvTestComunicazioneEvent event) {
		this.comunicazione = event.getComunicazione();
		this.allegato = event.getAllegato();
		
		revealInParent();
	}
	
}
