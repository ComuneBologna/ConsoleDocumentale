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
import it.eng.portlet.consolepec.gwt.shared.model.TemplatePdfDTO;

/**
 * Classe che gestisce la creazione/popolamento di una {@link TemplateDTO} dai {@link METADATO}
 *
 * @author aspiezia
 *
 */
public class MetadatoToTemplatePdf extends MetadatoToPraticaChain {

	private interface MetadatoToTemplatePdfCommand {
		public void set(TemplatePdfDTO template, String val);
	}

	private Map<String, MetadatoToTemplatePdfCommand> setterTemplate = new HashMap<String, MetadatoToTemplatePdfCommand>();

	@Override
	protected PraticaDTO creaPraticaInternal(SearchObjectResult metadati, String alfrescoPath) {

		if (TipologiaPratica.MODELLO_PDF.getNomeTipologia().equals(getTipologia(metadati)))
			return new TemplatePdfDTO(alfrescoPath);

		return null;

	}

	@Override
	protected void popolaMetadatiInternal(SearchObjectResult metadati, PraticaDTO pratica) {
		if (pratica instanceof TemplatePdfDTO) {
			TemplatePdfDTO template = (TemplatePdfDTO) pratica;
			for (String key : metadati.getKeys()) {
				if (setterTemplate.get(key) != null)
					setterTemplate.get(key).set(template, (String) metadati.getValue(key));
			}
		}

	}

	public MetadatoToTemplatePdf() {
		super();

		setterTemplate.put(MetadatiPratica.pTipoPratica.getNomeQualified(), new MetadatoToTemplatePdfCommand() {
			@Override
			public void set(TemplatePdfDTO ogg, String val) {
				if (val == null || val.trim().equals("")) {
					ogg.setTipologiaPratica(null);

				} else {
					AnagraficaModello am = gestioneConfigurazioni.getAnagraficaModello(val);
					ogg.setTipologiaPratica(PraticaUtil.toTipologiaPratica(am));
				}
			}
		});

		setterTemplate.put(MetadatiPratica.pNomeTemplate.getNomeQualified(), new MetadatoToTemplatePdfCommand() {

			@Override
			public void set(TemplatePdfDTO template, String val) {
				template.setNome(val);
				template.setTitolo(val); // serve per la ricerca libera
			}
		});

		setterTemplate.put(MetadatiPratica.pDescrizioneTemplate.getNomeQualified(), new MetadatoToTemplatePdfCommand() {

			@Override
			public void set(TemplatePdfDTO template, String val) {
				template.setDescrizione(val);
			}
		});

		setterTemplate.put(MetadatiPratica.pStatoTemplate.getNomeQualified(), new MetadatoToTemplatePdfCommand() {

			@Override
			public void set(TemplatePdfDTO template, String val) {
				template.setStatoLabel(val);
			}
		});
	}

}
