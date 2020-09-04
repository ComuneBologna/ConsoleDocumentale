package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.personale;

import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl.AggiungiAllegatoApiTaskImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl.ConcludiFascicoloTaskApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl.EliminaFascicoloTaskApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl.FirmaAllegatiApiTaskImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl.ModificaDatoAggiuntivoTaskApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl.RimuoviAllegatoTaskApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl.TerminaApiTaskImpl;

public class XMLGestioneFascicoloPersonaleTask extends XMLTaskFascicolo<DatiGestioneFascicoloPersonaleTask> implements GestioneFascicoloPersonaleTask {

	@Override
	protected void initApiTask() {
		operazioni.put(TipoApiTask.AGGIUNGI_ALLEGATO, new AggiungiAllegatoApiTaskImpl<DatiGestioneFascicoloPersonaleTask>(this));
		operazioni.put(TipoApiTask.CONCLUDI_FASCICOLO, new ConcludiFascicoloTaskApiImpl<DatiGestioneFascicoloPersonaleTask>(this));
		operazioni.put(TipoApiTask.FIRMA, new FirmaAllegatiApiTaskImpl<DatiGestioneFascicoloPersonaleTask>(this));
		operazioni.put(TipoApiTask.RIMUOVI_ALLEGATO, new RimuoviAllegatoTaskApiImpl<DatiGestioneFascicoloPersonaleTask>(this));
		operazioni.put(TipoApiTask.TERMINA, new TerminaApiTaskImpl<DatiGestioneFascicoloPersonaleTask>(this));
		operazioni.put(TipoApiTask.ELIMINA_FASCICOLO, new EliminaFascicoloTaskApiImpl<DatiGestioneFascicoloPersonaleTask>(this));
		operazioni.put(TipoApiTask.MODIFICA_DATO_AGGIUNTIVO, new ModificaDatoAggiuntivoTaskApiImpl<DatiGestioneFascicoloPersonaleTask>(this));
	}

	@Override
	protected DatiGestioneFascicoloPersonaleTask getDatiTask(Assegnatario asscorrente) {
		DatiGestioneFascicoloPersonaleTask.Builder builder = new DatiGestioneFascicoloPersonaleTask.Builder();
		builder.setAssegnatario(asscorrente);
		return builder.construct();
	}

}
