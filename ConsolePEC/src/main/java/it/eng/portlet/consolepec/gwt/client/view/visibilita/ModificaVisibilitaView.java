package it.eng.portlet.consolepec.gwt.client.view.visibilita;

import java.util.TreeSet;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.portlet.consolepec.gwt.client.presenter.visibilita.ModificaVisibilitaPresenter.MyView;
import it.eng.portlet.consolepec.gwt.client.presenter.visibilita.ModificaVisibilitaPresenter.RimuoviGruppoVisibilita;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoGruppoVisibilitaWidget;
import it.eng.portlet.consolepec.gwt.shared.model.GruppoVisibilita;

public class ModificaVisibilitaView extends ViewImpl implements MyView {

	@UiField
	protected Button annullaButton;
	@UiField
	protected Button confermaButton;
	@UiField
	protected HeadingElement titoloModificaVisibilita;
	@UiField
	protected HTMLPanel elencoGruppiSuggestBoxPanel;
	@UiField
	protected HTMLPanel pannelloElencoGruppi;
	@UiField
	protected Button buttonAggiungi;

	private final Widget widget;

	private RimuoviGruppoVisibilita rimuoviGruppoVisibilita;

	public interface Binder extends UiBinder<Widget, ModificaVisibilitaView> {}

	@Inject
	public ModificaVisibilitaView(final Binder binder) {
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
	public Button getAnnullaButton() {
		return annullaButton;
	}

	@Override
	public Button getConfermaButton() {
		return confermaButton;
	}

	@Override
	public void setTitle(String title) {
		titoloModificaVisibilita.setInnerText(title);
	}

	@Override
	public void mostraGruppoVisibilita(TreeSet<GruppoVisibilita> gruppiCorrenti, String assegnatario) {
		pannelloElencoGruppi.clear();
		pannelloElencoGruppi.getElement().setInnerHTML("");
		UListElement ul = Document.get().createULElement();
		ul.addClassName("contenitore-lista-gruppi");
		LIElement li = Document.get().createLIElement();
		li.addClassName("gruppo last clearfix");
		ul.appendChild(li);
		SpanElement span = Document.get().createSpanElement();
		span.addClassName("label protocollo");
		li.appendChild(span);
		DivElement div = Document.get().createDivElement();
		div.setClassName("corpo");
		li.appendChild(div);

		HTMLPanel panel = new HTMLPanel("");
		panel.setStyleName("box-mail");

		for (final GruppoVisibilita gruppoVisibilita : gruppiCorrenti) {
			HTMLPanel intPanel = new HTMLPanel("");
			intPanel.setStyleName("box-mail");
			ElementoGruppoVisibilitaWidget elementoGruppoVisibilitaWidget = new ElementoGruppoVisibilitaWidget();

			if (assegnatario != null && assegnatario.equals(gruppoVisibilita.getLabel())) {
				elementoGruppoVisibilitaWidget.getButtonEliminaButton().setEnabled(false);

			} else {
				elementoGruppoVisibilitaWidget.getButtonEliminaButton().addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent arg0) {
						rimuoviGruppoVisibilita.exe(gruppoVisibilita);
					}
				});
			}

			elementoGruppoVisibilitaWidget.mostraGruppoVisibilita(gruppoVisibilita);
			intPanel.add(elementoGruppoVisibilitaWidget);
			pannelloElencoGruppi.getElement().appendChild(ul);
			pannelloElencoGruppi.add(intPanel, div);
		}
	}

	@Override
	public void setSuggestBox(SuggestBox suggestBox) {
		elencoGruppiSuggestBoxPanel.clear();
		elencoGruppiSuggestBoxPanel.getElement().setInnerHTML("");
		elencoGruppiSuggestBoxPanel.add(suggestBox);
	}

	@Override
	public void setRimuoviGruppoVisibilitaCommand(RimuoviGruppoVisibilita rimuoviGruppoVisibilita) {
		this.rimuoviGruppoVisibilita = rimuoviGruppoVisibilita;
	}

	@Override
	public Button getAggiungiButton() {
		return this.buttonAggiungi;
	}

}
