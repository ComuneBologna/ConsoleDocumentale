package it.eng.portlet.consolepec.spring.bean.stampe;

import java.io.InputStream;

public interface GestioneStampe {
	
	public static class StampaBean {
		
		private String fileName;
		private InputStream fileStream;
		
		public StampaBean(String fileName, InputStream fileStream) {
			super();
			this.fileName = fileName;
			this.fileStream = fileStream;
		}

		public String getFileName() {
			return fileName;
		}

		public InputStream getFileStream() {
			return fileStream;
		}
	}
	
	
	public StampaBean stampaRicevutaDiConsegna(String praticaPath) throws Exception;
	
	public StampaBean stampaRiversamentoCartaceo(String praticaPath, String annoPG, String numPG) throws Exception;

}
