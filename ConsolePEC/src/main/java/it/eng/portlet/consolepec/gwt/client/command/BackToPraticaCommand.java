package it.eng.portlet.consolepec.gwt.client.command;

import it.eng.portlet.consolepec.gwt.client.operazioni.BackToPratica;

import com.google.gwt.user.client.Command;

public class BackToPraticaCommand implements Command {
	
	private BackToPratica backToPratica;
	
	public BackToPraticaCommand(BackToPratica backToPratica){
		this.backToPratica = backToPratica;
	}

	@Override
	public void execute() {
		backToPratica.goBackToPratica();
	}

}
