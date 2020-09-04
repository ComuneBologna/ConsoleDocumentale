package it.eng.portlet.consolepec.gwt.client.view;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.RicercaLiberaPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.CustomPager;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget.ProvidesElementoEvidenziatoInfo;
import it.eng.portlet.consolepec.gwt.client.widget.formricerca.AbstractGruppoFiltriPecWidget;
import it.eng.portlet.consolepec.gwt.client.widget.formricerca.ConfigurazioneCampiFactory;
import it.eng.portlet.consolepec.gwt.client.widget.formricerca.FormRicercaLibera;
import it.eng.portlet.consolepec.gwt.client.widget.formricerca.GruppoFiltriDatiProtocollazioneWidget;
import it.eng.portlet.consolepec.gwt.client.widget.formricerca.GruppoFiltriFascicoloDatiAggiuntiviWidget;
import it.eng.portlet.consolepec.gwt.client.widget.formricerca.GruppoFiltriFascicoloWidget;
import it.eng.portlet.consolepec.gwt.client.widget.formricerca.GruppoFiltriPecInWidget;
import it.eng.portlet.consolepec.gwt.client.widget.formricerca.GruppoFiltriPecOutWidget;
import it.eng.portlet.consolepec.gwt.client.widget.formricerca.GruppoFiltriPraticaProcediWidget;
import it.eng.portlet.consolepec.gwt.client.widget.images.ResPager;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.CheckRigaEventListener;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

public class RicercaLiberaView extends ViewImpl implements RicercaLiberaPresenter.MyView {

	private final Widget widget;
	@UiField(provided = true)
	DataGridWidget<PraticaDTO> dataGrid = new DataGridWidget<PraticaDTO>(ConsolePecConstants.WORKLIST_NUMERO_PER_PAGINA, WorklistStrategy.PRATICA_KEY_PROVIDER, new ElementoEvidenziatoRicercaLiberaInfoProvider());
	@UiField(provided = true)
	CustomPager pager;
	@UiField(provided = true)
	FormRicercaLibera formRicerca;
	@UiField
	DownloadAllegatoWidget downloadWidget;
	@UiField(provided = true)
	MessageAlertWidget messageWidget;

	private WorklistStrategy strategy;
	private GruppoFiltriFascicoloDatiAggiuntiviWidget datiAggiuntivi;
	
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;
	private ConfigurazioniHandler configurazioniHandler;

	public interface Binder extends UiBinder<Widget, RicercaLiberaView> {
	}

