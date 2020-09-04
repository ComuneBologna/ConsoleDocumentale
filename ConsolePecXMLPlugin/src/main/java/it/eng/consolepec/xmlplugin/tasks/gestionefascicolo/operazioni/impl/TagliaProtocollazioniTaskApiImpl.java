package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata;
import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Procedimento;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Protocollazione;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TagliaProtocollazioniTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TagliaProtocollazioniTaskApi.ProtocollazioneCapofilaOutput.ProtocollazioneCollegataContainer;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.util.TaskDiFirmaUtil;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * @author biagiot
 *
 * @param <T>
 */
public class TagliaProtocollazioniTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements TagliaProtocollazioniTaskApi {

	public TagliaProtocollazioniTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public TagliaProtocollazioniOutput tagliaProtocollazioni(List<Allegato> allegati, List<Pratica<?>> praticheCollegateProtocollate, Pratica<?> praticaDestinataria) throws Exception {

		for (Allegato allegato : allegati) {
			if (!XmlPluginUtil.checkPresenzaAllegato(allegato, task.getEnclosingPratica()))
				throw new PraticaException("L'allegato " + allegato.getNome() + " non è presente nel fascicolo " + getDatiFascicolo().getIdDocumentale());

			if (!checkTagliaAllegati(allegato))
				throw new PraticaException("L'allegato " + allegato.getNome() + " è bloccato e non può essere spostato dal fascicolo " + getDatiFascicolo().getIdDocumentale());

			if (!XmlPluginUtil.isAllegatoProtocollato(allegato, task.getEnclosingPratica()))
				throw new PraticaException("L'allegato " + allegato.getNome() + " non è protocollato");

			TaskDiFirmaUtil.invalidaTaskFirmaPrecedentiConclusi(allegato, task.getEnclosingPratica());
		}

		for (Pratica<?> praticaCollegataProtocollata : praticheCollegateProtocollate) {
			if (!checkTagliaPraticaProtocollata(praticaCollegataProtocollata))
				throw new PraticaException(
						"La pratica collegata " + praticaCollegataProtocollata.getDati().getIdDocumentale() + " non è presente nel fascicolo " + getDatiFascicolo().getIdDocumentale());
		}

		checkCapofila(allegati, praticheCollegateProtocollate);

		TagliaAllegatiProtocollatiOutput tagliaAllegatiProtocollatiOutput = tagliaAllegatiProtocollati(allegati, praticheCollegateProtocollate, praticaDestinataria);

		if (!cleanCollegamenti(praticheCollegateProtocollate)) {
			throw new PraticaException("Errore durante la rimozione dei collegamenti delle pratiche protocollate collegate");
		}

		return new TagliaProtocollazioniOutput(tagliaAllegatiProtocollatiOutput.getProtocollazioniCapofilaOutput(), tagliaAllegatiProtocollatiOutput.getProtocollazioniCollegateOutput(),
				praticheCollegateProtocollate);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return !getDatiFascicolo().getProtocollazioniCapofila().isEmpty() && Stato.IN_GESTIONE.equals(getDatiFascicolo().getStato());
	}

	@Override
	protected ITipoApiTask getTipoApiTask() {
		return TipoApiTask.TAGLIA_PROTOCOLLAZIONI;
	}

