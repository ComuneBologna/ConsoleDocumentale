package it.eng.portlet.consolepec.gwt.client.gin;

import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler;
import it.eng.portlet.consolepec.gwt.client.actionhandler.GetDatiAssegnaEsternoClientActionHandler;
import it.eng.portlet.consolepec.gwt.client.actionhandler.GruppiVisibilitaClientActionHandler;
import it.eng.portlet.consolepec.gwt.client.actionhandler.MatriceVisibilitaPraticaActionHandler;
import it.eng.portlet.consolepec.gwt.client.angular.AngularPresenter;
import it.eng.portlet.consolepec.gwt.client.composizione.RicercaComposizioneFascicoloHandler;
import it.eng.portlet.consolepec.gwt.client.drive.DriveCartellaPresenter;
import it.eng.portlet.consolepec.gwt.client.drive.DriveDetailPresenter;
import it.eng.portlet.consolepec.gwt.client.drive.DriveFilePresenter;
import it.eng.portlet.consolepec.gwt.client.drive.DrivePresenter;
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
import it.eng.portlet.consolepec.gwt.client.util.GestioneLinkSiteMapUtil;

@GinModules({ ClientModule.class })
public interface ClientGinjector extends Ginjector {

	EventBus getEventBus();

	PlaceManager getPlaceManager();

	GestioneLinkSiteMapUtil getGestioneLinkSiteMapUtil();

	DispatchAsync getDispatchAsync();

	/*
	 * Handler
	 */
	IndirizziEmailHandler getIndirizziEmailHandler();

	RestLoginHandler getRestLoginHandler();

	// Handler profilazione
	AutorizzazioneHandler getAutorizzazioneHandler();

	ProfilazioneUtenteHandler getProfilazioneUtenteHandler();

	DatiUtenteHandler getDatiUtenteHandler();

	PreferenzeUtenteHandler getPreferenzeUtenteHandler();

	AbilitazioniUtenteHandler getAbilitazioniUtenteHandler();

	WorklistHandler getWorklistHandler();

	// Handler configurazioni
	ConfigurazioniHandler getConfigurazioniHandler();

	ProprietaGeneraliHandler getProprietaGeneraliHandler();

	AnagrafichePraticheHandler getAnagrafichePraticheHandler();

	AnagraficheRuoliHandler getAnagraficheRuoliHandler();

	SettoriHandler getSettoriHandler();

	DizionariHandler getDizionariHandler();

	NomenclatureHandler getNomenclatureHandler();

	DriveHandler getDriveHandler();

	RicercaComposizioneFascicoloHandler getRicercaComposizioneFascicoloHandler();

	/*
	 * Presenter
	 */
	AsyncProvider<MainPresenter> getMainPresenter();

	AsyncProvider<DettaglioPecInPresenter> getDettaglioPresenter();

	AsyncProvider<FirmaAllegatoPresenter> getFirmaDocumentoPresenter();

	AsyncProvider<FormProtocollazionePresenter> getFormProtocollazionePresenter();

	AsyncProvider<DettaglioPecOutBozzaPresenter> getRispondiMailPresenter();

	AsyncProvider<CreaFascicoloFormPresenter> getCreaFascicoloFormPresenter();

	AsyncProvider<ConfermaSceltaProtocollazionePresenter> getConfermaSceltaProtocollazionePresenter();

	AsyncProvider<AppLoadingPresenter> getAppLoadingPresenter();

	AsyncProvider<GruppiPresenter> getGruppiPresenter();

	AsyncProvider<EsitoProtocollazionePresenter> getEsitoProtocollazionePresenter();

	AsyncProvider<DettaglioAllegatoPresenter> getDettaglioAllegatoPresenter();

	AsyncProvider<CaricaAllegatiDaPraticaPresenter> getCaricaAllegatiDaPraticaPresenter();

	AsyncProvider<DettaglioPecOutPresenter> getDettaglioPecOutPresenter();

	AsyncProvider<DettaglioPecOutInviatoPresenter> getDettaglioPecOutInviatoPresenter();

	AsyncProvider<SceltaCapofilaPresenter> getSceltaCapofilaPresenter();

	AsyncProvider<SceltaFascicoloPresenter> getSceltaFascicoloPresenter();

	AsyncProvider<WorklistPecInPresenter> getWorklistPecInPresenter();

	AsyncProvider<RicercaLiberaPresenter> getRicercaLiberaPresenter();

	AsyncProvider<ImportaAllegatiEmailPresenter> getImportaAllegatiEmailPresenter();

	AsyncProvider<DettaglioFascicoloGenericoPresenter> getDettaglioFascicoloGenericoPresenter();

	AsyncProvider<WorklistFascicoliGenericoPresenter> getWorklistFascioliGenericoPresenter();

	AsyncProvider<PubblicazioneAllegatiPresenter> getCondivisioneAllegatiPresenter();

	AsyncProvider<ModificaVisibilitaPresenter> getModificaVisibiltaPresenter();

	AsyncProvider<GruppiVisibilitaClientActionHandler> getGruppiVisibilitaClientActionHandler();

	AsyncProvider<GetDatiAssegnaEsternoClientActionHandler> getGetDatiAssegnaEsternoClientActionHandler();

