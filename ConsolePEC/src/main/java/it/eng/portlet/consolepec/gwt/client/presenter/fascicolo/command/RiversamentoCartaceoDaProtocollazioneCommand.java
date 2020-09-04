package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.EsitoProtocollazionePresenter;
import it.eng.portlet.consolepec.gwt.shared.action.RiversamentoCartaceo;

public class RiversamentoCartaceoDaProtocollazioneCommand extends AbstractRiversamentoCartaceoCommand<EsitoProtocollazionePresenter> {

	public RiversamentoCartaceoDaProtocollazioneCommand(EsitoProtocollazionePresenter presenter) {
		super(presenter);
	}

	@Override
	public RiversamentoCartaceo getRiversamentoCartaceoAction() {
		return new RiversamentoCartaceo(getPresenter().getFascicoloPath(), getPresenter().getAnnoPG(), getPresenter().getNumeroPG());
	}

}
