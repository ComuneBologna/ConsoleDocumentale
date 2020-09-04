package it.eng.portlet.consolepec.gwt.client.widget;

import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class DettaglioAllegatiWidget extends Composite {

	private static DettaglioAllegatiWidgetUiBinder uiBinder = GWT.create(DettaglioAllegatiWidgetUiBinder.class);

	interface DettaglioAllegatiWidgetUiBinder extends UiBinder<Widget, DettaglioAllegatiWidget> {
	}

	@UiField
	HTMLPanel elencoAllegatiPanel;
	@UiField
	Label nessunAllegatoLabel;
	private Command<Void, AllegatoDTO> mostraDettaglioAllegatoCommand;
	private Command<Void, AllegatoDTO> downloadAllegatoCommand;

	public DettaglioAllegatiWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setMostraDettaglioAllegatoCommand(Command<Void, AllegatoDTO> mostraDettaglioAllegatoCommand) {
		this.mostraDettaglioAllegatoCommand = mostraDettaglioAllegatoCommand;
	}

	public void setDownloadAllegatoCommand(Command<Void, AllegatoDTO> downloadAllegatoCommand) {
		this.downloadAllegatoCommand = downloadAllegatoCommand;
	}

	public void mostraAllegati(List<AllegatoDTO> allegati) {
		this.mostraAllegati(allegati, true);
	}

	public void mostraAllegati(List<AllegatoDTO> allegati, boolean checkBoxVisibile) {
		this.elencoAllegatiPanel.clear();
		while (this.elencoAllegatiPanel.getElement().hasChildNodes()) {
			this.elencoAllegatiPanel.getElement().removeChild(this.elencoAllegatiPanel.getElement().getLastChild());
		}
		if (allegati.size() > 0) {
			for (final AllegatoDTO allegato : allegati) {
				ElementoAllegatoElencoWidget allgwidget = new ElementoAllegatoElencoWidget();
				allgwidget.setCheckBoxVisible(checkBoxVisibile);
				allgwidget.setDownloadAllegatoCommand(downloadAllegatoCommand);
				allgwidget.setMostraDettaglioAllegatoCommand(mostraDettaglioAllegatoCommand);
				allgwidget.setSelezionaAllegatoCommand(null);
				elencoAllegatiPanel.add(allgwidget);
				allgwidget.mostraDettaglio(allegato);
			}
			nessunAllegatoLabel.setVisible(false);
			elencoAllegatiPanel.setVisible(true);
		} else {
			nessunAllegatoLabel.setVisible(true);
			elencoAllegatiPanel.setVisible(false);
		}
	}

}
