package it.eng.portlet.consolepec.gwt.client.view;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ConsoleDisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.profilazione.Settore;
import it.eng.cobo.consolepec.commons.profilazione.Utente;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.GruppiPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.UtenteWidget;
import it.eng.portlet.consolepec.gwt.client.widget.configurazioni.AnagraficheRuoliWidget;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.InputListWidget;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.IndirizziEmailSuggestOracle;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.UtentiSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.ricercautenti.RecuperaUtentiLdapAction;
import it.eng.portlet.consolepec.gwt.shared.action.ricercautenti.RecuperaUtentiLdapActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.StatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ricercautenti.TipoRicercaLdapUtenti;

public class GruppiView extends ViewImpl implements GruppiPresenter.MyView {

	private final Widget widget;
	@UiField
	Button confermaCambioGruppo;
	@UiField
	Button chiudiPanelGruppo;
	@UiField
	ListBox statiFascicolo;
	@UiField
	TextArea noteTextArea;
	@UiField
	HTMLPanel statiFascicoloPanel;
	@UiField
	HTMLPanel noteFascicoloPanel;
	@UiField
	HeadingElement avvisoProcedimenti;
	@UiField
	CheckBox ricordaSceltaCheckBox;
	@UiField
	HTMLPanel indirizziNotificaPanel;
	@UiField
	HTMLPanel indirizziNotificaContainer;
	@UiField(provided = true)
	MessageAlertWidget messaggioAlertWidget;
	@UiField(provided = true)
	AnagraficheRuoliWidget anagraficheRuoliWidget;
	@UiField(provided = true)
	ConsoleDisclosurePanel utentiDisclosurePanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Utenti");
	@UiField
	HTMLPanel utentiPanel;

	@UiField
	HTMLPanel utentiParentPanel;

	private List<Utente> utenti = new ArrayList<Utente>();

	private InputListWidget inputListWidgetDestinatari;
	private DispatchAsync dispatchAsync;
	private EventBus eventBus;

	public interface Binder extends UiBinder<Widget, GruppiView> {/**/}

	@Override
	protected void onAttach() {
		super.onAttach();
		Window.scrollTo(0, 0);
		utentiParentPanel.setVisible(false);
		utentiDisclosurePanel.setVisible(false);
		utentiPanel.clear();
		utenti.clear();
	}

