/**
 * Copyright 2011 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under the License.
 */

package it.eng.portlet.consolepec.gwt.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gwtplatform.dispatch.server.spring.configuration.DefaultModule;

import it.eng.portlet.consolepec.gwt.server.cartellafirma.CercaDocumentiFirmaVistoActionHandler;
import it.eng.portlet.consolepec.gwt.server.cartellafirma.FineWizardTaskFirmaActionHandler;
import it.eng.portlet.consolepec.gwt.server.collegamenti.CaricaComposizioneFascicoliCollegatiActionHandler;
import it.eng.portlet.consolepec.gwt.server.configurazioni.CaricaAnagrafichePraticheActionHandler;
import it.eng.portlet.consolepec.gwt.server.configurazioni.CaricaAnagraficheRuoliActionHandler;
import it.eng.portlet.consolepec.gwt.server.configurazioni.CaricaDizionariActionHandler;
import it.eng.portlet.consolepec.gwt.server.configurazioni.CaricaMatriceVisibilitaPraticaActionHandler;
import it.eng.portlet.consolepec.gwt.server.configurazioni.CaricaNomenclatureActionHandler;
import it.eng.portlet.consolepec.gwt.server.configurazioni.CaricaProprietaGeneraliActionHandler;
import it.eng.portlet.consolepec.gwt.server.configurazioni.CaricaSettoriActionHandler;
import it.eng.portlet.consolepec.gwt.server.configurazioni.amministrazione.AmministrazioneAbilitazioniRuoloActionHandler;
import it.eng.portlet.consolepec.gwt.server.configurazioni.amministrazione.AmministrazioneAnagraficaFascicoloActionHandler;
import it.eng.portlet.consolepec.gwt.server.configurazioni.amministrazione.AmministrazioneAnagraficaIngressoActionHandler;
import it.eng.portlet.consolepec.gwt.server.configurazioni.amministrazione.AmministrazioneAnagraficaRuoloActionHandler;
import it.eng.portlet.consolepec.gwt.server.drive.AggiornaCartellaActionHandler;
import it.eng.portlet.consolepec.gwt.server.drive.AggiornaFileActionHandler;
import it.eng.portlet.consolepec.gwt.server.drive.AggiornaPermessiActionHandler;
import it.eng.portlet.consolepec.gwt.server.drive.ApriCartellaActionHandler;
import it.eng.portlet.consolepec.gwt.server.drive.CercaElementoActionHandler;
import it.eng.portlet.consolepec.gwt.server.drive.CreaCartellaActionHandler;
import it.eng.portlet.consolepec.gwt.server.drive.EliminaElementoActionHandler;
import it.eng.portlet.consolepec.gwt.server.drive.RicercaDriveActionHandler;
import it.eng.portlet.consolepec.gwt.server.fascicolo.*;
import it.eng.portlet.consolepec.gwt.server.fascicolo.amianto.estrazioni.EstrazioneAmiantoActionHandler;
import it.eng.portlet.consolepec.gwt.server.firma.SendOTPRequestActionHandler;
import it.eng.portlet.consolepec.gwt.server.genericdata.CaricaIndirizziEmailActionHandler;
import it.eng.portlet.consolepec.gwt.server.inviomassivo.CreaComunicazioneActionHandler;
import it.eng.portlet.consolepec.gwt.server.inviomassivo.NuovoInvioComunicazioneActionHandler;
import it.eng.portlet.consolepec.gwt.server.modulistica.CambiaGruppoPraticaModulisticaActionHandler;
import it.eng.portlet.consolepec.gwt.server.modulistica.CambiaStatoPraticaModulisticaActionHandler;
import it.eng.portlet.consolepec.gwt.server.modulistica.CaricaPraticaModulisticaActionHandler;
import it.eng.portlet.consolepec.gwt.server.pec.AggiungiPraticaAFascicoloActionHandler;
import it.eng.portlet.consolepec.gwt.server.pec.CambiaGruppoPecInActionHandler;
import it.eng.portlet.consolepec.gwt.server.pec.CambiaStatoPecInActionHandler;
import it.eng.portlet.consolepec.gwt.server.pec.CancellaAllegatoPecOutActionHandler;
import it.eng.portlet.consolepec.gwt.server.pec.CaricaPraticaEmailActionHandler;
import it.eng.portlet.consolepec.gwt.server.pec.CaricaPraticaEmailOutActionHandler;
import it.eng.portlet.consolepec.gwt.server.pec.ElettoraleActionHandler;
import it.eng.portlet.consolepec.gwt.server.pec.EliminaBozzaActionHandler;
import it.eng.portlet.consolepec.gwt.server.pec.EstraiEMLActionHandler;
import it.eng.portlet.consolepec.gwt.server.pec.InviaMailActionActionHandler;
import it.eng.portlet.consolepec.gwt.server.pec.InvioMailDaCSVActionHandler;
import it.eng.portlet.consolepec.gwt.server.pec.ReinoltroActionHandler;
import it.eng.portlet.consolepec.gwt.server.pec.SalvaBozzaInvioActionHandler;
import it.eng.portlet.consolepec.gwt.server.pec.SalvaNotePecInActionHandler;
import it.eng.portlet.consolepec.gwt.server.pec.UploadAllegatoPraticaActionHandler;
import it.eng.portlet.consolepec.gwt.server.profilazione.CaricaAbilitazioniUtenteActionHandler;
import it.eng.portlet.consolepec.gwt.server.profilazione.CaricaDatiUtenteActionHandler;
import it.eng.portlet.consolepec.gwt.server.profilazione.CaricaSupervisoriUtenteActionHandler;
import it.eng.portlet.consolepec.gwt.server.profilazione.CaricaWorklistActionHandler;
import it.eng.portlet.consolepec.gwt.server.profilazione.GestionePreferenzeUtenteActionHandler;
import it.eng.portlet.consolepec.gwt.server.protocollazione.ProtocollaActionHandler;
import it.eng.portlet.consolepec.gwt.server.protocollazione.ProtocollaFascicoloNuovoActionHandler;
import it.eng.portlet.consolepec.gwt.server.protocollazione.RecuperaTipologieProcedimentiActionHandler;
import it.eng.portlet.consolepec.gwt.server.protocollazione.RicercaCapofilaActionHandler;
import it.eng.portlet.consolepec.gwt.server.rest.handler.RestLoginActionHandler;
import it.eng.portlet.consolepec.gwt.server.ricercautenti.RecuperaUtentiLdapActionHandler;
import it.eng.portlet.consolepec.gwt.server.richiedifirma.RichiediFirmaVistoActionHandler;
import it.eng.portlet.consolepec.gwt.server.rubrica.CreaAnagraficaActionHandler;
import it.eng.portlet.consolepec.gwt.server.rubrica.EliminaAnagraficaActionHandler;
import it.eng.portlet.consolepec.gwt.server.rubrica.ImportaLagActionHandler;
import it.eng.portlet.consolepec.gwt.server.rubrica.ModificaAnagraficaActionHandler;
import it.eng.portlet.consolepec.gwt.server.rubrica.RicercaAnagraficheActionHandler;
import it.eng.portlet.consolepec.gwt.server.sara.EmissionePermessoActionHandler;
import it.eng.portlet.consolepec.gwt.server.template.CaricaModelloActionHandler;
import it.eng.portlet.consolepec.gwt.server.template.CreaBozzaDaTemplateActionHandler;
import it.eng.portlet.consolepec.gwt.server.template.CreaPdfDaTemplateActionHandler;
import it.eng.portlet.consolepec.gwt.server.template.creazione.CreaModelloActionHandler;
import it.eng.portlet.consolepec.gwt.server.template.eliminazione.EliminaTemplateActionHandler;
import it.eng.portlet.consolepec.gwt.server.template.salvataggio.SalvaModelloActionHandler;
import it.eng.portlet.consolepec.gwt.server.urbanistica.CollegaPraticaProcediActionHandler;
import it.eng.portlet.consolepec.gwt.server.urbanistica.DettaglioAllegatoFirmatoActionHandler;
import it.eng.portlet.consolepec.gwt.server.urbanistica.DettaglioPraticaProcediActionHandler;
import it.eng.portlet.consolepec.gwt.server.urbanistica.EliminaCollegaPraticaProcediActionHandler;
import it.eng.portlet.consolepec.gwt.server.urbanistica.RicercaPraticaProcediActionHandler;
import it.eng.portlet.consolepec.gwt.server.visibilita.ModificaVisibilitaAllegatoActionHandler;
import it.eng.portlet.consolepec.gwt.server.visibilita.ModificaVisibilitaFascicoloActionHandler;
import it.eng.portlet.consolepec.gwt.shared.action.*;
import it.eng.portlet.consolepec.gwt.shared.action.assegna.fasciolo.RiassegnaFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.assegna.modulo.RiassegnaModulo;
import it.eng.portlet.consolepec.gwt.shared.action.assegna.pec.RiassegnaPecIn;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.CercaDocumentoFirmaVistoAction;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.FineWizardTaskFirmaAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaAnagrafichePraticheAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaAnagraficheRuoliAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaDizionariAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaMatriceVisibilitaPraticaAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaNomenclatureAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaProprietaGeneraliAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaSettoriAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAbilitazioniRuoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAnagraficaFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAnagraficaIngressoAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAnagraficaRuoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.*;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.amianto.EstrazioneAmianto;
import it.eng.portlet.consolepec.gwt.shared.action.firma.SendOTPRequest;
import it.eng.portlet.consolepec.gwt.shared.action.genericdata.CaricaIndirizziEmailAction;
import it.eng.portlet.consolepec.gwt.shared.action.inviomassivo.CreaComunicazioneAction;
import it.eng.portlet.consolepec.gwt.shared.action.inviomassivo.NuovoInvioComunicazioneAction;
import it.eng.portlet.consolepec.gwt.shared.action.modulistica.CambiaStatoPraticaModulistica;
import it.eng.portlet.consolepec.gwt.shared.action.modulistica.CaricaPraticaModulisticaAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.AggiungiPraticaAFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CambiaStatoPecInAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CancellaAllegatoPecOut;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CaricaPraticaEmailInAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CaricaPraticaEmailOutAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.ComposizioneFascicoliCollegatiAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.ElettoraleAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.EliminaBozza;
import it.eng.portlet.consolepec.gwt.shared.action.pec.EstraiEMLAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.InviaMailAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.InvioMailDaCSVAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.ReinoltroAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.SalvaBozzaInvio;
import it.eng.portlet.consolepec.gwt.shared.action.pec.UploadAllegatoPraticaAction;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.CaricaAbilitazioniUtenteAction;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.CaricaDatiUtenteAction;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.CaricaSupervisoriUtenteAction;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.CaricaWorklistAction;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.GestionePreferenzeUtenteAction;
import it.eng.portlet.consolepec.gwt.shared.action.ricercautenti.RecuperaUtentiLdapAction;
import it.eng.portlet.consolepec.gwt.shared.action.richiedifirma.RichiediFirmaVistoAction;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.CreaAnagraficaAction;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.EliminaAnagraficaAction;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.ImportaLagAction;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.ModificaAnagraficaAction;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.RicercaAnagrafiche;
import it.eng.portlet.consolepec.gwt.shared.action.sara.EmissionePermessoAction;
import it.eng.portlet.consolepec.gwt.shared.action.template.CaricaModelloAction;
import it.eng.portlet.consolepec.gwt.shared.action.template.CreaBozzaDaTemplateAction;
import it.eng.portlet.consolepec.gwt.shared.action.template.CreaPdfDaTemplateAction;
import it.eng.portlet.consolepec.gwt.shared.action.template.creazione.CreaModelloAction;
import it.eng.portlet.consolepec.gwt.shared.action.template.eliminazione.EliminaTemplateAction;
import it.eng.portlet.consolepec.gwt.shared.action.template.salvataggio.SalvaModelloAction;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.CollegaPraticaProcediAction;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.DettaglioAllegatoFirmatoAction;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.DettaglioPraticaProcediAction;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.EliminaCollegaPraticaProcediAction;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.RicercaPraticaProcediAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.AggiornaCartellaAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.AggiornaFileAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.AggiornaPermessiAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.ApriCartellaAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.CercaElementoAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.CreaCartellaAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.EliminaElementoAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.RicercaDriveAction;
import it.eng.portlet.consolepec.gwt.shared.rest.RestLoginAction;

