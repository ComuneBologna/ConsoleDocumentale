package it.eng.portlet.consolepec.gwt.client.worklist;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Strings;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SetSelectionModel;
import com.google.web.bindery.event.shared.EventBus;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.objects.Ref;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.cell.StyledCheckboxCell;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.ClickableCell;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO.ModelloVisitor;
import it.eng.portlet.consolepec.gwt.shared.model.ColonnaWorklist;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO.TipoPresaInCarico;
import it.eng.portlet.consolepec.gwt.shared.model.TemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TemplatePdfDTO;

public abstract class AbstractWorklistStrategy implements WorklistStrategy {
	protected RigaEspansaStrategy rigaEspansaStrategy;
	private final List<CheckRigaEventListener> checkRigaListeners = new ArrayList<WorklistStrategy.CheckRigaEventListener>();
	private final List<EspandiRigaEventListener> espandiRigaListeners = new ArrayList<WorklistStrategy.EspandiRigaEventListener>();
	private PecInPraticheDB praticheDB;
	private SitemapMenu sitemapMenu;
	private EventBus eventBus;
	private final Set<String> righeSelezionate = new HashSet<String>();
	private DataGridWidget<PraticaDTO> grid;
	private final AsyncDataProvider<PraticaDTO> dataProvider = new PraticaDataProvider();
	private RicercaEventListener ricercaListener;
	private final RicercaCallback ricercaCallback = new RicercaCallbackImpl();

	/* Colonne */
	protected Column<PraticaDTO, Boolean> selezColumn;
	private TextColumn<PraticaDTO> pgColumn, mittColumn;
	protected TextColumn<PraticaDTO> dataColumn;
	protected Column<PraticaDTO, String> statoColumn, assegnatarioColumn;
	private Column<PraticaDTO, SafeHtml> titoloColumn;
	private Column<PraticaDTO, SafeUri> presaInCaricoColumn;
	private Column<PraticaDTO, SafeUri> tipoPraticaColumn;

	@Override
	public void setRigaEspansaStrategy(RigaEspansaStrategy rigaEspansaStrategy) {
		if (this.rigaEspansaStrategy != null) {
			throw new IllegalStateException("rigaEspansaStrategy già impostato");
		}
		this.rigaEspansaStrategy = rigaEspansaStrategy;
	}

	/**
	 * Evento di cambio stato singola riga. ad uso interno
	 *
	 * @param listener
	 */
	@Override
	public void addCheckRigaEventListener(CheckRigaEventListener listener) {
		this.checkRigaListeners.add(listener);
	}

	@Override
	public void addEspandiRigaEventListener(EspandiRigaEventListener listener) {
		this.espandiRigaListeners.add(listener);
	}

	@Override
	public void setRicercaEventListener(RicercaEventListener listener) {
		this.ricercaListener = listener;
	}

	@Override
	public void configuraGrid(final DataGridWidget<PraticaDTO> dataGrid, SimplePager pager) {
		if (grid != null) {
			throw new IllegalStateException("grid già impostata");
		}
		this.grid = dataGrid;

		configuraColonne();

		/* gestione del reset delle righe selezionate */
		dataGrid.addRangeChangeHandler(new Handler() {

			@Override
			public void onRangeChange(RangeChangeEvent event) {
				resetSelezioni();
			}
		});

		/* gestione dell'ordinamento */
		dataGrid.getColumnSortList().setLimit(1);// solo una entry di ricerca
		dataGrid.getColumnSortList().push(getDefaultSort());
		dataGrid.addColumnSortHandler(new ColumnSortEvent.Handler() {

			@Override
			public void onColumnSort(ColumnSortEvent event) {
				restartSearchDatiGrid();
			}
		});
		DefaultSelectionEventManager<PraticaDTO> selevtman = DefaultSelectionEventManager.createCheckboxManager(0);
		MultiSelectionModel<PraticaDTO> selectionModel = new MultiSelectionModel<PraticaDTO>(PRATICA_KEY_PROVIDER);
		dataGrid.setSelectionModel(selectionModel, selevtman);
		/* gestione del pager */
		pager.setDisplay(dataGrid);
		/* gestione del dataprovider. provoca un trigger della ricerca */
		dataProvider.addDataDisplay(dataGrid);

	}

