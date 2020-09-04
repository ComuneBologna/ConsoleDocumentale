package it.eng.portlet.consolepec.gwt.client.widget.datiaggiuntivi;

import static it.eng.cobo.consolepec.util.datiaggiuntivi.DatiAggiuntiviUtil.calcolaNomeAnagrafica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo.DatoAggiuntivoVisitor;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoAnagrafica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoTabella;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreMultiplo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreSingolo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.RigaDatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.ValoreCellaDatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.ValoreCellaDatoAggiuntivo.ValoreCellaDatoAggiuntivoAnagrafica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.ValoreCellaDatoAggiuntivo.ValoreCellaDatoAggiuntivoMultiplo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.ValoreCellaDatoAggiuntivo.ValoreCellaDatoAggiuntivoSingolo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.ValoreCellaDatoAggiuntivo.ValoreCellaVisitor;
import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import it.eng.cobo.consolepec.commons.rubrica.FiltriRicerca;
import it.eng.cobo.consolepec.util.datiaggiuntivi.DatiAggiuntiviUtil;
import it.eng.cobo.consolepec.util.objects.Ref;
import it.eng.portlet.consolepec.gwt.client.presenter.rubrica.command.DettaglioAnagraficaCommand;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.ClickableCell;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.EmptyCell;
import it.eng.portlet.consolepec.gwt.client.widget.datiaggiuntivi.FormDatiAggiuntiviWidget.TipoPagina;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.RicercaAnagrafiche;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.RicercaAnagraficheResult;

public class DatoAggiuntivoTabellaWidget extends Composite {

	private static DatoAggiuntivoTabellaWidgetUiBinder uiBinder = GWT.create(DatoAggiuntivoTabellaWidgetUiBinder.class);

	interface DatoAggiuntivoTabellaWidgetUiBinder extends UiBinder<Widget, DatoAggiuntivoTabellaWidget> {}

	@UiField
	HTMLPanel formPanel;
	@UiField
	HTMLPanel itemsPanel;
	@UiField
	HTMLPanel alertPanel;

	private Button aggiungiButton;

	private Presenter<?, ?> presenter;
	private DispatchAsync dispatch;

	private DataGridWidget<RigaDatoAggiuntivo> grid;
	private ListDataProvider<RigaDatoAggiuntivo> data = new ListDataProvider<RigaDatoAggiuntivo>();
	private List<EliminaButtonCell> bottoniElimina = new ArrayList<EliminaButtonCell>();
	private Map<String, Widget> intestazioni = new HashMap<String, Widget>();
	private List<Widget> campiObbligatori = new ArrayList<Widget>();

