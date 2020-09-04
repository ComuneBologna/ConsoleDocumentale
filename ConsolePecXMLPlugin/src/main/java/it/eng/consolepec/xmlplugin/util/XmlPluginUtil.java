package it.eng.consolepec.xmlplugin.util;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import javax.xml.datatype.XMLGregorianCalendar;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo.DatoAggiuntivoVisitor;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoAnagrafica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoTabella;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreMultiplo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreSingolo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.TipoDato;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.RigaDatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.ValoreCellaDatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.ValoreCellaDatoAggiuntivo.ValoreCellaDatoAggiuntivoAnagrafica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.ValoreCellaDatoAggiuntivo.ValoreCellaDatoAggiuntivoMultiplo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.ValoreCellaDatoAggiuntivo.ValoreCellaDatoAggiuntivoSingolo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.ValoreCellaDatoAggiuntivo.ValoreCellaVisitor;
import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.cobo.consolepec.util.json.JsonFactory;
import it.eng.cobo.consolepec.util.objects.Ref;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.CampoModificabile;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.GruppoVisibilita;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.StoricoVersioni;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.StoricoVersioni.InformazioniCopia;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.StoricoVersioni.InformazioniTaskFirma;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.TipoFirma;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.TipoProtocollazione;
import it.eng.consolepec.xmlplugin.factory.DatiTask.TipoTask;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.jaxb.CampoEditabile;
import it.eng.consolepec.xmlplugin.jaxb.DatoAggiuntivo.Tabella;
import it.eng.consolepec.xmlplugin.jaxb.DatoAggiuntivo.Tabella.Riga;
import it.eng.consolepec.xmlplugin.jaxb.DestinatarioRichiestaFirmaJaxb;
import it.eng.consolepec.xmlplugin.jaxb.Documento;
import it.eng.consolepec.xmlplugin.jaxb.Gruppivisibilita;
import it.eng.consolepec.xmlplugin.jaxb.InformazioniCopiaDocumento;
import it.eng.consolepec.xmlplugin.jaxb.InformazioniTaskFirmaCopiaDocumento;
import it.eng.consolepec.xmlplugin.jaxb.InformazioniTaskFirmaDocumento;
import it.eng.consolepec.xmlplugin.jaxb.StoricoVersioniDocumento;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.ProtocollazionePEC;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Procedimento;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Protocollazione;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.NodoModulistica;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.NodoModulistica.TipoNodoModulistica;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.Sezione;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.ValoreModulo;
import it.eng.consolepec.xmlplugin.pratica.modulistica.PraticaModulistica;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask.Condivisione;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.StatoRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.TipoPropostaApprovazioneFirmaTask;

public class XmlPluginUtil {

	private static final String FILE_NAME_EXTENSION_REGEX = "\\.(?=[^\\.]+$)";
	public static final String INVIABILE = "Inviabile";
	private static final JsonFactory jsonFactory = JsonFactory.defaultFactory();

	/**
	 * Controlla se l'allegato è contenuto nella pratica
	 */
	public static List<DatiPratica.Allegato> allegatoInPratica(List<Allegato> allegati, Pratica<?> pratica) {
		List<DatiPratica.Allegato> list = new ArrayList<DatiPratica.Allegato>();
		for (Allegato all : allegati) {
			for (Allegato a : pratica.getDati().getAllegati()) {
				if (a.getNome().equals(all.getNome())) list.add(a);
			}
		}
		return list;
	}

	public static List<DatiPratica.Allegato> allegatoInPratica(Allegato allegato, Pratica<?> pratica) {
		ArrayList<Allegato> allegati = new ArrayList<DatiPratica.Allegato>();
		allegati.add(allegato);
		return allegatoInPratica(allegati, pratica);
	}

	public static String getNewNomeAllegatoFascicolo(Allegato allegato, Pratica<?> pratica) {
		if (pratica instanceof Fascicolo) {

			Fascicolo fascicolo = (Fascicolo) pratica;

			for (Allegato a : fascicolo.getDati().getAllegati()) {
				if (a.getNome().equalsIgnoreCase(allegato.getNome())) {
					return calcolaNewNameAllegato(allegato, pratica);
				}
			}
		}

		return getNewNomeAllegatoProtocollatoFascicolo(allegato, pratica);
	}

	public static String getNewNomeAllegatoProtocollatoFascicolo(Allegato allegato, Pratica<?> pratica) {
		if (pratica instanceof Fascicolo) {
			Fascicolo fascicolo = (Fascicolo) pratica;
			for (ProtocollazioneCapofila protocollazioneCapofila : fascicolo.getDati().getProtocollazioniCapofila()) {
				for (Allegato allegatoProtocollato : protocollazioneCapofila.getAllegatiProtocollati()) {
					if (allegato.getNome().equals(allegatoProtocollato.getNome())) {
						return calcolaNewNameAllegato(allegato, pratica);
					}
				}
				for (Protocollazione p : protocollazioneCapofila.getProtocollazioniCollegate()) {
					for (Allegato allegatoProtocollato : p.getAllegatiProtocollati()) {
						if (allegato.getNome().equals(allegatoProtocollato.getNome())) {
							return calcolaNewNameAllegato(allegato, pratica);
						}
					}
				}
			}

			return allegato.getNome();
		}

		return calcolaNewNameAllegato(allegato, pratica);
	}

	public static String calcolaNewNameAllegato(Allegato allegato, Pratica<?> pratica) {

		int indice = calcolaIndice(0, allegato.getNome(), allegato.getNome(), pratica);

		return calcolaNome(indice, allegato.getNome());

	}

	private static int calcolaIndice(int current, String nomeOriginale, String nomeCalcolato, Pratica<?> pratica) {
		for (Allegato a : pratica.getDati().getAllegati()) {
			if (a.getNome().equals(nomeCalcolato)) {
				int next = current + 1;
				return calcolaIndice(next, nomeOriginale, calcolaNome(next, nomeOriginale), pratica);
			}
		}

		return current;
	}

	private static String calcolaNome(int indice, String nome) {
		if (indice != 0) {
			String[] tokens = nome.split(FILE_NAME_EXTENSION_REGEX);
			if (tokens.length == 2) {
				String base = tokens[0];
				String extension = tokens[1];
				return base + "(" + indice + ")" + "." + extension;
			}

			return nome + "(" + indice + ")";

		}

		return nome;
	}

