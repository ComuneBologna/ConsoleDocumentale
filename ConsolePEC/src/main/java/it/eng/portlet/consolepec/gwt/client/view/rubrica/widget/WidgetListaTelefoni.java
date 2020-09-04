package it.eng.portlet.consolepec.gwt.client.view.rubrica.widget;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Strings;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ConsoleDisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

import it.eng.cobo.consolepec.commons.rubrica.Anagrafica.Stato;
import it.eng.cobo.consolepec.commons.rubrica.Telefono;
import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget;

/**
 * @author GiacomoFM
 * @since 19/ott/2017
 */
public class WidgetListaTelefoni extends Composite {

	private final List<Telefono> telefoni = new LinkedList<>();

	private Stato stato;

	@UiField(provided = true)
	ConsoleDisclosurePanel telefoniDisclosure = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Lista Telefoni");

	@UiField
	HTMLPanel telefoniPanel;

	@UiField
	TextBox tipologiaBox;
	@UiField
	TextBox numeroBox;

	@UiField
	Button aggiungiTelefono;

	private static Binder binder = GWT.create(Binder.class);

	public interface Binder extends UiBinder<Widget, WidgetListaTelefoni> {
		//
	}

	public WidgetListaTelefoni() {
		super();
		initWidget(binder.createAndBindUi(this));

		telefoniDisclosure.setOpen(false);

		aggiungiTelefono.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!Strings.isNullOrEmpty(numeroBox.getText())) {
					Telefono t = new Telefono();
					t.setTipologia(tipologiaBox.getText());
					t.setNumero(numeroBox.getText());
					telefoni.add(t);
					innerSet(telefoni);
					reset();
					
				}
			}
		});
	}

	public List<Telefono> get() {
		return this.telefoni;
	}

	public void set(List<Telefono> telefoni, Stato stato) {
		this.stato = stato;
		if (Stato.DISATTIVA.equals(stato)) {
			tipologiaBox.setEnabled(false);
			tipologiaBox.setStylePrimaryName("disabilitato");
			numeroBox.setEnabled(false);
			numeroBox.setStylePrimaryName("disabilitato");

			aggiungiTelefono.setEnabled(false);
		} else {
			tipologiaBox.setEnabled(true);
			tipologiaBox.removeStyleName("disabilitato");
			numeroBox.setEnabled(true);
			numeroBox.removeStyleName("disabilitato");

			aggiungiTelefono.setEnabled(true);
		}
		telefoniDisclosure.setOpen(false);
		innerSet(telefoni);
	}

	private void innerSet(List<Telefono> telefoni) {
		List<Telefono> tmp = new ArrayList<>(telefoni);
		this.telefoni.clear();
		telefoniPanel.clear();

		this.telefoni.addAll(tmp);
		disegnaTabella();
	}

	private void reset() {
		tipologiaBox.setText("");
		numeroBox.setText("");
	}

	private void disegnaTabella() {
		DataGridWidget<Telefono> dataGrid = new DataGridWidget<>();
		dataGrid.addColumn(new ColonnaTipologia(), "Tipologia");
		dataGrid.addColumn(new ColonnaTelefono(), "Telefono");

		ColonnaElimina colonnaElimina = new ColonnaElimina();
		colonnaElimina.setFieldUpdater(new FieldUpdater<Telefono, String>() {
			@Override
			public void update(int index, Telefono object, String value) {
				telefoni.remove(index);
				innerSet(telefoni);
			}
		});
		dataGrid.addColumn(colonnaElimina, "");

		ListDataProvider<Telefono> listDataProvider = new ListDataProvider<>();
		listDataProvider.setList(telefoni);
		listDataProvider.addDataDisplay(dataGrid);

		telefoniPanel.add(dataGrid);
	}

	private static class ColonnaTipologia extends TextColumn<Telefono> {
		@Override
		public String getValue(Telefono t) {
			return spaceIfNull(t.getTipologia());
		}
	}

	private static class ColonnaTelefono extends TextColumn<Telefono> {
		@Override
		public String getValue(Telefono o) {
			return spaceIfNull(o.getNumero());
		}
	}

	private class ColonnaElimina extends Column<Telefono, String> {
		public ColonnaElimina() {
			super(new StyledButtonCell());
			setCellStyleNames("last-column");
		}

		@Override
		public String getValue(Telefono o) {
			return "Elimina";
		}
	}

	private static String spaceIfNull(String text) {
		if (text == null) {
			return "";
		}
		return text;
	}

	private class StyledButtonCell extends ButtonCell {
		@Override
		public void render(Cell.Context context, SafeHtml data, SafeHtmlBuilder sb) {
			StringBuilder s = new StringBuilder("<button class=\"btn worklist_btn\" type=\"button\" tabindex=\"-1\"");
			if (Stato.DISATTIVA.equals(stato)) {
				s.append(" disabled=\"true\">");
			} else {
				s.append(">");
			}
			sb.appendHtmlConstant(s.toString());
			if (data != null) {
				sb.append(data);
			}
			sb.appendHtmlConstant("</button>");
		}
	}

}
