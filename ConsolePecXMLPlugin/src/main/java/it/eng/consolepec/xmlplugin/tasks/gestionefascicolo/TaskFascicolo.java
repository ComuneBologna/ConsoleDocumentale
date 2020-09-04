package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo;

import it.eng.consolepec.xmlplugin.factory.DatiTask;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.*;
import it.eng.consolepec.xmlplugin.tasks.operazioni.ModificaNoteTaskApi;

public interface TaskFascicolo<T extends DatiTask> extends Task<T> //
		, AgganciaPraticaAFascicoloTaskApi, AggiungiAllegatoApiTask, AggiungiDatoAggiuntivoTaskApi, AggiungiProtocollazioneBA01TaskApi //
		, AggiungiReinoltroAProtocollazione, AvviaProcedimentoTaskApi, CambiaStatoFascicoloTaskApi, CambiaTipoFascicoloTaskApi //
		, CambiaVisibilitaFascicoloTaskApi, ChiudiProcedimentoTaskApi, CollegaFascicoloTaskApi, ConcludiFascicoloTaskApi //
		, CondividiFascicoloTaskApi, EliminaCollegamentoFascicoloTaskApi, EliminaCondivisioneTaskApi, EliminaFascicoloTaskApi //
		, FirmaAllegatiTaskApi, GestionePresaInCaricoApiTask, RichiestaApprovazioneFirmaTaskApi, MettiInAffissioneTaskApi //
		, ProtocollaTaskApi, PubblicaAllegatoTaskApi, RiassegnaFascicoloTaskApi, RimuoviAllegatoTaskApi //
		, RimuoviDatoAggiuntivoTaskApi, RimuoviPraticaTaskApi, RimuoviPubblicazioneAllegatoTaskApi, RispondiTaskApi //
		, SganciaPecInApiTask, TerminaApiTask, RiportaInLetturaTaskApi, AssegnaUtenteEsternoTaskApi //
		, ModificaAbilitazioniAssegnaUtenteEsternoTaskApi, RitornaDaInoltrareEsternoTaskApi, RispondiEmailDaTemplateTaskApi, CambiaStepIterTaskApi //
		, ModificaDatoAggiuntivoTaskApi, ModificaOperatoreTaskApi, RitiroApprovazioneFirmaTaskApi, RicaricaAllegatoProtocollatoTaskApi //
		, AggiornaPGTaskApi, ModificaFascicoloTaskApi, VersionaAllegatoTaskFirmaApiTask, CambiaTipologiaAllegatoTaskApi //
		, CollegaPraticaProcediTaskApi, EmissionePermessoTaskApi, EstrazioneTaskFirmaTaskApi, InserisciModificaMetadatiAllegatoTaskApi //
		, EliminaMetadatiAllegatoTaskApi, CambiaVisibilitaAllegatoTaskApi, ModificaTipologieAllegatoTaskApi, OperativitaRidottaTaskApi //
		, TagliaAllegatiTaskApi, IncollaAllegatiTaskApi, TagliaProtocollazioniTaskApi, IncollaProtocollazioniTaskApi, ModificaNoteTaskApi, InviaDaCSVTaskApi, CreaBozzaTaskApi
//
{
	public boolean isGestionePresaInCaricoAbilitata();
}
