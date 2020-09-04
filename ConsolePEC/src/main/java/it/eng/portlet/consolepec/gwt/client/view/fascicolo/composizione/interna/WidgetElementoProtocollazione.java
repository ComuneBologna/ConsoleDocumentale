package it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.interna;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ElementoProtocollato;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.EmailComposizione;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.TabComposizione;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants.StatiElementiComposizioneFascicolo;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

/**
 * @author GiacomoFM
 * @since 15/gen/2019
 */
public class WidgetElementoProtocollazione extends WidgetComposizioneInterna {

	@UiField
	DownloadAllegatoWidget downloadWidget;

	@Override
	protected DownloadAllegatoWidget getDownloadAllegatoWidget() {
		return downloadWidget;
	}

	@UiField
	HTMLPanel protocollazionePanel;
	@UiField
	InlineLabel infoData;
	@UiField
	CheckBox checkElemento;
	@UiField
	Anchor iconaElemento;
	@UiField
	Anchor infoElemento;
	@UiField
	InlineLabel infoAggiuntive;

	@UiField
	Button allegatiMailBtn;
	@UiField
	HTMLPanel allegatiMail;

	private static Binder binder = GWT.create(Binder.class);

	public interface Binder extends UiBinder<Widget, WidgetElementoProtocollazione> {/**/}

