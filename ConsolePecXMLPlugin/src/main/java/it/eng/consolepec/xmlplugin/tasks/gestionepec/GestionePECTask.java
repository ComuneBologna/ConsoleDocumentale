package it.eng.consolepec.xmlplugin.tasks.gestionepec;

import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.AgganciaAFascicoloTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.CreaFascicoloTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.RiassegnaTaskPECApi;

public interface GestionePECTask extends Task<DatiGestionePECTask>, RiassegnaTaskPECApi, CreaFascicoloTaskPECApi, AgganciaAFascicoloTaskPECApi {

	public void setEmailId(String mailID);

}
