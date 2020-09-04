package it.eng.portlet.consolepec.gwt.client.widget.composizione;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Image;

import it.eng.cobo.consolepec.commons.urbanistica.AllegatoProcedi;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.command.AbstractConsolePecCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecUtils;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import lombok.Setter;

/**
 * Widget che disegna un riferimento PEC in un elenco di un dettaglio
 * 
 * @author pluttero
 * 
 */
public class AllegatoComposizioneWidget extends ComplexPanel {

	// private static ElementoPECElencoWidgetUiBinder uiBinder = GWT.create(ElementoPECElencoWidgetUiBinder.class);
	//
	// interface ElementoPECElencoWidgetUiBinder extends UiBinder<Widget, ElementoPECElencoWidget> {
	// }

	private static final DateTimeFormat df = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATAORA);

	CheckBox checkBox;

	DivElement divContainer;

	DivElement documento;
	DivElement azioni;

	private Command<Void, SelezioneAllegato> selezioneCommand;
	private Command<Void, AllegatoDTO> mostraCommand;
	private Command<Void, AllegatoDTO> downloadCommand;
	private AbstractConsolePecCommand<?> downloadAllegatoProcediCommand;
	private boolean capofila = false;

	private String numPGAnnoPG = null;
	private String numPGAnnoPGCapofila = null;

	private boolean mostraInformazioniDiProtocollazione = false;
	@Setter private boolean mostraTipologieImpostate = true;

	public AllegatoComposizioneWidget() {
		super();
		divContainer = Document.get().createDivElement();
		setElement(divContainer);
		divContainer.setClassName("documenti-mail");

		documento = Document.get().createDivElement();
		documento.setClassName("documento-container");
		divContainer.appendChild(documento);

		azioni = Document.get().createDivElement();
		azioni.setClassName("documento-azioni");
		divContainer.appendChild(azioni);

		checkBox = new CheckBox();
	}

	public String composizioneTitle(String numeroAnnoPg, String tipologia) {
		String title = "";
		if (numeroAnnoPg != null) {
			title = numeroAnnoPg;
		}
		if (tipologia != null) {
			if (!title.equals("")) {
				title = title + " - ";
			}
			title = title + tipologia;
		}
		return title;
	}

	public void mostraDettaglio(final AllegatoProcedi allegato) {

		if (mostraInformazioniDiProtocollazione) {
			if (capofila) {
				SpanElement capofilaSpan = Document.get().createSpanElement();
				capofilaSpan.setInnerHTML(ConsolePecConstants.CAPOFILA_LABEL);
				capofilaSpan.setClassName("capofila");
				documento.appendChild(capofilaSpan);
			}

			SpanElement protocollo = Document.get().createSpanElement();

			if (numPGAnnoPG != null) {
				protocollo.setInnerText(numPGAnnoPG);
			} else {
				protocollo.setInnerText(ConsolePecConstants.NON_PROTOCOLLATO);
			}
			protocollo.setClassName("protocollo");
			documento.appendChild(protocollo);

			SpanElement span = Document.get().createSpanElement();
			span.setInnerText(" - ");
			documento.appendChild(span);
		}

		if (numPGAnnoPGCapofila != null) {
			SpanElement numPGAnnoPGCapofilaSpan = Document.get().createSpanElement();
			numPGAnnoPGCapofilaSpan.setInnerHTML(" - CAPOFILA " + numPGAnnoPGCapofila);
			numPGAnnoPGCapofilaSpan.setClassName("data");
			documento.appendChild(numPGAnnoPGCapofilaSpan);
		}

		SpanElement dettaglioSpan = Document.get().createSpanElement();
		dettaglioSpan.setClassName("documento");
		documento.appendChild(dettaglioSpan);

		// ICONA

		Image iconaFirma = new Image(ConsolePECIcons._instance.nonfirmato());
		iconaFirma.setStyleName("ico-mail");
		iconaFirma.setHeight("16px");
		iconaFirma.setWidth("16px");

		Anchor iconaAnchor = new Anchor();
		iconaAnchor.getElement().appendChild(iconaFirma.getElement());
		add(iconaAnchor, dettaglioSpan);

		// dettaglio
		Anchor linkDettaglio = new Anchor(ConsolePecUtils.getLabelComposizioneFascicolo(allegato));
		linkDettaglio.setHref("javascript:;");
		linkDettaglio.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				downloadAllegatoProcediCommand.execute();
			}
		});

		String title = composizioneTitle(allegato.getNumeroAnnoPG(), allegato.getTipologia());

		linkDettaglio.setTitle(title);
		add(linkDettaglio, dettaglioSpan);
	}

	public void mostraDettaglio(final AllegatoDTO allegato) {

		/* Checkbox di selezione */
		checkBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				boolean checked = ((CheckBox) event.getSource()).getValue();
				SelezioneAllegato sa = new SelezioneAllegato();
				sa.checked = checked;
				sa.setAllegato(allegato);
				selezioneCommand.exe(sa);
			}
		});
		checkBox.setStyleName("checkbox-nonprot");
		add(checkBox, documento);

		if (mostraInformazioniDiProtocollazione) {
			if (capofila) {
				SpanElement capofilaSpan = Document.get().createSpanElement();
				capofilaSpan.setInnerHTML(ConsolePecConstants.CAPOFILA_LABEL);
				capofilaSpan.setClassName("capofila");
				documento.appendChild(capofilaSpan);
			}

			SpanElement protocollo = Document.get().createSpanElement();

			if (numPGAnnoPG != null) {
				// ElementoGruppoProtocollato gp = ((ElementoGruppoProtocollato) currentGruppo);
				// protocollo.setInnerText(gp.getNumeroPG() + "/" + gp.getAnnoPG());
				protocollo.setInnerText(numPGAnnoPG);
			} else {
				protocollo.setInnerText(ConsolePecConstants.NON_PROTOCOLLATO);
			}
			protocollo.setClassName("protocollo");
			documento.appendChild(protocollo);

			SpanElement span = Document.get().createSpanElement();
			span.setInnerText(" - ");
			documento.appendChild(span);
		}

		SpanElement data = Document.get().createSpanElement();

		if (allegato.getDataCaricamento() != null) {
			data.setInnerHTML(df.format(allegato.getDataCaricamento()));
		} else {
			data.setInnerHTML("Data caricamento non presente");
		}

		data.setClassName("data");
		documento.appendChild(data);

		if (numPGAnnoPGCapofila != null) {
			SpanElement numPGAnnoPGCapofilaSpan = Document.get().createSpanElement();
			numPGAnnoPGCapofilaSpan.setInnerHTML(" - CAPOFILA " + numPGAnnoPGCapofila);
			numPGAnnoPGCapofilaSpan.setClassName("data");
			documento.appendChild(numPGAnnoPGCapofilaSpan);
		}

		SpanElement dettaglioSpan = Document.get().createSpanElement();
		dettaglioSpan.setClassName("documento");
		documento.appendChild(dettaglioSpan);

		// ICONA

		Image iconaFirma = new Image(allegato.getIconaStato(ConsolePECIcons._instance));
		iconaFirma.setStyleName("ico-mail");
		iconaFirma.setHeight("16px");
		iconaFirma.setWidth("16px");

		Anchor iconaAnchor = new Anchor();
		iconaAnchor.setStyleName("ico verifica");
		iconaAnchor.setTitle("Visualizza dettagli");
		iconaAnchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				mostraCommand.exe(allegato);
			}
		});
		iconaAnchor.getElement().appendChild(iconaFirma.getElement());
		add(iconaAnchor, dettaglioSpan);

		// dettaglio
		Anchor linkDettaglio = new Anchor(ConsolePecUtils.getLabelComposizioneFascicolo(allegato));
		linkDettaglio.setHref("javascript:;");
		linkDettaglio.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				downloadCommand.exe(allegato);
			}
		});
		linkDettaglio.setTitle(allegato.getLabel());

		add(linkDettaglio, dettaglioSpan);

		if (mostraTipologieImpostate) {
			DivElement inviato = Document.get().createDivElement();
			StringBuilder tipologia = new StringBuilder();
			if (allegato.getTipologiaAllegato() != null && !allegato.getTipologiaAllegato().isEmpty()) {
				tipologia.append(allegato.getTipologiaAllegato());
			} else {
				tipologia.append(" ");
			}

			inviato.setInnerText(tipologia.toString());
			inviato.getStyle().setFontSize(80, Unit.PCT);
			inviato.getStyle().setMarginLeft(27, Unit.PX);
			documento.appendChild(inviato);
		}
	}

	public void setSelezioneCommand(Command<Void, SelezioneAllegato> selezioneCommand) {
		this.selezioneCommand = selezioneCommand;
	}

	public void setMostraCommand(Command<Void, AllegatoDTO> mostraCommand) {
		this.mostraCommand = mostraCommand;
	}

	public void setDownloadCommand(Command<Void, AllegatoDTO> downloadCommand) {
		this.downloadCommand = downloadCommand;
	}

	public void setCapofila(boolean capofila) {
		this.capofila = capofila;
	}

	public void setNumPGAnnoPG(String numPG, String annoPG) {
		this.numPGAnnoPG = numPG + "/" + annoPG;
	}

	public void setNumPGAnnoPGCapofila(String numeroPG, String annoPG) {
		this.numPGAnnoPGCapofila = numeroPG + "/" + annoPG;
	}

	public void setMostraInformazioniDiProtocollazione(boolean mostraInformazioniDiProtocollazione) {
		this.mostraInformazioniDiProtocollazione = mostraInformazioniDiProtocollazione;
	}

	public void setCheckBoxVisible(boolean visible) {
		this.checkBox.setVisible(visible);
		this.checkBox.setEnabled(visible);
	}

	public class SelezioneAllegato {
		boolean checked;
		AllegatoDTO allegato;

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}

		public AllegatoDTO getAllegato() {
			return allegato;
		}

		public void setAllegato(AllegatoDTO allegato) {
			this.allegato = allegato;
		}

	}

	public void setDownloadAllegatoProcediCommand(AbstractConsolePecCommand<?> downloadAllegatoProcediCommand) {
		this.downloadAllegatoProcediCommand = downloadAllegatoProcediCommand;
	}
}
