package it.eng.portlet.consolepec.gwt.client.view.protocollazione;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmaiInlLoaded;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmailOutLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.SceltaCapofilaPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoAllegatoElencoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoPECElencoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoProtocollazioneBA01ElencoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.widget.YesNoRadioButton;
import it.eng.portlet.consolepec.gwt.client.widget.YesNoRadioButton.YesNoRadioButtonCommand;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.pec.TipoRiferimentoPEC;
import it.eng.portlet.consolepec.gwt.shared.dto.DatiPg;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.CapofilaFromBA01DTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPECRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

public class SceltaCapofilaView extends ViewImpl implements SceltaCapofilaPresenter.MyView {

	private final Widget widget;
	@UiField
	HTMLPanel panelloCapofila;
	@UiField
	HTMLPanel panelloCapofilaNew;
	@UiField
	Button avantiButton;
	@UiField
	Button annullaButton;
	@UiField
	Button indietroButton;
	@UiField
	Button buttonCerca;
	@UiField
	TextBox numeroPgCapofila;
	@UiField
	TextBox annoPgCapofila;
	@UiField
	HTMLPanel titoloPannelloCapofila;
	@UiField
	HTMLPanel sceltaProtocollazioneCapofilaPanel;

	@UiField(provided = true)
	YesNoRadioButton sceltaProtocollazioneCapofila;

	@UiField(provided = true)
	MessageAlertWidget messageWidget;

	private DatiPg datiPgCorrente = new DatiPg();
	private Command indietroCommand;
	private Command avantiCommand;
	private Command annullaCommand;
	private Command cercaCommand;
	private final PecInPraticheDB praticheDB;
	private final SitemapMenu sitemapMenu;
	private final EventBus eventBus;

	public interface Binder extends UiBinder<Widget, SceltaCapofilaView> {
	}

