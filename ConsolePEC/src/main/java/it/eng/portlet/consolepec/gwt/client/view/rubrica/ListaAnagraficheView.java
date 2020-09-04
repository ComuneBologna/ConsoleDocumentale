package it.eng.portlet.consolepec.gwt.client.view.rubrica;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Strings;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import it.eng.cobo.consolepec.commons.rubrica.Anagrafica.Stato;
import it.eng.cobo.consolepec.commons.rubrica.PersonaFisica;
import it.eng.cobo.consolepec.commons.rubrica.PersonaGiuridica;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.rubrica.ListaAnagrafichePresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.rubrica.ListaAnagrafichePresenter.AnnullaCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.rubrica.ListaAnagrafichePresenter.AvantiCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.rubrica.ListaAnagrafichePresenter.CreaCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.rubrica.command.DettaglioAnagraficaCommand;
import it.eng.portlet.consolepec.gwt.client.widget.CustomPager;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.RadioButtonCell;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridTableBuilder;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget;
import it.eng.portlet.consolepec.gwt.client.widget.images.ResPager;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;

/**
 * @author GiacomoFM
 * @since 15/set/2017
 */
public class ListaAnagraficheView extends ViewImpl implements ListaAnagrafichePresenter.MyView {

	private static final String STATO_ERRATO = "STATO ERRATO";

	private ColonnaSelezione colonnaSelezione = new ColonnaSelezione();
	private ColonnaDenominazione colonnaDenominazione = new ColonnaDenominazione();
	private ColonnaID colonnaId = new ColonnaID();
	private ColonnaStato colonnaStato = new ColonnaStato();
	private ColonnaDettaglio colonnaDettaglio = new ColonnaDettaglio();

	private final Widget widget;
	private DettaglioAnagraficaCommand command;
	private Anagrafica anagraficaSelezionata;

	@UiField(provided = true) MessageAlertWidget messageAlertWidget;

	@UiField HeadingElement mainTitle;

	@UiField(provided = true) DataGridWidget<Anagrafica> dataGrid;

	@UiField(provided = true) SimplePager pager;

	@UiField TextBox identificativo;

	@UiField TextBox denominazione;

	@UiField(provided = true) SuggestBox statoDocumentale;

	@UiField Button pulisciButton;

	@UiField Button cercaButton;

	@UiField Button creaButton;

	@UiField Button annullaButton;

	@UiField Button avantiButton;

	@UiField DivElement buttonsRadio;

	private AvantiCommand avantiCommand;

	private AnnullaCommand annullaCommand;

	public interface Binder extends UiBinder<Widget, ListaAnagraficheView> {
		//
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		mainTitle.scrollIntoView();
		Window.scrollTo(0, 0);
	}

