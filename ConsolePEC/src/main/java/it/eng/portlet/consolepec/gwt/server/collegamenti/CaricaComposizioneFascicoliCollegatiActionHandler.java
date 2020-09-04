package it.eng.portlet.consolepec.gwt.server.collegamenti;

import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.action.pec.ComposizioneFascicoliCollegatiAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.ComposizioneFascicoliCollegatiActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPECRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPraticaModulisticaRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.PecDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

/**
 * 
 * @author biagiot
 *
 */
public class CaricaComposizioneFascicoliCollegatiActionHandler implements ActionHandler<ComposizioneFascicoliCollegatiAction, ComposizioneFascicoliCollegatiActionResult> {

	private Logger logger = LoggerFactory.getLogger(CaricaComposizioneFascicoliCollegatiActionHandler.class);
	
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	private XMLPluginToDTOConverter utilPratica;
	
	public CaricaComposizioneFascicoliCollegatiActionHandler() {

	}

	@Override
	public ComposizioneFascicoliCollegatiActionResult execute(ComposizioneFascicoliCollegatiAction action, ExecutionContext context) throws ActionException {
		
		ComposizioneFascicoliCollegatiActionResult result = new ComposizioneFascicoliCollegatiActionResult();
		
		List<PecDTO> pec = new ArrayList<PecDTO>();
		List<PraticaModulisticaDTO> praticheModulistica = new ArrayList<PraticaModulisticaDTO>();

		for (ElementoPECRiferimento pecRiferimento : action.getPecInComposizione()) {
			
			try {
				Pratica<?> praticaMail = praticaSessionUtil.loadPraticaFromEncodedPath(pecRiferimento.getRiferimento());
				PecDTO pecDTO = (PecDTO) utilPratica.praticaToDTO(praticaMail);
				pec.add(pecDTO);
				
			} catch (IOException e) {
				logger.error("Errore durante il caricamento della pec: " + pecRiferimento.getRiferimento(), e);
				result.addErrorPec();
			}	
		}

		for (ElementoPraticaModulisticaRiferimento praticaModulisticaRiferimento : action.getPraticheModulisticaInComposizione()) {
			
			try {
				Pratica<?> praticaModulistica = praticaSessionUtil.loadPraticaFromEncodedPath(praticaModulisticaRiferimento.getRiferimento());
				PraticaModulisticaDTO praticaModulisticaDTO = (PraticaModulisticaDTO) utilPratica.praticaToDTO(praticaModulistica);
				praticheModulistica.add(praticaModulisticaDTO);
				
			} catch (IOException e) {
				logger.error("Errore durante il caricamento della pratica modulistica: " + praticaModulisticaRiferimento.getRiferimento(), e);
				result.addErrorPraticheModulistica();
			}	
		}
		
		result.getPec().addAll(pec);
		result.getPraticheModulistica().addAll(praticheModulistica);
			
		return result;
	}

	@Override
	public Class<ComposizioneFascicoliCollegatiAction> getActionType() {
		return ComposizioneFascicoliCollegatiAction.class;
	}

	@Override
	public void undo(ComposizioneFascicoliCollegatiAction arg0, ComposizioneFascicoliCollegatiActionResult arg1, ExecutionContext arg2) throws ActionException {
		// TODO Auto-generated method stub

	}

}
