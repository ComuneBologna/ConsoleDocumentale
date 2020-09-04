package it.eng.portlet.consolepec.gwt.client.view.pecout;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HRElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.portlet.consolepec.gwt.client.presenter.pecout.DettaglioPecOutInviatoPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.pecout.DettaglioPecOutInviatoPresenter.GoToPraticaCommand;
import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.widget.DettaglioAllegatiWidget;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoPraticaCollegataWidget;
import it.eng.portlet.consolepec.gwt.client.widget.RicevuteErroreWidget;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.Destinatario;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

public class DettaglioPecOutInviatoView extends ViewImpl implements DettaglioPecOutInviatoPresenter.MyView {

	private final Widget widget;
	@UiField
	Label mittente;
	@UiField
	Element mainTitle;
	@UiField
	Label idDocumentaleReinoltro;
	@UiField
	Label idDocumentale;
	@UiField
	Label idMail;
	@UiField
	HTMLPanel oggettoPanel;

	@UiField
	Label PG;
	@UiField
	Label data;
	@UiField
	Label tipoMail;
	@UiField
	Label stato;
	@UiField
	Label visibileA;
	@UiField
	Label assegantario;
	@UiField
	DettaglioAllegatiWidget dettaglioAllegatiWidget;
	@UiField
	HTMLPanel destinatariCCPanel;
	@UiField
	HTMLPanel destinatariPanel;
	@UiField
	HTMLPanel bodyPanel;
	@UiField
	HTMLPanel firmaPanel;
	@UiField
	Button chiudiButton;
	@UiField
	Button reinoltraButton;
	@UiField
	Button ricevutaButton;
	@UiField
	DownloadAllegatoWidget downloadWidget;
	@UiField
	HRElement praticheCollegateHR;
	@UiField(provided = true)
	DisclosurePanel praticheCollegatePanel = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Pratiche collegate");
	@UiField
	Label interoperabile;
	@UiField
	HTMLPanel interoperabilePanel;
	@UiField
	HRElement errRicevutePanelHR;
	@UiField(provided = true)
	DisclosurePanel errRicevutePanel = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Errori di invio");

	private HTMLPanel praticheCollegateInnerPanel;

	private PecOutDTO pecOutBozzaDTO;

	private GoToPraticaCommand goToPraticaCommand;

	public interface Binder extends UiBinder<Widget, DettaglioPecOutInviatoView> {}

	@Inject
	public DettaglioPecOutInviatoView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {

		return widget;
	}

	@Override
	public void mostraBozza(PecOutDTO pec) {
		/* se cambia la pratica, reset dei disclosure */

		pecOutBozzaDTO = pec;

		String stato = sanitizeNull(pec.getStatoLabel());
		this.stato.setText(stato);
		String data = sanitizeNull(pec.getDataInvio());
		this.data.setText(data);
		if (pec.getNumeroPG() != null && pec.getAnnoPG() != null) {
			this.PG.setText(pec.getNumeroPG() + "/" + pec.getAnnoPG());
		} else {
			this.PG.setText(sanitizeNull(null));
		}
		this.assegantario.setText(sanitizeNull(pec.getAssegnatario()));
		this.visibileA.setText(sanitizeNull(pec.getVisibileA()));
		this.mittente.setText(sanitizeNull(pec.getMittente()));
		this.idDocumentaleReinoltro.setText(sanitizeReinoltro(pecOutBozzaDTO.getMessageIdReinoltro()));
		this.reinoltraButton.setEnabled(pecOutBozzaDTO.isReinoltrabile());
		this.ricevutaButton.setEnabled(pecOutBozzaDTO.hasRicevuteConsegna());

		String oggetto = sanitizeNull(pecOutBozzaDTO.getTitolo());
		SafeHtml oggettoHtml = SafeHtmlUtils.fromString(oggetto);
		HTML hOggetto = new HTML(oggettoHtml);
		oggettoPanel.clear();
		oggettoPanel.add(hOggetto);

		tipoMail.setText(sanitizeNull(pec.getTipoEmail()));

		String body = sanitizeNull(pec.getBody());
		body = formatText(body);
		HTML hBody = new HTML(body);

		String firma = sanitizeNull(pec.getFirma());
		firma = formatText(firma);
		HTML hFirma = new HTML(firma);

		this.bodyPanel.clear();
		this.firmaPanel.clear();

		this.bodyPanel.add(hBody);
		this.firmaPanel.add(hFirma);

		this.idDocumentale.setText(sanitizeNull(pec.getNumeroRepertorio()));
		this.idMail.setText(sanitizeNull(pec.getMailId()));

		/* pannello destinatari */
		StringBuilder sb = new StringBuilder();
		if (pecOutBozzaDTO.getDestinatari() != null) {

			if (pecOutBozzaDTO.getDestinatari().size() != 0) {
				for (Destinatario dest : pecOutBozzaDTO.getDestinatari()) {
					sb.append("<span>" + dest.getLabel() + "</span> <br/>");
				}
			} else {
				sb.append(sanitizeNull(null));
			}
		} else {
			sb.append(sanitizeNull(null));
		}
		HTML destinatari = new HTML(sb.toString());
		this.destinatariPanel.clear();
		this.destinatariPanel.add(destinatari);

		/* pannello destinatari cc */
		sb = new StringBuilder();
		if (pecOutBozzaDTO.getDestinatariCC() != null) {

			if (pecOutBozzaDTO.getDestinatariCC().size() != 0) {
				for (Destinatario dest : pecOutBozzaDTO.getDestinatariCC()) {
					sb.append("<span>" + dest.getLabel() + "</span> <br/>");
				}
			} else {
				sb.append(sanitizeNull(null));
			}
		} else {
			sb.append(sanitizeNull(null));
		}
		HTML destinatariCC = new HTML(sb.toString());
		this.destinatariCCPanel.clear();
		this.destinatariCCPanel.add(destinatariCC);

		praticheCollegatePanel.setVisible(false);
		praticheCollegatePanel.clear();
		if (pec.getIdPraticheCollegate().size() > 0) {
			praticheCollegateInnerPanel = new HTMLPanel("");
			praticheCollegateHR.getStyle().setDisplay(Display.BLOCK);
		} else {

			praticheCollegateHR.getStyle().setDisplay(Display.NONE);
		}
		praticheCollegatePanel.setOpen(false);

		aggiornaTabellaAllegati(pec);

		if (pec.isInteroperabile()) {
			interoperabilePanel.setVisible(true);
			interoperabile.setText("Si");
		} else {
			interoperabilePanel.setVisible(false);
			interoperabile.setText("-");
		}

		errRicevutePanel.setVisible(false);
		errRicevutePanel.clear();
		if (!pec.getRicevuteErrore().isEmpty()) {

			RicevuteErroreWidget errRicevuteWidget = new RicevuteErroreWidget();
			errRicevuteWidget.initWidget(pec.getRicevuteErrore());
			errRicevutePanel.add(errRicevuteWidget);

			errRicevutePanel.setVisible(true);
			errRicevutePanelHR.getStyle().setDisplay(Display.BLOCK);
		} else {
			errRicevutePanelHR.getStyle().setDisplay(Display.NONE);
		}
	}