	@Override
	public void resetSelezioni() {
		SelectionModel<? super PraticaDTO> selModel = grid.getSelectionModel();
		if (selModel instanceof SetSelectionModel) {
			@SuppressWarnings("unchecked") SetSelectionModel<PraticaDTO> setSelModel = (SetSelectionModel<PraticaDTO>) selModel;
			setSelModel.clear();
		}

		righeSelezionate.clear();
	}

	@Override
	public void restartSearchDatiGrid() {
		resetSelezioni();
		int start = 0;
		grid.setPageStart(start);
		int length = grid.getPageSize();
		String campoOrdinamento = null;
		boolean asc = false;

		if (grid.getColumnSortList().size() > 0) {
			campoOrdinamento = grid.getColumnSortList().get(0).getColumn().getDataStoreName();
			asc = grid.getColumnSortList().get(0).isAscending();
		}
		ricercaListener.onStartRicerca(start, length, ColonnaWorklist.getFromId(campoOrdinamento), asc, ricercaCallback);
	}

	@Override
	public void resetGrid() {
		resetSelezioni();
		dataProvider.updateRowCount(0, true);
		dataProvider.updateRowData(0, new ArrayList<PraticaDTO>());
	}

	@Override
	public void refreshDatiGrid() {
		resetSelezioni();
		int start = grid.getVisibleRange().getStart();
		grid.setPageStart(start);
		int length = grid.getPageSize();
		String campoOrdinamento = null;
		boolean asc = false;
		if (grid.getColumnSortList().size() > 0) {
			campoOrdinamento = grid.getColumnSortList().get(0).getColumn().getDataStoreName();
			asc = grid.getColumnSortList().get(0).isAscending();
		}
		ricercaListener.onStartRicerca(start, length, ColonnaWorklist.getFromId(campoOrdinamento), asc, ricercaCallback);
	}

	@Override
	public Set<String> getIdRigheSelezionate() {
		// unmodifiable genera errori se il set viene ser.
		Set<String> result = new HashSet<String>(righeSelezionate);
		return result;
	}

	@Override
	public void aggiornaRiga(PraticaDTO pratica) {
		int index = grid.getVisibleItems().indexOf(pratica);
		List<PraticaDTO> list = new ArrayList<PraticaDTO>();
		list.add(pratica);
		boolean aggEspansa = grid.isRigaEspansaByKey(pratica.getClientID());
		grid.updateRowData(index, list);
		if (aggEspansa) {
			espandiRiga(pratica.getClientID(), pratica);
		}
	}

	@Override
	public void espandiRiga(String clientID, PraticaDTO pratica) {
		// Dati dei bean

		HTMLPanel dettaglioContent = new HTMLPanel("");
		HTMLPanel operations = new HTMLPanel("");

		rigaEspansaStrategy.disegnaDettaglio(dettaglioContent, pratica);
		rigaEspansaStrategy.disegnaOperazioni(operations, pratica,
				/*
				 * listener per lo strategy della riga espansa. ripassa l'evento ai listener registrati qua
				 */
				new EspandiRigaEventListener() {

					@Override
					public void onEspandiRiga(String clientID, TipologiaPratica tipologiaPratica, boolean isEspansa) {
						notifyEspandiRigaEventListeners(clientID, tipologiaPratica, isEspansa);
					}
				});
		/*
		 * all'espansione vogliamo sempre mostrare la pratica come letta. la persistenza sarà gestita dai presenter
		 */
		boolean letto = pratica.isLetto();
		pratica.setLetto(true);
		getGrid().espandiRiga(pratica, dettaglioContent, operations);
		pratica.setLetto(letto);
	}

	@Override
	public void setPraticheDB(PecInPraticheDB praticheDB) {
		this.praticheDB = praticheDB;
	}

	@Override
	public void setSitemapMenu(SitemapMenu sitemapMenu) {
		this.sitemapMenu = sitemapMenu;
	}

	protected SitemapMenu getSitemapMenu() {
		return sitemapMenu;
	}

