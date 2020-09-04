package it.eng.consolepec.xmlplugin.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class IdentificativoProtocollazione {
	private String numPG;
	private Integer annoPG;
	
	@Override
	public String toString() {
		return "(" +numPG+ ","+annoPG+ ")";
	}
}
