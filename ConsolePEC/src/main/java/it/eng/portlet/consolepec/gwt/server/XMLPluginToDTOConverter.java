package it.eng.portlet.consolepec.gwt.server;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaComunicazione;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaEmailOut;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo.StepIter;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaModello;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaPraticaModulistica;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneComunicazioneAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneEmailOutAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneFascicoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneModelloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.ModificaFascicoloAbilitazione;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoTabella;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreMultiplo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreSingolo;
import it.eng.cobo.consolepec.commons.urbanistica.PraticaProcedi;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler.CondizioneAbilitazione;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler.QueryAbilitazione;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.EventoIterPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.GruppoVisibilita;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.StoricoVersioni;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.StoricoVersioni.InformazioniTaskFirma;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Utente;
import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.pratica.comunicazione.Comunicazione;
import it.eng.consolepec.xmlplugin.pratica.comunicazione.DatiComunicazione;
import it.eng.consolepec.xmlplugin.pratica.comunicazione.DatiComunicazione.Invio;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.ProtocollazionePEC;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Ricevuta;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.TipoRicevuta;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmail;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailDaEprotocollo;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Procedimento;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Protocollazione;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.NodoModulistica;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.NodoModulistica.TipoNodoModulistica;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.Sezione;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.TabellaModulo.Riga;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.ValoreModulo;
import it.eng.consolepec.xmlplugin.pratica.modulistica.PraticaModulistica;
import it.eng.consolepec.xmlplugin.pratica.template.AbstractTemplate;
import it.eng.consolepec.xmlplugin.pratica.template.DatiAbstractTemplate;
import it.eng.consolepec.xmlplugin.pratica.template.DatiTemplateDocumentoPDF;
import it.eng.consolepec.xmlplugin.pratica.template.DatiTemplateEmail;
import it.eng.consolepec.xmlplugin.pratica.template.TemplateDocumentoPDF;
import it.eng.consolepec.xmlplugin.pratica.template.TemplateEmail;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.GestioneComunicazioneTask;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.TipoApiTaskComunicazione;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.TaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask.Condivisione;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionemodulistica.GestionePraticaModulisticaTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.GestionePECInTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.GestionePECOutTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.GestioneAbstractTemplateTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.GestioneTemplateDocumentoPDFTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.GestioneTemplateEmailTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.TipoApiTaskTemplate;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaPECOutTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.ApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.DestinatarioGruppoRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.DestinatarioRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.DestinatarioUtenteRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.StatoRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;
import it.eng.portlet.consolepec.gwt.server.fascicolo.StatiComunicazioneTranslator;
import it.eng.portlet.consolepec.gwt.server.fascicolo.StatiFascicoloTranslator;
import it.eng.portlet.consolepec.gwt.server.fascicolo.StatiPraticaModulisticaTranslator;
import it.eng.portlet.consolepec.gwt.server.pec.StatiPecTranslator;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.action.pec.TipoRiferimentoPEC;
import it.eng.portlet.consolepec.gwt.shared.dto.CollegamentoDto;
import it.eng.portlet.consolepec.gwt.shared.dto.CondivisioneDto;
import it.eng.portlet.consolepec.gwt.shared.model.*;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.Stato;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.StoricoVersioniDTO;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.StoricoVersioniDTO.InformazioniCopiaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.StoricoVersioniDTO.InformazioniTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.TipologiaFirma;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO.StatoTemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.CampoTemplateDTO.TipoCampoTemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO.InvioComunicazioneDTO;
import it.eng.portlet.consolepec.gwt.shared.model.DatiTaskDTO.AssegnatarioDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoAllegato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoComunicazioneRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppo;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollatoCapofila;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPECRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPraticaModulisticaRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.StatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO.TipoPresaInCarico;
import it.eng.portlet.consolepec.gwt.shared.model.TabellaModuloDTO.RigaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ValoreModuloDTO.TipoValorModuloeDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioGruppoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioUtenteDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.ProponenteDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.StatoDestinatarioTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.StatoTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.TipoPropostaTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.richiedifirma.AllegatoRichiediFirmaDTO;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.GestioneTaskPratiche;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.IGestionePresaInCarico;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;

public class XMLPluginToDTOConverter {

	@Autowired
	private GestioneTaskPratiche gestioneTask;
	@Autowired
	private IGestionePresaInCarico gestionePresaInCarico;
	@Autowired
	private XMLPluginToDTOConverterUtil xmlPluginToDTOConverterUtil;
	@Autowired
	private GestioneConfigurazioni gestioneConfigurazioni;
	@Autowired
	private GestioneProfilazioneUtente gestioneProfilazioneUtente;

	@Autowired
	private PraticaSessionUtil praticaSessionUtil;

	private static final Logger logger = LoggerFactory.getLogger(XMLPluginToDTOConverter.class);

	public PraticaDTO praticaToDTO(Pratica<?> pratica) {
		if (pratica instanceof Fascicolo) {
			return fascicoloToDettaglio((Fascicolo) pratica);
		}
		if (pratica instanceof PraticaModulistica) {
			return praticaModulisticaToDettaglio((PraticaModulistica) pratica);
		}
		if (pratica instanceof PraticaEmailIn) {
			return emailToDettaglioIN((PraticaEmailIn) pratica);
		}
		if (pratica instanceof PraticaEmailDaEprotocollo) {
			return emailToDettaglioDaEprotocollo((PraticaEmailDaEprotocollo) pratica);
		}
		if (pratica instanceof PraticaEmailOut) {
			return emailToDettaglioOUT((PraticaEmailOut) pratica);
		}
		if (pratica instanceof AbstractTemplate) {
			return modelloToDettaglio((AbstractTemplate<?>) pratica);
		}
		if (pratica instanceof Comunicazione) {
			return comunicazioneToDettaglio((Comunicazione) pratica);
		}
		throw new IllegalArgumentException("Tipo di pratica sconoscita");
	}

	public FascicoloDTO fascicoloToDettaglio(Fascicolo fascicolo) {
		String path = fascicolo.getAlfrescoPath();
		path = Base64Utils.URLencodeAlfrescoPath(path);
		FascicoloDTO dto = null;
		dto = new FascicoloDTO(path);
		dto = fascicoloToDettaglio(fascicolo, dto);
		return dto;
	}

	public PecOutDTO emailToDettaglioOUT(PraticaEmailOut email) {
		String path = Base64Utils.URLencodeAlfrescoPath(email.getAlfrescoPath());
		PecOutDTO dettaglio = new PecOutDTO(path);

		gestioneTipoPratica(dettaglio, email.getDati().getTipo().getNomeTipologia(), email.getDati().getMittente(), email.getDati().getStato());
		emailToDettaglio(email, dettaglio);

		AnagraficaEmailOut af = null;
		if (!email.getDati().getStato().equals(it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato.BOZZA)) {
			af = gestioneConfigurazioni.getAnagraficaMailInUscita(email.getDati().getTipo().getNomeTipologia(), email.getDati().getMittente());
		}

		dettaglio.setProtocollabile(email.isProtocollaAbilitato() && ((af != null && af.isProtocollabile()) || af == null));

		/**
		 * Traduzione dell'enum del pluginXML in enum del dto
		 */
		dettaglio.setStato(StatiPecTranslator.getStatoPecOutDTOFromStato(email.getDati().getStato()));
		dettaglio.setStatoLabel(dettaglio.getStato().getLabel());

		GestionePECOutTask taskCorrente = gestioneTask.estraiTaskCorrente(email, GestionePECOutTask.class, gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());
		RiattivaPECOutTask taskRiattivazione = gestioneTask.estraiTaskRiattivazione(email);

		boolean isAssegnatarioRiattivazione = false;
		boolean isAssegnatario = false;

		if (taskCorrente != null) {
			isAssegnatario = gestioneProfilazioneUtente.getDatiUtente().getRuoli().contains(taskCorrente.getDati().getAssegnatario().getNome());
		} else if (taskRiattivazione != null) {
			isAssegnatarioRiattivazione = gestioneProfilazioneUtente.getDatiUtente().getRuoli().contains(taskRiattivazione.getDati().getAssegnatario().getNome());
		}

		dettaglio.setLetto(email.getDati().isLetto() && taskCorrente != null);
		dettaglio.setUtenteAssegnatario(taskCorrente != null);
		dettaglio.setInviata(email.isInviata());
		dettaglio.setReinoltro(email.isReinoltro());
		dettaglio.setMessageIdReinoltro(email.getDati().getMessageIDReinoltro());
		dettaglio.setReinoltrabile(email.isReinoltrabile() && (isAssegnatario || isAssegnatarioRiattivazione));
		dettaglio.setRicevuteConsegna(email.getDati().isRicevutaConsegna() && (isAssegnatario || isAssegnatarioRiattivazione));
		dettaglio.setFirma(xmlPluginToDTOConverterUtil.convertToSafeHtml(email.getDati().getFirma()));
		dettaglio.setDataInvio(email.getDati().getDataInvio() != null ? DateUtils.DATEFORMAT_DATEH.format(email.getDati().getDataInvio()) : "");
		dettaglio.setInteroperabile(email.isEmailInteroperabile());
		if (email.isEmailInteroperabile()) {
			dettaglio.setIdentificativoAllegatoPrincipale(email.getDati().getInteroperabile().getNomeAllegatoPrincipale());
		}

		gestionePresaInCarico.caricaPresaInCarico(email, dettaglio);

		dettaglio.setAzioniDisabilitate(email.isInviata() || dettaglio.getTipoPresaInCarico().equals(TipoPresaInCarico.ALTRO_UTENTE) || taskCorrente == null);

		if (taskCorrente != null && !dettaglio.getTipoPresaInCarico().equals(TipoPresaInCarico.ALTRO_UTENTE)) {
			dettaglio.setFirmaAllegatoAbilitato(isAssegnatario || gestioneProfilazioneUtente.getDatiUtente().isUtenteEsterno());
			dettaglio.setEliminaAllegatoAbilitato(isAssegnatario || gestioneProfilazioneUtente.getDatiUtente().isUtenteEsterno());
			dettaglio.setEliminaAbilitato(isAssegnatario || gestioneProfilazioneUtente.getDatiUtente().isUtenteEsterno());
			dettaglio.setCaricaAllegatoAbilitato(isAssegnatario || gestioneProfilazioneUtente.getDatiUtente().isUtenteEsterno());
			dettaglio.setCaricaAllegatoDaPraticaAbilitato(isAssegnatario || gestioneProfilazioneUtente.getDatiUtente().isUtenteEsterno());
			dettaglio.setInviaAbilitato(isAssegnatario || gestioneProfilazioneUtente.getDatiUtente().isUtenteEsterno());
			dettaglio.setAssegnatario(gestioneConfigurazioni.getAnagraficaRuolo((taskCorrente.getDati().getAssegnatario().getNome())).getEtichetta());

		} else {
			AnagraficaRuolo ass = gestioneConfigurazioni.getAnagraficaRuolo(email.getDati().getAssegnatarioCorrente());
			dettaglio.setFirmaAllegatoAbilitato(false);
			dettaglio.setEliminaAllegatoAbilitato(false);
			dettaglio.setEliminaAbilitato(false);
			dettaglio.setCaricaAllegatoAbilitato(false);
			dettaglio.setCaricaAllegatoDaPraticaAbilitato(false);
			dettaglio.setInviaAbilitato(false);
			dettaglio.setAssegnatario(ass != null ? ass.getEtichetta() : null);
		}

		for (Ricevuta ric : email.getDati().getRicevute()) {
			TipoRicevuta tipo = ric.getTipo();
			if (TipoRicevuta.ERRORE_CONSEGNA.equals(tipo) || TipoRicevuta.NON_ACCETTAZIONE.equals(tipo) || TipoRicevuta.PREAVVISO_ERRORE_CONSEGNA.equals(tipo)) {
				RicevuteErroreDTO re = new RicevuteErroreDTO();
				re.setDest(ric.getConsegna());
				re.setErrMsg(ric.getErroreEsteso());
				dettaglio.getRicevuteErrore().add(re);
			}
		}

		return dettaglio;
	}

	/*
	 * da ritornare sull gui : oggetto,allegati, body (estrazione) , id documento email.getUuid()
	 */

