package it.eng.portlet.consolepec.gwt.client.util;

import java.util.Comparator;
import java.util.Date;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoAllegato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoComunicazioneRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollatoCapofila;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGrupppoNonProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPECRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPraticaModulisticaRiferimento;

public class ComposizioneFascicoloComparator implements Comparator<ElementoElenco> {

	@Override
	public int compare(ElementoElenco o1, ElementoElenco o2) {
		Date d1 = estraiData(o1);
		Date d2 = estraiData(o2);

		// nel caso di mancato recupero delle date(es: allegato non presente su alfresco) non interrompo la visualizzazione del fascicolo con un errore bloccante.
		if (d1 != null && d2 != null) {
			return d2.compareTo(d1);
		}

		return 0;

	}

	private static Date estraiData(ElementoElenco el) {
		if (el instanceof ElementoGruppoProtocollatoCapofila) {
			return ((ElementoGruppoProtocollatoCapofila) el).getDataProtocollazione();
		} else if (el instanceof ElementoGruppoProtocollato) {
			return ((ElementoGruppoProtocollato) el).getDataProtocollazione();
		} else if (el instanceof ElementoGrupppoNonProtocollato) {
			return new Date(Long.MIN_VALUE); // sempre all'inizio
		} else if (el instanceof ElementoAllegato) {
			return ((ElementoAllegato) el).getDataCaricamento();
		} else if (el instanceof ElementoPECRiferimento) {
			return ((ElementoPECRiferimento) el).getDataPec();
		} else if (el instanceof ElementoPraticaModulisticaRiferimento) {
			return ((ElementoPraticaModulisticaRiferimento) el).getDataCaricamento();
		} else if (el instanceof ElementoComunicazioneRiferimento) {
			return ((ElementoComunicazioneRiferimento) el).getDataCaricamento();
		} else {
			throw new IllegalArgumentException("Tipo elemento elenco non gestito: " + el.getClass().getSimpleName());
		}
	}

}
