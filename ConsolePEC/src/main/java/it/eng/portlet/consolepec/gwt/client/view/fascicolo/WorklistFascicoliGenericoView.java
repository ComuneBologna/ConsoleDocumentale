package it.eng.portlet.consolepec.gwt.client.view.fascicolo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.RicercaCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.WorklistFascicoliGenericoPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.CustomPager;
import it.eng.portlet.consolepec.gwt.client.widget.CustomSuggestBox;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.GroupSuggestBoxProtocollazione;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget;
import it.eng.portlet.consolepec.gwt.client.widget.formricerca.FormRicercaBase;
import it.eng.portlet.consolepec.gwt.client.widget.formricerca.FormRicercaWorklistFascicoloGenerico;
import it.eng.portlet.consolepec.gwt.client.widget.formricerca.GruppoFiltriDatiProtocollazioneWidget;
import it.eng.portlet.consolepec.gwt.client.widget.formricerca.GruppoFiltriFascicoloDatiAggiuntiviWidget;
import it.eng.portlet.consolepec.gwt.client.widget.images.ResPager;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.CheckRigaEventListener;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

public class WorklistFascicoliGenericoView extends ViewImpl implements WorklistFascicoliGenericoPresenter.MyView {

	private final Widget widget;
	@UiField(provided = true)
	DataGridWidget<PraticaDTO> dataGrid = new DataGridWidget<PraticaDTO>(ConsolePecConstants.WORKLIST_NUMERO_PER_PAGINA, WorklistStrategy.PRATICA_KEY_PROVIDER, WorklistStrategy.PRATICA_EVID_PROVIDER);
	@UiField(provided = true)
	CustomPager pager;
	@UiField
	HTMLPanel cellTablePratichePanel;
	@UiField
	HTMLPanel dettaglioPanel;
	@UiField
	DownloadAllegatoWidget downloadWidget;
	@UiField
	Button archiviaButton;
	@UiField
	Button eliminaButton;
	@UiField
	Button riportaInGestioneButton;
	@UiField
	Button assegnaButton;

	@UiField
	HTMLPanel pannelloRicerca;
	private Map<String, FormRicercaBase> formRicercaPerNomeWorklistMap = new HashMap<String, FormRicercaBase>();
	FormRicercaWorklistFascicoloGenerico formRicercaAttivo;

	@UiField
	HeadingElement titoloFascicolo;

	@UiField(provided = true)
	MessageAlertWidget messageWidget;

	private WorklistStrategy strategy;
	private GruppoFiltriFascicoloDatiAggiuntiviWidget datiAggiuntivi;
	private ConfigurazioniHandler configurazioniHandler;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	public interface Binder extends UiBinder<Widget, WorklistFascicoliGenericoView> {}

	@Inject
	public WorklistFascicoliGenericoView(final Binder binder, final EventBus eventBus, ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		pager = new CustomPager(SimplePager.TextLocation.CENTER, ResPager.CSS, false, 0, true, configurazioniHandler.getProprietaGenerali().getMaxRisultatiWorklist());
		this.configurazioniHandler = configurazioniHandler;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		messageWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		Window.scrollTo(0, 0);
	}

