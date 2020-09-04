package it.eng.portlet.consolepec.gwt.client.view.fascicolo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.SceltaFascicoloPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.CustomPager;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget;
import it.eng.portlet.consolepec.gwt.client.widget.formricerca.FormRicercaSceltaFascicolo;
import it.eng.portlet.consolepec.gwt.client.widget.images.ResPager;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.CheckRigaEventListener;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

public class SceltaFascicoloView extends ViewImpl implements SceltaFascicoloPresenter.MyView {
	@UiField(provided = true)
	DataGridWidget<PraticaDTO> dataGrid = new DataGridWidget<PraticaDTO>(ConsolePecConstants.WORKLIST_NUMERO_PER_PAGINA, WorklistStrategy.PRATICA_KEY_PROVIDER, WorklistStrategy.PRATICA_EVID_PROVIDER);
	@UiField(provided = true)
	CustomPager pager;
	@UiField
	HTMLPanel cellTablePratichePanel;
	@UiField
	HTMLPanel dettaglioPanel;
	@UiField(provided = true)
	FormRicercaSceltaFascicolo formRicerca;

	@UiField
	Button annullaButton;
	@UiField
	Button avantiButton;

	@UiField(provided = true)
	MessageAlertWidget messaggioAlertWidget;

	private final Widget widget;
	private WorklistStrategy strategy;
	private String clientIdSlezionato;
	private Command annullaCommand;
	private Command avantiCommand;

	public interface Binder extends UiBinder<Widget, SceltaFascicoloView> {}

	@Override
	protected void onAttach() {
		super.onAttach();
		Window.scrollTo(0, 0);
	}

	@Inject
	public SceltaFascicoloView(final Binder binder, final EventBus eventBus, ConfigurazioniHandler configurazioniHandler) {

		pager = new CustomPager(SimplePager.TextLocation.CENTER, ResPager.CSS, false, 0, true, configurazioniHandler.getProprietaGenerali().getMaxRisultatiWorklist());
		formRicerca = new FormRicercaSceltaFascicolo(configurazioniHandler);

		messaggioAlertWidget = new MessageAlertWidget(eventBus);

		widget = binder.createAndBindUi(this);

		annullaButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				annullaCommand.execute();
			}
		});
		avantiButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				avantiCommand.execute();
			}
		});
	}

	@Override
	public Widget asWidget() {

		return widget;
	}

	@Override
	public Set<ConstraintViolation<CercaPratiche>> formRicercaToCercaPratiche(CercaPratiche dto) {
		return formRicerca.serializzaEValida(dto);
	}

	public Set<String> idSelezionati = new HashSet<String>();

	@Override
	public void init(final WorklistStrategy worklistStrategy, final Command cercaPraticheCommand) {
		this.strategy = worklistStrategy;
		strategy.configuraGrid(dataGrid, pager);
		/* handler per notificare il presenter dell'avvenuta selezione riga */
		strategy.addCheckRigaEventListener(new CheckRigaEventListener() {
			@Override
			public void onCheckRiga(String clientID, boolean checked) {

				if (checked) {
					idSelezionati.add(clientID);
					clientIdSlezionato = clientID;
				} else {
					idSelezionati.remove(clientID);
				}
				if (idSelezionati.size() > 0) {
					avantiButton.setEnabled(true);
				} else {
					avantiButton.setEnabled(false);
				}
			}
		});
		formRicerca.configura(cercaPraticheCommand);

	}

	@Override
	public String getClientIdSlezionato() {
		return clientIdSlezionato;
	}

	@Override
	public void setAvantiCommand(Command avantiCommand) {
		this.avantiCommand = avantiCommand;
	}

	@Override
	public void setAnnullaCommand(Command annullaCommand) {
		this.annullaCommand = annullaCommand;
	}

	@Override
	public String getClientIdFascicoloSelezionato() {
		return clientIdSlezionato;
	}

	@Override
	public void resetSelezioni() {
		strategy.resetSelezioni();
	}

	@Override
	public void aggiornaRigheSelezionate() {
		strategy.aggiornaRigheSelezionate();
	}

	@Override
	public void setProvenienza(String indirizzoMail) {
		formRicerca.setProvenienza(indirizzoMail);
	}

	@Override
	public String getProvenienza() {
		return formRicerca.getProvenienza();
	}

	@Override
	public void resetForm() {
		formRicerca.resetForm();
	}

	@Override
	public void setGruppiAbilitati(List<AnagraficaRuolo> gruppiAbilitati) {
		formRicerca.setGruppiAbilitati(gruppiAbilitati);
	}
}