/**
 * Module which binds the handlers and configurations.
 *
 * @author Philippe Beaudoin
 */
@Configuration
@Import(DefaultModule.class)
public class ServerModule extends IONOHandlerModule {

	public ServerModule() {}

	@Override
	protected void configureHandlers() {

		bindHandler(CaricaPraticaEmailInAction.class, CaricaPraticaEmailActionHandler.class);

		bindHandler(FirmaAllegatoPecOutBozzaAction.class, FirmaAllegatoPecOutBozzaActionHandler.class);

		bindHandler(CambiaStatoPecInAction.class, CambiaStatoPecInActionHandler.class);

		bindHandler(RiassegnaPecIn.class, CambiaGruppoPecInActionHandler.class);

		bindHandler(GetConfigurazioneCampiProtocollazione.class, GetConfigurazioneCampiProtocollazioneActionHandler.class);

		bindHandler(ProtocollaAction.class, ProtocollaActionHandler.class);

		bindHandler(CaricaPraticaFascicoloAction.class, CaricaPraticaFascicoloActionHandler.class);

		bindHandler(CambiaStatoPraticaModulistica.class, CambiaStatoPraticaModulisticaActionHandler.class);

		bindHandler(CaricaPraticaModulisticaAction.class, CaricaPraticaModulisticaActionHandler.class);

		bindHandler(CreaRisposta.class, CreaRispostaActionHandler.class);

		bindHandler(SalvaFascicolo.class, SalvaFascicoloActionHandler.class);

		bindHandler(CreaFascicoloAction.class, CreaFascicoloActionHandler.class);

		bindHandler(UploadAllegatoFascicolo.class, UploadAllegatoFascicoloActionHandler.class);

		bindHandler(CancellaAllegatoFascicolo.class, CancellaAllegatoFascicoloActionHandler.class);

		bindHandler(CambiaStatoFascicolo.class, CambiaStatoFascicoloActionHandler.class);

		bindHandler(CaricaPraticaEmailOutAction.class, CaricaPraticaEmailOutActionHandler.class);

		bindHandler(UploadAllegatoPraticaAction.class, UploadAllegatoPraticaActionHandler.class);

		bindHandler(CancellaAllegatoPecOut.class, CancellaAllegatoPecOutActionHandler.class);

		bindHandler(EliminaBozza.class, EliminaBozzaActionHandler.class);

		bindHandler(RiassegnaFascicolo.class, CambiaGruppoFascicoloActionHandler.class);

		bindHandler(SalvaBozzaInvio.class, SalvaBozzaInvioActionHandler.class);

		bindHandler(GetComboBoxProtocollazione.class, GetComboBoxProtocollazioneActionHandler.class);

		bindHandler(EliminaFascicolo.class, EliminaFascicoloActionHandler.class);

		bindHandler(InviaMailAction.class, InviaMailActionActionHandler.class);

		bindHandler(FirmaAllegatoFascicoloAction.class, FirmaAllegatoFascicoloActionHandler.class);

		bindHandler(GetDettagliAllegato.class, GetDettagliAllegatoActionHandler.class);

		bindHandler(SendOTPRequest.class, SendOTPRequestActionHandler.class);

		bindHandler(AggiungiPraticaAFascicolo.class, AggiungiPraticaAFascicoloActionHandler.class);

		bindHandler(GestionePresaInCaricoFascicoloAction.class, GestionePresaInCaricoFascicoloActionHandler.class);

		bindHandler(RicercaCapofilaAction.class, RicercaCapofilaActionHandler.class);

		bindHandler(ProtocollaFascicoloNuovoAction.class, ProtocollaFascicoloNuovoActionHandler.class);

		bindHandler(ReinoltroAction.class, ReinoltroActionHandler.class);

		bindHandler(ScaricaAllegatiMultipli.class, ScaricaAllegatiMultipliActionHandler.class);

		bindHandler(PubblicazioneAllegati.class, PubblicazioneAllegatiActionHandler.class);

		bindHandler(ModificaVisibilitaFascicolo.class, ModificaVisibilitaFascicoloActionHandler.class);

		bindHandler(ModificaVisibilitaAllegato.class, ModificaVisibilitaAllegatoActionHandler.class);

		bindHandler(RimozionePubblicazioneAllegati.class, RimozionePubblicazioneAllegatiActionHandler.class);

		bindHandler(RecuperaGruppiVisibilita.class, RecuperaGruppiVisibilitaActionHandler.class);

		bindHandler(CollegamentoFascicoli.class, CollegamentoFascicoliActionHandler.class);

		bindHandler(CondivisioneFascicolo.class, CondivisioneFascicoloActionHandler.class);

		bindHandler(CollegamentoFascicoliMultiplo.class, CollegamentoFascicoliMultiploActionHandler.class);

		bindHandler(CondivisioneFascicoloMultipla.class, CondivisioneFascicoloMultiplaActionHandler.class);

		bindHandler(RiassegnaModulo.class, CambiaGruppoPraticaModulisticaActionHandler.class);

		bindHandler(RecuperaTipologieProcedimenti.class, RecuperaTipologieProcedimentiActionHandler.class);

		bindHandler(AvvioProcedimento.class, AvvioProcedimentoActionHandler.class);

		bindHandler(CercaProcedimentiCollegati.class, CercaProcedimentiCollegatiActionHandler.class);

		bindHandler(ChiusuraProcedimentoAction.class, ChiusuraProcedimentoActionHandler.class);

		bindHandler(IterProcedimento.class, IterProcedimentoActionHandler.class);

		bindHandler(PropostaChiusuraProcedimentiAction.class, PropostaChiusuraProcedimentiActionHandler.class);

		bindHandler(RiversamentoCartaceo.class, RiversamentoCartaceoActionHandler.class);

		bindHandler(CaricaPraticaAction.class, CaricaPraticaActionActionHandler.class);

		bindHandler(SganciaPecIn.class, SganciaPecInActionHandler.class);

		bindHandler(RiportaInLettura.class, RiportaInLetturaActionHandler.class);

		bindHandler(CaricaModelloAction.class, CaricaModelloActionHandler.class);

		bindHandler(SalvaModelloAction.class, SalvaModelloActionHandler.class);

		bindHandler(EliminaTemplateAction.class, EliminaTemplateActionHandler.class);

		bindHandler(EstrazioneAmianto.class, EstrazioneAmiantoActionHandler.class);

		bindHandler(ElettoraleAction.class, ElettoraleActionHandler.class);

		bindHandler(GetDatiAssegnaEsterno.class, GetDatiAssegnaEsternoActionHandler.class);

		bindHandler(AssegnaUtenteEsternoAction.class, AssegnaUtenteEsternoActionHandler.class);

		bindHandler(ModificaAbilitazioniAssegnaUtenteEsternoAction.class, ModificaAbilitazioniAssegnaUtenteEsternoActionHandler.class);

		bindHandler(RitornaDaInoltrareEsternoAction.class, RitornaDaInoltrareEsternoActionHandler.class);

		bindHandler(StampaRicevuteConsegna.class, StampaRicevuteConsegnaActionHandler.class);

		bindHandler(CercaPratiche.class, NewCercaPraticheActionHandler.class);

		bindHandler(CancellaAllegatoPratica.class, CancellaAllegatoPraticaActionHandler.class);

		bindHandler(CreaBozzaDaTemplateAction.class, CreaBozzaDaTemplateActionHandler.class);

		bindHandler(CambiaStepIter.class, CambiaStepIterActionHandler.class);

		bindHandler(CreaComunicazioneAction.class, CreaComunicazioneActionHandler.class);

		bindHandler(NuovoInvioComunicazioneAction.class, NuovoInvioComunicazioneActionHandler.class);

		bindHandler(ModificaOperatore.class, ModificaOperatoreActionHandler.class);

		bindHandler(ValidazioneDatiAggiuntivi.class, ValidazioneDatiAggiuntiviActionHandler.class);

		bindHandler(CaricaIndirizziEmailAction.class, CaricaIndirizziEmailActionHandler.class);

		bindHandler(ComposizioneFascicoliCollegatiAction.class, CaricaComposizioneFascicoliCollegatiActionHandler.class);

		bindHandler(ComposizioneFascicolo.class, ComposizioneFascicoloActionHandler.class);

		bindHandler(CercaDocumentoFirmaVistoAction.class, CercaDocumentiFirmaVistoActionHandler.class);

		bindHandler(FineWizardTaskFirmaAction.class, FineWizardTaskFirmaActionHandler.class);

		bindHandler(RichiediFirmaVistoAction.class, RichiediFirmaVistoActionHandler.class);

		bindHandler(RecuperaUtentiLdapAction.class, RecuperaUtentiLdapActionHandler.class);

		bindHandler(CreaPdfDaTemplateAction.class, CreaPdfDaTemplateActionHandler.class);

		bindHandler(CreaModelloAction.class, CreaModelloActionHandler.class);

		bindHandler(EstraiEtichetteMetadatiAction.class, EstraiEtichetteMetadatiActionHandler.class);

		bindHandler(EstraiEMLAction.class, EstraiEMLActionHandler.class);

		bindHandler(ModificaFascicoloAction.class, ModificaFascicoloActionHandler.class);

		bindHandler(AggiornaPG.class, AggiornaPGActionHandler.class);

		bindHandler(RecuperaFileDaTmpAction.class, RecuperaFileDaTmpActionHandler.class);

		bindHandler(EliminaFileDaTmpAction.class, EliminaFileDaTmpActionHandler.class);

		bindHandler(RicercaAnagrafiche.class, RicercaAnagraficheActionHandler.class);

		bindHandler(CreaAnagraficaAction.class, CreaAnagraficaActionHandler.class);

		bindHandler(ModificaAnagraficaAction.class, ModificaAnagraficaActionHandler.class);

		bindHandler(EliminaAnagraficaAction.class, EliminaAnagraficaActionHandler.class);

		bindHandler(CambiaTipologiaAllegato.class, CambiaTipologiaAllegatoActionHandler.class);

		bindHandler(RicercaPraticaProcediAction.class, RicercaPraticaProcediActionHandler.class);

		bindHandler(DettaglioPraticaProcediAction.class, DettaglioPraticaProcediActionHandler.class);

		bindHandler(CollegaPraticaProcediAction.class, CollegaPraticaProcediActionHandler.class);

		bindHandler(EliminaCollegaPraticaProcediAction.class, EliminaCollegaPraticaProcediActionHandler.class);

		bindHandler(DettaglioAllegatoFirmatoAction.class, DettaglioAllegatoFirmatoActionHandler.class);

		bindHandler(EmissionePermessoAction.class, EmissionePermessoActionHandler.class);

		bindHandler(CaricaAnagrafichePraticheAction.class, CaricaAnagrafichePraticheActionHandler.class);

		bindHandler(CaricaAnagraficheRuoliAction.class, CaricaAnagraficheRuoliActionHandler.class);

		bindHandler(CaricaProprietaGeneraliAction.class, CaricaProprietaGeneraliActionHandler.class);

		bindHandler(CaricaSettoriAction.class, CaricaSettoriActionHandler.class);

		bindHandler(CaricaDizionariAction.class, CaricaDizionariActionHandler.class);

		bindHandler(CaricaNomenclatureAction.class, CaricaNomenclatureActionHandler.class);

		bindHandler(CaricaAbilitazioniUtenteAction.class, CaricaAbilitazioniUtenteActionHandler.class);

		bindHandler(CaricaDatiUtenteAction.class, CaricaDatiUtenteActionHandler.class);

		bindHandler(GestionePreferenzeUtenteAction.class, GestionePreferenzeUtenteActionHandler.class);

		bindHandler(CaricaWorklistAction.class, CaricaWorklistActionHandler.class);

		bindHandler(AmministrazioneAnagraficaRuoloAction.class, AmministrazioneAnagraficaRuoloActionHandler.class);

		bindHandler(AmministrazioneAbilitazioniRuoloAction.class, AmministrazioneAbilitazioniRuoloActionHandler.class);

		bindHandler(AmministrazioneAnagraficaIngressoAction.class, AmministrazioneAnagraficaIngressoActionHandler.class);

		bindHandler(AmministrazioneAnagraficaFascicoloAction.class, AmministrazioneAnagraficaFascicoloActionHandler.class);

		bindHandler(GeneraTitoloFascicoloAction.class, GeneraTitoloFascicoloActionHandler.class);

		bindHandler(ImportaLagAction.class, ImportaLagActionHandler.class);

		bindHandler(ModificaTipologieAllegatiAction.class, ModificaTipologieAllegatiActionHandler.class);

		bindHandler(CaricaMatriceVisibilitaPraticaAction.class, CaricaMatriceVisibilitaPraticaActionHandler.class);

		bindHandler(CaricaSupervisoriUtenteAction.class, CaricaSupervisoriUtenteActionHandler.class);

		bindHandler(UploadFileZipAction.class, UploadFileZipActionHandler.class);

		bindHandler(SpostaAllegatiAction.class, SpostaAllegatiActionHandler.class);

		bindHandler(SpostaProtocollazioniAction.class, SpostaProtocollazioniActionHandler.class);

		bindHandler(CercaElementoAction.class, CercaElementoActionHandler.class);

		bindHandler(ApriCartellaAction.class, ApriCartellaActionHandler.class);

		bindHandler(CreaCartellaAction.class, CreaCartellaActionHandler.class);

		bindHandler(AggiornaCartellaAction.class, AggiornaCartellaActionHandler.class);

		bindHandler(AggiornaFileAction.class, AggiornaFileActionHandler.class);

		bindHandler(RestLoginAction.class, RestLoginActionHandler.class);

		bindHandler(AggiornaPermessiAction.class, AggiornaPermessiActionHandler.class);

		bindHandler(EliminaElementoAction.class, EliminaElementoActionHandler.class);

		bindHandler(RicercaDriveAction.class, RicercaDriveActionHandler.class);

		bindHandler(InvioMailDaCSVAction.class, InvioMailDaCSVActionHandler.class);

		bindHandler(SalvaNoteAction.class, SalvaNoteActionHandler.class);

		bindHandler(SalvaNotePecInAction.class, SalvaNotePecInActionHandler.class);

	}

