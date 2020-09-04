package it.eng.cobo.consolepec.commons.services;

import it.eng.cobo.consolepec.commons.urbanistica.PraticaProcedi;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author GiacomoFM
 * @since 06/nov/2017
 */
@NoArgsConstructor
public class PraticaProcediResponse {

	@Getter private long count;
	
	@Getter private List<PraticaProcedi> listaPraticaProcedi = new ArrayList<>();

	public PraticaProcediResponse(long count) {
		this.count = count;
	}
	
	public PraticaProcediResponse(List<PraticaProcedi> listaPraticaProcedi) {
		this.listaPraticaProcedi = listaPraticaProcedi;
	}
}
