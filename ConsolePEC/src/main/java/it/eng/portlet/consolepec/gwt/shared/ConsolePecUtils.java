package it.eng.portlet.consolepec.gwt.shared;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import it.eng.cobo.consolepec.commons.urbanistica.AllegatoProcedi;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.StoricoVersioniDTO;
import it.eng.portlet.consolepec.gwt.shared.model.NodoModulisticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.NodoModulisticaDTO.TipoNodoModulisticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.SezioneDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ValoreModuloDTO;

public class ConsolePecUtils {

	private static DateTimeFormat DTF = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATA);
	private static DateTimeFormat FORMATO_DATA_ORA = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATAORA);

	public static String getDescrizioneEmail(PecDTO pec) {
		StringBuffer sb = new StringBuffer();
		sb.append(pec.getTitolo() != null ? pec.getTitolo() : "Nessun oggetto");
		return sb.toString();
	}

	public static String getDescrizionePraticaModulistica(PraticaModulisticaDTO pm) {
		StringBuffer sb = new StringBuffer();
		sb.append(pm.getTitolo() != null ? pm.getTitolo() : "Nessun oggetto");
		return sb.toString();
	}

	public static String getDescrizioneBA01(String oggetto) {
		if (oggetto.length() > ConsolePecConstants.DESCR_WIDGET_ELEMENTI_MAX_LEN) {
			oggetto = oggetto.substring(0, ConsolePecConstants.DESCR_WIDGET_ELEMENTI_MAX_LEN).concat("...");
		}
		return oggetto;
	}

	public static String getTestoPubblicazioneAllegato(String url, Date inizio, Date fine) {
		StringBuffer sb = new StringBuffer();
		sb.append("\n\nIl documento è disponibile per il download dal ").append(DTF.format(inizio)).append(" al ").append(DTF.format(fine)).append(" al seguente indirizzo:\n\n").append(url);
		return sb.toString();
	}

	public static String getLabelComposizioneFascicolo(AllegatoDTO allegato) {
		StringBuffer sb = new StringBuffer(allegato.getLabel());

		for (StoricoVersioniDTO sv : allegato.getStoricoVersioni()) {
			if (sv.getVersione().equals(allegato.getVersioneCorrente())) {
				if (sv.getInformazioniTaskFirma() != null && sv.getInformazioniTaskFirma().getStatoRichiesta() != null) {
					sb.append(" (").append(sv.getInformazioniTaskFirma().getStatoRichiesta().getLabel()).append(")");
				}
			}
		}

		if (allegato.isPubblicato()) {
			sb.append(" (Pubblicato dal ").append(DTF.format(allegato.getDataInizioPubblicazione())).append(" al ").append(DTF.format(allegato.getDataFinePubblicazione())).append(")");
		}
		if (allegato.getVisibilita().size() != 0) {
			sb.append(" (Riservato)");
		}
		return sb.toString();
	}

	public static String getLabelExtended(AllegatoDTO allegato) {
		String label = (allegato.getDataCaricamento() != null ? FORMATO_DATA_ORA.format(allegato.getDataCaricamento()) + " - " : "") + allegato.getLabel();
		StringBuffer sb = new StringBuffer(label);
		if (allegato.isPubblicato()) {
			sb.append(" (Pubblicato dal ").append(DTF.format(allegato.getDataInizioPubblicazione())).append(" al ").append(DTF.format(allegato.getDataFinePubblicazione())).append(")");
		}
		if (allegato.getVisibilita() != null && !allegato.getVisibilita().isEmpty()) {
			sb.append(" (Riservato)");
		}
		if (allegato.getTipologiaAllegato() != null && !allegato.getTipologiaAllegato().isEmpty()) {
			sb.append(" (Altre info: ").append(GenericsUtil.convertCollectionToString(allegato.getTipologiaAllegato())).append(")");
		}
		return sb.toString();
	}

	public static List<ValoreModuloDTO> buildValoriTotaliList(PraticaModulisticaDTO pm) {
		final List<ValoreModuloDTO> valori = new ArrayList<ValoreModuloDTO>();
		buildValoriTotali(pm.getValori(), new ValoreModuloVisitor() {

			@Override
			public void visit(ValoreModuloDTO vm) {
				valori.add(vm);
			}
		});
		return valori;
	}

	public static Map<String, ValoreModuloDTO> buildValoriTotaliMapByNome(PraticaModulisticaDTO pm) {
		final Map<String, ValoreModuloDTO> valori = new LinkedHashMap<String, ValoreModuloDTO>();
		buildValoriTotali(pm.getValori(), new ValoreModuloVisitor() {

			@Override
			public void visit(ValoreModuloDTO vm) {
				valori.put(vm.getNome(), vm);
			}
		});
		return valori;
	}

	private static void buildValoriTotali(List<NodoModulisticaDTO> nodi, ValoreModuloVisitor v) {
		for (NodoModulisticaDTO nodo : nodi) {
			if (nodo.getTipoNodo() == TipoNodoModulisticaDTO.SEZIONE) {
				SezioneDTO s = (SezioneDTO) nodo;
				buildValoriTotali(s.getNodi(), v);
			}
			if (nodo.getTipoNodo() == TipoNodoModulisticaDTO.VALORE_MODULO) {
				v.visit((ValoreModuloDTO) nodo);
			}
		}
	}

	private interface ValoreModuloVisitor {
		public void visit(ValoreModuloDTO vm);
	}

	public static String getDateToString(Date input) {
		if (input != null) {
			return DTF.format(input);
		}
		return null;
	}

	public static String formatText(String body) {
		body = escapeSpecialMail(body);

		if (body.contains("<"))
			return body;
		body = body.replaceAll("(\r\n|\n\r|\r|\n)", "<br/>");
		body = body.replaceAll("\t", "&nbsp;");
		body = body.replaceAll("  ", "&nbsp;&nbsp;");
		body = body.replaceAll("&nbsp; ", "&nbsp;&nbsp;");
		return body;
	}

	private static String escapeSpecialMail(String body) {
		RegExp regex = RegExp.compile("<[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*>", "g");

		MatchResult match = regex.exec(body);

		if (match != null) {
			for (int i = 0; i < match.getGroupCount(); i++) {
				String mail = match.getGroup(i);
				body = body.replace(mail, SafeHtmlUtils.htmlEscape(mail));
			}
		}
		return body;
	}

	public static String normalizzaValoreData(Date input, DateTimeFormat dateFormat) {
		return (input == null) ? null : dateFormat.format(input);
	}

	public static String getLabelComposizioneFascicolo(AllegatoProcedi allegato) {
		StringBuffer sb = null;

		if (allegato.getNumeroAnnoPG() != null && allegato.getNome() != null) {
			sb = new StringBuffer(allegato.getNumeroAnnoPG() + " " + allegato.getNome());
		} else if (allegato.getNumeroAnnoPG() != null && allegato.getNome() == null) {
			sb = new StringBuffer(allegato.getNumeroAnnoPG());
		} else if (allegato.getNumeroAnnoPG() == null && allegato.getNome() != null) {
			sb = new StringBuffer(allegato.getNome());
		} else {
			sb = new StringBuffer("Nessun titolo");
		}
		// if (allegato.isInTaskFirma()) {
		// sb.append(" (Inviato in proposta di firma/visto)");
		// }
		if (allegato.isPubblico())
			sb.append(" (Pubblico)");
		// if(allegato.getTipologiaAllegato()!=null)
		// sb.append(" (Altre info: ").append(allegato.getTipologiaAllegato()).append(")");
		return sb.toString();
	}
}
