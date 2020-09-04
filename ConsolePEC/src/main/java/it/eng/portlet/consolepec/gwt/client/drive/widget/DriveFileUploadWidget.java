package it.eng.portlet.consolepec.gwt.client.drive.widget;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
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
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Widget;

import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import lombok.Data;
import lombok.Setter;

/**
 * @author Giacomo F.M.
 * @since 2019-06-21
 */
public class DriveFileUploadWidget extends Composite {

	public static final String DRIVE_UPLOAD_FILE = "/drive/upload/file";

	@UiField
	HTMLPanel panel;
	@UiField
	FormPanel form;
	@UiField
	FileUpload upload;
	@UiField
	Hidden metadati, ruolo;

	@Setter
	private UploadHandler uploadHandler;

	private static Binder binder = GWT.create(Binder.class);

	public interface Binder extends UiBinder<Widget, DriveFileUploadWidget> {/**/}

	public DriveFileUploadWidget() {
		initWidget(binder.createAndBindUi(this));

		form.setMethod(FormPanel.METHOD_POST);
		form.setEncoding(FormPanel.ENCODING_MULTIPART);

		form.setAction("/" + LiferayPortletUnsecureActionImpl.getPortletContext() + DRIVE_UPLOAD_FILE);

		form.addSubmitHandler(new SubmitHandler() {
			@Override
			public void onSubmit(SubmitEvent event) {
				if (uploadHandler != null) {
					metadati.setValue(uploadHandler.onSubmit().getMetadati());
					ruolo.setValue(uploadHandler.onSubmit().getRuolo());
				}
			}
		});

		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				String res = event.getResults();
				if (res.startsWith("<pre>") && res.endsWith("</pre>")) {
					res = res.substring(5, res.length() - 6);
				}
				JSONObject json = JSONParser.parseLenient(res).isObject();
				if (uploadHandler != null)
					uploadHandler.onSubmitComplete( //
							json.get(ConsolePecConstants.DRIVE_OK).isBoolean().booleanValue(), //
							json.get(ConsolePecConstants.DRIVE_MESSAGE).isString().stringValue());
			}
		});
	}

	public void clear() {
		form.reset();
	}

	public void submit() {
		form.submit();
	}

	public static interface UploadHandler {

		SubmitInfo onSubmit();

		void onSubmitComplete(boolean ok, String message);

	}

	@Data
	public static class SubmitInfo {
		private String metadati;
		private String ruolo;
	}

}
