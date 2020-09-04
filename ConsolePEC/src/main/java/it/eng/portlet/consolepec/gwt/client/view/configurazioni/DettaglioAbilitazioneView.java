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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.configurazioni.AbilitazioniRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.Abilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.AmministrazioneAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneComunicazioneAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneModelloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.FiltroDatoAggiuntivoAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.ModificaRuoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.VisibilitaAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.VisibilitaFascicoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.VisibilitaRuoloAbilitazione;
import it.eng.cobo.consolepec.commons.profilazione.Settore;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.configurazioni.DettaglioAbilitazionePresenter;
import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.widget.EventoIterFascicoloWidget;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.YesNoRadioButton;
import it.eng.portlet.consolepec.gwt.client.widget.configurazioni.AbilitazioneFiltriRicercaWidget;
import it.eng.portlet.consolepec.gwt.client.widget.configurazioni.AbilitazioneMatriceVisibilitaWidget;
import it.eng.portlet.consolepec.gwt.client.widget.configurazioni.AbilitazioneStrumentiAmministrazioneWidget;
import it.eng.portlet.consolepec.gwt.client.widget.configurazioni.AbilitazioneSupervisoreWidget;
import it.eng.portlet.consolepec.gwt.client.widget.configurazioni.AnagraficheRuoliWidget;
import it.eng.portlet.consolepec.gwt.client.widget.configurazioni.OperatoriWidget;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.UtentiSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.model.EventoIterDTO;

/**
 *
 * @author biagiot
 *
 */
public class DettaglioAbilitazioneView extends ViewImpl implements DettaglioAbilitazionePresenter.MyView {