	public static boolean hasLock(Allegato allegato, Pratica<?> pratica) {
		for (Allegato a : pratica.getDati().getAllegati())
			if (a.getNome().equals(allegato.getNome())) return a.getLock() == null ? false : a.getLock();

		return false;
	}

	/**
	 * Controlla se l'allegato è protocollato
	 *
	 */
	public static boolean isAllegatoProtocollato(Allegato allegato, Pratica<?> pratica) {
		if (pratica instanceof Fascicolo) {
			Fascicolo fascicolo = (Fascicolo) pratica;

			List<ProtocollazioneCapofila> protocollazioniCapofila = fascicolo.getDati().getProtocollazioniCapofila();
			for (ProtocollazioneCapofila protocollazioneCapofila : protocollazioniCapofila) {
				List<Allegato> allegatiProtocollati = protocollazioneCapofila.getAllegatiProtocollati();
				for (Allegato allegatoProtocollato : allegatiProtocollati) {
					if (allegatoProtocollato.getNome().equals(allegato.getNome())) return true;
				}
				for (Protocollazione protocollazione : protocollazioneCapofila.getProtocollazioniCollegate()) {
					for (Allegato all : protocollazione.getAllegatiProtocollati()) {
						if (all.getNome().equals(allegato.getNome())) return true;
					}
				}
			}
		}

		if (pratica instanceof PraticaModulistica) {
			PraticaModulistica pm = (PraticaModulistica) pratica;

			List<it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.ProtocollazioneCapofila> protocollazioniCapofila = pm.getDati().getProtocollazioniCapofila();
			for (it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.ProtocollazioneCapofila protocollazioneCapofila : protocollazioniCapofila) {
				List<Allegato> allegatiProtocollati = protocollazioneCapofila.getAllegatiProtocollati();
				for (Allegato allegatoProtocollato : allegatiProtocollati) {
					if (allegatoProtocollato.getNome().equals(allegato.getNome())) return true;
				}
				for (it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.Protocollazione protocollazione : protocollazioneCapofila.getProtocollazioniCollegate()) {
					for (Allegato all : protocollazione.getAllegatiProtocollati()) {
						if (all.getNome().equals(allegato.getNome())) return true;
					}
				}
			}
		}

		return false;
	}

	public static Allegato getAllegatoProtocollato(Allegato allegato, Pratica<?> pratica) {
		return getAllegatoProtocollato(allegato.getNome(), pratica);
	}

	public static Allegato getAllegatoProtocollato(String nomeAllegato, Pratica<?> pratica) {
		if (pratica instanceof Fascicolo) {
			Fascicolo fascicolo = (Fascicolo) pratica;

			List<ProtocollazioneCapofila> protocollazioniCapofila = fascicolo.getDati().getProtocollazioniCapofila();
			for (ProtocollazioneCapofila protocollazioneCapofila : protocollazioniCapofila) {
				List<Allegato> allegatiProtocollati = protocollazioneCapofila.getAllegatiProtocollati();
				for (Allegato allegatoProtocollato : allegatiProtocollati) {
					if (allegatoProtocollato.getNome().equals(nomeAllegato)) return allegatoProtocollato;
				}
				for (Protocollazione protocollazione : protocollazioneCapofila.getProtocollazioniCollegate()) {
					for (Allegato all : protocollazione.getAllegatiProtocollati()) {
						if (all.getNome().equals(nomeAllegato)) return all;
					}
				}
			}
		}

		if (pratica instanceof PraticaModulistica) {
			PraticaModulistica pm = (PraticaModulistica) pratica;

			List<it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.ProtocollazioneCapofila> protocollazioniCapofila = pm.getDati().getProtocollazioniCapofila();
			for (it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.ProtocollazioneCapofila protocollazioneCapofila : protocollazioniCapofila) {
				List<Allegato> allegatiProtocollati = protocollazioneCapofila.getAllegatiProtocollati();
				for (Allegato allegatoProtocollato : allegatiProtocollati) {
					if (allegatoProtocollato.getNome().equals(nomeAllegato)) return allegatoProtocollato;
				}
				for (it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.Protocollazione protocollazione : protocollazioneCapofila.getProtocollazioniCollegate()) {
					for (Allegato all : protocollazione.getAllegatiProtocollati()) {
						if (all.getNome().equals(nomeAllegato)) return all;
					}
				}
			}
		}

		return null;
	}

	public static void sostituisciAllegatoProtocollato(Allegato oldAllegato, Allegato newAllegato, Fascicolo fascicolo) {
		for (ProtocollazioneCapofila capofila : fascicolo.getDati().getProtocollazioniCapofila()) {

			if (capofila.getAllegatiProtocollati().contains(oldAllegato)) {
				capofila.getAllegatiProtocollati().remove(oldAllegato);
				capofila.getAllegatiProtocollati().add(newAllegato);
				break;
			}

			for (Protocollazione collegata : capofila.getProtocollazioniCollegate()) {
				boolean found = false;
				if (collegata.getAllegatiProtocollati().contains(oldAllegato)) {
					collegata.getAllegatiProtocollati().remove(oldAllegato);
					collegata.getAllegatiProtocollati().add(newAllegato);
					break;
				}
				if (found) break;
			}
		}
	}

	public static List<String> getListaVisibilita(TreeSet<GruppoVisibilita> abilitati) {
		List<String> gruppi = new ArrayList<String>();
		for (GruppoVisibilita gr : abilitati) {
			gruppi.add(gr.getNomeGruppo());
		}
		return gruppi;
	}

	public static List<String> getElencoProtocollazionePec(ProtocollazionePEC protocollazionePec) {
		List<String> prots = new ArrayList<String>();
		if (protocollazionePec != null) {
			prots.add(new DatiProtocollazioneDto(protocollazionePec).toString());
			return prots;
		}
		return null;
	}

