package it.eng.consolepec.spagicclient.model.taskfirma;

/**
 *
 * @author biagiot
 *
 */
public enum OperazioneTaskFirmaClient {
	FIRMA(false),
	VISTO(false),
	DINIEGO(true),
	RITIRO(true),
	RICHIESTA(false),
	RISPONDI_PARERE(false),
	EVADI(true);

	boolean operazioneConclusiva;

	public boolean isOperazioneConclusiva() {
		return operazioneConclusiva;
	}

	OperazioneTaskFirmaClient(boolean operazioneConclusiva) {
		this.operazioneConclusiva = operazioneConclusiva;
	}
}
