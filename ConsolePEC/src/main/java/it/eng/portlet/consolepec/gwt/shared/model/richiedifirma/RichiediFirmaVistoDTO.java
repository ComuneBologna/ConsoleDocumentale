package it.eng.portlet.consolepec.gwt.shared.model.richiedifirma;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.ProponenteDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.TipoPropostaTaskFirmaDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author biagiot
 *
 */

@EqualsAndHashCode
@ToString
public class RichiediFirmaVistoDTO implements IsSerializable {

	@Getter @Setter private String oggettoDocumento;
	@Getter @Setter private String clientIdFascicolo;
	@Getter @Setter private TipoPropostaTaskFirmaDTO tipoRichiesta;
	@Getter @Setter private String note;
	@Getter private Set<DestinatarioDTO> destinatari = new LinkedHashSet<DestinatarioDTO>();
	@Getter @Setter private ProponenteDTO proponente;
	@Getter private Set<AllegatoRichiediFirmaDTO> allegati = new LinkedHashSet<AllegatoRichiediFirmaDTO>();
	@Getter private List<String> indirizziEmailNotifica = new ArrayList<String>();
	@Getter @Setter private Date dataScadenza;
	@Getter @Setter private Integer oraScadenza;
	@Getter @Setter private Integer minutoScadenza;
	@Getter @Setter private String mittenteOriginale;
	@Getter @Setter private String motivazione;
}
