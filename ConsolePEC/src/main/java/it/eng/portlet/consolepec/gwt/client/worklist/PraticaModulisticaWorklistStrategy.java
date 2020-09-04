package it.eng.portlet.consolepec.gwt.client.worklist;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaModulisticaLoaded;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.widget.CustomButtonCell;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.cellview.client.Column;

public class PraticaModulisticaWorklistStrategy extends AbstractWorklistStrategy {

	private Column<PraticaDTO, String> buttonDettaglioColumn;

	@Override
	public void aggiornaRigheEspanse() {
		List<PraticaDTO> espanse = getGrid().getElencoRigheEspanse();
		Set<String> ids = new HashSet<String>();
		for (PraticaDTO pratica : espanse) {
			ids.add(pratica.getClientID());
		}
		new GestioneRefreshPraticaModulistica(ids);
	}

	@Override
	public void aggiornaRigheSelezionate() {
		new GestioneRefreshPraticaModulistica(getIdRigheSelezionate());
	}

	class GestioneRefreshPraticaModulistica implements PraticaModulisticaLoaded {

		private boolean notAssegnateFound = false;

		public GestioneRefreshPraticaModulistica(Set<String> ids) {
			for (String id : ids)
				getPraticheDB().getPraticaModulisticaByPath(id, getSitemapMenu().containsLink(id), this);
			if (ids.size() == 0)
				refreshDatiGrid();
		}

		@Override
		public void onPraticaModulisticaLoaded(PraticaModulisticaDTO pm) {
			if (!notAssegnateFound) {
				if (pm.isUtenteAssegnatario() || pm.getAssegnatario() == null)
					aggiornaRiga(pm);
				else {
					notAssegnateFound = true;
					refreshDatiGrid();
				}
			}
		}

		/**
		 * Nel caso in cui l'utente non sia l'assegnatario e ne esista un altro, la pratica non deve più essere in worklist, quindi si fa reset della grid
		 */
		@Override
		public void onPraticaModulisticaError(String error) {

		}

	}

	@Override
	protected void configuraColonne() {

		configuraColonnaSelezione();
		configuraColonnaTipoPratica();
		configuraColonnaPresaInCarico();
		configuraColonnaPG();
		configuraColonnaStato();
		configuraColonnaTitolo();
		configuraColonnaAssegnatario();
		configuraColonnaData();
		configuraColonnaDettaglioButton();
		configuraColonnaAzioniButton();

		String[] stiliColonne = { "check", "tipopratica", "presaincarico", "numannopg", "stato", "titolo", "assegnatario", "data", "button", "button" };
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

	private void configuraColonnaAzioniButton() {
		final CustomButtonCell customButtonCell = new CustomButtonCell("btn black worklist_btn");
		Column<PraticaDTO, String> buttonAzioniColumn = new Column<PraticaDTO, String>(customButtonCell) {

			@Override
			public String getValue(PraticaDTO pratica) {
				customButtonCell.setVisible(pratica.getTipologiaPratica().isAzioniDettaglio());
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
		getGrid().addColumn(buttonAzioniColumn, "");

	}

	private void handleDettaglio(PraticaDTO dto, boolean showAction) {

		Place place = new Place();
		place.setToken(NameTokens.dettagliopraticamodulistica);
		place.addParam(NameTokensParams.showActions, Boolean.toString(showAction));
		place.addParam(NameTokensParams.idPratica, dto.getClientID());
		getEventBus().fireEvent(new GoToPlaceEvent(place));

	}
	
	@Override
	protected String checkTitoloLength(PraticaDTO praticaDTO) {
		String titolo = praticaDTO.getTitolo();
		if ( titolo != null && titolo.length() > ConsolePecConstants.MAX_LENGHT_TITOLO_MODULISTICA) {
			StringBuilder sb = new StringBuilder(titolo);
			titolo = sb.delete(ConsolePecConstants.MAX_LENGHT_TITOLO_MODULISTICA - 3, titolo.length()).append("...").toString();
		}
		return titolo;
	}

	
}
