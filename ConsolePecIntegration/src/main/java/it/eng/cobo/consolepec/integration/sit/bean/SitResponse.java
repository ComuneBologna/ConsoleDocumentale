package it.eng.cobo.consolepec.integration.sit.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.eng.cobo.consolepec.integration.sit.generated.Response;
import it.eng.cobo.consolepec.integration.sit.generated.Response.DataOutput.ElencoVie;
import it.eng.cobo.consolepec.integration.sit.generated.Response.DataOutput.Indirizzo.ZoneInclusione.Zona;
import lombok.Getter;

public class SitResponse {

	private List<String> errorCodes = Arrays.asList("NOCIVICO", "NOVIA");

	private static final String MULTIVIA_WARNING_CODE = "MULTIVIA";

	@Getter private Response sitResponse;

	public SitResponse(Response resp) {
		this.sitResponse = resp;
	}

	public String getErrorCode() {
		if ((this.sitResponse.getHeader() != null) && (this.sitResponse.getHeader().getErrCode() != null)) return this.sitResponse.getHeader().getErrCode();
		return null;
	}

	public ArrayList<String> getMultivia() {
		ArrayList<String> multivia = new ArrayList<String>();
		if ((this.sitResponse.getDataOutput() != null) && (this.sitResponse.getDataOutput().getElencoVie() != null)) {

			ElencoVie vie = this.sitResponse.getDataOutput().getElencoVie();
			for (int i = 0; i < vie.getVia().size(); i++) {
				multivia.add(vie.getVia().get(i).getViaDenom());
			}

		}
		return multivia;
	}

	private static boolean isNotNull(Object o) {
		return !isNull(o);
	}

	private static boolean isNull(Object o) {
		if (o == null) return true;
		else if (o instanceof String) return o.equals("");
		return false;
	}

	protected String getViaSit() {
		if (isNotNull(this.sitResponse.getDataOutput()) && isNotNull(this.sitResponse.getDataOutput().getIndirizzo()) && isNotNull(this.sitResponse.getDataOutput().getIndirizzo().getVia())
				&& isNotNull(this.sitResponse.getDataOutput().getIndirizzo().getVia().getViaDenom())) {
			return this.sitResponse.getDataOutput().getIndirizzo().getVia().getViaDenom();
		} else if (isNotNull(this.sitResponse.getDataOutput()) && isNotNull(this.sitResponse.getDataOutput().getVia()) && isNotNull(this.sitResponse.getDataOutput().getVia().getViaDenom()))
			return this.sitResponse.getDataOutput().getVia().getViaDenom();
		return null;
	}

	protected String getCivicoSit() {
		if (isNotNull(this.sitResponse.getDataOutput()) && isNotNull(this.sitResponse.getDataOutput().getIndirizzo()) && isNotNull(this.sitResponse.getDataOutput().getIndirizzo().getCivico())
				&& isNotNull(this.sitResponse.getDataOutput().getIndirizzo().getCivico().getCivicoNum())) {
			return this.sitResponse.getDataOutput().getIndirizzo().getCivico().getCivicoNum().toString();

		}
		return null;
	}

	protected String getCodViaSit() {
		if (isNotNull(this.sitResponse.getDataOutput()) && isNotNull(this.sitResponse.getDataOutput().getIndirizzo()) && isNotNull(this.sitResponse.getDataOutput().getIndirizzo().getVia())
				&& isNotNull(this.sitResponse.getDataOutput().getIndirizzo().getVia().getViaCod())) {
			return (new Integer(this.sitResponse.getDataOutput().getIndirizzo().getVia().getViaCod())).toString();

		} else if (isNotNull(this.sitResponse.getDataOutput()) && isNotNull(this.sitResponse.getDataOutput().getVia()) && isNotNull(this.sitResponse.getDataOutput().getVia().getViaCod()))
			return (new Integer(this.sitResponse.getDataOutput().getVia().getViaCod())).toString();
		return null;
	}

	public boolean isCentroStorico() {
		if (isNotNull(this.sitResponse.getDataOutput()) && isNotNull(this.sitResponse.getDataOutput().getIndirizzo()) && isNotNull(this.sitResponse.getDataOutput().getIndirizzo().getCivico())
				&& isNotNull(this.sitResponse.getDataOutput().getIndirizzo().getCivico().getCentroStorico())) {
			return Boolean.parseBoolean(this.sitResponse.getDataOutput().getIndirizzo().getCivico().getCentroStorico().getValueAttribute());

		}
		return false;
	}

	public String getQuartiereSit() {
		if (isNotNull(this.sitResponse.getDataOutput()) && isNotNull(this.sitResponse.getDataOutput().getIndirizzo())
				&& isNotNull(this.sitResponse.getDataOutput().getIndirizzo().getZoneInclusione())) {
			List<Zona> zona = this.sitResponse.getDataOutput().getIndirizzo().getZoneInclusione().getZona();
			for (int i = 0; i < zona.size(); i++) {
				if (zona.get(i).getNome().equalsIgnoreCase("quartiere")) return zona.get(i).getZonaDenom();
			}

		}
		return null;
	}

	public String getErrorMessage() {
		if (errorCodes.contains(getErrorCode())) return getHeaderMessage();
		return null;
	}

	public String getHeaderMessage() {
		if ((this.sitResponse.getHeader() != null) && (this.sitResponse.getHeader().getMessage() != null)) return this.sitResponse.getHeader().getMessage();
		return null;
	}

	public boolean isIndirizzoValido() {
		return this.sitResponse.getDataOutput() != null && this.sitResponse.getDataOutput().getIndirizzo() != null;
	}

	// VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV

	public boolean isError() {
		return getSitResponse() == null || getSitResponse().getHeader() == null //
				|| Boolean.parseBoolean(getSitResponse().getHeader().getIsError().getValueError());
	}

	public boolean isMultivia() {
		return !isError() && getSitResponse().getHeader().getErrCode() != null && MULTIVIA_WARNING_CODE.equalsIgnoreCase(getSitResponse().getHeader().getErrCode());
	}

	public boolean isZonaValida() {
		return !isError() && getSitResponse().getDataOutput() != null && getSitResponse().getDataOutput().getIndirizzo() != null;
	}

	public Indirizzo getIndirizzo() {
		Indirizzo ind = null;
		if (isZonaValida()) {
			ind = Indirizzo.builder() //
					.codiceVia(getSitResponse().getDataOutput().getIndirizzo().getVia().getViaCod()) //
					.via(getSitResponse().getDataOutput().getIndirizzo().getVia().getViaDenom()) //
					.civico(getSitResponse().getDataOutput().getIndirizzo().getCivico().getCivicoNum()) //
					.esponente(getSitResponse().getDataOutput().getIndirizzo().getCivico().getCivicoEsp()) //
					.centro(Boolean.parseBoolean(getSitResponse().getDataOutput().getIndirizzo().getCivico().getCentroStorico().getValueAttribute())) //
					.build();

			for (Zona zona : getSitResponse().getDataOutput().getIndirizzo().getZoneInclusione().getZona()) {
				if (zona.getNome().equalsIgnoreCase("quartiere")) {
					ind.setCodiceQuartiere(zona.getZonaCod());
					ind.setQuartiere(zona.getZonaDenom());
				}
				if (zona.getNome().equalsIgnoreCase("cap")) {
					ind.setCap(zona.getZonaCod());
				}
			}
		}
		return ind;
	}

}
