package it.eng.portlet.consolepec.gwt.client.view.fascicolo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo.DatoAggiuntivoVisitor;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoAnagrafica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoTabella;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreMultiplo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreSingolo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.RigaDatoAggiuntivo;
import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.cobo.consolepec.util.objects.Ref;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.ModificaFascicoloPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.datiaggiuntivi.FormDatiAggiuntiviWidget;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ValidazioneDatoAggiuntivoDTO;

/**
 * @author GiacomoFM
 * @since 17/lug/2017
 */
public class ModificaFascicoloView extends ViewImpl implements ModificaFascicoloPresenter.MyView {

	private static final String AUTO_COMPILED_TITLE = "...";

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, ModificaFascicoloView> {
		//
	}

	@UiField(provided = true) MessageAlertWidget messageAlertWidget;
	@UiField TextBox titoloTextBox;
	@UiField HTMLPanel titoloOriginalePanel;
	@UiField Label titoloOriginaleLabel;
	@UiField ListBox tipologiaFascicolo;
	@UiField Button confermaButton;
	@UiField Button annullaButton;
	@UiField HTMLPanel datiAggiuntiviPanel;

	private FormDatiAggiuntiviWidget formDatiAggiuntiviWidget;
	private FascicoloDTO fascicolo;
	private Map<String, AnagraficaFascicolo> tipologieAccessibili;

