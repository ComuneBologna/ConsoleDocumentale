package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.protocollazione;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmaiInlLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.SceltaCapofilaPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.mostraform.MostraFormProtocollazionePecInEvent;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.DatiDefaultProtocollazione;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;

public class ProtocollaPecInCommand extends AbstractProtocollazioneNuovoFascicoloCommand {

	private ProfilazioneUtenteHandler profilazioneUtenteHandler;
	
	public ProtocollaPecInCommand(SceltaCapofilaPresenter presenter, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(presenter);
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
	}

	@Override
	protected void _execute() {
		getPresenter().getPecInPraticheDB().getPecInByPath(getCreaFascicoloDTO().getClientID(), false, new PraticaEmaiInlLoaded() {

			@Override
			public void onPraticaLoaded(PecInDTO pec) {
			
				DatiDefaultProtocollazione datiDefaultProtocollazione = new DatiDefaultProtocollazione();
				datiDefaultProtocollazione.setProtocollazioneRiservata(getCreaFascicoloDTO().isProtocollazioneRiservata());
				datiDefaultProtocollazione.setNote(profilazioneUtenteHandler.getDatiUtente().getNomeCompleto());
				datiDefaultProtocollazione.setDatiPg(getDatiPg());
				datiDefaultProtocollazione.setTitolazione(getCreaFascicoloDTO().getTitolazione());
				datiDefaultProtocollazione.setOggettoProtocollazione(getCreaFascicoloDTO().getTitolo());

				MostraFormProtocollazionePecInEvent event = new MostraFormProtocollazionePecInEvent();
				event.setDatiDefaultProtocollazione(datiDefaultProtocollazione);
				event.setCreaFascicoloDTO(getCreaFascicoloDTO());
				event.setPecInDTO(pec);

				getPresenter()._getEventBus().fireEvent(event);
			}

			@Override
			public void onPraticaError(String error) {
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getPresenter()._getEventBus().fireEvent(event);
			}
		});
	}

}
