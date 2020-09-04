package it.eng.consolepec.xmlplugin.tasks.gestionepec;

import java.util.Date;
import java.util.List;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiTask;
import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.factory.DatiTask.TipoTask;
import it.eng.consolepec.xmlplugin.factory.XMLTaskFactory;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.pratica.operazionipecconcluse.AnnullaElettoraleApiTask;
import it.eng.consolepec.xmlplugin.pratica.operazionipecconcluse.impl.elettorale.AnnullaElettoraleApiTaskImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.ArchiviaTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.EliminaTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.ModificaOperatoreTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.NotificaTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.RiconsegnaTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.RiportaInLetturaTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.RispostaInteroperabileTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.ScartaTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.AgganciaAPECTaskPECApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.ArchiviaTaskPECApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.EliminaTaskPECApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.ImportaElettoraleTaskPECApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.ModificaNotePECInTaskApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.ModificaOperatoreTaskPECApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.NotificaTaskPECApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.RiconsegnaTaskPECApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.RiportaInLetturaTaskPECApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.RispostaInteroperabileTaskApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.ScartaTaskPECApiImpl;
import it.eng.consolepec.xmlplugin.tasks.operazioni.ModificaNoteTaskApi;
import it.eng.consolepec.xmlplugin.tasks.riattiva.DatiRiattivazioneTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaPECInTask;

