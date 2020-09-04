package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata;
import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.IncollaAllegatoHandler;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Protocollazione;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.IncollaProtocollazioniTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TagliaProtocollazioniTaskApi.ElementiProtocollazione;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TagliaProtocollazioniTaskApi.ProtocollazioneCapofilaOutput;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TagliaProtocollazioniTaskApi.ProtocollazioneCapofilaOutput.ProtocollazioneCollegataContainer;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TagliaProtocollazioniTaskApi.ProtocollazioneCollegataOutput;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TagliaProtocollazioniTaskApi.TagliaProtocollazioniOutput;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.util.TaskDiFirmaUtil;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

/**
 * 
 * @author biagiot
 *
 * @param <T>
 */
public class IncollaProtocollazioniTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements IncollaProtocollazioniTaskApi {

	public IncollaProtocollazioniTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void incollaProtocollazioni(TagliaProtocollazioniOutput tagliaProtocollazioniOutput, Pratica<?> praticaSorgente, IncollaAllegatoHandler allegatoHandler) throws Exception {
		incollaProtocollazioni(tagliaProtocollazioniOutput.getProtocollazioniCapofilaOutput(), tagliaProtocollazioniOutput.getProtocollazioniCollegateOutput(),
				tagliaProtocollazioniOutput.getPraticheDaCollegare(), allegatoHandler, praticaSorgente);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return Stato.IN_GESTIONE.equals(getDatiFascicolo().getStato());
	}

	@Override
	protected ITipoApiTask getTipoApiTask() {
		return TipoApiTask.INCOLLA_PROTOCOLLAZIONI;
	}

	private void incollaProtocollazioni(List<ProtocollazioneCapofilaOutput> protocollazioniCapofilaOutput, List<ProtocollazioneCollegataOutput> protocollazioniCollegateOutput,
			List<Pratica<?>> praticheDaCollegare, IncollaAllegatoHandler allegatoHandler, Pratica<?> praticaSorgente) throws Exception {

		incollaCapofila(protocollazioniCapofilaOutput, praticheDaCollegare, allegatoHandler, praticaSorgente);
		incollaCollegati(protocollazioniCollegateOutput, praticheDaCollegare, allegatoHandler, praticaSorgente);
	}

