package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni;

import java.util.List;

public interface RispostaInteroperabileTaskApi {
	
	public void inviaEccezione(List<String> indirizziNotifica, String pathAllegato, String nomeAllegato);
	
	public void inviaConferma(List<String> indirizziNotifica, String pathAllegato, String nomeAllegato);
	
	public void inviaAggiornamento(List<String> indirizziNotifica, String pathAllegato, String nomeAllegato);
	
	public void inviaAnnullamento(List<String> indirizziNotifica, String pathAllegato, String nomeAllegato);

}
