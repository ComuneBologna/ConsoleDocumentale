package it.eng.portlet.consolepec.gwt.client.view.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.template.WorklistTemplatePresenter;
import it.eng.portlet.consolepec.gwt.client.widget.CustomPager;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget;
import it.eng.portlet.consolepec.gwt.client.widget.formricerca.FormRicercaWorklistTemplate;
import it.eng.portlet.consolepec.gwt.client.widget.images.ResPager;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.CheckRigaEventListener;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO.StatoTemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

public class WorklistTemplateView extends ViewImpl implements WorklistTemplatePresenter.MyView {

	private final Widget widget;

	@UiField(provided = true)
	DataGridWidget<PraticaDTO> dataGrid = new DataGridWidget<PraticaDTO>(ConsolePecConstants.WORKLIST_NUMERO_PER_PAGINA, WorklistStrategy.PRATICA_KEY_PROVIDER, WorklistStrategy.PRATICA_EVID_PROVIDER);
	@UiField(provided = true)
	CustomPager pager;
	@UiField
	HTMLPanel cellTablePratichePanel;
	@UiField
	HTMLPanel dettaglioPanel;
	@UiField(provided = true)
	FormRicercaWorklistTemplate formRicerca;
	@UiField
	HeadingElement titoloTemplate;
	@UiField(provided = true)
	MessageAlertWidget messageWidget;

	private WorklistStrategy strategy;
	private ConfigurazioniHandler configurazioniHandler;

	public interface Binder extends UiBinder<Widget, WorklistTemplateView> {}

	@Inject
	public WorklistTemplateView(final Binder binder, final EventBus eventBus, ConfigurazioniHandler configurazioniHandler) {

		pager = new CustomPager(SimplePager.TextLocation.CENTER, ResPager.CSS, false, 0, true, configurazioniHandler.getProprietaGenerali().getMaxRisultatiWorklist());
		this.configurazioniHandler = configurazioniHandler;
		this.formRicerca = new FormRicercaWorklistTemplate(configurazioniHandler);
		messageWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		Window.scrollTo(0, 0);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void init(final Command cercaPraticheCommand, final WorklistStrategy worklistStrategy) {

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
		formRicerca.setElencoTipoPratiche(PraticaUtil.modelliToTipologiePratiche(configurazioniHandler.getAnagraficheModelli(true)));

		List<String> suggestionsStato = new ArrayList<String>();
		for (StatoTemplateDTO st : StatoTemplateDTO.values())
			suggestionsStato.add(st.getDescrizioneStato());
		formRicerca.setElencoStatiTemplate(suggestionsStato);
	}

	@Override
	public Set<ConstraintViolation<CercaPratiche>> formRicercaToCercaPratiche(CercaPratiche dto) {
		Set<ConstraintViolation<CercaPratiche>> val1 = formRicerca.serializzaEValida(dto);
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
	public void impostaTitolo(String titolo) {
		titoloTemplate.setInnerText(titolo);
	}

	@Override
	public void resetRicerca() {
		formRicerca.resetForm();
	}

	@Override
	public void setGruppiAbilitati(List<AnagraficaRuolo> gruppiAbilitati) {
		formRicerca.setGruppiAbilitati(gruppiAbilitati);

	}

}
