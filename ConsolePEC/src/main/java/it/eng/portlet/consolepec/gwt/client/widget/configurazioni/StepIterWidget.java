package it.eng.portlet.consolepec.gwt.client.widget.configurazioni;

import java.util.Collection;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo.StepIter;
import it.eng.portlet.consolepec.gwt.client.widget.ListaCampiWidget;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo.TipoWidget;

/**
 *
 * @author biagiot
 *
 */
public class StepIterWidget extends Composite {

	interface StepIterWidgetBinder extends UiBinder<Widget, StepIterWidget> {/**/}

	private static StepIterWidgetBinder uiBinder = GWT.create(StepIterWidgetBinder.class);

	@UiField
	HTMLPanel stepIterPanel;

	private CampiStepIterWidget campiWidget;

	public StepIterWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void render(List<StepIter> stepIter) {
		this.stepIterPanel.clear();
		this.campiWidget = new CampiStepIterWidget(stepIter != null ? stepIter.size() + 20 : 50);
		this.campiWidget.render(true, true);
		if (stepIter != null && !stepIter.isEmpty()) {
			for (StepIter fa : stepIter) {
				this.campiWidget.add(fa);
			}
		}
		this.stepIterPanel.add(campiWidget);

		this.campiWidget.setAggiungiCommand(new AggiungiCampoCommand());
		this.campiWidget.setEliminaCommand(new EliminaCampoCommand());
	}

	public List<StepIter> getStepIter() {
		return campiWidget.getStepIter();
	}

	public class CampiStepIterWidget extends ListaCampiWidget<StepIter> {

		public CampiStepIterWidget(Integer limit) {
			super(limit);
		}

		public List<StepIter> getStepIter() {
			return getValori();
		}

		@Override
		protected void definisciCampi() {
			creaCampo("nomeStepIter", "Nome", TipoWidget.TEXTBOX, 0).obbligatorio(true);

			creaCampo("tipoStepIter", "Tipo", TipoWidget.LISTBOX, 1).obbligatorio(false).lista("", "Iniziale", "Finale");
			creaCampo("creaBozzaModello", "Attiva modello mail", TipoWidget.YESNORADIOBUTTON, 2).obbligatorio(true);

			creaCampo("notificaStepIter", "Attiva notifica mail", TipoWidget.YESNORADIOBUTTON, 3).obbligatorio(true);
			creaCampo("mailNotificaStep", "Indirizzi mail notifica", TipoWidget.VALORIBOX, 4).obbligatorio(false);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected StepIter converti(Object[] riga) {

			StepIter si = new StepIter();
			si.setNome((String) riga[0]);

			if ("Iniziale".equals(riga[1])) {
				si.setIniziale(true);
				si.setFinale(false);
			} else if ("Finale".equals(riga[1])) {
				si.setIniziale(false);
				si.setFinale(true);
			} else {
				si.setIniziale(false);
				si.setFinale(false);
			}

			si.setCreaBozza((boolean) riga[2]);

			si.getDestinatariNotifica().add("FALSE");

			if (riga[3] != null && (boolean) riga[3]) {
				si.getDestinatariNotifica().remove(0);
				si.getDestinatariNotifica().add("TRUE");
			}

			if (riga[4] != null) {
				si.getDestinatariNotifica().addAll((Collection<? extends String>) riga[4]);
			}

			return si;
		}

		@Override
		protected Object[] converti(StepIter riga) {
			String tipo = "";
			if (riga.isIniziale()) {
				tipo = "Iniziale";
			} else if (riga.isFinale()) {
				tipo = "Finale";
			}
			return new Object[] { riga.getNome(), tipo, riga.isCreaBozza(), riga.getDestinatariNotifica() != null && !riga.getDestinatariNotifica().isEmpty(), riga.getDestinatariNotifica() };
		}

		@Override
		protected boolean validaInserimento(StepIter riga, List<String> errori) {

			if (riga == null) {
				errori.add("Step iter non valido");
				return false;
			}

			if (riga.getNome() == null || riga.getNome().trim().isEmpty()) {
				errori.add("Nome obbligatorio");
				return false;
			}

			boolean attivaNotifica = riga.getDestinatariNotifica().get(0).equals("TRUE");
			riga.getDestinatariNotifica().remove(0);

			if (attivaNotifica && (riga.getDestinatariNotifica() == null || riga.getDestinatariNotifica().isEmpty())) {
				errori.add("Inserire almeno un destinatario di notifica");
				return false;
			}

			if (getValori().contains(riga)) {
				errori.add("Step iter già inserito");
				return false;
			}

			if (riga.isIniziale()) {
				for (StepIter v : getValori()) {
					if (v.isIniziale()) {
						errori.add("Non è possibile inserire più di uno step di iter iniziale");
						return false;
					}
				}
			}

			return true;
		}
	}

	public class AggiungiCampoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, StepIter> {

		@Override
		public Void exe(StepIter t) {
			campiWidget.add(t);
			return null;
		}

	}

	public class EliminaCampoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, StepIter> {

		@Override
		public Void exe(StepIter t) {
			campiWidget.remove(t);
			return null;
		}

	}

	public void clear() {
		if (campiWidget != null) {
			campiWidget.getValori().clear();
		}
	}

}
