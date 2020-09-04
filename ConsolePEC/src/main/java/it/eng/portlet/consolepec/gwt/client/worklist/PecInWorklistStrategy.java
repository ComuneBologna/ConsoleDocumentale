package it.eng.portlet.consolepec.gwt.client.worklist;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmaiInlLoaded;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.widget.CustomButtonCell;
import it.eng.portlet.consolepec.gwt.shared.model.ColonnaWorklist;
import it.eng.portlet.consolepec.gwt.shared.model.Destinatario;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

public class PecInWorklistStrategy extends AbstractWorklistStrategy {

	private Column<PraticaDTO, String> buttonDettaglioColumn;
	private Column<PraticaDTO, String> buttonAzioniColumn;
	private TextColumn<PraticaDTO> destinatarioColumn;

	class GestioneRefreshPraticaPEC implements PraticaEmaiInlLoaded {

		private boolean notAssegnateFound = false;

		public GestioneRefreshPraticaPEC(Set<String> ids) {
			for (String id : ids) {
				getPraticheDB().getPecInByPath(id, getSitemapMenu().containsLink(id), this);
			}
			if (ids.size() == 0) {
				refreshDatiGrid();
			}
		}

		@Override
		public void onPraticaError(String error) {
			// TODO Auto-generated method stub

		}

		/**
		 * Nel caso in cui l'utente non sia l'assegnatario e ne esista un altro, la pratica non deve più essere in worklist, quindi si fa reset della grid
		 */
		@Override
		public void onPraticaLoaded(PecInDTO pec) {
			if (!notAssegnateFound) {
				if (pec.isUtenteAssegnatario() || pec.getAssegnatario() == null) {
					aggiornaRiga(pec);
				} else {
					notAssegnateFound = true;
					refreshDatiGrid();
				}
			}
		}

	}

	@Override
	protected String getIntestazioneColonnaProvenienza() {
		return "Mittente";
	}

	@Override
	protected String getIntestazioneColonnaTitolo() {
		return "Oggetto";
	}

	@Override
	protected String getProvenienza(PraticaDTO object) {
		return ((PecInDTO) object).getMittente();
	}

	@Override
	public void aggiornaRigheEspanse() {
		List<PraticaDTO> espanse = getGrid().getElencoRigheEspanse();
		Set<String> ids = new HashSet<String>();
		for (PraticaDTO pratica : espanse) {
			ids.add(pratica.getClientID());
		}
		new GestioneRefreshPraticaPEC(ids);
	}

	@Override
	public void aggiornaRigheSelezionate() {
		new GestioneRefreshPraticaPEC(getIdRigheSelezionate());
	}

	@Override
	public void aggiornaRiga(PraticaDTO pratica) {
		super.aggiornaRiga(pratica);

	}

	@Override
	protected void configuraColonne() {

		configuraColonnaSelezione();
		configuraColonnaTipoPratica();
		configuraColonnaPresaInCarico();
		configuraColonnaPG();
		configuraColonnaStato();
		configuraColonnaTitolo();
		configuraColonnaProvenienza();
		configuraColonnaDestinatario();
		configuraColonnaAssegnatario();
		configuraColonnaData();
		configuraColonnaDettaglioButton();
		configuraColonnaAzioniButton();

		String[] stiliColonne = { "check", "tipopratica", "presaincarico", "numannopg", "stato", "titolo", "mittente", "mittente", "assegnatario", "data", "button", "button" };
		for (int i = 0; i < stiliColonne.length; i++) {
			getGrid().getHeader(i).setHeaderStyleNames(stiliColonne[i]);
		}

	}

	private TextColumn<PraticaDTO> configuraColonnaDestinatario() {
		destinatarioColumn = new TextColumn<PraticaDTO>() {
			@Override
			public String getValue(PraticaDTO pratica) {
				return getDestinatario(((PecInDTO) pratica));
			}
		};
		destinatarioColumn.setCellStyleNames(ColonnaWorklist.DESTINATARIO.getId());
		destinatarioColumn.setDataStoreName(ColonnaWorklist.DESTINATARIO.getId());
		destinatarioColumn.setSortable(false);
		getGrid().addColumn(destinatarioColumn, getIntestazioneColonnaDestinatario());
		return destinatarioColumn;

	}

	private String getDestinatario(PecInDTO pecInDTO) {
		Destinatario destinatarioPrincipale = pecInDTO.getDestinatarioPrincipale();
		if (destinatarioPrincipale != null) {
			return destinatarioPrincipale.getDestinatario();
		}

		TreeSet<Destinatario> destinatari = pecInDTO.getDestinatari();
		if (!destinatari.isEmpty()) {
			return destinatari.first().getDestinatario();
		}

		return "";
	}

	private String getIntestazioneColonnaDestinatario() {
		return "Destinatario";
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
		place.setToken(NameTokens.dettagliopecin);
		place.addParam(NameTokensParams.showActions, Boolean.toString(showAction));
		place.addParam(NameTokensParams.idPratica, dto.getClientID());
		getEventBus().fireEvent(new GoToPlaceEvent(place));

	}

	@Override
	protected String getData(PraticaDTO praticaDTO) {
		PecInDTO pecIn = (PecInDTO) praticaDTO;
		return pecIn.getDataOraArrivo();
	}

	@Override
	protected TextColumn<PraticaDTO> configuraColonnaData() {
		dataColumn = new TextColumn<PraticaDTO>() {
			@Override
			public String getValue(PraticaDTO object) {
				return getData(object);
			}
		};
		dataColumn.setCellStyleNames(ColonnaWorklist.DATA_RIC.getId());
		dataColumn.setDataStoreName(ColonnaWorklist.DATA_RIC.getId());
		dataColumn.setSortable(true);
		getGrid().addColumn(dataColumn, getIntestazioneColonnaData());
		return dataColumn;
	}

}
