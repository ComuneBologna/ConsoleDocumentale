package it.eng.portlet.consolepec.gwt.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Widget che mantiene il keep alive attraverso le api javascript di liferay Va dichiarata solo una volta all'interno dell'app GWT, ad esempio nella view principale.
 * 
 * @author pluttero
 * 
 */
public class LiferaySessionKeepAliveWidget extends Composite {
	private static LiferaySessionKeepAliveWidgetUiBinder uiBinder = GWT.create(LiferaySessionKeepAliveWidgetUiBinder.class);
	private static final double WAIT_PERC = 0.70;
	private static Timer timer;

	interface LiferaySessionKeepAliveWidgetUiBinder extends UiBinder<Widget, LiferaySessionKeepAliveWidget> {
	}

	public LiferaySessionKeepAliveWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
		init();
	}

	private void init() {
		final long millis = Math.round((getExpirationMillis()) * WAIT_PERC);
		extend();
		timer = new Timer() {

			@Override
			public void run() {
				extend();
				this.schedule(Math.round(millis));
			}

		};
		timer.schedule((int) millis);

	}

	/**
	 * Chiama la primitiva Liferay per l'estensione della sessione
	 */
	private native void extend() /*-{
									if($wnd.Liferay.Session!=undefined)
									$wnd.Liferay.Session.extend();
									}-*/;

	/**
	 * Chiama la primitiva Liferay per conoscere la lunghezza della sessione in millis
	 * 
	 * @return
	 */
	// private native int getExpirationMillis() /*-{
	// return $wnd.Liferay.Session.getAttrs()['sessionLength'];
	// }-*/;
	/**
	 * Al momento 5 min staticamente. La scadenza effettiva di sessione, sembra non essere sufficiente
	 * 
	 * @return
	 */
	private int getExpirationMillis() {
		return 60 * 5 * 1000;
	}

}
