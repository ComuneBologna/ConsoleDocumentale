package it.eng.portlet.consolepec.gwt.client.view.pratica;

import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.presenter.pratica.DettaglioRidottoPraticaPresenter.MyView;
import it.eng.portlet.consolepec.gwt.client.util.VisualizzazioneElementiProtocollati;
import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoAllegatoElencoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecUtils;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;

import java.util.Collection;
import java.util.List;

import com.google.gwt.dom.client.HRElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

public class DettaglioRidottoPraticaView extends ViewImpl implements MyView {
	

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, DettaglioRidottoPraticaView> {
	}

	@UiField(provided = true)
	MessageAlertWidget messageWidget;

	
	@UiField
	HeadingElement titoloFascicolo;
	@UiField
	DownloadAllegatoWidget downloadWidget;
	@UiField
	HTMLPanel elencoAllegatiPanel;
	@UiField(provided = true)
	DisclosurePanel dettaglioAllegatiPanel = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Allegati");
	@UiField
	HRElement dettaglioAllegatiHR;
	@UiField
	Button chiudiButton;
	@UiField
	HTMLPanel dettaglio;
	
	private it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, AllegatoDTO> mostraDettaglioAllegatoCommand;
	private it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> downloadAllegatoCommand;
	
	
	
	@Inject
	public DettaglioRidottoPraticaView(final Binder binder, final EventBus eventBus) {
		messageWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}


	@Override
	public void mostraPratica(PraticaDTO pratica){
		dettaglio.clear();
		
		if(PraticaUtil.isIngresso(pratica.getTipologiaPratica())){
			PecInDTO pec = (PecInDTO) pratica;
			addCampo("ID documentale", pec.getNumeroRepertorio(), CampoRidottoTipologia.LABEL);
			addCampo("Mittente", pec.getMittente(), CampoRidottoTipologia.LABEL);
			addCampo("Destinatari", pec.getDestinatari(), CampoRidottoTipologia.LISTA);
			addCampo("Destinatari CC", pec.getDestinatariCC(), CampoRidottoTipologia.LISTA);
			addCampo("Data Ricezione", pec.getDataOraCreazione(), CampoRidottoTipologia.LABEL);
			addCampo("Oggetto", pec.getTitolo(), CampoRidottoTipologia.RAW_HTML);
			addCampo("Corpo mail", pec.getBody(), CampoRidottoTipologia.TEXTAREA);
			titoloFascicolo.setInnerText("Dettaglio Email");
			
		} else if(PraticaUtil.isEmailOut(pratica.getTipologiaPratica())){
			PecOutDTO pec = (PecOutDTO) pratica;
			addCampo("ID documentale", pec.getNumeroRepertorio(), CampoRidottoTipologia.LABEL);
			addCampo("Mittente", pec.getMittente(), CampoRidottoTipologia.LABEL);
			addCampo("Destinatari", pec.getDestinatari(), CampoRidottoTipologia.LISTA);
			addCampo("Destinatari CC", pec.getDestinatariCC(), CampoRidottoTipologia.LISTA);
			addCampo("Data Ricezione", pec.getDataOraCreazione(), CampoRidottoTipologia.LABEL);
			addCampo("Oggetto", pec.getTitolo(), CampoRidottoTipologia.RAW_HTML);
			addCampo("Corpo mail", pec.getBody(), CampoRidottoTipologia.TEXTAREA);
			titoloFascicolo.setInnerText("Dettaglio Email");
			
		} else if(PraticaUtil.isPraticaModulistica(pratica.getTipologiaPratica())){
			PraticaModulisticaDTO pec = (PraticaModulisticaDTO) pratica;
			addCampo("ID documentale", pec.getNumeroRepertorio(), CampoRidottoTipologia.LABEL);
			addCampo("Nome", pec.getNome(), CampoRidottoTipologia.LABEL);
			
			titoloFascicolo.setInnerText("Dettaglio Modulo");
		}
		
		mostraAllegati(pratica.getAllegati());
		Window.scrollTo(0, 0);
	}
	
