package it.eng.portlet.consolepec.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtplatform.mvp.client.DelayedBindRegistry;

import it.eng.portlet.consolepec.gwt.client.gin.ClientGinjector;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.drive.DriveHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.IndirizziEmailHandler;

public class ConsolePECGWTP implements EntryPoint {

	private final ClientGinjector ginjector = GWT.create(ClientGinjector.class);

	@Override
	public void onModuleLoad() {

		// This is required for Gwt-Platform proxy's generator
		DelayedBindRegistry.bind(ginjector);

		if (RootPanel.get("gwtdiv") != null) {

			/*
			 * CARICAMENTI SINCRONI ALL'AVVIO
			 */

			final ConfigurazioniHandler configurazioniHandler = ginjector.getConfigurazioniHandler();
			final ProfilazioneUtenteHandler profilazioneUtenteHandler = ginjector.getProfilazioneUtenteHandler();
			final DriveHandler driveHandler = ginjector.getDriveHandler();

			PostLoadingAction action = new PostLoadingAction() {
				int handlerCaricati = 0;

				@Override
				public void onComplete() {
					handlerCaricati++;
					if (handlerCaricati == 3) {
						ginjector.getPlaceManager().revealCurrentPlace();
					}
				}
			};

			configurazioniHandler.init(action);
			profilazioneUtenteHandler.init(action);
			driveHandler.init(action);

			/*
			 * CARICAMENTI DIFFERITI
			 */

			// Invocato in maniera asincrona per caricare la cache all'avvio
			final IndirizziEmailHandler indirizziEmailHandler = ginjector.getIndirizziEmailHandler();
			indirizziEmailHandler.caricaCache();

		}
	}
}
