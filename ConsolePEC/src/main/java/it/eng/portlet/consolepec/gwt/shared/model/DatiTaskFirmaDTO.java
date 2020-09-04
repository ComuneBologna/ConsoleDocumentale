package it.eng.portlet.consolepec.gwt.shared.model;

import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.TipoPropostaTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.richiedifirma.AllegatoRichiediFirmaDTO;

import java.util.LinkedHashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author biagiot
 *
 */
public class DatiTaskFirmaDTO extends DatiTaskDTO implements IsSerializable{

	@Getter
	@Setter
	private String dataCreazione;
	@Getter
	private Set<DestinatarioDTO> destinatari = new LinkedHashSet<DestinatarioDTO>();
	@Getter
	@Setter
	private AllegatoRichiediFirmaDTO riferimentoAllegato;
	@Getter
	@Setter
	private TipoPropostaTaskFirmaDTO tipoRichiesta;
	@Getter
	@Setter
	private String dataScadenza;
	@Getter
	@Setter
	private String mittenteOriginale;
	@Getter
	@Setter
	private boolean valido = true;
}
