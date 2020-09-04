package it.eng.portlet.consolepec.gwt.client.worklist;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaLoaded;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.RicercaLiberaPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.CustomButtonCell;
import it.eng.portlet.consolepec.gwt.shared.model.ErrorCode;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.cellview.client.Column;

public class RicercaLiberaStrategy extends AbstractWorklistStrategy {

	private Column<PraticaDTO, String> buttonDettaglioColumn;
	private Column<PraticaDTO, String> buttonAzioniColumn;

	PraticaLoaded p = new PraticaLoaded() {

		@Override
		public void onPraticaLoaded(PraticaDTO pratica) {
			aggiornaRiga(pratica);
		}

		@Override
		public void onPraticaError(String error) {
			if (ErrorCode.PRATICA_NOT_FOUND.name().equalsIgnoreCase(error))
				refreshDatiGrid();

		}
	};

	@Override
	public void aggiornaRigheEspanse() {
		for (PraticaDTO pratica : getGrid().getElencoRigheEspanse()) {
			getPraticheDB().getPraticaByPathETipo(pratica.getClientID(), pratica.getTipologiaPratica(), getSitemapMenu().containsLink(pratica.getClientID()), p);
		}

	}

	@Override
	public void aggiornaRigheSelezionate() {
		// non serve, non abbiamo checkbox di selezione
	}

	@Override
	protected void configuraColonne() {
		configuraColonnaTipoPratica();
		configuraColonnaPresaInCarico();
		configuraColonnaPG();
		configuraColonnaStato();
		configuraColonnaTitolo();
		configuraColonnaProvenienza();
		configuraColonnaAssegnatario();
		configuraColonnaData();
		configuraColonnaDettaglioButton();
		configuraColonnaAzioniButton();

		String[] stiliColonne = { "tipopratica", "presaincarico", "numannopg", "stato", "titolo", "mittente", "assegnatario", "data", "button", "button" };
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
		getGrid().addColumn(buttonDettaglioColumn, getIntestazioneColonnaDettaglioButton());

	}

	private String getIntestazioneColonnaDettaglioButton() {
		return "";
	}

	private void configuraColonnaAzioniButton() {
		final CustomButtonCell customButtonCell = new CustomButtonCell("btn black worklist_btn");
		buttonAzioniColumn = new Column<PraticaDTO, String>(customButtonCell) {

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
		place.addParam(NameTokensParams.nomeClasseDiRitorno, RicercaLiberaPresenter.class.getName());
		getEventBus().fireEvent(new GoToPlaceEvent(place));

	}

}