	private void incollaCapofila(List<ProtocollazioneCapofilaOutput> protocollazioniCapofilaOutput, List<Pratica<?>> praticheDaCollegare, IncollaAllegatoHandler allegatoHandler,
			Pratica<?> praticaSorgente) throws Exception {

		for (ProtocollazioneCapofilaOutput entry : protocollazioniCapofilaOutput) {

			ProtocollazioneCapofila capofilaDaIncollare = entry.getProtocollazioneCapofila();
			Map<ProtocollazioneCollegataContainer, ElementiProtocollazione> protocollazioniMap = entry.getElementiProtocollati();

			List<Allegato> allegatiCapofila = new ArrayList<Allegato>();
			List<PraticaCollegata> praticheCapofila = new ArrayList<PraticaCollegata>();
			for (Entry<ProtocollazioneCollegataContainer, ElementiProtocollazione> e : protocollazioniMap.entrySet()) {
				if (!e.getKey().isExists()) {
					allegatiCapofila.addAll(e.getValue().getAllegatiProtocollati());
					praticheCapofila.addAll(e.getValue().getPraticheProtocollate());
				}
			}

			boolean capofilaFound = false;

			for (ProtocollazioneCapofila capofilaFascicolo : getDatiFascicolo().getProtocollazioniCapofila()) {

				if (XmlPluginUtil.isSame(capofilaFascicolo, capofilaDaIncollare)) {
					// c'è già la prot. capofila (perchè c'è una protocollazione collegata)
					capofilaFound = true;
					if (!capofilaFascicolo.getAllegatiProtocollati().isEmpty() || !capofilaFascicolo.getPraticheCollegateProtocollate().isEmpty())
						throw new PraticaException("Impossibile modificare la protocollazione capofila " + capofilaFascicolo.getNumeroPG() + "/" + capofilaFascicolo.getAnnoPG());

					// Allegati protocollati capofila
					for (Allegato allegato : allegatiCapofila) {
						String nome = getNomeAllegato(allegato);
						Allegato incollato = allegatoHandler.incollaAllegato(nome, allegato);
						TaskDiFirmaUtil.invalidaTaskFirmaPrecedentiConclusi(incollato, task.getEnclosingPratica());
						capofilaFascicolo.getAllegatiProtocollati().add(incollato);
						generaEvento(EventiIterFascicolo.INCOLLA_ALLEGATO_PROTOCOLLATO, task.getCurrentUser(), incollato.getNome(), capofilaFascicolo.getNumeroPG(),
								capofilaFascicolo.getAnnoPG().toString(), praticaSorgente.getDati().getIdDocumentale());
					}

					// Pratiche capofila
					for (PraticaCollegata praticaCollegata : praticheCapofila) {
						capofilaFascicolo.getPraticheCollegateProtocollate().add(praticaCollegata);
						creaCollegamento(praticaCollegata, praticheDaCollegare);
						generaEvento(EventiIterFascicolo.INCOLLA_PRATICA_PROTOCOLLATA, task.getCurrentUser(), praticaCollegata.getAlfrescoPath().split("/")[4], capofilaFascicolo.getNumeroPG(),
								capofilaFascicolo.getAnnoPG().toString(), praticaSorgente.getDati().getIdDocumentale());
					}

					// Protocollazioni collegate
					for (Entry<ProtocollazioneCollegataContainer, ElementiProtocollazione> e : protocollazioniMap.entrySet()) {
						if (e.getKey().isExists()) {
							boolean collegataFound = false;

							// Se c'è la prot. collegata la aggiorno
							for (Protocollazione pcollegata : capofilaFascicolo.getProtocollazioniCollegate()) {

								if (e.getKey().isExists() && e.getKey().getNumeroPG().equals(pcollegata.getNumeroPG()) && e.getKey().getAnnoPG().equals(pcollegata.getAnnoPG())) {
									collegataFound = true;

									// allegati
									for (Allegato allegato : e.getValue().getAllegatiProtocollati()) {
										String nomeAllegato = getNomeAllegato(allegato);
										Allegato allegatoIncollatoCollegato = allegatoHandler.incollaAllegato(nomeAllegato, allegato);
										pcollegata.getAllegatiProtocollati().add(allegatoIncollatoCollegato);
										generaEvento(EventiIterFascicolo.INCOLLA_ALLEGATO_PROTOCOLLATO, task.getCurrentUser(), allegatoIncollatoCollegato.getNome(), pcollegata.getNumeroPG(),
												pcollegata.getAnnoPG().toString(), praticaSorgente.getDati().getIdDocumentale());
									}

									// pratiche
									for (PraticaCollegata praticaCollegata : e.getValue().getPraticheProtocollate()) {
										pcollegata.getPraticheCollegateProtocollate().add(praticaCollegata);
										creaCollegamento(praticaCollegata, praticheDaCollegare);
										generaEvento(EventiIterFascicolo.INCOLLA_PRATICA_PROTOCOLLATA, task.getCurrentUser(), praticaCollegata.getAlfrescoPath().split("/")[4],
												pcollegata.getNumeroPG(), pcollegata.getAnnoPG().toString(), praticaSorgente.getDati().getIdDocumentale());
									}
								}
							}

							// Se non c'è la creo
							if (!collegataFound) {
								Protocollazione protocollazione = e.getKey().getProtocollazioneCollegata();
								protocollazione.getAllegatiProtocollati().clear();
								protocollazione.getPraticheCollegateProtocollate().clear();

								for (Allegato allegato : e.getValue().getAllegatiProtocollati()) {
									String nomeAllegato = getNomeAllegato(allegato);
									Allegato allegatoIncollatoCollegato = allegatoHandler.incollaAllegato(nomeAllegato, allegato);
									protocollazione.getAllegatiProtocollati().add(allegatoIncollatoCollegato);
									generaEvento(EventiIterFascicolo.INCOLLA_ALLEGATO_PROTOCOLLATO, task.getCurrentUser(), allegatoIncollatoCollegato.getNome(), protocollazione.getNumeroPG(),
											protocollazione.getAnnoPG().toString(), praticaSorgente.getDati().getIdDocumentale());
								}

								for (PraticaCollegata praticaCollegata : e.getValue().getPraticheProtocollate()) {
									protocollazione.getPraticheCollegateProtocollate().add(praticaCollegata);
									creaCollegamento(praticaCollegata, praticheDaCollegare);
									generaEvento(EventiIterFascicolo.INCOLLA_PRATICA_PROTOCOLLATA, task.getCurrentUser(), praticaCollegata.getAlfrescoPath().split("/")[4],
											protocollazione.getNumeroPG(), protocollazione.getAnnoPG().toString(), praticaSorgente.getDati().getIdDocumentale());
								}

								capofilaFascicolo.getProtocollazioniCollegate().add(protocollazione);
							}
						}
					}

					break;
				}

				for (Protocollazione pcollegata : capofilaFascicolo.getProtocollazioniCollegate()) {
					if (pcollegata.getAnnoPG().equals(capofilaDaIncollare.getAnnoPG()) && pcollegata.getNumeroPG().equals(capofilaDaIncollare.getNumeroPG()))
						throw new PraticaException("Protocollazione con numero/anno PG " + capofilaDaIncollare.getNumeroPG() + "/" + capofilaDaIncollare.getAnnoPG() + " esistente");
				}
			}

			if (!capofilaFound) {
				capofilaDaIncollare.getProtocollazioniCollegate().clear();
				capofilaDaIncollare.getPraticheCollegateProtocollate().clear();
				capofilaDaIncollare.getAllegatiProtocollati().clear();

				for (Entry<ProtocollazioneCollegataContainer, ElementiProtocollazione> e : protocollazioniMap.entrySet()) {
					if (e.getKey().isExists()) {
						Protocollazione protocollazione = e.getKey().getProtocollazioneCollegata();
						protocollazione.getAllegatiProtocollati().clear();
						protocollazione.getPraticheCollegateProtocollate().clear();

						for (Allegato allegato : e.getValue().getAllegatiProtocollati()) {
							String nomeAllegato = getNomeAllegato(allegato);
							Allegato allegatoIncollatoCollegato = allegatoHandler.incollaAllegato(nomeAllegato, allegato);
							protocollazione.getAllegatiProtocollati().add(allegatoIncollatoCollegato);
							generaEvento(EventiIterFascicolo.INCOLLA_ALLEGATO_PROTOCOLLATO, task.getCurrentUser(), allegatoIncollatoCollegato.getNome(), protocollazione.getNumeroPG(),
									protocollazione.getAnnoPG().toString(), praticaSorgente.getDati().getIdDocumentale());
						}

						for (PraticaCollegata praticaCollegata : e.getValue().getPraticheProtocollate()) {
							protocollazione.getPraticheCollegateProtocollate().add(praticaCollegata);
							creaCollegamento(praticaCollegata, praticheDaCollegare);
							generaEvento(EventiIterFascicolo.INCOLLA_PRATICA_PROTOCOLLATA, task.getCurrentUser(), praticaCollegata.getAlfrescoPath().split("/")[4], protocollazione.getNumeroPG(),
									protocollazione.getAnnoPG().toString(), praticaSorgente.getDati().getIdDocumentale());
						}

						capofilaDaIncollare.getProtocollazioniCollegate().add(protocollazione);
					}
				}

				for (Allegato allegato : allegatiCapofila) {
					String nome = getNomeAllegato(allegato);
					Allegato incollato = allegatoHandler.incollaAllegato(nome, allegato);
					TaskDiFirmaUtil.invalidaTaskFirmaPrecedentiConclusi(incollato, task.getEnclosingPratica());
					capofilaDaIncollare.getAllegatiProtocollati().add(incollato);
					generaEvento(EventiIterFascicolo.INCOLLA_ALLEGATO_PROTOCOLLATO, task.getCurrentUser(), incollato.getNome(), capofilaDaIncollare.getNumeroPG(),
							capofilaDaIncollare.getAnnoPG().toString(), praticaSorgente.getDati().getIdDocumentale());
				}

				for (PraticaCollegata praticaCollegata : praticheCapofila) {
					capofilaDaIncollare.getPraticheCollegateProtocollate().add(praticaCollegata);
					creaCollegamento(praticaCollegata, praticheDaCollegare);
					generaEvento(EventiIterFascicolo.INCOLLA_PRATICA_PROTOCOLLATA, task.getCurrentUser(), praticaCollegata.getAlfrescoPath().split("/")[4], capofilaDaIncollare.getNumeroPG(),
							capofilaDaIncollare.getAnnoPG().toString(), praticaSorgente.getDati().getIdDocumentale());
				}

				getDatiFascicolo().getProtocollazioniCapofila().add(capofilaDaIncollare);
			}
		}

	}

