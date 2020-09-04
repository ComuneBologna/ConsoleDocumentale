package it.eng.cobo.consolepec.commons.services;

import java.io.InputStream;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InputStreamMapper {

	private final String name;
	private final InputStream inputStream;

}
