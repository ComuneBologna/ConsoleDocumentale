package it.eng.portlet.consolepec.gwt.shared.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Campo implements Serializable, Comparable<Campo> {

	private static final long serialVersionUID = -1831869372039818008L;

	private String nome, valore, tipoRecord, descrizione, descrizioneGruppo;
	private boolean visibile, modificabile, obbligatorio;
	private int maxOccurs = 1, lunghezza, posizione;
	private TipoWidget tipoWidget;

	private List<String> valoriListbox = new ArrayList<String>();
	private String selectedItem;
	
	public Campo() {

	}

	public enum TipoWidget {
		TEXTBOX, TEXTAREA, DATE, SUGGESTBOX, LISTBOX, INTEGERBOX, DOUBLEBOX, YESNORADIOBUTTON, VALORIBOX;
	}

	public Campo(String nome, String valore, boolean visibile, boolean modificabile, boolean obbligatorio, String tipoRecord, String descrizioneGruppo, int lunghezza, String tipoWidget, String descrizione, int posizione,int maxOccorrenze) {
		super();
		this.nome = nome;
		this.valore = valore;
		this.visibile = visibile;
		this.modificabile = modificabile;
		this.obbligatorio = obbligatorio;
		this.tipoRecord = tipoRecord;
		this.lunghezza = lunghezza;
		this.tipoWidget = TipoWidget.valueOf(tipoWidget);
		this.descrizione = descrizione;
		this.posizione = posizione;
		this.descrizioneGruppo = descrizioneGruppo;
		this.maxOccurs = maxOccorrenze;
	}

	public String getNome() {
		return nome;
	}

	public String getValore() {
		return valore;
	}

	public String getTipoRecord() {
		return tipoRecord;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public boolean isVisibile() {
		return visibile;
	}

	public boolean isModificabile() {
		return modificabile;
	}

	public boolean isObbligatorio() {
		return obbligatorio;
	}

	public int getMaxOccurs() {
		return maxOccurs;
	}

	public int getLunghezza() {
		return lunghezza;
	}

	public TipoWidget getTipoWidget() {
		return tipoWidget;
	}

	public int getPosizione() {
		return posizione;
	}

	public String getDescrizioneGruppo() {
		return descrizioneGruppo;
	}

	public List<String> getValoriListbox() {
		return valoriListbox;
	}
	
//	public Command<Void, String> getOnChangeCommand() {
//		return onChangeCommand;
//	}
//	
//	public void setOnChangeCommand(Command<Void, String> onChangeCommand) {
//		this.onChangeCommand = onChangeCommand;
//	}
	
	public void setSelectedItem(String selectedItem) {
		this.selectedItem = selectedItem;
	}
	
	public String getSelectedItem() {
		return selectedItem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		Campo other = (Campo) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Campo [nome=" + nome + ", valore=" + valore + "]";
	}

	@Override
	public int compareTo(Campo o) {
		return new Integer(this.posizione).compareTo(new Integer(o.posizione));
	}
}
