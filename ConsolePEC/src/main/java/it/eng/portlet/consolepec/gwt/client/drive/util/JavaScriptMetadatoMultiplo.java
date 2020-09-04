package it.eng.portlet.consolepec.gwt.client.drive.util;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

import it.eng.cobo.consolepec.commons.drive.metadato.MetadatoMultiplo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Giacomo F.M.
 * @since 2019-06-28
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JavaScriptMetadatoMultiplo extends JavaScriptObject {

	public final native String getChiave() /*-{
		return this.chiave;
	}-*/;

	public final native void setChiave(String chiave) /*-{
		this.chiave = chiave;
	}-*/;

	public final native JsArrayString getValori() /*-{
		return this.valori;
	}-*/;

	public final native void setValori(JsArrayString valori) /*-{
		this.valori = valori;
	}-*/;

	public static final JavaScriptMetadatoMultiplo build(MetadatoMultiplo m) {
		JavaScriptMetadatoMultiplo jsmm = createObject().cast();
		jsmm.setChiave(m.getChiave());
		JsArrayString valori = createArray().cast();
		for (String valore : m.getValori()) {
			valori.push(valore);
		}
		jsmm.setValori(valori);
		return jsmm;
	}
}
