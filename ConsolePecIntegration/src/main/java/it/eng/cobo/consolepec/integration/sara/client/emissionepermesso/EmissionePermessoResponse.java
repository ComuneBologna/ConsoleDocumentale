package it.eng.cobo.consolepec.integration.sara.client.emissionepermesso;

import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.Esito;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.File;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class EmissionePermessoResponse {

	@Getter @Setter private Esito esito;
	@Getter @Setter private File file;
}
