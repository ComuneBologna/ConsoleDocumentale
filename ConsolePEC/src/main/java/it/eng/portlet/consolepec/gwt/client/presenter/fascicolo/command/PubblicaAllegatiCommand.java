package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.event.MostraPubblicazioneAllegatiEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;

public class PubblicaAllegatiCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {
	public PubblicaAllegatiCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}
	
	@Override
	public void _execute() {
		// il controllo che getAllegatiSelezionati().size() ==1 è presente anche nella view per l'abilitazione del pulsante
		MostraPubblicazioneAllegatiEvent event = new MostraPubblicazioneAllegatiEvent(getPresenter().getFascicoloPath(), getPresenter().getView().getAllegatiSelezionati().iterator().next().getNome());
		getPresenter()._getEventBus().fireEvent(event);
	}

}