	@Bean
	public CaricaPraticaEmailActionHandler getEspandiRigaActionActionHandler() {
		return new CaricaPraticaEmailActionHandler();
	}

	@Bean
	public FirmaAllegatoPecOutBozzaActionHandler getFirmaDocumentoActionActionHandler() {
		return new FirmaAllegatoPecOutBozzaActionHandler();
	}

	@Bean
	public CambiaStatoPecInActionHandler getArchiviaEliminaActionHandler() {
		return new CambiaStatoPecInActionHandler();
	}

	@Bean
	public CambiaGruppoPecInActionHandler getCambiaGruppoActionHandler() {
		return new CambiaGruppoPecInActionHandler();
	}

	@Bean
	public GetConfigurazioneCampiProtocollazioneActionHandler getGetConfigurazioneCampiActionHandler() {
		return new GetConfigurazioneCampiProtocollazioneActionHandler();
	}

	@Bean
	public ProtocollaActionHandler getProtocollaActionHandler() {
		return new ProtocollaActionHandler();
	}

	@Bean
	public CaricaPraticaFascicoloActionHandler getRecuperaDettaglioPraticheActionHandler() {
		return new CaricaPraticaFascicoloActionHandler();
	}

	@Bean
	public CaricaPraticaModulisticaActionHandler getCaricaPraticaModulisticaActionHandler() {
		return new CaricaPraticaModulisticaActionHandler();
	}

