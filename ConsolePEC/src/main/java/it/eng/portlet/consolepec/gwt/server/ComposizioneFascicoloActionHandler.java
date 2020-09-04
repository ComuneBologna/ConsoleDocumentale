package it.eng.portlet.consolepec.gwt.server;

import it.eng.modulistica.spagic.servizi.ComposizioneFascicoloService;
import it.eng.modulistica.spagic.servizi.ComposizioneFascicoloService.AppcException;
import it.eng.modulistica.spagic.servizi.ComposizioneFascicoloService.CatenaDocumentale;
import it.eng.modulistica.spagic.servizi.ComposizioneFascicoloService.PG;
import it.eng.portlet.consolepec.gwt.shared.action.ComposizioneFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.ComposizioneFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.model.CatenaDocumentaleDTO;
import it.eng.portlet.consolepec.gwt.shared.model.CatenaDocumentaleDTO.PgDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class ComposizioneFascicoloActionHandler implements ActionHandler<ComposizioneFascicolo, ComposizioneFascicoloResult> {
	
	private Logger logger = LoggerFactory.getLogger(ComposizioneFascicoloActionHandler.class);
	
	@Autowired
	ComposizioneFascicoloService composizionefascicoloService;

	public ComposizioneFascicoloActionHandler() {
	}

	@Override
	public ComposizioneFascicoloResult execute(ComposizioneFascicolo action, ExecutionContext context) throws ActionException {
		
		logger.info("Richiesta composizione fascicolo per il PG {}/{}", action.getNumPG(),action.getAnnoPG());
		
		try {
			
			CatenaDocumentale c = composizionefascicoloService.getComposizioneFascicolo(action.getNumPG(), action.getAnnoPG());
			CatenaDocumentaleDTO dto = new CatenaDocumentaleDTO();
			boolean hasCapofila = false;
			for(PG pg : c.getProtocollazioni()){
				PgDTO pgdto = converti(pg);
				dto.getProtocollazioni().add(pgdto);
				if(pgdto.isCapofila())
					hasCapofila = true;
			}
			
			if(hasCapofila){			
				return new ComposizioneFascicoloResult(dto);
			} else {
				logger.error("Manca il capofila nella composizione fascicolo del PG {}/{}", action.getNumPG(),action.getAnnoPG());
				return new ComposizioneFascicoloResult("Impossibile recuperare la catena documentale completa", true);
			}
			
		} catch (AppcException e) {
			logger.error("Errore da APPC", e);
			return new ComposizioneFascicoloResult(e.getLocalizedMessage(), true);
		} catch (Exception e) {
			logger.error("Errore generico", e);
			return new ComposizioneFascicoloResult("Errore invocazione del servizio di visualizzazione della catena documentale", true);
		}
	}
	
	private PgDTO converti(PG pg){
		
		PgDTO dto = new PgDTO();
		dto.setAnnoFascicolo(pg.getAnnoFascicolo());
		dto.setAnnoPG(pg.getAnnoPG());
		dto.setAnnoPGCapofila(pg.getAnnoPGCapofila());
		dto.setCodCellaAssegnazione(pg.getCodCellaAssegnazione());
		dto.setCodCellaProtocollazione(pg.getCodCellaProtocollazione());
		dto.setIdRubrica(pg.getCodRubrica());
		dto.setIdSezione(pg.getCodSezione());
		dto.setTipologiaDocumento(pg.getCodTipologiaDoc());
		dto.setIdTitolo(pg.getCodTitolo());
		dto.setDataAnnullamentoDoc(pg.getDataAnnullamentoDoc());
		dto.setDataAnnullamentoPratica(pg.getDataAnnullamentoPratica());
		dto.setDataProtocollazione(pg.getDataPG());
		dto.setDescrCellaAssegnazione(pg.getDescrCellaAssegnazione());
		dto.setDescrCellaProtocollazione(pg.getDescrCellaProtocollazione());
		dto.setDescrTipologiaDoc(pg.getDescrTipologiaDoc());
		dto.setNumFascicolo(pg.getNumFascicolo());
		dto.setNumeroPG(pg.getNumPG());
		dto.setNumeroPGCapofila(pg.getNumPGCapofila());
		dto.setOggetto(pg.getOggettoProvenienza());
		dto.setProvenienza(pg.getProvenienza());
		return dto;
	}

	@Override
	public void undo(ComposizioneFascicolo action, ComposizioneFascicoloResult result, ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<ComposizioneFascicolo> getActionType() {
		return ComposizioneFascicolo.class;
	}
}
