package it.eng.portlet.consolepec.gwt.client.widget;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Strings;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;
import com.google.gwt.view.client.ListDataProvider;

import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.scan.AbstractScanWidget;
import it.eng.portlet.consolepec.gwt.client.scan.ConfigWidget;
import it.eng.portlet.consolepec.gwt.client.scan.VisitableWidget;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.SimpleInputListWidget;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo.TipoWidget;
import lombok.Getter;

public abstract class ListaCampiWidget<T> extends Composite {

	private static DateTimeFormat dateFormat = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATA);

	private static ListaCampiWidgetUiBinder uiBinder = GWT.create(ListaCampiWidgetUiBinder.class);

	interface ListaCampiWidgetUiBinder extends UiBinder<Widget, ListaCampiWidget<?>> {}

	private List<VisitableWidget> widgets = new ArrayList<VisitableWidget>();
	private List<CampoLista> campiLista = new ArrayList<CampoLista>();
	private List<String> errori = new ArrayList<String>();

	@UiField
	HTMLPanel formPanel;
	@UiField
	HTMLPanel itemsPanel;
	@UiField
	HTMLPanel alertPanel;

	private Button aggiungiButton;

	private DataGridWidget<Object[]> grid;
	private int numColonne = 0;
	private ListDataProvider<Object[]> data = new ListDataProvider<Object[]>();
	private List<ELiminaButtonCell> bottoniElimina = new ArrayList<ELiminaButtonCell>();

	private List<T> valori = new ArrayList<T>();

	private it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, T> onEliminaCampoListaCommand;

	public ListaCampiWidget(Integer limit) {
		super();
		initWidget(uiBinder.createAndBindUi(this));
		grid = new DataGridWidget<Object[]>(limit != null ? limit : 100, null, null);
		grid.setStyleName("tabella-modulo");
		grid.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		aggiungiButton = new Button();
		aggiungiButton.setText("Aggiungi");
		aggiungiButton.setStyleName("btn");

		data.addDataDisplay(grid);
		itemsPanel.add(grid);
	}

	public void render(boolean aggiungiAbilitato, boolean eliminaAbilitato) {
		formPanel.clear();
		campiLista.clear();
		widgets.clear();

		int gsize = grid.getColumnCount();
		for (int i = 0; i < gsize; i++) {
			grid.removeColumn(0);
		}
		// Tabella
		data.getList().clear();
		numColonne = 0;
		grid.setVisible(true);

		definisciCampi();

		for (CampoLista c : campiLista) {
			renderCampo(c);
		}

		bottoneElimina(eliminaAbilitato);
		bottoneAggiungi(aggiungiAbilitato);

		new InternalConfigWidget().scanListWidget(widgets);
	}

	protected abstract void definisciCampi();

	protected abstract T converti(Object[] riga);

	protected abstract Object[] converti(T riga);

	protected abstract boolean validaInserimento(T riga, List<String> errori);

	private void bottoneAggiungi(boolean abilitato) {
		aggiungiButton.setEnabled(abilitato);
		HTMLPanel buttonPanel = new HTMLPanel("");
		buttonPanel.setStyleName("cell");
		buttonPanel.add(aggiungiButton);
		formPanel.add(buttonPanel);
	}

	private void bottoneElimina(final boolean abilitato) {
		ELiminaButtonCell cell = new ELiminaButtonCell();
		cell.setEnabled(abilitato);
		bottoniElimina.add(cell);
		Column<Object[], String> eliminaColumn = new Column<Object[], String>(cell) {

			@Override
			public String getValue(Object[] riga) {
				return "Elimina";
			}
		};

		eliminaColumn.setFieldUpdater(new FieldUpdater<Object[], String>() {

			@Override
			public void update(int paramInt, Object[] riga, String paramC) {
				if (onEliminaCampoListaCommand != null) {
					onEliminaCampoListaCommand.exe(converti(riga));
				}
			}
		});
		grid.addColumn(eliminaColumn);
	}

	private void renderCampo(CampoLista c) {

		Campo campo = build(c);

		// form
		VisitableWidget widget = new VisitableWidget(campo);
		widget.setOnChangeCommand(c.onChangeCommand);
		widgets.add(widget);

		HTMLPanel widgetPanel = new HTMLPanel("");
		widgetPanel.setStyleName("cell acapo");

		SpanElement label = SpanElement.as(DOM.createSpan());
		label.setClassName("label");
		label.setInnerText(campo.getDescrizione());
		widgetPanel.getElement().appendChild(label);

		HTMLPanel inputPanel = new HTMLPanel("");
		inputPanel.setStyleName("abstract");
		inputPanel.add(widget.getWidget());
		widgetPanel.add(inputPanel);

		this.formPanel.add(widgetPanel);

		// tabella
		grid.addColumn(new ColonnaLista(numColonne++), campo.getDescrizione());
	}

	protected CampoLista creaCampo(String nome, String descrizione, TipoWidget tipoWidget, int posizione) {
		CampoLista cl = new CampoLista(nome, descrizione, tipoWidget, posizione);

		campiLista.add(cl);

		return cl;

	}

	public void setAggiungiCommand(final it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, T> command) {
		aggiungiButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent paramClickEvent) {
				alertPanel.setVisible(false);
				ValidaEOttieniValoreVisitor v = new ValidaEOttieniValoreVisitor();
				v.scanListWidget(widgets);

				if (v.isValid()) {
					alertPanel.clear();
					alertPanel.setVisible(false);
					Object[] riga = v.getValues();
					T obj = converti(riga);

					if (validaInserimento(obj, errori)) {
						if (command != null) {
							command.exe(obj);
						}
					} else {
						showErrori();
					}

				} else {
					showErrori();
				}
			}

		});

	}

	public List<T> getValori() {
		return valori;
	}

	public void add(T obj) {

		Object[] riga = converti(obj);

		if (riga != null) {
			data.getList().add(riga);
			valori.add(obj);
		}

	}

	public void clearValori() {
		data.getList().clear();
		valori.clear();
		bottoniElimina.clear();
	}

	public void clearForm() {
		new ClearVisitor().scanListWidget(widgets);

	}

	public void remove(T obj) {

		int index = valori.indexOf(obj);
		data.getList().remove(index);
		valori.remove(index);
	}

	public void setTabellaVisible(boolean b) {
		grid.setVisible(b);
	}

	public void setEliminaCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, T> onEliminaCampoListaCommand) {
		this.onEliminaCampoListaCommand = onEliminaCampoListaCommand;
	}

	private class ELiminaButtonCell extends ButtonCell {
		boolean abilitato;

		@Override
		public void render(Cell.Context context, SafeHtml data, SafeHtmlBuilder sb) {
			String abilitazione = abilitato ? "" : "disabled=\"disabled\"";
			sb.appendHtmlConstant("<button type=\"button\" tabindex=\"-1\" class=\"btn\"" + abilitazione + ">");
			if (data != null) {
				sb.append(data);
			}
			sb.appendHtmlConstant("</button>");

		}

		public void setEnabled(boolean abilitato) {
			this.abilitato = abilitato;
		}

	}

	private class InternalConfigWidget extends ConfigWidget {

		@Override
		public void scanDateBox(VisitableWidget visitableWidget) {
			super.scanDateBox(visitableWidget);
			Format format = new DateBox.DefaultFormat(dateFormat);
			((DateBox) visitableWidget.getWidget()).setFormat(format);
		}

		@Override
		public void scanTextBox(VisitableWidget visitableWidget) {
			super.scanTextBox(visitableWidget);

		}

		@Override
		public void scanDoubleBox(VisitableWidget visitableWidget) {
			super.scanDoubleBox(visitableWidget);
		}

		@Override
		public void scanIntegerBox(VisitableWidget visitableWidget) {
			super.scanIntegerBox(visitableWidget);

		}

	}

	private class ValidaEOttieniValoreVisitor extends AbstractScanWidget {

		private List<Object> objects = new ArrayList<Object>();
		boolean valid = true;

		public Object[] getValues() {
			return objects.toArray();
		}

		public boolean isValid() {
			return valid;
		}

		@Override
		public void scanHidden(VisitableWidget visitableWidget) {
			objects.add(((Hidden) visitableWidget.getWidget()).getValue());
		}

		@Override
		public void scanTextBox(VisitableWidget visitableWidget) {
			String value = ((TextBox) visitableWidget.getWidget()).getValue();

			if (visitableWidget.isObbligatorio() && Strings.isNullOrEmpty(value)) {
				valid = false;
				visitableWidget.getWidget().getElement().getStyle().setBorderColor("#FF0000");
				errori.add("- " + visitableWidget.getDescrizione() + " è un campo obbligatorio");
				return;
			}

			visitableWidget.getWidget().getElement().getStyle().clearBorderColor();

			if (Strings.isNullOrEmpty(value))
				value = null;

			objects.add(value);

		}

		@Override
		public void scanTextArea(VisitableWidget visitableWidget) {
			String value = ((TextArea) visitableWidget.getWidget()).getValue();

			if (visitableWidget.isObbligatorio() && Strings.isNullOrEmpty(value)) {
				valid = false;
				visitableWidget.getWidget().getElement().getStyle().setBorderColor("#FF0000");
				errori.add("- " + visitableWidget.getDescrizione() + " è un campo obbligatorio");
				return;
			}

			visitableWidget.getWidget().getElement().getStyle().clearBorderColor();

			if (Strings.isNullOrEmpty(value))
				value = null;

			objects.add(value);
		}

		@Override
		public void scanHTMLPanel(VisitableWidget visitableWidget) {
			// do nothing
		}

		@Override
		public void scanDateBox(VisitableWidget visitableWidget) {
			Date value = ((DateBox) visitableWidget.getWidget()).getValue();

			if (visitableWidget.isObbligatorio() && value == null) {
				valid = false;
				visitableWidget.getWidget().getElement().getStyle().setBorderColor("#FF0000");
				errori.add("- " + visitableWidget.getDescrizione() + " è un campo obbligatorio");
				return;
			}

			visitableWidget.getWidget().getElement().getStyle().clearBorderColor();
			objects.add(((DateBox) visitableWidget.getWidget()).getValue());
		}

		@Override
		public void scanListBox(VisitableWidget visitableWidget) {
			ListBox listbox = (ListBox) visitableWidget.getWidget();

			if (visitableWidget.isObbligatorio() && (listbox.getSelectedIndex() == -1 || Strings.isNullOrEmpty(listbox.getValue(listbox.getSelectedIndex())))) {
				valid = false;
				visitableWidget.getWidget().getElement().getStyle().setBorderColor("#FF0000");
				errori.add("- " + visitableWidget.getDescrizione() + " è un campo obbligatorio");
				return;
			} else if (listbox.getSelectedIndex() >= 0) {
				visitableWidget.getWidget().getElement().getStyle().clearBorderColor();
				objects.add(listbox.getValue(listbox.getSelectedIndex()));
			}
		}

		@Override
		public void scanSuggestBox(VisitableWidget visitableWidget) {
			objects.add(((SuggestBox) visitableWidget.getWidget()).getValue());
		}

		@Override
		public void scanCustomSuggestBox(VisitableWidget visitableWidget) {
			objects.add(((CustomSuggestBox) visitableWidget.getWidget()).getValue());
		}

		@Override
		public void scanIntegerBox(VisitableWidget visitableWidget) {
			try {
				// Controllo campi obbligatori
				String stringValue = ((IntegerBox) visitableWidget.getWidget()).getText();
				String pattern = "^[1-9][\\d]*$"; // regex by sebastiano

				if (visitableWidget.isObbligatorio() && Strings.isNullOrEmpty(stringValue)) {
					valid = false;
					visitableWidget.getWidget().getElement().getStyle().setBorderColor("#FF0000");
					errori.add("- " + visitableWidget.getDescrizione() + " è un campo obbligatorio");
					return;
				}

				if (!Strings.isNullOrEmpty(stringValue) && !stringValue.matches(pattern)) {
					valid = false;
					visitableWidget.getWidget().getElement().getStyle().setBorderColor("#FF0000");
					errori.add("- " + visitableWidget.getDescrizione() + " non ha un formato valido.");
					return;
				}

				visitableWidget.getWidget().getElement().getStyle().clearBorderColor();
				objects.add(((IntegerBox) visitableWidget.getWidget()).getValueOrThrow());
			} catch (ParseException e) {
				valid = false;
				visitableWidget.getWidget().getElement().getStyle().setBorderColor("#FF0000");
			}
		}

		@Override
		public void scanDoubleBox(VisitableWidget visitableWidget) {
			try {
				//
				// Controllo campi obbligatori
				String stringValue = ((DoubleBox) visitableWidget.getWidget()).getText();
				String pattern = "^0\\.\\d+$|^[1-9]\\d*\\.\\d+$|^[1-9][\\d]*$"; // regex by sebastiano

				if (visitableWidget.isObbligatorio() && Strings.isNullOrEmpty(stringValue)) {
					valid = false;
					visitableWidget.getWidget().getElement().getStyle().setBorderColor("#FF0000");
					errori.add("- " + visitableWidget.getDescrizione() + " è un campo obbligatorio");
					return;
				}

				if (!Strings.isNullOrEmpty(stringValue) && !stringValue.matches(pattern)) {
					valid = false;
					visitableWidget.getWidget().getElement().getStyle().setBorderColor("#FF0000");
					errori.add("- " + visitableWidget.getDescrizione() + " non ha un formato valido.");
					return;
				}

				visitableWidget.getWidget().getElement().getStyle().clearBorderColor();
				objects.add(((DoubleBox) visitableWidget.getWidget()).getValueOrThrow());
			} catch (ParseException e) {
				valid = false;
				visitableWidget.getWidget().getElement().getStyle().setBorderColor("#FF0000");
			}

		}

		@Override
		public void scanYesNoRadioButton(VisitableWidget visitableWidget) {
			objects.add(((YesNoRadioButton) visitableWidget.getWidget()).getValue());
		}

		@Override
		public void scanListWidget(VisitableWidget visitableWidget) {
			objects.add(((SimpleInputListWidget) visitableWidget.getWidget()).getItemSelected());
		}

	}

	/*
	 * pulisce il form
	 */
	private class ClearVisitor extends AbstractScanWidget {

		@Override
		public void scanHidden(VisitableWidget visitableWidget) {}

		@Override
		public void scanTextBox(VisitableWidget visitableWidget) {
			((TextBox) visitableWidget.getWidget()).setValue(null);
		}

		@Override
		public void scanTextArea(VisitableWidget visitableWidget) {
			((TextArea) visitableWidget.getWidget()).setValue(null);
		}

		@Override
		public void scanHTMLPanel(VisitableWidget visitableWidget) {
			// do nothing
		}

		@Override
		public void scanDateBox(VisitableWidget visitableWidget) {
			((DateBox) visitableWidget.getWidget()).setValue(null);
		}

		@Override
		public void scanListBox(VisitableWidget visitableWidget) {
			((ListBox) visitableWidget.getWidget()).setSelectedIndex(-1);

		}

		@Override
		public void scanSuggestBox(VisitableWidget visitableWidget) {}

		@Override
		public void scanCustomSuggestBox(VisitableWidget visitableWidget) {}

		@Override
		public void scanIntegerBox(VisitableWidget visitableWidget) {
			((IntegerBox) visitableWidget.getWidget()).setValue(null);
		}

		@Override
		public void scanDoubleBox(VisitableWidget visitableWidget) {
			((DoubleBox) visitableWidget.getWidget()).setValue(null);

		}

		@Override
		public void scanYesNoRadioButton(VisitableWidget visitableWidget) {
			((YesNoRadioButton) visitableWidget.getWidget()).setValue(null);

		}

		@Override
		public void scanListWidget(VisitableWidget visitableWidget) {
			((SimpleInputListWidget) visitableWidget.getWidget()).reset();

		}

	}

	private Campo build(CampoLista campoLista) {
		Campo campo = new Campo(campoLista.nome, campoLista.valore, campoLista.visibile, campoLista.modificabile, campoLista.obbligatorio, null, null, campoLista.lunghezza,
				campoLista.tipoWidget.name(), campoLista.descrizione, campoLista.posizione, campoLista.maxOccurs);
		if (campoLista.valoriListBox != null)
			campo.getValoriListbox().addAll(Arrays.asList(campoLista.valoriListBox));
		if (campoLista.selectedItem != null)
			campo.setSelectedItem(campoLista.selectedItem);
		return campo;
	}

	@Getter
	public static class CampoLista {

		private String nome, valore, descrizione;
		private boolean visibile = true, modificabile = true, obbligatorio = false;
		private int maxOccurs = 1, lunghezza = 20, posizione;
		private TipoWidget tipoWidget;

		/*
		 * Custom listbox
		 */
		private String[] valoriListBox;
		private Command<Void, String> onChangeCommand;
		private String selectedItem;

		public CampoLista(String nome, String descrizione, TipoWidget tipoWidget, int posizione) {
			super();
			this.nome = nome;
			this.descrizione = descrizione;
			this.posizione = posizione;
			this.tipoWidget = tipoWidget;
		}

		public CampoLista valore(Object valore) {
			this.valore = valore.toString();
			return this;
		}

		public CampoLista lista(String... valori) {
			valoriListBox = valori;
			return this;
		}

		public CampoLista obbligatorio(boolean o) {
			obbligatorio = o;
			return this;
		}

		public CampoLista addOnChangeCommand(Command<Void, String> c) {
			onChangeCommand = c;
			return this;
		}

		public CampoLista setSelectedValue(String item) {
			this.selectedItem = item;
			return this;
		}
	}

	public static class ColonnaLista extends TextColumn<Object[]> {
		private int index;

		public ColonnaLista(int index) {
			super();
			this.index = index;
		}

		@Override
		public String getValue(Object[] riga) {
			if (riga != null && riga.length > index && riga[index] != null) {

				if (riga[index] instanceof Boolean) {
					return ((Boolean) riga[index]) ? "Si" : "No";
				}

				if (riga[index] instanceof Date) {
					return dateFormat.format((Date) riga[index]);
				}

				if (riga[index] instanceof List) {
					@SuppressWarnings("unchecked") Iterator<String> valori = ((List<String>) riga[index]).iterator();
					StringBuilder bl = new StringBuilder();
					while (valori.hasNext()) {
						bl.append(valori.next());
						if (valori.hasNext())
							bl.append(", ");
					}
					return bl.toString();
				}

				return riga[index].toString();
			}
			return "";
		}

	}

	public void showErrori(List<String> errori) {
		this.errori = errori;
		showErrori();
	}

	private void showErrori() {
		// se ci sono errori li mostro
		if (errori.isEmpty() == false) {
			String message = "<br/>";
			for (String current : errori) {
				message = message + current + "<br/>";
			}

			alertPanel.setStyleName("alert-box error");

			String htmlSpan = errori.size() <= 1 ? "<span>Errore: </span>" : "<span>Errori: </span>";
			String messHtml = htmlSpan + message;
			alertPanel.getElement().setInnerHTML(messHtml);
			alertPanel.setVisible(true);
			errori.clear();
		}
	}

	public void setEnabled(boolean enabled) {
		aggiungiButton.setEnabled(enabled);
		new EnabledVisitor(enabled).scanListWidget(widgets);

		for (ELiminaButtonCell bottoneElimina : this.bottoniElimina) {
			bottoneElimina.setEnabled(enabled);
		}
		this.grid.redraw();

	}

	/*
	 * pulisce il form
	 */
	private class EnabledVisitor extends AbstractScanWidget {

		private boolean enabled;

		public EnabledVisitor(boolean enabled) {
			super();
			this.enabled = enabled;
		}

		@Override
		public void scanHidden(VisitableWidget visitableWidget) {}

		@Override
		public void scanTextBox(VisitableWidget visitableWidget) {
			((TextBox) visitableWidget.getWidget()).setEnabled(enabled);
		}

		@Override
		public void scanTextArea(VisitableWidget visitableWidget) {
			((TextArea) visitableWidget.getWidget()).setEnabled(enabled);
		}

		@Override
		public void scanHTMLPanel(VisitableWidget visitableWidget) {
			// do nothing
		}

		@Override
		public void scanDateBox(VisitableWidget visitableWidget) {
			((DateBox) visitableWidget.getWidget()).setEnabled(enabled);
		}

		@Override
		public void scanListBox(VisitableWidget visitableWidget) {
			((ListBox) visitableWidget.getWidget()).setEnabled(enabled);

		}

		@Override
		public void scanSuggestBox(VisitableWidget visitableWidget) {
			((SuggestBox) visitableWidget.getWidget()).setEnabled(enabled);
		}

		@Override
		public void scanCustomSuggestBox(VisitableWidget visitableWidget) {}

		@Override
		public void scanIntegerBox(VisitableWidget visitableWidget) {
			((IntegerBox) visitableWidget.getWidget()).setEnabled(enabled);
		}

		@Override
		public void scanDoubleBox(VisitableWidget visitableWidget) {
			((DoubleBox) visitableWidget.getWidget()).setEnabled(enabled);

		}

		@Override
		public void scanYesNoRadioButton(VisitableWidget visitableWidget) {
			((YesNoRadioButton) visitableWidget.getWidget()).setReadOnly(!enabled);

		}

		@Override
		public void scanListWidget(VisitableWidget visitableWidget) {
			((SimpleInputListWidget) visitableWidget.getWidget()).setAbilitato(enabled);

		}

	}
}
