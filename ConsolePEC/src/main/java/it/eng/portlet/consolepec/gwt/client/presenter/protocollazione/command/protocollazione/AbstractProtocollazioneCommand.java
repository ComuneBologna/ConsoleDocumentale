package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.protocollazione;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.SceltaCapofilaPresenter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.dto.DatiPg;
import it.eng.portlet.consolepec.gwt.shared.model.DatiDefaultProtocollazione;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

public abstract class AbstractProtocollazioneCommand extends AbstractDatiProtocollazioneCommand {

	public AbstractProtocollazioneCommand(SceltaCapofilaPresenter presenter, ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(presenter);
		this.configurazioniHandler = configurazioniHandler;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
	}

	private String idFascicolo;
	private DatiPg datiPg;
	private FascicoloDTO fascicoloDTO;
	protected ConfigurazioniHandler configurazioniHandler;
	protected ProfilazioneUtenteHandler profilazioneUtenteHandler;
	
	public void setIdFascicolo(String idFascicolo) {
		this.idFascicolo = idFascicolo;
	}

	@Override
	public DatiPg getDatiPg() {
		return datiPg;
	}

	@Override
	public void setDatiPg(DatiPg datiPg) {
		this.datiPg = datiPg;
	}

	@Override
	protected void _execute() {
		if (idFascicolo != null) {
			getPresenter().getPecInPraticheDB().getFascicoloByPath(idFascicolo, false, new PraticaFascicoloLoaded() {
				@Override
				public void onPraticaLoaded(FascicoloDTO fascicoloDTO) {
					AbstractProtocollazioneCommand.this.fascicoloDTO = fascicoloDTO;
					_execute(fascicoloDTO);
				}

				@Override
				public void onPraticaError(String error) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getPresenter()._getEventBus().fireEvent(event);
				}
			});
		} else
			_execute(fascicoloDTO);
	}

	protected abstract void _execute(FascicoloDTO fascicoloDTO);

	protected DatiDefaultProtocollazione getDatiDefaultProtocollazione() {
		DatiDefaultProtocollazione datiDafaultProtocollazione = new DatiDefaultProtocollazione();
		AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicolo(fascicoloDTO.getTipologiaPratica().getNomeTipologia());
		datiDafaultProtocollazione.setProtocollazioneRiservata(af.isProtocollazioneRiservata());
		datiDafaultProtocollazione.setNote(profilazioneUtenteHandler.getDatiUtente().getNomeCompleto());
		datiDafaultProtocollazione.setDatiPg(getDatiPg());
		return datiDafaultProtocollazione;
	}
	
	protected ConfigurazioniHandler getConfigurazioniHandler() {
		return configurazioniHandler;
	}
	
}
