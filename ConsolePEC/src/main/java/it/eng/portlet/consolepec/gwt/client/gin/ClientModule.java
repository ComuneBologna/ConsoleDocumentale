package it.eng.portlet.consolepec.gwt.client.gin;

import com.google.inject.Singleton;
import com.gwtplatform.common.client.CommonGinModule;
import com.gwtplatform.dispatch.client.DefaultExceptionHandler;
import com.gwtplatform.dispatch.client.DefaultSecurityCookieAccessor;
import com.gwtplatform.dispatch.client.ExceptionHandler;
import com.gwtplatform.dispatch.client.actionhandler.caching.Cache;
import com.gwtplatform.dispatch.client.actionhandler.caching.DefaultCacheImpl;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.dispatch.shared.SecurityCookieAccessor;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler;
import it.eng.cobo.consolepec.security.handler.SimpleAutorizzazioneHandler;
import it.eng.portlet.consolepec.gwt.client.angular.AngularPresenter;
import it.eng.portlet.consolepec.gwt.client.angular.AngularView;
import it.eng.portlet.consolepec.gwt.client.command.RicercaPraticheServerAdapter;
import it.eng.portlet.consolepec.gwt.client.composizione.RicercaComposizioneFascicoloHandler;
import it.eng.portlet.consolepec.gwt.client.composizione.RicercaComposizioneFascicoloHandlerImpl;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.drive.DriveCartellaPresenter;
import it.eng.portlet.consolepec.gwt.client.drive.DriveCartellaView;
import it.eng.portlet.consolepec.gwt.client.drive.DriveDetailPresenter;
import it.eng.portlet.consolepec.gwt.client.drive.DriveDetailView;
import it.eng.portlet.consolepec.gwt.client.drive.DriveFilePresenter;
import it.eng.portlet.consolepec.gwt.client.drive.DriveFileView;
import it.eng.portlet.consolepec.gwt.client.drive.DrivePresenter;
import it.eng.portlet.consolepec.gwt.client.drive.DriveView;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.AnagrafichePraticheHandler;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.AnagraficheRuoliHandler;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ProprietaGeneraliHandler;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.SettoriHandler;
import it.eng.portlet.consolepec.gwt.client.handler.drive.DizionariHandler;
import it.eng.portlet.consolepec.gwt.client.handler.drive.DriveHandler;
import it.eng.portlet.consolepec.gwt.client.handler.drive.NomenclatureHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.AbilitazioniUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.DatiUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.PreferenzeUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.WorklistHandler;
import it.eng.portlet.consolepec.gwt.client.listener.SessionTimeoutListener;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAbilitazioniVisibilitaApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAnagraficaFascicoloApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAnagraficaIngressoApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAnagraficaRuoliApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.impl.AmministrazioneAbilitazioniVisibilitaApiClientImpl;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.impl.AmministrazioneAnagraficaFascicoloApiClientImpl;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.impl.AmministrazioneAnagraficaIngressoApiClientImpl;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.impl.AmministrazioneAnagraficaRuoliApiClientImpl;
import it.eng.portlet.consolepec.gwt.client.operazioni.cartellafirma.CartellaFirmaRicercaApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.cartellafirma.CartellaFirmaWizardApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.cartellafirma.impl.CartellaFirmaRicercaApiClientImpl;
import it.eng.portlet.consolepec.gwt.client.operazioni.cartellafirma.impl.CartellaFirmaWizardApiClientImpl;
import it.eng.portlet.consolepec.gwt.client.operazioni.pec.PecApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.pec.impl.PecApiClientImpl;
import it.eng.portlet.consolepec.gwt.client.operazioni.template.TemplateCreazioneApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.template.TemplateSceltaWizardApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.template.impl.TemplateCreazioneApiClientImpl;
import it.eng.portlet.consolepec.gwt.client.operazioni.template.impl.TemplateSceltaWizardApiClientImpl;
import it.eng.portlet.consolepec.gwt.client.place.ClientPlaceManager;
import it.eng.portlet.consolepec.gwt.client.place.DefaultPlace;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.presenter.AppLoadingPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.DettaglioAllegatoPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.FirmaAllegatoPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.GruppiPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.IndirizziEmailHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.OperatorePresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.RestLoginHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.RicercaLiberaPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.cartellafirma.NotificaTaskFirmaPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.cartellafirma.StepOperazioneWizardTaskFirmaPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.cartellafirma.WorklistCartellaFirmaPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.configurazioni.DettaglioAbilitazionePresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.configurazioni.DettaglioAnagraficaFascicoloPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.configurazioni.DettaglioAnagraficaGruppiPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.configurazioni.DettaglioAnagraficaIngressiPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.configurazioni.ListaAbilitazioniPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.configurazioni.ListaAnagraficaFascicoloPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.configurazioni.ListaAnagraficaGruppiPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.configurazioni.ListaAnagraficaIngressiPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.AssegnaUtenteEsternoPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.CollegaFascicoloPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.CondividiFascicoloPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.ConfermaMailDaTemplatePresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.CreaFascicoloFormPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.DettaglioFascicoloGenericoPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.ImportaAllegatiEmailPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.ModificaAbilitazioniAssegnaUtenteEsternoPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.ModificaFascicoloPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.ModificaTipologieAllegatoPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.PubblicazioneAllegatiPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.SceltaFascicoloPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.SceltaProtocollazionePresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.WorklistFascicoliGenericoPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.amianto.EstrazioniAmiantoPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.CreaComunicazionePresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.DettaglioComunicazionePresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.InviaCsvTestComunicazionePresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.WorklistComunicazionePresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.modulistica.DettaglioPraticaModulisticaPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.modulistica.WorklistPraticaModulisticaPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.newsitemap.SiteMapConfiguraSiteMap;
import it.eng.portlet.consolepec.gwt.client.presenter.newsitemap.SiteMapHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.newsitemap.SiteMapInitializer;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.CaricaAllegatiDaPraticaPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.DettaglioPecInPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.WorklistPecInPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.pecout.DettaglioPecOutBozzaPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.pecout.DettaglioPecOutInviatoPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.pecout.DettaglioPecOutPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.pecout.InvioMailDaCSVPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.pratica.DettaglioRidottoPraticaPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.AvvioChiusuraProcedimentiPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.ConfermaSceltaProtocollazionePresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.EsitoProtocollazionePresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.FormProtocollazionePresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.RiepilogoAvvioChiusuraProcedimentoPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.SceltaCapofilaPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.richiedifirma.RichiediVistoFirmaPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.rubrica.CreaAnagraficaPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.rubrica.DettaglioAnagraficaPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.rubrica.ListaAnagrafichePresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.template.CompilaCampiTemplatePresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.template.CreaTemplateFormPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.template.SceltaTemplatePresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.template.WorklistTemplatePresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.template.dettaglio.DettaglioTemplatePresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.urbanistica.DettaglioAllegatoProcediPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.urbanistica.DettaglioPraticaProcediPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.urbanistica.ListaPraticaProcediPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.visibilita.ModificaVisibilitaPresenter;
import it.eng.portlet.consolepec.gwt.client.rpc.IONOIDispatchAsync;
import it.eng.portlet.consolepec.gwt.client.tasks.gestionefascicolo.operazioni.richiedifirma.RichiediFirmaTaskApiClient;
import it.eng.portlet.consolepec.gwt.client.tasks.gestionefascicolo.operazioni.richiedifirma.impl.RichiediFirmaTaskApiClientImpl;
import it.eng.portlet.consolepec.gwt.client.util.DatiDefaultProtocollazioneHandler;
import it.eng.portlet.consolepec.gwt.client.util.GestioneLinkSiteMapUtil;
import it.eng.portlet.consolepec.gwt.client.util.GestioneLinkSiteMapUtilImpl;
import it.eng.portlet.consolepec.gwt.client.view.AppLoadingView;
import it.eng.portlet.consolepec.gwt.client.view.DettaglioAllegatoView;
import it.eng.portlet.consolepec.gwt.client.view.FirmaAllegatoView;
import it.eng.portlet.consolepec.gwt.client.view.GruppiView;
import it.eng.portlet.consolepec.gwt.client.view.MainView;
import it.eng.portlet.consolepec.gwt.client.view.OperatoreView;
import it.eng.portlet.consolepec.gwt.client.view.RicercaLiberaView;
import it.eng.portlet.consolepec.gwt.client.view.cartellafirma.NotificaTaskFirmaView;
import it.eng.portlet.consolepec.gwt.client.view.cartellafirma.StepOperazioneWizardTaskFirmaView;
import it.eng.portlet.consolepec.gwt.client.view.cartellafirma.WorklistCartellaFirmaView;
import it.eng.portlet.consolepec.gwt.client.view.configurazioni.DettaglioAbilitazioneView;
import it.eng.portlet.consolepec.gwt.client.view.configurazioni.DettaglioAnagraficaFascicoloView;
import it.eng.portlet.consolepec.gwt.client.view.configurazioni.DettaglioAnagraficaGruppiView;
import it.eng.portlet.consolepec.gwt.client.view.configurazioni.DettaglioAnagraficaIngressiView;
import it.eng.portlet.consolepec.gwt.client.view.configurazioni.ListaAbilitazioniView;
import it.eng.portlet.consolepec.gwt.client.view.configurazioni.ListaAnagraficaFascicoloView;
import it.eng.portlet.consolepec.gwt.client.view.configurazioni.ListaAnagraficaGruppiView;
import it.eng.portlet.consolepec.gwt.client.view.configurazioni.ListaAnagraficaIngressiView;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.AssegnaUtenteEsternoView;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.CollegaFascicoloView;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.CondividiFascicoloView;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.ConfermaMailDaTemplateView;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.CreaFascicoloFormView;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.DettaglioFascicoloGenericoView;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.ImportaAllegatiEmailView;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.ModificaAbilitazioniAssegnaUtenteEsternoView;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.ModificaFascicoloView;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.ModificaTipologieAllegatoView;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.PubblicazioneAllegatiView;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.SceltaFascicoloView;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.SceltaProtocollazioneView;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.WorklistFascicoliGenericoView;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.amianto.EstrazioniAmiantoView;
import it.eng.portlet.consolepec.gwt.client.view.inviomassivo.CreaComunicazioneView;
import it.eng.portlet.consolepec.gwt.client.view.inviomassivo.DettaglioComunicazioneView;
import it.eng.portlet.consolepec.gwt.client.view.inviomassivo.InviaCsvTestComunicazioneView;
import it.eng.portlet.consolepec.gwt.client.view.inviomassivo.WorklistComunicazioneView;
import it.eng.portlet.consolepec.gwt.client.view.modulistica.DettaglioPraticaModulisticaView;
import it.eng.portlet.consolepec.gwt.client.view.modulistica.WorklistPraticaModulisticaView;
import it.eng.portlet.consolepec.gwt.client.view.pec.CaricaAllegatiDaPraticaView;
import it.eng.portlet.consolepec.gwt.client.view.pec.DettaglioPecInView;
import it.eng.portlet.consolepec.gwt.client.view.pec.WorklistPecInView;
import it.eng.portlet.consolepec.gwt.client.view.pecout.DettaglioPecOutBozzaView;
import it.eng.portlet.consolepec.gwt.client.view.pecout.DettaglioPecOutInviatoView;
import it.eng.portlet.consolepec.gwt.client.view.pecout.DettaglioPecOutView;
import it.eng.portlet.consolepec.gwt.client.view.pecout.InvioMailDaCSVView;
import it.eng.portlet.consolepec.gwt.client.view.pratica.DettaglioRidottoPraticaView;
import it.eng.portlet.consolepec.gwt.client.view.protocollazione.AvvioChiusuraProcedimentiView;
import it.eng.portlet.consolepec.gwt.client.view.protocollazione.ConfermaSceltaProtocollazioneView;
import it.eng.portlet.consolepec.gwt.client.view.protocollazione.EsitoProtocollazioneView;
import it.eng.portlet.consolepec.gwt.client.view.protocollazione.FormProtocollazioneView;
import it.eng.portlet.consolepec.gwt.client.view.protocollazione.RiepilogoAvvioChiusuraProcedimentoView;
import it.eng.portlet.consolepec.gwt.client.view.protocollazione.SceltaCapofilaView;
import it.eng.portlet.consolepec.gwt.client.view.richiedifirma.RichiediVistoFirmaView;
import it.eng.portlet.consolepec.gwt.client.view.rubrica.CreaAnagraficaView;
import it.eng.portlet.consolepec.gwt.client.view.rubrica.DettaglioAnagraficaView;
import it.eng.portlet.consolepec.gwt.client.view.rubrica.ListaAnagraficheView;
import it.eng.portlet.consolepec.gwt.client.view.template.CompilaCampiTemplateView;
import it.eng.portlet.consolepec.gwt.client.view.template.CreaTemplateFormView;
import it.eng.portlet.consolepec.gwt.client.view.template.SceltaTemplateView;
import it.eng.portlet.consolepec.gwt.client.view.template.WorklistTemplateView;
import it.eng.portlet.consolepec.gwt.client.view.template.dettaglio.DettaglioTemplateView;
import it.eng.portlet.consolepec.gwt.client.view.urbanistica.DettaglioAllegatoProcediView;
import it.eng.portlet.consolepec.gwt.client.view.urbanistica.DettaglioPraticaProcediView;
import it.eng.portlet.consolepec.gwt.client.view.urbanistica.ListaPraticaProcediView;
import it.eng.portlet.consolepec.gwt.client.view.visibilita.ModificaVisibilitaView;
import it.eng.portlet.consolepec.gwt.client.widget.GroupSuggestBoxProtocollazione;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.UtentiSuggestOracle;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.UtentiSuggestOracleImpl;

