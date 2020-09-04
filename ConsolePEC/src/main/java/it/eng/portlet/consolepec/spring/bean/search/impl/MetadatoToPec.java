package it.eng.portlet.consolepec.spring.bean.search.impl;

import java.util.HashMap;
import java.util.Map;

import it.eng.consolepec.spagicclient.search.SearchObjectResult;
import it.eng.consolepec.xmlplugin.factory.MetadatiPratica;
import it.eng.portlet.consolepec.gwt.shared.model.PecDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

/**
 * Classe che gestisce la creazione/popolamento di una {@link PecDTO} dai {@link METADATO}
 * 
 * @author pluttero
 *
 */
public class MetadatoToPec extends MetadatoToPraticaChain {
	private interface MetadatoToPecCommand {
		public void set(PecDTO pec, String val);
	}

	private final Map<String, MetadatoToPecCommand> setterPec = new HashMap<String, MetadatoToPec.MetadatoToPecCommand>();

	@Override
	protected PraticaDTO creaPraticaInternal(SearchObjectResult result, String alfrescoPath) {
		return null;
	}

	// @Override
	// protected void popolaMetadatiInternal(List<METADATO> metadati, PraticaDTO pratica) {
	// if (pratica instanceof PecDTO) {
	// PecDTO pec = (PecDTO) pratica;
	// for (METADATO m : metadati) {
	// if (setterPec.get(m.getNAME()) != null)
	// setterPec.get(m.getNAME()).set(pec, m.getVALUE());
	// }
	// }
	// }

	@Override
	protected void popolaMetadatiInternal(SearchObjectResult result, PraticaDTO pratica) {
		if (pratica instanceof PecDTO) {
			PecDTO pec = (PecDTO) pratica;
			for (String key : result.getKeys()) {
				if (setterPec.get(key) != null)
					setterPec.get(key).set(pec, (String) result.getValue(key));
			}
		}
	}

	public MetadatoToPec() {
		setterPec.put(MetadatiPratica.pProvenienza.getNomeQualified(), new MetadatoToPecCommand() {
			@Override
			public void set(PecDTO docInfo, String val) {
				if (val == null || val.trim().equals("")) {
					docInfo.setMittente(val);
				} else {
					docInfo.setMittente(val);
				}

			}

		});
		setterPec.put(MetadatiPratica.pTitolo.getNomeQualified(), new MetadatoToPecCommand() {
			@Override
			public void set(PecDTO docInfo, String val) {
				if (val == null || val.trim().equals("")) {
					docInfo.setTitolo(val);
				} else {
					docInfo.setTitolo(val);
				}

			}

		});
	}

}
