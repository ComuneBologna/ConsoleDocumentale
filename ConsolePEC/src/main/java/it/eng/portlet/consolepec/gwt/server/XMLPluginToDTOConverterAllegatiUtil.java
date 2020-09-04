package it.eng.portlet.consolepec.gwt.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.AllegatoComposizione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.AllegatoComposizione.AllegatoComposizioneBuilder;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ElementoProtocollato;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ElementoProtocollato.ElementoProtocollatoBuilder;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.EmailComposizione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.EmailComposizione.EmailComposizioneBuilder;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ProtocollazioneComposizione;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmail;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.pratica.modulistica.PraticaModulistica;
import it.eng.portlet.consolepec.gwt.client.util.ComposizioneFascicoloComparator;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants.StatiElementiComposizioneFascicolo;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoAllegato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoComunicazioneRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElencoVisitor;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppo;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollatoCapofila;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGrupppoNonProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPECRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPraticaModulisticaRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import lombok.RequiredArgsConstructor;

public class XMLPluginToDTOConverterAllegatiUtil {

	public static void creaListeAllegati(Fascicolo fascicolo, PraticaSessionUtil praticaSessionUtil, Logger log, FascicoloDTO dto) {
		ProtocollazioneVisitor pv = new ProtocollazioneVisitor(fascicolo, praticaSessionUtil, log);
		ConversazioneVisitor cv = new ConversazioneVisitor(fascicolo, praticaSessionUtil, log);
		AllegatoVisitro av = new AllegatoVisitro(fascicolo, praticaSessionUtil, log);
		for (ElementoElenco e : dto.getElenco()) {
			e.accept(pv);
			e.accept(cv);
			e.accept(av);
		}

		pv.aggiornaProtocollazioni(dto); // Per assurdo che sia lo devo fare qua dato che veniva fatto nel visitor e non a livello di allegato al momento della creazione
		cv.aggiornaConversazioni();
		av.aggiornaAllegati();

		dto.getComposizioneProtocollazioni().addAll(pv.tmp);
		dto.getComposizioneEmail().addAll(cv.tmp);
		dto.getComposizioneAllegati().addAll(av.tmp);
	}

	private static final ComposizioneFascicoloComparator CFC = new ComposizioneFascicoloComparator();

	private static List<ElementoElenco> sort(Collection<ElementoElenco> elencoElementi) {
		List<ElementoElenco> list = new ArrayList<ElementoElenco>(elencoElementi);
		Collections.sort(list, CFC);
		return list;
	}

	@RequiredArgsConstructor
	private static abstract class VisitorAdapter<T> implements ElementoElencoVisitor {
		protected final Fascicolo fascicolo;
		protected final PraticaSessionUtil praticaSessionUtil;
		protected final Logger log;

		protected final List<T> tmp = new LinkedList<>();

		@Override
		public void visit(ElementoGruppo elementoGruppo) {
			for (ElementoElenco e : sort(elementoGruppo.getElementi())) {
				e.accept(this);
			}
		}

		@Override
		public void visit(ElementoPraticaModulisticaRiferimento elementoPraticaModulisticaRiferimento) {
			// solo per protocollazione
		}

		@Override
		public void visit(ElementoComunicazioneRiferimento elementoComunicazioneRiferimento) {
			// non viene implementato da nessuno
		}

		protected int uniqueIdentifier(ElementoElenco e) {
			return System.identityHashCode(e);
		}

		protected PraticaModulistica loadPraticaModulistica(String clientID) {
			try {
				return praticaSessionUtil.loadPraticaModulisticaFromEncodedPath(clientID, TipologiaCaricamento.RICARICA);
			} catch (Exception e) {
				log.error("Errore nel caricamento della pratica modulistica: " + clientID, e);
			}
			return null;
		}

		protected PraticaEmailIn loadEmailIn(String encodedPath) {
			try {
				return praticaSessionUtil.loadPraticaEmailInFromEncodedPath(encodedPath, TipologiaCaricamento.RICARICA);
			} catch (Exception e) {
				log.error("Errore nel caricamento mail in con path: " + encodedPath, e);
			}
			return null;
		}