	@Inject
	public RicercaLiberaView(final Binder binder, final EventBus eventBus, ProfilazioneUtenteHandler profilazioneUtenteHandler, ConfigurazioniHandler configurazioniHandler) {
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		this.configurazioniHandler = configurazioniHandler;
		
		pager = new CustomPager(SimplePager.TextLocation.CENTER, ResPager.CSS, false, 0, true, configurazioniHandler.getProprietaGenerali().getMaxRisultatiWorklist());
		formRicerca = new FormRicercaLibera(configurazioniHandler);
		messageWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void init(WorklistStrategy worklistStrategy, Command cercaPraticheCommand) {
		/* configurazione form di ricerca e widget */
		this.strategy = worklistStrategy;
		strategy.configuraGrid(dataGrid, pager);
		/* handler per notificare il presenter dell'avvenuta selezione riga */
		strategy.addCheckRigaEventListener(new CheckRigaEventListener() {

			@Override
			public void onCheckRiga(String clientID, boolean checked) {
			}
		});

		formRicerca.configura(cercaPraticheCommand);
		AbstractGruppoFiltriPecWidget pecin = new GruppoFiltriPecInWidget(profilazioneUtenteHandler, configurazioniHandler);
		pecin.configura(cercaPraticheCommand);
		GruppoFiltriPecOutWidget pecout = new GruppoFiltriPecOutWidget(profilazioneUtenteHandler, configurazioniHandler);
		pecout.configura(cercaPraticheCommand);
		GruppoFiltriFascicoloWidget fascicolo = new GruppoFiltriFascicoloWidget(profilazioneUtenteHandler, configurazioniHandler);
		fascicolo.configura(cercaPraticheCommand);
		GruppoFiltriPraticaProcediWidget praticaprocedi = new GruppoFiltriPraticaProcediWidget(profilazioneUtenteHandler, configurazioniHandler);
		praticaprocedi.configura(cercaPraticheCommand);

		datiAggiuntivi = new GruppoFiltriFascicoloDatiAggiuntiviWidget(profilazioneUtenteHandler, configurazioniHandler);
		datiAggiuntivi.configura(cercaPraticheCommand);

		formRicerca.addGruppoFiltri(pecin);		
		formRicerca.addConfigurazioneCampi(ConfigurazioneCampiFactory.getConfigurazione(TipologiaPratica.EMAIL_IN));
		formRicerca.addGruppoFiltri(pecout);
		formRicerca.addConfigurazioneCampi(ConfigurazioneCampiFactory.getConfigurazione(TipologiaPratica.EMAIL_OUT));
		formRicerca.addGruppoFiltri(fascicolo);
		formRicerca.addGruppoFiltri(praticaprocedi);
		formRicerca.addGruppoFiltri(datiAggiuntivi);
		formRicerca.filtriPersonalizzati();
		
		List<TipologiaPratica> all = PraticaUtil.toTipologiePratiche(configurazioniHandler.getAnagraficheFascicoli(true), configurazioniHandler.getAnagraficheIngressi(true),
				configurazioniHandler.getAnagraficheMailInUscita(true), configurazioniHandler.getAnagraficheComunicazioni(true), configurazioniHandler.getAnagrafichePraticaModulistica(true)
				,configurazioniHandler.getAnagraficheModelli(true));
		setTipoPratiche(all);
		setRicercaLiberaBloccata(!(all.size() == 1));
	}

	@Override
	public Set<ConstraintViolation<CercaPratiche>> formRicercaToCercaPratiche(CercaPratiche action) {
		return formRicerca.serializzaEValida(action);
	}

	@Override
	public void espandiRiga(String rowAlfrescoPath, PraticaDTO pratica) {
		strategy.espandiRiga(rowAlfrescoPath, pratica);
	}

	@Override
	public void nascondiRiga(String rowAlfrescoPath) {
		dataGrid.chiudiRigaByKey(rowAlfrescoPath);
	}

	@Override
	public List<PraticaDTO> getRigheEspanse() {
		return dataGrid.getElencoRigheEspanse();
	}

	@Override
	public void aggiornaRiga(PraticaDTO pratica) {
		strategy.aggiornaRiga(pratica);
	}

	@Override
	public void sendDownload(SafeUri uri) {
		downloadWidget.sendDownload(uri);
	}

	@Override
	public void updateRigheEspanse() {
		strategy.aggiornaRigheEspanse();
	}

	@Override
	public void resetSelezioni() {
		strategy.resetSelezioni();
	}

	@Override
	public void updateRigheSelezionate() {
		strategy.aggiornaRigheSelezionate();
	}

	@Override
	public void resetRicerca() {
		formRicerca.resetForm();
	}

	/**
	 * In ricerca libera, non mostriamo mai le righe evidenziate
	 *
	 * @author pluttero
	 *
	 */
	class ElementoEvidenziatoRicercaLiberaInfoProvider implements ProvidesElementoEvidenziatoInfo<PraticaDTO> {

		@Override
		public boolean isEvidenziato(PraticaDTO item) {
			return !item.isLetto();
		}

	}

	@Override
	public void initDatiRicercaProtocollazione() {
		GruppoFiltriDatiProtocollazioneWidget proto = new GruppoFiltriDatiProtocollazioneWidget(profilazioneUtenteHandler, configurazioniHandler);
		formRicerca.addGruppoFiltriFissi(proto);

	}

	@Override
	public void setRicercaLiberaBloccata(boolean bloccata) {
		this.formRicerca.intiRicercaLibera(bloccata);
	}

	@Override
	public void setTipoPratiche(List<TipologiaPratica> tipi) {
		formRicerca.setElencoTipoPratiche(tipi);
	}

	@Override
	public void setTipiPraticaGestite(List<TipologiaPratica> tipi) {
		datiAggiuntivi.setTipiPraticaGestite(tipi);
	}

	@Override
	public void setGruppiAbilitati(List<AnagraficaRuolo> gruppiAbilitati) {
		formRicerca.setGruppiAbilitati(gruppiAbilitati);

	}

}
