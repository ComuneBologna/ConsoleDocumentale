package it.eng.portlet.consolepec.gwt.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DatiPg implements IsSerializable, Comparable<DatiPg> {

	private String numeroPg;
	private String annoPg;
	private String idTitolo;
	private String idRubrica;
	private String idSezione;
	private boolean capofilaFromBa01 = false;

	public DatiPg(String numeroPg, String annoPg, String idTitolo, String idRubrica, String idSezione) {
		super();
		this.numeroPg = numeroPg;
		this.annoPg = annoPg;
		this.idTitolo = idTitolo;
		this.idRubrica = idRubrica;
		this.idSezione = idSezione;
	}

	public DatiPg(String numeroPg, String annoPg) {
		super();
		this.numeroPg = numeroPg;
		this.annoPg = annoPg;
	}

	public DatiPg() {}

	public String getNumeroPg() {
		return numeroPg;
	}

	public void setNumeroPg(String numeroPg) {
		this.numeroPg = numeroPg;
	}

	public String getAnnoPg() {
		return annoPg;
	}

	public void setAnnoPg(String annoPg) {
		this.annoPg = annoPg;
	}

	public String getIdTitolo() {
		return idTitolo;
	}

	public void setIdTitolo(String idTitolo) {
		this.idTitolo = idTitolo;
	}

	public String getIdRubrica() {
		return idRubrica;
	}

	public void setIdRubrica(String idRubrica) {
		this.idRubrica = idRubrica;
	}

	public String getIdSezione() {
		return idSezione;
	}

	public void setIdSezione(String idSezione) {
		this.idSezione = idSezione;
	}

	public boolean isCapofilaFromBa01() {
		return capofilaFromBa01;
	}

	public void setCapofilaFromBa01(boolean capofilaFromBa01) {
		this.capofilaFromBa01 = capofilaFromBa01;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((annoPg == null) ? 0 : annoPg.hashCode());
		result = prime * result + ((numeroPg == null) ? 0 : numeroPg.hashCode());
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
		DatiPg other = (DatiPg) obj;
		if (annoPg == null) {
			if (other.annoPg != null)
				return false;
		} else if (!annoPg.equals(other.annoPg))
			return false;
		if (numeroPg == null) {
			if (other.numeroPg != null)
				return false;
		} else if (!numeroPg.equals(other.numeroPg))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return numeroPg + "/" + annoPg;
	}

	@Override
	public int compareTo(DatiPg datiPg) {
		int compareAnno = annoPg.compareTo(datiPg.annoPg);
		if (compareAnno == 0)
			return numeroPg.compareTo(datiPg.numeroPg);
		return compareAnno;
	}

}
