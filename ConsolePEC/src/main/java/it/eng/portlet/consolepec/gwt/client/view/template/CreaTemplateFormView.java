package it.eng.portlet.consolepec.gwt.client.view.template;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaModello;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.template.CreaTemplateFormPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.template.AbstractCorpoTemplateWidget;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.CampoTemplateDTO;

import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

/**
 *
 * @author biagiot
 *
 */
public class CreaTemplateFormView extends ViewImpl implements CreaTemplateFormPresenter.MyView {

	private final Widget widget;

	@UiField(provided = true)
	MessageAlertWidget messaggioAlertWidget;
	@UiField
	HTMLPanel sceltaTemplatePanel;
	@UiField
	ListBox sceltaTemplateListBox;
	@UiField
	HTMLPanel sceltaTemplateTitle;
	@UiField
	HTMLPanel corpoTemplatePanel;
	@UiField
	Button avantiButton;
	@UiField
	Button annullaButton;

	private AbstractCorpoTemplateWidget<?> corpoTemplateWidget;
	private it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CampoTemplateDTO> aggiungiCampoCommand;
	private it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CampoTemplateDTO> eliminaCampoCommand;
	private ConfigurazioniHandler configurazioniHandler;
	
	public interface Binder extends UiBinder<Widget, CreaTemplateFormView> {
	}

	@Inject
	public CreaTemplateFormView(final Binder binder, final EventBus eventBus, ConfigurazioniHandler configurazioniHandler) {
		this.configurazioniHandler = configurazioniHandler;
		messaggioAlertWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setCreaTemplateCommand(final Command avantiCreaTemplateCommand) {
		this.avantiButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				avantiCreaTemplateCommand.execute();
			}
		});
	}

	@Override
	public void setAnnullaCreazioneCommand(final Command annullaCommand) {
		this.annullaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				annullaCommand.execute();
			}
		});
	}

	@Override
	public void setAggiungiCampoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CampoTemplateDTO>  aggiungiCampoCommand) {
		this.aggiungiCampoCommand = aggiungiCampoCommand;
	}


	@Override
	public void setEliminaCampoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CampoTemplateDTO>  eliminaCampoCommand) {
		this.eliminaCampoCommand = eliminaCampoCommand;
	}

	@Override
	public void addCampo(CampoTemplateDTO obj) {
		corpoTemplateWidget.addCampo(obj);
	}


	@Override
	public void removeCampo(CampoTemplateDTO obj) {
		corpoTemplateWidget.removeCampo(obj);
	}

	@Override
	public boolean controlloCampi(List<String> errori) {
		return corpoTemplateWidget.validateForm(errori);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseTemplateDTO> T getTemplate() {
		return (T) corpoTemplateWidget.getTemplate();
	}

	@Override
	public void clearFormCampi() {
		corpoTemplateWidget.clearFormCampi();
	}

	@Override
	public void abilitaSceltaTemplate(boolean abiltiazione) {
		sceltaTemplateTitle.setVisible(abiltiazione);
		sceltaTemplatePanel.setVisible(abiltiazione);
	}

	@Override
	public void abilitaCorpoTemplate(boolean abiltiazione) {
		this.corpoTemplatePanel.setVisible(abiltiazione);
	}

	@Override
	public void initListBox(final Command changeStateCommand) {
		this.sceltaTemplateListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				changeStateCommand.execute();
			}
		});
	}

	@Override
	public void clearListBox() {
		sceltaTemplateListBox.clear();
	}

	@Override
	public void addItemListBox(TipologiaPratica tipoTemplate) {
				
		for (int i = 0; i < this.sceltaTemplateListBox.getItemCount(); i++) {
			if (this.sceltaTemplateListBox.getItemText(i).equals(tipoTemplate.getEtichettaTipologia())) {
				return;
			}
		}

		this.sceltaTemplateListBox.addItem(tipoTemplate.getEtichettaTipologia());
		this.sceltaTemplateListBox.setEnabled(this.sceltaTemplateListBox.getItemCount() > 1);
	}

	@Override
	public TipologiaPratica getTipoTemplateSelezionato() {
		AnagraficaModello am = configurazioniHandler.getAnagraficaModelloByEtichetta(this.sceltaTemplateListBox.getItemText(this.sceltaTemplateListBox.getSelectedIndex()));
		return PraticaUtil.toTipologiaPratica(am);
	}

	@Override
	public void resetPannelloWidget() {
		this.corpoTemplatePanel.clear();
	}

	@Override
	public void setCorpoTemplateWidget(AbstractCorpoTemplateWidget<?> corpoTemplateWidget) {
		this.corpoTemplateWidget = corpoTemplateWidget;
		this.corpoTemplateWidget.setAggiungiCampoCommand(aggiungiCampoCommand);
		this.corpoTemplateWidget.setEliminaCampoCommand(eliminaCampoCommand);
		this.corpoTemplatePanel.add(corpoTemplateWidget);
	}

	@Override
	public AbstractCorpoTemplateWidget<?> getCorpoTemplateWidget() {
		return this.corpoTemplateWidget;
	}

	@Override
	public void resetCorpoTemplateWidget() {
		if (this.corpoTemplateWidget != null)
			this.corpoTemplateWidget.clear();

		abilitaCorpoTemplate(false);
		resetPannelloWidget();
	}
}
