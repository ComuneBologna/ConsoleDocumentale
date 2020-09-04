package it.eng.portlet.consolepec.gwt.client.widget;

import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.DatiTitolazioneMultiWordSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.action.GetComboBoxProtocollazione;
import it.eng.portlet.consolepec.gwt.shared.action.GetComboBoxProtocollazioneResult;
import it.eng.portlet.consolepec.gwt.shared.dto.ComboProtocollazioneDto;
import it.eng.portlet.consolepec.gwt.shared.model.TipoProtocollazione;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;

public class GroupSuggestBoxProtocollazione {

	static private Set<ComboProtocollazioneDto> _documenti = new TreeSet<ComboProtocollazioneDto>();
	static private Set<ComboProtocollazioneDto> _rubriche = new TreeSet<ComboProtocollazioneDto>();
	static private Set<ComboProtocollazioneDto> _sezioni = new TreeSet<ComboProtocollazioneDto>();
	static private Set<ComboProtocollazioneDto> _titoli = new TreeSet<ComboProtocollazioneDto>();
	static private Set<ComboProtocollazioneDto> _interni = new TreeSet<ComboProtocollazioneDto>();
	static private Set<ComboProtocollazioneDto> _esterni = new TreeSet<ComboProtocollazioneDto>();
	static private List<GetComboBoxProtocollazioneResultListener> _listeners = new ArrayList<GroupSuggestBoxProtocollazione.GetComboBoxProtocollazioneResultListener>();
	static private boolean loaded = false;

	public static String getTitoloNameByIdDisplayName(String id) {
		return findById(_titoli, id);
	}

	public static String getRubricaNameByIdDisplayName(String id) {
		return findById(_rubriche, id);
	}

	public static String getSezioneNameByIdDisplayName(String id) {
		return findById(_sezioni, id);
	}

	public static String getTipoDocumentoNameByIdDisplayName(String id) {
		return findById(_documenti, id);
	}

	public static String getInterniNameByIdDisplayName(String id) {
		return findById(_interni, id);
	}

	public static String getEsterniNameByIdDisplayName(String id) {
		return findById(_esterni, id);
	}

	private static String findById(Set<ComboProtocollazioneDto> setCombo, String id) {

		for (ComboProtocollazioneDto comboProtocollazione : setCombo) {
			if (comboProtocollazione.getIdentificativo().equals(id)) {
				return comboProtocollazione.getDescrizione();
			}
		}
		return "";
	}

	public static String getRubricaNameByIdTitolo(String id, String idTitolo) {
		return findById(_rubriche, id, idTitolo);
	}

	public static String getSezioneNameByIdRubrica(String id, String idTitolo, String idRubrica) {
		return findById(_sezioni, id, idTitolo, idRubrica);
	}

	private static String findById(Set<ComboProtocollazioneDto> _sezioni, String id, String idTitolo, String idRubrica) {
		for (ComboProtocollazioneDto comboProtocollazione : _sezioni) {
			if (comboProtocollazione.getIdentificativo().equals(id)) {
				if (comboProtocollazione.getRiferimentoliv1().equalsIgnoreCase(idRubrica))
					if (comboProtocollazione.getRiferimentoliv2().equalsIgnoreCase(idTitolo))
						return comboProtocollazione.getDescrizione();
			}
		}
		return "";
	}

	private static String findById(Set<ComboProtocollazioneDto> setCombo, String id, String idLiv2) {

		for (ComboProtocollazioneDto comboProtocollazione : setCombo) {
			if (comboProtocollazione.getIdentificativo().equals(id)) {
				if (comboProtocollazione.getRiferimentoliv1().equalsIgnoreCase(idLiv2))
					return comboProtocollazione.getDescrizione();
			}
		}
		return "";
	}

