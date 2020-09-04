package it.eng.portlet.consolepec.gwt.client.view.configurazioni;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.Handler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica.Stato;
import it.eng.cobo.consolepec.util.configurazioni.ConfigurazioniUtil;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler.ConfigurazioniCallback;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.configurazioni.ListaAnagraficaFascicoloPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.configurazioni.ListaAnagraficaFascicoloPresenter.ListaAnagraficheFascicoliProvider;
import it.eng.portlet.consolepec.gwt.client.widget.CustomButtonCell;
import it.eng.portlet.consolepec.gwt.client.widget.CustomPager;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.YesNoRadioButton;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridTableBuilder;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.client.widget.images.ResPager;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;
import it.eng.portlet.consolepec.gwt.client.worklist.CustomSafeImageCell;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;

/**
 *
 * @author biagiot
 *
 */
public class ListaAnagraficaFascicoloView extends ViewImpl implements ListaAnagraficaFascicoloPresenter.MyView, HasHandlers {

	private static final DateTimeFormat dtf = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATAORA);
	private final Widget widget;

	@UiField
	HTMLPanel messageWidgetPanel;
	@UiField(provided = true)
	MessageAlertWidget messageAlertWidget;
	@UiField
	HeadingElement mainTitle;
	@UiField(provided = true)
	DataGridWidget<AnagraficaFascicolo> dataGrid;
	@UiField(provided = true)
	SimplePager pager;

	@UiField(provided = true)
	SuggestBox tipologiaFascicolo;
	@UiField
	DateBox dataCreazioneDa;
	@UiField
	DateBox dataCreazioneA;
	@UiField(provided = true)
	SuggestBox stato = new SuggestBox(new SpacebarSuggestOracle(Arrays.asList(Stato.ATTIVA.name(), Stato.DISATTIVA.name())));
	@UiField(provided = true)
	YesNoRadioButton protocollabile = new YesNoRadioButton("Fascicolo con protocollazione attiva", true);

	@UiField
	Button pulisciButton;
	@UiField
	Button cercaButton;
	@UiField
	Button creaButton;
	@UiField
	Button ricaricaButton;

	private Column<AnagraficaFascicolo, SafeUri> colonnaIcona;
	private ColonnaTipologia colonnaTipologia;
	private ColonnaProtocollazione colonnaProtocollabile;
	private ColonnaStato colonnaStato;
	private ColonnaData colonnaData;
	private ColonnaDettaglio colonnaDettaglio;
	private Column<AnagraficaFascicolo, String> colonnaAzioni;

	private ListaAnagraficheFascicoliProvider provider;
	private ConfigurazioniHandler configurazioniHandler;
	private EventBus eventBus;

	public interface Binder extends UiBinder<Widget, ListaAnagraficaFascicoloView> {
		//
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		mainTitle.scrollIntoView();
		Window.scrollTo(0, 0);
	}

	@Inject
	public ListaAnagraficaFascicoloView(final Binder binder, final EventBus eventBus, final ConfigurazioniHandler configurazioniHandler) {
		this.eventBus = eventBus;
		this.configurazioniHandler = configurazioniHandler;
		this.messageAlertWidget = new MessageAlertWidget(eventBus);

		inizializzaFiltriRicerca();
		inizializzaWorklist();

		widget = binder.createAndBindUi(this);

		configuraFiltriDiRicerca();
		configuraBottoni();
	}

	private void setPlaceholder(Widget widget, String placeholder) {
		InputElement inputElement = widget.getElement().cast();
		inputElement.setAttribute("placeholder", placeholder);
	}

	private void configuraPlaceHolder() {
		setPlaceholder(tipologiaFascicolo, "Tipo fascicolo");
		setPlaceholder(stato, "Stato");
		setPlaceholder(dataCreazioneDa, "Data creazione da");
		setPlaceholder(dataCreazioneA, "Data creazione a");
	}

	private void configuraFiltriDiRicerca() {
		configuraPlaceHolder();
		Format dateFormat = new DateBox.DefaultFormat(DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATA));
		dataCreazioneA.setFormat(dateFormat);
		dataCreazioneA.getDatePicker().setYearArrowsVisible(true);
		dataCreazioneDa.setFormat(dateFormat);
		dataCreazioneDa.getDatePicker().setYearArrowsVisible(true);
	}

	private void inizializzaFiltriRicerca() {
		List<String> etichette = new ArrayList<String>();
		for (AnagraficaFascicolo ai : configurazioniHandler.getAnagraficheFascicoli(false)) {
			etichette.add(ai.getEtichettaTipologia());
		}
		tipologiaFascicolo = new SuggestBox(new SpacebarSuggestOracle(etichette));

		List<String> etichetteRuoli = new ArrayList<String>();
		for (AnagraficaRuolo ar : configurazioniHandler.getAnagraficheRuoli()) {
			etichetteRuoli.add(ar.getEtichetta());
		}

		protocollabile.selectYes();
	}

	private void inizializzaWorklist() {
		pager = new CustomPager(SimplePager.TextLocation.CENTER, ResPager.CSS, false, 0, true, configurazioniHandler.getProprietaGenerali().getMaxRisultatiWorklist());
		dataGrid = new DataGridWidget<AnagraficaFascicolo>(ConsolePecConstants.WORKLIST_NUMERO_PER_PAGINA, null, null);
		configuraColonne();
	}

	private void configuraColonne() {
		colonnaTipologia = new ColonnaTipologia();
		colonnaProtocollabile = new ColonnaProtocollazione();
		colonnaDettaglio = new ColonnaDettaglio();
		colonnaData = new ColonnaData();
		colonnaStato = new ColonnaStato();

		final CustomSafeImageCell cellImg = new CustomSafeImageCell(true);
		colonnaIcona = new Column<AnagraficaFascicolo, SafeUri>(cellImg) {
			@Override
			public SafeUri getValue(AnagraficaFascicolo object) {
				return ConsolePECIcons._instance.fascicolo().getSafeUri();
			}

			@Override
			public void onBrowserEvent(Context context, Element elem, AnagraficaFascicolo object, NativeEvent event) {}
		};

		CustomButtonCell customButtonCell = new CustomButtonCell("btn black worklist_btn");
		colonnaAzioni = new Column<AnagraficaFascicolo, String>(customButtonCell) {

			@Override
			public String getValue(AnagraficaFascicolo pratica) {
				return "Azioni";
			}

			@Override
			public void onBrowserEvent(Context context, Element elem, AnagraficaFascicolo object, NativeEvent event) {
				event.preventDefault();
				if (event.getButton() == NativeEvent.BUTTON_LEFT) {
					dettaglioAnagraficaFascicolo(object, true);
				}
			}
		};

		dataGrid.addColumn(colonnaIcona, "");
		dataGrid.addColumn(colonnaTipologia, "Tipologia fascicolo");
		dataGrid.addColumn(colonnaProtocollabile, "Protocollabile");
		dataGrid.addColumn(colonnaStato, "Stato");
		dataGrid.addColumn(colonnaData, "Data");
		dataGrid.addColumn(colonnaDettaglio, "");
		dataGrid.addColumn(colonnaAzioni, "");

		String[] stiliColonne = { "tipopratica", "titolo", "titolo", "titolo", "data", "button", "button" };
		for (int i = 0; i < stiliColonne.length; i++) {
			dataGrid.getHeader(i).setHeaderStyleNames(stiliColonne[i]);
		}

		colonnaIcona.setCellStyleNames("tipopratica");
		colonnaTipologia.setCellStyleNames("titolo");
		colonnaTipologia.setSortable(true);
		colonnaTipologia.setDataStoreName(ConfigurazioniUtil.FILTRO_RICERCA_CONFIGURAZIONE_ETICHETTA_PRATICA);
		colonnaProtocollabile.setCellStyleNames("titolo");
		colonnaStato.setCellStyleNames("titolo");
		colonnaData.setCellStyleNames("data");
		colonnaData.setSortable(true);
		colonnaData.setDataStoreName(ConfigurazioniUtil.FILTRO_RICERCA_CONFIGURAZIONE_DATA_CREAZIONE);
		colonnaAzioni.setCellStyleNames("button");
		colonnaDettaglio.setCellStyleNames("button");

		dataGrid.getColumnSortList().setLimit(1);
		dataGrid.getColumnSortList().push(colonnaTipologia);
	}

	private void dettaglioAnagraficaFascicolo(AnagraficaFascicolo object, boolean showAction) {

		Place place = new Place();
		place.setToken(NameTokens.dettaglioanagraficafascicolo);
		place.addParam(NameTokensParams.showActions, Boolean.toString(showAction));
		place.addParam(NameTokensParams.tipologiaFascicolo, object.getNomeTipologia());
		place.addParam(NameTokensParams.parentPlace, NameTokens.listaanagraficafascicoli);
		eventBus.fireEvent(new GoToPlaceEvent(place));
	}

	private void configuraBottoni() {

		creaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Place place = new Place();
				place.setToken(NameTokens.creaanagraficafascicolo);
				place.addParam(NameTokensParams.parentPlace, NameTokens.listaanagraficafascicoli);
				eventBus.fireEvent(new GoToPlaceEvent(place));
			}
		});

		ricaricaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				ShowAppLoadingEvent.fire(ListaAnagraficaFascicoloView.this, true);
				configurazioniHandler.reloadAnagrafichePratiche(new ConfigurazioniCallback() {

					@Override
					public void onSuccess() {
						ShowAppLoadingEvent.fire(ListaAnagraficaFascicoloView.this, false);

					}

					@Override
					public void onFailure(String error) {
						ShowAppLoadingEvent.fire(ListaAnagraficaFascicoloView.this, false);
						showErrors(Arrays.asList(error));

					}
				}, true);
			}
		});
	}

	private void showErrors(List<String> errors) {
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
			messageAlertWidget.showWarningMessage(w.getHTML());
			messageWidgetPanel.setVisible(true);

		} else {
			messageWidgetPanel.setVisible(false);
			messageAlertWidget.reset();
		}
	}

	@UiHandler("pulisciButton")
	public void onPulisciClickEvent(ClickEvent event) {
		clear();
		event.stopPropagation();
	}

	private void clear() {
		tipologiaFascicolo.setValue(null);
		dataCreazioneA.setValue(null);
		dataCreazioneDa.setValue(null);
		stato.setValue(Stato.ATTIVA.name());
		protocollabile.selectYes();
	}

	private class ColonnaTipologia extends TextColumn<AnagraficaFascicolo> {
		@Override
		public String getValue(AnagraficaFascicolo object) {
			return object.getEtichettaTipologia();
		}
	}

	private class ColonnaProtocollazione extends TextColumn<AnagraficaFascicolo> {
		@Override
		public String getValue(AnagraficaFascicolo object) {
			return object.isProtocollabile() ? "SI" : "NO";
		}
	}

	private class ColonnaStato extends TextColumn<AnagraficaFascicolo> {
		@Override
		public String getValue(AnagraficaFascicolo object) {
			return object.getStato().toString();
		}
	}

	private class ColonnaData extends TextColumn<AnagraficaFascicolo> {
		@Override
		public String getValue(AnagraficaFascicolo object) {
			String data = null;
			if (object.getDataCreazione() != null) {
				data = dtf.format(object.getDataCreazione());

			} else {
				data = "-";
			}

			return data;
		}
	}

	private class ColonnaDettaglio extends Column<AnagraficaFascicolo, String> {
		public ColonnaDettaglio() {
			super(new StyledButtonCell());
			setCellStyleNames("last-column");
			setFieldUpdater(new DettaglioUpdater());

		}

		@Override
		public String getValue(AnagraficaFascicolo object) {
			return "Apri";
		}
	}

	private class StyledButtonCell extends ButtonCell {
		@Override
		public void render(Cell.Context context, SafeHtml data, SafeHtmlBuilder sb) {
			sb.appendHtmlConstant("<button class=\"btn worklist_btn\" type=\"button\" tabindex=\"-1\">");
			if (data != null) {
				sb.append(data);
			}
			sb.appendHtmlConstant("</button>");
		}
	}

	private class DettaglioUpdater implements FieldUpdater<AnagraficaFascicolo, String> {
		@Override
		public void update(int index, AnagraficaFascicolo anagraficaFascicolo, String value) {
			dettaglioAnagraficaFascicolo(anagraficaFascicolo, false);
		}
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public String getFiltroTipologiaFascicolo() {
		AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicoloByEtichetta(tipologiaFascicolo.getValue());
		return af != null ? af.getNomeTipologia() : null;
	}

	@Override
	public Stato getFiltroStato() {
		return Stato.from(stato.getValue());
	}

	@Override
	public boolean getFiltroProtocollabile() {
		return protocollabile.getValue();
	}

	@Override
	public Date getFiltroDataA() {
		return dataCreazioneA.getValue();
	}

	@Override
	public Date getFiltroDataDa() {
		return dataCreazioneDa.getValue();
	}

	@Override
	public void mostraAnagraficheFascicoli(List<AnagraficaFascicolo> anagraficheFascicoli, int start, int count) {
		DataGridTableBuilder<AnagraficaFascicolo> builder = new DataGridTableBuilder<AnagraficaFascicolo>(dataGrid);
		builder.start(true);

		for (int i = 0; i < anagraficheFascicoli.size(); i++) {
			builder.buildRowImpl(anagraficheFascicoli.get(i), i);
		}

		provider.updateRowData(start, anagraficheFascicoli);
		pager.setDisplay(dataGrid);

		if (start == 0) {
			pager.firstPage();
		}

		dataGrid.setRowCount(count, true);
	}

	@Override
	public void setDataProvider(ListaAnagraficheFascicoliProvider provider) {
		this.provider = provider;
		this.provider.addDataDisplay(dataGrid);
	}

	@Override
	public void setCercaCommand(final Command command) {
		this.cercaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				command.execute();
			}
		});
	}

	@Override
	public void clearView() {
		clear();
	}

	@Override
	public void setColumnSortEventHandler(Handler handler) {
		this.dataGrid.addColumnSortHandler(handler);
	}

	@Override
	public String getCampoOrdinamento() {
		return this.dataGrid.getColumnSortList().get(0).getColumn().getDataStoreName();
	}

	@Override
	public Boolean getTipoOrdinamento() {
		return this.dataGrid.getColumnSortList().get(0).isAscending();
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEventFromSource(event, this);
	}

}
