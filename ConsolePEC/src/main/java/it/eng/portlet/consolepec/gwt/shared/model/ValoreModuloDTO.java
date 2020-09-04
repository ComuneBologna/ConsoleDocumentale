package it.eng.portlet.consolepec.gwt.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ValoreModuloDTO implements NodoModulisticaDTO, IsSerializable {
	private String nome;
	private String etichetta;
	private String valore;
	private String descrizione;
	private TipoValorModuloeDTO tipoValoreModulo;
	private TabellaModuloDTO tabella;
	private boolean visibile;

	public ValoreModuloDTO() {
		super();
	}

	public ValoreModuloDTO(String nome, String etichetta, String valore, String descrizione, TipoValorModuloeDTO tipoValoreModulo, TabellaModuloDTO tabella, boolean visibile) {
		super();
		this.nome = nome;
		this.etichetta = etichetta;
		this.valore = valore;
		this.descrizione = descrizione;
		this.tipoValoreModulo = tipoValoreModulo;
		this.tabella = tabella;
		this.visibile = visibile;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEtichetta() {
		return etichetta;
	}

	public void setEtichetta(String etichetta) {
		this.etichetta = etichetta;
	}

	public String getValore() {
		return valore;
	}

	public void setValore(String valore) {
		this.valore = valore;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public TipoValorModuloeDTO getTipoValoreModulo() {
		return tipoValoreModulo;
	}

	public void setTipoValoreModulo(TipoValorModuloeDTO tipoValoreModulo) {
		this.tipoValoreModulo = tipoValoreModulo;
	}

	public TabellaModuloDTO getTabella() {
		return tabella;
	}

	public void setTabella(TabellaModuloDTO tabella) {
		this.tabella = tabella;
	}

	public boolean isVisibile() {
		return visibile;
	}

	public void setVisibile(boolean visibile) {
		this.visibile = visibile;
	}

	public static enum TipoValorModuloeDTO {
		Valore, Tabella;
	}

	@Override
	public TipoNodoModulisticaDTO getTipoNodo() {
		return TipoNodoModulisticaDTO.VALORE_MODULO;
	}

	@Override
	public String toString() {
		return "ValoreModuloDTO [nome=" + nome + ", valore=" + valore + ", descrizione=" + descrizione + "]";
	}
	
}
