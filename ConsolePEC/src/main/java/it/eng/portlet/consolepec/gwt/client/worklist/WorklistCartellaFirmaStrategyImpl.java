package it.eng.portlet.consolepec.gwt.client.worklist;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.base.Strings;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SetSelectionModel;
import com.google.web.bindery.event.shared.EventBus;

import it.eng.portlet.consolepec.gwt.client.cell.StyledCheckboxCell;
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.widget.CustomButtonCell;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.ClickableCell;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.EmptyCell;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ColonnaWorklist;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioGruppoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioUtenteDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DocumentoFirmaVistoDTO;

/**
 *
 * @author biagiot
 *
 */
public class WorklistCartellaFirmaStrategyImpl implements WorklistCartellaFirmaStrategy {

	private RigaEspansaCartellaFirmaStrategy rigaEspansaStrategy;
	private final List<CheckRigaEventListener> checkRigaListeners = new ArrayList<CheckRigaEventListener>();
	private final List<EspandiRigaEventListener> espandiRigaListeners = new ArrayList<EspandiRigaEventListener>();
	private EventBus eventBus;
	private final Set<DocumentoFirmaVistoDTO> documentiSelezionati = new HashSet<DocumentoFirmaVistoDTO>();
	private DataGridWidget<DocumentoFirmaVistoDTO> grid;
	private final AsyncDataProvider<DocumentoFirmaVistoDTO> dataProvider = new DocumentoFirmaVistoDataProvider();
	private RicercaEventListener ricercaListener;
	private final RicercaCallback ricercaCallback = new RicercaCallbackImpl();
	private Command<Void, AllegatoDTO> downloadAllegatoCommand;
	private Command<Void, AllegatoDTO> dettaglioAllegatoCommand;
	private Command<Void, DocumentoFirmaVistoDTO> dettaglioFascicoloCommand;
	private Column<DocumentoFirmaVistoDTO, Boolean> selezColumn;
	private Column<DocumentoFirmaVistoDTO, SafeUri> dettaglioAllegatoColumn;
	private Column<DocumentoFirmaVistoDTO, SafeHtml> oggettoColumn;
	private Column<DocumentoFirmaVistoDTO, SafeHtml> proponenteColumn;
	private Column<DocumentoFirmaVistoDTO, SafeHtml> tipoRichiestaColumn;
	private Column<DocumentoFirmaVistoDTO, SafeHtml> destinatariColumn;
	private Column<DocumentoFirmaVistoDTO, SafeHtml> titoloFascicoloColumn;
	private Column<DocumentoFirmaVistoDTO, SafeHtml> dataColumn;
	private Column<DocumentoFirmaVistoDTO, SafeHtml> dataScadenzaColumn;
	private Column<DocumentoFirmaVistoDTO, String> buttonApriColumn;

	private CustomHeaderCheckbox customHeaderCheckbox = new CustomHeaderCheckbox();
	private int clientWidth;

	public WorklistCartellaFirmaStrategyImpl(int clientWidth) {
		this.clientWidth = clientWidth;
	}

