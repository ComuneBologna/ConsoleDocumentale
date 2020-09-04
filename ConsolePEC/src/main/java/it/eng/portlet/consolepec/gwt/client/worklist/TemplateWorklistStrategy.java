package it.eng.portlet.consolepec.gwt.client.worklist;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;

import it.eng.cobo.consolepec.util.objects.Ref;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaLoaded;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.widget.CustomButtonCell;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO.ModelloVisitor;
import it.eng.portlet.consolepec.gwt.shared.model.ColonnaWorklist;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TemplatePdfDTO;

public class TemplateWorklistStrategy extends AbstractWorklistStrategy {

	private TextColumn<PraticaDTO> nomeColumn, descrizioneColumn;
	private TextColumn<PraticaDTO> statoColumn;
	private Column<PraticaDTO, String> buttonDettaglioColumn;
	private Column<PraticaDTO, String> buttonAzioniColumn;
	private Column<PraticaDTO, SafeUri> tipoTemplateColumn;

	@Override
	public void aggiornaRigheEspanse() {
		List<PraticaDTO> espanse = getGrid().getElencoRigheEspanse();
		Set<String> ids = new HashSet<String>();
		for (PraticaDTO pratica : espanse) {
			ids.add(pratica.getClientID());
		}
		new GestioneRefreshTemplate(ids);
	}

	@Override
	public void aggiornaRigheSelezionate() {
		new GestioneRefreshTemplate(getIdRigheSelezionate());
	}

	class GestioneRefreshTemplate implements PraticaLoaded {

		private boolean notAssegnateFound = false;

		public GestioneRefreshTemplate(Set<String> ids) {
			for (String id : ids) {
				getPraticheDB().getPraticaByPath(id, getSitemapMenu().containsLink(id), this);
			}
			if (ids.size() == 0) {
				refreshDatiGrid();
			}
		}

		@Override
		public void onPraticaLoaded(PraticaDTO pratica) {
			if (!notAssegnateFound) {
				if (pratica.isUtenteAssegnatario() || pratica.getAssegnatario() == null) {
					aggiornaRiga(pratica);
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

		configuraColonnaTipoTemplate();
		configuraColonnaStatoTemplate();
		configuraColonnaNome();
		configuraColonnaDescrizione();
		configuraColonnaData();
		configuraColonnaDettaglioButton();
		configuraColonnaAzioniButton();

		String[] stiliColonne = { "tipopratica", "stato", "nome", "descrizione", "data", "button", "button" };
		for (int i = 0; i < stiliColonne.length; i++) {
			getGrid().getHeader(i).setHeaderStyleNames(stiliColonne[i]);
		}

	}

	protected Column<PraticaDTO, SafeUri> configuraColonnaTipoTemplate() {
		final CustomSafeImageCell cellImg = new CustomSafeImageCell(false);
		tipoTemplateColumn = new Column<PraticaDTO, SafeUri>(cellImg) {
			@Override
			public SafeUri getValue(PraticaDTO object) {
				cellImg.setTitle(object.getTipologiaPratica().getEtichettaTipologia());

				final Ref<SafeUri> icon = Ref.of(null);

				BaseTemplateDTO modello = (BaseTemplateDTO) object;
				modello.accept(new ModelloVisitor() {

					@Override
					public void visit(TemplateDTO modelloMail) {
						icon.set(ConsolePECIcons._instance.bustinaApertaEmail().getSafeUri());

					}

					@Override
					public void visit(TemplatePdfDTO modelloPdf) {
						icon.set(ConsolePECIcons._instance.praticamodulistica().getSafeUri());

					}
				});

				return icon.get();
			}
		};

		tipoTemplateColumn.setSortable(false);
		tipoTemplateColumn.setCellStyleNames(ColonnaWorklist.TIPO_PRATICA.getId());
		tipoTemplateColumn.setDataStoreName(ColonnaWorklist.TIPO_PRATICA.getId());
		getGrid().addColumn(tipoTemplateColumn, getIntestazioneColonnaTipoTemplate());
		return tipoTemplateColumn;
	}

	private String getIntestazioneColonnaTipoTemplate() {
		return "";
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

	protected TextColumn<PraticaDTO> configuraColonnaStatoTemplate() {

		statoColumn = new TextColumn<PraticaDTO>() {

			@Override
			public String getValue(PraticaDTO object) {
				return getStato((BaseTemplateDTO) object);
			}
		};

		statoColumn.setSortable(false);
		statoColumn.setCellStyleNames(ColonnaWorklist.STATO.getId());
		statoColumn.setDataStoreName(ColonnaWorklist.STATO.getId());
		getGrid().addColumn(statoColumn, getIntestazioneColonnaStatoTemplate());
		return statoColumn;
	}

	protected TextColumn<PraticaDTO> configuraColonnaNome() {
		nomeColumn = new TextColumn<PraticaDTO>() {
			@Override
			public String getValue(PraticaDTO object) {
				return getNome((BaseTemplateDTO) object);
			}
		};
		nomeColumn.setCellStyleNames(ColonnaWorklist.TITOLO.getId());
		nomeColumn.setDataStoreName(ColonnaWorklist.TITOLO.getId());
		nomeColumn.setSortable(true);
		getGrid().addColumn(nomeColumn, getIntestazioneColonnaNome());
		return nomeColumn;
	}

	protected TextColumn<PraticaDTO> configuraColonnaDescrizione() {
		descrizioneColumn = new TextColumn<PraticaDTO>() {
			@Override
			public String getValue(PraticaDTO object) {
				return getDescrizione((BaseTemplateDTO) object);
			}
		};

		descrizioneColumn.setCellStyleNames(ColonnaWorklist.DESCRIZIONE.getId());
		descrizioneColumn.setDataStoreName(ColonnaWorklist.DESCRIZIONE.getId());
		descrizioneColumn.setSortable(false);
		getGrid().addColumn(descrizioneColumn, getIntestazioneColonnaDescrizione());
		return descrizioneColumn;
	}

	private void handleDettaglio(PraticaDTO dto, boolean showAction) {

		Place place = new Place();
		place.setToken(NameTokens.dettagliotemplate);
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

	protected String getNome(BaseTemplateDTO object) {
		return object.getNome();
	}

	protected String getIntestazioneColonnaNome() {
		return "Nome";
	}

	protected String getDescrizione(BaseTemplateDTO object) {
		return object.getDescrizione();
	}

	protected String getIntestazioneColonnaDescrizione() {
		return "Descrizione";
	}

	private String getIntestazioneColonnaAzioniButton() {
		return "";
	}

	private String getStato(BaseTemplateDTO object) {
		return object.getStatoLabel();
	}

	private String getIntestazioneColonnaStatoTemplate() {
		return "Stato";
	}
}
