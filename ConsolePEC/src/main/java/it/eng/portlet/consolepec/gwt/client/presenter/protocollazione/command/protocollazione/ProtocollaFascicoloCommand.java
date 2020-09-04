package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.protocollazione;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.SceltaCapofilaPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.mostraform.MostraFormProtocollazioneFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaEvent.SelectedObject;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.ProtocollazioneUtils;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class ProtocollaFascicoloCommand extends AbstractProtocollazioneCommand {

	public ProtocollaFascicoloCommand(SceltaCapofilaPresenter presenter, ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(presenter, configurazioniHandler, profilazioneUtenteHandler);
	}

	private List<SelectedObject> listaPec = new ArrayList<SelectedObject>();
	private List<AllegatoDTO> listaAllegati = new ArrayList<AllegatoDTO>();

	public List<SelectedObject> getListaPec() {
		return listaPec;
	}

	public void setListaPec(List<SelectedObject> listaPec) {
		this.listaPec = listaPec;
	}

	public List<AllegatoDTO> getListaAllegati() {
		return listaAllegati;
	}

	public void setListaAllegati(List<AllegatoDTO> listaAllegati) {
		this.listaAllegati = listaAllegati;
	}

	@Override
	protected void _execute(FascicoloDTO fascicoloDTO) {
		final MostraFormProtocollazioneFascicoloEvent event = new MostraFormProtocollazioneFascicoloEvent();
		event.setFascicoloDTO(fascicoloDTO);

		TreeSet<AllegatoDTO> listaAllegatiDTO = new TreeSet<AllegatoDTO>();

		for (AllegatoDTO allegato : fascicoloDTO.getAllegati()) {
			if (listaAllegati.contains(allegato))
				listaAllegatiDTO.add(allegato);
		}

		event.setAllegatiDTO(listaAllegatiDTO);

		final TreeSet<PraticaDTO> pratiche = new TreeSet<PraticaDTO>();

		event.setDatiDefaultProtocollazione(getDatiDefaultProtocollazione());
		event.setTipoProtocollazione(getTipoProtocollazione());

		if (listaPec.size() != 0) {
			for (SelectedObject pratica : listaPec) {
				TipologiaPratica tipologiaPratica = SelectedObject.getTipologiaPratica(pratica.getType());
				getPresenter().getPecInPraticheDB().getPraticaByPathETipo(pratica.getIdPec(), tipologiaPratica, false, new PraticaLoaded() {

					@Override
					public void onPraticaLoaded(PraticaDTO pratica) {
						pratiche.add(pratica);
						if (pratiche.size() == listaPec.size()) {
							event.setPraticheDTO(pratiche);
							getPresenter()._getEventBus().fireEvent(event);
						}
					}

					@Override
					public void onPraticaError(String error) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getPresenter()._getEventBus().fireEvent(event);

					}
				});
			}
		} else if (listaPec.size() == 0)
			getPresenter()._getEventBus().fireEvent(event);

	}

	public String getTipoProtocollazione() {
		if (listaAllegati.size() != 0)
			return ProtocollazioneUtils.TIPOLOGIA_PROTOCOLLAZIONE_INTERNA;
		boolean peIn = false;
		boolean pecOut = false;
		boolean modulistica = false;
		for (SelectedObject obj : listaPec) {
			if (obj.getType().equals(SelectedObject.ObjectType.PEC_IN))
				peIn = true;
			else if (obj.getType().equals(SelectedObject.ObjectType.PRATICA_MODULISTICA))
				modulistica = true;
			else if (obj.getType().equals(SelectedObject.ObjectType.PEC_OUT))
				pecOut = true;
		}

		if ((peIn || modulistica) && pecOut)
			throw new IllegalStateException("Impossibile trovare il tipo di protocollazione");
		else if ((peIn || modulistica) && !pecOut)
			return ProtocollazioneUtils.TIPOLOGIA_PROTOCOLLAZIONE_ENTRATA;
		else if (!(peIn || modulistica) && pecOut)
			return ProtocollazioneUtils.TIPOLOGIA_PROTOCOLLAZIONE_USCITA;

		return null;
	}

}