	private void incollaCollegati(List<ProtocollazioneCollegataOutput> protocollazioniCollegateOutput, List<Pratica<?>> praticheDaCollegare, IncollaAllegatoHandler allegatoHandler,
			Pratica<?> praticaSorgente) throws Exception {

		for (ProtocollazioneCollegataOutput entry : protocollazioniCollegateOutput) {
			ProtocollazioneCapofila capofila = entry.getProtocollazioneCapofila();
			List<Allegato> allegatiProtocollati = entry.getElementiProtocollazione().getAllegatiProtocollati();
			List<PraticaCollegata> praticheProtocollate = entry.getElementiProtocollazione().getPraticheProtocollate();
			Protocollazione collegata = entry.getProtocollazioneCollegata();

			boolean capofilaFound = false;
			boolean collegataFound = false;
			for (ProtocollazioneCapofila pc : getDatiFascicolo().getProtocollazioniCapofila()) {

				if (XmlPluginUtil.isSame(pc, capofila)) {
					capofilaFound = true;

					for (Protocollazione coll : pc.getProtocollazioniCollegate()) {
						if (XmlPluginUtil.isSame(coll, collegata)) {
							collegataFound = true;

							for (Allegato all : coll.getAllegatiProtocollati()) {
								for (Allegato allegatoDaIncollare : allegatiProtocollati) {
									if (all.getNome().equals(allegatoDaIncollare.getNome())) {
										throw new PraticaException("L'allegato " + allegatoDaIncollare.getNome() + " è già stato protocollato nella pratica con numero/anno PG " + coll.getNumeroPG()
												+ "/" + coll.getAnnoPG());
									}
								}
							}

							for (PraticaCollegata pcol : coll.getPraticheCollegateProtocollate()) {
								for (PraticaCollegata praticaDaIncollare : praticheProtocollate) {
									if (praticaDaIncollare.getAlfrescoPath().equals(pcol.getAlfrescoPath()))
										throw new PraticaException("La pratica " + praticaDaIncollare.getAlfrescoPath().split("/")[4] + " è già stata protocollata nella pratica con numero/anno PG "
												+ coll.getNumeroPG() + "/" + coll.getAnnoPG());
								}
							}

							for (Allegato daIncollare : allegatiProtocollati) {
								String nome = getNomeAllegato(daIncollare);
								Allegato incollato = allegatoHandler.incollaAllegato(nome, daIncollare);
								TaskDiFirmaUtil.invalidaTaskFirmaPrecedentiConclusi(incollato, task.getEnclosingPratica());
								coll.getAllegatiProtocollati().add(incollato);
								generaEvento(EventiIterFascicolo.INCOLLA_ALLEGATO_PROTOCOLLATO, task.getCurrentUser(), incollato.getNome(), coll.getNumeroPG(), coll.getAnnoPG().toString(),
										praticaSorgente.getDati().getIdDocumentale());
							}

							for (PraticaCollegata daIncollare : praticheProtocollate) {
								coll.getPraticheCollegateProtocollate().add(daIncollare);
								creaCollegamento(daIncollare, praticheDaCollegare);
								generaEvento(EventiIterFascicolo.INCOLLA_PRATICA_PROTOCOLLATA, task.getCurrentUser(), daIncollare.getAlfrescoPath().split("/")[4], coll.getNumeroPG(),
										coll.getAnnoPG().toString(), praticaSorgente.getDati().getIdDocumentale());
							}

						}
					}

					if (!collegataFound) {
						collegata.getAllegatiProtocollati().clear();
						collegata.getPraticheCollegateProtocollate().clear();

						for (Allegato daIncollare : allegatiProtocollati) {
							String nome = getNomeAllegato(daIncollare);
							Allegato incollato = allegatoHandler.incollaAllegato(nome, daIncollare);
							TaskDiFirmaUtil.invalidaTaskFirmaPrecedentiConclusi(incollato, task.getEnclosingPratica());
							collegata.getAllegatiProtocollati().add(incollato);
							generaEvento(EventiIterFascicolo.INCOLLA_ALLEGATO_PROTOCOLLATO, task.getCurrentUser(), incollato.getNome(), collegata.getNumeroPG(), collegata.getAnnoPG().toString(),
									praticaSorgente.getDati().getIdDocumentale());
						}

						for (PraticaCollegata daIncollare : praticheProtocollate) {
							collegata.getPraticheCollegateProtocollate().add(daIncollare);
							creaCollegamento(daIncollare, praticheDaCollegare);
							generaEvento(EventiIterFascicolo.INCOLLA_PRATICA_PROTOCOLLATA, task.getCurrentUser(), daIncollare.getAlfrescoPath().split("/")[4], collegata.getNumeroPG(),
									collegata.getAnnoPG().toString(), praticaSorgente.getDati().getIdDocumentale());
						}

						pc.getProtocollazioniCollegate().add(collegata);
					}
				}
			}

			if (!capofilaFound) {
				capofila.getAllegatiProtocollati().clear();
				capofila.getProtocollazioniCollegate().clear();
				capofila.getPraticheCollegateProtocollate().clear();
				collegata.getAllegatiProtocollati().clear();
				collegata.getPraticheCollegateProtocollate().clear();

				for (Allegato daIncollare : allegatiProtocollati) {
					String nome = getNomeAllegato(daIncollare);
					Allegato incollato = allegatoHandler.incollaAllegato(nome, daIncollare);
					TaskDiFirmaUtil.invalidaTaskFirmaPrecedentiConclusi(incollato, task.getEnclosingPratica());
					collegata.getAllegatiProtocollati().add(incollato);
					generaEvento(EventiIterFascicolo.INCOLLA_ALLEGATO_PROTOCOLLATO, task.getCurrentUser(), incollato.getNome(), collegata.getNumeroPG(), collegata.getAnnoPG().toString(),
							praticaSorgente.getDati().getIdDocumentale());
				}

				for (PraticaCollegata daIncollare : praticheProtocollate) {
					collegata.getPraticheCollegateProtocollate().add(daIncollare);
					creaCollegamento(daIncollare, praticheDaCollegare);
					generaEvento(EventiIterFascicolo.INCOLLA_PRATICA_PROTOCOLLATA, task.getCurrentUser(), daIncollare.getAlfrescoPath().split("/")[4], collegata.getNumeroPG(),
							collegata.getAnnoPG().toString(), praticaSorgente.getDati().getIdDocumentale());
				}

				capofila.getProtocollazioniCollegate().add(collegata);
				getDatiFascicolo().getProtocollazioniCapofila().add(capofila);
			}
		}
	}

	private void creaCollegamento(PraticaCollegata praticaCollegata, List<Pratica<?>> praticheDaCollegare) {
		for (Pratica<?> praticaDaCollegare : praticheDaCollegare) {
			if (praticaCollegata.getAlfrescoPath().equals(praticaDaCollegare.getAlfrescoPath())) {
				PraticaCollegata pc = praticaDaCollegare.getDati().new PraticaCollegata(task.getEnclosingPratica().getAlfrescoPath(), getDatiFascicolo().getTipo().getNomeTipologia(), new Date());
				praticaDaCollegare.addPraticaCollegata(pc);
			}
		}
	}

	private String getNomeAllegato(Allegato allegato) {

		if (TaskDiFirmaUtil.getApprovazioneFirmaTaskAttivoByAllegato(task.getEnclosingPratica(), allegato) != null || //
				XmlPluginUtil.isAllegatoProtocollato(allegato, task.getEnclosingPratica()) || //
				XmlPluginUtil.hasLock(allegato, task.getEnclosingPratica()) || //
				Boolean.FALSE.equals(allegato.getVersionable()))

			return XmlPluginUtil.getNewNomeAllegatoFascicolo(allegato, task.getEnclosingPratica());

		return allegato.getNome();
	}

}
