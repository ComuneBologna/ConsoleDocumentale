package it.eng.portlet.consolepec.gwt.client.view.protocollazione;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.Titolazione;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.FormProtocollazionePresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.bean.DescrittoreGruppoProtocollazione;
import it.eng.portlet.consolepec.gwt.client.scan.ConfigWidget;
import it.eng.portlet.consolepec.gwt.client.scan.ScanWidget;
import it.eng.portlet.consolepec.gwt.client.scan.VisitableWidget;
import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.widget.CustomSuggestBox;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoPECElencoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoPECElencoWidget.MostraPEC;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoPraticaModulisticaElencoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoPraticaModulisticaElencoWidget.MostraPraticaModulistica;
import it.eng.portlet.consolepec.gwt.client.widget.GroupSuggestBoxProtocollazione;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.client.widget.protocollazione.ElementoGruppoProtocollazioneWidget;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecUtils;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.DatiDefaultProtocollazione;
import it.eng.portlet.consolepec.gwt.shared.model.PecDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipoProtocollazione;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.base.Strings;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.ViewImpl;

public class FormProtocollazioneView extends ViewImpl implements FormProtocollazionePresenter.MyView {

	private final Widget widget;

	@UiField Button confermaButton;
	@UiField Button annullaButton;
	@UiField Button indietroButton;
	@UiField Label titolo;

	@UiField DownloadAllegatoWidget downloadWidget;

	@UiField HTMLPanel panelProtocollazione;

	@UiField(provided = true) MessageAlertWidget messaggioAlertWidget;

	private Command<Void, AllegatoDTO> mostraDettaglioAllegatoCommand;
	private Command<Void, PraticaDTO> mostraDettaglioPratica;

	private List<VisitableWidget> widgetList = new ArrayList<VisitableWidget>();

