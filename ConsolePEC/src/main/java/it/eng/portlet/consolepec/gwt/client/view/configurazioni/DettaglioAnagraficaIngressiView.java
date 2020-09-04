package it.eng.portlet.consolepec.gwt.client.view.configurazioni;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ConsoleDisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica.Stato;
import it.eng.cobo.consolepec.commons.configurazioni.properties.ProprietaGenerali.Server;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.configurazioni.DettaglioAnagraficaIngressiPresenter;
import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.widget.EventoIterFascicoloWidget;
import it.eng.portlet.consolepec.gwt.client.widget.GruppoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.YesNoRadioButton;
import it.eng.portlet.consolepec.gwt.client.widget.configurazioni.AnagraficheRuoliWidget;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.UtentiSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.model.EventoIterDTO;

/**
 *
 * @author biagiot
 *
 */
public class DettaglioAnagraficaIngressiView extends ViewImpl implements DettaglioAnagraficaIngressiPresenter.MyView {

	private static final DateTimeFormat dtf = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATAORA);
	private Widget widget;

	@UiField
	HeadingElement title;
	@UiField
	HeadingElement headTitle;

	@UiField
	HTMLPanel messageWidgetPanel;
	@UiField(provided = true)
	MessageAlertWidget messageWidget;

	@UiField(provided = true)
	ListBox tipoServer;
	@UiField
	TextBox nomeCasella;
	@UiField
	PasswordTextBox password;
	@UiField
	TextBox utenza;
	@UiField
	IntegerBox giorniCancellazione;
	@UiField
	TextBox cartellaLettura;
	@UiField
	TextBox cartellaScaricati;
	@UiField(provided = true)
	AnagraficheRuoliWidget ruoliWidget;
	@UiField
	YesNoRadioButton cancellazioneAttiva;
	@UiField
	YesNoRadioButton scaricoMailAttivo;
	@UiField
	YesNoRadioButton scaricoRicevuteAttive;
	@UiField
	YesNoRadioButton emailUscita;
	@UiField
	TextBox utenteCreazione;
	@UiField
	DateBox dataCreazione;

	@UiField
	Button chiudi;
	@UiField
	Button salva;

	@UiField(provided = true)
	ConsoleDisclosurePanel dettaglioDisclosure = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Dettaglio");

	@UiField(provided = true)
	ConsoleDisclosurePanel gruppiDisclosurePanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Gruppi abilitati");
	@UiField
	HTMLPanel gruppiPanel;

	@UiField(provided = true)
	ConsoleDisclosurePanel azioniDisclosurePanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Azioni");
	@UiField
	EventoIterFascicoloWidget eventiIter;

	public interface Binder extends UiBinder<Widget, DettaglioAnagraficaIngressiView> {
		//
	}

	private Command salvaCommand;
	private Command annullaCommand;

	private ConfigurazioniHandler configurazioniHandler;

	@Inject
	public DettaglioAnagraficaIngressiView(final Binder binder, final EventBus eventBus, ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler,
			UtentiSuggestOracle utentiSuggestOracle) {
		this.configurazioniHandler = configurazioniHandler;
		messageWidget = new MessageAlertWidget(eventBus);

		tipoServer = new ListBox();
		for (Server server : configurazioniHandler.getProprietaGenerali().getServerIngressiAbilitati()) {
			tipoServer.addItem(server.getNome());
		}

		ruoliWidget = new AnagraficheRuoliWidget(configurazioniHandler, profilazioneUtenteHandler, utentiSuggestOracle, true, false, false) //
				.rewriteLabel("Filtra per settore", "Gruppo primo assegnatario*", null);

		widget = binder.createAndBindUi(this);

		DateTimeFormat f = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATAORA);
		Format format = new DateBox.DefaultFormat(f);
		dataCreazione.setFormat(format);

		configuraBottoni();
	}

	private void configuraBottoni() {
		this.salva.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				salvaCommand.execute();
			}
		});

		this.chiudi.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				annullaCommand.execute();
			}
		});
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
	public void mostraFormDettaglio(AnagraficaIngresso anagraficaIngresso, AnagraficaRuolo primoAssegnatario, List<AnagraficaRuolo> gruppiAbilitatiLettura, boolean showGruppi) {

		this.title.setInnerText("Dettaglio ingresso");
		this.headTitle.setInnerText("");
		this.salva.setText("Salva");

		this.tipoServer.setEnabled(false);
		this.tipoServer.addStyleName("disabilitato");
		this.nomeCasella.setEnabled(false);
		this.nomeCasella.addStyleName("disabilitato");
		this.emailUscita.setReadOnly(true);

		popolaDettaglio(anagraficaIngresso, primoAssegnatario);
		popolaGruppi(gruppiAbilitatiLettura);
		popolaAzioni(anagraficaIngresso.getAzioni());
		azioniDisclosurePanel.setVisible(true);
		gruppiDisclosurePanel.setVisible(true);
		resetDisclosurePanels(showGruppi);
	}

	@Override
	public void mostraFormCreazione() {
		this.title.setInnerText("Crea ingresso");
		this.headTitle.setInnerText("Inserisci le informazioni dell'ingresso");
		this.salva.setText("Crea");

		this.tipoServer.setEnabled(true);
		this.tipoServer.removeStyleName("disabilitato");
		this.nomeCasella.setEnabled(true);
		this.nomeCasella.removeStyleName("disabilitato");
		this.emailUscita.setReadOnly(false);

		resetAll();
		azioniDisclosurePanel.setVisible(false);
		eventiIter.clear();
		gruppiDisclosurePanel.setVisible(false);
		gruppiPanel.clear();
		dettaglioDisclosure.setOpen(true);

		if (this.ruoliWidget != null)
			this.ruoliWidget.showWidget(null, null, false, false, true);
	}

	private void popolaDettaglio(AnagraficaIngresso anagraficaIngresso, AnagraficaRuolo primoAssegnatario) {

		for (int i = 0; i < this.tipoServer.getItemCount(); i++) {
			String m = this.tipoServer.getValue(i);
			if (anagraficaIngresso.getServer().equals(m)) {
				this.tipoServer.setSelectedIndex(i);
			}
		}

		this.nomeCasella.setText(anagraficaIngresso.getIndirizzo());
		this.password.setText(anagraficaIngresso.getPassword());
		this.utenza.setText(anagraficaIngresso.getUtenza());
		this.giorniCancellazione.setValue(anagraficaIngresso.getGiorniCancellazione());
		this.cartellaLettura.setValue(anagraficaIngresso.getFolderIn());
		this.cartellaScaricati.setValue(anagraficaIngresso.getFolderTo());

		if (primoAssegnatario != null) {
			this.ruoliWidget.showWidget(configurazioniHandler.getSettore(primoAssegnatario), primoAssegnatario, false, false, true);

		} else {
			this.ruoliWidget.showWidget(null, null, false, false, true);

		}

		if (Stato.ATTIVA.equals(anagraficaIngresso.getStato())) {
			scaricoMailAttivo.selectYes();

		} else {
			scaricoMailAttivo.selectNo();
		}

		if (Boolean.TRUE.equals(anagraficaIngresso.isScaricoRicevute())) {
			scaricoRicevuteAttive.selectYes();

		} else {
			scaricoRicevuteAttive.selectNo();
		}

		if (Boolean.TRUE.equals(anagraficaIngresso.isCancellazioneAutomatica())) {
			cancellazioneAttiva.selectYes();

		} else {
			cancellazioneAttiva.selectNo();
		}
	}

	private void popolaGruppi(List<AnagraficaRuolo> gruppiAbilitatiLettura) {
		gruppiPanel.clear();

		if ((gruppiAbilitatiLettura != null && !gruppiAbilitatiLettura.isEmpty())) {

			for (AnagraficaRuolo ar : gruppiAbilitatiLettura) {
				GruppoWidget gruppoWidget = new GruppoWidget();
				gruppoWidget.showWidget(ar, false, false);
				gruppiPanel.add(gruppoWidget);
			}
		}

	}

	private void resetAll() {
		this.nomeCasella.setValue(null);
		this.password.setValue(null);
		this.utenza.setValue(null);
		this.cartellaLettura.setValue(null);
		this.cartellaScaricati.setValue(null);
		this.tipoServer.setSelectedIndex(0);
		this.giorniCancellazione.setValue(30);
		this.cancellazioneAttiva.selectNo();
		this.emailUscita.selectNo();
		this.scaricoMailAttivo.selectNo();
		this.scaricoRicevuteAttive.selectNo();
		this.ruoliWidget.clear();
		this.eventiIter.clear();

		gruppiPanel.clear();
		gruppiPanel.getElement().removeAllChildren();

		resetDisclosurePanels(false);
	}

	@Override
	public void setSalvaIngressoCommand(Command command) {
		this.salvaCommand = command;
	}

	@Override
	public void setChiudiCommand(Command command) {
		this.annullaCommand = command;
	}

	@Override
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione.setValue(dataCreazione);
	}

	@Override
	public void setUtenteCreazione(String utente) {
		this.utenteCreazione.setValue(utente);
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
			messageWidget.showWarningMessage(w.getHTML());
			messageWidgetPanel.setVisible(true);

		} else {
			messageWidgetPanel.setVisible(false);
			messageWidget.reset();
		}
	}

	@Override
	public AnagraficaIngresso getAnagraficaIngresso() {
		AnagraficaIngresso ai = new AnagraficaIngresso();
		ai.setIndirizzo(this.nomeCasella.getValue() != null ? this.nomeCasella.getValue().toLowerCase().trim() : null);
		ai.setServer(this.tipoServer.getSelectedValue());
		ai.setPassword(this.password.getText() != null ? this.password.getText() : "");
		ai.setUtenza(this.utenza.getText().trim());
		ai.setGiorniCancellazione(this.giorniCancellazione.getValue());
		ai.setFolderIn(this.cartellaLettura.getValue());
		ai.setFolderTo(this.cartellaScaricati.getValue());
		ai.setScaricoRicevute(this.scaricoRicevuteAttive.getValue());
		ai.setDataCreazione(dataCreazione.getValue());
		ai.setUsernameCreazione(utenteCreazione.getValue());
		ai.setCancellazioneAutomatica(cancellazioneAttiva.getValue());
		ai.setStato(this.scaricoMailAttivo.getValue() ? Stato.ATTIVA : Stato.DISATTIVA);
		return ai;
	}

	private void resetDisclosurePanels(boolean showGruppi) {
		dettaglioDisclosure.setOpen(!showGruppi);
		azioniDisclosurePanel.setOpen(false);
		gruppiDisclosurePanel.setOpen(showGruppi);
	}

	private void popolaAzioni(List<Azione> azioni) {
		List<EventoIterDTO> eventiIterDTO = new ArrayList<EventoIterDTO>();

		if (azioni != null) {
			for (Azione azione : azioni) {
				StringBuilder sb = new StringBuilder();
				sb.append("L'utente ").append(azione.getUsernameUtente() != null ? azione.getUsernameUtente() : "-").append(" ha effettuato la seguente operazione: ").append(
						azione.getDescrizione() != null ? azione.getDescrizione() : "-");

				String data = null;
				if (azione.getData() != null) {
					data = dtf.format(azione.getData());

				} else {
					data = "-";
				}

				EventoIterDTO eventoIterDTO = new EventoIterDTO(sb.toString(), data);
				eventiIterDTO.add(eventoIterDTO);
			}
		}

		eventiIter.initWidget(eventiIterDTO);
	}

	@Override
	public void clearView() {
		resetAll();
	}

	@Override
	public AnagraficaRuolo getPrimoAssegnatario() {
		return ruoliWidget.getAnagraficaRuoloSelezionata();
	}

	@Override
	public void resetErrors() {
		messageWidgetPanel.setVisible(false);
		messageWidget.reset();
	}

	@Override
	public boolean isCreaEmailOut() {
		return emailUscita.getValue();
	}
}
