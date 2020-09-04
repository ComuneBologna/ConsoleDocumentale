package it.eng.consolepec.spagicclient.model.taskfirma;

import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class RichiestaTaskFirmaResult {
	
	private LockedPratica lockedPratica;
	private Map<String, Integer> allegatiIdTaskFirmaMap = new HashMap<String, Integer>();
	
}
