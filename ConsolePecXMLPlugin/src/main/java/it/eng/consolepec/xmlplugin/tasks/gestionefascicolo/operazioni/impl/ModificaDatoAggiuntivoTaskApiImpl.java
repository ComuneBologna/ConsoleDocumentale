package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import static it.eng.consolepec.xmlplugin.util.XmlPluginFormatUtil.format;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo.DatoAggiuntivoVisitor;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoAnagrafica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoTabella;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreMultiplo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreSingolo;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.CondizioneEsecuzione.ModificaDatoAggiuntivoValoreCondizioneEsecuzione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.CondizioneEsecuzione.ModificaDatoAggiuntivoValoriCondizioneEsecuzione;
import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ModificaDatoAggiuntivoTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

import java.util.List;

public class ModificaDatoAggiuntivoTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements ModificaDatoAggiuntivoTaskApi {

	public ModificaDatoAggiuntivoTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return (getDatiFascicolo().getStato().equals(Stato.IN_GESTIONE)
				|| getDatiFascicolo().getStato().equals(Stato.IN_VISIONE)
				|| getDatiFascicolo().getStato().equals(Stato.IN_AFFISSIONE)
				|| getDatiFascicolo().getStato().equals(Stato.DA_INOLTRARE_ESTERNO));
	}

	@Override
	protected TipoApiTask getTipoApiTask() {	
		return TipoApiTask.MODIFICA_DATO_AGGIUNTIVO;
	}
	
	@Override
	public void modificaDatoAggiuntivo(final DatoAggiuntivo datoAggiuntivo) {

		List<DatoAggiuntivo> datiAggiuntivi = getDatiFascicolo().getDatiAggiuntivi();
		
		for (DatoAggiuntivo datoDaModificare : datiAggiuntivi) {
			
			if (GenericsUtil.isNotNullOrEmpty(datoDaModificare.getNome()) && datoDaModificare.getNome().equals(datoAggiuntivo.getNome())) {
				
				datoDaModificare.accept(new DatoAggiuntivoVisitor() {
					
					@Override
					public void visit(DatoAggiuntivoTabella datoAggiuntivoTabella) {
						DatoAggiuntivoTabella dat = (DatoAggiuntivoTabella) datoAggiuntivo;
						
						if (!GenericsUtil.isSame(dat.getRighe(), datoAggiuntivoTabella.getRighe())) {
							datoAggiuntivoTabella.getRighe().clear();
							datoAggiuntivoTabella.getRighe().addAll(dat.getRighe());
							generaEvento(EventiIterFascicolo.MODIFICA_DATO_AGGIUNTIVO, task.getCurrentUser(), datoAggiuntivoTabella.getDescrizione());
						}
						
					}
					
					@Override
					public void visit(DatoAggiuntivoAnagrafica datoAggiuntivoAnagrafica) {
						DatoAggiuntivoAnagrafica daa = (DatoAggiuntivoAnagrafica) datoAggiuntivo;
						
						if (!GenericsUtil.isSame(daa.getValore(), datoAggiuntivoAnagrafica.getValore()) || !GenericsUtil.isSame(daa.getIdAnagrafica(), datoAggiuntivoAnagrafica.getIdAnagrafica())) {
							String valorePrecedente = datoAggiuntivoAnagrafica.getValore();
							datoAggiuntivoAnagrafica.setValore(daa.getValore());
							datoAggiuntivoAnagrafica.setIdAnagrafica(daa.getIdAnagrafica());
							eseguiEsecuzione(ModificaDatoAggiuntivoValoreCondizioneEsecuzione.builder().nome(daa.getNome()).valore(daa.getValore()).build());
							generaEvento(EventiIterFascicolo.MODIFICA_DATO_AGGIUNTIVO, task.getCurrentUser(), datoAggiuntivoAnagrafica.getDescrizione(), format(valorePrecedente), format(daa.getValore()));
						}
					}
					
					@Override
					public void visit(DatoAggiuntivoValoreMultiplo datoAggiuntivoValoreMultiplo) {
						DatoAggiuntivoValoreMultiplo dam = (DatoAggiuntivoValoreMultiplo) datoAggiuntivo;
						
						if (!GenericsUtil.isSame(dam.getValori(), datoAggiuntivoValoreMultiplo.getValori())) {
							List<String> valoriPrecedenti = datoAggiuntivoValoreMultiplo.getValori();
							datoAggiuntivoValoreMultiplo.getValori().clear();
							datoAggiuntivoValoreMultiplo.getValori().addAll(dam.getValori());
							eseguiEsecuzione(ModificaDatoAggiuntivoValoriCondizioneEsecuzione.builder().nome(dam.getNome()).valori(dam.getValori()).build());
							generaEvento(EventiIterFascicolo.MODIFICA_DATO_AGGIUNTIVO, task.getCurrentUser(), datoAggiuntivoValoreMultiplo.getDescrizione(), format(valoriPrecedenti), format(dam.getValori()));
						}
					}
					
					@Override
					public void visit(DatoAggiuntivoValoreSingolo datoAggiuntivoValoreSingolo) {
						DatoAggiuntivoValoreSingolo das = (DatoAggiuntivoValoreSingolo) datoAggiuntivo;
						
						if (!GenericsUtil.isSame(das.getValore(), datoAggiuntivoValoreSingolo.getValore())) {
							String valorePrecedente = datoAggiuntivoValoreSingolo.getValore();
							datoAggiuntivoValoreSingolo.setValore(das.getValore());
							eseguiEsecuzione(ModificaDatoAggiuntivoValoreCondizioneEsecuzione.builder().nome(das.getNome()).valore(das.getValore()).build());
							generaEvento(EventiIterFascicolo.MODIFICA_DATO_AGGIUNTIVO, task.getCurrentUser(), datoAggiuntivoValoreSingolo.getDescrizione(), format(valorePrecedente), format(das.getValore()));
						}
					}
				});
			}
		}
	}
}