public class ClientModule extends AbstractPresenterModule {

	@Override
	protected void configure() {

		bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.worklistcartellafirma);

		install(new CommonGinModule());

		install(new HireDefaultModule(ClientPlaceManager.class));

		bind(DispatchAsync.class).to(IONOIDispatchAsync.class).in(Singleton.class);

		bind(SecurityCookieAccessor.class).to(DefaultSecurityCookieAccessor.class).in(Singleton.class);

		bind(ExceptionHandler.class).to(DefaultExceptionHandler.class).in(Singleton.class);

		bind(PecInPraticheDB.class).in(Singleton.class);

		bind(DatiDefaultProtocollazioneHandler.class).in(Singleton.class);

		bind(SiteMapConfiguraSiteMap.class).in(Singleton.class);

		bind(SiteMapHandler.class).in(Singleton.class);

		bind(SiteMapInitializer.class).in(Singleton.class);

		bind(SitemapMenu.class).in(Singleton.class);

		bind(Cache.class).to(DefaultCacheImpl.class).in(Singleton.class);

		bind(GestioneLinkSiteMapUtil.class).to(GestioneLinkSiteMapUtilImpl.class).in(Singleton.class);

		bind(GroupSuggestBoxProtocollazione.class).in(Singleton.class);

		bind(RicercaPraticheServerAdapter.class).in(Singleton.class);

