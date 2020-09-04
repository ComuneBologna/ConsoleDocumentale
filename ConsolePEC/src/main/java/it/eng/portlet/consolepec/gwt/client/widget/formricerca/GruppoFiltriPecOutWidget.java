package it.eng.portlet.consolepec.gwt.client.widget.formricerca;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.model.Destinatario.StatoDestinatario;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO.StatoDTO;

public class GruppoFiltriPecOutWidget extends AbstractGruppoFiltriPecWidget {

	private static FormRicercaPecOutWidgetUiBinder uiBinder = GWT.create(FormRicercaPecOutWidgetUiBinder.class);

	@UiField(provided = true)
	CheckBox ricevutaAccettazioneCheckBox;
	@UiField(provided = true)
	CheckBox ricevutaConsegnaCheckBox;
	@UiField(provided = true)
	SuggestBox statoDestinatarioSuggestBox;

	private boolean ricevutaAccettazione;
	private boolean ricevutaConsegna;

	interface FormRicercaPecOutWidgetUiBinder extends UiBinder<Widget, GruppoFiltriPecOutWidget> {}

	public GruppoFiltriPecOutWidget(ProfilazioneUtenteHandler profilazioneUtenteHandler, ConfigurazioniHandler configurazioniHandler) {
		super();

		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		this.configurazioniHandler = configurazioniHandler;

		// Istanzio le checkBox e associo ad esse i rispettivi handler
		ricevutaAccettazioneCheckBox = new CheckBox(" Con ricevuta di accettazione ");
		ricevutaAccettazioneCheckBox.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				boolean checked = ((CheckBox) event.getSource()).getValue();
				ricevutaAccettazione = checked;
			}
		});

		ricevutaConsegnaCheckBox = new CheckBox(" Con ricevuta di consegna ");
		ricevutaConsegnaCheckBox.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				boolean checked = ((CheckBox) event.getSource()).getValue();
				ricevutaConsegna = checked;
			}
		});

		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public List<TipologiaPratica> getTipiPraticheGestite() {
		return PraticaUtil.mailInUscitaToTipologiePratiche(configurazioniHandler.getAnagraficheMailInUscita(true));
	}

	@Override
	public String getDescrizione() {
		return "Ricerca per dati Pec in uscita";
	}

	@Override
	protected void initSuggestBoxStati() {

		TreeSet<String> suggestions = new TreeSet<String>();

		for (StatoDTO stato : StatoDTO.values())
			suggestions.add(stato.getLabel());

		SpacebarSuggestOracle oracle = new SpacebarSuggestOracle(new ArrayList<String>(suggestions));
		statoSuggestBox = new SuggestBox(oracle);
		statoSuggestBox.setValue(StatoDTO.IN_GESTIONE.getLabel());

		TreeSet<String> suggestionsDest = new TreeSet<String>();
		for (StatoDestinatario stato : StatoDestinatario.values())
			suggestionsDest.add(stato.getLabel());

		SpacebarSuggestOracle oracleDest = new SpacebarSuggestOracle(new ArrayList<String>(suggestionsDest));
		statoDestinatarioSuggestBox = new SuggestBox(oracleDest);
		statoDestinatarioSuggestBox.setValue(null);

	}

	@Override
	public void configura(Command cercaCommand) {
		super.configura(cercaCommand);
		setPlaceholder(statoDestinatarioSuggestBox.getElement(), "Stato destinatario");
	}

	@Override
	public void serializza(CercaPratiche dto) {
		super.serializza(dto);
		dto.setHasRicevutaAccettazione(ricevutaAccettazione);
		dto.setHasRicevutaConsegna(ricevutaConsegna);
		StatoDestinatario sd = StatoDestinatario.fromLabel(statoDestinatarioSuggestBox.getValue().trim());
		dto.setStatoDestinatario(sd);
	}

	// Resetto i valori dei flag e delle checkBox a false
	@Override
	public void reset() {
		super.reset();
		ricevutaAccettazioneCheckBox.setValue(false);
		ricevutaAccettazione = false;
		ricevutaConsegnaCheckBox.setValue(false);
		ricevutaConsegna = false;
		statoDestinatarioSuggestBox.setValue(null);
	}

}
