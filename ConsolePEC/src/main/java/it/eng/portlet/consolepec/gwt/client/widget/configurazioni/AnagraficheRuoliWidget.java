package it.eng.portlet.consolepec.gwt.client.widget.configurazioni;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.profilazione.Settore;
import it.eng.cobo.consolepec.commons.profilazione.Utente;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.AnagraficheRuoliSuggestOracle;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.UtentiSuggestOracle;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.UtentiSuggestOracleImpl.UtenteSuggestion;

/**
 *
 * @author biagiot
 *
 */
public class AnagraficheRuoliWidget extends Composite {

	interface AnagraficheRuoliWidgetBinder extends UiBinder<Widget, AnagraficheRuoliWidget> {/**/}

	@UiField
	HTMLPanel settoriPanel;
	@UiField
	HTMLPanel utentiPanel;
	@UiField
	HTMLPanel ruoliPanel;
	@UiField
	HTMLPanel operatoriPanel;
	@UiField(provided = true)
	SuggestBox settoriSuggestBox = new SuggestBox(new SpacebarSuggestOracle(new ArrayList<String>()));
	@UiField(provided = true)
	SuggestBox utentiSuggestBox;
	@UiField(provided = true)
	SuggestBox gruppiSuggestBox = new SuggestBox(new AnagraficheRuoliSuggestOracle(new ArrayList<AnagraficaRuolo>()));
	@UiField(provided = true)
	SuggestBox operatoriSuggestBox = new SuggestBox(new SpacebarSuggestOracle(new ArrayList<String>()));

	private ConfigurazioniHandler configurazioniHandler;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;
	private static AnagraficheRuoliWidgetBinder uiBinder = GWT.create(AnagraficheRuoliWidgetBinder.class);
	private boolean showSettori;
	private boolean showOperatori;
	private boolean showUtenti;

	private HandlerRegistration handlerRegistration;
	private SelectionHandler<SuggestOracle.Suggestion> ruoliSelectionHandler;

	public AnagraficheRuoliWidget(ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler, UtentiSuggestOracle utentiSuggestOracle, boolean showSettori,
			boolean showOperatori, boolean showUtenti) {
		this.configurazioniHandler = configurazioniHandler;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		this.utentiSuggestBox = new SuggestBox(utentiSuggestOracle);
		this.showSettori = showSettori;
		this.showOperatori = showOperatori;
		this.showUtenti = showUtenti;

		initWidget(uiBinder.createAndBindUi(this));
	}

	public AnagraficheRuoliWidget rewriteLabel(String labelSettore, String labelGruppo, String labelUtente) {
		NodeList<Element> span = null;

		if (labelSettore != null) {
			span = settoriPanel.getElement().getElementsByTagName("span");

			for (int i = 0; i < span.getLength(); i++) {
				Element e = span.getItem(i);
				if ("Settore".equalsIgnoreCase(e.getInnerText())) {
					e.setInnerText(labelSettore);
				}
			}
		}

		if (labelGruppo != null) {
			span = ruoliPanel.getElement().getElementsByTagName("span");
			for (int x = 0; x < span.getLength(); x++) {
				Element e = span.getItem(x);
				if ("Gruppo*".equalsIgnoreCase(e.getInnerText())) {
					e.setInnerText(labelGruppo);
				}
			}
		}

		if (labelUtente != null) {
			span = utentiPanel.getElement().getElementsByTagName("span");
			for (int x = 0; x < span.getLength(); x++) {
				Element e = span.getItem(x);
				if ("Utente".equalsIgnoreCase(e.getInnerText())) {
					e.setInnerText(labelUtente);
				}
			}
		}

		return this;
	}

	private void reset() {
		clear();
		if (handlerRegistration != null) {
			handlerRegistration.removeHandler();
		}
	}

