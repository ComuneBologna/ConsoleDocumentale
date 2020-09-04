package it.eng.portlet.consolepec.gwt.client.presenter.cartellafirma;

import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeCartellaAttivita;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.NotificaTaskFirmaFineEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.NotificaTaskFirmaInizioEvent;
import it.eng.portlet.consolepec.gwt.client.event.cartellafirma.NotificaTaskFirmaInizioEvent.NotificaTaskFirmaInizioHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.InformazioniNotificaTaskFirmaDTO;

import java.util.List;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

/**
 * 
 * @author biagiot
 *
 */
public class NotificaTaskFirmaPresenter extends Presenter<NotificaTaskFirmaPresenter.MyView, NotificaTaskFirmaPresenter.MyProxy> implements NotificaTaskFirmaInizioHandler {

	private EventBus eventBus;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;
	
	public interface MyView extends View {
		List<String> getIndirizziNotifica();
		String getTextFromInputListWidgetDestinatari();
		String getNote();
		boolean isRicordaSceltaEnabled();
		void reset();
		void prevalorizzaForm(PreferenzeCartellaAttivita preferenzeCartellaAttivita);
		List<String> controllaForm();
		void showErrors(List<String> errors);
		void setTitolo(String titolo);
		void setConfermaCommand(Command command);
		void setAnnullaCommand(Command command);
	}
	
	@ProxyCodeSplit
	public interface MyProxy extends Proxy<NotificaTaskFirmaPresenter>{

	}
	
	@Inject
	public NotificaTaskFirmaPresenter(EventBus eventBus, MyView view, MyProxy proxy, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(eventBus, view, proxy);
		this.eventBus = eventBus;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}
	
	@Override
	protected void onBind() {
		super.onBind();
		getView().setTitolo("Notifica operazione");
		getView().setConfermaCommand(new ConfermaCommand());
		getView().setAnnullaCommand(new AnnullaCommand());
	}

	@Override
	protected void onHide() {
		super.onHide();
		getView().reset();
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);
	}
	
	@Override
	@ProxyEvent
	public void onNotificaTaskFirmaInizio(NotificaTaskFirmaInizioEvent event) {
		getView().prevalorizzaForm(profilazioneUtenteHandler.getPreferenzeUtente().getPreferenzeCartellaAttivita());
		revealInParent();
	}
	
	public class ConfermaCommand implements Command {

		@Override
		public void execute() {
			List<String> errors = getView().controllaForm();
			
			if (errors.isEmpty()) {
				InformazioniNotificaTaskFirmaDTO infoTask = new InformazioniNotificaTaskFirmaDTO();
				infoTask.setNote(getView().getNote());
				infoTask.setRicordaScelta(getView().isRicordaSceltaEnabled());
				infoTask.getIndirizziNotifica().addAll(getView().getIndirizziNotifica());
				
				NotificaTaskFirmaFineEvent.fire(NotificaTaskFirmaPresenter.this, infoTask);
				
			} else 
				getView().showErrors(errors);
		}
	}
	
	public class AnnullaCommand implements Command {

		@Override
		public void execute() {
			NotificaTaskFirmaFineEvent.fire(NotificaTaskFirmaPresenter.this, true);
		}
	}
}
