package it.eng.cobo.consolepec.commons.configurazioni;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Data
public class Operazione implements Serializable {
	private static final long serialVersionUID = -4578136625276332880L;

	private String nome;
}