	@Bean
	public CreaRispostaActionHandler getInviaRispostaActionHandler() {
		return new CreaRispostaActionHandler();
	}

	@Bean
	public SalvaFascicoloActionHandler getSalvaFascicoloActionHandler() {
		return new SalvaFascicoloActionHandler();
	}

	@Bean
	public CreaFascicoloActionHandler getCreaFascicoloActionActionHandler() {
		return new CreaFascicoloActionHandler();
	}

	@Bean
	public CaricaPraticaEmailOutActionHandler getCaricaPraticaEmailOutActionHandler() {
		return new CaricaPraticaEmailOutActionHandler();
	}

	@Bean
	public UploadAllegatoFascicoloActionHandler getUploadAllegatoFascicoloActionHandler() {
		return new UploadAllegatoFascicoloActionHandler();
	}

	@Bean
	public CancellaAllegatoFascicoloActionHandler getCancellaAllegatoFascicoloActionHandler() {
		return new CancellaAllegatoFascicoloActionHandler();
	}

	@Bean
	public UploadAllegatoPraticaActionHandler getUploadAllegatoPecOutActionHandler() {
		return new UploadAllegatoPraticaActionHandler();
	}

	@Bean
	public CancellaAllegatoPecOutActionHandler getCancellaAllegatoPecOutActionHandler() {
		return new CancellaAllegatoPecOutActionHandler();
	}

