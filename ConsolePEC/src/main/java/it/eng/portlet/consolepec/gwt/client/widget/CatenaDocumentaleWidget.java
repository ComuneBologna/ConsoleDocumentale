package it.eng.portlet.consolepec.gwt.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;

public class CatenaDocumentaleWidget extends Composite {

	private static CatenaDocumentaleWidgetUiBinder uiBinder = GWT.create(CatenaDocumentaleWidgetUiBinder.class);

	interface CatenaDocumentaleWidgetUiBinder extends UiBinder<Widget, CatenaDocumentaleWidget> {}

	@UiField
	HTMLPanel panelloRicercaPG;
	@UiField
	HTMLPanel resultPanel;
	@UiField
	TextBox numeroPg;
	@UiField
	TextBox annoPg;
	@UiField
	Button buttonCerca;

	HTMLPanel capofilaPanel;
	HTMLPanel collegatiPanel;

	public CatenaDocumentaleWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void reset() {
		numeroPg.setValue(null);
		annoPg.setValue(null);
		clearRisultati();
	}

	public void setSearchEnabled(boolean enabled) {
		buttonCerca.setEnabled(enabled);
	}

	public void setSearchPGCommand(final Command searchPgCommand) {
		buttonCerca.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (validazione()) {
					searchPgCommand.execute();

				} else {
					String messaggio = "I campi in rosso devono essere valorizzati";
					ShowMessageEvent messageEvent = new ShowMessageEvent();
					messageEvent.setWarningMessage(messaggio);
				}
			}
		});
	}

	private boolean validazione() {

		if (numeroPg.getValue() == null || numeroPg.getValue().trim().isEmpty()) {
			numeroPg.getElement().setAttribute("required", "required");
			return false;
		}

		if (annoPg.getValue() == null || annoPg.getValue().trim().isEmpty()) {
			annoPg.getElement().setAttribute("required", "required");
			return false;
		}

		try {
			Integer.parseInt(annoPg.getValue());

		} catch (Exception e) {
			annoPg.getElement().setAttribute("required", "required");
			return false;
		}

		try {
			Integer.parseInt(numeroPg.getValue());

		} catch (Exception e) {
			numeroPg.getElement().setAttribute("required", "required");
			return false;
		}

		numeroPg.getElement().removeAttribute("required");
		annoPg.getElement().removeAttribute("required");
		return true;
	}

	public void clearRisultati() {
		resultPanel.clear();
		collegatiPanel = new HTMLPanel("");
		capofilaPanel = new HTMLPanel("");
	}

	public void addElemento(ElementoCatenaDocumentaleWidget w) {
		Label mailId = new Label(w.getPg().getNumeroPG() + "/" + w.getPg().getAnnoPG());
		UListElement ul = Document.get().createULElement();
		if (w.getPg().isCapofila())
			ul.addClassName("contenitore-lista-gruppi");
		else
			ul.addClassName("lista-gruppi");
		LIElement li = Document.get().createLIElement();
		li.addClassName("gruppo last clearfix");
		ul.appendChild(li);
		SpanElement span = Document.get().createSpanElement();
		span.addClassName("label nessun-protocollo");
		li.appendChild(span);
		DivElement div = Document.get().createDivElement();
		div.setClassName("corpo");
		li.appendChild(div);
		HTMLPanel panel = new HTMLPanel("");
		panel.setStyleName("box-mail");
		panel.add(w);
		w.mostraDettaglio();

		if (w.getPg().isCapofila()) {
			capofilaPanel = new HTMLPanel("");
			capofilaPanel.getElement().appendChild(ul);
			capofilaPanel.add(mailId, span);
			capofilaPanel.add(panel, div);
		} else {
			if (collegatiPanel == null)
				collegatiPanel = new HTMLPanel("");
			collegatiPanel.getElement().appendChild(ul);
			collegatiPanel.add(mailId, span);
			collegatiPanel.add(panel, div);
		}

	}

	public void mostraRisultati() {
		resultPanel.add(capofilaPanel);
		resultPanel.add(collegatiPanel);
	}

	public SearchPGParams getSearchPGParams() {
		return new SearchPGParams(numeroPg.getValue(), annoPg.getValue());
	}

	public static class SearchPGParams {

		private String numero;
		private String anno;

		public SearchPGParams(String numero, String anno) {
			super();
			this.numero = numero;
			this.anno = anno;
		}

		public String getNumero() {
			return numero;
		}

		public String getAnno() {
			return anno;
		}
	}
}
