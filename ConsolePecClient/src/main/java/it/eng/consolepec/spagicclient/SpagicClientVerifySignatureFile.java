package it.eng.consolepec.spagicclient;

import java.io.File;

import it.eng.cobo.consolepec.commons.firmadigitale.FirmaDigitale;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public interface SpagicClientVerifySignatureFile {

	public FirmaDigitale verificaFirmaDigitaleFile(File file) throws SpagicClientException;
}
