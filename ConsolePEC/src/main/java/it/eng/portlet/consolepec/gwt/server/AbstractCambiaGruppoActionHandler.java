package it.eng.portlet.consolepec.gwt.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import com.gwtplatform.dispatch.shared.Result;

import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeRiassegnazione;
import it.eng.portlet.consolepec.gwt.shared.action.assegna.RiassegnaAction;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.genericdata.IndirizzoEmailCacheSecondoLivello;

public abstract class AbstractCambiaGruppoActionHandler<A extends RiassegnaAction<R>, R extends Result> implements ActionHandler<A, R> {

	@Autowired
	IndirizzoEmailCacheSecondoLivello indirizzoEmailCacheSecondoLivello;
	
	public AbstractCambiaGruppoActionHandler() {
	}

	@Override
	public R execute(A action, ExecutionContext context) throws ActionException {
		Logger logger = LoggerFactory.getLogger(AbstractCambiaGruppoActionHandler.class);
		
		PreferenzeRiassegnazione preferenzeRiassegnazione = null;
		
		preferenzeRiassegnazione = new PreferenzeRiassegnazione();
		preferenzeRiassegnazione.setRuolo(action.getAnagraficaRuolo().getRuolo());
		preferenzeRiassegnazione.setSettore(action.getSettore() != null ? action.getSettore().getNome() : null);
		preferenzeRiassegnazione.setRicordaScelta(action.isRicordaScelta());
		preferenzeRiassegnazione.setIndirizziNotifica(action.getIndirizziNotifica());
		
		try {
			
			if (action.getIndirizziNotifica() != null) {
				for (String indirizzoEmail : action.getIndirizziNotifica()) {
					indirizzoEmailCacheSecondoLivello.checkIndirizzoEmail(indirizzoEmail, getGestioneProfilazioneUtente().getDatiUtente());
				}
			}
			
		} catch (Exception e) {
			logger.error("Errore nel salvataggio degli indirizzi di notifica nella rubrica personale");
		}
		
		return execute(action, context, preferenzeRiassegnazione);
	}

	public abstract R execute(A action, ExecutionContext context, PreferenzeRiassegnazione aggiornaDatiDiRiassegnazione);

	public abstract GestioneProfilazioneUtente getGestioneProfilazioneUtente();
}
