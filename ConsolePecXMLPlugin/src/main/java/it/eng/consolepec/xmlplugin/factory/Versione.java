package it.eng.consolepec.xmlplugin.factory;

import java.util.Date;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Versione {
	@Getter
	@Setter(value = AccessLevel.PROTECTED)
	String label, author, versionid, hash;
	@Getter
	@Setter(value = AccessLevel.PROTECTED)
	Date dataVersione;
}
