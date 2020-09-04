package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.portlet.consolepec.gwt.client.command.BackToPraticaCommand;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.operazioni.template.TemplateSceltaWizardApiClient;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToConfermaMailDaTemplateEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.GoToConfermaMailDaTemplateEvent.GoToConfermaMailDaTemplateHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.IBackToFascicolo;

import com.google.gwt.user.client.Command;
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

public class ConfermaMailDaTemplatePresenter extends Presenter<ConfermaMailDaTemplatePresenter.MyView, ConfermaMailDaTemplatePresenter.MyProxy> implements IBackToFascicolo, GoToConfermaMailDaTemplateHandler{

	private String fascicoloPath;
	
	private PecInPraticheDB praticheDB; 
	private DispatchAsync dispatchAsync; 
	private PlaceManager placeManager;
	private TemplateSceltaWizardApiClient templateSceltaWizard;
	
	public interface MyView extends View {

		void setAnnullaCommand(BackToPraticaCommand backToFascicoloCommand);

		void setConfermaCommand(Command nuovaMailDaTemplateCommand);
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<ConfermaMailDaTemplatePresenter> {
	}

	@Inject
	public ConfermaMailDaTemplatePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final PecInPraticheDB praticheDB, final DispatchAsync dispatchAsync, final PlaceManager placeManager, final TemplateSceltaWizardApiClient templateSceltaWizard) {
		super(eventBus, view, proxy);
		this.praticheDB = praticheDB;
		this.dispatchAsync = dispatchAsync;
		this.placeManager = placeManager;
		this.templateSceltaWizard = templateSceltaWizard;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setAnnullaCommand(new BackToPraticaCommand(templateSceltaWizard));
		getView().setConfermaCommand(new NuovaMailDaTemplateCommand());
	}

	@Override
	@ProxyEvent
	public void onGoToConfermaMailDaTemplate(GoToConfermaMailDaTemplateEvent event) {
		fascicoloPath = event.getFascicoloPath();
		revealInParent();
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
	
	private class NuovaMailDaTemplateCommand implements Command{

		@Override
		public void execute() {
			templateSceltaWizard.goToCreaDaTemplate(fascicoloPath, TipologiaPratica.MODELLO_MAIL);
		}
	}
}
