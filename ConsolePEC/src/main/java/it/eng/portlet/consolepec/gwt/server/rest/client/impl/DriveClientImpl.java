package it.eng.portlet.consolepec.gwt.server.rest.client.impl;

import java.net.URLConnection;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import it.eng.cobo.consolepec.commons.drive.Cartella;
import it.eng.cobo.consolepec.commons.drive.Dizionario;
import it.eng.cobo.consolepec.commons.drive.DriveElement;
import it.eng.cobo.consolepec.commons.drive.File;
import it.eng.cobo.consolepec.commons.drive.Nomenclatura;
import it.eng.cobo.consolepec.commons.drive.permessi.PermessoDrive;
import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.ConsoleDocumentaleException;
import it.eng.portlet.consolepec.gwt.server.rest.ConsolePecRestClient;
import it.eng.portlet.consolepec.gwt.server.rest.ErrorResponse;
import it.eng.portlet.consolepec.gwt.server.rest.RestResponse;
import it.eng.portlet.consolepec.gwt.server.rest.client.DriveClient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Giacomo F.M.
 * @since 2019-06-03
 */
@Slf4j
public class DriveClientImpl extends ConsolePecRestClient implements DriveClient {

	private ErrorResponse getErrorResponse(final String json) {
		try {
			return jsonFactory.deserialize(json, ErrorResponse.class);
		} catch (Exception e) {
			log.warn("Problema nella deserializzazione della risposta di errore: " + json, e);
			return null;
		}
	}

	private String getErrorResponseMessage(final String json) {
		ErrorResponse error = getErrorResponse(json);
		if (error == null) {
			return "Errore nell'accesso al servizio";
		}
		return error.getMessage();
	}

	@Override
	public List<DriveElement> ricerca(final String query) throws ConsoleDocumentaleException {
		RestResponse resp = restClientInvoker.get("/service/drive/ricerca", "query=" + query);
		if (resp.isOk()) {
			return jsonFactory.deserializeList(resp.getJson(), DriveElement.class);
		}
		throw new ApplicationException(String.format("Errore qua durante la ricerca tramite (%s): %s", query, getErrorResponseMessage(resp.getJson())));
	}

	@Override
	public DriveElement cercaElemento(final String id) throws ConsoleDocumentaleException {
		RestResponse resp = restClientInvoker.get("/service/drive/" + id, null);
		if (resp.isOk()) {
			return jsonFactory.deserialize(resp.getJson(), DriveElement.class);
		}
		throw new ApplicationException(String.format("Errore nella ricerca dell'elemento (%s): %s", id, getErrorResponseMessage(resp.getJson())));
	}

	@Override
	public Cartella cercaCartella(final String idCartella) throws ConsoleDocumentaleException {
		RestResponse resp = restClientInvoker.get("/service/drive/cartella/" + idCartella, null);
		if (resp.isOk()) {
			return jsonFactory.deserialize(resp.getJson(), Cartella.class);
		}
		throw new ApplicationException(String.format("Errore nella ricerca della cartella (%s): %s", idCartella, getErrorResponseMessage(resp.getJson())));
	}

	@Override
	public List<DriveElement> apriCartella(final String idCartella, final Integer page, final Integer limit) throws ConsoleDocumentaleException {
		RestResponse resp = restClientInvoker.get("/service/drive", String.format("idCartella=%s&page=%s&limit=%s", idCartella, page, limit));
		if (resp.isOk()) {
			return jsonFactory.deserializeList(resp.getJson(), DriveElement.class);
		}
		throw new ApplicationException(String.format("Errore nella ricerca degli elementi della cartella (%s): %s", idCartella, getErrorResponseMessage(resp.getJson())));
	}

	@Override
	public Cartella creaCartella(final Cartella cartella, final String ruolo) throws ConsoleDocumentaleException {
		String body = jsonFactory.serialize(cartella);
		RestResponse resp = restClientInvoker.post("/service/drive/cartella", "ruolo=" + ruolo, body);
		if (resp.isOk()) {
			return jsonFactory.deserialize(resp.getJson(), Cartella.class);
		}
		throw new ApplicationException(String.format("Errore nella creazione della cartella (%s): %s", cartella.getNome(), getErrorResponseMessage(resp.getJson())));
	}

	@Override
	public Cartella aggiornaCartella(final Cartella cartella) throws ConsoleDocumentaleException {
		String body = jsonFactory.serialize(cartella);
		RestResponse resp = restClientInvoker.put("/service/drive/cartella", null, body);
		if (resp.isOk()) {
			return jsonFactory.deserialize(resp.getJson(), Cartella.class);
		}
		throw new ApplicationException(String.format("Errore nell'aggiornamento della cartella id (%s): %s", cartella.getId(), getErrorResponseMessage(resp.getJson())));
	}

