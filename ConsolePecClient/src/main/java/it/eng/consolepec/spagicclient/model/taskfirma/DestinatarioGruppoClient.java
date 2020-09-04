package it.eng.consolepec.spagicclient.model.taskfirma;

/**
 *
 * Bean destinatario per la proposta
 *
 * @author biagiot
 *
 */
public class DestinatarioGruppoClient extends DestinatarioTaskFirmaClient {

	private String nomeGruppo;

	public DestinatarioGruppoClient(String nomeGruppo) {
		this.nomeGruppo = nomeGruppo;
	}

	public String getNomeGruppo() {
		return nomeGruppo;
	}

	public void setNomeGruppo(String nomeGruppo) {
		this.nomeGruppo = nomeGruppo;
	}
}
