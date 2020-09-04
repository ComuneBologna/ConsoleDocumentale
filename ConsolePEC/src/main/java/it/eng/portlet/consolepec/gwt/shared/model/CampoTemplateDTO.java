package it.eng.portlet.consolepec.gwt.shared.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CampoTemplateDTO implements IsSerializable {

	private String nome;
	private TipoCampoTemplateDTO tipo;
	private String formato;
	private String regexValidazione;
	private Integer lunghezzaMassima;
	private List<String> valoriLista = new ArrayList<String>();
	private CampoMetadatoDTO campoMetadato;

	public CampoTemplateDTO() {
	}


	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public TipoCampoTemplateDTO getTipo() {
		return tipo;
	}

	public void setTipo(TipoCampoTemplateDTO tipo) {
		this.tipo = tipo;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public String getRegexValidazione() {
		return regexValidazione;
	}

	public void setRegexValidazione(String regexValidazione) {
		this.regexValidazione = regexValidazione;
	}

	public Integer getLunghezzaMassima() {
		return lunghezzaMassima;
	}

	public void setLunghezzaMassima(Integer lunghezzaMassima) {
		this.lunghezzaMassima = lunghezzaMassima;
	}

	public List<String> getValoriLista() {
		return valoriLista;
	}

	public CampoMetadatoDTO getCampoMetadato() {
		return campoMetadato;
	}


	public void setCampoMetadato(CampoMetadatoDTO campoMetadato) {
		this.campoMetadato = campoMetadato;
	}

	public enum TipoCampoTemplateDTO {
		TEXT("Testo", true),
		TEXTAREA("Area di testo", true),
		DATE("Data", true),
		LIST("Lista", true),
		INTEGER("Intero", true),
		DOUBLE("Decimale", true),
		YESNO("Si/No", true),
		METADATA("Metadati fascicolo", false);

		private String label;
		private boolean enabled;

		private TipoCampoTemplateDTO(String label, boolean enabled) {
			this.label = label;
			this.enabled = enabled;
		}

		public static String[] getAllLabels(){
			TipoCampoTemplateDTO[] values = values();
			String[] labels = new String[values.length];
			for(int i = 0; i < values.length; i++)
				labels[i] = values[i].label;
			return labels;
		}

		public static String[] getEnabledLabels(){
			List<String> labels = new ArrayList<String>();
			TipoCampoTemplateDTO[] values = values();

			for (TipoCampoTemplateDTO t : values) {
				if (t.isEnabled())
					labels.add(t.getLabel());
			}

			return labels.toArray(new String[labels.size()]);
		}

		public static TipoCampoTemplateDTO fromLabel(String l){
			for(TipoCampoTemplateDTO dc: values()) {
				if(dc.label.equals(l)) return dc;
			}
			return null;
		}

		public String getLabel() {
			return label;
		}

		public boolean isEnabled() {
			return enabled;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CampoTemplateDTO other = (CampoTemplateDTO) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (tipo != other.tipo)
			return false;
		return true;
	}
}