	public void addCampo(String etichetta, Object valore, CampoRidottoTipologia tipologia){
		
		
		HTMLPanel panel = new HTMLPanel("");
		
		SpanElement label = SpanElement.as(DOM.createSpan());
		label.setClassName("label");
		label.setInnerText(etichetta);
		panel.getElement().appendChild(label);

		
		HTMLPanel valorePanel = new HTMLPanel("");
		valorePanel.setStyleName("abstract");
		panel.add(valorePanel);
		
		switch(tipologia){
			case LABEL:
				valorePanel.add(new Label(sanitizeNull(valore)));
				break;
			case LISTA:
				Collection<?> coll = (Collection<?>) valore;
				StringBuilder sb = new StringBuilder();
				if (coll != null) {

					if (coll.size() != 0) {
						for (Object obj: coll) {
							sb.append("<span>" + obj + "</span> <br/>");
						}
					} else {
						sb.append(sanitizeNull(null));
					}
				} else {
					sb.append(sanitizeNull(null));
				}

				HTML lista = new HTML(sb.toString());
				valorePanel.add(lista);
				break;
			case TEXTAREA:
				String text = sanitizeNull(valore);
				text = ConsolePecUtils.formatText(text);
				HTML body = new HTML(text);
				valorePanel.add(body);
				break;
			case RAW_HTML:
				SafeHtml safeValore = SafeHtmlUtils.fromString(sanitizeNull(valore));
				HTML safeHtml = new HTML(safeValore);
				valorePanel.add(safeHtml);
				break;
			default: throw new IllegalArgumentException("Tipo Campo non supportato");
			
		
		}
		
		
		
		dettaglio.add(panel);

	}
		
	

	@Override
	public void setMostraDettaglioAllegatoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, AllegatoDTO> mostraDettaglioAllegatoCommand) {
		this.mostraDettaglioAllegatoCommand = mostraDettaglioAllegatoCommand;
	}
	
	@Override
	public void sendDownload(SafeUri uri) {
		downloadWidget.sendDownload(uri);
	}

	
	@Override
	public void setDownloadAllegatoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> downloadAllegatoCommand) {
		this.downloadAllegatoCommand = downloadAllegatoCommand;
	}



	@Override
	public void setChiudiDettaglioCommand(final Command chiudiDettaglioCommand) {
		this.chiudiButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				chiudiDettaglioCommand.execute();

			}
		});
	}

	private void mostraAllegati(List<AllegatoDTO> allegati) {
		 
		
		elencoAllegatiPanel.clear();
		while (elencoAllegatiPanel.getElement().hasChildNodes()) {
			elencoAllegatiPanel.getElement().removeChild(elencoAllegatiPanel.getElement().getLastChild());
		}
		
		
		
		if (allegati.size() > 0) {
			
			HTMLPanel pannelloGruppo = VisualizzazioneElementiProtocollati.buildGruppoPanel("Allegati", elencoAllegatiPanel);
			elencoAllegatiPanel.setVisible(true);
			dettaglioAllegatiPanel.setVisible(true);
			dettaglioAllegatiHR.getStyle().setDisplay(Display.BLOCK);
			for (final AllegatoDTO allegato : allegati) {
				ElementoAllegatoElencoWidget allgwidget = new ElementoAllegatoElencoWidget();
				allgwidget.setCheckBoxVisible(false);
				
				
				allgwidget.setDownloadAllegatoCommand(new it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO>() {
					@Override
					public Void exe(AllegatoDTO allegato) {
						downloadAllegatoCommand.exe(allegato);
						return null;
					}
				});
				allgwidget.setMostraDettaglioAllegatoCommand(new it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO>() {

					@Override
					public Void exe(AllegatoDTO allegato) {
						mostraDettaglioAllegatoCommand.exe(allegato);
						return null;
					}
				});
				
				pannelloGruppo.add(allgwidget);
				allgwidget.mostraDettaglio(allegato);
			
				
				
			}
			
			
			
		} else {
			elencoAllegatiPanel.setVisible(false);
			dettaglioAllegatiPanel.setVisible(false);
			dettaglioAllegatiHR.getStyle().setDisplay(Display.NONE);
		}
	}
	
	private String sanitizeNull(Object input) {
		return GenericsUtil.sanitizeNullObject(input, "-");
	}
	
	private enum CampoRidottoTipologia {
		LABEL, TEXTAREA, LISTA, RAW_HTML
		
	}
}