		protected PraticaEmailOut loadEmailOut(String encodedPath) {
			try {
				return praticaSessionUtil.loadPraticaEmailOutFromEncodedPath(encodedPath, TipologiaCaricamento.RICARICA);
			} catch (Exception e) {
				log.error("Errore nel caricamento mail out con path: " + encodedPath, e);
			}
			return null;
		}
	}

	private static class ProtocollazioneVisitor extends VisitorAdapter<ProtocollazioneComposizione> {
		private ProtocollazioneComposizione protocollazione = null;
		private ElementoProtocollatoBuilder builder = null;

		public ProtocollazioneVisitor(Fascicolo fascicolo, PraticaSessionUtil praticaSessionUtil, Logger log) {
			super(fascicolo, praticaSessionUtil, log);
		}

		public void aggiornaProtocollazioni(FascicoloDTO dto) {
			for (AllegatoDTO adto : dto.getAllegati()) {
				aggiornaProtocollazione(adto, tmp);
			}
		}

		private static void aggiornaProtocollazione(AllegatoDTO adto, List<ProtocollazioneComposizione> protocollazioni) {
			for (ProtocollazioneComposizione protocollazione : protocollazioni) {
				for (ElementoProtocollato e : protocollazione.getElementi()) {
					if (adto.getNome().equals(e.getNome()) && (adto.getVersioneCorrente() == null || adto.getVersioneCorrente().equals(e.getVersione()))) {
						adto.setProtocollato(e.isProtocollato());
					}
				}
				aggiornaProtocollazione(adto, protocollazione.getNonCapofila());
			}
		}

		@Override
		public void visit(ElementoGruppoProtocollatoCapofila elementoGruppoProtocollatoCapofila) {
			protocollazione = new ProtocollazioneComposizione(elementoGruppoProtocollatoCapofila.getNumeroPG(), elementoGruppoProtocollatoCapofila.getAnnoPG());
			for (ElementoElenco e : sortCapofila(elementoGruppoProtocollatoCapofila.getElementi())) {
				builder = ElementoProtocollato.builder().protocollato(true).capofila(true) //
						.numeroPg(elementoGruppoProtocollatoCapofila.getNumeroPG()) //
						.annoPg(elementoGruppoProtocollatoCapofila.getAnnoPG());
				if (e instanceof ElementoGruppoProtocollato) {
					ProtocollazioneVisitor nonCapofilaVisitor = new ProtocollazioneVisitor(fascicolo, praticaSessionUtil, log);
					e.accept(nonCapofilaVisitor);
					protocollazione.getNonCapofila().addAll(nonCapofilaVisitor.tmp);
				} else {
					e.accept(this);
				}
			}

			if (protocollazione.getElementi().isEmpty()) {
				builder.nome(elementoGruppoProtocollatoCapofila.getOggetto()) //
						.versione("") //
						.dataCaricamento(elementoGruppoProtocollatoCapofila.getDataProtocollazione()) //
						.stato(ConsolePecConstants.StatiElementiComposizioneFascicolo.STATO_ESTERNO);
				protocollazione.getElementi().add(builder.build());
			}

			tmp.add(protocollazione);
		}

		private static List<ElementoElenco> sortCapofila(Set<ElementoElenco> elementi) {
			List<ElementoElenco> sortedList = sort(elementi);
			Collections.sort(sortedList, new Comparator<ElementoElenco>() {
				@Override
				public int compare(ElementoElenco o1, ElementoElenco o2) {
					if (o1 instanceof ElementoGruppoProtocollato && !(o2 instanceof ElementoGruppoProtocollato)) {
						return -1;
					}
					return 0;
				}
			});
			return sortedList;
		}

		@Override
		public void visit(ElementoGruppoProtocollato elementoGruppoProtocollato) {
			protocollazione = new ProtocollazioneComposizione(elementoGruppoProtocollato.getNumeroPG(), elementoGruppoProtocollato.getAnnoPG(), //
					elementoGruppoProtocollato.getNumeroPGCapofila(), elementoGruppoProtocollato.getAnnoPGCapofila());
			for (ElementoElenco e : sort(elementoGruppoProtocollato.getElementi())) {
				builder = ElementoProtocollato.builder().protocollato(true).capofila(false) //
						.numeroPg(elementoGruppoProtocollato.getNumeroPG()) //
						.annoPg(elementoGruppoProtocollato.getAnnoPG()) //
						.numeroPgCapofila(elementoGruppoProtocollato.getNumeroPGCapofila()) //
						.annoPgCapofila(elementoGruppoProtocollato.getAnnoPGCapofila());
				e.accept(this);
			}
			tmp.add(protocollazione);
		}

