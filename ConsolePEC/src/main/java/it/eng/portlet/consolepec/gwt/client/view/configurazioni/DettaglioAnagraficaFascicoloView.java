package it.eng.portlet.consolepec.gwt.client.view.configurazioni;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo.StepIter;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica.Stato;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.presenter.configurazioni.DettaglioAnagraficaFascicoloPresenter;
import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.widget.EventoIterFascicoloWidget;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.YesNoRadioButton;
import it.eng.portlet.consolepec.gwt.client.widget.configurazioni.ConfigurazioneAllegatiWidget;
import it.eng.portlet.consolepec.gwt.client.widget.configurazioni.DatiAggiuntiviWidget;
import it.eng.portlet.consolepec.gwt.client.widget.configurazioni.StepIterWidget;
import it.eng.portlet.consolepec.gwt.shared.model.EventoIterDTO;

/**
 * 
 * @author biagiot
 * 
 */
public class DettaglioAnagraficaFascicoloView extends ViewImpl implements DettaglioAnagraficaFascicoloPresenter.MyView {

	private static final DateTimeFormat dtf = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATAORA);
	private Widget widget;

	@UiField HeadingElement title;
	@UiField HeadingElement headTitle;

	@UiField HTMLPanel messageWidgetPanel;
	@UiField(provided = true) MessageAlertWidget messageWidget;

	@UiField TextBox tipologiaFascicolo;
	@UiField(provided = true) YesNoRadioButton attivo = new YesNoRadioButton();
	@UiField TextBox utenteCreazione;
	@UiField DateBox dataCreazione;
	@UiField(provided = true) YesNoRadioButton protocollabile = new YesNoRadioButton();

	// XXX Fase 2: Dati di protocollazione
	// @UiField
	// TextBox titolo;
	// @UiField
	// TextBox rubrica;
	// @UiField
	// TextBox sezione;
	// @UiField(provided = true)
	// ConsoleDisclosurePanel datiProtocollazioneDisclosure = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(),
	// OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Dati di protocollazione");

	@UiField(provided = true) DatiAggiuntiviWidget datiAggiuntiviWidget = new DatiAggiuntiviWidget();

	@UiField(provided = true) StepIterWidget stepIterWidget = new StepIterWidget();

	@UiField(provided = true) ConfigurazioneAllegatiWidget allegatiWidget = new ConfigurazioneAllegatiWidget();

	@UiField Button chiudi;
	@UiField Button salva;

	@UiField(provided = true) ConsoleDisclosurePanel dettaglioTipologiaFascicoloDisclosure = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(),
			OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Dettaglio tipologia fascicolo");

	@UiField(provided = true) ConsoleDisclosurePanel datiAggiuntiviWidgetDisclosurePanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(),
			OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Dati aggiuntivi");

	@UiField(provided = true) ConsoleDisclosurePanel stepIterWidgetDisclosurePanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(),
			OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Step iter");

	@UiField(provided = true) ConsoleDisclosurePanel allegatiDisclosurePanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(),
			OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Allegati");

	@UiField(provided = true) ConsoleDisclosurePanel azioniDisclosurePanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(),
			OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Azioni");
	@UiField EventoIterFascicoloWidget eventiIter;

	private Command salvaCommand;
	private Command annullaCommand;

	public interface Binder extends UiBinder<Widget, DettaglioAnagraficaFascicoloView> {
		//
	}

	@Inject
	public DettaglioAnagraficaFascicoloView(final Binder binder, final EventBus eventBus) {
		messageWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);

		Format format = new DateBox.DefaultFormat(dtf);
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
	public void mostraFormDettaglio(AnagraficaFascicolo anagraficaFascicolo, boolean showAction) {
		resetAll();

		this.title.setInnerText("Dettaglio: " + anagraficaFascicolo.getEtichettaTipologia());
		this.headTitle.setInnerText("");
		this.salva.setText("Salva");

		this.tipologiaFascicolo.setEnabled(false);
		this.tipologiaFascicolo.addStyleName("disabilitato");

		popolaDettaglio(anagraficaFascicolo);
		popolaDatiAggiuntivi(anagraficaFascicolo.getDatiAggiuntivi());
		popolaStepIter(anagraficaFascicolo.getStepIterAbilitati());

		popolaAllegatiWidget(anagraficaFascicolo);

		popolaAzioni(anagraficaFascicolo.getAzioni());

		resetDisclosurePanels(showAction);
	}

	private void popolaStepIter(List<StepIter> stepIterAbilitati) {
		this.stepIterWidget.render(stepIterAbilitati);
	}

	private void popolaDatiAggiuntivi(List<DatoAggiuntivo> datiAggiuntivi) {
		Collections.sort(datiAggiuntivi, new Comparator<DatoAggiuntivo>() {
			@Override
			public int compare(DatoAggiuntivo o1, DatoAggiuntivo o2) {
				if (o1.getPosizione() == null || o2.getPosizione() == null) {
					return 0;
				}
				return o1.getPosizione().compareTo(o2.getPosizione());
			}
		});
		this.datiAggiuntiviWidget.render(datiAggiuntivi);
	}

	private void popolaDettaglio(AnagraficaFascicolo anagraficaFascicolo) {
		this.tipologiaFascicolo.setValue(anagraficaFascicolo.getEtichettaTipologia());

		if (anagraficaFascicolo.getStato().equals(Stato.ATTIVA)) {
			this.attivo.selectYes();
		} else {
			this.attivo.selectNo();
		}

		if (anagraficaFascicolo.isProtocollabile()) {
			this.protocollabile.selectYes();

		} else {
			this.protocollabile.selectNo();
		}
		// XXX Fase 2: Dati di protocollazione
		// if (anagraficaFascicolo.getTitolazione() != null) {
		// this.titolo.setValue(anagraficaFascicolo.getTitolazione().getTitolo());
		// this.rubrica.setValue(anagraficaFascicolo.getTitolazione().getRubrica());
		// this.sezione.setValue(anagraficaFascicolo.getTitolazione().getSezione());
		// }
	}

	private void popolaAllegatiWidget(AnagraficaFascicolo af) {
		allegatiWidget.setTipologieAllegati(af.getTipologieAllegato());
	}

	@Override
	public void mostraFormCreazione() {
		this.title.setInnerText("Crea ingresso");
		this.headTitle.setInnerText("Inserisci le informazioni dell'ingresso");
		this.salva.setText("Crea");

		this.tipologiaFascicolo.setEnabled(true);
		this.tipologiaFascicolo.removeStyleName("disabilitato");

		resetAll();

		eventiIter.clear();
		azioniDisclosurePanel.setVisible(false);
		azioniDisclosurePanel.clear();

		datiAggiuntiviWidget.render(new ArrayList<DatoAggiuntivo>());
		stepIterWidget.render(new ArrayList<AnagraficaFascicolo.StepIter>());
	}

	private void resetAll() {
		this.tipologiaFascicolo.setValue(null);
		this.attivo.selectNo();
		this.protocollabile.selectNo();

		// XXX Fase 2: Dati di protocollazione
		// this.titolo.setValue(null);
		// this.sezione.setValue(null);
		// this.rubrica.setValue(null);

		if (datiAggiuntiviWidget != null) datiAggiuntiviWidget.clear();

		if (stepIterWidget != null) stepIterWidget.clear();

		if (allegatiWidget != null) allegatiWidget.clear();

		if (eventiIter != null) {
			eventiIter.clear();
		}
		resetDisclosurePanels(false);
	}

	@Override
	public void setSalvaCommand(Command command) {
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
	public AnagraficaFascicolo getAnagraficaFascicolo() {
		AnagraficaFascicolo af = new AnagraficaFascicolo();
		af.setEtichettaTipologia(tipologiaFascicolo.getValue() != null ? tipologiaFascicolo.getValue().toUpperCase().trim() : "");
		af.setStato(this.attivo.getValue() ? Stato.ATTIVA : Stato.DISATTIVA);
		af.setProtocollabile(this.protocollabile.getValue());

		// XXX Fase 2: Dati di protocollazione
		// String titoloProto = this.titolo.getValue();
		// String rubrica = this.rubrica.getValue();
		// String sezione = this.sezione.getValue();
		//
		// Titolazione t = new Titolazione();
		// t.setTitolo(titoloProto);
		// t.setRubrica(rubrica);
		// t.setSezione(sezione);
		// af.setTitolazione(t);

		if (datiAggiuntiviWidget != null && datiAggiuntiviWidget.getDatiAggiuntivi() != null) {
			af.getDatiAggiuntivi().addAll(datiAggiuntiviWidget.getDatiAggiuntivi());
		}
		if (stepIterWidget != null && stepIterWidget.getStepIter() != null) {
			af.getStepIterAbilitati().addAll(stepIterWidget.getStepIter());
		}

		if (allegatiWidget != null && allegatiWidget.getTipologieAllegati() != null) {
			Set<String> tipologie = new HashSet<>(allegatiWidget.getTipologieAllegati());
			af.getTipologieAllegato().addAll(tipologie);
		}

		af.setDataCreazione(dataCreazione.getValue());
		af.setUsernameCreazione(utenteCreazione.getValue());

		return af;
	}

	private void resetDisclosurePanels(boolean showAction) {
		dettaglioTipologiaFascicoloDisclosure.setOpen(!showAction);
		azioniDisclosurePanel.setOpen(showAction);
		datiAggiuntiviWidgetDisclosurePanel.setOpen(false);
		// XXX Fase 2: Dati di protocollazione
		// datiProtocollazioneDisclosure.setOpen(false);
		stepIterWidgetDisclosurePanel.setOpen(false);
		allegatiDisclosurePanel.setOpen(false);
	}

	private void popolaAzioni(List<Azione> azioni) {
		List<EventoIterDTO> eventiIterDTO = new ArrayList<EventoIterDTO>();

		if (azioni != null) {
			for (Azione azione : azioni) {
				StringBuilder sb = new StringBuilder();
				sb.append("L'utente ") //
						.append(azione.getUsernameUtente() != null ? azione.getUsernameUtente() : "-") //
						.append(" ha effettuato la seguente operazione: ") //
						.append(azione.getDescrizione() != null ? azione.getDescrizione() : "-");
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
	public void resetErrors() {
		messageWidgetPanel.setVisible(false);
		messageWidget.reset();
	}

}
