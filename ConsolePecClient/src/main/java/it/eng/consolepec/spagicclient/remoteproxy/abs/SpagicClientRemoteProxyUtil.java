package it.eng.consolepec.spagicclient.remoteproxy.abs;

import java.util.Date;

import it.bologna.comune.base.Error;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientErrorCode;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public class SpagicClientRemoteProxyUtil {

	public static SpagicClientException processErrorResponse(Error error) {

		if (error == null || error.getType() == null) {
			return new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}

		SpagicClientErrorCode ec = SpagicClientErrorCode.from(error.getType());

		if (ec == null) {
			return new SpagicClientException(SpagicClientErrorCode.EAPPLICATION);
		}

		return new SpagicClientException(ec, error.getMessage() != null && !error.getMessage().isEmpty() ? error.getMessage() : ec.getDefaultMessage());
	}

	public static it.bologna.comune.base.Utente convert(it.eng.consolepec.spagicclient.Utente user) {
		it.bologna.comune.base.Utente utente = new it.bologna.comune.base.Utente();
		utente.setName(user.getNome());
		utente.setSurname(user.getCognome());
		utente.setUsername(user.getUsername());
		utente.setMatricola(user.getMatricola());
		utente.setCodicefiscale(user.getCodicefiscale());
		utente.getRuoli().addAll(user.getRuoli());
		utente.setUtenteEsterno(user.isUtenteEsterno());
		return utente;
	}

	public static it.eng.consolepec.spagicclient.Utente convert(it.bologna.comune.base.Utente user) {
		Date data = null;
		if (user.getDataPresaInCarico() != null) {
			data = user.getDataPresaInCarico().toGregorianCalendar().getTime();
		}

		return new Utente(user.getName(), user.getSurname(), user.getUsername(), user.getMatricola(), user.getCodicefiscale(), user.getRuoli(), data,
				user.isUtenteEsterno() == null ? false : user.isUtenteEsterno());
	}
}