	@Override
	public void init(final it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<String>> archiviaCommand,
			final it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<String>> riportaInGestioneCommand, final Command selezionaRigaCommand,
			final it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<String>> eliminaCommand, final WorklistStrategy worklistStrategy,
			final it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<String>> assengaCommand) {

		this.assegnaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Set<String> ids = getIDRigheSelezionate();
				assengaCommand.exe(ids);
			}
		});
		this.eliminaButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Set<String> ids = getIDRigheSelezionate();
				eliminaCommand.exe(ids);
			}
		});

		this.archiviaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Set<String> ids = getIDRigheSelezionate();
				archiviaCommand.exe(ids);
			}
		});
		this.riportaInGestioneButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Set<String> ids = getIDRigheSelezionate();
				riportaInGestioneCommand.exe(ids);
			}
		});
		/* configurazione form di ricerca e widget */
		this.strategy = worklistStrategy;
		strategy.configuraGrid(dataGrid, pager);
		/* handler per notificare il presenter dell'avvenuta selezione riga */
		strategy.addCheckRigaEventListener(new CheckRigaEventListener() {

			@Override
			public void onCheckRiga(String clientID, boolean checked) {
				selezionaRigaCommand.execute();
			}
		});

	}

	@Override
	public Set<ConstraintViolation<CercaPratiche>> formRicercaToCercaPratiche(CercaPratiche dto) {
		Set<ConstraintViolation<CercaPratiche>> val1 = formRicercaAttivo.serializzaEValida(dto);
		return val1;
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
	public void setArchiviaButtonEnabled(boolean enable) {
		archiviaButton.setEnabled(enable);
	}

	@Override
	public void setAssegnaButtonEnabled(boolean enable) {
		assegnaButton.setEnabled(enable);
	}

	@Override
	public void setRiportaInGestioneEnabled(boolean enable) {
		riportaInGestioneButton.setEnabled(enable);
	}

	@Override
	public void setEliminaButtonEnabled(boolean enable) {
		eliminaButton.setEnabled(enable);
	}

	@Override
	public void aggiornaRiga(FascicoloDTO fascicolo) {
		strategy.aggiornaRiga(fascicolo);
	}

	@Override
	public Set<String> getIDRigheSelezionate() {
		return strategy.getIdRigheSelezionate();
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
	public void refreshGrid() {
		strategy.refreshDatiGrid();
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
	public void impostaTitolo(String titolo) {
		titoloFascicolo.setInnerText(titolo);
	}

	@Override
	public void resetRicerca() {
		formRicercaAttivo.resetParziale();
	}

	@Override
	public void setGruppiAbilitati(List<AnagraficaRuolo> gruppiAbilitati) {
		formRicercaAttivo.setGruppiAbilitati(gruppiAbilitati);

	}

	/*
	 * @Override public void setDescrittoriDatiAggiuntivi(Map<TipoDTO, Set<DescrittoreDatoAggiuntivoDTO>> descrittoriDatiAggiuntivi) {
	 * datiAggiuntivi.setDescrittoriDatiAggiuntivi(descrittoriDatiAggiuntivi); }
	 */

	@Override
	public void setTipiPraticaGestite(List<TipologiaPratica> tipiPratiche) {
		datiAggiuntivi.setTipiPraticaGestite(tipiPratiche);
	}

	@Override
	public void setTipoPratiche(List<TipologiaPratica> suggestions) {
		formRicercaAttivo.setElencoTipoPratiche(suggestions);
	}

	@Override
	public void setFormRicerca(final AnagraficaWorklist worklistConfiguration, RicercaCommand ricercaCommand, GroupSuggestBoxProtocollazione groupSuggestBoxProtocollazione) {

		if (!formRicercaPerNomeWorklistMap.containsKey(worklistConfiguration.getNome())) {
			FormRicercaBase formRicerca = new FormRicercaWorklistFascicoloGenerico(profilazioneUtenteHandler, configurazioniHandler);
			formRicercaPerNomeWorklistMap.put(worklistConfiguration.getNome(), formRicerca);

			pannelloRicerca.clear();
			pannelloRicerca.add(formRicerca);
			formRicercaAttivo = (FormRicercaWorklistFascicoloGenerico) formRicerca;

			formRicercaAttivo.configura(ricercaCommand);

			// il pannello per la ricerca della protocollaione lo aggiungo comunque, poi verrà reso visibile o meno a seconda del tipo di pratica
			GruppoFiltriDatiProtocollazioneWidget proto = new GruppoFiltriDatiProtocollazioneWidget(profilazioneUtenteHandler, configurazioniHandler);
			CustomSuggestBox[] trs = groupSuggestBoxProtocollazione.getSuggestBoxTitoloRubricaSezione(null);
			proto.configura(ricercaCommand, groupSuggestBoxProtocollazione.getSuggestBoxTipologiaDocumento(), trs[0], trs[1], trs[2]);
			formRicercaAttivo.addGruppoFiltriFissi(proto);

			datiAggiuntivi = new GruppoFiltriFascicoloDatiAggiuntiviWidget(profilazioneUtenteHandler, configurazioniHandler);
			datiAggiuntivi.configura(ricercaCommand);
			formRicercaAttivo.addGruppoFiltriFissi(datiAggiuntivi);

			formRicercaAttivo.setParametriFissiWorklist(worklistConfiguration.getParametriFissiWorklist());

			formRicercaAttivo.setPulisciCommand(new Command() {
				@Override
				public void execute() {
					formRicercaAttivo.setParametriFissiWorklist(worklistConfiguration.getParametriFissiWorklist());
				}
			});
			pager.firstPage();
		}

		pannelloRicerca.clear();
		FormRicercaBase formRicerca = formRicercaPerNomeWorklistMap.get(worklistConfiguration.getNome());
		pannelloRicerca.add(formRicerca);

		if (formRicercaAttivo != formRicerca) {
			pager.firstPage();
		}

		formRicercaAttivo = (FormRicercaWorklistFascicoloGenerico) formRicerca;

	}

}
