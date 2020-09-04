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
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo.Stato;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.Abilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneEmailOutAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneFascicoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.EmailOutAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.FascicoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.GestioneDriveAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.IngressoAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.LetturaIngressoAbilitazione;
import it.eng.cobo.consolepec.commons.profilazione.Settore;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.configurazioni.DettaglioAnagraficaGruppiPresenter;
import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.widget.EventoIterFascicoloWidget;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.YesNoRadioButton;
import it.eng.portlet.consolepec.gwt.client.widget.configurazioni.AbilitazioneFascicoloWidget;
import it.eng.portlet.consolepec.gwt.client.widget.configurazioni.AbilitazioneIngressiWidget;
import it.eng.portlet.consolepec.gwt.client.widget.configurazioni.AbilitazioneMailOutWidget;
import it.eng.portlet.consolepec.gwt.client.widget.configurazioni.SettoreWidget;
import it.eng.portlet.consolepec.gwt.shared.model.EventoIterDTO;

/**
 * 
 * @author biagiot
 *
 */
public class DettaglioAnagraficaGruppiView extends ViewImpl implements DettaglioAnagraficaGruppiPresenter.MyView {

	private static final DateTimeFormat dtf = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATA);
	private Widget widget;

	@UiField
	HeadingElement title;
	@UiField
	HeadingElement headTitle;

	@UiField
	HTMLPanel messageWidgetPanel;
	@UiField(provided = true)
	MessageAlertWidget messageWidget;

	@UiField
	TextBox utenteCreazione;
	@UiField
	DateBox dataCreazione;
	@UiField
	YesNoRadioButton attivo;

	@UiField
	YesNoRadioButton drive;

	@UiField
	Button chiudi;
	@UiField
	Button copia;
	@UiField
	Button salva;

	@UiField
	TextBox nome;
	@UiField
	TextBox nomeGruppoLdap;
	@UiField
	TextBox mailAssegnazione;

	@UiField(provided = true)
	ConsoleDisclosurePanel dettaglioDisclosure = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Dettaglio");

	@UiField(provided = true)
	ConsoleDisclosurePanel settoreDisclosurePanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Settore");
	@UiField
	HTMLPanel settorePanel;

	@UiField(provided = true)
	ConsoleDisclosurePanel abilitazioniFascicoliDisclosurePanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(),
			OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Fascicoli abilitati");
	@UiField
	HTMLPanel abilitazioniFascicoliPanel;

	@UiField(provided = true)
	ConsoleDisclosurePanel abilitazioniIngressiDisclosurePanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(),
			OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Ingressi abilitati");
	@UiField
	HTMLPanel abilitazioniIngressiPanel;

	@UiField(provided = true)
	ConsoleDisclosurePanel abilitazioniUscitaDisclosurePanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(),
			OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Mail in uscita abilitate");
	@UiField
	HTMLPanel abilitazioniUscitaPanel;

	@UiField(provided = true)
	ConsoleDisclosurePanel azioniDisclosurePanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Azioni");
	@UiField
	EventoIterFascicoloWidget eventiIter = new EventoIterFascicoloWidget();

	private Command salvaCommand;
	private Command annullaCommand;
	private Command creaPerCopiaCommand;

	private AbilitazioneFascicoloWidget abilitazioneFascicoloWidget;
	private AbilitazioneIngressiWidget abilitazioneIngressiWidget;
	private AbilitazioneMailOutWidget abilitazioneMailOutWidget;
	private SettoreWidget settoreWidget;

	private ConfigurazioniHandler configurazioniHandler;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	private boolean driveAttivo = false;

	public interface Binder extends UiBinder<Widget, DettaglioAnagraficaGruppiView> {
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
	public DettaglioAnagraficaGruppiView(final Binder binder, final EventBus eventBus, ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler) {

		this.configurazioniHandler = configurazioniHandler;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;

		messageWidget = new MessageAlertWidget(eventBus);

		widget = binder.createAndBindUi(this);

		DateTimeFormat f = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATAORA);
		Format format = new DateBox.DefaultFormat(f);
		dataCreazione.setFormat(format);

		configuraBottoni();
	}

	@Override
	public void mostraFormDettaglio(AnagraficaRuolo anagraficaRuolo, Settore settore, AbilitazioniRuolo abilitazioniRuolo, boolean showAction) {
		this.title.setInnerText("Dettaglio gruppo");
		this.headTitle.setInnerText("");
		this.salva.setText("Salva");
		this.copia.setEnabled(true);

		this.nomeGruppoLdap.setEnabled(false);
		this.nomeGruppoLdap.addStyleName("disabilitato");

		this.nome.setEnabled(true);
		this.nome.removeStyleName("disabilitato");

		popolaDettaglioRuolo(anagraficaRuolo);
		popolaSettore(settore);
		popolaAbilitazioni(abilitazioniRuolo);

		azioniDisclosurePanel.setVisible(true);
		resetDisclosurePanels(showAction);
	}

	@Override
	public void mostraFormCreaPerCopia(AnagraficaRuolo anagraficaRuolo, Settore settore, AbilitazioniRuolo abilitazioniRuolo) {
		this.title.setInnerText("Crea gruppo per copia");
		this.headTitle.setInnerText("Inserisci le informazioni del gruppo");
		this.salva.setText("Crea");
		this.copia.setEnabled(false);

		this.nomeGruppoLdap.setEnabled(true);
		this.nomeGruppoLdap.removeStyleName("disabilitato");

		this.nome.setEnabled(true);
		this.nome.removeStyleName("disabilitato");

		resetAll(true);
		popolaSettore(settore);
		configurazioniHandler.cleanAbilitazioni(abilitazioniRuolo.getAbilitazioni());
		popolaAbilitazioni(abilitazioniRuolo);
		azioniDisclosurePanel.setVisible(false);
	}

	@Override
	public void mostraFormCreazione() {
		this.title.setInnerText("Crea gruppo");
		this.headTitle.setInnerText("Inserisci le informazioni del gruppo");
		this.salva.setText("Crea");
		this.copia.setEnabled(false);

		this.nomeGruppoLdap.setEnabled(true);
		this.nomeGruppoLdap.removeStyleName("disabilitato");

		this.nome.setEnabled(true);
		this.nome.removeStyleName("disabilitato");

		resetAll(false);
		inizializzaSettorePanel();
		inizializzaAbiltiazioniPanel();
		azioniDisclosurePanel.setVisible(false);
	}

	private void popolaDettaglioRuolo(AnagraficaRuolo ar) {
		this.nome.setText(ar.getEtichetta());
		this.nomeGruppoLdap.setText(ar.getRuolo());

		if (ar.getStato().equals(Stato.ATTIVA))
			this.attivo.selectYes();
		else
			this.attivo.selectNo();

		this.mailAssegnazione.setText(ar.getEmailPredefinita());

		popolaAzioni(ar.getAzioni());
	}

	private void inizializzaAbiltiazioniPanel() {
		popolaAbilitazioni(null);
	}

	private void inizializzaSettorePanel() {
		popolaSettore(null);
	}

	private void popolaSettore(Settore settore) {
		settorePanel.getElement().removeAllChildren();
		settoreWidget = new SettoreWidget();
		settoreWidget.render(configurazioniHandler, settore);
		settorePanel.add(settoreWidget);
	}

	private void popolaAbilitazioni(AbilitazioniRuolo abilitazioniRuolo) {

		this.drive.selectNo();
		this.driveAttivo = false;

		if (abilitazioniFascicoliPanel != null)
			abilitazioniFascicoliPanel.getElement().removeAllChildren();

		if (abilitazioneIngressiWidget != null)
			abilitazioneIngressiWidget.getElement().removeAllChildren();

		if (abilitazioneMailOutWidget != null)
			abilitazioneMailOutWidget.getElement().removeAllChildren();

		List<FascicoloAbilitazione> fascicoloAbilitazioni = new ArrayList<FascicoloAbilitazione>();
		List<IngressoAbilitazione> visIngressoAbilitazioni = new ArrayList<IngressoAbilitazione>();
		List<EmailOutAbilitazione> mailOutAbilitazione = new ArrayList<EmailOutAbilitazione>();

		if (abilitazioniRuolo != null && abilitazioniRuolo.getAbilitazioni() != null) {
			for (Abilitazione abilitazione : abilitazioniRuolo.getAbilitazioni()) {

				if (abilitazione instanceof CreazioneFascicoloAbilitazione) {
					fascicoloAbilitazioni.add((FascicoloAbilitazione) abilitazione);

				} else if (abilitazione instanceof LetturaIngressoAbilitazione) {
					visIngressoAbilitazioni.add((LetturaIngressoAbilitazione) abilitazione);

				} else if (abilitazione instanceof CreazioneEmailOutAbilitazione) {
					mailOutAbilitazione.add((CreazioneEmailOutAbilitazione) abilitazione);

				} else if (abilitazione instanceof GestioneDriveAbilitazione) {
					this.drive.selectYes();
					this.driveAttivo = true;
				}
			}
		}

		abilitazioneFascicoloWidget = new AbilitazioneFascicoloWidget();
		abilitazioneFascicoloWidget.render(configurazioniHandler, profilazioneUtenteHandler, fascicoloAbilitazioni);
		abilitazioniFascicoliPanel.add(abilitazioneFascicoloWidget);

		abilitazioneIngressiWidget = new AbilitazioneIngressiWidget();
		abilitazioneIngressiWidget.render(configurazioniHandler, profilazioneUtenteHandler, visIngressoAbilitazioni);
		abilitazioniIngressiPanel.add(abilitazioneIngressiWidget);

		abilitazioneMailOutWidget = new AbilitazioneMailOutWidget();
		abilitazioneMailOutWidget.render(configurazioniHandler, profilazioneUtenteHandler, mailOutAbilitazione);
		abilitazioniUscitaPanel.add(abilitazioneMailOutWidget);
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

	private void resetAll(boolean isCopia) {

		this.nome.setText(null);
		this.nomeGruppoLdap.setText(null);
		this.attivo.selectNo();
		this.mailAssegnazione.setText(null);
		this.eventiIter.clear();
		this.drive.selectNo();

		if (!isCopia) {
			if (this.abilitazioneFascicoloWidget != null) {
				this.abilitazioneFascicoloWidget.clear();
				this.abilitazioneFascicoloWidget.removeFromParent();
			}

			if (this.abilitazioneIngressiWidget != null) {
				this.abilitazioneIngressiWidget.clear();
				this.abilitazioneIngressiWidget.removeFromParent();
			}

			if (this.abilitazioneMailOutWidget != null) {
				this.abilitazioneMailOutWidget.clear();
				this.abilitazioneMailOutWidget.removeFromParent();
			}

			if (this.settoreWidget != null) {
				this.settoreWidget.removeFromParent();
			}
		}

		resetDisclosurePanels(false);
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

		this.copia.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				creaPerCopiaCommand.execute();
			}
		});
	}

	@Override
	public AnagraficaRuolo getAnagraficaRuolo() {
		AnagraficaRuolo anagraficaRuolo = new AnagraficaRuolo();
		anagraficaRuolo.setRuolo(this.nomeGruppoLdap.getValue().trim());
		anagraficaRuolo.setEtichetta(this.nome.getValue().trim());
		anagraficaRuolo.setStato(this.attivo.getValue() ? Stato.ATTIVA : Stato.DISATTIVA);
		anagraficaRuolo.setEmailPredefinita(this.mailAssegnazione.getValue() != null && !this.mailAssegnazione.getValue().isEmpty() ? this.mailAssegnazione.getValue().toLowerCase().trim() : null);
		anagraficaRuolo.setDataCreazione(dataCreazione.getValue());
		anagraficaRuolo.setUsernameCreazione(utenteCreazione.getText());
		return anagraficaRuolo;
	}

	@Override
	public Settore getSettore() {
		return settoreWidget != null ? settoreWidget.getSettore() : null;
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
	public void resetErrors() {
		messageWidgetPanel.setVisible(false);
		messageWidget.reset();
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
	public void setCreaRuoloPerCopiaCommand(Command command) {
		this.creaPerCopiaCommand = command;
	}

	private void resetDisclosurePanels(boolean showActions) {
		dettaglioDisclosure.setOpen(!showActions);
		settoreDisclosurePanel.setOpen(false);
		abilitazioniFascicoliDisclosurePanel.setOpen(false);
		abilitazioniIngressiDisclosurePanel.setOpen(false);
		abilitazioniUscitaDisclosurePanel.setOpen(false);
		azioniDisclosurePanel.setOpen(showActions);
	}

	@Override
	public void clearView() {
		resetAll(false);
	}

	@Override
	public List<Abilitazione> getAbilitazioniDaAggiungere() {
		List<Abilitazione> res = new ArrayList<Abilitazione>();

		if (abilitazioneFascicoloWidget != null && abilitazioneFascicoloWidget.getAbilitazioniDaAggiungere() != null) {
			res.addAll(abilitazioneFascicoloWidget.getAbilitazioniDaAggiungere());
		}

		if (abilitazioneIngressiWidget != null && abilitazioneIngressiWidget.getAbilitazioniDaAggiungere() != null) {
			res.addAll(abilitazioneIngressiWidget.getAbilitazioniDaAggiungere());
		}

		if (abilitazioneMailOutWidget != null && abilitazioneMailOutWidget.getAbilitazioniDaAggiungere() != null) {
			res.addAll(abilitazioneMailOutWidget.getAbilitazioniDaAggiungere());
		}

		if (!this.driveAttivo && this.drive.getValue())
			res.add(new GestioneDriveAbilitazione());

		return res;
	}

	@Override
	public List<Abilitazione> getAbilitazioniDaRimuovere() {
		List<Abilitazione> res = new ArrayList<Abilitazione>();

		if (abilitazioneFascicoloWidget != null && abilitazioneFascicoloWidget.getAbilitazioniDaRimuovere() != null) {
			res.addAll(abilitazioneFascicoloWidget.getAbilitazioniDaRimuovere());
		}

		if (abilitazioneIngressiWidget != null && abilitazioneIngressiWidget.getAbilitazioniDaRimuovere() != null) {
			res.addAll(abilitazioneIngressiWidget.getAbilitazioniDaRimuovere());
		}

		if (abilitazioneMailOutWidget != null && abilitazioneMailOutWidget.getAbilitazioniDaRimuovere() != null) {
			res.addAll(abilitazioneMailOutWidget.getAbilitazioniDaRimuovere());
		}

		if (this.driveAttivo && !this.drive.getValue())
			res.add(new GestioneDriveAbilitazione());

		return res;
	}

	@Override
	public List<Azione> getAzioni() {
		List<Azione> res = new ArrayList<Azione>();

		if (abilitazioneFascicoloWidget != null && abilitazioneFascicoloWidget.getAzioni() != null) {
			res.addAll(abilitazioneFascicoloWidget.getAzioni());
		}

		if (abilitazioneIngressiWidget != null && abilitazioneIngressiWidget.getAzioni() != null) {
			res.addAll(abilitazioneIngressiWidget.getAzioni());
		}

		if (abilitazioneMailOutWidget != null && abilitazioneMailOutWidget.getAzioni() != null) {
			res.addAll(abilitazioneMailOutWidget.getAzioni());
		}

		return res;
	}
}
