package it.eng.portlet.consolepec.gwt.client.widget.protocollazione;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ElementoGruppoIndirizziProtocollazioneWidget extends ElementoGruppoProtocollazioneWidget<IndirizzoWidgetDto> {

	public ElementoGruppoIndirizziProtocollazioneWidget() {
		super();
	}

	@Override
	protected void clearWidget(ArrayList<Widget> newArrayList) {
		((TextBox) ((HTMLPanel) newArrayList.get(0)).getWidget(0)).setValue(null);
		((TextBox) ((HTMLPanel) newArrayList.get(1)).getWidget(0)).setValue(null);
		((TextBox) ((HTMLPanel) newArrayList.get(2)).getWidget(0)).setValue(null);
		((TextBox) ((HTMLPanel) newArrayList.get(3)).getWidget(0)).setValue(null);
		((TextBox) ((HTMLPanel) newArrayList.get(4)).getWidget(0)).setValue(null);

	}

	@Override
	protected IndirizzoWidgetDto createElementFromGroupWidgets(ArrayList<Widget> newArrayList) {
		IndirizzoWidgetDto indirizzoWidgetDto = new IndirizzoWidgetDto();
		TextBox via = (TextBox) ((HTMLPanel) newArrayList.get(0)).getWidget(0);
		TextBox civico = (TextBox) ((HTMLPanel) newArrayList.get(1)).getWidget(0);
		TextBox esponenteCivico = (TextBox) ((HTMLPanel) newArrayList.get(2)).getWidget(0);
		TextBox interno = (TextBox) ((HTMLPanel) newArrayList.get(3)).getWidget(0);
		TextBox esponenteInterno = (TextBox) ((HTMLPanel) newArrayList.get(4)).getWidget(0);
		indirizzoWidgetDto.setCivico(civico.getValue());
		indirizzoWidgetDto.setDescrizioneVia(via.getValue());
		indirizzoWidgetDto.setEsponente(esponenteCivico.getValue());
		indirizzoWidgetDto.setInterno(interno.getValue());
		indirizzoWidgetDto.setEsponenteInterno(esponenteInterno.getValue());
		return indirizzoWidgetDto;
	}

	public class EliminaIndirizzoCommmand extends AbstractEliminaCommand {

		@Override
		protected void removeFromList(String name) {
			IndirizzoWidgetDto indirizzo = null;
			for (IndirizzoWidgetDto i : getElementi())
				if (i.toString().equals(name))
					indirizzo = i;

			if (indirizzo != null)
				getElementi().remove(indirizzo);

		}
	}

}
