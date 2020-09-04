package it.eng.cobo.consolepec.commons.configurazioni;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 *
 * @author biagiot
 *
 */
@Getter
@Setter
@EqualsAndHashCode(of = "nomeTipologia")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TipologiaPratica implements Serializable {

	private static final long serialVersionUID = -2173286129459711688L;

	@NonNull
	private String nomeTipologia;

	@NonNull
	private String etichettaTipologia;

	@NonNull
	protected Stato stato;

	protected boolean protocollabile = true;
	protected boolean azioniDettaglio = true;
	protected String dettaglioNameToken;

	public TipologiaPratica(String nomeTipologia) {
		this.nomeTipologia = nomeTipologia;
	}

	public static enum Stato {
		ATTIVA, DISATTIVA;

		public static Stato from(String value) {
			try {
				return Stato.valueOf(value);
			} catch (@SuppressWarnings("unused") Exception e) {
				return null;
			}
		}
	}

	@Override
	public String toString() {
		return this.nomeTipologia;
	}

	/*
	 * Tipologie Pratiche particolari, da gestire nel codice
	 */
	// Categoria Ingressi
	public static final TipologiaPratica EMAIL_IN = new TipologiaPratica("EMAIL_IN");
	public static final TipologiaPratica EMAIL_EPROTOCOLLO = new TipologiaPratica("EMAIL_EPROTOCOLLO");
	// Categoria Email Out
	public static final TipologiaPratica EMAIL_OUT = new TipologiaPratica("EMAIL_OUT");
	// Categoria Comunicazione
	public static final TipologiaPratica COMUNICAZIONE = new TipologiaPratica("COMUNICAZIONE");
	// Categoria P. modulistica
	public static final TipologiaPratica PRATICA_MODULISTICA = new TipologiaPratica("PRATICA_MODULISTICA");
	// Categoria Modelli
	public static final TipologiaPratica MODELLO_MAIL = new TipologiaPratica("TEMPLATE");
	public static final TipologiaPratica MODELLO_PDF = new TipologiaPratica("TEMPLATE_DOCUMENTO_PDF");
	// Categoria Fascicoli
	public static final TipologiaPratica FASCICOLO = new TipologiaPratica("FASCICOLO");
	public static final TipologiaPratica FASCICOLO_PERSONALE = new TipologiaPratica("FASCICOLO_PERSONALE");
	public static final TipologiaPratica FASCICOLO_ALBO_PRETORIO = new TipologiaPratica("FASCICOLO_ALBO_PRETORIO");
	public static final TipologiaPratica FASCICOLO_RISERVATO = new TipologiaPratica("FASCICOLO_RISERVATO");
	public static final TipologiaPratica FASCICOLO_FATTURAZIONE_ELETTRONICA = new TipologiaPratica("FASCICOLO_FATTURAZIONE_ELETTRONICA");
	public static final TipologiaPratica FASCICOLO_ELETTORALE_ELETTORE = new TipologiaPratica("FASCICOLO_ELETTORALE_ELETTORE");
	public static final TipologiaPratica FASCICOLO_ELETTORALE_COMUNICAZIONI = new TipologiaPratica("FASCICOLO_ELETTORALE_COMUNICAZIONI");
	public static final TipologiaPratica FASCICOLO_ELETTORALE_GENERICO = new TipologiaPratica("FASCICOLO_ELETTORALE_GENERICO");
	public static final TipologiaPratica FASCICOLO_SPORT_BORGOPANIGALE = new TipologiaPratica("FASCICOLO_SPORT_BORGOPANIGALE");
	public static final TipologiaPratica FASCICOLO_SPORT_NAVILE = new TipologiaPratica("FASCICOLO_SPORT_NAVILE");
	public static final TipologiaPratica FASCICOLO_SPORT_PORTO = new TipologiaPratica("FASCICOLO_SPORT_PORTO");
	public static final TipologiaPratica FASCICOLO_SPORT_RENO = new TipologiaPratica("FASCICOLO_SPORT_RENO");
	public static final TipologiaPratica FASCICOLO_SPORT_SANDONATO = new TipologiaPratica("FASCICOLO_SPORT_SANDONATO");
	public static final TipologiaPratica FASCICOLO_SPORT_SANTOSTEFANO = new TipologiaPratica("FASCICOLO_SPORT_SANTOSTEFANO");
	public static final TipologiaPratica FASCICOLO_SPORT_SANVITALE = new TipologiaPratica("FASCICOLO_SPORT_SANVITALE");
	public static final TipologiaPratica FASCICOLO_SPORT_SARAGOZZA = new TipologiaPratica("FASCICOLO_SPORT_SARAGOZZA");
	public static final TipologiaPratica FASCICOLO_SPORT_SAVENA = new TipologiaPratica("FASCICOLO_SPORT_SAVENA");
	public static final TipologiaPratica FASCICOLO_SPORT_PORTO_SARAGOZZA = new TipologiaPratica("FASCICOLO_SPORT_PORTO_SARAGOZZA");
	public static final TipologiaPratica FASCICOLO_SPORT_BORGOPANIGALE_RENO = new TipologiaPratica("FASCICOLO_SPORT_BORGOPANIGALE_RENO");
	public static final TipologiaPratica FASCICOLO_SPORT_SANDONATO_SANVITALE = new TipologiaPratica("FASCICOLO_SPORT_SANDONATO_SANVITALE");
	public static final TipologiaPratica FASCICOLO_SALUTE_SPORT = new TipologiaPratica("FASCICOLO_SALUTE_SPORT");
	public static final TipologiaPratica FASCICOLO_MODULISTICA = new TipologiaPratica("FASCICOLO_MODULISTICA");
	// Procedi
	public static final TipologiaPratica PRATICA_PROCEDI = new TipologiaPratica("PRATICA_PROCEDI");

	/*
	 * Dati di default per fascicoli, ingressi e email out
	 */
	public static final String FASCICOLO_DETTAGLIO_NAME_TOKEN_DEFAULT = "dettagliofascicolo";
	public static final String INGRESSO_DETTAGLIO_NAME_TOKEN_DEFAULT = "dettagliopecin";
	public static final String INGRESSO_EMAIL_IN_ETICHETTA_DEFAULT = "Email in Ingresso";
	public static final String INGRESSO_EMAIL_EPROTOCOLLO_ETICHETTA_DEFAULT = "Email EProtocollo";
	public static final String EMAIL_OUT_ETICHETTA_DEFAULT = "Email in Uscita";
	public static final String EMAIL_OUT_DETTAGLIO_NAME_TOKEN_DEFAULT = "dettagliopecout";

	/*
	 *
	 */
	public static final String IDENTIFICATIVO_TIPOLOGIE_MODELLI = "MODELLO";
}
