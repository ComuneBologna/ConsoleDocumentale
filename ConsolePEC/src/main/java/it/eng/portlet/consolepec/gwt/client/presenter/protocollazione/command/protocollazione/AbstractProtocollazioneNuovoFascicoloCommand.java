package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.command.protocollazione;

import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.SceltaCapofilaPresenter;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.dto.DatiPg;

public abstract class AbstractProtocollazioneNuovoFascicoloCommand extends AbstractDatiProtocollazioneCommand {

	public AbstractProtocollazioneNuovoFascicoloCommand(SceltaCapofilaPresenter presenter) {
		super(presenter);
	}

	private CreaFascicoloDTO creaFascicoloDTO;
	private DatiPg datiPg;

	public CreaFascicoloDTO getCreaFascicoloDTO() {
		return creaFascicoloDTO;
	}

	public void setCreaFascicoloDTO(CreaFascicoloDTO creaFascicoloDTO) {
		this.creaFascicoloDTO = creaFascicoloDTO;
	}

	@Override
	public DatiPg getDatiPg() {
		return datiPg;
	}

	@Override
	public void setDatiPg(DatiPg datiPg) {
		this.datiPg = datiPg;
	}
}
