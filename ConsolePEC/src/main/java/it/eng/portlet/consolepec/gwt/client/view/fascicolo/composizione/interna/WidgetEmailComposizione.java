package it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.interna;

import java.util.Set;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.AllegatoComposizione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ElementoComposizione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.EmailComposizione;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.TabComposizione;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

/**
 * @author GiacomoFM
 * @since 24/gen/2019
 */
public class WidgetEmailComposizione extends WidgetComposizioneInterna {

	@UiField
	DownloadAllegatoWidget downloadWidget;

	@Override
	protected DownloadAllegatoWidget getDownloadAllegatoWidget() {
		return downloadWidget;
	}

	@UiField
	InlineLabel infoProtocollazione;
	@UiField
	InlineLabel infoData;
	@UiField
	CheckBox checkEmail;
	@UiField
	Anchor iconaEmail;
	@UiField
	Anchor infoEmail;

	@UiField
	HTMLPanel mittentePanel;
	@UiField
	InlineLabel mittente;
	@UiField
	HTMLPanel destinatariPanel;
	@UiField
	InlineLabel destinatari;

	@UiField
	Button allegatiMailBtn;
	@UiField
	HTMLPanel allegatiMail;

	@UiField
	HTMLPanel elencoGruppo;

	private static Binder binder = GWT.create(Binder.class);

	public interface Binder extends UiBinder<Widget, WidgetEmailComposizione> {/**/}