	@Inject
	public GruppiView(final Binder binder, final EventBus eventBus, ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler,
			UtentiSuggestOracle utentiSuggestOracle, final DispatchAsync dispatcher) {

		this.dispatchAsync = dispatcher;
		this.eventBus = eventBus;

		anagraficheRuoliWidget = new AnagraficheRuoliWidget(configurazioniHandler, profilazioneUtenteHandler, utentiSuggestOracle, true, true, true) //
				.rewriteLabel("Filtra per settore", null, "Filtra per utente"); //

		messaggioAlertWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
		avvisoProcedimenti.getStyle().setDisplay(Display.NONE);
		avvisoProcedimenti.setInnerText(null);
		inputListWidgetDestinatari = new InputListWidget(new IndirizziEmailSuggestOracle(new ArrayList<String>()), "notifiche");
		RootPanel.get().add(inputListWidgetDestinatari);
		indirizziNotificaContainer.add(inputListWidgetDestinatari);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public Button getConfermaCambioGruppo() {
		return confermaCambioGruppo;
	}

	@Override
	public Button getAnnullaButton() {
		return chiudiPanelGruppo;
	}

	@Override
	public List<String> getInputListWidgetDestinatari() {
		return inputListWidgetDestinatari.getItemSelected();
	}

	@Override
	public String getTextFromInputListWidgetDestinatari() {
		return inputListWidgetDestinatari.getText();
	}

	@Override
	public void initStatiFascicolo(List<StatoDTO> stati, StatoDTO current) {
		statiFascicolo.clear();
		for (int i = 0; i < stati.size(); i++) {
			statiFascicolo.addItem(stati.get(i).getLabel(), stati.get(i).name());
			if (stati.get(i).equals(current))
				statiFascicolo.setItemSelected(i, true);
		}
	}

	@Override
	public void setStatiFascicoloVisible(boolean isVisible) {
		if (isVisible) {
			statiFascicoloPanel.setStyleName("cell acapo");
			statiFascicoloPanel.getElement().getStyle().setDisplay(Display.BLOCK);
			noteFascicoloPanel.setStyleName("cell acapo");
			noteFascicoloPanel.getElement().getStyle().setDisplay(Display.BLOCK);
		} else {
			statiFascicoloPanel.removeStyleName("cell acapo");
			statiFascicoloPanel.getElement().getStyle().setDisplay(Display.NONE);
			noteFascicoloPanel.removeStyleName("cell acapo");
			noteFascicoloPanel.getElement().getStyle().setDisplay(Display.NONE);
		}
	}

	@Override
	public void setNoteVisible(boolean isVisible) {
		if (isVisible) {
			noteFascicoloPanel.setStyleName("cell acapo");
			noteFascicoloPanel.getElement().getStyle().setDisplay(Display.BLOCK);
		} else {
			noteFascicoloPanel.removeStyleName("cell acapo");
			noteFascicoloPanel.getElement().getStyle().setDisplay(Display.NONE);
		}
	}

	@Override
	public StatoDTO getStatoFascicoloSelected() {
		return StatoDTO.valueOf(statiFascicolo.getValue(statiFascicolo.getSelectedIndex()));
	}

	@Override
	public String getNote() {
		return noteTextArea.getText();
	}

	@Override
	public boolean isStatiFascicoloVisible() {
		return statiFascicoloPanel.getElement().getStyle().getDisplay().equalsIgnoreCase(Display.BLOCK.name());
	}

	@Override
	public void mostraAvvisoProcedimenti(boolean mosta) {
		avvisoProcedimenti.getStyle().setDisplay((mosta) ? Display.BLOCK : Display.NONE);
	}

	@Override
	public void impostaAvvisoProcedimenti(String message) {
		avvisoProcedimenti.setInnerText(message);
	}

	@Override
	public void configuraIndirizziNotifica(boolean visible, List<String> indirizziNotifica) {
		inputListWidgetDestinatari.reset();
		indirizziNotificaPanel.setVisible(visible);

		if (indirizziNotifica != null && visible) {
			for (String ind : indirizziNotifica) {
				setIndirizzoNotifica(ind);
			}
		}
	}

	@Override
	public List<String> getIndirizziNotifica() {
		return inputListWidgetDestinatari.getItemSelected();
	}

	@Override
	public void setIndirizzoNotifica(String indirizzo) {
		inputListWidgetDestinatari.addValueItem(indirizzo);
	}

	@Override
	public boolean isRicordaSceltaEnabled() {
		return ricordaSceltaCheckBox.getValue();
	}

	@Override
	public void clearForm() {
		noteTextArea.setText("");
		inputListWidgetDestinatari.reset();
		ricordaSceltaCheckBox.setValue(false);
		utentiParentPanel.setVisible(false);
		utentiDisclosurePanel.setVisible(false);
		utentiPanel.clear();
		utenti.clear();
	}

	@Override
	public void showErrors(List<String> errors) {
		if (errors.size() > 0) {
			SafeHtmlBuilder sb = new SafeHtmlBuilder();
			sb.appendHtmlConstant("<ul>");
			for (String error : errors) {
				sb.appendHtmlConstant("<li>");
				sb.appendEscaped(error);
				sb.appendHtmlConstant("</li>");
			}
			sb.appendHtmlConstant("</ul>");
			HTML w = new HTML(sb.toSafeHtml());
			messaggioAlertWidget.showWarningMessage(w.getHTML());
			messaggioAlertWidget.setVisible(true);

		} else {
			messaggioAlertWidget.setVisible(false);
			messaggioAlertWidget.reset();
		}
	}

	@Override
	public Settore getSettoreSelezionato() {
		return anagraficheRuoliWidget.getSettoreSelezionato();
	}

	@Override
	public AnagraficaRuolo getAnagraficaRuoloSelezionata() {
		return anagraficheRuoliWidget.getAnagraficaRuoloSelezionata();
	}

	@Override
	public String getOperatoreSelezionato() {
		return anagraficheRuoliWidget.getOperatoreSelezionato();
	}

	@Override
	public void showWidget(Settore settore, AnagraficaRuolo anagraficaRuolo) {
		anagraficheRuoliWidget.showWidget(settore, anagraficaRuolo, false, true, true);
	}

	@Override
	public void setRicordaScelta(boolean ricordaScelta) {
		this.ricordaSceltaCheckBox.setValue(ricordaScelta);
	}

	@Override
	public void registerRuoliSelectionHandler() {
		anagraficheRuoliWidget.addRuoliSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> arg0) {
				inputListWidgetDestinatari.reset();
				utentiParentPanel.setVisible(false);
				utentiPanel.clear();
				utenti.clear();
				utentiDisclosurePanel.setVisible(false);

				if (anagraficheRuoliWidget.getAnagraficaRuoloSelezionata() != null) {

					RecuperaUtentiLdapAction action = new RecuperaUtentiLdapAction(TipoRicercaLdapUtenti.GRUPPO, anagraficheRuoliWidget.getAnagraficaRuoloSelezionata().getRuolo());
					dispatchAsync.execute(action, new AsyncCallback<RecuperaUtentiLdapActionResult>() {

						@Override
						public void onFailure(Throwable caught) {
							ShowMessageEvent event = new ShowMessageEvent();
							event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
							eventBus.fireEvent(event);
						}

						@Override
						public void onSuccess(RecuperaUtentiLdapActionResult result) {

							utentiPanel.clear();
							utenti.clear();
							utentiDisclosurePanel.setVisible(false);

							if (!result.isError()) {

								for (Utente utente : result.getUtenti()) {
									if (!utenti.contains(utente)) {
										utenti.add(utente);
									}
								}

								for (Utente utente : utenti) {
									UtenteWidget uw = new UtenteWidget();
									utentiPanel.add(uw);
									uw.showWidget(utente, false);
								}

								utentiParentPanel.setVisible(true);
								utentiDisclosurePanel.setVisible(true);

							} else {
								ShowMessageEvent event = new ShowMessageEvent();
								event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
								eventBus.fireEvent(event);
							}
						}
					});

					if (anagraficheRuoliWidget.getAnagraficaRuoloSelezionata().getEmailPredefinita() != null)
						inputListWidgetDestinatari.addValueItem(anagraficheRuoliWidget.getAnagraficaRuoloSelezionata().getEmailPredefinita());

				}
			}
		});
	}

}
