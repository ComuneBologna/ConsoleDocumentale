package it.eng.portlet.consolepec.gwt.client.presenter.pecout;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.event.ApriInvioCSVEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.ApriInvioCSVEvent.ApriInvioCSVHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.event.SceltaTemplateFromInvioCSV;
import it.eng.portlet.consolepec.gwt.shared.model.InvioDaCsvBean;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class InvioMailDaCSVPresenter extends Presenter<InvioMailDaCSVPresenter.MyView, InvioMailDaCSVPresenter.MyProxy> implements ApriInvioCSVHandler {

	private EventBus eventBus;
	private String clientIdFascicolo;
	private String nomeAllegato;

	private ProfilazioneUtenteHandler profilazioneUtenteHandler;
	private ConfigurazioniHandler configurazioniHandler;

	@Inject
	public InvioMailDaCSVPresenter(EventBus eventBus, MyView view, MyProxy proxy, ProfilazioneUtenteHandler profilazioneUtenteHandler, ConfigurazioniHandler configurazioniHandler) {
		super(eventBus, view, proxy);
		this.eventBus = eventBus;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		this.configurazioniHandler = configurazioniHandler;
	}

	public interface MyView extends View {

		void showErrors(List<String> errors);

		void clear();

		void setConfermaInvio(Command command);

		void setAnnullaInvio(Command command);

		Integer getHeader();

		Integer getPosizioneIndirizzo();

		Integer getPosizioneIdDocumentale();

		String getSeparatore();

		boolean isIndirizzoDestinatarioFromModello();

		String getAssegnatario();

		void setAssegnatariSuggestValues(List<AnagraficaRuolo> gruppi);

		void setSeparatoriListValues(List<String> separatori);

		boolean isPreValidazione();
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<InvioMailDaCSVPresenter> {}

	@Override
	@ProxyEvent
	public void onApriInvioCsv(final ApriInvioCSVEvent event) {
		ShowMessageEvent showMessageEvent = new ShowMessageEvent();
		showMessageEvent.setMessageDropped(true);
		this.eventBus.fireEvent(showMessageEvent);
		getView().clear();
		this.clientIdFascicolo = event.getClientIdFascicolo();
		this.nomeAllegato = event.getNomeAllegato();
		revealInParent();
	}

	@Override
	protected void onHide() {
		super.onHide();
		getView().clear();
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		Window.scrollTo(0, 0);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setConfermaInvio(new ConfermaInvioCommand());
		getView().setAnnullaInvio(new AnnullaInvioCommand());
		getView().setAssegnatariSuggestValues(profilazioneUtenteHandler.getDatiUtente().getAnagraficheRuoli());
		getView().setSeparatoriListValues(Separatore.getDescrizioni());
	}

	@AllArgsConstructor
	private static enum Separatore {
		TAB("Tabulazione", '\t'), SPAZIO("Spazio", ' '), PUNTO_E_VIRGOLA("Punto e virgola", ';'), PUNTO("Punto", '.'), PIPE("Pipe", '|');

		@Getter
		private String descrizione;

		@Getter
		private char separator;

		public static List<String> getDescrizioni() {
			List<String> res = new ArrayList<String>();
			for (Separatore s : Separatore.values()) {
				res.add(s.getDescrizione());
			}
			return res;
		}

		public static Separatore fromDescrizione(String descrizione) {
			for (Separatore s : Separatore.values()) {
				if (s.getDescrizione().equals(descrizione)) {
					return s;
				}
			}

			return null;
		}
	}

	private class AnnullaInvioCommand implements Command {

		@Override
		public void execute() {
			Place place = new Place();
			place.setToken(NameTokens.dettagliofascicolo);
			place.addParam(NameTokensParams.idPratica, clientIdFascicolo);
			place.addParam(NameTokensParams.resetComposizioneFascicolo, Boolean.toString(true));
			eventBus.fireEvent(new GoToPlaceEvent(place));
		}
	}

	private class ConfermaInvioCommand implements Command {

		@Override
		public void execute() {

			List<String> errors = validaForm();

			if (errors.isEmpty()) {
				eventBus.fireEvent(new SceltaTemplateFromInvioCSV(getBean()));

			} else {
				getView().showErrors(errors);
			}

		}
	}

	private InvioDaCsvBean getBean() {
		InvioDaCsvBean bean = new InvioDaCsvBean();
		Separatore separatore = Separatore.fromDescrizione(getView().getSeparatore());
		bean.setSeparatoreCSV(separatore.getSeparator());
		bean.setHeaderCSV(getView().getHeader());
		bean.setPosizioneIdDocumentaleFascicolo(getView().getPosizioneIdDocumentale());
		bean.setPosizioneIndirizzoEmailDestinatario(getView().getPosizioneIndirizzo());
		bean.setIndirizzoDestinatarioFromModello(getView().isIndirizzoDestinatarioFromModello());
		bean.setAssegnatario(getView().getAssegnatario());
		bean.setClientIdFascicolo(clientIdFascicolo);
		bean.setNomeAllegato(nomeAllegato);
		bean.setPreValidazione(getView().isPreValidazione());
		return bean;
	}

	private List<String> validaForm() {
		List<String> errors = new ArrayList<String>();

		if (getView().getAssegnatario() == null || getView().getAssegnatario().trim().isEmpty()) {
			errors.add("Gruppo assegnatario obbligatorio");

		} else {
			AnagraficaRuolo ar = configurazioniHandler.getAnagraficaRuoloByEtichetta(getView().getAssegnatario());
			if (ar == null) {
				errors.add("Gruppo assegnatario non valido");
			}
		}

		if (getView().getSeparatore() == null) {
			errors.add("Tipo separatore obbligatorio");
		}

		if (getView().getPosizioneIdDocumentale() == null) {
			errors.add("Specificare la colonna dell'identificativo documentale");
		}

		return errors;
	}
}
