package it.eng.cobo.consolepec.integration.sara.client.emissionepermesso;

import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.MotivoRilascio;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.TipoPermesso;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.TitolareGenerico;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.VeicoloGenerico;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class EmissionePermessoRequest {
	
	@Getter @Setter private TipoPermesso tipoPermesso;
	@Getter @Setter private TitolareGenerico titolare;
	@Getter @Setter private VeicoloGenerico veicolo;
	@Getter @Setter private Date dataIniVal;
	@Getter @Setter private Date dataFineVal;
	@Getter @Setter private MotivoRilascio motivoRilascio;
    
}
