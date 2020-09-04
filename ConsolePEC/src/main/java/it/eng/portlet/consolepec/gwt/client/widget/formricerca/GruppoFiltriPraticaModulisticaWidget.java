package it.eng.portlet.consolepec.gwt.client.widget.formricerca;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO.StatoDTO;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;

public class GruppoFiltriPraticaModulisticaWidget extends AbstractGruppoFiltriWidget {

	@UiField(provided = true)
	SuggestBox statoSuggestBox;

	private static FormRicercaPraticaModulisticaWidgetUiBinder uiBinder = GWT.create(FormRicercaPraticaModulisticaWidgetUiBinder.class);
	
	interface FormRicercaPraticaModulisticaWidgetUiBinder extends UiBinder<Widget, GruppoFiltriPraticaModulisticaWidget> {
	}

	public GruppoFiltriPraticaModulisticaWidget(ProfilazioneUtenteHandler profilazioneUtenteHandler, ConfigurazioniHandler configurazioniHandler) {
		super();
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		this.configurazioniHandler = configurazioniHandler;
		initSuggestBoxStati();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public List<TipologiaPratica> getTipiPraticheGestite() {
		return PraticaUtil.praticheModulisticaToTipologiePratiche(configurazioniHandler.getAnagrafichePraticaModulistica(true));
	}
	
	@Override
	public String getDescrizione() {
		return "Ricerca per dati Modulistica";
	}

	private void initSuggestBoxStati() {
		List<String> suggestions = new ArrayList<String>();
		for (StatoDTO stato : StatoDTO.values()) {
			suggestions.add(stato.getLabel());
		}
		SpacebarSuggestOracle oracle = new SpacebarSuggestOracle(suggestions);
		statoSuggestBox = new SuggestBox(oracle);
		statoSuggestBox.setValue(StatoDTO.IN_GESTIONE.getLabel());
	}

	// serve per forzare il valore del campo di ricerca relativo allo stato dei fascicoli
	public void refreshSuggestBoxStati() {
		statoSuggestBox.setText(StatoDTO.IN_GESTIONE.getLabel());
	}

	@Override
	public void serializza(CercaPratiche dto) {
		StatoDTO stato = StatoDTO.fromLabel(statoSuggestBox.getText().trim());
		if (stato != null)
			dto.setStato(new String[] { stato.name() });
	}

	@Override
	public void configura(final com.google.gwt.user.client.Command cercaCommand) {
		super.configura(cercaCommand);
		configuraCampo(statoSuggestBox, "Stato");
	}

	@Override
	public void reset() {
		statoSuggestBox.setText(null);

	}

}
