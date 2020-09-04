package it.eng.portlet.consolepec.spring.bean.search.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.consolepec.spagicclient.search.SearchObjectResult;
import it.eng.consolepec.xmlplugin.factory.MetadatiPratica;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

public class MetadatoToPratica extends MetadatoToPraticaChain {

	private interface MetadatoToPraticaCommand {
		public void set(PraticaDTO pratica, String val);
	}

	private final Map<String, MetadatoToPraticaCommand> setterPraticaDTOMap = new HashMap<String, MetadatoToPratica.MetadatoToPraticaCommand>();

	@Override
	protected PraticaDTO creaPraticaInternal(SearchObjectResult metadati, String alfrescoPath) {
		return null;
	}

	@Override
	protected void popolaMetadatiInternal(SearchObjectResult metadati, PraticaDTO pratica) {
		for (String key : metadati.getKeys()) {
			if (setterPraticaDTOMap.get(key) != null)
				setterPraticaDTOMap.get(key).set(pratica, (String) metadati.getValue(key));
		}

	}

	public MetadatoToPratica() {
		setterPraticaDTOMap.put(MetadatiPratica.pAnnoPG.getNomeQualified(), new MetadatoToPraticaCommand() {

			@Override
			public void set(PraticaDTO ogg, String val) {
				if (val == null || val.trim().equals("")) {
					ogg.setAnnoPG(null);
				} else {
					ogg.setAnnoPG(val);

				}
			}

		});
		setterPraticaDTOMap.put(MetadatiPratica.pNumeroPG.getNomeQualified(), new MetadatoToPraticaCommand() {

			@Override
			public void set(PraticaDTO ogg, String val) {
				if (val == null || val.trim().equals("")) {
					ogg.setNumeroPG(null);
				} else {
					ogg.setNumeroPG(val);

				}
			}

		});
		setterPraticaDTOMap.put(MetadatiPratica.pTitolo.getNomeQualified(), new MetadatoToPraticaCommand() {

			@Override
			public void set(PraticaDTO ogg, String val) {
				if (val == null || val.trim().equals("")) {
					ogg.setTitolo(null);
				} else {
					ogg.setTitolo(val);

				}
			}

		});

		setterPraticaDTOMap.put(MetadatiPratica.pProvenienza.getNomeQualified(), new MetadatoToPraticaCommand() {
			@Override
			public void set(PraticaDTO ogg, String val) {
				if (val == null || val.trim().equals("")) {
					ogg.setProvenienza(null);
				} else {
					if (val.contains("@")) {
						ogg.setProvenienza(val);
					} else {
						ogg.setProvenienza(gestioneConfigurazioni.getAnagraficaRuolo(val) != null ? gestioneConfigurazioni.getAnagraficaRuolo(val).getEtichetta() : "");
					}
				}
			}
		});

		setterPraticaDTOMap.put(MetadatiPratica.pDataCreazione.getNomeQualified(), new MetadatoToPraticaCommand() {

			@Override
			public void set(PraticaDTO ogg, String val) {
				if (val == null || val.trim().equals("")) {
					ogg.setDataOraCreazione(null);

				} else {
					Date data;

					try {
						data = DateUtils.DATEFORMAT_ISO8601.parse(val);
					} catch (ParseException e) {
						try {
							data = DateUtils.DATEFORMAT_ISO8601_2.parse(val);
						} catch (ParseException e1) {
							try {
								data = DateUtils.DATEFORMAT_ISO8601_3.parse(val);
							} catch (ParseException e2) {
								return;
							}
							return;
						}
					}
					ogg.setDataOraCreazione(DateUtils.DATEFORMAT_DATEH.format(data));
				}
			}
		});

		setterPraticaDTOMap.put(MetadatiPratica.pUtenteCreazione.getNomeQualified(), new MetadatoToPraticaCommand() {
			@Override
			public void set(PraticaDTO ogg, String val) {
				if (val == null || val.trim().equals("")) {
					ogg.setUtenteCreazione(null);
				} else {
					ogg.setUtenteCreazione(val);
				}
			}
		});

		setterPraticaDTOMap.put(MetadatiPratica.pAssegnatoA.getNomeQualified(), new MetadatoToPraticaCommand() {
			@Override
			public void set(PraticaDTO ogg, String val) {
				if (val == null || val.trim().equals("")) {
					ogg.setAssegnatario(null);
				} else {
					ogg.setAssegnatario(gestioneConfigurazioni.getAnagraficaRuolo(val) != null ? gestioneConfigurazioni.getAnagraficaRuolo(val).getEtichetta() : null);
				}
			}
		});

		setterPraticaDTOMap.put(MetadatiPratica.pIncaricoA.getNomeQualified(), new MetadatoToPraticaCommand() {
			@Override
			public void set(PraticaDTO ogg, String val) {
				if (val == null || val.trim().equals("")) {
					ogg.setInCaricoAUserName(null);
				} else {
					ogg.setInCaricoAUserName(val);
				}
			}
		});

		setterPraticaDTOMap.put(MetadatiPratica.pLetto.getNomeQualified(), new MetadatoToPraticaCommand() {
			@Override
			public void set(PraticaDTO ogg, String val) {
				if (val == null || val.trim().equals("")) {
					ogg.setLetto(false);
				} else {
					if (val.toLowerCase().equals("no")) {
						ogg.setLetto(false);
					} else {
						ogg.setLetto(true);
					}
				}
			}
		});
	}
}