		@Override
		public void visit(ElementoGrupppoNonProtocollato elementoGrupppoNonProtocollato) {
			for (ElementoElenco e : sort(elementoGrupppoNonProtocollato.getElementi())) {
				builder = ElementoProtocollato.builder().protocollato(false);
				protocollazione = new ProtocollazioneComposizione();
				e.accept(this);
				tmp.add(protocollazione);
			}
		}

		@Override
		public void visit(ElementoAllegato elementoAllegato) {
			builder.nome(elementoAllegato.getNome()) //
					.versione(elementoAllegato.getVersioneCorrente()) //
					.dataCaricamento(elementoAllegato.getDataCaricamento()) //
					.stato(elementoAllegato.getStato() != null ? elementoAllegato.getStato().name() : null);

			protocollazione.getElementi().add(builder.uid(uniqueIdentifier(elementoAllegato)).build());
		}

		@Override
		public void visit(ElementoPECRiferimento elementoPECRiferimento) {
			builder.nome("Email: PEC " + elementoPECRiferimento.getTipo().toString()) //
					.dataCaricamento(elementoPECRiferimento.getDataPec()) //
					.clientID(elementoPECRiferimento.getRiferimento());
			for (PraticaCollegata pc : fascicolo.getAllPraticheCollegate()) {
				String urlPec = Base64Utils.URLencodeAlfrescoPath(pc.getAlfrescoPath());
				if (elementoPECRiferimento.getRiferimento().equals(urlPec)) {
					if (PraticaUtil.isIngresso(pc.getTipo())) {
						PraticaEmailIn emailIn = loadEmailIn(urlPec);
						if (emailIn.getDati().getOggetto() != null && !emailIn.getDati().getOggetto().isEmpty()) {
							builder.nome(emailIn.getDati().getOggetto());
						}
						builder.stato(StatiElementiComposizioneFascicolo.STATO_EMAIL_IN);
					}
					if (PraticaUtil.isEmailOut(pc.getTipo())) {
						PraticaEmailOut emailOut = loadEmailOut(urlPec);
						if (emailOut.getDati().getOggetto() != null && !emailOut.getDati().getOggetto().isEmpty()) {
							builder.nome(emailOut.getDati().getOggetto());
						}
						builder.stato(StatiElementiComposizioneFascicolo.STATO_EMAIL_OUT);
					}
				}
			}
			protocollazione.getElementi().add(builder.uid(uniqueIdentifier(elementoPECRiferimento)).build());
		}

		@Override
		public void visit(ElementoPraticaModulisticaRiferimento elementoPraticaModulisticaRiferimento) {
			builder.stato(StatiElementiComposizioneFascicolo.STATO_PRATICA_MODULISTICA) //
					.clientID(elementoPraticaModulisticaRiferimento.getRiferimento()) //
					.dataCaricamento(elementoPraticaModulisticaRiferimento.getDataCaricamento());
			PraticaModulistica pm = loadPraticaModulistica(elementoPraticaModulisticaRiferimento.getRiferimento());
			if (pm != null && pm.getDati() != null) {
				builder.nome(pm.getDati().getNome());
			} else {
				builder.nome("Pratica modulistica danneggiata");
			}
			protocollazione.getElementi().add(builder.uid(uniqueIdentifier(elementoPraticaModulisticaRiferimento)).build());
		}

	}

	private static class ConversazioneVisitor extends VisitorAdapter<EmailComposizione> {
		private EmailComposizioneBuilder builder = null;
		private Map<String, EmailComposizione> replyMap = new HashMap<>();

		public ConversazioneVisitor(Fascicolo fascicolo, PraticaSessionUtil praticaSessionUtil, Logger log) {
			super(fascicolo, praticaSessionUtil, log);
		}

