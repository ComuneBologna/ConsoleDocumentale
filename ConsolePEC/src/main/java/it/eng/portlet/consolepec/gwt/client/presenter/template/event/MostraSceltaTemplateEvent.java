package it.eng.portlet.consolepec.gwt.client.presenter.template.event;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import lombok.Getter;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class MostraSceltaTemplateEvent extends GwtEvent<MostraSceltaTemplateEvent.MostraSceltaTemplateHandler> {

	public static Type<MostraSceltaTemplateHandler> TYPE = new Type<MostraSceltaTemplateHandler>();

	public interface MostraSceltaTemplateHandler extends EventHandler {
		void onMostraSceltaTemplate(MostraSceltaTemplateEvent event);
	}

	@Getter
	private FascicoloDTO fascicoloDTO;
	
	@Getter
	private TipologiaPratica tipo;
	
	public MostraSceltaTemplateEvent(FascicoloDTO fascicoloDTO, TipologiaPratica tipo) {
		super();
		this.fascicoloDTO = fascicoloDTO;
		this.tipo = tipo;
	}

	@Override
	protected void dispatch(MostraSceltaTemplateHandler handler) {
		handler.onMostraSceltaTemplate(this);
	}

	@Override
	public Type<MostraSceltaTemplateHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraSceltaTemplateHandler> getType() {
		return TYPE;
	}
}