	com.google.gwt.i18n.client.DateTimeFormat dateFormat = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATA);
	com.google.gwt.i18n.client.DateTimeFormat timeFormat = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_TIMESEC);

	private final GroupSuggestBoxProtocollazione groupSuggestBox;

	private final List<String> tipiProtocollazione = Arrays.asList("E", "I", "U");

	private final List<String> riservatezza = Arrays.asList("S", "N");

	private TreeSet<DescrittoreGruppoProtocollazione> descrittoriGruppiProtocollazioni = new TreeSet<DescrittoreGruppoProtocollazione>();

	public interface Binder extends UiBinder<Widget, FormProtocollazioneView> {
		//
	}

	@Inject
	public FormProtocollazioneView(final Binder binder, final EventBus eventBus, final GroupSuggestBoxProtocollazione groupSuggestBox) {

		messaggioAlertWidget = new MessageAlertWidget(eventBus);
		this.groupSuggestBox = groupSuggestBox;
		widget = binder.createAndBindUi(this);

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public Button getConfermaButton() {
		return confermaButton;
	}

	@Override
	public List<VisitableWidget> getWidget() {

		widgetList = new ArrayList<VisitableWidget>(getNewList(widgetList));

		for (DescrittoreGruppoProtocollazione descrittoreGruppoProtocollazione : descrittoriGruppiProtocollazioni)
			if (descrittoreGruppoProtocollazione.getMultiPanel() != null) {
				for (Campo campo : descrittoreGruppoProtocollazione.getMultiPanel().convertDto()) {
					widgetList.add(new VisitableWidget(campo));
				}
			}
		return this.widgetList;
	}

	private ArrayList<? extends VisitableWidget> getNewList(List<VisitableWidget> widgetList) {
		ArrayList<VisitableWidget> newWidgetList = new ArrayList<VisitableWidget>();

		for (VisitableWidget visitableWidget : widgetList)
			if (visitableWidget.getMaxOccurs() == 1) newWidgetList.add(visitableWidget);
		return newWidgetList;
	}

	@Override
	public Button getAnnullaButton() {
		return annullaButton;
	}

	@Override
	public Button getIndietroButton() {
		return indietroButton;
	}

	private String oggettoCapoFila;

	@Override
	public void setOggettoCapoFila(String oggetto) {
		this.oggettoCapoFila = oggetto;
	}

	@Override
	public void init(Map<String, Campo> campi, DatiDefaultProtocollazione datiDefaultProtocollazione, String tipoProtocollazione,
			boolean disableList, DispatchAsync dispatchAsync, Set<PraticaDTO> pratiche, Set<AllegatoDTO> allegati) {

		// pulizia del pannello della protocollazione
		panelProtocollazione.getElement().setInnerHTML("");

		datiDefaultProtocollazione.initNumeroAllegati(pratiche, allegati);

		// crea i widget e li aggiunge al pannello
		creaListWidget(campi);

		mostraOggettiDiProtocollazione(allegati, pratiche);

		// configura il form
		ScanWidget sw = new ConfigWidget();
		sw.scanListWidget(widgetList);

		if(pratiche.size() == 1 && allegati.size() == 0){
				for(PraticaDTO praticaDTO : pratiche){
					if(praticaDTO instanceof PecInDTO){
						PecInDTO pecInDTO = (PecInDTO) praticaDTO;
						
						Date date = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATAORA).parse(pecInDTO.getDataOraArrivo());
						
						datiDefaultProtocollazione.setDataDiProtocollazione(date);
						
						break;
					} 
				}
		}

		// imposta i date formatter
		Format dateFormatter = new DateBox.DefaultFormat(dateFormat);
		((DateBox) getWidgetByName("i1_data_arrivo")).setFormat(dateFormatter);
		((DateBox) getWidgetByName("i1_data_arrivo")).setValue(datiDefaultProtocollazione.getDataDiProtocollazione());

		Format timeFormatter = new DateBox.DefaultFormat(timeFormat);
		((DateBox) getWidgetByName("i1_ora_arrivo")).setFormat(timeFormatter);
		((DateBox) getWidgetByName("i1_ora_arrivo")).setValue(datiDefaultProtocollazione.getDataDiProtocollazione());

		if (!Strings.isNullOrEmpty(oggettoCapoFila) && getWidgetByName("il_oggetto_capofila") != null) {
			((TextArea) getWidgetByName("il_oggetto_capofila")).setValue(oggettoCapoFila);
		} else {
			((TextArea) getWidgetByName("i1_oggetto")).setValue(datiDefaultProtocollazione.getOggettoProtocollazione());
		}

		((TextBox) getWidgetByName("i1_numero_allegati")).setValue("" + datiDefaultProtocollazione.getNumeroAllegati());
		((TextBox) getWidgetByName("i1_provenienza")).setValue(datiDefaultProtocollazione.getProvenienza());
		((TextBox) getWidgetByName("i1_cf_provenienza")).setValue(datiDefaultProtocollazione.getCodiceFiscaleProvenienza());

		initListBox(tipoProtocollazione);

		CustomSuggestBox[] trs = groupSuggestBox.getSuggestBoxTitoloRubricaSezione(null);
		String idTitolo = datiDefaultProtocollazione.getDatiPg().getIdTitolo();
		String idRubrica = datiDefaultProtocollazione.getDatiPg().getIdRubrica();
		String idSezione = datiDefaultProtocollazione.getDatiPg().getIdSezione();

		((HTMLPanel) getWidgetByName("i1_codice_titolo")).clear();
		((HTMLPanel) getWidgetByName("i1_codice_rubrica")).clear();
		((HTMLPanel) getWidgetByName("i1_codice_sezione")).clear();
		((HTMLPanel) getWidgetByName("i1_tipologia_documento")).clear();
		((HTMLPanel) getWidgetByName("i1_codice_provenienza")).clear();
		((HTMLPanel) getWidgetByName("i1_codice_destinatario")).clear();

		((HTMLPanel) getWidgetByName("i1_codice_titolo")).add(trs[0]);
		((HTMLPanel) getWidgetByName("i1_codice_rubrica")).add(trs[1]);
		((HTMLPanel) getWidgetByName("i1_codice_sezione")).add(trs[2]);
		((HTMLPanel) getWidgetByName("i1_tipologia_documento")).add(groupSuggestBox.getSuggestBoxTipologiaDocumento());
		((HTMLPanel) getWidgetByName("i1_codice_provenienza")).add(groupSuggestBox.getSuggestBoxCodiceProvenienza(
				(TextBox) getWidgetByName("i1_provenienza"), tipoProtocollazione));
		((HTMLPanel) getWidgetByName("i1_codice_destinatario")).add(groupSuggestBox.getSuggestBoxCodiceDestinatari((TextBox) getWidgetByName("i1_destinatario")));

		if (idTitolo != null) {
			String titoloName = GroupSuggestBoxProtocollazione.getTitoloNameByIdDisplayName(idTitolo);
			getCustomSuggestBoxByName("i1_codice_titolo").setEnabled(false);
			getCustomSuggestBoxByName("i1_codice_titolo").setStylePrimaryName("testo disabilitato");
			getCustomSuggestBoxByName("i1_codice_titolo").setText(idTitolo + " - " + titoloName);
			getCustomSuggestBoxByName("i1_codice_titolo").setIdentificativo(idTitolo);
		}
		if (idRubrica != null) {
			String rubricaName = GroupSuggestBoxProtocollazione.getRubricaNameByIdTitolo(datiDefaultProtocollazione.getDatiPg().getIdRubrica(),
					idTitolo);
			getCustomSuggestBoxByName("i1_codice_rubrica").setEnabled(false);
			getCustomSuggestBoxByName("i1_codice_rubrica").setStyleName("testo disabilitato");
			getCustomSuggestBoxByName("i1_codice_rubrica").setText(idRubrica + " - " + rubricaName);
			getCustomSuggestBoxByName("i1_codice_rubrica").setIdentificativo(idRubrica);
		}
		if (idSezione != null) {
			String sezioneName = GroupSuggestBoxProtocollazione.getSezioneNameByIdRubrica(idSezione, idTitolo,
					datiDefaultProtocollazione.getDatiPg().getIdRubrica());
			getCustomSuggestBoxByName("i1_codice_sezione").setEnabled(false);
			getCustomSuggestBoxByName("i1_codice_sezione").setStyleName("testo disabilitato");
			getCustomSuggestBoxByName("i1_codice_sezione").setText(idSezione + " - " + sezioneName);
			getCustomSuggestBoxByName("i1_codice_sezione").setIdentificativo(idSezione);
		}
		//
		// inizializza il default di protocollazione in base al tipo di fascicolo
		if (idTitolo == null && idSezione == null && idRubrica == null) {
			Titolazione titolazione = datiDefaultProtocollazione.getTitolazione();
			if (titolazione != null) {

				if (!Strings.isNullOrEmpty(titolazione.getTitolo())) {
					String titoloName = GroupSuggestBoxProtocollazione.getTitoloNameByIdDisplayName(titolazione.getTitolo());
					assert !Strings.isNullOrEmpty(titoloName);
					getCustomSuggestBoxByName("i1_codice_titolo").setText(titolazione.getTitolo() + " - " + titoloName);
					getCustomSuggestBoxByName("i1_codice_titolo").setIdentificativo(titolazione.getTitolo());
				}
				if (!Strings.isNullOrEmpty(titolazione.getRubrica())) {
					String rubricaName = GroupSuggestBoxProtocollazione.getRubricaNameByIdTitolo(titolazione.getRubrica(), titolazione.getTitolo());
					assert !Strings.isNullOrEmpty(rubricaName);
					getCustomSuggestBoxByName("i1_codice_rubrica").setText(titolazione.getRubrica() + " - " + rubricaName);
					getCustomSuggestBoxByName("i1_codice_rubrica").setIdentificativo(titolazione.getRubrica());
				}
				if (!Strings.isNullOrEmpty(titolazione.getSezione())) {
					String sezioneName = GroupSuggestBoxProtocollazione.getSezioneNameByIdRubrica(titolazione.getSezione(), titolazione.getTitolo(),
							titolazione.getRubrica());
					assert !Strings.isNullOrEmpty(sezioneName);
					getCustomSuggestBoxByName("i1_codice_sezione").setText(titolazione.getSezione() + " - " + sezioneName);
					getCustomSuggestBoxByName("i1_codice_sezione").setIdentificativo(titolazione.getSezione());
				}
				if (!Strings.isNullOrEmpty(titolazione.getTipologiaDocumento())) {
					String tipologiaName = GroupSuggestBoxProtocollazione.getTipoDocumentoNameByIdDisplayName(titolazione.getTipologiaDocumento());
					assert !Strings.isNullOrEmpty(tipologiaName);
					getCustomSuggestBoxByName("i1_tipologia_documento").setText(titolazione.getTipologiaDocumento() + " - " + tipologiaName);
					getCustomSuggestBoxByName("i1_tipologia_documento").setIdentificativo(titolazione.getTipologiaDocumento());
				}
			}
		}
		//
		if (getCustomSuggestBoxByName("i1_codice_provenienza") != null) {
			(getCustomSuggestBoxByName("i1_codice_provenienza")).getValueBox().addKeyDownHandler(new KeyDownHandler() {

				@Override
				public void onKeyDown(KeyDownEvent event) {
					int key = event.getNativeEvent().getKeyCode();
					if (key != KeyCodes.KEY_ENTER && key != KeyCodes.KEY_TAB && key != KeyCodes.KEY_ALT) {
						if (((TextBox) getWidgetByName("i1_provenienza")) != null) ((TextBox) getWidgetByName("i1_provenienza")).setValue(null);
					}
				}
			});
		}

		if (getCustomSuggestBoxByName("i1_codice_destinatario") != null) {
			(getCustomSuggestBoxByName("i1_codice_destinatario")).getValueBox().addKeyDownHandler(new KeyDownHandler() {

				@Override
				public void onKeyDown(KeyDownEvent event) {
					int key = event.getNativeEvent().getKeyCode();
					if (key != KeyCodes.KEY_ENTER && key != KeyCodes.KEY_TAB && key != KeyCodes.KEY_ALT) {
						if (((TextBox) getWidgetByName("i1_destinatario")) != null) ((TextBox) getWidgetByName("i1_destinatario")).setValue(null);
					}
				}
			});
		}

		if (((TextBox) getWidgetByName("i1_provenienza")) != null) {
			((TextBox) getWidgetByName("i1_provenienza")).addKeyDownHandler(new KeyDownHandler() {

				@Override
				public void onKeyDown(KeyDownEvent event) {
					if (event.getNativeKeyCode() != KeyCodes.KEY_ENTER && event.getNativeKeyCode() != KeyCodes.KEY_TAB
							&& event.getNativeKeyCode() != KeyCodes.KEY_ALT) if (getCustomSuggestBoxByName("i1_codice_provenienza") != null) getCustomSuggestBoxByName(
							"i1_codice_provenienza").setValue(null);
				}
			});
		}

		if (((TextBox) getWidgetByName("i1_destinatario")) != null) {
			((TextBox) getWidgetByName("i1_destinatario")).addKeyDownHandler(new KeyDownHandler() {

				@Override
				public void onKeyDown(KeyDownEvent event) {
					if (event.getNativeKeyCode() != KeyCodes.KEY_ENTER && event.getNativeKeyCode() != KeyCodes.KEY_TAB
							&& event.getNativeKeyCode() != KeyCodes.KEY_ALT) if (getCustomSuggestBoxByName("i1_codice_destinatario") != null) getCustomSuggestBoxByName(
							"i1_codice_destinatario").setValue(null);
				}
			});
		}

		if (disableList) setEnabledListBox(tipoProtocollazione.equalsIgnoreCase(TipoProtocollazione.INTERNA.getCodice()));
		else setEnabledListBox(true);

		initListBoxTipologiaNominativo();
		// initListBoxRiservatezza(campi.get("i1_fascicolo_riservato").getValore()); // valore preso dal campo del DB
		initListBoxRiservatezza(datiDefaultProtocollazione.isProtocollazioneRiservata() ? "S" : "N");
	}

	private void initListBoxTipologiaNominativo() {
		ListBox tipoProtocollazioneListBox = (ListBox) getWidgetByName("i2_tipo_nominativo");
		tipoProtocollazioneListBox.addItem("Mittente", "M");
		tipoProtocollazioneListBox.addItem("Destinatario", "D");
		tipoProtocollazioneListBox.addItem("Interessato", "I");
	}

	private void initListBoxRiservatezza(String valorePredefinito) {
		ListBox box = (ListBox) getWidgetByName("i1_fascicolo_riservato");
		box.addItem("Si", "S");
		box.addItem("No", "N");
		box.setItemSelected(riservatezza.indexOf(valorePredefinito), true);
	}

	private void initListBox(String tipoProtocollazione) {
		ListBox tipoProtocollazioneListBox = getTipoProtocollazioneListBox();
		tipoProtocollazioneListBox.addItem("Entrata", "E");
		tipoProtocollazioneListBox.addItem("Interna", "I");
		tipoProtocollazioneListBox.addItem("Uscita", "U");
		tipoProtocollazioneListBox.setItemSelected(tipiProtocollazione.indexOf(tipoProtocollazione), true);
	}

	private void creaListWidget(Map<String, Campo> campi) {
		widgetList = new ArrayList<VisitableWidget>();
		Map<String, HTMLPanel> sezioniProtocollazione = getSezioniProtocollazione(campi);
		TreeSet<Campo> listaCampi = new TreeSet<Campo>(campi.values());
		for (Campo campo : listaCampi) {
			VisitableWidget visitableWidget = new VisitableWidget(campo);
			widgetList.add(visitableWidget);
			if (visitableWidget.isVisibile()) {
				sezioniProtocollazione.get(campo.getTipoRecord()).add(getDivElement(visitableWidget));
			}
		}
	}

	private Map<String, HTMLPanel> getSezioniProtocollazione(Map<String, Campo> campi) {
		Map<String, HTMLPanel> sezioni = new HashMap<String, HTMLPanel>();

		descrittoriGruppiProtocollazioni = new TreeSet<DescrittoreGruppoProtocollazione>();

		for (Campo c : campi.values()) {
			if (c.isVisibile()) descrittoriGruppiProtocollazioni.add(new DescrittoreGruppoProtocollazione(c.getTipoRecord(),
					c.getDescrizioneGruppo(), c.getMaxOccurs()));
		}

		boolean open = true;
		for (DescrittoreGruppoProtocollazione descrittoreGruppoProtocollazione : descrittoriGruppiProtocollazioni) {
			DisclosurePanel disclosurePanel = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(),
					OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), descrittoreGruppoProtocollazione.getDescrizione());
			disclosurePanel.setAnimationEnabled(true);
			disclosurePanel.setOpen(open);
			open = false;
			HTMLPanel htmlPanel = null;

			if (descrittoreGruppoProtocollazione.getMaxOccurs() == 1) {
				htmlPanel = initHtmlPanel(disclosurePanel, descrittoreGruppoProtocollazione);
			} else {
				htmlPanel = initHtmlMultiplePanel(disclosurePanel, descrittoreGruppoProtocollazione);
			}

			panelProtocollazione.add(disclosurePanel);
			sezioni.put(descrittoreGruppoProtocollazione.getNomeGruppo(), htmlPanel);
		}

		return sezioni;
	}

	@Override
	public void setTipoProtocollo(String valore) {
		((TextBox) getWidgetByName("i1_tipo_protocollazione")).setValue(valore);
	}

	@Override
	public void setTitle(String string) {
		titolo.setText(string);
	}

	// @Override
	// public void setFascicoloRiservato(boolean riservato) {
	// //((TextBox) getWidgetByName("i1_fascicolo_riservato")).setValue(riservato ? "S" : "N");
	// ListBox box = (ListBox) getWidgetByName("i1_fascicolo_riservato");
	// box.setItemSelected(riservatezza.indexOf(riservato ? "S" : "N"), true);
	// }

	@Override
	public ListBox getTipoProtocollazioneListBox() {
		return (ListBox) getWidgetByName("i1_tipo_protocollazione");
	}

	@Override
	public String getAnnoCapoFila() {
		return ((TextBox) getWidgetByName("i1_anno_protocollo_capofila")).getValue();
	}

	@Override
	public String getNumeroCapoFila() {
		return ((TextBox) getWidgetByName("i1_numero_protocollo_capofila")).getValue();
	}

	@Override
	public void setEnabledListBox(boolean enabled) {
		getTipoProtocollazioneListBox().setEnabled(enabled);
		if (enabled) getTipoProtocollazioneListBox().setStyleName("testo");
		else getTipoProtocollazioneListBox().setStyleName("testo disabilitato");
	}

	/* Metodi privati */
	private HTMLPanel getDivElement(VisitableWidget visitableWidget) {

		HTMLPanel div = new HTMLPanel("");

		div.setStyleName("cell");

		Element span = DOM.createSpan();
		span.addClassName("labelProtocollazione");
		String descrizione = visitableWidget.getDescrizione() + (visitableWidget.isObbligatorio() ? " *" : "");
		span.setInnerText(descrizione);

		div.getElement().appendChild(span);

		div.add(visitableWidget.getWidget());

		return div;
	}

	private HTMLPanel initHtmlPanel(DisclosurePanel disclosurePanel, DescrittoreGruppoProtocollazione descrittoreGruppoProtocollazione) {
		HTMLPanel filters = new HTMLPanel("");
		filters.setStyleName("filters");
		CaptionPanel captionPanel = new CaptionPanel();
		filters.add(captionPanel);
		HTMLPanel body = new HTMLPanel("");
		captionPanel.add(body);
		disclosurePanel.add(filters);
		return body;
	}

	private HTMLPanel initHtmlMultiplePanel(DisclosurePanel disclosurePanel, DescrittoreGruppoProtocollazione descrittoreGruppoProtocollazione) {
		HTMLPanel filters = new HTMLPanel("");
		filters.setStyleName("filters");
		CaptionPanel captionPanel = new CaptionPanel();
		filters.add(captionPanel);

		HTMLPanel body = new HTMLPanel("");

		Widget multiPanel = descrittoreGruppoProtocollazione.getMultiPanel();
		body.add(multiPanel);

		captionPanel.add(body);

		disclosurePanel.add(filters);
		return ((ElementoGruppoProtocollazioneWidget<?>) multiPanel).getGruppoPanel();
	}

	private Widget getWidgetByName(String widgetName) {
		for (VisitableWidget visitableWidget : widgetList)
			if (visitableWidget.getNome().equals(widgetName)) return visitableWidget.getWidget();
		return null;
	}

	private CustomSuggestBox getCustomSuggestBoxByName(String widgetName) {
		return (CustomSuggestBox) ((HTMLPanel) getWidgetByName(widgetName)).getWidget(0);
	}

	private void mostraOggettiDiProtocollazione(Set<AllegatoDTO> allegati, Set<PraticaDTO> pratiche) {
		DisclosurePanel disclosurePanel = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(),
				OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Allegati della protocollazione");
		disclosurePanel.setAnimationEnabled(true);
		disclosurePanel.setOpen(true);

		HTMLPanel htmlRootPanel = new HTMLPanel("");
		disclosurePanel.add(htmlRootPanel);

		UListElement ulCapofila = UListElement.as(DOM.createElement("ul"));
		ulCapofila.setClassName("contenitore-lista-gruppi");
		htmlRootPanel.getElement().appendChild(ulCapofila);

		if (allegati.size() > 0) crateAllegatiPanel(allegati, htmlRootPanel, ulCapofila);

		if (pratiche.size() > 0) cratePraticaPanel(pratiche, htmlRootPanel, ulCapofila);

		panelProtocollazione.add(disclosurePanel);
	}

	private void crateAllegatiPanel(Set<AllegatoDTO> allegati, HTMLPanel htmlRootPanel, UListElement ulCapofila) {

		LIElement curLi = LIElement.as(DOM.createElement("li"));
		ulCapofila.appendChild(curLi);
		curLi.setClassName("gruppo clearfix");
		/* span PG */
		SpanElement pgSpan = SpanElement.as(DOM.createSpan());
		pgSpan.setClassName("label nessun-protocollo");
		pgSpan.setInnerHTML("Documenti");// spazio importante

		curLi.appendChild(pgSpan);
		/* div corpo */
		HTMLPanel corpoDIV = new HTMLPanel("");
		corpoDIV.setStyleName("corpo");
		htmlRootPanel.add(corpoDIV, curLi);// curLi.add(corpoDIV);
		/*
		 * creo div box e documenti, nel caso il gruppo abbia dei files e / o email
		 */
		HTMLPanel boxDIV = new HTMLPanel(""); // .as(DOM.createElement("div"));
		boxDIV.setStyleName("box-mail last");
		corpoDIV.add(boxDIV);

		// DivElement.as(DOM.createElement("div"));
		ulCapofila.appendChild(curLi);

		for (final AllegatoDTO allegatoDTO : allegati) {
			HTMLPanel documentiDIV = new HTMLPanel("");
			documentiDIV.setStyleName("documenti-mail");
			CheckBox check = new CheckBox();
			check.setStyleName("checkbox-nonprot");
			Anchor linkFirma = new Anchor();
			documentiDIV.add(linkFirma);
			linkFirma.setStyleName("ico verifica");
			linkFirma.setTitle("Visualizza dettagli");
			linkFirma.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					mostraDettaglioAllegatoCommand.exe(allegatoDTO);
				}
			});
			Image iconaFirma = new Image(allegatoDTO.getIconaStato(ConsolePECIcons._instance));
			linkFirma.getElement().appendChild(iconaFirma.getElement());

			Anchor link = new Anchor(ConsolePecUtils.getLabelExtended(allegatoDTO));

			link.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					SafeUri uri = UriMapping.generaDownloadAllegatoServletURL(allegatoDTO.getClientID(), allegatoDTO);
					downloadWidget.sendDownload(uri);
				}
			});

			link.setTitle(allegatoDTO.getLabel());

			SpanElement spanLink = Document.get().createSpanElement();
			spanLink.setClassName("documento-semplice");
			documentiDIV.getElement().appendChild(spanLink);
			documentiDIV.add(link, spanLink);
			boxDIV.add(documentiDIV);
		}
	}

	private void cratePraticaPanel(Set<PraticaDTO> pratiche, HTMLPanel htmlRootPanel, UListElement ulCapofila) {

		LIElement curLi = LIElement.as(DOM.createElement("li"));
		ulCapofila.appendChild(curLi);
		curLi.setClassName("gruppo clearfix");
		/* span PG */
		SpanElement pgSpan = SpanElement.as(DOM.createSpan());
		pgSpan.setClassName("label nessun-protocollo");
		pgSpan.setInnerHTML("Pratiche");// spazio importante

		curLi.appendChild(pgSpan);
		/* div corpo */
		HTMLPanel corpoDIV = new HTMLPanel("");
		corpoDIV.setStyleName("corpo");
		htmlRootPanel.add(corpoDIV, curLi);// curLi.add(corpoDIV);
		/*
		 * creo div box e documenti, nel caso il gruppo abbia dei files e / o email
		 */
		HTMLPanel boxDIV = new HTMLPanel(""); // .as(DOM.createElement("div"));
		boxDIV.setStyleName("box-mail last");
		corpoDIV.add(boxDIV);

		// DivElement.as(DOM.createElement("div"));
		ulCapofila.appendChild(curLi);

		for (final PraticaDTO pratica : pratiche) {
			if (pratica instanceof PecDTO) {
				ElementoPECElencoWidget widget = new ElementoPECElencoWidget();
				widget.setCheckBoxVisible(false);
				widget.setCheckBoxEnabled(false);

				widget.setMostraDettaglioPEC(new Command<Void, MostraPEC>() {
					@Override
					public Void exe(MostraPEC t) {
						return mostraDettaglioPratica.exe(pratica);
					}
				});
				widget.mostraDettaglio((PecDTO) pratica);

				boxDIV.add(widget);
			} else if (pratica instanceof PraticaModulisticaDTO) {
				ElementoPraticaModulisticaElencoWidget elementoPraticaModulisticaElencoWidget = new ElementoPraticaModulisticaElencoWidget();
				elementoPraticaModulisticaElencoWidget.setCheckBoxVisible(false);
				elementoPraticaModulisticaElencoWidget.setCheckBoxEnabled(false);
				elementoPraticaModulisticaElencoWidget.setMostraDettaglioPraticaModulistica(new Command<Void, MostraPraticaModulistica>() {
					@Override
					public Void exe(MostraPraticaModulistica t) {
						return mostraDettaglioPratica.exe(pratica);
					}
				});
				elementoPraticaModulisticaElencoWidget.mostraDettaglio((PraticaModulisticaDTO) pratica);
				boxDIV.add(elementoPraticaModulisticaElencoWidget);
			}

		}
	}

	@Override
	public void setCommandDettaglioAllegato(Command<Void, AllegatoDTO> command) {
		this.mostraDettaglioAllegatoCommand = command;
	}

	@Override
	public void setCommandDettaglioPratica(Command<Void, PraticaDTO> command) {
		this.mostraDettaglioPratica = command;
	}

}
