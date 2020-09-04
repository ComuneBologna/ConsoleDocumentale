package it.eng.portlet.consolepec.gwt.client.widget.formricerca;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;

public class FormRicercaLibera extends FormRicercaBase {

	@UiField
	CheckBox nonAssegnateCheckBox;

	private static FormRicercaLiberaWidgetUiBinder uiBinder = GWT.create(FormRicercaLiberaWidgetUiBinder.class);

	interface FormRicercaLiberaWidgetUiBinder extends UiBinder<Widget, FormRicercaLibera> {}

	@UiField(provided = true)
	SuggestBox tipoPraticaSuggestBox;
	@UiField
	protected HTMLPanel gruppoFiltriPanel;

	private boolean locked = false;
	private ConfigurazioniHandler configurazioniHandler;
	private List<TipologiaPratica> tipologiePratiche = new ArrayList<TipologiaPratica>();

	private final Map<TipologiaPratica, ConfigurazioneCampiPerTipo> configurazioniCampiPerTipo = new HashMap<TipologiaPratica, FormRicercaBase.ConfigurazioneCampiPerTipo>();

	public FormRicercaLibera(final ConfigurazioniHandler configurazioniHandler) {
		super();

		this.configurazioniHandler = configurazioniHandler;
		tipologiePratiche = PraticaUtil.toTipologiePratiche(configurazioniHandler.getAnagraficheFascicoli(true), configurazioniHandler.getAnagraficheIngressi(true),
				configurazioniHandler.getAnagraficheMailInUscita(true), configurazioniHandler.getAnagraficheComunicazioni(true), configurazioniHandler.getAnagrafichePraticaModulistica(true),
				configurazioniHandler.getAnagraficheModelli(true));

		final List<String> tipiLabels = new ArrayList<String>();
		MultiWordSuggestOracle so = new SpacebarSuggestOracle(tipiLabels);
		tipoPraticaSuggestBox = new SuggestBox(so);
		tipoPraticaSuggestBox.setVisible(false);
		tipoPraticaSuggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				String tipo = event.getSelectedItem().getDisplayString();
				TipologiaPratica tp = null;

				for (TipologiaPratica t : tipologiePratiche) {
					if (t.getEtichettaTipologia().equals(tipo)) {
						tp = t;
						break;
					}
				}

				initializeSuggestBox();

				if (tp != null) {

					for (AbstractGruppoFiltriWidget w : listFiltri) {

						if (w.getTipiPraticheGestite().contains(tp)) {
							w.reset();
							mostraGruppoFiltri(w);
						}

					}

					ConfigurazioneCampiPerTipo cct = configurazioniCampiPerTipo.get(tp);
					if (cct != null)
						applicaConfigurazionePerTipo(cct);
					else
						applicaConfigurazionePerTipo(CONFIG_CAMPI_BASE);

					for (AbstractGruppoFiltriWidget w : listFiltri) {
						w.onTipoPraticaSelection(tp);
					}

				} else {
					applicaConfigurazionePerTipo(CONFIG_CAMPI_BASE);
				}
			}

		});
		tipoPraticaSuggestBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (event.getValue() == null || event.getValue().trim().length() == 0) {
					gruppoFiltriPanel.clear();
					applicaConfigurazionePerTipo(CONFIG_CAMPI_BASE);

					for (AbstractGruppoFiltriWidget w : listFiltri) {

						if (w instanceof GruppoFiltriFascicoloDatiAggiuntiviWidget) { // può essere visibile di default
							w.reset();
							mostraGruppoFiltri(w);
						}
					}
				}
			}
		});

		initWidget(uiBinder.createAndBindUi(this));
	}

	/**
	 * sovrascrive le suggestion eventualmente presenti per il tipo di pratica da cercare
	 *
	 * @param suggestions
	 */
	@Override
	public void setElencoTipoPratiche(List<TipologiaPratica> tipiPratiche) {
		tipoPraticaSuggestBox.setVisible(false);
		SpacebarSuggestOracle so = (SpacebarSuggestOracle) tipoPraticaSuggestBox.getSuggestOracle();
		List<String> suggestions = Lists.transform(tipiPratiche, new Function<TipologiaPratica, String>() {

			@Override
			public String apply(TipologiaPratica input) {
				return input.getEtichettaTipologia();
			}

		});
		so.setSuggestions(new ArrayList<String>(new TreeSet<String>(suggestions)));
		tipoPraticaSuggestBox.setVisible(true);
	}

	/**
	 * @param tp
	 */
	public void initializeSuggestBox() {
		gruppoFiltriPanel.clear();
		gruppoFiltriPanel.setVisible(false);
	}

	@Override
	public void configura(Command cercaCommand) {
		super.configura(cercaCommand);
		configuraHandlerCampo(tipoPraticaSuggestBox);
		setLabelCampo(tipoPraticaSuggestBox, "Tipologia documentale");
		nonAssegnateCheckBox.setText(" Non assegnate a me");

	}

	public void addGruppoFiltri(AbstractGruppoFiltriWidget widget) {
		listFiltri.add(widget);
	}

	public void addConfigurazioneCampi(ConfigurazioneCampiPerTipo config) {
		configurazioniCampiPerTipo.put(config.getTipologiaPratica(), config);
	}

	@Override
	public void resetForm() {
		super.resetForm();
		tipoPraticaSuggestBox.setValue(null);
		nonAssegnateCheckBox.setValue(false);
	}

	/* metodi interni */

	protected void mostraGruppoFiltri(AbstractGruppoFiltriWidget widget) {
		DisclosurePanel dp = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), widget.getDescrizione());
		dp.setStyleName("disclosure-ricerca");
		dp.add(widget);
		widget.setDisclosurePanel(dp);
		gruppoFiltriPanel.add(dp);
		gruppoFiltriPanel.setVisible(true);

	}

	@Override
	protected void serializza(CercaPratiche dto) {
		super.serializza(dto);
		TipologiaPratica tp = null;

		for (TipologiaPratica t : tipologiePratiche) {
			if (t.getEtichettaTipologia().equals(tipoPraticaSuggestBox.getValue())) {
				tp = t;
				break;
			}
		}

		if (tp != null)
			dto.setTipologiePratiche(Arrays.asList(tp));

		dto.setEscludiProprieAssegnazioni(nonAssegnateCheckBox.getValue());
	}

	public boolean isLocked() {
		return locked;
	}

	public void intiRicercaLibera(boolean abilitato) {
		this.locked = abilitato;
		tipoPraticaSuggestBox.setEnabled(abilitato);
		if (!abilitato)
			tipoPraticaSuggestBox.setText(configurazioniHandler.getAnagraficaFascicoloPersonale().getEtichettaTipologia());
	}

	/* classi interne */

	public void filtriPersonalizzati() {

		for (AbstractGruppoFiltriWidget gruppo : listFiltri) {
			boolean visibile = gruppo.hasFiltriPersonalizzati();

			if (visibile) {
				mostraGruppoFiltri(gruppo);
			}
		}
	}
}
