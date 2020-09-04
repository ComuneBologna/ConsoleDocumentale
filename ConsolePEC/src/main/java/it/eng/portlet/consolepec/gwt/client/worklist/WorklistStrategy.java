package it.eng.portlet.consolepec.gwt.client.worklist;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget.ProvidesElementoEvidenziatoInfo;
import it.eng.portlet.consolepec.gwt.shared.model.ColonnaWorklist;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.view.client.ProvidesKey;
import com.google.web.bindery.event.shared.EventBus;

public interface WorklistStrategy {

	public static final ProvidesKey<PraticaDTO> PRATICA_KEY_PROVIDER = new PraticaKeyProvider();
	public static final ProvidesElementoEvidenziatoInfo<PraticaDTO> PRATICA_EVID_PROVIDER = new ElementoEvidenziatoInfoProvider();

	/* metodi */
	/**
	 * Il metodo deve essere invocato dalla View che utilizza il componente tabella, una sola volta all'init
	 *
	 * @param dataGrid
	 * @param pagerZ
	 */
	public void configuraGrid(DataGridWidget<PraticaDTO> dataGrid, SimplePager pager);

	public void espandiRiga(String clientID, PraticaDTO pratica);

	public void aggiornaRiga(PraticaDTO pratica);

	public void aggiornaRigheEspanse();

	public void aggiornaRigheSelezionate();

	public void setRigaEspansaStrategy(RigaEspansaStrategy rigaEspansaStrategy);

	public void addEspandiRigaEventListener(EspandiRigaEventListener listener);

	/**
	 * Metodo per registrare un listener all'evento di selezione di una riga
	 *
	 * @param listener
	 */
	public void addCheckRigaEventListener(CheckRigaEventListener listener);

	public void setRicercaEventListener(RicercaEventListener listener);

	public Set<String> getIdRigheSelezionate();

	/**
	 * Riavvia una ricerca dei dati
	 */
	public void restartSearchDatiGrid();

	/**
	 * Aggiorna la ricerca corrente
	 */
	public void refreshDatiGrid();

	public void resetSelezioni();

	/**
	 * Svuota la griglia, reimpostando gli ordinamenti e il paginatore a zero.
	 */
	public void resetGrid();

	public void setPraticheDB(PecInPraticheDB praticheDB);

	public void setSitemapMenu(SitemapMenu sitemapMenu);

	public void setEventBus(EventBus eventBus);

	/* Classi interne */
	/**
	 * KeyProvider ad uso della DataGrid
	 *
	 * @author pluttero
	 *
	 */
	class PraticaKeyProvider implements ProvidesKey<PraticaDTO> {

		@Override
		public Object getKey(PraticaDTO item) {
			return item.getClientID();
		}

	}

	class ElementoEvidenziatoInfoProvider implements ProvidesElementoEvidenziatoInfo<PraticaDTO> {

		@Override
		public boolean isEvidenziato(PraticaDTO item) {
			return !item.isLetto();
		}

	}

	/* Interfacce */
	/**
	 * Evento che notifica le richieste di espansione riga
	 *
	 * @author pluttero
	 *
	 */
	public interface EspandiRigaEventListener {
		public void onEspandiRiga(String clientID, TipologiaPratica tipologiaPratica, boolean isEspansa);
	}

	/**
	 * Evento che notifica la (de)selezione di una riga
	 *
	 * @author pluttero
	 *
	 */
	public interface CheckRigaEventListener {
		public void onCheckRiga(String clientID, boolean checked);
	}

	public interface RicercaCallback {
		public void setRisultati(List<PraticaDTO> list, int count, boolean estimate);

		public void setRisultati(List<PraticaDTO> list);

		public void setCount(int count, boolean estimate);

		public void setNoResult();
	}

	public interface RicercaEventListener {
		public void onStartRicerca(int start, int length, ColonnaWorklist campoOrdinamento, boolean asc, RicercaCallback callback);
	}

}
