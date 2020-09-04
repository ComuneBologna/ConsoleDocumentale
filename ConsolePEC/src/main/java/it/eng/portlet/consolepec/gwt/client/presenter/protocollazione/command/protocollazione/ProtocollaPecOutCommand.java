package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.protocollazione;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmailOutLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.SceltaCapofilaPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.mostraform.MostraFormProtocollazionePecOutEvent;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

public class ProtocollaPecOutCommand extends AbstractProtocollazioneCommand {

	public ProtocollaPecOutCommand(SceltaCapofilaPresenter presenter, ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(presenter, configurazioniHandler, profilazioneUtenteHandler);
	}

	private String idEmailOut;
	private boolean emailInteroperabile;

	public void setIdEmailOut(String idEmailOut) {
		this.idEmailOut = idEmailOut;
	}

	public void setEmailInteroperabile(boolean emailInteroperabile) {
		this.emailInteroperabile = emailInteroperabile;
	}

	@Override
	protected void _execute(FascicoloDTO fascicoloDTO) {
		final MostraFormProtocollazionePecOutEvent event = new MostraFormProtocollazionePecOutEvent();
		event.setFascicoloDTO(fascicoloDTO);
		event.setEmailInteroperabile(emailInteroperabile);
		event.setDatiDefaultProtocollazione(getDatiDefaultProtocollazione());
		
		getPresenter().getPecInPraticheDB().getPecOutByPath(idEmailOut, true, new PraticaEmailOutLoaded() {
			
			@Override
			public void onPraticaLoaded(PecOutDTO pec) {
				event.setPecOutDTO(pec);
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