	@Override
	public File creaFile(final String metadati, final String ruolo, final java.io.File file) throws ConsoleDocumentaleException {
		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		if (mimeType == null) {
			mimeType = ContentType.APPLICATION_OCTET_STREAM.getMimeType();
		}
		HttpEntity entity = MultipartEntityBuilder.create() //
				.addTextBody("metadati", metadati, ContentType.APPLICATION_JSON) //
				.addBinaryBody("stream", file, ContentType.parse(mimeType), file.getName()) //
				.build();
		RestResponse resp = restClientInvoker.customPost("/service/drive/file", "ruolo=" + ruolo, entity);
		if (resp.isOk()) {
			return jsonFactory.deserialize(resp.getJson(), File.class);
		}
		throw new ApplicationException(String.format("Errore nella creazione del file nome (%s): %s", file == null ? "nessun nome" : file.getName(), getErrorResponseMessage(resp.getJson())));
	}

	@Override
	public File aggiornaFile(final File file) throws ConsoleDocumentaleException {
		String metadati = jsonFactory.serialize(file);
		HttpEntity entity = MultipartEntityBuilder.create() //
				.addTextBody("metadati", metadati, ContentType.APPLICATION_JSON) //
				.build();
		RestResponse resp = restClientInvoker.customPut("/service/drive/file", null, entity);
		if (resp.isOk()) {
			return jsonFactory.deserialize(resp.getJson(), File.class);
		}
		throw new ApplicationException(String.format("Errore nell'aggiornamento del file id (%s): %s", file.getId(), getErrorResponseMessage(resp.getJson())));
	}

	@Override
	public DriveElement aggiornaPermessi(final String id, final boolean recursive, final List<PermessoDrive> aggiunti, final List<PermessoDrive> rimossi) throws ConsoleDocumentaleException {
		boolean okAgg, okDel;
		String jsonResp = null;

		if (aggiunti != null && !aggiunti.isEmpty()) {
			String bodyAgg = jsonFactory.serialize(new GestionePermessiInput(recursive, aggiunti));
			RestResponse respAgg = restClientInvoker.put("/service/drive/permessi/aggiungi/" + id, null, bodyAgg);
			okAgg = respAgg.isOk();
			jsonResp = respAgg.getJson();
		} else {
			okAgg = true;
		}

		if (okAgg && rimossi != null && !rimossi.isEmpty()) {
			String bodyDel = jsonFactory.serialize(new GestionePermessiInput(recursive, rimossi));
			RestResponse respDel = restClientInvoker.put("/service/drive/permessi/rimuovi/" + id, null, bodyDel);
			okDel = respDel.isOk();
		} else {
			okDel = true;
		}

		if (okAgg && okDel) {
			return jsonFactory.deserialize(jsonResp, DriveElement.class);
		}
		throw new ApplicationException(String.format("Errore nell'aggiornamento dei permessi sull'id (%s): %s", id, getErrorResponseMessage(jsonResp)));
	}

	@Override
	public List<Dizionario> getDizionari() throws ConsoleDocumentaleException {

		RestResponse respDel = restClientInvoker.get("/service/drive/dizionari", null);

		if (respDel.isOk()) {
			return jsonFactory.deserializeList(respDel.getJson(), Dizionario.class);
		}

		throw new ApplicationException("Errore durante il recupero dei dizionari drive");
	}

	@Override
	public List<Nomenclatura> getNomenclature() throws ConsoleDocumentaleException {
		RestResponse respDel = restClientInvoker.get("/service/drive/nomenclatura", null);

		if (respDel.isOk()) {
			return jsonFactory.deserializeList(respDel.getJson(), Nomenclatura.class);
		}

		throw new ApplicationException("Errore durante il recupero della nomenclatura drive");

	}

	@Getter
	@AllArgsConstructor
	private static class GestionePermessiInput {
		private boolean recursive;
		private List<PermessoDrive> permessi;
	}

	@Override
	public void eliminaElemento(final DriveElement elemento) throws ConsoleDocumentaleException {
		RestResponse resp = restClientInvoker.delete(elemento.isCartella() ? "/service/drive/cartella" : "/service/drive/file", "id=" + elemento.getId());
		if (!resp.isOk()) {
			throw new ApplicationException(String.format("Errore nella cancellazione dell'elemento id (%s): %s", elemento.getId(), getErrorResponseMessage(resp.getJson())));
		}
	}
}
