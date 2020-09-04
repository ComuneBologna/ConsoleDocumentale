package it.eng.portlet.consolepec.gwt.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import it.eng.portlet.consolepec.gwt.shared.model.RispostaFileUploaderDTO;

/**
 * Widget che si occupa di gestire l'upload di files mediante /consolepec/upload servlet
 * 
 * @author pluttero
 * 
 */
public class UploadAllegatoWidget extends Composite {

	private static UploadAllegatoWidgetUiBinder uiBinder = GWT.create(UploadAllegatoWidgetUiBinder.class);

	interface UploadAllegatoWidgetUiBinder extends UiBinder<Widget, UploadAllegatoWidget> {/**/}

	@UiField
	HTMLPanel uploadPanel;
	@UiField
	FormPanel formUpload;
	@UiField
	FileUpload uploadField;

	private UploadAllegatoHandler handler;

	public UploadAllegatoWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
		uploadField.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				if (uploadField.getFilename() != null && uploadField.getFilename().trim().length() > 0 && UploadAllegatoWidget.this.handler != null)
					UploadAllegatoWidget.this.handler.onFileSelected(uploadField.getFilename());
			}
		});

		uploadField.getElement().setPropertyString("multiple", "multiple"); // aggiunto per gestire il multiupload

		formUpload.addSubmitHandler(new SubmitHandler() {
			@Override
			public void onSubmit(SubmitEvent event) {
				if (handler != null && !handler.onSubmitUpload(getFileCount(uploadField.getElement()), getFileNames(uploadField.getElement()), getFileLengths(uploadField.getElement()))) {
					event.cancel();
				}
			}
		});

		formUpload.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				formUpload.reset();
				String json = event.getResults();
				RispostaFileUploaderDTO risposta = RispostaFileUploaderDTO.leggiFormJson(json);
				if (UploadAllegatoWidget.this.handler != null)
					UploadAllegatoWidget.this.handler.onUploadDone(risposta);
				formUpload.removeFromParent();
				uploadPanel.add(formUpload);
			}
		});
	}

	/**
	 * Apre il popup nativo di scelta file
	 */
	public void sfoglia(String action) {
		formUpload.setAction(action);
		Element elem = uploadField.getElement();
		elem.<InputElement> cast().click();
		// NativeEvent evt = Document.get().createClickEvent(1, 0, 0, 0, 0, false, false, false, false);
		// elem.dispatchEvent(evt);
		// DOM.getElementById("outdebug").setInnerHTML("sfoglia: "+DOM.createUniqueId());
	}

	/**
	 * Invia la chiamata al server
	 * 
	 * @param handler
	 */
	public void startUpload() {
		formUpload.submit();
	}

	public void setUploadAllegatoHandler(UploadAllegatoHandler handler) {
		this.handler = handler;
	}

	private String[] getFileNames(final Element data) {
		JsArrayString jsa = getJSFileNames(data).cast();
		String[] names = new String[jsa.length()];
		for (int i = 0; i < jsa.length(); i++) {
			names[i] = jsa.get(i);
		}
		return names;
	}

	private Long[] getFileLengths(final Element data) {
		JsArrayNumber jsa = getJSFileLengths(data).cast();
		Long[] lengths = new Long[jsa.length()];
		for (int i = 0; i < jsa.length(); i++) {
			lengths[i] = (long) jsa.get(i);
		}
		return lengths;
	}

	private native int getFileCount(final Element data) /*-{
		return data.files.length;
	}-*/;

	private native JavaScriptObject getJSFileNames(final Element data) /*-{
		var names = [];
		for (i = 0; i < data.files.length; i++) {
			names.push(data.files[i].name);
		}
		return names;
	}-*/;

	private native JavaScriptObject getJSFileLengths(final Element data) /*-{
		var lengths = [];
		for (i = 0; i < data.files.length; i++) {
			lengths.push(data.files[i].size);
		}
		return lengths;
	}-*/;

	public interface UploadAllegatoHandler {

		public void onUploadDone(RispostaFileUploaderDTO dto);

		public void onFileSelected(String fileName);

		public boolean onSubmitUpload(Integer fileNumber, String[] fileNames, Long[] fileLength);

	}

}
