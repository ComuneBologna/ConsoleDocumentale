package it.eng.cobo.consolepec.integration.sara.client;

import it.eng.cobo.consolepec.integration.sara.client.SaraWSProfilaturaRequest.GetRuoliProfiloResponse;
import it.eng.cobo.consolepec.integration.sara.generated.profilazione.GetRuoliProfiloRequest;

public class SaraWsProfilaturaConverterUtil {

	public static GetRuoliProfiloRequest toJaxb(it.eng.cobo.consolepec.integration.sara.client.SaraWSProfilaturaRequest.GetRuoliProfiloRequest request) {
		GetRuoliProfiloRequest res = new GetRuoliProfiloRequest();
		res.setMatricola(request.getMatricola());
		res.setPassword(request.getPassword());
		res.setUsername(request.getUsername());
		return res;
	}

	public static GetRuoliProfiloResponse fromJaxb(it.eng.cobo.consolepec.integration.sara.generated.profilazione.GetRuoliProfiloResponse ruoliProfilo) {
		GetRuoliProfiloResponse res = new GetRuoliProfiloResponse();
		res.setUtenteTrovato(ruoliProfilo.isUtenteTrovato());
		res.getRuoli().addAll(ruoliProfilo.getListsRuolo());
		return res;
	}

}
