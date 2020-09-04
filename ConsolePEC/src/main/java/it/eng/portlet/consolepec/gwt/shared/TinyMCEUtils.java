package it.eng.portlet.consolepec.gwt.shared;

/**
 *
 * Metodi di utilità per TinyMCE 4.x
 *
 * @author biagiot
 *
 */
public class TinyMCEUtils {

	public static native void setupTinyMCE(String text, String domElementID, boolean editable) /*-{
		$wnd.tinymce.init({
			selector : '#' + domElementID,
			language : 'it',
			setup : function(ed) {
				ed.on('init', function(args) {
					$wnd.tinymce.get(domElementID).setContent(text);
					$wnd.tinymce.get(domElementID).getBody().setAttribute('contenteditable', editable);
					});
				}
		});
	}-*/;

	public static native void setupTinyMCEWithPlugins(String body, String domElementID, boolean editable) /*-{
	$wnd.tinymce.init({
		selector : '#' + domElementID,
		language : 'it',
		setup : function(ed) {
			ed.on('init', function(args) {
					$wnd.tinymce.get(domElementID).setContent(body);
					$wnd.tinymce.get(domElementID).getBody().setAttribute('contenteditable', editable);
			});
		},
		plugins: "table link image",
			toolbar: 'undo redo | styleselect | bold italic | table | link | image'
		});
	}-*/;

	public static native void setContent(String text, String domElementID) /*-{
		$wnd.tinymce.get(domElementID).setContent(text);
	}-*/;

	public static native String getContent(String domElementID) /*-{
		return $wnd.tinymce.get(domElementID).getContent();
	}-*/;

	public static native void removeTinyMCE() /*-{
		$wnd.tinymce.remove();
	}-*/;

	public static String escapeTinyMCE(String body) {
		int open = 0, close = 0;
		for (int i = 0; i < body.length(); i++) {
			char c = body.charAt(i);
			if ("<".equals(c))
				open++;
			else if (">".equals(c))
				close++;
		}
		return (open > 0 && open == close) ? body : "<pre>" + body + "</pre>";
	}
}
