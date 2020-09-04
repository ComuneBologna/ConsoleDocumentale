package it.eng.cobo.consolepec.integration.lag.bean;

import it.eng.cobo.consolepec.integration.lag.Utils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class PersonaFisicaDto {

	private PersonaFisicaDto() {}

	public enum Sesso {
		M, F
	}

	private String matricola;
	private String cognome;
	private String nome;
	private String codiceFiscale;
	private String dataNascita;
	private Sesso sesso;
	private String codComuneNascita;
	private String codComuneResidenza;
	private String numCivKeyResidenza;

	// Da decodificare
	@Setter private String descComuneNascita;
	@Setter private String codProvinciaNascita;
	@Setter private String descProvinciaNascita;
	@Setter private String codStatoNascita;
	@Setter private String descStatoNascita;
	@Setter private String descComuneResidenza;
	@Setter private String codProvinciaResidenza;
	@Setter private String descProvinciaResidenza;
	@Setter private String codStatoResidenza;
	@Setter private String descStatoResidenza;

	// Da decodificare
	@Setter private String indirizzoResidenza;
	@Setter private String indirizzoResidenzaCompleto;
	@Setter private String via;
	@Setter private Long civico;
	@Setter private String esponente;
	@Setter private String cap;

	public static class Factory {

		public static PersonaFisicaDto newInstance(AnagrafeDto anagrafeDto) throws Exception {
			if (anagrafeDto == null) {
				log.warn("anagrafeDto is null");
				throw new Exception("anagrafeDto is null");
			}

			if (Utils.isNull(anagrafeDto.getField(AnagrafeDto.XWC_MATRICOLA))) {
				log.warn("AnagrafeDto.XWC_MATRICOLA is null");
				throw new Exception("AnagrafeDto.XWC_MATRICOLA is null");
			}

			if (Utils.isNull(anagrafeDto.getField(AnagrafeDto.XWC_COG_NOME))) {
				log.warn("AnagrafeDto.XWC_COG_NOME is null");
				throw new Exception("AnagrafeDto.XWC_COG_NOME is null");
			}

			if (Utils.isNull(anagrafeDto.getField(AnagrafeDto.XWC_COD_FISCALE))) {
				log.warn("AnagrafeDto.XWC_COD_FISCALE is null");
				throw new Exception("AnagrafeDto.XWC_COD_FISCALE is null");
			}

			if (Utils.isNull(anagrafeDto.getField(AnagrafeDto.XWC_DATA_NASCITA))) {
				log.warn("AnagrafeDto.XWC_DATA_NASCITA is null");
				throw new Exception("AnagrafeDto.XWC_DATA_NASCITA is null");
			}

			if (Utils.isNull(anagrafeDto.getField(AnagrafeDto.XWC_SEX))) {
				log.warn("AnagrafeDto.XWC_SEX is null");
				throw new Exception("AnagrafeDto.XWC_SEX is null");
			}

			if (Utils.isNull(anagrafeDto.getField(AnagrafeDto.XWC_NAS_CMN_COD))) {
				log.warn("AnagrafeDto.XWC_NAS_CMN_COD is null");
				throw new Exception("AnagrafeDto.XWC_NAS_CMN_COD is null");
			}

			String codProvenienza = anagrafeDto.getField(AnagrafeDto.XWC_COD_PROVENIENZA);

			PersonaFisicaDto personaFisicaDto = new PersonaFisicaDto();

			personaFisicaDto.matricola = anagrafeDto.getField(AnagrafeDto.XWC_MATRICOLA);
			personaFisicaDto.cognome = anagrafeDto.getField(AnagrafeDto.XWC_COG_NOME).split("/")[0];
			personaFisicaDto.nome = anagrafeDto.getField(AnagrafeDto.XWC_COG_NOME).split("/")[1];
			personaFisicaDto.codiceFiscale = anagrafeDto.getField(AnagrafeDto.XWC_COD_FISCALE);
			personaFisicaDto.dataNascita = anagrafeDto.getField(AnagrafeDto.XWC_DATA_NASCITA);
			personaFisicaDto.sesso = Sesso.valueOf(anagrafeDto.getField(AnagrafeDto.XWC_SEX));
			personaFisicaDto.codComuneNascita = anagrafeDto.getField(AnagrafeDto.XWC_NAS_CMN_COD);
			personaFisicaDto.codComuneResidenza = AnagrafeDto.COD_PROVENIENZA_AR.equals(codProvenienza) ? AnagrafeDto.CODICE_COMUNE_BOLOGNA : anagrafeDto.getField(AnagrafeDto.XWC_RES_CMN_COD);
			personaFisicaDto.numCivKeyResidenza = anagrafeDto.getField(AnagrafeDto.XWC_RES_NUM_CIV_KEY);

			return personaFisicaDto;
		}
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("matricola: ").append(matricola).append("\n");
		stringBuffer.append("cognome: ").append(cognome).append("\n");
		stringBuffer.append("nome: ").append(nome).append("\n");
		stringBuffer.append("codiceFiscale: ").append(codiceFiscale).append("\n");
		stringBuffer.append("dataNascita: ").append(dataNascita).append("\n");
		stringBuffer.append("sesso: ").append(sesso).append("\n");
		stringBuffer.append("codComuneNascita: ").append(codComuneNascita).append("\n");
		stringBuffer.append("descComuneNascita: ").append(descComuneNascita).append("\n");
		stringBuffer.append("codProvinciaNascita: ").append(codProvinciaNascita).append("\n");
		stringBuffer.append("descProvinciaNascita: ").append(descProvinciaNascita).append("\n");
		stringBuffer.append("codStatoNascita: ").append(codStatoNascita).append("\n");
		stringBuffer.append("descStatoNascita: ").append(descStatoNascita).append("\n");
		stringBuffer.append("codComuneResidenza: ").append(codComuneResidenza).append("\n");
		stringBuffer.append("descComuneResidenza: ").append(descComuneResidenza).append("\n");
		stringBuffer.append("codProvinciaResidenza: ").append(codProvinciaResidenza).append("\n");
		stringBuffer.append("descProvinciaResidenza: ").append(descProvinciaResidenza).append("\n");
		stringBuffer.append("codStatoResidenza: ").append(codStatoResidenza).append("\n");
		stringBuffer.append("descStatoResidenza: ").append(descStatoResidenza).append("\n");
		stringBuffer.append("numCivKeyResidenza: ").append(numCivKeyResidenza).append("\n");
		stringBuffer.append("indirizzoResidenza: ").append(indirizzoResidenza).append("\n");
		stringBuffer.append("indirizzoResidenzaCompleto: ").append(indirizzoResidenzaCompleto).append("\n");
		stringBuffer.append("via: ").append(via).append("\n");
		stringBuffer.append("civico: ").append(civico).append("\n");
		stringBuffer.append("esponente: ").append(esponente).append("\n");
		stringBuffer.append("cap: ").append(cap);
		return stringBuffer.toString();
	}
}
