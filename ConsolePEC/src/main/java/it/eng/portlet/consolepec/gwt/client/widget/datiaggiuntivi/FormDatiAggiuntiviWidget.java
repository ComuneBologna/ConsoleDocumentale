package it.eng.portlet.consolepec.gwt.client.widget.datiaggiuntivi;

import static it.eng.cobo.consolepec.util.datiaggiuntivi.DatiAggiuntiviUtil.calcolaNomeAnagrafica;
import static it.eng.cobo.consolepec.util.datiaggiuntivi.DatiAggiuntiviUtil.getIdAnagrafica;
import static it.eng.cobo.consolepec.util.datiaggiuntivi.DatiAggiuntiviUtil.getValore;
import static it.eng.cobo.consolepec.util.datiaggiuntivi.DatiAggiuntiviUtil.getValori;
import static it.eng.cobo.consolepec.util.datiaggiuntivi.DatiAggiuntiviUtil.getValoriPredefiniti;
import static it.eng.cobo.consolepec.util.datiaggiuntivi.DatiAggiuntiviUtil.isEditabile;
import static it.eng.cobo.consolepec.util.datiaggiuntivi.DatiAggiuntiviUtil.isObbligatorio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.FieldSetElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo.ConfigurazioneEsecuzione;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo.DatoAggiuntivoVisitor;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoAnagrafica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoTabella;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreMultiplo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreSingolo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.TipoDato;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.CondizioneEsecuzione.RibaltaDatiAggiuntiviDaAnagraficaCondizioneEsecuzione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.ConseguenzaEsecuzione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.ConseguenzaEsecuzione.RibaltaDatiAggiuntiviDaAnagraficaConseguenzaEsecuzione;
import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import it.eng.cobo.consolepec.commons.rubrica.Indirizzo;
import it.eng.cobo.consolepec.commons.rubrica.PersonaFisica;
import it.eng.cobo.consolepec.commons.rubrica.Telefono;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.cobo.consolepec.util.objects.Ref;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.SalvaFascicoloCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.rubrica.command.SelezionaAnagraficaCommand;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecUtils;
import it.eng.portlet.consolepec.gwt.shared.model.ValidazioneDatoAggiuntivoDTO;

public class FormDatiAggiuntiviWidget extends Composite {

