package it.eng.portlet.consolepec.gwt.client.drive.popup;

import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import it.eng.cobo.consolepec.commons.drive.Dizionario;
import it.eng.cobo.consolepec.commons.drive.DriveElement;
import it.eng.cobo.consolepec.commons.drive.metadato.Metadato;
import it.eng.portlet.consolepec.gwt.client.drive.ImageSource;
import it.eng.portlet.consolepec.gwt.client.drive.util.DrawingUtil;
import it.eng.portlet.consolepec.gwt.client.drive.widget.DriveMetadatiWidget;
import it.eng.portlet.consolepec.gwt.client.drive.widget.DriveMetadatiWidget.ModificheDizionario;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.handler.drive.DriveHandler;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;

/**
 * @author Giacomo F.M.
 * @since 2019-06-07
 */
public abstract class DriveDialog<T extends DriveElement> extends DialogBox {

	protected static final String GLASS_STYLE = "drive-glass-style";
	protected static final String POPUP_STYLE = "drive-popup";
	protected static final String POPUP_CONTENT_STYLE = "drive-popup-content";
	protected static final String POPUP_CONTENT_TITLE_STYLE = "drive-popup-content-title";
	protected static final String POPUP_CONTENT_DIV_STYLE = "drive-popup-content-div";
	protected static final String POPUP_BTN_STYLE = "drive-popup-btn";

	protected static final String POPUP_DIV_BUTTON_STYLE = "drive-fun-box";

	protected T element;
	protected EventBus eventBus;
	protected DriveHandler driveHandler;

	protected DriveMetadatiWidget metadati;

	public DriveDialog(final String title, final T element, final EventBus eventBus, final DriveHandler driveHandler) {
		super();
		this.element = element;
		this.eventBus = eventBus;
		this.driveHandler = driveHandler;
		this.metadati = new DriveMetadatiWidget(element, driveHandler);
		setText(title);
		setStylePrimaryName(POPUP_STYLE);

		setGlassEnabled(true);
		setGlassStyleName(getGlassStyleName() + " " + GLASS_STYLE);
		setAnimationEnabled(true);

		HTMLPanel main = new HTMLPanel("");
		main.add(drawInfoPanel(element));
		main.add(metadati);
		main.add(drawButtonPanel());

		setWidget(main);
	}

	public void open() {
		this.center();
	}

	public void close() {
		this.hide();
	}

	protected abstract Widget drawInfoPanel(final T element);

	protected abstract void update(final Dizionario dizionario, final Set<Metadato> metadati);

	protected static Widget drawSimpleBox(final String chiave, final String valore) {
		HTMLPanel div = new HTMLPanel("");
		div.setStylePrimaryName(POPUP_CONTENT_DIV_STYLE);
		div.add(new InlineLabel(chiave));
		div.add(new InlineLabel(valore));
		return div;
	}

	private Widget drawButtonPanel() {
		HTMLPanel button = new HTMLPanel("");
		button.setStylePrimaryName(POPUP_DIV_BUTTON_STYLE);

		button.add(DrawingUtil.drawFunBtn("Salva", ImageSource.loadSave(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ModificheDizionario mod = metadati.getModifiche();
				if (mod != null) {
					update(mod.getDizionario(), mod.getMetadati());
					close();
				} else {
					Window.alert("Nessuna modifica eseguita");
				}
			}
		}));

		button.add(DrawingUtil.drawFunBtn("Modifica", ImageSource.loadEdit(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				metadati.ridisegna(true);
				open();
			}
		}));

		button.add(DrawingUtil.drawFunBtn("Annulla", ImageSource.loadCancel(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				metadati.ridisegna(false);
				close();
			}
		}));

		button.add(DrawingUtil.drawFunBtn("Dettaglio", ImageSource.loadServices(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Place place = new Place();
				place.setToken(NameTokens.drivedetail);
				place.addParam(NameTokensParams.id, element.getId());
				eventBus.fireEvent(new GoToPlaceEvent(place));
				close();
			}
		}));
		return button;
	}

}
