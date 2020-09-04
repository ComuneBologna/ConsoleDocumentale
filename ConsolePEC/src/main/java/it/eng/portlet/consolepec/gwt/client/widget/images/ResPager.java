package it.eng.portlet.consolepec.gwt.client.widget.images;


import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.user.cellview.client.SimplePager;


public interface ResPager extends SimplePager.Resources
{

   
	public static final SimplePager.Resources CSS =   GWT.create(ResPager.class);
	
	@Override
	@Source({"com/google/gwt/user/cellview/client/SimplePager.css", "../css/pager.css"})
	@CssResource.NotStrict
	PagerStyle simplePagerStyle();

	@Override
	@ImageOptions(flipRtl = true)
	ImageResource simplePagerFastForward();
	
	@Override
	@ImageOptions(flipRtl = true)
	ImageResource simplePagerFirstPage();
	
	@Override
	@ImageOptions(flipRtl = true)
	ImageResource simplePagerLastPage();
	
	@Override
	@ImageOptions(flipRtl = true)
	ImageResource simplePagerNextPage();
	
	@Override
	@ImageOptions(flipRtl = true)
	ImageResource simplePagerPreviousPage();
    
    interface PagerStyle extends SimplePager.Style {}
}