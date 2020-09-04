package it.eng.portlet.consolepec.gwt.client.view.pecout;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import it.eng.portlet.consolepec.gwt.client.drive.ImageSource;
import it.eng.portlet.consolepec.gwt.client.drive.util.DrawingUtil;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.action.pec.InvioMailDaCSVResult;
import it.eng.portlet.consolepec.gwt.shared.model.InvioCsvEsito;

public class EsitoInvioDaCSVDialog extends DialogBox {

	private static final String GLASS_STYLE = "drive-glass-style";
	private static final String POPUP_STYLE = "drive-popup";
	private static final String POPUP_CONTENT_STYLE = "drive-popup-content";
	private static final String POPUP_CONTENT_TITLE_STYLE = "drive-popup-content-title";
	private static final String POPUP_CONTENT_DIV_STYLE = "drive-popup-content-div";
	private static final String POPUP_DIV_BUTTON_STYLE = "drive-fun-box";

	private EventBus eventBus;
	private DownloadAllegatoWidget downloadAllegatoWidget;

	public EsitoInvioDaCSVDialog(final String title, final InvioMailDaCSVResult result, final String clientIdFascicolo, final EventBus eventBus) {
		super();
		this.eventBus = eventBus;
		setText(title);
		setStylePrimaryName(POPUP_STYLE);

		setGlassEnabled(true);
		setGlassStyleName(getGlassStyleName() + " " + GLASS_STYLE);
		setAnimationEnabled(true);

		HTMLPanel main = new HTMLPanel("");
		main.add(drawInfoPanel(result));

		SafeUri safeUri = null;

		safeUri = result.getEsitoFileName() != null ? UriMapping.generaDownalodFile(result.getEsitoFileName()) : null;
		downloadAllegatoWidget = new DownloadAllegatoWidget();
		main.add(downloadAllegatoWidget);
		main.add(drawButtonPanel(clientIdFascicolo, safeUri));

		setWidget(main);
	}

	public void open() {
		this.center();
	}

	public void close() {
		this.hide();
	}

	private Widget drawButtonPanel(final String clientIdFascicolo, final SafeUri uriEsito) {
		HTMLPanel button = new HTMLPanel("");
		button.setStylePrimaryName(POPUP_DIV_BUTTON_STYLE);

		Button reportButton = DrawingUtil.drawFunBtn("Report", ImageSource.loadInfo(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (uriEsito != null)
					downloadAllegatoWidget.sendDownload(uriEsito, false);
			}
		});

		button.add(reportButton);
		reportButton.setEnabled(uriEsito != null);

		button.add(DrawingUtil.drawFunBtn("Ok", ImageSource.loadOk(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Place place = new Place();
				place.setToken(NameTokens.dettagliofascicolo);
				place.addParam(NameTokensParams.idPratica, clientIdFascicolo);
				place.addParam(NameTokensParams.resetComposizioneFascicolo, Boolean.toString(true));
				eventBus.fireEvent(new GoToPlaceEvent(place));
				close();
			}
		}));

		return button;
	}

	private static Widget drawInfoPanel(InvioMailDaCSVResult result) {
		HTMLPanel content = new HTMLPanel("");
		content.setStylePrimaryName(POPUP_CONTENT_STYLE);

		if (!result.isAsync()) {

			if (result.isValido()) {
				int errori = 0;
				int ok = 0;

				for (InvioCsvEsito esito : result.getEsiti()) {
					if (!esito.isErrore()) {
						ok++;

					} else {
						errori++;
					}
				}

				HTMLPanel title = new HTMLPanel("Esiti");
				title.setStylePrimaryName(POPUP_CONTENT_TITLE_STYLE);
				content.add(title);
				content.add(drawSimpleBox("Record totali", String.valueOf(result.getEsiti().size())));
				content.add(drawSimpleBox("Record inviati correttamente", String.valueOf(ok)));
				content.add(drawSimpleBox("Record con errore", String.valueOf(errori)));

			} else {
				int err = 0;

				for (InvioCsvEsito esito : result.getEsiti()) {
					if (esito.isErrore()) {
						err++;
					}
				}

				HTMLPanel title = new HTMLPanel("Errore");
				title.setStylePrimaryName(POPUP_CONTENT_TITLE_STYLE);
				content.add(title);
				content.add(drawSimpleBox("Record totali", String.valueOf(result.getEsiti().size())));
				content.add(drawSimpleBox("Record con errore", String.valueOf(err)));
			}

		} else {
			HTMLPanel title = new HTMLPanel("Preso in carico");
			title.setStylePrimaryName(POPUP_CONTENT_TITLE_STYLE);
			content.add(title);
			content.add(drawSimpleBox("L'invio è stato preso in carico", ""));
		}

		return content;

	}

	private static Widget drawSimpleBox(final String chiave, final String valore) {
		HTMLPanel div = new HTMLPanel("");
		div.setStylePrimaryName(POPUP_CONTENT_DIV_STYLE);
		div.add(new InlineLabel(chiave));
		div.add(new InlineLabel(valore));
		return div;
	}
}
