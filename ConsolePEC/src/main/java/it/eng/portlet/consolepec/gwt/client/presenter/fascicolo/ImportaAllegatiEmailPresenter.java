package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import it.eng.portlet.consolepec.gwt.client.command.ConsolePecCommandBinder;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.event.MostraAllegatiEmailEvent;
import it.eng.portlet.consolepec.gwt.client.event.MostraAllegatiEmailEvent.MostraAllegatiEmailHandler;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.AnnullaImportaAllegatiCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.ImportaAllegatiCommand;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;

public class ImportaAllegatiEmailPresenter extends Presenter<ImportaAllegatiEmailPresenter.MyView, ImportaAllegatiEmailPresenter.MyProxy> implements MostraAllegatiEmailHandler, ConsolePecCommandBinder {

	private final Set<PecInDTO> listaPecIn = new HashSet<>();
	private final DispatchAsync dispatcher;

	private String clientID = null;

	public interface MyView extends View {

		void popolaAllegatiEmail(Set<PecInDTO> pecInList);

		void setAnnullaCommand(com.google.gwt.user.client.Command command);

		void setImportaAllegatiCommand(com.google.gwt.user.client.Command command);

		Map<String, List<AllegatoDTO>> getAllegatiEmailSelezionati();

		void restSelezioni();
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<ImportaAllegatiEmailPresenter> {/**/}

	@Inject
	public ImportaAllegatiEmailPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		this.dispatcher = dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		Window.scrollTo(0, 0);
		getView().popolaAllegatiEmail(listaPecIn);
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
		getView().setAnnullaCommand(new AnnullaImportaAllegatiCommand(this));
		getView().setImportaAllegatiCommand(new ImportaAllegatiCommand(this));
	}

	@Override
	@ProxyEvent
	public void onMostraAllegatiEmail(final MostraAllegatiEmailEvent event) {
		listaPecIn.clear();
		for (PecInDTO pec : event.getListaMail()) {
			listaPecIn.add(pec);
		}
		clientID = event.getFascicoloPath();
		revealInParent();
	}

	public String getClientID() {
		return clientID;
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

	@Override
	public EventBus _getEventBus() {
		return getEventBus();
	}

}
