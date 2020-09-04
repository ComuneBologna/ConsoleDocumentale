package it.eng.portlet.consolepec.gwt.client.widget.formricerca;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class FormRicercaWorklistPraticaModulistica extends FormRicercaBase {
	private static FormRicercaPraticaModulisticaWidgetUiBinder uiBinder = GWT.create(FormRicercaPraticaModulisticaWidgetUiBinder.class);

	interface FormRicercaPraticaModulisticaWidgetUiBinder extends UiBinder<Widget, FormRicercaWorklistPraticaModulistica> {
	}

	@UiField(provided = true)
	GruppoFiltriPraticaModulisticaWidget avanzatePraticaModulistica;
		
	@Inject
	public FormRicercaWorklistPraticaModulistica(ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super();
		this.avanzatePraticaModulistica = new GruppoFiltriPraticaModulisticaWidget(profilazioneUtenteHandler, configurazioniHandler);
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void configura(Command cercaCommand) {
		super.configura(cercaCommand);
		applicaConfigurazionePerTipo(ConfigurazioneCampiFactory.getConfigurazione(TipologiaPratica.PRATICA_MODULISTICA));
		avanzatePraticaModulistica.configura(cercaCommand);
	}

	@Override
	protected void serializza(CercaPratiche dto) {
		super.serializza(dto);
		avanzatePraticaModulistica.serializza(dto);
	}

	@Override
	protected void resetAvanzate() {
		super.resetAvanzate();
		avanzatePraticaModulistica.reset();
	}

	public void resetParziale() {
		resetForm(); // chiama anche resetAvanzate()
		avanzatePraticaModulistica.refreshSuggestBoxStati();
	}

	@Override
	protected void pulisci() {
		super.pulisci();
	}

}
