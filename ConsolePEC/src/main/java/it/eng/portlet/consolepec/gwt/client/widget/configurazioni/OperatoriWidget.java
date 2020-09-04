package it.eng.portlet.consolepec.gwt.client.widget.configurazioni;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import it.eng.portlet.consolepec.gwt.client.widget.ListaCampiWidget;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo.TipoWidget;

/**
 *
 * @author biagiot
 *
 */
public class OperatoriWidget extends Composite {

	interface OperatoriWidgetBinder extends UiBinder<Widget, OperatoriWidget> {}

	private static OperatoriWidgetBinder uiBinder = GWT.create(OperatoriWidgetBinder.class);

	@UiField
	HTMLPanel operatoriPanel;

	private CampiOperatoriWidget campiWidget;

	public OperatoriWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void render(List<String> operatori) {
		this.operatoriPanel.clear();
		this.campiWidget = new CampiOperatoriWidget(operatori != null ? operatori.size() + 10 : 20);
		this.campiWidget.render(true, true);
		if (operatori != null && !operatori.isEmpty()) {
			for (String fa : operatori) {
				this.campiWidget.add(fa);
			}
		}
		this.operatoriPanel.add(campiWidget);

		this.campiWidget.setAggiungiCommand(new AggiungiCampoCommand());
		this.campiWidget.setEliminaCommand(new EliminaCampoCommand());
	}

	public List<String> getOperatori() {
		return campiWidget.getOperatori();
	}

	public class CampiOperatoriWidget extends ListaCampiWidget<String> {

		public CampiOperatoriWidget(Integer limit) {
			super(limit);
		}

		public List<String> getOperatori() {
			return getValori();
		}

		@Override
		protected void definisciCampi() {
			creaCampo("operatore", "Operatore", TipoWidget.TEXTBOX, 0).obbligatorio(true);
		}

		@Override
		protected String converti(Object[] riga) {

			String operatore = null;

			if (riga[0] != null) {
				operatore = (String) riga[0];
			}

			return operatore;
		}

		@Override
		protected Object[] converti(String riga) {
			if (riga != null) {
				return new Object[] { riga };
			}

			return null;
		}

		@Override
		protected boolean validaInserimento(String riga, List<String> errori) {

			if (riga == null || riga.trim().isEmpty()) {
				errori.add("Operatore non valido");
				return false;
			}

			if (getValori().contains(riga)) {
				errori.add("Operatore già inserito");
				return false;
			}

			return true;
		}
	}

	public class AggiungiCampoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, String> {

		@Override
		public Void exe(String t) {
			if (!campiWidget.getValori().contains(t))
				campiWidget.add(t);
			return null;
		}

	}

	public class EliminaCampoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, String> {

		@Override
		public Void exe(String t) {
			if (campiWidget.getValori().contains(t))
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
