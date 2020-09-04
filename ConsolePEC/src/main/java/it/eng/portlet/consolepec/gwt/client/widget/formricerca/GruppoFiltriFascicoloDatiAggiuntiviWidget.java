package it.eng.portlet.consolepec.gwt.client.widget.formricerca;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.widget.datiaggiuntivi.FormDatiAggiuntiviWidget;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;

public class GruppoFiltriFascicoloDatiAggiuntiviWidget extends AbstractGruppoFiltriWidget {

	private static FormRicercaFascicoloDatiAggiuntiviWidgetUiBinder uiBinder = GWT.create(FormRicercaFascicoloDatiAggiuntiviWidgetUiBinder.class);

	interface FormRicercaFascicoloDatiAggiuntiviWidgetUiBinder extends UiBinder<Widget, GruppoFiltriFascicoloDatiAggiuntiviWidget> {/**/}

	@UiField
	FormDatiAggiuntiviWidget formDatiAggiuntiviWidget;

	private List<TipologiaPratica> tipiPraticaGestiti = new ArrayList<TipologiaPratica>();
	private Set<DatoAggiuntivo> descrittoriPredifinitiDaConfigurazione = new HashSet<DatoAggiuntivo>();

	protected GruppoFiltriFascicoloDatiAggiuntiviWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public GruppoFiltriFascicoloDatiAggiuntiviWidget(ProfilazioneUtenteHandler profilazioneUtenteHandler, ConfigurazioniHandler configurazioniHandler) {
		this();
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		this.configurazioniHandler = configurazioniHandler;
	}

	/**
	 * Il metodo viene invocato dalla View su cui è utilizzata la form. Deve essere chiamato una volta sola
	 *
	 * @param cercaCommand - Command invocato quando si preme il pulsante Cerca, oppure invio su di una widget
	 */
	@Override
	public void configura(final com.google.gwt.user.client.Command cercaCommand) {
		super.configura(cercaCommand);
	}

	@Override
	public void serializza(CercaPratiche dto) {
		dto.setDatiAggiuntivi(formDatiAggiuntiviWidget.getDatiAggiuntiviPerRicerca());
	}

	@Override
	public void reset() {
		formDatiAggiuntiviWidget.resetDatiAggiuntivi();
		hideParentPanel();

		creaPannelloDatiAggiuntivi(new HashSet<DatoAggiuntivo>()); // aggiungo i descrittori da configurazione
	}

	@Override
	public List<TipologiaPratica> getTipiPraticheGestite() {
		return tipiPraticaGestiti;
	}

	@Override
	public String getDescrizione() {
		return "Ricerca per dati aggiuntivi";
	}

	public void setTipiPraticaGestite(List<TipologiaPratica> tipiPraticaGestiti) {

		if (tipiPraticaGestiti != null) {
			for (TipologiaPratica tp : tipiPraticaGestiti) {
				if (PraticaUtil.isFascicolo(tp) && !PraticaUtil.isFascicoloPersonale(tp)) {
					this.tipiPraticaGestiti.add(tp);
				}
			}
		}
	}

	@Override
	protected void onTipoPraticaSelection(TipologiaPratica tp) {
		AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicolo(tp.getNomeTipologia());
		if (af != null) {
			Set<DatoAggiuntivo> descrittori = new HashSet<DatoAggiuntivo>(af.getDatiAggiuntivi());
			creaPannelloDatiAggiuntivi(descrittori);
		}
	}

	@Override
	public boolean hasFiltriPersonalizzati() {
		List<DatoAggiuntivo> filtriRicercaAggiuntivi = profilazioneUtenteHandler.getFiltriRicercaAggiuntivi();

		if (filtriRicercaAggiuntivi != null) {
			descrittoriPredifinitiDaConfigurazione.addAll(filtriRicercaAggiuntivi);
		}

		boolean visibile = !descrittoriPredifinitiDaConfigurazione.isEmpty();

		if (visibile) {
			creaPannelloDatiAggiuntivi(new HashSet<DatoAggiuntivo>()); // aggiungo i descrittori da configurazione
		}
		return visibile;
	}

	private void creaPannelloDatiAggiuntivi(Set<DatoAggiuntivo> descrittori) {
		Set<DatoAggiuntivo> dati = new HashSet<DatoAggiuntivo>();
		dati.addAll(descrittoriPredifinitiDaConfigurazione);

		for (DatoAggiuntivo dag : descrittori) {
			boolean f = false;
			for (DatoAggiuntivo dag2 : descrittoriPredifinitiDaConfigurazione) {
				if (dag.getNome().equals(dag2.getNome())) {
					f = true;
					break;
				}
			}
			if (!f) {
				dati.add(dag);
			}
		}

		if (dati != null && !dati.isEmpty()) {
			formDatiAggiuntiviWidget.setDatiAggiuntiviPerRicerca(dati);
			showParentPanel();
		} else {
			hideParentPanel();
		}
	}
}
