package it.eng.cobo.consolepec.commons.profilazione;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "nome")
public class Settore implements Serializable, Comparable<Settore> {

	private static final long serialVersionUID = 1617588443817020200L;

	@NonNull
	private String nome;
	private List<String> settoriSubordinati = new ArrayList<>();
	private List<String> ruoli = new ArrayList<>();
	private boolean riservato = false;

	@Override
	public int compareTo(Settore o) {
		return nome.compareToIgnoreCase(o.getNome());
	}
}
