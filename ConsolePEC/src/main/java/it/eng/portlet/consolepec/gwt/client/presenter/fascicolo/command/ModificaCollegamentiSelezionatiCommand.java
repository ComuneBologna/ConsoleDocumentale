package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.collegamento.MostraCollegaFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.event.collegamento.MostraCollegaFascicoloEvent.TipoMostraCollegaFascicolo;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.shared.dto.CollegamentoDto;

import java.util.Set;

public class ModificaCollegamentiSelezionatiCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {
	public ModificaCollegamentiSelezionatiCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}
	
	@Override
	public void _execute() {
	Set<CollegamentoDto> collegamentiSelezionati = getPresenter().getView().getCollegamentiSelezionati();
	String clientId = collegamentiSelezionati.iterator().next().getClientId();
	getPresenter()._getEventBus().fireEvent(new MostraCollegaFascicoloEvent(getPresenter().getFascicoloPath(), clientId, TipoMostraCollegaFascicolo.DIRETTO));
}

}
