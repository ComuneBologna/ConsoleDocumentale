package it.eng.consolepec.spagicclient.bean.request.template;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author biagiot
 *
 */

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class CampoMetadato {
	String idMetadato, etichettaMetadato;
}