		bind(SessionTimeoutListener.class).asEagerSingleton();

		bind(RichiediFirmaTaskApiClient.class).to(RichiediFirmaTaskApiClientImpl.class).in(Singleton.class);

		bind(CartellaFirmaRicercaApiClient.class).to(CartellaFirmaRicercaApiClientImpl.class).in(Singleton.class);

		bind(CartellaFirmaWizardApiClient.class).to(CartellaFirmaWizardApiClientImpl.class).in(Singleton.class);

		bind(UtentiSuggestOracle.class).to(UtentiSuggestOracleImpl.class);

		bind(TemplateSceltaWizardApiClient.class).to(TemplateSceltaWizardApiClientImpl.class).in(Singleton.class);

		bind(TemplateCreazioneApiClient.class).to(TemplateCreazioneApiClientImpl.class).in(Singleton.class);

		bind(PecApiClient.class).to(PecApiClientImpl.class).in(Singleton.class);

		bind(AutorizzazioneHandler.class).to(SimpleAutorizzazioneHandler.class).in(Singleton.class);

		bind(AmministrazioneAnagraficaRuoliApiClient.class).to(AmministrazioneAnagraficaRuoliApiClientImpl.class).in(Singleton.class);

		bind(AmministrazioneAnagraficaIngressoApiClient.class).to(AmministrazioneAnagraficaIngressoApiClientImpl.class).in(Singleton.class);

