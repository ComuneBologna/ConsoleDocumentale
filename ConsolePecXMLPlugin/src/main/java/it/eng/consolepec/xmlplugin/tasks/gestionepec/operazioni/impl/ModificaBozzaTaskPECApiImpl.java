package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import java.util.List;

import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.pratica.email.DatiPraticaEmailTaskAdapter;
import it.eng.consolepec.xmlplugin.pratica.email.XMLPraticaEmail;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.ModificaBozzaTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;

public class ModificaBozzaTaskPECApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements ModificaBozzaTaskPECApi {

	public ModificaBozzaTaskPECApiImpl(XMLGestionePECTask task) {
		super(task);
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.MODIFICA_BOZZA;
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return task.getEnclosingPratica().getDati().getStato().equals(Stato.BOZZA);
	}

	@Override
	public void modificaBozza(String mittente, List<String> destinatari, List<String> destinatariCC, String oggetto, String body, String firma) {

		DatiPraticaEmailTaskAdapter datiPraticaEmailTaskAdapter = (DatiPraticaEmailTaskAdapter) ((XMLPraticaEmail) task.getEnclosingPratica()).getDatiPraticaTaskAdapter();
		StringBuilder modifiche = new StringBuilder();

		if (task.getEnclosingPratica().getDati().getMittente() != null && !task.getEnclosingPratica().getDati().getMittente().equals(mittente)) {
			modifiche.append("Mittente: da").append(task.getEnclosingPratica().getDati().getMittente()).append(" a ").append(mittente).append(";");
		}

		if (task.getEnclosingPratica().getDati().getOggetto() != null && !task.getEnclosingPratica().getDati().getOggetto().equals(oggetto)) {
			modifiche.append("Oggetto: da").append(task.getEnclosingPratica().getDati().getOggetto()).append(" a ").append(oggetto).append(";");
		}

		datiPraticaEmailTaskAdapter.setMittente(mittente);
		datiPraticaEmailTaskAdapter.setOggetto(oggetto);
		datiPraticaEmailTaskAdapter.setBody(body);
		datiPraticaEmailTaskAdapter.setFirma(firma);
		datiPraticaEmailTaskAdapter.setDestinatari(destinatari);
		datiPraticaEmailTaskAdapter.setDestinatariCC(destinatariCC);

		String text = modifiche.toString();
		if (!text.isEmpty())
			generaEvento(EventiIterPEC.MODIFICA_BOZZA, task.getCurrentUser(), text);
	}

}
