package it.eng.portlet.consolepec.gwt.client.widget.formricerca;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class GruppoFiltriPraticaProcediWidget extends AbstractGruppoFiltriWidget {

	private static FormRicercaPraticaProcediWidgetUiBinder uiBinder = GWT.create(FormRicercaPraticaProcediWidgetUiBinder.class);

	interface FormRicercaPraticaProcediWidgetUiBinder extends UiBinder<Widget, GruppoFiltriPraticaProcediWidget> {
	}

	@UiField
	TextBox tipoPraticaProcedi;
	@UiField
	TextBox ambito;
	@UiField
	TextBox indirizzoVia;
	@UiField
	TextBox indirizzoCivico;
	@UiField
	TextBox nominativo;
	
	protected GruppoFiltriPraticaProcediWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public GruppoFiltriPraticaProcediWidget(ProfilazioneUtenteHandler profilazioneUtenteHandler, ConfigurazioniHandler configurazioniHandler) {
		this();
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		this.configurazioniHandler = configurazioniHandler;
	}

	/**
	 * Il metodo viene invocato dalla View su cui è utilizzata la form. Deve essere chiamato una volta sola
	 * 
	 * @param cercaCommand
	 *            - Command invocato quando si preme il pulsante Cerca, oppure invio su di una widget
	 */
	@Override
	public void configura(final com.google.gwt.user.client.Command cercaCommand) {
		super.configura(cercaCommand);
		configuraCampo(tipoPraticaProcedi, "Tipo Pratica");
		configuraCampo(ambito, "Ambito");
		configuraCampo(indirizzoVia, "Indirizzo");
		configuraCampo(indirizzoCivico, "Civico");
		configuraCampo(nominativo, "Nominativo");
	}

	@Override
	public void serializza(CercaPratiche dto) {
		dto.setTipoPraticaProcedi(tipoPraticaProcedi.getText());
		dto.setAmbito(ambito.getText());
		dto.setIndirizzoVia(indirizzoVia.getText());
		dto.setIndirizzoCivico(indirizzoCivico.getText());
		dto.setCognomeNome(nominativo.getText());
	}

	@Override
	public void reset() {
		tipoPraticaProcedi.setText(null);
		ambito.setText(null);
		indirizzoVia.setText(null);
		indirizzoCivico.setText(null);
		nominativo.setText(null);
	}

	@Override
	public List<TipologiaPratica> getTipiPraticheGestite() {
		return Arrays.asList(TipologiaPratica.PRATICA_PROCEDI);
	}

	@Override
	public String getDescrizione() {
		return "Ricerca per dati pratica procedi";
	}


}