	public void clear() {
		if (settoriSuggestBox != null) {
			settoriSuggestBox.setValue(null);
			settoriSuggestBox.setEnabled(true);
			settoriSuggestBox.setStylePrimaryName("testo");
		}

		if (utentiSuggestBox != null) {
			utentiSuggestBox.setValue(null);
			utentiSuggestBox.setEnabled(true);
			utentiSuggestBox.setStylePrimaryName("testo");
		}

		if (gruppiSuggestBox != null) {
			gruppiSuggestBox.setValue(null);
			gruppiSuggestBox.setEnabled(true);
			gruppiSuggestBox.setStylePrimaryName("testo");
		}

		if (operatoriSuggestBox != null) {
			operatoriSuggestBox.setValue(null);
			operatoriSuggestBox.setEnabled(true);
			operatoriSuggestBox.setStylePrimaryName("testo");
		}
	}

	public void setEditabile(boolean editabile) {
		if (settoriSuggestBox != null) {
			settoriSuggestBox.setEnabled(editabile);
			if (editabile) {
				settoriSuggestBox.removeStyleName("disabilitato");
			} else {
				settoriSuggestBox.addStyleName("disabilitato");
			}
		}

		if (gruppiSuggestBox != null) {
			gruppiSuggestBox.setEnabled(editabile);
			if (editabile) {
				gruppiSuggestBox.removeStyleName("disabilitato");
			} else {
				gruppiSuggestBox.addStyleName("disabilitato");
			}
		}

		if (operatoriSuggestBox != null) {
			operatoriSuggestBox.setEnabled(editabile);
			if (editabile) {
				operatoriSuggestBox.removeStyleName("disabilitato");
			} else {
				operatoriSuggestBox.addStyleName("disabilitato");
			}
		}

		if (utentiSuggestBox != null) {
			utentiSuggestBox.setEnabled(false);
			if (editabile) {
				utentiSuggestBox.removeStyleName("disabilitato");
			} else {
				utentiSuggestBox.addStyleName("disabilitato");
			}
		}
	}

	private Command<Void, AnagraficaRuolo> command;

	public void addSelectionCommand(Command<Void, AnagraficaRuolo> command) {
		this.command = command;
	}

	public void showWidget(Settore settore, AnagraficaRuolo anagraficaRuolo, boolean filtroUtente, boolean filtroRiservato, boolean subordinati) {
		reset();

		final Map<Settore, List<AnagraficaRuolo>> ruoliSettoriMap = profilazioneUtenteHandler.getSettori(filtroUtente, filtroRiservato, subordinati);
		final List<AnagraficaRuolo> ruoliVisibili = new ArrayList<>();
		for (Entry<Settore, List<AnagraficaRuolo>> entry : ruoliSettoriMap.entrySet()) {
			for (AnagraficaRuolo ar : entry.getValue()) {

				Collections.sort(entry.getValue(), new Comparator<AnagraficaRuolo>() {

					@Override
					public int compare(AnagraficaRuolo o1, AnagraficaRuolo o2) {
						return o1.getEtichetta().compareToIgnoreCase(o2.getEtichetta());
					}
				});

				if (!ruoliVisibili.contains(ar)) {
					ruoliVisibili.add(ar);
				}
			}
		}

		Collections.sort(ruoliVisibili, new Comparator<AnagraficaRuolo>() {

			@Override
			public int compare(AnagraficaRuolo o1, AnagraficaRuolo o2) {
				return o1.getEtichetta().compareToIgnoreCase(o2.getEtichetta());
			}
		});

		if (!showUtenti) {
			utentiPanel.removeFromParent();
		}

		if (showSettori) {
			List<String> nomiSettori = Lists.newArrayList(Lists.transform(Lists.newArrayList(ruoliSettoriMap.keySet()), new Function<Settore, String>() {
				@Override
				public String apply(Settore input) {
					return input.getNome();
				}
			}));

			Collections.sort(nomiSettori, new Comparator<String>() {

				@Override
				public int compare(String o1, String o2) {
					return o1.compareToIgnoreCase(o2);
				}

			});

			SpacebarSuggestOracle so = (SpacebarSuggestOracle) settoriSuggestBox.getSuggestOracle();
			so.setSuggestions(nomiSettori);
			handlerRegistration = settoriSuggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
				@Override
				public void onSelection(SelectionEvent<Suggestion> paramSelectionEvent) {
					Suggestion selected = paramSelectionEvent.getSelectedItem();
					Settore settore = configurazioniHandler.getSettore(selected.getDisplayString());
					if (settore != null) {
						gruppiSuggestBox.setValue(null);
						setSuggestionsRuoli(ruoliSettoriMap.get(settore));
					} else {
						settoriSuggestBox.setValue(null);
						setSuggestionsRuoli(ruoliVisibili);
					}
				}
			});

