package it.eng.portlet.consolepec.gwt.client.widget.formricerca;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO.StatoDTO;

public class GruppoFiltriPecInWidget extends AbstractGruppoFiltriPecWidget {

	private static FormRicercaPecInWidgetUiBinder uiBinder = GWT.create(FormRicercaPecInWidgetUiBinder.class);

	interface FormRicercaPecInWidgetUiBinder extends UiBinder<Widget, GruppoFiltriPecInWidget> {}

	@UiField
	TextBox operatoreTextBox;

	private ConfigurazioniHandler configurazioniHandler;

	protected GruppoFiltriPecInWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public GruppoFiltriPecInWidget(ProfilazioneUtenteHandler profilazioneUtenteHandler, ConfigurazioniHandler configurazioniHandler) {
		this();
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		this.configurazioniHandler = configurazioniHandler;
	}

	@Override
	public List<TipologiaPratica> getTipiPraticheGestite() {
		return PraticaUtil.ingressiToTipologiePratiche(configurazioniHandler.getAnagraficheIngressi(true));
	}

	@Override
	public String getDescrizione() {
		return "Ricerca per dati Pec in ingresso";
	}

	@Override
	protected void initSuggestBoxStati() {
		{
			TreeSet<String> suggestions = new TreeSet<String>();
			for (it.eng.portlet.consolepec.gwt.shared.model.PecInDTO.StatoDTO stato : it.eng.portlet.consolepec.gwt.shared.model.PecInDTO.StatoDTO.values()) {
				suggestions.add(stato.getLabel());
			}
			SpacebarSuggestOracle oracle = new SpacebarSuggestOracle(new ArrayList<String>(suggestions));
			statoSuggestBox = new SuggestBox(oracle);
			statoSuggestBox.setValue(StatoDTO.IN_GESTIONE.getLabel());
		}
	}

	// serve per forzare il valore del campo di ricerca relativo allo stato delle email
	public void refreshSuggestBoxStati() {
		statoSuggestBox.setText(StatoDTO.IN_GESTIONE.getLabel());
	}

	@Override
	public void configura(final com.google.gwt.user.client.Command cercaCommand) {
		super.configura(cercaCommand);
		configuraCampo(operatoreTextBox, "Operatore");
	}

	@Override
	public void serializza(CercaPratiche dto) {
		super.serializza(dto);
		dto.setOperatore(operatoreTextBox.getText());

	}

	@Override
	public void reset() {
		super.reset();
		operatoreTextBox.setText(null);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void setParametriFissiWorklist(Map<String, Object> parametriFissiWorklist) {
		super.setParametriFissiWorklist(parametriFissiWorklist);

		// Stato
		List<String> statiWorklist = new ArrayList<String>();
		if (parametriFissiWorklist.get(ConsoleConstants.FiltriRicerca.STATO_PRATICA.getFiltro()) != null) {
			Object statoParFisso = parametriFissiWorklist.get(ConsoleConstants.FiltriRicerca.STATO_PRATICA.getFiltro());

			if (statoParFisso instanceof String) {
				statiWorklist.add((String) statoParFisso);

			} else if (statoParFisso instanceof List) {
				statiWorklist.addAll((List<String>) statoParFisso);
			}
		}

		StatoDTO stato = StatoDTO.IN_GESTIONE;

		if (!statiWorklist.isEmpty() && statiWorklist.size() == 1) {

			try {
				stato = StatoDTO.valueOf(statiWorklist.get(0));

			} catch (Exception e) {}

		}

		statoSuggestBox.setValue(stato.getLabel());
	}
}
