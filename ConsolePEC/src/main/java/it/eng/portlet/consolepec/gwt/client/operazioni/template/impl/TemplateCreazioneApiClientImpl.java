package it.eng.portlet.consolepec.gwt.client.operazioni.template.impl;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.event.RichiestaConfermaAnnullaEvent;
import it.eng.portlet.consolepec.gwt.client.event.SceltaConfermaAnnullaEvent;
import it.eng.portlet.consolepec.gwt.client.event.SceltaConfermaAnnullaEvent.SceltaConfermaAnnullaHandler;
import it.eng.portlet.consolepec.gwt.client.operazioni.template.TemplateCreazioneApiClient;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.widget.template.AbstractCorpoTemplateWidget;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.EstraiEtichetteMetadatiAction;
import it.eng.portlet.consolepec.gwt.shared.action.EstraiEtichetteMetadatiResult;
import it.eng.portlet.consolepec.gwt.shared.action.template.creazione.CreaModelloAction;
import it.eng.portlet.consolepec.gwt.shared.action.template.creazione.CreaModelloResult;
import it.eng.portlet.consolepec.gwt.shared.action.template.salvataggio.SalvaModelloAction;
import it.eng.portlet.consolepec.gwt.shared.action.template.salvataggio.SalvaModelloResult;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;

/**
 *
 * @author biagiot
 *
 */
public class TemplateCreazioneApiClientImpl implements TemplateCreazioneApiClient, HasHandlers, SceltaConfermaAnnullaHandler {

	private DispatchAsync dispatcher;
	private PecInPraticheDB praticheDB;
	private final SitemapMenu sitemapMenu;
	private EventBus eventBus;
	private Map<TipologiaPratica, Map<String, String>> tipoPraticaMetadatiEtichetteMap = new HashMap<TipologiaPratica, Map<String,String>>();

	@Inject
	public TemplateCreazioneApiClientImpl(DispatchAsync dispatcher, PecInPraticheDB praticheDB, SitemapMenu siteMapMenu, EventBus eventBus) {
		this.dispatcher = dispatcher;
		this.praticheDB = praticheDB;
		this.sitemapMenu = siteMapMenu;
		eventBus.addHandler(SceltaConfermaAnnullaEvent.getType(), this);
		this.eventBus = eventBus;
	}

	@Override
	public void loadEtichetteMetadatiMap(final TipologiaPratica tipoPratica, final CallbackMap callbackMap) {

		if (tipoPraticaMetadatiEtichetteMap != null && tipoPraticaMetadatiEtichetteMap.get(tipoPratica)!=null)
			callbackMap.onComplete(tipoPraticaMetadatiEtichetteMap.get(tipoPratica));

		else {
			EstraiEtichetteMetadatiAction action = new EstraiEtichetteMetadatiAction(tipoPratica.getNomeTipologia());

			dispatcher.execute(action, new AsyncCallback<EstraiEtichetteMetadatiResult>() {

				@Override
				public void onFailure(Throwable caught) {
					callbackMap.onError(ConsolePecConstants.ERROR_MESSAGE);

				}

				@Override
				public void onSuccess(EstraiEtichetteMetadatiResult result) {
					if (result.isError()) {
						callbackMap.onError(result.getErrorMessage());

					} else {
						tipoPraticaMetadatiEtichetteMap.put(tipoPratica, result.getEtichetteMetadatiMap());
						callbackMap.onComplete(result.getEtichetteMetadatiMap());
					}
				}
			});
		}
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEventFromSource(event, this);
	}

	/*
	 * GESTIONE DIALOG DI CONFERMA IN CASO DI MODIFICA DEI FASCICOLI SELEZIONATI
	 */

	String operation;
	String eventID;
	AbstractCorpoTemplateWidget<?> corpoTemplateWidget;

	@Override
	public void gestisciModificheFascicoli(AbstractCorpoTemplateWidget<?> corpoTemplateWidget, String text, String operation) {
		eventID = DOM.createUniqueId();
		this.operation = operation;
		this.corpoTemplateWidget = corpoTemplateWidget;

		RichiestaConfermaAnnullaEvent.fire(TemplateCreazioneApiClientImpl.this, text, eventID);
	}

	@Override
	@ProxyEvent
	public void onSceltaConfermaAnnulla(SceltaConfermaAnnullaEvent event) {
		if (event.isConfermato() && event.getEventId().equals(eventID)) {
			this.corpoTemplateWidget.onEliminaAggiungiFascicolo(event, operation);
		}
	}

	@Override
	public <T extends BaseTemplateDTO> void creaModello(T template, final CallbackTemplate<T> callback) {

		CreaModelloAction action = new CreaModelloAction();
		action.setModello(template);
		dispatcher.execute(action, new AsyncCallback<CreaModelloResult>() {

			@Override
			public void onFailure(Throwable e) {
				callback.onError(ConsolePecConstants.ERROR_MESSAGE);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(CreaModelloResult result) {

				if (result.isError())
					callback.onError(result.getErrorMessage());
				else
					callback.onComplete((T) result.getModello());
			}
		});
		
	}

	@Override
	public <T extends BaseTemplateDTO> void salvaModello(T template, String path, final CallbackTemplate<T> callback) {
		
		path = 	Base64Utils.URLdecodeAlfrescoPath(path);
		SalvaModelloAction action = new SalvaModelloAction();
		action.setModello(template);
		action.setPathModello(path);
		
		dispatcher.execute(action, new AsyncCallback<SalvaModelloResult>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onError(ConsolePecConstants.ERROR_MESSAGE);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(SalvaModelloResult result) {

				if (result.isError())
					callback.onError(result.getErrorMessage());

				else {
					praticheDB.insertOrUpdate(result.getModello().getClientID(), result.getModello(), sitemapMenu.containsLink(result.getModello().getClientID()));
					callback.onComplete((T) result.getModello());
				}
			}
		});
	}

	@Override
	public <T extends BaseTemplateDTO> void creaModelloPerCopia(String idDocumentale, final CallbackTemplate<T> callback) {
		
		CreaModelloAction action = new CreaModelloAction();
		action.setIdDocumentale(idDocumentale);
		
		dispatcher.execute(action, new AsyncCallback<CreaModelloResult>() {

			@Override
			public void onFailure(Throwable e) {
				callback.onError(ConsolePecConstants.ERROR_MESSAGE);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(CreaModelloResult result) {

				if (result.isError())
					callback.onError(result.getErrorMessage());
				else
					callback.onComplete((T) result.getModello());
			}
		});
	}
}