	private void checkCapofila(List<Allegato> allegatiProtocollati, List<Pratica<?>> praticheCollegateProtocollate) {

		Map<ProtocollazioneCapofila, ElementiProtocollazione> mapCapofila = new TreeMap<ProtocollazioneCapofila, ElementiProtocollazione>();

		for (ProtocollazioneCapofila protocollazioneCapofila : getDatiFascicolo().getProtocollazioniCapofila()) {

			/*
			 * Allegati
			 */
			for (Allegato allegato : allegatiProtocollati) {
				for (Allegato allegatoProtocollato : protocollazioneCapofila.getAllegatiProtocollati()) {
					if (allegatoProtocollato.getNome().equals(allegato.getNome())) {
						if (hasProcedimento(protocollazioneCapofila))
							throw new PraticaException("Esiste un procedimento per il PG " + protocollazioneCapofila.getNumeroPG() + "/" + protocollazioneCapofila.getAnnoPG()
									+ " : non è possibile spostare l'allegato " + allegato.getNome());

						if (mapCapofila.containsKey(protocollazioneCapofila)) {
							mapCapofila.get(protocollazioneCapofila).getAllegatiProtocollati().add(allegatoProtocollato);

						} else {
							ElementiProtocollazione ep = new ElementiProtocollazione();
							ep.getAllegatiProtocollati().add(allegato);
							mapCapofila.put(protocollazioneCapofila, ep);
						}
					}
				}
			}

			/*
			 * Pratiche
			 */
			for (Pratica<?> pratica : praticheCollegateProtocollate) {
				for (PraticaCollegata praticaCollegataProtcollata : protocollazioneCapofila.getPraticheCollegateProtocollate()) {
					if (praticaCollegataProtcollata.getAlfrescoPath().equals(pratica.getAlfrescoPath())) {
						if (hasProcedimento(protocollazioneCapofila))
							throw new PraticaException("Esiste un procedimento per il PG " + protocollazioneCapofila.getNumeroPG() + "/" + protocollazioneCapofila.getAnnoPG()
									+ " : non è possibile spostare la pratica " + pratica.getDati().getIdDocumentale());

						if (mapCapofila.containsKey(protocollazioneCapofila)) {
							mapCapofila.get(protocollazioneCapofila).getPraticheProtocollate().add(praticaCollegataProtcollata);

						} else {
							ElementiProtocollazione ep = new ElementiProtocollazione();
							ep.getPraticheProtocollate().add(praticaCollegataProtcollata);
							mapCapofila.put(protocollazioneCapofila, ep);
						}
					}
				}
			}

		}

		for (Entry<ProtocollazioneCapofila, ElementiProtocollazione> entry : mapCapofila.entrySet())
			if (entry.getKey().getAllegatiProtocollati().size() != entry.getValue().getAllegatiProtocollati().size() || //
					entry.getKey().getPraticheCollegateProtocollate().size() != entry.getValue().getPraticheProtocollate().size())

				throw new PraticaException("Non è possibile spezzare il capofila. Può essere spostato solo spostando tutti gli elementi protocollati");
	}

	private boolean hasProcedimento(ProtocollazioneCapofila protocollazioneCapofila) {
		for (Procedimento procedimento : getDatiFascicolo().getProcedimenti()) {
			if (procedimento.getAnnoPG().equals(protocollazioneCapofila.getAnnoPG()) && procedimento.getNumeroPG().equals(protocollazioneCapofila.getNumeroPG()))
				return true;
		}

		return false;
	}

