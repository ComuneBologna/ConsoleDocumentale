package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione;

import it.eng.portlet.consolepec.gwt.client.command.ConsolePecCommandBinder;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.event.confermaprotocollazione.ConfermaProtocollazionePecInEvent;
import it.eng.portlet.consolepec.gwt.client.event.confermaprotocollazione.ConfermaProtocollazionePecInEvent.ConfermaProtocollazionePecInHandler;
import it.eng.portlet.consolepec.gwt.client.event.confermaprotocollazione.ConfermaProtocollazionePecOutEvent;
import it.eng.portlet.consolepec.gwt.client.event.confermaprotocollazione.ConfermaProtocollazionePecOutEvent.ConfermaProtocollazionePecOutHandler;
import it.eng.portlet.consolepec.gwt.client.event.confermaprotocollazione.ConfermaProtocollazionePraticaModulisticaEvent;
import it.eng.portlet.consolepec.gwt.client.event.confermaprotocollazione.ConfermaProtocollazionePraticaModulisticaEvent.ConfermaProtocollazionePraticaModulisticaHandler;
import it.eng.portlet.consolepec.gwt.client.event.confermaprotocollazione.ConfermaProtocollazioneSceltaFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.event.confermaprotocollazione.ConfermaProtocollazioneSceltaFascicoloEvent.ConfermaProtocollazioneSceltaFascicoloHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.AbbandonaProtocollazioneEmailInCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.AbbandonaProtocollazioneEmailOutCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.AbbandonaProtocollazionePraticaModulisticaCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.AbbandonaProtocollazioneSceltaFascicoloCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.AnnullaProtocollazioneEmailInCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.AnnullaProtocollazioneEmailOutCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.AnnullaProtocollazionePraticaModulisticaCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.AnnullaProtocollazioneSceltaFascicoloCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.ConfermaProtocollazioneEmailInCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.ConfermaProtocollazioneEmailOutCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.ConfermaProtocollazionePraticaModulisticaCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.ConfermaProtocollazioneSceltaFascicoloCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.IndietroProtocollazioneEmailInCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.IndietroProtocollazioneEmailOutCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.IndietroProtocollazionePraticaModulisticaCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.IndietroProtocollazioneSceltaFascicoloCommand;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloDTO;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
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

public class ConfermaSceltaProtocollazionePresenter extends Presenter<ConfermaSceltaProtocollazionePresenter.MyView, ConfermaSceltaProtocollazionePresenter.MyProxy> implements ConfermaProtocollazionePecInHandler, ConfermaProtocollazionePecOutHandler, ConfermaProtocollazioneSceltaFascicoloHandler, ConfermaProtocollazionePraticaModulisticaHandler, ConsolePecCommandBinder {

	public interface MyView extends View {

		void setConfermaProtocollazioneCommand(Command command);

		void setAbbandonaProtocollazioneCommand(Command command);

		void setAnnullaProtocollazioneCommand(Command command);

		void setIndietroButtonCommand(Command command);

		void setTitle(String string);
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<ConfermaSceltaProtocollazionePresenter> {
	}

	private final EventBus eventBus;
	private final DispatchAsync dispatcher;

	private final PecInPraticheDB praticheInDB;

	private final SitemapMenu sitemapMenu;

	private String idEmailIn;
	private String idEmailOut;
	private String idFascicolo;
	private String idPraticaModulistica;
	private CreaFascicoloDTO creaFascicoloDTO;

	private Command indietroCommand;
	private Command annullaCommand;
	private Command abbandonaProtocollazioneCommand;
	private Command confermaCommand;
	private String provenienza;

	@Inject
	public ConfermaSceltaProtocollazionePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final PecInPraticheDB praticheInDB, final DispatchAsync dispatcher, final SitemapMenu sitemapMenu) {

		super(eventBus, view, proxy);
		this.eventBus = eventBus;
		this.dispatcher = dispatcher;
		this.praticheInDB = praticheInDB;
		this.sitemapMenu = sitemapMenu;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {

		super.onBind();
		getView().setConfermaProtocollazioneCommand(new Command() {
			@Override
			public void execute() {
				confermaCommand.execute();
			}
		});

		getView().setIndietroButtonCommand(new Command() {
			@Override
			public void execute() {
				indietroCommand.execute();
			}
		});

		getView().setAnnullaProtocollazioneCommand(new Command() {
			@Override
			public void execute() {
				annullaCommand.execute();
			}
		});

		getView().setAbbandonaProtocollazioneCommand(new Command() {
			@Override
			public void execute() {
				abbandonaProtocollazioneCommand.execute();
			}
		});
	}

	@Override
	protected void onHide() {
		super.onHide();
		dropMessage();
	}

	@Override
	protected void onReset() {

		super.onReset();
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		getView().setTitle("Conferma scelta protocollazione");
		Window.scrollTo(0, 0);
	}

	@Override
	protected void onUnbind() {
		super.onUnbind();
	}


	private void dropMessage() {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);
	}

