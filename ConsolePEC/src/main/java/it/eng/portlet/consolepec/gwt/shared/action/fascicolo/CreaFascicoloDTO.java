package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.DatiProcedimento;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.Titolazione;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CreaFascicoloDTO implements IsSerializable {

	@Getter 
	@Setter 
	private String titolo;
	
	@Getter 
	@Setter 
	private String utente;
	
	@Getter 
	@Setter 
	private String note;
	
	@Getter 
	@Setter 
	private String clientID;
	
	@Getter
	@Setter
	private TipologiaPratica tipologiaFascicolo;
	
	@Getter 
	@Setter
	private String assegnatario;
	
	@Getter 
	@Setter 
	private Titolazione titolazione;
	
	@Getter 
	@Setter 
	private DatiProcedimento datiProcedimento;
	
	@Getter 
	private List<DatoAggiuntivo> datiAggiuntivi = new ArrayList<DatoAggiuntivo>();
	
	@Getter 
	@Setter 
	private boolean protocollazioneRiservata;
	

	public CreaFascicoloAction getAction() {
		CreaFascicoloAction creaFascicoloAction = new CreaFascicoloAction();
		creaFascicoloAction.setAssegnatario(assegnatario);
		creaFascicoloAction.setClientID(clientID);
		creaFascicoloAction.setNote(note);
		creaFascicoloAction.setTipologiaFascicolo(tipologiaFascicolo);
		creaFascicoloAction.setTitolo(titolo);
		creaFascicoloAction.setUtente(utente);
		creaFascicoloAction.getDatiAggiuntivi().addAll(datiAggiuntivi);

		return creaFascicoloAction;
	}
}
