package it.eng.consolepec.spagicclient.remoteproxy.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum SpagicClientErrorCode {

	EAPPLICATION("Sistema momentaneamente non disponibile."), //
	EAUTHORIZATION("Utente non autorizzato"), //
	ELOCK("Pratica bloccata"), //
	EINVALIDARGUMENT("Parametri di richiesta non validi"), //
	ENOTFOUND("Pratica non trovata"); //

	@Getter
	private String defaultMessage;

	public static SpagicClientErrorCode from(String value) {

		for (SpagicClientErrorCode ec : SpagicClientErrorCode.values()) {
			if (ec.name().equalsIgnoreCase(value)) {
				return ec;
			}
		}

		return EAPPLICATION;

	}

	public static final String DEFAULT_ERROR_MESSAGE = EAPPLICATION.getDefaultMessage();
}
