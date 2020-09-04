package it.eng.portlet.consolepec.gwt.client.presenter.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import it.eng.portlet.consolepec.gwt.client.presenter.event.SceltaTemplateFromInvioCSV.SceltaTemplateFromInvioCSVHandler;
import it.eng.portlet.consolepec.gwt.shared.model.InvioDaCsvBean;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SceltaTemplateFromInvioCSV extends GwtEvent<SceltaTemplateFromInvioCSVHandler> {

	@Getter
	private static Type<SceltaTemplateFromInvioCSVHandler> type = new Type<SceltaTemplateFromInvioCSVHandler>();

	@Getter
	private InvioDaCsvBean invioDaCsvBean;

	public interface SceltaTemplateFromInvioCSVHandler extends EventHandler {
		void onSceltaTemplate(SceltaTemplateFromInvioCSV event);
	}

	@Override
	public Type<SceltaTemplateFromInvioCSVHandler> getAssociatedType() {
		return type;
	}

	@Override
	protected void dispatch(SceltaTemplateFromInvioCSVHandler handler) {
		handler.onSceltaTemplate(this);
	}

}
