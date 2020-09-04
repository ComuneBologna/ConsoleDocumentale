package it.eng.portlet.consolepec.gwt.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Destinatario implements IsSerializable ,Comparable<Destinatario>{
	
	public enum StatoDestinatario {
		
		INVIATO("Inviato"),
		ACCETTATO("Accettato"),
		NON_ACCETTATO("Mancata accettazione"), 
		CONSEGNATO("Consegnato"), 
		NON_CONSEGNATO("Mancata consegna");
		
		private final String label;

		private StatoDestinatario(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
		
		public static StatoDestinatario fromName(String name){
			for (StatoDestinatario s : StatoDestinatario.values())
				if (s.name().equalsIgnoreCase(name))
					return s;
			return null;
		}
		
		public static StatoDestinatario fromLabel(String label){
			for (StatoDestinatario s : StatoDestinatario.values())
				if (s.getLabel().equalsIgnoreCase(label))
					return s;
			return null;
		}
	}

	private String destinatario;
	private TipoEmail tipoEmail;
	private boolean consegnato;
	private String errore;
	private StatoDestinatario statoDestinatario;

	public Destinatario() {
	}

	public Destinatario(String destinatario, String errore, TipoEmail tipoEmail, boolean consegnato) {
		this.destinatario = destinatario;
		this.tipoEmail = tipoEmail;
		this.consegnato = consegnato;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public TipoEmail getTipoEmail() {
		return tipoEmail;
	}

	public void setTipoEmail(TipoEmail tipoEmail) {
		this.tipoEmail = tipoEmail;
	}

	public boolean isConsegnato() {
		return consegnato;
	}

	public void setConsegnato(boolean consegnato) {
		this.consegnato = consegnato;
	}

	@Override
	public String toString() {
		return destinatario;
	}

	public String getErrore() {
		return errore;
	}

	public void setErrore(String errore) {
		this.errore = errore;
	}

	public String getLabel() {
		StringBuilder sb = new StringBuilder();
		sb.append(destinatario);
		//sb.append(consegnato ? " - (Consegnato)" : " - (Non Consegnato)");
		sb.append( (statoDestinatario == null) ? "" : " - " + statoDestinatario.getLabel() );
		if (errore != null) {
			sb.append(" (").append(errore).append(")");
		}
		return sb.toString();
	}

	@Override
	public int compareTo(Destinatario o) {
		return this.destinatario.compareToIgnoreCase(o.destinatario);
	}

	public StatoDestinatario getStatoDestinatario() {
		return statoDestinatario;
	}

	public void setStatoDestinatario(StatoDestinatario statoDestinatario) {
		this.statoDestinatario = statoDestinatario;
	}

}