	@Bean
	public EliminaBozzaActionHandler getEliminaBozzaActionHandler() {
		return new EliminaBozzaActionHandler();
	}

	@Bean
	public CambiaGruppoFascicoloActionHandler getCambiaGruppoFascicoloActionHandler() {
		return new CambiaGruppoFascicoloActionHandler();
	}

	@Bean
	public SalvaBozzaInvioActionHandler getSalvaBozzaInvioActionHandler() {
		return new SalvaBozzaInvioActionHandler();
	}

	@Bean
	public CambiaStatoFascicoloActionHandler getCambiaStatoFascicoloActionHandler() {
		return new CambiaStatoFascicoloActionHandler();
	}

	@Bean
	public GetComboBoxProtocollazioneActionHandler getGetComboBoxProtocollazioneActionHandler() {
		return new GetComboBoxProtocollazioneActionHandler();
	}

	@Bean
	public InviaMailActionActionHandler getInviaMailActionActionHandler() {
		return new InviaMailActionActionHandler();
	}

	@Bean
	public FirmaAllegatoFascicoloActionHandler getFirmaAllegatoFascicoloActionHandlerActionHandler() {
		return new FirmaAllegatoFascicoloActionHandler();
	}

	@Bean
	public GetDettagliAllegatoActionHandler getGetDettagliAllegatoActionHandler() {
		return new GetDettagliAllegatoActionHandler();
	}

