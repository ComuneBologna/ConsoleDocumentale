package it.eng.consolepec.spagicclient.remoteproxy.result;

import lombok.NoArgsConstructor;

@NoArgsConstructor // Per serializzazione JSON
public class LockedPratica {

	private String hash;
	private String metadatiXml;

	public LockedPratica(String hash, String metadatiXml) {
		super();
		this.hash = hash;
		this.metadatiXml = metadatiXml;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getMetadatiXml() {
		return metadatiXml;
	}

	public void setMetadatiXml(String metadatiXml) {
		this.metadatiXml = metadatiXml;
	}

}
