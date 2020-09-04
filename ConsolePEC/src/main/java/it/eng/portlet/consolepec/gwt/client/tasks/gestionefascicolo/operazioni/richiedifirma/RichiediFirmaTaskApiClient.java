package it.eng.portlet.consolepec.gwt.client.tasks.gestionefascicolo.operazioni.richiedifirma;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.profilazione.Utente;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioDTO;
import it.eng.portlet.consolepec.gwt.shared.model.richiedifirma.RichiediFirmaVistoDTO;

import java.util.Set;

/**
 *
 * @author biagiot
 *
 */
public interface RichiediFirmaTaskApiClient {

	boolean isTaskInvocabile(FascicoloDTO pratica, Set<AllegatoDTO> allegati);

	boolean isTaskAnnullabile(FascicoloDTO pratica, Set<AllegatoDTO> allegati);

	void goToRichiediVistoFirma(String fascicoloPath, Set<AllegatoDTO> allegati);

	void richiediFirmaVisto(RichiediFirmaVistoDTO richiediFirmaVistoDTO);

	DestinatarioDTO getDestinatario(Utente utente);
	DestinatarioDTO getDestinatario(AnagraficaRuolo ruolo);
}