		bind(AmministrazioneAnagraficaFascicoloApiClient.class).to(AmministrazioneAnagraficaFascicoloApiClientImpl.class).in(Singleton.class);

		bind(AmministrazioneAbilitazioniVisibilitaApiClient.class).to(AmministrazioneAbilitazioniVisibilitaApiClientImpl.class).in(Singleton.class);

		/*
		 * Handler
		 */
		bind(IndirizziEmailHandler.class).in(Singleton.class);
		bind(RestLoginHandler.class).in(Singleton.class);

		// Handler - profilazione
		bind(ProfilazioneUtenteHandler.class).in(Singleton.class);
		bind(DatiUtenteHandler.class).in(Singleton.class);
		bind(PreferenzeUtenteHandler.class).in(Singleton.class);
		bind(AbilitazioniUtenteHandler.class).in(Singleton.class);
		bind(WorklistHandler.class).in(Singleton.class);

		// Handler - configurazioni
		bind(ConfigurazioniHandler.class).in(Singleton.class);
		bind(ProprietaGeneraliHandler.class).in(Singleton.class);
		bind(AnagrafichePraticheHandler.class).in(Singleton.class);
		bind(AnagraficheRuoliHandler.class).in(Singleton.class);
		bind(SettoriHandler.class).in(Singleton.class);