	@Override
	public void addPraticaCollegataSection(PraticaDTO pratica) {
		UListElement ul = Document.get().createULElement();
		ul.addClassName("contenitore-lista-gruppi");
		LIElement li = Document.get().createLIElement();
		li.addClassName("gruppo last clearfix");
		ul.appendChild(li);
		SpanElement span = Document.get().createSpanElement();
		span.addClassName("label nessun-protocollo");
		li.appendChild(span);
		DivElement div = Document.get().createDivElement();
		div.setClassName("corpo");
		li.appendChild(div);
		HTMLPanel panel = new HTMLPanel("");
		panel.setStyleName("box-mail");
		ElementoPraticaCollegataWidget dettaglioWiget = new ElementoPraticaCollegataWidget();
		dettaglioWiget.setCheckBoxVisible(false);
		dettaglioWiget.setCheckBoxEnabled(false);
		dettaglioWiget.setCommand(this.goToPraticaCommand);
		panel.add(dettaglioWiget);
		dettaglioWiget.mostraDettaglio(pratica);
		praticheCollegateInnerPanel.getElement().appendChild(ul);
		praticheCollegateInnerPanel.add(panel, div);
	}

	@Override
	public void buildPanelPraticheCollegate() {
		praticheCollegatePanel.add(praticheCollegateInnerPanel);
		praticheCollegatePanel.setVisible(true);
	}

	private String sanitizeReinoltro(String messageIdReinoltro) {
		String reinoltro = sanitizeNull(messageIdReinoltro);
		if (reinoltro.equals("-"))
			return reinoltro;
		String[] splittedReinoltro = reinoltro.split("/");
		return splittedReinoltro[4];
	}

	private void aggiornaTabellaAllegati(PecOutDTO bozzaPecOut) {
		dettaglioAllegatiWidget.mostraAllegati(bozzaPecOut.getAllegati(), false);
	}

	@Override
	public void setDownloadAllegatoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> downloadAllegatoCommand) {
		dettaglioAllegatiWidget.setDownloadAllegatoCommand(downloadAllegatoCommand);
	}

	private String sanitizeNull(String input) {
		if (input == null || input.trim().isEmpty())
			return "-";
		else
			return input;
	}

	private String formatText(String body) {
		if (body.contains("<"))
			return body;
		body = body.replaceAll("(\r\n|\n\r|\r|\n)", "<br/>");
		body = body.replaceAll("\t", "&nbsp;");
		body = body.replaceAll(" ", "&nbsp;");
		return body;
	}

	@Override
	public Button getChiudiButton() {
		return this.chiudiButton;
	}

	@Override
	public Button getReinoltraButton() {
		return this.reinoltraButton;
	}

	@Override
	public Button getRicevutaButton() {
		return this.ricevutaButton;
	}

	@Override
	public void sendDownload(SafeUri uri) {
		downloadWidget.sendDownload(uri);
	}

	@Override
	public void initTitle(boolean isReinoltro) {
		if (isReinoltro)
			mainTitle.setInnerText(" Email di reinoltro ");
		else
			mainTitle.setInnerText(" Email in uscita ");

	}

	@Override
	public void setGotoPraticaCommand(GoToPraticaCommand goToPraticaCommand) {
		this.goToPraticaCommand = goToPraticaCommand;

	}

	@Override
	public void resetDisclosurePanels(boolean showActions) {
		this.praticheCollegatePanel.setOpen(false);
	}

}
