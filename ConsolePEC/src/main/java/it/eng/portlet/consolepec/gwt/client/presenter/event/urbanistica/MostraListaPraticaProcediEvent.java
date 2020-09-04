package it.eng.portlet.consolepec.gwt.client.presenter.event.urbanistica;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author GiacomoFM
 * @since 07/nov/2017
 */
@NoArgsConstructor
@AllArgsConstructor
public class MostraListaPraticaProcediEvent extends GwtEvent<MostraListaPraticaProcediEvent.MostraListaPraticaProcedHandler> {

	private static Type<MostraListaPraticaProcedHandler> TYPE = new Type<>();

	@Getter
	private String pathFascicolo;
	@Getter
	private Map<String, Object> filtri;
	@Getter
	private Map<String, Boolean> ordinamento = new HashMap<>();
	@Getter
	@Setter
	private String nameClass;
	@Setter
	@Getter
	private String idFascicolo;

	public MostraListaPraticaProcediEvent(String pathFascicolo, String nameClass) {
		this.pathFascicolo = pathFascicolo;
		filtri = Collections.emptyMap();
		this.nameClass = nameClass;
	}

	public interface MostraListaPraticaProcedHandler extends EventHandler {
		void onMostraListaPraticaProced(MostraListaPraticaProcediEvent event);
	}

	@Override
	public Type<MostraListaPraticaProcedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraListaPraticaProcedHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(MostraListaPraticaProcedHandler handler) {
		handler.onMostraListaPraticaProced(this);
	}
}
