package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoCatenaDocumentaleWidget;
import it.eng.portlet.consolepec.gwt.shared.action.RiversamentoCartaceo;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollatoCapofila;

public class RiversamentoCartaceoDaDettaglioFascicoloCommand extends AbstractRiversamentoCartaceoCommand<DettaglioFascicoloGenericoPresenter> {

	public RiversamentoCartaceoDaDettaglioFascicoloCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}

	@Override
	public RiversamentoCartaceo getRiversamentoCartaceoAction() {
		String fascicoloPath = getPresenter().getView().getFascicolo().getClientID();
		RiversamentoCartaceo action = null;
		
		ElementoCatenaDocumentaleWidget elementoCatenaDocumentale = getPresenter().getView().getElementoCatenaDocumentaleSelezionato();
		if(elementoCatenaDocumentale != null){
			action= new RiversamentoCartaceo(fascicoloPath, elementoCatenaDocumentale.getPg().getAnnoPG(), elementoCatenaDocumentale.getPg().getNumeroPG());
		} else {
			ElementoGruppoProtocollatoCapofila pgCapofila = getPresenter().getView().getCapofilaSelezionato();
			ElementoGruppoProtocollato pgCollegato = getPresenter().getView().getNonCapofilaSelezionato();
			if(pgCapofila != null && pgCollegato == null){
				action = new RiversamentoCartaceo(fascicoloPath, pgCapofila.getAnnoPG(), pgCapofila.getNumeroPG());
			} else if(pgCapofila == null && pgCollegato != null){
				action = new RiversamentoCartaceo(fascicoloPath, pgCollegato.getAnnoPG(), pgCollegato.getNumeroPG());
			} 
		}
		return action; 
	}

}
