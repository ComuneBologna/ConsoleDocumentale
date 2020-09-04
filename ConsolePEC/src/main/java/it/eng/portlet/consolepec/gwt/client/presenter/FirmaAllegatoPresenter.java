package it.eng.portlet.consolepec.gwt.client.presenter;

import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeFirmaDigitale;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.event.firmadigitale.FirmaDigitaleFineEvent;
import it.eng.portlet.consolepec.gwt.client.event.firmadigitale.FirmaDigitaleInizioEvent;
import it.eng.portlet.consolepec.gwt.client.event.firmadigitale.FirmaDigitaleInizioEvent.FirmaDigitaleInizioHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.firma.CredenzialiFirma;
import it.eng.portlet.consolepec.gwt.shared.action.firma.SendOTPRequest;
import it.eng.portlet.consolepec.gwt.shared.action.firma.SendOTPRequestResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.TipologiaFirma;

public class FirmaAllegatoPresenter extends Presenter<FirmaAllegatoPresenter.MyView, FirmaAllegatoPresenter.MyProxy> implements FirmaDigitaleInizioHandler {

	public interface MyView extends View {

		void setChiudiPageFirmaCommand(Command command);

		void setFirmaCommand(Command command);

		String getPassword();

		void setPassword(String password);

		String getUserName();

		void setUserName(String username);

		void setSalvaCredenziali(boolean salvaCredenziali);

		String getOtp();

		void focusOtpField();

		void reset();

		void setOTPSMSRequestCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, OTPCredentialTypeEnum> command);

		boolean isCredenzialiUserModificate();

		boolean isCredenzialiPasswordModificate();

		boolean isSalvaCredenziali();

		TipologiaFirma getSelectedTipologiaFirma();

		void addTipologiaFirma(String tipologia);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.firmaallegato)
	public interface MyProxy extends ProxyPlace<FirmaAllegatoPresenter> {}

	public enum OTPCredentialTypeEnum implements IsSerializable {
		ARUBACALL, SMS
	};

	private final DispatchAsync dispatcher;
	private final EventBus eventBus;
	private Object openingRequestor;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	@Inject
	public FirmaAllegatoPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(eventBus, view, proxy);
		this.eventBus = eventBus;
		this.dispatcher = dispatcher;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setOTPSMSRequestCommand(new SendOTPRequestCommand());
		getView().setChiudiPageFirmaCommand(new AnnullaCommand());
		getView().setFirmaCommand(new FirmaDocumentoCommand());
	}

	@Override
	@ProxyEvent
	public void onFirmaDigitaleStart(FirmaDigitaleInizioEvent event) {
		ShowMessageEvent showMessageEvent = new ShowMessageEvent();
		showMessageEvent.setMessageDropped(true);
		eventBus.fireEvent(showMessageEvent);
		getView().reset();
		this.openingRequestor = event.getOpeningRequestor();
		popolaTipoFirma(event.getAllegati());
		PreferenzeFirmaDigitale pf = profilazioneUtenteHandler.getPreferenzeFirmaDigitaleUtente();
		if (pf != null) {
			getView().setUserName(pf.getUsername());
			getView().setPassword(pf.getPassword());
			getView().setSalvaCredenziali(true);
		}

		revealInParent();
	}

	private void popolaTipoFirma(Set<AllegatoDTO> allegati) {
		boolean pades = true;
		Iterator<AllegatoDTO> iterator = allegati.iterator();

		while (iterator.hasNext()) {
			AllegatoDTO allegato = iterator.next();
			// XXX Controllare sul tipo di file e non solo sull'estensione -->
			// aggiungere flag ad allegatodto se firmabile pades/cades popolato dal server
			if (!allegato.getNome().toLowerCase().endsWith(".pdf") || (allegato.isFirmato() && TipologiaFirma.CADES.equals(allegato.getTipologiaFirma()))
					|| (allegato.isFirmatoHash() && TipologiaFirma.CADES.equals(allegato.getTipologiaFirma()))) {
				pades = false;
				break;
			}
		}

		if (pades)
			getView().addTipologiaFirma(TipologiaFirma.PADES.getLabel());

		getView().addTipologiaFirma(TipologiaFirma.CADES.getLabel());

	}

	public class FirmaDocumentoCommand implements com.google.gwt.user.client.Command {

		@Override
		public void execute() {

			String password = getView().getPassword();
			String userName = getView().getUserName();
			String otp = getView().getOtp();
			TipologiaFirma tipoFirma = getView().getSelectedTipologiaFirma();

			if (password != null && userName != null && otp != null && tipoFirma != null) {

				CredenzialiFirma cf = new CredenzialiFirma();
				cf.setOtp(otp);
				cf.setPassword(password);
				cf.setUsername(userName);
				cf.setSalvaCredenziali(getView().isSalvaCredenziali());
				cf.setCredenzialePasswordModificata(getView().isCredenzialiPasswordModificate());
				cf.setCredenzialeUsernameModificata(getView().isCredenzialiUserModificate());

				FirmaDigitaleFineEvent e = new FirmaDigitaleFineEvent(openingRequestor, cf, getView().getSelectedTipologiaFirma());

				eventBus.fireEvent(e);

			}
		}
	}

	public class AnnullaCommand implements Command {

		@Override
		public void execute() {
			eventBus.fireEvent(new FirmaDigitaleFineEvent(openingRequestor, true));
		}

	}

	public class SendOTPRequestCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, OTPCredentialTypeEnum> {

		@Override
		public Void exe(OTPCredentialTypeEnum otpCredentialTypeEnum) {
			ShowAppLoadingEvent.fire(FirmaAllegatoPresenter.this, true);

			// reset dei messaggi di errore
			ShowMessageEvent event = new ShowMessageEvent();
			event.setMessageDropped(true);
			eventBus.fireEvent(event);

			SendOTPRequest sendOTPRequest = new SendOTPRequest();
			sendOTPRequest.setPassword(getView().getPassword());
			sendOTPRequest.setUsername(getView().getUserName());
			sendOTPRequest.setCredentialType(otpCredentialTypeEnum.toString());
			dispatcher.execute(sendOTPRequest, new AsyncCallback<SendOTPRequestResult>() {

				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(FirmaAllegatoPresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);
				}

				@Override
				public void onSuccess(SendOTPRequestResult result) {
					ShowAppLoadingEvent.fire(FirmaAllegatoPresenter.this, false);
					if (result.isError()) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(result.getMessage());
						eventBus.fireEvent(event);
					} else {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setInfoMessage("Richiesta inoltrata con successo");
						eventBus.fireEvent(event);
						FirmaAllegatoPresenter.this.getView().focusOtpField();
					}
				}
			});

			return null;
		}
	}

	@Override
	protected void onHide() {
		super.onHide();

		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);
	}
}
