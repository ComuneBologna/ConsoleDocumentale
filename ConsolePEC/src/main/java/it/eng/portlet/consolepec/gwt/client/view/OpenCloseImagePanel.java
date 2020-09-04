package it.eng.portlet.consolepec.gwt.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;

public interface OpenCloseImagePanel extends ClientBundle {
	public final static OpenCloseImagePanel DEFAULT_IMAGES = GWT.create(OpenCloseImagePanel.class);

	@ImageOptions(flipRtl = true)
	@Source("close.png")
	ImageResource disclosurePanelClosed();

	@Source("open.png")
	ImageResource disclosurePanelOpen();

}
