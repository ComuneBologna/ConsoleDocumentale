package it.eng.portlet.consolepec.gwt.client.view.rubrica;

import java.util.Collections;

import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ConsoleDisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.profilazione.Azione;
import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import it.eng.cobo.consolepec.commons.rubrica.PersonaFisica;
import it.eng.cobo.consolepec.commons.rubrica.PersonaGiuridica;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.cobo.consolepec.util.console.SettoriUtils;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.presenter.rubrica.DettaglioAnagraficaPresenter;
import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.view.rubrica.widget.WidgetPersonaFisica;
import it.eng.portlet.consolepec.gwt.client.view.rubrica.widget.WidgetPersonaGiuridica;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;

/**
 * @author GiacomoFM
 * @since 15/set/2017
 */
public class DettaglioAnagraficaView extends ViewImpl implements DettaglioAnagraficaPresenter.MyView {

	private final Widget widget;

	@UiField(provided = true)
	MessageAlertWidget messageAlertWidget;

	@UiField(provided = true)
	WidgetPersonaFisica personaFisicaWidget;

	@UiField
	HTMLPanel azioniPanel;
	@UiField(provided = true)
	ConsoleDisclosurePanel azioniDisclosure = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Azioni");

	@UiField(provided = true)
	WidgetPersonaGiuridica personaGiuridicaWidget;

	private boolean personaFisica;

	private double id;

	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	@UiField
	Button indietroButton;

	@UiField
	Button modificaButton;

	@UiField
	Button eliminaButton;

	public interface Binder extends UiBinder<Widget, DettaglioAnagraficaView> {
		//
	}

	@Inject
	public DettaglioAnagraficaView(final Binder binder, final EventBus eventBus, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		messageAlertWidget = new MessageAlertWidget(eventBus);
		personaFisicaWidget = new WidgetPersonaFisica(profilazioneUtenteHandler.getSettoriUtente(false));
		personaGiuridicaWidget = new WidgetPersonaGiuridica(profilazioneUtenteHandler.getSettoriUtente(false));
		widget = binder.createAndBindUi(this);

		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		Window.scrollTo(0, 0);
	}

	@Override
	public void mostraDettaglioAnagrafica(Anagrafica anagrafica) {
		id = anagrafica.getId();
		if (anagrafica instanceof PersonaFisica) {
			personaFisicaWidget.set((PersonaFisica) anagrafica);
			personaFisica = true;
		}
		if (anagrafica instanceof PersonaGiuridica) {
			personaGiuridicaWidget.set((PersonaGiuridica) anagrafica);
			personaFisica = false;
		}
		personaFisicaWidget.setVisible(personaFisica);
		personaGiuridicaWidget.setVisible(!personaFisica);

		eliminaButton.setEnabled(controlloGruppi(anagrafica));

		azioniDisclosure.setOpen(false);

		azioniPanel.clear();
		azioniPanel.getElement().removeAllChildren();
		for (Azione azione : anagrafica.getAzioni()) {
			SpanElement dateSpan = SpanElement.as(DOM.createSpan());
			dateSpan.setClassName("label");
			DateTimeFormat dtf = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATAORA);
			dateSpan.setInnerHTML(dtf.format(azione.getData()));

			HTMLPanel corpoDIV = new HTMLPanel("");
			corpoDIV.setStyleName("abstract");
			InlineHTML inline = new InlineHTML(azione.getAzione());
			corpoDIV.add(inline);

			azioniPanel.getElement().appendChild(dateSpan);
			azioniPanel.add(corpoDIV);
		}
	}

	private boolean controlloGruppi(Anagrafica a) {
		return !Collections.disjoint(a.getGruppi(), SettoriUtils.transform(profilazioneUtenteHandler.getSettoriUtente(false)));
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
	public void setModificaCommand(final Command<Void, Anagrafica> command) {
		modificaButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				executeCommand(command);
			}
		});
	}

	@Override
	public void setEliminaCommand(final Command<Void, Anagrafica> command) {
		eliminaButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				executeCommand(command);
			}
		});
	}

	private void executeCommand(Command<Void, Anagrafica> command) {
		Anagrafica a;
		if (personaFisica) {
			a = personaFisicaWidget.get();
		} else {
			a = personaGiuridicaWidget.get();
		}
		a.setId(id);
		command.exe(a);
	}
}
