package it.eng.consolepec.xmlplugin.tasks.gestionepec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiTask;
import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.factory.DatiTask.TipoTask;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.AggiungiAllegato;
import it.eng.consolepec.xmlplugin.factory.XMLTaskFactory;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.ProtocollazionePEC;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Ricevuta;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.TipoEmail;
import it.eng.consolepec.xmlplugin.pratica.email.DatiPraticaEmailTaskAdapter;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;
import it.eng.consolepec.xmlplugin.pratica.email.XMLPraticaEmail;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.AgganciaPraticaAPECTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.AggiungiAllegatoTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.FirmaTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.InviaPECTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.MancataConsegnaReinoltroTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.ModificaBozzaTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.PECInAttesaDiPresaInCaricoTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.PECInviataTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.PECNonInviataTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.PecAccettataTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.PecConsegnataTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.PecNonAccettataTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.PecNonConsegnataTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.PecPreavvisoMancataConsegnaTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.RimuoviAllegatoTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.AgganciaPraticaAPECTaskApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.AggiungiAllegatoTaskPECApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.EliminaBozzaTaskPECApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.FirmaTaskPECApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.InviaPECTaskApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.MancataConsegnaReinoltroPECApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.ModificaBozzaTaskPECApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.PECInAttesaDiPresaInCaricoTaskApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.PECInviataTaskApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.PECNonInviataTaskApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.PecAccettataTaskApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.PecConsegnataTaskApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.PecNonAccettataTaskApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.PecNonConsegnataTaskApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.PecPreavvisoMancataConsegnaTaskApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.RimuoviAllegatoTaskPECApiImpl;
import it.eng.consolepec.xmlplugin.tasks.riattiva.DatiRiattivazioneTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaPECOutTask;

public class XMLGestionePECOutTask extends XMLGestionePECTask implements GestionePECOutTask {

	private DatiPraticaEmailTaskAdapter taskAdapter = null;

