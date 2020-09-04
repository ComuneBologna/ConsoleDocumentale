package it.eng.portlet.consolepec.gwt.client.worklist;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;

import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.EspandiRigaEventListener;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

public class RigaEspansaPecOutStrategy extends AbstractRigaEspansaPecStrategy {
	private Command<Void, String> mostraDettaglioPraticaCommand;

	private Command<Void, AllegatoDTO> mostraDettaglioAllegatoCommand;

	public RigaEspansaPecOutStrategy(Command<Void, String> mostraDettaglioPraticaCommand, Command<Void, AllegatoDTO> downloadAllegatoCommand,
			Command<Void, AllegatoDTO> mostraDettaglioAllegatoCommand) {
		this.downloadAllegatoCommand = downloadAllegatoCommand;
		this.mostraDettaglioPraticaCommand = mostraDettaglioPraticaCommand;
		this.mostraDettaglioAllegatoCommand = mostraDettaglioAllegatoCommand;
	}

	@Override
	public void disegnaOperazioni(HTMLPanel operations, final PraticaDTO pratica, final EspandiRigaEventListener listener) {
		/* pulsante apertura dettaglio */
		if (mostraDettaglioPraticaCommand != null) {

			Button chiudiButton = new Button();
			chiudiButton.setText("Chiudi");
			chiudiButton.setStyleName("btn black");
			chiudiButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					listener.onEspandiRiga(pratica.getClientID(), pratica.getTipologiaPratica(), true);
				}
			});
			operations.add(chiudiButton);

		}
	}

	@Override
	protected Command<Void, AllegatoDTO> getMostraDettaglioAllegatoCommand() {
		return this.mostraDettaglioAllegatoCommand;
	}

}
