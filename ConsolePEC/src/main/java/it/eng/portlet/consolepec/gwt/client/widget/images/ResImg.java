package it.eng.portlet.consolepec.gwt.client.widget.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ResImg extends ClientBundle {
	public static final ResImg IMG = GWT.create( ResImg.class);
	
	@Source("ajax-loader.gif")
	public ImageResource ajaxLoaderImg();
	
	@Source("help.png")
	public ImageResource helpImg();

}
