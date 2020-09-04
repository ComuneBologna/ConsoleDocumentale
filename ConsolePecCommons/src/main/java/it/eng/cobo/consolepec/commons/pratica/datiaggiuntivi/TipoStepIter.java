package it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi;

public enum TipoStepIter {

	STATO_INIZIALE("Stato iniziale"),
	STATO_FINALE("Stato finale");
	
    private String stato;

	TipoStepIter(String stato) {
        this.stato = stato;
    }

    public String getStato() {
        return stato;
    }
	
}
