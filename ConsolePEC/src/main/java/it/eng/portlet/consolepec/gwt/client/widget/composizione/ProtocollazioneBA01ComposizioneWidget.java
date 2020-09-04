package it.eng.portlet.consolepec.gwt.client.widget.composizione;

import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.CapofilaFromBA01DTO;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * Widget che disegna un riferimento PEC in un elenco di un dettaglio
 * 
 * @author pluttero
 * 
 */
public class ProtocollazioneBA01ComposizioneWidget extends ComplexPanel {

private static final DateTimeFormat df = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATAORA);
	
	CheckBox checkBox;
	
	DivElement divContainer;
	
	DivElement documento;
	DivElement azioni;
	
	private boolean mostraInformazioniDiProtocollazione = false;
	
	public ProtocollazioneBA01ComposizioneWidget() {
		super();
		divContainer = Document.get().createDivElement();
		setElement(divContainer);
		divContainer.setClassName("documenti-mail");
		
		documento  = Document.get().createDivElement();
		documento.setClassName("documento-container");
		divContainer.appendChild(documento);
		
		azioni = Document.get().createDivElement();
		azioni.setClassName("documento-azioni");
		divContainer.appendChild(azioni);
		
		checkBox = new CheckBox();
	}

	public void mostraDettaglio(final CapofilaFromBA01DTO capofila) {
		
		
		/* Checkbox di selezione */
		checkBox.setStyleName("checkbox-nonprot");
		add(checkBox, documento);

		if(mostraInformazioniDiProtocollazione) {
			
			SpanElement capofilaSpan = Document.get().createSpanElement();
			capofilaSpan.setInnerHTML(ConsolePecConstants.CAPOFILA_LABEL);
			capofilaSpan.setClassName("capofila");
			documento.appendChild(capofilaSpan);
				
			SpanElement protocollo = Document.get().createSpanElement();
			protocollo.setInnerText(capofila.getNumeroPg() + "/" + capofila.getAnnoPg());
			protocollo.setClassName("protocollo");
			documento.appendChild(protocollo);
				
			SpanElement span = Document.get().createSpanElement();
			span.setInnerText(" - ");
			documento.appendChild(span);
			
		}
		
		SpanElement data = Document.get().createSpanElement();
		data.setInnerHTML(df.format(capofila.getDataProtocollazione()));
		data.setClassName("data");
		documento.appendChild(data);
		
		
		SpanElement dettaglioSpan = Document.get().createSpanElement();
		dettaglioSpan.setClassName("documento");
		documento.appendChild(dettaglioSpan);
		
		//ICONA
		
		Image iconaFirma = new Image(ConsolePECIcons._instance.fascicoloBa01());
		iconaFirma.setStyleName("ico-mail");
		
		Anchor iconaAnchor = new Anchor();
		iconaAnchor.setStyleName("ico verifica");
		iconaAnchor.setTitle("Visualizza dettagli");
		iconaAnchor.getElement().appendChild(iconaFirma.getElement());
		add(iconaAnchor, dettaglioSpan);
		
		// dettaglio
		
		String oggetto = capofila.getOggetto();
		if (oggetto.length() > ConsolePecConstants.DESCR_WIDGET_ELEMENTI_MAX_LEN)
			oggetto = capofila.getOggetto().substring(0, ConsolePecConstants.DESCR_WIDGET_ELEMENTI_MAX_LEN).concat("...");
		
		Anchor linkDettaglio = new Anchor(oggetto);
		linkDettaglio.setHref("javascript:;");
		linkDettaglio.setTitle(oggetto);
		
		add(linkDettaglio, dettaglioSpan);
		
		
		
		
	}

	public void setMostraInformazioniDiProtocollazione(boolean mostraInformazioniDiProtocollazione) {
		this.mostraInformazioniDiProtocollazione = mostraInformazioniDiProtocollazione;
	}


}
