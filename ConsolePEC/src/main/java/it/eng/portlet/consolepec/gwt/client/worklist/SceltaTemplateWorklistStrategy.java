package it.eng.portlet.consolepec.gwt.client.worklist;

import java.util.List;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.SingleSelectionModel;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.ModelloLoaded;
import it.eng.portlet.consolepec.gwt.client.widget.RadioButtonCell;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ColonnaWorklist;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TemplateDTO;

public class SceltaTemplateWorklistStrategy extends AbstractWorklistStrategy {

	ModelloLoaded callback = new ModelloLoaded() {

		@Override
		public <T extends BaseTemplateDTO> void onPraticaLoaded(T template) {
			aggiornaRiga(template);

		}

		@Override
		public void onPraticaError(String error) {

		}
	};

	private TextColumn<PraticaDTO> nomeColumn, descrizioneColumn;

	@Override
	public void configuraGrid(final DataGridWidget<PraticaDTO> dataGrid, SimplePager pager) {
		super.configuraGrid(dataGrid, pager);
		SingleSelectionModel<PraticaDTO> selectionModel = new SingleSelectionModel<PraticaDTO>();
		dataGrid.setSelectionModel(selectionModel);
	}

	@Override
	protected void configuraColonne() {

		configuraColonnaSelezione();
		configuraColonnaNome();
		configuraColonnaDescrizione();
		configuraColonnaData();

		String[] stiliColonne = { "check", "nome", "descrizione", "data" };
		for (int i = 0; i < stiliColonne.length; i++) {
			getGrid().getHeader(i).setHeaderStyleNames(stiliColonne[i]);
		}

	}

	@Override
	protected Column<PraticaDTO, Boolean> configuraColonnaSelezione() {

		selezColumn = new Column<PraticaDTO, Boolean>(new RadioButtonCell(true, false)) {
			@Override
			public Boolean getValue(PraticaDTO object) {
				// return true if the object is selected
				boolean res = getGrid().getSelectionModel().isSelected(object);

				notifyCheckRigaListeners(object.getClientID(), res);
				return res;
			}
		};

		selezColumn.setCellStyleNames("check");

		selezColumn.setFieldUpdater(new FieldUpdater<PraticaDTO, Boolean>() {

			@Override
			public void update(int index, PraticaDTO object, Boolean value) {
				getGrid().getSelectionModel().setSelected(object, true);
			}
		});
		getGrid().addColumn(selezColumn, getIntestazioneColonnaSelezione());
		return selezColumn;

	}

	@Override
	public void aggiornaRigheEspanse() {
		List<PraticaDTO> espanse = getGrid().getElencoRigheEspanse();

		for (PraticaDTO pratica : espanse) {
			getPraticheDB().getModelloByPath(pratica.getClientID(), getSitemapMenu().containsLink(pratica.getClientID()), callback);
		}
	}

	@Override
	public void aggiornaRigheSelezionate() {
		for (String idRiga : getIdRigheSelezionate()) {
			getPraticheDB().getModelloByPath(idRiga, getSitemapMenu().containsLink(idRiga), callback);
		}
	}

	protected TextColumn<PraticaDTO> configuraColonnaNome() {
		nomeColumn = new TextColumn<PraticaDTO>() {
			@Override
			public String getValue(PraticaDTO object) {
				return getNome((TemplateDTO) object);
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
				return getDescrizione((TemplateDTO) object);
			}
		};
		descrizioneColumn.setCellStyleNames(ColonnaWorklist.DESCRIZIONE.getId());
		descrizioneColumn.setDataStoreName(ColonnaWorklist.DESCRIZIONE.getId());
		descrizioneColumn.setSortable(false);
		getGrid().addColumn(descrizioneColumn, getIntestazioneColonnaDescrizione());
		return descrizioneColumn;
	}

	protected String getNome(TemplateDTO object) {
		return object.getNome();
	}

	protected String getIntestazioneColonnaNome() {
		return "Nome";
	}

	protected String getDescrizione(TemplateDTO object) {
		return object.getDescrizione();
	}

	protected String getIntestazioneColonnaDescrizione() {
		return "Descrizione";
	}

}
