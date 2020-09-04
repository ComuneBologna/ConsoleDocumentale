package it.eng.portlet.consolepec.gwt.client.drive.widget;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.drive.DriveElement;
import it.eng.cobo.consolepec.commons.drive.permessi.CreaElementoPermessoDrive;
import it.eng.cobo.consolepec.commons.drive.permessi.EliminaElementoPermessoDrive;
import it.eng.cobo.consolepec.commons.drive.permessi.ModificaElementoPermessoDrive;
import it.eng.cobo.consolepec.commons.drive.permessi.ModificaPermessiElementoPermessoDrive;
import it.eng.cobo.consolepec.commons.drive.permessi.PermessoDrive;
import it.eng.cobo.consolepec.commons.drive.permessi.VisualizzaElementoPermessoDrive;
import it.eng.portlet.consolepec.gwt.client.drive.ImageSource;
import it.eng.portlet.consolepec.gwt.client.drive.util.DrawingUtil;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Giacomo F.M.
 * @since 2019-07-04
 */
public class DriveRuoliWidget extends Composite {

	private static final String BOX_STYLE = "ruolo-box";
	private static final String BOX_ACTION_STYLE = "ruolo-box-action";

	private static final String RECURSIVE_CONFIRM_TEXT = "Impostando questo check le modifiche effettuate ai PERMESSI verranno riportate automaticamente"
			+ " anche a tutti gli elementi contenuti in questa cartella. Confermi di voler applicare la ricorsione a queste modifiche?";

	@UiField
	HTMLPanel container, title, ruoli;
	@UiField
	CheckBox checkRicorsione;
	@UiField(provided = true)
	Button addBtn;

	private static Binder binder = GWT.create(Binder.class);

	public interface Binder extends UiBinder<Widget, DriveRuoliWidget> {/**/}

	private ConfigurazioniHandler configurazioniHandler;

	private Set<PermessoDrive> aggiunti = new HashSet<>();
	private Set<PermessoDrive> rimossi = new HashSet<>();

	private DriveElement elemento;

	public DriveRuoliWidget(final DriveElement elemento, final ConfigurazioniHandler configurazioniHandler) {
		this.elemento = elemento;
		this.configurazioniHandler = configurazioniHandler;

		addBtn = DrawingUtil.drawFunBtn(null, ImageSource.loadPlus(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				drawNewPermessoLine();
				addBtn.setEnabled(false);
			}
		});

		initWidget(binder.createAndBindUi(this));

