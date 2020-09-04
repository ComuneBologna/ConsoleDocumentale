package it.eng.portlet.consolepec.gwt.client.presenter.template.event;

import it.eng.portlet.consolepec.gwt.shared.model.CampoTemplateDTO;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class GoToInserimentoCampiTemplateEmailEvent extends GwtEvent<GoToInserimentoCampiTemplateEmailEvent.GoToInserimentoCampiTemplateEmailHandler> {
	
	private List<CampoTemplateDTO> campi = new ArrayList<CampoTemplateDTO>();

	public static Type<GoToInserimentoCampiTemplateEmailHandler> TYPE = new Type<GoToInserimentoCampiTemplateEmailHandler>();

	public interface GoToInserimentoCampiTemplateEmailHandler extends EventHandler {
		void onGoToInserimentoCampiTemplateEmail(GoToInserimentoCampiTemplateEmailEvent event);
	}
	
	public GoToInserimentoCampiTemplateEmailEvent(List<CampoTemplateDTO> campi) {
		super();
		this.campi.addAll(campi);
	}

	@Override
	protected void dispatch(GoToInserimentoCampiTemplateEmailHandler handler) {
		handler.onGoToInserimentoCampiTemplateEmail(this);
	}

	@Override
	public Type<GoToInserimentoCampiTemplateEmailHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<GoToInserimentoCampiTemplateEmailHandler> getType() {
		return TYPE;
	}

	public List<CampoTemplateDTO> getCampi(){
		return campi;
	}
}
