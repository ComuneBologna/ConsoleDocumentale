package it.eng.portlet.consolepec.gwt.client.view.urbanistica;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.urbanistica.FiltriRicercaUrbanistica;
import it.eng.cobo.consolepec.commons.urbanistica.PraticaProcedi;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.urbanistica.ListaPraticaProcediPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.urbanistica.ListaPraticaProcediPresenter.PraticaProcediDataProvider;
import it.eng.portlet.consolepec.gwt.client.widget.CustomPager;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.RadioButtonCell;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.ClickableCell;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridTableBuilder;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget;
import it.eng.portlet.consolepec.gwt.client.widget.images.ResPager;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;

/**
 * @author GiacomoFM
 * @since 07/nov/2017
 */
public class ListaPraticaProcediView extends ViewImpl implements ListaPraticaProcediPresenter.MyView {

	private final Widget widget;

	@UiField(provided = true)
	MessageAlertWidget messageAlertWidget;

	@UiField(provided = true)
	DataGridWidget<PraticaProcedi> dataGrid;
	private PraticaProcediDataProvider dataProvider;

	@UiField(provided = true)
	SimplePager pager;

	@UiField
	TextBox numeroProtocollo;
	@UiField
	TextBox annoProtocollo;
	@UiField
	TextBox oggetto;
	@UiField
	TextBox tipoPratica;
	@UiField
	TextBox ambito;
	@UiField
	TextBox indirizzoVia;
	@UiField
	TextBox indirizzoCivico;
	@UiField
	TextBox nominativo;

	@UiField
	Button pulisciButton;

	@UiField
	Button cercaButton;

	@UiField
	Button annullaButton;

	@UiField
	Button confermaButton;

	public interface Binder extends UiBinder<Widget, ListaPraticaProcediView> {
		//
	}

	@Inject
	public ListaPraticaProcediView(final Binder binder, final EventBus eventBus, ConfigurazioniHandler configurazioniHandler) {
		pager = new CustomPager(SimplePager.TextLocation.CENTER, ResPager.CSS, false, 0, true, configurazioniHandler.getProprietaGenerali().getMaxRisultatiWorklist());

		dataGrid = new DataGridWidget<PraticaProcedi>(ConsolePecConstants.WORKLIST_NUMERO_PER_PAGINA, null, null);

		RadioSelectionColumn radioSelectionColumn = new RadioSelectionColumn();
		radioSelectionColumn.setCellStyleNames("check");

		NumAnnoPGColumn numeroAnnoPGColumn = new NumAnnoPGColumn();
		numeroAnnoPGColumn.setCellStyleNames("nome");

		OggettoColumn oggettoColumn = new OggettoColumn();
		oggettoColumn.setCellStyleNames("descrizione");

		TipoColumn tipoColumn = new TipoColumn();
		tipoColumn.setCellStyleNames("stato");

		AmbitoColumn ambitoColumn = new AmbitoColumn();
		ambitoColumn.setCellStyleNames("stato");

		dataGrid.addColumn(radioSelectionColumn, "");
		dataGrid.addColumn(numeroAnnoPGColumn, "Numero/Anno PG");
		dataGrid.addColumn(oggettoColumn, "Oggetto");
		dataGrid.addColumn(tipoColumn, "Tipo");
		dataGrid.addColumn(ambitoColumn, "Ambito");

		dataGrid.getHeader(0).setHeaderStyleNames("check");
		dataGrid.getHeader(1).setHeaderStyleNames("nome");
		dataGrid.getHeader(2).setHeaderStyleNames("descrizione");
		dataGrid.getHeader(3).setHeaderStyleNames("stato");
		dataGrid.getHeader(4).setHeaderStyleNames("stato");

		messageAlertWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);

		numeroProtocollo.getElement().setPropertyString("placeholder", "Numero PG");
		annoProtocollo.getElement().setPropertyString("placeholder", "Anno PG");
		oggetto.getElement().setPropertyString("placeholder", "Oggetto");
		tipoPratica.getElement().setPropertyString("placeholder", "Tipo");
		ambito.getElement().setPropertyString("placeholder", "Ambito");
		indirizzoVia.getElement().setPropertyString("placeholder", "Indirizzo");
		indirizzoCivico.getElement().setPropertyString("placeholder", "Civico");
		nominativo.getElement().setPropertyString("placeholder", "Nominativo");
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@UiHandler("pulisciButton")
	public void onPulisciClickEvent(ClickEvent event) {
		resetFiltriRicerca();
		event.stopPropagation();
	}

	@Override
	public void resetFiltriRicerca() {
		numeroProtocollo.setValue("");
		numeroProtocollo.setValue("");
		annoProtocollo.setValue("");
		oggetto.setValue("");
		tipoPratica.setValue("");
		ambito.setValue("");
		indirizzoVia.setValue("");
		indirizzoCivico.setValue("");
		nominativo.setValue("");
	}

	@Override
	public Map<String, Boolean> getOrdinamento() {
		return new HashMap<>();
	}

