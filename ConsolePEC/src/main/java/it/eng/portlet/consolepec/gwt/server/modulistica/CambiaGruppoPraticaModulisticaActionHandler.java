package it.eng.portlet.consolepec.gwt.server.modulistica;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeRiassegnazione;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.xmlplugin.pratica.modulistica.PraticaModulistica;
import it.eng.consolepec.xmlplugin.tasks.gestionemodulistica.GestionePraticaModulisticaTask;
import it.eng.portlet.consolepec.gwt.server.AbstractCambiaGruppoActionHandler;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.assegna.modulo.RiassegnaModulo;
import it.eng.portlet.consolepec.gwt.shared.action.assegna.modulo.RiassegnaModuloResult;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.GestioneTaskPratiche;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class CambiaGruppoPraticaModulisticaActionHandler extends AbstractCambiaGruppoActionHandler<RiassegnaModulo, RiassegnaModuloResult> {

	Logger logger = LoggerFactory.getLogger(CambiaGruppoPraticaModulisticaActionHandler.class);

	@Autowired
	XMLPluginToDTOConverter praticaUtil;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	GestioneTaskPratiche gestioneTaskPratiche;
	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;
	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;

	public CambiaGruppoPraticaModulisticaActionHandler() {}

	@Override
	public RiassegnaModuloResult execute(RiassegnaModulo action, ExecutionContext context, PreferenzeRiassegnazione preferenzeRiassegnazione) {
		RiassegnaModuloResult result = null;
		List<PraticaModulisticaDTO> tempPratiche = new ArrayList<PraticaModulisticaDTO>();
		try {
			for (String id : action.getIds()) {
				PraticaModulistica pratica = praticaSessionUtil.loadPraticaModulisticaFromEncodedPath(id, TipologiaCaricamento.CARICA);
				GestionePraticaModulisticaTask taskCorrente = gestioneTaskPratiche.estraiTaskCorrente(pratica, GestionePraticaModulisticaTask.class,
						gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());

				// se in carico rilascio
				if (pratica.getDati().getInCaricoA() != null) {
					taskCorrente.rilasciaInCarico(pratica.getDati().getInCaricoA());
				}

				// riassegno la mail
				AnagraficaRuolo ar = gestioneConfigurazioni.getAnagraficaRuolo(preferenzeRiassegnazione.getRuolo());
				taskCorrente.riassegna(ar, action.getOperatore());
				praticaSessionUtil.updatePraticaModulistica(pratica);
				tempPratiche.add(praticaUtil.praticaModulisticaToDettaglio(pratica));
			}

			result = new RiassegnaModuloResult(tempPratiche, null, false);

		} catch (SpagicClientException e) {
			result = new RiassegnaModuloResult(null, e.getErrorMessage(), true);

		} catch (Exception e) {
			result = new RiassegnaModuloResult(null, ConsolePecConstants.ERROR_MESSAGE, true);
		}

		return result;
	}

	@Override
	public void undo(RiassegnaModulo action, RiassegnaModuloResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<RiassegnaModulo> getActionType() {
		return RiassegnaModulo.class;
	}

	@Override
	public GestioneProfilazioneUtente getGestioneProfilazioneUtente() {
		return gestioneProfilazioneUtente;
	}
}
