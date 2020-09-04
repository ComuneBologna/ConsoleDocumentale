package it.eng.portlet.consolepec.gwt.shared.model.richiedifirma;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import com.google.gwt.user.client.rpc.IsSerializable;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class AllegatoRichiediFirmaDTO implements IsSerializable, Comparable<AllegatoRichiediFirmaDTO> {
	private String nome, versioneCorrente;

	protected AllegatoRichiediFirmaDTO() {

	}

	@Override
	public int compareTo(AllegatoRichiediFirmaDTO o) {
		if (nome.compareTo(o.getNome()) == 0)
			return versioneCorrente.compareTo(o.getVersioneCorrente());
		return nome.compareTo(o.getNome());
	}
}
