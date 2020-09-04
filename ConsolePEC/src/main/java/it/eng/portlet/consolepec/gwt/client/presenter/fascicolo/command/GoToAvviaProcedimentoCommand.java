package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.GestioneProcedimentiDaDettaglioFascicoloEvent;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollatoCapofila;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPECRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPraticaModulisticaRiferimento;
import it.eng.portlet.consolepec.gwt.shared.procedimenti.OperazioniProcedimento;

import java.util.Set;

public class GoToAvviaProcedimentoCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {

	public GoToAvviaProcedimentoCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		GestioneProcedimentiDaDettaglioFascicoloEvent gestioneProcedimentiDaDettaglioFascicoloEvent = getEvent();
		if(gestioneProcedimentiDaDettaglioFascicoloEvent == null)
			throw new IllegalStateException();
		
		// è possibile selezionare solo una pratica protocollata
		Set<ElementoElenco> praticheSelezionate = getPresenter().getView().getPraticheProtSelezionate();
		if (praticheSelezionate.size() > 1)
			throw new IllegalStateException();

		if (!praticheSelezionate.isEmpty()) {
			ElementoElenco elemento = praticheSelezionate.iterator().next();
			if (elemento instanceof ElementoPECRiferimento)
				gestioneProcedimentiDaDettaglioFascicoloEvent.setIdPraticaProtocollataSelezionata(((ElementoPECRiferimento) elemento).getRiferimento());
			if (elemento instanceof ElementoPraticaModulisticaRiferimento)
				gestioneProcedimentiDaDettaglioFascicoloEvent.setIdPraticaProtocollataSelezionata(((ElementoPraticaModulisticaRiferimento) elemento).getRiferimento());
		}

		getPresenter()._getEventBus().fireEvent(gestioneProcedimentiDaDettaglioFascicoloEvent);
	}
	
	protected GestioneProcedimentiDaDettaglioFascicoloEvent getEvent() {
		ElementoGruppoProtocollatoCapofila capofila = getPresenter().getView().getCapofilaSelezionato();
		return new GestioneProcedimentiDaDettaglioFascicoloEvent(OperazioniProcedimento.AVVIO, capofila.getNumeroPG(), capofila.getAnnoPG(), capofila.getNumeroPG(), capofila.getAnnoPG(), capofila.getTipologiaDocumento(),
				capofila.getIdTitolo(), capofila.getIdRubrica(), capofila.getIdSezione(), getPresenter().getFascicoloPath());
	}

}
