package it.eng.cobo.consolepec.integration.sara.client;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public class SaraWSProfilaturaRequest {

	@Data
	public static class GetRuoliProfiloRequest {
		private String username;
		private String matricola;
		private String password;
	}

	@Data
	public static class GetRuoliProfiloResponse {
		private Boolean utenteTrovato;
		private List<String> ruoli = new ArrayList<>();
	}

}