	/* metodi interni */
	protected String sanitizeNull(String input) {
		if (input == null || input.trim().length() == 0) {
			return "-";
		} else {
			return input;
		}
	}

	protected void configuraColonne() {
		configuraColonnaSelezione();
		configuraColonnaTipoPratica();
		configuraColonnaPresaInCarico();
		configuraColonnaPG();
		configuraColonnaStato();
		configuraColonnaTitolo();
		configuraColonnaProvenienza();
		configuraColonnaAssegnatario();
		configuraColonnaData();

		String[] stiliColonne = { "check", "tipopratica", "presaincarico", "numannopg", "stato", "titolo", "mittente", "assegnatario", "data" };
		for (int i = 0; i < stiliColonne.length; i++) {
			this.grid.getHeader(i).setHeaderStyleNames(stiliColonne[i]);
		}
	}

	protected void notifyCheckRigaListeners(String clientID, boolean checked) {
		/* prima di notificare, aggiorno il numero totale di righe selezionate */
		if (checked) {
			righeSelezionate.add(clientID);
		} else {
			righeSelezionate.remove(clientID);
		}
		/* notifico i listeners */
		for (CheckRigaEventListener listener : checkRigaListeners) {
			listener.onCheckRiga(clientID, checked);
		}
	}

	protected void notifyEspandiRigaEventListeners(String clientID, TipologiaPratica tipologiaPratica, boolean isEspansa) {
		for (EspandiRigaEventListener listener : espandiRigaListeners) {
			listener.onEspandiRiga(clientID, tipologiaPratica, isEspansa);
		}
	}

	protected ColumnSortInfo getDefaultSort() {
		return new ColumnSortInfo(dataColumn, false);

	}

	protected DataGridWidget<PraticaDTO> getGrid() {
		return grid;
	}

	protected PecInPraticheDB getPraticheDB() {
		return praticheDB;
	}

	/* metodi di configurazione colonna */

	protected Column<PraticaDTO, Boolean> configuraColonnaSelezione() {

		selezColumn = new Column<PraticaDTO, Boolean>(new StyledCheckboxCell()) {

			@Override
			public Boolean getValue(PraticaDTO object) {
				boolean res = getGrid().getSelectionModel().isSelected(object);

				notifyCheckRigaListeners(object.getClientID(), res);
				return res;
			}
		};
		selezColumn.setCellStyleNames(ColonnaWorklist.SELEZIONE.getId());

		getGrid().addColumn(selezColumn, getIntestazioneColonnaSelezione());
		return selezColumn;

	}

	protected String getIntestazioneColonnaSelezione() {
		return "";
	}

	protected TextColumn<PraticaDTO> configuraColonnaPG() {
		pgColumn = new TextColumn<PraticaDTO>() {
			@Override
			public String getValue(PraticaDTO object) {
				if (object.getNumeroPG() != null && object.getAnnoPG() != null) {
					return object.getNumeroPG() + "/" + object.getAnnoPG();
				} else {
					return sanitizeNull(null);
				}
			}
		};

		pgColumn.setSortable(true);
		pgColumn.setCellStyleNames(ColonnaWorklist.PG.getId());
		pgColumn.setDataStoreName(ColonnaWorklist.PG.getId());
		getGrid().addColumn(pgColumn, getIntestazioneColonnaPG());
		return pgColumn;
	}

	protected String getIntestazioneColonnaPG() {
		return "P.G.";
	}

