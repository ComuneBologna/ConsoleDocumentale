package it.eng.portlet.consolepec.gwt.client.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Strings;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo.DatoAggiuntivoVisitor;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoAnagrafica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoTabella;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreMultiplo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreSingolo;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.DettaglioAllegatoPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.DettaglioAllegatoPresenter.ChiudiCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.DettaglioAllegatoPresenter.CreaUrlFileSbustato;
import it.eng.portlet.consolepec.gwt.client.presenter.DettaglioAllegatoPresenter.CreaUrlFileVersione;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecUtils;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.StoricoVersioniDTO;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.StoricoVersioniDTO.InformazioniCopiaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.StoricoVersioniDTO.InformazioniTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.DettagliAllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.InformazioniFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioGruppoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioUtenteDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.StatoDestinatarioTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.StatoTaskFirmaDTO;

public class DettaglioAllegatoView extends ViewImpl implements DettaglioAllegatoPresenter.MyView {

	private final Widget widget;

	@UiField(provided = true)
	MessageAlertWidget messageWidget;

	@UiField
	Button chiudiButton;
	@UiField
	HTMLPanel contenutoPanel;

	private ConfigurazioniHandler configurazioniHandler;
	private CreaUrlFileSbustato creaUrlFileSbustato;
	private CreaUrlFileVersione creaUrlFileVersione;
	private ChiudiCommand chiudiCommand;
	private final String SEPARATOR = " - ";

	public interface Binder extends UiBinder<Widget, DettaglioAllegatoView> {
		//
	}

