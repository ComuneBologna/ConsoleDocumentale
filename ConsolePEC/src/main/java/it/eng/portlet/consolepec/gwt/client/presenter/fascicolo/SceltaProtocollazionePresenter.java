package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.event.BackToFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.MostraSceltaProtocollazioneEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.MostraSceltaProtocollazioneEvent.MostraSceltaProtocollazioneHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.BackToFascicoloCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.IBackToFascicolo;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.AggiornaPG;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.AggiornaPGResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollato;

import java.util.List;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class SceltaProtocollazionePresenter extends Presenter<SceltaProtocollazionePresenter.MyView, SceltaProtocollazionePresenter.MyProxy> implements IBackToFascicolo, MostraSceltaProtocollazioneHandler{

	private String fascicoloPath;
	
	private PecInPraticheDB praticheDB; 
	private DispatchAsync dispatchAsync; 
	private PlaceManager placeManager;
	
	public interface MyView extends View {
		
		void setAnnullaCommand(Command annullaCommand);
		
		void setConfermaCommand(Command confermaCommand);

		void mostraProtocollazioni(FascicoloDTO fascicolo);

		List<ElementoGruppoProtocollato> getProtocollazioniSelezionate();

	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<SceltaProtocollazionePresenter> {
		//
	}

	@Inject
	public SceltaProtocollazionePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final PecInPraticheDB praticheDB, final DispatchAsync dispatchAsync, final PlaceManager placeManager) {
		super(eventBus, view, proxy);
		this.praticheDB = praticheDB;
		this.dispatchAsync = dispatchAsync;
		this.placeManager = placeManager;
	}
	
	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setAnnullaCommand(new BackToFascicoloCommand<SceltaProtocollazionePresenter>(this));
		getView().setConfermaCommand(new Command() {
			
			@Override
			public void execute() {
				
				AggiornaPG action = new AggiornaPG(fascicoloPath, getView().getProtocollazioniSelezionate());
				
				ShowAppLoadingEvent.fire(SceltaProtocollazionePresenter.this, true);
				dispatchAsync.execute(action, new AsyncCallback<AggiornaPGResult>(){

					@Override
					public void onFailure(Throwable arg0) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getEventBus().fireEvent(event);
						ShowAppLoadingEvent.fire(SceltaProtocollazionePresenter.this, false);
					}

					@Override
					public void onSuccess(AggiornaPGResult result) {
						ShowAppLoadingEvent.fire(SceltaProtocollazionePresenter.this, false);
						if(result.isError()){
							ShowMessageEvent event = new ShowMessageEvent();
							event.setErrorMessage(result.getMessageError());
							getEventBus().fireEvent(event);
						} else {
							praticheDB.remove(fascicoloPath);
							getEventBus().fireEvent(new BackToFascicoloEvent(fascicoloPath));
						}
					}
				});
				
				
				
				
			}
		});
	}

	@Override
	@ProxyEvent
	public void onMostraSceltaProtocollazione(MostraSceltaProtocollazioneEvent event) {
		fascicoloPath = event.getFascicoloPath();
		praticheDB.getFascicoloByPath(fascicoloPath, false, new PraticaFascicoloLoaded() {

			@Override
			public void onPraticaLoaded(FascicoloDTO fascicolo) {
				getView().mostraProtocollazioni(fascicolo);
				revealInParent();
			}

			@Override
			public void onPraticaError(String error) {
				ShowAppLoadingEvent.fire(SceltaProtocollazionePresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getEventBus().fireEvent(event);
			}
			
			
		});
		
	}
	
	@Override
	public com.google.web.bindery.event.shared.EventBus _getEventBus() {
		return getEventBus();
	}

	@Override
	public DispatchAsync getDispatchAsync() {
		return dispatchAsync;
	}

	@Override
	public PlaceManager getPlaceManager() {
		return placeManager;
	}

	@Override
	public PecInPraticheDB getPecInPraticheDB() {
		return praticheDB;
	}

	@Override
	public String getFascicoloPath() {
		return fascicoloPath;
	}

}
