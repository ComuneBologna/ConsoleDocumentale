package it.eng.portlet.consolepec.gwt.servermock;

import java.util.HashSet;
import java.util.Set;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CambiaStatoPecInAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CambiaStatoPecInAction.Azione;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CambiaStatoPecInActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;

public class ArchiviaEliminaActionMockHandler implements ActionHandler<CambiaStatoPecInAction, CambiaStatoPecInActionResult> {

	public ArchiviaEliminaActionMockHandler() {}

	@Override
	public CambiaStatoPecInActionResult execute(CambiaStatoPecInAction action, ExecutionContext context) throws ActionException {
		Azione tipoAzione = action.getTipoAzione();

		CambiaStatoPecInActionResult res = null;
		// impostare error true e mess error in caso di errore nella risposta
		switch (tipoAzione) {
		case ARCHIVIA:
			res = archivia(action.getId());
			break;
		case ARCHIVIAMASSIVA:
			res = archivia(action.getIds());
			break;
		case ELIMINA:
			res = elimina(action.getId());
			break;
		case ELIMINAMASSIVO:
			res = elimina(action.getIds());
			break;
		case RIPORTAINGESTIONEMASSIVO:
			break;
		case RIPORTAINGESTIONE:
			break;
		case RILASCIA_IN_CARICO:
			break;
		default:
			break;
		}
		return res;
	}

	private CambiaStatoPecInActionResult elimina(Set<String> ids) {
		return generaResult(ids, false);
	}

	private CambiaStatoPecInActionResult elimina(String id) {
		Set<String> ids = new HashSet<String>();
		ids.add(id);
		return generaResult(ids, false);
	}

	private CambiaStatoPecInActionResult archivia(Set<String> ids) {
		return generaResult(ids, true);
	}

	private CambiaStatoPecInActionResult archivia(String id) {
		Set<String> ids = new HashSet<String>();
		ids.add(id);
		return generaResult(ids, true);
	}

	private CambiaStatoPecInActionResult generaResult(Set<String> ids, boolean archivia) {
		CambiaStatoPecInActionResult res = new CambiaStatoPecInActionResult();
		res.setIsError(false);
		for (String id : ids) {
			PecInDTO dd = PecInDB.getInstance().getDettaglio(id);
			dd.setArchiviabile(false);
			dd.setEliminabile(archivia);
			Stato stato = (archivia ? Stato.ARCHIVIATA : Stato.ELIMINATA);
			// dd.setStatoLabel(stato.getLabel());
			dd.setStato(it.eng.portlet.consolepec.gwt.shared.model.PecInDTO.StatoDTO.valueOf(stato.name()));
			res.getDettagliRighe().add(PecInDB.getInstance().getDettaglio(id));
		}

		return res;
	}

	@Override
	public void undo(CambiaStatoPecInAction action, CambiaStatoPecInActionResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<CambiaStatoPecInAction> getActionType() {
		return CambiaStatoPecInAction.class;
	}
}
