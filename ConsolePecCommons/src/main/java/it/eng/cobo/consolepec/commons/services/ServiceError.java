package it.eng.cobo.consolepec.commons.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author biagiot
 *
 */
@NoArgsConstructor
@AllArgsConstructor
public class ServiceError {
	
	@Getter
	@Setter
	private String message;
	
	@Getter
	@Setter
	private ErrorCode code;
	
	
	public static enum ErrorCode {
		// Generici
		GE01("Errore generico"),
		RE01("Errore repository"),
		
		// Rubrica service
		RUBRSERV_E01("Anagrafica attiva esistente"),
		RUBRSERV_E02("Anagrafica non valida"),
		
		// SaraOnline
		SARAEMPERM01("Errore generico Sara Online"),
		SARAEMPERM02("Servizio non abilitato per il tipo pratica"),
		SARAEMPERM03("Errore validazione dati richiesta"),
		SARAEMPERM04("Errore nella risposta del servizio");
		
		@Getter
		String codeDescription;
		
		private ErrorCode(String codeDescription) {
			this.codeDescription = codeDescription;
		}
	}
}
