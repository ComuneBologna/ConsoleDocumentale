package it.eng.consolepec.xmlplugin.factory;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;

public interface TaskObserver {

	public interface AggiungiAllegato {
		public Allegato aggiungiAllegato(String alfrescoPathAllegato, String nomeAllegato) throws ApplicationException, InvalidArgumentException;
	}

	public interface IncollaAllegatoHandler {
		public Allegato incollaAllegato(String nomeAllegato, Allegato allegatoOriginale) throws ApplicationException, InvalidArgumentException;
	}
}
