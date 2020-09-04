package it.eng.portlet.consolepec.gwt.shared.action.cartellafirma;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.profilazione.Settore;

import java.util.HashSet;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author biagiot
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InformazioniRiassegnazioneFascicoliTaskFirmaDTO extends InformazioniNotificaTaskFirmaDTO implements IsSerializable{
	
	@Getter
	private Set<String> ids = new HashSet<String>();
	
	@Getter
	@Setter
	private String operatore;
	
	@Getter
	@Setter
	private Settore settore;
	
	@Getter
	@Setter
	AnagraficaRuolo anagraficaRuolo;

	public InformazioniRiassegnazioneFascicoliTaskFirmaDTO(Set<String> ids, AnagraficaRuolo anagraficaRuolo) {
		this.ids = ids;
		this.anagraficaRuolo = anagraficaRuolo;
	}
}