	@Inject
	public SceltaCapofilaView(final Binder binder, final PecInPraticheDB pecInDb, final SitemapMenu sitemapMenu, final EventBus eventBus) {

		messageWidget = new MessageAlertWidget(eventBus);
		sceltaProtocollazioneCapofila = new YesNoRadioButton("Vuoi protocollare come capofila? ");
		sceltaProtocollazioneCapofila.setYesNoRadioButtonCommand(new YesNoRadioButtonCommand() {

			@Override
			public void execute(Boolean value) {
				sceltaProtocollazioneCapofilaPanel.setVisible(!value);
			}
		});
		widget = binder.createAndBindUi(this);

		praticheDB = pecInDb;
		this.sitemapMenu = sitemapMenu;
		this.eventBus = eventBus;

		avantiButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				avantiCommand.execute();
			}
		});

		annullaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				annullaCommand.execute();
			}
		});

		buttonCerca.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cercaCommand.execute();
			}
		});

		indietroButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				indietroCommand.execute();
			}
		});
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void mostraSceltaCapofilaView(TreeMap<DatiPg, Set<ElementoPECRiferimento>> mapPec, TreeMap<DatiPg, Set<AllegatoDTO>> allegati, TreeMap<DatiPg, Set<CapofilaFromBA01DTO>> capofilaBa01) {
		// panelloCapofila.clear();
		panelloCapofila.getElement().setInnerHTML("");
		panelloCapofilaNew.getElement().setInnerHTML("");

		if (mapPec.size() != 0)
			titoloPannelloCapofila.getElement().setInnerHTML("<h6>Scegli il capofila per la protocollazione</h6>");
		else
			titoloPannelloCapofila.getElement().setInnerHTML("");

		// buttonAvanti.setEnabled(false);
		for (Entry<DatiPg, Set<ElementoPECRiferimento>> e : mapPec.entrySet()) {
			final DatiPg datiPg = e.getKey();
			Set<ElementoPECRiferimento> setPec = e.getValue();
			RadioButton rbPg = new RadioButton("pgRadioGroup", " " + datiPg);
			rbPg.setValue(false);
			rbPg.addClickHandler(new ClickHandler() {

				DatiPg _datiPg = datiPg;

				@Override
				public void onClick(ClickEvent event) {
					datiPgCorrente = _datiPg;
				}
			});
			rbPg.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

				DatiPg _datiPg = datiPg;

				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					if (event.getValue())
						datiPgCorrente = _datiPg;
				}
			});

			UListElement ul = Document.get().createULElement();
			ul.addClassName("contenitore-lista-gruppi");
			LIElement li = Document.get().createLIElement();
			li.addClassName("gruppo last clearfix");
			ul.appendChild(li);
			SpanElement span = Document.get().createSpanElement();
			span.addClassName("label protocollo");
			// span.setInnerText( pg );
			li.appendChild(span);
			DivElement div = Document.get().createDivElement();
			div.setClassName("corpo");
			li.appendChild(div);

			HTMLPanel panel = new HTMLPanel("");
			panel.setStyleName("box-mail");

			if (capofilaBa01 != null) {
				Set<CapofilaFromBA01DTO> elencoCapofila = capofilaBa01.get(datiPg);

				if (elencoCapofila != null)
					for (CapofilaFromBA01DTO capofilaDTO : elencoCapofila) {
						ElementoProtocollazioneBA01ElencoWidget elementoProtocollazioneBA01ElencoWidget = new ElementoProtocollazioneBA01ElencoWidget();
						elementoProtocollazioneBA01ElencoWidget.mostraCapofila(capofilaDTO);

						if (capofilaDTO.isNew()) {
							HTMLPanel intPanel = new HTMLPanel("");
							intPanel.setStyleName("box-mail");
							intPanel.add(elementoProtocollazioneBA01ElencoWidget);
							panelloCapofilaNew.getElement().appendChild(ul);
							panelloCapofilaNew.add(rbPg, span);
							panelloCapofilaNew.add(intPanel, div);
							if (mapPec.size() > 1){
								InlineHTML html = new InlineHTML();
								html.getElement().appendChild(Document.get().createHRElement());
								panelloCapofilaNew.add(html);
							}
								
							rbPg.setValue(capofilaDTO.isNew(), true);
						} else {
							panel.add(elementoProtocollazioneBA01ElencoWidget);
							panelloCapofila.getElement().appendChild(ul);
							panelloCapofila.add(rbPg, span);
							panelloCapofila.add(panel, div);
						}
					}
			}

			for (ElementoPECRiferimento pec : setPec) {
				final ElementoPECElencoWidget rigaPecIn = new ElementoPECElencoWidget();
				rigaPecIn.setCheckBoxVisible(false);
				rigaPecIn.setCheckBoxEnabled(false);
				panel.add(rigaPecIn);

				panelloCapofila.getElement().appendChild(ul);
				panelloCapofila.add(rbPg, span);
				panelloCapofila.add(panel, div);

				if (pec.getTipo() == TipoRiferimentoPEC.IN || pec.getTipo() == TipoRiferimentoPEC.EPROTO) {

					praticheDB.getPecInByPath(pec.getRiferimento(), sitemapMenu.containsLink(pec.getRiferimento()), new PraticaEmaiInlLoaded() {

						@Override
						public void onPraticaLoaded(PecInDTO pec) {
							rigaPecIn.mostraDettaglio(pec);
						}

						@Override
						public void onPraticaError(String error) {
							ShowMessageEvent event = new ShowMessageEvent();
							event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
							eventBus.fireEvent(event);
						}
					});
				} else {
					praticheDB.getPecOutByPath(pec.getRiferimento(), sitemapMenu.containsLink(pec.getRiferimento()), new PraticaEmailOutLoaded() {
						@Override
						public void onPraticaLoaded(PecOutDTO pec) {
							rigaPecIn.mostraDettaglio(pec);
						}

						@Override
						public void onPraticaError(String error) {
							ShowMessageEvent event = new ShowMessageEvent();
							event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
							eventBus.fireEvent(event);
						}
					});
				}

			}
			Set<AllegatoDTO> allegatiSet = allegati.get(datiPg);
			if (allegatiSet != null) {
				for (AllegatoDTO a : allegatiSet) {
					ElementoAllegatoElencoWidget dettaglioWiget = new ElementoAllegatoElencoWidget();
					dettaglioWiget.setCheckBoxVisible(false);
					panel.add(dettaglioWiget);
					dettaglioWiget.mostraDettaglio(a);
				}
				panelloCapofila.getElement().appendChild(ul);
				panelloCapofila.add(rbPg, span);
				panelloCapofila.add(panel, div);
			}
		}
	}

	@Override
	public DatiPg getDatiPg() {
		return this.datiPgCorrente;
	}

	@Override
	public void setAvantiCommand(Command avantiCommand) {
		this.avantiCommand = avantiCommand;
	}

	@Override
	public void setAnnullaCommand(Command annullaCommand) {
		this.annullaCommand = annullaCommand;
	}

	@Override
	public void setCercaCommand(Command cercaCommand) {
		this.cercaCommand = cercaCommand;
	}

	@Override
	public void setIndietroCommand(Command command) {
		this.indietroCommand = command;

	}

	@Override
	public void reset() {
		datiPgCorrente = new DatiPg();
	}

	@Override
	public void clear() {
		annoPgCapofila.setValue(null);
		numeroPgCapofila.setValue(null);
	}

	@Override
	public String getAnnoPgCapofila() {
		return annoPgCapofila.getValue();
	}

	@Override
	public String getNumeroPgCapofila() {
		return numeroPgCapofila.getValue();
	}

	@Override
	public void resetRicercaCapofila() {
		sceltaProtocollazioneCapofila.setValue(true);
	}

}
