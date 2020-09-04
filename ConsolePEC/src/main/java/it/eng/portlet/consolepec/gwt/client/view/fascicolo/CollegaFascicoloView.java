package it.eng.portlet.consolepec.gwt.client.view.fascicolo;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.collegamenti.Permessi;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.CollegaFascicoloPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.GruppoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.YesNoRadioButton;
import it.eng.portlet.consolepec.gwt.client.widget.operazioni.ElencoOperazioniWidget;

public class CollegaFascicoloView extends ViewImpl implements CollegaFascicoloPresenter.MyView {

	private final Widget widget;

	@UiField(provided = true)
	MessageAlertWidget messageWidget;
	@UiField
	Button confermaButton;
	@UiField
	Button annullaButton;
	@UiField
	HTMLPanel mainCollegaPanel;
	@UiField
	HeadingElement collegaTitle;
	@UiField
	HTMLPanel sceltaRuoloContainer;
	@UiField
	HTMLPanel sceltaRuoloPanel;

	@UiField
	YesNoRadioButton sorgenteAccessibileInLetturaRadioButton;
	@UiField
	YesNoRadioButton remotoAccessibileInLetturaRadioButton;

	private ElencoOperazioniWidget elencoOperazioniWidget;

	public interface Binder extends UiBinder<Widget, CollegaFascicoloView> {/**/}

	@Inject
	public CollegaFascicoloView(final Binder binder, final EventBus eventBus) {
		messageWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setAnnullaCommand(final com.google.gwt.user.client.Command command) {
		annullaButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
	}

	@Override
	public void setCollegaFascicoloCommand(final com.google.gwt.user.client.Command command) {
		confermaButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
	}

	@Override
	public void popolaPermessiCollegamentoFascicolo(String titolo, ImageResource icona, HashMap<String, Boolean> operazioni, Permessi permessi, Command onSelezioneCommand) {
		mainCollegaPanel.clear();
		elencoOperazioniWidget = new ElencoOperazioniWidget();
		elencoOperazioniWidget.init(titolo, icona, operazioni, true, false);
		elencoOperazioniWidget.setOnSelezioneCommand(onSelezioneCommand);
		mainCollegaPanel.add(elencoOperazioniWidget);

		if (permessi.isSorgenteAccessibileInLettura())
			sorgenteAccessibileInLetturaRadioButton.selectYes();
		else
			sorgenteAccessibileInLetturaRadioButton.selectNo();

		if (permessi.isRemotoAccessibileInLettura())
			remotoAccessibileInLetturaRadioButton.selectYes();
		else
			remotoAccessibileInLetturaRadioButton.selectNo();
	}

	@Override
	public Permessi getPermessi() {
		Permessi permessi = new Permessi();
		permessi.getOperazioniConsentite().addAll(elencoOperazioniWidget.getOperazioniSelezionate());
		permessi.setSorgenteAccessibileInLettura((sorgenteAccessibileInLetturaRadioButton.getValue() == null) ? true : sorgenteAccessibileInLetturaRadioButton.getValue());
		permessi.setRemotoAccessibileInLettura((remotoAccessibileInLetturaRadioButton.getValue() == null) ? true : remotoAccessibileInLetturaRadioButton.getValue());
		return permessi;
	}

	@Override
	public void attivaPulsanteConferma(boolean enabled) {
		confermaButton.setEnabled(enabled);
	}

	@Override
	public void impostaTitolo(String titolo) {
		collegaTitle.setInnerText(titolo);
	}

	@Override
	public void attivaPulsanteElimina(boolean enabled) {
		elencoOperazioniWidget.attivaPulsanteElimina(enabled);
	}

	@Override
	public void setEliminaCommand(Command eliminaCommand) {
		elencoOperazioniWidget.setOnEliminaCommand(eliminaCommand);
	}

	@Override
	public void configuraSceltaRuolo(List<String> ruoliAbiltiati, it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, String> selectCommand) {

		for (String ruolo : ruoliAbiltiati) {
			GruppoWidget widget = new GruppoWidget();
			widget.setSelectCommand(selectCommand);
			sceltaRuoloPanel.add(widget);
			widget.showWidgetWithRadioButton(ruolo);
		}

		sceltaRuoloContainer.setVisible(true);
	}

	@Override
	public void clear() {
		Window.scrollTo(0, 0);
		this.sceltaRuoloPanel.clear();
		this.sceltaRuoloContainer.setVisible(false);
	}

}
