package it.eng.portlet.consolepec.gwt.shared.model;

import it.eng.portlet.consolepec.gwt.client.util.GestionePraticaModulisticaUtil;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecUtils;

import java.util.Date;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.gwt.user.client.rpc.IsSerializable;

public class PraticaModulisticaSportDTO extends PraticaModulisticaDTO implements IsSerializable{
	
	public PraticaModulisticaSportDTO(String alfrescoPath) {
		super(alfrescoPath);
	}
	
	public PraticaModulisticaSportDTO() {

	}
	
	public String getCodiceFiscaleProvenienza() {
		Map<String, ValoreModuloDTO> mappaValori = ConsolePecUtils.buildValoriTotaliMapByNome(this);
		
		ValoreModuloDTO cfDTO = mappaValori.get(GestionePraticaModulisticaUtil.MODULO_CODICE_FISCALE);
		
		return cfDTO != null ? cfDTO.getValore() : null;
	}

	public String getTitoloProtocollazione() {
		Map<String, ValoreModuloDTO> mappaValori = ConsolePecUtils.buildValoriTotaliMapByNome(this);
		
		ValoreModuloDTO da = mappaValori.get(GestionePraticaModulisticaUtil.MODULO_ANNO_SPORT_DA);
		ValoreModuloDTO a = mappaValori.get(GestionePraticaModulisticaUtil.MODULO_ANNO_SPORT_A);
		
		String daVal = da != null ? da.getValore() : null;
		String aVal = a != null ? a.getValore() : null;
		
		return "Richiesta di assegnazione in uso di impianti sportivi anno sportivo " +  daVal + "/" + aVal;
	}
	
	public String getProvenienza() {
		
		Map<String, ValoreModuloDTO> mappaValori = ConsolePecUtils.buildValoriTotaliMapByNome(this);
		
		ValoreModuloDTO nomeDTO = mappaValori.get(GestionePraticaModulisticaUtil.MODULO_NOME_RICHIEDENTE);
		ValoreModuloDTO cognomeDTO = mappaValori.get(GestionePraticaModulisticaUtil.MODULO_COGNOME_RICHIEDENTE);
		
		String nome = nomeDTO != null ? nomeDTO.getValore() : null;
		String cognome = cognomeDTO != null ? cognomeDTO.getValore() : null;
		if (Strings.isNullOrEmpty(nome) || Strings.isNullOrEmpty(cognome))
			return null;
		return cognome + "/" + nome;
	}
	
	@Override
	public Date getDataFineBando() {
		// TODO Auto-generated method stub
		return super.getDataFineBando();
	}
	
	@Override
	public Date getDataInizioBando() {
		// TODO Auto-generated method stub
		return super.getDataInizioBando();
	}
	
	
	
}