	@Override
	public void setRigaEspansaStrategy(RigaEspansaCartellaFirmaStrategy rigaEspansaStrategy) {
		if (this.rigaEspansaStrategy != null) {
			throw new IllegalStateException("RigaEspansaCartellaFirmaStrategy già impostato");
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
	public void configuraGrid(final DataGridWidget<DocumentoFirmaVistoDTO> dataGrid, SimplePager pager) {
		if (grid != null) {
			throw new IllegalStateException("DataGrid per Cartella di Firma già impostata");
		}
		this.grid = dataGrid;

		configuraColonne();

		/* gestione dell'ordinamento */
		dataGrid.getColumnSortList().setLimit(1);// solo una entry di ricerca
		dataGrid.getColumnSortList().push(getDefaultSort());
		dataGrid.addColumnSortHandler(new ColumnSortEvent.Handler() {

			@Override
			public void onColumnSort(ColumnSortEvent event) {
				restartSearchDatiGrid();
			}
		});

		DefaultSelectionEventManager<DocumentoFirmaVistoDTO> selevtman = DefaultSelectionEventManager.createCheckboxManager(0);
		MultiSelectionModel<DocumentoFirmaVistoDTO> selectionModel = new MultiSelectionModel<DocumentoFirmaVistoDTO>(DOCUMENTO_FIRMA_VISTO_KEY_PROVIDER);
		selectionModel.addSelectionChangeHandler(customHeaderCheckbox);
		dataGrid.setSelectionModel(selectionModel, selevtman);
		pager.setDisplay(dataGrid);
		dataProvider.addDataDisplay(dataGrid);

	}

	@Override
	public void resetSelezioni() {
		SelectionModel<? super DocumentoFirmaVistoDTO> selModel = grid.getSelectionModel();
		if (selModel instanceof SetSelectionModel) {
			@SuppressWarnings("unchecked") SetSelectionModel<DocumentoFirmaVistoDTO> setSelModel = (SetSelectionModel<DocumentoFirmaVistoDTO>) selModel;
			setSelModel.clear();
		}

		documentiSelezionati.clear();
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
		dataProvider.updateRowData(0, new ArrayList<DocumentoFirmaVistoDTO>());
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
	public Set<DocumentoFirmaVistoDTO> getDocumentiFirmaVistoSelezionati() {
		Set<DocumentoFirmaVistoDTO> result = new HashSet<DocumentoFirmaVistoDTO>(documentiSelezionati);
		return result;
	}

	@Override
	public void aggiornaRiga(DocumentoFirmaVistoDTO documentoFirmaVistoDTO) {
		int index = grid.getVisibleItems().indexOf(documentoFirmaVistoDTO);
		List<DocumentoFirmaVistoDTO> list = new ArrayList<DocumentoFirmaVistoDTO>();
		list.add(documentoFirmaVistoDTO);
		boolean aggEspansa = grid.isRigaEspansaByKey(DocumentoFirmaVistoDTO.getKey(documentoFirmaVistoDTO));
		grid.updateRowData(index, list);
		if (aggEspansa) {
			espandiRiga(documentoFirmaVistoDTO);
		}
	}

	@Override
	public void espandiRiga(DocumentoFirmaVistoDTO documentoFirmaVistoDTO) {

		HTMLPanel dettaglioContent = new HTMLPanel("");
		HTMLPanel operations = new HTMLPanel("");

		rigaEspansaStrategy.disegnaDettaglio(dettaglioContent, documentoFirmaVistoDTO);
		rigaEspansaStrategy.disegnaOperazioni(operations, documentoFirmaVistoDTO,
				/*
				 * listener per lo strategy della riga espansa. ripassa l'evento ai listener registrati qua
				 */
				new EspandiRigaEventListener() {

					@Override
					public void onEspandiRiga(DocumentoFirmaVistoDTO documentoFirmaVistoDTO, boolean isEspansa) {
						notifyEspandiRigaEventListeners(documentoFirmaVistoDTO, isEspansa);
					}
				});
		/**
		 * all'espansione vogliamo mostrare il documento come letto TODO BT la persistenza sarà gestita dai presenter
		 */

		boolean letto = documentoFirmaVistoDTO.isLetto();
		documentoFirmaVistoDTO.setLetto(true);
		getGrid().espandiRiga(documentoFirmaVistoDTO, dettaglioContent, operations);
		documentoFirmaVistoDTO.setLetto(letto);
	}

	protected DataGridWidget<DocumentoFirmaVistoDTO> getGrid() {
		return grid;
	}

	@Override
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	private void configuraColonne() {
		configuraColonnaSelezione();
		configuraColonnaDettaglioAllegato();
		configuraColonnaOggetto();
		configuraColonnaProponente();
		configuraColonnaTipoRichiesta();
		configuraColonnaDestinatari();
		configuraColonnaTitoloFascicolo();
		configuraColonnaData();
		configuraColonnaDataScadenza();
		configuraColonnaButtonApri();

		String[] stiliColonne = { "check", //
				ColonnaWorklist.CARTELLA_FIRMA_DETTAGLIO_ALLEGATO.getId(), //
				ColonnaWorklist.CARTELLA_FIRMA_OGGETTO.getId(), //
				ColonnaWorklist.CARTELLA_FIRMA_PROPONENTE.getId(), //
				ColonnaWorklist.CARTELLA_FIRMA_TIPO_RICHIESTA.getId(), //
				ColonnaWorklist.CARTELLA_FIRMA_DESTINATARI.getId(), //
				ColonnaWorklist.CARTELLA_FIRMA_TITOLO_FASCICOLO.getId(), //
				ColonnaWorklist.CARTELLA_FIRMA_DATA_CREAZIONE.getId(), //
				ColonnaWorklist.CARTELLA_FIRMA_DATA_SCADENZA.getId(), //
				"button" };
		for (int i = 0; i < stiliColonne.length; i++) {
			this.grid.getHeader(i).setHeaderStyleNames(stiliColonne[i]);
		}
	}

	private void notifyCheckRigaListeners(DocumentoFirmaVistoDTO documentoFirmaVisto, boolean checked) {
		/* prima di notificare, aggiorno il numero totale di righe selezionate */
		if (checked) {
			if (!documentiSelezionati.contains(documentoFirmaVisto)) {
				documentiSelezionati.add(documentoFirmaVisto);
			}
		} else {
			if (documentiSelezionati.contains(documentoFirmaVisto)) {
				documentiSelezionati.remove(documentoFirmaVisto);
			}
		}

		/* notifico i listeners */
		for (CheckRigaEventListener listener : checkRigaListeners) {
			listener.onCheckRiga(documentoFirmaVisto, checked);
		}
	}

	private void notifyEspandiRigaEventListeners(DocumentoFirmaVistoDTO documentoFirmaVisto, boolean isEspansa) {
		for (EspandiRigaEventListener listener : espandiRigaListeners) {
			listener.onEspandiRiga(documentoFirmaVisto, isEspansa);
		}
	}

	private ColumnSortInfo getDefaultSort() {
		return new ColumnSortInfo(dataColumn, false);

	}

	/**
	 * COLONNE
	 */

	private Column<DocumentoFirmaVistoDTO, Boolean> configuraColonnaSelezione() {

		selezColumn = new Column<DocumentoFirmaVistoDTO, Boolean>(new StyledCheckboxCell()) {

			@Override
			public Boolean getValue(DocumentoFirmaVistoDTO object) {

				if (object.getAllegato() != null) {
					boolean res = getGrid().getSelectionModel().isSelected(object);
					notifyCheckRigaListeners(object, res);
					return res;

				} else {
					return false;
				}
			}
		};

		selezColumn.setCellStyleNames(ColonnaWorklist.SELEZIONE.getId());
		getGrid().addColumn(selezColumn, customHeaderCheckbox);
		return selezColumn;

	}

	private Column<DocumentoFirmaVistoDTO, SafeUri> configuraColonnaDettaglioAllegato() {

		final CustomSafeImageCell cellImg = new CustomSafeImageCell(true);

		dettaglioAllegatoColumn = new Column<DocumentoFirmaVistoDTO, SafeUri>(cellImg) {

			@Override
			public SafeUri getValue(DocumentoFirmaVistoDTO object) {

				if (object.getAllegato() != null) {
					cellImg.setTitle("Visualizza dettagli allegato");

					if (object.getAllegato().isFirmato() || object.getAllegato().isFirmatoHash()) {
						return ConsolePECIcons._instance.firmato().getSafeUri();
					} else {
						return ConsolePECIcons._instance.nonfirmato().getSafeUri();
					}

				} else {
					cellImg.setTitle("Allegato non disponibile");
					return ConsolePECIcons._instance.nonfirmato().getSafeUri();
				}
			}

			@Override
			public void onBrowserEvent(Context context, Element elem, DocumentoFirmaVistoDTO object, NativeEvent event) {

				if (object.getAllegato() != null) {
					event.preventDefault();

					if (event.getButton() == NativeEvent.BUTTON_LEFT) {
						dettaglioAllegatoCommand.exe(object.getAllegato());
					}
				}
			}
		};

		dettaglioAllegatoColumn.setSortable(false);
		dettaglioAllegatoColumn.setCellStyleNames(ColonnaWorklist.CARTELLA_FIRMA_DETTAGLIO_ALLEGATO.getId());
		dettaglioAllegatoColumn.setDataStoreName(ColonnaWorklist.CARTELLA_FIRMA_DETTAGLIO_ALLEGATO.getId());
		getGrid().addColumn(dettaglioAllegatoColumn, "");
		return dettaglioAllegatoColumn;
	}

	private Column<DocumentoFirmaVistoDTO, SafeHtml> configuraColonnaOggetto() {

		oggettoColumn = new Column<DocumentoFirmaVistoDTO, SafeHtml>(new ClickableCell()) {

			@Override
			public SafeHtml getValue(DocumentoFirmaVistoDTO object) {

				StringBuilder sb = new StringBuilder();

				String oggetto = null;
				String titleOggetto = null;
				String nomeAllegato = null;
				String titleAllegato = null;
				String htmlString = null;

				if (object.getAllegato() != null || !Strings.isNullOrEmpty(object.getAllegato().getOggetto())) {
					oggetto = checkLunghezzaTitolo(object.getAllegato().getOggetto(), "");
					titleOggetto = object.getAllegato().getOggetto();
					nomeAllegato = checkLunghezzaTitolo(object.getAllegato().getNome(), "");
					titleAllegato = object.getAllegato().getNome();

				} else {
					oggetto = "(Nessun oggetto)";
					titleOggetto = "(Nessun oggetto)";
					nomeAllegato = "Allegato non disponibile";
					titleAllegato = "Allegato non disponibile";
				}

				String tooltip = sb.append(titleOggetto).append(" (").append(titleAllegato).append(")").toString();

				if (clientWidth > ConsolePecConstants.MIN_DESKTOP_WIDTH_PIXELS) {
					htmlString = "<a href=\"javascript:;\" title=\"" + tooltip.replaceAll("\"", "'") + "\">" + oggetto + "</br>(" + nomeAllegato + ")</a>";

				} else {
					htmlString = "<a href=\"javascript:;\" title=\"" + tooltip.replaceAll("\"", "'") + "\">" + oggetto + " (" + nomeAllegato + ")</a>";

				}

				return SafeHtmlUtils.fromTrustedString(htmlString);
			}
		};

		oggettoColumn.setFieldUpdater(new FieldUpdater<DocumentoFirmaVistoDTO, SafeHtml>() {

			@Override
			public void update(int index, DocumentoFirmaVistoDTO object, SafeHtml value) {
				if (object.getAllegato() != null) {
					downloadAllegatoCommand.exe(object.getAllegato());
				}
			}
		});

		oggettoColumn.setSortable(false);
		oggettoColumn.setCellStyleNames(ColonnaWorklist.CARTELLA_FIRMA_OGGETTO.getId());
		oggettoColumn.setDataStoreName(ColonnaWorklist.CARTELLA_FIRMA_OGGETTO.getId());
		getGrid().addColumn(oggettoColumn, getIntestazioneColonnaOggetto());
		return oggettoColumn;
	}

	private Column<DocumentoFirmaVistoDTO, SafeHtml> configuraColonnaProponente() {

		proponenteColumn = new Column<DocumentoFirmaVistoDTO, SafeHtml>(new EmptyCell()) {

			@Override
			public SafeHtml getValue(DocumentoFirmaVistoDTO object) {

				StringBuilder htmlString = new StringBuilder();

				if (clientWidth > ConsolePecConstants.MIN_DESKTOP_WIDTH_PIXELS) {
					htmlString.append("<span>").append(object.getGruppoProponente().getNomeGruppo());

					if (!Strings.isNullOrEmpty(object.getMittenteOriginale())) {
						htmlString.append("\n").append("(").append(object.getMittenteOriginale()).append(")");
					}

					htmlString.append("</span>");

				} else {

					htmlString.append("<span class=\"etichetta\">Proponente</span>").append("<span class=\"valore-etichetta\">").append(object.getGruppoProponente().getNomeGruppo()).append("</span>");

					if (!Strings.isNullOrEmpty(object.getMittenteOriginale())) {
						htmlString.append("<span class=\"etichetta\">Mittente originale</span>").append("<span class=\"valore-etichetta\">").append(" (").append(object.getMittenteOriginale()) //
								.append(")").append("</span>");
					}
				}

				return SafeHtmlUtils.fromTrustedString(htmlString.toString());
			}
		};

		proponenteColumn.setSortable(false);
		proponenteColumn.setCellStyleNames(ColonnaWorklist.CARTELLA_FIRMA_PROPONENTE.getId());
		proponenteColumn.setDataStoreName(ColonnaWorklist.CARTELLA_FIRMA_PROPONENTE.getId());
		getGrid().addColumn(proponenteColumn, getIntestazioneColonnaProponente());
		return proponenteColumn;
	}

	private Column<DocumentoFirmaVistoDTO, SafeHtml> configuraColonnaTipoRichiesta() {

		tipoRichiestaColumn = new Column<DocumentoFirmaVistoDTO, SafeHtml>(new EmptyCell()) {

			@Override
			public SafeHtml getValue(DocumentoFirmaVistoDTO object) {

				StringBuilder htmlString = new StringBuilder();

				if (clientWidth > ConsolePecConstants.MIN_DESKTOP_WIDTH_PIXELS) {
					htmlString.append(object.getTipoRichiesta().getLabel());

				} else {
					htmlString.append("<span class=\"etichetta\">Tipo</span>").append("<span class=\"valore-etichetta\">").append(object.getTipoRichiesta().getLabel()).append("</span>");
				}

				return SafeHtmlUtils.fromTrustedString(htmlString.toString());
			}
		};

		tipoRichiestaColumn.setSortable(false);
		tipoRichiestaColumn.setCellStyleNames(ColonnaWorklist.CARTELLA_FIRMA_TIPO_RICHIESTA.getId());
		tipoRichiestaColumn.setDataStoreName(ColonnaWorklist.CARTELLA_FIRMA_TIPO_RICHIESTA.getId());
		getGrid().addColumn(tipoRichiestaColumn, getIntestazioneColonnaTipoRichiesta());
		return tipoRichiestaColumn;
	}

	private Column<DocumentoFirmaVistoDTO, SafeHtml> configuraColonnaDataScadenza() {
		dataScadenzaColumn = new Column<DocumentoFirmaVistoDTO, SafeHtml>(new EmptyCell()) {
			@Override
			public SafeHtml getValue(DocumentoFirmaVistoDTO object) {

				StringBuilder htmlString = new StringBuilder();

				if (clientWidth > ConsolePecConstants.MIN_DESKTOP_WIDTH_PIXELS) {
					htmlString.append(object.getDataScadenza() != null ? object.getDataScadenza() : "");

				} else {
					htmlString.append("<span class=\"etichetta\">Data scadenza</span>").append("<span class=\"valore-etichetta\">") //
							.append(object.getDataScadenza() != null ? object.getDataScadenza() : "-").append("</span>");
				}

				return SafeHtmlUtils.fromTrustedString(htmlString.toString());
			}
		};

		dataScadenzaColumn.setSortable(true);
		dataScadenzaColumn.setCellStyleNames(ColonnaWorklist.CARTELLA_FIRMA_DATA_SCADENZA.getId());
		dataScadenzaColumn.setDataStoreName(ColonnaWorklist.CARTELLA_FIRMA_DATA_SCADENZA.getId());
		getGrid().addColumn(dataScadenzaColumn, getIntestazioneColonnaDataScadenza());
		return dataScadenzaColumn;
	}

	private Column<DocumentoFirmaVistoDTO, SafeHtml> configuraColonnaData() {

		dataColumn = new Column<DocumentoFirmaVistoDTO, SafeHtml>(new EmptyCell()) {
			@Override
			public SafeHtml getValue(DocumentoFirmaVistoDTO object) {

				StringBuilder htmlString = new StringBuilder();

				if (clientWidth > ConsolePecConstants.MIN_DESKTOP_WIDTH_PIXELS) {
					htmlString.append(object.getDataProposta());

				} else {
					htmlString.append("<span class=\"etichetta\">Data proposta</span>").append("<span class=\"valore-etichetta\">").append(object.getDataProposta()).append("</span>");
				}

				return SafeHtmlUtils.fromTrustedString(htmlString.toString());
			}
		};

		dataColumn.setSortable(true);
		dataColumn.setCellStyleNames(ColonnaWorklist.CARTELLA_FIRMA_DATA_CREAZIONE.getId());
		dataColumn.setDataStoreName(ColonnaWorklist.CARTELLA_FIRMA_DATA_CREAZIONE.getId());
		getGrid().addColumn(dataColumn, getIntestazioneColonnaData());
		return dataColumn;
	}

	private Column<DocumentoFirmaVistoDTO, SafeHtml> configuraColonnaDestinatari() {

		destinatariColumn = new Column<DocumentoFirmaVistoDTO, SafeHtml>(new ClickableCell()) {

			@Override
			public SafeHtml getValue(DocumentoFirmaVistoDTO object) {

				Set<DestinatarioDTO> destinatari = object.getDestinatariFirma();
				SafeHtmlBuilder sb = new SafeHtmlBuilder();

				if (clientWidth > ConsolePecConstants.MIN_DESKTOP_WIDTH_PIXELS) {
					sb.appendHtmlConstant("<a href=\"javascript:;\">");
					if (destinatari.size() <= MAX_DESTINATARI_IN_WORKLIST) {
						for (DestinatarioDTO destinatario : destinatari) {
							sb.appendEscaped(getNomeDestinatario(destinatario));
							sb.appendHtmlConstant("</br>");
						}

					} else {
						Iterator<DestinatarioDTO> iterator = destinatari.iterator();
						int i = 0;
						while (iterator.hasNext() && i <= MAX_DESTINATARI_IN_WORKLIST) {
							sb.appendEscaped(getNomeDestinatario(iterator.next()));
							sb.appendHtmlConstant("</br>");
						}

						sb.appendEscaped("...");
					}

				} else {
					sb.appendHtmlConstant("<span class=\"etichetta\">Destinatari </span>");
					sb.appendHtmlConstant("<a class=\"valore-etichetta\" href=\"javascript:;\">");
					sb.appendHtmlConstant("<ul>");
					for (DestinatarioDTO destinatario : destinatari) {
						sb.appendHtmlConstant("<li>");
						sb.appendEscaped(getNomeDestinatario(destinatario));
						sb.appendEscaped(" - (" + destinatario.getStatoRichiesta().getLabel() + ")");
						sb.appendHtmlConstant("</br>");
						sb.appendHtmlConstant("</li>");
					}

					sb.appendHtmlConstant("</ul>");
				}

				sb.appendHtmlConstant("</a>");
				return sb.toSafeHtml();
			}
		};

		destinatariColumn.setFieldUpdater(new FieldUpdater<DocumentoFirmaVistoDTO, SafeHtml>() {

			@Override
			public void update(int index, DocumentoFirmaVistoDTO object, SafeHtml value) {
				boolean isEspansa = getGrid().isRigaEspansaByKey(DocumentoFirmaVistoDTO.getKey(object));
				notifyEspandiRigaEventListeners(object, isEspansa);
			}
		});

		destinatariColumn.setSortable(false);
		destinatariColumn.setCellStyleNames(ColonnaWorklist.CARTELLA_FIRMA_DESTINATARI.getId());
		destinatariColumn.setDataStoreName(ColonnaWorklist.CARTELLA_FIRMA_DESTINATARI.getId());
		getGrid().addColumn(destinatariColumn, getIntestazioneColonnaDestinatari());
		return destinatariColumn;
	}

	public static String getNomeDestinatario(DestinatarioDTO destinatario) {
		if (destinatario instanceof DestinatarioUtenteDTO) {
			return ((DestinatarioUtenteDTO) destinatario).getNomeCompleto();

		} else if (destinatario instanceof DestinatarioGruppoDTO) {
			return ((DestinatarioGruppoDTO) destinatario).getNomeGruppoDisplay();

		} else {
			return "";
		}
	}

	private Column<DocumentoFirmaVistoDTO, SafeHtml> configuraColonnaTitoloFascicolo() {

		titoloFascicoloColumn = new Column<DocumentoFirmaVistoDTO, SafeHtml>(new EmptyCell()) {

			@Override
			public SafeHtml getValue(DocumentoFirmaVistoDTO object) {

				String tooltip = object.getTitoloFascicolo();
				StringBuilder htmlString = new StringBuilder();

				if (clientWidth > ConsolePecConstants.MIN_DESKTOP_WIDTH_PIXELS) {

					htmlString.append("<span title=\"" + tooltip.replaceAll("\"", "'") + "\" >" + checkLunghezzaTitolo(object.getTitoloFascicolo(), "") + "</span>");

				} else {
					htmlString.append("<span title=\"" + tooltip.replaceAll("\"", "'") + "\" class=\"etichetta\">Titolo fascicolo</span>").append("<span class=\"valore-etichetta\">") //
							.append(checkLunghezzaTitolo(object.getTitoloFascicolo(), "")).append("</span>");
				}

				return SafeHtmlUtils.fromTrustedString(htmlString.toString());
			}

		};

		titoloFascicoloColumn.setCellStyleNames(ColonnaWorklist.CARTELLA_FIRMA_TITOLO_FASCICOLO.getId());
		titoloFascicoloColumn.setDataStoreName(ColonnaWorklist.CARTELLA_FIRMA_TITOLO_FASCICOLO.getId());
		titoloFascicoloColumn.setSortable(true);
		getGrid().addColumn(titoloFascicoloColumn, getIntestazioneColonnaTitoloFascicolo());
		return titoloFascicoloColumn;
	}

	private Column<DocumentoFirmaVistoDTO, String> configuraColonnaButtonApri() {
		CustomButtonCell customButtonCell = new CustomButtonCell("btn worklist_btn open-details");

		buttonApriColumn = new Column<DocumentoFirmaVistoDTO, String>(customButtonCell) {

			@Override
			public String getValue(DocumentoFirmaVistoDTO object) {
				return "Apri";
			}

			@Override
			public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context, Element elem, DocumentoFirmaVistoDTO object, NativeEvent event) {
				event.preventDefault();
				if (event.getButton() == NativeEvent.BUTTON_LEFT) {
					dettaglioFascicoloCommand.exe(object);
				}
			}
		};

		buttonApriColumn.setSortable(false);
		buttonApriColumn.setCellStyleNames(ColonnaWorklist.CARTELLA_FIRMA_APRI.getId() + " button-cartella-attivita");
		getGrid().addColumn(buttonApriColumn, "");
		return buttonApriColumn;
	}

	private class CustomHeaderCheckbox extends Header<Boolean> implements Handler {

		public CustomHeaderCheckbox() {
			super(new StyledCheckboxCell("header"));
		}

		@Override
		public Boolean getValue() {
			Boolean isChecked = getGrid().getVisibleItems().size() > 0;
			for (DocumentoFirmaVistoDTO v : getGrid().getVisibleItems()) {
				isChecked &= getGrid().getSelectionModel().isSelected(v);
			}
			return isChecked;
		}

		@Override
		public void onBrowserEvent(final Context context, final Element parent, NativeEvent event) {
			super.onBrowserEvent(context, parent, event);
			if (BrowserEvents.CHANGE.equals(event.getType())) {
				Boolean toCheck = !getValue();
				impostaSelezioneVisibili(toCheck);
				getCell().setValue(context, parent, toCheck);
			}
		}

		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			getValue();
		}

	}

	protected void impostaSelezioneVisibili(Boolean checked) {
		SelectionModel<? super DocumentoFirmaVistoDTO> selectionModel = getGrid().getSelectionModel();
		for (DocumentoFirmaVistoDTO d : getGrid().getVisibleItems()) {
			selectionModel.setSelected(d, checked);
			notifyCheckRigaListeners(d, checked);
		}
	}

	private String getIntestazioneColonnaOggetto() {
		return "Oggetto Proposta";
	}

	private String getIntestazioneColonnaProponente() {
		return new StringBuilder("Proponente").append("\n").append("(Mittente Originale)").toString();
	}

	private String getIntestazioneColonnaTipoRichiesta() {
		return "Tipo Proposta";
	}

	private String getIntestazioneColonnaDestinatari() {
		return "Destinatari";
	}

	private String getIntestazioneColonnaTitoloFascicolo() {
		return "Titolo Fascicolo";
	}

	private String getIntestazioneColonnaData() {
		return "Data Proposta";
	}

	private String getIntestazioneColonnaDataScadenza() {
		return "Data Scadenza Proposta";
	}

	private String checkLunghezzaTitolo(String text, String placeholder) {
		String titolo = text == null || text.trim().isEmpty() ? placeholder : text;

		if (titolo.length() > ConsolePecConstants.MAX_LENGTH_TITOLO) {
			StringBuilder sb = new StringBuilder(titolo);
			titolo = sb.delete(ConsolePecConstants.MAX_LENGTH_TITOLO - 3, titolo.length()).append("...").toString();
		}

		return titolo;
	}

	private class DocumentoFirmaVistoDataProvider extends AsyncDataProvider<DocumentoFirmaVistoDTO> {

		@Override
		protected void onRangeChanged(HasData<DocumentoFirmaVistoDTO> display) {
			int start = display.getVisibleRange().getStart();
			int length = display.getVisibleRange().getLength();
			String campoOrdinamento = null;
			boolean asc = false;
			if (grid.getColumnSortList().size() > 0) {
				campoOrdinamento = grid.getColumnSortList().get(0).getColumn().getDataStoreName();
				asc = grid.getColumnSortList().get(0).isAscending();
			}
			// resetSelezioni();
			if (ricercaListener != null) {
				ricercaListener.onStartRicerca(start, length, ColonnaWorklist.getFromId(campoOrdinamento), asc, ricercaCallback);
			} else {
				ricercaCallback.setRisultati(new ArrayList<DocumentoFirmaVistoDTO>(), 0, false);
			}
		}
	}

	private class RicercaCallbackImpl implements RicercaCallback {

		@Override
		public void setRisultati(List<DocumentoFirmaVistoDTO> list, int count, boolean estimate) {
			AsyncDataProvider<DocumentoFirmaVistoDTO> provider = WorklistCartellaFirmaStrategyImpl.this.dataProvider;
			DataGridWidget<DocumentoFirmaVistoDTO> grid = WorklistCartellaFirmaStrategyImpl.this.grid;

			provider.updateRowCount(count, !estimate);
			int start = grid.getVisibleRange().getStart();// +grid.getPageSize();
			provider.updateRowData(start, list);
		}

		@Override
		public void setRisultati(List<DocumentoFirmaVistoDTO> list) {
			AsyncDataProvider<DocumentoFirmaVistoDTO> provider = WorklistCartellaFirmaStrategyImpl.this.dataProvider;
			DataGridWidget<DocumentoFirmaVistoDTO> grid = WorklistCartellaFirmaStrategyImpl.this.grid;
			int start = grid.getVisibleRange().getStart();// +grid.getPageSize();
			provider.updateRowData(start, list);
		}

		@Override
		public void setCount(int count, boolean estimate) {
			AsyncDataProvider<DocumentoFirmaVistoDTO> provider = WorklistCartellaFirmaStrategyImpl.this.dataProvider;
			provider.updateRowCount(count, !estimate);
		}

		@Override
		public void setNoResult() {
			WorklistCartellaFirmaStrategyImpl.this.resetGrid();
		}
	}

	@Override
	public void setDownloadAllegatoCommand(Command<Void, AllegatoDTO> downloadAllegatoCommand) {
		this.downloadAllegatoCommand = downloadAllegatoCommand;
	}

	@Override
	public void setDettaglioAllegatoCommand(Command<Void, AllegatoDTO> dettaglioAllegatoCommand) {
		this.dettaglioAllegatoCommand = dettaglioAllegatoCommand;
	}

	@Override
	public void setDettaglioFascicoloCommand(Command<Void, DocumentoFirmaVistoDTO> dettaglioFascicoloCommand) {
		this.dettaglioFascicoloCommand = dettaglioFascicoloCommand;
	}

}
