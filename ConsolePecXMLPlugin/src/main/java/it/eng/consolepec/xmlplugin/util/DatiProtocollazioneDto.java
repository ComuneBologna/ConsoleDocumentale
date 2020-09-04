package it.eng.consolepec.xmlplugin.util;

import java.util.Date;

import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.TipoProtocollazione;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.ProtocollazionePEC;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Protocollazione;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila;
import lombok.Setter;

public class DatiProtocollazioneDto implements Comparable<DatiProtocollazioneDto> {

	private static final String SEPARATORE_COLONNA = ",";
	private static final String SEPARATORE_RIGA = ";";

	String numeroPG, numeroFascicolo, numeroPraticaModulistica, titolo, rubrica, sezione, tipologiadocumento, oggetto, provenienza, utenteprotocollazione, numeroRegistro;
	Integer annoPG, annoRegistro, annoFascicolo;
	Date dataprotocollazione;
	TipoProtocollazione tipoProtocollazione;

	public DatiProtocollazioneDto() {

	}

	public DatiProtocollazioneDto(ProtocollazioneCapofila protocollazioneCapofila) {

		this.annoPG = protocollazioneCapofila.getAnnoPG();
		this.annoRegistro = protocollazioneCapofila.getAnnoRegistro();
		this.dataprotocollazione = protocollazioneCapofila.getDataprotocollazione();
		this.numeroFascicolo = protocollazioneCapofila.getNumeroFascicolo();
		this.annoFascicolo = protocollazioneCapofila.getAnnoFascicolo();
		this.numeroPG = protocollazioneCapofila.getNumeroPG();
		this.numeroRegistro = protocollazioneCapofila.getNumeroRegistro();
		this.oggetto = protocollazioneCapofila.getOggetto();
		this.provenienza = protocollazioneCapofila.getProvenienza();
		this.rubrica = protocollazioneCapofila.getRubrica();
		this.sezione = protocollazioneCapofila.getSezione();
		this.titolo = protocollazioneCapofila.getTitolo();
		this.utenteprotocollazione = protocollazioneCapofila.getUtenteprotocollazione();
		this.tipologiadocumento = protocollazioneCapofila.getTipologiadocumento();
		this.tipoProtocollazione = protocollazioneCapofila.getTipoProtocollazione();

	}
	public DatiProtocollazioneDto(it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.ProtocollazioneCapofila protocollazioneCapofila) {
		this.annoPG = protocollazioneCapofila.getAnnoPG();
		this.annoRegistro = protocollazioneCapofila.getAnnoRegistro();
		this.dataprotocollazione = protocollazioneCapofila.getDataprotocollazione();
		this.numeroPraticaModulistica = protocollazioneCapofila.getNumeroPraticaModulistica();
		this.annoFascicolo = protocollazioneCapofila.getAnnoFascicolo();
		this.numeroPG = protocollazioneCapofila.getNumeroPG();
		this.numeroRegistro = protocollazioneCapofila.getNumeroRegistro();
		this.oggetto = protocollazioneCapofila.getOggetto();
		this.provenienza = protocollazioneCapofila.getProvenienza();
		this.rubrica = protocollazioneCapofila.getRubrica();
		this.sezione = protocollazioneCapofila.getSezione();
		this.titolo = protocollazioneCapofila.getTitolo();
		this.utenteprotocollazione = protocollazioneCapofila.getUtenteprotocollazione();
		this.tipologiadocumento = protocollazioneCapofila.getTipologiadocumento();
		this.tipoProtocollazione = protocollazioneCapofila.getTipoProtocollazione();

	}
	public DatiProtocollazioneDto(Protocollazione protocollazione) {

		this.annoPG = protocollazione.getAnnoPG();
		this.annoRegistro = protocollazione.getAnnoRegistro();
		this.dataprotocollazione = protocollazione.getDataprotocollazione();
		this.numeroFascicolo = protocollazione.getNumeroFascicolo();
		this.annoFascicolo = protocollazione.getAnnoFascicolo();
		this.numeroPG = protocollazione.getNumeroPG();
		this.numeroRegistro = protocollazione.getNumeroRegistro();
		this.oggetto = protocollazione.getOggetto();
		this.provenienza = protocollazione.getProvenienza();
		this.rubrica = protocollazione.getRubrica();
		this.sezione = protocollazione.getSezione();
		this.titolo = protocollazione.getTitolo();
		this.utenteprotocollazione = protocollazione.getUtenteprotocollazione();
		this.tipologiadocumento = protocollazione.getTipologiadocumento();
		this.tipoProtocollazione = protocollazione.getTipoProtocollazione();

	}

