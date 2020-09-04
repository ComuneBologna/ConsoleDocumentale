package it.eng.portlet.consolepec.gwt.client.widget.template;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.event.SceltaConfermaAnnullaEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.operazioni.template.TemplateCreazioneApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.template.TemplateCreazioneApiClient.CallbackMap;
import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.AnagraficheRuoliSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO.StatoTemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.CampoTemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.CampoTemplateDTO.TipoCampoTemplateDTO;

/**
 *
 * @author biagiot
 *
 */
public abstract class AbstractCorpoTemplateWidget<T extends BaseTemplateDTO> extends Composite {

	@UiField
	TextBox nomeTextBox;
	@UiField
	TextBox descrizioneTextBox;
	@UiField
	Label idDocumentaleLabel;
	@UiField
	SpanElement statoSpan;
	@UiField
	Label statoLabel;
	@UiField
	HTMLPanel idDocumentalePanel;
	@UiField
	HTMLPanel informazioniTemplatePanel;
	@UiField
	TextBox utenteTextBox;
	@UiField
	DateBox dataCreazionePraticaDateBox;
	@UiField
	HTMLPanel elencoGruppiSuggestBoxPanel;
	@UiField(provided = true)
	ElencoTipiFascicoloWidget elencoTipiFascicoloWidget;
	@UiField
	HTMLPanel listaCampiTemplatePanel;
	@UiField(provided = true) //
	DisclosurePanel listaCampiDisclosurePanel = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Campi");
	@UiField(provided = true) //
	DisclosurePanel tipiFascicoloDisclosurePanel = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Fascicoli Abilitati");