	protected Column<PraticaDTO, SafeUri> configuraColonnaTipoPratica() {
		final CustomSafeImageCell cellImg = new CustomSafeImageCell(false);
		tipoPraticaColumn = new Column<PraticaDTO, SafeUri>(cellImg) {
			@Override
			public SafeUri getValue(PraticaDTO object) {

				cellImg.setTitle(object.getTipologiaPratica().getEtichettaTipologia());

				TipologiaPratica tp = object.getTipologiaPratica();

				if (PraticaUtil.isIngresso(tp)) {
					return ConsolePECIcons._instance.bustinaChiusaEmail().getSafeUri();

				} else if (PraticaUtil.isEmailOut(tp)) {
					return ConsolePECIcons._instance.bustinaApertaEmail().getSafeUri();

				} else if (PraticaUtil.isPraticaModulistica(tp)) {
					return ConsolePECIcons._instance.praticamodulistica().getSafeUri();

				} else if (PraticaUtil.isFascicolo(tp)) {
					return ConsolePECIcons._instance.fascicolo().getSafeUri();

				} else if (PraticaUtil.isModello(tp)) {

					final Ref<SafeUri> safeUri = Ref.of(null);

					BaseTemplateDTO modello = (BaseTemplateDTO) object;
					modello.accept(new ModelloVisitor() {

						@Override
						public void visit(TemplateDTO modelloMail) {
							safeUri.set(ConsolePECIcons._instance.praticamodulistica().getSafeUri());
						}

						@Override
						public void visit(TemplatePdfDTO modelloPdf) {
							safeUri.set(ConsolePECIcons._instance.praticamodulistica().getSafeUri());
						}
					});

					return safeUri.get();

				} else {
					return ConsolePECIcons._instance.imgVuota19x23().getSafeUri();
				}
			}
		};
		tipoPraticaColumn.setSortable(false);
		tipoPraticaColumn.setCellStyleNames(ColonnaWorklist.TIPO_PRATICA.getId());
		tipoPraticaColumn.setDataStoreName(ColonnaWorklist.TIPO_PRATICA.getId());
		getGrid().addColumn(tipoPraticaColumn, getIntestazioneColonnaTipoPratica());
		return tipoPraticaColumn;

	}

	protected String getIntestazioneColonnaTipoPratica() {
		return "";
	}

	protected Column<PraticaDTO, SafeUri> configuraColonnaPresaInCarico() {
		final CustomSafeImageCell cellImg = new CustomSafeImageCell(false);
		presaInCaricoColumn = new Column<PraticaDTO, SafeUri>(cellImg) {
			@Override
			public SafeUri getValue(PraticaDTO object) {
				TipoPresaInCarico tp = object.getTipoPresaInCarico();
				if (tp != null) {
					cellImg.setTitle(tp.getDesc());
					switch (tp) {
					case ALTRO_UTENTE: {
						return ConsolePECIcons._instance.praticaInCaricoAdAltri().getSafeUri();
					}
					case UTENTE_CORRENTE: {
						return ConsolePECIcons._instance.praticaInCaricoAMe().getSafeUri();
					}
					default: {
						return ConsolePECIcons._instance.imgVuota19x23().getSafeUri();
					}
					}
				}
				return ConsolePECIcons._instance.imgVuota19x23().getSafeUri();
			}

		};
		presaInCaricoColumn.setSortable(false);
		presaInCaricoColumn.setCellStyleNames(ColonnaWorklist.PRESA_IN_CARICO.getId());
		presaInCaricoColumn.setDataStoreName(ColonnaWorklist.PRESA_IN_CARICO.getId());
		getGrid().addColumn(presaInCaricoColumn, getIntestazioneColonnaPresaInCarico());
		return presaInCaricoColumn;
	}

	protected String getIntestazioneColonnaPresaInCarico() {
		return "";
	}

	protected Column<PraticaDTO, String> configuraColonnaStato() {

		AbstractCell<String> noEscapeCell = new AbstractCell<String>() {

			@Override
			public void render(Context context, String value, SafeHtmlBuilder sb) {
				if (value != null) {
					sb.appendHtmlConstant(value);
				}
			}
		};

		statoColumn = new Column<PraticaDTO, String>(noEscapeCell) {

			@Override
			public String getValue(PraticaDTO praticaDTO) {

				FascicoloDTO fascicoloDTO = null;

				if (praticaDTO instanceof FascicoloDTO) {
					fascicoloDTO = (FascicoloDTO) praticaDTO;
				}

				if (fascicoloDTO != null && fascicoloDTO.getStepIter() != null) {
					return praticaDTO.getStatoLabel() + "<BR/>" + "(" + fascicoloDTO.getStepIter().getNome() + ")";
				} else {
					return praticaDTO.getStatoLabel();
				}

			}

		};

		statoColumn.setCellStyleNames(ColonnaWorklist.STATO.getId());
		statoColumn.setSortable(false);
		statoColumn.setDataStoreName(ColonnaWorklist.STATO.getId());
		getGrid().addColumn(statoColumn, getIntestazioneColonnaStato());
		return statoColumn;

	}