		// Drive
		bind(DriveHandler.class).in(Singleton.class);
		bind(DizionariHandler.class).in(Singleton.class);
		bind(NomenclatureHandler.class).in(Singleton.class);

		bind(RicercaComposizioneFascicoloHandler.class).to(RicercaComposizioneFascicoloHandlerImpl.class).in(Singleton.class);

		/*
		 * Presenter
		 */
		bindPresenter(MainPresenter.class, MainPresenter.MyView.class, MainView.class, MainPresenter.MyProxy.class);

		bindPresenter(DettaglioPecInPresenter.class, DettaglioPecInPresenter.MyView.class, DettaglioPecInView.class, DettaglioPecInPresenter.MyProxy.class);

		bindPresenter(FirmaAllegatoPresenter.class, FirmaAllegatoPresenter.MyView.class, FirmaAllegatoView.class, FirmaAllegatoPresenter.MyProxy.class);

		bindPresenter(FormProtocollazionePresenter.class, FormProtocollazionePresenter.MyView.class, FormProtocollazioneView.class, FormProtocollazionePresenter.MyProxy.class);

		bindPresenter(DettaglioPecOutBozzaPresenter.class, DettaglioPecOutBozzaPresenter.MyView.class, DettaglioPecOutBozzaView.class, DettaglioPecOutBozzaPresenter.MyProxy.class);

		bindPresenter(CreaFascicoloFormPresenter.class, CreaFascicoloFormPresenter.MyView.class, CreaFascicoloFormView.class, CreaFascicoloFormPresenter.MyProxy.class);

		bindPresenter(ConfermaSceltaProtocollazionePresenter.class, ConfermaSceltaProtocollazionePresenter.MyView.class, ConfermaSceltaProtocollazioneView.class,
				ConfermaSceltaProtocollazionePresenter.MyProxy.class);

		bindPresenter(AppLoadingPresenter.class, AppLoadingPresenter.MyView.class, AppLoadingView.class, AppLoadingPresenter.MyProxy.class);

		bindPresenter(GruppiPresenter.class, GruppiPresenter.MyView.class, GruppiView.class, GruppiPresenter.MyProxy.class);

		bindPresenter(EsitoProtocollazionePresenter.class, EsitoProtocollazionePresenter.MyView.class, EsitoProtocollazioneView.class, EsitoProtocollazionePresenter.MyProxy.class);

		bindPresenter(DettaglioAllegatoPresenter.class, DettaglioAllegatoPresenter.MyView.class, DettaglioAllegatoView.class, DettaglioAllegatoPresenter.MyProxy.class);

		bindPresenter(CaricaAllegatiDaPraticaPresenter.class, CaricaAllegatiDaPraticaPresenter.MyView.class, CaricaAllegatiDaPraticaView.class, CaricaAllegatiDaPraticaPresenter.MyProxy.class);

		bindPresenter(DettaglioPecOutPresenter.class, DettaglioPecOutPresenter.MyView.class, DettaglioPecOutView.class, DettaglioPecOutPresenter.MyProxy.class);

		bindPresenter(DettaglioPecOutInviatoPresenter.class, DettaglioPecOutInviatoPresenter.MyView.class, DettaglioPecOutInviatoView.class, DettaglioPecOutInviatoPresenter.MyProxy.class);