public class XMLGestionePECInTask extends XMLGestionePECTask implements GestionePECInTask {

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GestionePECInTask) {
			GestionePECInTask task2 = (GestionePECInTask) obj;
			return task2.getDati().getId().equals(getDati().getId());
		}
		return false;
	}

	@Override
	public TipoTask getTipo() {
		return DatiTask.TipoTask.GestionePECTask;
	}

	@Override
	public void termina() {
		getDati().setAttivo(false);
		getDati().getAssegnatario().setDataFine(new Date());

		DatiRiattivazioneTask.Builder builderTask = new DatiRiattivazioneTask.Builder();
		builderTask.setAssegnatario(new Assegnatario(getDati().getAssegnatario().getNome(), getDati().getAssegnatario().getEtichetta(), new Date(System.currentTimeMillis() - 1000), null));
		builderTask.setAttivo(true);
		builderTask.setIdTaskDaRiattivare(getDati().getId());
		DatiRiattivazioneTask riportaInGestioneTask = builderTask.construct();
		XMLTaskFactory xtf = new XMLTaskFactory();
		xtf.newTaskInstance(RiattivaPECInTask.class, pratica, riportaInGestioneTask);
	}

	public void setAttivo() {
		getDati().setAttivo(true);
	}

	@Override
	protected void initApiTask() {
		super.initApiTask();
		operazioni.put(TipoApiTaskPEC.ARCHIVIA, new ArchiviaTaskPECApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.ELIMINA, new EliminaTaskPECApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.RICONSEGNA, new RiconsegnaTaskPECApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.RIPORTA_IN_LETTURA, new RiportaInLetturaTaskPECApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.IMPORTA_ELETTORALE, new ImportaElettoraleTaskPECApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.ANNULLA_ELETTORALE, new AnnullaElettoraleApiTaskImpl(this));
		operazioni.put(TipoApiTaskPEC.SCARTA, new ScartaTaskPECApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.NOTIFICA, new NotificaTaskPECApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.AGGANCIA_A_PEC, new AgganciaAPECTaskPECApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.RISPOSTA_INTEROPERABILE, new RispostaInteroperabileTaskApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.MODIFICA_OPERATORE, new ModificaOperatoreTaskPECApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.MODIFICA_NOTE, new ModificaNotePECInTaskApiImpl<DatiGestionePECTask>(this));
	}

	@Override
	public void archivia() {
		checkAbilitazione(TipoApiTaskPEC.ARCHIVIA);
		((ArchiviaTaskPECApi) operazioni.get(TipoApiTaskPEC.ARCHIVIA)).archivia();
	}

	@Override
	public void elimina() {
		checkAbilitazione(TipoApiTaskPEC.ELIMINA);
		((EliminaTaskPECApi) operazioni.get(TipoApiTaskPEC.ELIMINA)).elimina();
	}

	@Override
	public void riconsegna() {
		checkAbilitazione(TipoApiTaskPEC.RICONSEGNA);
		((RiconsegnaTaskPECApi) operazioni.get(TipoApiTaskPEC.RICONSEGNA)).riconsegna();
	}

	@Override
	public void riportaInLettura() throws PraticaException {
		checkAbilitazione(TipoApiTaskPEC.RIPORTA_IN_LETTURA);
		((RiportaInLetturaTaskPECApi) operazioni.get(TipoApiTaskPEC.RIPORTA_IN_LETTURA)).riportaInLettura();
	}

	@Override
	public void scarta() {
		checkAbilitazione(TipoApiTaskPEC.SCARTA);
		((ScartaTaskPECApi) operazioni.get(TipoApiTaskPEC.SCARTA)).scarta();
	}

	@Override
	public void notifica() {
		checkAbilitazione(TipoApiTaskPEC.NOTIFICA);
		((NotificaTaskPECApi) operazioni.get(TipoApiTaskPEC.NOTIFICA)).notifica();
	}

	@Override
	public void inviaEccezione(List<String> indirizziNotifica, String pathAllegato, String nomeAllegato) {
		checkAbilitazione(TipoApiTaskPEC.RISPOSTA_INTEROPERABILE);
		((RispostaInteroperabileTaskApi) operazioni.get(TipoApiTaskPEC.RISPOSTA_INTEROPERABILE)).inviaEccezione(indirizziNotifica, pathAllegato, nomeAllegato);
	}

	@Override
	public void inviaConferma(List<String> indirizziNotifica, String pathAllegato, String nomeAllegato) {
		checkAbilitazione(TipoApiTaskPEC.RISPOSTA_INTEROPERABILE);
		((RispostaInteroperabileTaskApi) operazioni.get(TipoApiTaskPEC.RISPOSTA_INTEROPERABILE)).inviaConferma(indirizziNotifica, pathAllegato, nomeAllegato);
	}

	@Override
	public void inviaAggiornamento(List<String> indirizziNotifica, String pathAllegato, String nomeAllegato) {
		checkAbilitazione(TipoApiTaskPEC.RISPOSTA_INTEROPERABILE);
		((RispostaInteroperabileTaskApi) operazioni.get(TipoApiTaskPEC.RISPOSTA_INTEROPERABILE)).inviaAggiornamento(indirizziNotifica, pathAllegato, nomeAllegato);
	}

	@Override
	public void inviaAnnullamento(List<String> indirizziNotifica, String pathAllegato, String nomeAllegato) {
		checkAbilitazione(TipoApiTaskPEC.RISPOSTA_INTEROPERABILE);
		((RispostaInteroperabileTaskApi) operazioni.get(TipoApiTaskPEC.RISPOSTA_INTEROPERABILE)).inviaAnnullamento(indirizziNotifica, pathAllegato, nomeAllegato);
	}

	@Override
	public void annullaElettorale(List<Fascicolo> fascicoliCollegati, List<Fascicolo> daArchiviare) {
		checkAbilitazione(TipoApiTaskPEC.ANNULLA_ELETTORALE);
		((AnnullaElettoraleApiTask) operazioni.get(TipoApiTaskPEC.ANNULLA_ELETTORALE)).annullaElettorale(fascicoliCollegati, daArchiviare);
	}

	@Override
	public void modificaOperatore(String operatore) throws PraticaException {
		checkAbilitazione(TipoApiTaskPEC.MODIFICA_OPERATORE);
		((ModificaOperatoreTaskPECApi) operazioni.get(TipoApiTaskPEC.MODIFICA_OPERATORE)).modificaOperatore(operatore);

	}

	@Override
	public void aggiungiNote(String note) {
		checkAbilitazione(TipoApiTaskPEC.MODIFICA_NOTE);
		((ModificaNoteTaskApi) operazioni.get(TipoApiTaskPEC.MODIFICA_NOTE)).aggiungiNote(note);
	}

	@Override
	public void modificaNote(String note) {
		checkAbilitazione(TipoApiTaskPEC.MODIFICA_NOTE);
		((ModificaNoteTaskApi) operazioni.get(TipoApiTaskPEC.MODIFICA_NOTE)).modificaNote(note);
	}

}
