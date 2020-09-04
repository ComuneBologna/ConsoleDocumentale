package it.eng.portlet.consolepec.gwt.client.handler.profilazione;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;

import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeCartellaAttivita;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeFirmaDigitale;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeRiassegnazione;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler.ProfilazioneUtenteCallback;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.GestionePreferenzeUtenteAction;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.GestionePreferenzeUtenteAction.TipoOperazione;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.GestionePreferenzeUtenteResult;
import lombok.AccessLevel;
import lombok.Getter;

/**
 *
 * @author biagiot
 *
 */
public class PreferenzeUtenteHandler {

	private DispatchAsync dispatchAsync;
	
	@Getter(AccessLevel.PROTECTED)
	private PreferenzeUtente preferenzeUtente;
	
	@Getter(AccessLevel.PROTECTED)
	private PreferenzeFirmaDigitale preferenzeFirmaDigitale;
	
	@Getter(AccessLevel.PROTECTED)
	private PreferenzeRiassegnazione preferenzeRiassegnazione;
	
	@Inject
	public PreferenzeUtenteHandler(DispatchAsync dispatchAsync) {
		super();
		this.dispatchAsync = dispatchAsync;
	}


	protected void caricaPreferenze(final ProfilazioneUtenteCallback callback) {
		
		dispatchAsync.execute(new GestionePreferenzeUtenteAction(), new AsyncCallback<GestionePreferenzeUtenteResult>() {

			@Override
			public void onFailure(Throwable t) {
				
				if (callback != null)
					callback.onFailure(ConsolePecConstants.ERROR_MESSAGE);
			}

			@Override
			public void onSuccess(GestionePreferenzeUtenteResult result) {
				
				if (result.isError()) {
					if (callback != null)
						callback.onFailure(result.getErrorMessage());
					
				} else {
					preferenzeUtente = result.getPreferenzeUtente();
					preferenzeFirmaDigitale = result.getPreferenzeFirmaDigitale();
					preferenzeRiassegnazione = result.getPreferenzeRiassegnazione();
					
					if (callback != null)
						callback.onSuccess();
				}
			}
		});
	}
	
	protected void aggiornaPreferenzeFirmaDigitale(boolean ricorda, String username, String password, final ProfilazioneUtenteCallback callback) {
		
		if (preferenzeFirmaDigitale == null) {
			preferenzeFirmaDigitale = new PreferenzeFirmaDigitale();
		}
		
		if (ricorda) {

			preferenzeFirmaDigitale.setUsername(username);
			preferenzeFirmaDigitale.setPassword(password);
			
			dispatchAsync.execute(new GestionePreferenzeUtenteAction(preferenzeFirmaDigitale, TipoOperazione.AGGIORNA), new AsyncCallback<GestionePreferenzeUtenteResult>() {

				@Override
				public void onFailure(Throwable t) {
					if (callback != null)
						callback.onFailure(ConsolePecConstants.ERROR_MESSAGE);
				}

				@Override
				public void onSuccess(GestionePreferenzeUtenteResult result) {
					
					if (callback != null) {
						
						if (result.isError()) {
							callback.onFailure(result.getErrorMessage());
							
						} else {
							callback.onSuccess();
						}
					}
				}
			});
			
		} else {
			dispatchAsync.execute(new GestionePreferenzeUtenteAction(preferenzeFirmaDigitale, TipoOperazione.ELIMINA), new AsyncCallback<GestionePreferenzeUtenteResult>() {

				@Override
				public void onFailure(Throwable t) {
					if (callback != null)
						callback.onFailure(ConsolePecConstants.ERROR_MESSAGE);
				}

				@Override
				public void onSuccess(GestionePreferenzeUtenteResult result) {
					
					if (callback != null) {
						
						if (result.isError()) {
							callback.onFailure(result.getErrorMessage());
							
						} else {
							callback.onSuccess();
						}
					}
				}
			});
		}
	}
	
