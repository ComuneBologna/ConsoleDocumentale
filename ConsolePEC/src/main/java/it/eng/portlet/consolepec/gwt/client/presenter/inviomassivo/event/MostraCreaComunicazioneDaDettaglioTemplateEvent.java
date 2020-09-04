package it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event;

import it.eng.portlet.consolepec.gwt.shared.model.TemplateDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class MostraCreaComunicazioneDaDettaglioTemplateEvent extends GwtEvent<MostraCreaComunicazioneDaDettaglioTemplateEvent.MostraCreaComunicazioneDaDettaglioTemplateHandler> {

	public static Type<MostraCreaComunicazioneDaDettaglioTemplateHandler> TYPE = new Type<MostraCreaComunicazioneDaDettaglioTemplateHandler>();

	public interface MostraCreaComunicazioneDaDettaglioTemplateHandler extends EventHandler {
		void onMostraCreaComunicazioneDaDettaglioTemplate(MostraCreaComunicazioneDaDettaglioTemplateEvent event);
	}

	private TemplateDTO template;
	
	
	public MostraCreaComunicazioneDaDettaglioTemplateEvent(TemplateDTO template) {
		super();
		this.template = template;
	}

	@Override
	protected void dispatch(MostraCreaComunicazioneDaDettaglioTemplateHandler handler) {
		handler.onMostraCreaComunicazioneDaDettaglioTemplate(this);
	}

	@Override
	public Type<MostraCreaComunicazioneDaDettaglioTemplateHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraCreaComunicazioneDaDettaglioTemplateHandler> getType() {
		return TYPE;
	}

	public TemplateDTO getTemplate() {
		return template;
	}

}
