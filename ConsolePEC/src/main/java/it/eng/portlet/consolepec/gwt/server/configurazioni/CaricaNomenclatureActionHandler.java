package it.eng.portlet.consolepec.gwt.server.configurazioni;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.drive.Nomenclatura;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaNomenclatureAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaNomenclatureResult;
import it.eng.portlet.consolepec.spring.bean.drive.DriveUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Giacomo F.M.
 * @since 2019-07-08
 */
@Slf4j
public class CaricaNomenclatureActionHandler implements ActionHandler<CaricaNomenclatureAction, CaricaNomenclatureResult> {

	@Autowired
	private DriveUtil driveUtil;

	@Override
	public CaricaNomenclatureResult execute(CaricaNomenclatureAction action, ExecutionContext context) throws ActionException {
		log.info("Inizio CaricaNomenclatureActionHandler");
		CaricaNomenclatureResult result = null;
		try {
			List<Nomenclatura> nomenclature = driveUtil.getNomenclature();
			result = new CaricaNomenclatureResult(nomenclature);
		} catch (Exception e) {
			log.error("Errore durante il recupero delle nomenclature", e);
			result = new CaricaNomenclatureResult(ConsolePecConstants.ERROR_MESSAGE);
		}
		log.info("Fine CaricaNomenclatureActionHandler");
		return result;
	}

	@Override
	public Class<CaricaNomenclatureAction> getActionType() {
		return CaricaNomenclatureAction.class;
	}

	@Override
	public void undo(CaricaNomenclatureAction action, CaricaNomenclatureResult result, ExecutionContext context) throws ActionException {
		// ~
	}

}
