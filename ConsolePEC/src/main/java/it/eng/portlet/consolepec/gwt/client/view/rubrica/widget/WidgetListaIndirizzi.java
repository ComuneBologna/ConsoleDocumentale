package it.eng.portlet.consolepec.gwt.client.view.rubrica.widget;

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
import it.eng.cobo.consolepec.commons.rubrica.Indirizzo;
import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget;

/**
 * @author GiacomoFM
 * @since 18/ott/2017
 */
public class WidgetListaIndirizzi extends Composite {

	private final List<Indirizzo> indirizzi = new LinkedList<Indirizzo>();

	private Stato stato;

	@UiField(provided = true)
	ConsoleDisclosurePanel indirizziDisclosure = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Lista Indirizzi");

	@UiField
	HTMLPanel indirizziPanel;

	@UiField
	TextBox tipologiaBox;
	@UiField
	TextBox viaBox;
	@UiField
	TextBox civicoBox;
	@UiField
	TextBox esponenteBox;
	@UiField
	TextBox internoBox;
	@UiField
	TextBox pianoBox;
	@UiField
	TextBox comuneBox;
	@UiField
	TextBox capBox;
	@UiField
	TextBox nazioneBox;

	@UiField
	Button aggiungiIndirizzo;

	private static Binder binder = GWT.create(Binder.class);

	public interface Binder extends UiBinder<Widget, WidgetListaIndirizzi> {
		//
	}

	public WidgetListaIndirizzi() {
		super();
		initWidget(binder.createAndBindUi(this));

		indirizziDisclosure.setOpen(false);

		aggiungiIndirizzo.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				if (!Strings.isNullOrEmpty(viaBox.getText())) {
					Indirizzo i = new Indirizzo();
					i.setTipologia(tipologiaBox.getText());
					i.setVia(viaBox.getText());
					i.setCivico(civicoBox.getText());
					i.setEsponente(esponenteBox.getText());
					i.setInterno(internoBox.getText());
					i.setPiano(pianoBox.getText());
					i.setComune(comuneBox.getText());
					i.setCap(capBox.getText());
					i.setNazione(nazioneBox.getText());

					indirizzi.add(i);
					innerSet(indirizzi);
					reset();
				}

			}
		});
	}

	public List<Indirizzo> get() {
		return this.indirizzi;
	}

	public void set(List<Indirizzo> indirizzi, Stato stato) {
		this.stato = stato;
		if (Stato.DISATTIVA.equals(stato)) {
			disattiva(tipologiaBox, viaBox, civicoBox, esponenteBox, internoBox, pianoBox, comuneBox, capBox, nazioneBox);
			aggiungiIndirizzo.setEnabled(false);
		} else {
			attiva(tipologiaBox, viaBox, civicoBox, esponenteBox, internoBox, pianoBox, comuneBox, capBox, nazioneBox);
			aggiungiIndirizzo.setEnabled(true);
		}
		indirizziDisclosure.setOpen(indirizzi != null && !indirizzi.isEmpty());
		innerSet(indirizzi);
	}

	private static void disattiva(TextBox... boxs) {
		for (TextBox textBox : boxs) {
			textBox.setEnabled(false);
			textBox.setStylePrimaryName("disabilitato");
		}
	}

	private static void attiva(TextBox... boxs) {
		for (TextBox textBox : boxs) {
			textBox.setEnabled(true);
			textBox.removeStyleName("disabilitato");
		}
	}

	private void innerSet(List<Indirizzo> indirizzi) {
		List<Indirizzo> tmp = new LinkedList<Indirizzo>(indirizzi);
		this.indirizzi.clear();
		indirizziPanel.clear();

		this.indirizzi.addAll(tmp);
		disegnaTabella();
	}

	private void reset() {
		aggiungiIndirizzo.setText("Aggiungi Indirizzo");

		tipologiaBox.setText("");
		viaBox.setText("");
		civicoBox.setText("");
		esponenteBox.setText("");
		internoBox.setText("");
		pianoBox.setText("");

		comuneBox.setText("");
		capBox.setText("");
		nazioneBox.setText("");
	}

	private void modificaIndirizzo(Indirizzo i) {
		if (i != null) {
			aggiungiIndirizzo.setText("Modifica Indirizzo");

			tipologiaBox.setText(i.getTipologia());
			viaBox.setText(i.getVia());
			civicoBox.setText(i.getCivico());
			esponenteBox.setText(i.getEsponente());
			internoBox.setText(i.getInterno());
			pianoBox.setText(i.getPiano());

			comuneBox.setText(i.getComune());
			capBox.setText(i.getCap());
			nazioneBox.setText(i.getNazione());
		}
	}

	private void disegnaTabella() {
		DataGridWidget<Indirizzo> dataGrid = new DataGridWidget<Indirizzo>();
		dataGrid.addColumn(new ColonnaTipologia(), "Tipologia");
		dataGrid.addColumn(new ColonnaIndirizzo(), "Indirizzo");

		ColonnaModifica colonnaModifica = new ColonnaModifica();
		colonnaModifica.setFieldUpdater(new FieldUpdater<Indirizzo, String>() {
			@Override
			public void update(int index, Indirizzo object, String value) {
				Indirizzo i = indirizzi.remove(index);
				modificaIndirizzo(i);
				innerSet(indirizzi);
			}
		});
		dataGrid.addColumn(colonnaModifica, "");

		ColonnaElimina colonnaElimina = new ColonnaElimina();
		colonnaElimina.setFieldUpdater(new FieldUpdater<Indirizzo, String>() {
			@Override
			public void update(int index, Indirizzo object, String value) {
				indirizzi.remove(index);
				innerSet(indirizzi);
			}
		});
		dataGrid.addColumn(colonnaElimina, "");

		ListDataProvider<Indirizzo> listDataProvider = new ListDataProvider<Indirizzo>();
		listDataProvider.setList(indirizzi);
		listDataProvider.addDataDisplay(dataGrid);

		indirizziPanel.add(dataGrid);
	}

	private static class ColonnaTipologia extends TextColumn<Indirizzo> {
		@Override
		public String getValue(Indirizzo i) {
			return i.getTipologia();
		}
	}

	private static class ColonnaIndirizzo extends TextColumn<Indirizzo> {
		@Override
		public String getValue(Indirizzo i) {
			return i.toString(false);
			// return spaceIfNull(i.getVia()) //
			// + " " + spaceIfNull(i.getCivico()) //
			// + " " + spaceIfNull(i.getEsponente()) //
			// + " " + spaceIfNull(i.getInterno()) //
			// + " " + spaceIfNull(i.getPiano()) //
			// + " " + spaceIfNull(i.getComune()) //
			// + " " + spaceIfNull(i.getCap()) //
			// + " " + spaceIfNull(i.getNazione());
		}
	}

	public class ColonnaModifica extends Column<Indirizzo, String> {

		public ColonnaModifica() {
			super(new StyledButtonCell());
			setCellStyleNames("last-column");
		}

		@Override
		public String getValue(Indirizzo i) {
			return "Modifica";
		}

	}

	private class ColonnaElimina extends Column<Indirizzo, String> {

		public ColonnaElimina() {
			super(new StyledButtonCell());
			setCellStyleNames("last-column");
		}

		@Override
		public String getValue(Indirizzo i) {
			return "Elimina";
		}
	}

	private class StyledButtonCell extends ButtonCell {
		@Override
		public void render(Cell.Context context, SafeHtml data, SafeHtmlBuilder sb) {
			StringBuilder s = new StringBuilder("<button class=\"btn worklist_btn\" type=\"button\" tabindex=\"-1\" style=\"max-width: 60px;\"");
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