	@Inject
	public ModificaFascicoloView(final Binder binder, final EventBus eventBus) {
		messageAlertWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
		tipologieAccessibili = new LinkedHashMap<>();
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@UiHandler("tipologiaFascicolo")
	public void onTipologiaChange(ChangeEvent event) {
		if (isTipologiaModificata()) {
			final Map<String, DatoAggiuntivo> datiDelFascicolo = Maps.uniqueIndex(fascicolo.getValoriDatiAggiuntivi(), new CalcolaValoriPerNome());

			List<DatoAggiuntivo> valori = Lists.transform(Lists.newArrayList(tipologieAccessibili.get(getTipoFascicolo()).getDatiAggiuntivi()), new Function<DatoAggiuntivo, DatoAggiuntivo>() {
				@Override
				public DatoAggiuntivo apply(final DatoAggiuntivo datoDaConfiguratione) {

					if (datiDelFascicolo.containsKey(datoDaConfiguratione.getNome())) {
						final DatoAggiuntivo datoDelFascicolo = datiDelFascicolo.get(datoDaConfiguratione.getNome());

						DatoAggiuntivo nuovo = datoDaConfiguratione.clona();
						nuovo.accept(new DatoAggiuntivoVisitor() {

							@Override
							public void visit(DatoAggiuntivoTabella nuovo) {
								nuovo.getRighe().clear();
								nuovo.getRighe().addAll(((DatoAggiuntivoTabella) datoDelFascicolo).getRighe());
							}

							@Override
							public void visit(DatoAggiuntivoAnagrafica nuovo) {
								nuovo.setValore(((DatoAggiuntivoAnagrafica) datoDelFascicolo).getValore());
							}

							@Override
							public void visit(DatoAggiuntivoValoreMultiplo nuovo) {
								nuovo.getValori().addAll((((DatoAggiuntivoValoreMultiplo) datoDelFascicolo).getValori()));
							}

							@Override
							public void visit(DatoAggiuntivoValoreSingolo nuovo) {
								nuovo.setValore(((DatoAggiuntivoValoreSingolo) datoDelFascicolo).getValore());
							}
						});

						return nuovo;
					} else {
						return datoDaConfiguratione;
					}
				}
			});

			formDatiAggiuntiviWidget.setDatiAggiuntiviPerModifica(valori, false);

		} else {
			formDatiAggiuntiviWidget.setDatiAggiuntiviPerModifica(fascicolo.getValoriDatiAggiuntivi(), true);
		}
		controllaTitoloPredefinito();
		event.stopPropagation();
	}

	@Override
	public boolean isModificaEseguita() {
		return isTitoloModificato() || isTipologiaModificata() || isDatiAggiuntiviModificati();
	}

	private boolean isTipologiaModificata() {
		return !Strings.nullToEmpty(getTipoFascicolo()).equals(Strings.nullToEmpty(fascicolo.getTipologiaPratica().getNomeTipologia()));
	}

	private boolean isTitoloModificato() {
		return !Strings.nullToEmpty(getTitolo()).equals(Strings.nullToEmpty(fascicolo.getTitolo()));
	}

	private boolean isDatiAggiuntiviModificati() {

		Map<String, DatoAggiuntivo> valoriDelFascicolo = Maps.uniqueIndex(fascicolo.getValoriDatiAggiuntivi(), new CalcolaValoriPerNome());

		for (final DatoAggiuntivo valoreDelForm : formDatiAggiuntiviWidget.getDatiAggiuntivi()) {

			final DatoAggiuntivo valoreDelFascicolo = valoriDelFascicolo.get(valoreDelForm.getNome());
			final Ref<Boolean> valoreNonModificato = Ref.of(true);
			valoreDelFascicolo.accept(new DatoAggiuntivoVisitor() {

				@Override
				public void visit(DatoAggiuntivoTabella valoreDelFascicolo) {
					List<RigaDatoAggiuntivo> righe = ((DatoAggiuntivoTabella) valoreDelForm).getRighe();
					valoreNonModificato.set(valoreDelFascicolo.getRighe().equals(righe));
				}

				@Override
				public void visit(DatoAggiuntivoAnagrafica valoreDelFascicolo) {
					Double idAnagrafica1 = valoreDelFascicolo.getIdAnagrafica();
					Double idAnagrafica2 = ((DatoAggiuntivoAnagrafica) valoreDelForm).getIdAnagrafica();
					valoreNonModificato.set(GenericsUtil.isSame(idAnagrafica1, idAnagrafica2));
				}

				@Override
				public void visit(DatoAggiuntivoValoreMultiplo valoreDelFascicolo) {
					List<String> valore1 = valoreDelFascicolo.getValori();
					List<String> valore2 = ((DatoAggiuntivoValoreMultiplo) valoreDelForm).getValori();
					valoreNonModificato.set(valore1.equals(valore2));
				}

				@Override
				public void visit(DatoAggiuntivoValoreSingolo valoreDelFascicolo) {
					String valore1 = Strings.nullToEmpty(valoreDelFascicolo.getValore());
					String valore2 = Strings.nullToEmpty(((DatoAggiuntivoValoreSingolo) valoreDelForm).getValore());
					valoreNonModificato.set(valore1.equals(valore2));
				}

			});

			if (valoreNonModificato.get()) {
				continue;

			} else {
				return true;
			}
		}

		return false;
	}

	@Override
	public void setAnnullaCommand(final Command annullaCommand) {
		this.annullaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				annullaCommand.execute();
			}
		});
	}

	@Override
	public void setConfermaCommand(final Command confermaCommand) {
		this.confermaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				confermaCommand.execute();
			}
		});
	}

	private void controllaTitoloOriginale() {
		if (Strings.isNullOrEmpty(fascicolo.getTitoloOriginale())) {
			titoloOriginalePanel.setVisible(false);
		} else {
			titoloOriginalePanel.setVisible(true);
			titoloOriginaleLabel.setText(fascicolo.getTitoloOriginale());
		}
	}

	@Override
	public boolean controlloClientDatiAggiuntivi() {
		return formDatiAggiuntiviWidget.validazioneClient();
	}

	@Override
	public boolean controlloServerDatiAggiuntivi(List<ValidazioneDatoAggiuntivoDTO> validazione) {
		return formDatiAggiuntiviWidget.validazioneServer(validazione);
	}

	@Override
	public void setRequiredField() {
		if (Strings.isNullOrEmpty(getTitolo())) {
			titoloTextBox.getElement().setAttribute("required", "required");
		} else {
			titoloTextBox.getElement().removeAttribute("required");
		}
	}

	private void controllaTitoloPredefinito() {
		boolean isTitoloPredefinito = !Strings.isNullOrEmpty(tipologieAccessibili.get(getTipoFascicolo()).getTemplateTitolo());
		if (isTitoloPredefinito) {
			titoloTextBox.setValue("...");
			titoloTextBox.setStyleName("testo disabilitato");
		} else {
			if (AUTO_COMPILED_TITLE.equals(Strings.nullToEmpty(titoloTextBox.getText()))) {
				titoloTextBox.setValue(getTitolo());
			}
			titoloTextBox.removeStyleName("disabilitato");
		}
		titoloTextBox.setEnabled(!isTitoloPredefinito);
	}

	@Override
	public void setTipologieFascicolo(List<AnagraficaFascicolo> tipologie) {
		tipologiaFascicolo.clear();
		tipologieAccessibili.clear();
		for (AnagraficaFascicolo tipo : tipologie) {
			tipologieAccessibili.put(tipo.getNomeTipologia(), tipo);
			tipologiaFascicolo.addItem(tipo.getEtichettaTipologia(), tipo.getNomeTipologia());
		}
	}

	@Override
	public void mostraFascicolo(FascicoloDTO fascicolo) {
		this.fascicolo = fascicolo;
		titoloTextBox.setText(fascicolo.getTitolo());
		formDatiAggiuntiviWidget.setDatiAggiuntiviPerModifica(fascicolo.getValoriDatiAggiuntivi(), true);
		int index = 0;
		for (AnagraficaFascicolo tipo : tipologieAccessibili.values()) {
			if (tipo.getNomeTipologia().equals(fascicolo.getTipologiaPratica().getNomeTipologia())) {
				tipologiaFascicolo.setSelectedIndex(index);
			}
			index++;
		}
		controllaTitoloPredefinito();
		controllaTitoloOriginale();
	}

	@Override
	public FascicoloDTO getFascicolo() {
		return fascicolo;
	}

	@Override
	public String getTitolo() {
		String txt = titoloTextBox.getText();
		if (txt != null) {
			txt = txt.trim();
		}
		if (AUTO_COMPILED_TITLE.equals(txt)) {
			txt = fascicolo.getTitolo();
		}
		return txt;
	}

	@Override
	public String getTipoFascicolo() {
		return tipologiaFascicolo.getSelectedValue();
	}

	@Override
	public List<DatoAggiuntivo> getValoriDatiAggiuntivi() {
		return formDatiAggiuntiviWidget.getDatiAggiuntivi();
	}

	private class CalcolaValoriPerNome implements Function<DatoAggiuntivo, String> {
		@Override
		public String apply(DatoAggiuntivo v) {
			return v.getNome();
		}
	}

	@Override
	public void loadFormDatiAggiuntivi(EventBus eventBus, Object openingRequestor, DispatchAsync dispatcher) {
		formDatiAggiuntiviWidget = new FormDatiAggiuntiviWidget(eventBus, openingRequestor, dispatcher);
		datiAggiuntiviPanel.add(formDatiAggiuntiviWidget);
	}

	@Override
	public FormDatiAggiuntiviWidget getFormDatiAggiuntivi() {
		return formDatiAggiuntiviWidget;
	}
}