	@Inject
	public GroupSuggestBoxProtocollazione(final DispatchAsync dispatcher) {
		dispatcher.execute(new GetComboBoxProtocollazione(), new AsyncCallback<GetComboBoxProtocollazioneResult>() {

			@Override
			public void onFailure(Throwable caught) {

				throw new RuntimeException("Servizio non disponibile: errore di comunicazione");

			}

			@Override
			public void onSuccess(final GetComboBoxProtocollazioneResult result) {

				_documenti = result.getDocumenti();
				_rubriche = result.getRubriche();
				_sezioni = result.getSezioni();
				_titoli = result.getTitoli();
				_interni = result.getInterni();
				_esterni = result.getEsterni();
				loaded = true;
				/**
				 * Artifizio per uscire dal loop javascript e ritornare il controllo al browser. In questo modo si spezza il "conteggio" che porta a msg di script javascript "running slow"
				 */
				Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {

					@Override
					public boolean execute() {
						for (GetComboBoxProtocollazioneResultListener listener : _listeners) {
							listener.onResult(result);
						}
						return false;
					}
				}, 1);

			}

		});

	}

	private Set<ComboProtocollazioneDto> getTitoli(Set<ComboProtocollazioneDto> _titoli, TipoProtocollazione tipoProtocollazione) {
		Set<ComboProtocollazioneDto> newSet = new TreeSet<ComboProtocollazioneDto>();
		if (tipoProtocollazione == null)
			return _titoli;
		switch (tipoProtocollazione) {
		case ENTRATA:
			for (ComboProtocollazioneDto combo : _titoli) {
				if ("S".equalsIgnoreCase(combo.getFlagentrata()))
					newSet.add(combo);
			}
			break;
		case USCITA:
			for (ComboProtocollazioneDto combo : _titoli) {
				if ("S".equalsIgnoreCase(combo.getFlaguscita()))
					newSet.add(combo);
			}
			break;
		case INTERNA:
			for (ComboProtocollazioneDto combo : _titoli) {
				if ("S".equalsIgnoreCase(combo.getFlaginterna()))
					newSet.add(combo);
			}
			break;
		default:
			break;

		}
		return newSet;
	}

	public CustomSuggestBox getSuggestBoxCodiceDestinatari(final TextBox destinatari) {
		final DatiTitolazioneMultiWordSuggestOracle oracleDestinatariSuggest = new DatiTitolazioneMultiWordSuggestOracle();
		oracleDestinatariSuggest.setDati(_esterni);
		if (!loaded) {
			GetComboBoxProtocollazioneResultListener listener = new GetComboBoxProtocollazioneResultListener();
			listener.setOracleDestinatariSuggest(oracleDestinatariSuggest);
			_listeners.add(listener);
		}
		final CustomSuggestBox suggestBoxCodiceDestinatari = new CustomSuggestBox(oracleDestinatariSuggest);
		suggestBoxCodiceDestinatari.getValueBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (suggestBoxCodiceDestinatari.getValue() == null || suggestBoxCodiceDestinatari.getValue().isEmpty())
					destinatari.setValue(null);
			}
		});
		suggestBoxCodiceDestinatari.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {

				if (event.getSelectedItem() instanceof DatiTitolazioneMultiWordSuggestOracle.SuggestionExt) {
					DatiTitolazioneMultiWordSuggestOracle.SuggestionExt suggestion = (DatiTitolazioneMultiWordSuggestOracle.SuggestionExt) event.getSelectedItem();
					suggestBoxCodiceDestinatari.setIdentificativo(suggestion.getIdentificativo());
					destinatari.setValue(suggestion.getDescrizione());
				}
			}
		});
		return suggestBoxCodiceDestinatari;
	}

	/**
	 * 
	 * @return CustomSuggestBox[]. [0] Titolo [1] Rubrica [2] Sezione
	 */
	public CustomSuggestBox[] getSuggestBoxTitoloRubricaSezione(TipoProtocollazione tipoProt) {
		CustomSuggestBox[] boxs = new CustomSuggestBox[3];
		final DatiTitolazioneMultiWordSuggestOracle oracleTitoloSuggest = new DatiTitolazioneMultiWordSuggestOracle();
		final DatiTitolazioneMultiWordSuggestOracle oracleRubricaSuggest = new DatiTitolazioneMultiWordSuggestOracle();
		final DatiTitolazioneMultiWordSuggestOracle oracleSezioneSuggest = new DatiTitolazioneMultiWordSuggestOracle();
		if (!loaded) {
			GetComboBoxProtocollazioneResultListener listener = new GetComboBoxProtocollazioneResultListener();
			listener.setTp(tipoProt);
			listener.setOracleRubricaSuggest(oracleRubricaSuggest);
			listener.setOracleSezioneSuggest(oracleSezioneSuggest);
			listener.setOracleTitoloSuggest(oracleTitoloSuggest);
			_listeners.add(listener);
		}
		oracleTitoloSuggest.setDati(getTitoli(_titoli, tipoProt));
		oracleRubricaSuggest.setDati(_rubriche);
		oracleSezioneSuggest.setDati(_sezioni);

		final CustomSuggestBox suggestBoxTitolo = new CustomSuggestBox(oracleTitoloSuggest);
		final CustomSuggestBox suggestBoxRubrica = new CustomSuggestBox(oracleRubricaSuggest);
		final CustomSuggestBox suggestBoxSezione = new CustomSuggestBox(oracleSezioneSuggest);

		suggestBoxRubrica.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {

				if (event.getSelectedItem() instanceof DatiTitolazioneMultiWordSuggestOracle.SuggestionExt) {
					suggestBoxRubrica.setIdentificativo(((DatiTitolazioneMultiWordSuggestOracle.SuggestionExt) event.getSelectedItem()).getIdentificativo());
					((DatiTitolazioneMultiWordSuggestOracle) suggestBoxSezione.getSuggestOracle()).refreshLiv1(suggestBoxRubrica.getIdentificativo());
					((DatiTitolazioneMultiWordSuggestOracle) suggestBoxSezione.getSuggestOracle()).refreshLiv2(suggestBoxTitolo.getIdentificativo());
					suggestBoxSezione.refreshSuggestionList();
					suggestBoxSezione.setValue("");
				}
			}
		});

		suggestBoxSezione.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {

				if (event.getSelectedItem() instanceof DatiTitolazioneMultiWordSuggestOracle.SuggestionExt) {
					suggestBoxSezione.setIdentificativo(((DatiTitolazioneMultiWordSuggestOracle.SuggestionExt) event.getSelectedItem()).getIdentificativo());
				}
			}
		});

		suggestBoxTitolo.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {

				if (event.getSelectedItem() instanceof DatiTitolazioneMultiWordSuggestOracle.SuggestionExt) {
					suggestBoxTitolo.setIdentificativo(((DatiTitolazioneMultiWordSuggestOracle.SuggestionExt) event.getSelectedItem()).getIdentificativo());
					((DatiTitolazioneMultiWordSuggestOracle) suggestBoxRubrica.getSuggestOracle()).refreshLiv1(suggestBoxTitolo.getIdentificativo());
					suggestBoxRubrica.refreshSuggestionList();
					suggestBoxRubrica.setValue("");
					suggestBoxSezione.setValue("");
				}
			}
		});

		boxs[0] = suggestBoxTitolo;
		boxs[1] = suggestBoxRubrica;
		boxs[2] = suggestBoxSezione;
		return boxs;
	}

	public CustomSuggestBox getSuggestBoxTipologiaDocumento() {
		final DatiTitolazioneMultiWordSuggestOracle oracleTipologiaDocumento = new DatiTitolazioneMultiWordSuggestOracle();
		oracleTipologiaDocumento.setDati(_documenti);
		if (!loaded) {
			GetComboBoxProtocollazioneResultListener listener = new GetComboBoxProtocollazioneResultListener();
			listener.setOracleTipologiaDocumento(oracleTipologiaDocumento);
			_listeners.add(listener);
		}
		final CustomSuggestBox suggestBoxTipologiaDocumento = new CustomSuggestBox(oracleTipologiaDocumento);
		suggestBoxTipologiaDocumento.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {

				if (event.getSelectedItem() instanceof DatiTitolazioneMultiWordSuggestOracle.SuggestionExt) {
					suggestBoxTipologiaDocumento.setIdentificativo(((DatiTitolazioneMultiWordSuggestOracle.SuggestionExt) event.getSelectedItem()).getIdentificativo());
				}
			}
		});
		return suggestBoxTipologiaDocumento;
	}

	public CustomSuggestBox getSuggestBoxCodiceProvenienza(final TextBox descrizione, String tipologiaProtocollazione) {
		final DatiTitolazioneMultiWordSuggestOracle oracleProvenienzaSuggest = new DatiTitolazioneMultiWordSuggestOracle();
		if (tipologiaProtocollazione.equalsIgnoreCase("E"))
			oracleProvenienzaSuggest.setDati(_esterni);
		else
			oracleProvenienzaSuggest.setDati(_interni);
		if (!loaded) {
			GetComboBoxProtocollazioneResultListener listener = new GetComboBoxProtocollazioneResultListener();
			listener.setOracleProvenienzaSuggest(oracleProvenienzaSuggest);
			_listeners.add(listener);
		}
		final CustomSuggestBox suggestBoxCodiceProvenienza = new CustomSuggestBox(oracleProvenienzaSuggest);
		suggestBoxCodiceProvenienza.getValueBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (suggestBoxCodiceProvenienza.getValue() == null || suggestBoxCodiceProvenienza.getValue().isEmpty())
					descrizione.setValue(null);
			}
		});
		suggestBoxCodiceProvenienza.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {

				if (event.getSelectedItem() instanceof DatiTitolazioneMultiWordSuggestOracle.SuggestionExt) {

					DatiTitolazioneMultiWordSuggestOracle.SuggestionExt suggestion = (DatiTitolazioneMultiWordSuggestOracle.SuggestionExt) event.getSelectedItem();

					suggestBoxCodiceProvenienza.setIdentificativo(suggestion.getIdentificativo());
					descrizione.setValue(suggestion.getDescrizione());
				}
			}
		});
		return suggestBoxCodiceProvenienza;
	}

	private class GetComboBoxProtocollazioneResultListener {
		private TipoProtocollazione tp;
		private DatiTitolazioneMultiWordSuggestOracle oracleSezioneSuggest;

		public void setTp(TipoProtocollazione tp) {
			this.tp = tp;
		}

		public void setOracleSezioneSuggest(DatiTitolazioneMultiWordSuggestOracle oracleSezioneSuggest) {
			this.oracleSezioneSuggest = oracleSezioneSuggest;
		}

		public void setOracleRubricaSuggest(DatiTitolazioneMultiWordSuggestOracle oracleRubricaSuggest) {
			this.oracleRubricaSuggest = oracleRubricaSuggest;
		}

		public void setOracleTitoloSuggest(DatiTitolazioneMultiWordSuggestOracle oracleTitoloSuggest) {
			this.oracleTitoloSuggest = oracleTitoloSuggest;
		}

		public void setOracleTipologiaDocumento(DatiTitolazioneMultiWordSuggestOracle oracleTipologiaDocumento) {
			this.oracleTipologiaDocumento = oracleTipologiaDocumento;
		}

		public void setOracleProvenienzaSuggest(DatiTitolazioneMultiWordSuggestOracle oracleProvenienzaSuggest) {
			this.oracleProvenienzaSuggest = oracleProvenienzaSuggest;
		}

		public void setOracleDestinatariSuggest(DatiTitolazioneMultiWordSuggestOracle oracleDestinatariSuggest) {
			this.oracleDestinatariSuggest = oracleDestinatariSuggest;
		}

		private DatiTitolazioneMultiWordSuggestOracle oracleRubricaSuggest;
		private DatiTitolazioneMultiWordSuggestOracle oracleTitoloSuggest;
		private DatiTitolazioneMultiWordSuggestOracle oracleTipologiaDocumento;
		private DatiTitolazioneMultiWordSuggestOracle oracleProvenienzaSuggest;
		private DatiTitolazioneMultiWordSuggestOracle oracleDestinatariSuggest;

		GetComboBoxProtocollazioneResultListener() {

		}

		public void onResult(GetComboBoxProtocollazioneResult result) {
			if (this.oracleProvenienzaSuggest != null)
				this.oracleProvenienzaSuggest.setDati(result.getInterni());
			if (this.oracleTipologiaDocumento != null)
				this.oracleTipologiaDocumento.setDati(result.getDocumenti());
			if (this.oracleTitoloSuggest != null)
				this.oracleTitoloSuggest.setDati(getTitoli(result.getTitoli(), tp));
			if (this.oracleRubricaSuggest != null)
				this.oracleRubricaSuggest.setDati(result.getRubriche());
			if (this.oracleSezioneSuggest != null)
				this.oracleSezioneSuggest.setDati(result.getSezioni());
			if (this.oracleDestinatariSuggest != null)
				this.oracleDestinatariSuggest.setDati(result.getEsterni());
		}
	}

}
