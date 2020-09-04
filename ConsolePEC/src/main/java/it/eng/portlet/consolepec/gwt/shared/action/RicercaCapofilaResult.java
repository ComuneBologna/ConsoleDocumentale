package it.eng.portlet.consolepec.gwt.shared.action;

import java.util.Date;

import com.gwtplatform.dispatch.shared.Result;

public class RicercaCapofilaResult implements Result {

	private static final long serialVersionUID = 2583398544016066721L;

	private String numeroPg, numeroPgCapofila, oggetto, messageError, messageWarninig;
	private int annoPg, annoPgCapofila, titolo, rubrica, sezione;
	private Date dataProtocollazione;
	private boolean capofila, completo, error, warninig;

	public RicercaCapofilaResult() {
	}

	public String getNumeroPg() {
		return numeroPg;
	}

	public void setNumeroPg(String numeroPg) {
		this.numeroPg = numeroPg;
	}

	public String getNumeroPgCapofila() {
		return numeroPgCapofila;
	}

	public void setNumeroPgCapofila(String numeroPgCapofila) {
		this.numeroPgCapofila = numeroPgCapofila;
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public String getMessageError() {
		return messageError;
	}

	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}

	public int getAnnoPg() {
		return annoPg;
	}

	public void setAnnoPg(int annoPg) {
		this.annoPg = annoPg;
	}

	public int getAnnoPgCapofila() {
		return annoPgCapofila;
	}

	public void setAnnoPgCapofila(int annoPgCapofila) {
		this.annoPgCapofila = annoPgCapofila;
	}

	public int getTitolo() {
		return titolo;
	}

	public void setTitolo(int titolo) {
		this.titolo = titolo;
	}

	public int getRubrica() {
		return rubrica;
	}

	public void setRubrica(int rubrica) {
		this.rubrica = rubrica;
	}

	public int getSezione() {
		return sezione;
	}

	public void setSezione(int sezione) {
		this.sezione = sezione;
	}

	public Date getDataProtocollazione() {
		return dataProtocollazione;
	}

	public void setDataProtocollazione(Date dataProtocollazione) {
		this.dataProtocollazione = dataProtocollazione;
	}

	public boolean isCapofila() {
		return capofila;
	}

	public void setCapofila(boolean capofila) {
		this.capofila = capofila;
	}

	public boolean isCompleto() {
		return completo;
	}

	public void setCompleto(boolean completo) {
		this.completo = completo;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public boolean isWarninig() {
		return warninig;
	}

	public String getMessageWarninig() {
		return messageWarninig;
	}

	public void setMessageWarninig(String messageWarninig) {
		this.messageWarninig = messageWarninig;
	}

	public void setWarninig(boolean warninig) {
		this.warninig = warninig;
	}

}
