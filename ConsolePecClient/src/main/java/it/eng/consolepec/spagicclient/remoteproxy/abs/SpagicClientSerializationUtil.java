package it.eng.consolepec.spagicclient.remoteproxy.abs;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.bologna.comune.alfresco.create.folder.REQUEST;
import it.bologna.comune.alfresco.generic.service.ObjectFactory;
import it.bologna.comune.alfresco.generic.service.Request;
import it.bologna.comune.alfresco.generic.service.Response;
import it.bologna.comune.alfresco.versioning.AllVersions;
import it.bologna.comune.alfresco.versioning.GetVersions;
import it.bologna.comune.mongodb.search.metadata.SearchRequest;
import it.bologna.comune.mongodb.search.metadata.SearchResponse;
import it.bologna.comune.spagic.combo.protocollazione.Combos;
import it.bologna.comune.spagic.iperfascicolo.RequestCreazioneCollegamento;
import it.bologna.comune.spagic.iperfascicolo.RequestCreazioneCondivisione;
import it.eng.cobo.consolepec.commons.firmadigitale.FirmaDigitale;
import it.eng.consolepec.firmadigitale.FirmaDigitaleClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientErrorCode;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

/**
 *
 * Utilizzare NewSpagicClientSerializationUtil
 *
 */
public class SpagicClientSerializationUtil {

	private static Logger logger = LoggerFactory.getLogger(SpagicClientSerializationUtil.class);

	private static JAXBContext jaxbContextGenericRequest;
	private static JAXBContext jaxbContextVerificaFirma;
	private static JAXBContext jaxbContextVersion;
	private static JAXBContext jaxbContextCreateFolder;
	private static JAXBContext jaxbContextFirma;
	private static JAXBContext jaxbContextCreateFascicloloRequest;
	private static JAXBContext jaxbContextCreatePraticaModulisticaRequest;
	private static JAXBContext jaxbContextComboEprot;
	private static JAXBContext jaxbContextCreaBozza;
	private static JAXBContext jaxbContextProtocollazioneCompleta;
	private static JAXBContext jaxbContextFirmaMultipla;
	private static JAXBContext jaxbContexComboProtocollazione;
	private static JAXBContext jaxbContexElencoProtocollazione;
	private static JAXBContext jaxbContextProtocollazioneDettaglio;
	private static JAXBContext jaxbContextDownloadAttachment;
	private static JAXBContext jaxbContexInterrogaPG;
	private static JAXBContext jaxbContexVersamentoParerPG;
	private static JAXBContext jaxbContexRicercaCapofila;
	private static JAXBContext jaxbContexInserimentoNote;
	private static JAXBContext jaxbContexRecuperoPratiche;
	private static JAXBContext jaxbContextCambiaVisibilitaFascicolo;
	private static JAXBContext jaxbContextPubblicazioneAllegato;
	private static JAXBContext jaxbContextRimozionePubblicazioneAllegato;
	private static JAXBContext jaxbContextAggiuntaDatiAggiuntivi;
	private static JAXBContext jaxbContextRimozioneDatiAggiuntivi;
	private static JAXBContext jaxbContextIperfascicolo;
	private static JAXBContext jaxbContextTipologieProcedimenti;
	private static JAXBContext jaxbContextGestioneProcedimento;
	private static JAXBContext jaxbContextIterProcedimento;
	private static JAXBContext jaxbContextPropostaChiusuraProcedimento;
	private static JAXBContext jaxbContextRicercaMongoDb;
	private static JAXBContext jaxbContextCreaEstrazioneAmiantoRequest;
	private static JAXBContext jaxbContextAssegnaUtenteEsterno;
	private static JAXBContext jaxbContextStampe;
	private static JAXBContext jaxbContextElettorale;
	private static JAXBContext jaxbContextSearchPratiche;
	private static JAXBContext jaxbContextGestioneTemplate;
	private static JAXBContext jaxbContextComunicazione;
	private static JAXBContext jaxbContextGestioneTemplatePDF;
	private static JAXBContext jaxbContextRiassegna;

