package it.eng.consolepec.xmlplugin.exception;

import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;

public class OperazioneNonConsentita extends PraticaException {

	private static final long serialVersionUID = -3041792474345393463L;

	public OperazioneNonConsentita(ITipoApiTask tipoApiTask) {
		super("Operazione: " + tipoApiTask.toString() + " non consentita.");
	}

}
