package it.eng.portlet.consolepec.gwt.client.widget.datagrid;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.view.client.ProvidesKey;

public class DataGridWidget<T> extends CellTable<T> {
	static DataGridResources res = GWT.create(DataGridResources.class);
	private DataGridHeaderBuilder<T> headerBuilder;
	private DataGridTableBuilder<T> tableBuilder;
	private ProvidesElementoEvidenziatoInfo<T> evidenziatoInfoProvider;

	public DataGridWidget() {
		this(10, null, null);
	}

	public DataGridWidget(int pageSize, ProvidesKey<T> keyProvider, ProvidesElementoEvidenziatoInfo<T> evidenziatoInfo) {
		super(pageSize, res, keyProvider);
		headerBuilder = new DataGridHeaderBuilder<T>(this);
		tableBuilder = new DataGridTableBuilder<T>(this);
		evidenziatoInfoProvider = evidenziatoInfo;
		this.setTableBuilder(tableBuilder);
		this.setHeaderBuilder(headerBuilder);

	}

	public void espandiRiga(T element, HTMLPanel content, HTMLPanel operations) {
		int id = getVisibleItems().indexOf(element);
		tableBuilder.espandiRiga(id, content, operations);
		tableBuilder.updateEvidenziato(element, id);
	}

	public void chiudiRiga(T element) {
		int id = getVisibleItems().indexOf(element);
		tableBuilder.chiudiRiga(id);
		tableBuilder.updateEvidenziato(element, id);
	}

	@Override
	public void setRowData(int start, List<? extends T> values) {
		super.setRowData(start, values);
		if (evidenziatoInfoProvider != null) {
			for (int i = 0; i < values.size(); i++) {
				tableBuilder.updateEvidenziato(values.get(i), i);
			}
		}
	}

	public void updateRowData(int start, List<? extends T> values) {
		super.setRowData(start + getPageStart(), values);
		if (evidenziatoInfoProvider != null) {
			for (int i = 0; i < values.size(); i++) {
				tableBuilder.updateEvidenziato(values.get(i), start);
			}
		}
	}

	/**
	 * Ritorna un elenco non modificabile, degli oggetti {@link T} che hanno la
	 * riga correntemente espansa
	 * 
	 * @return
	 */
	public List<T> getElencoRigheEspanse() {
		List<T> list = new ArrayList<T>();
		List<T> items = getVisibleItems();
		for (int i = 0; i < items.size(); i++) {
			T item = items.get(i);
			if (tableBuilder.isRigaEspansa(i))
				list.add(item);
		}
		// Collections.unmodifiableList(list) dà errori in ser
		List<T> res = new ArrayList<T>(list);
		return res;
	}

	/**
	 * Richiede che la {@link DataGridWidget} sia stata creata con un {@link ProvidesKey}
	 * 
	 * @param id
	 */
	public void chiudiRigaByKey(Object id) {
		Integer index = getItemRelativeIndexByKey(id);
		tableBuilder.chiudiRiga(index);
	}

	public boolean isRigaEspansaByKey(Object id) {
		Integer index = getItemRelativeIndexByKey(id);
		return tableBuilder.isRigaEspansa(index);
	}

	public ProvidesElementoEvidenziatoInfo<T> getEvidenziatoInfoProvider() {
		return evidenziatoInfoProvider;
	}

	public DataGridResources getDataGridResources() {
		return res;
	}

	/* metodi interni */
	private Integer getItemRelativeIndexByKey(Object id) {
		List<T> items = getVisibleItems();
		for (int i = 0; i < items.size(); i++) {
			T item = items.get(i);
			if (getKeyProvider().getKey(item).equals(id)) {
				return i;
			}
		}
		return null;
	}

	/* interfacce */
	public interface DataGridResources extends Resources {
		@Override
		@Source("it/eng/portlet/consolepec/gwt/client/widget/datagrid/dataGridWidget.css")
		Style cellTableStyle();

		@Override
		@Source("table-asc-11x7.png")
		@ImageOptions(flipRtl = true)
		ImageResource cellTableSortAscending();

		@Override
		@Source("table-asc-11x7.png")
		@ImageOptions(flipRtl = true)
		ImageResource cellTableSortDescending();
	}

	public interface Style extends com.google.gwt.user.cellview.client.CellTable.Style {
		String itemtd();

		String item();

		String itemtr();

		String itemred();

		String itemnew();

		String details();

		String operations();

		String row();
	}

	public interface ProvidesElementoEvidenziatoInfo<T> {
		public boolean isEvidenziato(T item);
	}

}
