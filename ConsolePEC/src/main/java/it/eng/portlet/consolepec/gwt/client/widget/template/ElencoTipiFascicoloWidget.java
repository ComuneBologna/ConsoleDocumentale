package it.eng.portlet.consolepec.gwt.client.widget.template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;

public class ElencoTipiFascicoloWidget extends Composite {

	private static ElencoTipiFascicoloWidgetUiBinder uiBinder = GWT.create(ElencoTipiFascicoloWidgetUiBinder.class);

	interface ElencoTipiFascicoloWidgetUiBinder extends UiBinder<Widget, ElencoTipiFascicoloWidget> {/**/}

	@UiField(provided = true) SuggestBox tipiFascicoloSuggestBox;

	@UiField HTMLPanel tipiFascicoloPanel;

	private List<TipologiaPratica> tipiFascicoloAbilitati;
	private List<TipologiaPratica> tipiFascicoloSelezionati = new ArrayList<>();
	private Map<TipologiaPratica, ElementoElencoTipiFascicoloWidget> tipoElementoWidgetMap = new HashMap<TipologiaPratica, ElementoElencoTipiFascicoloWidget>();
	private it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, TipologiaPratica> eliminaCommand;
	private List<ElementoElencoTipiFascicoloWidget> elementoElencoTipiFascicoloWidgets = new ArrayList<ElementoElencoTipiFascicoloWidget>();

	private ConfigurazioniHandler configurazioniHandler;
	private final Command aggiungiCommand;

	public ElencoTipiFascicoloWidget(final ConfigurazioniHandler configurazioniHandler, Command aggiungiCmd,
			it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, TipologiaPratica> eliminaCmd) {
		super();

		this.aggiungiCommand = aggiungiCmd;
		this.eliminaCommand = eliminaCmd;

		this.configurazioniHandler = configurazioniHandler;
		MultiWordSuggestOracle so = new SpacebarSuggestOracle(new ArrayList<String>());
		tipiFascicoloSuggestBox = new SuggestBox(so);

		tipiFascicoloSuggestBox.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER || event.getNativeKeyCode() == KeyCodes.KEY_TAB) {
					if (aggiungiCommand != null) {
						aggiungiCommand.execute();

					} else {
						String etichettaFascicolo = tipiFascicoloSuggestBox.getValue();
						AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicoloByEtichetta(etichettaFascicolo);
						addTipoFascicoloAbilitatoToElenco(PraticaUtil.toTipologiaPratica(af));
					}
				}
			}
		});

		tipiFascicoloSuggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				if (aggiungiCommand != null) {
					aggiungiCommand.execute();

				} else {
					String etichettaFascicolo = tipiFascicoloSuggestBox.getValue();
					AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicoloByEtichetta(etichettaFascicolo);
					addTipoFascicoloAbilitatoToElenco(PraticaUtil.toTipologiaPratica(af));
				}
			}

		});
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setListaTipiFascicoloAbilitati(List<TipologiaPratica> tipiFascicolo) {
		this.tipiFascicoloAbilitati = tipiFascicolo;
		SpacebarSuggestOracle so = (SpacebarSuggestOracle) tipiFascicoloSuggestBox.getSuggestOracle();

		List<String> res = new ArrayList<>(Lists.transform(tipiFascicolo, new Function<TipologiaPratica, String>() {

			@Override
			public String apply(TipologiaPratica input) {
				return configurazioniHandler.getAnagraficaFascicolo(input.getNomeTipologia()).getEtichettaTipologia();
			}
		}));

		Collections.sort(res, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});

		so.setSuggestions(res);
	}

	public void addTipoFascicoloAbilitatoToElenco(final TipologiaPratica tipologia) {

		if (tipologia != null) {

			if (tipiFascicoloAbilitati != null && !tipiFascicoloAbilitati.contains(tipologia)) {
				tipiFascicoloSuggestBox.getElement().getStyle().setBorderColor("#FF0000");
				return;
			}

			if (tipiFascicoloSelezionati.contains(tipologia)) {
				tipiFascicoloSuggestBox.getElement().getStyle().setBorderColor("#FF0000");
				return;
			}

			tipiFascicoloSelezionati.add(tipologia);

			tipiFascicoloSuggestBox.getElement().getStyle().clearBorderColor();

			final ElementoElencoTipiFascicoloWidget elemento = new ElementoElencoTipiFascicoloWidget();
			elemento.init(configurazioniHandler.getAnagraficaFascicolo(tipologia.getNomeTipologia()).getEtichettaTipologia());
			tipoElementoWidgetMap.put(tipologia, elemento);

			elemento.setOnEliminaCommand(new Command() {

				@Override
				public void execute() {

					if (eliminaCommand != null) eliminaCommand.exe(tipologia);

					else {
						tipoElementoWidgetMap.remove(tipologia);
						tipiFascicoloPanel.remove(elemento);
						tipiFascicoloSelezionati.remove(tipologia);
					}
				}
			});
			elementoElencoTipiFascicoloWidgets.add(elemento);
			tipiFascicoloPanel.add(elemento);
			tipiFascicoloSuggestBox.setValue(null);
		}
	}

	public void eliminaFascicolo(TipologiaPratica af) {

		if (af != null) {
			ElementoElencoTipiFascicoloWidget elemento = tipoElementoWidgetMap.get(af);
			tipoElementoWidgetMap.remove(af);
			tipiFascicoloPanel.remove(elemento);
			tipiFascicoloSelezionati.remove(af);
		}
	}

	public void clear() {
		tipiFascicoloSuggestBox.getElement().getStyle().clearBorderColor();
		elementoElencoTipiFascicoloWidgets.clear();
		tipiFascicoloSelezionati.clear();
		tipiFascicoloPanel.clear();
	}

	public List<TipologiaPratica> getValues() {
		return tipiFascicoloSelezionati;
	}

	public void setRequired(boolean required) {
		if (required) {
			tipiFascicoloSuggestBox.getElement().setAttribute("required", "required");
		} else {
			tipiFascicoloSuggestBox.getElement().removeAttribute("required");
		}
	}

	public void setEnabled(boolean enabled) {
		tipiFascicoloSuggestBox.setEnabled(enabled);
	}

	public TipologiaPratica getTipoSelezionato() {
		return PraticaUtil.toTipologiaPratica(configurazioniHandler.getAnagraficaFascicoloByEtichetta(tipiFascicoloSuggestBox.getValue()));
	}
}
