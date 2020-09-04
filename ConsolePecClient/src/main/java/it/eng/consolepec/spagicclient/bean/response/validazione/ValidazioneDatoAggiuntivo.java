package it.eng.consolepec.spagicclient.bean.response.validazione;

import it.eng.consolepec.spagicclient.bean.request.datiaggiuntivi.DatoAggiuntivo;

import java.util.ArrayList;
import java.util.List;

public class ValidazioneDatoAggiuntivo {
	
	
	private DatoAggiuntivo datoAggiuntivo;
	private boolean valido;
	private List<String> vieConLoStessoNomeSit = new ArrayList<String>();
	
		public ValidazioneDatoAggiuntivo(DatoAggiuntivo datoAggiuntivo, boolean valido) {
		super();
		this.datoAggiuntivo = datoAggiuntivo;
		this.valido = valido;
	}

	public boolean isValido() {
		return valido;
	}

	public List<String> getVieConLoStessoNome() {
		return vieConLoStessoNomeSit;
	}

	public DatoAggiuntivo getDatoAggiuntivo() {
		return datoAggiuntivo;
	}
	
	
}
