package it.eng.portlet.consolepec.gwt.client.presenter;

import it.eng.portlet.consolepec.gwt.client.event.AppTerminatedEvent;
import it.eng.portlet.consolepec.gwt.client.event.AppTerminatedEvent.AppTerminatedHandler;
import it.eng.portlet.consolepec.gwt.client.event.RichiestaConfermaAnnullaEvent;
import it.eng.portlet.consolepec.gwt.client.event.RichiestaConfermaAnnullaEvent.RichiestaConfermaAnnullaHandler;
import it.eng.portlet.consolepec.gwt.client.event.SceltaConfermaAnnullaEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.TerminaAttesaEvent;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class AppLoadingPresenter extends Presenter<AppLoadingPresenter.MyView, AppLoadingPresenter.MyProxy> implements ShowAppLoadingEvent.ShowAppLoadingHandler, AppTerminatedHandler, RichiestaConfermaAnnullaHandler {

	private static final String messaggioAttesa = "<h4> Terminare l'attesa? </h4>";
	private static final String messaggio = "<h4> Attendere prego... </h4>";
	private final TerminaCommand terminaCommand = new TerminaCommand();
	private String eventId;

	private int mills = 0;

	public interface MyView extends View {

		public void nascondi();

		public void mostra();

		public void setMessageHtml(HTML htmlMessage);

		public void setMessagePanel(HTMLPanel panel);

		public void showImageCaricamento(boolean b);

		public void setTerminaCommand(Command terminaCommand);

		public void setAttendiCommand(Command attendiCommand);

		public void setConfermaCommand(Command confermaCommand);

		public void setAnnullaCommand(Command annullaCommand);

		public void showTeminaAttendiButtons(boolean enabled);

		public void showConfermaAnnullaButtons(boolean enabled);
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<AppLoadingPresenter> {
	}

	private int numWait = 0;// conta quante wait sono chiamate. Al fine di rimuovere lo splash deve raggiungere <=0

	@Inject
	public AppLoadingPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetSplashScreenContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setAttendiCommand(new Command() {
			@Override
			public void execute() {
				new AttendiCommand().execute();
			}
		});

		getView().setTerminaCommand(new Command() {
			@Override
			public void execute() {
				TerminaAttesaEvent.fire(AppLoadingPresenter.this);
				new NascondiCommand().execute();
			}
		});

		getView().setAnnullaCommand(new Command() {

			@Override
			public void execute() {
				sceltaCancellazione(false);
			}

		});

		getView().setConfermaCommand(new Command() {

			@Override
			public void execute() {
				sceltaCancellazione(true);
			}
		});
	}

	@Override
	protected void onHide() {
		super.onHide();

	}

	@Override
	protected void onReset() {
		super.onReset();
	}

	@Override
	protected void onReveal() {
		super.onReveal();

	}

	@Override
	protected void onUnbind() {
		super.onUnbind();
	}

	@Override
	@ProxyEvent
	public void onAppLoading(ShowAppLoadingEvent event) {
		getView().setMessageHtml(new HTML(messaggio));
		getView().showTeminaAttendiButtons(false);
		getView().showConfermaAnnullaButtons(false);
		getView().showImageCaricamento(true);
		boolean complete = event.isComplete();
		mills = event.showMessageAlfterTimeOut();
		if (mills != 0) {
			terminaCommand.setShowForm(true);
			Scheduler.get().scheduleFixedPeriod(terminaCommand, event.showMessageAlfterTimeOut());
		}
		revealInParent();
		if (complete) {
			terminaCommand.setShowForm(false);
			nascondi();
		} else {
			mostra();
		}

	}

	private void sceltaCancellazione(boolean conferma) {
		SceltaConfermaAnnullaEvent.fire(AppLoadingPresenter.this, conferma, eventId);
		eventId = null;
		getView().nascondi();
	}

	private void mostra() {
		numWait++;
		getView().mostra();
	}

	private void nascondi() {
		numWait--;
		if (numWait <= 0) {
			numWait = 0;
			getView().nascondi();
		}
	}

	/* Mostra il messaggio e i button per annullare l'attesa */
	class TerminaCommand implements RepeatingCommand {

		private boolean showForm;

		public boolean isShowForm() {
			return showForm;
		}

		public void setShowForm(boolean showForm) {
			this.showForm = showForm;
		}

		public TerminaCommand() {
		}

		@Override
		public boolean execute() {
			if (numWait <= 1 && showForm) {
				getView().setMessageHtml(new HTML(messaggioAttesa));
				getView().showTeminaAttendiButtons(true);
			}
			return false;
		}

	}

	/* comando eseguito quando si vuol continuare ad attendere e nascondere il messaggio */
	class AttendiCommand implements Command {

		public AttendiCommand() {
		}

		@Override
		public void execute() {
			getView().setMessageHtml(new HTML(messaggio));
			getView().showTeminaAttendiButtons(false);
			if (mills != 0)
				Scheduler.get().scheduleFixedPeriod(new TerminaCommand(), mills);
		}

	}

	class NascondiCommand implements Command {

		@Override
		public void execute() {
			nascondi();
		}
	}

	@ProxyEvent
	@Override
	public void onAppTerminated(AppTerminatedEvent event) {
		getView().setMessagePanel(event.getMessagePanel());
		getView().showImageCaricamento(false);
		mostra();
	}

	@ProxyEvent
	@Override
	public void onRichiestaConfermaAnnulla(RichiestaConfermaAnnullaEvent event) {
		if (eventId != null)
			throw new RuntimeException();
		eventId = event.getEventId();
		getView().setMessagePanel(new HTMLPanel(event.getMessage()));
		getView().showConfermaAnnullaButtons(true);
		getView().showTeminaAttendiButtons(false);
		getView().showImageCaricamento(false);
		getView().mostra();
	}

}
