package it.eng.cobo.consolepec.commons.atti;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author GiacomoFM
 * @since 06/mar/2018
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentoTaskFirma {

	@Getter private Integer idTaskFirma;
	@Getter private String praticaPath;
	@Getter private DocumentoAssegnatario assegnatario;
	@Getter private List<DocumentoAssegnatario> assegnatariPassati = new ArrayList<>();
	@Getter private String stato;
	@Getter private String tipo;
	@Getter private String mittenteOriginale;
	@Getter private boolean attivo;
	@Getter private boolean valido;
	@Getter private Date dataCreazione;
	@Getter private Date dataScadenza;
	@Getter private DocumentoRiferimentoAllegato allegato;
	@Getter private TreeSet<DocumentoDestinatarioRichiestaApprovazione> destinatari = new TreeSet<>();

}