	private static DateTimeFormat dateFormat = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATA);
	private static FormDatiAggiuntiviWidgetUiBinder uiBinder = GWT.create(FormDatiAggiuntiviWidgetUiBinder.class);

	interface FormDatiAggiuntiviWidgetUiBinder extends UiBinder<Widget, FormDatiAggiuntiviWidget> {
		//
	}

	@UiField
	HTMLPanel datiAggiuntiviPanel;
	@UiField
	HTMLPanel bottoneSalvaPanel;
	@UiField
	Button salvaButton;

	public enum TipoPagina {
		RICERCA, CREAZIONE, DETTAGLIO, MODIFICA;
	}

	private Map<String, Widget> campi = new LinkedHashMap<String, Widget>();
	private List<Widget> campiObbligatori = new ArrayList<Widget>();
	private List<DatoAggiuntivo> valoriDettaglio = new ArrayList<DatoAggiuntivo>();

	private EventBus eventBus;
	private Object openingRequestor;
	private DispatchAsync dispatch;

	public FormDatiAggiuntiviWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public FormDatiAggiuntiviWidget(EventBus eventBus, Object openingRequestor, DispatchAsync dispatch) {
		this();
		this.eventBus = eventBus;
		this.openingRequestor = openingRequestor;
		this.dispatch = dispatch;
	}

	/*
	 * CREAZIONE
	 */
	public void setDatiAggiuntiviPerCreazione(List<DatoAggiuntivo> datiset) {

		datiset = new ArrayList<DatoAggiuntivo>(filtraDatiDiRicerca(datiset));

		bottoneSalvaPanel.setVisible(false);
		datiAggiuntiviPanel.clear();
		campiObbligatori.clear();
		campi.clear();
		valoriDettaglio = new ArrayList<DatoAggiuntivo>();

		Collections.sort(datiset, new Comparator<DatoAggiuntivo>() {
			@Override
			public int compare(DatoAggiuntivo d1, DatoAggiuntivo d2) {
				return d1.getPosizione() - d2.getPosizione();
			}
		});

		for (DatoAggiuntivo datoAggiuntivo : datiset) {

			if (datoAggiuntivo.isVisibile()) {

				Widget datoAggiuntivoWidget = getDatoAggiuntivoWidget(datoAggiuntivo, TipoPagina.CREAZIONE);

				if (isObbligatorio(datoAggiuntivo)) {
					campiObbligatori.add(datoAggiuntivoWidget);
				}

				campi.put(datoAggiuntivo.getNome(), datoAggiuntivoWidget);

				Widget panel = PanelContenitoreStrategy.CREAZIONE.creaContenitore(datoAggiuntivo, datoAggiuntivoWidget);
				datiAggiuntiviPanel.add(panel);
			}

			valoriDettaglio.add(datoAggiuntivo.clona());
		}
	}

	/*
	 * DETTAGLIO
	 */
	public void setDatiAggiuntiviPerDettaglio(List<DatoAggiuntivo> valoriDatiAggiuntivi) {

		valoriDatiAggiuntivi = new ArrayList<DatoAggiuntivo>(filtraDatiDiRicerca(valoriDatiAggiuntivi));

		valoriDettaglio = valoriDatiAggiuntivi;
		datiAggiuntiviPanel.clear();
		campiObbligatori.clear();

		Collections.sort(valoriDettaglio, new Comparator<DatoAggiuntivo>() {
			@Override
			public int compare(DatoAggiuntivo d1, DatoAggiuntivo d2) {
				return d1.getPosizione() - d2.getPosizione();
			}
		});

		datiAggiuntiviPanel.setStyleName("filters");

		for (DatoAggiuntivo datoAggiuntivo : valoriDettaglio) {
			if (datoAggiuntivo.isVisibile()) {

				Widget datoAggiuntivoWidget = getDatoAggiuntivoValorizzatoWidget(datoAggiuntivo, TipoPagina.DETTAGLIO);

				if (isEditabile(datoAggiuntivo)) {
					bottoneSalvaPanel.setVisible(true);
					if (isObbligatorio(datoAggiuntivo))
						campiObbligatori.add(datoAggiuntivoWidget);
				}

				campi.put(datoAggiuntivo.getNome(), datoAggiuntivoWidget);
				Widget panel = PanelContenitoreStrategy.DETTAGLIO.creaContenitore(datoAggiuntivo, datoAggiuntivoWidget);
				datiAggiuntiviPanel.add(panel);
			}
		}

		datiAggiuntiviPanel.add(bottoneSalvaPanel);
	}

	/*
	 * RICERCA
	 */
	private List<DatoAggiuntivo> datiAggiuntiviRicerca;

	public void setDatiAggiuntiviPerRicerca(Set<DatoAggiuntivo> datiset) {

		resetDatiAggiuntivi();
		campi.clear();

		datiAggiuntiviRicerca = new ArrayList<DatoAggiuntivo>(filtraDati(datiset));
		Collections.sort(datiAggiuntiviRicerca, new Comparator<DatoAggiuntivo>() {
			@Override
			public int compare(DatoAggiuntivo d1, DatoAggiuntivo d2) {
				return d1.getPosizione() - d2.getPosizione();
			}
		});

		int colonnaRicerca = 0;
		FieldSetElement fieldSet = null;
		for (DatoAggiuntivo datoAggiuntivo : datiAggiuntiviRicerca) {
			if (datoAggiuntivo.isVisibile()) {

				Widget datoAggiuntivoWidget = getDatoAggiuntivoWidget(datoAggiuntivo, TipoPagina.RICERCA);

				campi.put(datoAggiuntivo.getNome(), datoAggiuntivoWidget);

				if (datoAggiuntivoWidget != null) {

					if (!datoAggiuntivo.getTipo().equals(TipoDato.MultiploTesto)) {
						datoAggiuntivoWidget.setStyleName("testo");

						if (TipoDato.Lista.equals(datoAggiuntivo.getTipo())) {
							datoAggiuntivoWidget.setStyleName("testo filtro-lista-dati-agg");
						}

						InputElement inputElement = datoAggiuntivoWidget.getElement().cast();
						inputElement.setAttribute("placeholder", datoAggiuntivo.getDescrizione());

					} else {
						((DatoAggiuntivoMultiploWidget) datoAggiuntivoWidget).setPlaceholder(datoAggiuntivo.getDescrizione());
					}
				}

				if (colonnaRicerca == 0) {
					fieldSet = DOM.createFieldSet().cast();
					datiAggiuntiviPanel.getElement().appendChild(fieldSet);
				}

				Widget panel = PanelContenitoreStrategy.RICERCA.creaContenitore(datoAggiuntivo, datoAggiuntivoWidget);
				datiAggiuntiviPanel.add(panel, fieldSet);

				colonnaRicerca++;
				if (colonnaRicerca == 3)
					colonnaRicerca = 0;

			}
		}
	}

	private DatoAggiuntivo getDatoAggiuntivo(String idDato) {

		if (datiAggiuntiviRicerca == null)
			return null;

		for (DatoAggiuntivo dag : datiAggiuntiviRicerca) {
			if (dag.getNome().equals(idDato)) {
				return dag;
			}
		}

		return null;

	}

	/*
	 * MODIFICA
	 */
	public void setDatiAggiuntiviPerModifica(List<DatoAggiuntivo> valoriAggiuntivi, boolean tipoOriginale) {

		valoriAggiuntivi = new ArrayList<DatoAggiuntivo>(filtraDatiDiRicerca(valoriAggiuntivi));

		if (valoriDettaglio != null)
			valoriDettaglio.clear();
		else
			valoriDettaglio = new ArrayList<DatoAggiuntivo>();

		for (DatoAggiuntivo dag : valoriAggiuntivi) {
			valoriDettaglio.add(dag.clona());
		}

		bottoneSalvaPanel.setVisible(false);
		datiAggiuntiviPanel.clear();
		campiObbligatori.clear();
		campi.clear();

		// ordino i campi
		Collections.sort(valoriDettaglio, new Comparator<DatoAggiuntivo>() {

			@Override
			public int compare(DatoAggiuntivo d1, DatoAggiuntivo d2) {
				return d1.getPosizione() - d2.getPosizione();
			}
		});

		TipoPagina tipoPagina = TipoPagina.MODIFICA;

		for (DatoAggiuntivo datoAggiuntivo : valoriDettaglio) {
			Widget datoAggiuntivoWidget;

			if (tipoOriginale) {
				datoAggiuntivoWidget = getDatoAggiuntivoValorizzatoWidget(datoAggiuntivo, tipoPagina, tipoOriginale);

			} else {
				datoAggiuntivoWidget = getDatoAggiuntivoWidget(datoAggiuntivo, tipoPagina, tipoOriginale);
				if (getValore(datoAggiuntivo) != null || getValori(datoAggiuntivo) != null) {
					DatiAggiuntiviWidgetUtil.setValoreAggiuntivo(datoAggiuntivoWidget, datoAggiuntivo);
				}
			}

			if (datoAggiuntivo.isVisibile()) {

				if (isObbligatorio(datoAggiuntivo)) {
					campiObbligatori.add(datoAggiuntivoWidget);
				}

				campi.put(datoAggiuntivo.getNome(), datoAggiuntivoWidget);
				Widget panel = PanelContenitoreStrategy.MODIFICA.creaContenitore(datoAggiuntivo, datoAggiuntivoWidget);
				datiAggiuntiviPanel.add(panel);
			}
		}
	}

	/*
	 * CREAZIONE WIDGET
	 */

	private Widget getDatoAggiuntivoWidget(DatoAggiuntivo dato, TipoPagina tipo) {
		return getDatoAggiuntivoWidget(dato, tipo, null);
	}

	private Widget getDatoAggiuntivoWidget(DatoAggiuntivo dato, TipoPagina tipo, Boolean tipoOriginale) {
		Widget field = null;
		switch (dato.getTipo()) {

		case Testo:
			field = new TextBox();
			break;

		case Numerico:
			field = new DatiAggiuntiviNumberBox();
			break;

		case Data:
			field = new DateBox();
			Format format = new DateBox.DefaultFormat(dateFormat);
			((DateBox) field).setFormat(format);
			((DateBox) field).getDatePicker().setYearArrowsVisible(true);
			break;

		case Suggest:
			field = new SuggestBox(new SpacebarSuggestOracle(getValoriPredefiniti(dato)));
			break;

		case Lista:
			field = new DatiAggiuntiviListaBox(dato, tipo);
			break;

		case IndirizzoVia:
			field = new SuggestBox(new SpacebarSuggestOracle(new ArrayList<String>()));
			break;

		case IndirizzoCivico:
			field = new DatiAggiuntiviNumberBox();
			break;

		case IndirizzoEsponente:
			field = new TextBox();
			break;

		case MultiploTesto:
			field = new DatoAggiuntivoMultiploWidget(getValoriPredefiniti(dato), tipo);
			break;

		case Anagrafica:

			if (!TipoPagina.RICERCA.equals(tipo)) {

				if (this.eventBus != null) {
					SelezionaAnagraficaCommand selezionaDatoAggiuntivoAnagraficaLocal = new SelezionaAnagraficaCommand(this.eventBus, this.openingRequestor);

					if (selezionaDatoAggiuntivoAnagraficaLocal != null)
						selezionaDatoAggiuntivoAnagraficaLocal.setNomeDatoAggiuntivo(dato.getNome());
					field = new DatoAggiuntivoAnagraficaWidget(null, selezionaDatoAggiuntivoAnagraficaLocal, false, this.dispatch, this);

				} else {
					SelezionaAnagraficaCommand selezionaDatoAggiuntivoAnagraficaLocal = new SelezionaAnagraficaCommand();

					if (selezionaDatoAggiuntivoAnagraficaLocal != null)
						selezionaDatoAggiuntivoAnagraficaLocal.setNomeDatoAggiuntivo(dato.getNome());
					field = new DatoAggiuntivoAnagraficaWidget(null, selezionaDatoAggiuntivoAnagraficaLocal, false, this.dispatch, this);
				}

			} else {
				field = new TextBox();
			}

			break;

		case Tabella:
			field = generaTabella((DatoAggiuntivoTabella) dato, tipo, tipoOriginale);
			break;

		case MultiploRicerca:
			field = new TextBox();
			break;

		default:
			throw new IllegalArgumentException("Tipo Dato Aggiuntivo non gestito: " + dato.getTipo());

		}

		return field;
	}

	private Widget getDatoAggiuntivoValorizzatoWidget(DatoAggiuntivo dato, TipoPagina tipo) {
		return getDatoAggiuntivoValorizzatoWidget(dato, tipo, null);
	}

	private Widget getDatoAggiuntivoValorizzatoWidget(DatoAggiuntivo dato, TipoPagina tipo, Boolean tipoOriginale) {
		Widget field = null;

		if (isEditabile(dato)) {

			switch (dato.getTipo()) {
			case Testo:
				field = new TextBox();
				((TextBox) field).setValue(getValore(dato));
				break;

			case Numerico:
				field = new DatiAggiuntiviNumberBox();
				((DatiAggiuntiviNumberBox) field).setValue((dato == null || getValore(dato) == null) ? null : Long.valueOf(getValore(dato)));
				break;

			case Data:
				field = new DateBox();
				Format format = new DateBox.DefaultFormat(dateFormat);
				((DateBox) field).setFormat(format);
				((DateBox) field).setValue((dato == null || getValore(dato) == null || getValore(dato).isEmpty()) ? null : dateFormat.parse(getValore(dato)));
				((DateBox) field).getDatePicker().setYearArrowsVisible(true);
				break;

			case Suggest:
				field = new SuggestBox(new SpacebarSuggestOracle(getValoriPredefiniti(dato)));
				((SuggestBox) field).setValue(getValore(dato));
				break;

			case Lista:
				field = new DatiAggiuntiviListaBox(dato, tipo);
				((DatiAggiuntiviListaBox) field).setValue(getValore(dato));
				break;

			case IndirizzoVia:
				field = new SuggestBox(new SpacebarSuggestOracle(new ArrayList<String>()));
				((SuggestBox) field).setValue(getValore(dato));
				break;

			case IndirizzoCivico:
				field = new DatiAggiuntiviNumberBox();
				String valore = getValore(dato);
				((DatiAggiuntiviNumberBox) field).setValue((dato == null || valore == null || valore.isEmpty()) ? null : Long.valueOf(getValore(dato)));
				break;

			case IndirizzoEsponente:
				field = new TextBox();
				((TextBox) field).setValue(getValore(dato));
				break;

			case MultiploTesto:
				field = new DatoAggiuntivoMultiploWidget(getValoriPredefiniti(dato), tipo);
				for (String val : getValori(dato)) {
					((DatoAggiuntivoMultiploWidget) field).addValueItem(val);
				}
				break;

			case Anagrafica:

				SelezionaAnagraficaCommand selezionaDatoAggiuntivoAnagraficaLocal = new SelezionaAnagraficaCommand(this.eventBus, this.openingRequestor);
				selezionaDatoAggiuntivoAnagraficaLocal.setNomeDatoAggiuntivo(dato.getNome());
				field = new DatoAggiuntivoAnagraficaWidget(getValore(dato), selezionaDatoAggiuntivoAnagraficaLocal, true, this.dispatch, this);
				((DatoAggiuntivoAnagraficaWidget) field).setIdAnagrafica(getIdAnagrafica(dato));
				((DatoAggiuntivoAnagraficaWidget) field).setEditable(true);
				break;

			case Tabella:
				field = generaTabella((DatoAggiuntivoTabella) dato, tipo, tipoOriginale);
				break;

			default:
				throw new IllegalArgumentException("Tipo Dato Aggiuntivo non gestito: " + dato.getTipo());
			}

		} else {

			if ((getValore(dato) != null && !getValore(dato).equals("")) || getValori(dato).size() > 0 || dato.getTipo() == TipoDato.Anagrafica || dato.getTipo().equals(TipoDato.Tabella)) {

				switch (dato.getTipo()) {

				case Lista:
					field = new DatiAggiuntiviListaBox(dato, tipo);
					((DatiAggiuntiviListaBox) field).setValue(getValore(dato));
					((DatiAggiuntiviListaBox) field).setEditable(false);
					break;

				case Data:
				case IndirizzoCivico:
				case IndirizzoEsponente:
				case IndirizzoVia:
				case Numerico:
				case Suggest:
				case Testo:
					field = new Label(getValore(dato));
					break;

				case MultiploTesto:

					StringBuilder strBuilder = new StringBuilder();

					for (String val : getValori(dato)) {
						strBuilder.append(val + "<br/>");
					}

					field = new InlineHTML(strBuilder.toString());
					break;

				case Anagrafica:
					SelezionaAnagraficaCommand selezionaDatoAggiuntivoAnagraficaLocal = new SelezionaAnagraficaCommand(this.eventBus, this.openingRequestor);
					selezionaDatoAggiuntivoAnagraficaLocal.setNomeDatoAggiuntivo(dato.getNome());
					field = new DatoAggiuntivoAnagraficaWidget(getValore(dato), selezionaDatoAggiuntivoAnagraficaLocal, false, this.dispatch, this);
					((DatoAggiuntivoAnagraficaWidget) field).setEditable(false);
					((DatoAggiuntivoAnagraficaWidget) field).setIdAnagrafica(getIdAnagrafica(dato));
					break;

				case Tabella:
					field = generaTabella((DatoAggiuntivoTabella) dato, tipo, tipoOriginale);
					((DatoAggiuntivoTabellaWidget) field).setEditable(false);
					break;

				default:
					throw new IllegalArgumentException("Tipo Dato Aggiuntivo non gestito: " + dato.getTipo());
				}

			} else {
				field = new Label("-");
			}
		}

		return field;
	}

	private Widget generaTabella(DatoAggiuntivoTabella datoTabella, TipoPagina tipo, Boolean tipoOriginale) {

		Map<DatoAggiuntivo, Widget> widgets = new LinkedHashMap<>();

		for (DatoAggiuntivo dato : datoTabella.getIntestazioni()) {
			if (TipoPagina.CREAZIONE.equals(tipo) || TipoPagina.RICERCA.equals(tipo) || (TipoPagina.MODIFICA.equals(tipo) && !Boolean.TRUE.equals(tipoOriginale))) {
				Widget w = getDatoAggiuntivoWidget(dato, tipo, tipoOriginale);
				widgets.put(dato, w);

				if (TipoPagina.RICERCA.equals(tipo)) {
					InputElement inputElement = w.getElement().cast();
					inputElement.setAttribute("placeholder", dato.getDescrizione());
				}

			} else {
				widgets.put(dato, getDatoAggiuntivoValorizzatoWidget(dato, tipo, tipoOriginale));
			}
		}

		DatoAggiuntivoTabellaWidget tabellaWidget = new DatoAggiuntivoTabellaWidget(widgets, datoTabella.getRighe(), tipo, this.openingRequestor, this.dispatch);
		return tabellaWidget;
	}

	/*
	 * GET
	 */

	public List<DatoAggiuntivo> getDatiAggiuntivi() {
		if (valoriDettaglio == null) {
			return new ArrayList<DatoAggiuntivo>();

		} else {
			for (DatoAggiuntivo valore : this.valoriDettaglio) {

				final Widget campo = this.campi.get(valore.getNome());

				valore.accept(new DatoAggiuntivoVisitor() {

					@Override
					public void visit(DatoAggiuntivoTabella datoAggiuntivoTabella) {

						DatiAggiuntiviWidgetUtil.pulisciIntestazioni(datoAggiuntivoTabella);

						if (campo instanceof DatoAggiuntivoTabellaWidget) {
							datoAggiuntivoTabella.getRighe().clear();
							datoAggiuntivoTabella.getRighe().addAll(((DatoAggiuntivoTabellaWidget) campo).getRigheDatiAggiuntivi());
						}
					}

					@Override
					public void visit(DatoAggiuntivoAnagrafica valore) {
						if (campo instanceof DatoAggiuntivoAnagraficaWidget) {
							valore.setValore(((DatoAggiuntivoAnagraficaWidget) campo).getValoreDatoAggiuntivo());
							valore.setIdAnagrafica(((DatoAggiuntivoAnagraficaWidget) campo).getIdAnagrafica());
						}
					}

					@Override
					public void visit(DatoAggiuntivoValoreMultiplo valore) {
						if (campo instanceof DatoAggiuntivoMultiploWidget) {
							List<String> listValori = valore.getValori();
							listValori.clear();
							listValori.addAll(((DatoAggiuntivoMultiploWidget) campo).getItemSelected());
						}
					}

					@Override
					public void visit(DatoAggiuntivoValoreSingolo valore) {
						if (campo instanceof TextBox) {
							valore.setValore(GenericsUtil.normalizzaValoreTesto(((TextBox) campo).getValue()));
						}
						if (campo instanceof SuggestBox) {
							valore.setValore(GenericsUtil.normalizzaValoreTesto(((SuggestBox) campo).getValue()));
						}
						if (campo instanceof DateBox) {
							valore.setValore(ConsolePecUtils.normalizzaValoreData(((DateBox) campo).getValue(), dateFormat));
						}
						if (campo instanceof DatiAggiuntiviNumberBox) {
							valore.setValore(GenericsUtil.normalizzaValoreNumerico(((DatiAggiuntiviNumberBox) campo).getValue()));
						}
						if (campo instanceof DatiAggiuntiviListaBox) {
							valore.setValore(GenericsUtil.normalizzaValoreTesto(((DatiAggiuntiviListaBox) campo).getValue()));
						}

						if (campo instanceof Label && ((Label) campo).getText() != null && !((Label) campo).getText().trim().equals("")) {
							String value = ((Label) campo).getText();
							valore.setValore(value);
						}
					}
				});
			}
		}

		return this.valoriDettaglio;
	}

	public Map<DatoAggiuntivo, Object> getDatiAggiuntiviPerRicerca() {
		Map<DatoAggiuntivo, Object> map = new LinkedHashMap<DatoAggiuntivo, Object>();
		getDatiAggiuntiviPerRicerca(map, campi);
		return map;
	}

	private void getDatiAggiuntiviPerRicerca(Map<DatoAggiuntivo, Object> result, Map<String, Widget> campi) {

		for (Entry<String, Widget> entry : campi.entrySet()) {
			Widget campo = entry.getValue();
			if (campo instanceof TextBox && !((TextBox) campo).getValue().trim().equals(""))
				result.put(getDatoAggiuntivo(entry.getKey()), ((TextBox) campo).getValue().trim());
			if (campo instanceof SuggestBox && !((SuggestBox) campo).getValue().trim().equals(""))
				result.put(getDatoAggiuntivo(entry.getKey()), ((SuggestBox) campo).getValue().trim());
			if (campo instanceof DateBox && ((DateBox) campo).getValue() != null)
				result.put(getDatoAggiuntivo(entry.getKey()), dateFormat.format(((DateBox) campo).getValue()));
			if (campo instanceof DatiAggiuntiviNumberBox && ((DatiAggiuntiviNumberBox) campo).getValue() != null)
				result.put(getDatoAggiuntivo(entry.getKey()), Long.toString(((DatiAggiuntiviNumberBox) campo).getValue()));
			if (campo instanceof DatiAggiuntiviListaBox && ((DatiAggiuntiviListaBox) campo).getValue() != null)
				result.put(getDatoAggiuntivo(entry.getKey()), String.valueOf(((DatiAggiuntiviListaBox) campo).getValue()));
			if (campo instanceof Label && ((Label) campo).getText() != null && !((Label) campo).getText().trim().equals(""))
				result.put(getDatoAggiuntivo(entry.getKey()), ((Label) campo).getText().trim());
			if (campo instanceof DatoAggiuntivoMultiploWidget && ((DatoAggiuntivoMultiploWidget) campo).getItemSelected().size() > 0) {

				List<String> trimmed = new ArrayList<String>();
				for (String v : ((DatoAggiuntivoMultiploWidget) campo).getItemSelected()) {
					if (v != null && !v.trim().equals(""))
						trimmed.add(v.trim());
				}
				result.put(getDatoAggiuntivo(entry.getKey()), trimmed);
			}

			if (campo instanceof DatoAggiuntivoAnagraficaWidget) {
				continue;
			}

			if (campo instanceof DatoAggiuntivoTabellaWidget) {
				continue;
			}
		}
	}

	/*
	 * SET Dato Anagrafica
	 */
	public void setAnagrafica(final String nomeDatoAggiuntivo, final Anagrafica anagrafica, List<ConfigurazioneEsecuzione> esecuzioni) {

		final Ref<DatoAggiuntivoAnagrafica> datoRef = Ref.of(null);
		final Ref<DatoAggiuntivoTabella> tabellaRef = Ref.of(null);

		for (DatoAggiuntivo valore : valoriDettaglio) {

			valore.accept(new DatoAggiuntivo.DatoAggiuntivoVisitorAdapter() {

				@Override
				public void visit(final DatoAggiuntivoTabella datoAggiuntivoTabella) {

					for (DatoAggiuntivo dag : datoAggiuntivoTabella.getIntestazioni()) {

						dag.accept(new DatoAggiuntivo.DatoAggiuntivoVisitorAdapter() {

							@Override
							public void visit(DatoAggiuntivoAnagrafica datoAggiuntivoAnagrafica) {
								if (datoAggiuntivoAnagrafica.getNome().equals(nomeDatoAggiuntivo)) {
									datoRef.set(datoAggiuntivoAnagrafica);
									tabellaRef.set(datoAggiuntivoTabella);
								}
							}
						});
					}
				}

				@Override
				public void visit(DatoAggiuntivoAnagrafica datoAggiuntivoAnagrafica) {
					if (datoAggiuntivoAnagrafica.getNome().equals(nomeDatoAggiuntivo)) {
						datoRef.set(datoAggiuntivoAnagrafica);
					}
				}
			});

			if (datoRef.get() != null) {
				break;
			}
		}

		if (datoRef.get() != null) {
			if (tabellaRef.get() != null) {
				Widget campo = campi.get(tabellaRef.get().getNome());
				((DatoAggiuntivoTabellaWidget) campo).setAnagrafica(datoRef.get(), anagrafica);
			} else {

				popolaDatoAggiuntivoIndirizzo(anagrafica, esecuzioni);

				final Widget widgetSelezionato = campi.get(nomeDatoAggiuntivo);

				if (anagrafica != null) {
					((DatoAggiuntivoAnagraficaWidget) widgetSelezionato).setAnagraficaSelezionata(true);
					((DatoAggiuntivoAnagraficaWidget) widgetSelezionato).getButton().setText("Rimuovi");

					datoRef.get().accept(new DatoAggiuntivo.DatoAggiuntivoVisitorAdapter() {

						@Override
						public void visit(DatoAggiuntivoAnagrafica datoAggiuntivo) {
							datoAggiuntivo.setValore(calcolaNomeAnagrafica(anagrafica));
							datoAggiuntivo.setIdAnagrafica(anagrafica.getId());
							DatiAggiuntiviWidgetUtil.setValoreAggiuntivo(widgetSelezionato, datoAggiuntivo);
						}
					});

				} else {
					((DatoAggiuntivoAnagraficaWidget) widgetSelezionato).setAnagraficaSelezionata(false);
					((DatoAggiuntivoAnagraficaWidget) widgetSelezionato).getButton().setText("Seleziona");

					datoRef.get().accept(new DatoAggiuntivo.DatoAggiuntivoVisitorAdapter() {
						@Override
						public void visit(DatoAggiuntivoAnagrafica datoAggiuntivo) {
							datoAggiuntivo.setValore(null);
							datoAggiuntivo.setIdAnagrafica(null);
							DatiAggiuntiviWidgetUtil.setValoreAggiuntivo(widgetSelezionato, datoAggiuntivo);
						}
					});
				}

			}
		}
	}

	private Map<String, DatoAggiuntivoValoreSingolo> loadMapDatiAggiuntiviValoreSingolo() {
		final Map<String, DatoAggiuntivoValoreSingolo> map = new HashMap<>();
		for (DatoAggiuntivo v : valoriDettaglio) {
			v.accept(new DatoAggiuntivo.DatoAggiuntivoVisitorAdapter() {
				@Override
				public void visit(DatoAggiuntivoValoreSingolo datoAggiuntivoValoreSingolo) {
					map.put(datoAggiuntivoValoreSingolo.getNome(), datoAggiuntivoValoreSingolo);
				}
			});
		}
		return map;
	}

	private void popolaDatoAggiuntivoIndirizzo(Anagrafica a, List<ConfigurazioneEsecuzione> esecuzioni) {
		Map<String, DatoAggiuntivoValoreSingolo> map = loadMapDatiAggiuntiviValoreSingolo();
		if (a == null || esecuzioni == null || map.isEmpty())
			return;
		for (ConfigurazioneEsecuzione e : esecuzioni) {
			if (e.getCondizione().equals(new RibaltaDatiAggiuntiviDaAnagraficaCondizioneEsecuzione())) {
				for (ConseguenzaEsecuzione exe : e.getEsecuzioni()) {
					if (exe instanceof RibaltaDatiAggiuntiviDaAnagraficaConseguenzaEsecuzione) {
						ribaltaSuDatiAggiuntivi(map, a, (RibaltaDatiAggiuntiviDaAnagraficaConseguenzaEsecuzione) exe);
					}
				}
			}
		}
	}

	private void ribaltaSuDatiAggiuntivi(Map<String, DatoAggiuntivoValoreSingolo> map, Anagrafica a, RibaltaDatiAggiuntiviDaAnagraficaConseguenzaEsecuzione exe) {
		if (!Strings.isNullOrEmpty(exe.getNome()) && a instanceof PersonaFisica) {
			impostaCampoValoreSingolo(map, exe.getNome(), ((PersonaFisica) a).getNome());
		}
		if (!Strings.isNullOrEmpty(exe.getCognome()) && a instanceof PersonaFisica) {
			impostaCampoValoreSingolo(map, exe.getCognome(), ((PersonaFisica) a).getCognome());
		}
		if (!Strings.isNullOrEmpty(exe.getCodiceFiscale()) && a instanceof PersonaFisica) {
			impostaCampoValoreSingolo(map, exe.getCodiceFiscale(), ((PersonaFisica) a).getCodiceFiscale());
		}
		if (!Strings.isNullOrEmpty(exe.getLuogoNascita()) && a instanceof PersonaFisica) {
			impostaCampoValoreSingolo(map, exe.getLuogoNascita(), ((PersonaFisica) a).getLuogoNascita());
		}

		if (!Strings.isNullOrEmpty(exe.getDataNascita()) && a instanceof PersonaFisica) {
			impostaCampoValoreSingolo(map, exe.getDataNascita(), ConsolePecUtils.normalizzaValoreData(((PersonaFisica) a).getDataNascita(), dateFormat));
		}

		if (!Strings.isNullOrEmpty(exe.getTipologiaIndirizzo())) {
			Indirizzo i = null;
			for (Indirizzo ia : a.getIndirizzi()) {
				if (exe.getTipologiaIndirizzo().equalsIgnoreCase(ia.getTipologia())) {
					i = ia;
					break;
				}
			}
			if (!Strings.isNullOrEmpty(exe.getIndirizzo()) && i != null) {
				impostaCampoValoreSingolo(map, exe.getIndirizzo(), i.toString(false));
			}
			if (!Strings.isNullOrEmpty(exe.getVia()) && i != null) {
				impostaCampoValoreSingolo(map, exe.getVia(), i.getVia());
			}
			if (!Strings.isNullOrEmpty(exe.getCivico()) && i != null) {
				impostaCampoValoreSingolo(map, exe.getCivico(), i.getCivico());
			}
			if (!Strings.isNullOrEmpty(exe.getEsponente()) && i != null) {
				impostaCampoValoreSingolo(map, exe.getEsponente(), i.getEsponente());
			}
			if (!Strings.isNullOrEmpty(exe.getPiano()) && i != null) {
				impostaCampoValoreSingolo(map, exe.getPiano(), i.getPiano());
			}
			if (!Strings.isNullOrEmpty(exe.getInterno()) && i != null) {
				impostaCampoValoreSingolo(map, exe.getInterno(), i.getInterno());
			}
			if (!Strings.isNullOrEmpty(exe.getCap()) && i != null) {
				impostaCampoValoreSingolo(map, exe.getCap(), i.getCap());
			}
			if (!Strings.isNullOrEmpty(exe.getComune()) && i != null) {
				impostaCampoValoreSingolo(map, exe.getComune(), i.getComune());
			}
			if (!Strings.isNullOrEmpty(exe.getNazione()) && i != null) {
				impostaCampoValoreSingolo(map, exe.getNazione(), i.getNazione());
			}
		}

		if (!Strings.isNullOrEmpty(exe.getTipologiaTelefono())) {
			Telefono tel = null;
			for (Telefono t : a.getTelefoni()) {
				if (exe.getTipologiaTelefono().equalsIgnoreCase(t.getTipologia())) {
					tel = t;
					break;
				}
			}

			if (!Strings.isNullOrEmpty(exe.getTelefono()) && tel != null) {
				impostaCampoValoreSingolo(map, exe.getTelefono(), tel.getNumero());
			}
		}
	}

	private void impostaCampoValoreSingolo(Map<String, DatoAggiuntivoValoreSingolo> map, String campo, String valore) {
		if (map.containsKey(campo)) {
			map.get(campo).setValore(valore);
			DatiAggiuntiviWidgetUtil.setValoreAggiuntivo(campi.get(campo), map.get(campo));
		}
	}

	public void resetDatiAggiuntivi() {
		datiAggiuntiviPanel.clear();
		datiAggiuntiviPanel.getElement().setInnerHTML("");

		if (datiAggiuntiviRicerca != null) {
			datiAggiuntiviRicerca.clear();
		}
	}

	public void setSaveEnabled(boolean enabled) {
		salvaButton.setEnabled(enabled);
	}

	public void setSalvaFascicoloCommand(final SalvaFascicoloCommand salvaFascicoloCommand) {
		salvaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (validazioneClient()) {
					salvaFascicoloCommand.execute();

				} else {
					String messaggio = "I campi in rosso devono essere valorizzati correttamente";
					ShowMessageEvent messageEvent = new ShowMessageEvent();
					messageEvent.setWarningMessage(messaggio);
					eventBus.fireEvent(event);
				}
			}
		});
	}

	/*
	 * VALIDAZIONE SERVER
	 */
	public boolean validazioneServer(List<ValidazioneDatoAggiuntivoDTO> validazioneDatiAggiuntivi) {
		boolean valido = true;

		for (ValidazioneDatoAggiuntivoDTO va : validazioneDatiAggiuntivi) {
			Widget campo = campi.get(va.getDatoAggiuntivo().getNome());

			if (campo != null) {
				if (va.isValido()) {
					campo.getElement().removeAttribute("required");

				} else {
					campo.getElement().setAttribute("required", "required");

					if (va.getDatoAggiuntivo().getTipo().equals(TipoDato.IndirizzoVia)) {

						SpacebarSuggestOracle oracle = (SpacebarSuggestOracle) ((SuggestBox) campo).getSuggestOracle();
						oracle.setSuggestions(va.getVieConLoStessoNome());
						if (!va.getVieConLoStessoNome().isEmpty()) {
							((SuggestBox) campo).showSuggestionList();
						}
					}
					valido = false;
				}

				DatiAggiuntiviWidgetUtil.setValoreAggiuntivo(campo, va.getDatoAggiuntivo());
			}
		}

		return valido;
	}

	/*
	 * VALIDAZIONE CLIENT
	 */
	public boolean validazioneClient() {
		return DatiAggiuntiviWidgetUtil.validazioneClient(campi, campiObbligatori);
	}

	private Collection<DatoAggiuntivo> filtraDatiDiRicerca(Collection<DatoAggiuntivo> datiAggiuntivi) {

		return Collections2.filter(datiAggiuntivi, new Predicate<DatoAggiuntivo>() {

			@Override
			public boolean apply(DatoAggiuntivo dag) {
				return !TipoDato.MultiploRicerca.equals(dag.getTipo());
			}

		});
	}

	private Collection<DatoAggiuntivo> filtraDati(Collection<DatoAggiuntivo> datiAggiuntivi) {

		return Collections2.filter(datiAggiuntivi, new Predicate<DatoAggiuntivo>() {

			@Override
			public boolean apply(DatoAggiuntivo dag) {
				return !TipoDato.Anagrafica.equals(dag.getTipo()) && !TipoDato.Tabella.equals(dag.getTipo());
			}

		});
	}
}
