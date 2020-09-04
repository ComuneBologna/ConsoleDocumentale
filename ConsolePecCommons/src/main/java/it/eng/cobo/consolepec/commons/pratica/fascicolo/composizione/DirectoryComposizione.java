package it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class DirectoryComposizione {

	public static final String SEPARATOR = "/";

	private String path;
	private DirectoryComposizione parent;
	private List<DirectoryComposizione> subDirectories = new ArrayList<>();
	private List<AllegatoComposizione> elementi = new ArrayList<>();

	public DirectoryComposizione(String path, DirectoryComposizione parent) {
		this.path = path;
		this.parent = parent;
	}

}
