package it.eng.portlet.consolepec.spring.bean.drive;

import java.util.List;

import it.eng.cobo.consolepec.commons.drive.Dizionario;
import it.eng.cobo.consolepec.commons.drive.Nomenclatura;

public interface DriveUtil {

	List<Dizionario> getDizionari();

	List<Nomenclatura> getNomenclature();

}
