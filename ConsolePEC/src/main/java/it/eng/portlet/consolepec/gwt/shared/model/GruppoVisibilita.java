package it.eng.portlet.consolepec.gwt.shared.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GruppoVisibilita implements IsSerializable,Serializable, Comparable<GruppoVisibilita> {

	private static final long serialVersionUID = -4342962719280947180L;

	private String gruppoVisibilita, label;
	private TipoVisibilita tipoVisibilita;

	public enum TipoVisibilita {
		GRUPPO, UTENTE;

	}

	@SuppressWarnings("unused")
	private GruppoVisibilita() {
		// DO NOT REMOVE
	}

	public GruppoVisibilita(String gruppoVisibilita, String label, TipoVisibilita tipoVisibilita) {
		super();
		this.gruppoVisibilita = gruppoVisibilita;
		this.label = label;
		this.tipoVisibilita = tipoVisibilita;
	}

	public String getGruppoVisibilita() {
		return gruppoVisibilita;
	}

	public String getLabel() {
		return label;
	}

	public TipoVisibilita getTipoVisibilita() {
		return this.tipoVisibilita;
	}

	@Override
	public int compareTo(GruppoVisibilita o) {
		int compareTipoVisibilita = tipoVisibilita.compareTo(o.tipoVisibilita);
		if (compareTipoVisibilita == 0)
			return this.gruppoVisibilita.compareTo(o.gruppoVisibilita);
		return compareTipoVisibilita;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gruppoVisibilita == null) ? 0 : gruppoVisibilita.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
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
		GruppoVisibilita other = (GruppoVisibilita) obj;
		if (gruppoVisibilita == null) {
			if (other.gruppoVisibilita != null)
				return false;
		} else if (!gruppoVisibilita.equals(other.gruppoVisibilita))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GruppoVisibilita [gruppoVisibilita=" + gruppoVisibilita + ", label=" + label + "]";
	}

}
