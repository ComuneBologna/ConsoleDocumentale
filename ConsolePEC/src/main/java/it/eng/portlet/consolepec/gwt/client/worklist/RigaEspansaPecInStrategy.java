package it.eng.portlet.consolepec.gwt.client.worklist;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;

import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.EspandiRigaEventListener;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

public class RigaEspansaPecInStrategy extends AbstractRigaEspansaPecStrategy {
	private Command<Void, String> mostraDettaglioPraticaCommand;
	private Command<Void, PecInDTO> mostraCreaFascicoloCommand;
	private Command<Void, String> eliminaPraticaCommand;
	private Command<Void, String> archiviaPraticaCommand;

	private Command<Void, AllegatoDTO> mostraDettaglioAllegatoCommand;

	public RigaEspansaPecInStrategy(Command<Void, String> mostraDettaglioPraticaCommand, Command<Void, String> archiviaPraticaCommand, Command<Void, String> eliminaPraticaCommand,
			Command<Void, PecInDTO> mostraCreaFascicoloCommand, Command<Void, AllegatoDTO> downloadAllegatoCommand, Command<Void, AllegatoDTO> mostraDettaglioAllegatoCommand) {
		this.mostraDettaglioPraticaCommand = mostraDettaglioPraticaCommand;
		this.archiviaPraticaCommand = archiviaPraticaCommand;
		this.eliminaPraticaCommand = eliminaPraticaCommand;
		this.mostraCreaFascicoloCommand = mostraCreaFascicoloCommand;
		this.downloadAllegatoCommand = downloadAllegatoCommand;
		this.mostraDettaglioAllegatoCommand = mostraDettaglioAllegatoCommand;
	}

	@Override
	public void disegnaOperazioni(HTMLPanel operations, final PraticaDTO pratica, final EspandiRigaEventListener listener) {
		final PecInDTO pec = (PecInDTO) pratica;
		final String clientID = pratica.getClientID();
		/* pulsante apertura dettaglio */
		if (mostraDettaglioPraticaCommand != null) {
			Button apriButton = new Button();
			apriButton.setText("Apri");
			apriButton.addStyleName("btn");
			apriButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {
					mostraDettaglioPraticaCommand.exe(clientID);
				}
			});
			// addButtonToOperations(apriButton, operations);
		}
		if (archiviaPraticaCommand != null) {
			/* pulsante di archiviazione */
			Button archiviaButton = new Button();
			archiviaButton.setText("Archivia");
			archiviaButton.addStyleName("btn");
			archiviaButton.setEnabled(pec.isArchiviabile());
			archiviaButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {
					archiviaPraticaCommand.exe(clientID);
				}
			});
			addButtonToOperations(archiviaButton, operations);
		}
		if (eliminaPraticaCommand != null) {
			/* pulsante di eliminazione */
			Button eliminaButton = new Button();
			eliminaButton.setText("Elimina");
			eliminaButton.addStyleName("btn");
			eliminaButton.setEnabled(pec.isEliminabile());
			eliminaButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {
					eliminaPraticaCommand.exe(clientID);
				}
			});
			addButtonToOperations(eliminaButton, operations);
		}
		if (mostraCreaFascicoloCommand != null) {
			/* pulsante di creazione fascicolo */
			Button creaFascicoloButton = new Button();
			creaFascicoloButton.setText("Crea fascicolo");
			creaFascicoloButton.addStyleName("btn");
			creaFascicoloButton.setEnabled(pec.isCreaFascicoloAbilitato());
			creaFascicoloButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {
					mostraCreaFascicoloCommand.exe(pec);
				}
			});
			addButtonToOperations(creaFascicoloButton, operations);
		}
		/* pulsante di chiusura del dettaglio */
		Button chiudiButton = new Button();
		chiudiButton.setText("Chiudi");
		chiudiButton.setStyleName("btn black");
		chiudiButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				listener.onEspandiRiga(pratica.getClientID(), pratica.getTipologiaPratica(), true);
			}
		});
		addButtonToOperations(chiudiButton, operations);
	}

	@Override
	protected Command<Void, AllegatoDTO> getMostraDettaglioAllegatoCommand() {
		return mostraDettaglioAllegatoCommand;
	}

}
