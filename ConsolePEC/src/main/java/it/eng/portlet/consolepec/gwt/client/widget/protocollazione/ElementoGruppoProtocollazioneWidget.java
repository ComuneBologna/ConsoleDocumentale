package it.eng.portlet.consolepec.gwt.client.widget.protocollazione;

import it.eng.portlet.consolepec.gwt.shared.dto.Campo;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class ElementoGruppoProtocollazioneWidget<T extends AbstractGroupDto> extends Composite {

	private static ElementoGruppoProtocollazioneWidgetUiBinder uiBinder = GWT.create(ElementoGruppoProtocollazioneWidgetUiBinder.class);

	@SuppressWarnings("rawtypes")
	interface ElementoGruppoProtocollazioneWidgetUiBinder extends UiBinder<Widget, ElementoGruppoProtocollazioneWidget> {
	}

	@UiField
	HTMLPanel gruppoPanel;
	@UiField
	HTMLPanel itemsPanel;
	private TreeSet<T> elementi = new TreeSet<T>();
	private AbstractEliminaCommand eliminaCommand;

	public void setEliminaCommand(AbstractEliminaCommand eliminaCommand) {
		this.eliminaCommand = eliminaCommand;
	}

	public ElementoGruppoProtocollazioneWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public HTMLPanel getGruppoPanel() {
		return gruppoPanel;
	}

	@UiHandler("aggiungiButton")
	public void onClick(ClickEvent clickEvent) {
		ArrayList<Widget> newArrayList = Lists.newArrayList(gruppoPanel.iterator());
		T elemento = createElementFromGroupWidgets(newArrayList);
		if (!Strings.isNullOrEmpty(elemento.toString().trim()))
			elementi.add(elemento);
		clearWidget(newArrayList);
		repaint();
	}

	protected void repaint() {
		itemsPanel.getElement().setInnerHTML("");
		DivElement div = Document.get().createDivElement();
		div.setClassName("corpo");
		itemsPanel.getElement().appendChild(div);
		for (T elemento : elementi) {
			HTMLPanel intPanel = new HTMLPanel("");
			intPanel.setStyleName("box-mail");
			ElencoElementiProtocollazioneWidget elencoElementiProtocollazioneWidget = new ElencoElementiProtocollazioneWidget();
			elencoElementiProtocollazioneWidget.mostraElemento(elemento.toString(), eliminaCommand);
			intPanel.add(elencoElementiProtocollazioneWidget);
			itemsPanel.add(intPanel, div);
		}

	}

	protected TreeSet<T> getElementi() {
		return elementi;
	}

	public List<Campo> convertDto() {
		ArrayList<Campo> campi = new ArrayList<Campo>();
		int i = 0;
		for (T elemento : getElementi())
			campi.addAll(elemento.getCampo(i++));
		return campi;
	}

	protected abstract void clearWidget(ArrayList<Widget> newArrayList);

	protected abstract T createElementFromGroupWidgets(ArrayList<Widget> newArrayList);

	public abstract class AbstractEliminaCommand {

		public void elimina(String name) {
			removeFromList(name);
			repaint();
		}

		protected abstract void removeFromList(String name);
	}

}
