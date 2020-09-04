package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Collegamento;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.EliminaCollegamentoFascicoloTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

import java.text.MessageFormat;
import java.util.TreeSet;

public class EliminaCollegamentoFascicoloTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements EliminaCollegamentoFascicoloTaskApi {

	public EliminaCollegamentoFascicoloTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void eliminaCollegamento(Fascicolo fascicolo) throws PraticaException {

		// collegamenti del fascicolo del quale eseguo il task - LOCALE
		TreeSet<Collegamento> collegamentiFascicoloLocale = new TreeSet<Collegamento>(getDatiFascicolo().getCollegamenti());
		
		// collegamenti del fascicolo in input - REMOTO
		TreeSet<Collegamento> collegamentiFascicoloRemoto = new TreeSet<Collegamento>(fascicolo.getDati().getCollegamenti());

		// collegamento corripondente al fascicolo LOCALE 
		Collegamento collegamentoLocale = retrieveCollegamentoLocale(fascicolo.getDati(), task.getEnclosingPratica().getAlfrescoPath());

		// collegamento corripondente al fascicolo REMOTO 
		Collegamento collegamentoRemoto = retrieveCollegamentoRemoto(getDatiFascicolo(), fascicolo.getAlfrescoPath());

		if (collegamentoLocale != null && collegamentoRemoto != null) {
			// ranzo il collegamento locale al fascicolo remoto 
			collegamentiFascicoloLocale.remove(collegamentoRemoto);
			// ranzo il collegamento remoto al fasicolo locale
			collegamentiFascicoloRemoto.remove(collegamentoLocale);
		} else
			throw new PraticaException(MessageFormat.format("Il collegamento tra il fascicolo {0} e il fascicolo {1} contiene degli errori", getDatiFascicolo().getIdDocumentale(), fascicolo.getDati().getIdDocumentale()));

		// reset delle liste originali 
		getDatiFascicolo().getCollegamenti().clear();
		fascicolo.getDati().getCollegamenti().clear();

		// aggiungo le liste "ripulite" di collegamenti 
		getDatiFascicolo().getCollegamenti().addAll(collegamentiFascicoloLocale);
		fascicolo.getDati().getCollegamenti().addAll(collegamentiFascicoloRemoto);
		
		generaEvento(EventiIterFascicolo.ELIMINA_COLLEGAMENTO_FASCICOLO, task.getCurrentUser(), fascicolo.getDati().getIdDocumentale());

	}

	private Collegamento retrieveCollegamentoRemoto(DatiFascicolo datiFascicolo, String alfrescoPath) {
		return retrieveCollegamento(datiFascicolo.getCollegamenti(), alfrescoPath);
	}

	private Collegamento retrieveCollegamentoLocale(DatiFascicolo datiFascicolo, String alfrescoPath) {
		return retrieveCollegamento(datiFascicolo.getCollegamenti(), alfrescoPath);
	}

	private Collegamento retrieveCollegamento(TreeSet<Collegamento> collegamenti, String alfrescoPath) {
		for (Collegamento collegamento : collegamenti)
			if (collegamento.getPath().equals(alfrescoPath))
				return collegamento;
		return null;
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.ELIMINA_COLLEGAMENTO_FASCICOLO;
	}

}
