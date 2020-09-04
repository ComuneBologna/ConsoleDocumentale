package it.eng.consolepec.xmlplugin.tasks.esecuzioni;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo.StepIter;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo.DatoAggiuntivoVisitor;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoAnagrafica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoTabella;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreMultiplo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreSingolo;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.ConseguenzaEsecuzione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.ConseguenzaEsecuzione.CambiaStepIterConseguenzaEsecuzione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.ConseguenzaEsecuzione.ModificaDatoAggiuntivoValoreConseguenzaEsecuzione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.ConseguenzaEsecuzione.ModificaDatoAggiuntivoValoriConseguenzaEsecuzione;
import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.TaskFascicolo;

/**
 *
 * @author biagiot
 *
 */
public interface Esecuzione<T extends Task<?>, CE extends ConseguenzaEsecuzione>  {

	void esegui(T task, CE conseguenzaEsecuzione);


	/*
	 *
	 * ESECUZIONI TASK FASCICOLO
	 *
	 */

	public static class CambiaStepIterEsecuzione implements Esecuzione<TaskFascicolo<?>, CambiaStepIterConseguenzaEsecuzione> {

		@Override
		public void esegui(final TaskFascicolo<?> task, final CambiaStepIterConseguenzaEsecuzione conseguenzaEsecuzione) {

			final DatiFascicolo datiFascicolo = (DatiFascicolo) task.getEnclosingPratica().getDati();
			AnagraficaFascicolo af = datiFascicolo.getAnagraficaFascicolo();
			if (af.getStepIterAbilitati() != null
					&& !(datiFascicolo.getStepIter() != null && datiFascicolo.getStepIter().getNome() != null && conseguenzaEsecuzione.getValore().equals(datiFascicolo.getStepIter().getNome()))) {

				for (StepIter stepIter : af.getStepIterAbilitati()) {
					if (conseguenzaEsecuzione.getValore().equals(stepIter.getNome())) {

						if (stepIter.getDestinatariNotifica() != null && !stepIter.getDestinatariNotifica().isEmpty())
							task.cambiaStep(stepIter.getNome(), stepIter.isFinale(), stepIter.isFinale(), stepIter.isCreaBozza(), stepIter.getDestinatariNotifica());
						else
							task.cambiaStep(stepIter.getNome(), stepIter.isFinale(), stepIter.isFinale(), stepIter.isCreaBozza());

						break;
					}
				}
			}
		}
	}

	public static class ModificaDatoAggiuntivoValoreEsecuzione implements Esecuzione<TaskFascicolo<?>, ModificaDatoAggiuntivoValoreConseguenzaEsecuzione> {

		@Override
		public void esegui(final TaskFascicolo<?> task, final ModificaDatoAggiuntivoValoreConseguenzaEsecuzione conseguenzaEsecuzione) {
			
			final DatiFascicolo datiFascicolo = (DatiFascicolo) task.getEnclosingPratica().getDati();
			AnagraficaFascicolo af = datiFascicolo.getAnagraficaFascicolo();
			if (af.getDatiAggiuntivi() != null && datiFascicolo.getDatiAggiuntivi() != null) {

				for (DatoAggiuntivo datoAggiuntivo : af.getDatiAggiuntivi()) {
					
					if (GenericsUtil.isNotNullOrEmpty(datoAggiuntivo.getNome()) && datoAggiuntivo.getNome().equals(conseguenzaEsecuzione.getNome())) {
						datoAggiuntivo.accept(new DatoAggiuntivoVisitor() {
							
							@Override
							public void visit(DatoAggiuntivoTabella datoAggiuntivoTabella) {
								// TODO
							}
							
							@Override
							public void visit(DatoAggiuntivoAnagrafica datoAggiuntivoAnagrafica) {
								DatoAggiuntivoAnagrafica datoModificato = datoAggiuntivoAnagrafica.clona();
								datoModificato.setValore(conseguenzaEsecuzione.getValore());
								task.modificaDatoAggiuntivo(datoModificato);
							}
							
							@Override
							public void visit(DatoAggiuntivoValoreMultiplo datoAggiuntivoValoreMultiplo) {
								// NOP
							}
							
							@Override
							public void visit(DatoAggiuntivoValoreSingolo datoAggiuntivoValoreSingolo) {
								DatoAggiuntivoValoreSingolo datoModificato = datoAggiuntivoValoreSingolo.clona();
								datoModificato.setValore(conseguenzaEsecuzione.getValore());
								task.modificaDatoAggiuntivo(datoModificato);
							}
						});
						
						break;
					}
				}
			}
		}
	}

	public static class ModificaDatoAggiuntivoValoriEsecuzione implements Esecuzione<TaskFascicolo<?>, ModificaDatoAggiuntivoValoriConseguenzaEsecuzione> {

		@Override
		public void esegui(final TaskFascicolo<?> task, final ModificaDatoAggiuntivoValoriConseguenzaEsecuzione conseguenzaEsecuzione) {

			final DatiFascicolo datiFascicolo = (DatiFascicolo) task.getEnclosingPratica().getDati();
			AnagraficaFascicolo af = datiFascicolo.getAnagraficaFascicolo();
			if (af.getDatiAggiuntivi() != null && datiFascicolo.getDatiAggiuntivi() != null) {

				for (DatoAggiuntivo datoAggiuntivo : af.getDatiAggiuntivi()) {
					
					if (GenericsUtil.isNotNullOrEmpty(datoAggiuntivo.getNome()) && datoAggiuntivo.getNome().equals(conseguenzaEsecuzione.getNome())) {
						datoAggiuntivo.accept(new DatoAggiuntivoVisitor() {
							
							@Override
							public void visit(DatoAggiuntivoTabella datoAggiuntivoTabella) {
								// TODO
							}
							
							@Override
							public void visit(DatoAggiuntivoAnagrafica datoAggiuntivoAnagrafica) {
								// NOP
							}
							
							@Override
							public void visit(DatoAggiuntivoValoreMultiplo datoAggiuntivoValoreMultiplo) {
								DatoAggiuntivoValoreMultiplo datoModificato = datoAggiuntivoValoreMultiplo.clona();
								datoModificato.getValori().clear();
								datoModificato.getValori().addAll(conseguenzaEsecuzione.getValori());
								task.modificaDatoAggiuntivo(datoModificato);
							}
							
							@Override
							public void visit(DatoAggiuntivoValoreSingolo datoAggiuntivoValoreSingolo) {
								// NOP
							}
						});
						
						break;
					}
				}
			}
		}
	}
}