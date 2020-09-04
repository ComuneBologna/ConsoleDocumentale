package it.eng.portlet.consolepec.spring.bean.search.impl;

import java.util.HashMap;
import java.util.Map;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaModello;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.consolepec.spagicclient.search.SearchObjectResult;
import it.eng.consolepec.xmlplugin.factory.MetadatiPratica;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TemplateDTO;

/**
 * Classe che gestisce la creazione/popolamento di una {@link TemplateDTO} dai {@link METADATO}
 *
 * @author aspiezia
 *
 */
public class MetadatoToTemplate extends MetadatoToPraticaChain {

	private interface MetadatoToTemplateCommand {
		public void set(TemplateDTO template, String val);
	}

	private Map<String, MetadatoToTemplateCommand> setterTemplate = new HashMap<String, MetadatoToTemplateCommand>();

	@Override
	protected PraticaDTO creaPraticaInternal(SearchObjectResult metadati, String alfrescoPath) {
		if (TipologiaPratica.MODELLO_MAIL.getNomeTipologia().equals(getTipologia(metadati)))
			return new TemplateDTO(alfrescoPath);

		return null;
	}

	@Override
	protected void popolaMetadatiInternal(SearchObjectResult metadati, PraticaDTO pratica) {
		if (pratica instanceof TemplateDTO) {
			TemplateDTO template = (TemplateDTO) pratica;
			for (String key : metadati.getKeys()) {
				if (setterTemplate.get(key) != null)
					setterTemplate.get(key).set(template, (String) metadati.getValue(key));
			}
		}

	}

	public MetadatoToTemplate() {
		super();

		setterTemplate.put(MetadatiPratica.pTipoPratica.getNomeQualified(), new MetadatoToTemplateCommand() {
			@Override
			public void set(TemplateDTO ogg, String val) {
				if (val == null || val.trim().equals("")) {
					ogg.setTipologiaPratica(null);

				} else {
					AnagraficaModello am = gestioneConfigurazioni.getAnagraficaModello(val);
					ogg.setTipologiaPratica(PraticaUtil.toTipologiaPratica(am));
				}
			}
		});

		setterTemplate.put(MetadatiPratica.pNomeTemplate.getNomeQualified(), new MetadatoToTemplateCommand() {

			@Override
			public void set(TemplateDTO template, String val) {
				template.setNome(val);
				template.setTitolo(val); // serve per la ricerca libera
			}
		});

		setterTemplate.put(MetadatiPratica.pDescrizioneTemplate.getNomeQualified(), new MetadatoToTemplateCommand() {

			@Override
			public void set(TemplateDTO template, String val) {
				template.setDescrizione(val);
			}
		});

		setterTemplate.put(MetadatiPratica.pStatoTemplate.getNomeQualified(), new MetadatoToTemplateCommand() {

			@Override
			public void set(TemplateDTO template, String val) {
				template.setStatoLabel(val);
			}
		});
	}

}
