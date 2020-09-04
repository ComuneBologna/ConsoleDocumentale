package it.eng.portlet.consolepec.gwt.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;

public class DownloadAllegatoWidget extends Composite {
	private static DownloadAllegatoWidgetUiBinder uiBinder = GWT.create(DownloadAllegatoWidgetUiBinder.class);

	interface DownloadAllegatoWidgetUiBinder extends UiBinder<Widget, DownloadAllegatoWidget> {/**/}

	@UiField
	Frame downloadFrame;

	public DownloadAllegatoWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void sendDownload(SafeUri uri, boolean forceDownload) {
		StringBuilder url = new StringBuilder(uri.asString());
		if (isMobileIOS() || isAndroid()) {
			// Se ios mobile, apriamo su nuovo tab per forza, in quanto visualizza solo inline.
			// Per android è opzionale, adottiamo questo comportamento per omogeneità.
			openPopup(url.toString());
		}

		if (forceDownload)
			url.append("/forcedownload/y");

		downloadFrame.setUrl(url.toString());
	}

	public void sendDownload(SafeUri uri) {
		sendDownload(uri, true);
	}

	private boolean isMobileIOS() {
		String ua = Window.Navigator.getUserAgent();
		return (ua.contains("iPad") || ua.contains("iPod") || ua.contains("iPhone"));
	}

	private boolean isAndroid() {
		String ua = Window.Navigator.getUserAgent();
		return (ua.toLowerCase().contains("android"));
	}

	private void openPopup(String url) {
		Window.open(url, "_blank", null);
	}

	@Override
	protected void onUnload() {
		// problema del rilancio download a seguito di riapertura presenter
		downloadFrame.setUrl("");
		super.onUnload();
	}

}
