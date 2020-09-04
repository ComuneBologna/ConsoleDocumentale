package it.eng.portlet.consolepec.gwt.client.presenter.template.event;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import it.eng.portlet.consolepec.gwt.shared.model.CampoTemplateDTO;

public class GoToInserimentoCampiTemplatePdfEvent extends GwtEvent<GoToInserimentoCampiTemplatePdfEvent.GoToInserimentoCampiTemplatePdfHandler> {

	private List<CampoTemplateDTO> campi = new ArrayList<CampoTemplateDTO>();
	private boolean titoloAutomatico;

	public static Type<GoToInserimentoCampiTemplatePdfHandler> TYPE = new Type<GoToInserimentoCampiTemplatePdfHandler>();

	public interface GoToInserimentoCampiTemplatePdfHandler extends EventHandler {
		void onGoToInserimentoCampiTemplatePdf(GoToInserimentoCampiTemplatePdfEvent event);
	}

	public GoToInserimentoCampiTemplatePdfEvent(List<CampoTemplateDTO> campi, boolean titoloAutomatico) {
		super();
		this.campi.addAll(campi);
		this.titoloAutomatico = titoloAutomatico;
	}

	@Override
	protected void dispatch(GoToInserimentoCampiTemplatePdfHandler handler) {
		handler.onGoToInserimentoCampiTemplatePdf(this);
	}

	@Override
	public Type<GoToInserimentoCampiTemplatePdfHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<GoToInserimentoCampiTemplatePdfHandler> getType() {
		return TYPE;
	}

	public List<CampoTemplateDTO> getCampi() {
		return campi;
	}

	public boolean isTitoloAutomatico() {
		return titoloAutomatico;
	}
}
