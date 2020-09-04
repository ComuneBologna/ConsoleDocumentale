package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command;

import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.SganciaPecIn;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.SganciaPecInResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPECRiferimento;

import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class SganciaPecInDaFascicoloCommand extends AbstractConsolePecCommand<DettaglioFascicoloGenericoPresenter> {
	public SganciaPecInDaFascicoloCommand(DettaglioFascicoloGenericoPresenter presenter) {
		super(presenter);
	}

	@Override
	protected void _execute() {
		// Path del fascicolo su Alfresco
		final String fascicoloPath = getPresenter().getView().getFascicolo().getClientID();
		// Riferimento alla mail
		String pecInPath = null;

		//-- Recupero il path della PecIn. Il path e' quello usato in Alfresco --
		//Scorro le pratiche non protocollate
		Set<ElementoElenco> praticheNonProtocollateList = getPresenter().getView().getPraticheNonProtSelezionate();
		if (praticheNonProtocollateList != null) {
			if (praticheNonProtocollateList.size() == 1) {
				// Restituisci l'email??
				pecInPath = ((ElementoPECRiferimento) praticheNonProtocollateList.iterator().next()).getRiferimento();
			} else {
				throw new IllegalStateException("Verificare che sia stata selezionata solo una PEcIn. E' possibile che sia stato selezionato piu' di un documento oltre alla PecIn.");
			}
		}

		final String pecInPathFinal = pecInPath;

		ShowAppLoadingEvent.fire(getPresenter(), true);
		getPresenter().getDispatchAsync().execute(new SganciaPecIn(fascicoloPath, pecInPath), new AsyncCallback<SganciaPecInResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getPresenter()._getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(SganciaPecInResult result) {
				ShowAppLoadingEvent.fire(getPresenter(), false);
				if (!result.isError()) {
					getPresenter().getPecInPraticheDB().update(result.getFascicoloDTO().getClientID(), result.getFascicoloDTO(), getPresenter().getSitemapMenu().containsLink(result.getFascicoloDTO().getClientID()));
					getPresenter().getPecInPraticheDB().remove(pecInPathFinal);
					getPresenter().getPlaceManager().revealCurrentPlace();
				} else {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getPresenter()._getEventBus().fireEvent(event);
				}
			}

		});
	}
}
