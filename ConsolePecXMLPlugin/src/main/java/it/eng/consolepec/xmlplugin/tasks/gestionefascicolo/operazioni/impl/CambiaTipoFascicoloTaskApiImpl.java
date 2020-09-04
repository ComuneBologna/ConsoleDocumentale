package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPraticaTaskAdapter;
import it.eng.consolepec.xmlplugin.factory.XMLPratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.CambiaTipoFascicoloTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

public class CambiaTipoFascicoloTaskApiImpl <T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements CambiaTipoFascicoloTaskApi {

	public CambiaTipoFascicoloTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void cambiaTipo(TipologiaPratica nuovoTipo) throws PraticaException {
		
		if (nuovoTipo == null )
			throw new PraticaException("Specificare un tipo valido");
		
		Fascicolo fascicolo = (Fascicolo) task.getEnclosingPratica();
		TipologiaPratica vecchioTipoPratica = fascicolo.getDati().getTipo();
		DatiPraticaTaskAdapter datiPraticaTaskAdapter = ((XMLPratica<?>) fascicolo).getDatiPraticaTaskAdapter();
		datiPraticaTaskAdapter.setTipo(nuovoTipo);
		generaEvento( EventiIterFascicolo.CAMBIA_TIPO_FASCICOLO, task.getCurrentUser(), vecchioTipoPratica.getEtichettaTipologia(), nuovoTipo.getEtichettaTipologia());
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return !(getDatiFascicolo().getStato().equals(Stato.ARCHIVIATO));
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.CAMBIA_TIPO_FASCICOLO;
	}

}
