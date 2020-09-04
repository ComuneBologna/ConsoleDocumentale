package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import java.util.ArrayList;
import java.util.List;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
 * @author biagiot
 *
 */
public interface TagliaAllegatiTaskApi extends ITaskApi {

	public TagliaAllegatiOutput tagliaAllegati(List<Allegato> allegati, Pratica<?> praticaDestinataria) throws ApplicationException, InvalidArgumentException;

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class TagliaAllegatiOutput {
		private List<Allegato> allegati = new ArrayList<Allegato>();

		public TagliaAllegatiOutput(List<Allegato> allegati) {
			// il cloning è necessario per evitare che gli oggetti puntino alla pratica sorgente
			for (Allegato allegato : allegati)
				this.allegati.add(allegato.clona());
		}
	}
}
