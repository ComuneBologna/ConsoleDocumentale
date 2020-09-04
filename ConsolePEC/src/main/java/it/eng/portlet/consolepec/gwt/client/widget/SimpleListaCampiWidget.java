package it.eng.portlet.consolepec.gwt.client.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

import it.eng.portlet.consolepec.gwt.client.widget.ListaCampiWidget.CampoLista;
import it.eng.portlet.consolepec.gwt.client.widget.ListaCampiWidget.ColonnaLista;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo.TipoWidget;

public abstract class SimpleListaCampiWidget<T> extends Composite {

	private static SimpleListaCampiWidgetUiBinder uiBinder = GWT.create(SimpleListaCampiWidgetUiBinder.class);

	interface SimpleListaCampiWidgetUiBinder extends UiBinder<Widget, SimpleListaCampiWidget<?>> {}

	@UiField
	HTMLPanel itemsPanel;

	private DataGridWidget<Object[]> grid;
	private int numColonne = 0;
	private ListDataProvider<Object[]> data = new ListDataProvider<Object[]>();
	private List<CampoLista> colonneCampiLista = new ArrayList<CampoLista>();
	private List<T> valori = new ArrayList<T>();
	private List<Column<Object[], ?>> colonneBottoni = new ArrayList<>();

	public SimpleListaCampiWidget(Integer limit) {
		super();
		initWidget(uiBinder.createAndBindUi(this));
		grid = new DataGridWidget<Object[]>(limit != null ? limit : 100, null, null);
		grid.setStyleName("tabella-modulo");
		grid.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);

		data.addDataDisplay(grid);
		itemsPanel.add(grid);
	}

	public void render() {
		colonneCampiLista.clear();

		int gsize = grid.getColumnCount();
		for (int i = 0; i < gsize; i++) {
			grid.removeColumn(0);
		}

		data.getList().clear();
		numColonne = 0;
		grid.setVisible(true);

		configuraColonneCampi();

		for (CampoLista c : colonneCampiLista) {
			aggiungiColonna(c);
		}

		configuraColonneBottoni();

		for (Column<Object[], ?> colonna : colonneBottoni) {
			grid.addColumn(colonna);
		}
	}

	protected abstract void configuraColonneCampi();

	protected abstract void configuraColonneBottoni();

	protected abstract T converti(Object[] riga);

	protected abstract Object[] converti(T riga);

	private void aggiungiColonna(CampoLista c) {
		Campo campo = build(c);
		grid.addColumn(new ColonnaLista(numColonne++), campo.getDescrizione());
	}

	protected CampoLista creaCampoColonna(String nome, String descrizione, TipoWidget tipoWidget, int posizione) {
		CampoLista cl = new CampoLista(nome, descrizione, tipoWidget, posizione);
		colonneCampiLista.add(cl);
		return cl;
	}

	protected void creaColonnaBottone(Column<Object[], ?> e) {
		colonneBottoni.add(e);
	}

	public List<T> getValori() {
		return valori;
	}

	public void add(T obj) {
		Object[] riga = converti(obj);

		if (riga != null) {
			data.getList().add(riga);
			valori.add(obj);
		}
	}

	public void clearValori() {
		data.getList().clear();
		valori.clear();
	}

	public void remove(T obj) {

		int index = valori.indexOf(obj);
		data.getList().remove(index);
		valori.remove(index);
	}

	public void setTabellaVisible(boolean b) {
		grid.setVisible(b);
	}

	private Campo build(CampoLista campoLista) {
		Campo campo = new Campo(campoLista.getNome(), campoLista.getValore(), campoLista.isVisibile(), campoLista.isModificabile(), campoLista.isObbligatorio(), null, null, campoLista.getLunghezza(),
				campoLista.getTipoWidget().name(), campoLista.getDescrizione(), campoLista.getPosizione(), campoLista.getMaxOccurs());
		if (campoLista.getValoriListBox() != null)
			campo.getValoriListbox().addAll(Arrays.asList(campoLista.getValoriListBox()));
		if (campoLista.getSelectedItem() != null)
			campo.setSelectedItem(campoLista.getSelectedItem());
		return campo;
	}
}
