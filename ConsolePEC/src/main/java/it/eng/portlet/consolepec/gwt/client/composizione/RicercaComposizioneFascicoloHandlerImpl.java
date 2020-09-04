package it.eng.portlet.consolepec.gwt.client.composizione;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.AllegatoComposizione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ElementoProtocollato;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.EmailComposizione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ProtocollazioneComposizione;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

public class RicercaComposizioneFascicoloHandlerImpl implements RicercaComposizioneFascicoloHandler {

	private Map<String, Set<Object>> index = new HashMap<>();

	@Override
	public void index(FascicoloDTO fascicolo) {
		Set<AllegatoComposizione> viewAllegati = new HashSet<>(fascicolo.getComposizioneAllegati());
		for (AllegatoComposizione a : viewAllegati) {
			if (a.getFolderOriginName() != null) {
				add(a.getFolderOriginName(), a, true);
			} else if (a.getNome() != null) {
				add(a.getNome(), a, true);
			}
			
			if (a.getFolderOriginPath() != null) {
				add(a.getFolderOriginPath(), a, false);
			}
			
			for (String tag : a.getTag()) {
				add(tag, a, false);
			}
		}
		indexEmail(fascicolo.getComposizioneEmail());
		indexProtocolli(fascicolo.getComposizioneProtocollazioni());
	}

	private void indexEmail(List<EmailComposizione> emails) {
		if (!emails.isEmpty()) {
			Set<EmailComposizione> viewEmail = new HashSet<>(emails);
			for (EmailComposizione mail : viewEmail) {
				if (mail.getOggetto() != null) {
					add(mail.getOggetto(), mail, true);
				}
				if (mail.getMittente() != null) {
					add(mail.getMittente(), mail, false);
				}
				for (String destinatario : mail.getDestinatari()) {
					add(destinatario, mail, false);
				}
				for (AllegatoComposizione allegato : mail.getAllegatiEmail()) {
					add(allegato.getNome(), mail, true);
				}
				indexEmail(mail.getConversazione());
			}
		}
	}

	private void indexProtocolli(List<ProtocollazioneComposizione> composizioneProtocollazioni) {
		for (ProtocollazioneComposizione protocollazione : composizioneProtocollazioni) {
			for (ElementoProtocollato elementoProtocollato : protocollazione.getElementi()) {
				if (elementoProtocollato.isProtocollato()) {
					// add(elementoProtocollato.getAnnoPg(), elementoProtocollato,false);
					// add(elementoProtocollato.getNumeroPg(), elementoProtocollato,false);
					add(elementoProtocollato.getNumeroPg() + "/" + elementoProtocollato.getAnnoPg(), elementoProtocollato, true);
				}
			}
			indexProtocolli(protocollazione.getNonCapofila());
		}
	}

	@Override
	public Set<String> autocomplete(String prefix) {
		prefix = prefix.toLowerCase();
		Set<String> result = new HashSet<>();
		for (String string : index.keySet()) {
			if (string.startsWith(prefix))
				result.add(string);
		}
		return result;
	}

	@Override
	public Set<Object> search(String key) {
		key = key.toLowerCase();
		Set<Object> result = new HashSet<>();
		for (String k : index.keySet()) {
			if (k.contains(key))
				result.addAll(index.get(k));
		}
		return result;
	}

	private void add(String key, Object obj, boolean tokenize) {
		key = key.toLowerCase();

		if (tokenize) {
			for (String token : tokenize(key)) {
				if (index.containsKey(token)) {
					index.get(token).add(obj);
				} else {
					Set<Object> s = new HashSet<>();
					s.add(obj);
					index.put(token, s);
				}
			}
		}

		if (index.containsKey(key)) {
			index.get(key).add(obj);
		} else {
			Set<Object> s = new HashSet<>();
			s.add(obj);
			index.put(key, s);
		}
	}

	private static String[] tokenize(String word) {
		return word.split("[^a-zA-Z0-9]+");
	}

	@Override
	public void clear() {
		index.clear();
	}

}
