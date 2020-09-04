package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import com.google.common.base.Strings;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPraticaTaskAdapter;
import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.factory.XMLPratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ModificaFascicoloTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

/**
 * @author GiacomoFM
 * @since 12/lug/2017
 */
public class ModificaFascicoloTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements ModificaFascicoloTaskApi {

	public ModificaFascicoloTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return !Stato.ARCHIVIATO.equals(getDatiFascicolo().getStato());
	}

	@Override
	protected ITipoApiTask getTipoApiTask() {
		return TipoApiTask.MODIFICA_FASCICOLO;
	}

	@Override
	public boolean modifica(String titolo, TipologiaPratica tipologiaPratica) {
		boolean modTitolo = modificaTitolo(titolo);
		boolean modTipoFascicolo = modificaTipoFascicolo(tipologiaPratica);

		return modTitolo || modTipoFascicolo;
	}

	public boolean modificaTitolo(final String titolo) {
		String old = getDatiFascicolo().getTitolo();
		if (!Strings.nullToEmpty(old).equals(Strings.nullToEmpty(titolo))) {
			getDatiFascicolo().setTitolo(titolo);
			if (Strings.isNullOrEmpty(getDatiFascicolo().getTitoloOriginale())) {
				getDatiFascicolo().setTitoloOriginale(old);
			}
			generaEvento(EventiIterFascicolo.MODIFICA_TITOLO, task.getCurrentUser(), old, titolo);
			return true;
		}
		return false;
	}

	public boolean modificaTipoFascicolo(final TipologiaPratica tipo) {
		TipologiaPratica old = getDatiFascicolo().getTipo();

		if (tipo != null && !old.equals(tipo)) {
			Fascicolo fascicolo = (Fascicolo) task.getEnclosingPratica();
			DatiPraticaTaskAdapter datiPraticaTaskAdapter = ((XMLPratica<?>) fascicolo).getDatiPraticaTaskAdapter();
			datiPraticaTaskAdapter.setTipo(tipo);
			generaEvento(EventiIterFascicolo.CAMBIA_TIPO_FASCICOLO, task.getCurrentUser(), old.getEtichettaTipologia(), tipo.getEtichettaTipologia());
			return true;
		}
		return false;
	}

}
