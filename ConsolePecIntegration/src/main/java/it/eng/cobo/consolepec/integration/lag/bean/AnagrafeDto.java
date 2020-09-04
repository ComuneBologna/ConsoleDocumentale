package it.eng.cobo.consolepec.integration.lag.bean;

import java.util.Iterator;
import java.util.LinkedHashMap;

import lombok.Setter;

public class AnagrafeDto {

	public static final String CODICE_COMUNE_BOLOGNA = "037006";

	public static final String COD_PROVENIENZA_AR = "1";
	public static final String COD_PROVENIENZA_AT = "2";
	public static final String COD_PROVENIENZA_AA = "3";

	public static final String XWC_OP = "XWC_OP";
	public static final String XWC_RC = "XWC_RC";
	public static final String XWC_COD_FISCALE = "XWC_COD_FISCALE";
	public static final String XWC_ENTE = "XWC_ENTE";
	public static final String XWC_MATRICOLA = "XWC_MATRICOLA";
	public static final String XWC_COG_NOME = "XWC_COG_NOME";
	public static final String XWC_DATA_NASCITA = "XWC_DATA_NASCITA";
	public static final String XWC_SEX = "XWC_SEX";
	public static final String XWC_NUMERO_FAMIGLIA = "XWC_NUMERO_FAMIGLIA";
	public static final String XWC_PARENTELA = "XWC_PARENTELA";
	public static final String XWC_NAS_CMN_COD = "XWC_NAS_CMN_COD";
	public static final String XWC_RES_CMN_COD = "XWC_RES_CMN_COD";
	public static final String XWC_RES_CMN_DES = "XWC_RES_CMN_DES";
	public static final String XWC_RES_PRV = "XWC_RES_PRV";
	public static final String XWC_RES_NUM_CIV_KEY = "XWC_RES_NUM_CIV_KEY";
	public static final String XWC_RES_DATA_ISCR = "XWC_RES_DATA_ISCR";
	public static final String XWC_INDIRIZZO_EDIT = "XWC_INDIRIZZO_EDIT";
	public static final String XWC_DATA_MORTE = "XWC_DATA_MORTE";
	public static final String XWC_SERVIZIO = "XWC_SERVIZIO";
	public static final String XWC_OPERATORE = "XWC_OPERATORE";
	public static final String XWC_DATA_EMIGRAZIONE = "XWC_DATA_EMIGRAZIONE";
	public static final String XWC_DATA_IRREPERIBILITA = "XWC_DATA_IRREPERIBILITA";
	public static final String XWC_DATA_ISCRIZIONE_AIRE = "XWC_DATA_ISCRIZIONE_AIRE";
	public static final String XWC_DATA_SCADENZA_AIRE = "XWC_DATA_SCADENZA_AIRE";
	public static final String XWC_DATA_ISCRIZIONE_ANAG_TEMP = "XWC_DATA_ISCRIZIONE_ANAG_TEMP";
	public static final String XWC_DATA_SCADENZA_ANAG_TEMP = "XWC_DATA_SCADENZA_ANAG_TEMP";
	public static final String XWC_DATA_RICHIESTA_RESIDENZA = "XWC_DATA_RICHIESTA_RESIDENZA";
	public static final String XWC_RES_RICHIESTA_NUM_CIV_KEY = "XWC_RES_RICHIESTA_NUM_CIV_KEY";
	public static final String XWC_COD_PROVENIENZA = "XWC_COD_PROVENIENZA";
	public static final String XWC_FILLER = "XWC_FILLER";

	private LinkedHashMap<String, GenericField> map = null;

	@Setter private String record;

	public AnagrafeDto() {
		super();
		map = new LinkedHashMap<String, GenericField>();

		map.put(XWC_OP, new GenericField(0, 1));
		map.put(XWC_RC, new GenericField(1, 3));
		map.put(XWC_COD_FISCALE, new GenericField(3, 19));
		map.put(XWC_ENTE, new GenericField(19, 21));
		map.put(XWC_MATRICOLA, new GenericField(21, 28));
		map.put(XWC_COG_NOME, new GenericField(28, 83));
		map.put(XWC_DATA_NASCITA, new GenericField(83, 91));
		map.put(XWC_SEX, new GenericField(91, 92));
		map.put(XWC_NUMERO_FAMIGLIA, new GenericField(92, 99));
		map.put(XWC_PARENTELA, new GenericField(99, 101));
		map.put(XWC_NAS_CMN_COD, new GenericField(101, 107));
		map.put(XWC_RES_CMN_COD, new GenericField(107, 113));
		map.put(XWC_RES_CMN_DES, new GenericField(113, 153));
		map.put(XWC_RES_PRV, new GenericField(153, 156));
		map.put(XWC_RES_NUM_CIV_KEY, new GenericField(156, 168));
		map.put(XWC_RES_DATA_ISCR, new GenericField(168, 176));
		map.put(XWC_INDIRIZZO_EDIT, new GenericField(176, 276));
		map.put(XWC_DATA_MORTE, new GenericField(276, 284));
		map.put(XWC_SERVIZIO, new GenericField(284, 288));
		map.put(XWC_OPERATORE, new GenericField(288, 328));
		map.put(XWC_DATA_EMIGRAZIONE, new GenericField(328, 336));
		map.put(XWC_DATA_IRREPERIBILITA, new GenericField(336, 344));
		map.put(XWC_DATA_ISCRIZIONE_AIRE, new GenericField(344, 352));
		map.put(XWC_DATA_SCADENZA_AIRE, new GenericField(352, 360));
		map.put(XWC_DATA_ISCRIZIONE_ANAG_TEMP, new GenericField(360, 368));
		map.put(XWC_DATA_SCADENZA_ANAG_TEMP, new GenericField(368, 376));
		map.put(XWC_DATA_RICHIESTA_RESIDENZA, new GenericField(376, 384));
		map.put(XWC_RES_RICHIESTA_NUM_CIV_KEY, new GenericField(384, 396));
		map.put(XWC_COD_PROVENIENZA, new GenericField(396, 397));
		map.put(XWC_FILLER, new GenericField(397, 2048));
	}

	public void setField(String fieldName, String fieldValue) {
		map.get(fieldName).setValue(fieldValue);
	}

	public String getField(String fieldName) {
		return record.substring(map.get(fieldName).getBegin(), map.get(fieldName).getEnd()).trim();
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer();
		Iterator<GenericField> iterator = map.values().iterator();
		while (iterator.hasNext()) {
			GenericField genericField = iterator.next();
			ret.append(genericField.getValue());
		}
		return ret.toString();
	}

}
