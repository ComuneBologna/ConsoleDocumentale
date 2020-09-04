package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

public interface RimuoviDatoAggiuntivoTaskApi extends ITaskApi {

	public void rimuoviDatoAggiuntivo(String nomeCampo);

	public void rimuoviDatoAggiuntivoIfExists(String nomeCampo);

	public void rimuoviDatiAggiuntivi();

}