	@Bean
	public EliminaFascicoloActionHandler getEliminaFascicoloActionHandler() {
		return new EliminaFascicoloActionHandler();
	}

	@Bean
	public SendOTPRequestActionHandler getSendOTPRequestActionHandler() {
		return new SendOTPRequestActionHandler();
	}

	@Bean
	public AggiungiPraticaAFascicoloActionHandler getAggiungiPraticaAFascicoloActionHandler() {
		return new AggiungiPraticaAFascicoloActionHandler();
	}

	@Bean
	public GestionePresaInCaricoFascicoloActionHandler getGestionePresaInCaricoActionActionHandler() {
		return new GestionePresaInCaricoFascicoloActionHandler();
	}

	@Bean
	public RicercaCapofilaActionHandler getRicercaCapofilaActionHandlerActionHandler() {
		return new RicercaCapofilaActionHandler();
	}

	@Bean
	public ProtocollaFascicoloNuovoActionHandler getProtocollaFascicoloNuovoActionActionHandler() {
		return new ProtocollaFascicoloNuovoActionHandler();
	}

	@Bean
	public ReinoltroActionHandler getReinoltroActionHandler() {
		return new ReinoltroActionHandler();
	}

	@Bean
	public ScaricaAllegatiMultipliActionHandler getScaricaAllegatiMultipliActionHandler() {
		return new ScaricaAllegatiMultipliActionHandler();
	}

	@Bean
	public PubblicazioneAllegatiActionHandler getPubblicazioneAllegatiActionHandlerActionHandler() {
		return new PubblicazioneAllegatiActionHandler();
	}

	@Bean
	public ModificaVisibilitaFascicoloActionHandler getModificaVisibilitaFascicoloActionHandler() {
		return new ModificaVisibilitaFascicoloActionHandler();
	}

	@Bean
	public ModificaVisibilitaAllegatoActionHandler getModificaVisibilitaAllegatoActionHandler() {
		return new ModificaVisibilitaAllegatoActionHandler();
	}

	@Bean
	public RimozionePubblicazioneAllegatiActionHandler getRimozionePubblicazioneAllegatiActionActionHandler() {
		return new RimozionePubblicazioneAllegatiActionHandler();
	}

	@Bean
	public RecuperaGruppiVisibilitaActionHandler getRecuperaGruppiVisibilitaActionHandler() {
		return new RecuperaGruppiVisibilitaActionHandler();
	}

	@Bean
	public CollegamentoFascicoliActionHandler getCollegaFascicoloActionHandler() {
		return new CollegamentoFascicoliActionHandler();
	}

	@Bean
	public CondivisioneFascicoloActionHandler getCondivisioneFascicoloActionHandler() {
		return new CondivisioneFascicoloActionHandler();
	}

	@Bean
	public CollegamentoFascicoliMultiploActionHandler getCollegamentoFascicoliMultiploActionHandler() {
		return new CollegamentoFascicoliMultiploActionHandler();
	}

	@Bean
	public CondivisioneFascicoloMultiplaActionHandler getCondivisioneFascicoloMultiplaActionHandler() {
		return new CondivisioneFascicoloMultiplaActionHandler();
	}

	@Bean
	public CambiaStatoPraticaModulisticaActionHandler getCambiaStatoPraticaModulisticaActionHandler() {
		return new CambiaStatoPraticaModulisticaActionHandler();
	}

	@Bean
	public CambiaGruppoPraticaModulisticaActionHandler getCambiaGruppoPraticaModulisticaActionHandler() {
		return new CambiaGruppoPraticaModulisticaActionHandler();
	}

	@Bean
	public RecuperaTipologieProcedimentiActionHandler getRecuperaTipologieProcedimentiActionHandler() {
		return new RecuperaTipologieProcedimentiActionHandler();
	}

	@Bean
	public AvvioProcedimentoActionHandler getAvvioProcedimentoActionHandler() {
		return new AvvioProcedimentoActionHandler();
	}

	@Bean
	public CercaProcedimentiCollegatiActionHandler getCercaProcedimentiCollegatiActionHandler() {
		return new CercaProcedimentiCollegatiActionHandler();
	}

	@Bean
	public ChiusuraProcedimentoActionHandler getChiusuraProcedimentoActionHandler() {
		return new ChiusuraProcedimentoActionHandler();
	}

	@Bean
	public IterProcedimentoActionHandler getIterProcedimentoActionHandler() {
		return new IterProcedimentoActionHandler();
	}

	@Bean
	public PropostaChiusuraProcedimentiActionHandler getPropostaChiusuraProcedimentiActionHandler() {
		return new PropostaChiusuraProcedimentiActionHandler();
	}

	@Bean
	public RiversamentoCartaceoActionHandler getRiversamentoCartaceoActionHandler() {
		return new RiversamentoCartaceoActionHandler();
	}

	@Bean
	public SganciaPecInActionHandler getSganciaPecInActionHandler() {
		return new SganciaPecInActionHandler();
	}

	@Bean
	public CaricaPraticaActionActionHandler getCaricaPraticaActionActionHandler() {
		return new CaricaPraticaActionActionHandler();
	}

	@Bean
	public RiportaInLetturaActionHandler getRiportaInLetturaActionHandler() {
		return new RiportaInLetturaActionHandler();
	}

	@Bean
	public CreaModelloActionHandler getCreaModelloActionHandler() {
		return new CreaModelloActionHandler();
	}

	@Bean
	public CaricaModelloActionHandler getCaricaModelloActionHandler() {
		return new CaricaModelloActionHandler();
	}

	@Bean
	public EstrazioneAmiantoActionHandler getEstrazioneAmiantoActionHandler() {
		return new EstrazioneAmiantoActionHandler();
	}

	@Bean
	public SalvaModelloActionHandler getModelloActionHandler() {
		return new SalvaModelloActionHandler();
	}

	@Bean
	public EliminaTemplateActionHandler getEliminaTemplateActionHandler() {
		return new EliminaTemplateActionHandler();
	}

	@Bean
	public ElettoraleActionHandler getElettoraleActionHandler() {
		return new ElettoraleActionHandler();
	}

