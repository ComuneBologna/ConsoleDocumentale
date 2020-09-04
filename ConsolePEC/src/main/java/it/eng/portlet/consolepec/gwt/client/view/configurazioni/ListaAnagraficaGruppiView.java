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

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo.Stato;
import it.eng.cobo.consolepec.util.configurazioni.ConfigurazioniUtil;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler.ConfigurazioniCallback;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.configurazioni.ListaAnagraficaGruppiPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.configurazioni.ListaAnagraficaGruppiPresenter.ListaAnagraficheGruppiProvider;
import it.eng.portlet.consolepec.gwt.client.widget.CustomButtonCell;
import it.eng.portlet.consolepec.gwt.client.widget.CustomPager;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
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
public class ListaAnagraficaGruppiView extends ViewImpl implements ListaAnagraficaGruppiPresenter.MyView, HasHandlers {

	private static final DateTimeFormat dtf = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATAORA);
	private final Widget widget;

	@UiField
	HTMLPanel messageWidgetPanel;
	@UiField(provided = true)
	MessageAlertWidget messageAlertWidget;
	@UiField
	HeadingElement mainTitle;
	@UiField(provided = true)
	DataGridWidget<AnagraficaRuolo> dataGrid;
	@UiField(provided = true)
	SimplePager pager;

	@UiField(provided = true)
	SuggestBox nomeGruppo;
	@UiField
	DateBox dataCreazioneDa;
	@UiField
	DateBox dataCreazioneA;
	@UiField(provided = true)
	SuggestBox gruppoLdap;
	@UiField(provided = true)
	SuggestBox stato = new SuggestBox(new SpacebarSuggestOracle(Stato.labels()));

	@UiField
	Button pulisciButton;
	@UiField
	Button cercaButton;
	@UiField
	Button creaButton;
	@UiField
	Button ricaricaButton;

	private Column<AnagraficaRuolo, SafeUri> colonnaIcona;
	private ColonnaNome colonnaNome = new ColonnaNome();
	private ColonnaLdap colonnaLdap = new ColonnaLdap();
	private ColonnaStato colonnaStato = new ColonnaStato();
	private ColonnaData colonnaDataCreazione = new ColonnaData();
	private Column<AnagraficaRuolo, String> colonnaAzioni;
	private ColonnaDettaglio colonnaDettaglio = new ColonnaDettaglio();

	private ListaAnagraficheGruppiProvider provider;
	private ConfigurazioniHandler configurazioniHandler;
	private EventBus eventBus;

	public interface Binder extends UiBinder<Widget, ListaAnagraficaGruppiView> {
		//
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		mainTitle.scrollIntoView();
		Window.scrollTo(0, 0);
	}

	@Inject
	public ListaAnagraficaGruppiView(final Binder binder, final EventBus eventBus, ConfigurazioniHandler configurazioniHandler) {

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
		setPlaceholder(nomeGruppo, "Nome");
		setPlaceholder(stato, "Stato");
		setPlaceholder(dataCreazioneDa, "Data creazione da");
		setPlaceholder(dataCreazioneA, "Data creazione a");
		setPlaceholder(gruppoLdap, "Gruppo LDAP");
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

		stato.setValue(Stato.ATTIVA.toString());

		List<String> etichetteGruppi = new ArrayList<String>();
		List<String> gruppiLdap = new ArrayList<String>();
		for (AnagraficaRuolo ar : configurazioniHandler.getAnagraficheRuoli()) {
			gruppiLdap.add(ar.getRuolo());
			etichetteGruppi.add(ar.getEtichetta());
		}
		gruppoLdap = new SuggestBox(new SpacebarSuggestOracle(gruppiLdap));
		nomeGruppo = new SuggestBox(new SpacebarSuggestOracle(etichetteGruppi));
	}

	private void inizializzaWorklist() {
		pager = new CustomPager(SimplePager.TextLocation.CENTER, ResPager.CSS, false, 0, true, configurazioniHandler.getProprietaGenerali().getMaxRisultatiWorklist());
		dataGrid = new DataGridWidget<AnagraficaRuolo>(ConsolePecConstants.WORKLIST_NUMERO_PER_PAGINA, null, null);
		configuraColonne();
	}

	private void configuraColonne() {

		colonnaNome = new ColonnaNome();
		colonnaLdap = new ColonnaLdap();
		colonnaDataCreazione = new ColonnaData();
		colonnaDettaglio = new ColonnaDettaglio();

		final CustomSafeImageCell cellImg = new CustomSafeImageCell(true);
		colonnaIcona = new Column<AnagraficaRuolo, SafeUri>(cellImg) {
			@Override
			public SafeUri getValue(AnagraficaRuolo object) {
				return ConsolePECIcons._instance.gruppo().getSafeUri();
			}

			@Override
			public void onBrowserEvent(Context context, Element elem, AnagraficaRuolo object, NativeEvent event) {}
		};

		CustomButtonCell customButtonCell = new CustomButtonCell("btn black worklist_btn");
		colonnaAzioni = new Column<AnagraficaRuolo, String>(customButtonCell) {

			@Override
			public String getValue(AnagraficaRuolo pratica) {
				return "Azioni";
			}

			@Override
			public void onBrowserEvent(Context context, Element elem, AnagraficaRuolo object, NativeEvent event) {
				event.preventDefault();
				if (event.getButton() == NativeEvent.BUTTON_LEFT) {
					dettaglioAnagraficaRuolo(object, true);
				}
			}
		};

		dataGrid.addColumn(colonnaIcona, "");
		dataGrid.addColumn(colonnaNome, "Nome");
		dataGrid.addColumn(colonnaLdap, "Gruppo LDAP");
		dataGrid.addColumn(colonnaStato, "Stato");
		dataGrid.addColumn(colonnaDataCreazione, "Data");
		dataGrid.addColumn(colonnaDettaglio, "");
		dataGrid.addColumn(colonnaAzioni, "");

		String[] stiliColonne = { "tipopratica", "titolo", "titolo", "titolo", "data", "button", "button" };
		for (int i = 0; i < stiliColonne.length; i++) {
			dataGrid.getHeader(i).setHeaderStyleNames(stiliColonne[i]);
		}

		colonnaIcona.setCellStyleNames("tipopratica documento");
		colonnaNome.setCellStyleNames("titolo");
		colonnaNome.setSortable(true);
		colonnaNome.setDataStoreName(ConfigurazioniUtil.FILTRO_RICERCA_RUOLI_ETICHETTA);
		colonnaLdap.setCellStyleNames("titolo");
		colonnaLdap.setSortable(true);
		colonnaLdap.setDataStoreName(ConfigurazioniUtil.FILTRO_RICERCA_RUOLI_NOME);
		colonnaStato.setCellStyleNames("titolo");
		colonnaDataCreazione.setCellStyleNames("data");
		colonnaDataCreazione.setSortable(true);
		colonnaDataCreazione.setDataStoreName(ConfigurazioniUtil.FILTRO_RICERCA_CONFIGURAZIONE_DATA_CREAZIONE);
		colonnaAzioni.setCellStyleNames("button");
		colonnaDettaglio.setCellStyleNames("button");

		dataGrid.getColumnSortList().setLimit(1);
		dataGrid.getColumnSortList().push(colonnaNome);
	}

	private void configuraBottoni() {

		creaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Place place = new Place();
				place.setToken(NameTokens.creaanagraficagruppo);
				place.addParam(NameTokensParams.parentPlace, NameTokens.listaanagraficagruppi);
				eventBus.fireEvent(new GoToPlaceEvent(place));
			}
		});

		ricaricaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				ShowAppLoadingEvent.fire(ListaAnagraficaGruppiView.this, true);
				configurazioniHandler.reloadRuoli(new ConfigurazioniCallback() {

					@Override
					public void onSuccess() {
						ShowAppLoadingEvent.fire(ListaAnagraficaGruppiView.this, false);

					}

					@Override
					public void onFailure(String error) {
						ShowAppLoadingEvent.fire(ListaAnagraficaGruppiView.this, false);
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
		dataCreazioneA.setValue(null);
		dataCreazioneDa.setValue(null);
		gruppoLdap.setValue(null);
		nomeGruppo.setValue(null);
		stato.setValue(Stato.ATTIVA.toString());
	}

	private class ColonnaNome extends TextColumn<AnagraficaRuolo> {
		@Override
		public String getValue(AnagraficaRuolo object) {
			return object.getEtichetta();
		}
	}

	private class ColonnaLdap extends TextColumn<AnagraficaRuolo> {
		@Override
		public String getValue(AnagraficaRuolo object) {
			return object.getRuolo();
		}
	}

	private class ColonnaStato extends TextColumn<AnagraficaRuolo> {
		@Override
		public String getValue(AnagraficaRuolo object) {
			return object.getStato().name();
		}
	}

	private class ColonnaData extends TextColumn<AnagraficaRuolo> {
		@Override
		public String getValue(AnagraficaRuolo object) {

			String data = null;
			if (object.getDataCreazione() != null) {
				data = dtf.format(object.getDataCreazione());

			} else {
				data = "-";
			}

			if (data == null) {
				data = "-";
			}

			return data;
		}
	}

	private class ColonnaDettaglio extends Column<AnagraficaRuolo, String> {
		public ColonnaDettaglio() {
			super(new StyledButtonCell());
			setCellStyleNames("last-column");
			setFieldUpdater(new DettaglioUpdater());

		}

		@Override
		public String getValue(AnagraficaRuolo object) {
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

	private class DettaglioUpdater implements FieldUpdater<AnagraficaRuolo, String> {
		@Override
		public void update(int index, AnagraficaRuolo anagraficaRuolo, String value) {

			dettaglioAnagraficaRuolo(anagraficaRuolo, false);
		}
	}

	private void dettaglioAnagraficaRuolo(AnagraficaRuolo object, boolean showAction) {

		Place place = new Place();
		place.setToken(NameTokens.dettaglioanagraficagruppo);
		place.addParam(NameTokensParams.showActions, Boolean.toString(showAction));
		place.addParam(NameTokensParams.ruolo, object.getRuolo());
		place.addParam(NameTokensParams.parentPlace, NameTokens.listaanagraficagruppi);
		eventBus.fireEvent(new GoToPlaceEvent(place));
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void mostraAnagraficheGruppi(List<AnagraficaRuolo> anagraficheRuoli, int start, int count) {

		DataGridTableBuilder<AnagraficaRuolo> builder = new DataGridTableBuilder<AnagraficaRuolo>(dataGrid);
		builder.start(true);
		for (int i = 0; i < anagraficheRuoli.size(); i++) {
			builder.buildRowImpl(anagraficheRuoli.get(i), i);
		}

		provider.updateRowData(start, anagraficheRuoli);
		pager.setDisplay(dataGrid);
		if (start == 0) {
			pager.setPageStart(start);
		}

		dataGrid.setRowCount(count, true);
	}

	@Override
	public String getFiltroNome() {
		return nomeGruppo.getValue();
	}

	@Override
	public String getFiltroLdap() {
		return gruppoLdap.getValue();
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
	public Stato getFiltroStato() {
		if (stato.getValue() != null) {
			return Stato.from(stato.getValue());
		}

		return null;
	}

	@Override
	public void setDataProvider(ListaAnagraficheGruppiProvider provider) {
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
