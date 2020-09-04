package it.eng.portlet.consolepec.gwt.client.presenter.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class UploadEvent extends GwtEvent<UploadEvent.UploadHandler> {

	public enum UploadStatus { START, DONE, ERROR};
	
	public static Type<UploadHandler> TYPE = new Type<UploadHandler>();
	
	public interface UploadHandler extends EventHandler {
		void onUpload(UploadEvent event);
	}

	private String clientID;
	private UploadStatus uploadStatus;

	public UploadEvent(String clientID, UploadStatus uploadStatus) {
		this.uploadStatus = uploadStatus;
		this.clientID = clientID;
	}

	@Override
	protected void dispatch(UploadHandler handler) {
		handler.onUpload(this);
	}

	@Override
	public Type<UploadHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<UploadHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String clientID, UploadStatus uploadStatus) {
		source.fireEvent(new UploadEvent(clientID, uploadStatus));
	}

	public String getClientID() {
		return clientID;
	}
	
	public UploadStatus getUploadStatus() {
		return uploadStatus;
	}
}
