package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.protocollazione;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaModulisticaLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.SceltaCapofilaPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.mostraform.MostraFormProtocollazionePraticaModulisticaEvent;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.DatiDefaultProtocollazione;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;

public class ProtocollaPraticaModulisticaCommand extends AbstractProtocollazioneNuovoFascicoloCommand {

	public ProtocollaPraticaModulisticaCommand(SceltaCapofilaPresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {

		getPresenter().getPecInPraticheDB().getPraticaModulisticaByPath(getCreaFascicoloDTO().getClientID(), false, new PraticaModulisticaLoaded() {

			@Override
			public void onPraticaModulisticaLoaded(PraticaModulisticaDTO pratica) {
				DatiDefaultProtocollazione datiDefaultProtocollazione = new DatiDefaultProtocollazione();
				datiDefaultProtocollazione.setProtocollazioneRiservata(getCreaFascicoloDTO().isProtocollazioneRiservata());
				datiDefaultProtocollazione.setDatiPg(getDatiPg());
				datiDefaultProtocollazione.setTitolazione(getCreaFascicoloDTO().getTitolazione());
				datiDefaultProtocollazione.setOggettoProtocollazione(getCreaFascicoloDTO().getTitolo());

				MostraFormProtocollazionePraticaModulisticaEvent event = new MostraFormProtocollazionePraticaModulisticaEvent();
				event.setCreaFascicoloDTO(getCreaFascicoloDTO());
				event.setDatiDefaultProtocollazione(datiDefaultProtocollazione);
				event.setPraticaModulisticaDTO(pratica);

				getPresenter()._getEventBus().fireEvent(event);
			}

			@Override
			public void onPraticaModulisticaError(String error) {
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getPresenter()._getEventBus().fireEvent(event);

			}
		});

	}

}