	@Bean
	public GetDatiAssegnaEsternoActionHandler getGetDatiAssegnaEsternoActionHandler() {
		return new GetDatiAssegnaEsternoActionHandler();
	}

	@Bean
	public AssegnaUtenteEsternoActionHandler getAssegnaUtenteEsternoActionHandler() {
		return new AssegnaUtenteEsternoActionHandler();
	}

	@Bean
	public ModificaAbilitazioniAssegnaUtenteEsternoActionHandler getModificaAbilitazioniAssegnaUtenteEsternoActionHandler() {
		return new ModificaAbilitazioniAssegnaUtenteEsternoActionHandler();
	}

	@Bean
	public RitornaDaInoltrareEsternoActionHandler getRitornaDaInoltrareEsternoActionHandler() {
		return new RitornaDaInoltrareEsternoActionHandler();
	}

	@Bean
	public StampaRicevuteConsegnaActionHandler getStampaRicevuteConsegnaActionHandler() {
		return new StampaRicevuteConsegnaActionHandler();
	}

	@Bean
	public NewCercaPraticheActionHandler getNewCercaPraticheActionHandler() {
		return new NewCercaPraticheActionHandler();
	}

	@Bean
	public CancellaAllegatoPraticaActionHandler getCancellaAllegatoPraticaActionHandler() {
		return new CancellaAllegatoPraticaActionHandler();
	}

	@Bean
	public CreaBozzaDaTemplateActionHandler getCreaBozzaDaTemplateActionHandler() {
		return new CreaBozzaDaTemplateActionHandler();
	}

	@Bean
	public CambiaStepIterActionHandler getCambiaStepIterActionHandler() {
		return new CambiaStepIterActionHandler();
	}

	@Bean
	public CreaComunicazioneActionHandler getCreaComunicazioneActionHandler() {
		return new CreaComunicazioneActionHandler();
	}

	@Bean
	public NuovoInvioComunicazioneActionHandler getNuovoInvioComunicazioneActionHandler() {
		return new NuovoInvioComunicazioneActionHandler();
	}

	@Bean
	public ModificaOperatoreActionHandler getModificaOperatoreActionHandler() {
		return new ModificaOperatoreActionHandler();
	}

	@Bean
	public ValidazioneDatiAggiuntiviActionHandler getValidazioneIndirizziActionHandler() {
		return new ValidazioneDatiAggiuntiviActionHandler();
	}

	@Bean
	public CaricaIndirizziEmailActionHandler getCaricaIndirizziEmailActionHandler() {
		return new CaricaIndirizziEmailActionHandler();
	}

	@Bean
	public CaricaComposizioneFascicoliCollegatiActionHandler getCaricaComposizioneFascicoliCollegatiActionHandler() {
		return new CaricaComposizioneFascicoliCollegatiActionHandler();
	}

	@Bean
	public ComposizioneFascicoloActionHandler getComposizioneFascicoloActionHandler() {
		return new ComposizioneFascicoloActionHandler();
	}

	@Bean
	public CercaDocumentiFirmaVistoActionHandler getCercaDocumentiFirmaVistoActionHandler() {
		return new CercaDocumentiFirmaVistoActionHandler();
	}

	@Bean
	public FineWizardTaskFirmaActionHandler getFineWizardTaskFirmaActionHandler() {
		return new FineWizardTaskFirmaActionHandler();
	}

	@Bean
	public RichiediFirmaVistoActionHandler getRichiediFirmaVistoActionHandler() {
		return new RichiediFirmaVistoActionHandler();
	}

	@Bean
	public RecuperaUtentiLdapActionHandler getRecuperaUtentiActionHandler() {
		return new RecuperaUtentiLdapActionHandler();
	}

	@Bean
	public CreaPdfDaTemplateActionHandler getCreaPdfDaTemplateActionActionHandler() {
		return new CreaPdfDaTemplateActionHandler();
	}

	@Bean
	public EstraiEtichetteMetadatiActionHandler getEstraiEtichetteMetadatiActionHandler() {
		return new EstraiEtichetteMetadatiActionHandler();
	}

	@Bean
	public EstraiEMLActionHandler getEstraiEMLActionHandler() {
		return new EstraiEMLActionHandler();
	}

	@Bean
	public ModificaFascicoloActionHandler getModificaFascicoloActionHandler() {
		return new ModificaFascicoloActionHandler();
	}

	@Bean
	public AggiornaPGActionHandler getAggiornaPGActionHandler() {
		return new AggiornaPGActionHandler();
	}

	@Bean
	public RecuperaFileDaTmpActionHandler getRecuperaFileDaTmpActionHandler() {
		return new RecuperaFileDaTmpActionHandler();
	}

	@Bean
	public EliminaFileDaTmpActionHandler getEliminaFileDaTmpActionHandler() {
		return new EliminaFileDaTmpActionHandler();
	}

	@Bean
	public RicercaAnagraficheActionHandler getRicercaAnagraficheActionHandler() {
		return new RicercaAnagraficheActionHandler();
	}

	@Bean
	public CreaAnagraficaActionHandler getCreaAnagraficaActionHandler() {
		return new CreaAnagraficaActionHandler();
	}

	@Bean
	public ModificaAnagraficaActionHandler getModificaAnagraficaActionHandler() {
		return new ModificaAnagraficaActionHandler();
	}

	@Bean
	public EliminaAnagraficaActionHandler getEliminaAnagraficaActionHandler() {
		return new EliminaAnagraficaActionHandler();
	}

	@Bean
	public RicercaPraticaProcediActionHandler getRicercaPraticaProcediActionHandler() {
		return new RicercaPraticaProcediActionHandler();
	}

	@Bean
	public DettaglioPraticaProcediActionHandler getDettaglioPraticaProcediActionHandler() {
		return new DettaglioPraticaProcediActionHandler();
	}

	@Bean
	public CollegaPraticaProcediActionHandler getCollegaPraticaProcediActionHandler() {
		return new CollegaPraticaProcediActionHandler();
	}

	@Bean
	public EliminaCollegaPraticaProcediActionHandler getEliminaCollegaPraticaProcediActionHandler() {
		return new EliminaCollegaPraticaProcediActionHandler();
	}