	public DatiProtocollazioneDto(it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.Protocollazione protocollazione) {
		this.annoPG = protocollazione.getAnnoPG();
		this.annoRegistro = protocollazione.getAnnoRegistro();
		this.dataprotocollazione = protocollazione.getDataprotocollazione();
		this.numeroPraticaModulistica = protocollazione.getNumeroPratica();
		this.annoFascicolo = protocollazione.getAnnoFascicolo();
		this.numeroPG = protocollazione.getNumeroPG();
		this.numeroRegistro = protocollazione.getNumeroRegistro();
		this.oggetto = protocollazione.getOggetto();
		this.provenienza = protocollazione.getProvenienza();
		this.rubrica = protocollazione.getRubrica();
		this.sezione = protocollazione.getSezione();
		this.titolo = protocollazione.getTitolo();
		this.utenteprotocollazione = protocollazione.getUtenteprotocollazione();
		this.tipologiadocumento = protocollazione.getTipologiadocumento();
		this.tipoProtocollazione = protocollazione.getTipoProtocollazione();
	}

	public DatiProtocollazioneDto(ProtocollazionePEC protocollazione) {
		this.annoPG = protocollazione.getAnnoPG();
		this.annoRegistro = protocollazione.getAnnoRegistro();
		this.dataprotocollazione = protocollazione.getDataprotocollazione();
		this.numeroFascicolo = protocollazione.getNumeroFascicolo();
		this.annoFascicolo = protocollazione.getAnnoFascicolo();
		this.numeroPG = protocollazione.getNumeroPG();
		this.numeroRegistro = protocollazione.getNumeroRegistro();
		this.oggetto = protocollazione.getOggetto();
		this.provenienza = protocollazione.getProvenienza();
		this.rubrica = protocollazione.getRubrica();
		this.sezione = protocollazione.getSezione();
		this.titolo = protocollazione.getTitolo();
		this.utenteprotocollazione = protocollazione.getUtenteprotocollazione();
		this.tipologiadocumento = protocollazione.getTipologiadocumento();
		this.tipoProtocollazione = protocollazione.getTipoProtocollazione();
	}

	public String getNumeroPG() {
		return numeroPG;
	}

	public void setNumeroPG(String numeroPG) {
		this.numeroPG = numeroPG;
	}

	public String getNumeroFascicolo() {
		return numeroFascicolo;
	}

