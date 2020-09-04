package it.eng.portlet.consolepec.gwt.shared.model;

/**
 * Enum per identificare le colonne della worklist.
 *
 * @author pluttero
 *
 */
public enum ColonnaWorklist {
	SELEZIONE("check"), PG("numannopg"), PRESA_IN_CARICO("presaincarico"), TIPO_PRATICA("tipopratica"), STATO("stato"), TITOLO("titolo"), PROVENIENZA("provenienza"), DATA("data"),
	DESTINATARIO("mittente"), ASSEGNATARIO("assegnatario"), DATA_RIC("dataricezione"), DESCRIZIONE("descrizione"), CODICE("codice"), ID_TEMPLATE("idtemplate") //
	, CARTELLA_FIRMA_DATA_CREAZIONE("cartellafirmadataproposta") //
	, CARTELLA_FIRMA_DATA_SCADENZA("cartellafirmadatascadenza") //
	, CARTELLA_FIRMA_TITOLO_FASCICOLO("cartellafirmatitolofascicolo") //
	, CARTELLA_FIRMA_DETTAGLIO_ALLEGATO("cartellafirmadettaglioallegato") //
	, CARTELLA_FIRMA_OGGETTO("cartellafirmaoggetto") //
	, CARTELLA_FIRMA_PROPONENTE("cartellafirmaproponente") //
	, CARTELLA_FIRMA_TIPO_RICHIESTA("cartellafirmatiporichiesta") //
	, CARTELLA_FIRMA_DESTINATARI("cartellafirmadestinatari") //
	, CARTELLA_FIRMA_APRI("button");

	private String id;

	private ColonnaWorklist(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public static ColonnaWorklist getFromId(String id) {
		for (ColonnaWorklist cw : values()) {
			if (cw.getId().equals(id)) {
				return cw;
			}
		}
		return null;
	}
}
