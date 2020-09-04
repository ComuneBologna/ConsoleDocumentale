package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import java.util.List;
import java.util.TreeSet;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.GruppoVisibilita;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.CambiaVisibilitaFascicoloTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

public class CambiaVisibilitaFascicoloTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements CambiaVisibilitaFascicoloTaskApi {

	public CambiaVisibilitaFascicoloTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void rimuoviVisibilitaFascicolo(List<AnagraficaRuolo> gruppiDaRimuovere) throws PraticaException {
		TreeSet<GruppoVisibilita> gruppiAttuali = new TreeSet<GruppoVisibilita>(getDatiFascicolo().getGruppiVisibilita());
		TreeSet<GruppoVisibilita> nuoviGruppi = new TreeSet<>();

		List<String> ruoliDaRimuovere = Lists.newArrayList(Lists.transform(gruppiDaRimuovere, new Function<AnagraficaRuolo, String>() {

			@Override
			public String apply(AnagraficaRuolo input) {
				return input.getRuolo();
			}

		}));

		for (GruppoVisibilita gv : gruppiAttuali) {
			if (!ruoliDaRimuovere.contains(gv.getNomeGruppo())) {
				nuoviGruppi.add(gv);
			}
		}

		getDatiFascicolo().getGruppiVisibilita().clear();
		getDatiFascicolo().getGruppiVisibilita().addAll(nuoviGruppi);

		gestioneVisibilitaAllegati();

		List<String> etichetteRuoliDaRimuovere = Lists.newArrayList(Lists.transform(gruppiDaRimuovere, new Function<AnagraficaRuolo, String>() {

			@Override
			public String apply(AnagraficaRuolo input) {
				return input.getEtichetta();
			}

		}));

		generaEvento(EventiIterFascicolo.RIMOZIONE_VISIBILITA_FASCICOLO, task.getCurrentUser(), GenericsUtil.convertCollectionToString(etichetteRuoliDaRimuovere));

	}

	@Override
	public void aggiungiVisibilita(List<GruppoVisibilita> gruppiDaAggiungere) throws PraticaException {
		TreeSet<GruppoVisibilita> gruppiAttuali = new TreeSet<GruppoVisibilita>(getDatiFascicolo().getGruppiVisibilita());

		getDatiFascicolo().getGruppiVisibilita().clear();
		getDatiFascicolo().getGruppiVisibilita().addAll(gruppiAttuali);
		getDatiFascicolo().getGruppiVisibilita().addAll(gruppiDaAggiungere);

		gestioneVisibilitaAllegati();
	}

	@Override
	public void aggiungiVisibilitaFascicolo(List<AnagraficaRuolo> gruppiDaAggiungere) throws PraticaException {
		TreeSet<GruppoVisibilita> gruppiAttuali = new TreeSet<GruppoVisibilita>(getDatiFascicolo().getGruppiVisibilita());
		TreeSet<GruppoVisibilita> nuoviGruppi = new TreeSet<>();

		for (AnagraficaRuolo ar : gruppiDaAggiungere) {
			GruppoVisibilita gv = new GruppoVisibilita(ar.getRuolo());
			nuoviGruppi.add(gv);
		}

		getDatiFascicolo().getGruppiVisibilita().clear();
		getDatiFascicolo().getGruppiVisibilita().addAll(gruppiAttuali);
		getDatiFascicolo().getGruppiVisibilita().addAll(nuoviGruppi);

		gestioneVisibilitaAllegati();

		List<String> etichetteRuoliDaAgg = Lists.newArrayList(Lists.transform(gruppiDaAggiungere, new Function<AnagraficaRuolo, String>() {

			@Override
			public String apply(AnagraficaRuolo input) {
				return input.getEtichetta();
			}

		}));

		generaEvento(EventiIterFascicolo.AGGIUNTA_VISIBILITA_FASCICOLO, task.getCurrentUser(), GenericsUtil.convertCollectionToString(etichetteRuoliDaAgg));
	}

	private void gestioneVisibilitaAllegati() {
		for (Allegato allg : getDatiFascicolo().getAllegati()) {
			TreeSet<GruppoVisibilita> gruppiVisibilitaFascicolo = new TreeSet<DatiPratica.GruppoVisibilita>(getDatiFascicolo().getGruppiVisibilita());
			TreeSet<GruppoVisibilita> gruppiVisibilitaAllegato = allg.getGruppiVisibilita();

			gruppiVisibilitaFascicolo.retainAll(gruppiVisibilitaAllegato);

			allg.getGruppiVisibilita().clear();
			allg.getGruppiVisibilita().addAll(gruppiVisibilitaFascicolo);
		}
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.CAMBIA_VISIBILITA_FASCICOLO;
	}
}
