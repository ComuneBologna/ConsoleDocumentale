package it.eng.portlet.consolepec.gwt.client.widget.configurazioni;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.MostraCondividiFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.event.ModificaVisibilitaFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.widget.SimpleListaCampiWidget;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.ModificaVisibilitaFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.ModificaVisibilitaFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaMatriceVisibilitaPraticaAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaMatriceVisibilitaPraticaResult;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CondivisioneFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CondivisioneFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo.TipoWidget;
import it.eng.portlet.consolepec.gwt.shared.dto.CollegamentoDto;
import it.eng.portlet.consolepec.gwt.shared.dto.CondivisioneDto;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.GruppoVisibilita;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 * @author biagiot
 *
 */
public class AutorizzazioniFascicoloWidget extends Composite {

	interface AutorizzazioniFascicoloWidgetBinder extends UiBinder<Widget, AutorizzazioniFascicoloWidget> {}

	private static AutorizzazioniFascicoloWidgetBinder uiBinder = GWT.create(AutorizzazioniFascicoloWidgetBinder.class);

	@UiField
	HTMLPanel autorizzazioniFascPanel;

	@UiField
	Button aggiungiCondivisione;

	@UiField
	Button estendiVisibilita;

	private ProfilazioneUtenteHandler profilazioneUtenteHandler;
	private ConfigurazioniHandler configurazioniHandler;
	private FascicoloDTO fascicolo;
	private AutorizzazioniFascicoloCampiWidget widget;
	private EventBus eventBus;
	private DispatchAsync dispatcher;
	private PecInPraticheDB praticheDB;

	public AutorizzazioniFascicoloWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public AutorizzazioniFascicoloWidget(ProfilazioneUtenteHandler profilazioneUtenteHandler, ConfigurazioniHandler configurazioniHandler, EventBus eventBus, DispatchAsync dispatcher,
			PecInPraticheDB praticheDB) {
		this();
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		this.configurazioniHandler = configurazioniHandler;
		this.eventBus = eventBus;
		this.dispatcher = dispatcher;
		this.praticheDB = praticheDB;

		estendiVisibilita.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				aggiungiVisibilita();
			}
		});

		aggiungiCondivisione.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				aggiungiCondivisione();
			}
		});
	}

	public void clear() {
		if (widget != null) {
			widget.getValori().clear();
		}

		this.autorizzazioniFascPanel.clear();
	}

	public void render(final FascicoloDTO fascicolo) {
		clear();
		this.fascicolo = fascicolo;

		this.aggiungiCondivisione.setEnabled(fascicolo.isCondivisioneAbilitata());
		this.estendiVisibilita.setEnabled(fascicolo.isModificaVisibilitaFascicoloAbilitato());

		final List<Autorizzazione> autorizzazioni = new ArrayList<>();

		if (PraticaUtil.isFascicoloPersonale(fascicolo.getTipologiaPratica())) {
			/*
			 * Assegnatario
			 */
			if (fascicolo.getAssegnatario() != null) {
				autorizzazioni.add(new Autorizzazione(TipoAutorizzazione.MODIFICA, MotivoAutorizzazione.ASSEGNATARIO, fascicolo.getAssegnatario()));
			}

			/*
			 * Visibilità
			 */
			if (fascicolo.getVisibilita() != null) {
				for (GruppoVisibilita gv : fascicolo.getVisibilita()) {
					autorizzazioni.add(new Autorizzazione(TipoAutorizzazione.LETTURA, MotivoAutorizzazione.VISIBILITA, gv.getLabel()));
				}
			}

			/*
			 * Creazione widget
			 */
			widget = new AutorizzazioniFascicoloCampiWidget(autorizzazioni.size() + 20);
			widget.render();
			for (Autorizzazione a : autorizzazioni) {
				widget.add(a);
			}

			autorizzazioniFascPanel.add(widget);

		} else {
			CaricaMatriceVisibilitaPraticaAction action = new CaricaMatriceVisibilitaPraticaAction(fascicolo.getTipologiaPratica());

			dispatcher.execute(action, new AsyncCallback<CaricaMatriceVisibilitaPraticaResult>() {

				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(AutorizzazioniFascicoloWidget.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);
				}

				@Override
				public void onSuccess(CaricaMatriceVisibilitaPraticaResult result) {
					if (!result.isError()) {
						/*
						 * Assegnatario
						 */
						if (fascicolo.getAssegnatario() != null) {
							autorizzazioni.add(new Autorizzazione(TipoAutorizzazione.MODIFICA, MotivoAutorizzazione.ASSEGNATARIO, fascicolo.getAssegnatario()));
						}

						/*
						 * Visibilità
						 */
						if (fascicolo.getVisibilita() != null) {
							for (GruppoVisibilita gv : fascicolo.getVisibilita()) {
								autorizzazioni.add(new Autorizzazione(TipoAutorizzazione.LETTURA, MotivoAutorizzazione.VISIBILITA, gv.getLabel()));
							}
						}

						/*
						 * Condivisioni
						 */
						if (fascicolo.getCondivisioni() != null) {
							for (CondivisioneDto c : fascicolo.getCondivisioni()) {
								autorizzazioni.add(new Autorizzazione(TipoAutorizzazione.CONDIVISIONE, MotivoAutorizzazione.CONDIVISO, c.getRuolo().getEtichetta()));
							}
						}

						/*
						 * Superutenti
						 */
						List<AnagraficaRuolo> superutenti = profilazioneUtenteHandler.getAnagraficheRuoliSuperutenti(
								configurazioniHandler.getAnagraficaRuoloByEtichetta(fascicolo.getAssegnatario()).getRuolo());
						Collections.sort(superutenti, new Comparator<AnagraficaRuolo>() {

							@Override
							public int compare(AnagraficaRuolo o1, AnagraficaRuolo o2) {
								return o1.getEtichetta().compareToIgnoreCase(o2.getEtichetta());
							}

						});
						for (AnagraficaRuolo ar : superutenti) {
							autorizzazioni.add(new Autorizzazione(TipoAutorizzazione.MODIFICA, MotivoAutorizzazione.SUPERVISORE, ar.getEtichetta()));
						}

						/*
						 * Matr. visibilità Ruoli
						 */
						List<AnagraficaRuolo> matrVis1 = profilazioneUtenteHandler.getAnagraficheRuoliSuperutentiMatriceVisibilita(
								configurazioniHandler.getAnagraficaRuoloByEtichetta(fascicolo.getAssegnatario()).getRuolo());
						Collections.sort(matrVis1, new Comparator<AnagraficaRuolo>() {

							@Override
							public int compare(AnagraficaRuolo o1, AnagraficaRuolo o2) {
								return o1.getEtichetta().compareToIgnoreCase(o2.getEtichetta());
							}

						});
						for (AnagraficaRuolo ar : matrVis1) {
							autorizzazioni.add(new Autorizzazione(TipoAutorizzazione.LETTURA, MotivoAutorizzazione.MATRICE_VISIBILITA_RUOLO, ar.getEtichetta()));
						}

						/*
						 * Matr. visibilità Tipo fascicolo
						 */
						if (result.getRuoli() != null) {
							for (AnagraficaRuolo ar : result.getRuoli()) {
								autorizzazioni.add(new Autorizzazione(TipoAutorizzazione.LETTURA, MotivoAutorizzazione.MATRICE_VISIBILITA_FASCICOLO, ar.getEtichetta()));
							}
						}

						/*
						 * Creazione widget
						 */
						widget = new AutorizzazioniFascicoloCampiWidget(autorizzazioni.size() + 20);
						widget.render();
						for (Autorizzazione a : autorizzazioni) {
							widget.add(a);
						}

						autorizzazioniFascPanel.add(widget);

					} else {
						ShowAppLoadingEvent.fire(AutorizzazioniFascicoloWidget.this, false);
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						eventBus.fireEvent(event);
					}
				}

			});
		}
	}

	public class AutorizzazioniFascicoloCampiWidget extends SimpleListaCampiWidget<Autorizzazione> {

		public AutorizzazioniFascicoloCampiWidget(Integer limit) {
			super(limit);
		}

		@Override
		protected Autorizzazione converti(Object[] riga) {
			return AutorizzazioniFascicoloWidget.converti(riga);
		}

		@Override
		protected Object[] converti(Autorizzazione riga) {
			return new Object[] { riga.getMotivoAutorizzazione().getDescrizione(), riga.getEtichettaGruppo(), riga.getTipoAutorizzazione().getDescrizione() };
		}

		@Override
		protected void configuraColonneCampi() {
			creaCampoColonna("descrMotivoAutorizzazioneFascicolo", "Autorizzazione", TipoWidget.TEXTBOX, 0);
			creaCampoColonna("gruppoAutorizzazioneFascicolo", "Gruppo", TipoWidget.TEXTBOX, 1);
			creaCampoColonna("descrTipoAutorizzazioneFascicolo", "Tipo", TipoWidget.TEXTBOX, 2);
		}

		@Override
		protected void configuraColonneBottoni() {
			Column<Object[], String> colonnaElimina = configuraColonnaButtonElimina();
			colonnaElimina.setFieldUpdater(new FieldUpdater<Object[], String>() {

				@Override
				public void update(int index, Object[] object, String value) {
					Autorizzazione a = AutorizzazioniFascicoloWidget.converti(object);
					eliminaAutorizzazione(a);
				}

			});

			creaColonnaBottone(colonnaElimina);
		}
	}

	private static Autorizzazione converti(Object[] riga) {
		return new Autorizzazione(TipoAutorizzazione.fromDescrizione((String) riga[2]), MotivoAutorizzazione.fromDescrizione((String) riga[0]), (String) riga[1]);
	}

	private void eliminaCondivisione(final Autorizzazione autorizzazione) {

		if (fascicolo.isCondivisioneAbilitata() && MotivoAutorizzazione.CONDIVISO.equals(autorizzazione.getMotivoAutorizzazione())) {

			CondivisioneFascicolo action = new CondivisioneFascicolo(CondivisioneFascicolo.SHARE_DELETE, autorizzazione.getEtichettaGruppo(), fascicolo.getClientID(), new ArrayList<String>());

			ShowAppLoadingEvent.fire(AutorizzazioniFascicoloWidget.this, true);
			dispatcher.execute(action, new AsyncCallback<CondivisioneFascicoloResult>() {

				@Override
				public void onFailure(Throwable arg0) {
					ShowAppLoadingEvent.fire(AutorizzazioniFascicoloWidget.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);
				}

				@Override
				public void onSuccess(CondivisioneFascicoloResult result) {
					ShowAppLoadingEvent.fire(AutorizzazioniFascicoloWidget.this, false);
					if (result.isError()) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(result.getErrorMsg());
						eventBus.fireEvent(event);

					} else {
						praticheDB.remove(fascicolo.getClientID());

						praticheDB.getFascicoloByPath(fascicolo.getClientID(), true, new PraticaFascicoloLoaded() {

							@Override
							public void onPraticaLoaded(FascicoloDTO pec) {
								if (widget.getValori().contains(autorizzazione)) {
									widget.getValori().remove(autorizzazione);
								}

								render(pec);
							}

							@Override
							public void onPraticaError(String error) {
								ShowMessageEvent event = new ShowMessageEvent();
								event.setErrorMessage(error);
								eventBus.fireEvent(event);
							}
						});
					}
				}
			});

		} else {
			throw new IllegalArgumentException("Autorizzazione non valida");
		}
	}

	private void eliminaAutorizzazione(Autorizzazione autorizzazione) {
		if (MotivoAutorizzazione.VISIBILITA.equals(autorizzazione.getMotivoAutorizzazione())) {
			eliminaVisibilita(autorizzazione);

		} else if (MotivoAutorizzazione.CONDIVISO.equals(autorizzazione.getMotivoAutorizzazione())) {
			eliminaCondivisione(autorizzazione);

		} else {
			throw new IllegalArgumentException("Autorizzazione non valida");
		}
	}

	private void eliminaVisibilita(final Autorizzazione autorizzazione) {

		TreeSet<GruppoVisibilita> gruppi = new TreeSet<>();
		for (GruppoVisibilita gv : fascicolo.getVisibilita()) {
			if (!gv.getLabel().equals(autorizzazione.getEtichettaGruppo())) {
				gruppi.add(gv);
			}
		}

		if (fascicolo.isModificaVisibilitaFascicoloAbilitato() && MotivoAutorizzazione.VISIBILITA.equals(autorizzazione.getMotivoAutorizzazione())) {

			ModificaVisibilitaFascicolo action = new ModificaVisibilitaFascicolo();
			action.setClientID(fascicolo.getClientID());
			action.setGruppiVisibilita(gruppi);

			ShowAppLoadingEvent.fire(AutorizzazioniFascicoloWidget.this, true);
			dispatcher.execute(action, new AsyncCallback<ModificaVisibilitaFascicoloResult>() {

				@Override
				public void onFailure(Throwable arg0) {
					ShowAppLoadingEvent.fire(AutorizzazioniFascicoloWidget.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);
				}

				@Override
				public void onSuccess(ModificaVisibilitaFascicoloResult result) {
					ShowAppLoadingEvent.fire(AutorizzazioniFascicoloWidget.this, false);
					if (result.isError()) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(result.getErrorMessage());
						eventBus.fireEvent(event);

					} else {
						if (widget.getValori().contains(autorizzazione)) {
							widget.getValori().remove(autorizzazione);
						}

						praticheDB.remove(fascicolo.getClientID());
						fascicolo = result.getFascicoloDTO();
						render(fascicolo);
					}
				}
			});

		} else {
			throw new IllegalArgumentException("Autorizzazione non valida");
		}

	}

	private void aggiungiVisibilita() {
		if (fascicolo.isModificaVisibilitaFascicoloAbilitato()) {
			eventBus.fireEvent(new ModificaVisibilitaFascicoloEvent(fascicolo));
		}
	}

	private void aggiungiCondivisione() {
		if (fascicolo.isCondivisioneAbilitata()) {
			eventBus.fireEvent(new MostraCondividiFascicoloEvent(fascicolo.getClientID()));
		}
	}

	@Data
	@EqualsAndHashCode
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	private static class Autorizzazione {
		private TipoAutorizzazione tipoAutorizzazione;
		private MotivoAutorizzazione motivoAutorizzazione;
		private String etichettaGruppo;
	}

	private static enum TipoAutorizzazione {
		LETTURA("Lettura"), MODIFICA("Modifica"), CONDIVISIONE("Condivisione");

		@Getter
		private String descrizione;

		TipoAutorizzazione(String descrizione) {
			this.descrizione = descrizione;
		}

		static TipoAutorizzazione fromDescrizione(String descrizione) {
			for (TipoAutorizzazione ta : TipoAutorizzazione.values()) {
				if (ta.getDescrizione().equals(descrizione)) {
					return ta;
				}
			}

			return null;
		}
	}

	private static enum MotivoAutorizzazione {
		ASSEGNATARIO("Assegnatario"), SUPERVISORE("Supervisore"), MATRICE_VISIBILITA_FASCICOLO("Matrice di visibilità per fascicolo"), MATRICE_VISIBILITA_RUOLO("Matrice di visibilità per gruppo"),
		VISIBILITA("Visibilità"), CONDIVISO("Condivisione");

		@Getter
		private String descrizione;

		MotivoAutorizzazione(String descrizione) {
			this.descrizione = descrizione;
		}

		static MotivoAutorizzazione fromDescrizione(String descrizione) {
			for (MotivoAutorizzazione ta : MotivoAutorizzazione.values()) {
				if (ta.getDescrizione().equals(descrizione)) {
					return ta;
				}
			}

			return null;
		}
	}

	private class EliminaAutorizzazioneCell extends ButtonCell {
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

		public void setAbilitato(boolean abilitato) {
			this.abilitato = abilitato;
		}
	}

	private Column<Object[], String> configuraColonnaButtonElimina() {
		final EliminaAutorizzazioneCell eliminaAutorizzazioneCell = new EliminaAutorizzazioneCell();
		Column<Object[], String> colonna = new Column<Object[], String>(eliminaAutorizzazioneCell) {

			@Override
			public String getValue(Object[] object) {

				Autorizzazione aut = AutorizzazioniFascicoloWidget.converti(object);

				boolean collegamentiAttivi = false;

				for (CollegamentoDto collegamento : fascicolo.getCollegamenti()) {
					if (collegamento.getDisplayNameGruppo().equals(aut.getEtichettaGruppo())) {
						collegamentiAttivi = true;
						break;
					}
				}

				boolean checkCollegamentiAttivi = MotivoAutorizzazione.CONDIVISO.equals(aut.getMotivoAutorizzazione()) && collegamentiAttivi;
				boolean checkAssegnatario = !fascicolo.getAssegnatario().equals(aut.getEtichettaGruppo());

				boolean enabled = (fascicolo.isModificaVisibilitaFascicoloAbilitato() && MotivoAutorizzazione.VISIBILITA.equals(aut.getMotivoAutorizzazione()))
						|| (fascicolo.isCondivisioneAbilitata() && MotivoAutorizzazione.CONDIVISO.equals(aut.getMotivoAutorizzazione()));

				eliminaAutorizzazioneCell.setAbilitato(enabled && checkAssegnatario && !checkCollegamentiAttivi);
				return "Elimina";
			}
		};

		return colonna;
	}
}
