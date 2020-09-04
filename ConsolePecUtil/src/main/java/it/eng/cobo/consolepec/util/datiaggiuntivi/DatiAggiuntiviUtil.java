package it.eng.cobo.consolepec.util.datiaggiuntivi;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo.DatoAggiuntivoVisitor;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoAnagrafica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoTabella;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValore;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreMultiplo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreSingolo;
import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import it.eng.cobo.consolepec.commons.rubrica.Anagrafica.AnagraficaVisitor;
import it.eng.cobo.consolepec.commons.rubrica.PersonaFisica;
import it.eng.cobo.consolepec.commons.rubrica.PersonaGiuridica;
import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.cobo.consolepec.util.objects.Ref;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DatiAggiuntiviUtil {
	
	public static boolean isObbligatorio(DatoAggiuntivo dato){
		final Ref<Boolean> obbligatorio = Ref.of(false);
		dato.accept(new DatoAggiuntivo.DatoAggiuntivoValoreVisitorAdapter() {
			
			@Override
			public void visitValore(DatoAggiuntivoValore dato) {
				obbligatorio.set(dato.isObbligatorio());
			}
		});
		
		return obbligatorio.get();
	}
	
	public static boolean isEditabile(DatoAggiuntivo dato){
		final Ref<Boolean> editabile = Ref.of(false);
		dato.accept(new DatoAggiuntivo.DatoAggiuntivoValoreVisitorAdapter() {
			
			@Override
			public void visitValore(DatoAggiuntivoValore dato) {
				editabile.set(dato.isEditabile());
			}
			@Override
			public void visit(DatoAggiuntivoTabella dato) {
				editabile.set(dato.isEditabile());
			}
			
		});
		
		return editabile.get();
	}
	
	public static List<String> getValoriPredefiniti(DatoAggiuntivo dato){
		final Ref<List<String>> valoriPredefiniti = Ref.of(new ArrayList<String>());
		dato.accept(new DatoAggiuntivo.DatoAggiuntivoVisitorAdapter() {
			
			@Override
			public void visit(DatoAggiuntivoValoreMultiplo dag) {
				valoriPredefiniti.set(dag.getValoriPredefiniti());
			}
			
			@Override
			public void visit(DatoAggiuntivoValoreSingolo dag) {
				valoriPredefiniti.set(dag.getValoriPredefiniti());
			}
		});
		
		return valoriPredefiniti.get();
	}
	
	public static String getValore(DatoAggiuntivo dato){
		final Ref<String> valore = Ref.of(null);
		dato.accept(new DatoAggiuntivo.DatoAggiuntivoVisitorAdapter() {
			
			@Override
			public void visit(DatoAggiuntivoValoreSingolo dag) {
				valore.set(dag.getValore());
			}
			
			@Override
			public void visit(DatoAggiuntivoAnagrafica dag) {
				valore.set(dag.getValore());
			}
			
		});
		
		return valore.get();
	}
	
	public static List<String> getValori(DatoAggiuntivo dato){
		final Ref<List<String>> valori = Ref.of(new ArrayList<String>());
		dato.accept(new DatoAggiuntivo.DatoAggiuntivoVisitorAdapter() {
			
			@Override
			public void visit(DatoAggiuntivoValoreMultiplo dag) {
				valori.set(dag.getValori());
			}
		});
		
		return valori.get();
	}
	
	public static Double getIdAnagrafica(DatoAggiuntivo dato){
		final Ref<Double> idAnagrafica = Ref.of(null);
		dato.accept(new DatoAggiuntivo.DatoAggiuntivoVisitorAdapter() {
			
			@Override
			public void visit(DatoAggiuntivoAnagrafica dag) {
				idAnagrafica.set(dag.getIdAnagrafica());
			}
			
		});
		
		return idAnagrafica.get();
	}
	
	public static String calcolaNomeAnagrafica(Anagrafica a){
		final Ref<String> nomeAnagrafica = Ref.of(null);
		a.accept(new AnagraficaVisitor() {
			
			@Override
			public void visit(PersonaGiuridica personaGiuridica) {
				StringBuilder sb = new StringBuilder(personaGiuridica.getPartitaIva());
				
				if (personaGiuridica.getRagioneSociale() != null)
					sb.append(" - ").append(personaGiuridica.getRagioneSociale());
				
				nomeAnagrafica.set(sb.toString());
			}
			
			@Override
			public void visit(PersonaFisica personaFisica) {
				StringBuilder sb = new StringBuilder(personaFisica.getCodiceFiscale());
				
				if (personaFisica.getNome() != null || personaFisica.getCognome() != null) 
					sb.append(" - ");

				if (personaFisica.getNome() != null) 
					sb.append(personaFisica.getNome());

				if (personaFisica.getCognome() != null) {
					if (personaFisica.getNome() != null) 
						sb.append(" ");
					
					sb.append(personaFisica.getCognome());
				}

				nomeAnagrafica.set(sb.toString());
			}
		});
		
		return nomeAnagrafica.get();
	}
	
	public static Object datiAggiuntiviToList(List<DatoAggiuntivo> datiAggiuntivi) {
		final StringBuffer sb = new StringBuffer();
		final Iterator<DatoAggiuntivo> it = datiAggiuntivi.iterator();
		while (it.hasNext()) {
						
			it.next().accept(new DatoAggiuntivoVisitor() {
				
				@Override
				public void visit(DatoAggiuntivoTabella datoAggiuntivoTabella) {					
					// NOP
				}
				
				@Override
				public void visit(DatoAggiuntivoAnagrafica datoAggiuntivoAnagrafica) {					
					// NOP
				}
				
				@Override
				public void visit(DatoAggiuntivoValoreMultiplo datoAggiuntivoValoreMultiplo) {					
					// NOP
				}
				
				@Override
				public void visit(DatoAggiuntivoValoreSingolo datoAggiuntivoValoreSingolo) {
					sb.append(datoAggiuntivoValoreSingolo.getNome()).append("=").append(GenericsUtil.sanitizeNull(datoAggiuntivoValoreSingolo.getValore(), "-").toLowerCase());
					
					if (it.hasNext()) {
						sb.append(",");
						
					} else {
						sb.append(";");
					}
				}
			});			
		}
		
		return sb.toString();
	}
	
}
