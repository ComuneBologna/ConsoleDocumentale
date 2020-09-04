package it.eng.portlet.consolepec.gwt.client.view.modulistica;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.RicercaCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.modulistica.WorklistPraticaModulisticaPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.modulistica.WorklistPraticaModulisticaPresenter.AssegnaCommand;
import it.eng.portlet.consolepec.gwt.client.widget.CustomPager;
import it.eng.portlet.consolepec.gwt.client.widget.CustomSuggestBox;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.GroupSuggestBoxProtocollazione;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget;
import it.eng.portlet.consolepec.gwt.client.widget.formricerca.FormRicercaBase;
import it.eng.portlet.consolepec.gwt.client.widget.formricerca.FormRicercaWorklistPraticaModulistica;
import it.eng.portlet.consolepec.gwt.client.widget.formricerca.GruppoFiltriDatiProtocollazioneWidget;
import it.eng.portlet.consolepec.gwt.client.widget.images.ResPager;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.CheckRigaEventListener;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

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
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

public class WorklistPraticaModulisticaView extends ViewImpl implements WorklistPraticaModulisticaPresenter.MyView {

	@UiField(provided = true)
	DataGridWidget<PraticaDTO> dataGrid = new DataGridWidget<PraticaDTO>(ConsolePecConstants.WORKLIST_NUMERO_PER_PAGINA, WorklistStrategy.PRATICA_KEY_PROVIDER, WorklistStrategy.PRATICA_EVID_PROVIDER);
	@UiField(provided = true)
	CustomPager pager;
	@UiField
	Button archiviaButton;
	@UiField
	Button eliminaButton;
	@UiField
	HTMLPanel dettaglioPanel;
	@UiField
	HTMLPanel cellTablePanel;
	@UiField
	Label errorLabel;
	@UiField
	HTMLPanel firmaPanel;
	@UiField
	DownloadAllegatoWidget downloadWidget;

	@UiField(provided = true)
	MessageAlertWidget messageWidget;

	@UiField
	Button riportaInGestioneButton;
	@UiField
	Button assegnaButton;

	@UiField
	HTMLPanel pannelloRicerca;
	private Map<String, FormRicercaBase> formRicercaPerNomeWorklistMap = new HashMap<String, FormRicercaBase>();
	FormRicercaWorklistPraticaModulistica formRicercaAttivo;

	@UiField
	HeadingElement titolo;

	private final Widget widget;
	private WorklistStrategy strategy;
	private ConfigurazioniHandler configurazioniHandler;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	public interface Binder extends UiBinder<Widget, WorklistPraticaModulisticaView> {
	}

	@Override
	public void setInSlot(Object slot, IsWidget content) {
		super.setInSlot(slot, content);
	}
	
	@Override
	protected void onAttach() {
		super.onAttach();
		Window.scrollTo(0, 0);
	}

	@Inject
	public WorklistPraticaModulisticaView(final Binder binder, final EventBus eventBus, ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		pager = new CustomPager(SimplePager.TextLocation.CENTER, ResPager.CSS, false, 0, true, configurazioniHandler.getProprietaGenerali().getMaxRisultatiWorklist());
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		this.configurazioniHandler = configurazioniHandler;
		messageWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
	}

	@Override
	public void setFormRicerca(final AnagraficaWorklist worklistConfiguration, RicercaCommand ricercaCommand, GroupSuggestBoxProtocollazione groupSuggestBoxProtocollazione) {

		if(!formRicercaPerNomeWorklistMap.containsKey(worklistConfiguration.getNome())){

			FormRicercaBase formRicerca = new FormRicercaWorklistPraticaModulistica(configurazioniHandler, profilazioneUtenteHandler);
			formRicercaPerNomeWorklistMap.put(worklistConfiguration.getNome(), formRicerca);

			pannelloRicerca.clear();
			pannelloRicerca.add(formRicerca);
			formRicercaAttivo = (FormRicercaWorklistPraticaModulistica) formRicerca;

			formRicercaAttivo.configura(ricercaCommand);
			GruppoFiltriDatiProtocollazioneWidget proto = new GruppoFiltriDatiProtocollazioneWidget(profilazioneUtenteHandler, configurazioniHandler);
			CustomSuggestBox[] trs = groupSuggestBoxProtocollazione.getSuggestBoxTitoloRubricaSezione(null);
			proto.configura(ricercaCommand, groupSuggestBoxProtocollazione.getSuggestBoxTipologiaDocumento(), trs[0], trs[1], trs[2]);
			formRicercaAttivo.addGruppoFiltriFissi(proto);

			pager.firstPage();
		}

		pannelloRicerca.clear();
		FormRicercaBase formRicerca = formRicercaPerNomeWorklistMap.get(worklistConfiguration.getNome());
		pannelloRicerca.add(formRicerca);

		if(formRicercaAttivo != formRicerca){
			pager.firstPage();
		}

		formRicercaAttivo = (FormRicercaWorklistPraticaModulistica) formRicerca;

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void init(WorklistStrategy strategy, final Command selezionaRigaCommand) {
		this.strategy = strategy;
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
		Set<ConstraintViolation<CercaPratiche>> res1 = formRicercaAttivo.serializzaEValida(dto);
		return res1;
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
	public void aggiornaRiga(PraticaDTO fascicolo) {
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
	public void setArchiviaCommand(final it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<String>> archivia) {
		this.archiviaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				archivia.exe(getIDRigheSelezionate());
			}
		});
	}

	@Override
	public void setEliminaCommand(final it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<String>> eliminaCommand) {
		this.eliminaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				eliminaCommand.exe(getIDRigheSelezionate());
			}
		});
	}

	@Override
	public void setRiportaInGestioneCommand(final it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<String>> riportaInGestione) {
		this.riportaInGestioneButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				riportaInGestione.exe(getIDRigheSelezionate());

			}
		});
	}

	@Override
	public void setAssegnaCommand(final AssegnaCommand assegnaCommand) {
		this.assegnaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				assegnaCommand.exe(getIDRigheSelezionate());
			}
		});

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
		formRicercaAttivo.resetParziale();
	}

	@Override
	public void impostaTitolo(String titoloWorklistModulo) {
		titolo.setInnerText(titoloWorklistModulo);
	}

	@Override
	public void setGruppiAbilitati(List<AnagraficaRuolo> gruppiAbilitati) {
		formRicercaAttivo.setGruppiAbilitati(gruppiAbilitati);

	}

}
