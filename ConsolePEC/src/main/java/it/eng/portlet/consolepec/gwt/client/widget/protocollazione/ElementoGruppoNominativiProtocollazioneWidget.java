package it.eng.portlet.consolepec.gwt.client.widget.protocollazione;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ElementoGruppoNominativiProtocollazioneWidget extends ElementoGruppoProtocollazioneWidget<NominativoDto> {

	@Override
	protected void clearWidget(ArrayList<Widget> newArrayList) {
		((ListBox) ((HTMLPanel) newArrayList.get(0)).getWidget(0)).setItemSelected(1, true);
		((TextArea) ((HTMLPanel) newArrayList.get(1)).getWidget(0)).setValue(null);
		((TextBox) ((HTMLPanel) newArrayList.get(2)).getWidget(0)).setValue(null);

	}

	@Override
	protected NominativoDto createElementFromGroupWidgets(ArrayList<Widget> newArrayList) {

		ListBox listBox = (ListBox) ((HTMLPanel) newArrayList.get(0)).getWidget(0);

		NominativoDto nominativoDto = new NominativoDto();

		nominativoDto.setTipo(listBox.getValue(listBox.getSelectedIndex()));
		nominativoDto.setDescrizione(((TextArea) ((HTMLPanel) newArrayList.get(1)).getWidget(0)).getValue());
		nominativoDto.setCodiceFiscale(((TextBox) ((HTMLPanel) newArrayList.get(2)).getWidget(0)).getValue());
		return nominativoDto;
	}

	public class EliminaNominativoCommmand extends AbstractEliminaCommand {

		@Override
		protected void removeFromList(String name) {
			NominativoDto indirizzo = null;
			for (NominativoDto i : getElementi())
				if (i.toString().equals(name))
					indirizzo = i;

			if (indirizzo != null)
				getElementi().remove(indirizzo);

		}
	}

}
