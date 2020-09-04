package it.eng.portlet.consolepec.gwt.server.rest.client;

import java.util.List;

import it.eng.cobo.consolepec.commons.drive.Cartella;
import it.eng.cobo.consolepec.commons.drive.Dizionario;
import it.eng.cobo.consolepec.commons.drive.DriveElement;
import it.eng.cobo.consolepec.commons.drive.File;
import it.eng.cobo.consolepec.commons.drive.Nomenclatura;
import it.eng.cobo.consolepec.commons.drive.permessi.PermessoDrive;
import it.eng.cobo.consolepec.commons.exception.ConsoleDocumentaleException;

/**
 * @author Giacomo F.M.
 * @since 2019-06-03
 */
public interface DriveClient {

	List<DriveElement> ricerca(String query) throws ConsoleDocumentaleException;

	DriveElement cercaElemento(String id) throws ConsoleDocumentaleException;

	Cartella cercaCartella(String idCartella) throws ConsoleDocumentaleException;

	List<DriveElement> apriCartella(String idCartella, Integer page, Integer limit) throws ConsoleDocumentaleException;

	Cartella creaCartella(Cartella cartella, String ruolo) throws ConsoleDocumentaleException;

	Cartella aggiornaCartella(Cartella cartella) throws ConsoleDocumentaleException;

	File creaFile(String metadati, String ruolo, java.io.File file) throws ConsoleDocumentaleException;

	File aggiornaFile(File file) throws ConsoleDocumentaleException;

	DriveElement aggiornaPermessi(String id, boolean recursive, List<PermessoDrive> aggiunti, List<PermessoDrive> rimossi) throws ConsoleDocumentaleException;

	List<Dizionario> getDizionari() throws ConsoleDocumentaleException;

	List<Nomenclatura> getNomenclature() throws ConsoleDocumentaleException;

	void eliminaElemento(DriveElement elemento) throws ConsoleDocumentaleException;
}
