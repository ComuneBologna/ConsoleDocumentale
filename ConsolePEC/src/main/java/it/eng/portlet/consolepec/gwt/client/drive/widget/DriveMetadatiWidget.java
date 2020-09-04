package it.eng.portlet.consolepec.gwt.client.drive.widget;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import it.eng.cobo.consolepec.commons.drive.Dizionario;
import it.eng.cobo.consolepec.commons.drive.DriveElement;
import it.eng.cobo.consolepec.commons.drive.metadato.Metadato;
import it.eng.cobo.consolepec.commons.drive.metadato.MetadatoSingolo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo.DatoAggiuntivoVisitorAdapter;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreSingolo;
import it.eng.portlet.consolepec.gwt.client.handler.drive.DriveHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author Giacomo F.M.
 * @since 2019-06-19
 */
public class DriveMetadatiWidget extends Composite {

	private static final String BOX_STYLE = "metadato-box";
	private static final String BOX_DIZIONARIO_STYLE = "dizionario-box";

	public static final Dizionario EMPTY = new Dizionario();
	{
		EMPTY.setNome("nessun-dizionario");
		EMPTY.setLabel("Nessun dizionario");
	}

	@Getter
	private boolean modifica;

	@UiField
	HTMLPanel container;
	@UiField
	HTMLPanel title;
	@UiField
	HTMLPanel metadati;

	private static Binder binder = GWT.create(Binder.class);

	public interface Binder extends UiBinder<Widget, DriveMetadatiWidget> {/**/}

	private Dizionario dizionario;
	private DriveElement elemento;

	private DriveHandler driveHandler;

	private PresentaDizionarioVisitor showDizionarioVisitor;
	private ModificaDizionarioVisitor editDizionarioVisitor;

	public DriveMetadatiWidget(final DriveElement elemento, final DriveHandler configurazioniHandler) {
		this.elemento = elemento;
		this.driveHandler = configurazioniHandler;
		initWidget(binder.createAndBindUi(this));
		show();
	}

	public void clear() {
		if (title != null) {
			title.clear();
		}
		if (metadati != null) {
			metadati.clear();
		}

		showDizionarioVisitor = null;
		editDizionarioVisitor = null;
	}

	public void ridisegna(boolean modifica) {
		clear();
		if (modifica) {
			edit();
		} else {
			show();
		}
	}

	private void show() {
		modifica = false;
		dizionario = driveHandler.getDizionario(elemento.getDizionario());
		if (dizionario == null) {
			container.setVisible(false);
		} else {
			title.add(new InlineLabel(dizionario.getLabel()));
			showDizionarioVisitor = new PresentaDizionarioVisitor(metadati, elemento.getMetadati());
			for (DatoAggiuntivo da : dizionario.getDatiAggiuntivi()) {
				da.accept(showDizionarioVisitor);
			}
		}
	}

	private void edit() {
		modifica = true;
		container.setVisible(true);
		title.add(new InlineLabel("Modifica dizionario"));

		int index = 0;
		final ListBox dizionariBox = new ListBox();
		if (dizionario == null) {
			dizionariBox.addItem(EMPTY.getLabel(), EMPTY.getNome());
		}
		for (Dizionario d : driveHandler.getDizionari(elemento.isCartella())) {
			if (dizionario != null && dizionario.getNome().equals(d.getNome())) {
				index = dizionariBox.getItemCount();
			}
			dizionariBox.addItem(d.getLabel(), d.getNome());
		}
		dizionariBox.setSelectedIndex(index);
		dizionariBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				Dizionario d = driveHandler.getDizionario(dizionariBox.getSelectedValue());
				drawMetadatiEditPanel(dizionariBox, d != null ? d : EMPTY);
			}
		});

		drawMetadatiEditPanel(dizionariBox, dizionario != null ? dizionario : EMPTY);
	}

	protected void drawMetadatiEditPanel(final ListBox listBox, final Dizionario d) {
		metadati.clear();
		HTMLPanel div = new HTMLPanel("");
		div.setStylePrimaryName(BOX_DIZIONARIO_STYLE);
		div.add(listBox);
		metadati.add(div);
		editDizionarioVisitor = new ModificaDizionarioVisitor(new ModificheDizionario(d), metadati, elemento.getMetadati());
		for (DatoAggiuntivo da : d.getDatiAggiuntivi()) {
			da.accept(editDizionarioVisitor);
		}
	}

	public ModificheDizionario getModifiche() {
		if (editDizionarioVisitor != null && !EMPTY.getNome().equals(editDizionarioVisitor.getModifiche().getDizionario().getNome())) {
			return editDizionarioVisitor.getModifiche();
		}
		return null;
	}

	@Getter
	@RequiredArgsConstructor
	public class ModificheDizionario {
		@NonNull
		private Dizionario dizionario;
		private Set<Metadato> metadati = new HashSet<>();
	}

	@AllArgsConstructor
	private static class PresentaDizionarioVisitor extends DatoAggiuntivoVisitorAdapter {

		private HTMLPanel panel;
		private Set<Metadato> metadati;

		@Override
		public void visit(DatoAggiuntivoValoreSingolo datoAggiuntivoValoreSingolo) {
			HTMLPanel box = new HTMLPanel("");
			box.setStylePrimaryName(BOX_STYLE);
			box.add(new InlineLabel(datoAggiuntivoValoreSingolo.getDescrizione()));
			InlineLabel value = new InlineLabel();
			for (Metadato m : metadati) {
				if (datoAggiuntivoValoreSingolo.getNome().equals(m.getChiave())) {
					value.setText(((MetadatoSingolo) m).getValore());
				}
			}
			box.add(value);
			panel.add(box);
		}

	}

	@RequiredArgsConstructor
	private static class ModificaDizionarioVisitor extends DatoAggiuntivoVisitorAdapter {

		@NonNull
		private ModificheDizionario mod;
		@NonNull
		private HTMLPanel panel;
		@NonNull
		private Set<Metadato> metadati;

		private Map<String, TextBox> metadatiSingoli = new HashMap<>();

		@Override
		public void visit(DatoAggiuntivoValoreSingolo datoAggiuntivoValoreSingolo) {
			HTMLPanel box = new HTMLPanel("");
			box.setStylePrimaryName(BOX_STYLE);
			box.add(new InlineLabel(datoAggiuntivoValoreSingolo.getDescrizione()));
			TextBox input = new TextBox();
			for (Metadato m : metadati) {
				if (datoAggiuntivoValoreSingolo.getNome().equals(m.getChiave())) {
					input.setText(((MetadatoSingolo) m).getValore());
				}
			}

			metadatiSingoli.put(datoAggiuntivoValoreSingolo.getNome(), input);

			box.add(input);
			panel.add(box);
		}

		public ModificheDizionario getModifiche() {
			mod.getMetadati().clear();
			for (Entry<String, TextBox> entry : metadatiSingoli.entrySet()) {
				MetadatoSingolo ms = new MetadatoSingolo(entry.getKey());
				ms.setValore(entry.getValue().getText());
				mod.getMetadati().add(ms);
			}
			return mod;
		}

	}

}
