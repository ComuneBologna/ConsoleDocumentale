package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaEvent.SelectedObject;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaEvent.SelectedObject.ObjectType;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaProtocollazioneFascicoloEvent;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPECRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPraticaModulisticaRiferimento;

import java.util.HashSet;
import java.util.Set;

public class ProtocollaCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {
	public ProtocollaCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}
	
	@Override
	public void _execute() {
		Set<AllegatoDTO> allegati = getPresenter().getView().getAllegatiSelezionati();
		Set<ElementoElenco> praticheNonSelezionate = getPresenter().getView().getPraticheNonProtSelezionate();

		Set<SelectedObject> praticheSelezionate = new HashSet<SelectedObject>();

		for (ElementoElenco pratica : praticheNonSelezionate) {
			if (pratica instanceof ElementoPECRiferimento) {
				ElementoPECRiferimento e = (ElementoPECRiferimento) pratica;
				praticheSelezionate.add(new SelectedObject(e.getRiferimento(), SelectedObject.getObjectType(e.getTipo())));
			}
			if (pratica instanceof ElementoPraticaModulisticaRiferimento) {
				ElementoPraticaModulisticaRiferimento e = (ElementoPraticaModulisticaRiferimento) pratica;
				praticheSelezionate.add(new SelectedObject(e.getRiferimento(), ObjectType.PRATICA_MODULISTICA));
			}
		}

		MostraSceltaCapofilaProtocollazioneFascicoloEvent event = new MostraSceltaCapofilaProtocollazioneFascicoloEvent();
		event.setIdFascicolo(getPresenter().getFascicoloPath());
		event.setListEmail(praticheSelezionate);
		event.setAllegati(allegati);
		getPresenter()._getEventBus().fireEvent(event);
	}

}
