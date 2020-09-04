package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import java.util.Map;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.CampoModificabile;
import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.AggiungiAllegato;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Protocollazione;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.RicaricaAllegatoProtocollatoTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class RicaricaAllegatoProtocollatoTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements RicaricaAllegatoProtocollatoTaskApi {

	public RicaricaAllegatoProtocollatoTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void ricaricaAllegatoProtocollato(Allegato allegato, AggiungiAllegato handler, Map<String, String> campi) throws InvalidArgumentException, ApplicationException {

		if (!XmlPluginUtil.isPdfEditabile(allegato))
			throw new PraticaException("L'allegato " + allegato.getNome() + " non è un pdf editabile.");

		String nomeAllegato = null;
		Allegato aggiungiAllegato = null;
		nomeAllegato = allegato.getNome();
		aggiungiAllegato = handler.aggiungiAllegato(new StringBuilder().append(getDatiFascicolo().getFolderPath()).append("/").append(task.getEnclosingPratica().getSubFolderPath()).toString(),
				nomeAllegato);

		if (!XmlPluginUtil.isPdfEditabile(aggiungiAllegato))
			throw new PraticaException("Il nuovo allegato " + aggiungiAllegato.getNome() + " non è un pdf editabile.");

		for (String key : campi.keySet()) {
			for (CampoModificabile c : aggiungiAllegato.getCampiModificabili()) {
				if (key.equalsIgnoreCase(c.getNome())) {
					c.setValore(campi.get(key));
					c.setAbilitato(false);
				}
			}
		}

		if (XmlPluginUtil.allegatoInPratica(aggiungiAllegato, task.getEnclosingPratica()).size() != 0)
			getDatiFascicolo().getAllegati().removeAll(XmlPluginUtil.allegatoInPratica(aggiungiAllegato, task.getEnclosingPratica()));

		XmlPluginUtil.sostituisciAllegatoProtocollato(allegato, aggiungiAllegato, (Fascicolo) task.getEnclosingPratica());

		generaEvento(EventiIterFascicolo.RICARICA_ALLEGATO_PROTOCOLLATO, task.getCurrentUser(), allegato.getLabel(), GenericsUtil.convertMapToString(campi));
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {

		Fascicolo fascicolo = (Fascicolo) task.getEnclosingPratica();
		for (ProtocollazioneCapofila capofila : fascicolo.getDati().getProtocollazioniCapofila()) {

			for (Allegato a : capofila.getAllegatiProtocollati())
				if (XmlPluginUtil.isPdfEditabile(a))
					return true;
			for (Protocollazione collegata : capofila.getProtocollazioniCollegate())
				for (Allegato a : collegata.getAllegatiProtocollati())
					if (XmlPluginUtil.isPdfEditabile(a))
						return true;
		}
		return false;
	}

	@Override
	protected ITipoApiTask getTipoApiTask() {
		return TipoApiTask.RICARICA_ALLEGATO_PROTOCOLLATO;
	}
}