	@Override
	public Map<String, Object> getFiltriRicerca() {
		Map<String, Object> map = new HashMap<>();
		map.put(FiltriRicercaUrbanistica.NUMERO_PROTOCOLLO, numeroProtocollo.getText());
		map.put(FiltriRicercaUrbanistica.ANNO_PROTOCOLLO, annoProtocollo.getText());
		map.put(FiltriRicercaUrbanistica.OGGETTO, oggetto.getText());
		map.put(FiltriRicercaUrbanistica.AMBITO, ambito.getText());
		map.put(FiltriRicercaUrbanistica.INDIRIZZO_VIA, indirizzoVia.getText());
		map.put(FiltriRicercaUrbanistica.INDIRIZZO_CIVICO, indirizzoCivico.getText());
		map.put(FiltriRicercaUrbanistica.TIPO_PRATICA, tipoPratica.getText());
		map.put(FiltriRicercaUrbanistica.COGNOME_NOME, nominativo.getText());
		return map;
	}

	@Override
	public void setRicercaCommand(final Command command) {
		cercaButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
	}

	@Override
	public void setAnnullaCommand(final Command command) {
		annullaButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
	}

	@Override
	public void setConfermaCommand(final Command command) {
		confermaButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
	}

	private PraticaProcedi selectedElement;

	@Override
	public PraticaProcedi getSelectedPraticaProcedi() {
		return selectedElement;
	}

	@Override
	public void mostraListaPraticaProcedi(final List<PraticaProcedi> listaPraticaProcedi, Integer start, Integer maxSize) {

		confermaButton.setEnabled(false);

		// =========================================================================================
		final SingleSelectionModel<PraticaProcedi> selectionModel = new SingleSelectionModel<>();
		dataGrid.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				selectedElement = selectionModel.getSelectedObject();
				confermaButton.setEnabled(true);
			}
		});
		// =========================================================================================

		DataGridTableBuilder<PraticaProcedi> builder = new DataGridTableBuilder<PraticaProcedi>(dataGrid);
		builder.start(true);
		for (int i = 0; i < listaPraticaProcedi.size(); i++) {
			builder.buildRowImpl(listaPraticaProcedi.get(i), i);
		}

		dataProvider.updateRowData(start, listaPraticaProcedi);

		pager.setDisplay(dataGrid);
		pager.setPageSize(ConsolePecConstants.WORKLIST_NUMERO_PER_PAGINA);
		if (start == 0) {
			pager.setPageStart(start);
		}

		dataGrid.setRowCount(maxSize, true);
	}

	public void refreshListaPraticheProcedi(final List<PraticaProcedi> listaPraticaProcedi) {

	}

	private class RadioSelectionColumn extends Column<PraticaProcedi, Boolean> {
		public RadioSelectionColumn() {
			super(new RadioButtonCell(true, false));
		}

		@Override
		public Boolean getValue(PraticaProcedi object) {
			return dataGrid.getSelectionModel().isSelected(object);
		}
	}

	private class OggettoColumn extends Column<PraticaProcedi, SafeHtml> {
		public OggettoColumn() {
			super(new ClickableCell());
		}

		@Override
		public SafeHtml getValue(PraticaProcedi object) {
			String oggetto = object != null ? object.getOggetto() : ConsolePecConstants.NESSUN_OGGETTO_MAIL_IN;
			String titolo = checkTitoloLength(oggetto);
			String htmlString = "<a href=\"javascript:;\" title=\"" + checkTitoloNotNull(oggetto) + "\">" + titolo + "</a>";
			SafeHtml html = SafeHtmlUtils.fromTrustedString(htmlString);
			return html;
		}
	}

	protected String checkTitoloNotNull(String titolo) {
		String result = titolo == null || titolo.isEmpty() ? ConsolePecConstants.NESSUN_OGGETTO_MAIL_IN : titolo;
		return result;
	}

	protected String checkTitoloLength(String oggetto) {
		String titolo = checkTitoloNotNull(oggetto);
		if (titolo.length() > ConsolePecConstants.MAX_LENGTH_TITOLO) {
			StringBuilder sb = new StringBuilder(titolo);
			titolo = sb.delete(ConsolePecConstants.MAX_LENGTH_TITOLO - 3, titolo.length()).append("...").toString();
		}

		return titolo;
	}

	private class NumAnnoPGColumn extends TextColumn<PraticaProcedi> {
		@Override
		public String getValue(PraticaProcedi object) {
			if (object != null) {
				return object.getNumeroProtocollo() + "/" + object.getAnnoProtocollo();
			}
			return "-/-";
		}
	}

	private class TipoColumn extends TextColumn<PraticaProcedi> {
		@Override
		public String getValue(PraticaProcedi object) {
			if (object != null) {
				return object.getTipoPratica();
			}
			return "-";
		}
	}

	private class AmbitoColumn extends TextColumn<PraticaProcedi> {
		@Override
		public String getValue(PraticaProcedi object) {
			if (object != null) {
				return object.getAmbito();
			}
			return "-";
		}
	}

	@Override
	public void setDataProvider(PraticaProcediDataProvider dataProvider) {
		this.dataProvider = dataProvider;
		this.dataProvider.addDataDisplay(dataGrid);
	}

}