	private static final DateTimeFormat dtf = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATAORA);
	private Widget widget;

	@UiField
	HeadingElement title;

	@UiField
	HTMLPanel messageWidgetPanel;
	@UiField(provided = true)
	MessageAlertWidget messageWidget;

	@UiField(provided = true)
	AnagraficheRuoliWidget settoreRuoliWidget;
	@UiField(provided = true)
	YesNoRadioButton amministratore = new YesNoRadioButton();

	@UiField
	TextBox utenteCreazione;
	@UiField
	DateBox dataCreazione;

	@UiField
	Button chiudi;
	@UiField
	Button salva;

	@UiField(provided = true)
	ConsoleDisclosurePanel abilitazioniDisclosurePanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(),
			OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Abilitazioni");
	@UiField
	HTMLPanel abilitazioniPanel;

	@UiField(provided = true)
	ConsoleDisclosurePanel operatoriDisclosurePanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Operatori");
	@UiField
	HTMLPanel operatoriPanel;

	@UiField(provided = true)
	ConsoleDisclosurePanel filtriRicercaDisclosurePanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(),
			OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Filtri di ricerca");
	@UiField
	HTMLPanel filtriRicercaPanel;

	@UiField(provided = true)
	ConsoleDisclosurePanel strumentiAmministrazioneDisclosurePanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(),
			OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Strumenti di amministrazione");
	@UiField
	HTMLPanel strumentiAmministrazionePanel;

	@UiField(provided = true)
	ConsoleDisclosurePanel supervisoriDisclosurePanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Supervisore");
	@UiField
	HTMLPanel supervisoriPanel;

	@UiField(provided = true)
	ConsoleDisclosurePanel azioniDisclosurePanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Azioni");
	@UiField
	EventoIterFascicoloWidget eventiIter = new EventoIterFascicoloWidget();

	private Command salvaCommand;
	private Command annullaCommand;

	private ConfigurazioniHandler configurazioniHandler;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	private AbilitazioneMatriceVisibilitaWidget abilitazioneMatriceVisibilitaWidget;
	private AbilitazioneSupervisoreWidget abilitazioneSupervisoreWidget;
	private AbilitazioneStrumentiAmministrazioneWidget abilitazioneStrumentiAmministrazioneWidget;
	private AbilitazioneFiltriRicercaWidget abilitazioneFiltriRicercaWidget;
	private OperatoriWidget operatoriWidget;

	public interface Binder extends UiBinder<Widget, DettaglioAbilitazioneView> {
		//
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

	@Inject
	public DettaglioAbilitazioneView(final Binder binder, final EventBus eventBus, ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler,
			UtentiSuggestOracle utentiSuggestOracle) {
		this.configurazioniHandler = configurazioniHandler;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;

		messageWidget = new MessageAlertWidget(eventBus);
		settoreRuoliWidget = new AnagraficheRuoliWidget(configurazioniHandler, profilazioneUtenteHandler, utentiSuggestOracle, true, false, false);
		amministratore.selectNo();

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
	public void mostraFormDettaglio(AnagraficaRuolo anagraficaRuolo, AbilitazioniRuolo abilitazioniRuolo) {
		this.title.setInnerText("Dettaglio abilitazioni");
		this.salva.setText("Salva");
		this.amministratore.selectNo();

		popolaDettaglioRuolo(anagraficaRuolo);
		popolaAbilitazioni(abilitazioniRuolo.getAbilitazioni(), anagraficaRuolo.getRuolo());
		popolaAzioni(abilitazioniRuolo.getAzioni());

		azioniDisclosurePanel.setVisible(true);
		resetDisclosurePanels();
	}

	@Override
	public void mostraFormCreazione() {
		this.title.setInnerText("Crea abilitazioni");
		this.salva.setText("Salva");

		resetAll();

		inizializzaDettaglioRuolo();
		inizializzaAbiltiazioniPanel();
		azioniDisclosurePanel.setVisible(false);
	}

	private void popolaDettaglioRuolo(AnagraficaRuolo anagraficaRuolo) {
		Settore settore = configurazioniHandler.getSettore(anagraficaRuolo);
		settoreRuoliWidget.showWidget(settore, anagraficaRuolo, false, false, true);
		settoreRuoliWidget.setEditabile(false);

		popolaOperatori(anagraficaRuolo.getOperatori());
	}

	private void inizializzaDettaglioRuolo() {
		settoreRuoliWidget.showWidget(null, null, false, false, true);
		inizializzaOperatori();
	}

	private void inizializzaOperatori() {
		popolaOperatori(null);
	}

	private void popolaAbilitazioni(List<Abilitazione> abilitazioni, String ruoloCorrente) {
		abilitazioniPanel.clear();
		filtriRicercaPanel.clear();
		supervisoriPanel.clear();
		strumentiAmministrazionePanel.clear();

		List<FiltroDatoAggiuntivoAbilitazione> filtriDatiAggAbilitazioni = new ArrayList<FiltroDatoAggiuntivoAbilitazione>();
		List<ModificaRuoloAbilitazione> ruoloAbilitazioni = new ArrayList<ModificaRuoloAbilitazione>();
		List<CreazioneModelloAbilitazione> modelloAbilitazioni = new ArrayList<CreazioneModelloAbilitazione>();
		List<CreazioneComunicazioneAbilitazione> comunicazioneAbilitazioni = new ArrayList<CreazioneComunicazioneAbilitazione>();
		List<VisibilitaAbilitazione> visibilitaAbilitazioni = new ArrayList<VisibilitaAbilitazione>();

		if (abilitazioni != null) {
			for (Abilitazione abilitazione : abilitazioni) {

				if (abilitazione instanceof AmministrazioneAbilitazione) {
					this.amministratore.selectYes();
				}

				if (abilitazione instanceof CreazioneModelloAbilitazione) {
					modelloAbilitazioni.add((CreazioneModelloAbilitazione) abilitazione);

				} else if (abilitazione instanceof CreazioneComunicazioneAbilitazione) {
					comunicazioneAbilitazioni.add((CreazioneComunicazioneAbilitazione) abilitazione);

				} else if (abilitazione instanceof ModificaRuoloAbilitazione) {
					ruoloAbilitazioni.add((ModificaRuoloAbilitazione) abilitazione);

				} else if (abilitazione instanceof FiltroDatoAggiuntivoAbilitazione) {
					filtriDatiAggAbilitazioni.add((FiltroDatoAggiuntivoAbilitazione) abilitazione);

				} else if (abilitazione instanceof VisibilitaFascicoloAbilitazione || abilitazione instanceof VisibilitaRuoloAbilitazione) {
					visibilitaAbilitazioni.add((VisibilitaAbilitazione) abilitazione);
				}
			}
		}

		abilitazioneMatriceVisibilitaWidget = new AbilitazioneMatriceVisibilitaWidget();
		abilitazioneMatriceVisibilitaWidget.render(configurazioniHandler, profilazioneUtenteHandler, visibilitaAbilitazioni);
		abilitazioniPanel.add(abilitazioneMatriceVisibilitaWidget);

		abilitazioneFiltriRicercaWidget = new AbilitazioneFiltriRicercaWidget();
		abilitazioneFiltriRicercaWidget.render(configurazioniHandler, profilazioneUtenteHandler, filtriDatiAggAbilitazioni, "", new String[] { "" });
		filtriRicercaPanel.add(abilitazioneFiltriRicercaWidget);

		abilitazioneSupervisoreWidget = new AbilitazioneSupervisoreWidget();
		abilitazioneSupervisoreWidget.render(configurazioniHandler, profilazioneUtenteHandler, ruoloAbilitazioni, ruoloCorrente);
		supervisoriPanel.add(abilitazioneSupervisoreWidget);

		abilitazioneStrumentiAmministrazioneWidget = new AbilitazioneStrumentiAmministrazioneWidget();
		abilitazioneStrumentiAmministrazioneWidget.render(configurazioniHandler, profilazioneUtenteHandler, modelloAbilitazioni, comunicazioneAbilitazioni);
		strumentiAmministrazionePanel.add(abilitazioneStrumentiAmministrazioneWidget);
	}

	private void inizializzaAbiltiazioniPanel() {
		popolaAbilitazioni(null, null);
	}

	private void popolaOperatori(List<String> operatori) {
		operatoriPanel.clear();
		operatoriWidget = new OperatoriWidget();
		operatoriWidget.render(operatori);
		operatoriPanel.add(operatoriWidget);
	}

	private void popolaAzioni(List<Azione> azioni) {

		List<EventoIterDTO> eventiIterDTO = new ArrayList<EventoIterDTO>();

		if (azioni != null) {
			for (Azione azione : azioni) {
				StringBuilder sb = new StringBuilder();
				sb.append("L'utente ").append(azione.getUsernameUtente() != null ? azione.getUsernameUtente() : "-").append(" ha effettuato la seguente operazione: ") //
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
	public void setSalvaRuoloCommand(Command command) {
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
	public AnagraficaRuolo getRuoloSelezionato() {
		AnagraficaRuolo ar = settoreRuoliWidget != null ? settoreRuoliWidget.getAnagraficaRuoloSelezionata() : null;
		return ar;
	}

	@Override
	public boolean isAmministratore() {
		return amministratore.getValue();
	}

	@Override
	public List<Abilitazione> getAbilitazioniDaAggiungere() {
		List<Abilitazione> res = new ArrayList<Abilitazione>();

		if (abilitazioneMatriceVisibilitaWidget != null && abilitazioneMatriceVisibilitaWidget.getAbilitazioniDaAggiungere() != null) {
			res.addAll(abilitazioneMatriceVisibilitaWidget.getAbilitazioniDaAggiungere());
		}

		if (abilitazioneFiltriRicercaWidget != null && abilitazioneFiltriRicercaWidget.getAbilitazioniDaAggiungere() != null) {
			res.addAll(abilitazioneFiltriRicercaWidget.getAbilitazioniDaAggiungere());
		}

		if (abilitazioneStrumentiAmministrazioneWidget != null && abilitazioneStrumentiAmministrazioneWidget.getAbilitazioniDaAggiungere() != null) {
			res.addAll(abilitazioneStrumentiAmministrazioneWidget.getAbilitazioniDaAggiungere());
		}

		if (abilitazioneSupervisoreWidget != null && abilitazioneSupervisoreWidget.getAbilitazioniDaAggiungere() != null) {
			res.addAll(abilitazioneSupervisoreWidget.getAbilitazioniDaAggiungere());
		}

		return res;
	}

	@Override
	public List<Abilitazione> getAbilitazioniDaRimuovere() {
		List<Abilitazione> res = new ArrayList<Abilitazione>();

		if (abilitazioneMatriceVisibilitaWidget != null && abilitazioneMatriceVisibilitaWidget.getAbilitazioniDaRimuovere() != null) {
			res.addAll(abilitazioneMatriceVisibilitaWidget.getAbilitazioniDaRimuovere());
		}

		if (abilitazioneFiltriRicercaWidget != null && abilitazioneFiltriRicercaWidget.getAbilitazioniDaRimuovere() != null) {
			res.addAll(abilitazioneFiltriRicercaWidget.getAbilitazioniDaRimuovere());
		}

		if (abilitazioneStrumentiAmministrazioneWidget != null && abilitazioneStrumentiAmministrazioneWidget.getAbilitazioniDaRimuovere() != null) {
			res.addAll(abilitazioneStrumentiAmministrazioneWidget.getAbilitazioniDaRimuovere());
		}

		if (abilitazioneSupervisoreWidget != null && abilitazioneSupervisoreWidget.getAbilitazioniDaRimuovere() != null) {
			res.addAll(abilitazioneSupervisoreWidget.getAbilitazioniDaRimuovere());
		}

		return res;
	}

	@Override
	public List<String> getOperatori() {
		if (operatoriWidget != null) {
			return operatoriWidget.getOperatori();
		}

		return null;
	}

	@Override
	public void clearView() {
		resetAll();
	}

	private void resetAll() {
		this.amministratore.selectNo();

		if (abilitazioneMatriceVisibilitaWidget != null) {
			abilitazioneMatriceVisibilitaWidget.clear();
			abilitazioneMatriceVisibilitaWidget.removeFromParent();
		}

		if (this.abilitazioneFiltriRicercaWidget != null) {
			this.abilitazioneFiltriRicercaWidget.clear();
			this.abilitazioneFiltriRicercaWidget.removeFromParent();
		}

		if (this.abilitazioneStrumentiAmministrazioneWidget != null) {
			this.abilitazioneStrumentiAmministrazioneWidget.clear();
			this.abilitazioneStrumentiAmministrazioneWidget.removeFromParent();
		}

		if (this.abilitazioneSupervisoreWidget != null) {
			this.abilitazioneSupervisoreWidget.clear();
			this.abilitazioneSupervisoreWidget.removeFromParent();
		}

		if (this.operatoriWidget != null) {
			this.operatoriWidget.clear();
			this.operatoriWidget.removeFromParent();
		}

		this.eventiIter.clear();
		resetDisclosurePanels();
	}

	private void resetDisclosurePanels() {
		abilitazioniDisclosurePanel.setOpen(false);
		operatoriDisclosurePanel.setOpen(false);
		filtriRicercaDisclosurePanel.setOpen(false);
		strumentiAmministrazioneDisclosurePanel.setOpen(false);
		supervisoriDisclosurePanel.setOpen(false);
		azioniDisclosurePanel.setOpen(false);
	}

	@Override
	public void setOnSelectRuoloCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AnagraficaRuolo> command) {
		if (settoreRuoliWidget != null) {
			settoreRuoliWidget.addSelectionCommand(command);
		}
	}

	@Override
	public void resetErrors() {
		messageWidgetPanel.setVisible(false);
		messageWidget.reset();
	}

	@Override
	public List<Azione> getAzioni() {
		List<Azione> res = new ArrayList<Azione>();

		if (abilitazioneMatriceVisibilitaWidget != null && abilitazioneMatriceVisibilitaWidget.getAzioni() != null) {
			res.addAll(abilitazioneMatriceVisibilitaWidget.getAzioni());
		}

		if (abilitazioneFiltriRicercaWidget != null && abilitazioneFiltriRicercaWidget.getAzioni() != null) {
			res.addAll(abilitazioneFiltriRicercaWidget.getAzioni());
		}

		if (abilitazioneStrumentiAmministrazioneWidget != null && abilitazioneStrumentiAmministrazioneWidget.getAzioni() != null) {
			res.addAll(abilitazioneStrumentiAmministrazioneWidget.getAzioni());
		}

		if (abilitazioneSupervisoreWidget != null && abilitazioneSupervisoreWidget.getAzioni() != null) {
			res.addAll(abilitazioneSupervisoreWidget.getAzioni());
		}

		return res;
	}
}
