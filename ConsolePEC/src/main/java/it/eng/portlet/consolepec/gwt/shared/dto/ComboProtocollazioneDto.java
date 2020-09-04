package it.eng.portlet.consolepec.gwt.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ComboProtocollazioneDto implements IsSerializable, Comparable<ComboProtocollazioneDto> {

	private String identificativo, riferimentoliv1, riferimentoliv2, descrizione, flagentrata, flaguscita, flaginterna;

	public ComboProtocollazioneDto() {
	}

	public ComboProtocollazioneDto(String identificativo, String riferimentoliv1, String riferimentoliv2, String descrizione) {
		this.identificativo = identificativo;
		this.riferimentoliv1 = riferimentoliv1;
		this.riferimentoliv2 = riferimentoliv2;
		this.descrizione = descrizione;
	}

	public ComboProtocollazioneDto(String identificativo, String riferimentoliv1, String riferimentoliv2, String descrizione, String flagentrata, String flaguscita, String flaginterna) {
		super();
		this.identificativo = identificativo;
		this.riferimentoliv1 = riferimentoliv1;
		this.riferimentoliv2 = riferimentoliv2;
		this.descrizione = descrizione;
		this.flagentrata = flagentrata;
		this.flaguscita = flaguscita;
		this.flaginterna = flaginterna;
	}

	public String getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}

	public String getRiferimentoliv1() {
		return riferimentoliv1;
	}

	public void setRiferimentoliv1(String riferimentoliv1) {
		this.riferimentoliv1 = riferimentoliv1;
	}

	public String getRiferimentoliv2() {
		return riferimentoliv2;
	}

	public void setRiferimentoliv2(String riferimentoliv2) {
		this.riferimentoliv2 = riferimentoliv2;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getFlagentrata() {
		return flagentrata;
	}

	public void setFlagentrata(String flagentrata) {
		this.flagentrata = flagentrata;
	}

	public String getFlaguscita() {
		return flaguscita;
	}

	public void setFlaguscita(String flaguscita) {
		this.flaguscita = flaguscita;
	}

	public String getFlaginterna() {
		return flaginterna;
	}

	public void setFlaginterna(String flaginterna) {
		this.flaginterna = flaginterna;
	}

	@Override
	public int compareTo(ComboProtocollazioneDto obj) {
		Integer identificativo1 = Integer.parseInt(this.getIdentificativo());
		Integer identificativo2 = Integer.parseInt(obj.getIdentificativo());
		if (identificativo1.compareTo(identificativo2) == 0)
			return descrizione.compareTo(obj.descrizione);
		return identificativo1.compareTo(identificativo2);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Identificativo: ").append(this.identificativo);
		sb.append(" IdentificativoLiv1: ").append(this.riferimentoliv1);
		sb.append(" IdentificativoLiv2: ").append(this.riferimentoliv2);
		sb.append(" Descrizione: ").append(this.descrizione);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		result = prime * result + ((flagentrata == null) ? 0 : flagentrata.hashCode());
		result = prime * result + ((flaginterna == null) ? 0 : flaginterna.hashCode());
		result = prime * result + ((flaguscita == null) ? 0 : flaguscita.hashCode());
		result = prime * result + ((identificativo == null) ? 0 : identificativo.hashCode());
		result = prime * result + ((riferimentoliv1 == null) ? 0 : riferimentoliv1.hashCode());
		result = prime * result + ((riferimentoliv2 == null) ? 0 : riferimentoliv2.hashCode());
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
		ComboProtocollazioneDto other = (ComboProtocollazioneDto) obj;
		if (descrizione == null) {
			if (other.descrizione != null)
				return false;
		} else if (!descrizione.equals(other.descrizione))
			return false;
		if (flagentrata == null) {
			if (other.flagentrata != null)
				return false;
		} else if (!flagentrata.equals(other.flagentrata))
			return false;
		if (flaginterna == null) {
			if (other.flaginterna != null)
				return false;
		} else if (!flaginterna.equals(other.flaginterna))
			return false;
		if (flaguscita == null) {
			if (other.flaguscita != null)
				return false;
		} else if (!flaguscita.equals(other.flaguscita))
			return false;
		if (identificativo == null) {
			if (other.identificativo != null)
				return false;
		} else if (!identificativo.equals(other.identificativo))
			return false;
		if (riferimentoliv1 == null) {
			if (other.riferimentoliv1 != null)
				return false;
		} else if (!riferimentoliv1.equals(other.riferimentoliv1))
			return false;
		if (riferimentoliv2 == null) {
			if (other.riferimentoliv2 != null)
				return false;
		} else if (!riferimentoliv2.equals(other.riferimentoliv2))
			return false;
		return true;
	}

}