	public WidgetElementoProtocollazione(ElementoProtocollato elementoProtocollato, Set<ElementoComposizione> elementiSelezionati, final EventBus eventBus, final FascicoloDTO dto,
			final TabComposizione<?> tab) {
		super(eventBus, dto, tab, elementoProtocollato, elementiSelezionati);
		initWidget(binder.createAndBindUi(this));
		impostaCheckElemento(checkElemento);
		info(elementoProtocollato);

		allegatiMailBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				allegatiMail.setVisible(!allegatiMail.isVisible());
			}
		});
	}

	private void info(ElementoProtocollato elementoProtocollato) {
		infoElemento(elementoProtocollato);
		infoData.setText(dateTimeFormat.format(elementoProtocollato.getDataCaricamento()));

		StringBuilder sb = new StringBuilder();
		@SuppressWarnings("unlikely-arg-type") int index = dto.getComposizioneAllegati().indexOf(elementoProtocollato);
		if (index > -1) {
			sb.append(Arrays.toString(dto.getComposizioneAllegati().get(index).getTag().toArray()).replace("[", "").replace("]", ""));
		}

		EmailComposizione ec = getEmailComposizione(elementoProtocollato);
		if (ec != null) {
			if (StatiElementiComposizioneFascicolo.STATO_BOZZA.equals(ec.getStato())) {
				this.elemento.setStato(StatiElementiComposizioneFascicolo.STATO_BOZZA);
				impostaCheckElemento(checkElemento);
				sb.append(StatiElementiComposizioneFascicolo.STATO_BOZZA).append(" - ");
			}
			sb.append("Mittente: ");
			sb.append(ec.getMittente());
			if (!ec.getDestinatari().isEmpty()) {
				sb.append(" - Destinatari: ");
				sb.append(listaDestinatari(ec.getDestinatari()));
			}

			impostaAllegatiMail(ec);
		} else {
			allegatiMailBtn.setVisible(false);
		}
		if (StatiElementiComposizioneFascicolo.STATO_ESTERNO.equals(elementoProtocollato.getStato())) {
			sb.append("Questo capofila NON e' un allegato appartenente a questo fascicolo");
		}

		if (sb.length() > 0) {
			infoAggiuntive.setText(sb.toString());
		}
	}

	@Override
	protected String getNome() {
		StringBuilder sb = new StringBuilder(super.getNome());

		EmailComposizione ec = getEmailComposizione((ElementoProtocollato) elemento);
		if (ec != null && ec.getStato() != null && !ec.getStato().isEmpty()) {
			if (ec.isPecOut()) {
				for (PecOutDTO.StatoDTO outStatoDTO : PecOutDTO.StatoDTO.values()) {
					if (outStatoDTO.name().equals(ec.getStato())) {
						sb.append(" (").append(outStatoDTO.getLabel()).append(")");
					}
				}
			} else {
				for (PecInDTO.StatoDTO inStatoDTO : PecInDTO.StatoDTO.values()) {
					if (inStatoDTO.name().equals(ec.getStato())) {
						sb.append(" (").append(inStatoDTO.getLabel()).append(")");
					}
				}
			}
		}

		return sb.toString();
	}

	private EmailComposizione getEmailComposizione(ElementoProtocollato elementoProtocollato) {
		@SuppressWarnings("unlikely-arg-type") int index = dto.getComposizioneEmail().indexOf(elementoProtocollato);
		if (index > -1) {
			return dto.getComposizioneEmail().get(index);
		}
		EmailComposizione emailComposizione = null;
		for (EmailComposizione ec : dto.getComposizioneEmail()) {
			emailComposizione = cercaEmailAnnidata(ec, elementoProtocollato, ec.getConversazione());
			if (emailComposizione != null) {
				return emailComposizione;
			}
		}
		return null;
	}

	private EmailComposizione cercaEmailAnnidata(EmailComposizione emailComposizione, ElementoProtocollato elementoProtocollato, List<EmailComposizione> conversazione) {
		if (elementoProtocollato.equals(emailComposizione)) {
			return emailComposizione;
		}
		EmailComposizione email = null;
		for (EmailComposizione ec : conversazione) {
			email = cercaEmailAnnidata(ec, elementoProtocollato, ec.getConversazione());
			if (email != null) {
				return email;
			}
		}
		return null;
	}

	private void infoElemento(final ElementoProtocollato elementoProtocollato) {
		Image iconaFirma = new Image(caricaIcona(elementoProtocollato));
		iconaFirma.setStyleName("ico-mail");
		iconaFirma.setHeight("16px");
		iconaFirma.setWidth("16px");

		iconaElemento.setStyleName("ico verifica");
		iconaElemento.setHref("javascript:;");
		final Object o = loadAllegato(elementoProtocollato);
		if (o != null && o instanceof AllegatoDTO) {
			iconaElemento.setTitle("Visualizza dettagli");
			iconaElemento.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					mostraDettaglioCommand.exe((AllegatoDTO) o);
				}
			});
		} else {
			iconaElemento.setTitle("Nessun dettaglio visualizzabile");
		}
		iconaElemento.getElement().appendChild(iconaFirma.getElement());
		impostaAnchor(infoElemento);
	}

	private void impostaAllegatiMail(EmailComposizione emailComposizione) {
		boolean abilitaPulsante = false;
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
			abilitaPulsante = true;
		}
		allegatiMail.setVisible(false);
		allegatiMailBtn.setText("Vedi allegati");
		allegatiMailBtn.setVisible(abilitaPulsante);
	}

	@Override
	protected Object loadAllegato(ElementoComposizione allegato) {
		if (allegato instanceof AllegatoComposizione && (StatiElementiComposizioneFascicolo.STATO_EMAIL_IN.equals(elemento.getStato()) //
				|| StatiElementiComposizioneFascicolo.STATO_EMAIL_OUT.equals(elemento.getStato()))) {
			AllegatoComposizione a = (AllegatoComposizione) allegato;
			return new AllegatoDTO(a.getNome(), a.getFolderOriginPath(), a.getFolderOriginName(), a.getClientID(), a.getVersione());
		}
		if (StatiElementiComposizioneFascicolo.STATO_PRATICA_MODULISTICA.equals(elemento.getStato()) //
				|| StatiElementiComposizioneFascicolo.STATO_EMAIL_IN.equals(elemento.getStato()) //
				|| StatiElementiComposizioneFascicolo.STATO_EMAIL_OUT.equals(elemento.getStato()) //
				|| StatiElementiComposizioneFascicolo.STATO_BOZZA.equals(elemento.getStato())) {
			return elemento;
		}
		for (AllegatoDTO a : dto.getAllegati()) {
			if (a.getNome().equals(elemento.getNome()) && a.getVersioneCorrente().equals(elemento.getVersione())) {
				return a;
			}
		}
		return null;
	}

	@Override
	protected void onClickExecution(Object o) {
		if (o instanceof AllegatoDTO) {
			scaricaElementoCommand.exe((AllegatoDTO) o);
		}
		if (o instanceof ElementoComposizione) {
			ElementoComposizione e = (ElementoComposizione) o;
			if (e.getStato() != null && !e.getStato().isEmpty() && e.getClientID() != null && !e.getClientID().isEmpty()) {
				if (StatiElementiComposizioneFascicolo.STATO_PRATICA_MODULISTICA.equals(e.getStato())) {
					mostraPraticaModulistica.exe(e.getClientID());
				}
				if (StatiElementiComposizioneFascicolo.STATO_EMAIL_IN.equals(e.getStato())) {
					mostraPecIn.exe(e.getClientID());
				}
				if (StatiElementiComposizioneFascicolo.STATO_EMAIL_OUT.equals(e.getStato())) {
					mostraPecOut.exe(e.getClientID());
				}
				if (StatiElementiComposizioneFascicolo.STATO_BOZZA.equals(e.getStato())) {
					mostraPecOut.exe(e.getClientID());
				}
			}
		}
	}

	@Override
	protected void postSelezione(boolean selezionato) {
		checkElemento.setValue(selezionato);
	}

}
