package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.GestioneProcedimentiDaDettaglioFascicoloEvent;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollatoCapofila;
import it.eng.portlet.consolepec.gwt.shared.model.ProcedimentoDto;
import it.eng.portlet.consolepec.gwt.shared.procedimenti.OperazioniProcedimento;

public class GoToChiudiProcedimentoCommand extends GoToAvviaProcedimentoCommand {

	public GoToChiudiProcedimentoCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}
	
	@Override
	protected GestioneProcedimentiDaDettaglioFascicoloEvent getEvent() {
		if( getPresenter().getView().getProcedimentiSelezionati().size() == 1){
			ProcedimentoDto dto = getPresenter().getView().getProcedimentiSelezionati().iterator().next();
			ElementoGruppoProtocollatoCapofila capofila = null;
			for (ElementoElenco elementoElenco : getPresenter().getView().getFascicolo().getElenco()) {
				if (elementoElenco instanceof ElementoGruppoProtocollatoCapofila) {
					ElementoGruppoProtocollatoCapofila capo = (ElementoGruppoProtocollatoCapofila) elementoElenco;
					if( dto.getAnnoPG().toString().equals(capo.getAnnoPG()) && dto.getNumeroPG().equals(capo.getNumeroPG()) )
						capofila = capo; 
				}
			}		
			GestioneProcedimentiDaDettaglioFascicoloEvent event = (capofila == null) ? null : new GestioneProcedimentiDaDettaglioFascicoloEvent(OperazioniProcedimento.CHIUSURA, capofila.getNumeroPG(), capofila.getAnnoPG(), capofila.getNumeroPG(), capofila.getAnnoPG(), capofila.getTipologiaDocumento(), capofila.getIdTitolo(), capofila.getIdRubrica(), capofila.getIdSezione(), getPresenter().getFascicoloPath());
			event.setDataInizioDecorrenzaProcedimento(dto.getDataInizioDecorrenzaProcedimento());
			event.setCodTipologiaProcedimento(dto.getCodTipologiaProcedimento());
			return event;
		} else {
			ElementoGruppoProtocollato nonCapofila = getPresenter().getView().getNonCapofilaSelezionato();
			return new GestioneProcedimentiDaDettaglioFascicoloEvent(OperazioniProcedimento.CHIUSURA, nonCapofila.getNumeroPG(), nonCapofila.getAnnoPG(), nonCapofila.getNumeroPGCapofila(), nonCapofila.getAnnoPGCapofila(), nonCapofila.getTipologiaDocumento(), nonCapofila.getIdTitolo(), nonCapofila.getIdRubrica(), nonCapofila.getIdSezione(), getPresenter().getFascicoloPath());
		}
	}	

}
