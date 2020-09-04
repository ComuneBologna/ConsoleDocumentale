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
import it.eng.cobo.consolepec.commons.rubrica.Email;
import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget;

/**
 * @author GiacomoFM
 * @since 19/ott/2017
 */
public class WidgetListaEmail extends Composite {

	private final List<Email> emails = new LinkedList<>();

	private Stato stato;

	@UiField(provided = true)
	ConsoleDisclosurePanel emailDisclosure = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Lista Email");

	@UiField
	HTMLPanel emailPanel;

	@UiField
	TextBox tipologiaBox;
	@UiField
	TextBox emailBox;

	@UiField
	Button aggiungiEmail;

	private static Binder binder = GWT.create(Binder.class);

	public interface Binder extends UiBinder<Widget, WidgetListaEmail> {
		//
	}

	public WidgetListaEmail() {
		super();
		initWidget(binder.createAndBindUi(this));

		emailDisclosure.setOpen(false);

		aggiungiEmail.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!Strings.isNullOrEmpty(emailBox.getText())) {
					Email t = new Email();
					t.setTipologia(tipologiaBox.getText());
					t.setEmail(emailBox.getText());
					emails.add(t);
					innerSet(emails);
					reset();
					
				}

			}
		});
	}

	public List<Email> get() {
		return this.emails;
	}

	public void set(List<Email> listaEmail, Stato stato) {
		this.stato = stato;
		if (Stato.DISATTIVA.equals(stato)) {
			tipologiaBox.setEnabled(false);
			tipologiaBox.setStylePrimaryName("disabilitato");
			emailBox.setEnabled(false);
			emailBox.setStylePrimaryName("disabilitato");

			aggiungiEmail.setEnabled(false);
		} else {
			tipologiaBox.setEnabled(true);
			tipologiaBox.removeStyleName("disabilitato");
			emailBox.setEnabled(true);
			emailBox.removeStyleName("disabilitato");

			aggiungiEmail.setEnabled(true);
		}
		emailDisclosure.setOpen(false);
		innerSet(listaEmail);
	}

	private void innerSet(List<Email> listaEmail) {
		List<Email> tmp = new ArrayList<>(listaEmail);
		this.emails.clear();
		emailPanel.clear();

		this.emails.addAll(tmp);
		disegnaTabella();
	}

	private void reset() {
		tipologiaBox.setText("");
		emailBox.setText("");
	}

	private void disegnaTabella() {
		DataGridWidget<Email> dataGrid = new DataGridWidget<>();
		dataGrid.addColumn(new ColonnaTipologia(), "Tipologia");
		dataGrid.addColumn(new ColonnaEmail(), "Email");

		ColonnaElimina colonnaElimina = new ColonnaElimina();
		colonnaElimina.setFieldUpdater(new FieldUpdater<Email, String>() {
			@Override
			public void update(int index, Email object, String value) {
				emails.remove(index);
				innerSet(emails);
			}
		});
		dataGrid.addColumn(colonnaElimina, "");

		ListDataProvider<Email> listDataProvider = new ListDataProvider<>();
		listDataProvider.setList(emails);
		listDataProvider.addDataDisplay(dataGrid);

		emailPanel.add(dataGrid);
	}

	private static class ColonnaTipologia extends TextColumn<Email> {
		@Override
		public String getValue(Email e) {
			return spaceIfNull(e.getTipologia());
		}
	}

	private static class ColonnaEmail extends TextColumn<Email> {
		@Override
		public String getValue(Email o) {
			return spaceIfNull(o.getEmail());
		}
	}

	private class ColonnaElimina extends Column<Email, String> {
		public ColonnaElimina() {
			super(new StyledButtonCell());
			setCellStyleNames("last-column");
		}

		@Override
		public String getValue(Email o) {
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