	/* Generic Request */
	private static JAXBContext getJaxbContextComunicazione() {
		if (jaxbContextComunicazione == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextComunicazione == null) {
					try {
						jaxbContextComunicazione = JAXBContext.newInstance(it.bologna.comune.spagic.ricerca.comunicazioni.Request.class, it.bologna.comune.spagic.ricerca.comunicazioni.Response.class,
								it.bologna.comune.spagic.gestione.comunicazione.Request.class, it.bologna.comune.alfresco.creazione.comunicazione.Request.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextComunicazione;
	}

	/* Generic Request */
	private static JAXBContext getJaxbContextGenericRequest() {
		if (jaxbContextGenericRequest == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextGenericRequest == null) {
					try {
						jaxbContextGenericRequest = JAXBContext.newInstance(it.bologna.comune.alfresco.generic.service.Response.class, it.bologna.comune.alfresco.generic.service.Request.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextGenericRequest;
	}

	/* Verifica Firma */
	private static JAXBContext getJaxbContextVerificaFirma() {
		if (jaxbContextVerificaFirma == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextVerificaFirma == null) {
					try {
						jaxbContextVerificaFirma = JAXBContext.newInstance(it.bologna.comune.alfresco.verifica.firma.SERVICERESPONSE.class, it.bologna.comune.alfresco.verifica.firma.REQUEST.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextVerificaFirma;
	}

	/* Versioni */
	private static JAXBContext getJaxbContextVersions() {
		if (jaxbContextVersion == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextVersion == null) {
					try {
						jaxbContextVersion = JAXBContext.newInstance(GetVersions.class, AllVersions.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextVersion;
	}

	/* Create Folder */
	private static JAXBContext getJaxbContextCreateFolder() {
		if (jaxbContextCreateFolder == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextCreateFolder == null) {
					try {
						jaxbContextCreateFolder = JAXBContext.newInstance(it.bologna.comune.alfresco.create.folder.REQUEST.class, it.bologna.comune.alfresco.create.folder.SERVICERESPONSE.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextCreateFolder;
	}

	/* Firma */
	private static JAXBContext getJaxbContextFirma() {
		if (jaxbContextFirma == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextFirma == null) {
					try {
						jaxbContextFirma = JAXBContext.newInstance(it.bologna.comune.alfresco.firma.digitale.REQUEST.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextFirma;
	}

	/* Firma */
	private static JAXBContext getJaxbContextFirmaMultipla() {
		if (jaxbContextFirmaMultipla == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextFirmaMultipla == null) {
					try {
						jaxbContextFirmaMultipla = JAXBContext.newInstance(it.bologna.comune.alfresco.firma.digitale.multipla.REQUEST.class,
								it.bologna.comune.alfresco.firma.digitale.multipla.SERVICERESPONSE.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextFirmaMultipla;
	}

	/* Crea Fascicolo */
	private static JAXBContext getJaxbContextCreaPraticaModulisticaRequest() {
		if (jaxbContextCreatePraticaModulisticaRequest == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextCreatePraticaModulisticaRequest == null) {
					try {
						jaxbContextCreatePraticaModulisticaRequest = JAXBContext.newInstance(it.bologna.comune.alfresco.creazione.modulistica.Request.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextCreatePraticaModulisticaRequest;
	}

	private static JAXBContext getJaxbContextCreaFascicolo() {
		if (jaxbContextCreateFascicloloRequest == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextCreateFascicloloRequest == null) {
					try {
						jaxbContextCreateFascicloloRequest = JAXBContext.newInstance(it.bologna.comune.alfresco.creazione.fascicolo.Request.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextCreateFascicloloRequest;
	}

	/* Combo Eprot */
	private static JAXBContext getJaxbContextComboEprot() {
		if (jaxbContextComboEprot == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextComboEprot == null) {
					try {
						jaxbContextComboEprot = JAXBContext.newInstance(it.bologna.comune.spagic.combo.protocollazione.Combos.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextComboEprot;
	}

	/* Crea Bozza */
	private static JAXBContext getJaxbContextCreaBozza() {
		if (jaxbContextCreaBozza == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextCreaBozza == null) {
					try {
						jaxbContextCreaBozza = JAXBContext.newInstance(it.bologna.comune.alfresco.create.draft.Request.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextCreaBozza;
	}

	/* Dati protocollazione completa */
	private static JAXBContext getJaxbContextProtocollazioneCompleta() {
		if (jaxbContextProtocollazioneCompleta == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextProtocollazioneCompleta == null) {
					try {
						jaxbContextProtocollazioneCompleta = JAXBContext.newInstance(it.bologna.comune.spagic.protocollazionecompleta.Request.class,
								it.bologna.comune.spagic.protocollazionecompleta.Response.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextProtocollazioneCompleta;
	}

	/* Combo protocollazione */
	private static JAXBContext getJaxbContexComboProtocollazione() {
		if (jaxbContexComboProtocollazione == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContexComboProtocollazione == null) {
					try {
						jaxbContexComboProtocollazione = JAXBContext.newInstance(Combos.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContexComboProtocollazione;
	}

	/* Elenco protocollazione */
	private static JAXBContext getJaxbContexElencoProtocollazione() {
		if (jaxbContexElencoProtocollazione == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContexElencoProtocollazione == null) {
					try {
						jaxbContexElencoProtocollazione = JAXBContext.newInstance(it.bologna.comune.alfresco.protocollazione.Request.class, it.bologna.comune.alfresco.protocollazione.Response.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContexElencoProtocollazione;
	}

	/* Dettaglio protocollazione */
	private static JAXBContext getJaxbContextProtocollazioneDettaglio() {
		if (jaxbContextProtocollazioneDettaglio == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextProtocollazioneDettaglio == null) {
					try {
						jaxbContextProtocollazioneDettaglio = JAXBContext.newInstance(it.bologna.comune.spagic.protocollazione.dettaglio.Request.class,
								it.bologna.comune.spagic.protocollazione.dettaglio.Response.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextProtocollazioneDettaglio;
	}

	/* Download Attachment */
	private static JAXBContext getJaxbContextDownloadAttachment() {
		if (jaxbContextDownloadAttachment == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextDownloadAttachment == null) {
					try {
						jaxbContextDownloadAttachment = JAXBContext.newInstance(it.bologna.comune.alfresco.download.attachment.REQUEST.class,
								it.bologna.comune.alfresco.download.attachment.SERVICERESPONSE.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextDownloadAttachment;
	}

	private static JAXBContext getJaxbContexInterrogaPG() {
		if (jaxbContexInterrogaPG == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContexInterrogaPG == null) {
					try {
						jaxbContexInterrogaPG = JAXBContext.newInstance(it.bologna.comune.spagic.interrogapg.Request.class, it.bologna.comune.spagic.interrogapg.Response.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}
		}
		return jaxbContexInterrogaPG;
	}

	private static JAXBContext getJaxbContexVersamentoParerPG() {
		if (jaxbContexVersamentoParerPG == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContexVersamentoParerPG == null) {
					try {
						jaxbContexVersamentoParerPG = JAXBContext.newInstance(it.bologna.comune.spagic.versamentoparerpg.Request.class, it.bologna.comune.spagic.versamentoparerpg.Response.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}
		}
		return jaxbContexVersamentoParerPG;
	}

	/* ricerca capofila */
	private static JAXBContext getJaxbContexRicercaCapofila() {
		if (jaxbContexRicercaCapofila == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContexRicercaCapofila == null) {
					try {
						jaxbContexRicercaCapofila = JAXBContext.newInstance(it.bologna.comune.spagic.ricercaba01.capofila.Request.class, it.bologna.comune.spagic.ricercaba01.capofila.Response.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}
		}
		return jaxbContexRicercaCapofila;
	}

	/* Inserimento Note */
	private static JAXBContext getJaxbContexInserimentoNote() {
		if (jaxbContexInserimentoNote == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContexInserimentoNote == null) {
					try {
						jaxbContexInserimentoNote = JAXBContext.newInstance(it.bologna.comune.spagic.ba01.inserimentonote.Request.class, it.bologna.comune.spagic.ba01.inserimentonote.Response.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}
		}
		return jaxbContexInserimentoNote;
	}

	/* Verifica Firma */
	private static JAXBContext getJaxbContextRecuperaPratiche() {
		if (jaxbContexRecuperoPratiche == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContexRecuperoPratiche == null) {
					try {
						jaxbContexRecuperoPratiche = JAXBContext.newInstance(it.bologna.comune.spagic.recuperapratiche.Request.class, it.bologna.comune.spagic.recuperapratiche.Response.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContexRecuperoPratiche;
	}

	/* Cambia visibilità fascicolo */
	private static JAXBContext getJaxbContextCambiaVisibilitaFascicolo() {
		if (jaxbContextCambiaVisibilitaFascicolo == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextCambiaVisibilitaFascicolo == null) {
					try {
						jaxbContextCambiaVisibilitaFascicolo = JAXBContext.newInstance(it.bologna.comune.spagic.cambiavisibilita.fascicolo.Request.class,
								it.bologna.comune.spagic.cambiavisibilita.fascicolo.Response.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}
		}
		return jaxbContextCambiaVisibilitaFascicolo;
	}

	/* Pubblicazione allegato */
	private static JAXBContext getJaxbContextPubblicazioneAllegato() {
		if (jaxbContextPubblicazioneAllegato == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextPubblicazioneAllegato == null) {
					try {
						jaxbContextPubblicazioneAllegato = JAXBContext.newInstance(it.bologna.comune.spagic.pubblicazioneallegato.Request.class,
								it.bologna.comune.spagic.pubblicazioneallegato.Response.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}
		}
		return jaxbContextPubblicazioneAllegato;
	}

	/* rimozione pubblicazione allegato */
	private static JAXBContext getJaxbContextRimozionePubblicazioneAllegato() {
		if (jaxbContextRimozionePubblicazioneAllegato == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextRimozionePubblicazioneAllegato == null) {
					try {
						jaxbContextRimozionePubblicazioneAllegato = JAXBContext.newInstance(it.bologna.comune.spagic.rimozionepubblicazioneallegato.Request.class,
								it.bologna.comune.spagic.rimozionepubblicazioneallegato.Response.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}
		}
		return jaxbContextRimozionePubblicazioneAllegato;
	}

	private static JAXBContext getJaxbContextAggiuntaDatiAggiuntivi() {
		if (jaxbContextAggiuntaDatiAggiuntivi == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextAggiuntaDatiAggiuntivi == null) {
					try {
						jaxbContextAggiuntaDatiAggiuntivi = JAXBContext.newInstance(it.bologna.comune.spagic.aggiuntadatiaggiuntivi.Request.class,
								it.bologna.comune.spagic.aggiuntadatiaggiuntivi.Response.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextAggiuntaDatiAggiuntivi;
	}

	private static JAXBContext getJaxbContextRimozioneDatiAggiuntivi() {
		if (jaxbContextRimozioneDatiAggiuntivi == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextRimozioneDatiAggiuntivi == null) {
					try {
						jaxbContextRimozioneDatiAggiuntivi = JAXBContext.newInstance(it.bologna.comune.spagic.rimozionedatiaggiuntivi.Request.class,
								it.bologna.comune.spagic.rimozionedatiaggiuntivi.Response.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextRimozioneDatiAggiuntivi;
	}

	private static JAXBContext getJaxbContextIperFascicolo() {
		if (jaxbContextIperfascicolo == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextIperfascicolo == null) {
					try {
						jaxbContextIperfascicolo = JAXBContext.newInstance(RequestCreazioneCondivisione.class, RequestCreazioneCollegamento.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextIperfascicolo;
	}

	private static JAXBContext getJaxbContextRecuperoTiplogie() {
		if (jaxbContextTipologieProcedimenti == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextTipologieProcedimenti == null) {
					try {
						jaxbContextTipologieProcedimenti = JAXBContext.newInstance(it.bologna.comune.spagic.procedimenti.tipologie.Request.class,
								it.bologna.comune.spagic.procedimenti.tipologie.Response.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextTipologieProcedimenti;
	}

	private static JAXBContext getJaxbContextGestioneProcedimento() {
		if (jaxbContextGestioneProcedimento == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextGestioneProcedimento == null) {
					try {
						jaxbContextGestioneProcedimento = JAXBContext.newInstance(it.bologna.comune.spagic.procedimenti.gestione.Request.class,
								it.bologna.comune.spagic.procedimenti.gestione.Response.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextGestioneProcedimento;
	}

	private static JAXBContext getJaxbContextIterProcedimento() {
		if (jaxbContextIterProcedimento == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextIterProcedimento == null) {
					try {
						jaxbContextIterProcedimento = JAXBContext.newInstance(it.bologna.comune.spagic.procedimenti.iter.Request.class, it.bologna.comune.spagic.procedimenti.iter.Response.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextIterProcedimento;
	}

	private static JAXBContext getJaxbContextPropostaChiusuraProcedimento() {
		if (jaxbContextPropostaChiusuraProcedimento == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextPropostaChiusuraProcedimento == null) {
					try {
						jaxbContextPropostaChiusuraProcedimento = JAXBContext.newInstance(it.bologna.comune.spagic.procedimenti.propostachiusura.Request.class,
								it.bologna.comune.spagic.procedimenti.propostachiusura.Response.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextPropostaChiusuraProcedimento;
	}

	private static JAXBContext getJaxbContextRicercaMongoDb() {
		if (jaxbContextRicercaMongoDb == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextRicercaMongoDb == null) {
					try {
						jaxbContextRicercaMongoDb = JAXBContext.newInstance(SearchRequest.class, SearchResponse.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextRicercaMongoDb;
	}

	private static JAXBContext getJaxbContextAssegnaUtenteEsterno() {
		if (jaxbContextAssegnaUtenteEsterno == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextAssegnaUtenteEsterno == null) {
					try {
						jaxbContextAssegnaUtenteEsterno = JAXBContext.newInstance(it.bologna.comune.spagic.assegnautenteesterno.Request.class,
								it.bologna.comune.spagic.assegnautenteesterno.Response.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextAssegnaUtenteEsterno;
	}

	private static JAXBContext getJaxbContextStampe() {
		if (jaxbContextStampe == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextStampe == null) {
					try {
						jaxbContextStampe = JAXBContext.newInstance(it.bologna.comune.spagic.stampe.Request.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}
		}
		return jaxbContextStampe;
	}

	private static JAXBContext getJaxbContextElettorale() {
		if (jaxbContextElettorale == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextElettorale == null) {
					try {
						jaxbContextElettorale = JAXBContext.newInstance(it.bologna.comune.spagic.elettorale.Request.class, it.bologna.comune.spagic.elettorale.Response.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextElettorale;
	}

	private static JAXBContext getJaxbContextSearchPratiche() {
		if (jaxbContextSearchPratiche == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextSearchPratiche == null) {
					try {
						jaxbContextSearchPratiche = JAXBContext.newInstance(it.bologna.comune.mongodb.search.pratiche.Request.class, it.bologna.comune.mongodb.search.pratiche.Response.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextSearchPratiche;
	}

	private static JAXBContext getJaxbContextGestioneTemplate() {
		if (jaxbContextGestioneTemplate == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextGestioneTemplate == null) {
					try {
						jaxbContextGestioneTemplate = JAXBContext.newInstance(it.bologna.comune.spagic.gestione.template.Request.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextGestioneTemplate;
	}

	private static JAXBContext getJaxbContextGestioneTemplatePDF() {
		if (jaxbContextGestioneTemplatePDF == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextGestioneTemplatePDF == null) {
					try {
						jaxbContextGestioneTemplatePDF = JAXBContext.newInstance(it.bologna.comune.spagic.gestione.template.TemplatePdfRequest.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextGestioneTemplatePDF;
	}

	private static JAXBContext getJaxbContextRiassegna() {
		if (jaxbContextRiassegna == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextRiassegna == null) {
					try {
						jaxbContextRiassegna = JAXBContext.newInstance(it.bologna.comune.spagic.riassegna.Request.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}
		}
		return jaxbContextRiassegna;
	}

	/**
	 * Request Generica
	 *
	 * @param xml
	 * @return
	 */
	public static Request getRequestToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextGenericRequest().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 * Request Generica
	 *
	 * @param xml
	 * @return
	 */
	public static String getRequestToString(Request request) {
		try {
			Marshaller marshaller = getJaxbContextGenericRequest().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 * Request Generica
	 *
	 * @param xml
	 * @return
	 */
	public static Response getResponseXmlToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextGenericRequest().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), Response.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 * Request Generica
	 *
	 * @param xml
	 * @return
	 */
	public static String getResponseToString(Response response) {
		try {
			Marshaller marshaller = getJaxbContextGenericRequest().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new ObjectFactory().createResponse(response), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 * Verifica firma
	 *
	 * @param request
	 * @return
	 */
	public static String getVerificaFirmaRequestToString(it.bologna.comune.alfresco.verifica.firma.REQUEST request) {
		try {
			Marshaller marshaller = getJaxbContextVerificaFirma().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.alfresco.verifica.firma.ObjectFactory().createREQUEST(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 * Verifica firma
	 *
	 * @param request
	 * @return
	 */
	public static it.bologna.comune.alfresco.verifica.firma.SERVICERESPONSE getVerificaFirmaServiceResponsetToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextVerificaFirma().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.alfresco.verifica.firma.SERVICERESPONSE.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 * Gestione firma digitale raggruppata
	 */
	public static FirmaDigitale getVerificaFirmaResponsetToObject(String xml) {
		return FirmaDigitaleClient.cast(getVerificaFirmaServiceResponsetToObject(xml));
	}

	/**
	 *
	 * Versione
	 *
	 * @param xml
	 * @return
	 */
	public static AllVersions getAllVersionsToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextVersions().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), AllVersions.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 *
	 * Versione
	 *
	 * @param xml
	 * @return
	 */
	public static GetVersions getVersionsToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextVersions().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), GetVersions.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 *
	 * Versione
	 *
	 * @param xml
	 * @return
	 */
	public static String getVersionsToXml(GetVersions getVersions) {
		try {
			Marshaller marshaller = getJaxbContextVersions().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.alfresco.versioning.ObjectFactory().createGetVersions(getVersions), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 *
	 * Versione
	 *
	 * @param xml
	 * @return
	 */
	public static String getAllVersionsToXml(AllVersions allVersions) {
		try {
			Marshaller marshaller = getJaxbContextVersions().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.alfresco.versioning.ObjectFactory().createAllVersions(allVersions), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 * Create Folder
	 *
	 * @param request
	 * @return
	 */
	public static String getCreateFolderRequestToString(REQUEST request) {
		try {
			Marshaller marshaller = getJaxbContextCreateFolder().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.alfresco.create.folder.ObjectFactory().createREQUEST(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 * Servizio di Firma
	 *
	 * @param request
	 * @return
	 */
	public static String getFirmaRequestToString(it.bologna.comune.alfresco.firma.digitale.REQUEST request) {
		try {
			Marshaller marshaller = getJaxbContextFirma().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.alfresco.firma.digitale.ObjectFactory().createREQUEST(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	public static String getFirmaRequestToString(it.bologna.comune.alfresco.firma.digitale.multipla.REQUEST request) {
		try {
			Marshaller marshaller = getJaxbContextFirmaMultipla().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.alfresco.firma.digitale.multipla.ObjectFactory().createREQUEST(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 *
	 * @param xml
	 * @return
	 */
	public static it.bologna.comune.alfresco.firma.digitale.multipla.SERVICERESPONSE getFirmaResponseToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextFirmaMultipla().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.alfresco.firma.digitale.multipla.SERVICERESPONSE.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 * Creazione modulistica
	 *
	 * @param combos
	 * @return
	 */
	public static String getCreatePraticaModulisticaRequestToString(it.bologna.comune.alfresco.creazione.modulistica.Request request) {
		try {
			Marshaller marshaller = getJaxbContextCreaPraticaModulisticaRequest().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.alfresco.creazione.modulistica.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 * Creazione modulistica
	 *
	 * @param xml
	 * @return
	 */
	public static it.bologna.comune.alfresco.creazione.modulistica.Request getCreatePraticaModulisticaRequestToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextCreaPraticaModulisticaRequest().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.alfresco.creazione.modulistica.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 * Creazione fascicolo
	 *
	 * @param combos
	 * @return
	 */
	public static String getCreateFascicoloRequestToString(it.bologna.comune.alfresco.creazione.fascicolo.Request request) {
		try {
			Marshaller marshaller = getJaxbContextCreaFascicolo().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.alfresco.creazione.fascicolo.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 * Creazione fascicolo
	 *
	 * @param xml
	 * @return
	 */
	public static it.bologna.comune.alfresco.creazione.fascicolo.Request getCreateFascicoloRequestToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextCreaFascicolo().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.alfresco.creazione.fascicolo.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 * Recupero combo boxes e-protocollo
	 *
	 * @param combos
	 * @return
	 */
	public static String getComboBoxesRequestToString(Combos combos) {
		try {
			Marshaller marshaller = getJaxbContextComboEprot().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.combo.protocollazione.ObjectFactory().createRequest(combos), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 * Creazione Bozza
	 *
	 * @param xml
	 * @return
	 */
	public static it.bologna.comune.alfresco.create.draft.Request getCreazioneBozzaXmlToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextCreaBozza().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.alfresco.create.draft.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	public static String getCreazioneBozzaObjectToXml(it.bologna.comune.alfresco.create.draft.Request request) {
		try {
			Marshaller marshaller = getJaxbContextCreaBozza().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.alfresco.create.draft.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 * Protocollazione Completa
	 *
	 * @param xml
	 * @return
	 */
	public static it.bologna.comune.spagic.protocollazionecompleta.Request getRequestProtocollazioneCompletaToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextProtocollazioneCompleta().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.protocollazionecompleta.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 * Protocollazione Completa
	 *
	 * @param xml
	 * @return
	 */
	public static it.bologna.comune.spagic.protocollazionecompleta.Response getResponseProtocollazioneCompletaToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextProtocollazioneCompleta().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.protocollazionecompleta.Response.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 * Protocollazione Completa
	 *
	 * @param xml
	 * @return
	 */
	public static String getRequestProtocollazioneCompletaToString(it.bologna.comune.spagic.protocollazionecompleta.Request protocollazione) {
		try {
			Marshaller marshaller = getJaxbContextProtocollazioneCompleta().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.protocollazionecompleta.ObjectFactory().createRequest(protocollazione), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	public static String getProtocollazioneDettaglioRequestToXml(it.bologna.comune.spagic.protocollazione.dettaglio.Request request) {
		try {
			Marshaller marshaller = getJaxbContextProtocollazioneDettaglio().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.protocollazione.dettaglio.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 *
	 * @param xml
	 * @return
	 */
	public static it.bologna.comune.spagic.protocollazione.dettaglio.Response getProtocollazioneDettaglioResponseToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextProtocollazioneDettaglio().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.protocollazione.dettaglio.Response.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	public static String getDownloadRequestToString(it.bologna.comune.alfresco.download.attachment.REQUEST request) {
		try {
			Marshaller marshaller = getJaxbContextDownloadAttachment().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.alfresco.download.attachment.ObjectFactory().createREQUEST(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 *
	 * @param xml
	 * @return
	 */
	public static it.bologna.comune.alfresco.download.attachment.SERVICERESPONSE getDownloadResponseToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextDownloadAttachment().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.alfresco.download.attachment.SERVICERESPONSE.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	public static String getElencoProtocollazioneRequestToString(it.bologna.comune.alfresco.protocollazione.Request request) {
		try {
			Marshaller marshaller = getJaxbContexElencoProtocollazione().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.alfresco.protocollazione.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 *
	 * @param xml
	 * @return
	 */
	public static it.bologna.comune.alfresco.protocollazione.Response getElencoProtocollazioneResponseToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContexElencoProtocollazione().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.alfresco.protocollazione.Response.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 *
	 * @param xml
	 * @return
	 */
	public static Combos getComboBoxesRequestToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContexComboProtocollazione().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), Combos.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	// Interroga PG
	public static it.bologna.comune.spagic.interrogapg.Request getRequestInterrogaPGToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContexInterrogaPG().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.interrogapg.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.interrogapg.Response getResponseInterrogaPGToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContexInterrogaPG().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.interrogapg.Response.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getRequestInterrogaPGToString(it.bologna.comune.spagic.interrogapg.Request request) {
		try {
			Marshaller marshaller = getJaxbContexInterrogaPG().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.interrogapg.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getResponseInterrogaPGToString(it.bologna.comune.spagic.interrogapg.Response response) {
		try {
			Marshaller marshaller = getJaxbContexInterrogaPG().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.interrogapg.ObjectFactory().createResponse(response), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/* Versamento Parer PG al Protocollo */
	public static it.bologna.comune.spagic.versamentoparerpg.Request getRequestVersamentoParerPGToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContexVersamentoParerPG().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.versamentoparerpg.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.versamentoparerpg.Response getResponseVersamentoParerPGToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContexVersamentoParerPG().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.versamentoparerpg.Response.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getRequestVersamentoParerPGToString(it.bologna.comune.spagic.versamentoparerpg.Request request) {
		try {
			Marshaller marshaller = getJaxbContexVersamentoParerPG().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.versamentoparerpg.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getResponseVersamentoParerPGToString(it.bologna.comune.spagic.versamentoparerpg.Response response) {
		try {
			Marshaller marshaller = getJaxbContexVersamentoParerPG().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.versamentoparerpg.ObjectFactory().createResponse(response), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/* Ricerca Capofila */
	public static it.bologna.comune.spagic.ricercaba01.capofila.Request getRicercaCapofilaRequestToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContexRicercaCapofila().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.ricercaba01.capofila.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.ricercaba01.capofila.Response getRicercaCapofilaResponseToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContexRicercaCapofila().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.ricercaba01.capofila.Response.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getRequestRicercaCapofilaToString(it.bologna.comune.spagic.ricercaba01.capofila.Request request) {
		try {
			Marshaller marshaller = getJaxbContexRicercaCapofila().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.ricercaba01.capofila.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getResponseRicercaCapofilaToString(it.bologna.comune.spagic.ricercaba01.capofila.Response response) {
		try {
			Marshaller marshaller = getJaxbContexRicercaCapofila().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.ricercaba01.capofila.ObjectFactory().createResponse(response), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getResponseInserimentoNoteToString(it.bologna.comune.spagic.ba01.inserimentonote.Response response) {
		try {
			Marshaller marshaller = getJaxbContexInserimentoNote().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.ba01.inserimentonote.ObjectFactory().createResponse(response), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getRequestInserimentoNoteToString(it.bologna.comune.spagic.ba01.inserimentonote.Request request) {
		try {
			Marshaller marshaller = getJaxbContexInserimentoNote().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.ba01.inserimentonote.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.ba01.inserimentonote.Request getRequestInserimentoNoteToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContexInserimentoNote().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.ba01.inserimentonote.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.ba01.inserimentonote.Response getResponseInserimentoNoteToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContexInserimentoNote().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.ba01.inserimentonote.Response.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.recuperapratiche.Request getRequestRecuperaPraticheToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextRecuperaPratiche().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.recuperapratiche.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.recuperapratiche.Response getResponseRecuperaPraticheToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextRecuperaPratiche().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.recuperapratiche.Response.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.cambiavisibilita.fascicolo.Request getRequestCambiaVisibilitaFascicoloToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextCambiaVisibilitaFascicolo().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.cambiavisibilita.fascicolo.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.cambiavisibilita.fascicolo.Response getResponseCambiaVisibilitaFascicoloToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextCambiaVisibilitaFascicolo().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.cambiavisibilita.fascicolo.Response.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getResponseRecuperaPraticheToString(it.bologna.comune.spagic.recuperapratiche.Response response) {
		try {
			Marshaller marshaller = getJaxbContextRecuperaPratiche().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.recuperapratiche.ObjectFactory().createResponse(response), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getRequestRecuperaPraticheToString(it.bologna.comune.spagic.recuperapratiche.Request request) {
		try {
			Marshaller marshaller = getJaxbContextRecuperaPratiche().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.recuperapratiche.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getResponseCambiaVisibilitaFascicoloToString(it.bologna.comune.spagic.cambiavisibilita.fascicolo.Response response) {
		try {
			Marshaller marshaller = getJaxbContextCambiaVisibilitaFascicolo().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.cambiavisibilita.fascicolo.ObjectFactory().createResponse(response), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	// pubblicazione allegato

	public static it.bologna.comune.spagic.pubblicazioneallegato.Request getRequestPubblicazioneAllegatoToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextPubblicazioneAllegato().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.pubblicazioneallegato.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getRequestCambiaVisibilitaFascicoloString(it.bologna.comune.spagic.cambiavisibilita.fascicolo.Request request) {
		try {
			Marshaller marshaller = getJaxbContextCambiaVisibilitaFascicolo().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.cambiavisibilita.fascicolo.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.pubblicazioneallegato.Response getResponsePubblicazioneAllegatoToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextPubblicazioneAllegato().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.pubblicazioneallegato.Response.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getResponsePubblicazioneAllegatoToString(it.bologna.comune.spagic.pubblicazioneallegato.Response response) {
		try {
			Marshaller marshaller = getJaxbContextPubblicazioneAllegato().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.pubblicazioneallegato.ObjectFactory().createResponse(response), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getRequestPubblicazioneAllegatoToString(it.bologna.comune.spagic.pubblicazioneallegato.Request request) {
		try {
			Marshaller marshaller = getJaxbContextPubblicazioneAllegato().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.pubblicazioneallegato.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	// rimozione pubblicazione allegato

	public static it.bologna.comune.spagic.rimozionepubblicazioneallegato.Request getRequestRimozionePubblicazioneAllegatoToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextRimozionePubblicazioneAllegato().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.rimozionepubblicazioneallegato.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.rimozionepubblicazioneallegato.Response getResponseRimozionePubblicazioneAllegatoToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextRimozionePubblicazioneAllegato().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.rimozionepubblicazioneallegato.Response.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getResponseRimozionePubblicazioneAllegatoToString(it.bologna.comune.spagic.rimozionepubblicazioneallegato.Response response) {
		try {
			Marshaller marshaller = getJaxbContextRimozionePubblicazioneAllegato().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.rimozionepubblicazioneallegato.ObjectFactory().createResponse(response), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getRequestRimozionePubblicazioneAllegatoToString(it.bologna.comune.spagic.rimozionepubblicazioneallegato.Request request) {
		try {
			Marshaller marshaller = getJaxbContextRimozionePubblicazioneAllegato().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.rimozionepubblicazioneallegato.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getRequestAggiuntaDatiAggiuntiviToString(it.bologna.comune.spagic.aggiuntadatiaggiuntivi.Request request) {
		try {
			Marshaller marshaller = getJaxbContextAggiuntaDatiAggiuntivi().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.aggiuntadatiaggiuntivi.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getResponseAggiuntaDatiAggiuntiviToString(it.bologna.comune.spagic.aggiuntadatiaggiuntivi.Response response) {
		try {
			Marshaller marshaller = getJaxbContextAggiuntaDatiAggiuntivi().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.aggiuntadatiaggiuntivi.ObjectFactory().createResponse(response), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.aggiuntadatiaggiuntivi.Request getRequestAggiuntaDatiAggiuntiviToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextAggiuntaDatiAggiuntivi().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.aggiuntadatiaggiuntivi.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.aggiuntadatiaggiuntivi.Response getResponseAggiuntaDatiAggiuntiviToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextAggiuntaDatiAggiuntivi().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.aggiuntadatiaggiuntivi.Response.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getRequestRimozioneDatiAggiuntiviToString(it.bologna.comune.spagic.rimozionedatiaggiuntivi.Request request) {
		try {
			Marshaller marshaller = getJaxbContextRimozioneDatiAggiuntivi().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.rimozionedatiaggiuntivi.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getResponseRimozioneDatiAggiuntiviToString(it.bologna.comune.spagic.rimozionedatiaggiuntivi.Response response) {
		try {
			Marshaller marshaller = getJaxbContextRimozioneDatiAggiuntivi().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.rimozionedatiaggiuntivi.ObjectFactory().createResponse(response), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.rimozionedatiaggiuntivi.Request getRequestRimozioneDatiAggiuntiviToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextRimozioneDatiAggiuntivi().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.rimozionedatiaggiuntivi.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.rimozionedatiaggiuntivi.Response getResponseRimozioneDatiAggiuntiviToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextRimozioneDatiAggiuntivi().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.rimozionedatiaggiuntivi.Response.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/* IPERFASCICOLO */
	public static String getRequestCreaCollegamentoToString(RequestCreazioneCollegamento request) {
		try {
			Marshaller marshaller = getJaxbContextIperFascicolo().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.iperfascicolo.ObjectFactory().createRequestCreazioneCollegamento(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getRequestCreaCondivisioneToString(RequestCreazioneCondivisione request) {
		try {
			Marshaller marshaller = getJaxbContextIperFascicolo().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.iperfascicolo.ObjectFactory().createRequestCreazioneCondivisione(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static RequestCreazioneCollegamento getRequestCreazioneCollegamentoToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextIperFascicolo().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), RequestCreazioneCollegamento.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static RequestCreazioneCondivisione getRequestCreazioneCondivisioneToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextIperFascicolo().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), RequestCreazioneCondivisione.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getTipologieProcedimentiRequestToXml(it.bologna.comune.spagic.procedimenti.tipologie.Request request) {
		try {
			Marshaller marshaller = getJaxbContextRecuperoTiplogie().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.procedimenti.tipologie.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getTipologieProcedimentiResponseToXml(it.bologna.comune.spagic.procedimenti.tipologie.Response response) {
		try {
			Marshaller marshaller = getJaxbContextRecuperoTiplogie().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.procedimenti.tipologie.ObjectFactory().createResponse(response), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.procedimenti.tipologie.Response getTipologieProcedimentiResponseToObject(String response) {
		try {
			Unmarshaller unmarshaller = getJaxbContextRecuperoTiplogie().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(response.getBytes("utf-8"))), it.bologna.comune.spagic.procedimenti.tipologie.Response.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getRequestGestioneProcedimentoToXml(it.bologna.comune.spagic.procedimenti.gestione.Request request) {
		try {
			Marshaller marshaller = getJaxbContextGestioneProcedimento().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.procedimenti.gestione.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getResponseGestioneProcedimentoToXml(it.bologna.comune.spagic.procedimenti.gestione.Response response) {
		try {
			Marshaller marshaller = getJaxbContextGestioneProcedimento().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.procedimenti.gestione.ObjectFactory().createResponse(response), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.procedimenti.gestione.Request getRequestGestioneProcedimentoToObject(String request) {
		try {
			Unmarshaller unmarshaller = getJaxbContextGestioneProcedimento().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(request.getBytes("utf-8"))), it.bologna.comune.spagic.procedimenti.gestione.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.procedimenti.gestione.Response getResponseGestioneProcedimentoToObject(String response) {
		try {
			Unmarshaller unmarshaller = getJaxbContextGestioneProcedimento().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(response.getBytes("utf-8"))), it.bologna.comune.spagic.procedimenti.gestione.Response.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getRequestIterProcedimentoToXml(it.bologna.comune.spagic.procedimenti.iter.Request request) {
		try {
			Marshaller marshaller = getJaxbContextIterProcedimento().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.procedimenti.iter.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getResponseIterProcedimentoToXml(it.bologna.comune.spagic.procedimenti.iter.Response response) {
		try {
			Marshaller marshaller = getJaxbContextIterProcedimento().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.procedimenti.iter.ObjectFactory().createResponse(response), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.procedimenti.iter.Request getRequestIterProcedimentoToObject(String request) {
		try {
			Unmarshaller unmarshaller = getJaxbContextIterProcedimento().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(request.getBytes("utf-8"))), it.bologna.comune.spagic.procedimenti.iter.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.procedimenti.iter.Response getResponseIterProcedimentoToObject(String response) {
		try {
			Unmarshaller unmarshaller = getJaxbContextIterProcedimento().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(response.getBytes("utf-8"))), it.bologna.comune.spagic.procedimenti.iter.Response.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getRequestPropostaChiusuraProcedimentoToXml(it.bologna.comune.spagic.procedimenti.propostachiusura.Request request) {
		try {
			Marshaller marshaller = getJaxbContextPropostaChiusuraProcedimento().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.procedimenti.propostachiusura.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getResponsePropostaChiusuraProcedimentoToXml(it.bologna.comune.spagic.procedimenti.propostachiusura.Response response) {
		try {
			Marshaller marshaller = getJaxbContextPropostaChiusuraProcedimento().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.procedimenti.propostachiusura.ObjectFactory().createResponse(response), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.procedimenti.propostachiusura.Request getRequestPropostaChiusuraProcedimentoToObject(String request) {
		try {
			Unmarshaller unmarshaller = getJaxbContextPropostaChiusuraProcedimento().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(request.getBytes("utf-8"))), it.bologna.comune.spagic.procedimenti.propostachiusura.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.procedimenti.propostachiusura.Response getResponsePropostaChiusuraProcedimentoToObject(String response) {
		try {
			Unmarshaller unmarshaller = getJaxbContextPropostaChiusuraProcedimento().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(response.getBytes("utf-8"))), it.bologna.comune.spagic.procedimenti.propostachiusura.Response.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getSearchRequestToString(SearchRequest request) {
		try {
			Marshaller marshaller = getJaxbContextRicercaMongoDb().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.mongodb.search.metadata.ObjectFactory().createSearchRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getSearchResponseToString(SearchResponse response) {
		try {
			Marshaller marshaller = getJaxbContextRicercaMongoDb().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.mongodb.search.metadata.ObjectFactory().createSearchResponse(response), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static SearchRequest getSearchRequestToObject(String request) {
		try {
			Unmarshaller unmarshaller = getJaxbContextRicercaMongoDb().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(request.getBytes("utf-8"))), SearchRequest.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static SearchResponse getSearchResponseToObject(String response) {
		try {
			Unmarshaller unmarshaller = getJaxbContextRicercaMongoDb().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(response.getBytes("utf-8"))), SearchResponse.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	/**
	 * Estrazione fascicolo amianto
	 *
	 * @param combos
	 * @return
	 */
	public static String getCreateEstrazioneAmiantoRequestToString(it.bologna.comune.mongodb.estrazioni.amianto.EstrazioneAmiantoRequest request) {
		try {
			Marshaller marshaller = getJaxbContextCreaEstrazioneAmianto().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.mongodb.estrazioni.amianto.ObjectFactory().createEstrazioneAmiantoRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.mongodb.estrazioni.amianto.EstrazioneAmiantoRequest getRequestEstrazioneAmiantoToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextCreaEstrazioneAmianto().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.mongodb.estrazioni.amianto.EstrazioneAmiantoRequest.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	private static JAXBContext getJaxbContextCreaEstrazioneAmianto() {
		if (jaxbContextCreaEstrazioneAmiantoRequest == null) {
			synchronized (SpagicClientSerializationUtil.class) {
				if (jaxbContextCreaEstrazioneAmiantoRequest == null) {
					try {
						jaxbContextCreaEstrazioneAmiantoRequest = JAXBContext.newInstance(it.bologna.comune.mongodb.estrazioni.amianto.EstrazioneAmiantoRequest.class,
								it.bologna.comune.mongodb.estrazioni.amianto.EstrazioneAmiantoResponse.class);
					} catch (JAXBException e) {
						logger.info("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}

		}
		return jaxbContextCreaEstrazioneAmiantoRequest;
	}

	public static String getResponseEstrazioneAmiantoToString(it.bologna.comune.mongodb.estrazioni.amianto.EstrazioneAmiantoResponse response) {
		try {
			Marshaller marshaller = getJaxbContextCreaEstrazioneAmianto().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.mongodb.estrazioni.amianto.ObjectFactory().createEstrazioneAmiantoResponse(response), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.mongodb.estrazioni.amianto.EstrazioneAmiantoResponse getResponseEstrazioneAmiantoToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextCreaEstrazioneAmianto().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.mongodb.estrazioni.amianto.EstrazioneAmiantoResponse.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getRequestAssegnaUtenteEsternoToString(it.bologna.comune.spagic.assegnautenteesterno.Request request) {
		try {
			Marshaller marshaller = getJaxbContextAssegnaUtenteEsterno().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.assegnautenteesterno.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getResponseAssegnaUtenteEsternoToString(it.bologna.comune.spagic.assegnautenteesterno.Response response) {
		try {
			Marshaller marshaller = getJaxbContextAssegnaUtenteEsterno().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.assegnautenteesterno.ObjectFactory().createResponse(response), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.assegnautenteesterno.Request getRequestAssegnaUtenteEsternoToObject(String request) {
		try {
			Unmarshaller unmarshaller = getJaxbContextAssegnaUtenteEsterno().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(request.getBytes("utf-8"))), it.bologna.comune.spagic.assegnautenteesterno.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.assegnautenteesterno.Response getResponseAssegnaUtenteEsternoToObject(String response) {
		try {
			Unmarshaller unmarshaller = getJaxbContextAssegnaUtenteEsterno().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(response.getBytes("utf-8"))), it.bologna.comune.spagic.assegnautenteesterno.Response.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getRequestDownloadStampeToString(it.bologna.comune.spagic.stampe.Request request) {
		try {
			Marshaller marshaller = getJaxbContextStampe().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.stampe.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.stampe.Request getRequestDownloadStampeToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextStampe().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.stampe.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	// elettorale

	public static String getRequestElettoraleToString(it.bologna.comune.spagic.elettorale.Request request) {
		try {
			Marshaller marshaller = getJaxbContextElettorale().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.elettorale.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getResponseElettoraleToString(it.bologna.comune.spagic.elettorale.Response response) {
		try {
			Marshaller marshaller = getJaxbContextElettorale().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.elettorale.ObjectFactory().createResponse(response), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.elettorale.Request getRequestElettoraleToObject(String request) {
		try {
			Unmarshaller unmarshaller = getJaxbContextElettorale().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(request.getBytes("utf-8"))), it.bologna.comune.spagic.elettorale.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.elettorale.Response getResponseElettoraleToObject(String response) {
		try {
			Unmarshaller unmarshaller = getJaxbContextElettorale().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(response.getBytes("utf-8"))), it.bologna.comune.spagic.elettorale.Response.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	// mongo

	public static String getRequestSearchPraticheToString(it.bologna.comune.mongodb.search.pratiche.Request request) {
		try {
			Marshaller marshaller = getJaxbContextSearchPratiche().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.mongodb.search.pratiche.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getResponseSearchPraticheToString(it.bologna.comune.mongodb.search.pratiche.Response response) {
		try {
			Marshaller marshaller = getJaxbContextSearchPratiche().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.mongodb.search.pratiche.ObjectFactory().createResponse(response), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.mongodb.search.pratiche.Request getRequestSearchPraticheToObject(String request) {
		try {
			Unmarshaller unmarshaller = getJaxbContextSearchPratiche().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(request.getBytes("utf-8"))), it.bologna.comune.mongodb.search.pratiche.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.mongodb.search.pratiche.Response getResponseSearchPraticheToObject(String response) {
		try {
			Unmarshaller unmarshaller = getJaxbContextSearchPratiche().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(response.getBytes("utf-8"))), it.bologna.comune.mongodb.search.pratiche.Response.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getRequestGestioneTemplateToString(it.bologna.comune.spagic.gestione.template.Request request) {
		try {
			Marshaller marshaller = getJaxbContextGestioneTemplate().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.gestione.template.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getRequestGestioneTemplatePDFToString(it.bologna.comune.spagic.gestione.template.TemplatePdfRequest request) {
		try {
			Marshaller marshaller = getJaxbContextGestioneTemplatePDF().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.gestione.template.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.gestione.template.Request getRequestGestioneTemplateToObject(String request) {
		try {
			Unmarshaller unmarshaller = getJaxbContextGestioneTemplate().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(request.getBytes("utf-8"))), it.bologna.comune.spagic.gestione.template.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.gestione.template.TemplatePdfRequest getRequestGestioneTemplatePDFToObject(String request) {
		try {
			Unmarshaller unmarshaller = getJaxbContextGestioneTemplatePDF().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(request.getBytes("utf-8"))), it.bologna.comune.spagic.gestione.template.TemplatePdfRequest.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getRequestRicercaComunicazioniToString(it.bologna.comune.spagic.ricerca.comunicazioni.Request request) {
		try {
			Marshaller marshaller = getJaxbContextComunicazione().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.ricerca.comunicazioni.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.ricerca.comunicazioni.Request getRequestRicercaComunicazioniToObject(String request) {
		try {
			Unmarshaller unmarshaller = getJaxbContextGestioneTemplate().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(request.getBytes("utf-8"))), it.bologna.comune.spagic.ricerca.comunicazioni.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getResponseRicercaComunicazioniToString(it.bologna.comune.spagic.ricerca.comunicazioni.Response response) {
		try {
			Marshaller marshaller = getJaxbContextComunicazione().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.ricerca.comunicazioni.ObjectFactory().createResponse(response), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.ricerca.comunicazioni.Response getResponseRicercaComunicazioniToObject(String response) {
		try {
			Unmarshaller unmarshaller = getJaxbContextComunicazione().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(response.getBytes("utf-8"))), it.bologna.comune.spagic.ricerca.comunicazioni.Response.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getRequestGestioneComunicazioniToString(it.bologna.comune.spagic.gestione.comunicazione.Request request) {
		try {
			Marshaller marshaller = getJaxbContextComunicazione().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.gestione.comunicazione.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.gestione.comunicazione.Request getRequestGestioneComunicazioniToObject(String request) {
		try {
			Unmarshaller unmarshaller = getJaxbContextComunicazione().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(request.getBytes("utf-8"))), it.bologna.comune.spagic.gestione.comunicazione.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getCreateComunicazioneRequestToString(it.bologna.comune.alfresco.creazione.comunicazione.Request request) {
		try {
			Marshaller marshaller = getJaxbContextComunicazione().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.alfresco.creazione.comunicazione.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.alfresco.creazione.comunicazione.Request getCreateComunicazioneRequestToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextComunicazione().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.alfresco.creazione.comunicazione.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static it.bologna.comune.spagic.riassegna.Request getRequestRiassegnaToObject(String xml) {
		try {
			Unmarshaller unmarshaller = getJaxbContextRiassegna().createUnmarshaller();
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes("utf-8"))), it.bologna.comune.spagic.riassegna.Request.class).getValue();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}

	public static String getRequestRiassegnaToString(it.bologna.comune.spagic.riassegna.Request request) {
		try {
			Marshaller marshaller = getJaxbContextRiassegna().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter sw = new StringWriter();
			marshaller.marshal(new it.bologna.comune.spagic.riassegna.ObjectFactory().createRequest(request), sw);
			return sw.toString();
		} catch (Exception e) {
			throw new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}
	}
}