		checkRicorsione.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (elemento.isCartella()) {
					if (event.getValue()) {
						checkRicorsione.setValue(Window.confirm(RECURSIVE_CONFIRM_TEXT));
					}
				}
			}
		});

		init();
	}

	public void clear() {
		if (title != null)
			title.clear();
		if (ruoli != null)
			ruoli.clear();
		if (checkRicorsione != null)
			checkRicorsione.setValue(false);
		if (addBtn != null) {
			addBtn.setEnabled(true);
		}
	}

	private void init() {
		clear();
		title.add(new InlineLabel("Permessi"));
		for (PermessoDrive permesso : elemento.getPermessi()) {
			drawPermesso(permesso);
		}
	}

	private void drawPermesso(final PermessoDrive permesso) {
		final HTMLPanel box = new HTMLPanel("");
		box.setStylePrimaryName(BOX_STYLE);
		box.getElement().appendChild(ImageSource.loadGroup().getElement());
		AnagraficaRuolo ar = configurazioniHandler.getAnagraficaRuolo(permesso.getRuolo());
		box.add(new InlineLabel(ar != null ? ar.getEtichetta() : "Errore nella lettura del ruolo"));
		box.add(new InlineLabel(permesso.getDescrizione()));

		HTMLPanel action = new HTMLPanel("");
		action.setStylePrimaryName(BOX_ACTION_STYLE);
		action.add(DrawingUtil.drawFunBtn(null, ImageSource.loadTrash(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (Window.confirm("Confermi di voler eliminare questo permesso?")) {
					rimossi.add(permesso);
					ruoli.remove(box);
				}
			}
		}));
		box.add(action);

		ruoli.add(box);
	}

	private void drawNewPermessoLine() {
		final HTMLPanel box = new HTMLPanel("");
		box.setStylePrimaryName(BOX_STYLE);

		final ListBox ruoliBox = new ListBox();
		for (AnagraficaRuolo ar : configurazioniHandler.getAnagraficheRuoli()) {
			ruoliBox.addItem(ar.getEtichetta(), ar.getRuolo());
		}

		final ListBox permessiBox = new ListBox();
		permessiBox.addItem(VisualizzaElementoPermessoDrive.descrizione(), VisualizzaElementoPermessoDrive.class.getName());
		permessiBox.addItem(CreaElementoPermessoDrive.descrizione(), CreaElementoPermessoDrive.class.getName());
		permessiBox.addItem(ModificaElementoPermessoDrive.descrizione(), ModificaElementoPermessoDrive.class.getName());
		permessiBox.addItem(EliminaElementoPermessoDrive.descrizione(), EliminaElementoPermessoDrive.class.getName());
		permessiBox.addItem(ModificaPermessiElementoPermessoDrive.descrizione(), ModificaPermessiElementoPermessoDrive.class.getName());

		box.getElement().appendChild(ImageSource.loadGroup().getElement());

		box.add(ruoliBox);
		box.add(permessiBox);

		HTMLPanel action = new HTMLPanel("");
		action.setStylePrimaryName(BOX_ACTION_STYLE);
		action.add(DrawingUtil.drawFunBtn(null, ImageSource.loadOk(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				PermessoDrive permessoDrive = creaPermesso(ruoliBox, permessiBox);
				aggiunti.add(permessoDrive);
				drawPermesso(permessoDrive);
				ruoli.remove(box);
				addBtn.setEnabled(true);
			}
		}));
		action.add(DrawingUtil.drawFunBtn(null, ImageSource.loadCancel(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ruoli.remove(box);
				addBtn.setEnabled(true);
			}
		}));
		box.add(action);

		ruoli.add(box);
	}

	private static PermessoDrive creaPermesso(final ListBox ruoliBox, final ListBox permessiBox) {
		String ruoloSelezionato = ruoliBox.getSelectedValue();
		String permessoSelezionato = permessiBox.getSelectedValue();
		if (VisualizzaElementoPermessoDrive.class.getName().equals(permessoSelezionato)) {
			return new VisualizzaElementoPermessoDrive(ruoloSelezionato);
		}
		if (CreaElementoPermessoDrive.class.getName().equals(permessoSelezionato)) {
			return new CreaElementoPermessoDrive(ruoloSelezionato);
		}
		if (ModificaElementoPermessoDrive.class.getName().equals(permessoSelezionato)) {
			return new ModificaElementoPermessoDrive(ruoloSelezionato);
		}
		if (EliminaElementoPermessoDrive.class.getName().equals(permessoSelezionato)) {
			return new EliminaElementoPermessoDrive(ruoloSelezionato);
		}
		if (ModificaPermessiElementoPermessoDrive.class.getName().equals(permessoSelezionato)) {
			return new ModificaPermessiElementoPermessoDrive(ruoloSelezionato);
		}
		return null;
	}

	public ModificheRuoli getModifiche() {
		if (!aggiunti.isEmpty() || !rimossi.isEmpty()) {
			return new ModificheRuoli(checkRicorsione.getValue(), aggiunti, rimossi);
		}
		return null;
	}

	@Getter
	@AllArgsConstructor
	public class ModificheRuoli {
		private boolean ricorsivo;
		private Set<PermessoDrive> aggiunti;
		private Set<PermessoDrive> rimossi;
	}

}
