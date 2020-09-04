package it.eng.portlet.consolepec.spring.bean.search.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaEmailOut;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.consolepec.spagicclient.search.SearchObjectResult;
import it.eng.consolepec.xmlplugin.factory.MetadatiPratica;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail;
import it.eng.portlet.consolepec.gwt.server.pec.StatiPecTranslator;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO.StatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

public class MetadatoToPecOut extends MetadatoToPraticaChain {
	
	private interface MetadatoToPecOutCommand {
		public void set(PecOutDTO pec, String val);
	}
	
	Map<String, MetadatoToPecOutCommand> setterPecOUT = new HashMap<String, MetadatoToPecOut.MetadatoToPecOutCommand>();
	
	@Override
	protected PraticaDTO creaPraticaInternal(SearchObjectResult metadati, String alfrescoPath) {
		if (TipologiaPratica.EMAIL_OUT.getNomeTipologia().equals(getTipologia(metadati)))
			return new PecOutDTO(alfrescoPath);

		return null;
	}

	@Override
	protected void popolaMetadatiInternal(SearchObjectResult metadati, PraticaDTO pratica) {		
		if (pratica instanceof PecOutDTO) {
			PecOutDTO pec = (PecOutDTO) pratica;
			for (String key : metadati.getKeys()) {
				if (setterPecOUT.get(key) != null)
					setterPecOUT.get(key).set(pec,(String) metadati.getValue(key));
			}
		}
	}
	
	public MetadatoToPecOut(){
		
		setterPecOUT.put(MetadatiPratica.pTipoPratica.getNomeQualified(), new MetadatoToPecOutCommand() {
			@Override
			public void set(PecOutDTO ogg, String val) {
				if (val == null || val.trim().equals("")) {
					ogg.setTipologiaPratica(null);

				} else {
					
					if (!ogg.getStato().equals(StatoDTO.BOZZA)) {
						AnagraficaEmailOut am = gestioneConfigurazioni.getAnagraficaMailInUscita(val, ogg.getMittente());
						ogg.setTipologiaPratica(PraticaUtil.toTipologiaPratica(am));
						
					} else {
						TipologiaPratica tp = TipologiaPratica.EMAIL_OUT;
						tp.setEtichettaTipologia(TipologiaPratica.EMAIL_OUT_ETICHETTA_DEFAULT);
						tp.setDettaglioNameToken(TipologiaPratica.EMAIL_OUT_DETTAGLIO_NAME_TOKEN_DEFAULT);
						tp.setStato(it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica.Stato.ATTIVA);
						ogg.setTipologiaPratica(tp);
					}
					
					
				}
			}
		});
		
		setterPecOUT.put(MetadatiPratica.pDataInvio.getNomeQualified(), new MetadatoToPecOutCommand() {

			@Override
			public void set(PecOutDTO docInfo, String val) {
				Date date = null;
				if (val == null || val.trim().equals("")) {
					docInfo.setDataInvio(null);
				} else {
					try {
						date = DateUtils.DATEFORMAT_US_DATE.parse(val.substring(0, 10));
						docInfo.setDataInvio(DateUtils.DATEFORMAT_DATE.format(date));
					} catch (ParseException e) {
						return;
					}
				}
			}
		});
		
		setterPecOUT.put(MetadatiPratica.pStato.getNomeQualified(), new MetadatoToPecOutCommand() {
			@Override
			public void set(PecOutDTO docInfo, String val) {
				if (val == null || val.trim().equals("")) {
					docInfo.setStato(null);
				} else {
					DatiEmail.Stato stato = DatiEmail.Stato.valueOf(val);
					StatoDTO dto = StatiPecTranslator.getStatoPecOutDTOFromStato(stato);
					docInfo.setStato(dto);
					docInfo.setStatoLabel(dto.getLabel());
				}
			}
		});
	}

}
