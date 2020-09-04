package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event;

import it.eng.portlet.consolepec.gwt.shared.procedimenti.OperazioniProcedimento;

import java.util.Date;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class GestioneProcedimentiDaDettaglioFascicoloEvent extends GwtEvent<GestioneProcedimentiDaDettaglioFascicoloEvent.GestioneProcedimentiDaDettaglioFascicoloHandler> {

	public static Type<GestioneProcedimentiDaDettaglioFascicoloHandler> TYPE = new Type<GestioneProcedimentiDaDettaglioFascicoloHandler>();

	public interface GestioneProcedimentiDaDettaglioFascicoloHandler extends EventHandler {
		void onGestioneProcedimentiDaDettaglioFascicolo(GestioneProcedimentiDaDettaglioFascicoloEvent event);
	}

	private String pg, anno, pgCapofila, annoCapofila, tipologiaDocumento, titolo, rubrica, sezione, pathFascicolo, idPraticaProtocollataSelezionata;
	private OperazioniProcedimento operazione;
	
	private Date dataInizioDecorrenzaProcedimento;
	private Integer codTipologiaProcedimento;

	public GestioneProcedimentiDaDettaglioFascicoloEvent(OperazioniProcedimento operazione, String pg, String anno, String pgCapofila, String annoCapofila, String tipologiaDocumento, String titolo, String rubrica, String sezione, String pathFascicolo) {
		super();
		this.pg = pg;
		this.anno = anno;
		this.pgCapofila = pgCapofila;
		this.annoCapofila = annoCapofila;
		this.tipologiaDocumento = tipologiaDocumento;
		this.titolo = titolo;
		this.rubrica = rubrica;
		this.sezione = sezione;
		this.setOperazione(operazione);
		this.pathFascicolo = pathFascicolo;
	}

	@Override
	protected void dispatch(GestioneProcedimentiDaDettaglioFascicoloHandler handler) {
		handler.onGestioneProcedimentiDaDettaglioFascicolo(this);
	}

	@Override
	public Type<GestioneProcedimentiDaDettaglioFascicoloHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<GestioneProcedimentiDaDettaglioFascicoloHandler> getType() {
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

	public String getIdPraticaProtocollataSelezionata() {
		return idPraticaProtocollataSelezionata;
	}

	public void setIdPraticaProtocollataSelezionata(String idPraticaProtocollataSelezionata) {
		this.idPraticaProtocollataSelezionata = idPraticaProtocollataSelezionata;
	}

	public OperazioniProcedimento getOperazione() {
		return operazione;
	}

	public void setOperazione(OperazioniProcedimento operazione) {
		this.operazione = operazione;
	}

	public Date getDataInizioDecorrenzaProcedimento() {
		return dataInizioDecorrenzaProcedimento;
	}

	public void setDataInizioDecorrenzaProcedimento(Date dataInizioDecorrenzaProcedimento) {
		this.dataInizioDecorrenzaProcedimento = dataInizioDecorrenzaProcedimento;
	}

	public Integer getCodTipologiaProcedimento() {
		return codTipologiaProcedimento;
	}

	public void setCodTipologiaProcedimento(Integer codTipologiaProcedimento) {
		this.codTipologiaProcedimento = codTipologiaProcedimento;
	}

}
