package it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author biagiot
 *
 */
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class RigaDatoAggiuntivo implements Serializable {

	private static final long serialVersionUID = 4202199720118339697L;
	
	@Getter
	List<ValoreCellaDatoAggiuntivo> celle = new ArrayList<>();
	
	public RigaDatoAggiuntivo(List<ValoreCellaDatoAggiuntivo> celle) {
		this.celle = celle;
	}
}
