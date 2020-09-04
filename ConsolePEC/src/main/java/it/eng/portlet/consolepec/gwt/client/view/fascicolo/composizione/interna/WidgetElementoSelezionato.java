package it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.interna;

import java.util.Set;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ElementoComposizione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.EmailComposizione;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.TabComposizione;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants.StatiElementiComposizioneFascicolo;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

/**
 * @author GiacomoFM
 * @since 01/feb/2019
 */
public class WidgetElementoSelezionato extends WidgetComposizioneInterna {

	@UiField
	DownloadAllegatoWidget downloadWidget;

	@Override
	protected DownloadAllegatoWidget getDownloadAllegatoWidget() {
		return downloadWidget;
	}

	@UiField
	CheckBox checkDeseleziona;
	@UiField
	Anchor iconaElemento;
	@UiField
	Anchor infoElemento;

	private static Binder binder = GWT.create(Binder.class);

	public interface Binder extends UiBinder<Widget, WidgetElementoSelezionato> {/**/}

	public WidgetElementoSelezionato(final EventBus eventBus, final FascicoloDTO dto, final TabComposizione<?> tab, ElementoComposizione elemento, Set<ElementoComposizione> elementiSelezionati) {
		super(eventBus, dto, tab, elemento, elementiSelezionati);
		initWidget(binder.createAndBindUi(this));
		deseleziona(elemento);
		infoElemento(elemento);
	}

	private void deseleziona(final ElementoComposizione elemento) {
		// Image iconaElimina = new Image(ConsolePECIcons._instance.elimina());
		// iconaElimina.setStyleName("ico-mail");
		// iconaElimina.setHeight("16px");
		// iconaElimina.setWidth("16px");
		// iconaDeseleziona.setStyleName("ico");
		// iconaDeseleziona.setHref("javascript:;");
		// iconaDeseleziona.setTitle("Deseleziona allegato");
		// iconaDeseleziona.addClickHandler(new ClickHandler() {
		// @Override public void onClick(ClickEvent event) {
		// eventBus.fireEvent(new SelezioneElementoEvent(false, elemento)); } });
		// iconaDeseleziona.getElement().appendChild(iconaElimina.getElement());
		checkDeseleziona.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new SelezioneElementoEvent(false, elemento));
			}
		});
	}

	private void infoElemento(final ElementoComposizione elemento) {
		Image iconaFirma = new Image(caricaIcona(elemento));
		iconaFirma.setStyleName("ico-mail");
		iconaFirma.setHeight("16px");
		iconaFirma.setWidth("16px");

		iconaElemento.setStyleName("ico verifica");
		iconaElemento.setHref("javascript:;");
		final Object o = loadAllegato(elemento);
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

	@Override
	protected String getNome() {
		if (elemento instanceof EmailComposizione) {
			String oggetto = ((EmailComposizione) elemento).getOggetto();
			return oggetto == null || oggetto.trim().isEmpty() ? super.getNome() : oggetto;
		}
		return super.getNome();
	}

	@Override
	protected Object loadAllegato(ElementoComposizione elemento) {
		if (StatiElementiComposizioneFascicolo.STATO_PRATICA_MODULISTICA.equals(elemento.getStato()) //
				|| StatiElementiComposizioneFascicolo.STATO_EMAIL_IN.equals(elemento.getStato()) //
				|| StatiElementiComposizioneFascicolo.STATO_EMAIL_OUT.equals(elemento.getStato())) {
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
			if (e.getClientID() != null && !e.getClientID().isEmpty() && e.getStato() != null && !e.getStato().isEmpty()) {
				if (StatiElementiComposizioneFascicolo.STATO_PRATICA_MODULISTICA.equals(e.getStato())) {
					mostraPraticaModulistica.exe(e.getClientID());
				}
				if (StatiElementiComposizioneFascicolo.STATO_EMAIL_IN.equals(e.getStato())) {
					mostraPecIn.exe(e.getClientID());
				}
				if (StatiElementiComposizioneFascicolo.STATO_EMAIL_OUT.equals(e.getStato())) {
					mostraPecOut.exe(e.getClientID());
				}
			}
		}
	}

	@Override
	protected void postSelezione(boolean selezionato) {
		// ~
	}

}
