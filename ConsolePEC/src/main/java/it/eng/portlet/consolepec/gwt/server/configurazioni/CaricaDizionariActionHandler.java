package it.eng.portlet.consolepec.gwt.server.configurazioni;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.drive.Dizionario;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaDizionariAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaDizionariResult;
import it.eng.portlet.consolepec.spring.bean.drive.DriveUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Giacomo F.M.
 * @since 2019-06-11
 */
@Slf4j
public class CaricaDizionariActionHandler implements ActionHandler<CaricaDizionariAction, CaricaDizionariResult> {

	@Autowired
	private DriveUtil driveUtil;

	@Override
	public CaricaDizionariResult execute(CaricaDizionariAction action, ExecutionContext context) throws ActionException {
		log.info("Inizio CaricaDizionariActionHandler");
		CaricaDizionariResult result = null;
		try {
			List<Dizionario> dizionari = driveUtil.getDizionari();
			result = new CaricaDizionariResult(dizionari);
		} catch (Exception e) {
			log.error("Errore durante il recupero dei dizionari", e);
			result = new CaricaDizionariResult(ConsolePecConstants.ERROR_MESSAGE);
		}
		log.info("Fine CaricaDizionariActionHandler");
		return result;
	}

	@Override
	public Class<CaricaDizionariAction> getActionType() {
		return CaricaDizionariAction.class;
	}

	@Override
	public void undo(CaricaDizionariAction action, CaricaDizionariResult result, ExecutionContext context) throws ActionException {
		// ~
	}

}
