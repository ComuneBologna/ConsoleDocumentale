package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaStatoFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaStatoFascicoloEnum;

import java.util.HashSet;
import java.util.Set;

public class EliminaFascicoloCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {
	public EliminaFascicoloCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}
	
	@Override
	public void _execute() {

		Set<String> listId = new HashSet<String>();
		listId.add(getPresenter().getFascicoloPath());
		getPresenter().getPecInPraticheDB().remove(getPresenter().getFascicoloPath()); // aggiunto per ricaricare il fascicolo lato server dopo l'importazione allegati dalle email
		CambiaStatoFascicoloEnum stato = CambiaStatoFascicoloEnum.ELIMINATO;
		getPresenter().setCambiaStatoFascicolo(new CambiaStatoFascicolo(listId, stato));
		getPresenter().eliminaAction();
	}


}
