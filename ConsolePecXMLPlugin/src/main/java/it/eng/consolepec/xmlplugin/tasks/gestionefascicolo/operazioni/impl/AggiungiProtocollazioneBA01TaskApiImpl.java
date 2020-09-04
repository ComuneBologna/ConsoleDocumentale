package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofilaBuilder;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.AggiungiProtocollazioneBA01TaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

import java.util.Date;
import java.util.List;

public class AggiungiProtocollazioneBA01TaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements AggiungiProtocollazioneBA01TaskApi {

	public AggiungiProtocollazioneBA01TaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void aggiungiCapofilaFromBA01(String numeroPG, Integer annoPG, String titolo, String rubrica, String sezione, String oggetto, Date dataProtocollazione, String dataArrivo, String oraArrivo) throws PraticaException {
		List<ProtocollazioneCapofila> protocollazioniCapofila = ((DatiFascicolo) task.getEnclosingPratica().getDati()).getProtocollazioniCapofila();

		for (ProtocollazioneCapofila p : protocollazioniCapofila) {
			if (p.getNumeroPG().equalsIgnoreCase(numeroPG) && p.getAnnoPG().equals(annoPG))
				return;
		}

		ProtocollazioneCapofilaBuilder protocollazioneCapofilaBuilder = new ProtocollazioneCapofilaBuilder((DatiFascicolo) task.getEnclosingPratica().getDati());
		protocollazioneCapofilaBuilder.setNumeroPG(numeroPG);
		protocollazioneCapofilaBuilder.setAnnoPG(annoPG);
		protocollazioneCapofilaBuilder.setTitolo(titolo);
		protocollazioneCapofilaBuilder.setRubrica(rubrica);
		protocollazioneCapofilaBuilder.setSezione(sezione);
		protocollazioneCapofilaBuilder.setDataprotocollazione(dataProtocollazione);
		protocollazioneCapofilaBuilder.setDataArrivo(dataArrivo);
		protocollazioneCapofilaBuilder.setOraArrivo(oraArrivo);
		protocollazioneCapofilaBuilder.setOggetto(oggetto);
		protocollazioneCapofilaBuilder.setFromBa01(true);
		protocollazioneCapofilaBuilder.setTipoProtocollazione(null);

		protocollazioniCapofila.add(protocollazioneCapofilaBuilder.construct());

	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.AGGIUNGI_PROTOCOLLAZIONE_BA01;
	}
}
