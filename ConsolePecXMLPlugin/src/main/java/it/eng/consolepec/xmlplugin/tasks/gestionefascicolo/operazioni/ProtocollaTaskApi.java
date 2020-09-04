package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.TipoProtocollazione;
import it.eng.consolepec.xmlplugin.factory.Pratica;

import java.util.Collection;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public interface ProtocollaTaskApi extends ITaskApi {

	public class ProtocollazioneBean {

		@Setter
		@Getter
		Collection<Pratica<?>> praticheCollegate;
		@Setter
		@Getter
		Collection<Allegato> allegati;
		@Setter
		@Getter
		String numeroPG, numeroFascicolo, titolo, rubrica, sezione, tipologiadocumento, oggetto, provenienza, utenteprotocollazione, numeroRegistro, note, dataArrivo, oraArrivo;
		@Setter
		@Getter
		Date dataprotocollazione;
		@Setter
		@Getter
		Integer annoRegistro, annoPG, annoFascicolo;
		@Setter
		@Getter
		TipoProtocollazione tipoProtocollazione;
	}

	public void protocolla(ProtocollazioneBean protocollazioneBean);

	public void protocolla(ProtocollazioneBean protocollazioneBean, String numeroPgCapofila, String annoPgCapofila);

}
