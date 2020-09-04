package it.eng.portlet.consolepec.gwt.client.worklist;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloGenericoLoaded;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.widget.CustomButtonCell;
import it.eng.portlet.consolepec.gwt.shared.model.ErrorCode;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.cellview.client.Column;

public class FascicoloGenericoWorklistStrategy extends AbstractWorklistStrategy {

	private Column<PraticaDTO, String> buttonDettaglioColumn;
	private Column<PraticaDTO, String> buttonAzioniColumn;
	
	class GestioneRefreshPraticaFascicolo implements PraticaFascicoloGenericoLoaded {

		private boolean notAssegnateFound = false;

		public GestioneRefreshPraticaFascicolo(Set<String> ids) {
			for (String id : ids)
				getPraticheDB().getFascicoloByPath(id, getSitemapMenu().containsLink(id), this);
			if (ids.size() == 0)
				refreshDatiGrid();
		}

		/**
		 * Nel caso in cui l'utente non sia l'assegnatario e ne esista un altro,(oppure il fascicolo è stato eliminato) la pratica non deve più essere in worklist, quindi si fa reset della grid
		 */
		@Override
		public void onPraticaLoaded(FascicoloDTO fascicolo) {
			if (!notAssegnateFound) {
				if (fascicolo.isUtenteAssegnatario() || fascicolo.getAssegnatario() == null)
					aggiornaRiga(fascicolo);
				else {
					notAssegnateFound = true;
					refreshDatiGrid();
				}
			}
		}

		@Override
		public void onPraticaError(String error) {
			if (ErrorCode.PRATICA_NOT_FOUND.name().equalsIgnoreCase(error))
				refreshDatiGrid();
		}

	}

	public FascicoloGenericoWorklistStrategy() {
	}

	@Override
	public void aggiornaRigheEspanse() {
		List<PraticaDTO> espanse = getGrid().getElencoRigheEspanse();
		Set<String> ids = new HashSet<String>();
		for (PraticaDTO pratica : espanse) {
			ids.add(pratica.getClientID());
		}
		new GestioneRefreshPraticaFascicolo(ids);
	}

	@Override
	public void aggiornaRigheSelezionate() {
		new GestioneRefreshPraticaFascicolo(getIdRigheSelezionate());
	}

	/* metodi interni */

	@Override
	protected void configuraColonne() {
		super.configuraColonne();
		configuraColonnaDettaglioButton();
		configuraColonnaAzioniButton();
		
		int columnCount = getGrid().getColumnCount();

		String[] stiliColonne = { "button", "button" };
		for (int i = columnCount - stiliColonne.length; i < columnCount; i++) {
			getGrid().getHeader(i).setHeaderStyleNames(stiliColonne[columnCount - (i + 1)]);
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
		getGrid().addColumn(buttonDettaglioColumn, getIntestazioneColonnaDettaglioButton());

	}

	private String getIntestazioneColonnaDettaglioButton() {
		return "";
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

	private String getIntestazioneColonnaAzioniButton() {
		return "";
	}

	private void handleDettaglio(PraticaDTO dto, boolean showAction) {

		Place place = new Place();
		place.setToken(dto.getTipologiaPratica().getDettaglioNameToken());
		place.addParam(NameTokensParams.showActions, Boolean.toString(showAction));
		place.addParam(NameTokensParams.idPratica, dto.getClientID());
		getEventBus().fireEvent(new GoToPlaceEvent(place));

	}

}
