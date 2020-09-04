package it.eng.cobo.consolepec.integration.sara.client.emissionecontrassegno;

import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.CategoriaContrassegno;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.DatiResidenti;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.TitoloPossesso;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineRequest.Veicolo;


public class EmissioneContrassegnoRequest {

	private CategoriaContrassegno categoriaContrassegno;
	private int numeroMatricola;
	private Veicolo veicolo;
	private TitoloPossesso titoloPossesso;
	private DatiResidenti datiResidenti;

	public CategoriaContrassegno getCategoriaContrassegno() {
		return categoriaContrassegno;
	}

	public void setCategoriaContrassegno(CategoriaContrassegno categoriaContrassegno) {
		this.categoriaContrassegno = categoriaContrassegno;
	}

	public int getNumeroMatricola() {
		return numeroMatricola;
	}

	public void setNumeroMatricola(int numeroMatricola) {
		this.numeroMatricola = numeroMatricola;
	}

	public Veicolo getVeicolo() {
		return veicolo;
	}

	public void setVeicolo(Veicolo veicolo) {
		this.veicolo = veicolo;
	}

	public TitoloPossesso getTitoloPossesso() {
		return titoloPossesso;
	}

	public void setTitoloPossesso(TitoloPossesso titoloPossesso) {
		this.titoloPossesso = titoloPossesso;
	}

	public DatiResidenti getDatiResidenti() {
		return datiResidenti;
	}

	public void setDatiResidenti(DatiResidenti datiResidenti) {
		this.datiResidenti = datiResidenti;
	}

}
