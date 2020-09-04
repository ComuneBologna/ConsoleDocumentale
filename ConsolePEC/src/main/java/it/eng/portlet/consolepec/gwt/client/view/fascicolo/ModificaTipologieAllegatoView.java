package it.eng.portlet.consolepec.gwt.client.view.fascicolo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.ModificaTipologieAllegatoPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.GenericCheckBoxWidget;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.composizione.AllegatoComposizioneWidget;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import lombok.AllArgsConstructor;

/**
 * @author GiacomoFM
 * @since 10/dic/2018
 */
public class ModificaTipologieAllegatoView extends ViewImpl implements ModificaTipologieAllegatoPresenter.MyView {

	@UiField(provided = true) MessageAlertWidget messageAlertWidget;

	@UiField HTMLPanel allegatiSelezionatiPanel;
	@UiField HTMLPanel tipologiePanel;
	@UiField Button annullaButton;
	@UiField Button confermaButton;

	private Set<String> tipologieComuni = new HashSet<>();
	private Set<String> tipologieSelezionate = new HashSet<>();

	private final Widget widget;

	@Override
	public Widget asWidget() {
		return widget;
	}

	public interface Binder extends UiBinder<Widget, ModificaTipologieAllegatoView> {
		//
	}

	@Inject
	public ModificaTipologieAllegatoView(final Binder binder, final EventBus eventBus) {
		messageAlertWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
	}

	@Override
	public void setAllegatiSelezionati(Set<AllegatoDTO> allegati) {
		tipologieComuni.clear();
		allegatiSelezionatiPanel.clear();

		for (AllegatoDTO allegato : allegati) {
			AllegatoComposizioneWidget w = creaNuovoAllegatoComposizioneWidget();
			w.mostraDettaglio(allegato);
			w.setCheckBoxVisible(false);
			allegatiSelezionatiPanel.add(w);

			if (tipologieComuni.isEmpty()) {
				tipologieComuni.addAll(allegato.getTipologiaAllegato());
			} else {
				tipologieComuni.retainAll(allegato.getTipologiaAllegato());
			}
		}
	}

	private static AllegatoComposizioneWidget creaNuovoAllegatoComposizioneWidget() {
		AllegatoComposizioneWidget w = new AllegatoComposizioneWidget();
		w.setCheckBoxVisible(false);
		w.setMostraTipologieImpostate(false);
		w.setMostraCommand(new it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO>() {
			@Override
			public Void exe(AllegatoDTO t) {
				return null;
			}
		});
		w.setDownloadCommand(new it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO>() {
			@Override
			public Void exe(AllegatoDTO t) {
				return null;
			}
		});
		return w;
	}

	@Override
	public void setTipologieSelezionabili(List<String> tipologie) {
		tipologieSelezionate.clear();
		tipologiePanel.clear();

		for (String t : tipologie) {
			if (tipologieComuni.contains(t)) {
				tipologieSelezionate.add(t);
				tipologiePanel.add(new GenericCheckBoxWidget(t, true, new CheckBoxClickHandler(t)));
			} else {
				tipologiePanel.add(new GenericCheckBoxWidget(t, new CheckBoxClickHandler(t)));
			}
		}
	}

	@Override
	public List<String> getTipologieSelezionate() {
		return new ArrayList<>(this.tipologieSelezionate);
	}

	@Override
	public void setAnnullaCommand(final Command annullaCommand) {
		annullaButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				annullaCommand.execute();
			}
		});
	}

	@Override
	public void setConfermaCommand(final Command confermaCommand) {
		confermaButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				confermaCommand.execute();
			}
		});
	}

	@AllArgsConstructor
	private class CheckBoxClickHandler implements ClickHandler {
		String tipologia;

		@Override
		public void onClick(ClickEvent event) {
			boolean checked = ((CheckBox) event.getSource()).getValue();
			if (checked) {
				tipologieSelezionate.add(this.tipologia);
			} else {
				tipologieSelezionate.remove(this.tipologia);
			}
		}
	}

}