		bindPresenter(SceltaCapofilaPresenter.class, SceltaCapofilaPresenter.MyView.class, SceltaCapofilaView.class, SceltaCapofilaPresenter.MyProxy.class);

		bindPresenter(SceltaFascicoloPresenter.class, SceltaFascicoloPresenter.MyView.class, SceltaFascicoloView.class, SceltaFascicoloPresenter.MyProxy.class);

		bindPresenter(WorklistPecInPresenter.class, WorklistPecInPresenter.MyView.class, WorklistPecInView.class, WorklistPecInPresenter.MyProxy.class);

		bindPresenter(RicercaLiberaPresenter.class, RicercaLiberaPresenter.MyView.class, RicercaLiberaView.class, RicercaLiberaPresenter.MyProxy.class);

		bindPresenter(ImportaAllegatiEmailPresenter.class, ImportaAllegatiEmailPresenter.MyView.class, ImportaAllegatiEmailView.class, ImportaAllegatiEmailPresenter.MyProxy.class);

		bindPresenter(DettaglioFascicoloGenericoPresenter.class, DettaglioFascicoloGenericoPresenter.MyView.class, DettaglioFascicoloGenericoView.class,
				DettaglioFascicoloGenericoPresenter.MyProxy.class);

		bindPresenter(WorklistFascicoliGenericoPresenter.class, WorklistFascicoliGenericoPresenter.MyView.class, WorklistFascicoliGenericoView.class, WorklistFascicoliGenericoPresenter.MyProxy.class);

		bindPresenter(PubblicazioneAllegatiPresenter.class, PubblicazioneAllegatiPresenter.MyView.class, PubblicazioneAllegatiView.class, PubblicazioneAllegatiPresenter.MyProxy.class);

		bindPresenter(ModificaVisibilitaPresenter.class, ModificaVisibilitaPresenter.MyView.class, ModificaVisibilitaView.class, ModificaVisibilitaPresenter.MyProxy.class);

		bindPresenter(CollegaFascicoloPresenter.class, CollegaFascicoloPresenter.MyView.class, CollegaFascicoloView.class, CollegaFascicoloPresenter.MyProxy.class);

		bindPresenter(CondividiFascicoloPresenter.class, CondividiFascicoloPresenter.MyView.class, CondividiFascicoloView.class, CondividiFascicoloPresenter.MyProxy.class);

		bindPresenter(DettaglioPraticaModulisticaPresenter.class, DettaglioPraticaModulisticaPresenter.MyView.class, DettaglioPraticaModulisticaView.class,
				DettaglioPraticaModulisticaPresenter.MyProxy.class);

		bindPresenter(WorklistPraticaModulisticaPresenter.class, WorklistPraticaModulisticaPresenter.MyView.class, WorklistPraticaModulisticaView.class,
				WorklistPraticaModulisticaPresenter.MyProxy.class);

		bindPresenter(AvvioChiusuraProcedimentiPresenter.class, AvvioChiusuraProcedimentiPresenter.MyView.class, AvvioChiusuraProcedimentiView.class, AvvioChiusuraProcedimentiPresenter.MyProxy.class);

		bindPresenter(RiepilogoAvvioChiusuraProcedimentoPresenter.class, RiepilogoAvvioChiusuraProcedimentoPresenter.MyView.class, RiepilogoAvvioChiusuraProcedimentoView.class,
				RiepilogoAvvioChiusuraProcedimentoPresenter.MyProxy.class);

		bindPresenter(CreaTemplateFormPresenter.class, CreaTemplateFormPresenter.MyView.class, CreaTemplateFormView.class, CreaTemplateFormPresenter.MyProxy.class);

		bindPresenter(DettaglioTemplatePresenter.class, DettaglioTemplatePresenter.MyView.class, DettaglioTemplateView.class, DettaglioTemplatePresenter.MyProxy.class);

		bindPresenter(WorklistTemplatePresenter.class, WorklistTemplatePresenter.MyView.class, WorklistTemplateView.class, WorklistTemplatePresenter.MyProxy.class);

		bindPresenter(SceltaTemplatePresenter.class, SceltaTemplatePresenter.MyView.class, SceltaTemplateView.class, SceltaTemplatePresenter.MyProxy.class);

