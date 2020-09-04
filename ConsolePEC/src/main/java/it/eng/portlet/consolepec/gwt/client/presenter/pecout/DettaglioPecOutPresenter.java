package it.eng.portlet.consolepec.gwt.client.presenter.pecout;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmailOutLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.pecout.event.MostraDettaglioBozzaEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.pecout.event.MostraDettaglioPecOutInviataEvent;
import it.eng.portlet.consolepec.gwt.client.util.GestioneLinkSiteMapUtil;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO.StatoDTO;

public class DettaglioPecOutPresenter extends Presenter<DettaglioPecOutPresenter.MyView, DettaglioPecOutPresenter.MyProxy> {

	public interface MyView extends View {
		//
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.dettagliopecout)
	public interface MyProxy extends ProxyPlace<DettaglioPecOutPresenter> {
		//
	}

	private String idPratica;
	private String onChiudiToken;
	PecInPraticheDB pecInDb;
	private final EventBus eventBus;
	private final SitemapMenu siteMap;
	private GestioneLinkSiteMapUtil gestioneLinkSiteMapUtil;
	private boolean interoperabile;

	@Inject
	public DettaglioPecOutPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final PecInPraticheDB pecInDb, final SitemapMenu siteMap,
			final GestioneLinkSiteMapUtil gestioneLinkSiteMapUtil) {
		super(eventBus, view, proxy);
		this.eventBus = eventBus;
		this.pecInDb = pecInDb;
		this.siteMap = siteMap;
		this.gestioneLinkSiteMapUtil = gestioneLinkSiteMapUtil;
	}

	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_SetDettaglio = new Type<RevealContentHandler<?>>();

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();

	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		idPratica = request.getParameter(NameTokensParams.idPratica, null);
		onChiudiToken = request.getParameter(NameTokensParams.idOnChiudiDettaglioToken, null);
		interoperabile = request.getParameter(NameTokensParams.interoperabile, null) != null;
		if (isVisible())
			caricaPecOut();
	}

	private void caricaPecOut() {
		pecInDb.getPecOutByPath(idPratica, siteMap.containsLink(idPratica), new PraticaEmailOutLoaded() {

			@Override
			public void onPraticaLoaded(final PecOutDTO pecOutDto) {
				gestioneLinkSiteMapUtil.aggiungiLinkInLavorazione(pecOutDto);
				if (pecOutDto.getStato().compareTo(StatoDTO.BOZZA) == 0) {
					MostraDettaglioBozzaEvent event = new MostraDettaglioBozzaEvent();
					event.setIdPratica(pecOutDto.getClientID());
					event.setOnChiudiToken(onChiudiToken);
					event.setInteroperabile(interoperabile);
					eventBus.fireEvent(event);
				} else {
					MostraDettaglioPecOutInviataEvent event = new MostraDettaglioPecOutInviataEvent();
					event.setIdPratica(pecOutDto.getClientID());
					event.setOnChiudiToken(onChiudiToken);
					eventBus.fireEvent(event);
				}

			}

			@Override
			public void onPraticaError(String error) {
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				eventBus.fireEvent(event);
			}
		});
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		caricaPecOut();
	}

	@Override
	protected void onHide() {
		super.onHide();
		setInSlot(TYPE_SetDettaglio, null);

	}
}