	protected String getIntestazioneColonnaStato() {
		return "Stato";
	}

	protected Column<PraticaDTO, SafeHtml> configuraColonnaTitolo() {

		titoloColumn = new Column<PraticaDTO, SafeHtml>(new ClickableCell()) {

			@Override
			public SafeHtml getValue(PraticaDTO object) {
				String titolo = checkTitoloLength(object);
				String htmlString = "<a href=\"javascript:;\" title=\"" + checkTitoloNotNull(object.getTitolo()).replaceAll("\"", "'") + "\">" + titolo + "</a>";
				SafeHtml html = SafeHtmlUtils.fromTrustedString(htmlString);
				return html;
			}
		};

		titoloColumn.setFieldUpdater(new FieldUpdater<PraticaDTO, SafeHtml>() {

			@Override
			public void update(int index, PraticaDTO object, SafeHtml value) {

				if (!PraticaUtil.isPraticaProcedi(object.getTipologiaPratica())) {
					boolean isEspansa = getGrid().isRigaEspansaByKey(object.getClientID());
					notifyEspandiRigaEventListeners(object.getClientID(), object.getTipologiaPratica(), isEspansa);
				}
			}
		});
		getGrid().addColumn(titoloColumn, getIntestazioneColonnaTitolo());
		titoloColumn.setCellStyleNames(ColonnaWorklist.TITOLO.getId());
		titoloColumn.setDataStoreName(ColonnaWorklist.TITOLO.getId());
		titoloColumn.setSortable(true);
		return titoloColumn;
	}

	protected String getIntestazioneColonnaTitolo() {
		return "Titolo";
	}

	protected TextColumn<PraticaDTO> configuraColonnaProvenienza() {
		mittColumn = new TextColumn<PraticaDTO>() {
			@Override
			public String getValue(PraticaDTO object) {
				return getProvenienza(object);
			}
		};
		mittColumn.setCellStyleNames(ColonnaWorklist.PROVENIENZA.getId());
		mittColumn.setDataStoreName(ColonnaWorklist.PROVENIENZA.getId());
		mittColumn.setSortable(true);
		getGrid().addColumn(mittColumn, getIntestazioneColonnaProvenienza());
		return mittColumn;
	}

	protected TextColumn<PraticaDTO> configuraColonnaAssegnatario() {

		AbstractCell<String> noEscapeCell = new AbstractCell<String>() {

			@Override
			public void render(Context context, String value, SafeHtmlBuilder sb) {
				if (value != null) {
					sb.appendHtmlConstant(value);
				}
			}
		};

		assegnatarioColumn = new Column<PraticaDTO, String>(noEscapeCell) {
			@Override
			public String getValue(PraticaDTO praticaDTO) {
				if (getAssegnatario(praticaDTO) != null && !getAssegnatario(praticaDTO).isEmpty()) {
					if (praticaDTO instanceof FascicoloDTO) {
						FascicoloDTO f = (FascicoloDTO) praticaDTO;
						return getAssegnatario(praticaDTO) + ((Strings.isNullOrEmpty(f.getOperatore())) ? "" : "<BR/>" + "(" + f.getOperatore() + ")");

					} else if (praticaDTO instanceof PecInDTO) {
						PecInDTO p = (PecInDTO) praticaDTO;
						return getAssegnatario(praticaDTO) + ((Strings.isNullOrEmpty(p.getOperatore())) ? "" : "<BR/>" + "(" + p.getOperatore() + ")");

					} else {
						return getAssegnatario(praticaDTO);
					}
				} else {
					return "";
				}
			}
		};
		assegnatarioColumn.setCellStyleNames(ColonnaWorklist.ASSEGNATARIO.getId());
		assegnatarioColumn.setDataStoreName(ColonnaWorklist.ASSEGNATARIO.getId());
		getGrid().addColumn(assegnatarioColumn, getIntestazioneColonnaAssegnatario());
		return mittColumn;
	}