	/*
	 * Restituisce una concatenazione di stringhe che rappresenta tutte le protocollazioni di un fascicolo(capofila e non)
	 */
	public static List<String> protocollazioniModulisticaToList(List<ProtocollazioneCapofila> protocollazioniCapofila) {
		List<DatiProtocollazioneDto> prots = new ArrayList<DatiProtocollazioneDto>();
		for (ProtocollazioneCapofila protocollazioneCapofila : protocollazioniCapofila) {
			prots.add(new DatiProtocollazioneDto(protocollazioneCapofila));
			for (Protocollazione protocollazione : protocollazioneCapofila.getProtocollazioniCollegate()) {
				prots.add(new DatiProtocollazioneDto(protocollazione));
			}
		}

		// ordinamento della lista
		Collections.sort(prots);

		// to string
		return toString(prots);
	}

	public static Object protocollazioniToList(List<it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.ProtocollazioneCapofila> protocollazioniCapofila) {
		List<DatiProtocollazioneDto> prots = new ArrayList<DatiProtocollazioneDto>();
		for (it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.ProtocollazioneCapofila protocollazioneCapofila : protocollazioniCapofila) {
			prots.add(new DatiProtocollazioneDto(protocollazioneCapofila));
			for (it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.Protocollazione protocollazione : protocollazioneCapofila.getProtocollazioniCollegate()) {
				prots.add(new DatiProtocollazioneDto(protocollazione));
			}
		}

		// ordinamento della lista
		Collections.sort(prots);

		// to string
		return toString(prots);
	}

	private static List<String> toString(List<DatiProtocollazioneDto> prots) {
		ArrayList<String> list = new ArrayList<String>();
		for (DatiProtocollazioneDto datiProtocollazioneDto : prots) {
			list.add(datiProtocollazioneDto.toString());
		}
		return list;
	}

	/* traduzione da enum jaxb a enum XmlPlugin */
	public static TipoProtocollazione loadTipoProtocollazione(it.eng.consolepec.xmlplugin.jaxb.TipoProtocollazione tipoProtocollazione) {
		if (tipoProtocollazione == null) return null;
		switch (tipoProtocollazione) {
		case E:
			return TipoProtocollazione.ENTRATA;
		case U:
			return TipoProtocollazione.USCITA;
		case I:
			return TipoProtocollazione.INTERNA;
		default:
			return null;
		}

	}

	/* traduzione da enum XmlPlugin a enum jaxb */
	public static it.eng.consolepec.xmlplugin.jaxb.TipoProtocollazione serializeTipoProtocollazione(TipoProtocollazione tipoProtocollazione) {
		if (tipoProtocollazione == null) return null;
		switch (tipoProtocollazione) {
		case ENTRATA:
			return it.eng.consolepec.xmlplugin.jaxb.TipoProtocollazione.E;
		case USCITA:
			return it.eng.consolepec.xmlplugin.jaxb.TipoProtocollazione.U;
		case INTERNA:
			return it.eng.consolepec.xmlplugin.jaxb.TipoProtocollazione.I;
		default:
			return null;
		}
	}

	public static String checkTipoFirmaAllegato(Allegato allegato) {
		if (allegato.getFirmato()) {
			if (allegato.getTipoFirma() == null) {
				return TipoFirma.CADES.name();
			}
			return allegato.getTipoFirma().name();
		}
		return null;
	}

	// Recupera la lista dei GruppoVisibilita dall'XML della pratica
	public static List<GruppoVisibilita> getGruppiVisibilita(it.eng.consolepec.xmlplugin.jaxb.Pratica jaxb) {
		List<GruppoVisibilita> gruppi = new ArrayList<DatiPratica.GruppoVisibilita>();
		for (String abilitato : jaxb.getGruppivisibilita().getGruppovisibilita()) {
			gruppi.add(new GruppoVisibilita(abilitato));
		}
		return gruppi;
	}

	public static List<String> getListaCondivisioni(TreeSet<Condivisione> condivisioni) {
		ArrayList<String> res = new ArrayList<String>();
		for (Condivisione c : condivisioni) {
			res.add(c.getNomeGruppo());
		}
		return res;
	}

	public static String valoriModuloToList(List<NodoModulistica> valori) {
		StringBuffer sb = new StringBuffer();
		Iterator<NodoModulistica> it = valori.iterator();
		while (it.hasNext()) {
			NodoModulistica n = it.next();
			valoreModuloToString(sb, n, it);
		}
		sb.append(";");
		return sb.toString();
	}

	private static void valoreModuloToString(StringBuffer sb, NodoModulistica n, Iterator<NodoModulistica> it) {
		if (n.getTipoNodo() == TipoNodoModulistica.VALORE_MODULO) {
			ValoreModulo v = (ValoreModulo) n;
			sb.append(v.getNome()).append("=").append(GenericsUtil.sanitizeNull(v.getValore()).toLowerCase());
			if (it.hasNext()) {
				sb.append(",");
			}
		}
		if (n.getTipoNodo() == TipoNodoModulistica.SEZIONE) {
			Sezione sezione = (Sezione) n;
			Iterator<NodoModulistica> sezioneIt = sezione.getNodi().iterator();
			while (sezioneIt.hasNext()) {
				NodoModulistica nodoSezione = sezioneIt.next();
				valoreModuloToString(sb, nodoSezione, it);
			}
			if (it.hasNext()) {
				sb.append(",");
			}
		}

	}

	public static List<String> avvioProcedimentoToList(TreeSet<Procedimento> procedimenti) {
		ArrayList<String> list = new ArrayList<String>();
		for (Procedimento p : procedimenti) {
			StringBuffer sb = new StringBuffer();
			sb.append("utente=" + p.getCodUtente() + ",");
			sb.append("numeroPG=" + p.getNumeroPG() + ",");
			sb.append("annoPG=" + p.getAnnoPG() + ",");
			sb.append("codQuartiere=" + p.getCodQuartiere() + ",");
			sb.append("codTipologiaProcedimento=" + p.getCodTipologiaProcedimento() + ",");
			sb.append("modAvvioProcedimento=" + p.getModAvvioProcedimento() + ",");
			sb.append("dataInizioProcedimento=" + p.getDataInizioDecorrenzaProcedimento() + ",");
			sb.append("termine=" + p.getTermine() + ",");
			sb.append("durata=" + p.getDurata() + ",");
			sb.append("dataChiusuraProcedimento=" + p.getDataChiusura());
			list.add(sb.toString());
		}
		return list;
	}

