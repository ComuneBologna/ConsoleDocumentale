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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.VisibilitaAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.VisibilitaPraticaAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.VisibilitaRuoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.util.AbilitazioneRuoloSingola;
import it.eng.cobo.consolepec.util.configurazioni.ConfigurazioniUtil;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler.ConfigurazioniCallback;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAbilitazioniVisibilitaApiClient.TipoAbilitazione;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAbilitazioniVisibilitaApiClient.TipoAccessoAbilitazione;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.configurazioni.ListaAbilitazioniPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.configurazioni.ListaAbilitazioniPresenter.ListaAbilitazioniProvider;
import it.eng.portlet.consolepec.gwt.client.widget.CustomPager;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridTableBuilder;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.client.widget.images.ResPager;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;
import it.eng.portlet.consolepec.gwt.client.worklist.CustomSafeImageCell;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;

public class ListaAbilitazioniView extends ViewImpl implements ListaAbilitazioniPresenter.MyView, HasHandlers {

	private static final DateTimeFormat dtf = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATAORA);
	private final Widget widget;

	@UiField
	HTMLPanel messageWidgetPanel;
	@UiField(provided = true)
	MessageAlertWidget messageAlertWidget;
	@UiField
	HeadingElement mainTitle;
	@UiField(provided = true)
	DataGridWidget<AbilitazioneRuoloSingola> dataGrid;
	@UiField(provided = true)
	SimplePager pager;

	@UiField(provided = true)
	SuggestBox gruppoAbilitato;
	@UiField
	TextBox abilitazione;
	@UiField(provided = true)
	SuggestBox praticaAssociata;
	@UiField(provided = true)
	SuggestBox gruppoAssociato;
	@UiField(provided = true)
	SuggestBox tipoAbilitazione;
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

	private Column<AbilitazioneRuoloSingola, SafeUri> colonnaIcona;
	private ColonnaGruppoAbilitato colonnaGruppoAbilitato;
	private ColonnaTipo colonnaTipo;
	private ColonnaPraticaAssociata colonnaFascicoloAssociato;
	private ColonnaGruppoAssociato colonnaGruppoAssociato;
	private ColonnaAbilitazione colonnaAbilitazione;
	private ColonnaData colonnaData;
	private ColonnaDettaglio colonnaDettaglio;
	private List<TipologiaPratica> tipologiePratiche = new ArrayList<TipologiaPratica>();

	private ListaAbilitazioniProvider provider;
	private ConfigurazioniHandler configurazioniHandler;
	private EventBus eventBus;

	public interface Binder extends UiBinder<Widget, ListaAbilitazioniView> {
		//
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		mainTitle.scrollIntoView();
		Window.scrollTo(0, 0);
	}

	@Inject
	public ListaAbilitazioniView(final Binder binder, final EventBus eventBus, final ConfigurazioniHandler configurazioniHandler) {
		this.eventBus = eventBus;
		this.configurazioniHandler = configurazioniHandler;
		this.messageAlertWidget = new MessageAlertWidget(eventBus);

		this.tipologiePratiche = PraticaUtil.toTipologiePratiche(configurazioniHandler.getAnagraficheFascicoli(false), null, null, configurazioniHandler.getAnagraficheComunicazioni(false),
				configurazioniHandler.getAnagrafichePraticaModulistica(false), configurazioniHandler.getAnagraficheModelli(false));

		inizializzaFiltriRicerca();
		inizializzaWorklist();

		widget = binder.createAndBindUi(this);

		configuraFiltriDiRicerca();
		configuraBottoni();
	}

	private void inizializzaFiltriRicerca() {

		List<String> gruppi = new ArrayList<String>();
		for (AnagraficaRuolo ar : configurazioniHandler.getAnagraficheRuoli()) {
			gruppi.add(ar.getEtichetta());
		}
		gruppoAbilitato = new SuggestBox(new SpacebarSuggestOracle(gruppi));
		gruppoAssociato = new SuggestBox(new SpacebarSuggestOracle(gruppi));

		List<String> t = new ArrayList<String>();
		for (TipologiaPratica af : tipologiePratiche) {
			t.add(af.getEtichettaTipologia());
		}
		praticaAssociata = new SuggestBox(new SpacebarSuggestOracle(t));
		tipoAbilitazione = new SuggestBox(new SpacebarSuggestOracle(TipoAbilitazione.labels()));
	}

	private void setPlaceholder(Widget widget, String placeholder) {
		InputElement inputElement = widget.getElement().cast();
		inputElement.setAttribute("placeholder", placeholder);
	}

	private void configuraPlaceHolder() {
		setPlaceholder(gruppoAbilitato, "Gruppo abilitato");
		setPlaceholder(gruppoAssociato, "Gruppo associato");
		setPlaceholder(praticaAssociata, "Pratica associata");
		setPlaceholder(tipoAbilitazione, "Tipo");
		setPlaceholder(abilitazione, "Abilitazione");
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
		abilitazione.setText(TipoAccessoAbilitazione.Lettura.toString());
	}

	private void inizializzaWorklist() {
		pager = new CustomPager(SimplePager.TextLocation.CENTER, ResPager.CSS, false, 0, true, configurazioniHandler.getProprietaGenerali().getMaxRisultatiWorklist());
		dataGrid = new DataGridWidget<AbilitazioneRuoloSingola>(ConsolePecConstants.WORKLIST_NUMERO_PER_PAGINA, null, null);
		configuraColonne();
	}

	private void configuraColonne() {

		final CustomSafeImageCell cellImg = new CustomSafeImageCell(true);
		colonnaIcona = new Column<AbilitazioneRuoloSingola, SafeUri>(cellImg) {
			@Override
			public SafeUri getValue(AbilitazioneRuoloSingola object) {
				return ConsolePECIcons._instance.gruppo().getSafeUri();
			}

			@Override
			public void onBrowserEvent(Context context, Element elem, AbilitazioneRuoloSingola object, NativeEvent event) {}
		};

		colonnaGruppoAbilitato = new ColonnaGruppoAbilitato();
		colonnaTipo = new ColonnaTipo();
		colonnaFascicoloAssociato = new ColonnaPraticaAssociata();
		colonnaGruppoAssociato = new ColonnaGruppoAssociato();
		colonnaAbilitazione = new ColonnaAbilitazione();
		colonnaData = new ColonnaData();
		colonnaDettaglio = new ColonnaDettaglio();

		dataGrid.addColumn(colonnaIcona, "");
		dataGrid.addColumn(colonnaGruppoAbilitato, "Gruppo Abilitato");
		dataGrid.addColumn(colonnaTipo, "Tipo");
		dataGrid.addColumn(colonnaFascicoloAssociato, "Pratica Associata");
		dataGrid.addColumn(colonnaGruppoAssociato, "Gruppo Associato");
		dataGrid.addColumn(colonnaAbilitazione, "Abilitazione");
		dataGrid.addColumn(colonnaData, "Data");
		dataGrid.addColumn(colonnaDettaglio, "");

		String[] stiliColonne = { "tipopratica", "titolo", "titolo", "titolo", "titolo", "titolo", "data", "button" };
		for (int i = 0; i < stiliColonne.length; i++) {
			dataGrid.getHeader(i).setHeaderStyleNames(stiliColonne[i]);
		}

		colonnaIcona.setCellStyleNames("tipopratica documento");
		colonnaGruppoAbilitato.setCellStyleNames("titolo");
		colonnaGruppoAbilitato.setSortable(true);
		colonnaGruppoAbilitato.setDataStoreName(ConfigurazioniUtil.FILTRO_RICERCA_ABILITAZIONI_RUOLO_RUOLO);
		colonnaTipo.setCellStyleNames("titolo");
		colonnaFascicoloAssociato.setCellStyleNames("titolo");
		colonnaGruppoAssociato.setCellStyleNames("titolo");
		colonnaFascicoloAssociato.setCellStyleNames("titolo");
		colonnaFascicoloAssociato.setCellStyleNames("titolo");
		colonnaData.setCellStyleNames("data");
		colonnaData.setSortable(true);
		colonnaData.setDataStoreName(ConfigurazioniUtil.FILTRO_RICERCA_CONFIGURAZIONE_DATA_CREAZIONE);
		colonnaDettaglio.setCellStyleNames("button");

		dataGrid.getColumnSortList().setLimit(1);
		dataGrid.getColumnSortList().push(colonnaGruppoAbilitato);
	}

	private void configuraBottoni() {

		creaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Place place = new Place();
				place.setToken(NameTokens.creaabilitazione);
				place.addParam(NameTokensParams.parentPlace, NameTokens.listaabilitazioni);
				eventBus.fireEvent(new GoToPlaceEvent(place));
			}
		});

		ricaricaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				ShowAppLoadingEvent.fire(ListaAbilitazioniView.this, true);
				configurazioniHandler.reloadRuoli(new ConfigurazioniCallback() {

					@Override
					public void onSuccess() {
						ShowAppLoadingEvent.fire(ListaAbilitazioniView.this, false);
					}

					@Override
					public void onFailure(String error) {
						ShowAppLoadingEvent.fire(ListaAbilitazioniView.this, false);
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
		gruppoAbilitato.setValue(null);
		abilitazione.setText(TipoAccessoAbilitazione.Lettura.toString());
		praticaAssociata.setValue(null);
		gruppoAssociato.setValue(null);
		tipoAbilitazione.setValue(null);
		dataCreazioneDa.setValue(null);
		dataCreazioneA.setValue(null);

	}

	private class ColonnaGruppoAbilitato extends TextColumn<AbilitazioneRuoloSingola> {
		@Override
		public String getValue(AbilitazioneRuoloSingola object) {
			AnagraficaRuolo ar = configurazioniHandler.getAnagraficaRuolo(object.getRuolo());
			return ar != null ? ar.getEtichetta() : "";
		}
	}

	private class ColonnaGruppoAssociato extends TextColumn<AbilitazioneRuoloSingola> {

		@Override
		public String getValue(AbilitazioneRuoloSingola object) {

			if (object.getAbilitazione() instanceof VisibilitaRuoloAbilitazione) {
				VisibilitaRuoloAbilitazione ra = (VisibilitaRuoloAbilitazione) object.getAbilitazione();
				AnagraficaRuolo ar = configurazioniHandler.getAnagraficaRuolo(ra.getRuolo());
				if (ar != null) {
					return ar.getEtichetta();
				}
			}

			return "";
		}
	}

	private class ColonnaPraticaAssociata extends TextColumn<AbilitazioneRuoloSingola> {

		@Override
		public String getValue(AbilitazioneRuoloSingola object) {

			if (object.getAbilitazione() instanceof VisibilitaPraticaAbilitazione) {
				VisibilitaPraticaAbilitazione fa = (VisibilitaPraticaAbilitazione) object.getAbilitazione();

				for (TipologiaPratica tp : tipologiePratiche) {
					if (tp.getNomeTipologia().equals(fa.getTipo())) {
						return tp.getEtichettaTipologia();
					}
				}
			}

			return "";
		}
	}

	private class ColonnaTipo extends TextColumn<AbilitazioneRuoloSingola> {
		@Override
		public String getValue(AbilitazioneRuoloSingola object) {

			if (object.getAbilitazione() instanceof VisibilitaPraticaAbilitazione) {
				VisibilitaPraticaAbilitazione fa = (VisibilitaPraticaAbilitazione) object.getAbilitazione();

				if (PraticaUtil.isFascicolo(fa.getTipo())) {
					return TipoAbilitazione.Fascicolo.toString();

				} else if (PraticaUtil.isComunicazione(fa.getTipo())) {
					return TipoAbilitazione.Comunicazione.toString();

				} else if (PraticaUtil.isModello(fa.getTipo())) {
					return TipoAbilitazione.Modello.toString();

				} else if (PraticaUtil.isPraticaModulistica(fa.getTipo())) {
					return TipoAbilitazione.PModulistica.toString();
				}

			} else if (object.getAbilitazione() instanceof VisibilitaRuoloAbilitazione) {
				return TipoAbilitazione.Gruppo.toString();

			}

			return "";
		}
	}

	private class ColonnaAbilitazione extends TextColumn<AbilitazioneRuoloSingola> {

		@Override
		public String getValue(AbilitazioneRuoloSingola object) {

			if (object.getAbilitazione() instanceof VisibilitaAbilitazione) {
				return TipoAccessoAbilitazione.Lettura.toString();
			}

			return "";
		}
	}

	private class ColonnaData extends TextColumn<AbilitazioneRuoloSingola> {
		@Override
		public String getValue(AbilitazioneRuoloSingola object) {
			String data = null;
			if (object.getAbilitazione().getDataCreazione() != null) {
				data = dtf.format(object.getAbilitazione().getDataCreazione());

			} else {
				data = "-";
			}

			return data;
		}
	}

	private class ColonnaDettaglio extends Column<AbilitazioneRuoloSingola, String> {

		public ColonnaDettaglio() {
			super(new StyledButtonCell());
			setCellStyleNames("last-column");
			setFieldUpdater(new DettaglioUpdater());
		}

		@Override
		public String getValue(AbilitazioneRuoloSingola object) {
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

	private class DettaglioUpdater implements FieldUpdater<AbilitazioneRuoloSingola, String> {
		@Override
		public void update(int index, AbilitazioneRuoloSingola a, String value) {
			Place place = new Place();
			place.setToken(NameTokens.dettaglioabilitazione);
			place.addParam(NameTokensParams.ruolo, a.getRuolo());
			place.addParam(NameTokensParams.parentPlace, NameTokens.listaabilitazioni);
			eventBus.fireEvent(new GoToPlaceEvent(place));
		}
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public String getFiltroGruppoAbilitato() {
		return gruppoAbilitato.getValue();
	}

	@Override
	public String getFiltroTipologiaPraticaAssociata() {
		for (TipologiaPratica tp : tipologiePratiche) {
			if (tp.getEtichettaTipologia().equals(praticaAssociata.getValue())) {
				return tp.getNomeTipologia();
			}
		}

		return null;
	}

	@Override
	public String getFiltroTipoAbilitazione() {
		return tipoAbilitazione.getValue();
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
	public String getFiltroGruppoAssociato() {
		return gruppoAssociato.getValue();
	}

	@Override
	public void mostraAbilitazioni(List<AbilitazioneRuoloSingola> abilitazioni, int start, int count) {
		DataGridTableBuilder<AbilitazioneRuoloSingola> builder = new DataGridTableBuilder<AbilitazioneRuoloSingola>(dataGrid);
		builder.start(true);
		for (int i = 0; i < abilitazioni.size(); i++) {
			builder.buildRowImpl(abilitazioni.get(i), i);
		}

		provider.updateRowData(start, abilitazioni);
		pager.setDisplay(dataGrid);
		if (start == 0) {
			pager.setPageStart(start);
		}

		dataGrid.setRowCount(count, true);
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
	public void setDataProvider(ListaAbilitazioniProvider provider) {
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
	public String getFiltroAbilitazione() {
		return abilitazione.getValue();
	}

	@Override
	public void clearView() {
		clear();
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEventFromSource(event, this);
	}
}
