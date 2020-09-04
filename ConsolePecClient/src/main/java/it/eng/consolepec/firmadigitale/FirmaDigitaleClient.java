package it.eng.consolepec.firmadigitale;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import it.bologna.comune.alfresco.verifica.firma.SERVICERESPONSE;
import it.bologna.comune.alfresco.verifica.firma.SIGNER;
import it.eng.cobo.consolepec.commons.firmadigitale.CRL;
import it.eng.cobo.consolepec.commons.firmadigitale.FirmaDigitale;
import it.eng.cobo.consolepec.commons.firmadigitale.Firmatario;
import it.eng.cobo.consolepec.util.date.DateUtils;

/**
 * @author GiacomoFM
 * @since 14/feb/2018
 */
public class FirmaDigitaleClient {

	public static FirmaDigitale cast(final SERVICERESPONSE serviceresponse) {
		FirmaDigitale fd = new FirmaDigitale();
		fd.setCodice(serviceresponse.getCODE());
		fd.setDescrizione(serviceresponse.getDESCR());
		fd.getFirmatari().addAll(creaListaFirmatari(serviceresponse.getSIGNER()));
		return fd;
	}

	private static Collection<Firmatario> creaListaFirmatari(final List<SIGNER> signers) {
		List<Firmatario> firmatari = new ArrayList<>(signers.size());
		for (SIGNER signer : signers) {
			firmatari.add(Firmatario.builder()
					.DN(signer.getDN())
					.CA(signer.getCA())
					.validoDal(dateCast(signer.getVALIDODAL()))
					.validoAl(dateCast(signer.getVALIDOAL()))
					.dataFirma(dateCast(signer.getDATAFIRMA()))
					.tipoFirma(Firmatario.TipoFirma.getEnum(signer.getTIPOFIRMA()))
					.CRL(creaCRL(signer))
					.stato(Firmatario.Stato.getEnum(signer.getSTATUS()))
					.descrizione(signer.getDESCR())
					.build());
		}
		return firmatari;
	}

	private static CRL creaCRL(final SIGNER signer) {
		it.bologna.comune.alfresco.verifica.firma.CRL crl = signer.getCRL();
		if (crl == null) return null;
		CRL.Stato stato = CRL.Stato.getEnum(crl.getSTATUS());
		boolean revocato = Boolean.getBoolean(crl.getREVOCATO());
		return new CRL(crl.getValue(), stato, revocato);
	}

	private static Date dateCast(String date) {
		if (date == null) return null;
		try {
			return DateUtils.DATEFORMAT_GLOBAL.parse(date);
		} catch (ParseException pe) {
			return null;
		}
	}
	
	public static DateFormat getDateFormat() {
		return DateUtils.DATEFORMAT_GLOBAL;
	}

}
