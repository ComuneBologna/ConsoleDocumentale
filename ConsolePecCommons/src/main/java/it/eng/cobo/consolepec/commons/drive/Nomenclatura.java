package it.eng.cobo.consolepec.commons.drive;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author Giacomo F.M.
 * @since 2019-07-08
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "nome")
@Setter(AccessLevel.NONE)
public class Nomenclatura implements Serializable {

	private static final long serialVersionUID = 1L;

	@NonNull
	private String nome;

	private String descrizione;

	@NonNull
	private List<String> nomenclatura = new ArrayList<>();

}