		bindPresenter(CompilaCampiTemplatePresenter.class, CompilaCampiTemplatePresenter.MyView.class, CompilaCampiTemplateView.class, CompilaCampiTemplatePresenter.MyProxy.class);

		bindPresenter(EstrazioniAmiantoPresenter.class, EstrazioniAmiantoPresenter.MyView.class, EstrazioniAmiantoView.class, EstrazioniAmiantoPresenter.MyProxy.class);

		bindPresenter(AssegnaUtenteEsternoPresenter.class, AssegnaUtenteEsternoPresenter.MyView.class, AssegnaUtenteEsternoView.class, AssegnaUtenteEsternoPresenter.MyProxy.class);

		bindPresenter(ModificaAbilitazioniAssegnaUtenteEsternoPresenter.class, ModificaAbilitazioniAssegnaUtenteEsternoPresenter.MyView.class, ModificaAbilitazioniAssegnaUtenteEsternoView.class,
				ModificaAbilitazioniAssegnaUtenteEsternoPresenter.MyProxy.class);

		bindPresenter(WorklistComunicazionePresenter.class, WorklistComunicazionePresenter.MyView.class, WorklistComunicazioneView.class, WorklistComunicazionePresenter.MyProxy.class);

		bindPresenter(DettaglioComunicazionePresenter.class, DettaglioComunicazionePresenter.MyView.class, DettaglioComunicazioneView.class, DettaglioComunicazionePresenter.MyProxy.class);

		bindPresenter(DettaglioRidottoPraticaPresenter.class, DettaglioRidottoPraticaPresenter.MyView.class, DettaglioRidottoPraticaView.class, DettaglioRidottoPraticaPresenter.MyProxy.class);

		bindPresenter(CreaComunicazionePresenter.class, CreaComunicazionePresenter.MyView.class, CreaComunicazioneView.class, CreaComunicazionePresenter.MyProxy.class);

		bindPresenter(InviaCsvTestComunicazionePresenter.class, InviaCsvTestComunicazionePresenter.MyView.class, InviaCsvTestComunicazioneView.class, InviaCsvTestComunicazionePresenter.MyProxy.class);

		bindPresenter(OperatorePresenter.class, OperatorePresenter.MyView.class, OperatoreView.class, OperatorePresenter.MyProxy.class);

		bindPresenter(ConfermaMailDaTemplatePresenter.class, ConfermaMailDaTemplatePresenter.MyView.class, ConfermaMailDaTemplateView.class, ConfermaMailDaTemplatePresenter.MyProxy.class);

		bindPresenter(RichiediVistoFirmaPresenter.class, RichiediVistoFirmaPresenter.MyView.class, RichiediVistoFirmaView.class, RichiediVistoFirmaPresenter.MyProxy.class);

		bindPresenter(WorklistCartellaFirmaPresenter.class, WorklistCartellaFirmaPresenter.MyView.class, WorklistCartellaFirmaView.class, WorklistCartellaFirmaPresenter.MyProxy.class);

		bindPresenter(StepOperazioneWizardTaskFirmaPresenter.class, StepOperazioneWizardTaskFirmaPresenter.MyView.class, StepOperazioneWizardTaskFirmaView.class,
				StepOperazioneWizardTaskFirmaPresenter.MyProxy.class);

		bindPresenter(NotificaTaskFirmaPresenter.class, NotificaTaskFirmaPresenter.MyView.class, NotificaTaskFirmaView.class, NotificaTaskFirmaPresenter.MyProxy.class);

		bindPresenter(SceltaProtocollazionePresenter.class, SceltaProtocollazionePresenter.MyView.class, SceltaProtocollazioneView.class, SceltaProtocollazionePresenter.MyProxy.class);

		bindPresenter(ModificaFascicoloPresenter.class, ModificaFascicoloPresenter.MyView.class, ModificaFascicoloView.class, ModificaFascicoloPresenter.MyProxy.class);

		bindPresenter(ListaAnagrafichePresenter.class, ListaAnagrafichePresenter.MyView.class, ListaAnagraficheView.class, ListaAnagrafichePresenter.MyProxy.class);

