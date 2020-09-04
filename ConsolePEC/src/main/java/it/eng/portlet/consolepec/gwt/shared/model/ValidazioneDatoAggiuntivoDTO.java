package it.eng.portlet.consolepec.gwt.shared.model;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ValidazioneDatoAggiuntivoDTO implements IsSerializable {
	
	private DatoAggiuntivo valoreDatoAggiuntivo;
	private boolean valido;
	private List<String> vieConLoStessoNomeSit = new ArrayList<String>();
	
	protected ValidazioneDatoAggiuntivoDTO() {
		super();
	}

	public ValidazioneDatoAggiuntivoDTO(DatoAggiuntivo valoreDatoAggiuntivo, boolean valido) {
		super();
		this.valoreDatoAggiuntivo = valoreDatoAggiuntivo;
		this.valido = valido;
	}

	public boolean isValido() {
		return valido;
	}

	public List<String> getVieConLoStessoNome() {
		return vieConLoStessoNomeSit;
	}

	public DatoAggiuntivo getDatoAggiuntivo() {
		return valoreDatoAggiuntivo;
	}
}