	private TagliaAllegatiProtocollatiOutput tagliaAllegatiProtocollati(List<Allegato> allegatiProtocollati, List<Pratica<?>> praticheCollegate, Pratica<?> praticaDestinataria) {
		Map<Protocollazione, ProtocollazioneCollegataOutput> mapCollegati = new HashMap<Protocollazione, ProtocollazioneCollegataOutput>();
		Map<ProtocollazioneCapofila, ProtocollazioneCapofilaOutput> mapCapofila = new HashMap<ProtocollazioneCapofila, ProtocollazioneCapofilaOutput>();

		List<Pratica<?>> praticheCollegateProtocollate = new ArrayList<Pratica<?>>(praticheCollegate);

		/*
		 * Creo le mappe
		 */

		/*
		 * 1. capofila
		 */
		List<Allegato> allegatiElaborati = new ArrayList<Allegato>();
		List<Pratica<?>> praticheElaborate = new ArrayList<Pratica<?>>();
		for (ProtocollazioneCapofila protocollazioneCapofila : getDatiFascicolo().getProtocollazioniCapofila()) {

			/*
			 * 1a) Allegati
			 */
			for (Allegato allegato : allegatiProtocollati) {
				for (Allegato allegatoProtocollato : protocollazioneCapofila.getAllegatiProtocollati()) {
					if (allegatoProtocollato.getNome().equals(allegato.getNome())) {
						if (mapCapofila.containsKey(protocollazioneCapofila)) {
							boolean ok = false;

							for (Entry<ProtocollazioneCollegataContainer, ElementiProtocollazione> e : mapCapofila.get(protocollazioneCapofila).getElementiProtocollati().entrySet()) {
								if (!e.getKey().isExists()) {
									e.getValue().getAllegatiProtocollati().add(allegato);
									ok = true;
									break;
								}
							}

							if (!ok)
								throw new PraticaException("Errore durante l'eliminazione dell'allegato " + allegato.getNome());

						} else {
							Map<ProtocollazioneCollegataContainer, ElementiProtocollazione> elementiProtocollati = new HashMap<ProtocollazioneCollegataContainer, ElementiProtocollazione>();
							ElementiProtocollazione ep = new ElementiProtocollazione();
							ep.getAllegatiProtocollati().add(allegato);
							elementiProtocollati.put(new ProtocollazioneCollegataContainer(null), ep);
							mapCapofila.put(protocollazioneCapofila, new ProtocollazioneCapofilaOutput(protocollazioneCapofila, elementiProtocollati));
						}

						allegatiElaborati.add(allegato);
					}
				}
			}

			allegatiProtocollati.removeAll(allegatiElaborati);

			/*
			 * 1b) Pratiche
			 */
			for (Pratica<?> pratica : praticheCollegateProtocollate) {
				for (PraticaCollegata praticaCollegataProtcollata : protocollazioneCapofila.getPraticheCollegateProtocollate()) {
					if (praticaCollegataProtcollata.getAlfrescoPath().equals(pratica.getAlfrescoPath())) {
						if (mapCapofila.containsKey(protocollazioneCapofila)) {
							boolean ok = false;

							for (Entry<ProtocollazioneCollegataContainer, ElementiProtocollazione> e : mapCapofila.get(protocollazioneCapofila).getElementiProtocollati().entrySet()) {
								if (!e.getKey().isExists()) {
									e.getValue().getPraticheProtocollate().add(praticaCollegataProtcollata);
									ok = true;
									break;
								}
							}

							if (!ok)
								throw new PraticaException("Errore durante l'eliminazione della pratica protocollata " + praticaCollegataProtcollata.getAlfrescoPath());

						} else {
							Map<ProtocollazioneCollegataContainer, ElementiProtocollazione> elementiProtocollati = new HashMap<ProtocollazioneCollegataContainer, ElementiProtocollazione>();
							ElementiProtocollazione ep = new ElementiProtocollazione();
							ep.getPraticheProtocollate().add(praticaCollegataProtcollata);
							elementiProtocollati.put(new ProtocollazioneCollegataContainer(null), ep);
							mapCapofila.put(protocollazioneCapofila, new ProtocollazioneCapofilaOutput(protocollazioneCapofila, elementiProtocollati));
						}

						praticheElaborate.add(pratica);
					}
				}
			}

			praticheCollegateProtocollate.removeAll(praticheElaborate);
		}

		/*
		 * 2. Collegati
		 */

		for (ProtocollazioneCapofila protocollazioneCapofila : getDatiFascicolo().getProtocollazioniCapofila()) {

			for (Protocollazione protocollazione : protocollazioneCapofila.getProtocollazioniCollegate()) {

				/*
				 * 2a) allegati
				 */
				for (Allegato allegato : allegatiProtocollati) {
					for (Allegato allegatoProtocollato : protocollazione.getAllegatiProtocollati()) {
						if (allegatoProtocollato.getNome().equals(allegato.getNome())) {
							if (mapCapofila.containsKey(protocollazioneCapofila)) {
								// Ho anche il capofila da spostare
								boolean found = false;

								for (Entry<ProtocollazioneCollegataContainer, ElementiProtocollazione> e : mapCapofila.get(protocollazioneCapofila).getElementiProtocollati().entrySet()) {
									if (e.getKey().isExists() && e.getKey().getAnnoPG().equals(protocollazione.getAnnoPG()) && e.getKey().getNumeroPG().equals(protocollazione.getNumeroPG())) {
										e.getValue().getAllegatiProtocollati().add(allegato);
										found = true;
									}
								}

								if (!found) {
									ProtocollazioneCollegataContainer cont = new ProtocollazioneCollegataContainer(protocollazione);
									ElementiProtocollazione el = new ElementiProtocollazione();
									el.getAllegatiProtocollati().add(allegato);
									mapCapofila.get(protocollazioneCapofila).getElementiProtocollati().put(cont, el);
								}

							} else {
								if (mapCollegati.containsKey(protocollazione)) {
									mapCollegati.get(protocollazione).getElementiProtocollazione().getAllegatiProtocollati().add(allegato);

								} else {
									ElementiProtocollazione ep = new ElementiProtocollazione();
									ep.getAllegatiProtocollati().add(allegato);
									ProtocollazioneCollegataOutput pco = new ProtocollazioneCollegataOutput(protocollazione, protocollazioneCapofila, ep);
									mapCollegati.put(protocollazione, pco);
								}
							}
						}
					}
				}

				/*
				 * 2b) pratiche
				 */
				for (Pratica<?> pratica : praticheCollegateProtocollate) {
					for (PraticaCollegata praticaCollegataProtcollata : protocollazione.getPraticheCollegateProtocollate()) {
						if (praticaCollegataProtcollata.getAlfrescoPath().equals(pratica.getAlfrescoPath())) {
							if (mapCapofila.containsKey(protocollazioneCapofila)) {
								// Ho anche il capofila da spostare
								boolean found = false;

								for (Entry<ProtocollazioneCollegataContainer, ElementiProtocollazione> e : mapCapofila.get(protocollazioneCapofila).getElementiProtocollati().entrySet()) {
									if (e.getKey().isExists() && e.getKey().getAnnoPG().equals(protocollazione.getAnnoPG()) && e.getKey().getNumeroPG().equals(protocollazione.getNumeroPG())) {
										e.getValue().getPraticheProtocollate().add(praticaCollegataProtcollata);
										found = true;
									}
								}

								if (!found) {
									ProtocollazioneCollegataContainer cont = new ProtocollazioneCollegataContainer(protocollazione);
									ElementiProtocollazione el = new ElementiProtocollazione();
									el.getPraticheProtocollate().add(praticaCollegataProtcollata);
									mapCapofila.get(protocollazioneCapofila).getElementiProtocollati().put(cont, el);
								}

							} else {
								if (mapCollegati.containsKey(protocollazione)) {
									mapCollegati.get(protocollazione).getElementiProtocollazione().getPraticheProtocollate().add(praticaCollegataProtcollata);

								} else {
									ElementiProtocollazione ep = new ElementiProtocollazione();
									ep.getPraticheProtocollate().add(praticaCollegataProtcollata);
									ProtocollazioneCollegataOutput pco = new ProtocollazioneCollegataOutput(protocollazione, protocollazioneCapofila, ep);
									mapCollegati.put(protocollazione, pco);
								}
							}
						}
					}
				}
			}
		}

		/*
		 * Taglia
		 */

		List<ProtocollazioneCollegataOutput> protocollazioniCollegateOutput = new ArrayList<ProtocollazioneCollegataOutput>(mapCollegati.values());
		List<ProtocollazioneCapofilaOutput> protocollazioniCapofilaOutput = new ArrayList<ProtocollazioneCapofilaOutput>(mapCapofila.values());

		if (!tagliaCollegati(protocollazioniCollegateOutput, praticaDestinataria) || !tagliaCapofila(protocollazioniCapofilaOutput, praticaDestinataria))
			throw new PraticaException("Errore durante l'eliminazione degli elementi protocollati");

		return new TagliaAllegatiProtocollatiOutput(protocollazioniCapofilaOutput, protocollazioniCollegateOutput);
	}