	public DatoAggiuntivoTabellaWidget(final Map<DatoAggiuntivo, Widget> intestazioni, List<RigaDatoAggiuntivo> righe, TipoPagina tipoPagina, Object openingRequestor, DispatchAsync dispatch) {
		super();
		initWidget(uiBinder.createAndBindUi(this));

		this.presenter = (Presenter<?, ?>) openingRequestor;
		this.dispatch = dispatch;

		grid = new DataGridWidget<RigaDatoAggiuntivo>(100, null, null);
		grid.setStyleName("tabella-modulo");
		grid.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		for (Entry<DatoAggiuntivo, Widget> entry : intestazioni.entrySet()) {
			this.intestazioni.put(entry.getKey().getNome(), entry.getValue());

			if (DatiAggiuntiviUtil.isObbligatorio(entry.getKey())) {
				campiObbligatori.add(entry.getValue());
			}
		}

		/*
		 * CREAZIONE FORM
		 */
		PanelContenitoreStrategy panelContenitoreStrategy = null;

		switch (tipoPagina) {
		case CREAZIONE:
			panelContenitoreStrategy = PanelContenitoreStrategy.CREAZIONE;
			break;

		case DETTAGLIO:
			panelContenitoreStrategy = PanelContenitoreStrategy.DETTAGLIO;
			break;

		case RICERCA:
			panelContenitoreStrategy = PanelContenitoreStrategy.RICERCA;
			break;

		case MODIFICA:
			panelContenitoreStrategy = PanelContenitoreStrategy.MODIFICA;
			break;

		default:
			throw new UnsupportedOperationException();
		}

		for (Entry<DatoAggiuntivo, Widget> e : intestazioni.entrySet()) {
			DatoAggiuntivo datoAggiuntivo = e.getKey();
			Widget datoAggiuntivoWidget = e.getValue();
			Widget widgetPanel = panelContenitoreStrategy.creaContenitore(datoAggiuntivo, datoAggiuntivoWidget);
			this.formPanel.add(widgetPanel);
		}

		/*
		 * CREAZIONE TABELLA La tabella non viene visualizzata in ricerca
		 */
		if (!TipoPagina.RICERCA.equals(tipoPagina)) {

			data.addDataDisplay(grid);
			itemsPanel.add(grid);

			aggiungiButton = new Button();
			aggiungiButton.setText("Aggiungi");
			aggiungiButton.setStyleName("btn");

			final Ref<Integer> ref = Ref.of(0);
			for (Entry<DatoAggiuntivo, Widget> e : intestazioni.entrySet()) {
				final DatoAggiuntivo datoAggiuntivo = e.getKey();

				datoAggiuntivo.accept(new DatoAggiuntivoVisitor() {

					@Override
					public void visit(DatoAggiuntivoTabella datoAggiuntivoTabella) {}

					@Override
					public void visit(DatoAggiuntivoAnagrafica datoAggiuntivoAnagrafica) {
						Integer i = ref.get();
						grid.addColumn(new DatoAggiuntivoColumn(new ClickableCell(), i),
								datoAggiuntivo.getDescrizione() + (DatiAggiuntiviUtil.isObbligatorio(datoAggiuntivo) && !datoAggiuntivo.getDescrizione().endsWith("*") ? " *" : ""));
						i++;
						ref.set(i);
					}

					@Override
					public void visit(DatoAggiuntivoValoreMultiplo datoAggiuntivoValoreMultiplo) {
						Integer i = ref.get();
						grid.addColumn(new DatoAggiuntivoColumn(new EmptyCell(), i),
								datoAggiuntivo.getDescrizione() + (DatiAggiuntiviUtil.isObbligatorio(datoAggiuntivo) && !datoAggiuntivo.getDescrizione().endsWith("*") ? " *" : ""));
						i++;
						ref.set(i);
					}

					@Override
					public void visit(DatoAggiuntivoValoreSingolo datoAggiuntivoValoreSingolo) {
						Integer i = ref.get();
						grid.addColumn(new DatoAggiuntivoColumn(new EmptyCell(), i),
								datoAggiuntivo.getDescrizione() + (DatiAggiuntiviUtil.isObbligatorio(datoAggiuntivo) && !datoAggiuntivo.getDescrizione().endsWith("*") ? " *" : ""));
						i++;
						ref.set(i);
					}
				});
			}

			HTMLPanel buttonPanel = new HTMLPanel("");
			buttonPanel.setStyleName("cell");
			aggiungiButton.setEnabled(true);
			buttonPanel.add(aggiungiButton);
			formPanel.add(buttonPanel);

			aggiungiButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {

					if (!DatiAggiuntiviWidgetUtil.validazioneClient(DatoAggiuntivoTabellaWidget.this.intestazioni, campiObbligatori))
						return;

					final RigaDatoAggiuntivo riga = new RigaDatoAggiuntivo();

					for (Entry<DatoAggiuntivo, Widget> e : intestazioni.entrySet()) {
						DatoAggiuntivo datoAggiuntivo = e.getKey();
						final Widget campo = e.getValue();
						ValoreCellaDatoAggiuntivo valoreCella = DatiAggiuntiviWidgetUtil.calcolaValoreCella(datoAggiuntivo, campo);
						riga.getCelle().add(valoreCella);
					}

					data.getList().add(riga);
				}
			});

			if (righe != null) {
				data.getList().clear();
				data.getList().addAll(righe);
			}

