package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event;

import it.eng.portlet.consolepec.gwt.shared.dto.Element;
import it.eng.portlet.consolepec.gwt.shared.procedimenti.OperazioniProcedimento;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class GestioneProcedimentiDaProtocollazioneEvent extends GwtEvent<GestioneProcedimentiDaProtocollazioneEvent.GestioneProcedimentiDaProtocollazioneHandler> {

	public static Type<GestioneProcedimentiDaProtocollazioneHandler> TYPE = new Type<GestioneProcedimentiDaProtocollazioneHandler>();

	public interface GestioneProcedimentiDaProtocollazioneHandler extends EventHandler {
		void onGestioneProcedimentiDaProtocollazione(GestioneProcedimentiDaProtocollazioneEvent event);
	}

	private String pg, anno, pgCapofila, annoCapofila, tipologiaDocumento, titolo, rubrica, sezione, pathFascicolo;
	
	private Map<String, Map<String, Element>> map = new HashMap<String, Map<String, Element>>();
	private Set<String> praticheSelezionateProtocollate = new HashSet<String>();
	private OperazioniProcedimento operazione;

	public GestioneProcedimentiDaProtocollazioneEvent(OperazioniProcedimento operazione, String pg, String anno, String pgCapofila, String annoCapofila, String tipologiaDocumento, String titolo, String rubrica, String sezione, String pathFascicolo, Map<String, Map<String, Element>> map) {
		super();
		this.pg = pg;
		this.anno = anno;
		this.pgCapofila = pgCapofila;
		this.annoCapofila = annoCapofila;
		this.tipologiaDocumento = tipologiaDocumento;
		this.titolo = titolo;
		this.rubrica = rubrica;
		this.sezione = sezione;
		this.pathFascicolo = pathFascicolo;
		this.map.clear();
		this.map.putAll(map);
		this.setOperazione(operazione);
	}

	@Override
	protected void dispatch(GestioneProcedimentiDaProtocollazioneHandler handler) {
		handler.onGestioneProcedimentiDaProtocollazione(this);
	}

	@Override
	public Type<GestioneProcedimentiDaProtocollazioneHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<GestioneProcedimentiDaProtocollazioneHandler> getType() {
		return TYPE;
	}

	public String getPg() {
		return pg;
	}

	public void setPg(String pg) {
		this.pg = pg;
	}

	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getPgCapofila() {
		return pgCapofila;
	}

	public void setPgCapofila(String pgCapofila) {
		this.pgCapofila = pgCapofila;
	}

	public String getAnnoCapofila() {
		return annoCapofila;
	}

	public void setAnnoCapofila(String annoCapofila) {
		this.annoCapofila = annoCapofila;
	}

	public String getTipologiaDocumento() {
		return tipologiaDocumento;
	}

	public void setTipologiaDocumento(String tipologiaDocumento) {
		this.tipologiaDocumento = tipologiaDocumento;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getRubrica() {
		return rubrica;
	}

	public void setRubrica(String rubrica) {
		this.rubrica = rubrica;
	}

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getPathFascicolo() {
		return pathFascicolo;
	}

	public Map<String, Map<String, Element>> getMap() {
		return map;
	}

	public void setIdPraticheSelezionateProtocollate(Set<String> praticheProtocollate) {
		this.praticheSelezionateProtocollate = praticheProtocollate;
	}

	public Set<String> getPraticheSelezionateProtocollate() {
		return praticheSelezionateProtocollate;
	}

	public OperazioniProcedimento getOperazione() {
		return operazione;
	}

	public void setOperazione(OperazioniProcedimento operazione) {
		this.operazione = operazione;
	}

}