	private boolean tagliaCapofila(List<ProtocollazioneCapofilaOutput> protocollazioniCapofilaOutput, Pratica<?> praticaDestinataria) {

		for (ProtocollazioneCapofilaOutput entry : protocollazioniCapofilaOutput) {
			ProtocollazioneCapofila capofila = entry.getProtocollazioneCapofila();

			boolean hasCollegati = false;
			for (Entry<ProtocollazioneCollegataContainer, ElementiProtocollazione> e : entry.getElementiProtocollati().entrySet()) {
				if (e.getKey().isExists()) {
					hasCollegati = true;
					break;
				}
			}

			if (capofila.getProtocollazioniCollegate().isEmpty()) {
				if (hasCollegati)
					throw new PraticaException("Elaborazione non corretta");

				// Non ho protocollazioni collegate -> elimino tutto il capofila
				if (!getDatiFascicolo().getProtocollazioniCapofila().remove(capofila))
					return false;

			} else {
				// Ho protocollazioni collegate nella pratica
				List<ProtocollazioneCapofila> cToDel = new ArrayList<ProtocollazioneCapofila>();

				if (hasCollegati) {
					// ci sono anche degli allegati prot. collegati da spostare: elimino dal fascicolo quelle che desidero spostare e lascio le altre (se ci sono)
					for (ProtocollazioneCapofila pc : getDatiFascicolo().getProtocollazioniCapofila()) {
						List<Protocollazione> ccToDel = new ArrayList<Protocollazione>();

						if (XmlPluginUtil.isSame(pc, capofila)) {
							pc.getAllegatiProtocollati().clear();
							pc.getPraticheCollegateProtocollate().clear();

							for (Protocollazione pcc : pc.getProtocollazioniCollegate()) {

								for (Entry<ProtocollazioneCollegataContainer, ElementiProtocollazione> e : entry.getElementiProtocollati().entrySet()) {

									if (e.getKey().isExists() && e.getKey().getAnnoPG().equals(pcc.getAnnoPG()) && e.getKey().getNumeroPG().equals(pcc.getNumeroPG())) {

										if (!e.getValue().getAllegatiProtocollati().isEmpty())
											if (!pcc.getAllegatiProtocollati().removeAll(e.getValue().getAllegatiProtocollati()))
												return false;

										if (!e.getValue().getPraticheProtocollate().isEmpty())
											if (!pcc.getPraticheCollegateProtocollate().removeAll(e.getValue().getPraticheProtocollate()))
												return false;

										if (pcc.getAllegatiProtocollati().isEmpty() && pcc.getPraticheCollegateProtocollate().isEmpty())
											// se non ho più alleg. protocollati e pratiche coll. protocollate la protocollazione coll. non ha più senso di esistere
											ccToDel.add(pcc);

										break;
									}
								}
							}

							pc.getProtocollazioniCollegate().removeAll(ccToDel);

							if (pc.getProtocollazioniCollegate().isEmpty())
								// se non ho protocoll. collegate la protocollazione capofila non ha più senso di esistere
								cToDel.add(pc);

							break;
						}

					}

					getDatiFascicolo().getProtocollazioniCapofila().removeAll(cToDel);

				} else {
					// elimino solo gli allegati e lascio il riferimento al capofila per le prot. collegate
					for (ProtocollazioneCapofila pc : getDatiFascicolo().getProtocollazioniCapofila()) {
						if (XmlPluginUtil.isSame(pc, capofila)) {
							pc.getAllegatiProtocollati().clear();
							pc.getPraticheCollegateProtocollate().clear();
							// faccio il clear perchè è possibile tagliare solo tutti gli elementi prot. del capofila altrimenti qui non ci si arriva
							break;
						}
					}
				}
			}

			for (Entry<ProtocollazioneCollegataContainer, ElementiProtocollazione> e : entry.getElementiProtocollati().entrySet()) {

				if (!e.getValue().getAllegatiProtocollati().isEmpty())
					if (!getDatiFascicolo().getAllegati().removeAll(e.getValue().getAllegatiProtocollati()))
						return false;

				String numPg = e.getKey().isExists() ? e.getKey().getNumeroPG() : capofila.getNumeroPG();
				Integer annoPG = e.getKey().isExists() ? e.getKey().getAnnoPG() : capofila.getAnnoPG();

				for (Allegato allegato : e.getValue().getAllegatiProtocollati()) {
					generaEvento(EventiIterFascicolo.TAGLIA_ALLEGATO_PROTOCOLLATO, task.getCurrentUser(), allegato.getNome(), numPg, annoPG.toString(),
							praticaDestinataria.getDati().getIdDocumentale());
				}

				for (PraticaCollegata praticaCollegata : e.getValue().getPraticheProtocollate()) {
					generaEvento(EventiIterFascicolo.TAGLIA_PRATICA_PROTOCOLLATA, task.getCurrentUser(), praticaCollegata.getAlfrescoPath().split("/")[4], numPg, annoPG.toString(),
							praticaDestinataria.getDati().getIdDocumentale());
				}
			}
		}

		return true;

	}

