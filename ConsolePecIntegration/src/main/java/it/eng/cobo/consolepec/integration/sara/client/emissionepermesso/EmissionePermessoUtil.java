package it.eng.cobo.consolepec.integration.sara.client.emissionepermesso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;

public class EmissionePermessoUtil {
	
	public static List<DatiAggiuntiviEmissionePermesso> validaDatiRichiesta(Set<DatiAggiuntiviEmissionePermesso> dati) {
		
		List<DatiAggiuntiviEmissionePermesso> datiObbligatoriMancanti = new ArrayList<DatiAggiuntiviEmissionePermesso>();
		
		Set<DatiAggiuntiviEmissionePermesso> datiObbligatori = new HashSet<DatiAggiuntiviEmissionePermesso>();
		for (DatiAggiuntiviEmissionePermesso dato : DatiAggiuntiviEmissionePermesso.values()) {
			if (dato.isObbligatorio()) {
				datiObbligatori.add(dato);
			}
		}
		
		Set<DatiAggiuntiviEmissionePermesso> datiObbligatoriPresenti = new HashSet<DatiAggiuntiviEmissionePermesso>();
		for (DatiAggiuntiviEmissionePermesso dato : dati) {
			if (dato.isObbligatorio())
				datiObbligatoriPresenti.add(dato);
		}
		
		if (datiObbligatori.size() > datiObbligatoriPresenti.size()) {
			datiObbligatori.removeAll(datiObbligatoriPresenti);
			datiObbligatoriMancanti = new ArrayList<DatiAggiuntiviEmissionePermesso>(datiObbligatori);
		}
		
		return datiObbligatoriMancanti;
	}
	
	public static enum DatiAggiuntiviEmissionePermesso {
		
		TITOLARE("titolare", "Titolare" , true),
		DATA_INIZIO_VALIDITA("data_inizio_validita", "Data inizio validità", true),
		DATA_FINE_VALIDITA("data_fine_validita", "Data fine validità", true),
		MOTIVO_RILASCIO("motivo_rilascio", "Motivo rilascio", false),
		STATO("stato", "Stato" , true),
		PROVINCIA("provincia", "Provincia", true),
		COMUNE("comune", "Comune", false),
		TARGA("targa1", "Targa", true),
		VEICOLO_ESTERO("veicolo_estero", "Veicolo estero", true),
		TIPO_VEICOLO("tipo_veicolo", "Tipo veicolo", false),
		COMPATIBILITA_AMBIENTALE("compatibilita_ambientale", "Compatibilità ambientale", false);
		
		@Getter private String idDatoAggiuntivo;
		@Getter private String descrizione;
		@Getter private boolean obbligatorio;
		
		private DatiAggiuntiviEmissionePermesso(String idDatoAggiuntivo, String descrizione, boolean obbligatorio) {
			this.idDatoAggiuntivo = idDatoAggiuntivo;
			this.descrizione = descrizione;
			this.obbligatorio = obbligatorio;
		}
	}

}
