package it.eng.consolepec.xmlplugin.pratica.factory;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.consolepec.xmlplugin.factory.XMLTask;
import it.eng.consolepec.xmlplugin.pratica.albopretorio.DatiFascicoloAlboPretorio;
import it.eng.consolepec.xmlplugin.pratica.albopretorio.FascicoloAlboPretorio;
import it.eng.consolepec.xmlplugin.pratica.elettorale.DatiFascicoloElettoraleComunicazioni;
import it.eng.consolepec.xmlplugin.pratica.elettorale.DatiFascicoloElettoraleElettore;
import it.eng.consolepec.xmlplugin.pratica.elettorale.DatiFascicoloElettoraleGenerico;
import it.eng.consolepec.xmlplugin.pratica.elettorale.FascicoloElettoraleComunicazioni;
import it.eng.consolepec.xmlplugin.pratica.elettorale.FascicoloElettoraleElettore;
import it.eng.consolepec.xmlplugin.pratica.elettorale.FascicoloElettoraleGenerico;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.pratica.fatturazione.DatiFascicoloFatturazione;
import it.eng.consolepec.xmlplugin.pratica.fatturazione.FascicoloFatturazione;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiFascicoloModulistica;
import it.eng.consolepec.xmlplugin.pratica.modulistica.FascicoloModulistica;
import it.eng.consolepec.xmlplugin.pratica.personale.DatiFascicoloPersonale;
import it.eng.consolepec.xmlplugin.pratica.personale.FascicoloPersonale;
import it.eng.consolepec.xmlplugin.pratica.riservato.DatiFascicoloRiservato;
import it.eng.consolepec.xmlplugin.pratica.riservato.FascicoloRiservato;
import it.eng.consolepec.xmlplugin.pratica.sport.DatiFascicoloSaluteSport;
import it.eng.consolepec.xmlplugin.pratica.sport.DatiFascicoloSportBorgoPanigale;
import it.eng.consolepec.xmlplugin.pratica.sport.DatiFascicoloSportBorgoPanigaleReno;
import it.eng.consolepec.xmlplugin.pratica.sport.DatiFascicoloSportNavile;
import it.eng.consolepec.xmlplugin.pratica.sport.DatiFascicoloSportPorto;
import it.eng.consolepec.xmlplugin.pratica.sport.DatiFascicoloSportPortoSaragozza;
import it.eng.consolepec.xmlplugin.pratica.sport.DatiFascicoloSportReno;
import it.eng.consolepec.xmlplugin.pratica.sport.DatiFascicoloSportSanDonato;
import it.eng.consolepec.xmlplugin.pratica.sport.DatiFascicoloSportSanDonatoSanVitale;
import it.eng.consolepec.xmlplugin.pratica.sport.DatiFascicoloSportSanVitale;
import it.eng.consolepec.xmlplugin.pratica.sport.DatiFascicoloSportSantoStefano;
import it.eng.consolepec.xmlplugin.pratica.sport.DatiFascicoloSportSaragozza;
import it.eng.consolepec.xmlplugin.pratica.sport.DatiFascicoloSportSavena;
import it.eng.consolepec.xmlplugin.pratica.sport.FascicoloSaluteSport;
import it.eng.consolepec.xmlplugin.pratica.sport.FascicoloSportBorgoPanigale;
import it.eng.consolepec.xmlplugin.pratica.sport.FascicoloSportBorgoPanigaleReno;
import it.eng.consolepec.xmlplugin.pratica.sport.FascicoloSportNavile;
import it.eng.consolepec.xmlplugin.pratica.sport.FascicoloSportPorto;
import it.eng.consolepec.xmlplugin.pratica.sport.FascicoloSportPortoSaragozza;
import it.eng.consolepec.xmlplugin.pratica.sport.FascicoloSportReno;
import it.eng.consolepec.xmlplugin.pratica.sport.FascicoloSportSanDonato;
import it.eng.consolepec.xmlplugin.pratica.sport.FascicoloSportSanDonatoSanVitale;
import it.eng.consolepec.xmlplugin.pratica.sport.FascicoloSportSanVitale;
import it.eng.consolepec.xmlplugin.pratica.sport.FascicoloSportSantoStefano;
import it.eng.consolepec.xmlplugin.pratica.sport.FascicoloSportSaragozza;
import it.eng.consolepec.xmlplugin.pratica.sport.FascicoloSportSavena;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.TaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.albopretorio.DatiGestioneFascicoloAlboPretorioTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.albopretorio.GestioneFascicoloAlboPretorioTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.albopretorio.XMLGestioneFascicoloAlboPretorioTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.elettorale.DatiGestioneFascicoloElettoraleComunicazioniTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.elettorale.DatiGestioneFascicoloElettoraleElettoreTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.elettorale.DatiGestioneFascicoloElettoraleGenericoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.elettorale.GestioneFascicoloElettoraleComunicazioniTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.elettorale.GestioneFascicoloElettoraleElettoreTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.elettorale.GestioneFascicoloElettoraleGenericoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.elettorale.XMLGestioneFascicoloElettoraleComunicazioniTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.elettorale.XMLGestioneFascicoloElettoraleElettoreTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.elettorale.XMLGestioneFascicoloElettoraleGenericoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fatturazione.DatiGestioneFascicoloFatturazioneTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fatturazione.GestioneFascicoloFatturazioneTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fatturazione.XMLGestioneFascicoloFatturazioneTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.modulistica.DatiGestioneFascicoloModulisticaTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.modulistica.GestioneFascicoloModulisticaTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.modulistica.XMLGestioneFascicoloModulisticaTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.personale.DatiGestioneFascicoloPersonaleTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.personale.GestioneFascicoloPersonaleTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.personale.XMLGestioneFascicoloPersonaleTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.protocollo.GestioneFascicoloGenericoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.riservato.DatiGestioneFascicoloRiservatoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.riservato.GestioneFascicoloRiservatoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.riservato.XMLGestioneFascicoloRiservatoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.DatiGestioneFascicoloSaluteSportTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.DatiGestioneFascicoloSportBorgoPanigaleRenoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.DatiGestioneFascicoloSportBorgoPanigaleTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.DatiGestioneFascicoloSportNavileTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.DatiGestioneFascicoloSportPortoSaragozzaTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.DatiGestioneFascicoloSportPortoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.DatiGestioneFascicoloSportRenoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.DatiGestioneFascicoloSportSanDonatoSanVitaleTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.DatiGestioneFascicoloSportSanDonatoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.DatiGestioneFascicoloSportSanVitaleTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.DatiGestioneFascicoloSportSantoStefanoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.DatiGestioneFascicoloSportSaragozzaTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.GestioneFascicoloSaluteSportTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.GestioneFascicoloSportBorgoPanigaleRenoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.GestioneFascicoloSportBorgoPanigaleTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.GestioneFascicoloSportNavileTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.GestioneFascicoloSportPortoSaragozzaTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.GestioneFascicoloSportPortoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.GestioneFascicoloSportRenoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.GestioneFascicoloSportSanDonatoSanVitaleTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.GestioneFascicoloSportSanDonatoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.GestioneFascicoloSportSanVitaleTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.GestioneFascicoloSportSantoStefanoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.GestioneFascicoloSportSaragozzaTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.GestioneFascicoloSportSavenaTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.XMLGestioneFascicoloSaluteSportTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.XMLGestioneFascicoloSportBorgoPanigaleRenoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.XMLGestioneFascicoloSportBorgoPanigaleTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.XMLGestioneFascicoloSportNavileTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.XMLGestioneFascicoloSportPortoSaragozzaTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.XMLGestioneFascicoloSportPortoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.XMLGestioneFascicoloSportRenoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.XMLGestioneFascicoloSportSanDonatoSanVitaleTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.XMLGestioneFascicoloSportSanDonatoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.XMLGestioneFascicoloSportSanVitaleTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.XMLGestioneFascicoloSportSantoStefanoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.XMLGestioneFascicoloSportSaragozzaTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.XMLGestioneFascicoloSportSavenaTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.XMLRiattivaFascicoloTask;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class FascicoliFactory {

	//////////
	
	public static DatiFascicolo.Builder getBuilder(String tipoPratica) {
		
		if (PraticaUtil.isFascicolo(tipoPratica)) {
			TipologiaPratica tp = new TipologiaPratica(tipoPratica);
			return getBuilder(tp);
		}
		
		return null;
	}
	
	public static DatiFascicolo.Builder getBuilder(TipologiaPratica tipoPratica) {
		
		if (PraticaUtil.isFascicolo(tipoPratica)) {
			
			if(tipoPratica.equals(TipologiaPratica.FASCICOLO) || tipoPratica.equals(TipologiaPratica.FASCICOLO_PERSONALE))
				return new DatiFascicoloPersonale.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_ALBO_PRETORIO))
				return new DatiFascicoloAlboPretorio.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_RISERVATO))
				return new DatiFascicoloRiservato.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_FATTURAZIONE_ELETTRONICA))
				return new DatiFascicoloFatturazione.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_BORGOPANIGALE))
				return new DatiFascicoloSportBorgoPanigale.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_NAVILE))
				return new DatiFascicoloSportNavile.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_PORTO))
				return new DatiFascicoloSportPorto.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_RENO))
				return new DatiFascicoloSportReno.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SANDONATO))
				return new DatiFascicoloSportSanDonato.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SANTOSTEFANO))
				return new DatiFascicoloSportSantoStefano.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SANVITALE))
				return new DatiFascicoloSportSanVitale.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SARAGOZZA))
				return new DatiFascicoloSportSaragozza.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SAVENA))
				return new DatiFascicoloSportSavena.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_BORGOPANIGALE_RENO))
				return new DatiFascicoloSportBorgoPanigaleReno.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_PORTO_SARAGOZZA))
				return new DatiFascicoloSportPortoSaragozza.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SANDONATO_SANVITALE))
				return new DatiFascicoloSportSanDonatoSanVitale.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SALUTE_SPORT))
				return new DatiFascicoloSaluteSport.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_ELETTORALE_ELETTORE))
				return new DatiFascicoloElettoraleElettore.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_ELETTORALE_COMUNICAZIONI))
				return new DatiFascicoloElettoraleComunicazioni.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_ELETTORALE_GENERICO))
				return new DatiFascicoloElettoraleGenerico.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_MODULISTICA))
				return new DatiFascicoloModulistica.Builder();
			else 
				return new DatiFascicolo.Builder();
		} else
			return null;
	}
	

	//////////
	
	public static <S extends Fascicolo> Class<S> getPraticaClass(String tipoPratica) {
		if (PraticaUtil.isFascicolo(tipoPratica)) {
			TipologiaPratica tp = new TipologiaPratica(tipoPratica);
			return getPraticaClass(tp);
		}
		
		return null;
	}
	
	public static <S extends Fascicolo> Class<S> getPraticaClass(TipologiaPratica tipoPratica) {
		
		if (PraticaUtil.isFascicolo(tipoPratica)) {
			
			if(tipoPratica.equals(TipologiaPratica.FASCICOLO) || tipoPratica.equals(TipologiaPratica.FASCICOLO_PERSONALE))
				return (Class<S>) FascicoloPersonale.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_ALBO_PRETORIO))
				return (Class<S>) FascicoloAlboPretorio.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_RISERVATO))
				return (Class<S>) FascicoloRiservato.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_FATTURAZIONE_ELETTRONICA))
				return (Class<S>) FascicoloFatturazione.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_BORGOPANIGALE))
				return (Class<S>) FascicoloSportBorgoPanigale.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_NAVILE))
				return (Class<S>) FascicoloSportNavile.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_PORTO))
				return (Class<S>) FascicoloSportPorto.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_RENO))
				return (Class<S>) FascicoloSportReno.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SANDONATO))
				return (Class<S>) FascicoloSportSanDonato.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SANTOSTEFANO))
				return (Class<S>) FascicoloSportSantoStefano.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SANVITALE))
				return (Class<S>) FascicoloSportSanVitale.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SARAGOZZA))
				return (Class<S>) FascicoloSportSaragozza.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SAVENA))
				return (Class<S>) FascicoloSportSavena.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_BORGOPANIGALE_RENO))
				return (Class<S>) FascicoloSportBorgoPanigaleReno.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_PORTO_SARAGOZZA))
				return (Class<S>) FascicoloSportPortoSaragozza.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SANDONATO_SANVITALE))
				return (Class<S>) FascicoloSportSanDonatoSanVitale.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SALUTE_SPORT))
				return (Class<S>) FascicoloSaluteSport.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_ELETTORALE_ELETTORE))
				return (Class<S>) FascicoloElettoraleElettore.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_ELETTORALE_COMUNICAZIONI))
				return (Class<S>) FascicoloElettoraleComunicazioni.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_ELETTORALE_GENERICO))
				return (Class<S>) FascicoloElettoraleGenerico.class;
			else if (tipoPratica.equals(TipologiaPratica.FASCICOLO_MODULISTICA))
				return (Class<S>) FascicoloModulistica.class;
			else 
				return (Class<S>) Fascicolo.class;
			
		} else 
			return null;
	}

	//////////
	
	public static XMLTask<?> getTaskImpl(String tipoPratica) {
		
		if (PraticaUtil.isFascicolo(tipoPratica)) {
			TipologiaPratica tp = new TipologiaPratica(tipoPratica);
			return getTaskImpl(tp);
		}
		
		return null;
	}
	
	/* Task del Fasciolo */
	public static XMLTask<?> getTaskImpl(TipologiaPratica tipoPratica) {
		
		if (PraticaUtil.isFascicolo(tipoPratica)) {
			
			if(tipoPratica.equals(TipologiaPratica.FASCICOLO) || tipoPratica.equals(TipologiaPratica.FASCICOLO_PERSONALE))
				return new XMLGestioneFascicoloPersonaleTask();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_ALBO_PRETORIO))
				return new XMLGestioneFascicoloAlboPretorioTask();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_RISERVATO))
				return new XMLGestioneFascicoloRiservatoTask();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_FATTURAZIONE_ELETTRONICA))
				return new XMLGestioneFascicoloFatturazioneTask();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_BORGOPANIGALE))
				return new XMLGestioneFascicoloSportBorgoPanigaleTask();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_NAVILE))
				return new XMLGestioneFascicoloSportNavileTask();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_PORTO))
				return new XMLGestioneFascicoloSportPortoTask();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_RENO))
				return new XMLGestioneFascicoloSportRenoTask();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SANDONATO))
				return new XMLGestioneFascicoloSportSanDonatoTask();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SANTOSTEFANO))
				return new XMLGestioneFascicoloSportSantoStefanoTask();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SANVITALE))
				return new XMLGestioneFascicoloSportSanVitaleTask();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SARAGOZZA))
				return new XMLGestioneFascicoloSportSaragozzaTask();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SAVENA))
				return new XMLGestioneFascicoloSportSavenaTask();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_BORGOPANIGALE_RENO))
				return new XMLGestioneFascicoloSportBorgoPanigaleRenoTask();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_PORTO_SARAGOZZA))
				return new XMLGestioneFascicoloSportPortoSaragozzaTask();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SANDONATO_SANVITALE))
				return new XMLGestioneFascicoloSportSanDonatoSanVitaleTask();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SALUTE_SPORT))
				return new XMLGestioneFascicoloSaluteSportTask();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_ELETTORALE_ELETTORE))
				return new XMLGestioneFascicoloElettoraleElettoreTask();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_ELETTORALE_COMUNICAZIONI))
				return new XMLGestioneFascicoloElettoraleComunicazioniTask();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_ELETTORALE_GENERICO))
				return new XMLGestioneFascicoloElettoraleGenericoTask();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_MODULISTICA))
				return new XMLGestioneFascicoloModulisticaTask();
			else
				return new XMLTaskFascicolo();
			
		} else
			return null;
	}

	/* Task riattivazione fascicolo */
	public static XMLTask<?> getRiattivaTaskImpl(String tipoPratica) {
		if (PraticaUtil.isFascicolo(tipoPratica)) {
			TipologiaPratica tp = new TipologiaPratica(tipoPratica);
			return getRiattivaTaskImpl(tp);
		}
		
		return null;
	}
	
	public static XMLTask<?> getRiattivaTaskImpl(TipologiaPratica tipoPratica) {
		
		if (PraticaUtil.isFascicolo(tipoPratica))
			return new XMLRiattivaFascicoloTask();
	
		return null;
		
	}
	
	//////////
	public static DatiGestioneFascicoloTask.Builder getBuilderTask(String tipoPratica) {
		if (PraticaUtil.isFascicolo(tipoPratica)) {
			TipologiaPratica tp = new TipologiaPratica(tipoPratica);
			return getBuilderTask(tp);
		}
		
		return null;
	}
	
	public static DatiGestioneFascicoloTask.Builder getBuilderTask(TipologiaPratica tipoPratica) {
		
		if (PraticaUtil.isFascicolo(tipoPratica)) {
			
			if(tipoPratica.equals(TipologiaPratica.FASCICOLO) || tipoPratica.equals(TipologiaPratica.FASCICOLO_PERSONALE))
				return new DatiGestioneFascicoloPersonaleTask.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_ALBO_PRETORIO))
				return new DatiGestioneFascicoloAlboPretorioTask.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_RISERVATO))
				return new DatiGestioneFascicoloRiservatoTask.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_FATTURAZIONE_ELETTRONICA))
				return new DatiGestioneFascicoloFatturazioneTask.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_BORGOPANIGALE))
				return new DatiGestioneFascicoloSportBorgoPanigaleTask.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_NAVILE))
				return new DatiGestioneFascicoloSportNavileTask.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_PORTO))
				return new DatiGestioneFascicoloSportPortoTask.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_RENO))
				return new DatiGestioneFascicoloSportRenoTask.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SANDONATO))
				return new DatiGestioneFascicoloSportSanDonatoTask.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SANTOSTEFANO))
				return new DatiGestioneFascicoloSportSantoStefanoTask.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SANVITALE))
				return new DatiGestioneFascicoloSportSanVitaleTask.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SARAGOZZA))
				return new DatiGestioneFascicoloSportSaragozzaTask.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SAVENA))
				return new DatiGestioneFascicoloSportSaragozzaTask.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_BORGOPANIGALE_RENO))
				return new DatiGestioneFascicoloSportBorgoPanigaleRenoTask.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_PORTO_SARAGOZZA))
				return new DatiGestioneFascicoloSportPortoSaragozzaTask.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SANDONATO_SANVITALE))
				return new DatiGestioneFascicoloSportSanDonatoSanVitaleTask.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SALUTE_SPORT))
				return new DatiGestioneFascicoloSaluteSportTask.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_ELETTORALE_ELETTORE))
				return new DatiGestioneFascicoloElettoraleElettoreTask.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_ELETTORALE_COMUNICAZIONI))
				return new DatiGestioneFascicoloElettoraleComunicazioniTask.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_ELETTORALE_GENERICO))
				return new DatiGestioneFascicoloElettoraleGenericoTask.Builder();
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_MODULISTICA))
				return new DatiGestioneFascicoloModulisticaTask.Builder();
			else
				return new DatiGestioneFascicoloTask.Builder();
				
		} else 
			return null;
	}

	//////////
	public static <Q extends TaskFascicolo> Class<Q> getTaskClass(String tipoPratica) {
		if (PraticaUtil.isFascicolo(tipoPratica)) {
			TipologiaPratica tp = new TipologiaPratica(tipoPratica);
			return getTaskClass(tp);
		}
		
		return null;
	}
	
	public static <Q extends TaskFascicolo> Class<Q> getTaskClass(TipologiaPratica tipoPratica) {
	
		if (PraticaUtil.isFascicolo(tipoPratica)) {
			
			if(tipoPratica.equals(TipologiaPratica.FASCICOLO) || tipoPratica.equals(TipologiaPratica.FASCICOLO_PERSONALE))
				return (Class<Q>) GestioneFascicoloPersonaleTask.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_ALBO_PRETORIO))
				return (Class<Q>) GestioneFascicoloAlboPretorioTask.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_RISERVATO))
				return (Class<Q>) GestioneFascicoloRiservatoTask.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_FATTURAZIONE_ELETTRONICA))
				return (Class<Q>) GestioneFascicoloFatturazioneTask.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_BORGOPANIGALE))
				return (Class<Q>) GestioneFascicoloSportBorgoPanigaleTask.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_NAVILE))
				return (Class<Q>) GestioneFascicoloSportNavileTask.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_PORTO))
				return (Class<Q>) GestioneFascicoloSportPortoTask.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_RENO))
				return (Class<Q>) GestioneFascicoloSportRenoTask.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SANDONATO))
				return (Class<Q>) GestioneFascicoloSportSanDonatoTask.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SANTOSTEFANO))
				return (Class<Q>) GestioneFascicoloSportSantoStefanoTask.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SANVITALE))
				return (Class<Q>) GestioneFascicoloSportSanVitaleTask.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SARAGOZZA))
				return (Class<Q>) GestioneFascicoloSportSaragozzaTask.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SAVENA))
				return (Class<Q>) GestioneFascicoloSportSavenaTask.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_BORGOPANIGALE_RENO))
				return (Class<Q>) GestioneFascicoloSportBorgoPanigaleRenoTask.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_PORTO_SARAGOZZA))
				return (Class<Q>) GestioneFascicoloSportPortoSaragozzaTask.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SPORT_SANDONATO_SANVITALE))
				return (Class<Q>) GestioneFascicoloSportSanDonatoSanVitaleTask.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_SALUTE_SPORT))
				return (Class<Q>) GestioneFascicoloSaluteSportTask.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_ELETTORALE_ELETTORE))
				return (Class<Q>) GestioneFascicoloElettoraleElettoreTask.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_ELETTORALE_COMUNICAZIONI))
				return (Class<Q>) GestioneFascicoloElettoraleComunicazioniTask.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_ELETTORALE_GENERICO))
				return (Class<Q>) GestioneFascicoloElettoraleGenericoTask.class;
			else if(tipoPratica.equals(TipologiaPratica.FASCICOLO_MODULISTICA))
				return (Class<Q>) GestioneFascicoloModulisticaTask.class;
			else 
				return (Class<Q>) GestioneFascicoloGenericoTask.class;
			
		} else 
			return null;
	}

}
