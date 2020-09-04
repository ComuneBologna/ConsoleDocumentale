package it.eng.portlet.consolepec.gwt.servermock.fascicolo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.portlet.consolepec.gwt.servermock.PecInDB;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.action.pec.TipoRiferimentoPEC;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.Stato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoAllegato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollatoCapofila;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPECRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.StatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

public class FascicoloDB {

	// @Autowired
	// Util praticaUtil;

	private static FascicoloDB _inst = new FascicoloDB();
	private Map<String, FascicoloDTO> dettagli = new HashMap<String, FascicoloDTO>();

	public static FascicoloDB getInstance() {
		return _inst;
	}

	private FascicoloDB() {
		FascicoloDTO dto = null;
		for (int i = 0; i < 10; i++) {
			String path = "/dummy/path/fascicolo" + i + "/metadati.xml";
			path = Base64Utils.URLencodeAlfrescoPath(path);
			dto = new FascicoloDTO(path);
			dto.setAssegnatario("Assegnatario " + i);
			dto.setDataOraCreazione("01/02/200" + i % 10);
			dto.setNote("Note lalalalala " + i);
			dto.setLetto(Math.random() > 0.5 ? true : false);
			dto.setTitolo("Pratica " + i);
			dto.setProvenienza("Provenienza " + i);
			it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato stato = DatiFascicolo.Stato.values()[(int) (Math.random() * DatiFascicolo.Stato.values().length)];
			dto.setStato(StatoDTO.valueOf(stato.name()));
			// dto.setStatoLabel(stato.getLabel());
			// dto.setTipoDocumento(TipoPratica.FASCICOLO);
			/* autorizzazioni */
			dto.setRispondi(true);
			dto.setCaricaAllegato(true);
			/* gruppo no prot */
			FascicoloDTO.ElementoGrupppoNonProtocollato noProt = new FascicoloDTO.ElementoGrupppoNonProtocollato();
			dto.getElenco().add(noProt);
			ElementoAllegato allegato = new ElementoAllegato("test.pdf", null, null, path, "0.1");
			allegato.setStato(Stato.FIRMATO);
			noProt.addAllegato(allegato);
			dto.getAllegati().add(allegato);
			ElementoPECRiferimento rif = new ElementoPECRiferimento(PecInDB.getInstance().getDettaglios().get(0).getClientID(), TipoRiferimentoPEC.IN, new Date());
			rif.setTipo(TipoRiferimentoPEC.IN);
			noProt.addRiferimentoPEC(rif);
			/* gruppo prot capofila */
			ElementoGruppoProtocollatoCapofila prot = new ElementoGruppoProtocollatoCapofila();
			prot.setAnnoPG("2013");
			int pg = (int) ((Math.random() * 100) + 1);
			prot.setNumeroPG("" + pg);
			dto.getElenco().add(prot);
			allegato = new ElementoAllegato("test2.pdf", null, null, path, "0.1");
			allegato.setStato(Stato.NONFIRMATO);
			prot.addAllegato(allegato);
			dto.getAllegati().add(allegato);
			rif = new ElementoPECRiferimento(PecInDB.getInstance().getDettaglios().get(1).getClientID(), TipoRiferimentoPEC.IN, new Date());
			prot.addRiferimentoPEC(rif);
			/* imposto una prot figlia */
			ElementoGruppoProtocollato subProt = new ElementoGruppoProtocollato();
			prot.addElementoGruppoProtocollato(subProt);
			allegato = new ElementoAllegato("test3.pdf", null, null, path, "0.1");
			allegato.setStato(Stato.FIRMANONVALIDA);
			subProt.setAnnoPG("2013");
			subProt.setNumeroPG("" + (pg + 1));
			subProt.addAllegato(allegato);
			dto.getAllegati().add(allegato);
			rif = new ElementoPECRiferimento(PecInDB.getInstance().getDettaglios().get(2).getClientID(), TipoRiferimentoPEC.IN, new Date());
			subProt.addRiferimentoPEC(rif);

			dettagli.put(dto.getClientID(), dto);
		}
	}

	public FascicoloDTO getDettaglio(String path) {
		if (path == null)
			throw new IllegalArgumentException("path: " + path + " non trovato");
		return dettagli.get(path);
	}

	public void setDettaglio(FascicoloDTO dto) {
		dettagli.put(dto.getClientID(), dto);
	}

	public List<PraticaDTO> asList() {
		return new ArrayList<PraticaDTO>(dettagli.values());
	}

}
