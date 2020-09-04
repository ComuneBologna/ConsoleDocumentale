package it.eng.portlet.consolepec.gwt.client.worklist;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaComunicazioneLoaded;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.widget.CustomButtonCell;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.ColonnaWorklist;
import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

public class ComunicazioneWorklistStrategy extends AbstractWorklistStrategy {

	private TextColumn<PraticaDTO> nomeColumn, descrizioneColumn;
	private Column<PraticaDTO, String> buttonDettaglioColumn;
	private Column<PraticaDTO, String> buttonAzioniColumn;

	@Override
	public void aggiornaRigheEspanse() {
		List<PraticaDTO> espanse = getGrid().getElencoRigheEspanse();
		Set<String> ids = new HashSet<String>();
		for (PraticaDTO pratica : espanse) {
			ids.add(pratica.getClientID());
		}
		new GestioneRefreshComunicazione(ids);
	}

	@Override
	public void aggiornaRigheSelezionate() {
		new GestioneRefreshComunicazione(getIdRigheSelezionate());
	}

	class GestioneRefreshComunicazione implements PraticaComunicazioneLoaded {

		private boolean notAssegnateFound = false;

		public GestioneRefreshComunicazione(Set<String> ids) {
			for (String id : ids) {
				getPraticheDB().getComunicazioneByPath(id, getSitemapMenu().containsLink(id), this);
			}
			if (ids.size() == 0) {
				refreshDatiGrid();
			}
		}

		@Override
		public void onPraticaLoaded(ComunicazioneDTO c) {
			if (!notAssegnateFound) {
				if (c.isUtenteAssegnatario() || c.getAssegnatario() == null) {
					aggiornaRiga(c);
				} else {
					notAssegnateFound = true;
					refreshDatiGrid();
				}
			}
		}

		/**
		 * Nel caso in cui l'utente non sia l'assegnatario e ne esista un altro, la pratica non deve più essere in worklist, quindi si fa reset della grid
		 */
		@Override
		public void onPraticaError(String error) {

		}

	}

	@Override
	protected void configuraColonne() {

		configuraColonnaCodice();
		configuraColonnaIdTemplate();
		configuraColonnaData();
		configuraColonnaDettaglioButton();
		configuraColonnaAzioniButton();

		String[] stiliColonne = { "nome", "nome", "button", "button" };
		for (int i = 0; i < stiliColonne.length; i++) {
			getGrid().getHeader(i).setHeaderStyleNames(stiliColonne[i]);
		}

	}

	private void configuraColonnaDettaglioButton() {
		CustomButtonCell customButtonCell = new CustomButtonCell("btn worklist_btn");
		buttonDettaglioColumn = new Column<PraticaDTO, String>(customButtonCell) {

			@Override
			public String getValue(PraticaDTO pratica) {
				return "Apri";
			}

			@Override
			public void onBrowserEvent(Context context, Element elem, PraticaDTO object, NativeEvent event) {
				event.preventDefault();
				if (event.getButton() == NativeEvent.BUTTON_LEFT) {
					handleDettaglio(object, false);
				}
			}
		};

		buttonDettaglioColumn.setCellStyleNames("button");
		getGrid().addColumn(buttonDettaglioColumn, "");

	}

	protected TextColumn<PraticaDTO> configuraColonnaCodice() {
		nomeColumn = new TextColumn<PraticaDTO>() {
			@Override
			public String getValue(PraticaDTO object) {
				return ((ComunicazioneDTO) object).getCodice();
			}
		};
		nomeColumn.setCellStyleNames(ColonnaWorklist.CODICE.getId());
		nomeColumn.setDataStoreName(ColonnaWorklist.CODICE.getId());
		nomeColumn.setSortable(false);
		getGrid().addColumn(nomeColumn, getIntestazioneColonnaCodice());
		return nomeColumn;
	}

	protected TextColumn<PraticaDTO> configuraColonnaIdTemplate() {
		descrizioneColumn = new TextColumn<PraticaDTO>() {
			@Override
			public String getValue(PraticaDTO object) {
				return ((ComunicazioneDTO) object).getIdDocumentaleTemplate();
			}
		};
		descrizioneColumn.setCellStyleNames(ColonnaWorklist.ID_TEMPLATE.getId());
		descrizioneColumn.setDataStoreName(ColonnaWorklist.ID_TEMPLATE.getId());
		descrizioneColumn.setSortable(false);
		getGrid().addColumn(descrizioneColumn, getIntestazioneIdTemplate());
		return descrizioneColumn;
	}

	private void handleDettaglio(PraticaDTO dto, boolean showAction) {

		Place place = new Place();
		place.setToken(NameTokens.dettagliocomunicazione);
		place.addParam(NameTokensParams.showActions, Boolean.toString(showAction));
		place.addParam(NameTokensParams.idPratica, dto.getClientID());
		getEventBus().fireEvent(new GoToPlaceEvent(place));

	}

	@Override
	protected String checkTitoloLength(PraticaDTO praticaDTO) {
		String titolo = praticaDTO.getTitolo();
		if (titolo != null && titolo.length() > ConsolePecConstants.MAX_LENGHT_TITOLO_MODULISTICA) {
			StringBuilder sb = new StringBuilder(titolo);
			titolo = sb.delete(ConsolePecConstants.MAX_LENGHT_TITOLO_MODULISTICA - 3, titolo.length()).append("...").toString();
		}
		return titolo;
	}

	private void configuraColonnaAzioniButton() {
		CustomButtonCell customButtonCell = new CustomButtonCell("btn black worklist_btn");
		buttonAzioniColumn = new Column<PraticaDTO, String>(customButtonCell) {

			@Override
			public String getValue(PraticaDTO pratica) {
				return "Azioni";
			}

			@Override
			public void onBrowserEvent(Context context, Element elem, PraticaDTO object, NativeEvent event) {
				event.preventDefault();
				if (event.getButton() == NativeEvent.BUTTON_LEFT) {
					handleDettaglio(object, true);
				}
			}
		};
		buttonAzioniColumn.setCellStyleNames("button");
		getGrid().addColumn(buttonAzioniColumn, getIntestazioneColonnaAzioniButton());

	}

	protected String getIntestazioneColonnaCodice() {
		return "Codice";
	}

	protected String getIntestazioneIdTemplate() {
		return "Id template";
	}

	private String getIntestazioneColonnaAzioniButton() {
		return "";
	}
}
