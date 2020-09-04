package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.event.BackToFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToModificaAbilitazioniAssegnaEsternoFromDettaglioFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToModificaAbilitazioniAssegnaEsternoFromDettaglioFascicoloEvent.GoToModificaAbilitazioniAssegnaEsternoFromDettaglioFascicoloHandler;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.ModificaAbilitazioniAssegnaUtenteEsternoAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.ModificaAbilitazioniAssegnaUtenteEsternoResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.resources.client.ImageResource;
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

public class ModificaAbilitazioniAssegnaUtenteEsternoPresenter extends Presenter<ModificaAbilitazioniAssegnaUtenteEsternoPresenter.MyView, ModificaAbilitazioniAssegnaUtenteEsternoPresenter.MyProxy> implements GoToModificaAbilitazioniAssegnaEsternoFromDettaglioFascicoloHandler {

	private String fascicoloPath;
	private final PecInPraticheDB praticheDB;
	private final DispatchAsync dispatcher;
	
	public interface MyView extends View {

		void popolaOperazioniFascicolo(String titolo, ImageResource icona, HashMap<String, Boolean> operazioni, Command onSelezioneCommand);

		void setAnnullaCommand(Command command);

		void setConfermaCommand(Command command);

		List<String> getOperazioni();

		void setDestinatario(List<String> destinatari);

		void setDataNotifica(String dataNotifica);
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<ModificaAbilitazioniAssegnaUtenteEsternoPresenter> {
	}

	
	@Inject
	public ModificaAbilitazioniAssegnaUtenteEsternoPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final PecInPraticheDB praticheDB,  final DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		this.praticheDB = praticheDB;
		this.dispatcher = dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		
		getView().setAnnullaCommand(new AnnullaCommand());
		getView().setConfermaCommand(new ConfermaCommand());
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		
		ShowAppLoadingEvent.fire(ModificaAbilitazioniAssegnaUtenteEsternoPresenter.this, true);
		praticheDB.getFascicoloByPath(fascicoloPath, false, new PraticaFascicoloLoaded() {

			@Override
			public void onPraticaLoaded(FascicoloDTO fascicolo) {
				ShowAppLoadingEvent.fire(ModificaAbilitazioniAssegnaUtenteEsternoPresenter.this, false);
				HashMap<String, Boolean> mappaCompletaOperazioni = new HashMap<String, Boolean>();
				
				for (String operazione : fascicolo.getOperazioni()) {
					boolean selezionato = fascicolo.getAssegnazioneEsterna().getOperazioni().contains(operazione);
					mappaCompletaOperazioni.put(operazione, selezionato );
				}
				
				ImageResource icona = ConsolePECIcons._instance.fascicolo();
				String titolo = fascicolo.getTitolo() +  " (" + fascicolo.getNumeroRepertorio() + ")";
				
				getView().popolaOperazioniFascicolo(titolo, icona, mappaCompletaOperazioni, new Command() {

					@Override
					public void execute() {
						// TODO: cosa?
					}
				});
				
				getView().setDataNotifica(fascicolo.getAssegnazioneEsterna().getDataNotifica());
				getView().setDestinatario(fascicolo.getAssegnazioneEsterna().getDestinatari());
			}

			@Override
			public void onPraticaError(String error) {
				ShowAppLoadingEvent.fire(ModificaAbilitazioniAssegnaUtenteEsternoPresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getEventBus().fireEvent(event);
			}
			
		});
		
		
		
		
		
		
		
		

	}
	@Override
	@ProxyEvent
	public void onGoToModificaAbilitazioniAssegnaEsternoFromDettaglioFascicolo(GoToModificaAbilitazioniAssegnaEsternoFromDettaglioFascicoloEvent event) {
		fascicoloPath = event.getIdFascicolo();
		revealInParent();
	}
	
	
	private class AnnullaCommand implements Command {

		@Override
		public void execute() {
			getEventBus().fireEvent(new BackToFascicoloEvent(fascicoloPath));			
		}
		
	}
	
	private class ConfermaCommand implements Command {

		@Override
		public void execute() {
			
			List<String> operazioni = getView().getOperazioni();
			ModificaAbilitazioniAssegnaUtenteEsternoAction action = new ModificaAbilitazioniAssegnaUtenteEsternoAction(fascicoloPath, operazioni);
			
			dispatcher.execute(action, new AsyncCallback<ModificaAbilitazioniAssegnaUtenteEsternoResult>() {

				
				@Override
				public void onSuccess(ModificaAbilitazioniAssegnaUtenteEsternoResult res) {
					if(res.isError() == false) {
						praticheDB.remove(fascicoloPath);
						getEventBus().fireEvent(new BackToFascicoloEvent(fascicoloPath));	
					} else {
						ShowAppLoadingEvent.fire(ModificaAbilitazioniAssegnaUtenteEsternoPresenter.this, false);
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getEventBus().fireEvent(event);
					}
					
					
				}
				
				@Override
				public void onFailure(Throwable t) {
					ShowAppLoadingEvent.fire(ModificaAbilitazioniAssegnaUtenteEsternoPresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(event);
				}

				
			});

		}
		
	}
}
