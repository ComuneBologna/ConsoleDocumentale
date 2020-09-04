package it.eng.portlet.consolepec.gwt.client.operazioni.pec.impl;

import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;

import it.eng.cobo.consolepec.util.objects.Ref;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmaiInlLoaded;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmailOutLoaded;
import it.eng.portlet.consolepec.gwt.client.operazioni.pec.PecApiClient;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.pec.EstraiEMLAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.EstraiEMLResult;
import it.eng.portlet.consolepec.gwt.shared.action.pec.TipoRiferimentoPEC;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPECRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO.StatoDTO;

/**
 *
 * @author biagiot
 *
 */
public class PecApiClientImpl implements PecApiClient {

	private DispatchAsync dispatcher;
	private PecInPraticheDB praticheDB;

	@Inject
	public PecApiClientImpl(DispatchAsync dispatcher, PecInPraticheDB praticheDB) {
		this.dispatcher = dispatcher;
		this.praticheDB = praticheDB;
	}

	@Override
	public void isEstrazioneEMLAbilitata(Set<ElementoElenco> pecSelezionate, final BooleanCallback booleanCallback) {

		if (pecSelezionate.size() == 1) {
			ElementoElenco el = pecSelezionate.iterator().next();

			if (el instanceof ElementoPECRiferimento) {
				ElementoPECRiferimento elPECRif = (ElementoPECRiferimento) el;

				if (TipoRiferimentoPEC.OUT.equals(elPECRif.getTipo())) {
					praticheDB.getPecOutByPath(elPECRif.getRiferimento(), false, new PraticaEmailOutLoaded() {

						@Override
						public void onPraticaLoaded(PecOutDTO pec) {
							booleanCallback.onComplete(!StatoDTO.BOZZA.equals(pec.getStato()) && !StatoDTO.DA_INVIARE.equals(pec.getStato()));
						}

						@Override
						public void onPraticaError(String error) {
							booleanCallback.onError(ConsolePecConstants.ERROR_MESSAGE);
						}
					});

				} else if (TipoRiferimentoPEC.IN.equals(elPECRif.getTipo()) || TipoRiferimentoPEC.EPROTO.equals(elPECRif.getTipo())) {
					booleanCallback.onComplete(true);
				} else {
					booleanCallback.onComplete(false);
				}
			}
		} else {
			booleanCallback.onComplete(false);
		}
	}

	@Override
	public void estraiEML(String pecPath, String praticaPath, final PraticaCallback praticaCallback) {
		EstraiEMLAction action = new EstraiEMLAction(pecPath, praticaPath);

		dispatcher.execute(action, new AsyncCallback<EstraiEMLResult>() {

			@Override
			public void onFailure(Throwable caught) {
				praticaCallback.onError(ConsolePecConstants.ERROR_MESSAGE);
			}

			@Override
			public void onSuccess(EstraiEMLResult result) {

				if (result.isError())
					praticaCallback.onError(result.getErrorMessage());
				else
					praticaCallback.onComplete(result.getPratica());
			}
		});
	}

	@Override
	public void isProtocollazioneAbilitata(final Set<ElementoElenco> pecSelezionate, final BooleanCallback booleanCallback) {

		if (pecSelezionate.isEmpty()) {
			booleanCallback.onComplete(true);
			return;
		}

		Iterator<ElementoElenco> iterator = pecSelezionate.iterator();

		for (int i = 1; i <= pecSelezionate.size(); i++) {

			final Ref<Integer> ref = Ref.of(i);

			ElementoElenco el = iterator.next();

			if (el instanceof ElementoPECRiferimento) {
				ElementoPECRiferimento elPECRif = (ElementoPECRiferimento) el;

				if (TipoRiferimentoPEC.OUT.equals(elPECRif.getTipo())) {

					praticheDB.getPecOutByPath(elPECRif.getRiferimento(), false, new PraticaEmailOutLoaded() {

						@Override
						public void onPraticaLoaded(PecOutDTO pec) {
							if (!pec.isProtocollabile())
								booleanCallback.onComplete(false);

							else if (ref.get() == pecSelezionate.size())
								booleanCallback.onComplete(true);
						}

						@Override
						public void onPraticaError(String error) {
							booleanCallback.onError(ConsolePecConstants.ERROR_MESSAGE);
						}
					});

				} else if (TipoRiferimentoPEC.IN.equals(elPECRif.getTipo()) || TipoRiferimentoPEC.EPROTO.equals(elPECRif.getTipo())) {

					praticheDB.getPecInByPath(elPECRif.getRiferimento(), false, new PraticaEmaiInlLoaded() {

						@Override
						public void onPraticaLoaded(PecInDTO pec) {
							if (!pec.isProtocollabile())
								booleanCallback.onComplete(false);

							else if (ref.get() == pecSelezionate.size())
								booleanCallback.onComplete(true);
						}

						@Override
						public void onPraticaError(String error) {
							booleanCallback.onError(ConsolePecConstants.ERROR_MESSAGE);
						}

					});

				} else {
					booleanCallback.onComplete(false);
				}

			}
		}
	}
}