	public static Procedimento convertiGestioneProcedimentoBean(GestioneProcedimentoBean bean) {
		Procedimento p = new Procedimento();
		p.setCodQuartiere(bean.getCodQuartiere());
		p.setCodTipologiaProcedimento(bean.getCodTipologiaProcedimento());
		p.setCodUnitaOrgCompetenza(bean.getCodUnitaOrgCompetenza());
		p.setCodUnitaOrgResponsabile(bean.getCodUnitaOrgResponsabile());
		p.setCodUtente(bean.getCodUtente());
		p.setDataInizioDecorrenzaProcedimento(bean.getDataInizioDecorrenzaProcedimento());
		p.setDurata(bean.getDurata());
		p.setFlagInterruzione(bean.getFlagInterruzione());
		if (bean.getPg() != null) {
			p.setModAvvioProcedimento(bean.getModAvvioProcedimento());
			p.setAnnoPG(bean.getPg().getAnnoPG().intValue());
			p.setNumeroPG(bean.getPg().getNumeroPG());
		}
		p.setTermine(bean.getTermine());
		p.setResponsabile(bean.getResponsabile());
		p.setProvenienza(bean.getProvenienza());
		p.setModalitaChiusura(bean.getModalitaChiusura());
		if (bean.getPgChiusura() != null) {
			p.setAnnoPGChiusura(bean.getPgChiusura().getAnnoPG().intValue());
			p.setNumeroPGChiusura(bean.getPgChiusura().getNumeroPG());
		}
		p.setDataChiusura(bean.getDataChiusura());
		return p;
	}

	public static String format(String messaggio, Object... params) {
		return MessageFormat.format(messaggio, params);
	}

	public static String getNomeAllegato(String pathAllegato) {
		String[] split = pathAllegato.split("/");
		return split[split.length - 1];
	}

	public static String getFolderRelativo(String pathAllegato) {
		String[] split = pathAllegato.split("/");
		StringBuilder bld = new StringBuilder();
		for (int i = 0; i < split.length - 1; i++) {
			bld.append(split[i]);
			if (i < split.length - 2) bld.append("/");
		}
		return bld.toString();
	}

	public static NumAnnoPG getNumAnnoPG(Fascicolo fascicolo) {
		if (fascicolo != null && fascicolo.getDati().getProtocollazioniCapofila() != null && fascicolo.getDati().getProtocollazioniCapofila().size() > 0) {
			List<ProtocollazioneCapofila> protocollazioniCapofila = fascicolo.getDati().getProtocollazioniCapofila();
			Collections.sort(protocollazioniCapofila);
			ProtocollazioneCapofila pc = fascicolo.getDati().getProtocollazioniCapofila().get(0);
			if (pc.isFromBa01() || (pc.getAllegatiProtocollati().isEmpty() && pc.getPraticheCollegateProtocollate().isEmpty())) {
				// recupero il primo pg associato al fascicolo
				List<Protocollazione> protocollazioniCollegate = pc.getProtocollazioniCollegate();
				Collections.sort(protocollazioniCollegate);
				Protocollazione protocollazione = pc.getProtocollazioniCollegate().get(0);
				return new NumAnnoPG(protocollazione.getNumeroPG(), protocollazione.getAnnoPG(), protocollazione.getOggetto());
			}

			return new NumAnnoPG(pc.getNumeroPG(), pc.getAnnoPG(), pc.getOggetto());

		}

		return null;
	}

	public static class NumAnnoPG {
		private String nomePG;
		private int annoPG;
		private String oggetto;

		public NumAnnoPG(String nomePG, int annoPG, String oggetto) {
			super();
			this.nomePG = nomePG;
			this.annoPG = annoPG;
			this.oggetto = oggetto;
		}

		public String getNomePG() {
			return nomePG;
		}

		public int getAnnoPG() {
			return annoPG;
		}

