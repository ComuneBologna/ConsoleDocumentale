package it.eng.portlet.consolepec.gwt.client.worklist;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.widget.RadioButtonCell;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import java.util.List;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.SingleSelectionModel;

public class SceltaFascicoloWorklistStrategy extends AbstractWorklistStrategy {

	PraticaFascicoloLoaded praticaFascicoloLoaded = new PraticaFascicoloLoaded() {

		@Override
		public void onPraticaLoaded(FascicoloDTO pec) {
			aggiornaRiga(pec);
		}

		@Override
		public void onPraticaError(String error) {
			// TODO Auto-generated method stub

		}

	};

	@Override
	public void configuraGrid(final DataGridWidget<PraticaDTO> dataGrid, SimplePager pager) {
		super.configuraGrid(dataGrid, pager);
		SingleSelectionModel<PraticaDTO> selectionModel = new SingleSelectionModel<PraticaDTO>();
		dataGrid.setSelectionModel(selectionModel);
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
			getPraticheDB().getFascicoloByPath(pratica.getClientID(), getSitemapMenu().containsLink(pratica.getClientID()), praticaFascicoloLoaded);
		}
	}

	@Override
	public void aggiornaRigheSelezionate() {
		for (String idRiga : getIdRigheSelezionate()) {
			getPraticheDB().getFascicoloByPath(idRiga, getSitemapMenu().containsLink(idRiga), praticaFascicoloLoaded);
		}
	}

}