	@Inject
	public ListaAnagraficheView(final Binder binder, final EventBus eventBus, ConfigurazioniHandler configurazioniHandler) {
		messageAlertWidget = new MessageAlertWidget(eventBus);
		pager = new CustomPager(SimplePager.TextLocation.CENTER, ResPager.CSS, false, 0, true, configurazioniHandler.getProprietaGenerali().getMaxRisultatiWorklist());

		dataGrid = new DataGridWidget<Anagrafica>(ConsolePecConstants.WORKLIST_NUMERO_PER_PAGINA, null, null);
		SingleSelectionModel<Anagrafica> selectionModel = new SingleSelectionModel<Anagrafica>();
		dataGrid.setSelectionModel(selectionModel);

		dataGrid.addColumn(colonnaSelezione, "");
		dataGrid.addColumn(colonnaDenominazione, "Denominazione");
		dataGrid.addColumn(colonnaId, "Codice fiscale");
		dataGrid.addColumn(colonnaStato, "Stato documentale");
		dataGrid.addColumn(colonnaDettaglio, "");

		statoDocumentale = new SuggestBox(new SpacebarSuggestOracle(new ArrayList<String>()));
		List<String> stati = Arrays.asList(Stato.ATTIVA.toString(), Stato.DISATTIVA.toString());
		((SpacebarSuggestOracle) statoDocumentale.getSuggestOracle()).setSuggestions(stati);
		statoDocumentale.getElement().setPropertyString("placeholder", "Stato Documentale");

		widget = binder.createAndBindUi(this);

		identificativo.getElement().setPropertyString("placeholder", "Codice Fiscale/Partita IVA");
		denominazione.getElement().setPropertyString("placeholder", "Denominazione");

		avantiButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				avantiCommand.execute();
			}
		});

		annullaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent ce) {
				annullaCommand.execute();
			}
		});
	}

	@UiHandler("pulisciButton")
	public void onPulisciClickEvent(ClickEvent event) {
		resetFiltriRicerca();
		event.stopPropagation();
	}

	@Override
	public void setRicercaCommand(final Command command) {
		cercaButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				command.execute();
				event.stopPropagation();
			}
		});
	}

	@Override
	public void setCreaCommand(final CreaCommand command) {
		creaButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				command.execute();
				event.stopPropagation();
			}
		});
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public String getFiltroIdentificativo() {
		return identificativo.getValue();
	}

	@Override
	public String getFiltroDenominazione() {
		return denominazione.getValue();
	}

	@Override
	public String getFiltroStatoDocumentale() {
		return statoDocumentale.getValue();
	}

	@Override
	public void resetFiltriRicerca() {
		identificativo.setValue("");
		denominazione.setValue("");
		statoDocumentale.setValue("");
	}

	@Override
	public void setDettaglioAnagraficaCommand(final DettaglioAnagraficaCommand command) {
		this.command = command;
	}

	@Override
	public void mostraAnagrafiche(final List<Anagrafica> anagrafiche) {

		dataGrid.setRowCount(anagrafiche.size(), true);

		DataGridTableBuilder<Anagrafica> builder = new DataGridTableBuilder<Anagrafica>(dataGrid);
		builder.start(true);
		for (int i = 0; i < anagrafiche.size(); i++) {
			builder.buildRowImpl(anagrafiche.get(i), i);
		}
		avantiButton.setEnabled(false);
		for (int i = 0; i < anagrafiche.size(); i++) {

			if (dataGrid.getSelectionModel().isSelected(anagrafiche.get(i))) {
				anagraficaSelezionata = anagrafiche.get(i);
				avantiButton.setEnabled(true);
			}

		}

		ListDataProvider<Anagrafica> dataProvider = new ListDataProvider<>();
		dataProvider.setList(anagrafiche);
		dataProvider.addDataDisplay(dataGrid);

		pager.setDisplay(dataGrid);
		pager.setPageSize(ConsolePecConstants.WORKLIST_NUMERO_PER_PAGINA);
	}

	private class ColonnaDenominazione extends TextColumn<Anagrafica> {
		@Override
		public String getValue(Anagrafica object) {
			String val = "";
			if (object instanceof PersonaFisica) {
				val = ((PersonaFisica) object).getNome() + " " + (Strings.isNullOrEmpty(((PersonaFisica) object).getCognome()) ? "" : ((PersonaFisica) object).getCognome());
			} else {
				val = ((PersonaGiuridica) object).getRagioneSociale();
			}
			return val;
		}
	}

	private class ColonnaID extends TextColumn<Anagrafica> {
		@Override
		public String getValue(Anagrafica object) {
			String val = "";
			if (object instanceof PersonaFisica) {
				val = ((PersonaFisica) object).getCodiceFiscale();
			} else {
				val = ((PersonaGiuridica) object).getPartitaIva();
			}
			return val;
		}
	}

	private class ColonnaStato extends TextColumn<Anagrafica> {
		@Override
		public String getValue(Anagrafica object) {
			if (object.getStato() != null) {
				return object.getStato().toString();
			} else {
				return STATO_ERRATO;
			}
		}
	}

	private class ColonnaDettaglio extends Column<Anagrafica, String> {
		public ColonnaDettaglio() {
			super(new StyledButtonCell());
			setCellStyleNames("last-column");
			setFieldUpdater(new DettaglioUpdater());

		}

		@Override
		public String getValue(Anagrafica object) {
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

	private class DettaglioUpdater implements FieldUpdater<Anagrafica, String> {
		@Override
		public void update(int index, Anagrafica anagrafica, String value) {
			command.setAnagrafica(anagrafica);
			command.execute();
		}
	}

	private class ColonnaSelezione extends Column<Anagrafica, Boolean> {

		public ColonnaSelezione() {
			super(new RadioButtonCell(true, false));
			setCellStyleNames("check");
		}

		@Override
		public Boolean getValue(Anagrafica anagrafica) {
			boolean selezione = dataGrid.getSelectionModel().isSelected(anagrafica);
			if (selezione) {
				anagraficaSelezionata = anagrafica;
				avantiButton.setEnabled(true);
			}

			return selezione;
		}
	}

	@Override
	public void mostraColonnaSelezione(boolean selezione) {
		avantiButton.setEnabled(false);

		int i = 0;

		while (dataGrid.getColumnCount() > 0) {
			dataGrid.removeColumn(0);
		}

		if (selezione) {
			avantiButton.setEnabled(false);
			avantiButton.setVisible(true);
			annullaButton.setVisible(true);
			creaButton.setVisible(true);
			buttonsRadio.getStyle().setDisplay(Display.BLOCK);
			dataGrid.addColumn(colonnaSelezione, "");
			dataGrid.getHeader(i).setHeaderStyleNames("check");
			i++;
		}
		colonnaDenominazione.setCellStyleNames("nome");

		dataGrid.addColumn(colonnaDenominazione, "Denominazione");
		dataGrid.getHeader(i).setHeaderStyleNames("nome");

		i++;

		colonnaDenominazione.setCellStyleNames("descrizione");
		dataGrid.addColumn(colonnaId, "Codice fiscale");
		dataGrid.getHeader(i).setHeaderStyleNames("descrizione");

		i++;

		colonnaDenominazione.setCellStyleNames("stato");
		dataGrid.addColumn(colonnaStato, "Stato documentale");
		dataGrid.getHeader(i).setHeaderStyleNames("stato");
		if (!selezione) {
			avantiButton.setEnabled(false);
			avantiButton.setVisible(false);
			annullaButton.setVisible(false);
			creaButton.setVisible(false);
			dataGrid.addColumn(colonnaDettaglio, "");
			buttonsRadio.getStyle().setDisplay(Display.NONE);
			i++;

			dataGrid.getHeader(i).setHeaderStyleNames("last-column");
		}
	}

	@Override
	public void setAvantiCommand(AvantiCommand avantiCommand) {
		this.avantiCommand = avantiCommand;
	}

	@Override
	public void setAnnullaCommand(AnnullaCommand annullaCommand) {
		this.annullaCommand = annullaCommand;
	}

	@Override
	public Anagrafica getAnagrafica() {
		return anagraficaSelezionata;
	}
}
