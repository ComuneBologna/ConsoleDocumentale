package it.eng.portlet.consolepec.gwt.servermock.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CancellaAllegatoFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CancellaAllegatoFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoAllegato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoComunicazioneRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppo;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollatoCapofila;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGrupppoNonProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPECRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPraticaModulisticaRiferimento;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
public class CancellaAllegatoFascicoloMockActionHandler implements ActionHandler<CancellaAllegatoFascicolo, CancellaAllegatoFascicoloResult> {


	public CancellaAllegatoFascicoloMockActionHandler() {
	}

	@Override
	public CancellaAllegatoFascicoloResult execute(CancellaAllegatoFascicolo action, ExecutionContext context) throws ActionException {
		String clientID= action.getClientID();
		FascicoloDTO dto = FascicoloDB.getInstance().getDettaglio(clientID);
		CancellaAllegatoFascicoloResult result = null;
		/*
		 * L'allegato da cancellare può essere solo uno tra quelli dei non protocollati
		 */
		FascicoloVisitor fv = new FascicoloVisitor(dto);
		
		if(fv.getNonProt().getElementi().containsAll(action.getAllegati())){
			fv.getNonProt().getElementi().removeAll(action.getAllegati());
			//rimuovo da elenco generico
			dto.getAllegati().removeAll(action.getAllegati());
			result = new CancellaAllegatoFascicoloResult(dto);
			result.setError(false);
		}
		else{
			//erro
			result = new CancellaAllegatoFascicoloResult(null);
			result.setError(true);
			result.setErrorMsg("Elenco allegati non coerente");
		}
		return result;
	}

	@Override
	public void undo(CancellaAllegatoFascicolo action, CancellaAllegatoFascicoloResult result, ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<CancellaAllegatoFascicolo> getActionType() {
		return CancellaAllegatoFascicolo.class;
	}

	/**
	 * Naviga il fascicolo, alla ricerca del gruppo non protocollato e di tutti
	 * gli allegati
	 * 
	 * @author pluttero
	 * 
	 */
	private class FascicoloVisitor implements FascicoloDTO.ElementoElencoVisitor {

		private ElementoGrupppoNonProtocollato nonProt;

		public FascicoloVisitor(FascicoloDTO dto) {
			for (ElementoElenco elem : dto.getElenco())
				elem.accept(this);
		}

		public ElementoGrupppoNonProtocollato getNonProt() {
			return nonProt;
		}

		@Override
		public void visit(ElementoGrupppoNonProtocollato gruppo) {
			nonProt = gruppo;
		}

		@Override
		public void visit(ElementoPECRiferimento pec) {
			// nop
		}

		@Override
		public void visit(ElementoGruppo gruppo) {
			// nop
		}

		@Override
		public void visit(ElementoGruppoProtocollatoCapofila capofila) {
			// nop
		}

		@Override
		public void visit(ElementoGruppoProtocollato subProt) {
			// nop
		}

		@Override
		public void visit(ElementoAllegato allegato) {
			// nop
		}

		@Override
		public void visit(ElementoPraticaModulisticaRiferimento elementoPraticaModulisticaRiferimento) {
			// nop
		}

		@Override
		public void visit(ElementoComunicazioneRiferimento elementoComunicazioneRiferimento) {
			// nop
		}

	}
}
