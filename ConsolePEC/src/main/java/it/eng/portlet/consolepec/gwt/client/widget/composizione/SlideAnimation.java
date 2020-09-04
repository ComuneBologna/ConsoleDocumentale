package it.eng.portlet.consolepec.gwt.client.widget.composizione;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTMLPanel;

public class SlideAnimation extends Animation {
	private final HTMLPanel widget;
	private boolean opening;

	private int height;
	public SlideAnimation(HTMLPanel panel) {
		this.widget = panel;
	}

	@Override
	protected void onComplete() {
		if (!opening)
			this.widget.setVisible(false);
		
		this.widget.getElement().getStyle().clearHeight();
	}

	@Override
	protected void onStart() {
		super.onStart();
		opening = !this.widget.isVisible();

		if (opening) {
			this.widget.getElement().getStyle().setHeight(0, Unit.PX);
			this.widget.setVisible(true);
		}

		this.height = widget.getElement().getScrollHeight();
		
	}

	@Override
	protected void onUpdate(double progress) {
		double heightCalcolata = progress * height;
		
		if (!opening) {
			heightCalcolata = height - heightCalcolata;
		}
		heightCalcolata = Math.max(heightCalcolata, 1);
		this.widget.getElement().getStyle().setHeight(heightCalcolata, Unit.PX);
	}
}