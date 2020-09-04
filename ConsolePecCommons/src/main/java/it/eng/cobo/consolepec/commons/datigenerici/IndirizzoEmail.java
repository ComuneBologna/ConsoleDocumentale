package it.eng.cobo.consolepec.commons.datigenerici;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 
 * @author biagiot
 *
 */
@Data
public class IndirizzoEmail implements Comparable<IndirizzoEmail>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String indirizzoEmail;
	private String fonte;
	private boolean abilitatoPerTutti;
	private List<String> ruoli = new ArrayList<String>();

	@Override
	public int compareTo(IndirizzoEmail o) {

		if (this.indirizzoEmail.equalsIgnoreCase(o.indirizzoEmail)) {
			return this.fonte.compareTo(o.getFonte());
		}

		return this.indirizzoEmail.compareTo(o.getIndirizzoEmail());
	}

}