		bindPresenter(DettaglioAnagraficaPresenter.class, DettaglioAnagraficaPresenter.MyView.class, DettaglioAnagraficaView.class, DettaglioAnagraficaPresenter.MyProxy.class);

		bindPresenter(CreaAnagraficaPresenter.class, CreaAnagraficaPresenter.MyView.class, CreaAnagraficaView.class, CreaAnagraficaPresenter.MyProxy.class);

		bindPresenter(DettaglioPraticaProcediPresenter.class, DettaglioPraticaProcediPresenter.MyView.class, DettaglioPraticaProcediView.class, DettaglioPraticaProcediPresenter.MyProxy.class);

		bindPresenter(ListaPraticaProcediPresenter.class, ListaPraticaProcediPresenter.MyView.class, ListaPraticaProcediView.class, ListaPraticaProcediPresenter.MyProxy.class);

		bindPresenter(ListaAnagraficaFascicoloPresenter.class, ListaAnagraficaFascicoloPresenter.MyView.class, ListaAnagraficaFascicoloView.class, ListaAnagraficaFascicoloPresenter.MyProxy.class);

		bindPresenter(DettaglioAllegatoProcediPresenter.class, DettaglioAllegatoProcediPresenter.MyView.class, DettaglioAllegatoProcediView.class, DettaglioAllegatoProcediPresenter.MyProxy.class);

		bindPresenter(DettaglioAnagraficaFascicoloPresenter.class, DettaglioAnagraficaFascicoloPresenter.MyView.class, DettaglioAnagraficaFascicoloView.class,
				DettaglioAnagraficaFascicoloPresenter.MyProxy.class);

		bindPresenter(ListaAnagraficaGruppiPresenter.class, ListaAnagraficaGruppiPresenter.MyView.class, ListaAnagraficaGruppiView.class, ListaAnagraficaGruppiPresenter.MyProxy.class);

		bindPresenter(DettaglioAnagraficaGruppiPresenter.class, DettaglioAnagraficaGruppiPresenter.MyView.class, DettaglioAnagraficaGruppiView.class, DettaglioAnagraficaGruppiPresenter.MyProxy.class);

		bindPresenter(ListaAnagraficaIngressiPresenter.class, ListaAnagraficaIngressiPresenter.MyView.class, ListaAnagraficaIngressiView.class, ListaAnagraficaIngressiPresenter.MyProxy.class);

		bindPresenter(DettaglioAnagraficaIngressiPresenter.class, DettaglioAnagraficaIngressiPresenter.MyView.class, DettaglioAnagraficaIngressiView.class,
				DettaglioAnagraficaIngressiPresenter.MyProxy.class);

		bindPresenter(ListaAbilitazioniPresenter.class, ListaAbilitazioniPresenter.MyView.class, ListaAbilitazioniView.class, ListaAbilitazioniPresenter.MyProxy.class);

		bindPresenter(DettaglioAbilitazionePresenter.class, DettaglioAbilitazionePresenter.MyView.class, DettaglioAbilitazioneView.class, DettaglioAbilitazionePresenter.MyProxy.class);

		bindPresenter(ModificaTipologieAllegatoPresenter.class, ModificaTipologieAllegatoPresenter.MyView.class, ModificaTipologieAllegatoView.class, ModificaTipologieAllegatoPresenter.MyProxy.class);

		bindPresenter(AngularPresenter.class, AngularPresenter.MyView.class, AngularView.class, AngularPresenter.MyProxy.class);

		bindPresenter(DrivePresenter.class, DrivePresenter.MyView.class, DriveView.class, DrivePresenter.MyProxy.class);

		bindPresenter(DriveDetailPresenter.class, DriveDetailPresenter.MyView.class, DriveDetailView.class, DriveDetailPresenter.MyProxy.class);

		bindPresenter(DriveCartellaPresenter.class, DriveCartellaPresenter.MyView.class, DriveCartellaView.class, DriveCartellaPresenter.MyProxy.class);

		bindPresenter(DriveFilePresenter.class, DriveFilePresenter.MyView.class, DriveFileView.class, DriveFilePresenter.MyProxy.class);

		bindPresenter(InvioMailDaCSVPresenter.class, InvioMailDaCSVPresenter.MyView.class, InvioMailDaCSVView.class, InvioMailDaCSVPresenter.MyProxy.class);

	}
}
