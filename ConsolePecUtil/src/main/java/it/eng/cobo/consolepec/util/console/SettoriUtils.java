package it.eng.cobo.consolepec.util.console;

import it.eng.cobo.consolepec.commons.profilazione.Settore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author GiacomoFM
 * @since 12/dic/2017
 */
public class SettoriUtils {

	public static List<String> transform(List<Settore> settori) {
		if (settori == null) {
			return Collections.emptyList();
		}
		Set<String> gruppi = new HashSet<>();
		for (Settore s : settori) {
			gruppi.addAll(s.getRuoli());
		}
		return new ArrayList<>(gruppi);
	}

}
