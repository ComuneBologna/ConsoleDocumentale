package it.eng.portlet.consolepec.spring.bean.search.impl;

import java.util.HashMap;
import java.util.Map;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo.StepIter;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.consolepec.spagicclient.search.SearchObjectResult;
import it.eng.consolepec.xmlplugin.factory.MetadatiPratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.portlet.consolepec.gwt.server.fascicolo.StatiFascicoloTranslator;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.StatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

/**
 * Classe che gestisce la creazione/popolamento di una {@link FascicoloDTO} dai {@link METADATO}
 * 
 * @author pluttero
 * 
 */
public class MetadatoToFascicolo extends MetadatoToPraticaChain {

	private interface MetadatoToFascicoloCommand {
		public void set(FascicoloDTO fascicolo, String val);
	}

	private Map<String, MetadatoToFascicoloCommand> setterFascicolo = new HashMap<String, MetadatoToFascicolo.MetadatoToFascicoloCommand>();

	@Override
	protected PraticaDTO creaPraticaInternal(SearchObjectResult metadati, String alfrescoPath) {
		AnagraficaFascicolo af = gestioneConfigurazioni.getAnagraficaFascicolo(getTipologia(metadati));
		if (af != null)
			return new FascicoloDTO(alfrescoPath);

		return null;
	}

	@Override
	protected void popolaMetadatiInternal(SearchObjectResult result, PraticaDTO pratica) {
		if (pratica instanceof FascicoloDTO) {
			FascicoloDTO fascicolo = (FascicoloDTO) pratica;
			for (String key : result.getKeys()) {
				if (setterFascicolo.get(key) != null)
					setterFascicolo.get(key).set(fascicolo, (String) result.getValue(key));
			}
		}
	}

	public MetadatoToFascicolo() {

		setterFascicolo.put(MetadatiPratica.pTipoPratica.getNomeQualified(), new MetadatoToFascicoloCommand() {
			@Override
			public void set(FascicoloDTO ogg, String val) {
				if (val == null || val.trim().equals("")) {
					ogg.setTipologiaPratica(null);

				} else {
					AnagraficaFascicolo am = gestioneConfigurazioni.getAnagraficaFascicolo(val);
					ogg.setTipologiaPratica(PraticaUtil.toTipologiaPratica(am));
				}
			}
		});

		setterFascicolo.put(MetadatiPratica.pStato.getNomeQualified(), new MetadatoToFascicoloCommand() {
			@Override
			public void set(FascicoloDTO ogg, String val) {
				if (val == null || val.trim().equals("")) {
					ogg.setStato(null);
				} else {
					Stato stato = Stato.valueOf(val);
					StatoDTO statoDTO = StatiFascicoloTranslator.getStatoDTOFromStato(stato);
					ogg.setStato(statoDTO);
					ogg.setStatoLabel(statoDTO.getLabel());
				}
			}
		});

		setterFascicolo.put(MetadatiPratica.pStepIter.getNomeQualified(), new MetadatoToFascicoloCommand() {
			@Override
			public void set(FascicoloDTO ogg, String val) {
				if (val == null || val.trim().equals("")) {
					ogg.setStepIter(null);
				} else {
					StepIter stepIter = new StepIter();
					stepIter.setNome(val);
					ogg.setStepIter(stepIter);

				}
			}
		});

		setterFascicolo.put(MetadatiPratica.pOperatore.getNomeQualified(), new MetadatoToFascicoloCommand() {
			@Override
			public void set(FascicoloDTO ogg, String val) {
				if (val == null || val.trim().equals("")) {
					ogg.setOperatore(null);
				} else {
					ogg.setOperatore(val);
				}
			}
		});
	}

}