	protected String getAssegnatario(PraticaDTO object) {
		return object.getAssegnatario() != null ? object.getAssegnatario() : "";
	}

	protected String getIntestazioneColonnaAssegnatario() {
		return "Assegnatario";
	}

	protected String getProvenienza(PraticaDTO object) {
		return object.getProvenienza();
	}

	protected String getIntestazioneColonnaProvenienza() {
		return "Provenienza";
	}

	protected TextColumn<PraticaDTO> configuraColonnaData() {
		dataColumn = new TextColumn<PraticaDTO>() {
			@Override
			public String getValue(PraticaDTO object) {
				return getData(object);
			}
		};
		dataColumn.setCellStyleNames(ColonnaWorklist.DATA.getId());
		dataColumn.setDataStoreName(ColonnaWorklist.DATA.getId());
		dataColumn.setSortable(true);
		getGrid().addColumn(dataColumn, getIntestazioneColonnaData());
		return dataColumn;
	}

	protected String getData(PraticaDTO praticaDTO) {
		return praticaDTO.getDataOraCreazione();
	}

	protected String getIntestazioneColonnaData() {
		return "Data";
	}

	@Override
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	protected String checkTitoloNotNull(String titolo) {
		String result = titolo == null || titolo.isEmpty() ? ConsolePecConstants.NESSUN_OGGETTO_MAIL_IN : titolo;
		return result;
	}

	protected String checkTitoloLength(PraticaDTO praticaDTO) {
		String titolo = checkTitoloNotNull(praticaDTO.getTitolo());
		if (titolo.length() > ConsolePecConstants.MAX_LENGTH_TITOLO) {
			StringBuilder sb = new StringBuilder(titolo);
			titolo = sb.delete(ConsolePecConstants.MAX_LENGTH_TITOLO - 3, titolo.length()).append("...").toString();
		}

		return titolo;
	}

	/* classi internet */
	class PraticaDataProvider extends AsyncDataProvider<PraticaDTO> {

		@Override
		protected void onRangeChanged(HasData<PraticaDTO> display) {
			int start = display.getVisibleRange().getStart();
			int length = display.getVisibleRange().getLength();
			String campoOrdinamento = null;
			boolean asc = false;
			if (grid.getColumnSortList().size() > 0) {
				campoOrdinamento = grid.getColumnSortList().get(0).getColumn().getDataStoreName();
				asc = grid.getColumnSortList().get(0).isAscending();
			}
			resetSelezioni();
			if (ricercaListener != null) {
				ricercaListener.onStartRicerca(start, length, ColonnaWorklist.getFromId(campoOrdinamento), asc, ricercaCallback);
			} else {
				ricercaCallback.setRisultati(new ArrayList<PraticaDTO>(), 0, false);
			}
		}

	}

	private class RicercaCallbackImpl implements RicercaCallback {

		@Override
		public void setRisultati(List<PraticaDTO> list, int count, boolean estimate) {
			AsyncDataProvider<PraticaDTO> provider = AbstractWorklistStrategy.this.dataProvider;
			DataGridWidget<PraticaDTO> grid = AbstractWorklistStrategy.this.grid;

			provider.updateRowCount(count, !estimate);
			int start = grid.getVisibleRange().getStart();// +grid.getPageSize();
			provider.updateRowData(start, list);
		}

		@Override
		public void setRisultati(List<PraticaDTO> list) {
			AsyncDataProvider<PraticaDTO> provider = AbstractWorklistStrategy.this.dataProvider;
			DataGridWidget<PraticaDTO> grid = AbstractWorklistStrategy.this.grid;
			int start = grid.getVisibleRange().getStart();// +grid.getPageSize();
			provider.updateRowData(start, list);
		}

		@Override
		public void setCount(int count, boolean estimate) {
			AsyncDataProvider<PraticaDTO> provider = AbstractWorklistStrategy.this.dataProvider;
			provider.updateRowCount(count, !estimate);
		}

		@Override
		public void setNoResult() {
			AbstractWorklistStrategy.this.resetGrid();
		}

	}

}
