package it.eng.portlet.consolepec.gwt.servermock.fascicolo;

import it.eng.portlet.consolepec.gwt.servermock.PecOutDB;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaRisposta;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaRispostaResult;
import it.eng.portlet.consolepec.gwt.shared.action.pec.TipoRiferimentoPEC;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppo;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPECRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class CreaRispostaMockActionHandler implements ActionHandler<CreaRisposta, CreaRispostaResult> {

	Logger logger = LoggerFactory.getLogger(CreaRispostaMockActionHandler.class);
	
	public CreaRispostaMockActionHandler() {
	}

	@Override
	public CreaRispostaResult execute(CreaRisposta action, ExecutionContext context) throws ActionException {

		logger.debug("Creazione bozza. pathFascicoloClient: {}", action.getPathFascicolo());
		
		FascicoloDTO fascicolo = FascicoloDB.getInstance().getDettaglio(action.getPathFascicolo());

		PecOutDTO bozza = PecOutDB.getInstance().creaDettaglio(fascicolo);
		
		for(ElementoElenco elemento :  fascicolo.getElenco()){
			if(elemento instanceof ElementoGruppo && !(elemento instanceof ElementoGruppoProtocollato)){
				ElementoPECRiferimento rif = new ElementoPECRiferimento(bozza.getClientID(), TipoRiferimentoPEC.OUT, new Date());
				((ElementoGruppo) elemento).addRiferimentoPEC(rif);
			}
		}
		bozza.setDataOraCreazione("01/01/2013");
		bozza.setTitolo("Bozza ");
		return new CreaRispostaResult(fascicolo, bozza, null, false);
	}

	@Override
	public void undo(CreaRisposta action, CreaRispostaResult result, ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<CreaRisposta> getActionType() {
		return CreaRisposta.class;
	}
}
