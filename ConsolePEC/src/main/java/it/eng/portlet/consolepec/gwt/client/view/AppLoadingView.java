package it.eng.portlet.consolepec.gwt.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.portlet.consolepec.gwt.client.presenter.AppLoadingPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.images.ResImg;

public class AppLoadingView extends ViewImpl implements AppLoadingPresenter.MyView {

	private final Widget widget;

	@UiField(provided = true)
	Image imageCaricamento = new Image(ResImg.IMG.ajaxLoaderImg());

	@UiField
	HTMLPanel panel;
	@UiField
	HTMLPanel contentPanel;
	@UiField
	Button attendiButton;
	@UiField
	Button terminaButton;
	@UiField
	Button confermaButton;
	@UiField
	Button annullaButton;
	@UiField
	HTMLPanel terminaButtonPanel;
	@UiField
	HTMLPanel confermaButtonPanel;
	@UiField
	HTMLPanel internalPanel;

	private Command attendiCommand;
	private Command terminaCommand;
	private Command confermaCommand;
	private Command annullaCommand;

	public interface Binder extends UiBinder<Widget, AppLoadingView> {}

	@Inject
	public AppLoadingView(final Binder binder) {
		widget = binder.createAndBindUi(this);

		attendiButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				attendiCommand.execute();

			}
		});

		terminaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				terminaCommand.execute();
			}
		});

		confermaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				confermaCommand.execute();
			}
		});

		annullaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				annullaCommand.execute();
			}
		});
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void nascondi() {
		panel.setVisible(false);
	}

	@Override
	public void mostra() {
		panel.setVisible(true);
	}

	@Override
	public void setMessageHtml(HTML htmlMessage) {
		this.contentPanel.clear();
		this.contentPanel.add(htmlMessage);
	}

	@Override
	public void setMessagePanel(HTMLPanel panel) {
		this.contentPanel.clear();
		this.contentPanel.add(panel);
	}

	@Override
	public void showImageCaricamento(boolean b) {
		imageCaricamento.setVisible(b);
	}

	@Override
	public void setTerminaCommand(Command terminaCommand) {
		this.terminaCommand = terminaCommand;
	}

	@Override
	public void setAttendiCommand(Command attendiCommand) {
		this.attendiCommand = attendiCommand;
	}

	@Override
	public void showTeminaAttendiButtons(boolean enabled) {
		terminaButtonPanel.setVisible(enabled);
		if (enabled) {
			internalPanel.removeStyleName("apploading-content");
			internalPanel.addStyleName("apploading-content-wait");
			confermaButtonPanel.setVisible(false);
		} else {
			internalPanel.removeStyleName("apploading-content-wait");
			internalPanel.addStyleName("apploading-content");
		}
	}

	@Override
	public void showConfermaAnnullaButtons(boolean enabled) {
		confermaButtonPanel.setVisible(enabled);
		if (enabled) {
			internalPanel.removeStyleName("apploading-content");
			internalPanel.addStyleName("apploading-content-wait");
			terminaButtonPanel.setVisible(false);
		} else {
			internalPanel.removeStyleName("apploading-content-wait");
			internalPanel.addStyleName("apploading-content");
		}

	}

	@Override
	public void setConfermaCommand(final Command confermaCommand) {
		this.confermaCommand = confermaCommand;
	}

	@Override
	public void setAnnullaCommand(Command annullaCommand) {
		this.annullaCommand = annullaCommand;
	}

}
