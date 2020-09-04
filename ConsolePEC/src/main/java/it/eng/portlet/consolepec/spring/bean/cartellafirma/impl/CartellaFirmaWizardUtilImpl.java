package it.eng.portlet.consolepec.spring.bean.cartellafirma.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeFirmaDigitale;
import it.eng.consolepec.spagicclient.SpagicClientTaskFirma;
import it.eng.consolepec.spagicclient.model.taskfirma.TipoRispostaTaskFirmaClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.InformazioniNotificaTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.InformazioniRiassegnazioneFascicoliTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.TipologiaFirma;
import it.eng.portlet.consolepec.gwt.shared.model.FileDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.TipoRispostaParereDTO;
import it.eng.portlet.consolepec.spring.bean.cartellafirma.CartellaFirmaWizardUtil;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import it.eng.portlet.consolepec.spring.firma.GestioneFirma;
import it.eng.portlet.consolepec.spring.firma.impl.CredenzialiFirma;

/**
 *
 * @author biagiot
 *
 */
public class CartellaFirmaWizardUtilImpl implements CartellaFirmaWizardUtil {

	private static final Logger logger = LoggerFactory.getLogger(CartellaFirmaWizardUtilImpl.class);

	@Autowired UserSessionUtil userSessionUtil;

	@Autowired SpagicClientTaskFirma spagicClientTaskFirma;

	@Autowired GestioneFirma gestioneFirma;

	@Autowired GestioneProfilazioneUtente gestioneProfilazioneUtente;

	@Override
	public List<LockedPratica> firmaDocumenti(Map<String, List<String>> mapPathAllegati, CredenzialiFirma cf, TipologiaFirma tipologiaFirma, boolean riassegna,
			InformazioniNotificaTaskFirmaDTO infoNotifica, String ruolo, List<FileDTO> fileDaAllegare, String motivazione) throws SpagicClientException {

		String username = cf.getUsername();
		String password = cf.getPassword();
		String otp = cf.getOtp();
		logger.debug("username: {} password is null: {} otp is null: {} ", username, password == null, otp == null);

		if (username == null || password == null) {
			PreferenzeFirmaDigitale pfd = gestioneProfilazioneUtente.getPreferenzeFirmaDigitale();

			if (username == null && pfd.getUsername() != null) username = pfd.getUsername();

			if (password == null && pfd.getPassword() != null) password = pfd.getPassword();
		}

		if (riassegna) {
			InformazioniRiassegnazioneFascicoliTaskFirmaDTO riassegnaFascicoliInfo = (InformazioniRiassegnazioneFascicoliTaskFirmaDTO) infoNotifica;
			return spagicClientTaskFirma.firmaDocumenti(mapPathAllegati, username, password, otp, tipologiaFirma.name(), true, riassegnaFascicoliInfo.getAnagraficaRuolo().getRuolo(),
					riassegnaFascicoliInfo.getNote(), riassegnaFascicoliInfo.getIndirizziNotifica(), riassegnaFascicoliInfo.getOperatore(), userSessionUtil.getUtenteSpagic(), ruolo,
					(fileDaAllegare != null) ? Lists.transform(fileDaAllegare, FunctionFile.INSTANCE) : null, motivazione);

		}

		return spagicClientTaskFirma.firmaDocumenti(mapPathAllegati, username, password, otp, tipologiaFirma.name(), false, null, infoNotifica != null ? infoNotifica.getNote() : null,
				infoNotifica != null ? infoNotifica.getIndirizziNotifica() : new ArrayList<String>(), null, userSessionUtil.getUtenteSpagic(), ruolo,
				(fileDaAllegare != null) ? Lists.transform(fileDaAllegare, FunctionFile.INSTANCE) : null, motivazione);

	}

	@Override
	public List<LockedPratica> vistoDocumenti(Map<String, List<String>> mapPathAllegati, boolean riassegna, InformazioniNotificaTaskFirmaDTO infoNotifica, String ruolo, List<FileDTO> fileDaAllegare,
			String motivazione) throws SpagicClientException {

		if (riassegna) {
			InformazioniRiassegnazioneFascicoliTaskFirmaDTO riassegnaFascicoliInfo = (InformazioniRiassegnazioneFascicoliTaskFirmaDTO) infoNotifica;
			return spagicClientTaskFirma.vistoDocumenti(mapPathAllegati, true, riassegnaFascicoliInfo.getAnagraficaRuolo().getRuolo(), riassegnaFascicoliInfo.getNote(),
					riassegnaFascicoliInfo.getIndirizziNotifica(), riassegnaFascicoliInfo.getOperatore(), userSessionUtil.getUtenteSpagic(), ruolo,
					(fileDaAllegare != null) ? Lists.transform(fileDaAllegare, FunctionFile.INSTANCE) : null, motivazione);
		}

		return spagicClientTaskFirma.vistoDocumenti(mapPathAllegati, false, null, infoNotifica != null ? infoNotifica.getNote() : null,
				infoNotifica != null ? infoNotifica.getIndirizziNotifica() : new ArrayList<String>(), null, userSessionUtil.getUtenteSpagic(), ruolo,
				(fileDaAllegare != null) ? Lists.transform(fileDaAllegare, FunctionFile.INSTANCE) : null, motivazione);

	}