	protected void aggiornaPreferenzeRiassegnazione(boolean ricorda, String settore, String ruolo, List<String> indirizziNotifica, final ProfilazioneUtenteCallback callback) {
		if (preferenzeRiassegnazione == null) {
			preferenzeRiassegnazione = new PreferenzeRiassegnazione();
		}
		
		if (ricorda) {
			
			preferenzeRiassegnazione.setSettore(settore);
			preferenzeRiassegnazione.setRuolo(ruolo);
			if (indirizziNotifica != null)
				preferenzeRiassegnazione.setIndirizziNotifica(indirizziNotifica);
			preferenzeRiassegnazione.setRicordaScelta(ricorda);
			
			dispatchAsync.execute(new GestionePreferenzeUtenteAction(preferenzeRiassegnazione, TipoOperazione.AGGIORNA), new AsyncCallback<GestionePreferenzeUtenteResult>() {

				@Override
				public void onFailure(Throwable t) {
					if (callback != null)
						callback.onFailure(ConsolePecConstants.ERROR_MESSAGE);
				}

				@Override
				public void onSuccess(GestionePreferenzeUtenteResult result) {
					
					if (callback != null) {
						
						if (result.isError()) {
							callback.onFailure(result.getErrorMessage());
							
						} else {
							callback.onSuccess();
						}
					}
				}
			});	
			
		} else {
			dispatchAsync.execute(new GestionePreferenzeUtenteAction(preferenzeRiassegnazione, TipoOperazione.ELIMINA), new AsyncCallback<GestionePreferenzeUtenteResult>() {

				@Override
				public void onFailure(Throwable t) {
					if (callback != null)
						callback.onFailure(ConsolePecConstants.ERROR_MESSAGE);
				}

				@Override
				public void onSuccess(GestionePreferenzeUtenteResult result) {
					
					if (callback != null) {
						
						if (result.isError()) {
							callback.onFailure(result.getErrorMessage());
							
						} else {
							callback.onSuccess();
						}
					}
				}
			});
		}
		
	}
	
	protected void aggiornaPreferenze(String firmaEmail, String fascicoloDefault, PreferenzeCartellaAttivita preferenzeCartellaAttivita, final ProfilazioneUtenteCallback callback) {
		
		if (firmaEmail != null) {
			preferenzeUtente.setFirmaEmail(firmaEmail);
		}
		
		if (fascicoloDefault != null) {
			preferenzeUtente.setFascicoloDefault(fascicoloDefault);
		}
		
		if (preferenzeCartellaAttivita != null) {
			
			if (preferenzeUtente.getPreferenzeCartellaAttivita() == null)
				preferenzeUtente.setPreferenzeCartellaAttivita(new PreferenzeCartellaAttivita());
			
			if (preferenzeCartellaAttivita.getIndirizziNotifica() != null)
				preferenzeUtente.getPreferenzeCartellaAttivita().setIndirizziNotifica(preferenzeCartellaAttivita.getIndirizziNotifica());
			
			if (preferenzeCartellaAttivita.getStatoFinaleRicerca() != null)
				preferenzeUtente.getPreferenzeCartellaAttivita().setStatoFinaleRicerca(preferenzeCartellaAttivita.getStatoFinaleRicerca());
			
			if (preferenzeCartellaAttivita.getTipoRicerca() != null)
				preferenzeUtente.getPreferenzeCartellaAttivita().setTipoRicerca(preferenzeCartellaAttivita.getTipoRicerca());
		}
		
		dispatchAsync.execute(new GestionePreferenzeUtenteAction(preferenzeUtente, TipoOperazione.AGGIORNA), new AsyncCallback<GestionePreferenzeUtenteResult>() {

			@Override
			public void onFailure(Throwable t) {
				if (callback != null)
					callback.onFailure(ConsolePecConstants.ERROR_MESSAGE);
			}

			@Override
			public void onSuccess(GestionePreferenzeUtenteResult result) {
				
				if (callback != null) {

					if (result.isError()) {
						callback.onFailure(result.getErrorMessage());
						
					} else {
						callback.onSuccess();
					}
				}
			}
		});		
	}
}