	public PecInDTO emailToDettaglioIN(PraticaEmailIn email) {

		String path = Base64Utils.URLencodeAlfrescoPath(email.getAlfrescoPath());
		PecInDTO dettaglio = new PecInDTO(path);

		String destinatario = null;

		if (email.getDati().getTipo().getNomeTipologia().equals(TipologiaPratica.EMAIL_IN.getNomeTipologia())) {

			if (email.getDati().getDestinatariInoltro() != null && !email.getDati().getDestinatariInoltro().isEmpty()) {
				for (String dest : email.getDati().getDestinatariInoltro()) {
					if (gestioneConfigurazioni.getAnagraficaIngresso(TipologiaPratica.EMAIL_IN.getNomeTipologia(), dest) != null) {
						destinatario = dest;
					}
				}

			} else if (email.getDati().getDestinatarioPrincipale() != null && email.getDati().getDestinatarioPrincipale().getDestinatario() != null) {
				if (gestioneConfigurazioni.getAnagraficaIngresso(TipologiaPratica.EMAIL_IN.getNomeTipologia(), email.getDati().getDestinatarioPrincipale().getDestinatario()) != null) {
					destinatario = email.getDati().getDestinatarioPrincipale().getDestinatario();
				}

			} else if (email.getDati().getDestinatari() != null && !email.getDati().getDestinatari().isEmpty()) {
				for (Destinatario dest : email.getDati().getDestinatari()) {
					if (gestioneConfigurazioni.getAnagraficaIngresso(TipologiaPratica.EMAIL_IN.getNomeTipologia(), dest.getDestinatario()) != null) {
						destinatario = dest.getDestinatario();
					}
				}

			}

		} else if (email.getDati().getTipo().getNomeTipologia().equals(TipologiaPratica.EMAIL_EPROTOCOLLO.getNomeTipologia())) {
			for (String dest : email.getDati().getDestinatariInoltro()) {
				if (gestioneConfigurazioni.getAnagraficaIngresso(TipologiaPratica.EMAIL_EPROTOCOLLO.getNomeTipologia(), dest) != null) {
					destinatario = dest;
				}
			}
		}

		if (destinatario == null) {
			throw new IllegalArgumentException("Anagrafica non trovata per mail " + email.getDati().getIdDocumentale());
		}

		AnagraficaIngresso af = gestioneConfigurazioni.getAnagraficaIngresso(email.getDati().getTipo().getNomeTipologia(), destinatario);
		gestioneTipoPratica(dettaglio, email.getDati().getTipo().getNomeTipologia(), destinatario);
		emailToDettaglio(email, dettaglio);
		dettaglio.setDataOraArrivo(email.getDati().getDataRicezione() != null ? DateUtils.DATEFORMAT_DATEH.format(email.getDati().getDataRicezione()) : "");

		dettaglio.setProtocollabile(email.isProtocollaAbilitato() && af != null && af.isProtocollabile());
		/**
		 * Traduzione dell'enum del pluginXML in enum del dto
		 */
		dettaglio.setStato(StatiPecTranslator.getStatoPecInDTOFromStato(email.getDati().getStato()));
		dettaglio.setStatoLabel(email.getDati().getStato().getLabel());
		/* attivazione pulsantiera */
		gestionePresaInCarico.caricaPresaInCarico(email, dettaglio);
		GestionePECInTask task = gestioneTask.estraiTaskCorrente(email, GestionePECInTask.class, gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());
		dettaglio.setUtenteAssegnatario(task != null);
		dettaglio.setLetto(email.getDati().isLetto());
		dettaglio.setInoltratoDaEProtocollo(email.getDati().getDestinatariInoltro().size() > 0);

		if (task != null && !dettaglio.getTipoPresaInCarico().equals(TipoPresaInCarico.ALTRO_UTENTE)) {

			dettaglio.setArchiviabile(gestioneProfilazioneUtente.isOperazioneAbilitata(email.getDati().getTipo(), task, TipoApiTaskPEC.ARCHIVIA, //
					new ArrayList<>(email.getDati().getOperazioniSuperUtente()), //
					email.getDati().getAssegnazioneEsterna() != null
							? email.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(email.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null : null,
					null, null, destinatario));

			dettaglio.setEliminabile(gestioneProfilazioneUtente.isOperazioneAbilitata(email.getDati().getTipo(), task, TipoApiTaskPEC.ELIMINA, //
					new ArrayList<>(email.getDati().getOperazioniSuperUtente()), //
					email.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(email.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null, //
					null, null, destinatario));

			dettaglio.setCreaFascicoloAbilitato(gestioneProfilazioneUtente.isOperazioneAbilitata(email.getDati().getTipo(), task, TipoApiTaskPEC.CREA_FASCICOLO, //
					new ArrayList<>(email.getDati().getOperazioniSuperUtente()), //
					email.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(email.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null, //
					null, null, destinatario) && gestioneProfilazioneUtente.getAutorizzazioniUtente().isAbilitato(CreazioneFascicoloAbilitazione.class));

			dettaglio.setRiassegnaAbilitato(gestioneProfilazioneUtente.isOperazioneAbilitata(email.getDati().getTipo(), task, TipoApiTaskPEC.RIASSEGNA, //
					new ArrayList<>(email.getDati().getOperazioniSuperUtente()), //
					email.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(email.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null, //
					null, null, destinatario));

			dettaglio.setAgganciaFascicoloAbilitato(gestioneProfilazioneUtente.isOperazioneAbilitata(email.getDati().getTipo(), task, TipoApiTaskPEC.AGGANCIA_A_FASCICOLO, //
					new ArrayList<>(email.getDati().getOperazioniSuperUtente()), //
					email.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(email.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null, //
					null, null, destinatario));

			dettaglio.setRiportaInLetturaAbilitato(gestioneProfilazioneUtente.isOperazioneAbilitata(email.getDati().getTipo(), task, TipoApiTaskPEC.RIPORTA_IN_LETTURA, //
					new ArrayList<>(email.getDati().getOperazioniSuperUtente()), //
					email.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(email.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null, //
					null, null, destinatario));

			dettaglio.setModificaOperatoreAbilitato(gestioneProfilazioneUtente.isOperazioneAbilitata(email.getDati().getTipo(), task, TipoApiTaskPEC.MODIFICA_OPERATORE, //
					new ArrayList<>(email.getDati().getOperazioniSuperUtente()), //
					email.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(email.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null, //
					null, null, destinatario));

			boolean isElettorale = gestioneProfilazioneUtente.isOperazioneAbilitata(email.getDati().getTipo(), task, TipoApiTaskPEC.IMPORTA_ELETTORALE, //
					new ArrayList<>(email.getDati().getOperazioniSuperUtente()), //
					email.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(email.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null, //
					null, null, destinatario) && isUtenteAbilitatoPerElettorale();
			dettaglio.setImportaElettoraleAbilitato(isElettorale);

			dettaglio.setSalvaNote(gestioneProfilazioneUtente.isOperazioneAbilitata(email.getDati().getTipo(), task, TipoApiTaskPEC.MODIFICA_NOTE, //
					new ArrayList<>(email.getDati().getOperazioniSuperUtente()), //
					email.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(email.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null, //
					null, null, destinatario));

			dettaglio.setAssegnatario(gestioneConfigurazioni.getAnagraficaRuolo(task.getDati().getAssegnatario().getNome()).getEtichetta());

		} else {
			AnagraficaRuolo ass = gestioneConfigurazioni.getAnagraficaRuolo(email.getDati().getAssegnatarioCorrente());
			dettaglio.setAssegnatario(ass != null ? ass.getEtichetta() : null);
			dettaglio.setArchiviabile(false);
			dettaglio.setEliminabile(false);
			dettaglio.setRiassegnaAbilitato(false);
			dettaglio.setAgganciaFascicoloAbilitato(false);
			dettaglio.setCreaFascicoloAbilitato(false);
			dettaglio.setSalvaNote(false);
			dettaglio.setRiportaInLetturaAbilitato(false);
			dettaglio.setModificaOperatoreAbilitato(false);
			dettaglio.setImportaElettoraleAbilitato(false);

		}

		/*
		 * L'abilitazione del riporta in gestione della pratica viene ricavata dalla pratica e non dal task.
		 */

		dettaglio.setRiportaInGestioneAbilitato(email.isRiattivabile());

		if (email.getDati().getInteroperabile() != null) {
			dettaglio.setInteroperabile(true);
		}

		dettaglio.setAnnullaElettoraleAbilitato(email.isRiattivabileElettorale() && isUtenteAbilitatoPerElettorale());

		return dettaglio;
	}

	private boolean isUtenteAbilitatoPerElettorale() {
		return isUtenteAbilitatoPerElettoraleCreazione() || isUtenteAbilitatoPerElettoraleModifica();
	}

	private boolean isUtenteAbilitatoPerElettoraleCreazione() {
		QueryAbilitazione<CreazioneFascicoloAbilitazione> qa = new QueryAbilitazione<>();
		for (final TipologiaPratica tp : PraticaUtil.getTipologieElettorale()) {
			qa.addCondition(new CondizioneAbilitazione<CreazioneFascicoloAbilitazione>() {
				@Override
				public boolean valutaCondizione(CreazioneFascicoloAbilitazione abilitazione) {
					return abilitazione.getTipo().equals(tp.getNomeTipologia());
				}
			});
		}
		return gestioneProfilazioneUtente.getAutorizzazioniUtente().isAbilitato(CreazioneFascicoloAbilitazione.class, qa);
	}

	private boolean isUtenteAbilitatoPerElettoraleModifica() {
		QueryAbilitazione<CreazioneFascicoloAbilitazione> qa = new QueryAbilitazione<>();
		for (final TipologiaPratica tp : PraticaUtil.getTipologieElettorale()) {
			qa.addCondition(new CondizioneAbilitazione<CreazioneFascicoloAbilitazione>() {
				@Override
				public boolean valutaCondizione(CreazioneFascicoloAbilitazione abilitazione) {
					return abilitazione.getTipo().equals(tp.getNomeTipologia());
				}
			});
		}
		return gestioneProfilazioneUtente.getAutorizzazioniUtente().isAbilitato(CreazioneFascicoloAbilitazione.class, qa);
	}

	public PecInDTO emailToDettaglioDaEprotocollo(PraticaEmailDaEprotocollo email) {
		return emailToDettaglioIN(email);
	}

	protected PecDTO emailToDettaglio(PraticaEmail email, PecDTO dettaglio) {
		DatiEmail dati = email.getDati();

		if (dati.getDestinatarioPrincipale() != null) {
			dettaglio.setDestinatarioPrincipale(xmlPluginToDTOConverterUtil.getDestinatario(dati.getDestinatarioPrincipale()));
		}
		dettaglio.setTitolo(dati.getOggetto());
		dettaglio.setBody(xmlPluginToDTOConverterUtil.convertToSafeHtml(dati.getBody()));
		dettaglio.setMailId(dati.getMessageID());
		dettaglio.setDataOraCreazione(DateUtils.DATEFORMAT_DATEH.format(dati.getDataCreazione()));
		dettaglio.setUtenteCreazione(dati.getUtenteCreazione());
		dettaglio.setUsernameCreazione(dati.getUsernameCreazione());
		dettaglio.setTipoEmail(dati.getTipoEmail().toString());
		dettaglio.setMittente(dati.getMittente());
		dettaglio.setReplyTo(dati.getReplyTo());

		xmlPluginToDTOConverterUtil.popolaVisibilita(dettaglio, dati.getGruppiVisibilita());
		if (email.hasPraticheCollegate()) {
			for (PraticaCollegata pc : email.getAllPraticheCollegate()) {
				String fascicoloID = Base64Utils.URLencodeAlfrescoPath(pc.getAlfrescoPath());
				dettaglio.getIdPraticheCollegate().add(fascicoloID);
			}

		} else {
			logger.debug("Attenzione: fascicolo collegato non trovato nella pratica :" + email);
		}

		dettaglio.setNote(dati.getNote());

		if (dati.getProtocollazionePec() != null) {
			ProtocollazionePEC protocollazionePec = dati.getProtocollazionePec();
			dettaglio.setAnnoPG(protocollazionePec.getAnnoPG() + "");
			dettaglio.setNumeroPG(protocollazionePec.getNumeroPG());

			if (protocollazionePec.getCapofila() != null && protocollazionePec.getCapofila().getNumeroPG() != null) {
				dettaglio.setAnnoPGCapofila(String.valueOf(protocollazionePec.getCapofila().getAnnoPG()));
				dettaglio.setNumeroPGCapofila(protocollazionePec.getCapofila().getNumeroPG());
			}

		}

		dettaglio.setNumeroRepertorio(dati.getIdDocumentale());

		/* destinatari */
		TreeSet<it.eng.portlet.consolepec.gwt.shared.model.Destinatario> destinatari = new TreeSet<it.eng.portlet.consolepec.gwt.shared.model.Destinatario>();

		if (dati.getDestinatarioPrincipale() != null) {
			destinatari.add(xmlPluginToDTOConverterUtil.getDestinatario(dati.getDestinatarioPrincipale()));
		}

		if (dati.getDestinatari() != null) {
			for (Destinatario dest : dati.getDestinatari()) {
				destinatari.add(xmlPluginToDTOConverterUtil.getDestinatario(dest));
			}
		}

		dettaglio.setDestinatari(destinatari);
		/* destinatariCC */
		/* TODO temporaneo!! */
		TreeSet<it.eng.portlet.consolepec.gwt.shared.model.Destinatario> destinatariCC = new TreeSet<it.eng.portlet.consolepec.gwt.shared.model.Destinatario>();
		if (dati.getDestinatariCC() != null) {
			for (Destinatario dCc : dati.getDestinatariCC()) {
				destinatariCC.add(xmlPluginToDTOConverterUtil.getDestinatario(dCc));
			}
		}
		dettaglio.setDestinatariCC(destinatariCC);

		/* allegati */
		List<AllegatoDTO> allegati = new ArrayList<AllegatoDTO>();
		for (Allegato a : dati.getAllegati()) {
			AllegatoDTO allegato = new AllegatoDTO(a.getNome(), a.getFolderOriginPath(), a.getFolderOriginName(), dettaglio.getClientID(), a.getCurrentVersion());
			// verificaFirmaAllegato(allegato, email);

			Stato stato = (a.getFirmato()) ? Stato.FIRMATO : Stato.NONFIRMATO;
			allegato.setStato(stato);
			allegato.setFirmatoHash(a.getFirmatoHash());

			if (a.getFirmato() || a.getFirmatoHash()) {
				TipologiaFirma tf = TipologiaFirma.getTipologiaFirma(a.getTipoFirma().toString());
				allegato.setTipologiaFirma(tf == null ? TipologiaFirma.NONFIRMATO : tf);
			} else {
				allegato.setTipologiaFirma(TipologiaFirma.NONFIRMATO);
			}

			allegato.setLock(a.getLock());
			allegato.setLockedBy(a.getLockedBy());
			allegato.setDataCaricamento(a.getDataCaricamento());
			allegato.setOggetto(a.getOggettoDocumento());
			popolaStoricoVersioniAllegato(a, allegato, null);

			allegato.getDatiAggiuntivi().addAll(a.getDatiAggiuntivi());

			allegati.add(allegato);
		}

		dettaglio.setAllegati(allegati);

		/* azioni */
		for (EventoIterPratica evento : email.getDati().getIter()) {
			dettaglio.getEventiIterDTO().add(new EventoIterDTO(evento.getTestoEvento(), DateUtils.DATEFORMAT_DATEH.format(evento.getDataEvento())));
		}

		for (String destInoltro : dati.getDestinatariInoltro()) {
			dettaglio.getDestinatariInoltro().add(destInoltro);
		}
		if (dati.getProgressivoInoltro() != null) {
			dettaglio.setProgressivoInoltro(new BigInteger(dati.getProgressivoInoltro().toString()));
		}

		if (dati.getAssegnazioneEsterna() != null) {
			dettaglio.setAssegnazioneEsterna(new AssegnazioneEsternaDTO());
			dettaglio.getAssegnazioneEsterna().getDestinatari().addAll(dati.getAssegnazioneEsterna().getDestinatari());
			dettaglio.getAssegnazioneEsterna().setTestoMail(dati.getAssegnazioneEsterna().getTestoMail());
			for (Operazione operazione : dati.getAssegnazioneEsterna().getOperazioniConsentite()) {
				dettaglio.getAssegnazioneEsterna().getOperazioni().add(operazione.getNomeOperazione());
			}
			if (dati.getAssegnazioneEsterna().getDataNotifica() != null) {
				dettaglio.getAssegnazioneEsterna().setDataNotifica(DateUtils.DATEFORMAT_DATEH.format(dati.getAssegnazioneEsterna().getDataNotifica()));
			}

		}
		for (Operazione o : email.getDati().getOperazioniAssegnaUtenteEsterno()) {
			dettaglio.getOperazioniAssegnaUtenteEsterno().add(o.getNomeOperazione());
		}

		if (email.getDati().getOperatore().getNome() != null) {
			dettaglio.setOperatore(email.getDati().getOperatore().getNome());
		}

		return dettaglio;
	}

	private FascicoloDTO fascicoloToDettaglio(Fascicolo fascicolo, FascicoloDTO dto) {

		String path = fascicolo.getAlfrescoPath();
		path = Base64Utils.URLencodeAlfrescoPath(path);

		DatiFascicolo dati = fascicolo.getDati();
		dto.setDataOraCreazione(DateUtils.DATEFORMAT_DATEH.format(dati.getDataCreazione()));

		gestioneTipoPratica(dto, dati.getTipo().getNomeTipologia());
		AnagraficaFascicolo af = gestioneConfigurazioni.getAnagraficaFascicolo(dati.getTipo().getNomeTipologia());

		if (fascicolo.getInCaricoA() != null) {
			Utente inCaricoA = fascicolo.getInCaricoA();
			dto.setInCaricoALabel(gestionePresaInCarico.getInCaricoALabel(inCaricoA));
			dto.setInCaricoAUserName(inCaricoA.getUsername());
		}
		gestionePresaInCarico.caricaPresaInCarico(fascicolo, dto);
		if (dati.getProtocollazioniCapofila() != null && dati.getProtocollazioniCapofila().size() > 0) {
			List<ProtocollazioneCapofila> protocollazioniCapofila = dati.getProtocollazioniCapofila();
			Collections.sort(protocollazioniCapofila);
			ProtocollazioneCapofila pc = dati.getProtocollazioniCapofila().get(0);
			if (pc.isFromBa01() || (pc.getAllegatiProtocollati().isEmpty() && pc.getPraticheCollegateProtocollate().isEmpty())) {
				List<Protocollazione> protocollazioniCollegate = pc.getProtocollazioniCollegate();
				Collections.sort(protocollazioniCollegate);
				Protocollazione protocollazione = pc.getProtocollazioniCollegate().get(0);
				dto.setAnnoPG(protocollazione.getAnnoPG() + "");
				dto.setNumeroPG(protocollazione.getNumeroPG());
				dto.setNumeroFascicolo(protocollazione.getNumeroFascicolo());
			} else {
				dto.setAnnoPG(pc.getAnnoPG() + "");
				dto.setNumeroPG(pc.getNumeroPG());
				dto.setNumeroFascicolo(pc.getNumeroFascicolo());
			}
		}

		for (EventoIterPratica evento : fascicolo.getDati().getIter()) {
			dto.getEventiIterDTO().add(new EventoIterDTO(evento.getTestoEvento(), DateUtils.DATEFORMAT_DATEH.format(evento.getDataEvento())));
		}

		dto.setNote(dati.getNote());
		dto.setNumeroRepertorio(dati.getIdDocumentale());
		if (dati.getProvenienza() != null) {
			if (gestioneConfigurazioni.getAnagraficaRuolo(dati.getProvenienza()) != null) {
				dto.setProvenienza(gestioneConfigurazioni.getAnagraficaRuolo(dati.getProvenienza()).getEtichetta());
			}
		}
		dto.setUtenteCreazione(dati.getUtenteCreazione());
		dto.setUsernameCreazione(dati.getUsernameCreazione());
		dto.setStato(StatiFascicoloTranslator.getStatoDTOFromStato(dati.getStato()));
		dto.setStatoLabel(dto.getStato().getLabel());
		dto.setTitolo(dati.getTitolo());
		dto.setChiudiAbilitato(!gestioneProfilazioneUtente.getDatiUtente().isUtenteEsterno());
		dto.setTitoloOriginale(dati.getTitoloOriginale());
		dto.getIdPraticheProcedi().addAll(dati.getIdPraticheProcedi());

		if (dati.getAssegnazioneEsterna() != null) {
			dto.setAssegnazioneEsterna(new AssegnazioneEsternaDTO());
			dto.getAssegnazioneEsterna().getDestinatari().addAll(dati.getAssegnazioneEsterna().getDestinatari());
			dto.getAssegnazioneEsterna().setTestoMail(dati.getAssegnazioneEsterna().getTestoMail());
			for (Operazione operazione : dati.getAssegnazioneEsterna().getOperazioniConsentite()) {
				dto.getAssegnazioneEsterna().getOperazioni().add(operazione.getNomeOperazione());
			}
			if (dati.getAssegnazioneEsterna().getDataNotifica() != null) {
				dto.getAssegnazioneEsterna().setDataNotifica(DateUtils.DATEFORMAT_DATEH.format(dati.getAssegnazioneEsterna().getDataNotifica()));
			}

		}

		for (Operazione o : fascicolo.getDati().getOperazioniAssegnaUtenteEsterno()) {
			dto.getOperazioniAssegnaUtenteEsterno().add(o.getNomeOperazione());
		}

		xmlPluginToDTOConverterUtil.popolaVisibilita(dto, dati.getGruppiVisibilita());

		for (Operazione operazione : fascicolo.getDati().getOperazioni()) {
			dto.getOperazioni().add(operazione.getNomeOperazione());
		}

		Task<?> taskC = gestioneTask.estraiTaskCorrente(fascicolo);
		if (taskC != null && taskC instanceof TaskFascicolo<?>) {
			TreeSet<Condivisione> condivisioni = ((DatiGestioneFascicoloTask) taskC.getDati()).getCondivisioni();
			for (Condivisione condivisione : condivisioni) {
				CondivisioneDto condivisioneDTO = new CondivisioneDto();
				condivisioneDTO.setRuolo(gestioneConfigurazioni.getAnagraficaRuolo(condivisione.getNomeGruppo()));
				for (Operazione operazione : condivisione.getOperazioni()) {
					condivisioneDTO.getOperazioni().add(operazione.getNomeOperazione());
				}
				dto.getCondivisioni().add(condivisioneDTO);
			}
		}

		TaskFascicolo<?> taskCurrentUser = null;
		Task<?> taskCorrente = gestioneTask.estraiTaskCorrente(fascicolo, gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());

		xmlPluginToDTOConverterUtil.popolaCollegamenti(dto, dati.getCollegamenti());

		dto.setUtenteAssegnatario(taskCorrente != null);
		dto.setLetto(dati.isLetto() && (taskCorrente != null));/* letto conta solo per chi ha la pratica in worklist */
		/* distinzione qualora ci fosse solo il task di riattivazione */

		if (fascicolo.getDati().getOperativitaRidotta() != null) {
			dto.setOperativitaRidotta(fascicolo.getDati().getOperativitaRidotta());
		}

		if ((taskCorrente != null && taskCorrente instanceof TaskFascicolo<?>) && !dto.getTipoPresaInCarico().equals(TipoPresaInCarico.ALTRO_UTENTE)) {

			taskCurrentUser = (TaskFascicolo<?>) taskCorrente;
			dto.setAssegnatario(gestioneConfigurazioni.getAnagraficaRuolo(taskCurrentUser.getDati().getAssegnatario().getNome()).getEtichetta());

			dto.setCancellaAllegato(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), //
					taskCurrentUser, TipoApiTask.RIMUOVI_ALLEGATO, //
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()), //
					fascicolo.getDati().getAssegnazioneEsterna() != null
							? fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null : null, //
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setCaricaAllegato(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), //
					taskCurrentUser, TipoApiTask.AGGIUNGI_ALLEGATO, //
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()), //
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null, //
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setConcludi(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), //
					taskCurrentUser, TipoApiTask.CONCLUDI_FASCICOLO, //
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()), //
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null, //
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setEliminaFascicoloAbilitato(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), //
					taskCurrentUser, TipoApiTask.ELIMINA_FASCICOLO, //
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()), //
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null, //
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setAffissioneAbilitato(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), //
					taskCurrentUser, TipoApiTask.METTI_IN_AFFISSIONE, //
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()), //
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null, //
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setRiassegna(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), //
					taskCurrentUser, TipoApiTask.RIASSEGNA, //
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()), //
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null, //
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));

			dto.setRispondi(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), //
					taskCurrentUser, TipoApiTask.RISPONDI_MAIL, //
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()), //
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null, //
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null) //
					&& gestioneProfilazioneUtente.getAutorizzazioniUtente().isAbilitato(CreazioneEmailOutAbilitazione.class));

