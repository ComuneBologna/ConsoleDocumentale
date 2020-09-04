package it.eng.portlet.consolepec.gwt.client.view.fascicolo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.ImportaAllegatiEmailPresenter;
import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoAllegatoElencoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoAllegatoElencoWidget.SelezioneAllegato;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;

public class ImportaAllegatiEmailView extends ViewImpl implements ImportaAllegatiEmailPresenter.MyView {

	private final Widget widget;

	@UiField Button importaSelezionatiTreeButton;
	@UiField Button annullaButton;

	@UiField(provided = true) DisclosurePanel disclosurePanelEmailAllegati = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(),
			OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Seleziona allegati da importare");
	@UiField(provided = true) MessageAlertWidget messageWidget;

	@UiField HTMLPanel treeAllegatiPanel; // serve solo da contenitore

	private Map<String, List<AllegatoDTO>> allegatiEmailSelezionati = new HashMap<String, List<AllegatoDTO>>();

	public interface Binder extends UiBinder<Widget, ImportaAllegatiEmailView> {/**/}

	@Inject
	public ImportaAllegatiEmailView(final Binder binder, final EventBus eventBus) {
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
	public void setImportaAllegatiCommand(final com.google.gwt.user.client.Command command) {
		importaSelezionatiTreeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
	}

	@Override
	public void popolaAllegatiEmail(Set<PecInDTO> listaPecIn) {
		allegatiEmailSelezionati = new HashMap<String, List<AllegatoDTO>>();
		disclosurePanelEmailAllegati.clear();
		// disclosurePanelEmailAllegati.setVisible(false);
		HTMLPanel allegatiPanel = new HTMLPanel("");
		for (PecInDTO pec : listaPecIn) {
			addAllegatiSection(pec.getClientID(), pec.getNumeroRepertorio(), pec.getAllegati(), allegatiPanel);
		}
		disclosurePanelEmailAllegati.setVisible(true);
		disclosurePanelEmailAllegati.add(allegatiPanel);

	}

	protected void addAllegatiSection(final String clientId, String idDocumentale, final List<AllegatoDTO> allegati, HTMLPanel panelContenitore) {
		Label mailId = new Label(idDocumentale);
		UListElement ul = Document.get().createULElement();
		ul.addClassName("contenitore-lista-gruppi");
		LIElement li = Document.get().createLIElement();
		li.addClassName("gruppo last clearfix");
		ul.appendChild(li);
		SpanElement span = Document.get().createSpanElement();
		span.addClassName("label nessun-protocollo");
		// span.setInnerText( pg );
		li.appendChild(span);
		DivElement div = Document.get().createDivElement();
		div.setClassName("corpo");
		li.appendChild(div);
		HTMLPanel panel = new HTMLPanel("");
		panel.setStyleName("box-mail");
		for (AllegatoDTO a : allegati) {
			ElementoAllegatoElencoWidget dettaglioWiget = new ElementoAllegatoElencoWidget();
			dettaglioWiget.setCheckBoxVisible(true);
			dettaglioWiget.setSelezionaAllegatoCommand(new Command<Void, ElementoAllegatoElencoWidget.SelezioneAllegato>() {

				@Override
				public Void exe(SelezioneAllegato selezioneAllegato) {

					if (selezioneAllegato.isChecked()) {
						List<AllegatoDTO> list = allegatiEmailSelezionati.get(clientId);
						if (list == null) list = new ArrayList<AllegatoDTO>();
						list.add(selezioneAllegato.getAllegato());
						allegatiEmailSelezionati.put(clientId, list);
					} else {
						List<AllegatoDTO> list = allegatiEmailSelezionati.get(clientId);
						list.remove(selezioneAllegato.getAllegato());
						if (allegatiEmailSelezionati.get(clientId).size() == 0) allegatiEmailSelezionati.remove(clientId);
					}

					importaSelezionatiTreeButton.setEnabled(allegatiEmailSelezionati.size() > 0);

					return null;
				}
			});
			panel.add(dettaglioWiget);
			dettaglioWiget.mostraDettaglio(a);
		}
		panelContenitore.getElement().appendChild(ul);
		panelContenitore.add(mailId, span);
		panelContenitore.add(panel, div);
	}

	@Override
	public Map<String, List<AllegatoDTO>> getAllegatiEmailSelezionati() {
		return allegatiEmailSelezionati;
	}

	@Override
	public void restSelezioni() {
		allegatiEmailSelezionati = new HashMap<String, List<AllegatoDTO>>();
		importaSelezionatiTreeButton.setEnabled(false);
	}

}