		public String getOggettoPG() {
			return oggetto;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends Task<?>> T getGestioneTaskCorrente(Pratica<?> pratica, Class<T> clazz, String utente) throws PraticaException {
		for (Task<?> t : pratica.getTasks()) {
			if (t.isAttivo() && (clazz == null || clazz.isAssignableFrom(t.getClass()))) {
				t.setCurrentUser(utente);
				return (T) t;
			}
		}
		throw new PraticaException("Task di gestione non trovato.");
	}

	@SuppressWarnings("unchecked")
	public static <T extends Task<?>> T getRiattivaTaskCorrente(Pratica<?> pratica, Class<T> clazz, String utente) throws PraticaException {
		for (Task<?> t : pratica.getTasks()) {
			if (t instanceof RiattivaTask && t.isAttivo() && (clazz == null || clazz.isAssignableFrom(t.getClass()))) {
				t.setCurrentUser(utente);
				return (T) t;
			}
		}
		throw new PraticaException("Task di gestione non trovato.");
	}

	@SuppressWarnings("unchecked")
	public static XMLTaskFascicolo<DatiGestioneFascicoloTask> getGestioneFasicoloTaskCorrente(Pratica<?> pratica, String utente) {
		XMLTaskFascicolo<?> task = getGestioneTaskCorrente(pratica, XMLTaskFascicolo.class, utente);
		if (task.getTipo().equals(TipoTask.GestioneFascicoloTask)) {
			return (XMLTaskFascicolo<DatiGestioneFascicoloTask>) task;
		}
		throw new PraticaException("Task di gestione fasicolo non trovato.");
	}

	public static Allegato getAllegatoFromDocumento(Documento doc, DatiPratica datiPratica) {
		Allegato allegato = datiPratica.new Allegato(doc.getNome(), doc.getLabel(), doc.getFolderRelativePath(), doc.getFolderOriginPath(), doc.getFolderOriginName(), doc.getDimensioneBytes(),
				doc.getCurrentVersion(), doc.isFirmato(), TipoFirma.translate(doc.getTipoFirma()), doc.isFirmatoHash(), DateUtils.xmlGrCalToDate(doc.getDataInizioPubblicazione()),
				DateUtils.xmlGrCalToDate(doc.getDataFinePubblicazione()), DateUtils.xmlGrCalToDate(doc.getDataCaricamento()), doc.getOggettoDocumento(), doc.isLock(), doc.getLockedBy());
		allegato.setHash(doc.getHash());

		if (!doc.getTipologiaAllegato().isEmpty()) {
			allegato.getTipologiaDocumento().addAll(doc.getTipologiaAllegato());
		}

		if (doc.getGruppivisibilita() != null) {
			if (doc.getGruppivisibilita().getGruppovisibilita().size() != 0) {
				for (String abilitato : doc.getGruppivisibilita().getGruppovisibilita()) {
					allegato.getGruppiVisibilita().add(new GruppoVisibilita(abilitato));
				}
			}
		}

		if (doc.getDatiAggiuntivi() != null) {
			for (it.eng.consolepec.xmlplugin.jaxb.DatoAggiuntivo dta : doc.getDatiAggiuntivi()) {
				allegato.getDatiAggiuntivi().add(loadDatoAggiuntivo(dta));
			}
		}

		/*
		 * STORICO
		 */
		if (doc.getStoricoVersioni() != null) {
			for (StoricoVersioniDocumento storicoJaxb : doc.getStoricoVersioni()) {
				StoricoVersioni storicoXML = new StoricoVersioni(storicoJaxb.getVersione());
				storicoXML.setUtente(storicoJaxb.getUtente());

				/*
				 * INFORMAZIONI TASK FIRMA
				 */
				if (storicoJaxb.getInformazioniTaskFirma() != null) {
					storicoXML.setInformazioniTaskFirma(getInformazioniTaskFirma(storicoJaxb.getInformazioniTaskFirma()));
				}

				/*
				 * Informazioni di copia
				 */
				if (storicoJaxb.getInformazioniCopiaDocumento() != null) {
					InformazioniCopia ic = new InformazioniCopia(storicoJaxb.getInformazioniCopiaDocumento().getIdDocumentaleSorgente());

					if (storicoJaxb.getInformazioniCopiaDocumento().getInformazioniTaskFirmaCopiaDocumento() != null
							&& !storicoJaxb.getInformazioniCopiaDocumento().getInformazioniTaskFirmaCopiaDocumento().isEmpty()) {
						for (InformazioniTaskFirmaCopiaDocumento itfcd : storicoJaxb.getInformazioniCopiaDocumento().getInformazioniTaskFirmaCopiaDocumento()) {
							InformazioniTaskFirma infoTask = getInformazioniTaskFirma(itfcd.getInformazioniTaskFirma());
							ic.getInformazioniTaskFirma().put(itfcd.getVersione(), infoTask);
						}
					}

					storicoXML.setInformazioniCopia(ic);
				}

				allegato.getStoricoVersioni().add(storicoXML);
			}
		}

		if (doc.getCampiEditabili() != null && !doc.getCampiEditabili().isEmpty()) {
			for (CampoEditabile c : doc.getCampiEditabili()) {
				allegato.getCampiModificabili().add(new CampoModificabile(c.getNome(), c.getValore(), c.isAbilitato()));
			}
		}

		return allegato;
	}

	private static InformazioniTaskFirma getInformazioniTaskFirma(InformazioniTaskFirmaDocumento jaxb) {
		String oggetto = jaxb.getOggettoProposta();
		String proponente = jaxb.getGruppoProponente();
		TipoPropostaApprovazioneFirmaTask tipoRichiesta = TipoPropostaApprovazioneFirmaTask.fromValue(jaxb.getTipoRichiesta());
		StatoRichiestaApprovazioneFirmaTask operazioneEffettuata = StatoRichiestaApprovazioneFirmaTask.fromValue(jaxb.getOperazioneEffettuata());
		StatoRichiestaApprovazioneFirmaTask statoRichiesta = StatoRichiestaApprovazioneFirmaTask.fromValue(jaxb.getStatoRichiesta());

		List<it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.DestinatarioRichiestaApprovazioneFirmaTask> destinatari = new ArrayList<it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.DestinatarioRichiestaApprovazioneFirmaTask>();
		if (jaxb.getDestinatariRichiestaFirma() != null) {
			for (DestinatarioRichiestaFirmaJaxb jaxbDest : jaxb.getDestinatariRichiestaFirma()) {
				destinatari.add(TaskDiFirmaUtil.convertDestinatarioFromJaxb(jaxbDest));
			}
		}

		String mittenteOriginale = jaxb.getMittenteOriginale();
		String motivazione = jaxb.getMotivazione();
		Date dataScadenza = jaxb.getDataScadenza() != null ? DateUtils.xmlGrCalToDate(jaxb.getDataScadenza()) : null;
		return new InformazioniTaskFirma(oggetto, proponente, tipoRichiesta, destinatari, operazioneEffettuata, mittenteOriginale, dataScadenza, statoRichiesta, motivazione);
	}

	public static Documento getDocumentoFromAllegato(Allegato allg) {
		Documento documentoJaxb = new Documento();
		documentoJaxb.setDimensioneBytes(allg.getDimensioneByte() == null ? 0 : allg.getDimensioneByte().longValue());
		documentoJaxb.setNome(allg.getNome());
		documentoJaxb.setFolderRelativePath(allg.getFolderRelativePath());
		documentoJaxb.setFolderOriginPath(allg.getFolderOriginPath());
		documentoJaxb.setFolderOriginName(allg.getFolderOriginName());
		documentoJaxb.setLabel(allg.getLabel());
		documentoJaxb.setCurrentVersion(allg.getCurrentVersion());
		documentoJaxb.setFirmato(allg.getFirmato());
		documentoJaxb.setTipoFirma(XmlPluginUtil.checkTipoFirmaAllegato(allg));
		documentoJaxb.setFirmatoHash(allg.getFirmatoHash());
		documentoJaxb.setHash(allg.getHash());
		documentoJaxb.getTipologiaAllegato().addAll(allg.getTipologiaDocumento());

		if (allg.getDataInizioPubblicazione() != null) {
			documentoJaxb.setDataInizioPubblicazione(DateUtils.dateToXMLGrCal(allg.getDataInizioPubblicazione()));
		}
		if (allg.getDataFinePubblicazione() != null) {
			documentoJaxb.setDataFinePubblicazione(DateUtils.dateToXMLGrCal(allg.getDataFinePubblicazione()));
		}
		if (allg.getDataCaricamento() != null) {
			documentoJaxb.setDataCaricamento(DateUtils.dateToXMLGrCal(allg.getDataCaricamento()));
		}

		documentoJaxb.setGruppivisibilita(new Gruppivisibilita());
		for (DatiPratica.GruppoVisibilita gr : allg.getGruppiVisibilita()) {
			documentoJaxb.getGruppivisibilita().getGruppovisibilita().add(gr.getNomeGruppo());
		}

		documentoJaxb.setOggettoDocumento(allg.getOggettoDocumento());
		documentoJaxb.setLock(allg.getLock());
		documentoJaxb.setLockedBy(allg.getLockedBy());

		if (allg.getDatiAggiuntivi() != null) {
			for (DatoAggiuntivo dta : allg.getDatiAggiuntivi()) {
				documentoJaxb.getDatiAggiuntivi().add(serializeDatoAggiuntivo(dta));
			}
		}

		/*
		 * STORICO
		 */
		if (!allg.getStoricoVersioni().isEmpty()) {
			for (StoricoVersioni storicoVersioniXML : allg.getStoricoVersioni()) {
				StoricoVersioniDocumento storicoJaxb = new StoricoVersioniDocumento();
				storicoJaxb.setVersione(storicoVersioniXML.getVersione());
				storicoJaxb.setUtente(storicoVersioniXML.getUtente());

				/*
				 * INFORMAZIONI TASK FIRMA
				 */
				if (storicoVersioniXML.getInformazioniTaskFirma() != null) {
					storicoJaxb.setInformazioniTaskFirma(getInformazioniTaskFirma(storicoVersioniXML.getInformazioniTaskFirma()));
				}

				/*
				 * Informazioni copia
				 */
				if (storicoVersioniXML.getInformazioniCopia() != null) {
					InformazioniCopiaDocumento ic = new InformazioniCopiaDocumento();
					ic.setIdDocumentaleSorgente(storicoVersioniXML.getInformazioniCopia().getIdDocumentaleSorgente());

					if (storicoVersioniXML.getInformazioniCopia().getInformazioniTaskFirma() != null) {
						for (Entry<String, InformazioniTaskFirma> entry : storicoVersioniXML.getInformazioniCopia().getInformazioniTaskFirma().entrySet()) {
							InformazioniTaskFirmaCopiaDocumento i = new InformazioniTaskFirmaCopiaDocumento();
							i.setVersione(entry.getKey());
							i.setInformazioniTaskFirma(getInformazioniTaskFirma(entry.getValue()));
							ic.getInformazioniTaskFirmaCopiaDocumento().add(i);
						}
					}

					storicoJaxb.setInformazioniCopiaDocumento(ic);
				}

				documentoJaxb.getStoricoVersioni().add(storicoJaxb);
			}
		}

		if (allg.getCampiModificabili() != null && !allg.getCampiModificabili().isEmpty()) {

			for (CampoModificabile c : allg.getCampiModificabili()) {
				CampoEditabile campo = new CampoEditabile();
				campo.setNome(c.getNome());
				campo.setValore(c.getValore());
				campo.setAbilitato(c.isAbilitato());
				documentoJaxb.getCampiEditabili().add(campo);
			}
		}

		return documentoJaxb;
	}

	private static InformazioniTaskFirmaDocumento getInformazioniTaskFirma(InformazioniTaskFirma infoTask) {
		InformazioniTaskFirmaDocumento infoTaskFirmaJaxb = new InformazioniTaskFirmaDocumento();
		infoTaskFirmaJaxb.setTipoRichiesta(infoTask.getTipoRichiesta().name());
		infoTaskFirmaJaxb.setGruppoProponente(infoTask.getProponente());
		infoTaskFirmaJaxb.setOperazioneEffettuata(infoTask.getOperazioneEffettuata().name());
		infoTaskFirmaJaxb.setStatoRichiesta(infoTask.getStatoRichiesta().name());
		infoTaskFirmaJaxb.setOggettoProposta(infoTask.getOggetto());

		infoTaskFirmaJaxb.setMotivazione(infoTask.getMotivazione());

		if (infoTask.getDestinatari() != null) {

			for (it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.DestinatarioRichiestaApprovazioneFirmaTask destXML : infoTask.getDestinatari()) {
				infoTaskFirmaJaxb.getDestinatariRichiestaFirma().add(TaskDiFirmaUtil.convertDestinatarioToJaxb(destXML));
			}
		}

		infoTaskFirmaJaxb.setMittenteOriginale(infoTask.getMittenteOriginale());
		XMLGregorianCalendar xgc = infoTask.getDataScadenza() != null ? DateUtils.dateToXMLGrCal(infoTask.getDataScadenza()) : null;
		infoTaskFirmaJaxb.setDataScadenza(xgc);

		return infoTaskFirmaJaxb;

	}

	public static String getExtension(Allegato allegato) {
		String[] tokens = allegato.getNome().split(FILE_NAME_EXTENSION_REGEX);
		if (tokens.length == 2) {
			String extension = tokens[1];
			return extension;
		}

		return null;
	}

	public static boolean isAllegatoComunicazioneInviabile(Allegato allegato) {
		return isAllegatoComunicazioneInviabile(allegato.getTipologiaDocumento());
	}

	public static boolean isAllegatoComunicazioneInviabile(List<String> tipologie) {
		if (tipologie != null && !tipologie.isEmpty()) {
			for (String t : tipologie) {
				if (INVIABILE.equalsIgnoreCase(t)) {
					return true;
				}
			}
		}
		return false;
	}

	public static List<Stato> statiEmailInviata() {
		List<Stato> statiEmailInviata = new ArrayList<Stato>();

		for (Stato stato : Stato.values()) {
			if (stato.getIdStato() > Stato.BOZZA.getIdStato()) {
				statiEmailInviata.add(stato);
			}

		}
		return statiEmailInviata;

	}

	public static String convertIdDocumentaleFromAlfrescoPath(String alfrescoPath) {

		String[] splits = alfrescoPath.split("/");
		if (splits.length != 6) {
			return alfrescoPath;
		}
		return splits[4].trim();

	}

	/*
	 * ricava l'alfrescopath dall'id documentale
	 */
	public static String convertAlfrescoPathFromIdDocumentale(String idDocumentale) {
		String folder = null;
		if (idDocumentale == null || idDocumentale.length() < 2) return null;
		switch (idDocumentale.substring(0, 2).toLowerCase()) {
		case "fs":
			folder = "PRATICHE";
			break;
		case "ee":
			folder = "IN";
			break;
		case "eu":
			folder = "OUT";
			break;
		case "md":
			folder = "MODULISTICA";
			break;
		case "tp":
			folder = "TEMPLATE";
			break;
		}
		return (folder == null) ? null : new StringBuffer("/PEC/CONSOLE/").append(folder).append("/").append(idDocumentale).append("/metadati.xml").toString();
	}

	private static boolean isPdf(String nomeFile) {
		return nomeFile != null && nomeFile.toLowerCase().endsWith(".pdf");
	}

	public static boolean isPdfEditabile(Allegato a) {

		if (isPdf(a.getNome()) && a.getCampiModificabili() != null && !a.getCampiModificabili().isEmpty()) {

			boolean result = false;
			for (CampoModificabile c : a.getCampiModificabili()) {
				if (c.isAbilitato()) result = true;
			}
			return result;

		}

		return false;
	}

	public static String serializeAnagraficaFascicolo(AnagraficaFascicolo ap) {
		return jsonFactory.serialize(ap);
	}

	public static AnagraficaFascicolo deserializeAnagraficaFascicolo(String ap) {
		return jsonFactory.deserialize(ap, AnagraficaFascicolo.class);
	}

	public final static String CELLA_VUOTA = "CELLA_VUOTA";

	public static it.eng.consolepec.xmlplugin.jaxb.DatoAggiuntivo serializeDatoAggiuntivo(it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo datoAggiuntivo) {
		final it.eng.consolepec.xmlplugin.jaxb.DatoAggiuntivo xmlValore = new it.eng.consolepec.xmlplugin.jaxb.DatoAggiuntivo();
		xmlValore.setNome(datoAggiuntivo.getNome());
		xmlValore.setDescrizione(datoAggiuntivo.getDescrizione());
		xmlValore.setTipo(datoAggiuntivo.getTipo().name());
		xmlValore.setPosizione(new BigInteger(datoAggiuntivo.getPosizione().toString()));
		xmlValore.setVisibile(datoAggiuntivo.isVisibile());
		datoAggiuntivo.accept(new DatoAggiuntivoVisitor() {

			@Override
			public void visit(DatoAggiuntivoTabella datoAggiuntivo) {
				xmlValore.setEditabile(datoAggiuntivo.isEditabile());
				Tabella tabella = new Tabella();
				for (DatoAggiuntivo intestazione : datoAggiuntivo.getIntestazioni()) {
					it.eng.consolepec.xmlplugin.jaxb.DatoAggiuntivo jaxbIntestazione = serializeDatoAggiuntivo(intestazione);
					tabella.getIntestazioni().add(jaxbIntestazione);
				}
				List<Riga> jaxbRighe = tabella.getRiga();
				for (RigaDatoAggiuntivo riga : datoAggiuntivo.getRighe()) {
					final Riga jaxbRiga = new Riga();
					for (ValoreCellaDatoAggiuntivo cella : riga.getCelle()) {
						if (cella == null || cella.isEmpty()) {
							jaxbRiga.getCelle().add(CELLA_VUOTA);
						} else {
							cella.accept(new ValoreCellaVisitor() {
								@Override
								public void visit(ValoreCellaDatoAggiuntivoSingolo cella) {
									jaxbRiga.getCelle().add(cella.getValore());
								}

								@Override
								public void visit(ValoreCellaDatoAggiuntivoMultiplo cella) {
									StringBuilder sb = new StringBuilder();
									for (String val : cella.getValori()) {
										sb.append(val).append("||");
									}
									jaxbRiga.getCelle().add(sb.toString());
								}

								@Override
								public void visit(ValoreCellaDatoAggiuntivoAnagrafica cella) {
									StringBuilder sb = new StringBuilder(cella.getIdAnagrafica().toString()).append("||").append(cella.getValore()).append("||");
									jaxbRiga.getCelle().add(sb.toString());
								}
							});
						}
					}
					jaxbRighe.add(jaxbRiga);
				}
				xmlValore.setTabella(tabella);
			}

			@Override
			public void visit(DatoAggiuntivoAnagrafica datoAggiuntivo) {
				if (datoAggiuntivo.getValore() != null) {
					xmlValore.setValore(datoAggiuntivo.getIdAnagrafica().toString() + "||" + datoAggiuntivo.getValore() + "||");
				}
				xmlValore.setEditabile(datoAggiuntivo.isEditabile());
				xmlValore.setObbligatorio(datoAggiuntivo.isObbligatorio());
			}

			@Override
			public void visit(DatoAggiuntivoValoreMultiplo datoAggiuntivoValoreMultiplo) {
				if (datoAggiuntivoValoreMultiplo.getValori() != null) {
					StringBuilder stringBuilder = new StringBuilder();
					for (String val : datoAggiuntivoValoreMultiplo.getValori()) {
						stringBuilder.append(val + "||");
					}
					xmlValore.setValore(stringBuilder.toString());
				}
				xmlValore.setEditabile(datoAggiuntivoValoreMultiplo.isEditabile());
				xmlValore.setObbligatorio(datoAggiuntivoValoreMultiplo.isObbligatorio());
			}

			@Override
			public void visit(DatoAggiuntivoValoreSingolo datoAggiuntivoValoreSingolo) {
				xmlValore.setValore(datoAggiuntivoValoreSingolo.getValore());
				xmlValore.setEditabile(datoAggiuntivoValoreSingolo.isEditabile());
				xmlValore.setObbligatorio(datoAggiuntivoValoreSingolo.isObbligatorio());
			}
		});
		return xmlValore;
	}

	public static it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo loadDatoAggiuntivo(final it.eng.consolepec.xmlplugin.jaxb.DatoAggiuntivo jaxbVal) {
		DatoAggiuntivo datoAggiuntivo = TipoDato.valueOf(jaxbVal.getTipo()).createDato();
		datoAggiuntivo.setNome(jaxbVal.getNome());
		datoAggiuntivo.setDescrizione(jaxbVal.getDescrizione());
		datoAggiuntivo.setTipo(TipoDato.valueOf(jaxbVal.getTipo()));
		datoAggiuntivo.setPosizione(jaxbVal.getPosizione().intValue());
		datoAggiuntivo.setVisibile(jaxbVal.isVisibile());
		datoAggiuntivo.accept(new DatoAggiuntivoVisitor() {

			@Override
			public void visit(DatoAggiuntivoTabella datoAggiuntivoTabella) {
				datoAggiuntivoTabella.setEditabile(jaxbVal.isEditabile());
				Tabella tabella = jaxbVal.getTabella();
				final Map<Riga, List<ValoreCellaDatoAggiuntivo>> celleMap = new LinkedHashMap<>();
				final Ref<Integer> ref = Ref.of(0);
				for (it.eng.consolepec.xmlplugin.jaxb.DatoAggiuntivo jaxbIntestazione : tabella.getIntestazioni()) {
					DatoAggiuntivo intestazione = loadDatoAggiuntivo(jaxbIntestazione);
					datoAggiuntivoTabella.getIntestazioni().add(intestazione);
					for (final Riga rigaXml : tabella.getRiga()) {
						final String cella = rigaXml.getCelle().get(ref.get());
						if (celleMap.get(rigaXml) == null) {
							List<ValoreCellaDatoAggiuntivo> celle = new ArrayList<>();
							celleMap.put(rigaXml, celle);
						}
						if (!CELLA_VUOTA.equals(cella)) {
							intestazione.accept(new DatoAggiuntivoVisitor() {
								@Override
								public void visit(DatoAggiuntivoTabella datoAggiuntivoTabella) {
									throw new UnsupportedOperationException();
								}

								@Override
								public void visit(DatoAggiuntivoAnagrafica datoAggiuntivoAnagrafica) {
									List<String> valoriAnagrafica = new ArrayList<String>(Arrays.asList(cella.split("\\|\\|")));
									ValoreCellaDatoAggiuntivoAnagrafica cella = new ValoreCellaDatoAggiuntivoAnagrafica(Double.valueOf(valoriAnagrafica.get(0)), valoriAnagrafica.get(1));
									celleMap.get(rigaXml).add(cella);
								}

								@Override
								public void visit(DatoAggiuntivoValoreMultiplo datoAggiuntivoValoreMultiplo) {
									List<String> valori = new ArrayList<String>(Arrays.asList(cella.split("\\|\\|")));
									ValoreCellaDatoAggiuntivoMultiplo cella = new ValoreCellaDatoAggiuntivoMultiplo(valori);
									celleMap.get(rigaXml).add(cella);
								}

								@Override
								public void visit(DatoAggiuntivoValoreSingolo datoAggiuntivoValoreSingolo) {
									ValoreCellaDatoAggiuntivoSingolo cellaSingola = new ValoreCellaDatoAggiuntivoSingolo(cella);
									celleMap.get(rigaXml).add(cellaSingola);
								}
							});
						} else {
							celleMap.get(rigaXml).add(null);
						}
					}
					Integer cnt = ref.get();
					ref.set(++cnt);
				}
				for (Entry<Riga, List<ValoreCellaDatoAggiuntivo>> entry : celleMap.entrySet()) {
					datoAggiuntivoTabella.getRighe().add(new RigaDatoAggiuntivo(entry.getValue()));
				}
			}

			@Override
			public void visit(DatoAggiuntivoAnagrafica datoAggiuntivoAnagrafica) {
				if (jaxbVal.getValore() != null && jaxbVal.getValore().contains("||")) {
					String[] valore = jaxbVal.getValore().split("\\|\\|");
					datoAggiuntivoAnagrafica.setValore(valore[1]);
					datoAggiuntivoAnagrafica.setIdAnagrafica(Double.valueOf(valore[0]));
				}
				datoAggiuntivoAnagrafica.setEditabile(jaxbVal.isEditabile());
				datoAggiuntivoAnagrafica.setObbligatorio(jaxbVal.isObbligatorio());
			}

			@Override
			public void visit(DatoAggiuntivoValoreMultiplo datoAggiuntivoValoreMultiplo) {
				if (jaxbVal.getValore() != null && jaxbVal.getValore().contains("||")) {
					List<String> valori = new ArrayList<String>(Arrays.asList(jaxbVal.getValore().split("\\|\\|")));
					datoAggiuntivoValoreMultiplo.getValori().addAll(valori);
				}
				datoAggiuntivoValoreMultiplo.setEditabile(jaxbVal.isEditabile());
				datoAggiuntivoValoreMultiplo.setObbligatorio(jaxbVal.isObbligatorio());
			}

			@Override
			public void visit(DatoAggiuntivoValoreSingolo datoAggiuntivoValoreSingolo) {
				datoAggiuntivoValoreSingolo.setValore(jaxbVal.getValore());
				datoAggiuntivoValoreSingolo.setEditabile(jaxbVal.isEditabile());
				datoAggiuntivoValoreSingolo.setObbligatorio(jaxbVal.isObbligatorio());
			}
		});

		return datoAggiuntivo;
	}

	public static boolean checkPresenzaAllegato(Allegato allegato, Pratica<?> pratica) {

		for (Allegato a : pratica.getDati().getAllegati()) {
			if (a.getNome().equals(allegato.getNome())) return true;
		}

		return false;
	}

	public static boolean isSame(ProtocollazioneCapofila pc1, ProtocollazioneCapofila pc2) {
		return pc1.getAnnoPG().equals(pc2.getAnnoPG()) && pc1.getNumeroPG().equals(pc2.getNumeroPG());
	}

	public static boolean isSame(Protocollazione pc1, Protocollazione pc2) {
		return pc1.getAnnoPG().equals(pc2.getAnnoPG()) && pc1.getNumeroPG().equals(pc2.getNumeroPG());
	}
}
