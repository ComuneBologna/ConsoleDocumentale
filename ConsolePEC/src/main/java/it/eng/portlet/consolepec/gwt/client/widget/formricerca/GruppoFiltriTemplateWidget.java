package it.eng.portlet.consolepec.gwt.client.widget.formricerca;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class GruppoFiltriTemplateWidget extends AbstractGruppoFiltriWidget {

	private static FormRicercaTemplateWidgetUiBinder uiBinder = GWT.create(FormRicercaTemplateWidgetUiBinder.class);

	interface FormRicercaTemplateWidgetUiBinder extends UiBinder<Widget, GruppoFiltriTemplateWidget> {
	}

	protected GruppoFiltriTemplateWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public GruppoFiltriTemplateWidget(ProfilazioneUtenteHandler profilazioneUtenteHandler, ConfigurazioniHandler configurazioniHandler) {
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

	}

	@Override
	public void serializza(CercaPratiche dto) {

	}

	@Override
	public void reset() {

	}

	@Override
	public List<TipologiaPratica> getTipiPraticheGestite() {
		return PraticaUtil.modelliToTipologiePratiche(configurazioniHandler.getAnagraficheModelli(true));
	}

	@Override
	public String getDescrizione() {
		return "Ricerca per dati template";
	}

}