	private boolean tagliaCollegati(List<ProtocollazioneCollegataOutput> protocollazioniCollegateOutput, Pratica<?> praticaDestinataria) {

		for (ProtocollazioneCollegataOutput entry : protocollazioniCollegateOutput) {
			Protocollazione protocollazioneCollegata = entry.getProtocollazioneCollegata();
			List<Allegato> allegati = entry.getElementiProtocollazione().getAllegatiProtocollati();
			List<PraticaCollegata> pratiche = entry.getElementiProtocollazione().getPraticheProtocollate();
			ProtocollazioneCapofila protocollazioneCapofila = entry.getProtocollazioneCapofila();

			if (!allegati.isEmpty())
				if (!protocollazioneCollegata.getAllegatiProtocollati().removeAll(allegati))
					return false;

			if (!pratiche.isEmpty())
				if (!protocollazioneCollegata.getPraticheCollegateProtocollate().removeAll(pratiche))
					return false;

			if (protocollazioneCollegata.getAllegatiProtocollati().isEmpty() && protocollazioneCollegata.getPraticheCollegateProtocollate().isEmpty()) {
				// Se non ci sono più allegati protocollati e pratiche protocollate allora la prot. collegata non ha più senso di esistere
				for (ProtocollazioneCapofila pc : getDatiFascicolo().getProtocollazioniCapofila()) {
					if (XmlPluginUtil.isSame(pc, protocollazioneCapofila)) {
						if (!pc.getProtocollazioniCollegate().remove(protocollazioneCollegata)) {
							return false;
						}

						break;
					}
				}
			}

			ProtocollazioneCapofila pcToDel = null;
			for (ProtocollazioneCapofila pc : getDatiFascicolo().getProtocollazioniCapofila()) {
				if (XmlPluginUtil.isSame(pc, protocollazioneCapofila)) {
					if (pc.getAllegatiProtocollati().isEmpty() && pc.getPraticheCollegateProtocollate().isEmpty() && pc.getProtocollazioniCollegate().isEmpty())
						// Se non ci sono più allegati protocollati e pratiche protocollate coll. e allegati prot. collegati allora la prot. capofila non ha più senso di esistere
						pcToDel = pc;
				}
			}
			if (pcToDel != null)
				getDatiFascicolo().getProtocollazioniCapofila().remove(pcToDel);

			for (Allegato allegato : allegati) {
				getDatiFascicolo().getAllegati().remove(allegato);
				generaEvento(EventiIterFascicolo.TAGLIA_ALLEGATO_PROTOCOLLATO, task.getCurrentUser(), allegato.getNome(), protocollazioneCollegata.getNumeroPG(),
						protocollazioneCollegata.getAnnoPG().toString(), praticaDestinataria.getDati().getIdDocumentale());
			}

			for (PraticaCollegata praticaCollegata : pratiche) {
				generaEvento(EventiIterFascicolo.TAGLIA_PRATICA_PROTOCOLLATA, task.getCurrentUser(), praticaCollegata.getAlfrescoPath().split("/")[4], protocollazioneCollegata.getNumeroPG(),
						protocollazioneCollegata.getAnnoPG().toString(), praticaDestinataria.getDati().getIdDocumentale());
			}
		}

		return true;
	}

