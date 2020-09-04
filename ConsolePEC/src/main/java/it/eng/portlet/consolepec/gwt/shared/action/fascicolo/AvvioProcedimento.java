package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AvvioProcedimento extends LiferayPortletUnsecureActionImpl<AvvioProcedimentoResult> {

	private String fascicoloPath;
	
	private List<String> emails = new ArrayList<String>();
	
	private int annoProtocollazione;
	private String numProtocollazione;
	private int codTipologiaProcedimento;
	private Date dataInizioProcedimento;
	private String modAvvioProcedimento;
	private int codQuartiere;
	
	public AvvioProcedimento() {
	}

	public String getFascicoloPath() {
		return fascicoloPath;
	}

	public void setFascicoloPath(String fascicoloPath) {
		this.fascicoloPath = fascicoloPath;
	}

	public int getAnnoProtocollazione() {
		return annoProtocollazione;
	}

	public String getNumProtocollazione() {
		return numProtocollazione;
	}

	public int getCodTipologiaProcedimento() {
		return codTipologiaProcedimento;
	}

	public Date getDataInizioProcedimento() {
		return dataInizioProcedimento;
	}

	public String getModAvvioProcedimento() {
		return modAvvioProcedimento;
	}

	public int getCodQuartiere() {
		return codQuartiere;
	}

	public void setAnnoProtocollazione(int annoProtocollazione) {
		this.annoProtocollazione = annoProtocollazione;
	}

	public void setNumProtocollazione(String numProtocollazione) {
		this.numProtocollazione = numProtocollazione;
	}

	public void setCodTipologiaProcedimento(int codTipologiaProcedimento) {
		this.codTipologiaProcedimento = codTipologiaProcedimento;
	}

	public void setDataInizioProcedimento(Date dataInizioProcedimento) {
		this.dataInizioProcedimento = dataInizioProcedimento;
	}

	public void setModAvvioProcedimento(String modAvvioProcedimento) {
		this.modAvvioProcedimento = modAvvioProcedimento;
	}

	public void setCodQuartiere(int codQuartiere) {
		this.codQuartiere = codQuartiere;
	}

	public List<String> getEmails() {
		return emails;
	}
}