	@Bean
	public EmissionePermessoActionHandler getEmissionePermessoActionHandler() {
		return new EmissionePermessoActionHandler();
	}

	@Bean
	public CaricaAnagrafichePraticheActionHandler getCaricaAnagrafichePraticheActionHandler() {
		return new CaricaAnagrafichePraticheActionHandler();
	}

	@Bean
	public CaricaAnagraficheRuoliActionHandler getCaricaAnagraficheRuoliActionHandler() {
		return new CaricaAnagraficheRuoliActionHandler();
	}

	@Bean
	public CaricaProprietaGeneraliActionHandler getCaricaProprietaGeneraliActionHandler() {
		return new CaricaProprietaGeneraliActionHandler();
	}

	@Bean
	public CaricaSettoriActionHandler getCaricaSettoriActionHandler() {
		return new CaricaSettoriActionHandler();
	}

	@Bean
	public CaricaAbilitazioniUtenteActionHandler getCaricaAbilitazioniUtenteActionHandler() {
		return new CaricaAbilitazioniUtenteActionHandler();
	}

	@Bean
	public CaricaDatiUtenteActionHandler getCaricaProfilazioneUtenteActionHandler() {
		return new CaricaDatiUtenteActionHandler();
	}

	@Bean
	public GestionePreferenzeUtenteActionHandler getGestionePreferenzeUtenteActionHandler() {
		return new GestionePreferenzeUtenteActionHandler();
	}

	@Bean
	public CaricaWorklistActionHandler getCaricaWorklistActionHandler() {
		return new CaricaWorklistActionHandler();
	}

	@Bean
	public DettaglioAllegatoFirmatoActionHandler getDettaglioAllegatoFirmatoActionHandler() {
		return new DettaglioAllegatoFirmatoActionHandler();
	}

	@Bean
	public AmministrazioneAnagraficaRuoloActionHandler getAmministrazioneAnagraficaRuoloActionHandler() {
		return new AmministrazioneAnagraficaRuoloActionHandler();
	}

	@Bean
	public AmministrazioneAbilitazioniRuoloActionHandler getAmministrazioneAbilitazioniRuoloActionHandler() {
		return new AmministrazioneAbilitazioniRuoloActionHandler();
	}

	@Bean
	public AmministrazioneAnagraficaIngressoActionHandler getAmministrazioneAnagraficaIngressoActionHandler() {
		return new AmministrazioneAnagraficaIngressoActionHandler();
	}

	@Bean
	public AmministrazioneAnagraficaFascicoloActionHandler getAmministrazioneAnagraficaFascicoloActionHandler() {
		return new AmministrazioneAnagraficaFascicoloActionHandler();
	}

	@Bean
	public GeneraTitoloFascicoloActionHandler getGeneraTitoloFascicoloActionHandler() {
		return new GeneraTitoloFascicoloActionHandler();
	}

	@Bean
	public CambiaTipologiaAllegatoActionHandler getCambiaTipologiaAllegatoActionHandler() {
		return new CambiaTipologiaAllegatoActionHandler();
	}

	@Bean
	public ImportaLagActionHandler getImportaLagActionHandler() {
		return new ImportaLagActionHandler();
	}

	@Bean
	public ModificaTipologieAllegatiActionHandler getModificaTipologieAllegatiActionHandler() {
		return new ModificaTipologieAllegatiActionHandler();
	}

	@Bean
	public CaricaMatriceVisibilitaPraticaActionHandler getCaricaMatriceVisibilitaPraticaActionHandler() {
		return new CaricaMatriceVisibilitaPraticaActionHandler();
	}

	@Bean
	public CaricaSupervisoriUtenteActionHandler getCaricaSupervisoriUtenteActionHandler() {
		return new CaricaSupervisoriUtenteActionHandler();
	}

	@Bean
	public UploadFileZipActionHandler getUploadFileZipActionHandler() {
		return new UploadFileZipActionHandler();
	}

	@Bean
	public SpostaAllegatiActionHandler getSpostaAllegatiActionHandler() {
		return new SpostaAllegatiActionHandler();
	}

	@Bean
	public SpostaProtocollazioniActionHandler getSpostaProtocollazioniActionHandler() {
		return new SpostaProtocollazioniActionHandler();
	}

	@Bean
	public CercaElementoActionHandler getCercaCartellaActionHandler() {
		return new CercaElementoActionHandler();
	}

	@Bean
	public ApriCartellaActionHandler getApriCartellaActionHandler() {
		return new ApriCartellaActionHandler();
	}

	@Bean
	public CaricaDizionariActionHandler getCaricaDizionariActionHandler() {
		return new CaricaDizionariActionHandler();
	}

	@Bean
	public CreaCartellaActionHandler getCreaCartellaActionHandler() {
		return new CreaCartellaActionHandler();
	}

	@Bean
	public AggiornaCartellaActionHandler getAggiornaCartellaActionHandler() {
		return new AggiornaCartellaActionHandler();
	}

	@Bean
	public AggiornaFileActionHandler getAggiornaFileActionHandler() {
		return new AggiornaFileActionHandler();
	}

	@Bean
	public RestLoginActionHandler getRestLoginActionHandler() {
		return new RestLoginActionHandler();
	}

	@Bean
	public AggiornaPermessiActionHandler getAggiornaPermessiActionHandler() {
		return new AggiornaPermessiActionHandler();
	}

	@Bean
	public CaricaNomenclatureActionHandler getCaricaNomenclatureActionHandler() {
		return new CaricaNomenclatureActionHandler();
	}

	@Bean
	public EliminaElementoActionHandler getEliminaElementoActionHandler() {
		return new EliminaElementoActionHandler();
	}

	@Bean
	public RicercaDriveActionHandler getRicercaDriveActionHandler() {
		return new RicercaDriveActionHandler();
	}

	@Bean
	public InvioMailDaCSVActionHandler invioMailDaCSVActionHandler() {
		return new InvioMailDaCSVActionHandler();
	}

	@Bean
	public SalvaNoteActionHandler salvaNoteActionHandler() {
		return new SalvaNoteActionHandler();
	}

	@Bean
	public SalvaNotePecInActionHandler salvaNotePecInActionHandlerActionHandler() {
		return new SalvaNotePecInActionHandler();
	}
}