		public void aggiornaConversazioni() {
			List<EmailComposizione> tmpReply = new ArrayList<>();
			if (!replyMap.isEmpty()) {
				for (Entry<String, EmailComposizione> r : replyMap.entrySet()) {
					for (EmailComposizione a : tmp) {
						if (r.getKey().equals(a.getMailID())) {
							a.getConversazione().add(r.getValue());
							tmpReply.add(r.getValue());
						}
					}
				}
			}
			if (!tmpReply.isEmpty()) {
				for (EmailComposizione a : tmpReply) {
					if (tmp.contains(a))
						tmp.remove(a);
				}
			}

			Collections.sort(tmp, new Comparator<EmailComposizione>() {
				@Override
				public int compare(EmailComposizione o1, EmailComposizione o2) {
					if (o1 == null || o1.getDataCaricamento() == null || o2 == null || o2.getDataCaricamento() == null)
						return 0;
					return o2.getDataCaricamento().compareTo(o1.getDataCaricamento());
				}
			});
		}

		@Override
		public void visit(ElementoGruppoProtocollatoCapofila elementoGruppoProtocollatoCapofila) {
			for (ElementoElenco e : sort(elementoGruppoProtocollatoCapofila.getElementi())) {
				builder = EmailComposizione.builder() //
						.protocollato(true) //
						.numeroPg(elementoGruppoProtocollatoCapofila.getNumeroPG()) //
						.annoPg(elementoGruppoProtocollatoCapofila.getAnnoPG());
				e.accept(this);
			}
		}

		@Override
		public void visit(ElementoGruppoProtocollato elementoGruppoProtocollato) {
			for (ElementoElenco e : sort(elementoGruppoProtocollato.getElementi())) {
				builder = EmailComposizione.builder() //
						.protocollato(true) //
						.numeroPg(elementoGruppoProtocollato.getNumeroPG()) //
						.annoPg(elementoGruppoProtocollato.getAnnoPG());
				e.accept(this);
			}
		}

		@Override
		public void visit(ElementoGrupppoNonProtocollato elementoGrupppoNonProtocollato) {
			for (ElementoElenco e : sort(elementoGrupppoNonProtocollato.getElementi())) {
				builder = EmailComposizione.builder().protocollato(false);
				e.accept(this);
			}
		}

		@Override
		public void visit(ElementoAllegato elementoAllegato) {
			// non lo uso
		}

		@Override
		public void visit(ElementoPECRiferimento elementoPECRiferimento) {
			String replyTo = null;
			builder.nome("Email tipo: PEC " + elementoPECRiferimento.getTipo().toString()) //
					.tipo(elementoPECRiferimento.getTipo().name()) //
					.dataCaricamento(elementoPECRiferimento.getDataPec()) //
					.clientID(elementoPECRiferimento.getRiferimento());
			for (PraticaCollegata pc : fascicolo.getAllPraticheCollegate()) {
				String urlPec = Base64Utils.URLencodeAlfrescoPath(pc.getAlfrescoPath());
				if (elementoPECRiferimento.getRiferimento().equals(urlPec)) {
					PraticaEmail email = null;
					if (PraticaUtil.isIngresso(pc.getTipo())) {
						builder.pecOut(false);
						email = loadEmailIn(urlPec);
					}
					if (PraticaUtil.isEmailOut(pc.getTipo())) {
						builder.pecOut(true);
						email = loadEmailOut(urlPec);
					}
					if (email != null && email.getDati() != null) {
						List<String> destinatari = loadDestinatari(email.getDati().getDestinatari());
						destinatari.addAll(email.getDati().getDestinatariInoltro());
						if (email.getDati().getDestinatarioPrincipale() != null && email.getDati().getDestinatarioPrincipale().getDestinatario() != null)
							destinatari.add(email.getDati().getDestinatarioPrincipale().getDestinatario());
						builder.stato(email.getDati().getStato().name()) //
								.mailID(email.getDati().getIdDocumentale()) //
								.oggetto(email.getDati().getOggetto()) //
								.mittente(email.getDati().getMittente()) //
								.destinatari(destinatari) //
								.destinatariCC(loadDestinatari(email.getDati().getDestinatariCC()));
						if (email.getDati().getReplyTo() != null && !email.getDati().getReplyTo().isEmpty()) {
							replyTo = email.getDati().getReplyTo();
							builder.reply(true);
						} else {
							builder.reply(false);
						}
						if (email.getDati().getAllegati() != null && !email.getDati().getAllegati().isEmpty()) {
							addAllegatiEmail(builder, email.getDati().getAllegati(), elementoPECRiferimento.getRiferimento());
						}
					}
				}
			}
			EmailComposizione allegatoConversazione = builder.uid(uniqueIdentifier(elementoPECRiferimento)).build();
			if (replyTo != null) {
				replyMap.put(replyTo, allegatoConversazione);
			}
			tmp.add(allegatoConversazione);
		}