	AsyncProvider<MatriceVisibilitaPraticaActionHandler> getMatriceVisibilitaPraticaActionHandler();

	AsyncProvider<CollegaFascicoloPresenter> getCollegaFascicoloPresenter();

	AsyncProvider<CondividiFascicoloPresenter> getCondividiFascicoloPresenter();

	AsyncProvider<DettaglioPraticaModulisticaPresenter> getDettaglioPraticaModulisticaPresenter();

	AsyncProvider<WorklistPraticaModulisticaPresenter> getWorklistPraticaModulisticaPresenter();

	AsyncProvider<AvvioChiusuraProcedimentiPresenter> getAvvioChiusuraProcedimentiPresenter();

	AsyncProvider<RiepilogoAvvioChiusuraProcedimentoPresenter> getRiepilogoAvvioChiusuraProcedimentoPresenter();

	AsyncProvider<CreaTemplateFormPresenter> getCreaTemplateFormPresenter();

	AsyncProvider<DettaglioTemplatePresenter> getDettaglioTemplatePresenter();

	AsyncProvider<WorklistTemplatePresenter> getWorklistTemplatePresenter();

	AsyncProvider<SceltaTemplatePresenter> getSceltaTemplatePresenter();

	AsyncProvider<CompilaCampiTemplatePresenter> getCompilaCampiTemplatePresenter();

	AsyncProvider<EstrazioniAmiantoPresenter> getEstrazioniAmiantoPresenter();

	AsyncProvider<AssegnaUtenteEsternoPresenter> getAssegnaUtenteEsternoPresenter();

	AsyncProvider<ModificaAbilitazioniAssegnaUtenteEsternoPresenter> getModificaAbilitazioniAssegnaUtenteEsternoPresenter();

	AsyncProvider<DettaglioComunicazionePresenter> getDettaglioComunicazionePresenter();

	AsyncProvider<WorklistComunicazionePresenter> getWorklistComunicazionePresenter();

	AsyncProvider<DettaglioRidottoPraticaPresenter> getDettaglioRidottoPraticaPresenter();

	AsyncProvider<CreaComunicazionePresenter> getCreaComunicazionePresenter();

	AsyncProvider<InviaCsvTestComunicazionePresenter> getInviaCsvTestComunicazionePresenter();

	AsyncProvider<OperatorePresenter> getOperatorePresenter();

	AsyncProvider<ConfermaMailDaTemplatePresenter> getConfermaMailDaTemplatePresenter();

	AsyncProvider<RichiediVistoFirmaPresenter> getRichiediVistoFirmaPresenter();

	AsyncProvider<WorklistCartellaFirmaPresenter> getWorklistCartellaFirmaPresenter();

	AsyncProvider<StepOperazioneWizardTaskFirmaPresenter> getStepOperazioneWizardTaskFirmaPresenter();

	AsyncProvider<NotificaTaskFirmaPresenter> getNotificaTaskFirmaPresenter();

	AsyncProvider<SceltaProtocollazionePresenter> getSceltaProtocollazionePresenter();

	AsyncProvider<ModificaFascicoloPresenter> getModificaFascicoloPresenter();

	AsyncProvider<ListaAnagrafichePresenter> getListaAnagrafichePresenter();

	AsyncProvider<DettaglioAnagraficaPresenter> getDettaglioAnagraficaPresenter();

	AsyncProvider<CreaAnagraficaPresenter> getCreaAnagraficaPresenter();

	AsyncProvider<DettaglioPraticaProcediPresenter> getDettaglioPraticaProcediPresenter();

	AsyncProvider<ListaPraticaProcediPresenter> getListaPraticaProcediPresenter();

	AsyncProvider<DettaglioAllegatoProcediPresenter> getDettaglioAllegatoProcediPresenter();

	AsyncProvider<ListaAnagraficaFascicoloPresenter> getListaAnagraficaFascicoloPresenter();

	AsyncProvider<DettaglioAnagraficaFascicoloPresenter> getDettaglioAnagraficaFascicoloPresenter();

	AsyncProvider<ListaAnagraficaGruppiPresenter> getListaAnagraficaGruppiPresenter();

	AsyncProvider<DettaglioAnagraficaGruppiPresenter> getDettaglioAnagraficaGruppiPresenter();

	AsyncProvider<ListaAnagraficaIngressiPresenter> getListaAnagraficaIngressiPresenter();

	AsyncProvider<DettaglioAnagraficaIngressiPresenter> getDettaglioAnagraficaIngressiPresenter();

	AsyncProvider<ListaAbilitazioniPresenter> getListaAbilitazioniPresenter();

	AsyncProvider<DettaglioAbilitazionePresenter> getDettaglioAbilitazionePresenter();

	AsyncProvider<ModificaTipologieAllegatoPresenter> getModificaTipologiaAllegatoPresenter();

	AsyncProvider<AngularPresenter> getAngularPresenter();

	AsyncProvider<DrivePresenter> getDrivePresenter();

	AsyncProvider<DriveDetailPresenter> getDriveDetailPresenter();

	AsyncProvider<DriveCartellaPresenter> getDriveCartellaPresenter();

	AsyncProvider<DriveFilePresenter> getDriveFilePresenter();

	AsyncProvider<InvioMailDaCSVPresenter> getInvioMailDaCSVPresenter();

}
