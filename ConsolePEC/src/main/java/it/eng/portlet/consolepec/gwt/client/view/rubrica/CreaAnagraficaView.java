package it.eng.portlet.consolepec.gwt.client.view.rubrica;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.ImportazioneLagAbilitazione;
import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import it.eng.cobo.consolepec.commons.rubrica.Anagrafica.Stato;
import it.eng.cobo.consolepec.commons.rubrica.PersonaFisica;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.presenter.rubrica.CreaAnagraficaPresenter;
import it.eng.portlet.consolepec.gwt.client.view.rubrica.widget.WidgetPersonaFisica;
import it.eng.portlet.consolepec.gwt.client.view.rubrica.widget.WidgetPersonaGiuridica;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;

/**
 * @author GiacomoFM
 * @since 16/ott/2017
 */
public class CreaAnagraficaView extends ViewImpl implements CreaAnagraficaPresenter.MyView {

	private final Widget widget;

	@UiField(provided = true)
	MessageAlertWidget messageAlertWidget;

	@UiField
	ListBox tipologiaAnagrafica;

	@UiField(provided = true)
	WidgetPersonaFisica creaPersonaFisicaWidget;

	@UiField(provided = true)
	WidgetPersonaGiuridica creaPersonaGiuridicaWidget;

	@UiField
	Button indietroButton;

	@UiField
	Button creaButton;

	private static final String PERSONA_FISICA = "Persona fisica", PERSONA_GIURIDICA = "Persona giuridica";

	public interface Binder extends UiBinder<Widget, CreaAnagraficaView> {
		//
	}

	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	@Inject
	public CreaAnagraficaView(final Binder binder, final EventBus eventBus, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		messageAlertWidget = new MessageAlertWidget(eventBus);
		creaPersonaFisicaWidget = new WidgetPersonaFisica(profilazioneUtenteHandler.getSettoriUtente(false));
		creaPersonaGiuridicaWidget = new WidgetPersonaGiuridica(profilazioneUtenteHandler.getSettoriUtente(false));
		widget = binder.createAndBindUi(this);

		tipologiaAnagrafica.addItem(PERSONA_FISICA);
		tipologiaAnagrafica.addItem(PERSONA_GIURIDICA);

		tipologiaAnagrafica.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				creaPersonaFisicaWidget.setVisible(tipologiaAnagrafica.getSelectedValue().equals(PERSONA_FISICA));
				creaPersonaGiuridicaWidget.setVisible(tipologiaAnagrafica.getSelectedValue().equals(PERSONA_GIURIDICA));
			}
		});

		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setImportaLagCommand(com.google.gwt.user.client.Command command) {
		creaPersonaFisicaWidget.setImportaLagCommand(command);
	}

	@Override
	public void setIndietroCommand(final com.google.gwt.user.client.Command command) {
		indietroButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
	}

	@Override
	public void setCreaCommand(final Command<Void, Anagrafica> command) {
		creaButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				if (tipologiaAnagrafica.getSelectedValue().equals(PERSONA_FISICA)) {
					command.exe(creaPersonaFisicaWidget.get());
					return;
				}

				if (tipologiaAnagrafica.getSelectedValue().equals(PERSONA_GIURIDICA)) {
					command.exe(creaPersonaGiuridicaWidget.get());
					return;
				}
			}
		});
	}

	@Override
	public void reset() {
		creaPersonaFisicaWidget.reset();
		creaPersonaGiuridicaWidget.reset();

		if (profilazioneUtenteHandler.isAbilitato(ImportazioneLagAbilitazione.class)) {
			creaPersonaFisicaWidget.showImportaLagButton();
		} else {
			creaPersonaFisicaWidget.hideImportaLagButton();
		}

	}

	@Override
	public String getCodiceFiscale() {
		return (creaPersonaFisicaWidget == null) ? null : creaPersonaFisicaWidget.getCodiceFiscale();
	}

	@Override
	public void prePopola(Anagrafica anagrafica) {
		if (anagrafica instanceof PersonaFisica) {
			anagrafica.setStato(Stato.ATTIVA);
			creaPersonaFisicaWidget.set((PersonaFisica) anagrafica);
		}
	}

}
