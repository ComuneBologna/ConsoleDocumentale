package it.eng.portlet.consolepec.gwt.client.drive.util;

import com.google.gwt.core.client.JavaScriptObject;

import it.eng.cobo.consolepec.commons.drive.metadato.MetadatoSingolo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Giacomo F.M.
 * @since 2019-06-28
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JavaScriptMetadatoSingolo extends JavaScriptObject {

	public final native String getChiave() /*-{
		return this.chiave;
	}-*/;

	public final native void setChiave(String chiave) /*-{
		this.chiave = chiave;
	}-*/;

	public final native String getValore() /*-{
		return this.valore;
	}-*/;

	public final native void setValore(String valore) /*-{
		this.valore = valore;
	}-*/;

	public static final JavaScriptMetadatoSingolo build(MetadatoSingolo m) {
		JavaScriptMetadatoSingolo jsms = createObject().cast();
		jsms.setChiave(m.getChiave());
		jsms.setValore(m.getValore());
		return jsms;
	}

}
