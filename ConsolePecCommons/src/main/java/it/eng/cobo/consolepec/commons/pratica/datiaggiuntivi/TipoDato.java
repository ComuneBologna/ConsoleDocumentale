package it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi;

import lombok.Getter;


public enum TipoDato {
	
	MultiploRicerca(true){

		@Override
		public DatoAggiuntivo createDato() {
			return new DatoAggiuntivoValoreMultiplo();
		}
	}, 
	
	MultiploTesto(true){

		@Override
		public DatoAggiuntivo createDato() {
			return new DatoAggiuntivoValoreMultiplo();
		}
	}, 
	
	Tabella(false){
		@Override
		public DatoAggiuntivo createDato() {
			return new DatoAggiuntivoTabella();
		}
	}, 
	
	Anagrafica(false){
		
		@Override
		public DatoAggiuntivo createDato() {
			return new DatoAggiuntivoAnagrafica();
		}
	},
	
	Testo(false), 
	Lista(true), 
	Numerico(false), 
	Data(false), 
	Suggest(true), 
	IndirizzoVia(false), 
	IndirizzoCivico(false), 
	IndirizzoEsponente(false);
	
	@Getter
	private boolean valoriPredefiniti;
	
	private TipoDato(boolean hasValoriPredefiniti) {
		this.valoriPredefiniti = hasValoriPredefiniti;
	}
	
	public DatoAggiuntivo createDato() { // default
		return new DatoAggiuntivoValoreSingolo();
	}
}