	/**
	 * Gestisce la provenienza dalla form di scelta del fascicolo da associare
	 * alla pec in
	 */
	@Override
	@ProxyEvent
	public void onConfermaProtocollazioneSceltaFascicolo(ConfermaProtocollazioneSceltaFascicoloEvent event) {
		idEmailIn = event.getIdEmail();
		idPraticaModulistica = event.getIdPraticaModulistica();
		idFascicolo = event.getIdFascicolo();
		provenienza = event.getProvenienza();
		confermaCommand = new ConfermaProtocollazioneSceltaFascicoloCommand(this);
		indietroCommand = new IndietroProtocollazioneSceltaFascicoloCommand(this);
		annullaCommand = new AnnullaProtocollazioneSceltaFascicoloCommand(this);
		abbandonaProtocollazioneCommand = new AbbandonaProtocollazioneSceltaFascicoloCommand(this);
		revealInParent();
	}

	/**
	 * Gestisce la provenienza dalla maschera di creazione di una pec out
	 */
	@Override
	@ProxyEvent
	public void onConfermaProtocollazionePecOut(ConfermaProtocollazionePecOutEvent event) {
		idEmailOut = event.getIdEmailOut();
		idFascicolo = event.getIdFascicolo();
		confermaCommand = new ConfermaProtocollazioneEmailOutCommand(this);
		indietroCommand = new IndietroProtocollazioneEmailOutCommand(this);
		annullaCommand = new AnnullaProtocollazioneEmailOutCommand(this);
		abbandonaProtocollazioneCommand = new AbbandonaProtocollazioneEmailOutCommand(this);
		revealInParent();

	}

	/**
	 * Gestisce la provenienza dalla maschera di creazione del fascicolo a
	 * partire da email in
	 */
	@Override
	@ProxyEvent
	public void onConfermaProtocollazionePecIn(ConfermaProtocollazionePecInEvent event) {
		creaFascicoloDTO = event.getCreaFascicoloDTO();
		idEmailIn = event.getIdEmailIn();
		confermaCommand = new ConfermaProtocollazioneEmailInCommand(this);
		indietroCommand = new IndietroProtocollazioneEmailInCommand(this);
		annullaCommand = new AnnullaProtocollazioneEmailInCommand(this);
		abbandonaProtocollazioneCommand = new AbbandonaProtocollazioneEmailInCommand(this);
		revealInParent();
	}

	@Override
	@ProxyEvent
	public void onConfermaProtocollazionePraticaModulistica(ConfermaProtocollazionePraticaModulisticaEvent event) {
		creaFascicoloDTO = event.getCreaFascicoloDTO();
		idPraticaModulistica = event.getIdPraticaModulistica();
		confermaCommand = new ConfermaProtocollazionePraticaModulisticaCommand(this);
		indietroCommand = new IndietroProtocollazionePraticaModulisticaCommand(this);
		annullaCommand = new AnnullaProtocollazionePraticaModulisticaCommand(this);
		abbandonaProtocollazioneCommand = new AbbandonaProtocollazionePraticaModulisticaCommand(this);
		revealInParent();
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
		return getPlaceManager();
	}

	@Override
	public PecInPraticheDB getPecInPraticheDB() {
		return praticheInDB;
	}

	public String getIdEmailIn() {
		return idEmailIn;
	}

	public String getIdEmailOut() {
		return idEmailOut;
	}

	public String getIdFascicolo() {
		return idFascicolo;
	}

	public String getIdPraticaModulistica() {
		return idPraticaModulistica;
	}

	public CreaFascicoloDTO getCreaFascicoloDTO() {
		return creaFascicoloDTO;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public SitemapMenu getSitemapMenu() {
		return sitemapMenu;
	}

}