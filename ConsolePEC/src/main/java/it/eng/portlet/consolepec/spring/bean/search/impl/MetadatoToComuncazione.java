package it.eng.portlet.consolepec.spring.bean.search.impl;

import java.util.HashMap;
import java.util.Map;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaComunicazione;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.consolepec.spagicclient.search.SearchObjectResult;
import it.eng.consolepec.xmlplugin.factory.MetadatiPratica;
import it.eng.consolepec.xmlplugin.pratica.comunicazione.DatiComunicazione.Stato;
import it.eng.portlet.consolepec.gwt.server.fascicolo.StatiComunicazioneTranslator;
import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO.StatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

/**
 * Classe che gestisce la creazione/popolamento di una {@link ComunicazioneDTO} dai {@link METADATO}
 * 
 * @author aspiezia
 * 
 */
public class MetadatoToComuncazione extends MetadatoToPraticaChain {

	private interface MetadatoToComunicazioneCommand {
		public void set(ComunicazioneDTO comunicazione, String val);
	}

	private Map<String, MetadatoToComunicazioneCommand> setterComunicazione = new HashMap<String, MetadatoToComunicazioneCommand>();

	@Override
	protected PraticaDTO creaPraticaInternal(SearchObjectResult metadati, String alfrescoPath) {

		if (TipologiaPratica.COMUNICAZIONE.getNomeTipologia().equals(getTipologia(metadati)))
			return new ComunicazioneDTO(alfrescoPath);

		return null;
	}

	@Override
	protected void popolaMetadatiInternal(SearchObjectResult metadati, PraticaDTO pratica) {
		if (pratica instanceof ComunicazioneDTO) {
			ComunicazioneDTO comunicazione = (ComunicazioneDTO) pratica;
			for (String key : metadati.getKeys()) {
				if (setterComunicazione.get(key) != null)
					setterComunicazione.get(key).set(comunicazione, (String) metadati.getValue(key));
			}
		}

	}

	public MetadatoToComuncazione() {
		super();

		setterComunicazione.put(MetadatiPratica.pTipoPratica.getNomeQualified(), new MetadatoToComunicazioneCommand() {
			@Override
			public void set(ComunicazioneDTO ogg, String val) {
				if (val == null || val.trim().equals("")) {
					ogg.setTipologiaPratica(null);

				} else {
					AnagraficaComunicazione am = gestioneConfigurazioni.getAnagraficaComunicazione(val);
					ogg.setTipologiaPratica(PraticaUtil.toTipologiaPratica(am));
				}
			}
		});

		setterComunicazione.put(MetadatiPratica.pCodiceComunicazione.getNomeQualified(), new MetadatoToComunicazioneCommand() {

			@Override
			public void set(ComunicazioneDTO comunicazione, String val) {
				comunicazione.setCodice(val);
				comunicazione.setTitolo(val); // serve per la ricerca libera
			}
		});

		setterComunicazione.put(MetadatiPratica.pDescrizioneComunicazione.getNomeQualified(), new MetadatoToComunicazioneCommand() {

			@Override
			public void set(ComunicazioneDTO comunicazione, String val) {
				comunicazione.setDescrizione(val);
			}
		});

		setterComunicazione.put(MetadatiPratica.pIdTemplateComunicazione.getNomeQualified(), new MetadatoToComunicazioneCommand() {

			@Override
			public void set(ComunicazioneDTO comunicazione, String val) {
				comunicazione.setIdDocumentaleTemplate(val);
			}
		});
		setterComunicazione.put(MetadatiPratica.pStato.getNomeQualified(), new MetadatoToComunicazioneCommand() {
			@Override
			public void set(ComunicazioneDTO comunicazione, String val) {
				if (val == null || val.trim().equals("")) {
					comunicazione.setStato(null);
				} else {
					Stato stato = Stato.valueOf(val);
					it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO.StatoDTO statoDTO = StatiComunicazioneTranslator.getStatoDTOFromStato(
							stato);
					comunicazione.setStato(statoDTO);
					comunicazione.setStatoLabel(statoDTO.getLabel());
				}
			}
		});
		setterComunicazione.put(MetadatiPratica.pStato.getNomeQualified(), new MetadatoToComunicazioneCommand() {
			@Override
			public void set(ComunicazioneDTO comunicazione, String val) {
				if (val == null || val.trim().equals("")) {
					comunicazione.setStato(null);
				} else {
					Stato stato = Stato.valueOf(val);
					StatoDTO statoDTO = StatiComunicazioneTranslator.getStatoDTOFromStato(stato);
					comunicazione.setStato(statoDTO);
					comunicazione.setStatoLabel(statoDTO.getLabel());
				}
			}
		});

	}

}
