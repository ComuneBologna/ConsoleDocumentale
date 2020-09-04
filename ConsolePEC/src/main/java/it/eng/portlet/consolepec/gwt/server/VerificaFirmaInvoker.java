package it.eng.portlet.consolepec.gwt.server;

import java.io.File;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import it.eng.cobo.consolepec.commons.firmadigitale.FirmaDigitale;
import it.eng.cobo.consolepec.commons.firmadigitale.Firmatario;
import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.cobo.consolepec.util.firmadigitale.FirmaDigitaleUtil;
import it.eng.consolepec.spagicclient.SpagicClientVerifySignatureFile;
import it.eng.consolepec.spagicclient.SpagicClientVerifySingnatureDocument;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.Stato;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.TipologiaFirma;
import it.eng.portlet.consolepec.gwt.shared.model.DettagliAllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FileDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FileDTO.InformazioniFirmaDigitaleDTO;

public class VerificaFirmaInvoker {

	@Autowired
	private SpagicClientVerifySingnatureDocument spagicClientVerifySingnatureDocument;

	@Autowired
	private SpagicClientVerifySignatureFile spagicClientVerifySingnatureFile;

	public FileDTO verificaFirmaFile(File file) {
		FirmaDigitale firmaDigitale = spagicClientVerifySingnatureFile.verificaFirmaDigitaleFile(file);
		boolean isFirmato = false;
		boolean isFirmatoHash = false;
		Stato stato = null;
		TipologiaFirma tipoFirma = null;
		if (FirmaDigitaleUtil.isFirmaDigitaleValida(firmaDigitale)) {
			tipoFirma = convertiTipologia(FirmaDigitaleUtil.getTipoFirma(firmaDigitale));
			stato = convertiStato(FirmaDigitaleUtil.getStato(firmaDigitale));

			if (FirmaDigitaleUtil.isFirmato(firmaDigitale)) {
				isFirmato = FirmaDigitaleUtil.getStatoFirmato(firmaDigitale);

			} else {
				isFirmato = false;
			}

			isFirmatoHash = isFirmato;
		}

		InformazioniFirmaDigitaleDTO informazioniFirma = new InformazioniFirmaDigitaleDTO(isFirmato, isFirmatoHash, tipoFirma, stato);
		return new FileDTO(file.getName(), file.getAbsolutePath(), informazioniFirma);
	}

	private static TipologiaFirma convertiTipologia(Firmatario.TipoFirma tipo) {
		if (tipo == null)
			return TipologiaFirma.NONFIRMATO;
		switch (tipo) {
		case CADES:
			return TipologiaFirma.CADES;
		case PADES:
			return TipologiaFirma.PADES;
		case XADES:
			return TipologiaFirma.XADES;
		default:
			return TipologiaFirma.NONFIRMATO;
		}
	}

	private static Stato convertiStato(Firmatario.Stato stato) {
		if (stato == null)
			return Stato.NONFIRMATO;
		switch (stato) {
		case OK:
			return Stato.FIRMATO;
		case KO:
			return Stato.FIRMANONVALIDA;
		default:
			return Stato.NONFIRMATO;
		}
	}

	public void verificaFirmaAllegato(DettagliAllegatoDTO dto) {
		FirmaDigitale firmaDigitale = spagicClientVerifySingnatureDocument.verificaFirmaDocumentoByUuid(dto.getUUID());
		if (FirmaDigitaleUtil.isFirmaDigitaleValida(firmaDigitale)) {
			dto.setTipologiaFirma(convertiTipologia(FirmaDigitaleUtil.getTipoFirma(firmaDigitale)));
			dto.setStato(convertiStato(FirmaDigitaleUtil.getStato(firmaDigitale)));
			/* non ci interessano le informazioni di signer se non firmato */
			if (!dto.getStato().equals(Stato.NONFIRMATO)) {
				String dataFirmaLabel, validoDalLabel, validoALabel, descrizioneLabel;
				for (Firmatario firmatario : firmaDigitale.getFirmatari()) {
					dataFirmaLabel = parseDataFirma(firmatario.getDataFirma());
					validoDalLabel = parseDataFirma(firmatario.getValidoDal());
					validoALabel = parseDataFirma(firmatario.getValidoAl());
					descrizioneLabel = concatenaDescrizione(firmatario);
					dto.addInformazioneDiFirma(validoDalLabel, validoALabel, firmatario.getCA(), dataFirmaLabel, firmatario.getStato().toString(), descrizioneLabel, firmatario.getDN());
				}
			}
		} else {
			dto.setStato(Stato.NONFIRMATO);
		}
	}

	private static String concatenaDescrizione(Firmatario firmatario) {
		if (firmatario == null)
			return " - ";
		if (firmatario.getDescrizione() != null && firmatario.getCRL() != null) {
			return firmatario.getDescrizione() + " - " + firmatario.getCRL().getValue();
		}
		return firmatario.getDescrizione();
	}

	private static String parseDataFirma(final Date date) {
		if (date == null) {
			return ConsolePecConstants.DATA_FIRMA_NONVALIDA_O_ASSENTE;
		}

		return DateUtils.DATEFORMAT_DATEH.format(date);
	}
}