	public WidgetEmailComposizione(final EmailComposizione emailComposizione, final Set<ElementoComposizione> elementiSelezionati, final EventBus eventBus, final FascicoloDTO dto,
			final TabComposizione<?> tab) {
		super(eventBus, dto, tab, emailComposizione, elementiSelezionati);
		initWidget(binder.createAndBindUi(this));
		impostaCheckElemento(checkEmail);
		info(emailComposizione);

		allegatiMailBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!emailComposizione.getAllegatiEmail().isEmpty()) {
					allegatiMail.setVisible(!allegatiMail.isVisible());
				}
			}
		});

		impostaAllegatiMail(emailComposizione);
		creaElenco(emailComposizione, elementiSelezionati);
	}

	private void info(EmailComposizione emailComposizione) {
		infoProtocollazione(emailComposizione);
		infoAllegato(emailComposizione);
		infoData.setText(dateTimeFormat.format(emailComposizione.getDataCaricamento()));
		infoMittenteDestinatari(emailComposizione);
		// StringBuilder sb = new StringBuilder("Mittente: ");
		// sb.append(emailComposizione.getMittente());
		// if (!emailComposizione.getDestinatari().isEmpty())
		// sb.append(" - Destinatari: ").append(listaDestinatari(emailComposizione.getDestinatari()));
		// infoAggiuntive.setText(sb.toString());
	}

	private void infoMittenteDestinatari(EmailComposizione emailComposizione) {
		mittentePanel.setVisible(true);
		mittente.setText(emailComposizione.getMittente() != null ? emailComposizione.getMittente() : "mittente non definito");
		if (emailComposizione.getDestinatari().isEmpty()) {
			destinatariPanel.setVisible(false);
		} else {
			destinatariPanel.setVisible(true);
			destinatari.setText(listaDestinatari(emailComposizione.getDestinatari()));
		}
	}

	private void infoProtocollazione(EmailComposizione allegatoConversazione) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		if (allegatoConversazione.isProtocollato()) {
			sb.appendHtmlConstant("<b>");
			sb.appendEscaped(allegatoConversazione.getNumeroPg()).appendEscaped("/").appendEscaped(allegatoConversazione.getAnnoPg());
			sb.appendHtmlConstant("</b>");
		} else {
			sb.appendHtmlConstant("<b>");
			sb.appendEscaped("NON PROTOCOLLATO");
			sb.appendHtmlConstant("</b>");
		}
		infoProtocollazione.getElement().setInnerSafeHtml(sb.toSafeHtml());
	}

	private void infoAllegato(final EmailComposizione allegatoConversazione) {
		Image iconaFirma = new Image(caricaIcona(allegatoConversazione));
		iconaFirma.setStyleName("ico-mail");
		iconaFirma.setHeight("16px");
		iconaFirma.setWidth("16px");

		iconaEmail.setStyleName("ico verifica");
		iconaEmail.setHref("javascript:;");
		iconaEmail.setTitle("Visualizza dettagli");
		iconaEmail.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onClickExecution(allegatoConversazione);
			}
		});
		iconaEmail.getElement().appendChild(iconaFirma.getElement());
		impostaAnchor(infoEmail);
	}

	private void impostaAllegatiMail(EmailComposizione emailComposizione) {
		StringBuilder sb = null;
		for (AllegatoComposizione allegato : emailComposizione.getAllegatiEmail()) {
			final Object o = loadAllegato(allegato);
			HTMLPanel div = new HTMLPanel("");
			Image iconaImage = new Image(caricaIcona(allegato));
			iconaImage.setStyleName("ico-mail");
			iconaImage.setHeight("16px");
			iconaImage.setWidth("16px");
			Anchor icona = new Anchor(true);
			icona.setStyleName("ico verifica");
			// if (o != null && o instanceof AllegatoDTO) {
			// icona.setTitle("Visualizza dettagli");
			// icona.addClickHandler(new ClickHandler() {
			// @Override public void onClick(ClickEvent event) {
			// mostraDettaglioCommand.exe((AllegatoDTO) o); } }); } else {
			// icona.setTitle("Nessun dettaglio visualizzabile"); }
			icona.getElement().appendChild(iconaImage.getElement());
			div.add(icona);

			Anchor infoAllegato = new Anchor(true);
			sb = new StringBuilder(allegato.getNome() == null ? "Nessun nome" : allegato.getNome());
			if (allegato.getVersione() != null) {
				sb.append(" ").append(allegato.getVersione());
			}
			if (sb.length() > ConsolePecConstants.DESCR_WIDGET_ELEMENTI_MAX_LEN) {
				sb.replace(ConsolePecConstants.DESCR_WIDGET_ELEMENTI_MAX_LEN - 3, sb.length(), "...");
			}
			infoAllegato.setHTML(sb.toString());
			if (o != null) {
				infoAllegato.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (o instanceof AllegatoDTO) {
							scaricaElementoCommand.exe((AllegatoDTO) o);
						}
					}
				});
				infoAllegato.setTitle("Apri allegato");
			} else {
				infoAllegato.setTitle("Allegato non scaricabile");
			}
			div.add(infoAllegato);
			allegatiMail.add(div);
		}
		allegatiMail.setVisible(false);
		allegatiMailBtn.setText("Vedi allegati");
		allegatiMailBtn.setEnabled(!emailComposizione.getAllegatiEmail().isEmpty());
		allegatiMailBtn.setVisible(!emailComposizione.getAllegatiEmail().isEmpty());
	}

	@Override
	protected String getNome() {
		StringBuilder sb = new StringBuilder();
		sb.append(((EmailComposizione) elemento).getOggetto());

		if (sb.length() > ConsolePecConstants.DESCR_WIDGET_ELEMENTI_MAX_LEN) {
			sb.replace(ConsolePecConstants.DESCR_WIDGET_ELEMENTI_MAX_LEN - 3, sb.length(), "...");
		}

		if (elemento.getStato() != null && !elemento.getStato().isEmpty()) {
			if (((EmailComposizione) elemento).isPecOut()) {
				for (PecOutDTO.StatoDTO outStatoDTO : PecOutDTO.StatoDTO.values()) {
					if (outStatoDTO.name().equals(elemento.getStato())) {
						sb.append(" (").append(outStatoDTO.getLabel()).append(")");
					}
				}
			} else {
				for (PecInDTO.StatoDTO inStatoDTO : PecInDTO.StatoDTO.values()) {
					if (inStatoDTO.name().equals(elemento.getStato())) {
						sb.append(" (").append(inStatoDTO.getLabel()).append(")");
					}
				}
			}
		}
		return sb.length() > 0 ? sb.toString() : super.getNome();
	}

	private void creaElenco(EmailComposizione allegatoConversazione, Set<ElementoComposizione> allegatiSelezionati) {
		for (EmailComposizione a : allegatoConversazione.getConversazione()) {
			elencoGruppo.add(new WidgetEmailComposizione(a, allegatiSelezionati, eventBus, dto, tab));
		}
	}

	@Override
	protected Object loadAllegato(ElementoComposizione allegato) {
		if (allegato instanceof AllegatoComposizione) {
			return new AllegatoDTO(allegato.getNome(), ((AllegatoComposizione) allegato).getFolderOriginPath(), ((AllegatoComposizione) allegato).getFolderOriginName(), //
					allegato.getClientID(), allegato.getVersione());
		}
		if (allegato instanceof EmailComposizione)
			return allegato;
		return null;
	}

	@Override
	protected void onClickExecution(Object o) {
		EmailComposizione a = (EmailComposizione) o;
		if (a.isPecOut()) {
			mostraPecOut.exe(a.getClientID());
		} else {
			mostraPecIn.exe(a.getClientID());
		}
	}

	@Override
	protected void postSelezione(boolean selezionato) {
		checkEmail.setValue(selezionato);
	}

}
