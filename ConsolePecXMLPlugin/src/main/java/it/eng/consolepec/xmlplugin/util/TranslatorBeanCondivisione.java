package it.eng.consolepec.xmlplugin.util;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione;
import it.eng.consolepec.xmlplugin.jaxb.Collegamenti;
import it.eng.consolepec.xmlplugin.jaxb.ElencoCondivisioni;
import it.eng.consolepec.xmlplugin.jaxb.Operazioni;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Collegamento;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask.Condivisione;

public class TranslatorBeanCondivisione {

	// trasforma l'oggetto operazioni jaxb in una lista Operazione di PluginXML
	public static TreeSet<Operazione> getListaOperazioni(Operazioni operazioni) {
		TreeSet<Operazione> listaOperazioni = new TreeSet<DatiFascicolo.Operazione>();
		if (operazioni == null) return listaOperazioni;
		for (it.eng.consolepec.xmlplugin.jaxb.Operazione o : operazioni.getListaOperazioni())
			listaOperazioni.add(new DatiFascicolo.Operazione(o.getNomeOperazione(), o.isAbilitata()));
		return listaOperazioni;
	}

	// trasfrorma l'oggetto PluginXML in un oggetto Operazioni Jaxb
	public static Operazioni getListaOperazioni(TreeSet<Operazione> listaOperazioni) {
		Operazioni op = new Operazioni();
		for (Operazione operazione : listaOperazioni) {
			it.eng.consolepec.xmlplugin.jaxb.Operazione opJaxb = new it.eng.consolepec.xmlplugin.jaxb.Operazione();
			opJaxb.setAbilitata(operazione.isAbilitata());
			opJaxb.setNomeOperazione(operazione.getNomeOperazione());
			op.getListaOperazioni().add(opJaxb);
		}
		return op;
	}

	public static ElencoCondivisioni getElencoCondivisioni(TreeSet<Condivisione> condivisioni) {
		ElencoCondivisioni elencoCondivisioni = new ElencoCondivisioni();
		for (Condivisione c : condivisioni) {
			it.eng.consolepec.xmlplugin.jaxb.Condivisione cJaxb = new it.eng.consolepec.xmlplugin.jaxb.Condivisione();
			cJaxb.setNomeGruppo(c.getNomeGruppo());
			cJaxb.setDataInizio(DateUtils.dateToXMLGrCal(c.getDataInizio()));
			cJaxb.setOperazioni(getListaOperazioni(new TreeSet<Operazione>(c.getOperazioni())));
			elencoCondivisioni.getCondivisioni().add(cJaxb);
		}
		return elencoCondivisioni;
	}

	//
	public static TreeSet<Condivisione> getElencoCondivisioni(ElencoCondivisioni elencoCondivisioni) {
		TreeSet<DatiGestioneFascicoloTask.Condivisione> conds = new TreeSet<DatiGestioneFascicoloTask.Condivisione>();
		if (elencoCondivisioni == null) return conds;
		for (it.eng.consolepec.xmlplugin.jaxb.Condivisione condivisione : elencoCondivisioni.getCondivisioni())
			conds.add(new Condivisione(condivisione.getNomeGruppo(), DateUtils.xmlGrCalToDate(condivisione.getDataInizio()), getListaOperazioni(condivisione.getOperazioni())));
		return conds;
	}

	public static Collegamenti getCollegamenti(TreeSet<Collegamento> collegamenti) {
		Collegamenti elencoCollegamenti = new Collegamenti();
		for (Collegamento c : collegamenti) {
			it.eng.consolepec.xmlplugin.jaxb.Collegamento cJaxb = new it.eng.consolepec.xmlplugin.jaxb.Collegamento();
			cJaxb.setDataCollegamento(DateUtils.dateToXMLGrCal(c.getDataCollegamento()));
			cJaxb.setGruppoCondivisione(c.getNomeGruppo());
			cJaxb.setPathPratica(c.getPath());
			cJaxb.setAccessibileInLettura((c.getAccessibileInLettura() == null) ? true : c.getAccessibileInLettura());
			cJaxb.setOperazioni(getListaOperazioni(c.getOperazioniConsentite()));
			elencoCollegamenti.getElencoCollegamenti().add(cJaxb);
		}
		return elencoCollegamenti;
	}

	public static List<Collegamento> getCollegamenti(Collegamenti collegamenti) {
		ArrayList<Collegamento> listaCollegamenti = new ArrayList<DatiFascicolo.Collegamento>();
		if (collegamenti == null) return listaCollegamenti;
		for (it.eng.consolepec.xmlplugin.jaxb.Collegamento collegamento : collegamenti.getElencoCollegamenti()) {
			TreeSet<Operazione> listaOperazioni = getListaOperazioni(collegamento.getOperazioni());
			boolean accessibileInLettura = collegamento.isAccessibileInLettura() == null ? true : collegamento.isAccessibileInLettura();
			Collegamento col = new Collegamento(collegamento.getGruppoCondivisione(), collegamento.getPathPratica(), //
					DateUtils.xmlGrCalToDate(collegamento.getDataCollegamento()), accessibileInLettura, listaOperazioni);
			listaCollegamenti.add(col);
		}
		return listaCollegamenti;
	}

}