	private DatiPraticaEmailTaskAdapter getTaskAdapter() {
		if (taskAdapter == null)
			taskAdapter = (DatiPraticaEmailTaskAdapter) ((XMLPraticaEmail) getEnclosingPratica()).getDatiPraticaTaskAdapter();
		return taskAdapter;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GestionePECOutTask) {
			GestionePECOutTask task2 = (GestionePECOutTask) obj;
			return task2.getDati().getId().equals(getDati().getId());
		}
		return false;
	}

	@Override
	public boolean isEmailValida() {
		return getTaskAdapter().getTotaleDestinatari() != 0 && ((XMLPraticaEmail) getEnclosingPratica()).getDati().getMittente() != null
				&& !((XMLPraticaEmail) getEnclosingPratica()).getDati().getMittente().isEmpty();
	}

	@Override
	public void setMittente(String mittente) {
		getTaskAdapter().setMittente(mittente);
		getTaskAdapter().setProvenienza(mittente);
	}

	@Override
	public void setTitolo(String titolo) {
		getTaskAdapter().setTitolo(titolo);
	}

	@Override
	public void setDataCreazione(Date dataCreazione) {
		getTaskAdapter().setDataCreazione(dataCreazione);
	}

	@Override
	public void setOggetto(String oggetto) {
		getTaskAdapter().setOggetto(oggetto);
	}

	@Override
	public void setDestinatarioPrincipale(String destinatarioPrincipale) {
		getTaskAdapter().setDestinatarioPrincipale(destinatarioPrincipale);
	}

	@Override
	public void setBody(String body) {
		getTaskAdapter().setBody(body);
	}

	@Override
	public void setFirma(String firma) {
		getTaskAdapter().setFirma(firma);
	}

	@Override
	public void setTipoEmail(TipoEmail tipoEmail) {
		getTaskAdapter().setTipoEmail(tipoEmail);
	}

	@Override
	public void setStato(DatiEmail.Stato stato) {
		getTaskAdapter().setStato(stato);
	}

	@Override
	public void setDataInvio(Date dataInvio) {
		getTaskAdapter().setDataInvio(dataInvio);
	}

	@Override
	public void addAllegato(Allegato allegato) {
		TreeSet<DatiPratica.Allegato> allegati = getEnclosingPratica().getDati().getAllegati();
		allegati.add(allegato);
	}

	@Override
	public List<DatiPratica.Allegato> getAllegati() {
		ArrayList<DatiPratica.Allegato> allegati = new ArrayList<DatiPratica.Allegato>(getEnclosingPratica().getDati().getAllegati());
		List<DatiPratica.Allegato> b = new ArrayList<DatiPratica.Allegato>(allegati.size());
		Collections.copy(b, allegati);
		return b;
	}

	@Override
	public List<Destinatario> getDestinatari() {
		List<Destinatario> destinatari = getEnclosingPratica().getDati().getDestinatari();
		List<Destinatario> b = new ArrayList<Destinatario>(destinatari.size());
		Collections.copy(b, destinatari);
		return b;
	}

	@Override
	public void addDestinatario(Destinatario destinatario) {
		List<Destinatario> destinatari = getEnclosingPratica().getDati().getDestinatari();
		destinatari.add(destinatario);
	}

	@Override
	public void removeDestinatario(Destinatario destinatario) {
		List<Destinatario> destinatari = getEnclosingPratica().getDati().getDestinatari();
		destinatari.remove(destinatario);
	}

	@Override
	public void removeAllDestinatari() {
		getEnclosingPratica().getDati().getDestinatari().clear();
	}

	@Override
	public List<Destinatario> getDestinatariCC() {
		List<Destinatario> destinatariCC = getEnclosingPratica().getDati().getDestinatariCC();
		List<Destinatario> b = new ArrayList<Destinatario>(destinatariCC.size());
		Collections.copy(b, destinatariCC);
		return b;
	}

	@Override
	public void addDestinatarioCC(Destinatario destinatariocc) {
		List<Destinatario> destinatari = getEnclosingPratica().getDati().getDestinatariCC();
		destinatari.add(destinatariocc);
	}

	@Override
	public void removeDestinatarioCC(Destinatario destinatariocc) {
		List<Destinatario> destinatari = getEnclosingPratica().getDati().getDestinatariCC();
		destinatari.remove(destinatariocc);
	}

	@Override
	public void removeAllDestinatariCC() {
		getEnclosingPratica().getDati().getDestinatariCC().clear();
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
		xtf.newTaskInstance(RiattivaPECOutTask.class, pratica, riportaInGestioneTask);

	}

	@Override
	public void aggiungiProtocollo(ProtocollazionePEC protocollazionePEC) {
		getTaskAdapter().setProtocollazionePEC(protocollazionePEC);

	}

	@Override
	public void setInteroperabile(boolean interoperabile) {
		getTaskAdapter().setInteroperabile(interoperabile);
	}

	@Override
	protected void initApiTask() {
		super.initApiTask();
		operazioni.put(TipoApiTaskPEC.FIRMA, new FirmaTaskPECApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.AGGIUNGI_ALLEGATO, new AggiungiAllegatoTaskPECApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.RIMUOVI_ALLEGATO, new RimuoviAllegatoTaskPECApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.ELIMINA_BOZZA, new EliminaBozzaTaskPECApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.AGGANCIA_PRATICA, new AgganciaPraticaAPECTaskApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.MANCATA_CONSEGNA_REINOLTRO, new MancataConsegnaReinoltroPECApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.INVIA_PEC, new InviaPECTaskApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.PEC_IN_ATTESA_PRESA_IN_CARICO, new PECInAttesaDiPresaInCaricoTaskApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.PEC_INVIATA, new PECInviataTaskApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.PEC_NON_INVIATA, new PECNonInviataTaskApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.PEC_ACCETTATA, new PecAccettataTaskApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.PEC_NON_ACCETTATA, new PecNonAccettataTaskApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.PEC_CONSEGNATA, new PecConsegnataTaskApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.PEC_PREAVVISO_MANCATA_CONSEGNA, new PecPreavvisoMancataConsegnaTaskApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.PEC_NON_CONSEGNATA, new PecNonConsegnataTaskApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.MODIFICA_BOZZA, new ModificaBozzaTaskPECApiImpl<DatiGestionePECTask>(this));
	}

	@Override
	public void aggiungiAllegato(Allegato allegato, AggiungiAllegato handler) throws ApplicationException, InvalidArgumentException {
		checkAbilitazione(TipoApiTaskPEC.AGGIUNGI_ALLEGATO);
		((AggiungiAllegatoTaskPECApi) operazioni.get(TipoApiTaskPEC.AGGIUNGI_ALLEGATO)).aggiungiAllegato(allegato, handler);
	}

	@Override
	public void aggiungiAllegato(Allegato allegato, Pratica<?> pratica, AggiungiAllegato handler) throws ApplicationException, InvalidArgumentException {
		checkAbilitazione(TipoApiTaskPEC.AGGIUNGI_ALLEGATO);
		((AggiungiAllegatoTaskPECApi) operazioni.get(TipoApiTaskPEC.AGGIUNGI_ALLEGATO)).aggiungiAllegato(allegato, pratica, handler);
	}

	@Override
	public void rimuoviAllegato(Allegato allegato) {
		checkAbilitazione(TipoApiTaskPEC.RIMUOVI_ALLEGATO);
		((RimuoviAllegatoTaskPECApi) operazioni.get(TipoApiTaskPEC.RIMUOVI_ALLEGATO)).rimuoviAllegato(allegato);
	}

	@Override
	public void firmaAllegati(List<Allegato> allegati) {
		checkAbilitazione(TipoApiTaskPEC.FIRMA);
		((FirmaTaskPECApi) operazioni.get(TipoApiTaskPEC.FIRMA)).firmaAllegati(allegati);
	}

	@Override
	public void agganciaPraticaAPEC(Pratica<?> pratica) throws PraticaException {
		checkAbilitazione(TipoApiTaskPEC.AGGANCIA_PRATICA);
		((AgganciaPraticaAPECTaskApi) operazioni.get(TipoApiTaskPEC.AGGANCIA_PRATICA)).agganciaPraticaAPEC(pratica);
	}

	@Override
	public void impostaMancataConsegnaInReinoltro() {
		checkAbilitazione(TipoApiTaskPEC.MANCATA_CONSEGNA_REINOLTRO);
		((MancataConsegnaReinoltroTaskPECApi) operazioni.get(TipoApiTaskPEC.MANCATA_CONSEGNA_REINOLTRO)).impostaMancataConsegnaInReinoltro();

	}

	@Override
	public void inviaPec() {
		checkAbilitazione(TipoApiTaskPEC.INVIA_PEC);
		((InviaPECTaskApi) operazioni.get(TipoApiTaskPEC.INVIA_PEC)).inviaPec();
	}

	@Override
	public void pecInAttesaDiPresaInCarico(Integer idEmailServer) {
		checkAbilitazione(TipoApiTaskPEC.PEC_IN_ATTESA_PRESA_IN_CARICO);
		((PECInAttesaDiPresaInCaricoTaskApi) operazioni.get(TipoApiTaskPEC.PEC_IN_ATTESA_PRESA_IN_CARICO)).pecInAttesaDiPresaInCarico(idEmailServer);
	}

	@Override
	public void pecInviata(String messageId, Date dataInvio) {
		checkAbilitazione(TipoApiTaskPEC.PEC_INVIATA);
		((PECInviataTaskApi) operazioni.get(TipoApiTaskPEC.PEC_INVIATA)).pecInviata(messageId, dataInvio);

	}

	@Override
	public void pecNonInviata(String messageId) {
		checkAbilitazione(TipoApiTaskPEC.PEC_NON_INVIATA);
		((PECNonInviataTaskApi) operazioni.get(TipoApiTaskPEC.PEC_NON_INVIATA)).pecNonInviata(messageId);
	}

	@Override
	public void pecAccettata() {
		checkAbilitazione(TipoApiTaskPEC.PEC_ACCETTATA);
		((PecAccettataTaskApi) operazioni.get(TipoApiTaskPEC.PEC_ACCETTATA)).pecAccettata();
	}

	@Override
	public void pecNonAccettata(String errore) {
		checkAbilitazione(TipoApiTaskPEC.PEC_NON_ACCETTATA);
		((PecNonAccettataTaskApi) operazioni.get(TipoApiTaskPEC.PEC_NON_ACCETTATA)).pecNonAccettata(errore);
	}

	@Override
	public void pecConsegnata(Ricevuta ricevutaConsegna) {
		checkAbilitazione(TipoApiTaskPEC.PEC_CONSEGNATA);
		((PecConsegnataTaskApi) operazioni.get(TipoApiTaskPEC.PEC_CONSEGNATA)).pecConsegnata(ricevutaConsegna);
	}

	@Override
	public void pecPreavvisoMancataConsegna(Ricevuta ricevutaConsegna) {
		checkAbilitazione(TipoApiTaskPEC.PEC_PREAVVISO_MANCATA_CONSEGNA);
		((PecPreavvisoMancataConsegnaTaskApi) operazioni.get(TipoApiTaskPEC.PEC_PREAVVISO_MANCATA_CONSEGNA)).pecPreavvisoMancataConsegna(ricevutaConsegna);
	}

	@Override
	public void pecNonConsegnata(Ricevuta ricevutaConsegna) {
		checkAbilitazione(TipoApiTaskPEC.PEC_NON_CONSEGNATA);
		((PecNonConsegnataTaskApi) operazioni.get(TipoApiTaskPEC.PEC_NON_CONSEGNATA)).pecNonConsegnata(ricevutaConsegna);
	}

	@Override
	public void inserisciRicevuta(Ricevuta ricevuta) {
		if (!(pratica instanceof PraticaEmailOut))
			throw new PraticaException("Le ricevute sono gestibili solo per le pec in uscita");
		getTaskAdapter().addRicevuta(ricevuta);
	}

	@Override
	public void modificaBozza(String mittente, List<String> destinatari, List<String> destinatariCC, String oggetto, String body, String firma) {
		checkAbilitazione(TipoApiTaskPEC.MODIFICA_BOZZA);
		((ModificaBozzaTaskPECApi) operazioni.get(TipoApiTaskPEC.MODIFICA_BOZZA)).modificaBozza(mittente, destinatari, destinatariCC, oggetto, body, firma);
	}

}
