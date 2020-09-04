package it.eng.consolepec.xmlplugin.tasks.gestionepec;

import java.util.Date;
import java.util.List;

import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.ProtocollazionePEC;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Ricevuta;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.TipoEmail;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.AgganciaPraticaAPECTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.AggiungiAllegatoTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.EliminaBozzaTaskPECApi;
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

public interface GestionePECOutTask extends GestionePECTask, FirmaTaskPECApi, AggiungiAllegatoTaskPECApi, RimuoviAllegatoTaskPECApi, EliminaBozzaTaskPECApi, AgganciaPraticaAPECTaskApi, MancataConsegnaReinoltroTaskPECApi, InviaPECTaskApi, PECInAttesaDiPresaInCaricoTaskApi, PECInviataTaskApi, PECNonInviataTaskApi, //
		PecAccettataTaskApi, PecNonAccettataTaskApi, PecConsegnataTaskApi, PecPreavvisoMancataConsegnaTaskApi, PecNonConsegnataTaskApi, ModificaBozzaTaskPECApi {

	public boolean isEmailValida();

	public void setMittente(String mittente);

	public void setTitolo(String titolo);

	public void setDataCreazione(Date dataCreazione);

	public void setOggetto(String oggetto);

	public void setDestinatarioPrincipale(String destinatarioPrincipale);

	public void setBody(String body);

	public void setFirma(String firma);

	public void setTipoEmail(TipoEmail tipoEmail);

	public void setStato(DatiEmail.Stato stato);

	public void setDataInvio(Date dataInvio);

	public void addAllegato(Allegato allegato);

	public List<DatiPratica.Allegato> getAllegati();

	public List<Destinatario> getDestinatari();

	public void addDestinatario(Destinatario destinatario);

	public void removeDestinatario(Destinatario destinatario);

	public void removeAllDestinatari();

	public List<Destinatario> getDestinatariCC();

	public void addDestinatarioCC(Destinatario destinatariocc);

	public void removeDestinatarioCC(Destinatario destinatariocc);

	public void removeAllDestinatariCC();

	public void aggiungiProtocollo(ProtocollazionePEC protocollazionePEC);

	public void setInteroperabile(boolean interoperabile);

	void inserisciRicevuta(Ricevuta ricevuta);

}
