package it.eng.portlet.consolepec.gwt.servermock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;

public class PecInDB {
	private static PecInDB _inst = new PecInDB();
	private Map<String, PecInDTO> dettagli = new HashMap<String, PecInDTO>();

	private PecInDB() {
		for (int i = 0; i < 10; i++) {
			String path = Base64Utils.URLencodeAlfrescoPath("/dummy/path/pecin" + i + "/metadati.xml");
			PecInDTO dett = new PecInDTO(path);
			dett.setMittente("test" + i + "@test.com");
			dett.setDataOraArrivo("01/02/2012");
			dett.setArchiviabile(true);
			dett.setAssegnatario("pinco pallino");
			dett.setBody("body 123");
			dett.setEliminabile(true);
			dett.setMailId("pec12343" + i);
			dett.setLetto(false);
			dett.setNumeroPG("231" + Math.round((Math.random() * 10)));
			dett.setAnnoPG("2013");
			dett.setTitolo("Titolo email " + i);
			// dett.setTipoDocumento(TipoPratica.EMAIL_IN);
			dett.setTipoEmail("PEC");
			// dett.setDestinatari(Arrays.asList(new String[]{"dest1@test.com, dest2@test.com"}));
			// dett.setDestinatariCC(Arrays.asList(new String[]{"destCC1@test.com, destCC2@test.com"}));
			dett.setNumeroRepertorio("fdkjfjk344334kj" + i);
			dett.getAllegati().add(new AllegatoDTO("allegato" + i + ".txt", null, null, dett.getClientID(), "0.1"));
			dettagli.put(dett.getClientID(), dett);
		}
	}

	public static PecInDB getInstance() {
		return _inst;
	}

	public PecInDTO getDettaglio(String id) {
		return dettagli.get(id);
	}

	public List<PecInDTO> getDettaglios() {
		ArrayList<PecInDTO> result = new ArrayList<PecInDTO>();
		for (PecInDTO pec : dettagli.values())
			result.add(pec);
		return result;
	}

}
