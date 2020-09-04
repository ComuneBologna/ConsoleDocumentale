package it.eng.portlet.consolepec.gwt.shared.model.cartellafirma;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * @author biagiot
 * 
 */

@Getter
@Setter
public class DocumentoFirmaVistoDTO implements IsSerializable, Comparable<DocumentoFirmaVistoDTO> {

	private String clientIdFascicolo;
	private String titoloFascicolo;
	private TipologiaPratica tipologiaPratica;
	private String note;
	private AllegatoDTO allegato;

	private ProponenteDTO gruppoProponente;
	private String dataProposta;
	private String dataScadenza;
	private TipoPropostaTaskFirmaDTO tipoRichiesta;
	private StatoTaskFirmaDTO statoRichiesta;
	private Set<DestinatarioDTO> destinatariFirma;
	private String mittenteOriginale;

	private boolean operazioniDestinatarioAbilitate;
	private boolean operazioniProponenteAbilitate;
	private boolean riassegnabilePerProponente;

	private boolean letto;
	private Boolean taskPerRuoli;
	private Integer idTaskFirma;

	public static String getKey(DocumentoFirmaVistoDTO documentoFirmaVistoDTO) {
		return documentoFirmaVistoDTO.getClientIdFascicolo() + "_" + documentoFirmaVistoDTO.getAllegato().getNome();
	}

	@Override
	public int compareTo(DocumentoFirmaVistoDTO o) {

		if (clientIdFascicolo.compareTo(o.getClientIdFascicolo()) == 0) {
			return allegato.compareTo(o.getAllegato());
		}

		return clientIdFascicolo.compareTo(o.getClientIdFascicolo());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((allegato == null) ? 0 : allegato.hashCode());
		result = prime * result + ((clientIdFascicolo == null) ? 0 : clientIdFascicolo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;

		DocumentoFirmaVistoDTO other = (DocumentoFirmaVistoDTO) obj;

		if (allegato == null) {
			if (other.allegato != null) return false;
		} else if (!allegato.equals(other.allegato)) return false;

		if (clientIdFascicolo == null) {
			if (other.clientIdFascicolo != null) return false;
		} else if (!clientIdFascicolo.equals(other.clientIdFascicolo)) return false;

		return true;
	}
}
