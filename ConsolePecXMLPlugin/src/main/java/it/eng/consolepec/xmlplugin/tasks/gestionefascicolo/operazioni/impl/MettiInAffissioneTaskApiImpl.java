package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.MettiInAffissioneTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

import java.util.List;

public class MettiInAffissioneTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements MettiInAffissioneTaskApi {

	public MettiInAffissioneTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void mettiInAffissione() {
		if (!isOperazioneAbilitata())
			throw new PraticaException("Operazione non abilitata.");
		((XMLFascicolo) task.getEnclosingPratica()).getDatiPraticaTaskAdapter().setStato(Stato.IN_AFFISSIONE);

		new TerminaApiTaskImpl(task).termina();

		DatiFascicolo d = (DatiFascicolo) task.getEnclosingPratica().getDati();

		List<ProtocollazioneCapofila> protocollazioniCapofila = d.getProtocollazioniCapofila();

		if (protocollazioniCapofila != null && protocollazioniCapofila.size() != 0) {
			ProtocollazioneCapofila capofila = protocollazioniCapofila.get(0);
			generaEventoPerProtocollazione( EventiIterFascicolo.IN_AFFISSIONE, capofila.getNumeroPG(), capofila.getAnnoPG(), task.getCurrentUser(), task.getDati().getAssegnatario().getEtichetta());
		}
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		Stato stato = ((Fascicolo) task.getEnclosingPratica()).getDati().getStato();
		//TipoPratica tipo = ((Fascicolo) task.getEnclosingPratica()).getDati().getTipo();
		
		//boolean isAlboPretorio = tipo.equals(TipologiaPratica.FASCICOLO_ALBO_PRETORIO);
		boolean hasStatoCoerente = stato.equals(DatiFascicolo.Stato.IN_GESTIONE) || stato.equals(DatiFascicolo.Stato.IN_VISIONE) || stato.equals(DatiFascicolo.Stato.DA_INOLTRARE_ESTERNO);
		//tutte quelle pratiche che sono state messe in affissione senza disattivare il task
		boolean isToFix = stato.equals(DatiFascicolo.Stato.IN_AFFISSIONE); 
		//return isAlboPretorio && (hasStatoCoerente||isToFix);
		return hasStatoCoerente||isToFix;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.METTI_IN_AFFISSIONE;
	}

}
