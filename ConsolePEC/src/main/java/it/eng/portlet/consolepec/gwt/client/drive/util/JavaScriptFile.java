package it.eng.portlet.consolepec.gwt.client.drive.util;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

import it.eng.cobo.consolepec.commons.drive.File;
import it.eng.cobo.consolepec.commons.drive.metadato.Metadato;
import it.eng.cobo.consolepec.commons.drive.metadato.MetadatoMultiplo;
import it.eng.cobo.consolepec.commons.drive.metadato.MetadatoSingolo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Giacomo F.M.
 * @since 2019-06-28
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JavaScriptFile extends JavaScriptObject {

	public final native String getIdCartella() /*-{
		return this.idCartella;
	}-*/;

	public final native void setIdCartella(String idCartella) /*-{
		this.idCartella = idCartella;
	}-*/;

	public final native String getNome() /*-{
		return this.nome;
	}-*/;

	public final native void setNome(String nome) /*-{
		this.nome = nome;
	}-*/;

	public final native String getDizionario() /*-{
		return this.dizionario;
	}-*/;

	public final native void setDizionario(String dizionario) /*-{
		this.dizionario = dizionario;
	}-*/;

	public final native JsArray<JavaScriptObject> getMetadati() /*-{
		return this.metadati;
	}-*/;

	public final native void setMetadati(JsArray<JavaScriptObject> metadati) /*-{
		this.metadati = metadati;
	}-*/;

	public static final JavaScriptFile build(File file) {
		JavaScriptFile jsf = createObject().cast();
		jsf.setIdCartella(file.getIdCartella());
		jsf.setNome(file.getNome());
		if (file.getDizionario() != null)
			jsf.setDizionario(file.getDizionario());
		JsArray<JavaScriptObject> metadati = createArray().cast();
		for (Metadato m : file.getMetadati()) {
			if (m instanceof MetadatoSingolo) {
				metadati.push(JavaScriptMetadatoSingolo.build((MetadatoSingolo) m));
			}
			if (m instanceof MetadatoMultiplo) {
				metadati.push(JavaScriptMetadatoMultiplo.build((MetadatoMultiplo) m));
			}
		}
		jsf.setMetadati(metadati);
		return jsf;
	}

}
