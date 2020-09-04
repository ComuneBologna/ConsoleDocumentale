package it.eng.portlet.consolepec.gwt.client.presenter.urbanistica;

import it.eng.cobo.consolepec.commons.firmadigitale.FirmaDigitale;
import it.eng.cobo.consolepec.commons.urbanistica.AllegatoProcedi;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.RicercaLiberaPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.event.urbanistica.MostraDettaglioAllegatoProcediEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.urbanistica.MostraDettaglioAllegatoProcediEvent.MostraDettaglioAllegatoProcediHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.urbanistica.command.DettaglioPraticaProcediCommand;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.DettaglioAllegatoFirmatoAction;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.DettaglioAllegatoFirmatoResult;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

/**
 * @author GiacomoFM
 * @since 09/feb/2018
 */
public class DettaglioAllegatoProcediPresenter extends Presenter<DettaglioAllegatoProcediPresenter.MyView, DettaglioAllegatoProcediPresenter.MyProxy> implements MostraDettaglioAllegatoProcediHandler {

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<DettaglioAllegatoProcediPresenter> {
		//
	}

	public interface MyView extends View {
		void mostraAllegato(final AllegatoProcedi allegatoProcedi, final FirmaDigitale firmaDigitale);

		void mostraAllegato(final AllegatoProcedi allegatoProcedi, final String infoFirma);

		void setIndietroCommand(final DettaglioPraticaProcediCommand dettaglioPraticaProcediCommand);
	}

	private final DispatchAsync dispatcher;

	@Inject
	public DettaglioAllegatoProcediPresenter(EventBus eventBus, MyView view, MyProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		this.dispatcher = dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	@ProxyEvent
	public void onMostraDettaglioAllegatoProcedi(final MostraDettaglioAllegatoProcediEvent event) {
		DettaglioPraticaProcediCommand dppc = new DettaglioPraticaProcediCommand(this);
		dppc.setId(event.getIdPraticaProcedi());
		dppc.setClassName(RicercaLiberaPresenter.class.getName());
		getView().setIndietroCommand(dppc);

		ShowAppLoadingEvent.fire(DettaglioAllegatoProcediPresenter.this, true);
		dispatcher.execute(new DettaglioAllegatoFirmatoAction(event.getAllegatoProcedi().getIdAlfresco()),
				new AsyncCallback<DettaglioAllegatoFirmatoResult>() {
					@Override
					public void onFailure(Throwable caught) {
						getView().mostraAllegato(event.getAllegatoProcedi(), "Non &egrave; possibile verificare la firma");
						ShowAppLoadingEvent.fire(DettaglioAllegatoProcediPresenter.this, false);
					}

					@Override
					public void onSuccess(DettaglioAllegatoFirmatoResult result) {
						if (result.isError()) {
							getView().mostraAllegato(event.getAllegatoProcedi(), result.getMsgError());
							
						} else {
							getView().mostraAllegato(event.getAllegatoProcedi(), result.getFirmaDigitale());
						}
						
						ShowAppLoadingEvent.fire(DettaglioAllegatoProcediPresenter.this, false);
					}
				});
		revealInParent();
	}

}
