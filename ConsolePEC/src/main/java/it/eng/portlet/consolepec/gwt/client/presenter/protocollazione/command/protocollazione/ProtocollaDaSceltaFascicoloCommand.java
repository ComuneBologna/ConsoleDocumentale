package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.protocollazione;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmaiInlLoaded;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaModulisticaLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.SceltaCapofilaPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.mostraform.MostraFormProtocollazioneSceltaFascicoloEvent;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.DatiDefaultProtocollazione;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;

import com.google.common.base.Strings;

public class ProtocollaDaSceltaFascicoloCommand extends AbstractProtocollazioneCommand {

	public ProtocollaDaSceltaFascicoloCommand(SceltaCapofilaPresenter presenter, ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(presenter, configurazioniHandler, profilazioneUtenteHandler);
	}

	private String idEmailIn;
	private String idPraticaModulistica;

	public String getIdEmailIn() {
		return idEmailIn;
	}

	public void setIdEmailIn(String idEmailIn) {
		this.idEmailIn = idEmailIn;
	}

	public String getIdPraticaModulistica() {
		return idPraticaModulistica;
	}

	public void setIdPraticaModulistica(String idPraticaModulistica) {
		this.idPraticaModulistica = idPraticaModulistica;
	}

	@Override
	protected void _execute(final FascicoloDTO fascicoloDTO) {

		if (!Strings.isNullOrEmpty(idEmailIn)) {
			
			getPresenter().getPecInPraticheDB().getPecInByPath(idEmailIn, true, new PraticaEmaiInlLoaded() {
				
				@Override
				public void onPraticaLoaded(PecInDTO pec) {
					execute(fascicoloDTO, pec);
				}
				
				@Override
				public void onPraticaError(String error) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getPresenter()._getEventBus().fireEvent(event);
				}
			});
			
		} else if (!Strings.isNullOrEmpty(idPraticaModulistica)){
			
			getPresenter().getPecInPraticheDB().getPraticaModulisticaByPath(idPraticaModulistica, true, new PraticaModulisticaLoaded() {
				
				@Override
				public void onPraticaModulisticaLoaded(PraticaModulisticaDTO pratica) {
					execute(fascicoloDTO, pratica);
				}
				
				@Override
				public void onPraticaModulisticaError(String error) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getPresenter()._getEventBus().fireEvent(event);
				}
			});
			
		} else {
			ShowMessageEvent event = new ShowMessageEvent();
			event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
			getPresenter()._getEventBus().fireEvent(event);
		}
	}

	private void execute(FascicoloDTO fascicoloDTO, PraticaDTO pratica) {
		
		AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicolo(fascicoloDTO.getTipologiaPratica().getNomeTipologia());
		MostraFormProtocollazioneSceltaFascicoloEvent event = new MostraFormProtocollazioneSceltaFascicoloEvent();
		DatiDefaultProtocollazione datiPerFormProtocollazone = new DatiDefaultProtocollazione();
		datiPerFormProtocollazone.setProtocollazioneRiservata(af.isProtocollazioneRiservata());
		datiPerFormProtocollazone.setDatiPg(getDatiPg());
		event.setDatiPerFormProtocollazione(datiPerFormProtocollazone);
		event.setPraticaDaProtocollare(pratica);
		event.setFascicoloDTO(fascicoloDTO);
		getPresenter()._getEventBus().fireEvent(event);
	}
}
