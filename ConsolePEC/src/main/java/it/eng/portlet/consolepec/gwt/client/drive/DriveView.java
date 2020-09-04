package it.eng.portlet.consolepec.gwt.client.drive;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.drive.Cartella;
import it.eng.cobo.consolepec.commons.drive.DriveElement;
import it.eng.cobo.consolepec.commons.drive.File;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.drive.DrivePresenter.DrivePlace;
import it.eng.portlet.consolepec.gwt.client.drive.event.ApriCartellaEvent;
import it.eng.portlet.consolepec.gwt.client.drive.util.DrawingUtil;
import it.eng.portlet.consolepec.gwt.client.drive.widget.DriveMessageWidget;
import it.eng.portlet.consolepec.gwt.client.drive.widget.WidgetCartella;
import it.eng.portlet.consolepec.gwt.client.drive.widget.WidgetFile;
import it.eng.portlet.consolepec.gwt.client.drive.widget.WidgetHistory;
import it.eng.portlet.consolepec.gwt.client.handler.drive.DriveHandler;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;

/**
 * @author Giacomo F.M.
 * @since 2019-05-30
 */
public class DriveView extends ViewImpl implements DrivePresenter.MyView {

	public static final DateTimeFormat DRIVE_DATE_TIME_FORMAT = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATAORA);

	public static final String PAGING_STYLE = "drive-paging-div";

	public static final String MAIN_DIV_STYLE = "drive-main-div";
	public static final String DIV_BTN_STYLE = "drive-div-btn";

	public static final String BTN_HISTORY_STYLE = "drive-history-btn";
	public static final String BTN_STYLE = "drive-btn";
	public static final String LEFT_SUFFIX = "left";
	public static final String MIDDLE_SUFFIX = "middle";
	public static final String RIGHT_SUFFIX = "right";
	// public static final String HIDE_SUFFIX = "out";

	@UiField
	HTMLPanel historyPanel, btnPanel, mainPanel;

	Button detailBtn;

	@UiField
	TextBox ricercaText;

	@Override
	public String getTextRicerca() {
		return ricercaText.getText();
	}

	@UiField
	DriveMessageWidget message;

	@Override
	public DriveMessageWidget getMessageWidget() {
		return this.message;
	}

	@UiField
	DownloadAllegatoWidget downloadWidget;

	@Override
	public DownloadAllegatoWidget getDownloadWidget() {
		return this.downloadWidget;
	}

	public interface Binder extends UiBinder<Widget, DriveView> {/**/}

	@Inject
	private DriveHandler driveHandler;

	@Inject
	public DriveView(final Binder binder) {
		initWidget(binder.createAndBindUi(this));
	}

	private void clear() {
		Window.scrollTo(0, 0);
		if (historyPanel != null)
			historyPanel.clear();
		if (mainPanel != null)
			mainPanel.clear();
	}

	@Override
	public void bindDettaglioCartella(final Command command) {
		detailBtn = DrawingUtil.drawFunBtn("Dettaglio cartella", ImageSource.loadServices(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
		btnPanel.add(detailBtn);
	}

	@Override
	public void bindCreaCartella(final Command command) {
		btnPanel.add(DrawingUtil.drawFunBtn("Crea cartella", ImageSource.loadPlus(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		}));
	}

	@Override
	public void bindUploadFile(final Command command) {
		btnPanel.add(DrawingUtil.drawFunBtn("Carica file", ImageSource.loadUpload(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		}));
	}

	@Override
	public void bindRicerca(final Command command) {
		btnPanel.add(DrawingUtil.drawFunBtn("Cerca", ImageSource.loadSearch(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		}));
	}

	@Override
	public void apriCartella(final EventBus eventBus, final DrivePlace currentPlace, final Cartella cartella, final List<DriveElement> elements) {
		clear();
		historyPanel.add(new WidgetHistory(currentPlace.getHistory(), eventBus));

		if (detailBtn != null) {
			detailBtn.setEnabled(!currentPlace.getHistory().getLast().getId().equals(Cartella.PRE_ID + 0));
		}

		for (DriveElement e : elements) {
			if (e instanceof Cartella) {
				mainPanel.add(new WidgetCartella((Cartella) e, eventBus, driveHandler));
			}
			if (e instanceof File) {
				mainPanel.add(new WidgetFile((File) e, eventBus, driveHandler));
			}
		}

		if (elements.size() > DrivePresenter.LIMIT) {
			mainPanel.add(pagination(eventBus, currentPlace, cartella));
		}
	}

	private static Widget pagination(final EventBus eventBus, final DrivePlace currentPlace, final Cartella cartella) {
		HTMLPanel paging = new HTMLPanel("");
		paging.setStylePrimaryName(PAGING_STYLE);
		Button backward = new Button("<"), forward = new Button(">");
		backward.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new ApriCartellaEvent(cartella.getId(), currentPlace.getPage() - 1));
			}
		});
		forward.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new ApriCartellaEvent(cartella.getId(), currentPlace.getPage() + 1));
			}
		});
		backward.setEnabled(currentPlace.getPage() > 1);
		forward.setEnabled(currentPlace.getPage() < (Math.ceil(cartella.getIdFigli().size() / DrivePresenter.LIMIT.doubleValue())));
		paging.add(backward);
		paging.add(new InlineLabel(currentPlace.getPage() + "/" + (int) Math.ceil(cartella.getIdFigli().size() / DrivePresenter.LIMIT.doubleValue())));
		paging.add(forward);
		return paging;
	}

}
