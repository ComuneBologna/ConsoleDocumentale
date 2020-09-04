package it.eng.cobo.consolepec.integration.sit.bean;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import it.eng.cobo.consolepec.integration.sit.generated.ObjectFactory;
import it.eng.cobo.consolepec.integration.sit.generated.Request;

public class SitRequest {

	private static final ObjectFactory FACTORY = new ObjectFactory();

	public static final String SERVIZIO_RICERCA_VIA = "RicercaVia";
	public static final String SERVIZIO_RICERCA_ZONA = "RicercaZona";

	public static final String RICERCA_PER_NOME = "n";
	public static final String RICERCA_PER_CODICE = "c";

	public static final String ENTE_COD = "01";
	public static final String ZONA_TIPO = "TUTTE";

	private Request request;

	private SitRequest(Request request) {
		this.request = request;
	}

	public static SitRequestBuilder builder() {
		return new SitRequestBuilder();
	}

	public Request getRequest() {
		return this.request;
	}

	public static class SitRequestBuilder {
		private Request request;

		private SitRequestBuilder() {
			this.request = FACTORY.createRequest();
		}

		public ServizioRicercaVia servizioRicercaVia() {
			return new ServizioRicercaVia(request);
		}

		public ServizioRicercaZona servizioRicercaZona() {
			return new ServizioRicercaZona(request);
		}
	}

	public static class ServizioRicercaVia {
		private Request request;

		private ServizioRicercaVia(Request request) {
			this.request = request;
			this.request.setServiceName(SERVIZIO_RICERCA_VIA);
			this.request.setServiceParams(FACTORY.createRequestServiceParams());
			this.request.getServiceParams().setMaxRowsRet(100);
		}

		public ServizioRicercaVia nomeVia(String nomeVia) {
			this.request.getServiceParams().setIndirizzo(FACTORY.createRequestServiceParamsIndirizzo());
			this.request.getServiceParams().getIndirizzo().setTipo(RICERCA_PER_NOME);
			this.request.getServiceParams().getIndirizzo().setEnteCod(ENTE_COD);
			this.request.getServiceParams().getIndirizzo().setViaDenomCod(nomeVia);
			return this;
		}

		public ServizioRicercaVia codiceVia(String codiceVia) {
			this.request.getServiceParams().setIndirizzo(FACTORY.createRequestServiceParamsIndirizzo());
			this.request.getServiceParams().getIndirizzo().setTipo(RICERCA_PER_CODICE);
			this.request.getServiceParams().getIndirizzo().setEnteCod(ENTE_COD);
			this.request.getServiceParams().getIndirizzo().setViaDenomCod(codiceVia);
			return this;
		}

		public ServizioRicercaVia aggiungiData(Calendar data) {
			setDate(this.request, data);
			return this;
		}

		public SitRequest build() {
			return new SitRequest(this.request);
		}
	}

	public static class ServizioRicercaZona {
		private Request request;

		private ServizioRicercaZona(Request request) {
			this.request = request;
			this.request.setServiceName(SERVIZIO_RICERCA_ZONA);
			this.request.setServiceParams(FACTORY.createRequestServiceParams());
			this.request.getServiceParams().setMaxRowsRet(100);

			this.request.getServiceParams().setZona(FACTORY.createRequestServiceParamsZona());
			this.request.getServiceParams().getZona().setTipo(ZONA_TIPO);
		}

		public ServizioRicercaZona nomeVia(String nomeVia, String numeroCivico) {
			return nomeVia(nomeVia, numeroCivico, null);
		}

		public ServizioRicercaZona nomeVia(String nomeVia, String numeroCivico, String esponente) {
			this.request.getServiceParams().setIndirizzo(FACTORY.createRequestServiceParamsIndirizzo());
			this.request.getServiceParams().getIndirizzo().setTipo(RICERCA_PER_NOME);
			this.request.getServiceParams().getIndirizzo().setEnteCod(ENTE_COD);
			this.request.getServiceParams().getIndirizzo().setViaDenomCod(nomeVia);

			this.request.getServiceParams().getIndirizzo().setCivico(FACTORY.createRequestServiceParamsIndirizzoCivico());
			this.request.getServiceParams().getIndirizzo().getCivico().setCivicoNum(numeroCivico);
			if (esponente != null) {
				this.request.getServiceParams().getIndirizzo().getCivico().setCivicoEsp(esponente);
			}
			return this;
		}

		public ServizioRicercaZona codiceVia(String nomeVia, String numeroCivico) {
			return codiceVia(nomeVia, numeroCivico, null);
		}

		public ServizioRicercaZona codiceVia(String codiceVia, String numeroCivico, String esponente) {
			this.request.getServiceParams().setIndirizzo(FACTORY.createRequestServiceParamsIndirizzo());
			this.request.getServiceParams().getIndirizzo().setTipo(RICERCA_PER_CODICE);
			this.request.getServiceParams().getIndirizzo().setEnteCod(ENTE_COD);
			this.request.getServiceParams().getIndirizzo().setViaDenomCod(codiceVia);

			this.request.getServiceParams().getIndirizzo().setCivico(FACTORY.createRequestServiceParamsIndirizzoCivico());
			this.request.getServiceParams().getIndirizzo().getCivico().setCivicoNum(numeroCivico);
			if (esponente != null) {
				this.request.getServiceParams().getIndirizzo().getCivico().setCivicoEsp(esponente);
			}
			return this;
		}

		public ServizioRicercaZona aggiungiData(Calendar data) {
			setDate(this.request, data);
			return this;
		}

		public ServizioRicercaZona impostaServiceOutput() {
			this.request.setServiceOutput(FACTORY.createRequestServiceOutput());
			this.request.getServiceOutput().setOutArco(FACTORY.createRequestServiceOutputOutArco());
			this.request.getServiceOutput().setOutCivicoId(FACTORY.createRequestServiceOutputOutCivicoId());
			this.request.getServiceOutput().setOutCivicoMotCes(FACTORY.createRequestServiceOutputOutCivicoMotCes());
			this.request.getServiceOutput().setOutCivicoProvvis(FACTORY.createRequestServiceOutputOutCivicoProvvis());
			this.request.getServiceOutput().setOutCivicoServRsu(FACTORY.createRequestServiceOutputOutCivicoServRsu());

			this.request.getServiceOutput().getOutArco().setRetrieve(Boolean.FALSE.toString());
			this.request.getServiceOutput().getOutCivicoId().setRetrieve(Boolean.FALSE.toString());
			this.request.getServiceOutput().getOutCivicoMotCes().setRetrieve(Boolean.FALSE.toString());
			this.request.getServiceOutput().getOutCivicoProvvis().setRetrieve(Boolean.FALSE.toString());
			this.request.getServiceOutput().getOutCivicoServRsu().setRetrieve(Boolean.FALSE.toString());

			return this;
		}

		public SitRequest build() {
			return new SitRequest(this.request);
		}
	}

	private static void setDate(Request request, Calendar date) {
		if (request.getServiceParams().getIndirizzo() != null) {
			request.getServiceParams().getIndirizzo().setData(FACTORY.createRequestServiceParamsIndirizzoData());
			String mm = new SimpleDateFormat("MM").format(date.getTime());
			String gg = new SimpleDateFormat("dd").format(date.getTime());
			request.getServiceParams().getIndirizzo().getData().setAaaa(Integer.toString(date.get(Calendar.YEAR)));
			request.getServiceParams().getIndirizzo().getData().setMm(mm);
			request.getServiceParams().getIndirizzo().getData().setGg(gg);
		}
	}

}
