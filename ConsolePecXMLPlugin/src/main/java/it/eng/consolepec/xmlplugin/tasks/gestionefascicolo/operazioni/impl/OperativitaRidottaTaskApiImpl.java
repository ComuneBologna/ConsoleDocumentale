package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import it.eng.cobo.consolepec.commons.pratica.OperativitaRidotta;
import it.eng.cobo.consolepec.commons.pratica.OperativitaRidotta.OperazioneOperativitaRidotta;
import it.eng.cobo.consolepec.commons.pratica.OperativitaRidotta.OperazioneOperativitaRidotta.TipoAccesso;
import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.OperativitaRidottaTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

public class OperativitaRidottaTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements OperativitaRidottaTaskApi {

	public OperativitaRidottaTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void applicaOperativitaRidotta(String operazione, List<TipoAccesso> accessiConsentiti) {
		Pratica<?> pratica = task.getEnclosingPratica();
		Set<TipoAccesso> accessi = new TreeSet<TipoAccesso>(accessiConsentiti);

		if (pratica.getDati().getOperativitaRidotta() != null) {
			OperazioneOperativitaRidotta operazioneOperativitaRidotta = null;
			for (OperazioneOperativitaRidotta op : pratica.getDati().getOperativitaRidotta().getOperazioni()) {
				if (op.getNomeOperazione().equals(operazione)) {
					operazioneOperativitaRidotta = op;
					accessi.addAll(op.getAccessiConsentiti());
					break;
				}
			}

			if (operazioneOperativitaRidotta == null) {
				operazioneOperativitaRidotta = new OperazioneOperativitaRidotta(operazione);
				pratica.getDati().getOperativitaRidotta().getOperazioni().add(operazioneOperativitaRidotta);
			}

			for (TipoAccesso tipoAccesso : accessiConsentiti) {
				if (!operazioneOperativitaRidotta.getAccessiConsentiti().contains(tipoAccesso))
					operazioneOperativitaRidotta.getAccessiConsentiti().add(tipoAccesso);
			}

		} else {
			OperativitaRidotta operativitaRidotta = new OperativitaRidotta();
			OperazioneOperativitaRidotta operazioneOperativitaRidotta = new OperazioneOperativitaRidotta(operazione);
			operazioneOperativitaRidotta.getAccessiConsentiti().addAll(accessiConsentiti);
			operativitaRidotta.getOperazioni().add(operazioneOperativitaRidotta);
			pratica.getDati().setOperativitaRidotta(operativitaRidotta);
		}

		generaEvento(EventiIterFascicolo.APPLICA_OPERATIVITA_RIDOTTA, task.getCurrentUser(), operazione, getDescrizioneAccessi(accessi));
	}

	private static String getDescrizioneAccessi(Set<TipoAccesso> accessiConsentiti) {
		StringBuilder sb = new StringBuilder();

		for (TipoAccesso ta : accessiConsentiti) {
			if (!sb.toString().isEmpty())
				sb.append(", ");

			switch (ta) {
			case ASSEGNATARIO:
				sb.append("assegnatario fascicolo");
				break;
			case SUPERVISORE:
				sb.append("supervisori dell'assegnatario");
				break;
			case UTENTE_ESTERNO:
				sb.append("assegnatario esterno");
				break;
			case COLLEGAMENTO:
				sb.append("assegnatari dei fascicoli collegati");
				break;
			case LETTURA:
				sb.append("gruppi di visibilità del fascicolo");
				break;
			case MATR_VISIBILITA:
				sb.append("gruppi di visibilità del fascicolo");
				break;
			default:
				break;
			}
		}

		return sb.toString();
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected ITipoApiTask getTipoApiTask() {
		return TipoApiTask.OPERATIVITA_RIDOTTA;
	}

}
