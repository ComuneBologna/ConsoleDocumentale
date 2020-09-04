package it.eng.portlet.consolepec.gwt.client.widget;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;

/**
 * Implementazione di DateBox che gestisce la chiusura del {@link DatePicker} anche a fronte dell'evente onBlur Questo permette di gestire il tabbing mediante tastiera iPad
 * 
 * @author pluttero
 * 
 */
public class IpadDateBox extends DateBox {

	public IpadDateBox() {
		super();
		new DateBoxBlurHandler(this);
		new DateBoxTouchStartHandler(this);
	}

	class DateBoxBlurHandler implements BlurHandler {

		private final DateBox db;
		private boolean over = false;

		public DateBoxBlurHandler(DateBox db) {
			this.db = db;
			db.getTextBox().addBlurHandler(this);
			db.getDatePicker().addDomHandler(new MouseOverHandler() {
				@Override
				public void onMouseOver(MouseOverEvent event) {
					if(IpadDateBox.this.isEnabled())
						over = true;
				}
			}, MouseOverEvent.getType());
		}

		@Override
		public void onBlur(BlurEvent event) {
			if(IpadDateBox.this.isEnabled()){
				if (!over)
					db.hideDatePicker();
	
				over = false;
			}
		}
	}

	// Handles reopening the datepicker when focus is lost (blur)
	class DateBoxTouchStartHandler implements TouchStartHandler {

		private final DateBox db;

		public DateBoxTouchStartHandler(DateBox dateBox) {
			this.db = dateBox;
			db.getTextBox().addTouchStartHandler(this);
			db.addHandler(new TouchStartHandler() {

				@Override
				public void onTouchStart(TouchStartEvent arg0) {
					if(IpadDateBox.this.isEnabled())
						db.showDatePicker();
				}

			}, TouchStartEvent.getType());
		}

		@Override
		public void onTouchStart(TouchStartEvent arg0) {
			if(IpadDateBox.this.isEnabled())
				db.showDatePicker();
		}

	}
}