	private boolean cleanCollegamenti(List<Pratica<?>> praticheCollegateProtocollate) {
		if (praticheCollegateProtocollate.isEmpty())
			return true;

		for (Pratica<?> pratica : praticheCollegateProtocollate) {
			PraticaCollegata collegamento = null;

			for (PraticaCollegata praticaCollegata : pratica.getAllPraticheCollegate()) {
				if (praticaCollegata.getAlfrescoPath().equals(task.getEnclosingPratica().getAlfrescoPath())) {
					collegamento = praticaCollegata;
					break;
				}
			}

			if (collegamento == null)
				throw new PraticaException("Errore durante l'eliminazione dei collegamenti");

			pratica.removePraticaCollegata(collegamento);
			task.getEnclosingPratica().removePraticaCollegata(pratica);
		}

		return true;
	}

	private boolean checkTagliaAllegati(Allegato allegato) {
		return (TaskDiFirmaUtil.getApprovazioneFirmaTaskAttivoByAllegato(task.getEnclosingPratica(), allegato) == null) //
				&& (allegato.getLock() == null || !Boolean.TRUE.equals(allegato.getLock())); //
	}

	private boolean checkTagliaPraticaProtocollata(Pratica<?> praticaProtocollata) {

		for (ProtocollazioneCapofila protocollazioneCapofila : getDatiFascicolo().getProtocollazioniCapofila()) {

			if (!protocollazioneCapofila.isFromBa01()) {
				for (PraticaCollegata praticaCollegata : protocollazioneCapofila.getPraticheCollegateProtocollate()) {
					if (praticaCollegata.getAlfrescoPath().equals(praticaProtocollata.getAlfrescoPath()))
						return true;
				}
			}

			for (Protocollazione protocollazioneCollegata : protocollazioneCapofila.getProtocollazioniCollegate()) {
				for (PraticaCollegata praticaCollegata : protocollazioneCollegata.getPraticheCollegateProtocollate()) {
					if (praticaCollegata.getAlfrescoPath().equals(praticaProtocollata.getAlfrescoPath()))
						return true;
				}
			}
		}

		return false;
	}

	@AllArgsConstructor
	@Getter
	private static class TagliaAllegatiProtocollatiOutput {
		private List<ProtocollazioneCapofilaOutput> protocollazioniCapofilaOutput = new ArrayList<ProtocollazioneCapofilaOutput>();
		private List<ProtocollazioneCollegataOutput> protocollazioniCollegateOutput = new ArrayList<ProtocollazioneCollegataOutput>();
	}
}
