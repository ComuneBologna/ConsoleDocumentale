package it.eng.portlet.consolepec.gwt.servermock.fascicolo;

import java.util.List;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.UploadAllegatoFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.UploadAllegatoFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoAllegato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGrupppoNonProtocollato;

public class UploadAllegatoFascicoloMockActionHandler implements ActionHandler<UploadAllegatoFascicolo, UploadAllegatoFascicoloResult> {

	public UploadAllegatoFascicoloMockActionHandler() {}

	@Override
	public UploadAllegatoFascicoloResult execute(UploadAllegatoFascicolo action, ExecutionContext context) throws ActionException {

		FascicoloDTO fascicolo = FascicoloDB.getInstance().getDettaglio(action.getClientID());

		List<String> fnames = action.getFileNames();
		for (String fname : fnames) {
			ElementoAllegato allegato = new ElementoAllegato(fname, null, null, fascicolo.getClientID(), "0.1");
			// aggiungo alla lista generica
			fascicolo.getAllegati().add(allegato);
			// aggiungo al gruppo non prot
			for (ElementoElenco elem : fascicolo.getElenco()) {
				if (elem instanceof ElementoGrupppoNonProtocollato) {
					((ElementoGrupppoNonProtocollato) elem).addAllegato(allegato);
				}
			}
		}
		UploadAllegatoFascicoloResult result = new UploadAllegatoFascicoloResult(fascicolo, false, null);
		return result;
	}

	@Override
	public void undo(UploadAllegatoFascicolo action, UploadAllegatoFascicoloResult result, ExecutionContext context) throws ActionException {
		//
	}

	@Override
	public Class<UploadAllegatoFascicolo> getActionType() {
		return UploadAllegatoFascicolo.class;
	}
}
