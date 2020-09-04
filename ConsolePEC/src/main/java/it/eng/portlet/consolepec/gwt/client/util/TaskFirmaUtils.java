package it.eng.portlet.consolepec.gwt.client.util;

import it.eng.portlet.consolepec.gwt.client.operazioni.cartellafirma.CartellaFirmaWizardApiClient.OperazioneWizardTaskFirma;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.DatiTaskDTO.TipoTaskDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TaskDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.StatoDestinatarioTaskFirmaDTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author biagiot
 *
 */
public class TaskFirmaUtils {

	/**
	 * Restituisce un task di firma attivo in cui è coinvolto un allegato
	 *
	 * @param pratica
	 * @param allegato
	 * @return
	 */
	public static TaskFirmaDTO getTaskFirmaAttivo(PraticaDTO pratica, AllegatoDTO allegato) {

		TaskFirmaDTO result = null;

		for(TaskDTO<?> task : pratica.getTasks()) {
			if (TipoTaskDTO.RICHIESTA_FIRMA_TASK.equals(task.getTipo())) {
				TaskFirmaDTO taskFirma = (TaskFirmaDTO) task;

				if (taskFirma.getDati().getRiferimentoAllegato().getNome().equalsIgnoreCase(allegato.getNome())
						&& taskFirma.getDati().isAttivo() && taskFirma.getDati().isValido()) {

					result = taskFirma;
					break;
				}
			}
		}

		return result;
	}

	/**
	 * Restituisce tutti i task di firma attivi di una pratica
	 *
	 * @param pratica
	 * @return
	 */
	public static Set<TaskFirmaDTO> getAllTaskFirmaAttivi(PraticaDTO pratica) {

		Set<TaskFirmaDTO> result = new HashSet<TaskFirmaDTO>();

		for(TaskDTO<?> task : pratica.getTasks()) {
			if (TipoTaskDTO.RICHIESTA_FIRMA_TASK.equals(task.getTipo())) {
				TaskFirmaDTO taskFirma = (TaskFirmaDTO) task;
				if (taskFirma.getDati().isAttivo() && taskFirma.getDati().isValido())
					result.add(taskFirma);
			}
		}

		return result;
	}

	/**
	 * Check sull'abilitazione alla riassegnazione
	 *
	 * @param map
	 * @return
	 */
	public static boolean riassegnazioneAbilitata(Map<FascicoloDTO, List<AllegatoDTO>> map, OperazioneWizardTaskFirma operazioneTask) {

		for (Entry<FascicoloDTO, List<AllegatoDTO>> entry : map.entrySet()) {
			FascicoloDTO fascicolo = entry.getKey();
			List<AllegatoDTO> allegati = entry.getValue();

			List<AllegatoDTO> allegatiEsclusi = new ArrayList<AllegatoDTO>(fascicolo.getAllegati());
			allegatiEsclusi.removeAll(allegati);

			/*
			 * Se c'è almeno un allegato sul quale non voglio operare:
			 * 1. Se uno di questi è in un task di firma attivo e valido allora non è abilitata la riassegnazione nel task di firma.
			 * 2. Se nessuno di questi è in un task di firma attivo allora contollo quelli sui quali voglio operare.
			 *
			 * Se voglio operare su tutti gli allegati della pratica:
			 * 2. contollo quelli sui quali voglio operare
			 */

			// 1.
			if (allegatiEsclusi.size() > 0) {
				for (AllegatoDTO allegato : allegatiEsclusi) {

					if (getTaskFirmaAttivo(fascicolo, allegato) != null)
						return false;
				}
			}

			// 2. Se l'operazioe che voglio effettuare è conclusiva, ovvero conclude il processo di task di firma allora la riassegnazione è abilitata
			// Se non lo è allora controllo se è abilitato a riassegnare
			if (operazioneTask.isOperazioneConclusiva())
				return true;

			for (AllegatoDTO allegato : allegati) {
				if (!riassegnazioneAbilitata(fascicolo, allegato))
					return false;
			}
		}

		return true;
	}

	private static boolean riassegnazioneAbilitata(FascicoloDTO fascicolo, AllegatoDTO allegato) {

		TaskFirmaDTO task = getTaskFirmaAttivo(fascicolo, allegato);

		if (task != null) {
			int inApprovazione = 0;
			for (DestinatarioDTO destinatario : task.getDati().getDestinatari()) {

				if (StatoDestinatarioTaskFirmaDTO.IN_APPROVAZIONE.equals(destinatario.getStatoRichiesta()))
					inApprovazione++;

				if(inApprovazione > 1)
					return false;
			}
		}

		return true;
	}


}
