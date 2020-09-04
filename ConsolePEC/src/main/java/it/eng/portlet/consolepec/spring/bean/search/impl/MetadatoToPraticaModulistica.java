package it.eng.portlet.consolepec.spring.bean.search.impl;

import java.util.HashMap;
import java.util.Map;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaPraticaModulistica;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.consolepec.spagicclient.search.SearchObjectResult;
import it.eng.consolepec.xmlplugin.factory.MetadatiPratica;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica;
import it.eng.portlet.consolepec.gwt.server.fascicolo.StatiPraticaModulisticaTranslator;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO.StatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ValoreModuloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ValoreModuloDTO.TipoValorModuloeDTO;

/**
 * Classe che gestisce la creazione/popolamento di una {@link PraticaModulisticaDTO} dai {@link METADATO}
 * 
 * @author pluttero
 * 
 */
public class MetadatoToPraticaModulistica extends MetadatoToPraticaChain {

	private interface MetadatoToPraticaModulisticaCommand {
		public void set(PraticaModulisticaDTO praticaModulisticaDTO, String val);
	}

	private Map<String, MetadatoToPraticaModulisticaCommand> setterPraticaModulistica = new HashMap<String, MetadatoToPraticaModulisticaCommand>();

	@Override
	protected PraticaDTO creaPraticaInternal(SearchObjectResult metadati, String alfrescoPath) {

		if (TipologiaPratica.PRATICA_MODULISTICA.getNomeTipologia().equals(getTipologia(metadati)))
			return new PraticaModulisticaDTO(alfrescoPath);

		return null;
	}

	@Override
	protected void popolaMetadatiInternal(SearchObjectResult metadati, PraticaDTO pratica) {
		if (pratica instanceof PraticaModulisticaDTO) {
			PraticaModulisticaDTO pm = (PraticaModulisticaDTO) pratica;
			for (String key : metadati.getKeys()) {
				if (setterPraticaModulistica.get(key) != null)
					setterPraticaModulistica.get(key).set(pm, (String) metadati.getValue(key));
			}
		}

	}

	public MetadatoToPraticaModulistica() {

		setterPraticaModulistica.put(MetadatiPratica.pTipoPratica.getNomeQualified(), new MetadatoToPraticaModulisticaCommand() {
			@Override
			public void set(PraticaModulisticaDTO ogg, String val) {
				if (val == null || val.trim().equals("")) {
					ogg.setTipologiaPratica(null);

				} else {
					AnagraficaPraticaModulistica am = gestioneConfigurazioni.getAnagraficaPraticaModulistica(val);
					ogg.setTipologiaPratica(PraticaUtil.toTipologiaPratica(am));
				}
			}
		});

		setterPraticaModulistica.put(MetadatiPratica.pTitolo.getNomeQualified(), new MetadatoToPraticaModulisticaCommand() {

			@Override
			public void set(PraticaModulisticaDTO praticaModulisticaDTO, String val) {
				praticaModulisticaDTO.setTitolo(val);
			}
		});

		setterPraticaModulistica.put(MetadatiPratica.pValoriModulo.getNomeQualified(), new MetadatoToPraticaModulisticaCommand() {

			@Override
			public void set(PraticaModulisticaDTO praticaModulisticaDTO, String val) {
				if (val == null || val.trim().equals("")) {
					praticaModulisticaDTO.setStato(null);
				} else {

					String[] valori = val.split(",");

					for (String valore : valori) {
						if (valore.contains("DENOMINAZIONE_SOC")) {
							String[] valoreaggiuntivo = valore.split("=");
							praticaModulisticaDTO.getValori().add(
									new ValoreModuloDTO(valoreaggiuntivo[0], "", valoreaggiuntivo[1], "", TipoValorModuloeDTO.Valore, null, false));
						}
					}

				}
			}
		});
		setterPraticaModulistica.put(MetadatiPratica.pStato.getNomeQualified(), new MetadatoToPraticaModulisticaCommand() {

			@Override
			public void set(PraticaModulisticaDTO praticaModulisticaDTO, String val) {
				if (val == null || val.trim().equals("")) {
					praticaModulisticaDTO.setStato(null);
				} else {
					DatiModulistica.Stato stato = DatiModulistica.Stato.valueOf(val);
					StatoDTO dto = StatiPraticaModulisticaTranslator.getStatoDTOFromStato(stato);
					praticaModulisticaDTO.setStato(dto);
					praticaModulisticaDTO.setStatoLabel(dto.getLabel());
				}
			}
		});
	}

}
