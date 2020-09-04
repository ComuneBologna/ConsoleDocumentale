package it.eng.portlet.consolepec.gwt.client.widget.configurazioni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import it.eng.portlet.consolepec.gwt.client.widget.ListaCampiWidget;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo.TipoWidget;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author biagiot
 *
 */
public class ValoriDatiAggiuntiviWidget extends Composite {

	interface ValoriDatiAggiuntiviWidgetBinder extends UiBinder<Widget, ValoriDatiAggiuntiviWidget> {/**/}

	private static ValoriDatiAggiuntiviWidgetBinder uiBinder = GWT.create(ValoriDatiAggiuntiviWidgetBinder.class);

	@UiField
	HTMLPanel valoriPanel;

	private CampiValoriPredefinitiDatiAggiuntiviWidget campiWidget;
	private List<String> idDatiAggiuntivi = new ArrayList<String>();

	public ValoriDatiAggiuntiviWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void render(List<String> idDatiAggiuntivi, Map<String, List<String>> valoriPredefiniti) {
		this.idDatiAggiuntivi = idDatiAggiuntivi;
		this.valoriPanel.clear();
		this.campiWidget = new CampiValoriPredefinitiDatiAggiuntiviWidget(valoriPredefiniti != null ? valoriPredefiniti.size() + 50 : 100);
		this.campiWidget.render(true, true);
		if (valoriPredefiniti != null && !valoriPredefiniti.isEmpty()) {
			for (Entry<String, List<String>> fa : valoriPredefiniti.entrySet()) {
				for (String val : fa.getValue()) {
					if (val != null && !val.trim().isEmpty()) {
						this.campiWidget.add(new ValorePredefinitoBean(fa.getKey(), val));
					}
				}
			}
		}
		this.valoriPanel.add(campiWidget);
		this.campiWidget.setAggiungiCommand(new AggiungiCampoCommand());
		this.campiWidget.setEliminaCommand(new EliminaCampoCommand());
	}

	public Map<String, List<String>> getValoriPredefiniti() {
		return toMap(campiWidget.getValori());
	}

	public List<String> getValoriPredefiniti(String idDato) {
		return getValoriPredefiniti().get(idDato);
	}

	public class CampiValoriPredefinitiDatiAggiuntiviWidget extends ListaCampiWidget<ValorePredefinitoBean> {

		public CampiValoriPredefinitiDatiAggiuntiviWidget(Integer limit) {
			super(limit);
		}

		@Override
		protected void definisciCampi() {
			String[] tipiArr = new String[idDatiAggiuntivi.size()];
			tipiArr = idDatiAggiuntivi.toArray(tipiArr);

			creaCampo("idDatoAggiuntivoValMultiplo", "Identificativo dato aggiuntivo", TipoWidget.LISTBOX, 0).obbligatorio(true).lista(tipiArr);
			creaCampo("valorePredefinito", "Valore", TipoWidget.TEXTBOX, 1).obbligatorio(true);
		}

		@Override
		protected ValorePredefinitoBean converti(Object[] riga) {
			ValorePredefinitoBean res = null;
			if (riga[0] != null && riga[1] != null) {
				String idDato = (String) riga[0];
				String valore = (String) riga[1];
				res = new ValorePredefinitoBean(idDato, valore);
			}
			return res;
		}

		@Override
		protected Object[] converti(ValorePredefinitoBean riga) {
			return new Object[] { riga.getIdDato(), riga.getValore() };
		}

		@Override
		protected boolean validaInserimento(ValorePredefinitoBean riga, List<String> errori) {
			if (riga == null) {
				errori.add("Valore non valido");
				return false;
			}
			if (getValori().contains(riga)) {
				errori.add("Valore già inserito");
				return false;
			}
			if (!idDatiAggiuntivi.contains(riga.getIdDato())) {
				errori.add("Id dato aggiuntivo non valido");
				return false;
			}
			return true;
		}
	}

	public class AggiungiCampoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, ValorePredefinitoBean> {

		@Override
		public Void exe(ValorePredefinitoBean t) {
			campiWidget.add(t);
			return null;
		}
	}

	public class EliminaCampoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, ValorePredefinitoBean> {

		@Override
		public Void exe(ValorePredefinitoBean t) {
			campiWidget.remove(t);
			return null;
		}
	}

	public void clear() {
		if (campiWidget != null) {
			campiWidget.getValori().clear();
		}
	}

	@AllArgsConstructor
	@Data
	public static class ValorePredefinitoBean {
		private String idDato;
		private String valore;
	}

	private Map<String, List<String>> toMap(List<ValorePredefinitoBean> valori) {
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		for (ValorePredefinitoBean b : valori) {
			if (b.getValore() != null && !b.getValore().trim().isEmpty()) {
				if (result.get(b.getIdDato()) != null) {
					if (!result.get(b.getIdDato()).contains(b.getValore())) {
						result.get(b.getIdDato()).add(b.getValore());
					}
				} else {
					List<String> val = new ArrayList<String>();
					val.add(b.getValore());
					result.put(b.getIdDato(), val);
				}
			}
		}
		return result;
	}
}
