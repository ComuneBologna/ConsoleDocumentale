package it.eng.portlet.consolepec.gwt.shared.model;

import java.util.ArrayList;
import java.util.List;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import lombok.Getter;

/**
 * Dto contenente i dettagli dell'allegato, con informazioni di firma più approfondite e versioni precedenti del file
 * 
 * @author pluttero
 * 
 */
public class DettagliAllegatoDTO extends AllegatoDTO {

	private List<InformazioniFirmaDTO> informazioniFirma = new ArrayList<InformazioniFirmaDTO>();
	private String UUID;
	private String hash;
	private String dataCreazioneLabel;
	private DettagliAllegatoDTO previousVersion;

	@Getter
	private List<DatoAggiuntivo> datiAggiuntivi = new ArrayList<>();

	public DettagliAllegatoDTO() {
		// ser
	}

	public DettagliAllegatoDTO(String nome, String folderOriginPath, String folderOriginName, String clientID, String versione, String uuid, boolean firmatoHash) {
		super(nome, folderOriginPath, folderOriginName, clientID, versione);
		this.setUUID(uuid);
		setFirmatoHash(firmatoHash);
	}

	public void addInformazioneDiFirma(String validoDal, String validoAl, String ca, String dataFirma, String status, String descr, String dn) {
		InformazioniFirmaDTO inf = new InformazioniFirmaDTO();
		inf.setCa(ca);
		inf.setDataFirma(dataFirma);
		inf.setDescr(descr);
		inf.setDn(dn);
		inf.setStatus(status);
		inf.setValidoAl(validoAl);
		inf.setValidoDal(validoDal);
		informazioniFirma.add(inf);
	}

	public InformazioniFirmaDTO[] getInformazioniFirma() {
		return informazioniFirma.toArray(new InformazioniFirmaDTO[informazioniFirma.size()]);
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public DettagliAllegatoDTO getPreviousVersion() {
		return previousVersion;
	}

	public void setPreviousVersion(DettagliAllegatoDTO previousVersion) {
		this.previousVersion = previousVersion;
	}

	public String getDataCreazioneLabel() {
		return dataCreazioneLabel;
	}

	public void setDataCreazioneLabel(String dataCreazioneLabel) {
		this.dataCreazioneLabel = dataCreazioneLabel;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
}
