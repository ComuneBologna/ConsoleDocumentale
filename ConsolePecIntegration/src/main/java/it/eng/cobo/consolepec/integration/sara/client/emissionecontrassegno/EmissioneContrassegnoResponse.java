package it.eng.cobo.consolepec.integration.sara.client.emissionecontrassegno;

import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.ArrayOfXsdString;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.Contrassegno;
import it.eng.cobo.consolepec.integration.sara.client.SaraOnlineResponse.Esito;

public class EmissioneContrassegnoResponse {

	private Esito esito;
	private Contrassegno contrassegno;
	private ArrayOfXsdString listaCodMessaggio;

	public Esito getEsito() {
		return esito;
	}

	public void setEsito(Esito esito) {
		this.esito = esito;
	}

	public Contrassegno getContrassegno() {
		return contrassegno;
	}

	public void setContrassegno(Contrassegno contrassegno) {
		this.contrassegno = contrassegno;
	}

	public ArrayOfXsdString getListaCodMessaggio() {
		return listaCodMessaggio;
	}

	public void setListaCodMessaggio(ArrayOfXsdString listaCodMessaggio) {
		this.listaCodMessaggio = listaCodMessaggio;
	}


}
