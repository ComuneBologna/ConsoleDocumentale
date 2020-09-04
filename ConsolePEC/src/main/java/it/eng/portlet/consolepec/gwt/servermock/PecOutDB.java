package it.eng.portlet.consolepec.gwt.servermock;

import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PecOutDB {
	private static PecOutDB _inst = new PecOutDB();
	private final Map<String, PecOutDTO> dettagli = new HashMap<String, PecOutDTO>();
	private int counter = 0;

	private PecOutDB() {

	}

	public static PecOutDB getInstance() {
		return _inst;
	}

	public PecOutDTO getDettaglio(String path) {
		return dettagli.get(path);
	}

	public PecOutDTO creaDettaglio(FascicoloDTO fascicolo) {
		String path = "/dummy/path/pecout" + (counter++) + "/metadati.xml";
		path = Base64Utils.URLencodeAlfrescoPath(path);
		PecOutDTO dto = new PecOutDTO(path);
		dto.setDataOraCreazione("01/01/2014");
		dto.setStato(it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO.StatoDTO.BOZZA);
		dto.getIdPraticheCollegate().add(fascicolo.getClientID());
		dettagli.put(dto.getClientID(), dto);
		return dto;
	}

	public List<PecOutDTO> getDettaglios() {
		ArrayList<PecOutDTO> result = new ArrayList<PecOutDTO>();
		for (PecOutDTO pec : dettagli.values())
			result.add(pec);
		return result;
	}

}
