package it.eng.consolepec.xmlplugin.tasks.riattiva;

import it.eng.consolepec.xmlplugin.factory.Task;

public interface RiattivaTask extends Task<DatiRiattivazioneTask> {

	public void setCurrentUser(String user);

	public void riattivaTaskAssociato();

}