	@Override
	public List<LockedPratica> rispondiDocumenti(TipoRispostaParereDTO tipoRisposta, Map<String, List<String>> mapPathAllegati, boolean riassegna, InformazioniNotificaTaskFirmaDTO infoNotifica,
			String ruolo, List<FileDTO> fileDaAllegare, String motivazione) throws SpagicClientException {

		if (riassegna) {
			InformazioniRiassegnazioneFascicoliTaskFirmaDTO riassegnaFascicoliInfo = (InformazioniRiassegnazioneFascicoliTaskFirmaDTO) infoNotifica;
			return spagicClientTaskFirma.rispondiDocumenti(TipoRispostaTaskFirmaClient.valueOf(tipoRisposta.name()), mapPathAllegati, true, riassegnaFascicoliInfo.getAnagraficaRuolo().getRuolo(),
					riassegnaFascicoliInfo.getNote(), riassegnaFascicoliInfo.getIndirizziNotifica(), riassegnaFascicoliInfo.getOperatore(), userSessionUtil.getUtenteSpagic(), ruolo,
					(fileDaAllegare != null) ? Lists.transform(fileDaAllegare, FunctionFile.INSTANCE) : null, motivazione);

		}

		return spagicClientTaskFirma.rispondiDocumenti(TipoRispostaTaskFirmaClient.valueOf(tipoRisposta.name()), mapPathAllegati, false, null, infoNotifica != null ? infoNotifica.getNote() : null,
				infoNotifica != null ? infoNotifica.getIndirizziNotifica() : new ArrayList<String>(), null, userSessionUtil.getUtenteSpagic(), ruolo,
				(fileDaAllegare != null) ? Lists.transform(fileDaAllegare, FunctionFile.INSTANCE) : null, motivazione);

	}

	@Override
	public List<LockedPratica> diniegoDocumenti(Map<String, List<String>> mapPathAllegati, boolean riassegna, InformazioniNotificaTaskFirmaDTO infoNotifica, String ruolo, List<FileDTO> fileDaAllegare,
			String motivazione) throws SpagicClientException {

		if (riassegna) {
			InformazioniRiassegnazioneFascicoliTaskFirmaDTO riassegnaFascicoliInfo = (InformazioniRiassegnazioneFascicoliTaskFirmaDTO) infoNotifica;
			return spagicClientTaskFirma.diniegoDocumenti(mapPathAllegati, true, riassegnaFascicoliInfo.getAnagraficaRuolo().getRuolo(), riassegnaFascicoliInfo.getNote(),
					riassegnaFascicoliInfo.getIndirizziNotifica(), riassegnaFascicoliInfo.getOperatore(), userSessionUtil.getUtenteSpagic(), ruolo,
					(fileDaAllegare != null) ? Lists.transform(fileDaAllegare, FunctionFile.INSTANCE) : null, motivazione);
		}

		return spagicClientTaskFirma.diniegoDocumenti(mapPathAllegati, false, null, infoNotifica != null ? infoNotifica.getNote() : null,
				infoNotifica != null ? infoNotifica.getIndirizziNotifica() : new ArrayList<String>(), null, userSessionUtil.getUtenteSpagic(), ruolo,
				(fileDaAllegare != null) ? Lists.transform(fileDaAllegare, FunctionFile.INSTANCE) : null, motivazione);

	}

	@Override
	public List<LockedPratica> ritiroDocumenti(Map<String, List<String>> mapPathAllegati, InformazioniNotificaTaskFirmaDTO infoNotifica, String motivazione) throws SpagicClientException {
		return spagicClientTaskFirma.ritiroDocumenti(mapPathAllegati, infoNotifica != null ? infoNotifica.getNote() : null,
				infoNotifica != null ? infoNotifica.getIndirizziNotifica() : new ArrayList<String>(), userSessionUtil.getUtenteSpagic(), motivazione);
	}

	@Override
	public List<LockedPratica> evadiDocumenti(Map<String, List<Integer>> praticaIdTaskFirmaMap) throws SpagicClientException {
		return spagicClientTaskFirma.evadiDocumenti(praticaIdTaskFirmaMap, userSessionUtil.getUtenteSpagic());
	}

	private enum FunctionFile implements Function<FileDTO, File> {
		INSTANCE;

		@Override
		public File apply(FileDTO input) {
			return new File(input.getPath());
		}
	}
}
