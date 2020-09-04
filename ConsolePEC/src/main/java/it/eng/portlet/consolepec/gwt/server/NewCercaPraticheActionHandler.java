package it.eng.portlet.consolepec.gwt.server;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.ModificaFascicoloAbilitazione;
import it.eng.cobo.consolepec.commons.services.PraticaProcediResponse;
import it.eng.cobo.consolepec.commons.services.ServiceResponse;
import it.eng.cobo.consolepec.commons.urbanistica.PraticaProcedi;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler.CondizioneAbilitazione;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler.QueryAbilitazione;
import it.eng.consolepec.client.IntegrazionePraticaProcediClient;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPraticheResult;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.search.NewMongoDbSearchAdapter;
import it.eng.portlet.consolepec.spring.bean.search.NewMongoDbSearchAdapter.CountResponse;
import it.eng.portlet.consolepec.spring.bean.search.PraticaProcediSearchUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class NewCercaPraticheActionHandler implements ActionHandler<CercaPratiche, CercaPraticheResult> {
	private org.slf4j.Logger logger;

	@Autowired
	NewMongoDbSearchAdapter newMongoDbSearchAdapter;

	@Autowired
	IntegrazionePraticaProcediClient integrazionePraticaProcediClient;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	XMLPluginToDTOConverter xmlPluginToDTOConverter;

	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;

	@Autowired
	PraticaProcediSearchUtil praticaProcediSearchUtil;

	public NewCercaPraticheActionHandler() {}

	@Override
	public CercaPraticheResult execute(CercaPratiche cercaPraticheAction, ExecutionContext context) throws ActionException {
		logger = LoggerFactory.getLogger(NewCercaPraticheActionHandler.class);
		logger.debug("RicercaPraticheActionHandler: DEBUG IN");

		List<PraticaDTO> praticheProcediDTO = new ArrayList<PraticaDTO>();

		CercaPraticheResult res = new CercaPraticheResult();
		res.setError(false);
		res.setMessError(null);

		if (cercaPraticheAction.getTipologiePratiche() != null && !cercaPraticheAction.getTipologiePratiche().isEmpty() && cercaPraticheAction.getTipologiePratiche().size() == 1
				&& cercaPraticheAction.getTipologiePratiche().get(0) != null
				&& TipologiaPratica.PRATICA_PROCEDI.getNomeTipologia().equals(cercaPraticheAction.getTipologiePratiche().get(0).getNomeTipologia())) {

			QueryAbilitazione<ModificaFascicoloAbilitazione> qab = new QueryAbilitazione<ModificaFascicoloAbilitazione>();
			qab.addCondition(new CondizioneAbilitazione<ModificaFascicoloAbilitazione>() {

				@Override
				protected boolean valutaCondizione(ModificaFascicoloAbilitazione abilitazione) {
					return abilitazione.getTipo().equals(TipologiaPratica.PRATICA_PROCEDI.getNomeTipologia());
				}

			});

			if (gestioneProfilazioneUtente.getAutorizzazioniUtente().isAbilitato(ModificaFascicoloAbilitazione.class, qab)) {
				if (cercaPraticheAction.isCount()) {
					ServiceResponse<PraticaProcediResponse> serviceResponse = integrazionePraticaProcediClient.countPratiche(praticaProcediSearchUtil.createQueryFilters(cercaPraticheAction),
							userSessionUtil.getUtenteSpagic());
					if (serviceResponse.isError()) {
						res.setError(true);
						res.setMessError(ConsolePecConstants.ERROR_MESSAGE);
					} else {
						long countPratiche = serviceResponse.getResponse().getCount();
						Integer count = new Integer((int) countPratiche);
						res.setEstimate(false);
						res.setMaxResult(count);
					}
				} else {
					int limit = cercaPraticheAction.getFine() - cercaPraticheAction.getInizio();
					int offset = cercaPraticheAction.getInizio();
					ServiceResponse<PraticaProcediResponse> serviceResponse = integrazionePraticaProcediClient.ricerca(praticaProcediSearchUtil.createQueryFilters(cercaPraticheAction),
							praticaProcediSearchUtil.createSortFilters(cercaPraticheAction), limit, offset, userSessionUtil.getUtenteSpagic());
					if (serviceResponse.isError()) {
						res.setError(true);
						res.setMessError(ConsolePecConstants.ERROR_MESSAGE);
					} else {
						List<PraticaProcedi> praticheProcedi = serviceResponse.getResponse().getListaPraticaProcedi();

						for (PraticaProcedi p : praticheProcedi)
							if (p != null) {
								PraticaDTO praticaDTO = xmlPluginToDTOConverter.procediToPraticaDTO(p);
								if (praticaDTO != null) {
									praticheProcediDTO.add(praticaDTO);
								}
							}

						if (!praticheProcediDTO.isEmpty()) {
							res.setPratiche(praticheProcediDTO);
							logger.debug("Caricate {} pratiche)", res.getPratiche().size());

						} else {
							res.setError(true);
							res.setMessError(ConsolePecConstants.ERROR_MESSAGE);
						}
					}
				}

			} else {

				if (cercaPraticheAction.isCount()) {
					res.setEstimate(false);
					res.setMaxResult(0);

				} else {
					res.setPratiche(new ArrayList<PraticaDTO>());

				}
			}

		} else {
			try {
				/* query di count */
				if (cercaPraticheAction.isCount()) {
					CountResponse countPratiche = newMongoDbSearchAdapter.countPratiche(cercaPraticheAction);
					if (countPratiche != null) {
						res.setEstimate(countPratiche.isEstimate());
						res.setMaxResult(countPratiche.getCount());
					} else {
						res.setError(true);
						res.setMessError(ConsolePecConstants.ERROR_MESSAGE);
					}
					/* query di Fetch dati */
				} else {
					List<PraticaDTO> pratiche = newMongoDbSearchAdapter.searchPratiche(cercaPraticheAction);
					if (pratiche != null) {
						res.setPratiche(pratiche);
						logger.debug("Caricate {} pratiche)", res.getPratiche().size());
					} else {
						res.setError(true);
						res.setMessError(ConsolePecConstants.ERROR_MESSAGE);
					}
				}
			} catch (Exception e) {
				logger.error(e.getLocalizedMessage(), e);
				res.setError(true);
				res.setMessError(ConsolePecConstants.ERROR_MESSAGE);
			}
		}
		return res;
	}

	@Override
	public void undo(CercaPratiche action, CercaPraticheResult result, ExecutionContext context) throws ActionException {
		//
	}

	@Override
	public Class<CercaPratiche> getActionType() {
		return CercaPratiche.class;
	}

}
