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

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.configurazioni.properties.ProprietaGenerali.Server;
import it.eng.cobo.consolepec.commons.configurazioni.properties.ProprietaGenerali.Server.TipoMail;
import it.eng.cobo.consolepec.util.configurazioni.ConfigurazioniUtil;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler.ConfigurazioniCallback;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.configurazioni.ListaAnagraficaIngressiPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.configurazioni.ListaAnagraficaIngressiPresenter.ListaAnagraficheIngressiProvider;
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
public class ListaAnagraficaIngressiView extends ViewImpl implements ListaAnagraficaIngressiPresenter.MyView, HasHandlers {

	private static final DateTimeFormat dtf = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATAORA);
	private final Widget widget;

	@UiField
	HTMLPanel messageWidgetPanel;
	@UiField(provided = true)
	MessageAlertWidget messageAlertWidget;
	@UiField
	HeadingElement mainTitle;
	@UiField(provided = true)
	DataGridWidget<AnagraficaIngresso> dataGrid;
	@UiField(provided = true)
	SimplePager pager;

	@UiField(provided = true)
	SuggestBox tipo;
	@UiField(provided = true)
	SuggestBox nome;
	@UiField
	DateBox dataCreazioneDa;
	@UiField
	DateBox dataCreazioneA;

	@UiField
	Button pulisciButton;
	@UiField
	Button cercaButton;
	@UiField
	Button creaButton;
	@UiField
	Button ricaricaButton;

	private Column<AnagraficaIngresso, SafeUri> colonnaIcona;
	private ColonnaNome colonnaNome;
	private ColonnaTipo colonnaTipo;
	private ColonnaData colonnaDataCreazione;
	private Column<AnagraficaIngresso, String> colonnaRuoli;
	private ColonnaDettaglio colonnaDettaglio;
	private ColonnaPrimoAssegnatario colonnaPrimoAssegnatario;

	private ListaAnagraficheIngressiProvider provider;
	private ConfigurazioniHandler configurazioniHandler;
	private EventBus eventBus;

	public interface Binder extends UiBinder<Widget, ListaAnagraficaIngressiView> {
		//
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		mainTitle.scrollIntoView();
		Window.scrollTo(0, 0);
	}

	@Inject
	public ListaAnagraficaIngressiView(final Binder binder, final EventBus eventBus, final ConfigurazioniHandler configurazioniHandler) {
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
		setPlaceholder(nome, "Nome");
		setPlaceholder(tipo, "Tipo");
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
		List<String> indirizzi = new ArrayList<String>();
		for (AnagraficaIngresso ai : configurazioniHandler.getAnagraficheIngressi(false)) {
			if (TipologiaPratica.EMAIL_IN.getNomeTipologia().equals(ai.getNomeTipologia()))
				indirizzi.add(ai.getIndirizzo());
		}
		nome = new SuggestBox(new SpacebarSuggestOracle(indirizzi));

		List<String> tipi = new ArrayList<String>();
		for (TipoMail ti : TipoMail.values()) {
			tipi.add(ti.toString());
		}
		tipo = new SuggestBox(new SpacebarSuggestOracle(tipi));
	}

	private void inizializzaWorklist() {
		pager = new CustomPager(SimplePager.TextLocation.CENTER, ResPager.CSS, false, 0, true, configurazioniHandler.getProprietaGenerali().getMaxRisultatiWorklist());
		dataGrid = new DataGridWidget<AnagraficaIngresso>(ConsolePecConstants.WORKLIST_NUMERO_PER_PAGINA, null, null);
		configuraColonne();
	}

	private void configuraColonne() {

		colonnaTipo = new ColonnaTipo();
		colonnaNome = new ColonnaNome();
		colonnaPrimoAssegnatario = new ColonnaPrimoAssegnatario();
		colonnaDataCreazione = new ColonnaData();
		colonnaDettaglio = new ColonnaDettaglio();

		final CustomSafeImageCell cellImg = new CustomSafeImageCell(true);
		colonnaIcona = new Column<AnagraficaIngresso, SafeUri>(cellImg) {
			@Override
			public SafeUri getValue(AnagraficaIngresso object) {
				return ConsolePECIcons._instance.bustinaChiusaEmail().getSafeUri();
			}

			@Override
			public void onBrowserEvent(Context context, Element elem, AnagraficaIngresso object, NativeEvent event) {}
		};

		CustomButtonCell customButtonCell = new CustomButtonCell("btn black worklist_btn");
		colonnaRuoli = new Column<AnagraficaIngresso, String>(customButtonCell) {

			@Override
			public String getValue(AnagraficaIngresso pratica) {
				return "Ruoli";
			}

			@Override
			public void onBrowserEvent(Context context, Element elem, AnagraficaIngresso object, NativeEvent event) {
				event.preventDefault();
				if (event.getButton() == NativeEvent.BUTTON_LEFT) {
					Place place = new Place();
					place.setToken(NameTokens.dettaglioanagraficaingresso);
					place.addParam(NameTokensParams.indirizzoIngresso, object.getIndirizzo());
					place.addParam(NameTokensParams.showActions, Boolean.toString(true));
					place.addParam(NameTokensParams.parentPlace, NameTokens.listaanagraficaingresso);
					eventBus.fireEvent(new GoToPlaceEvent(place));
				}
			}
		};

		dataGrid.addColumn(colonnaIcona, "");
		dataGrid.addColumn(colonnaTipo, "Tipo");
		dataGrid.addColumn(colonnaNome, "Nome");
		dataGrid.addColumn(colonnaPrimoAssegnatario, "Primo assegnatario");
		dataGrid.addColumn(colonnaDataCreazione, "Data");
		dataGrid.addColumn(colonnaDettaglio, "");
		dataGrid.addColumn(colonnaRuoli, "");

		String[] stiliColonne = { "tipopratica", "titolo", "titolo", "titolo", "data", "button", "button" };
		for (int i = 0; i < stiliColonne.length; i++) {
			dataGrid.getHeader(i).setHeaderStyleNames(stiliColonne[i]);
		}

		colonnaIcona.setCellStyleNames("tipopratica");
		colonnaNome.setCellStyleNames("titolo");
		colonnaNome.setSortable(true);
		colonnaNome.setDataStoreName(ConfigurazioniUtil.FILTRO_RICERCA_INGRESSI_INDIRIZZO);
		colonnaTipo.setCellStyleNames("titolo");
		colonnaTipo.setSortable(true);
		colonnaTipo.setDataStoreName(ConfigurazioniUtil.FILTRO_RICERCA_INGRESSI_SERVER);
		colonnaPrimoAssegnatario.setCellStyleNames("titolo");
		colonnaDataCreazione.setCellStyleNames("data");
		colonnaDataCreazione.setSortable(true);
		colonnaDataCreazione.setDataStoreName(ConfigurazioniUtil.FILTRO_RICERCA_CONFIGURAZIONE_DATA_CREAZIONE);
		colonnaRuoli.setCellStyleNames("button");
		colonnaDettaglio.setCellStyleNames("button");

		dataGrid.getColumnSortList().setLimit(1);
		dataGrid.getColumnSortList().push(colonnaNome);
	}

	private void configuraBottoni() {

		creaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Place place = new Place();
				place.setToken(NameTokens.creaanagraficaingresso);
				place.addParam(NameTokensParams.parentPlace, NameTokens.listaanagraficaingresso);
				eventBus.fireEvent(new GoToPlaceEvent(place));
			}
		});

		ricaricaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				ShowAppLoadingEvent.fire(ListaAnagraficaIngressiView.this, true);
				configurazioniHandler.reloadAnagrafichePratiche(new ConfigurazioniCallback() {

					@Override
					public void onSuccess() {
						ShowAppLoadingEvent.fire(ListaAnagraficaIngressiView.this, false);

					}

					@Override
					public void onFailure(String error) {
						ShowAppLoadingEvent.fire(ListaAnagraficaIngressiView.this, false);
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
		nome.setValue(null);
		dataCreazioneA.setValue(null);
		dataCreazioneDa.setValue(null);
		tipo.setValue(null);
	}

	private class ColonnaTipo extends TextColumn<AnagraficaIngresso> {
		@Override
		public String getValue(AnagraficaIngresso object) {

			for (Server server : configurazioniHandler.getProprietaGenerali().getServerIngressiAbilitati()) {
				if (server.getNome().equals(object.getServer()))
					return server.getTipoMail().name();
			}

			return "-";
		}
	}

	private class ColonnaNome extends TextColumn<AnagraficaIngresso> {
		@Override
		public String getValue(AnagraficaIngresso object) {
			return object.getIndirizzo();
		}
	}

	private class ColonnaData extends TextColumn<AnagraficaIngresso> {
		@Override
		public String getValue(AnagraficaIngresso object) {

			String data = null;
			if (object.getDataCreazione() != null) {
				data = dtf.format(object.getDataCreazione());

			} else {
				data = "-";
			}

			return data;
		}
	}

	private class ColonnaDettaglio extends Column<AnagraficaIngresso, String> {
		public ColonnaDettaglio() {
			super(new StyledButtonCell());
			setCellStyleNames("last-column");
			setFieldUpdater(new DettaglioUpdater());

		}

		@Override
		public String getValue(AnagraficaIngresso object) {
			return "Apri";
		}
	}

	private class ColonnaPrimoAssegnatario extends TextColumn<AnagraficaIngresso> {

		@Override
		public String getValue(AnagraficaIngresso a) {
			AnagraficaRuolo ar = configurazioniHandler.getPrimoAssegnatario(a.getIndirizzo(), a.getNomeTipologia());
			return ar != null ? ar.getEtichetta() : "";
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

	private class DettaglioUpdater implements FieldUpdater<AnagraficaIngresso, String> {
		@Override
		public void update(int index, AnagraficaIngresso anagraficaIngresso, String value) {
			dettaglioAnagraficaIngresso(anagraficaIngresso, false);
		}
	}

	private void dettaglioAnagraficaIngresso(AnagraficaIngresso object, boolean showAction) {

		Place place = new Place();
		place.setToken(NameTokens.dettaglioanagraficaingresso);
		place.addParam(NameTokensParams.showActions, Boolean.toString(showAction));
		place.addParam(NameTokensParams.indirizzoIngresso, object.getIndirizzo());
		place.addParam(NameTokensParams.parentPlace, NameTokens.listaanagraficaingresso);
		eventBus.fireEvent(new GoToPlaceEvent(place));
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public List<String> getFiltroServer() {
		List<String> list = new ArrayList<String>();

		for (Server server : configurazioniHandler.getProprietaGenerali().getServerIngressiAbilitati()) {
			if (server.getTipoMail().name().equals(tipo.getValue()))
				list.add(server.getNome());
		}

		return list;
	}

	@Override
	public String getFiltroIndirizzo() {
		return nome.getValue();
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
	public void mostraAnagraficheIngressi(List<AnagraficaIngresso> anagraficheIngressi, int start, int count) {
		DataGridTableBuilder<AnagraficaIngresso> builder = new DataGridTableBuilder<AnagraficaIngresso>(dataGrid);
		builder.start(true);
		for (int i = 0; i < anagraficheIngressi.size(); i++) {
			builder.buildRowImpl(anagraficheIngressi.get(i), i);
		}

		provider.updateRowData(start, anagraficheIngressi);
		pager.setDisplay(dataGrid);
		if (start == 0) {
			pager.setPageStart(start);
		}

		dataGrid.setRowCount(count, true);

	}

	@Override
	public void setDataProvider(ListaAnagraficheIngressiProvider provider) {
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
