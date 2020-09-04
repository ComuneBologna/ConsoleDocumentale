package it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.interna;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.web.bindery.event.shared.EventBus;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ElementoComposizione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.EmailComposizione;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.MostraDettaglioAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.TabComposizione;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.interna.SelezioneElementoEvent.SelezioneElementoEventHandler;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants.StatiElementiComposizioneFascicolo;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecUtils;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

/**
 * @author GiacomoFM
 * @since 22/gen/2019
 */
public abstract class WidgetComposizioneInterna extends Composite implements SelezioneElementoEventHandler {

	protected static DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATAORA);

	protected EventBus eventBus;
	protected FascicoloDTO dto;
	protected TabComposizione<?> tab;
	protected ElementoComposizione elemento;
	protected Set<ElementoComposizione> elementiSelezionati;

	protected Command<Void, AllegatoDTO> mostraDettaglioCommand;
	protected Command<Void, AllegatoDTO> scaricaElementoCommand;
	protected Command<Void, String> mostraPecIn;
	protected Command<Void, String> mostraPecOut;
	protected Command<Void, String> mostraPraticaModulistica;

	protected WidgetComposizioneInterna(final EventBus eventBus, final FascicoloDTO dto, final TabComposizione<?> tab, final ElementoComposizione elemento,
			final Set<ElementoComposizione> elementiSelezionati) {
		super();
		this.eventBus = eventBus;
		this.dto = dto;
		this.tab = tab;
		this.elemento = elemento;
		this.elementiSelezionati = elementiSelezionati;

		mostraDettaglioCommand = new Command<Void, AllegatoDTO>() {
			@Override
			public Void exe(AllegatoDTO t) {
				eventBus.fireEvent(new MostraDettaglioAllegatoEvent(t.getClientID(), dto.getClientID(), t));
				return null;
			}
		};
		scaricaElementoCommand = new Command<Void, AllegatoDTO>() {
			@Override
			public Void exe(AllegatoDTO t) {
				SafeUri uri = UriMapping.generaDownloadAllegatoServletURL(t.getClientID(), t);
				getDownloadAllegatoWidget().sendDownload(uri);
				return null;
			}
		};
		mostraPecIn = new Command<Void, String>() {
			@Override
			public Void exe(String pecID) {
				Place place = new Place();
				place.setToken(NameTokens.dettagliopecin);
				place.addParam(NameTokensParams.idPratica, pecID);
				eventBus.fireEvent(new GoToPlaceEvent(place));
				return null;
			}
		};
		mostraPecOut = new Command<Void, String>() {
			@Override
			public Void exe(String pecID) {
				Place place = new Place();
				place.setToken(NameTokens.dettagliopecout);
				place.addParam(NameTokensParams.idPratica, pecID);
				eventBus.fireEvent(new GoToPlaceEvent(place));
				return null;
			}
		};
		mostraPraticaModulistica = new Command<Void, String>() {
			@Override
			public Void exe(String pm) {
				Place place = new Place();
				place.setToken(NameTokens.dettagliopraticamodulistica);
				place.addParam(NameTokensParams.idPratica, pm);
				eventBus.fireEvent(new GoToPlaceEvent(place));
				return null;
			}
		};

		eventBus.addHandler(SelezioneElementoEvent.TYPE, this);
		// addHandler(this, SelezioneElementoEvent.TYPE);
	}

	protected void impostaCheckElemento(final CheckBox checkBox) {
		if (ConsolePecConstants.StatiElementiComposizioneFascicolo.STATO_ESTERNO.equals(elemento.getStato())
				|| ConsolePecConstants.StatiElementiComposizioneFascicolo.STATO_BOZZA.equals(elemento.getStato())) {
			checkBox.setValue(false);
			checkBox.setEnabled(false);
		} else {
			checkBox.setValue(elementiSelezionati.contains(elemento));
			checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					eventBus.fireEvent(new SelezioneElementoEvent(event.getValue(), elemento));
				}
			});
		}
	}

	protected Anchor impostaAnchor(Anchor a) {
		if (a == null)
			a = new Anchor();
		String text = getNome();
		a.setHref("javascript:;");
		final Object o = loadAllegato(elemento);
		if (o != null) {
			a.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					onClickExecution(o);
				}
			});
			a.setTitle("Apri allegato");
		} else {
			a.setTitle("Allegato non scaricabile");
		}
		a.setHTML(text);
		return a;
	}

	protected String getNome() {
		Object o = loadAllegato(elemento);
		if (o != null && o instanceof AllegatoDTO) {
			String name = ConsolePecUtils.getLabelComposizioneFascicolo((AllegatoDTO) o);
			if (name.length() > ConsolePecConstants.DESCR_WIDGET_ELEMENTI_MAX_LEN) {
				name = name.substring(0, ConsolePecConstants.DESCR_WIDGET_ELEMENTI_MAX_LEN - 3).concat("...");
			}
			return name;
		}

		StringBuilder sb = new StringBuilder();
		if (elemento.getNome() != null && !elemento.getNome().trim().isEmpty()) {
			sb.append(elemento.getNome());
			if (elemento.getVersione() != null && !elemento.getVersione().isEmpty()) {
				sb.append(" v. ").append(elemento.getVersione());
			}
		} else {
			sb.append("Nome allegato non impostato");
		}

		if (sb.length() > ConsolePecConstants.DESCR_WIDGET_ELEMENTI_MAX_LEN) {
			sb.replace(ConsolePecConstants.DESCR_WIDGET_ELEMENTI_MAX_LEN - 3, sb.length(), "...");
		}
		return sb.toString();
	}

	protected static String listaDestinatari(List<String> destinatari) {
		StringBuilder sb = new StringBuilder();
		sb.append(Arrays.toString(destinatari.toArray()).replace("[", "").replace("]", ""));
		if (sb.length() > ConsolePecConstants.DESCR_WIDGET_ELEMENTI_MAX_LEN) {
			sb.replace(ConsolePecConstants.DESCR_WIDGET_ELEMENTI_MAX_LEN - 3, sb.length(), "...");
		}
		return sb.toString();
	}

	protected static ImageResource caricaIcona(ElementoComposizione elemento) {
		if (elemento instanceof EmailComposizione) {
			if (((EmailComposizione) elemento).isPecOut()) {
				if (((EmailComposizione) elemento).isReply()) {
					return ConsolePECIcons._instance.mailDiRisposta();
				}
				return ConsolePECIcons._instance.mailInUscita();
			}
			return ConsolePECIcons._instance.mailAperta();
		}

		if (elemento.getStato() == null)
			return ConsolePECIcons._instance.help();

		switch (elemento.getStato()) {
		case "FIRMATO":
			return ConsolePECIcons._instance.firmato();
		case "NONFIRMATO":
			return ConsolePECIcons._instance.nonfirmato();
		case "FIRMANONVALIDA":
			return ConsolePECIcons._instance.firmanonvalida();
		case StatiElementiComposizioneFascicolo.STATO_EMAIL_IN:
			return ConsolePECIcons._instance.mailAperta();
		case StatiElementiComposizioneFascicolo.STATO_BOZZA:
		case StatiElementiComposizioneFascicolo.STATO_EMAIL_OUT:
			return ConsolePECIcons._instance.mailInUscita();
		case StatiElementiComposizioneFascicolo.STATO_PRATICA_MODULISTICA:
			return ConsolePECIcons._instance.firmato();
		case StatiElementiComposizioneFascicolo.STATO_ESTERNO:
			return ConsolePECIcons._instance.allegatoEsterno();
		default:
			return ConsolePECIcons._instance.allegato();
		}
	}

	protected abstract DownloadAllegatoWidget getDownloadAllegatoWidget();

	protected abstract Object loadAllegato(ElementoComposizione elemento);

	protected abstract void onClickExecution(Object o);

	protected abstract void postSelezione(boolean selezionato);

	@Override
	public void onSelezionaElemento(ElementoComposizione elementoSelezionato) {
		if (elemento.equals(elementoSelezionato)) {
			elementiSelezionati.add(elementoSelezionato);
			postSelezione(true);
			eventBus.fireEvent(new AggiornaPostSelezioneEvent());
		}
	}

	@Override
	public void onDeselezionaElemento(ElementoComposizione elementoDeselezionato) {
		if (elemento.equals(elementoDeselezionato)) {
			elementiSelezionati.remove(elementoDeselezionato);
			postSelezione(false);
			eventBus.fireEvent(new AggiornaPostSelezioneEvent());
		}
	}
}