	public void setNumeroFascicolo(String numeroFascicolo) {
		this.numeroFascicolo = numeroFascicolo;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getRubrica() {
		return rubrica;
	}

	public void setRubrica(String rubrica) {
		this.rubrica = rubrica;
	}

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getTipologiadocumento() {
		return tipologiadocumento;
	}

	public void setTipologiadocumento(String tipologiadocumento) {
		this.tipologiadocumento = tipologiadocumento;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public String getUtenteprotocollazione() {
		return utenteprotocollazione;
	}

	public void setUtenteprotocollazione(String utenteprotocollazione) {
		this.utenteprotocollazione = utenteprotocollazione;
	}

	public String getNumeroRegistro() {
		return numeroRegistro;
	}

	public void setNumeroRegistro(String numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	public Integer getAnnoPG() {
		return annoPG;
	}

	public void setAnnoPG(Integer annoPG) {
		this.annoPG = annoPG;
	}

	public Integer getAnnoRegistro() {
		return annoRegistro;
	}

	public void setAnnoRegistro(Integer annoRegistro) {
		this.annoRegistro = annoRegistro;
	}

	public Date getDataprotocollazione() {
		return dataprotocollazione;
	}

	public void setDataprotocollazione(Date dataprotocollazione) {
		this.dataprotocollazione = dataprotocollazione;
	}

	@Override
	public String toString() {
		DtoStringBuilder sb = new DtoStringBuilder();
		sb.setAnnoPG(annoPG.toString());
		sb.setDataprotocollazione(dataprotocollazione == null ? "" : DateUtils.DATEFORMAT_ISO8601.format(dataprotocollazione));
		sb.setNumeroFascicolo(numeroFascicolo);
		sb.setAnnoFascicolo(annoFascicolo != null ? annoFascicolo.toString() : "");
		sb.setNumeroPG(numeroPG);
		sb.setOggetto(oggetto);
		sb.setTipologiaDocumento(tipologiadocumento);
		sb.setProvenienza(provenienza);
		sb.setRubrica(rubrica);
		sb.setSezione(sezione);
		sb.setTitolo(titolo);
		sb.setNumeroRegistro(numeroRegistro);
		sb.setAnnoRegistro(annoRegistro != null ? annoRegistro.toString() : "");
		sb.setUtenteprotocollazione(utenteprotocollazione);
		if (XmlPluginUtil.serializeTipoProtocollazione(tipoProtocollazione) != null)
			sb.setTipoProtocollazione(XmlPluginUtil.serializeTipoProtocollazione(tipoProtocollazione).name());

		return sb.toString();
	}

	@Override
	public int compareTo(DatiProtocollazioneDto capofila) {
		if (annoPG.compareTo(capofila.getAnnoPG()) == 0) {
			return new Integer(numeroPG).compareTo(new Integer(capofila.getNumeroPG()));
		}
		return annoPG.compareTo(capofila.getAnnoPG());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof DatiProtocollazioneDto))
			return false;
		return toString().equals(obj.toString());
	}

	public static class DtoStringBuilder {
		@Setter
		String annoPG, dataprotocollazione, numeroFascicolo, annoFascicolo, numeroPG, oggetto, tipologiaDocumento, provenienza, rubrica, sezione, titolo, numeroRegistro, annoRegistro, utenteprotocollazione, tipoProtocollazione;

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();

			sb.append(NomiChiaviDatiProtocollazioneEnum.annoPG.name()).append("=").append(GenericsUtil.sanitizeNull(annoPG).toLowerCase()).append(SEPARATORE_COLONNA);
			sb.append(NomiChiaviDatiProtocollazioneEnum.dataprotocollazione.name()).append("=").append(GenericsUtil.sanitizeNull(dataprotocollazione).toLowerCase()).append(SEPARATORE_COLONNA);
			sb.append(NomiChiaviDatiProtocollazioneEnum.numeroFascicolo.name()).append("=").append(GenericsUtil.sanitizeNull(numeroFascicolo).toLowerCase()).append(SEPARATORE_COLONNA);
			if( annoFascicolo != null && !"".equalsIgnoreCase(annoFascicolo) )
				sb.append(NomiChiaviDatiProtocollazioneEnum.annoFascicolo.name()).append("=").append(GenericsUtil.sanitizeNull(annoFascicolo).toLowerCase()).append(SEPARATORE_COLONNA);
			sb.append(NomiChiaviDatiProtocollazioneEnum.numeroPG.name()).append("=").append(GenericsUtil.sanitizeNull(numeroPG).toLowerCase()).append(SEPARATORE_COLONNA);
			sb.append(NomiChiaviDatiProtocollazioneEnum.oggetto.name()).append("=").append(GenericsUtil.sanitizeNull(oggetto).toLowerCase()).append(SEPARATORE_COLONNA);
			sb.append(NomiChiaviDatiProtocollazioneEnum.tipologiadocumento.name()).append("=").append(GenericsUtil.sanitizeNull(tipologiaDocumento).toLowerCase()).append(SEPARATORE_COLONNA);
			sb.append(NomiChiaviDatiProtocollazioneEnum.provenienza.name()).append("=").append(GenericsUtil.sanitizeNull(provenienza).toLowerCase()).append(SEPARATORE_COLONNA);
			sb.append(NomiChiaviDatiProtocollazioneEnum.rubrica.name()).append("=").append(GenericsUtil.sanitizeNull(rubrica).toLowerCase()).append(SEPARATORE_COLONNA);
			sb.append(NomiChiaviDatiProtocollazioneEnum.sezione.name()).append("=").append(GenericsUtil.sanitizeNull(sezione).toLowerCase()).append(SEPARATORE_COLONNA);
			sb.append(NomiChiaviDatiProtocollazioneEnum.titolo.name()).append("=").append(GenericsUtil.sanitizeNull(titolo).toLowerCase()).append(SEPARATORE_COLONNA);
			sb.append(NomiChiaviDatiProtocollazioneEnum.numeroRegistro.name()).append("=").append(GenericsUtil.sanitizeNull(numeroRegistro).toLowerCase()).append(SEPARATORE_COLONNA);
			sb.append(NomiChiaviDatiProtocollazioneEnum.annoRegistro.name()).append("=").append(GenericsUtil.sanitizeNull(annoRegistro).toLowerCase()).append(SEPARATORE_COLONNA);
			sb.append(NomiChiaviDatiProtocollazioneEnum.tipoProtocollazione.name()).append("=").append(GenericsUtil.sanitizeNull(tipoProtocollazione)).append(SEPARATORE_COLONNA);
			sb.append(NomiChiaviDatiProtocollazioneEnum.utenteprotocollazione.name()).append("=").append(GenericsUtil.sanitizeNull(utenteprotocollazione).toLowerCase()).append(SEPARATORE_RIGA);

			return sb.toString();
		}
	}
}
