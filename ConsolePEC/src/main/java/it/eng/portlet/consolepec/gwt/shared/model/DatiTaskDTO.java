package it.eng.portlet.consolepec.gwt.shared.model;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author biagiot
 *
 */

@EqualsAndHashCode
public class DatiTaskDTO implements IsSerializable{

	@Getter
	@Setter
	int id;
	@Getter
	@Setter
	boolean attivo;
	@Getter
	@Setter
	String stato;
	@Getter
	@Setter
	AssegnatarioDTO assegnatarioCorrente;
	@Getter
	@Setter
	List<AssegnatarioDTO> assegnatariPassati = new ArrayList<AssegnatarioDTO>();

	public enum TipoTaskDTO {
		RICHIESTA_FIRMA_TASK;
	}

	@EqualsAndHashCode
	@Getter
	@Setter
	public static class AssegnatarioDTO implements IsSerializable {
		String nome;
		String dataInizio;
		String dataFine;
	}
}