			dto.setCreaBozzaAbilitato(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), //
					taskCurrentUser, TipoApiTask.CREA_BOZZA, //
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()), //
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null, //
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null) //
					&& gestioneProfilazioneUtente.getAutorizzazioniUtente().isAbilitato(CreazioneEmailOutAbilitazione.class));

			dto.setModificaVisibilitaFascicoloAbilitato(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.CAMBIA_VISIBILITA_FASCICOLO,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));

			dto.setModificaVisibilitaAllegatoAbilitato(

					gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.CAMBIA_VISIBILITA_ALLEGATO,
							new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
							fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
							new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));

			dto.setFirmaAllegato(
					gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.FIRMA, new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
							fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
							new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setPubblicazioneAbilitata(
					gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.PUBBLICA, new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
							fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
							new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setRimozionePubblicazioneAbilitata(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.RIMUOVI_PUBBLICAZIONE,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setCollegamentoAbilitato(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.COLLEGA_FASCICOLO,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setRimozioneCollegamentoAbilitato(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.ELIMINA_COLLEGAMENTO_FASCICOLO,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setCondivisioneAbilitata(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.CONDIVIDI_FASCICOLO,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setRimozioneCondivisioneAbilitata(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.ELIMINA_CONDIVISIONE_FASCICOLO,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setProtocolla(fascicolo.isProtocollaAbilitato() && af.isProtocollabile()
					&& gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.PROTOCOLLAZIONE,
							new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
							fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
							new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setAvviaProcedimento(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.AVVIA_PROCEDIMENTO,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setPropostaFirmaAbilitato(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.RICHIESTA_APPROVAZIONE_FIRMA,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setChiudiProcedimento(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.CHIUDI_PROCEDIMENTO,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setSganciaPecIn(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.SGANCIA_PEC_IN,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setCambiaTipologiaFascicolo(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.CAMBIA_TIPO_FASCICOLO,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setRiportaInLettura(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.RIPORTA_IN_LETTURA,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setAssegnaUtenteEsterno(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.ASSEGNA_UTENTE_ESTERNO,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setModificaAbilitazioniAssegnaUtenteEsterno(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser,
					TipoApiTask.MODIFICA_ABILITAZIONI_ASSEGNA_UTENTE_ESTERNO, new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setRitornaDaInoltrareEsterno(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.RITORNA_DA_INOLTRARE_ESTERNO,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setNuovaEmailDaTemplate(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.RISPONDI_MAIL_DA_TEMPLATE,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null)
					&& gestioneProfilazioneUtente.getAutorizzazioniUtente().isAbilitato(CreazioneEmailOutAbilitazione.class));
			dto.setCambiaStepIter(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.CAMBIA_STEP_ITER,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setModificaOperatore(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.MODIFICA_OPERATORE,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setModificaDatiAggiuntivi(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.MODIFICA_DATO_AGGIUNTIVO,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setSalvaNote(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.MODIFICA_NOTE,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));

			dto.setGestionePresaInCarico(xmlPluginToDTOConverterUtil.isAssegnatario(taskCurrentUser) && !dto.getStato().equals(StatoDTO.DA_INOLTRARE_ESTERNO));
			dto.setRitiroPropostaFirmaEnabled(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.RITIRO_APPROVAZIONE_FIRMA,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setNuovoPdfDaTemplate(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.CREA_PDF_DA_TEMPLATE,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setAggiornaPG(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.AGGIORNA_PG,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setEstraiEML(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.ESTRAI_EML_PEC,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));
			dto.setModificaFascicolo(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.MODIFICA_FASCICOLO,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));

			dto.setModificaTagAbilitata(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.MODIFICA_TIPOLOGIE_ALLEGATO,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));

			dto.setAccessoVersioniPrecedentiAllegati(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.ESTRAI_VERSIONI_PRECEDENTI_ALLEGATI,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null, true));

			dto.setSpostaAllegati( //
					gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.TAGLIA_ALLEGATI,
							new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
							fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
							new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));

			dto.setSpostaProtocollazioni( //
					gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.TAGLIA_PROTOCOLLAZIONI,
							new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
							fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
							new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));

			dto.setInviaDaCsvAbilitato( //
					gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.INVIA_MAIL_DA_CSV,
							new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
							fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
							new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));

			QueryAbilitazione<ModificaFascicoloAbilitazione> qab = new QueryAbilitazione<ModificaFascicoloAbilitazione>();
			qab.addCondition(new CondizioneAbilitazione<ModificaFascicoloAbilitazione>() {

				@Override
				protected boolean valutaCondizione(ModificaFascicoloAbilitazione abilitazione) {
					return abilitazione.getTipo().equals(TipologiaPratica.PRATICA_PROCEDI.getNomeTipologia());
				}

			});

			dto.setCollegaPraticaProcedi(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.COLLEGA_PRATICA_PROCEDI,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null)
					&& gestioneProfilazioneUtente.getAutorizzazioniUtente().isAbilitato(ModificaFascicoloAbilitazione.class, qab));

			dto.setEmissionePermesso(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCurrentUser, TipoApiTask.EMISSIONE_PERMESSO,
					new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()),
					fascicolo.getDati().getAssegnazioneEsterna() != null ? new ArrayList<>(fascicolo.getDati().getAssegnazioneEsterna().getOperazioniConsentite()) : null,
					new ArrayList<>(fascicolo.getDati().getCollegamenti()), fascicolo.getDati().getOperativitaRidotta(), null));

		} else {
			AnagraficaRuolo ass = gestioneConfigurazioni.getAnagraficaRuolo(fascicolo.getDati().getAssegnatarioCorrente());
			dto.setAssegnatario(ass != null ? ass.getEtichetta() : null);
			dto.setCancellaAllegato(false);
			dto.setCaricaAllegato(false);
			dto.setConcludi(false);
			dto.setProtocolla(false);
			dto.setRiassegna(false);
			dto.setRispondi(false);
			dto.setSalvaNote(false);
			dto.setEliminaFascicoloAbilitato(false);
			dto.setGestionePresaInCarico((taskCorrente != null && taskCorrente instanceof TaskFascicolo<?>) && ((TaskFascicolo<?>) taskCorrente).isGestionePresaInCaricoAbilitata());
			dto.setAffissioneAbilitato(false);
			dto.setModificaVisibilitaAllegatoAbilitato(false);
			dto.setModificaVisibilitaFascicoloAbilitato(false);
			dto.setPubblicazioneAbilitata(false);
			dto.setRimozionePubblicazioneAbilitata(false);
			dto.setCollegamentoAbilitato(false);
			dto.setRimozioneCollegamentoAbilitato(false);
			dto.setCondivisioneAbilitata(false);
			dto.setRimozioneCondivisioneAbilitata(false);
			dto.setPropostaFirmaAbilitato(false);
			dto.setAvviaProcedimento(false);
			dto.setChiudiProcedimento(false);
			dto.setCambiaTipologiaFascicolo(false);
			dto.setAssegnaUtenteEsterno(false);
			dto.setModificaAbilitazioniAssegnaUtenteEsterno(false);
			dto.setRitornaDaInoltrareEsterno(false);
			dto.setNuovaEmailDaTemplate(false);
			dto.setCambiaStepIter(false);
			dto.setModificaOperatore(false);
			dto.setModificaDatiAggiuntivi(false);
			dto.setRitiroPropostaFirmaEnabled(false);
			dto.setAggiornaPG(false);
			dto.setEstraiEML(false);
			dto.setModificaFascicolo(false);
			dto.setCollegaPraticaProcedi(false);
			dto.setEmissionePermesso(false);
			dto.setAccessoVersioniPrecedentiAllegati(ass != null
					? gestioneProfilazioneUtente.checkOperazioneVisibilita(dto.getTipologiaPratica(), ass, TipoApiTask.ESTRAI_VERSIONI_PRECEDENTI_ALLEGATI, fascicolo.getDati().getOperativitaRidotta())
					: false);
			dto.setModificaTagAbilitata(false);
			dto.setSpostaAllegati(false);
			dto.setSpostaProtocollazioni(false);
			dto.setInviaDaCsvAbilitato(false);
			dto.setCreaBozzaAbilitato(false);
		}

		/*
		 * L'abilitazione del riporta in gestione della pratica viene ricavata dalla pratica e non dal task.
		 */
		RiattivaFascicoloTask riattivaFascicoloTask = gestioneTask.getRiattivaFascicoloTask(fascicolo);
		dto.setRiportaInGestioneAbilitato(fascicolo.isRiattivabile() && xmlPluginToDTOConverterUtil.checkAbilitazioneRiportaInGestione(riattivaFascicoloTask,
				new ArrayList<>(fascicolo.getDati().getOperazioniSuperUtente()), dto.getTipologiaPratica(), null));

		/*
		 * Gestione task di firma
		 */
		Integer taskFirmaAttivi = 0;
		for (ApprovazioneFirmaTask taskFirma : gestioneTask.getAllTaskFirma(fascicolo)) {

			if (taskFirma.isAttivo()) {
				taskFirmaAttivi++;
			}

			DatiApprovazioneFirmaTask datiTaskFirma = taskFirma.getDati();
			DatiTaskFirmaDTO datiTaskFirmaDTO = new DatiTaskFirmaDTO();
			datiTaskFirmaDTO.setId(datiTaskFirma.getId());
			AssegnatarioDTO assegnatarioCorrente = new AssegnatarioDTO();
			assegnatarioCorrente.setNome(datiTaskFirma.getAssegnatario().getNome());
			assegnatarioCorrente.setDataInizio(DateUtils.DATEFORMAT_DATEH.format(datiTaskFirma.getAssegnatario().getDataInizio()));
			if (datiTaskFirma.getAssegnatario().getDataFine() != null) {
				assegnatarioCorrente.setDataFine(DateUtils.DATEFORMAT_DATEH.format(datiTaskFirma.getAssegnatario().getDataFine()));
			}
			datiTaskFirmaDTO.setAssegnatarioCorrente(assegnatarioCorrente);
			for (Assegnatario assegnatarioPassato : datiTaskFirma.getAssegnatariPassati()) {
				AssegnatarioDTO assegnatarioPassatoDTO = new AssegnatarioDTO();
				assegnatarioCorrente.setNome(assegnatarioPassato.getNome());
				assegnatarioCorrente.setDataInizio(DateUtils.DATEFORMAT_DATEH.format(assegnatarioPassato.getDataInizio()));
				assegnatarioCorrente.setDataFine(DateUtils.DATEFORMAT_DATEH.format(assegnatarioPassato.getDataFine()));
				datiTaskFirmaDTO.getAssegnatariPassati().add(assegnatarioPassatoDTO);
			}
			datiTaskFirmaDTO.setDataCreazione(DateUtils.DATEFORMAT_DATEH.format(datiTaskFirma.getDataCreazione()));
			datiTaskFirmaDTO.setAttivo(datiTaskFirma.getAttivo());
			datiTaskFirmaDTO.setRiferimentoAllegato(new AllegatoRichiediFirmaDTO(datiTaskFirma.getRiferimentoAllegato().getNome(), datiTaskFirma.getRiferimentoAllegato().getCurrentVersion()));
			datiTaskFirmaDTO.setStato(datiTaskFirma.getStato());

			datiTaskFirmaDTO.setMittenteOriginale(datiTaskFirma.getMittenteOriginale());
			if (datiTaskFirma.getDataScadenza() != null) {
				datiTaskFirmaDTO.setDataScadenza(DateUtils.DATEFORMAT_DATEH.format(datiTaskFirma.getDataScadenza()));
			}

			if (datiTaskFirma.getValido() != null) {
				datiTaskFirmaDTO.setValido(datiTaskFirma.getValido());
			}

			TipoPropostaTaskFirmaDTO tipoRichiestaDTO = null;
			switch (datiTaskFirma.getTipo()) {
			case FIRMA:
				tipoRichiestaDTO = TipoPropostaTaskFirmaDTO.FIRMA;
				break;

			case VISTO:
				tipoRichiestaDTO = TipoPropostaTaskFirmaDTO.VISTO;
				break;

			case PARERE:
				tipoRichiestaDTO = TipoPropostaTaskFirmaDTO.PARERE;
				break;
			}
			datiTaskFirmaDTO.setTipoRichiesta(tipoRichiestaDTO);

			for (DestinatarioRichiestaApprovazioneFirmaTask dest : datiTaskFirma.getDestinatari()) {
				DestinatarioDTO destDTO = convertDestinatario(dest);

				StatoDestinatarioTaskFirmaDTO statoRichiestaDTO = null;
				switch (dest.getStato()) {
				case IN_APPROVAZIONE:
					statoRichiestaDTO = StatoDestinatarioTaskFirmaDTO.IN_APPROVAZIONE;
					break;

				case DINIEGATO:
					statoRichiestaDTO = StatoDestinatarioTaskFirmaDTO.DINIEGATO;
					break;

				case APPROVATO:
					statoRichiestaDTO = StatoDestinatarioTaskFirmaDTO.APPROVATO;
					break;

				case RISPOSTA_NEGATIVA:
					statoRichiestaDTO = StatoDestinatarioTaskFirmaDTO.RISPOSTA_NEGATIVA;
					break;

				case RISPOSTA_POSITIVA:
					statoRichiestaDTO = StatoDestinatarioTaskFirmaDTO.RISPOSTA_POSITIVA;
					break;

				case RISPOSTA_POSITIVA_CON_PRESCRIZIONI:
					statoRichiestaDTO = StatoDestinatarioTaskFirmaDTO.RISPOSTA_POSITIVA_CON_PRESCRIZIONI;
					break;

				case RISPOSTA_SOSPESA:
					statoRichiestaDTO = StatoDestinatarioTaskFirmaDTO.RISPOSTA_SOSPESA;
					break;

				case RISPOSTA_RIFIUTATA:
					statoRichiestaDTO = StatoDestinatarioTaskFirmaDTO.RISPOSTA_RIFIUTATA;
					break;
				}

				destDTO.setStatoRichiesta(statoRichiestaDTO);
				datiTaskFirmaDTO.getDestinatari().add(destDTO);
			}

			TaskFirmaDTO taskFirmaDTO = new TaskFirmaDTO(datiTaskFirmaDTO);
			taskFirmaDTO.setCurrentUser(taskFirma.getCurrentUser());
			taskFirmaDTO.setUtenteEsterno(taskFirma.isUtenteEsterno());

			dto.getTasks().add(taskFirmaDTO);
		}

		/*
		 * Gli allegati sono tutti elencati nella lista generica di pratica. Utilizziamo quindi una mappa di appoggio, per poterli recuperare quando si ciclano le sottoliste di protocollazione in modo
		 * da non reinvocare inutilmente la verifica della firma. Da questa mappa rimuoviamo gli allegati protocollati, in modo che alla fine rimangano solo quelli non prot. Anche le pratiche
		 * protocollate e non, sono tutte elencate in praticheCollegate
		 */
		Map<String, ElementoAllegato> allegatiMap = new HashMap<String, FascicoloDTO.ElementoAllegato>();
		Map<String, ElementoPECRiferimento> praticheMap = new HashMap<String, FascicoloDTO.ElementoPECRiferimento>();
		Map<String, ElementoPraticaModulisticaRiferimento> praticheModulistica = new HashMap<String, FascicoloDTO.ElementoPraticaModulisticaRiferimento>();
		Map<String, ElementoComunicazioneRiferimento> comunicazioni = new HashMap<String, FascicoloDTO.ElementoComunicazioneRiferimento>();

		List<AllegatoDTO> listaAllegati = new ArrayList<AllegatoDTO>();
		/* elenco tutti allegati */
		for (Allegato allg : dati.getAllegati()) {
			if (checkVisibilitaallegato(allg)) {
				ElementoAllegato ea = new ElementoAllegato(allg.getNome(), allg.getFolderOriginPath(), allg.getFolderOriginName(), path, allg.getCurrentVersion());
				ea.getTipologiaAllegato().addAll(allg.getTipologiaDocumento());
				ea.setFirmatoHash(allg.getFirmatoHash());
				// verificaFirmaAllegato(ea, fascicolo);
				Stato stato = (allg.getFirmato()) ? Stato.FIRMATO : Stato.NONFIRMATO;
				ea.setStato(stato);
				ea.setPubblicato(xmlPluginToDTOConverterUtil.pubblicazioneOK(allg.getDataInizioPubblicazione(), allg.getDataFinePubblicazione()));
				ea.setDataInizioPubblicazione(allg.getDataInizioPubblicazione());
				ea.setDataFinePubblicazione(allg.getDataFinePubblicazione());
				ea.setDataCaricamento(allg.getDataCaricamento());
				AllegatoDTO adto = new AllegatoDTO(allg.getNome(), allg.getFolderOriginPath(), allg.getFolderOriginName(), path, allg.getCurrentVersion());
				adto.setFirmatoHash(allg.getFirmatoHash());

				if (allg.getFirmato() || allg.getFirmatoHash()) {
					TipologiaFirma tf = TipologiaFirma.getTipologiaFirma(allg.getTipoFirma().toString());
					adto.setTipologiaFirma(tf == null ? TipologiaFirma.NONFIRMATO : tf);
					ea.setTipologiaFirma(tf == null ? TipologiaFirma.NONFIRMATO : tf);
				} else {
					adto.setTipologiaFirma(TipologiaFirma.NONFIRMATO);
				}

				adto.setLock(allg.getLock());
				adto.setLockedBy(allg.getLockedBy());
				adto.setOggetto(allg.getOggettoDocumento());
				ea.setLock(allg.getLock());
				ea.setLockedBy(allg.getLockedBy());
				ea.setOggetto(allg.getOggettoDocumento());
				adto.setInTaskFirma(isAllegatoInTaskFirmaAttivo(allg, fascicolo));
				ea.setInTaskFirma(isAllegatoInTaskFirmaAttivo(allg, fascicolo));
				adto.getTipologiaAllegato().addAll(allg.getTipologiaDocumento());
				Date inizio = allg.getDataInizioPubblicazione();
				Date fine = allg.getDataFinePubblicazione();
				Date oggi = DateUtils.getMidnightToday();
				adto.setPubblicato((inizio != null && fine != null) && (inizio.equals(oggi) || inizio.after(oggi)) && (fine.equals(inizio) || fine.after(inizio)));
				adto.setDataInizioPubblicazione(inizio);
				adto.setDataFinePubblicazione(fine);
				TreeSet<it.eng.portlet.consolepec.gwt.shared.model.GruppoVisibilita> visibilitaAllegato = xmlPluginToDTOConverterUtil.popolaVisibilitaAllegato(adto, allg.getGruppiVisibilita());
				ea.getVisibilita().addAll(visibilitaAllegato);
				// guardo solo la data di fine pubblicazione, quindi può risultare pubblicato anche se la data di inizio è maggiore di oggi
				adto.setPubblicato(xmlPluginToDTOConverterUtil.pubblicazioneOK(allg.getDataInizioPubblicazione(), allg.getDataFinePubblicazione()));
				adto.setDataInizioPubblicazione(allg.getDataInizioPubblicazione());
				adto.setDataFinePubblicazione(allg.getDataFinePubblicazione());
				adto.setDataCaricamento(allg.getDataCaricamento());
				adto.setStato(stato);
				popolaStoricoVersioniAllegato(allg, adto, ea);

				adto.getDatiAggiuntivi().addAll(allg.getDatiAggiuntivi());

				allegatiMap.put(ea.getNome(), ea);
				listaAllegati.add(adto);
			}
		}

		dto.setAllegati(listaAllegati);

		/* elenco tutte le pratiche */
		for (PraticaCollegata pc : fascicolo.getAllPraticheCollegate()) {
			String encPath = Base64Utils.URLencodeAlfrescoPath(pc.getAlfrescoPath());

			if (PraticaUtil.isIngresso(pc.getTipo())) {

				if (TipologiaPratica.EMAIL_IN.getNomeTipologia().equals(pc.getTipo())) {
					praticheMap.put(pc.getAlfrescoPath(), new ElementoPECRiferimento(encPath, TipoRiferimentoPEC.IN, pc.getDataCaricamento()));
				}

				else if (TipologiaPratica.EMAIL_EPROTOCOLLO.getNomeTipologia().equals(pc.getTipo())) {
					praticheMap.put(pc.getAlfrescoPath(), new ElementoPECRiferimento(encPath, TipoRiferimentoPEC.EPROTO, pc.getDataCaricamento()));
				}
			}

			else if (PraticaUtil.isEmailOut(pc.getTipo())) {
				praticheMap.put(pc.getAlfrescoPath(), new ElementoPECRiferimento(encPath, TipoRiferimentoPEC.OUT, pc.getDataCaricamento()));
			}

			else if (PraticaUtil.isPraticaModulistica(pc.getTipo())) {
				praticheModulistica.put(pc.getAlfrescoPath(), new ElementoPraticaModulisticaRiferimento(encPath, pc.getDataCaricamento()));
			} else if (PraticaUtil.isComunicazione(pc.getTipo())) {
				// comunicazioni.put(pc.getAlfrescoPath(), new ElementoComunicazioneRiferimento(encPath, pc.getDataCaricamento()));
			}

		}
		/* gestione gruppi protocollati CAPOFILA */
		Collections.sort(dati.getProtocollazioniCapofila());
		for (ProtocollazioneCapofila pc : dati.getProtocollazioniCapofila()) {

			boolean visibile = false;

			ElementoGruppoProtocollatoCapofila capofila = new ElementoGruppoProtocollatoCapofila();
			capofila.setAnnoPG(pc.getAnnoPG().toString());
			capofila.setNumeroPG(pc.getNumeroPG());
			capofila.setAnnoPGCapofila(pc.getAnnoPG().toString());
			capofila.setNumeroPGCapofila(pc.getNumeroPG());
			capofila.setIdTitolo(pc.getTitolo());
			capofila.setIdRubrica(pc.getRubrica());
			capofila.setIdSezione(pc.getSezione());
			capofila.setTipologiaDocumento(pc.getTipologiadocumento());
			capofila.setDataProtocollazione(pc.getDataprotocollazione());

			for (PraticaCollegata pc2 : pc.getPraticheCollegateProtocollate()) {
				boolean praticaProtocollata = true;

				if (PraticaUtil.isIngresso(pc2.getTipo())) {
					String encPath = Base64Utils.URLencodeAlfrescoPath(pc2.getAlfrescoPath());
					capofila.addRiferimentoPEC(new ElementoPECRiferimento(encPath, TipoRiferimentoPEC.IN, pc2.getDataCaricamento()));

				} else if (PraticaUtil.isEmailOut(pc2.getTipo())) {
					String encPath = Base64Utils.URLencodeAlfrescoPath(pc2.getAlfrescoPath());
					capofila.addRiferimentoPEC(new ElementoPECRiferimento(encPath, TipoRiferimentoPEC.OUT, pc2.getDataCaricamento()));

				} else if (PraticaUtil.isPraticaModulistica(pc2.getTipo())) {
					String encPath = Base64Utils.URLencodeAlfrescoPath(pc2.getAlfrescoPath());
					capofila.addRiferimentoPraticaModulistica(new ElementoPraticaModulisticaRiferimento(encPath, pc2.getDataCaricamento()));

				} else {
					praticaProtocollata = false;
				}

				/* rimuovo la pratica dalle candidate non prot */
				praticheMap.remove(pc2.getAlfrescoPath());
				praticheModulistica.remove(pc2.getAlfrescoPath());
				visibile = praticaProtocollata;
			}
			for (Allegato allg : pc.getAllegatiProtocollati()) {
				if (checkVisibilitaallegato(allg)) {
					capofila.addAllegato(allegatiMap.remove(allg.getNome()));
					visibile = true;
				}
			}
			if (pc.getAllegatiProtocollati().size() == 0 && pc.getPraticheCollegateProtocollate().size() == 0) {
				capofila.setOggetto(pc.getOggetto());
			}
			/* gestione gruppi protocollati sotto capofila */
			Collections.sort(pc.getProtocollazioniCollegate());
			for (Protocollazione pr : pc.getProtocollazioniCollegate()) {
				ElementoGruppoProtocollato subProt = new ElementoGruppoProtocollato();
				capofila.addElementoGruppoProtocollato(subProt);
				subProt.setAnnoPG(pr.getAnnoPG() + "");
				subProt.setNumeroPG(pr.getNumeroPG());
				subProt.setAnnoPGCapofila(pc.getAnnoPG().toString());
				subProt.setNumeroPGCapofila(pc.getNumeroPG());
				subProt.setIdTitolo(pc.getTitolo());
				subProt.setIdRubrica(pc.getRubrica());
				subProt.setIdSezione(pc.getSezione());
				subProt.setTipologiaDocumento(pc.getTipologiadocumento());
				subProt.setDataProtocollazione(pc.getDataprotocollazione());
				subProt.setOggetto(pc.getOggetto());

				for (PraticaCollegata pc3 : pr.getPraticheCollegateProtocollate()) {

					if (PraticaUtil.isIngresso(pc3.getTipo())) {
						String encPath = Base64Utils.URLencodeAlfrescoPath(pc3.getAlfrescoPath());
						subProt.addRiferimentoPEC(new ElementoPECRiferimento(encPath, TipoRiferimentoPEC.IN, pc3.getDataCaricamento()));

					} else if (PraticaUtil.isEmailOut(pc3.getTipo())) {
						String encPath = Base64Utils.URLencodeAlfrescoPath(pc3.getAlfrescoPath());
						subProt.addRiferimentoPEC(new ElementoPECRiferimento(encPath, TipoRiferimentoPEC.OUT, pc3.getDataCaricamento()));

					} else {
						String encPath = Base64Utils.URLencodeAlfrescoPath(pc3.getAlfrescoPath());
						subProt.addRiferimentoPraticaModulistica(new ElementoPraticaModulisticaRiferimento(encPath, pc3.getDataCaricamento()));
					}
					/* rimuovo la pratica dalle candidate non prot */
					praticheMap.remove(pc3.getAlfrescoPath());
					praticheModulistica.remove(pc3.getAlfrescoPath());
					visibile = true;
				}
				Collections.sort(pr.getAllegatiProtocollati());
				for (Allegato allg : pr.getAllegatiProtocollati()) {
					if (checkVisibilitaallegato(allg)) {
						subProt.addAllegato(allegatiMap.remove(allg.getNome()));
						visibile = true;
					}
				}
			}

			if (visibile) {
				dto.getElenco().add(capofila);
			}

		}

		/* gestione gruppo NON protocollati */
		ElementoGruppo nonProt = new FascicoloDTO.ElementoGrupppoNonProtocollato();
		/* gestione allegati non protocollati */
		for (String nome : allegatiMap.keySet()) {
			nonProt.addAllegato(allegatiMap.get(nome));
		}
		/* gestione pratiche non collegate */
		for (String alfrescoPath : praticheMap.keySet()) {
			nonProt.addRiferimentoPEC(praticheMap.get(alfrescoPath));
		}
		for (String alfrescoPath : praticheModulistica.keySet()) {
			nonProt.addRiferimentoPraticaModulistica(praticheModulistica.get(alfrescoPath));
		}
		for (String alfrescoPath : comunicazioni.keySet()) {
			nonProt.addRiferimentoComunicazione(comunicazioni.get(alfrescoPath));
		}

		if (nonProt.getElementi().size() > 0) {
			dto.getElenco().add(nonProt);
		}

		dto.getValoriDatiAggiuntivi().addAll(dati.getDatiAggiuntivi());

		Map<String, List<String>> valoriPredefiniti = xmlPluginToDTOConverterUtil.getValoriPredefiniti(af);
		for (DatoAggiuntivo datoAggiuntivo : dto.getValoriDatiAggiuntivi()) {
			popolaValoriPredefiniti(datoAggiuntivo, valoriPredefiniti);
		}

		TreeSet<Procedimento> procedimenti = fascicolo.getDati().getProcedimenti();
		for (Procedimento procedimento : procedimenti) {
			ProcedimentoDto p = xmlPluginToDTOConverterUtil.convertoProcedimentoToDto(procedimento);
			dto.getProcedimenti().add(p);
		}

		if (fascicolo.getDati().getStepIter() != null) {
			StepIter stepIter = new StepIter();
			stepIter.setNome(fascicolo.getDati().getStepIter().getNome());
			stepIter.setFinale(fascicolo.getDati().getStepIter().getFinale());
			stepIter.setIniziale(fascicolo.getDati().getStepIter().getIniziale());
			stepIter.setCreaBozza(fascicolo.getDati().getStepIter().getCreaBozza());
			stepIter.setDestinatariNotifica(fascicolo.getDati().getStepIter().getDestinatariNotifica());
			if (fascicolo.getDati().getStepIter().getDataAggiornamento() != null) {
				stepIter.setDataAggiornamento(fascicolo.getDati().getStepIter().getDataAggiornamento());
			}
			dto.setStepIter(stepIter);
		}

		if (fascicolo.getDati().getOperatore().getNome() != null) {
			dto.setOperatore(fascicolo.getDati().getOperatore().getNome());
		}

		dto.setCollegamentoVisitabile(xmlPluginToDTOConverterUtil.isCollegamentoVisitabile(dto.getAssegnatario()));

		XMLPluginToDTOConverterAllegatiUtil.creaListeAllegati(fascicolo, praticaSessionUtil, logger, dto);

		return dto;
	}

	private void popolaValoriPredefiniti(DatoAggiuntivo datoAggiuntivo, final Map<String, List<String>> valoriPredefiniti) {

		datoAggiuntivo.accept(new it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo.DatoAggiuntivoVisitorAdapter() {

			@Override
			public void visit(DatoAggiuntivoValoreMultiplo d) {
				d.getValoriPredefiniti().clear();
				if (valoriPredefiniti.get(d.getNome()) != null) {
					d.getValoriPredefiniti().addAll(valoriPredefiniti.get(d.getNome()));
				}

			}

			@Override
			public void visit(DatoAggiuntivoValoreSingolo d) {
				d.getValoriPredefiniti().clear();
				if (valoriPredefiniti.get(d.getNome()) != null) {
					d.getValoriPredefiniti().addAll(valoriPredefiniti.get(d.getNome()));
				}
			}

			@Override
			public void visit(DatoAggiuntivoTabella d) {
				for (DatoAggiuntivo dag : d.getIntestazioni()) {
					popolaValoriPredefiniti(dag, valoriPredefiniti);
				}
			}
		});
	}

	private DestinatarioDTO convertDestinatario(DestinatarioRichiestaApprovazioneFirmaTask destinatario) {

		if (destinatario instanceof DestinatarioUtenteRichiestaApprovazioneFirmaTask) {
			DestinatarioUtenteRichiestaApprovazioneFirmaTask dest = (DestinatarioUtenteRichiestaApprovazioneFirmaTask) destinatario;
			DestinatarioUtenteDTO destDTO = new DestinatarioUtenteDTO();
			destDTO.setUserId(dest.getNomeUtente());
			destDTO.setNome(dest.getNome());
			destDTO.setCognome(dest.getCognome());
			destDTO.setNomeCompleto(new StringBuilder(dest.getNome()).append(" ").append(dest.getCognome()).toString());
			destDTO.setSettore(dest.getSettore());
			destDTO.setMatricola(dest.getMatricola());
			return destDTO;

		} else if (destinatario instanceof DestinatarioGruppoRichiestaApprovazioneFirmaTask) {
			DestinatarioGruppoRichiestaApprovazioneFirmaTask dest = (DestinatarioGruppoRichiestaApprovazioneFirmaTask) destinatario;
			DestinatarioGruppoDTO destDTO = new DestinatarioGruppoDTO();
			destDTO.setNomeGruppoConsole(dest.getNomeGruppo());
			destDTO.setNomeGruppoDisplay(gestioneConfigurazioni.getAnagraficaRuolo(dest.getNomeGruppo()).getEtichetta());
			return destDTO;

		} else {
			throw new IllegalArgumentException("Tipo destinatario non valido");
		}
	}

	private void popolaStoricoVersioniAllegato(Allegato a, AllegatoDTO allegato, ElementoAllegato ea) {

		if (a.getStoricoVersioni() != null && !a.getStoricoVersioni().isEmpty()) {

			for (StoricoVersioni storicoVersione : a.getStoricoVersioni()) {

				StoricoVersioniDTO storicoVersioniDTO = new StoricoVersioniDTO(storicoVersione.getVersione());
				storicoVersioniDTO.setUtente(storicoVersione.getUtente());

				if (storicoVersione.getInformazioniTaskFirma() != null) {
					InformazioniTaskFirmaDTO iDTO = convertiInformazioniTaskFirma(storicoVersione.getInformazioniTaskFirma());
					storicoVersioniDTO.setInformazioniTaskFirma(iDTO);
				}

				if (storicoVersione.getInformazioniCopia() != null) {
					InformazioniCopiaDTO infoCopia = new InformazioniCopiaDTO(storicoVersione.getInformazioniCopia().getIdDocumentaleSorgente());

					if (storicoVersione.getInformazioniCopia().getInformazioniTaskFirma() != null) {
						for (Entry<String, InformazioniTaskFirma> entry : storicoVersione.getInformazioniCopia().getInformazioniTaskFirma().entrySet()) {
							infoCopia.getInformazioniTaskFirma().put(entry.getKey(), convertiInformazioniTaskFirma(entry.getValue()));
						}
					}

					storicoVersioniDTO.setInformazioniCopia(infoCopia);
				}

				if (ea != null) {
					ea.getStoricoVersioni().add(storicoVersioniDTO);
				}

				allegato.getStoricoVersioni().add(storicoVersioniDTO);
			}
		}
	}

	private InformazioniTaskFirmaDTO convertiInformazioniTaskFirma(InformazioniTaskFirma infoTask) {
		ProponenteDTO proponente = new ProponenteDTO();
		proponente.setNomeGruppo(infoTask.getProponente());

		TipoPropostaTaskFirmaDTO tipoRichiesta = null;
		switch (infoTask.getTipoRichiesta()) {
		case FIRMA:
			tipoRichiesta = TipoPropostaTaskFirmaDTO.FIRMA;
			break;

		case VISTO:
			tipoRichiesta = TipoPropostaTaskFirmaDTO.VISTO;
			break;

		case PARERE:
			tipoRichiesta = TipoPropostaTaskFirmaDTO.PARERE;
			break;
		}

		StatoTaskFirmaDTO operazione = null;
		switch (infoTask.getOperazioneEffettuata()) {
		case APPROVATO:
			operazione = StatoTaskFirmaDTO.APPROVATO;
			break;

		case DINIEGATO:
			operazione = StatoTaskFirmaDTO.DINIEGATO;
			break;

		case IN_APPROVAZIONE:
			operazione = StatoTaskFirmaDTO.IN_APPROVAZIONE;
			break;

		case RITIRATO:
			operazione = StatoTaskFirmaDTO.RITIRATO;
			break;

		case PARERE_RICEVUTO:
			operazione = StatoTaskFirmaDTO.PARERE_RICEVUTO;
			break;

		case EVASO:
			operazione = StatoTaskFirmaDTO.EVASO;
			break;
		}

		List<DestinatarioDTO> destinatari = new ArrayList<DestinatarioDTO>();

		if (infoTask.getDestinatari() != null) {

			for (DestinatarioRichiestaApprovazioneFirmaTask destXML : infoTask.getDestinatari()) {

				DestinatarioDTO destinatario = convertDestinatario(destXML);
				StatoDestinatarioTaskFirmaDTO statoRichiesta = null;

				switch (destXML.getStato()) {
				case APPROVATO:
					statoRichiesta = StatoDestinatarioTaskFirmaDTO.APPROVATO;
					break;

				case DINIEGATO:
					statoRichiesta = StatoDestinatarioTaskFirmaDTO.DINIEGATO;
					break;

				case IN_APPROVAZIONE:
					statoRichiesta = StatoDestinatarioTaskFirmaDTO.IN_APPROVAZIONE;
					break;

				case RISPOSTA_NEGATIVA:
					statoRichiesta = StatoDestinatarioTaskFirmaDTO.RISPOSTA_NEGATIVA;
					break;

				case RISPOSTA_POSITIVA:
					statoRichiesta = StatoDestinatarioTaskFirmaDTO.RISPOSTA_POSITIVA;
					break;

				case RISPOSTA_POSITIVA_CON_PRESCRIZIONI:
					statoRichiesta = StatoDestinatarioTaskFirmaDTO.RISPOSTA_POSITIVA_CON_PRESCRIZIONI;
					break;

				case RISPOSTA_SOSPESA:
					statoRichiesta = StatoDestinatarioTaskFirmaDTO.RISPOSTA_SOSPESA;
					break;

				case RISPOSTA_RIFIUTATA:
					statoRichiesta = StatoDestinatarioTaskFirmaDTO.RISPOSTA_RIFIUTATA;
					break;
				}
				destinatario.setStatoRichiesta(statoRichiesta);
				destinatari.add(destinatario);
			}
		}

		String mittenteOriginale = infoTask.getMittenteOriginale();
		String dataScadenza = null;
		if (infoTask.getDataScadenza() != null) {
			dataScadenza = DateUtils.DATEFORMAT_DATEH.format(infoTask.getDataScadenza());
		}

		String oggetto = infoTask.getOggetto();
		String motivazione = infoTask.getMotivazione();
		return new InformazioniTaskFirmaDTO(oggetto, proponente, tipoRichiesta, destinatari, operazione, mittenteOriginale, dataScadenza, loadStatoTaskFirmaDTO(infoTask.getStatoRichiesta()),
				motivazione);
	}

	private static StatoTaskFirmaDTO loadStatoTaskFirmaDTO(StatoRichiestaApprovazioneFirmaTask statoRichiesta) {
		switch (statoRichiesta) {
		case APPROVATO:
			return StatoTaskFirmaDTO.APPROVATO;
		case DINIEGATO:
			return StatoTaskFirmaDTO.DINIEGATO;
		case IN_APPROVAZIONE:
			return StatoTaskFirmaDTO.IN_APPROVAZIONE;
		case RITIRATO:
			return StatoTaskFirmaDTO.RITIRATO;
		case PARERE_RICEVUTO:
			return StatoTaskFirmaDTO.PARERE_RICEVUTO;
		case EVASO:
			return StatoTaskFirmaDTO.EVASO;
		default:
			throw new IllegalArgumentException("Stato richiesta task non valido");
		}
	}

	private ValoreModuloDTO createValoreModulo(ValoreModulo v) {
		ValoreModuloDTO valore = new ValoreModuloDTO();
		valore.setNome(v.getNome());
		valore.setValore(v.getValore());
		valore.setTipoValoreModulo(TipoValorModuloeDTO.valueOf(v.getTipo().name()));
		valore.setEtichetta(v.getEtichetta());
		valore.setDescrizione(v.getDescrizione());
		valore.setVisibile(v.isVisibile());

		TabellaModuloDTO tabella = null;
		if (v.getTabella() != null) {
			tabella = new TabellaModuloDTO();
			for (Riga r : v.getTabella().getRighe()) {
				RigaDTO riga = new RigaDTO();
				for (ValoreModulo colonna : r.getColonne()) {
					riga.getColonne().add(createValoreModulo(colonna));
				}
				tabella.getRighe().add(riga);
			}

		}
		valore.setTabella(tabella);
		return valore;
	}

	public PraticaModulisticaDTO praticaModulisticaToDettaglio(PraticaModulistica modulistica) {
		DatiModulistica dati = modulistica.getDati();

		String path = modulistica.getAlfrescoPath();
		path = Base64Utils.URLencodeAlfrescoPath(path);
		PraticaModulisticaDTO dto = getModulisticaDto(path, dati);

		dto.setTitolo(dati.getTitolo());
		dto.setDataOraCreazione(DateUtils.DATEFORMAT_DATEH.format(dati.getDataCreazione()));
		dto.setUtenteCreazione(dati.getUtenteCreazione());
		dto.setUsernameCreazione(dati.getUsernameCreazione());
		gestioneTipoPratica(dto, dati.getTipo().getNomeTipologia());
		dto.setNumeroRepertorio(dati.getIdDocumentale());

		if (dati.getProvenienza() != null) {
			if (gestioneConfigurazioni.getAnagraficaRuolo(dati.getProvenienza()) != null) {
				dto.setProvenienza(gestioneConfigurazioni.getAnagraficaRuolo(dati.getProvenienza()).getEtichetta());
			}
		}

		dto.setLetto(dati.isLetto());
		if (dati.getStato() != null) {
			dto.setStato(StatiPraticaModulisticaTranslator.getStatoDTOFromStato(dati.getStato()));
			dto.setStatoLabel(dto.getStato().getLabel());
		}

		/* allegati */
		List<AllegatoDTO> allegati = new ArrayList<AllegatoDTO>();
		for (Allegato a : dati.getAllegati()) {
			AllegatoDTO allegato = new AllegatoDTO(a.getNome(), a.getFolderOriginPath(), a.getFolderOriginName(), dto.getClientID(), a.getCurrentVersion());
			allegato.setFirmatoHash(a.getFirmatoHash());
			// verificaFirmaAllegato(allegato, email);
			Stato stato = (a.getFirmato()) ? Stato.FIRMATO : Stato.NONFIRMATO;
			allegato.setStato(stato);
			allegato.setDataCaricamento(a.getDataCaricamento());
			allegato.setLock(a.getLock());
			allegato.setLockedBy(a.getLockedBy());
			allegato.setOggetto(a.getOggettoDocumento());
			popolaStoricoVersioniAllegato(a, allegato, null);
			allegato.getDatiAggiuntivi().addAll(a.getDatiAggiuntivi());
			allegati.add(allegato);
		}

		dto.setAllegati(allegati);

		gestionePresaInCarico.caricaPresaInCarico(modulistica, dto);

		if (dati.getNome() != null) {
			dto.setNome(dati.getNome());
		}
		if (dati.getValori() != null) {
			for (NodoModulistica n : dati.getValori()) {
				dto.getValori().add(createNodoModulistica(n));
			}
		}
		if (modulistica.hasPraticheCollegate()) {
			// XXX FM COLLEGAMENTI: funziona solo perchè da gui i moduli possono essere collegate solamente ad un fascicolo: valutare con Ceci/Rob se
			// è vero e se c'è una soluzione più robusta
			String fascicoloID = Base64Utils.URLencodeAlfrescoPath(modulistica.getFascicoliCollegati().get(0).getAlfrescoPath());
			dto.setIdClientFascicolo(fascicoloID);
		}

		GestionePraticaModulisticaTask task = gestioneTask.estraiTaskCorrente(modulistica, GestionePraticaModulisticaTask.class, gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());
		if (task != null) {
			// TODO ASSEGNATARIO? SUPERUTENTE?
			dto.setArchiviaAbilitato(task.isArchiviaAbilitato());
			dto.setEliminaAbilitato(task.isEliminaAbilitato());
			dto.setCreaFascicoloAbilitato(task.isCreaFascicoloAbilitato() && gestioneProfilazioneUtente.getAutorizzazioniUtente().isAbilitato(CreazioneFascicoloAbilitazione.class));
			dto.setRiassegnaAbilitato(task.isRiassegnaAbilitato());
			dto.setAggiungiFascicoloAbilitato(task.isAggiungiFascicoloAbilitato());
			dto.setAssegnatario(gestioneConfigurazioni.getAnagraficaRuolo(task.getDati().getAssegnatario().getNome()).getEtichetta());

		} else {
			AnagraficaRuolo ass = gestioneConfigurazioni.getAnagraficaRuolo(modulistica.getDati().getAssegnatarioCorrente());

			dto.setAssegnatario(ass != null ? ass.getEtichetta() : null);
			dto.setArchiviaAbilitato(false);
			dto.setEliminaAbilitato(false);
			dto.setCreaFascicoloAbilitato(false);
			dto.setRiassegnaAbilitato(false);
			dto.setAggiungiFascicoloAbilitato(false);
		}

		dto.setRiportaInGestioneAbilitato(modulistica.isRiattivabile());
		return dto;
	}

	private NodoModulisticaDTO createNodoModulistica(NodoModulistica n) {
		if (n.getTipoNodo() == TipoNodoModulistica.VALORE_MODULO) {
			return createValoreModulo((ValoreModulo) n);
		}
		if (n.getTipoNodo() == TipoNodoModulistica.SEZIONE) {
			Sezione sezione = (Sezione) n;
			SezioneDTO s = new SezioneDTO();
			s.setTitolo(sezione.getTitolo());
			for (NodoModulistica nodoSezione : sezione.getNodi()) {
				s.getNodi().add(createNodoModulistica(nodoSezione));
			}
			return s;
		}
		if (n.getTipoNodo() == null) {
			// gestione vecchi elementi
			return createValoreModulo((ValoreModulo) n);
		}
		return null;
	}

	private static PraticaModulisticaDTO getModulisticaDto(String path, DatiModulistica datiModulistica) {
		if (datiModulistica.getNome() != null
				&& (datiModulistica.getNome().equalsIgnoreCase("AssegnazioneImpiantiSportiviQuart") || datiModulistica.getNome().equalsIgnoreCase("AssegnazioneImpiantiSportivi"))) {
			return new PraticaModulisticaSportDTO(path);
		}

		return new PraticaModulisticaDTO(path);
	}

	/* Metodo che controlla se l'utente corrente ha la visibilita sul'allegato */
	private boolean checkVisibilitaallegato(Allegato allg) {
		TreeSet<GruppoVisibilita> gruppiVisibilita = allg.getGruppiVisibilita();
		if (allg.getGruppiVisibilita().size() == 0) {
			return true;
		}
		for (GruppoVisibilita gruppoVisibilita : gruppiVisibilita) {
			String nomeGruppo = gruppoVisibilita.getNomeGruppo();
			if (gestioneProfilazioneUtente.getDatiUtente().getRuoli().contains(nomeGruppo)) {
				return true;
			}
		}
		logger.info("L'allegato {} non è visibile all'utente {}", allg, gestioneProfilazioneUtente.getDatiUtente().getUsername());
		return false;
	}

	@SuppressWarnings("unchecked")
	public <T extends BaseTemplateDTO> T modelloToDettaglio(AbstractTemplate<?> pratica) {

		T result = null;

		DatiAbstractTemplate dati = pratica.getDati();

		if (pratica instanceof TemplateDocumentoPDF) {
			result = (T) templatePdfToDettaglio((TemplateDocumentoPDF) pratica);
		}
		if (pratica instanceof TemplateEmail) {
			result = (T) templateToDettaglio((TemplateEmail) pratica);
		}

		if (result != null) {
			result.setTitolo(dati.getNome());
			result.setNome(dati.getNome());
			result.setDescrizione(dati.getDescrizione());

			List<TipologiaPratica> tipi = Lists.newArrayList(Lists.transform(dati.getFascicoliAbilitati(), new Function<String, TipologiaPratica>() {

				@Override
				public TipologiaPratica apply(String input) {
					return PraticaUtil.toTipologiaPratica(gestioneConfigurazioni.getAnagraficaFascicolo(input));
				}

			}));
			tipi.removeAll(Collections.singleton(null));
			result.getFascicoliAbilitati().addAll(tipi);
			xmlPluginToDTOConverterUtil.popolaVisibilita(result, dati.getGruppiVisibilita());
			result.setDataOraCreazione(DateUtils.DATEFORMAT_DATEH.format(dati.getDataCreazione()));
			result.setUtenteCreazione(dati.getUtenteCreazione());
			result.setUsernameCreazione(dati.getUsernameCreazione());
			result.setNumeroRepertorio(dati.getIdDocumentale());
			result.setStatoTemplate(getStatoTemplate(dati.getStato()));

			for (it.eng.consolepec.xmlplugin.pratica.template.CampoTemplate c : dati.getCampi()) {
				CampoTemplateDTO campo = new CampoTemplateDTO();
				campo.setNome(c.getNome());
				campo.setTipo(TipoCampoTemplateDTO.valueOf(c.getTipo().name()));
				campo.setFormato(c.getFormato());
				campo.setRegexValidazione(c.getRegexValidazione());
				campo.setLunghezzaMassima(c.getLunghezzaMassima());
				campo.getValoriLista().addAll(c.getValoriLista());

				if (c.getCampoMetadato() != null) {
					CampoMetadatoDTO cm = new CampoMetadatoDTO(c.getCampoMetadato().getIdMetadato(), c.getCampoMetadato().getEtichettaMetadato());
					campo.setCampoMetadato(cm);
				}

				result.getCampi().add(campo);
			}

			for (EventoIterPratica evento : dati.getIter()) {
				result.getEventiIterDTO().add(new EventoIterDTO(evento.getTestoEvento(), DateUtils.DATEFORMAT_DATEH.format(evento.getDataEvento())));
			}

			final AnagraficaModello am = gestioneConfigurazioni.getAnagraficaModello(result.getTipologiaPratica().getNomeTipologia());

			Task<?> taskCorrente = gestioneTask.estraiTaskCorrente(pratica, gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());
			if ((taskCorrente != null && taskCorrente instanceof GestioneAbstractTemplateTask) && !result.getTipoPresaInCarico().equals(TipoPresaInCarico.ALTRO_UTENTE)) {

				result.setEliminaButtonAbilitato(gestioneProfilazioneUtente.isOperazioneAbilitata(result.getTipologiaPratica(), taskCorrente, TipoApiTaskTemplate.ELIMINA, //
						new ArrayList<>(dati.getOperazioniSuperUtente()), //
						dati.getAssegnazioneEsterna() != null ? dati.getAssegnazioneEsterna() != null ? new ArrayList<>(dati.getAssegnazioneEsterna().getOperazioniConsentite()) : null : null, //
						null, null, null));

				result.setSalvaButtonAbilitato(gestioneProfilazioneUtente.isOperazioneAbilitata(result.getTipologiaPratica(), taskCorrente, TipoApiTaskTemplate.MODIFICA, //
						new ArrayList<>(dati.getOperazioniSuperUtente()), //
						dati.getAssegnazioneEsterna() != null ? dati.getAssegnazioneEsterna() != null ? new ArrayList<>(dati.getAssegnazioneEsterna().getOperazioniConsentite()) : null : null, //
						null, null, null));

				QueryAbilitazione<CreazioneModelloAbilitazione> qab = new QueryAbilitazione<CreazioneModelloAbilitazione>();
				qab.addCondition(new CondizioneAbilitazione<CreazioneModelloAbilitazione>() {

					@Override
					protected boolean valutaCondizione(CreazioneModelloAbilitazione abilitazione) {
						return abilitazione.getTipo().equals(am.getNomeTipologia());
					}

				});

				result.setCreaTemplatePerCopiaAbilitato(gestioneProfilazioneUtente.isOperazioneAbilitata(result.getTipologiaPratica(), taskCorrente, TipoApiTaskTemplate.CREA_TEMPLATE_PER_COPIA, //
						new ArrayList<>(dati.getOperazioniSuperUtente()), //
						dati.getAssegnazioneEsterna() != null ? dati.getAssegnazioneEsterna() != null ? new ArrayList<>(dati.getAssegnazioneEsterna().getOperazioniConsentite()) : null : null, //
						null, null, //
						null) && gestioneProfilazioneUtente.getAutorizzazioniUtente().isAbilitato(CreazioneModelloAbilitazione.class, qab));

			} else {
				result.setEliminaButtonAbilitato(false);
				result.setSalvaButtonAbilitato(false);
				result.setCreaTemplatePerCopiaAbilitato(false);
			}
		}

		return result;
	}

	private TemplateDTO templateToDettaglio(TemplateEmail template) {

		String path = Base64Utils.URLencodeAlfrescoPath(template.getAlfrescoPath());
		TemplateDTO dto = new TemplateDTO(path);
		DatiTemplateEmail dati = template.getDati();
		gestioneTipoPratica(dto, dati.getTipo().getNomeTipologia());

		dto.setOggettoMail(dati.getOggettoMail());
		dto.setCorpoMail(dati.getCorpoMail());
		dto.setMittente(dati.getMittente());
		dto.getDestinatari().addAll(dati.getDestinatari());
		dto.getDestinatariCC().addAll(dati.getDestinatariCC());
		List<AllegatoDTO> allegati = new ArrayList<AllegatoDTO>();
		for (Allegato a : dati.getAllegati()) {
			AllegatoDTO allegato = new AllegatoDTO(a.getNome(), a.getFolderOriginPath(), a.getFolderOriginName(), dto.getClientID(), a.getCurrentVersion());
			allegato.setFirmatoHash(a.getFirmatoHash());

			Stato stato = (a.getFirmato()) ? Stato.FIRMATO : Stato.NONFIRMATO;
			allegato.setStato(stato);
			allegato.setDataCaricamento(a.getDataCaricamento());
			allegato.getTipologiaAllegato().addAll(a.getTipologiaDocumento());
			allegato.setLock(a.getLock());
			allegato.setLockedBy(a.getLockedBy());
			allegato.setOggetto(a.getOggettoDocumento());
			popolaStoricoVersioniAllegato(a, allegato, null);

			allegato.getDatiAggiuntivi().addAll(a.getDatiAggiuntivi());

			allegati.add(allegato);
		}
		dto.setAllegati(allegati);

		gestionePresaInCarico.caricaPresaInCarico(template, dto);

		Task<?> taskCorrente = gestioneTask.estraiTaskCorrente(template, gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());
		dto.setUtenteAssegnatario(taskCorrente != null);
		dto.setLetto(dati.isLetto() && (taskCorrente != null));
		if ((taskCorrente != null && taskCorrente instanceof GestioneTemplateEmailTask) && !dto.getTipoPresaInCarico().equals(TipoPresaInCarico.ALTRO_UTENTE)) {

			dto.setAssegnatario(gestioneConfigurazioni.getAnagraficaRuolo(taskCorrente.getDati().getAssegnatario().getNome()).getEtichetta());

			dto.setCaricaAllegatoAbilitato(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCorrente, TipoApiTaskTemplate.AGGIUNGI_ALLEGATO, //
					new ArrayList<>(dati.getOperazioniSuperUtente()), //
					dati.getAssegnazioneEsterna() != null ? dati.getAssegnazioneEsterna() != null ? new ArrayList<>(dati.getAssegnazioneEsterna().getOperazioniConsentite()) : null : null, //
					null, null, null));

			dto.setEliminaAllegatoAbilitato(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCorrente, TipoApiTaskTemplate.RIMUOVI_ALLEGATO, //
					new ArrayList<>(dati.getOperazioniSuperUtente()), //
					dati.getAssegnazioneEsterna() != null ? dati.getAssegnazioneEsterna() != null ? new ArrayList<>(dati.getAssegnazioneEsterna().getOperazioniConsentite()) : null : null, //
					null, null, null));

			dto.setCreaComunicazioneButtonAbilitato(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCorrente, TipoApiTaskTemplate.CREA_COMUNICAZIONE, //
					new ArrayList<>(dati.getOperazioniSuperUtente()), //
					dati.getAssegnazioneEsterna() != null ? dati.getAssegnazioneEsterna() != null ? new ArrayList<>(dati.getAssegnazioneEsterna().getOperazioniConsentite()) : null : null, //
					null, null, //
					null) && gestioneProfilazioneUtente.getAutorizzazioniUtente().isAbilitato(CreazioneComunicazioneAbilitazione.class));

			dto.setGestionePresaInCarico(xmlPluginToDTOConverterUtil.isAssegnatario(taskCorrente));

		} else {
			AnagraficaRuolo ass = gestioneConfigurazioni.getAnagraficaRuolo(template.getDati().getAssegnatarioCorrente());
			dto.setAssegnatario(ass != null ? ass.getEtichetta() : null);
			dto.setCaricaAllegatoAbilitato(false);
			dto.setEliminaAllegatoAbilitato(false);
			dto.setCreaComunicazioneButtonAbilitato(false);
		}

		return dto;
	}

	private TemplatePdfDTO templatePdfToDettaglio(TemplateDocumentoPDF template) {

		String path = Base64Utils.URLencodeAlfrescoPath(template.getAlfrescoPath());
		TemplatePdfDTO dto = new TemplatePdfDTO(path);
		DatiTemplateDocumentoPDF dati = template.getDati();
		gestioneTipoPratica(dto, dati.getTipo().getNomeTipologia());

		if (!dati.getAllegati().isEmpty()) {
			Allegato a = dati.getAllegati().iterator().next();
			AllegatoDTO allegato = new AllegatoDTO(a.getNome(), a.getFolderOriginPath(), a.getFolderOriginName(), dto.getClientID(), a.getCurrentVersion());
			allegato.setFirmatoHash(a.getFirmatoHash());

			Stato stato = (a.getFirmato()) ? Stato.FIRMATO : Stato.NONFIRMATO;
			allegato.setStato(stato);
			allegato.setDataCaricamento(a.getDataCaricamento());
			allegato.getTipologiaAllegato().addAll(a.getTipologiaDocumento());
			allegato.setLock(a.getLock());
			allegato.setLockedBy(a.getLockedBy());
			allegato.setOggetto(a.getOggettoDocumento());
			popolaStoricoVersioniAllegato(a, allegato, null);

			allegato.getDatiAggiuntivi().addAll(a.getDatiAggiuntivi());

			dto.setModelloOdt(allegato);
		}

		dto.setTitoloFile(dati.getTitoloFile());
		gestionePresaInCarico.caricaPresaInCarico(template, dto);

		Task<?> taskCorrente = gestioneTask.estraiTaskCorrente(template, gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());
		dto.setUtenteAssegnatario(taskCorrente != null);
		dto.setLetto(dati.isLetto() && (taskCorrente != null));

		if ((taskCorrente != null && taskCorrente instanceof GestioneTemplateDocumentoPDFTask) && !dto.getTipoPresaInCarico().equals(TipoPresaInCarico.ALTRO_UTENTE)) {

			dto.setAssegnatario(gestioneConfigurazioni.getAnagraficaRuolo(taskCorrente.getDati().getAssegnatario().getNome()).getEtichetta());
			dto.setCaricaModelloOdtAbilitato(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCorrente, TipoApiTaskTemplate.CARICA_ODT, //
					new ArrayList<>(dati.getOperazioniSuperUtente()), //
					dati.getAssegnazioneEsterna() != null ? dati.getAssegnazioneEsterna() != null ? new ArrayList<>(dati.getAssegnazioneEsterna().getOperazioniConsentite()) : null : null, //
					null, null, null));

			dto.setGestionePresaInCarico(xmlPluginToDTOConverterUtil.isAssegnatario(taskCorrente));

		} else {
			AnagraficaRuolo ass = gestioneConfigurazioni.getAnagraficaRuolo(template.getDati().getAssegnatarioCorrente());
			dto.setAssegnatario(ass != null ? ass.getEtichetta() : null);
			dto.setCaricaModelloOdtAbilitato(false);
		}

		return dto;
	}

	private static StatoTemplateDTO getStatoTemplate(it.eng.consolepec.xmlplugin.pratica.template.DatiAbstractTemplate.Stato stato) {

		switch (stato) {
		case IN_GESTIONE:
			return StatoTemplateDTO.IN_GESTIONE;

		case BOZZA:
			return StatoTemplateDTO.BOZZA;

		default:
			throw new IllegalArgumentException("Stato template errato");
		}
	}

	public ComunicazioneDTO comunicazioneToDettaglio(Comunicazione comunicazione) {
		String path = Base64Utils.URLencodeAlfrescoPath(comunicazione.getAlfrescoPath());
		ComunicazioneDTO dto = new ComunicazioneDTO(path);

		DatiComunicazione dati = comunicazione.getDati();

		gestioneTipoPratica(dto, dati.getTipo().getNomeTipologia());
		dto.setDataOraCreazione(DateUtils.DATEFORMAT_DATEH.format(dati.getDataCreazione()));
		dto.setCodice(dati.getCodComunicazione());
		dto.setDescrizione(dati.getDescComunicazione());
		dto.setIdDocumentaleTemplate(dati.getIdTemplate());
		dto.setStato(StatiComunicazioneTranslator.getStatoDTOFromStato(dati.getStato()));
		dto.setStatoLabel(dto.getStato().getLabel());

		dto.setNumeroRepertorio(dati.getIdDocumentale());

		for (Invio i : dati.getInvii()) {
			InvioComunicazioneDTO invio = new InvioComunicazioneDTO();
			invio.setCodEsito(i.getCodEsito());
			invio.setFlgTestProd(i.getFlgTestProd());
			invio.setNumRecordTest(i.getNumRecordTest());
			invio.setPecDestinazioneTest(i.getPecDestinazioneTest());
			dto.getInviiComunicazione().add(invio);
		}

		Task<?> taskCorrente = gestioneTask.estraiTaskCorrente(comunicazione, gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());
		dto.setLetto(dati.isLetto() && (taskCorrente != null));

		xmlPluginToDTOConverterUtil.popolaVisibilita(dto, dati.getGruppiVisibilita());

		gestionePresaInCarico.caricaPresaInCarico(comunicazione, dto);

		List<AllegatoDTO> allegati = new ArrayList<AllegatoDTO>();
		for (Allegato a : dati.getAllegati()) {
			AllegatoDTO allegato = new AllegatoDTO(a.getNome(), a.getFolderOriginPath(), a.getFolderOriginName(), dto.getClientID(), a.getCurrentVersion());
			allegato.setFirmatoHash(a.getFirmatoHash());

			Stato stato = (a.getFirmato()) ? Stato.FIRMATO : Stato.NONFIRMATO;
			allegato.setStato(stato);
			allegato.setDataCaricamento(a.getDataCaricamento());
			allegato.getTipologiaAllegato().addAll(a.getTipologiaDocumento());
			allegato.setLock(a.getLock());
			allegato.setLockedBy(a.getLockedBy());
			allegato.setOggetto(a.getOggettoDocumento());
			popolaStoricoVersioniAllegato(a, allegato, null);

			allegato.getDatiAggiuntivi().addAll(a.getDatiAggiuntivi());

			allegati.add(allegato);
		}

		Collections.sort(allegati, new Comparator<AllegatoDTO>() {

			@Override
			public int compare(AllegatoDTO d1, AllegatoDTO d2) {
				if (d1 != null && d2 != null) {
					return d2.compareTo(d1);
				} else {
					return 0;
				}
			}
		});
		dto.setAllegati(allegati);

		for (EventoIterPratica evento : comunicazione.getDati().getIter()) {
			dto.getEventiIterDTO().add(new EventoIterDTO(evento.getTestoEvento(), DateUtils.DATEFORMAT_DATEH.format(evento.getDataEvento())));
		}

		if ((taskCorrente != null && taskCorrente instanceof GestioneComunicazioneTask) && !dto.getTipoPresaInCarico().equals(TipoPresaInCarico.ALTRO_UTENTE)) {

			dto.setAssegnatario(gestioneConfigurazioni.getAnagraficaRuolo(taskCorrente.getDati().getAssegnatario().getNome()).getEtichetta());

			dto.setCaricaAllegatoAbilitato(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCorrente, TipoApiTaskComunicazione.AGGIUNGI_ALLEGATO, //
					new ArrayList<>(dati.getOperazioniSuperUtente()), //
					dati.getAssegnazioneEsterna() != null ? dati.getAssegnazioneEsterna() != null ? new ArrayList<>(dati.getAssegnazioneEsterna().getOperazioniConsentite()) : null : null, //
					null, null, null));

			dto.setEliminaAllegatoAbilitato(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCorrente, TipoApiTaskComunicazione.RIMUOVI_ALLEGATO, //
					new ArrayList<>(dati.getOperazioniSuperUtente()), //
					dati.getAssegnazioneEsterna() != null ? new ArrayList<>(dati.getAssegnazioneEsterna().getOperazioniConsentite()) : null, //
					null, null, null));

			dto.setCreaComunicazionePerCopiaAbilitato(gestioneProfilazioneUtente.isOperazioneAbilitata(dto.getTipologiaPratica(), taskCorrente, TipoApiTaskComunicazione.CREA_COMUNICAZIONE_PER_COPIA, //
					new ArrayList<>(dati.getOperazioniSuperUtente()), //
					dati.getAssegnazioneEsterna() != null ? new ArrayList<>(dati.getAssegnazioneEsterna().getOperazioniConsentite()) : null, //
					null, null, //
					null) && gestioneProfilazioneUtente.getAutorizzazioniUtente().isAbilitato(CreazioneComunicazioneAbilitazione.class));

			// abilitazione pulsante allegati
			for (AllegatoDTO allegato : dto.getAllegati()) {
				if (XmlPluginUtil.isAllegatoComunicazioneInviabile(allegato.getTipologiaAllegato())) {
					allegato.setInvioComunicazioneAbilitato(true);
				}
			}

		} else {
			AnagraficaRuolo ass = gestioneConfigurazioni.getAnagraficaRuolo(comunicazione.getDati().getAssegnatarioCorrente());
			dto.setAssegnatario(ass != null ? ass.getEtichetta() : null);
			dto.setCaricaAllegatoAbilitato(false);
			dto.setEliminaAllegatoAbilitato(false);
			dto.setCreaComunicazionePerCopiaAbilitato(false);

			for (AllegatoDTO allegato : dto.getAllegati()) {
				allegato.setInvioComunicazioneAbilitato(false);
			}

		}

		for (PraticaCollegata praticaCollegata : comunicazione.getAllPraticheCollegate()) {
			CollegamentoDto collegamentoDTO = new CollegamentoDto();
			collegamentoDTO.setClientId(Base64Utils.URLencodeAlfrescoPath(praticaCollegata.getAlfrescoPath()));

			dto.getCollegamenti().add(collegamentoDTO);
		}

		return dto;
	}

	private boolean isAllegatoInTaskFirmaAttivo(Allegato allegato, Fascicolo fascicolo) {
		for (ApprovazioneFirmaTask taskFirma : gestioneTask.getAllTaskFirma(fascicolo)) {
			if (taskFirma.getDati().getRiferimentoAllegato().getNome().equalsIgnoreCase(allegato.getNome())) {
				return true;
			}
		}

		return false;
	}

	private void gestioneTipoPratica(FascicoloDTO f, String nomeTipologia) {
		AnagraficaFascicolo af = gestioneConfigurazioni.getAnagraficaFascicolo(nomeTipologia);
		f.setTipologiaPratica(PraticaUtil.toTipologiaPratica(af));
	}

	private void gestioneTipoPratica(ComunicazioneDTO f, String nomeTipologia) {
		AnagraficaComunicazione af = gestioneConfigurazioni.getAnagraficaComunicazione(nomeTipologia);
		f.setTipologiaPratica(PraticaUtil.toTipologiaPratica(af));
	}

	private void gestioneTipoPratica(PraticaModulisticaDTO f, String nomeTipologia) {
		AnagraficaPraticaModulistica af = gestioneConfigurazioni.getAnagraficaPraticaModulistica(nomeTipologia);
		f.setTipologiaPratica(PraticaUtil.toTipologiaPratica(af));
	}

	private void gestioneTipoPratica(BaseTemplateDTO f, String nomeTipologia) {
		AnagraficaModello af = gestioneConfigurazioni.getAnagraficaModello(nomeTipologia);
		f.setTipologiaPratica(PraticaUtil.toTipologiaPratica(af));
	}

	private void gestioneTipoPratica(PecInDTO f, String nomeTipologia, String indirizzo) {
		AnagraficaIngresso af = gestioneConfigurazioni.getAnagraficaIngresso(nomeTipologia, indirizzo);
		f.setTipologiaPratica(PraticaUtil.toTipologiaPratica(af));
	}

	private void gestioneTipoPratica(PecOutDTO f, String nomeTipologia, String indirizzo, it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato stato) {

		if (!stato.equals(it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato.BOZZA)) {
			AnagraficaEmailOut af = gestioneConfigurazioni.getAnagraficaMailInUscita(nomeTipologia, indirizzo);
			f.setTipologiaPratica(PraticaUtil.toTipologiaPratica(af));

		} else {
			TipologiaPratica tp = TipologiaPratica.EMAIL_OUT;
			tp.setEtichettaTipologia(TipologiaPratica.EMAIL_OUT_ETICHETTA_DEFAULT);
			tp.setDettaglioNameToken(TipologiaPratica.EMAIL_OUT_DETTAGLIO_NAME_TOKEN_DEFAULT);
			tp.setStato(it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica.Stato.ATTIVA);
			f.setTipologiaPratica(tp);
		}

	}

	public PraticaDTO procediToPraticaDTO(PraticaProcedi praticaProcedi) {
		PraticaDTO praticaDTO = null;

		if (praticaProcedi != null && praticaProcedi.getChiaveAllegati() != null) {
			AnagraficaFascicolo af = gestioneConfigurazioni.getAnagraficaFascicolo(TipologiaPratica.PRATICA_PROCEDI.getNomeTipologia());
			praticaDTO = new PraticaDTO(praticaProcedi.getChiaveAllegati());
			praticaDTO.setTipologiaPratica(PraticaUtil.toTipologiaPratica(af));
			praticaDTO.setAnnoPG(praticaProcedi.getAnnoProtocollo());
			praticaDTO.setNumeroPG(praticaProcedi.getNumeroProtocollo());
			praticaDTO.setDataOraCreazione("");
			if (praticaProcedi.getDataCreazione() != null) {
				praticaDTO.setDataOraCreazione(DateUtils.convertStringDateToAnnotherFormatterStringDate(praticaProcedi.getDataCreazione(), "yyyy-MM-dd'T'HH:mm:ss", ConsoleConstants.FORMATO_DATAORA));
			}
			praticaDTO.setTitolo(praticaProcedi.getOggetto());
			praticaDTO.setProvenienza("");
			praticaDTO.setStatoLabel("");
			praticaDTO.setAssegnatario("Procedi");
		}

		return praticaDTO;
	}
}