			bottoneElimina(true);
		}
	}

	public List<RigaDatoAggiuntivo> getRigheDatiAggiuntivi() {
		return data.getList();
	}

	public void setRigheDatiAggiuntivi(List<RigaDatoAggiuntivo> righe) {
		data.getList().clear();

		if (righe != null)
			data.getList().addAll(righe);
	}

	private void bottoneElimina(final boolean abilitato) {
		EliminaButtonCell cell = new EliminaButtonCell();
		cell.setEnabled(abilitato);
		bottoniElimina.add(cell);
		Column<RigaDatoAggiuntivo, String> eliminaColumn = new Column<RigaDatoAggiuntivo, String>(cell) {

			@Override
			public String getValue(RigaDatoAggiuntivo riga) {
				return "Elimina";
			}
		};

		eliminaColumn.setFieldUpdater(new FieldUpdater<RigaDatoAggiuntivo, String>() {

			@Override
			public void update(int paramInt, RigaDatoAggiuntivo riga, String paramC) {
				data.getList().remove(paramInt);
			}
		});

		grid.addColumn(eliminaColumn);
	}

	private class EliminaButtonCell extends ButtonCell {
		boolean abilitato;

		@Override
		public void render(Cell.Context context, SafeHtml data, SafeHtmlBuilder sb) {
			String abilitazione = abilitato ? "" : "disabled=\"disabled\"";
			sb.appendHtmlConstant("<button type=\"button\" tabindex=\"-1\" class=\"btn\"" + abilitazione + ">");
			if (data != null) {
				sb.append(data);
			}
			sb.appendHtmlConstant("</button>");

		}

		public void setEnabled(boolean abilitato) {
			this.abilitato = abilitato;
		}
	}

	private class DatoAggiuntivoColumn extends Column<RigaDatoAggiuntivo, SafeHtml> {
		private Column<RigaDatoAggiuntivo, SafeHtml> thiz;
		private int index;

		public DatoAggiuntivoColumn(Cell<SafeHtml> cell, int index) {
			super(cell);
			this.index = index;
			thiz = this;
		}

		@Override
		public SafeHtml getValue(RigaDatoAggiuntivo riga) {
			final Ref<SafeHtml> valore = Ref.of(null);

			ValoreCellaDatoAggiuntivo v = riga.getCelle().get(index);

			if (v != null) {
				v.accept(new ValoreCellaVisitor() {

					@Override
					public void visit(ValoreCellaDatoAggiuntivoSingolo cella) {
						StringBuilder sb = new StringBuilder("<span>").append(cella.toString()).append("</span>");
						SafeHtml sh = SafeHtmlUtils.fromTrustedString(sb.toString());
						valore.set(sh);
					}

					@Override
					public void visit(ValoreCellaDatoAggiuntivoMultiplo cella) {
						StringBuilder sb = new StringBuilder("<span>").append(cella.toString()).append("</span>");
						SafeHtml sh = SafeHtmlUtils.fromTrustedString(sb.toString());
						valore.set(sh);
					}

					@Override
					public void visit(final ValoreCellaDatoAggiuntivoAnagrafica cella) {
						StringBuilder sb = new StringBuilder("<a href=\"javascript:;\"><span>").append(cella.toString()).append("</span></a>");
						SafeHtml sh = SafeHtmlUtils.fromTrustedString(sb.toString());
						valore.set(sh);

						if (!cella.isEmpty()) {
							thiz.setFieldUpdater(new FieldUpdater<RigaDatoAggiuntivo, SafeHtml>() {

								@Override
								public void update(int index, RigaDatoAggiuntivo object, SafeHtml value) {
									final DettaglioAnagraficaCommand dettaglioAnagraficaCommand = new DettaglioAnagraficaCommand(presenter);

									RicercaAnagrafiche ricercaAnagrafiche = new RicercaAnagrafiche();
									ricercaAnagrafiche.getFiltri().put(FiltriRicerca.IDENTIFICATIVO, cella.getIdAnagrafica());
									dispatch.execute(ricercaAnagrafiche, new AsyncCallback<RicercaAnagraficheResult>() {

										@Override
										public void onFailure(Throwable caught) {}

										@Override
										public void onSuccess(RicercaAnagraficheResult result) {
											if (result.isError()) {

											} else {
												if (result.getAnagrafiche() != null && !result.getAnagrafiche().isEmpty()) {
													dettaglioAnagraficaCommand.setAnagrafica(result.getAnagrafiche().get(0));
													dettaglioAnagraficaCommand.execute();
												}
											}
										}
									});
								}
							});
						}
					}
				});

			} else {
				StringBuilder sb = new StringBuilder("<span>").append("</span>");
				SafeHtml sh = SafeHtmlUtils.fromTrustedString(sb.toString());
				valore.set(sh);
			}

			return valore.get();
		}

	}

	public void setEditable(boolean editabile) {
		aggiungiButton.setEnabled(editabile);
		for (EliminaButtonCell bottoneElimina : this.bottoniElimina) {
			bottoneElimina.setEnabled(editabile);
		}

		this.grid.redraw();
	}

	public void setAnagrafica(DatoAggiuntivoAnagrafica datoAggiuntivoAnagrafica, final Anagrafica anagrafica) {

		final Widget widgetAnagrafica = intestazioni.get(datoAggiuntivoAnagrafica.getNome());

		if (anagrafica != null) {
			((DatoAggiuntivoAnagraficaWidget) widgetAnagrafica).setAnagraficaSelezionata(true);
			((DatoAggiuntivoAnagraficaWidget) widgetAnagrafica).getButton().setText("Rimuovi");

			datoAggiuntivoAnagrafica.accept(new DatoAggiuntivo.DatoAggiuntivoVisitorAdapter() {

				@Override
				public void visit(DatoAggiuntivoAnagrafica datoAggiuntivo) {
					datoAggiuntivo.setIdAnagrafica(anagrafica.getId());
					datoAggiuntivo.setValore(calcolaNomeAnagrafica(anagrafica));
					DatiAggiuntiviWidgetUtil.setValoreAggiuntivo(widgetAnagrafica, datoAggiuntivo);
					datoAggiuntivo.setEditabile(datoAggiuntivo.isEditabile());
				}
			});

		} else {
			((DatoAggiuntivoAnagraficaWidget) widgetAnagrafica).setAnagraficaSelezionata(false);
			((DatoAggiuntivoAnagraficaWidget) widgetAnagrafica).getButton().setText("Seleziona");

			datoAggiuntivoAnagrafica.accept(new DatoAggiuntivo.DatoAggiuntivoVisitorAdapter() {

				@Override
				public void visit(DatoAggiuntivoAnagrafica datoAggiuntivo) {
					datoAggiuntivo.setIdAnagrafica(null);
					datoAggiuntivo.setValore(null);
					DatiAggiuntiviWidgetUtil.setValoreAggiuntivo(widgetAnagrafica, datoAggiuntivo);
					datoAggiuntivo.setEditabile(datoAggiuntivo.isEditabile());
				}
			});
		}
	}
}