		private static List<String> loadDestinatari(List<Destinatario> destinatari) {
			List<String> tmp = new ArrayList<>();
			if (destinatari != null) {
				for (Destinatario destinatario : destinatari) {
					tmp.add(destinatario.getDestinatario());
				}
			}
			return tmp;
		}

		private static void addAllegatiEmail(EmailComposizioneBuilder emailBuilder, TreeSet<Allegato> allegati, String clientID) {
			AllegatoComposizioneBuilder builder = null;
			List<AllegatoComposizione> allegatiEmail = new ArrayList<>();
			for (Allegato a : allegati) {
				builder = AllegatoComposizione.builder() //
						.nome(a.getNome()) //
						.versione(a.getCurrentVersion()) //
						.clientID(clientID) //
						.stato("ALLEGATO MAIL") //
						.dataCaricamento(a.getDataCaricamento());
				allegatiEmail.add(builder.build());
			}
			emailBuilder.allegatiEmail(allegatiEmail);
		}

	}

	private static class AllegatoVisitro extends VisitorAdapter<AllegatoComposizione> {
		private AllegatoComposizioneBuilder builder = null;

		public AllegatoVisitro(Fascicolo fascicolo, PraticaSessionUtil praticaSessionUtil, Logger log) {
			super(fascicolo, praticaSessionUtil, log);
		}

		public void aggiornaAllegati() {
			Collections.sort(tmp, new Comparator<AllegatoComposizione>() {
				@Override
				public int compare(AllegatoComposizione o1, AllegatoComposizione o2) {
					if (o1 == null || o1.getDataCaricamento() == null || o2 == null || o2.getDataCaricamento() == null)
						return 0;
					return o2.getDataCaricamento().compareTo(o1.getDataCaricamento());
				}
			});
		}

		@Override
		public void visit(ElementoGruppoProtocollatoCapofila elementoGruppoProtocollatoCapofila) {
			for (ElementoElenco e : sort(elementoGruppoProtocollatoCapofila.getElementi())) {
				builder = AllegatoComposizione.builder() //
						.protocollato(true) //
						.numeroPg(elementoGruppoProtocollatoCapofila.getNumeroPG()) //
						.annoPg(elementoGruppoProtocollatoCapofila.getAnnoPG());
				e.accept(this);
			}
		}

		@Override
		public void visit(ElementoGruppoProtocollato elementoGruppoProtocollato) {
			for (ElementoElenco e : sort(elementoGruppoProtocollato.getElementi())) {
				builder = AllegatoComposizione.builder() //
						.protocollato(true) //
						.numeroPg(elementoGruppoProtocollato.getNumeroPG()) //
						.annoPg(elementoGruppoProtocollato.getAnnoPG());
				e.accept(this);
			}
		}

		@Override
		public void visit(ElementoGrupppoNonProtocollato elementoGrupppoNonProtocollato) {
			for (ElementoElenco e : sort(elementoGrupppoNonProtocollato.getElementi())) {
				builder = AllegatoComposizione.builder().protocollato(false);
				e.accept(this);
			}
		}

		@Override
		public void visit(ElementoAllegato elementoAllegato) {
			builder.nome(elementoAllegato.getNome()) //
					.dataCaricamento(elementoAllegato.getDataCaricamento()) //
					.versione(elementoAllegato.getVersioneCorrente()) //
					.folderOriginPath(elementoAllegato.getFolderOriginPath()) //
					.folderOriginName(elementoAllegato.getFolderOriginName()) //
					.tag(elementoAllegato.getTipologiaAllegato()) //
					.stato(elementoAllegato.getStato() != null ? elementoAllegato.getStato().toString() : null);
			tmp.add(builder.uID(uniqueIdentifier(elementoAllegato)).build());
		}

		@Override
		public void visit(ElementoPECRiferimento elementoPECRiferimento) {
			// non lo uso
		}

	}

}