	@Inject
	public DettaglioAllegatoView(final Binder binder, final EventBus eventBus, ConfigurazioniHandler configurazioniHandler) {
		messageWidget = new MessageAlertWidget(eventBus);
		this.configurazioniHandler = configurazioniHandler;
		widget = binder.createAndBindUi(this);
		chiudiButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				chiudiCommand.execute();
			}
		});
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setChiudiDettaglioCommand(final ChiudiCommand chiudiCommand) {
		this.chiudiCommand = chiudiCommand;
	}

	@Override
	public void resetView() {
		contenutoPanel.clear();
		while (contenutoPanel.getElement().hasChildNodes()) {
			contenutoPanel.getElement().removeChild(contenutoPanel.getElement().getLastChild());
		}
		Window.scrollTo(0, 0);
	}

	@Override
	public void disegnaDettagliAllegato(DettagliAllegatoDTO dto, Set<StoricoVersioniDTO> storicoVersioni) {
		Map<String, StoricoVersioniDTO> storicoVersioniMap = new HashMap<String, StoricoVersioniDTO>();
		if (storicoVersioni != null) {
			storicoVersioniMap = creaMapStoricoVersioni(storicoVersioni);
		}
		if (dto != null) {
			addLabel("Nome allegato");
			if (dto.getFolderOriginName() != null && !dto.getFolderOriginName().isEmpty()) {
				addValue(dto.getFolderOriginName());
			} else {
				addValue(dto.getNome());
			}
			addLabel("Percorso allegato");
			addValue(dto.getFolderOriginPath() == null ? "/" : dto.getFolderOriginPath());
		}
		while (dto != null) {

			/*
			 * Versione
			 */
			addLabel("Versione");
			HTMLPanel versionePanel = new HTMLPanel("");
			versionePanel.setStyleName("abstract-anchor abstract");
			DownloadAllegatoWidget downloadAllegatoWidgetV = new DownloadAllegatoWidget();
			Anchor aversione = new Anchor();
			String versioneCorrente = dto.getVersioneCorrente();
			aversione.addClickHandler(new DownloadVersioneFileHandler(downloadAllegatoWidgetV, versioneCorrente));
			aversione.getElement().setAttribute("style", "cursor:pointer");
			aversione.setText(versioneCorrente);
			versionePanel.add(aversione);
			versionePanel.add(downloadAllegatoWidgetV);
			contenutoPanel.add(versionePanel);

			/*
			 * Hash
			 */
			addLabel("Hash");
			addValue(sanitizeNull(dto.getHash()));

			/*
			 * Data
			 */
			if (dto.getPreviousVersion() != null) {
				addLabel("Data di ultima modifica");
			} else {
				addLabel("Data di creazione");
			}
			addValue(sanitizeNull(dto.getDataCreazioneLabel()));
			addLabel("Utente");
			String utente = null;
			if (storicoVersioniMap.get(dto.getVersioneCorrente()) != null) {
				utente = storicoVersioniMap.get(dto.getVersioneCorrente()).getUtente();
			}
			addValue(sanitizeNull(utente));
			addLabel("Id Alfresco");
			addValue(sanitizeNull(dto.getUUID()));
			addLabel("Tipologie");
			StringBuilder sb = new StringBuilder();
			for (String tipologiaAllegato : dto.getTipologiaAllegato()) {
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(tipologiaAllegato);
			}
			addValue(sanitizeNull(sb.toString()));
			addLabel("Stato");
			Image ico = new Image(dto.getIconaStato(ConsolePECIcons._instance));
			HTMLPanel icoPanel = new HTMLPanel("");
			icoPanel.add(ico);
			icoPanel.setStyleName("abstract");
			SpanElement icoText = Document.get().createSpanElement();
			icoText.setInnerHTML(dto.getStato().getLabel());
			icoText.setClassName("dettaglioallegato-statolabel");
			icoPanel.getElement().appendChild(icoText);
			contenutoPanel.add(icoPanel);

			sezioneDatiAggiuntivi(dto.getDatiAggiuntivi());

			/*
			 * Informazioni firma digitale remota
			 */
			int count = 1;
			for (InformazioniFirmaDTO ifirma : dto.getInformazioniFirma()) {
				if (count == 1) {
					addHeading("Firma digitale");
				}

				addLabel("Allegato non firmato");
				HTMLPanel fileSbustatoPanel = new HTMLPanel("");
				fileSbustatoPanel.setStyleName("abstract-anchor abstract");
				DownloadAllegatoWidget downloadAllegatoWidget = new DownloadAllegatoWidget();

				Anchor a = new Anchor();
				a.addClickHandler(new DownloadFileSbustatoHandler(dto.getUUID(), downloadAllegatoWidget));
				a.getElement().setAttribute("style", "cursor:pointer");

				String nomeAllegato = dto.getFolderOriginName() != null && !dto.getFolderOriginName().isEmpty() ? dto.getFolderOriginName() : dto.getNome();
				if (!nomeAllegato.toLowerCase().contains(".p7m")) {
					a.setText(nomeAllegato);
				} else {
					a.setText(nomeAllegato.replaceAll(".p7m", "").replaceAll(".P7M", "").replaceAll(".p7M", "").replaceAll(".P7m", ""));
				}

				fileSbustatoPanel.add(a);
				fileSbustatoPanel.add(downloadAllegatoWidget);
				contenutoPanel.add(fileSbustatoPanel);

				addLabel("Firma numero ");
				addValue("" + count++);
				addLabel("Tipologia di firma");
				addValue(dto.getTipologiaFirma().getLabel());
				addLabel("CA");
				addValue(sanitizeNull(ifirma.getCa()));
				addLabel("Data di firma");
				addValue(sanitizeNull(ifirma.getDataFirma()));
				addLabel("Descrizione");
				addValue(sanitizeNull(ifirma.getDescr()));
				addLabel("DN");
				addValue(sanitizeNull(ifirma.getDn()));
				addLabel("Periodo di validità");
				addValue("Dal " + ifirma.getValidoDal() + " al " + ifirma.getValidoAl());
			}

			if (storicoVersioniMap.get(dto.getVersioneCorrente()) != null) {

				/*
				 * Informazioni copia
				 */
				InformazioniCopiaDTO infoCopia = storicoVersioniMap.get(dto.getVersioneCorrente()).getInformazioniCopia();
				if (infoCopia != null) {
					addHeading("Informazioni copia");

					addLabel("Versione importata dalla pratica");
					addValue(infoCopia.getIdDocumentaleSorgente());
				}

				/*
				 * Informazioni proposta firma
				 */
				InformazioniTaskFirmaDTO infoTaskFirma = storicoVersioniMap.get(dto.getVersioneCorrente()).getInformazioniTaskFirma();

				if (infoTaskFirma != null) {
					addHeading("Informazioni proposta di firma/visto");

					addLabel("Operazione effettuata");
					addValue(sanitizeNull(getDescrizioneOperazioneEffettuata(infoTaskFirma.getOperazioneEffettuata())));

					addLabel("Oggetto proposta");
					addValue(sanitizeNull(infoTaskFirma.getOggettoProposta()));

					addLabel("Gruppo proponente");
					addValue(sanitizeNull(configurazioniHandler.getAnagraficaRuolo(infoTaskFirma.getProponente().getNomeGruppo()).getEtichetta()));

					addLabel("Tipo proposta");
					addValue(sanitizeNull(infoTaskFirma.getTipoRichiesta().getLabel()));

					addLabel("Mittente originale");
					addValue(sanitizeNull(infoTaskFirma.getMittenteOriginale()));

					addLabel("Data scadenza");
					addValue(sanitizeNull(infoTaskFirma.getDataScadenza()));

					addLabel("Motivazione");
					addValueHtml(ConsolePecUtils.formatText(sanitizeNull(infoTaskFirma.getMotivazione())));

					if (!StatoTaskFirmaDTO.RITIRATO.equals(infoTaskFirma.getOperazioneEffettuata())) {
						addLabel("Destinatari");
						int i = 0;
						for (DestinatarioDTO destinatario : infoTaskFirma.getDestinatari()) {
							if (i != 0) {
								addLabel("");
							}
							addValue(getDescrizioneDestinatario(destinatario));
							i++;
						}
					}
				}
			}

			if (dto.getPreviousVersion() != null) {
				contenutoPanel.getElement().appendChild(Document.get().createHRElement());
			}

			dto = dto.getPreviousVersion();
		}
	}

	private void sezioneDatiAggiuntivi(List<DatoAggiuntivo> datiAggiuntivi) {
		if (datiAggiuntivi == null || datiAggiuntivi.isEmpty()) {
			return;
		}
		addHeading("Dati aggiuntivi");
		for (DatoAggiuntivo datoAggiuntivo : datiAggiuntivi) {
			datoAggiuntivo.accept(new DatoAggiuntivoVisitor() {

				@Override
				public void visit(DatoAggiuntivoTabella datoAggiuntivoTabella) {
					// ~
				}

				@Override
				public void visit(DatoAggiuntivoAnagrafica datoAggiuntivoAnagrafica) {
					// ~
				}

				@Override
				public void visit(DatoAggiuntivoValoreMultiplo datoAggiuntivoValoreMultiplo) {
					// ~
				}

				@Override
				public void visit(DatoAggiuntivoValoreSingolo datoAggiuntivoValoreSingolo) {
					if (datoAggiuntivoValoreSingolo != null && !Strings.isNullOrEmpty(datoAggiuntivoValoreSingolo.getDescrizione())
							&& !Strings.isNullOrEmpty(datoAggiuntivoValoreSingolo.getValore())) {
						addLabel(datoAggiuntivoValoreSingolo.getDescrizione());
						addValue(datoAggiuntivoValoreSingolo.getValore());
					}
				}
			});
		}
	}

	private String getDescrizioneDestinatario(DestinatarioDTO destinatario) {
		StringBuilder sb = new StringBuilder();

		if (destinatario instanceof DestinatarioUtenteDTO) {
			DestinatarioUtenteDTO dest = (DestinatarioUtenteDTO) destinatario;

			sb.append(dest.getNomeCompleto());

			if (!Strings.isNullOrEmpty(dest.getMatricola())) {
				sb.append(SEPARATOR).append(dest.getMatricola());
			}
			if (!Strings.isNullOrEmpty(dest.getSettore())) {
				sb.append(SEPARATOR).append(dest.getSettore());
			}
			if (dest.getStatoRichiesta() != null) {
				sb.append(SEPARATOR).append(getDescrizioneStatoDestinatario(dest.getStatoRichiesta()));
			}
		} else if (destinatario instanceof DestinatarioGruppoDTO) {
			DestinatarioGruppoDTO dest = (DestinatarioGruppoDTO) destinatario;
			sb.append(dest.getNomeGruppoDisplay());
			sb.append(SEPARATOR).append(getDescrizioneStatoDestinatario(dest.getStatoRichiesta()));
		}
		return sb.toString();
	}

	private String getDescrizioneStatoDestinatario(StatoDestinatarioTaskFirmaDTO statoRichiesta) {
		switch (statoRichiesta) {

		case APPROVATO:
			return "(PROPOSTA APPROVATA)";

		case DINIEGATO:
			return "(PROPOSTA DINIEGATA)";

		case IN_APPROVAZIONE:
		case RISPOSTA_NEGATIVA:
		case RISPOSTA_POSITIVA:
		case RISPOSTA_POSITIVA_CON_PRESCRIZIONI:
		case RISPOSTA_SOSPESA:
		case RISPOSTA_RIFIUTATA:
			return new StringBuilder("(").append(statoRichiesta.getLabel().toUpperCase()).append(")").toString();
		default:
			return SEPARATOR;
		}
	}

	private String getDescrizioneOperazioneEffettuata(StatoTaskFirmaDTO statoRichiesta) {
		switch (statoRichiesta) {

		case APPROVATO:
			return "Proposta approvata";

		case DINIEGATO:
			return "Proposta diniegata";

		case RITIRATO:
			return "Ritiro proposta";

		case IN_APPROVAZIONE:
			return "Invio proposta";

		case PARERE_RICEVUTO:
			return "Risposta parere";

		default:
			return SEPARATOR;
		}
	}

	private static Map<String, StoricoVersioniDTO> creaMapStoricoVersioni(Set<StoricoVersioniDTO> storicoVersioni) {
		Map<String, StoricoVersioniDTO> result = new HashMap<String, StoricoVersioniDTO>();
		for (StoricoVersioniDTO storico : storicoVersioni) {
			if (!result.containsKey(storico.getVersione())) {
				result.put(storico.getVersione(), storico);
			}
		}
		return result;
	}

	private String sanitizeNull(String in) {
		if (in == null || in.trim().length() == 0) {
			return SEPARATOR;
		}
		return in;
	}

	private void addLabel(String content) {
		SpanElement span = Document.get().createSpanElement();
		span.setClassName("label");
		span.setInnerHTML(content);
		contenutoPanel.getElement().appendChild(span);
	}

	private void addHeading(String content) {
		DivElement div = Document.get().createDivElement();
		HeadingElement heading = Document.get().createHElement(6);
		heading.setInnerHTML(content);
		div.appendChild(heading);
		contenutoPanel.getElement().appendChild(div);
	}

	private void addValueHtml(String content) {
		DivElement div = Document.get().createDivElement();
		div.setClassName("abstract");
		HTML html = new HTML(content);
		contenutoPanel.getElement().appendChild(div);
		contenutoPanel.add(html, div);
	}

	private void addValue(String content) {
		DivElement div = Document.get().createDivElement();
		div.setClassName("abstract");
		Label label = new Label(content);
		contenutoPanel.getElement().appendChild(div);
		contenutoPanel.add(label, div);
	}

	class DownloadVersioneFileHandler implements com.google.gwt.event.dom.client.ClickHandler {

		private final DownloadAllegatoWidget downloadAllegatoWidget;
		private String versione;

		public DownloadVersioneFileHandler(DownloadAllegatoWidget downloadAllegatoWidget, String versione) {
			this.downloadAllegatoWidget = downloadAllegatoWidget;
			this.versione = versione;
		}

		@Override
		public void onClick(ClickEvent arg0) {
			SafeUri uri = creaUrlFileVersione.exe(versione);
			downloadAllegatoWidget.sendDownload(uri);
		}
	}

	class DownloadFileSbustatoHandler implements com.google.gwt.event.dom.client.ClickHandler {
		private final String uuid;
		private final DownloadAllegatoWidget downloadAllegatoWidget;

		public DownloadFileSbustatoHandler(String uuid, DownloadAllegatoWidget downloadAllegatoWidget) {
			this.uuid = uuid;
			this.downloadAllegatoWidget = downloadAllegatoWidget;
		}

		@Override
		public void onClick(ClickEvent event) {
			SafeUri uri = creaUrlFileSbustato.exe(uuid);
			downloadAllegatoWidget.sendDownload(uri);

		}
	}

	@Override
	public void setCreaUrlFileSbustato(CreaUrlFileSbustato creaUrl) {
		this.creaUrlFileSbustato = creaUrl;
	}

	@Override
	public void setCreaUrlFileVersione(CreaUrlFileVersione creaUrl) {
		this.creaUrlFileVersione = creaUrl;
	}
}
