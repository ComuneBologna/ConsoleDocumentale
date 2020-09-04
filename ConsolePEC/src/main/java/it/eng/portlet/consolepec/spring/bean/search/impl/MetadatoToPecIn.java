package it.eng.portlet.consolepec.spring.bean.search.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.consolepec.spagicclient.search.SearchObjectResult;
import it.eng.consolepec.xmlplugin.factory.MetadatiPratica;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail;
import it.eng.portlet.consolepec.gwt.server.pec.StatiPecTranslator;
import it.eng.portlet.consolepec.gwt.shared.model.Destinatario;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO.StatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

/**
 * Classe che gestisce la creazione/popolamento di una {@link PecInDTO} dai {@link METADATO}
 * 
 * @author pluttero
 * 
 */
public class MetadatoToPecIn extends MetadatoToPraticaChain {
	private static final Logger logger = LoggerFactory.getLogger(MetadatoToPecIn.class);

	private interface MetadatoToPecInCommand {
		public void set(PecInDTO pec, String val);
	}

	private final Map<String, MetadatoToPecInCommand> setterPecIN = new HashMap<String, MetadatoToPecIn.MetadatoToPecInCommand>();

	@Override
	protected PraticaDTO creaPraticaInternal(SearchObjectResult metadati, String alfrescoPath) {

		if (TipologiaPratica.EMAIL_IN.getNomeTipologia().equals(getTipologia(metadati)))
			return new PecInDTO(alfrescoPath);

		return null;
	}

	@Override
	protected void popolaMetadatiInternal(SearchObjectResult metadati, PraticaDTO pratica) {
		if (pratica instanceof PecInDTO) {
			PecInDTO pec = (PecInDTO) pratica;
			for (String key : metadati.getKeys()) {
				if (setterPecIN.get(key) != null && !MetadatiPratica.pTipoPratica.getNomeQualified().equals(key))
					setterPecIN.get(key).set(pec, (String) metadati.getValue(key));
			}

			setterPecIN.get(MetadatiPratica.pTipoPratica.getNomeQualified()).set(pec,
					(String) metadati.getValue(MetadatiPratica.pTipoPratica.getNomeQualified()));
		}
	}

	public MetadatoToPecIn() {

		setterPecIN.put(MetadatiPratica.pDataRicezione.getNomeQualified(), new MetadatoToPecInCommand() {

			@Override
			public void set(PecInDTO docInfo, String val) {
				Date date = null;
				if (val == null || val.trim().equals("")) {
					docInfo.setDataOraArrivo(null);
				} else {
					try {
						date = DateUtils.DATEFORMAT_ISO8601.parse(val);
						docInfo.setDataOraArrivo(DateUtils.DATEFORMAT_DATEH.format(date));
					} catch (Exception e1) {
						try {
							// vecchia gestione per eventuale retrocompatibilità
							date = DateUtils.DATEFORMAT_US_DATE.parse(val.substring(0, 10));
							docInfo.setDataOraArrivo(DateUtils.DATEFORMAT_DATE.format(date));
						} catch (ParseException e2) {
							return;
						}
					}

				}
			}
		});

		setterPecIN.put(MetadatiPratica.pStato.getNomeQualified(), new MetadatoToPecInCommand() {
			@Override
			public void set(PecInDTO docInfo, String val) {
				if (val == null || val.trim().equals("")) {
					docInfo.setStato(null);
				} else {
					DatiEmail.Stato stato = DatiEmail.Stato.valueOf(val);
					StatoDTO dto = StatiPecTranslator.getStatoPecInDTOFromStato(stato);
					docInfo.setStato(dto);
					docInfo.setStatoLabel(dto.getLabel());
				}
			}
		});

		setterPecIN.put(MetadatiPratica.pDestinatario.getNomeQualified(), new MetadatoToPecInCommand() {
			@Override
			public void set(PecInDTO docInfo, String val) {
				if (!(val == null || val.trim().equals(""))) {
					String[] destinatari = val.split(",");
					logger.info("Destinatari " + val);
					for (String d : destinatari) {
						if (!d.isEmpty()) {
							Destinatario destinatario = new Destinatario();
							destinatario.setDestinatario(d);
							docInfo.getDestinatari().add(destinatario);
						}
					}
				}
			}
		});

		setterPecIN.put(MetadatiPratica.pDestinatariInoltro.getNomeQualified(), new MetadatoToPecInCommand() {
			@Override
			public void set(PecInDTO docInfo, String val) {
				if (!(val == null || val.trim().equals(""))) {
					String[] destinatari = val.split(",");
					for (String d : destinatari) {
						if (!d.isEmpty()) {
							docInfo.getDestinatariInoltro().add(d);
						}
					}
				}
			}
		});

		setterPecIN.put(MetadatiPratica.pTipoPratica.getNomeQualified(), new MetadatoToPecInCommand() {
			@Override
			public void set(PecInDTO ogg, String val) {
				if (val == null || val.trim().equals("")) {
					ogg.setTipologiaPratica(null);

				} else {

					if (TipologiaPratica.EMAIL_IN.getNomeTipologia().equals(val)) {

						if (ogg.getDestinatariInoltro() != null && !ogg.getDestinatariInoltro().isEmpty()) {
							for (String destinatario : ogg.getDestinatariInoltro()) {
								AnagraficaIngresso am = gestioneConfigurazioni.getAnagraficaIngresso(val, destinatario);
								if (am != null) {
									ogg.setTipologiaPratica(PraticaUtil.toTipologiaPratica(am));
									return;
								}
							}
						}

						if (ogg.getDestinatarioPrincipale() != null && ogg.getDestinatarioPrincipale().getDestinatario() != null) {
							Destinatario destinatario = ogg.getDestinatarioPrincipale();
							AnagraficaIngresso am = gestioneConfigurazioni.getAnagraficaIngresso(val, destinatario.getDestinatario());
							if (am != null) {
								ogg.setTipologiaPratica(PraticaUtil.toTipologiaPratica(am));
								return;
							}

						}

						if (ogg.getDestinatari() != null && !ogg.getDestinatari().isEmpty()) {
							for (Destinatario destinatario : ogg.getDestinatari()) {
								AnagraficaIngresso am = gestioneConfigurazioni.getAnagraficaIngresso(val, destinatario.getDestinatario());
								if (am != null) {
									ogg.setTipologiaPratica(PraticaUtil.toTipologiaPratica(am));
									return;
								}
							}
						}

						throw new IllegalArgumentException("Tipologia pratica non trovata per " + ogg.getClientID());

					} else if (TipologiaPratica.EMAIL_EPROTOCOLLO.getNomeTipologia().equals(val)) {

						if (ogg.getDestinatariInoltro() != null && !ogg.getDestinatariInoltro().isEmpty()) {
							String destInoltro = ogg.getDestinatariInoltro().first();
							AnagraficaIngresso am = gestioneConfigurazioni.getAnagraficaIngresso(val, destInoltro);
							ogg.setTipologiaPratica(PraticaUtil.toTipologiaPratica(am));
						} else {
							throw new IllegalArgumentException("Tipologia pratica non trovata per " + ogg.getClientID());
						}

					} else {
						throw new IllegalArgumentException("Tipologia pratica non trovata per " + ogg.getClientID());
					}
				}
			}
		});

		setterPecIN.put(MetadatiPratica.pOperatore.getNomeQualified(), new MetadatoToPecInCommand() {

			@Override
			public void set(PecInDTO pec, String val) {
				if (val == null || val.trim().equals("")) {
					pec.setOperatore(null);
				} else {
					pec.setOperatore(val);
				}
			}

		});
	}
}