	protected TemplateCreazioneApiClient templateCreazioneApiClient;
	protected final String VALIDATE_FORM_ERROR = "I campi in rosso devono essere valorizzati correttamente";
	protected SuggestBox gruppiSuggestBox;
	protected Map<String, String> etichetteMetadatiMap;
	private DateTimeFormat dateFormat = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATAORA);
	protected ListaCampiTemplateWidget listaCampiTemplateWidget;

	it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CampoTemplateDTO> eliminaCampoCommand;
	it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CampoTemplateDTO> aggiungiCampoCommand;

	public AbstractCorpoTemplateWidget(ConfigurazioniHandler configurazioniHandler, TemplateCreazioneApiClient templateCreazioneApiClient) {
		this.templateCreazioneApiClient = templateCreazioneApiClient;
		elencoTipiFascicoloWidget = new ElencoTipiFascicoloWidget(configurazioniHandler, new AggiungiFascicoloCommand(), new EliminaFascicoloCommand());
	}

	public void initListaCampiTemplate() {
		listaCampiTemplateWidget = new ListaCampiTemplateWidget(100);
		listaCampiTemplateWidget.render(true, true);
		listaCampiTemplatePanel.add(listaCampiTemplateWidget);
	}

	public abstract T getTemplate();

	public void setTemplate(T template) {
		nomeTextBox.setValue(template.getNome());
		descrizioneTextBox.setValue(template.getDescrizione());

		elencoTipiFascicoloWidget.clear();
		for (TipologiaPratica fa : template.getFascicoliAbilitati()) {
			elencoTipiFascicoloWidget.addTipoFascicoloAbilitatoToElenco(fa);
		}

		manageListaCampiWidget(elencoTipiFascicoloWidget.getValues().size() == 1, template.getCampi());

		utenteTextBox.setValue(template.getUtenteCreazione());
		gruppiSuggestBox.setValue(template.getAssegnatario());

		if (template.getDataOraCreazione() != null) {
			dataCreazionePraticaDateBox.setValue(dateFormat.parse(template.getDataOraCreazione()));
		}

		if (template.getNumeroRepertorio() != null) {
			idDocumentaleLabel.setText(template.getNumeroRepertorio());
			idDocumentalePanel.setVisible(true);
		}
	}

	public abstract void setReadOnly(boolean readOnly);

	public void clear() {
		elencoTipiFascicoloWidget.clear();
		idDocumentaleLabel.setText("");
		idDocumentalePanel.setVisible(false);
		nomeTextBox.setValue("");
		descrizioneTextBox.setValue("");
		listaCampiDisclosurePanel.setOpen(false);
		tipiFascicoloDisclosurePanel.setOpen(false);
		nomeTextBox.getElement().removeAttribute("required");
		elencoTipiFascicoloWidget.setRequired(false);
	}

	public boolean validateForm(List<String> errors) {
		boolean valid = true;

		boolean campiInRossoObbligatori = false;
		if (!Strings.isNullOrEmpty(nomeTextBox.getValue())) {
			nomeTextBox.getElement().removeAttribute("required");
		} else {
			valid = false;
			nomeTextBox.getElement().setAttribute("required", "required");
			campiInRossoObbligatori = true;
		}

		AnagraficheRuoliSuggestOracle gruppiMultiWordSuggestOracle = (AnagraficheRuoliSuggestOracle) gruppiSuggestBox.getSuggestOracle();
		List<AnagraficaRuolo> ruoliDTO = gruppiMultiWordSuggestOracle.getAnagraficheRuoli();
		Boolean isValidRole = false;

		if (!Strings.isNullOrEmpty(gruppiSuggestBox.getValue())) {

			for (AnagraficaRuolo ruoloDTO : ruoliDTO) {
				if (ruoloDTO.getEtichetta().equalsIgnoreCase(gruppiSuggestBox.getValue())) {
					isValidRole = true;
					break;
				}
			}
		}

		if (isValidRole) {
			gruppiSuggestBox.getElement().removeAttribute("required");
		} else {
			valid = false;
			gruppiSuggestBox.getElement().setAttribute("required", "required");
			campiInRossoObbligatori = true;
		}

		if (elencoTipiFascicoloWidget.getValues().isEmpty() == false) {
			elencoTipiFascicoloWidget.setRequired(false);
		} else {
			valid = false;
			elencoTipiFascicoloWidget.setRequired(true);
			errors.add("Specificare almeno un fascicolo abilitato");
		}

		for (CampoTemplateDTO ct : listaCampiTemplateWidget.getValori())
			if (ct.getTipo().equals(TipoCampoTemplateDTO.METADATA) && elencoTipiFascicoloWidget.getValues().size() != 1) {
				valid = false;
				errors.add("I campi di tipo metadato possono essere specificati selezionando un solo fascicolo");
				break;
			}

		if (campiInRossoObbligatori) {
			errors.add(0, VALIDATE_FORM_ERROR);
		}

		return valid;
	}

	public void headingInformazioniVisibile(boolean visibile) {
		informazioniTemplatePanel.setVisible(visibile);
	}

	public void setTipiFascicoloAbilitati(List<TipologiaPratica> tipiFascicolo) {
		elencoTipiFascicoloWidget.setListaTipiFascicoloAbilitati(tipiFascicolo);
	}

	public void setAggiungiCampoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CampoTemplateDTO> aggiungiCampoCmd) {
		this.aggiungiCampoCommand = aggiungiCampoCmd;
		listaCampiTemplateWidget.setAggiungiCommand(aggiungiCampoCmd);
	}

	public void setEliminaCampoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CampoTemplateDTO> eliminaCampoCmd) {
		this.eliminaCampoCommand = eliminaCampoCmd;
		listaCampiTemplateWidget.setEliminaCommand(eliminaCampoCmd);
	}

	public void addCampo(CampoTemplateDTO obj) {
		listaCampiTemplateWidget.add(obj);
	}

	public void removeCampo(CampoTemplateDTO obj) {
		listaCampiTemplateWidget.remove(obj);
	}

	public void clearFormCampi() {
		listaCampiTemplateWidget.clearForm();
	}

	public void setGruppiSuggestBox(SuggestBox gs) {
		gruppiSuggestBox = gs;
		gruppiSuggestBox.getElement().removeAttribute("required");
		elencoGruppiSuggestBoxPanel.clear();
		elencoGruppiSuggestBoxPanel.getElement().setInnerHTML("");
		elencoGruppiSuggestBoxPanel.add(gs);
	}

	public void setUtente(String utente) {
		this.utenteTextBox.setValue(utente);
	}

	public void setDataCreazionePratica(Date date) {
		this.dataCreazionePraticaDateBox.setValue(date);
	}

	protected void popolaBaseTemplate(T template) {
		template.setNome(nomeTextBox.getValue());
		template.setDescrizione(descrizioneTextBox.getValue());
		template.getFascicoliAbilitati().addAll(elencoTipiFascicoloWidget.getValues());
		template.getCampi().addAll(listaCampiTemplateWidget.getValori());
		template.setUtenteCreazione(utenteTextBox.getValue());
		template.setAssegnatario(gruppiSuggestBox.getValue());
	}

	public void enableStato(StatoTemplateDTO statoTemplate) {
		statoLabel.setText(statoTemplate.getDescrizioneStato());
		statoSpan.getStyle().clearDisplay();
		statoLabel.setVisible(true);
	}

	/*
	 * GESTIONE METADATI LISTA CAMPI WIDGET
	 */

	private TipologiaPratica tipoFascicolo;

	public class AggiungiFascicoloCommand implements Command {

		@Override
		public void execute() {

			if (elencoTipiFascicoloWidget.getValues() != null && elencoTipiFascicoloWidget.getValues().size() == 1) {
				tipoFascicolo = elencoTipiFascicoloWidget.getTipoSelezionato();
				templateCreazioneApiClient.gestisciModificheFascicoli(AbstractCorpoTemplateWidget.this, "<h4>Aggiungendo un fascicolo verranno eliminati i campi di tipo metadato<h4>",
						TemplateCreazioneApiClient.OPERATION_AGGIUNGI_FASCICOLO);

			} else {
				aggiungiFascicolo(elencoTipiFascicoloWidget.getTipoSelezionato());
			}
		}
	}

	public class EliminaFascicoloCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, TipologiaPratica> {

		@Override
		public Void exe(TipologiaPratica tFascicolo) {

			if (elencoTipiFascicoloWidget.getValues() != null && elencoTipiFascicoloWidget.getValues().size() == 1) {
				tipoFascicolo = tFascicolo;
				templateCreazioneApiClient.gestisciModificheFascicoli(AbstractCorpoTemplateWidget.this, "<h4>Eliminando il fascicolo verranno eliminati i campi di tipo metadato<h4>",
						TemplateCreazioneApiClient.OPERATION_ELIMINA_FASCICOLO);

			} else {
				eliminaFascicolo(tFascicolo);
			}

			return null;
		}

	}

	public void onEliminaAggiungiFascicolo(SceltaConfermaAnnullaEvent event, String operation) {
		if (TemplateCreazioneApiClient.OPERATION_AGGIUNGI_FASCICOLO.equalsIgnoreCase(operation)) {
			aggiungiFascicolo(tipoFascicolo);

		} else if (TemplateCreazioneApiClient.OPERATION_ELIMINA_FASCICOLO.equalsIgnoreCase(operation)) {
			eliminaFascicolo(tipoFascicolo);
		}
	}

	private void aggiungiFascicolo(TipologiaPratica tipo) {
		elencoTipiFascicoloWidget.addTipoFascicoloAbilitatoToElenco(tipo);
		boolean abilitaMetadati = elencoTipiFascicoloWidget.getValues() != null && elencoTipiFascicoloWidget.getValues().size() == 1;
		manageListaCampiWidget(abilitaMetadati, pulisciCampiMetadati(listaCampiTemplateWidget.getValori()));
	}

	private void eliminaFascicolo(TipologiaPratica tipo) {
		elencoTipiFascicoloWidget.eliminaFascicolo(tipo);
		boolean abilitaMetadati = elencoTipiFascicoloWidget.getValues() != null && elencoTipiFascicoloWidget.getValues().size() == 1;
		manageListaCampiWidget(abilitaMetadati, pulisciCampiMetadati(listaCampiTemplateWidget.getValori()));
	}

	private void manageListaCampiWidget(boolean enableMetadati, final List<CampoTemplateDTO> campi) {

		listaCampiTemplatePanel.clear();
		listaCampiTemplateWidget = new ListaCampiTemplateWidget(campi != null ? campi.size() + 50 : 100);
		TipologiaPratica tipoPratica = (elencoTipiFascicoloWidget.getValues() != null && !elencoTipiFascicoloWidget.getValues().isEmpty()) ? elencoTipiFascicoloWidget.getValues().get(0) : null;

		if (enableMetadati && tipoPratica != null) {
			templateCreazioneApiClient.loadEtichetteMetadatiMap(tipoPratica, new CallbackMap() {

				@Override
				public void onError(String errorMessage) {
					listaCampiTemplateWidget.render(true, true);
					for (CampoTemplateDTO campo : campi) {
						listaCampiTemplateWidget.add(campo);
					}
					listaCampiTemplateWidget.setAggiungiCommand(aggiungiCampoCommand);
					listaCampiTemplateWidget.setEliminaCommand(eliminaCampoCommand);
					listaCampiTemplatePanel.add(listaCampiTemplateWidget);
				}

				@Override
				public void onComplete(Map<String, String> map) {
					etichetteMetadatiMap = map;
					listaCampiTemplateWidget.enableMetadati(etichetteMetadatiMap);
					listaCampiTemplateWidget.render(true, true);

					for (CampoTemplateDTO campo : campi) {
						listaCampiTemplateWidget.add(campo);
					}

					listaCampiTemplateWidget.setAggiungiCommand(aggiungiCampoCommand);
					listaCampiTemplateWidget.setEliminaCommand(eliminaCampoCommand);
					listaCampiTemplatePanel.add(listaCampiTemplateWidget);
				}
			});

		} else {

			listaCampiTemplateWidget.render(true, true);
			for (CampoTemplateDTO campo : campi) {
				listaCampiTemplateWidget.add(campo);
			}
			listaCampiTemplateWidget.setAggiungiCommand(aggiungiCampoCommand);
			listaCampiTemplateWidget.setEliminaCommand(eliminaCampoCommand);
			listaCampiTemplatePanel.add(listaCampiTemplateWidget);
		}
	}

	private static List<CampoTemplateDTO> pulisciCampiMetadati(List<CampoTemplateDTO> campiConMetadati) {

		List<CampoTemplateDTO> campi = new ArrayList<CampoTemplateDTO>();
		for (CampoTemplateDTO campo : campiConMetadati)
			if (!TipoCampoTemplateDTO.METADATA.equals(campo.getTipo()))
				campi.add(campo);

		return campi;
	}
}