			handlerRegistration = settoriSuggestBox.getValueBox().addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent paramChangeEvent) {
					if (settoriSuggestBox.getValue() == null || configurazioniHandler.getSettore(settoriSuggestBox.getValue()) == null) {

						if (showUtenti) {
							utentiSuggestBox.setValue(null);
						}

						setSuggestionsRuoli(ruoliVisibili);
					}
				}
			});

			if (showUtenti) {
				utentiSuggestBox.getValueBox().addChangeHandler(new ChangeHandler() {

					@Override
					public void onChange(ChangeEvent event) {
						if (utentiSuggestBox.getValue() == null || utentiSuggestBox.getValue().trim().equals("")) {
							gruppiSuggestBox.setValue(null);
							settoriSuggestBox.setValue(null);
							setSuggestionsRuoli(ruoliVisibili);

						} else {
							UtentiSuggestOracle oc = (UtentiSuggestOracle) utentiSuggestBox.getSuggestOracle();
							Utente utente = oc.getUtenteFromSuggest(utentiSuggestBox.getValue());
							if (utente != null) {

								List<AnagraficaRuolo> ruoli = utente.getAnagraficheRuoli();
								List<AnagraficaRuolo> ruoliFiltrati = new ArrayList<>();

								for (AnagraficaRuolo ruolo : ruoli) {
									if (ruoliVisibili.contains(ruolo)) {
										ruoliFiltrati.add(ruolo);
									}
								}
								gruppiSuggestBox.setValue(null);
								settoriSuggestBox.setValue(null);
								setSuggestionsRuoli(ruoliFiltrati);
							}

						}
					}

				});
				utentiSuggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

					@Override
					public void onSelection(SelectionEvent<Suggestion> event) {
						UtenteSuggestion suggestion = (UtenteSuggestion) event.getSelectedItem();
						if (suggestion != null && suggestion.getUtente() != null) {
							List<AnagraficaRuolo> ruoli = suggestion.getUtente().getAnagraficheRuoli();
							List<AnagraficaRuolo> ruoliFiltrati = new ArrayList<>();

							for (AnagraficaRuolo ruolo : ruoli) {
								if (ruoliVisibili.contains(ruolo)) {
									ruoliFiltrati.add(ruolo);
								}
							}
							gruppiSuggestBox.setValue(null);
							settoriSuggestBox.setValue(null);
							setSuggestionsRuoli(ruoliFiltrati);
						}

					}

				});
			}

			if (settore != null) {
				settoriSuggestBox.setValue(settore.getNome());
				setSuggestionsRuoli(ruoliSettoriMap.get(settore));
			}

		} else {
			settoriPanel.removeFromParent();
		}

		if (showOperatori) {
			handlerRegistration = gruppiSuggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
				@Override
				public void onSelection(SelectionEvent<Suggestion> paramSelectionEvent) {
					Suggestion selected = paramSelectionEvent.getSelectedItem();
					AnagraficaRuolo ruolo = configurazioniHandler.getAnagraficaRuoloByEtichetta(selected.getDisplayString());
					SpacebarSuggestOracle so = (SpacebarSuggestOracle) operatoriSuggestBox.getSuggestOracle();
					so.setSuggestions(ruolo.getOperatori());
				}
			});
		} else {
			operatoriPanel.removeFromParent();
		}

		if (anagraficaRuolo != null) {
			if (showSettori && settore != null) {
				if (ruoliSettoriMap.get(settore) != null && ruoliSettoriMap.get(settore).contains(anagraficaRuolo)) {
					gruppiSuggestBox.setValue(anagraficaRuolo.getEtichetta());
				} else {
					settoriSuggestBox.setValue(null);
					gruppiSuggestBox.setValue(anagraficaRuolo.getEtichetta());
					setSuggestionsRuoli(ruoliVisibili);
				}
			} else {
				gruppiSuggestBox.setValue(anagraficaRuolo.getEtichetta());
				setSuggestionsRuoli(ruoliVisibili);
			}
		} else {
			if ((showSettori && settore == null) || settore == null || !showSettori) {
				setSuggestionsRuoli(ruoliVisibili);
			}
		}

		/*
		 * SELECTION HANDLER
		 */
		if (ruoliSelectionHandler != null) {
			handlerRegistration = gruppiSuggestBox.addSelectionHandler(ruoliSelectionHandler);
		}

		handlerRegistration = gruppiSuggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> paramSelectionEvent) {
				Suggestion selected = paramSelectionEvent.getSelectedItem();
				AnagraficaRuolo ruolo = configurazioniHandler.getAnagraficaRuoloByEtichetta(selected.getDisplayString());

				if (ruolo != null && showSettori) {
					List<Settore> settori = new ArrayList<Settore>();

					for (Entry<Settore, List<AnagraficaRuolo>> entry : ruoliSettoriMap.entrySet()) {
						if (entry.getValue().contains(ruolo)) {
							settori.add(entry.getKey());
						}
					}

					String sett = !settori.isEmpty() ? settori.get(0).getNome() : "";
					for (Settore s : settori) {
						if (s.getRuoli().contains(ruolo.getRuolo())) {
							sett = s.getNome();
							break;
						}
					}

					settoriSuggestBox.setValue(sett);

					if (command != null) {
						command.exe(ruolo);
					}
				}
			}
		});

	}

	private void setSuggestionsRuoli(List<AnagraficaRuolo> anagraficheRuoli) {
		if (anagraficheRuoli != null) {
			Collections.sort(anagraficheRuoli, new Comparator<AnagraficaRuolo>() {

				@Override
				public int compare(AnagraficaRuolo o1, AnagraficaRuolo o2) {
					return o1.getEtichetta().compareToIgnoreCase(o2.getEtichetta());
				}
			});

			AnagraficheRuoliSuggestOracle so = (AnagraficheRuoliSuggestOracle) gruppiSuggestBox.getSuggestOracle();
			so.setAnagraficheRuoli(new ArrayList<>(new HashSet<>(anagraficheRuoli)));
			if (anagraficheRuoli.size() == 1) {
				gruppiSuggestBox.setValue(anagraficheRuoli.get(0).getEtichetta());
			}

			if (showSettori) {
				if (anagraficheRuoli.size() == 1) {
					gruppiSuggestBox.setEnabled(false);
					gruppiSuggestBox.setStyleName("testo disabilitato");
				} else {
					gruppiSuggestBox.setEnabled(true);
					gruppiSuggestBox.setStyleName("testo");
					settoriSuggestBox.setStyleName("testo");

					if (showUtenti) {
						utentiSuggestBox.setStyleName("testo");
					}
				}
			}
		}
	}

	public void addRuoliSelectionHandler(SelectionHandler<SuggestOracle.Suggestion> ruoliSelectionHandler) {
		this.ruoliSelectionHandler = ruoliSelectionHandler;
	}

	public AnagraficaRuolo getAnagraficaRuoloSelezionata() {
		return configurazioniHandler.getAnagraficaRuoloByEtichetta(gruppiSuggestBox.getValue());
	}

	public Settore getSettoreSelezionato() {
		return settoriSuggestBox != null ? configurazioniHandler.getSettore(settoriSuggestBox.getValue()) : null;
	}

	public String getOperatoreSelezionato() {
		String operatore = null;

		if (showOperatori) {
			operatore = operatoriSuggestBox.getValue();
		}

		return operatore;
	}
}
